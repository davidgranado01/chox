package idas.chox.web.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import idas.chox.data.services.BaseDataService;
import org.hibernate.resource.transaction.spi.TransactionStatus;

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
    private boolean hashData;
    private int hashDataPeriod;
    private boolean closeClaims;
    private int closeClaimsPeriod;
    private boolean removeNotes;
    private int removeNotesPeriod;
    private boolean removeAttachments;
    private int removeAttachmentsPeriod;
    private boolean removeTasks;
    private int removeTasksPeriod;
    private boolean deactivateUsers;
    private int deactivateUsersPeriod;
    private boolean hashUserData;
    private int hashUserDataPeriodFromDeactivate;
    private int hashUserDataPeriodFromLastLogin;
    private boolean hashVrns;
    private int hashVrnsPeriodFromCreation;
    private int hashVrnsPeriodFromClosure;

    public void setHashData(boolean hashData) {
        this.hashData = hashData;
    }

    public void setHashDataPeriod(int hashDataPeriod) {
        this.hashDataPeriod = hashDataPeriod;
    }

    public void setCloseClaims(boolean closeClaims) {
        this.closeClaims = closeClaims;
    }

    public void setCloseClaimsPeriod(int closeClaimsPeriod) {
        this.closeClaimsPeriod = closeClaimsPeriod;
    }

    public void setRemoveNotes(boolean removeNotes) {
        this.removeNotes = removeNotes;
    }

    public void setRemoveNotesPeriod(int removeNotesPeriod) {
        this.removeNotesPeriod = removeNotesPeriod;
    }

    public void setRemoveAttachments(boolean removeAttachments) {
        this.removeAttachments = removeAttachments;
    }

    public void setRemoveAttachmentsPeriod(int removeAttachmentsPeriod) {
        this.removeAttachmentsPeriod = removeAttachmentsPeriod;
    }

    public void setHashVrns(boolean hashVrns) {
        this.hashVrns = hashVrns;
    }

    public void setHashVrnsPeriodFromCreation(int hashVrnsPeriodFromCreation) {
        this.hashVrnsPeriodFromCreation = hashVrnsPeriodFromCreation;
    }

    public void setHashVrnsPeriodFromClosure(int hashVrnsPeriodFromClosure) {
        this.hashVrnsPeriodFromClosure = hashVrnsPeriodFromClosure;
    }

    public void setRemoveTasks(boolean removeTasks) {
        this.removeTasks = removeTasks;
    }

    public void setRemoveTasksPeriod(int removeTasksPeriod) {
        this.removeTasksPeriod = removeTasksPeriod;
    }

    public void setDeactivateUsers(boolean deactivateUsers) {
        this.deactivateUsers = deactivateUsers;
    }

    public void setDeactivateUsersPeriod(int deactivateUsersPeriod) {
        this.deactivateUsersPeriod = deactivateUsersPeriod;
    }

    public void setHashUserData(boolean hashUserData) {
        this.hashUserData = hashUserData;
    }

    public void setHashUserDataPeriodFromDeactivate(int hashUserDataPeriodFromDeactivate) {
        this.hashUserDataPeriodFromDeactivate = hashUserDataPeriodFromDeactivate;
    }

    public void setHashUserDataPeriodFromLastLogin(int hashUserDataPeriodFromLastLogin) {
        this.hashUserDataPeriodFromLastLogin = hashUserDataPeriodFromLastLogin;
    }
      

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
                    baseDataService.callUpdateWorkflowTables();
                    break;
                case "updateRemainingSlaDays":
                    baseDataService.callUpdateRemainingSlaDays(999);
                    break;
                case "hashClaims":
                    if (hashData) {
                        baseDataService.callHashClaims(hashDataPeriod);
                    } else {
                        LOG.info("Hashing of claim data has been deactivated.");
                    }
                    break;
                case "closeOldClaims":
                    if (closeClaims) {
                        baseDataService.callCloseOldClaims(closeClaimsPeriod);
                    } else {
                        LOG.info("Closing of old claims has been deactivated.");
                    }
                    break;
                case "removeNotes":
                    if (removeNotes) {
                        baseDataService.callRemoveNotes(hashDataPeriod+removeNotesPeriod);
                    } else {
                        LOG.info("Note removal has been deactivated.");
                    }
                    break;
                case "removeTasks":
                    if (removeTasks) {
                        baseDataService.callRemoveTasks(hashDataPeriod+removeTasksPeriod);
                    } else {
                        LOG.info("Task removal has been deactivated.");
                    }
                    break;
                case "removeAttachments":
                    if (removeAttachments) {
                        baseDataService.callRemoveAttachments(hashDataPeriod+removeAttachmentsPeriod);
                    } else {
                        LOG.info("Attachment removal has been deactivated.");
                    }
                    break;
                case "deactivateUsers":
                    if (deactivateUsers) {
                        baseDataService.callDeactivateUsers(deactivateUsersPeriod);
                    } else {
                        LOG.info("Deactivation of users has been deactivated.");
                    }
                    break;
                case "hashUsers":
                    if (hashUserData) {
                        baseDataService.callHashUsers(hashUserDataPeriodFromDeactivate, hashUserDataPeriodFromLastLogin);
                    } else {
                        LOG.info("Deactivation of users has been deactivated.");
                    }
                    break;
                case "hashVrns":
                    if (hashVrns) {
                        baseDataService.callHashVrns(hashVrnsPeriodFromCreation, hashVrnsPeriodFromClosure);
                    } else {
                        LOG.info("Hashin of VRNs has been deactivated.");
                    }
                    break;
                default:
                    LOG.error("'{}' stired procedure doesn't exist", storedProcName);
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
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException ex) {
            LOG.debug("Exception thrown getting current session: {}", ex.getMessage());
            session = sessionFactory.openSession();
        }
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
        if (hibernateTransaction!=null && hibernateTransaction.getStatus() == TransactionStatus.ACTIVE) {
            hibernateTransaction.commit();
            LOG.debug("Transaction committed.");
        } else if (hibernateTransaction != null) {
            LOG.debug("Hibernate Transaction getStatus={}", hibernateTransaction.getStatus());
        } else {
            LOG.debug("Hibernate Transaction is null");
        }
        if (session != null) {
            TransactionSynchronizationManager.unbindResource(sessionFactory);
            session.clear();
            session.close();
            session = null;
        }
    }
    
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.applicationContext = ac;
    }

}
