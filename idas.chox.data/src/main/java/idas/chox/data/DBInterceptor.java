package idas.chox.data;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import idas.chox.core.model.Invoice;
import idas.chox.core.model.InvoiceOriginal;
import idas.chox.core.model.Auditable;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.FullAudit;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.Claim;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.FullAuditService;
import idas.chox.core.services.InvoiceService;
import idas.chox.core.services.NotificationService;
import idas.chox.core.util.DateHelper;
import idas.chox.data.notifications.EcdUpdatedNotification;
import idas.chox.data.notifications.HireUpdatedNotification;
import idas.chox.data.notifications.LiabilityStatusUpdatedNotification;

public class DBInterceptor extends EmptyInterceptor implements BeanFactoryAware {

    private static final Logger LOG = LoggerFactory.getLogger(DBInterceptor.class);
    private SecurityInfoProvider securityInfoProvider;
    private FullAuditService fullAuditService;
    private NotificationService notificationService;
    private BeanFactory bf;

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof Invoice) {
            //delete invoice_original when the invoice is deleted from the claim
            Invoice invoice = (Invoice) entity;
            InvoiceService invoiceService = (InvoiceService) bf.getBean("invoiceService");
            invoiceService.deleteOriginalInvoice(invoice);
        }
    }

    @Override
    public boolean onSave(Object entity,
            Serializable id,
            Object[] state,
            String[] propertyNames,
            Type[] types) {
        if (entity instanceof Auditable) {
            LOG.debug("In onSave for Auditable entity...");
            for (int i = 0; i < propertyNames.length; i++) {
                if ("createdDate".equals(propertyNames[i])) {
                    state[i] = DateHelper.getCurrentDateTime();
                } else if ("createdBy".equals(propertyNames[i])) {
                    state[i] = this.getSecurityInfoProvider().getCurrentUser();
                } else if ("lastModifiedDate".equals(propertyNames[i])) {
                    state[i] = DateHelper.getCurrentDateTime();
                } else if ("lastModifiedBy".equals(propertyNames[i])) {
                    state[i] = this.getSecurityInfoProvider().getCurrentUser();
                }
            }
        }

        if (entity instanceof Invoice) {
            Invoice invoice = (Invoice) entity;
            InvoiceService invoiceService = (InvoiceService) bf.getBean("invoiceService");
            InvoiceOriginal invoiceOriginal = invoiceService.saveOriginalInvoice(invoice);
            for (int i = 0; i < propertyNames.length; i++) {
                if ("invoiceOriginal".equals(propertyNames[i])) {

                    state[i] = invoiceOriginal;
                    break;
                }
            }
        } else if (entity instanceof HireMonitoringDetail) {

            Integer indexOfInspectionBookedDate = null;
            Integer indexOfInspectionBookedDateLastModified = null;
            Integer indexOfInspectionDate = null;
            Integer indexOfInspectionDateLastModified = null;
            Integer indexOfRepairAuthorisedDate = null;
            Integer indexOfRepairAuthorisedDateLastModified = null;
            Integer indexOfRepairBookInDate = null;
            Integer indexOfRepairBookInDateLastModified = null;
            Integer indexOfRepairCommencedDate = null;
            Integer indexOfRepairCommencedDateLastModified = null;
            Integer indexOfIsTotalLostCheck = null;
            Integer indexOfIsTotalLostCheckLastModified = null;
            Integer indexOfRepairCompletionDate = null;
            Integer indexOfRepairCompletionDateLastModified = null;
            Integer indexOfTotalLossOfferMadeDate = null;
            Integer indexOfTotalLossOfferMadeLastModified = null;
            Integer indexOfTotalLossOfferAcceptedDate = null;
            Integer indexOfTotalLossOfferAcceptedLastModified = null;
            Integer indexOfTotalLossOfferCheckIssuedDate = null;
            Integer indexOfTotalLossCheckIssuedLastModified = null;
            Integer indexOfTotalLossOfferCheckReceivedDate = null;
            Integer indexOfTotalLossCheckReceivedLastModified = null;
            Integer indexOfIsRepairOnlyCheck = null;
            Integer indexOfIsRepairOnlyCheckLastModified = null;
            Integer indexOfIsNFInsurerManagingRepair = null;
            Integer indexOfIsNFInsurerManagingRepairLastModified = null;
            Integer indexOfClientVatRegistered = null;
            Integer indexOfClientVatRegisteredLastModified = null;

            for (int i = 0; i < propertyNames.length; i++) {


                if ("inspectionBookedDateLastModified".equals(propertyNames[i])) {

                    indexOfInspectionBookedDateLastModified = i;

                    if (indexOfInspectionBookedDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("inspectionDateLastModified".equals(propertyNames[i])) {

                    indexOfInspectionDateLastModified = i;
                    if (indexOfInspectionDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("repairAuthorisedDateLastModified".equals(propertyNames[i])) {

                    indexOfRepairAuthorisedDateLastModified = i;
                    if (indexOfRepairAuthorisedDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("repairBookInDateLastModified".equals(propertyNames[i])) {

                    indexOfRepairBookInDateLastModified = i;
                    if (indexOfRepairBookInDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("repairCommencedDateLastModified".equals(propertyNames[i])) {

                    indexOfRepairCommencedDateLastModified = i;
                    if (indexOfRepairCommencedDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("isTotalLostCheckLastModified".equals(propertyNames[i])) {

                    indexOfIsTotalLostCheckLastModified = i;
                    if (indexOfIsTotalLostCheck != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("totalLossOfferMadeLastModified".equals(propertyNames[i])) {
                    indexOfTotalLossOfferMadeLastModified = i;
                    if (indexOfTotalLossOfferMadeDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("totalLossOfferAcceptedLastModified".equals(propertyNames[i])) {

                    indexOfTotalLossOfferAcceptedLastModified = i;
                    if (indexOfTotalLossOfferAcceptedDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("totalLossCheckIssuedLastModified".equals(propertyNames[i])) {
                    indexOfTotalLossCheckIssuedLastModified = i;
                    if (indexOfTotalLossOfferCheckIssuedDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("totalLossCheckReceivedLastModified".equals(propertyNames[i])) {
                    indexOfTotalLossCheckReceivedLastModified = i;
                    if (indexOfTotalLossOfferCheckReceivedDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("repairCompletionDateLastModified".equals(propertyNames[i])) {

                    indexOfRepairCompletionDateLastModified = i;
                    if (indexOfRepairCompletionDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("isRepairOnlyCheckLastModified".equals(propertyNames[i])) {

                    indexOfIsRepairOnlyCheckLastModified = i;
                    if (indexOfIsRepairOnlyCheck != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("isNFInsurerManagingRepairLastModified".equals(propertyNames[i])) {

                    indexOfIsNFInsurerManagingRepairLastModified = i;
                    if (indexOfIsNFInsurerManagingRepair != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("clientVatRegisteredLastModified".equals(propertyNames[i])) {

                    indexOfClientVatRegisteredLastModified = i;
                    if (indexOfClientVatRegistered != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("repairBookInDate".equals(propertyNames[i])) {
                    Date status = (Date) state[i];
                    if (status != null) {
                        indexOfRepairBookInDate = i;

                        if (indexOfRepairBookInDateLastModified != null) {
                            state[indexOfRepairBookInDateLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("repairAuthorisedDate".equals(propertyNames[i])) {
                    Date status = (Date) state[i];
                    if (status != null) {
                        indexOfRepairAuthorisedDate = i;

                        if (indexOfRepairAuthorisedDateLastModified != null) {
                            state[indexOfRepairAuthorisedDateLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("repairCommencedDate".equals(propertyNames[i])) {
                    Date status = (Date) state[i];
                    if (status != null) {
                        indexOfRepairCommencedDate = i;

                        if (indexOfRepairCommencedDateLastModified != null) {
                            state[indexOfRepairCommencedDateLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("isTotalLostCheck".equals(propertyNames[i])) {
                    String status = state[i].toString();
                    if (status != null && status.equals("true")) {
                        indexOfIsTotalLostCheck = i;

                        if (indexOfIsTotalLostCheckLastModified != null) {
                            state[indexOfIsTotalLostCheckLastModified] = DateHelper.getCurrentDateTime();
                        }

                    }
                } else if ("totalLossOfferMadeDate".equals(propertyNames[i])) {
                    Date status = (Date) state[i];
                    if (status != null) {
                        indexOfTotalLossOfferMadeDate = i;
                        if (indexOfTotalLossOfferMadeLastModified != null) {
                            state[indexOfTotalLossOfferMadeLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("totalLossOfferAcceptedDate".equals(propertyNames[i])) {
                    Date status = (Date) state[i];
                    if (status != null) {
                        indexOfTotalLossOfferAcceptedDate = i;
                        if (indexOfTotalLossOfferAcceptedLastModified != null) {
                            state[indexOfTotalLossOfferAcceptedLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("totalLossOfferCheckIssuedDate".equals(propertyNames[i])) {
                    Date status = (Date) state[i];
                    if (status != null) {
                        indexOfTotalLossOfferCheckIssuedDate = i;
                        if (indexOfTotalLossCheckIssuedLastModified != null) {
                            state[indexOfTotalLossCheckIssuedLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("totalLossOfferCheckReceivedDate".equals(propertyNames[i])) {
                    Date status = (Date) state[i];
                    if (status != null) {
                        indexOfTotalLossOfferCheckReceivedDate = i;
                        if (indexOfTotalLossCheckReceivedLastModified != null) {
                            state[indexOfTotalLossCheckReceivedLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("inspectionBookedDate".equals(propertyNames[i])) {
                    Date status = (Date) state[i];
                    if (status != null) {
                        indexOfInspectionBookedDate = i;
                        if (indexOfInspectionBookedDateLastModified != null) {
                            state[indexOfInspectionBookedDateLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("inspectionDate".equals(propertyNames[i])) {
                    Date status = (Date) state[i];
                    if (status != null) {
                        indexOfInspectionDate = i;
                        if (indexOfInspectionDateLastModified != null) {
                            state[indexOfInspectionDateLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("repairCompletionDate".equals(propertyNames[i])) {
                    Date status = (Date) state[i];
                    if (status != null) {
                        indexOfRepairCompletionDate = i;
                        if (indexOfRepairCompletionDateLastModified != null) {
                            state[indexOfRepairCompletionDateLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("isRepairOnlyCheck".equals(propertyNames[i])) {
                    String status = state[i].toString();
                    if (status != null && status.equals("true")) {
                        indexOfIsRepairOnlyCheck = i;
                        if (indexOfIsRepairOnlyCheckLastModified != null) {
                            state[indexOfIsRepairOnlyCheckLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("isNFInsurerManagingRepair".equals(propertyNames[i])) {
                    String status = state[i].toString();
                    if (status != null && status.equals("true")) {
                        indexOfIsNFInsurerManagingRepair = i;
                        if (indexOfIsNFInsurerManagingRepairLastModified != null) {
                            state[indexOfIsNFInsurerManagingRepairLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("clientVatRegistered".equals(propertyNames[i])) {
                    String status = null;
                    if (state[i] != null) {
                        status = state[i].toString();
                    }
                    if (status != null && status.equals("true")) {
                        indexOfClientVatRegistered = i;
                        if (indexOfClientVatRegisteredLastModified != null) {
                            state[indexOfClientVatRegisteredLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                }
            }
        } else if (entity instanceof HireMonitoringEcd) {
            HireMonitoringEcd ecd = (HireMonitoringEcd) entity;
            if (ecd.isUpdateInsurer()) {
                LOG.debug("Adding ECD Updated notification to claim '{}' with id={}", ecd.getClaim().getChoReference(), ecd.getClaim().getId());
                getNotificationService().addNotification(ecd.getClaim(), new EcdUpdatedNotification());

            } else {
                LOG.debug("No ECD Updated notification to be added as UpdateInsurer was false: {}", ecd.getClaim());
            }
        }

        return true;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
            Object[] previousState, String[] propertyNames, Type[] types) {
       LOG.debug("In onFlushDirty() for entity class '{}': {}", entity.getClass(), entity);
        if (getSecurityInfoProvider().getCurrentUser() == null) {
            LOG.warn("No 'current' user found in DB Interceptor for entity class '{}'", entity.getClass());
        }

        if (entity instanceof Auditable && getSecurityInfoProvider().getCurrentUser() != null) {
            LOG.debug("   onFlushDirty(): we have an Auditable entity");
            Integer indexOfStatusModifiedDate = null;
            Integer indexForPrevStatus = null;
            Date statusModifiedDate = null;
            String prevStatus = null;

            for (int i = 0; i < propertyNames.length; i++) {
                LOG.debug("Checking auditable property [{}]:{}", i, propertyNames[i]);
                if ("lastModifiedDate".equals(propertyNames[i])) {
                    currentState[i] = DateHelper.getCurrentDateTime();
                } else if ("lastModifiedBy".equals(propertyNames[i])) {
                    currentState[i] = getSecurityInfoProvider().getCurrentUser();
                } else if ("statusModifiedDate".equals(propertyNames[i])) {
                    if (statusModifiedDate == null) {
                        indexOfStatusModifiedDate = i;
                    } else {
                        currentState[i] = statusModifiedDate;
                    }
                } else if ("previousStatus".equals(propertyNames[i])) {
                    if (prevStatus == null) {
                        indexForPrevStatus = i;
                    } else {
                        currentState[i] = prevStatus;
                    }
                } else if ("status".equals(propertyNames[i])) {

                    String newStatus;
                    if (currentState[i] != null) {
                        newStatus = currentState[i].toString();
                    } else {
                        newStatus = "";
                    }

                    String oldStatus;
                    if (previousState[i] != null) {
                        oldStatus = previousState[i].toString();
                    } else {
                        oldStatus = "";
                    }

                    LOG.debug("newStatus={}, oldStatus={}", newStatus, oldStatus);


                    if (!newStatus.equals(oldStatus)) {
                        LOG.debug("Status change from '{}' to '{}': updating statusModifiedDate", oldStatus, newStatus);
                        if (indexOfStatusModifiedDate != null) {
                            currentState[indexOfStatusModifiedDate] = new Date();
                        } else {
                            statusModifiedDate = new Date();
                        }

                        if (indexForPrevStatus != null) {
                            currentState[indexForPrevStatus] = oldStatus;
                        } else {
                            prevStatus = oldStatus;
                        }
                    }
                }
            }
        }

        if (entity instanceof FullAudit && getSecurityInfoProvider().getCurrentUser() != null) {
            LOG.debug("   onFlushDirty(): we have a FullAudit entity");
            for (int i = 0; i < propertyNames.length; i++) {
                // ignore auditable entries
                if (!"lastModifiedDate".equals(propertyNames[i])
                        && !"lastModifiedBy".equals(propertyNames[i])
                        && !"createdBy".equals(propertyNames[i])
                        && !"createdDate".equals(propertyNames[i])) {
                    if ((currentState[i] != null && previousState[i] != null && !currentState[i].equals(previousState[i])
                            || (currentState[i] != null && previousState[i] == null))
                            || (currentState[i] == null && previousState[i] != null)) {
                        String oldValue = null;
                        String newValue = null;
                        if (previousState[i] != null) {
                            oldValue = previousState[i].toString();
                        }
                        if (currentState[i] != null) {
                            newValue = currentState[i].toString();
                        }
                        getFullAuditService().logAuditEntry(entity.getClass().toString(), id,
                                propertyNames[i], oldValue, newValue,
                                getSecurityInfoProvider().getCurrentUser());
                        LOG.debug("Audit entry: table='{}', id={}, parameter='{}', old_value='{}', new_value='{}', by='{}'",
                                new Object[]{entity.getClass().toString(), id,
                                    propertyNames[i], previousState[i], currentState[i],
                                    getSecurityInfoProvider().getCurrentUser().getId()});
                    }
                }
            }
        }

        if (entity instanceof HireMonitoringDetail) {
            HireMonitoringDetail hmd = (HireMonitoringDetail) entity;
            LOG.debug("In onFlushDirty() for HireMonitoringDetail with id={}...", hmd.getId());
            // Hire Monitoring Detail must have changed so we need to add a notification
            if (hmd.isUpdateInsurer()) {
                LOG.debug("Adding Hire Monitoring Updated notification to claim '{}' with id={}", hmd.getClaim().getChoReference(), hmd.getClaim().getId());
                getNotificationService().addNotification(hmd.getClaim(), new HireUpdatedNotification());
            }

            Integer indexOfInspectionBookedDate = null;
            Integer indexOfInspectionBookedDateLastModified = null;
            Integer indexOfInspectionDate = null;
            Integer indexOfInspectionDateLastModified = null;
            Integer indexOfRepairAuthorisedDate = null;
            Integer indexOfRepairAuthorisedDateLastModified = null;
            Integer indexOfRepairBookInDate = null;
            Integer indexOfRepairBookInDateLastModified = null;
            Integer indexOfRepairCommencedDate = null;
            Integer indexOfRepairCommencedDateLastModified = null;
            Integer indexOfIsTotalLostCheck = null;
            Integer indexOfIsTotalLostCheckLastModified = null;
            Integer indexOfRepairCompletionDate = null;
            Integer indexOfRepairCompletionDateLastModified = null;
            Integer indexOfTotalLossOfferMadeDate = null;
            Integer indexOfTotalLossOfferMadeLastModified = null;
            Integer indexOfTotalLossOfferAcceptedDate = null;
            Integer indexOfTotalLossOfferAcceptedLastModified = null;
            Integer indexOfTotalLossOfferCheckIssuedDate = null;
            Integer indexOfTotalLossCheckIssuedLastModified = null;
            Integer indexOfTotalLossOfferCheckReceivedDate = null;
            Integer indexOfTotalLossCheckReceivedLastModified = null;
            Integer indexOfIsRepairOnlyCheck = null;
            Integer indexOfIsRepairOnlyCheckLastModified = null;
            Integer indexOfIsNFInsurerManagingRepair = null;
            Integer indexOfIsNFInsurerManagingRepairLastModified = null;
            Integer indexOfClientVatRegistered = null;
            Integer indexOfClientVatRegisteredLastModified = null;

            for (int i = 0; i < propertyNames.length; i++) {

                if ("totalLossOfferCheckReceivedDate".equals(propertyNames[i])) {
                    Date newStatus = (Date) currentState[i];
                    Date oldStatus = (Date) previousState[i];

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfTotalLossOfferCheckReceivedDate = i;
                        if (indexOfTotalLossCheckReceivedLastModified != null) {
                            currentState[indexOfTotalLossCheckReceivedLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("totalLossCheckReceivedLastModified".equals(propertyNames[i])) {
                    indexOfTotalLossCheckReceivedLastModified = i;
                    if (indexOfTotalLossOfferCheckReceivedDate != null) {
                        currentState[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("totalLossOfferCheckIssuedDate".equals(propertyNames[i])) {

                    Date newStatus = (Date) currentState[i];
                    Date oldStatus = (Date) previousState[i];

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfTotalLossOfferCheckIssuedDate = i;
                        if (indexOfTotalLossCheckIssuedLastModified != null) {
                            currentState[indexOfTotalLossCheckIssuedLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("totalLossCheckIssuedLastModified".equals(propertyNames[i])) {
                    indexOfTotalLossCheckIssuedLastModified = i;

                    if (indexOfTotalLossOfferCheckIssuedDate != null) {
                        currentState[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("totalLossOfferAcceptedDate".equals(propertyNames[i])) {

                    Date newStatus = (Date) currentState[i];
                    Date oldStatus = (Date) previousState[i];

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfTotalLossOfferAcceptedDate = i;
                        if (indexOfTotalLossOfferAcceptedLastModified != null) {
                            currentState[indexOfTotalLossOfferAcceptedLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("totalLossOfferAcceptedLastModified".equals(propertyNames[i])) {
                    indexOfTotalLossOfferAcceptedLastModified = i;

                    if (indexOfTotalLossOfferAcceptedDate != null) {
                        currentState[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("totalLossOfferMadeDate".equals(propertyNames[i])) {
                    Date newStatus = (Date) currentState[i];
                    Date oldStatus = (Date) previousState[i];

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfTotalLossOfferMadeDate = i;
                        if (indexOfTotalLossOfferMadeLastModified != null) {
                            currentState[indexOfTotalLossOfferMadeLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("totalLossOfferMadeLastModified".equals(propertyNames[i])) {
                    indexOfTotalLossOfferMadeLastModified = i;

                    if (indexOfTotalLossOfferMadeDate != null) {
                        currentState[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("repairCompletionDate".equals(propertyNames[i])) {
                    Date newStatus = (Date) currentState[i];
                    Date oldStatus = (Date) previousState[i];

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfRepairCompletionDate = i;
                        if (indexOfRepairCompletionDateLastModified != null) {
                            currentState[indexOfRepairCompletionDateLastModified] = DateHelper.getCurrentDateTime();

                        }
                    }
                } else if ("repairCompletionDateLastModified".equals(propertyNames[i])) {
                    indexOfRepairCompletionDateLastModified = i;

                    if (indexOfRepairCompletionDate != null) {
                        currentState[i] = DateHelper.getCurrentDateTime();

                    }
                } else if ("isTotalLostCheck".equals(propertyNames[i])) {
                    String newStatus = currentState[i].toString();
                    String oldStatus = previousState[i].toString();

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfIsTotalLostCheck = i;
                        if (indexOfIsTotalLostCheckLastModified != null) {
                            currentState[indexOfIsTotalLostCheckLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("isTotalLostCheckLastModified".equals(propertyNames[i])) {
                    indexOfIsTotalLostCheckLastModified = i;

                    if (indexOfIsTotalLostCheck != null) {
                        currentState[i] = DateHelper.getCurrentDateTime();

                    }
                } else if ("repairCommencedDate".equals(propertyNames[i])) {
                    Date newStatus = (Date) currentState[i];
                    Date oldStatus = (Date) previousState[i];

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfRepairCommencedDate = i;
                        if (indexOfRepairCommencedDateLastModified != null) {
                            currentState[indexOfRepairCommencedDateLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("repairCommencedDateLastModified".equals(propertyNames[i])) {
                    indexOfRepairCommencedDateLastModified = i;

                    if (indexOfRepairCommencedDate != null) {
                        currentState[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("repairBookInDate".equals(propertyNames[i])) {

                    Date newStatus = (Date) currentState[i];
                    Date oldStatus = (Date) previousState[i];

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfRepairBookInDate = i;
                        if (indexOfRepairBookInDateLastModified != null) {
                            currentState[indexOfRepairBookInDateLastModified] = DateHelper.getCurrentDateTime();

                        }
                    }
                } else if ("repairBookInDateLastModified".equals(propertyNames[i])) {
                    indexOfRepairBookInDateLastModified = i;

                    if (indexOfRepairBookInDate != null) {
                        currentState[i] = DateHelper.getCurrentDateTime();
                    }

                } else if ("repairAuthorisedDate".equals(propertyNames[i])) {

                    Date newStatus = (Date) currentState[i];
                    Date oldStatus = (Date) previousState[i];

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfRepairAuthorisedDate = i;
                        if (indexOfRepairAuthorisedDateLastModified != null) {
                            currentState[indexOfRepairAuthorisedDateLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("repairAuthorisedDateLastModified".equals(propertyNames[i])) {

                    indexOfRepairAuthorisedDateLastModified = i;

                    if (indexOfRepairAuthorisedDate != null) {
                        currentState[i] = DateHelper.getCurrentDateTime();

                    }

                } else if ("inspectionBookedDate".equals(propertyNames[i])) {
                    Date newStatus = (Date) currentState[i];
                    Date oldStatus = (Date) previousState[i];


                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfInspectionBookedDate = i;

                        if (indexOfInspectionBookedDateLastModified != null) {
                            currentState[indexOfInspectionBookedDateLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }

                } else if ("inspectionBookedDateLastModified".equals(propertyNames[i])) {
                    indexOfInspectionBookedDateLastModified = i;

                    if (indexOfInspectionBookedDate != null) {
                        currentState[i] = DateHelper.getCurrentDateTime();
                    }

                } else if ("inspectionDate".equals(propertyNames[i])) {

                    Date newStatus = (Date) currentState[i];
                    Date oldStatus = (Date) previousState[i];


                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {

                        indexOfInspectionDate = i;
                        if (indexOfInspectionDateLastModified != null) {
                            currentState[indexOfInspectionDateLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("inspectionDateLastModified".equals(propertyNames[i])) {

                    indexOfInspectionDateLastModified = i;

                    if (indexOfInspectionDate != null) {
                        currentState[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("isRepairOnlyCheck".equals(propertyNames[i])) {

                    String newStatus = currentState[i].toString();
                    String oldStatus = previousState[i].toString();

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfIsRepairOnlyCheck = i;
                        if (indexOfIsRepairOnlyCheckLastModified != null) {
                            currentState[indexOfIsRepairOnlyCheckLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("isRepairOnlyCheckLastModified".equals(propertyNames[i])) {

                    indexOfIsRepairOnlyCheckLastModified = i;

                    if (indexOfIsRepairOnlyCheck != null) {
                        currentState[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("isNFInsurerManagingRepair".equals(propertyNames[i])) {

                    String newStatus = currentState[i].toString();
                    String oldStatus = previousState[i].toString();

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfIsNFInsurerManagingRepair = i;
                        if (indexOfIsNFInsurerManagingRepairLastModified != null) {
                            currentState[indexOfIsNFInsurerManagingRepairLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("isNFInsurerManagingRepairLastModified".equals(propertyNames[i])) {

                    indexOfIsNFInsurerManagingRepairLastModified = i;

                    if (indexOfIsNFInsurerManagingRepair != null) {
                        currentState[i] = DateHelper.getCurrentDateTime();
                    }
                } else if ("clientVatRegistered".equals(propertyNames[i])) {

                    String newStatus = currentState[i] != null ? currentState[i].toString() : null;
                    String oldStatus = null;
                    if (previousState[i] != null) {
                        oldStatus = previousState[i].toString();
                    }

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfClientVatRegistered = i;
                        if (indexOfClientVatRegisteredLastModified != null) {
                            currentState[indexOfClientVatRegisteredLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }
                } else if ("clientVatRegisteredLastModified".equals(propertyNames[i])) {

                    indexOfClientVatRegisteredLastModified = i;

                    if (indexOfClientVatRegistered != null) {
                        currentState[i] = DateHelper.getCurrentDateTime();
                    }
                }
            }

        } else if (entity instanceof Claim) {
            // Check for Liability Status update and add notification if changed
            Claim claim = (Claim) entity;
            for (int i = 0; i < propertyNames.length; i++) {
                if ("liabilityStatus".equals(propertyNames[i]) && ((previousState[i] == null && currentState[i] != null)
                        || (previousState[i] != null && currentState[i] == null)
                        || (!currentState[i].toString().equals(previousState[i].toString())))) {
                    LOG.debug("Liability status changed from '{}' to '{}': adding notification", previousState[i], currentState[i]);
                    getNotificationService().addNotification(claim, new LiabilityStatusUpdatedNotification((LiabilityStatus)currentState[i]));
                }
            }
        }

        return true;
    }

    public SecurityInfoProvider getSecurityInfoProvider() {
        return securityInfoProvider;

    }

    public synchronized NotificationService getNotificationService() {
        if (notificationService == null) {
            /*
             * This is a bit of a hack....
             * Letting spring inject this bean causes a circular dependency error,
             * so we'll make this class BeanFactoryAware and get the bean ourselves
             */
            notificationService = (NotificationService) bf.getBean("notificationService");
        }
        return notificationService;
    }

    public synchronized FullAuditService getFullAuditService() {
        if (fullAuditService == null) {
            /*
             * This is a bit of a hack....
             * Letting spring inject this bean causes a circular dependency error,
             * so we'll make this class BeanFactoryAware and get the bean ourselves
             */
            fullAuditService = (FullAuditService) bf.getBean("fullAuditService");
        }
        return fullAuditService;
    }

    
    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }

    @Override
    public void setBeanFactory(BeanFactory bf) throws BeansException {
        this.bf = bf;
    }
}
