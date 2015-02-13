package idas.chox.web.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import idas.chox.data.services.BaseDataService;

/**
 *
 * @author John
 */
public class StoredProcSchedulerJob implements Scheduler, ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(StoredProcSchedulerJob.class);
    private String storedProcName;
    private SessionFactory sessionFactory;
    private BaseDataService baseDataService;
    private ApplicationContext applicationContext;
    private Session session;
    private Transaction hibernateTransaction;
    
    @Override
    public void execute() throws JobExecutionException {
        LOG.info("calling stored proc '{}' with '{}'", storedProcName, baseDataService);
        try {
            handleHibernateTransactionIntricacies();

            if (null != storedProcName) switch (storedProcName) {
                case "addInvoicePenaltyTask":
                    baseDataService.callAddInvoicePenaltyTask(999);
                    break;
                case "addMissingEcdTask":
                    baseDataService.callAddMissingEcdTask(999);
                    break;
                case "applyAutoPenaltyCharge":
                    baseDataService.callApplyAutoPenaltyCharge(999, -1);
                    break;
                case "updateDashboard":
                    baseDataService.callUpdateDashboard(999);
                    break;
                case "updateWorkflowTables":
                    baseDataService.callUpdateWorkflowTables(999);
                    break;
            }
            LOG.info("stored proc '{}' job finished.", storedProcName);
        } catch (Exception ex) {
            LOG.error("Exception thrown calling stored proc '{}' in scheduler job:", storedProcName, ex);
        } finally {
            releaseHibernateSessionConditionally();
        }
    }

    public String getStoredProcName() {
        return storedProcName;
    }

    public void setStoredProcName(String storedProcName) {
        this.storedProcName = storedProcName;
    }

    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    public void handleHibernateTransactionIntricacies() {
        session = SessionFactoryUtils.getSession(sessionFactory, true);
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            hibernateTransaction = session.beginTransaction();
        }
    }

    public void releaseHibernateSessionConditionally() {
        if (hibernateTransaction!=null && !hibernateTransaction.wasCommitted()) {
            hibernateTransaction.commit();
            LOG.debug("Transaction committed.");
        }
        TransactionSynchronizationManager.unbindResource(sessionFactory);
        session.clear();
        SessionFactoryUtils.closeSession(session);
        SessionFactoryUtils.releaseSession(session, sessionFactory);
    }
    
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.applicationContext = ac;
    }

}
