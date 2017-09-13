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
import idas.chox.core.model.Auditable;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.model.FullAudit;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.FullAuditService;
import idas.chox.core.services.InvoiceService;
import idas.chox.core.util.DateHelper;

public class DBInterceptor extends EmptyInterceptor implements BeanFactoryAware {

    private final Object LOCK = new Object();
    private static final Logger LOG = LoggerFactory.getLogger(DBInterceptor.class);
    private SecurityInfoProvider securityInfoProvider;
    private volatile FullAuditService fullAuditService;
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

        if (getSecurityInfoProvider().getCurrentUser() == null) {
            LOG.error("No 'current' user found in onSave DB Interceptor for entity class '{}'", entity.getClass());
        }

        if (entity instanceof Auditable) {
            LOG.debug("In onSave for Auditable entity class '{}': {}", entity.getClass(), entity);
            for (int i = 0; i < propertyNames.length; i++) {
                if (null != propertyNames[i]) switch (propertyNames[i]) {
                    case "createdDate":
                        state[i] = DateHelper.getCurrentDateTime();
                        break;
                    case "createdBy":
                        state[i] = this.getSecurityInfoProvider().getCurrentUser();
                        break;
                    case "lastModifiedDate":
                        state[i] = DateHelper.getCurrentDateTime();
                        break;
                    case "lastModifiedBy":
                        state[i] = this.getSecurityInfoProvider().getCurrentUser();
                        break;
                }
            }
        }

