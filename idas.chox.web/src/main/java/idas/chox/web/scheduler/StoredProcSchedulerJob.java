package idas.chox.web.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
    
    @Override
    public void execute() throws JobExecutionException {
        LOG.info("calling stored proc '{}' with '{}'", storedProcName, baseDataService);
        try {
            handleHibernateTransactionIntricacies();
//            ((SecureDataService)baseDataService).setSecurityInfoProvider(((SecureDataService)baseDataService).getSecurityInfoProvider());

            if ("addInvoicePenaltyTask".equals(storedProcName)) {
                baseDataService.callAddInvoicePenaltyTask(999);
            }
            else if ("addMissingEcdTask".equals(storedProcName)) {
                baseDataService.callAddMissingEcdTask(999);
            }
            else if ("applyAutoPenaltyCharge".equals(storedProcName)) {
                baseDataService.callApplyAutoPenaltyCharge(999, -1);
            }
            else if ("updateDashboard".equals(storedProcName)) {
                baseDataService.callUpdateDashboard(999);
            }
            else if ("updateWorkflowTables".equals(storedProcName)) {
                baseDataService.callUpdateWorkflowTables(999);
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
    }

    public void releaseHibernateSessionConditionally() {
        TransactionSynchronizationManager.unbindResource(sessionFactory);
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
