package idas.chox.web.scheduler;

import idas.chox.keoghs.Keoghs;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 *
 * @author john
 */
public class KeoghsCheckJob implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(KeoghsCheckJob.class);
    private Keoghs keoghs;
//    private SecurityInfoProvider securityInfoProvider;
    private Session session;
    private SessionFactory sessionFactory;
    private Transaction hibernateTransaction;
    private AuthenticationManager authenticationManager;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setKeoghs(Keoghs keoghs) {
        this.keoghs = keoghs;
    }
    
    @Override
    public void run() {
        // Do your daily job here.
        LOG.info("KeoghsCheckJob running with keoghs={}...", keoghs);
        handleHibernateTransactionIntricacies();
        authenticateSender("SYSTEM", "C0mpliance");
        
        try {
            if (keoghs != null) {
                LOG.info("Checking status of submitted requests...");
                keoghs.checkDebug();
                // Start new transaction?
//                hibernateTransaction.commit(); hibernateTransaction = session.beginTransaction();
                LOG.info("Submitting new requests");
                keoghs.submit(true);
            } else {
                LOG.error("No Keoghs!!!");
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown checking Keoghs jobs: {}", ex.getMessage(), ex);
        } finally {
            releaseHibernateSessionConditionally();
        }
    }
    public void handleHibernateTransactionIntricacies() {
        session = SessionFactoryUtils.getSession(sessionFactory, true);
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            try {
                hibernateTransaction = session.beginTransaction();
                LOG.debug("Hibernate Transaction started: {}", hibernateTransaction);
            } catch (HibernateException ex) {
                LOG.error("Exception thrown starting hibernate transaction: {}\n", ex.getMessage(), ex);
            }
        } else {
            LOG.debug("Transaction already active: {}", TransactionSynchronizationManager.getCurrentTransactionName());
        }
    }

    public void releaseHibernateSessionConditionally() {
        if (hibernateTransaction!=null && !hibernateTransaction.wasCommitted() && hibernateTransaction.isActive()) {
            hibernateTransaction.commit();
            LOG.debug("Hibernate Transaction committed: {}", hibernateTransaction);
        } else {
            LOG.debug("Hibernate Transaction not committed: {}", hibernateTransaction);
            if (hibernateTransaction != null) {
                LOG.debug("Hibernate Transaction wasCommitted={}, wasRolledBack={}", hibernateTransaction.wasCommitted(), hibernateTransaction.wasRolledBack());
            }
        }
        TransactionSynchronizationManager.unbindResource(sessionFactory);
        session.clear();
        SessionFactoryUtils.closeSession(session);
        SessionFactoryUtils.releaseSession(session, sessionFactory);
    }

    private void authenticateSender(String userName, String password) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(userName, password);
            authentication = authenticationManager.authenticate(authentication);
            if (!authentication.isAuthenticated()) {
                LOG.error("User '{}' with password '{}' is not authenticated. ", userName, password);
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (SecurityException se) {
            LOG.error("Exception authenticating sender '{}': ", userName, se);
            throw se;
        }
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