        if (entity instanceof HireMonitoringDetail) {

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


                if (null != propertyNames[i]) switch (propertyNames[i]) {
                    case "inspectionBookedDateLastModified":
                        indexOfInspectionBookedDateLastModified = i;
                        if (indexOfInspectionBookedDate != null) {
                            state[i] = DateHelper.getCurrentDateTime();
                        }
                        break;
                    case "inspectionDateLastModified":
                        indexOfInspectionDateLastModified = i;
                        if (indexOfInspectionDate != null) {
                            state[i] = DateHelper.getCurrentDateTime();
                        }
                        break;
                    case "repairAuthorisedDateLastModified":
                        indexOfRepairAuthorisedDateLastModified = i;
                        if (indexOfRepairAuthorisedDate != null) {
                            state[i] = DateHelper.getCurrentDateTime();
                        }
                        break;
                    case "repairBookInDateLastModified":
                        indexOfRepairBookInDateLastModified = i;
                        if (indexOfRepairBookInDate != null) {
                            state[i] = DateHelper.getCurrentDateTime();
                        }
                        break;
                    case "repairCommencedDateLastModified":
                        indexOfRepairCommencedDateLastModified = i;
                        if (indexOfRepairCommencedDate != null) {
                            state[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "isTotalLostCheckLastModified":
                        indexOfIsTotalLostCheckLastModified = i;
                        if (indexOfIsTotalLostCheck != null) {
                            state[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "totalLossOfferMadeLastModified":
                        indexOfTotalLossOfferMadeLastModified = i;
                        if (indexOfTotalLossOfferMadeDate != null) {
                            state[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "totalLossOfferAcceptedLastModified":
                        indexOfTotalLossOfferAcceptedLastModified = i;
                        if (indexOfTotalLossOfferAcceptedDate != null) {
                            state[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "totalLossCheckIssuedLastModified":
                        indexOfTotalLossCheckIssuedLastModified = i;
                        if (indexOfTotalLossOfferCheckIssuedDate != null) {
                            state[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "totalLossCheckReceivedLastModified":
                        indexOfTotalLossCheckReceivedLastModified = i;
                        if (indexOfTotalLossOfferCheckReceivedDate != null) {
                            state[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "repairCompletionDateLastModified":
                        indexOfRepairCompletionDateLastModified = i;
                        if (indexOfRepairCompletionDate != null) {
                            state[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "isRepairOnlyCheckLastModified":
                        indexOfIsRepairOnlyCheckLastModified = i;
                        if (indexOfIsRepairOnlyCheck != null) {
                            state[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "isNFInsurerManagingRepairLastModified":
                        indexOfIsNFInsurerManagingRepairLastModified = i;
                        if (indexOfIsNFInsurerManagingRepair != null) {
                            state[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "clientVatRegisteredLastModified":
                        indexOfClientVatRegisteredLastModified = i;
                        if (indexOfClientVatRegistered != null) {
                            state[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "repairBookInDate":
                        if (state[i] != null) {
                            indexOfRepairBookInDate = i;
                            
                            if (indexOfRepairBookInDateLastModified != null) {
                                state[indexOfRepairBookInDateLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    case "repairAuthorisedDate":
                        if (state[i] != null) {
                            indexOfRepairAuthorisedDate = i;
                            
                            if (indexOfRepairAuthorisedDateLastModified != null) {
                                state[indexOfRepairAuthorisedDateLastModified] = DateHelper.getCurrentDateTime();
                            }
                            }       
                        break;
                    case "repairCommencedDate":
                        if (state[i] != null) {
                            indexOfRepairCommencedDate = i;
                            
                            if (indexOfRepairCommencedDateLastModified != null) {
                                state[indexOfRepairCommencedDateLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    case "isTotalLostCheck":
                        if (state[i] != null && state[i].toString().equals("true")) {
                            indexOfIsTotalLostCheck = i;
                            
                            if (indexOfIsTotalLostCheckLastModified != null) {
                                state[indexOfIsTotalLostCheckLastModified] = DateHelper.getCurrentDateTime();
                            }

                        }       
                        break;
                    case "totalLossOfferMadeDate":
                        if (state[i] != null) {
                            indexOfTotalLossOfferMadeDate = i;
                            if (indexOfTotalLossOfferMadeLastModified != null) {
                                state[indexOfTotalLossOfferMadeLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    case "totalLossOfferAcceptedDate":
                        if (state[i] != null) {
                            indexOfTotalLossOfferAcceptedDate = i;
                            if (indexOfTotalLossOfferAcceptedLastModified != null) {
                                state[indexOfTotalLossOfferAcceptedLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    case "totalLossOfferCheckIssuedDate":
                        if (state[i] != null) {
                            indexOfTotalLossOfferCheckIssuedDate = i;
                            if (indexOfTotalLossCheckIssuedLastModified != null) {
                                state[indexOfTotalLossCheckIssuedLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    case "totalLossOfferCheckReceivedDate":
                        if (state[i] != null) {
                            indexOfTotalLossOfferCheckReceivedDate = i;
                            if (indexOfTotalLossCheckReceivedLastModified != null) {
                                state[indexOfTotalLossCheckReceivedLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    case "inspectionBookedDate":
                        if (state[i] != null) {
                            indexOfInspectionBookedDate = i;
                            if (indexOfInspectionBookedDateLastModified != null) {
                                state[indexOfInspectionBookedDateLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    case "inspectionDate":
                        if (state[i] != null) {
                            indexOfInspectionDate = i;                        
                            if (indexOfInspectionDateLastModified != null) {
                                state[indexOfInspectionDateLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    case "repairCompletionDate":
                        if (state[i] != null) {
                            indexOfRepairCompletionDate = i;
                            if (indexOfRepairCompletionDateLastModified != null) {
                                state[indexOfRepairCompletionDateLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    case "isRepairOnlyCheck":
                        if (state[i] != null && state[i].toString().equals("true")) {
                            indexOfIsRepairOnlyCheck = i;
                            if (indexOfIsRepairOnlyCheckLastModified != null) {
                                state[indexOfIsRepairOnlyCheckLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    case "isNFInsurerManagingRepair":
                        if (state[i] != null && state[i].toString().equals("true")) {
                            indexOfIsNFInsurerManagingRepair = i;
                            if (indexOfIsNFInsurerManagingRepairLastModified != null) {
                                state[indexOfIsNFInsurerManagingRepairLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }
                        break;
                    case "clientVatRegistered":
                        if (state[i] != null && state[i].toString().equals("true")) {
                            indexOfClientVatRegistered = i;
                            if (indexOfClientVatRegisteredLastModified != null) {
                                state[indexOfClientVatRegisteredLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                }
            }
        } // End of entity instanceof HireMonitoringDetail

        return true;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
            Object[] previousState, String[] propertyNames, Type[] types) {
        LOG.debug("**** In onFlushDirty() for entity class '{}' (id={}) ****", entity.getClass(), id);
        if (getSecurityInfoProvider().getCurrentUser() == null) {
            // NB. updating failed login attempts will have no current user as authentication has failed
            LOG.debug("No 'current' user found in DB Interceptor for entity class '{}'", entity.getClass());
        }

        if (entity instanceof Auditable && getSecurityInfoProvider().getCurrentUser() != null) {
            LOG.debug("    onFlushDirty(): we have an Auditable entity");
            Integer indexOfStatusModifiedDate = null;
            Integer indexForPrevStatus = null;
            Date statusModifiedDate = null;
            String prevStatus = null;

            for (int i = 0; i < propertyNames.length; i++) {
                LOG.trace("Checking auditable property [{}]:{}", i, propertyNames[i]);
                if (null != propertyNames[i]) switch (propertyNames[i]) {
                    case "lastModifiedDate":
                        currentState[i] = DateHelper.getCurrentDateTime();
                        break;
                    case "lastModifiedBy":
                        currentState[i] = getSecurityInfoProvider().getCurrentUser();
                        break;
                    case "statusModifiedDate":
                        if (statusModifiedDate == null) {
                            indexOfStatusModifiedDate = i;
                        } else {
                            currentState[i] = statusModifiedDate;
                        }  
                        break;
                    case "previousStatus":
                        if (prevStatus == null) {
                            indexForPrevStatus = i;
                        } else {
                            currentState[i] = prevStatus;
                        }   
                        break;
                    case "status":
                        String newStatus;
                        if (currentState[i] != null) {
                            newStatus = currentState[i].toString();
                        } else {
                            newStatus = "";
                        }   
                        String oldStatus;
                        if (previousState != null && previousState[i] != null) {
                            oldStatus = previousState[i].toString();
                        } else {
                            oldStatus = "";
                        }   
                        LOG.debug("    newStatus={}, oldStatus={}", newStatus, oldStatus);
                        if (!newStatus.equals(oldStatus)) {
                            LOG.debug("    Status change from '{}' to '{}': updating statusModifiedDate", oldStatus, newStatus);
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
                    }   break;
                }
            }
        }

        if (entity instanceof FullAudit && getSecurityInfoProvider().getCurrentUser() != null) {
            LOG.debug("    we have a FullAudit entity");
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
                        LOG.debug("    Audit entry: table='{}', id={}, parameter='{}', old_value='{}', new_value='{}', by='{}'",
                                new Object[]{entity.getClass().toString(), id,
                                    propertyNames[i], previousState[i], currentState[i],
                                    getSecurityInfoProvider().getCurrentUser().getId()});
                    }
                }
            }
        }

        if (entity instanceof HireMonitoringDetail) {
            HireMonitoringDetail hmd = (HireMonitoringDetail) entity;
            LOG.debug("    HireMonitoringDetail instance with id={}...", hmd.getId());

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

                if (null != propertyNames[i]) switch (propertyNames[i]) {
                    case "totalLossOfferCheckReceivedDate":{
                        Date newStatus = (Date) currentState[i];
                        Date oldStatus = (Date) previousState[i];
                        if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                            indexOfTotalLossOfferCheckReceivedDate = i;
                            if (indexOfTotalLossCheckReceivedLastModified != null) {
                                currentState[indexOfTotalLossCheckReceivedLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    }
                    case "totalLossCheckReceivedLastModified":
                        indexOfTotalLossCheckReceivedLastModified = i;
                        if (indexOfTotalLossOfferCheckReceivedDate != null) {
                            currentState[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "totalLossOfferCheckIssuedDate":{
                        Date newStatus = (Date) currentState[i];
                        Date oldStatus = (Date) previousState[i];
                        if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                            indexOfTotalLossOfferCheckIssuedDate = i;
                            if (indexOfTotalLossCheckIssuedLastModified != null) {
                                currentState[indexOfTotalLossCheckIssuedLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    }
                    case "totalLossCheckIssuedLastModified":
                        indexOfTotalLossCheckIssuedLastModified = i;
                        if (indexOfTotalLossOfferCheckIssuedDate != null) {
                            currentState[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "totalLossOfferAcceptedDate":{
                        Date newStatus = (Date) currentState[i];
                        Date oldStatus = (Date) previousState[i];
                        if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                            indexOfTotalLossOfferAcceptedDate = i;
                            if (indexOfTotalLossOfferAcceptedLastModified != null) {
                                currentState[indexOfTotalLossOfferAcceptedLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    }
                    case "totalLossOfferAcceptedLastModified":
                        indexOfTotalLossOfferAcceptedLastModified = i;
                        if (indexOfTotalLossOfferAcceptedDate != null) {
                            currentState[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "totalLossOfferMadeDate":{
                        Date newStatus = (Date) currentState[i];
                        Date oldStatus = (Date) previousState[i];
                        if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                            indexOfTotalLossOfferMadeDate = i;
                            if (indexOfTotalLossOfferMadeLastModified != null) {
                                currentState[indexOfTotalLossOfferMadeLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    }
                    case "totalLossOfferMadeLastModified":
                        indexOfTotalLossOfferMadeLastModified = i;
                        if (indexOfTotalLossOfferMadeDate != null) {
                            currentState[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "repairCompletionDate":{
                        Date newStatus = (Date) currentState[i];
                        Date oldStatus = (Date) previousState[i];
                        if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                            indexOfRepairCompletionDate = i;
                            if (indexOfRepairCompletionDateLastModified != null) {
                                currentState[indexOfRepairCompletionDateLastModified] = DateHelper.getCurrentDateTime();
                                
                            }
                        }       
                        break;
                    }
                    case "repairCompletionDateLastModified":
                        indexOfRepairCompletionDateLastModified = i;
                        if (indexOfRepairCompletionDate != null) {
                            currentState[i] = DateHelper.getCurrentDateTime();

                        }   
                        break;
                    case "isTotalLostCheck":{
                        String newStatus = currentState[i].toString();
                        String oldStatus = previousState[i].toString();
                        if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                            indexOfIsTotalLostCheck = i;
                            if (indexOfIsTotalLostCheckLastModified != null) {
                                currentState[indexOfIsTotalLostCheckLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    }
                    case "isTotalLostCheckLastModified":
                        indexOfIsTotalLostCheckLastModified = i;
                        if (indexOfIsTotalLostCheck != null) {
                            currentState[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "repairCommencedDate":{
                        Date newStatus = (Date) currentState[i];
                        Date oldStatus = (Date) previousState[i];
                        if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                            indexOfRepairCommencedDate = i;
                            if (indexOfRepairCommencedDateLastModified != null) {
                                currentState[indexOfRepairCommencedDateLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    }
                    case "repairCommencedDateLastModified":
                        indexOfRepairCommencedDateLastModified = i;
                        if (indexOfRepairCommencedDate != null) {
                            currentState[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "repairBookInDate":{
                        Date newStatus = (Date) currentState[i];
                        Date oldStatus = (Date) previousState[i];
                        if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                            indexOfRepairBookInDate = i;
                            if (indexOfRepairBookInDateLastModified != null) {
                                currentState[indexOfRepairBookInDateLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    }
                    case "repairBookInDateLastModified":
                        indexOfRepairBookInDateLastModified = i;
                        if (indexOfRepairBookInDate != null) {
                            currentState[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "repairAuthorisedDate":{
                        Date newStatus = (Date) currentState[i];
                        Date oldStatus = (Date) previousState[i];
                        if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                            indexOfRepairAuthorisedDate = i;
                            if (indexOfRepairAuthorisedDateLastModified != null) {
                                currentState[indexOfRepairAuthorisedDateLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    }
                    case "repairAuthorisedDateLastModified":
                        indexOfRepairAuthorisedDateLastModified = i;
                        if (indexOfRepairAuthorisedDate != null) {
                            currentState[i] = DateHelper.getCurrentDateTime();

                        }   
                        break;
                    case "inspectionBookedDate":{
                        Date newStatus = (Date) currentState[i];
                        Date oldStatus = (Date) previousState[i];
                        if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                            indexOfInspectionBookedDate = i;
                            
                            if (indexOfInspectionBookedDateLastModified != null) {
                                currentState[indexOfInspectionBookedDateLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    }
                    case "inspectionBookedDateLastModified":
                        indexOfInspectionBookedDateLastModified = i;
                        if (indexOfInspectionBookedDate != null) {
                            currentState[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "inspectionDate":{
                        Date newStatus = (Date) currentState[i];
                        Date oldStatus = (Date) previousState[i];
                        if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                            indexOfInspectionDate = i;
                            if (indexOfInspectionDateLastModified != null) {
                                currentState[indexOfInspectionDateLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    }
                    case "inspectionDateLastModified":
                        indexOfInspectionDateLastModified = i;
                        if (indexOfInspectionDate != null) {
                            currentState[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "isRepairOnlyCheck":{
                        String newStatus = currentState[i].toString();
                        String oldStatus = previousState[i].toString();
                        if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                            indexOfIsRepairOnlyCheck = i;
                            if (indexOfIsRepairOnlyCheckLastModified != null) {
                                currentState[indexOfIsRepairOnlyCheckLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    }
                    case "isRepairOnlyCheckLastModified":
                        indexOfIsRepairOnlyCheckLastModified = i;
                        if (indexOfIsRepairOnlyCheck != null) {
                            currentState[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "isNFInsurerManagingRepair":{
                        String newStatus = currentState[i].toString();
                        String oldStatus = previousState[i].toString();
                        if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                            indexOfIsNFInsurerManagingRepair = i;
                            if (indexOfIsNFInsurerManagingRepairLastModified != null) {
                                currentState[indexOfIsNFInsurerManagingRepairLastModified] = DateHelper.getCurrentDateTime();
                            }
                        }       
                        break;
                    }
                    case "isNFInsurerManagingRepairLastModified":
                        indexOfIsNFInsurerManagingRepairLastModified = i;
                        if (indexOfIsNFInsurerManagingRepair != null) {
                            currentState[i] = DateHelper.getCurrentDateTime();
                        }   
                        break;
                    case "clientVatRegistered":{
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
                        break;
                    }
                    case "clientVatRegisteredLastModified":
                        indexOfClientVatRegisteredLastModified = i;
                        if (indexOfClientVatRegistered != null) {
                            currentState[i] = DateHelper.getCurrentDateTime();
                        }  
                        break;
                }
            }

        } // End of entity instanceof HireMonitoringDetail
        
        LOG.debug("**** Finished onFlushDirty() for entity class '{}' ****", entity.getClass());
        return true;
    }

    public SecurityInfoProvider getSecurityInfoProvider() {
        return securityInfoProvider;

    }


    public FullAuditService getFullAuditService() {
        FullAuditService myAuditService = this.fullAuditService;
        if (myAuditService == null) {
            /*
             * This is a bit of a hack....
             * Letting spring inject this bean causes a circular dependency error,
             * so we'll make this class BeanFactoryAware and get the bean ourselves
             */
            synchronized (LOCK) {
                myAuditService = this.fullAuditService;
                if (myAuditService == null) {
                    this.fullAuditService = myAuditService = (FullAuditService) bf.getBean("fullAuditService");
                }
            }
        }
        return myAuditService;
    }

    
    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }

    @Override
    public void setBeanFactory(BeanFactory bf) throws BeansException {
        this.bf = bf;
    }
}
