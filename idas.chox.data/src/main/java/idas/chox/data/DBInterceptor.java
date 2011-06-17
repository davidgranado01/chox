package idas.chox.data;

import idas.chox.core.model.Auditable;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.util.DateHelper;
import java.io.Serializable;

import java.util.Date;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBInterceptor extends EmptyInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(DBInterceptor.class);
    private SecurityInfoProvider securityInfoProvider;

    @Override
    public boolean onSave(Object entity,
            Serializable id,
            Object[] state,
            String[] propertyNames,
            Type[] types) {

        if (entity instanceof Auditable) {

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



            for (int i = 0; i < propertyNames.length; i++) {


                if ("inspectionBookedDateLastModified".equals(propertyNames[i])) {

                    indexOfInspectionBookedDateLastModified = i;

                    if (indexOfInspectionBookedDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                }


                if ("inspectionDateLastModified".equals(propertyNames[i])) {

                    indexOfInspectionDateLastModified = i;
                    if (indexOfInspectionDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                }

                if ("repairAuthorisedDateLastModified".equals(propertyNames[i])) {

                    indexOfRepairAuthorisedDateLastModified = i;
                    if (indexOfRepairAuthorisedDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                }

                if ("repairBookInDateLastModified".equals(propertyNames[i])) {

                    indexOfRepairBookInDateLastModified = i;
                    if (indexOfRepairBookInDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                }

                if ("repairCommencedDateLastModified".equals(propertyNames[i])) {

                    indexOfRepairCommencedDateLastModified = i;
                    if (indexOfRepairCommencedDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                }

                if ("isTotalLostCheckLastModified".equals(propertyNames[i])) {

                    indexOfIsTotalLostCheckLastModified = i;
                    if (indexOfIsTotalLostCheck != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                }

                if ("totalLossOfferMadeLastModified".equals(propertyNames[i])) {
                    indexOfTotalLossOfferMadeLastModified = i;
                    if (indexOfTotalLossOfferMadeDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                }

                if ("totalLossOfferAcceptedLastModified".equals(propertyNames[i])) {

                    indexOfTotalLossOfferAcceptedLastModified = i;
                    if (indexOfTotalLossOfferAcceptedDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                }

                if ("totalLossCheckIssuedLastModified".equals(propertyNames[i])) {
                    indexOfTotalLossCheckIssuedLastModified = i;
                    if (indexOfTotalLossOfferCheckIssuedDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                }

                if ("totalLossCheckReceivedLastModified".equals(propertyNames[i])) {
                    indexOfTotalLossCheckReceivedLastModified = i;
                    if (indexOfTotalLossOfferCheckReceivedDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }
                }

                if ("repairCompletionDateLastModified".equals(propertyNames[i])) {

                    indexOfRepairCompletionDateLastModified = i;
                    if (indexOfRepairCompletionDate != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }


                }
                if ("isRepairOnlyCheckLastModified".equals(propertyNames[i])) {

                    indexOfIsRepairOnlyCheckLastModified = i;
                    if (indexOfIsRepairOnlyCheck != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }


                }
                
                if ("isNFInsurerManagingRepairLastModified".equals(propertyNames[i])) {

                    indexOfIsNFInsurerManagingRepairLastModified = i;
                    if (indexOfIsNFInsurerManagingRepair != null) {
                        state[i] = DateHelper.getCurrentDateTime();
                    }


                }


                
               // =========================================================================

              if ("repairBookInDate".equals(propertyNames[i])) {

                  Date status = (Date)state[i];
                  if(status != null){
                   indexOfRepairBookInDate =i;

                    if ( indexOfRepairBookInDateLastModified != null) {
                        state[indexOfRepairBookInDateLastModified] = DateHelper.getCurrentDateTime();
                    }

                  }
                }


                if ("repairAuthorisedDate".equals(propertyNames[i])) {

                    Date status = (Date)state[i];
                  if(status != null){
                   indexOfRepairAuthorisedDate =i;

                    if ( indexOfRepairAuthorisedDateLastModified != null) {
                        state[indexOfRepairAuthorisedDateLastModified] = DateHelper.getCurrentDateTime();
                    }

                  }
                }

                 if ("repairCommencedDate".equals(propertyNames[i])) {

                    Date status = (Date)state[i];
                  if(status != null){
                   indexOfRepairCommencedDate =i;

                    if ( indexOfRepairCommencedDateLastModified != null) {
                        state[indexOfRepairCommencedDateLastModified] = DateHelper.getCurrentDateTime();
                    }

                  }
                }

                if ("isTotalLostCheck".equals(propertyNames[i])) {

                    String status =state[i].toString();
                  if(status != null && status.equals("true")){
                   indexOfIsTotalLostCheck =i;

                    if ( indexOfIsTotalLostCheckLastModified != null) {
                        state[indexOfIsTotalLostCheckLastModified] = DateHelper.getCurrentDateTime();
                    }

                  }
                }


                if ("totalLossOfferMadeDate".equals(propertyNames[i])) {

                    Date status = (Date)state[i];
                  if(status != null){
                   indexOfTotalLossOfferMadeDate =i;

                    if ( indexOfTotalLossOfferMadeLastModified != null) {
                        state[indexOfTotalLossOfferMadeLastModified] = DateHelper.getCurrentDateTime();
                    }

                  }
                }

                if ("totalLossOfferAcceptedDate".equals(propertyNames[i])) {

                    Date status = (Date)state[i];
                  if(status != null){
                   indexOfTotalLossOfferAcceptedDate =i;

                    if ( indexOfTotalLossOfferAcceptedLastModified != null) {
                        state[indexOfTotalLossOfferAcceptedLastModified] = DateHelper.getCurrentDateTime();
                    }

                  }
                }

                if ("totalLossOfferCheckIssuedDate".equals(propertyNames[i])) {

                    Date status = (Date)state[i];
                  if(status != null){
                   indexOfTotalLossOfferCheckIssuedDate =i;

                    if ( indexOfTotalLossCheckIssuedLastModified != null) {
                        state[indexOfTotalLossCheckIssuedLastModified] = DateHelper.getCurrentDateTime();
                    }

                  }
                }

                 if ("totalLossOfferCheckReceivedDate".equals(propertyNames[i])) {

                    Date status = (Date)state[i];
                  if(status != null){
                   indexOfTotalLossOfferCheckReceivedDate =i;

                    if ( indexOfTotalLossCheckReceivedLastModified != null) {
                        state[indexOfTotalLossCheckReceivedLastModified] = DateHelper.getCurrentDateTime();
                    }

                  }
                }

                if ("inspectionBookedDate".equals(propertyNames[i])) {

                    Date status = (Date)state[i];
                  if(status != null){
                   indexOfInspectionBookedDate =i;

                    if ( indexOfInspectionBookedDateLastModified != null) {
                        state[indexOfInspectionBookedDateLastModified] = DateHelper.getCurrentDateTime();
                    }

                  }
                }

                if ("inspectionDate".equals(propertyNames[i])) {

                    Date status = (Date)state[i];
                  if(status != null){
                   indexOfInspectionDate =i;

                    if ( indexOfInspectionDateLastModified != null) {
                        state[indexOfInspectionDateLastModified] = DateHelper.getCurrentDateTime();
                    }

                  }
                }

                 if ("repairCompletionDate".equals(propertyNames[i])) {

                    Date status = (Date)state[i];
                  if(status != null){
                   indexOfRepairCompletionDate =i;

                    if ( indexOfRepairCompletionDateLastModified != null) {
                        state[indexOfRepairCompletionDateLastModified] = DateHelper.getCurrentDateTime();
                    }

                  }
                }

                if ("isRepairOnlyCheck".equals(propertyNames[i])) {

                    String status =state[i].toString();
                  if(status != null && status.equals("true")) {
                   indexOfIsRepairOnlyCheck =i;

                    if ( indexOfIsRepairOnlyCheckLastModified != null) {
                        state[indexOfIsRepairOnlyCheckLastModified] = DateHelper.getCurrentDateTime();
                    }

                  }
                }


                 if ("isNFInsurerManagingRepair".equals(propertyNames[i])) {

                    String status =state[i].toString();
                  if(status != null && status.equals("true")) {
                   indexOfIsNFInsurerManagingRepair =i;

                    if ( indexOfIsNFInsurerManagingRepairLastModified != null) {
                        state[indexOfIsNFInsurerManagingRepairLastModified] = DateHelper.getCurrentDateTime();
                    }

                  }
                }

  
            }

        }
        return true;

    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] state1, Object[] state2, String[] propertyNames, Type[] types) {
        if (entity instanceof Auditable) {

            Integer indexOfStatusModifiedDate = null;
            Integer indexForPrevStatus = null;
            Date statusModifiedDate = null;
            String prevStatus = null;

            for (int i = 0; i < propertyNames.length; i++) {
                if ("lastModifiedDate".equals(propertyNames[i])) {

                    LOG.debug("propertyNames[" + i + "]" + propertyNames[i]);
                    state1[i] = DateHelper.getCurrentDateTime();

                    LOG.debug("state1[" + i + "]" + state1[i]);
                } else if ("lastModifiedBy".equals(propertyNames[i])) {
                    state1[i] = this.getSecurityInfoProvider().getCurrentUser();
                } else if ("statusModifiedDate".equals(propertyNames[i])) {
                    if (statusModifiedDate == null) {
                        indexOfStatusModifiedDate = i;
                    } else {
                        state1[i] = statusModifiedDate;
                    }
                } else if ("previousStatus".equals(propertyNames[i])) {
                    if (prevStatus == null) {
                        indexForPrevStatus = i;
                    } else {
                        state1[i] = prevStatus;
                    }
                } else if ("status".equals(propertyNames[i])) {

                    String newStatus = state1[i].toString();
                    String oldStatus = state2[i].toString();

                    LOG.debug("newStatus" + newStatus + "state1[i]" + state1[i]);
                    LOG.debug("oldStatus" + oldStatus + "state2[i]" + state2[i]);

                    if (!newStatus.equalsIgnoreCase(oldStatus)) {
                        if (indexOfStatusModifiedDate != null) {
                            state1[indexOfStatusModifiedDate] = new Date();
                        } else {
                            statusModifiedDate = new Date();
                        }

                        if (indexForPrevStatus != null) {
                            state1[indexForPrevStatus] = oldStatus;
                        } else {
                            prevStatus = oldStatus;
                        }
                    }

                }

            }
        }




        if (entity instanceof HireMonitoringDetail) {


            LOG.debug("Inside  HireMonitoringDetail");
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




            for (int i = 0; i < propertyNames.length; i++) {



                if ("totalLossOfferCheckReceivedDate".equals(propertyNames[i])) {

                    LOG.debug("Inside  totalLossOfferCheckReceivedDate");
                    Date newStatus = (Date) state1[i];
                    Date oldStatus = (Date) state2[i];
                    LOG.debug("newStatus   " + newStatus);
                    LOG.debug("oldStatus   " + oldStatus);

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfTotalLossOfferCheckReceivedDate = i;
                        LOG.debug("Inside  if1");
                        if (indexOfTotalLossCheckReceivedLastModified != null) {
                            LOG.debug("Inside  if2");
                            state1[indexOfTotalLossCheckReceivedLastModified] = DateHelper.getCurrentDateTime();

                        }
                    }

                }

                if ("totalLossCheckReceivedLastModified".equals(propertyNames[i])) {

                    LOG.debug("Inside  totalLossCheckReceivedLastModified");
                    indexOfTotalLossCheckReceivedLastModified = i;

                    if (indexOfTotalLossOfferCheckReceivedDate != null) {

                        LOG.debug("Inside  totalLossCheckReceivedLastModified if ");

                        state1[i] = DateHelper.getCurrentDateTime();

                    }

                }



                if ("totalLossOfferCheckIssuedDate".equals(propertyNames[i])) {

                    LOG.debug("Inside  totalLossOfferCheckIssuedDate");
                    Date newStatus = (Date) state1[i];
                    Date oldStatus = (Date) state2[i];
                    LOG.debug("newStatus   " + newStatus);
                    LOG.debug("oldStatus   " + oldStatus);

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfTotalLossOfferCheckIssuedDate = i;
                        LOG.debug("Inside  if1");
                        if (indexOfTotalLossCheckIssuedLastModified != null) {
                            LOG.debug("Inside  if2");
                            state1[indexOfTotalLossCheckIssuedLastModified] = DateHelper.getCurrentDateTime();

                        }
                    }

                }

                if ("totalLossCheckIssuedLastModified".equals(propertyNames[i])) {

                    LOG.debug("Inside  totalLossCheckIssuedLastModified");
                    indexOfTotalLossCheckIssuedLastModified = i;

                    if (indexOfTotalLossOfferCheckIssuedDate != null) {

                        LOG.debug("Inside  totalLossCheckIssuedLastModified if ");

                        state1[i] = DateHelper.getCurrentDateTime();

                    }

                }



                if ("totalLossOfferAcceptedDate".equals(propertyNames[i])) {

                    LOG.debug("Inside  totalLossOfferAcceptedDate");
                    Date newStatus = (Date) state1[i];
                    Date oldStatus = (Date) state2[i];
                    LOG.debug("newStatus   " + newStatus);
                    LOG.debug("oldStatus   " + oldStatus);

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfTotalLossOfferAcceptedDate = i;
                        LOG.debug("Inside  if1");
                        if (indexOfTotalLossOfferAcceptedLastModified != null) {
                            LOG.debug("Inside  if2");
                            state1[indexOfTotalLossOfferAcceptedLastModified] = DateHelper.getCurrentDateTime();

                        }
                    }

                }

                if ("totalLossOfferAcceptedLastModified".equals(propertyNames[i])) {

                    LOG.debug("Inside  totalLossOfferAcceptedLastModified");
                    indexOfTotalLossOfferAcceptedLastModified = i;

                    if (indexOfTotalLossOfferAcceptedDate != null) {

                        LOG.debug("Inside  totalLossOfferAcceptedLastModified if ");

                        state1[i] = DateHelper.getCurrentDateTime();

                    }

                }

                if ("totalLossOfferMadeDate".equals(propertyNames[i])) {

                    LOG.debug("Inside  totalLossOfferMadeDate");
                    Date newStatus = (Date) state1[i];
                    Date oldStatus = (Date) state2[i];
                    LOG.debug("newStatus   " + newStatus);
                    LOG.debug("oldStatus   " + oldStatus);

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfTotalLossOfferMadeDate = i;
                        LOG.debug("Inside  if1");
                        if (indexOfTotalLossOfferMadeLastModified != null) {
                            LOG.debug("Inside  if2");
                            state1[indexOfTotalLossOfferMadeLastModified] = DateHelper.getCurrentDateTime();

                        }
                    }
                }

                if ("totalLossOfferMadeLastModified".equals(propertyNames[i])) {

                    LOG.debug("Inside  totalLossOfferMadeLastModified");
                    indexOfTotalLossOfferMadeLastModified = i;

                    if (indexOfTotalLossOfferMadeDate != null) {

                        LOG.debug("Inside  totalLossOfferMadeLastModified if ");

                        state1[i] = DateHelper.getCurrentDateTime();

                    }

                }

                if ("repairCompletionDate".equals(propertyNames[i])) {

                    LOG.debug("Inside  repairCompletionDate");
                    Date newStatus = (Date) state1[i];
                    Date oldStatus = (Date) state2[i];
                    LOG.debug("newStatus   " + newStatus);
                    LOG.debug("oldStatus   " + oldStatus);

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfRepairCompletionDate = i;
                        LOG.debug("Inside  if1");
                        if (indexOfRepairCompletionDateLastModified != null) {
                            LOG.debug("Inside  if2");
                            state1[indexOfRepairCompletionDateLastModified] = DateHelper.getCurrentDateTime();

                        }
                    }

                }

                if ("repairCompletionDateLastModified".equals(propertyNames[i])) {

                    LOG.debug("Inside  repairCompletionDateLastModified");
                    indexOfRepairCompletionDateLastModified = i;

                    if (indexOfRepairCompletionDate != null) {

                        LOG.debug("Inside  repairCompletionDateLastModified if ");

                        state1[i] = DateHelper.getCurrentDateTime();

                    }

                }

                if ("isTotalLostCheck".equals(propertyNames[i])) {

                    LOG.debug("Inside  isTotalLostCheck");
                    String newStatus = state1[i].toString();
                    String oldStatus = state2[i].toString();
                    LOG.debug("newStatus   " + newStatus);
                    LOG.debug("oldStatus   " + oldStatus);

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfIsTotalLostCheck = i;
                        LOG.debug("Inside  if1");
                        if (indexOfIsTotalLostCheckLastModified != null) {
                            LOG.debug("Inside  if2");
                            state1[indexOfIsTotalLostCheckLastModified] = DateHelper.getCurrentDateTime();

                        }
                    }

                }

                if ("isTotalLostCheckLastModified".equals(propertyNames[i])) {

                    LOG.debug("Inside  isTotalLostCheckLastModified");
                    indexOfIsTotalLostCheckLastModified = i;

                    if (indexOfIsTotalLostCheck != null) {

                        LOG.debug("Inside  isTotalLostCheckLastModified if ");

                        state1[i] = DateHelper.getCurrentDateTime();

                    }

                }


                if ("repairCommencedDate".equals(propertyNames[i])) {

                    LOG.debug("Inside  repairCommencedDate");
                    Date newStatus = (Date) state1[i];
                    Date oldStatus = (Date) state2[i];
                    LOG.debug("newStatus   " + newStatus);
                    LOG.debug("oldStatus   " + oldStatus);

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfRepairCommencedDate = i;
                        LOG.debug("Inside  if1");
                        if (indexOfRepairCommencedDateLastModified != null) {
                            LOG.debug("Inside  if2");
                            state1[indexOfRepairCommencedDateLastModified] = DateHelper.getCurrentDateTime();

                        }
                    }
                }

                if ("repairCommencedDateLastModified".equals(propertyNames[i])) {

                    LOG.debug("Inside  repairCommencedDateLastModified");
                    indexOfRepairCommencedDateLastModified = i;

                    if (indexOfRepairCommencedDate != null) {

                        LOG.debug("Inside  repairCommencedDateLastModified if ");

                        state1[i] = DateHelper.getCurrentDateTime();

                    }

                }


                if ("repairBookInDate".equals(propertyNames[i])) {

                    LOG.debug("Inside  repairBookInDate");
                    Date newStatus = (Date) state1[i];
                    Date oldStatus = (Date) state2[i];
                    LOG.debug("newStatus   " + newStatus);
                    LOG.debug("oldStatus   " + oldStatus);


                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfRepairBookInDate = i;
                        LOG.debug("Inside  if1");
                        if (indexOfRepairBookInDateLastModified != null) {
                            LOG.debug("Inside  if2");
                            state1[indexOfRepairBookInDateLastModified] = DateHelper.getCurrentDateTime();

                        }

                    }
                }

                if ("repairBookInDateLastModified".equals(propertyNames[i])) {

                    LOG.debug("Inside  repairBookInDateLastModified");
                    indexOfRepairBookInDateLastModified = i;

                    if (indexOfRepairBookInDate != null) {

                        LOG.debug("Inside  repairBookInDateLastModified if ");

                        state1[i] = DateHelper.getCurrentDateTime();

                    }

                }

                if ("repairAuthorisedDate".equals(propertyNames[i])) {

                    LOG.debug("Inside  repairAuthorisedDate");
                    Date newStatus = (Date) state1[i];
                    Date oldStatus = (Date) state2[i];

                    LOG.debug("newStatus   " + newStatus);
                    LOG.debug("oldStatus   " + oldStatus);

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfRepairAuthorisedDate = i;
                        if (indexOfRepairAuthorisedDateLastModified != null) {

                            state1[indexOfRepairAuthorisedDateLastModified] = DateHelper.getCurrentDateTime();

                        }
                    }
                }

                if ("repairAuthorisedDateLastModified".equals(propertyNames[i])) {

                    LOG.debug("Inside  repairAuthorisedDateLastModified");
                    indexOfRepairAuthorisedDateLastModified = i;

                    if (indexOfRepairAuthorisedDate != null) {

                        LOG.debug("Inside  repairAuthorisedDateLastModified   if");

                        state1[i] = DateHelper.getCurrentDateTime();

                    }

                }


                if ("inspectionBookedDate".equals(propertyNames[i])) {

                    LOG.debug("Inside  inspectionBookedDate");

                    Date newStatus = (Date) state1[i];
                    Date oldStatus = (Date) state2[i];

                    LOG.debug("newStatus   " + newStatus);
                    LOG.debug("oldStatus   " + oldStatus);

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfInspectionBookedDate = i;

                        if (indexOfInspectionBookedDateLastModified != null) {

                            state1[indexOfInspectionBookedDateLastModified] = DateHelper.getCurrentDateTime();
                        }
                    }

                }

                if ("inspectionBookedDateLastModified".equals(propertyNames[i])) {

                    LOG.debug("Inside  inspectionBookedDateLastModified");
                    indexOfInspectionBookedDateLastModified = i;

                    if (indexOfInspectionBookedDate != null) {

                        LOG.debug("Inside  inspectionBookedDateLastModified   if");

                        state1[i] = DateHelper.getCurrentDateTime();
                    }

                }




                if ("inspectionDate".equals(propertyNames[i])) {

                    LOG.debug("Inside  inspectionDate");
                    Date newStatus = (Date) state1[i];
                    Date oldStatus = (Date) state2[i];

                    LOG.debug("newStatus   " + newStatus);
                    LOG.debug("oldStatus   " + oldStatus);

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {

                        indexOfInspectionDate = i;

                        if (indexOfInspectionDateLastModified != null) {

                            state1[indexOfInspectionDateLastModified] = DateHelper.getCurrentDateTime();

                        }

                    }


                }

                if ("inspectionDateLastModified".equals(propertyNames[i])) {

                    LOG.debug("Inside  inspectionDateLastModified");
                    indexOfInspectionDateLastModified = i;

                    if (indexOfInspectionDate != null) {

                        LOG.debug("Inside  inspectionDateLastModified   if");

                        state1[i] = DateHelper.getCurrentDateTime();
                    }

                }


                if ("isRepairOnlyCheck".equals(propertyNames[i])) {

                    LOG.debug("Inside  isRepairOnlyCheck");
                    String newStatus = state1[i].toString();
                    String oldStatus = state2[i].toString();
                    LOG.debug("newStatus   " + newStatus);
                    LOG.debug("oldStatus   " + oldStatus);

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfIsRepairOnlyCheck = i;
                        LOG.debug("Inside  if1");
                        if (indexOfIsRepairOnlyCheckLastModified != null) {
                            LOG.debug("Inside  if2");
                            state1[indexOfIsRepairOnlyCheckLastModified] = DateHelper.getCurrentDateTime();

                        }
                    }

                }

                if ("isRepairOnlyCheckLastModified".equals(propertyNames[i])) {

                    LOG.debug("Inside  isRepairOnlyCheckLastModified");
                    indexOfIsRepairOnlyCheckLastModified = i;

                    if (indexOfIsRepairOnlyCheck != null) {

                        LOG.debug("Inside  isRepairOnlyCheckLastModified if ");

                        state1[i] = DateHelper.getCurrentDateTime();

                    }

                }


                if ("isNFInsurerManagingRepair".equals(propertyNames[i])) {

                    LOG.debug("Inside  isNFInsurerManagingRepair");
                    String newStatus = state1[i].toString();
                    String oldStatus = state2[i].toString();
                    LOG.debug("newStatus   " + newStatus);
                    LOG.debug("oldStatus   " + oldStatus);

                    if ((newStatus != null && oldStatus != null && !newStatus.equals(oldStatus)) || (newStatus != null && oldStatus == null) || (newStatus == null && oldStatus != null)) {
                        indexOfIsNFInsurerManagingRepair = i;
                        LOG.debug("Inside  if1");
                        if (indexOfIsNFInsurerManagingRepairLastModified != null) {
                            LOG.debug("Inside  if2");
                            state1[indexOfIsNFInsurerManagingRepairLastModified] = DateHelper.getCurrentDateTime();

                        }
                    }

                }

                if ("isNFInsurerManagingRepairLastModified".equals(propertyNames[i])) {

                    LOG.debug("Inside  isNFInsurerManagingRepairLastModified");
                    indexOfIsNFInsurerManagingRepairLastModified = i;

                    if (indexOfIsNFInsurerManagingRepair != null) {

                        LOG.debug("Inside  isNFInsurerManagingRepairLastModified if ");

                        state1[i] = DateHelper.getCurrentDateTime();

                    }

                }


            }

        }

        return true;

    }

    public SecurityInfoProvider getSecurityInfoProvider() {

        return securityInfoProvider;

    }

    public void setSecurityInfoProvider(SecurityInfoProvider securityInforProvider) {

        this.securityInfoProvider = securityInforProvider;


    }
}
