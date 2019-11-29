package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.ReportDataService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.TextHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.HandlerActionsReportObject;
import idas.chox.service.reports.viewdata.HandlerActionsStatusLineItem;


public class NewIncomingHandlerActionsReport implements Report {

    private static final Logger LOG = LoggerFactory.getLogger(NewIncomingHandlerActionsReport.class);
    private Map<String, Object> externalParameter;
    private List<String> reportParameterNames;
    private BaseDataService baseDataService;
    private ReportDataService reportDataService;
    private boolean isInsurerInvoiceUploadEnabled;
    
    @Override
    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }
    
    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    @Override
    public void setReportDataService(ReportDataService reportDataService) {
        this.reportDataService = reportDataService;
    }

    @Override
    public Map<String, Object> getReportParameters() throws Exception {
        Map<String, Object> reportParameters = new HashMap<>();

        try {
            WebUser currentUser = ((WebUser) externalParameter.get("CurrentUser"));
            Date startDate = null;
            Date endDate = null;
            Integer insurerId = -1;
            Integer selectedCHOId = -1;
            Integer selectedWorkgroupId = -1;
            Integer selectedOwnerId = -1;
            boolean isWorkgroupEnabled = false;
            boolean isClaimOwnershipEnabled = false;
            Integer hasStarred = 0;
            
            
            if (currentUser.getInsurer().isInvoiceUploadEnabled() || currentUser.getInsurer().isClaimUploadEnabled()) {
                isInsurerInvoiceUploadEnabled = true;
            }
            
            if (currentUser.getInsurer() != null) {
                insurerId = currentUser.getInsurer().getId();
                isWorkgroupEnabled = currentUser.getInsurer().isWorkgroupEnable();
                isClaimOwnershipEnabled = currentUser.getInsurer().isClaimOwnershipEnable();
            }
            if (((String[]) externalParameter.get("DateStart")) != null) {
                startDate = DateHelper.parse(((String[]) externalParameter.get("DateStart"))[0]);
            }
            if (((String[]) externalParameter.get("DateStart")) != null) {
                endDate = DateHelper.parse(((String[]) externalParameter.get("DateEnd"))[0]);
                endDate = DateHelper.setEndOfDay(endDate);
            }

            if (endDate == null || startDate == null) {
                throw new Exception("Start and End dates cannot be empty.");
            }

            if (endDate.before(startDate)) {
                throw new Exception("End date (" + endDate.toString() + ") is before start date (" + startDate.toString() +  ") ");
            }
            
            if (isClaimOwnershipEnabled) {
                if (((String[]) externalParameter.get("ownerId")) != null) {
                    String ownerId = ((String[]) externalParameter.get("ownerId"))[0];
                    if (!ownerId.equalsIgnoreCase("-1") && !ownerId.equalsIgnoreCase("") && !ownerId.equalsIgnoreCase("--- ALL ---")) {
                        selectedOwnerId = TextHelper.getId(((String[]) externalParameter.get("ownerId"))[0]);
                        LOG.debug("selectedOwnerId={}", selectedOwnerId);
                    }
                }
            }
            
            if (((String[]) externalParameter.get("nhrSupplierId")) != null) {
                String selectedCHO = ((String[]) externalParameter.get("nhrSupplierId"))[0];
                if (!selectedCHO.equalsIgnoreCase("-1") && !selectedCHO.equalsIgnoreCase("") && !selectedCHO.equalsIgnoreCase("--- ALL ---")) {
                    selectedCHOId = TextHelper.getId(selectedCHO);
                    LOG.debug("selectedCHOId={}", selectedOwnerId);
                }
            }
            if (isWorkgroupEnabled) {
                if (((String[]) externalParameter.get("workgroupId")) != null) {
                    String workgropId = ((String[]) externalParameter.get("workgroupId"))[0];
                    if (!workgropId.equalsIgnoreCase("-1") && !workgropId.equalsIgnoreCase("") && !workgropId.equalsIgnoreCase("--- ALL ---")) {
                        selectedWorkgroupId = TextHelper.getId(((String[]) externalParameter.get("workgroupId"))[0]);
                        LOG.debug("selectedWorkgroupId={}", selectedWorkgroupId);
                    }
                }
            }

            List<HandlerActionsReportObject> handlerActionReportObjects = new ArrayList<>();
            if (isWorkgroupEnabled && isClaimOwnershipEnabled) {
                HashMap queryParameters = new HashMap();
                queryParameters.put("pInsurerId", currentUser.getInsurer().getId());
                StringBuilder sb = new StringBuilder();
                sb.append("select id, name from workgroup where insurer_id = :pInsurerId ");
                if (selectedWorkgroupId != -1) {
                    sb.append("and id = :pWorkgroupId ");
                    queryParameters.put("pWorkgroupId", selectedWorkgroupId);
                }
                if (selectedOwnerId != -1) {
                    sb.append("and exists (select * from web_user_workgroup where workgroup_id = workgroup.id and user_id = :pOwnerId) ");
                    queryParameters.put("pOwnerId", selectedOwnerId);
                }
                if (selectedWorkgroupId == -1 && selectedOwnerId != -1) {
                    // Add workgroups user no longer a member of but worked in during period
                    sb.append("union select distinct w.id, name || ' (*)' as name from workgroup w, claim c ")
                      .append("where c.workgroup_id = w.id ")
                      .append("and c.created_date between :pStartDate and :pEndDate ")
                      .append("and c.claim_owner_id = :pOwnerId and not exists ")
                      .append("(select * from web_user_workgroup where workgroup_id = w.id and user_id = :pOwnerId) ");
                    queryParameters.put("pStartDate", startDate);
                    queryParameters.put("pEndDate", endDate);
                }
                sb.append("order by name");
                
                List<Map> result = (List<Map>)reportDataService.getReportData(sb.toString(), queryParameters);
                result.stream().map((data) -> {
                    HandlerActionsReportObject actionReportObject = new HandlerActionsReportObject();
                    actionReportObject.setWorkgroup(data.get("name").toString());
                    actionReportObject.setId((Integer) data.get("id"));
                    return actionReportObject;
                }).map((actionReportObject) -> {
                    handlerActionReportObjects.add(actionReportObject);
                    return actionReportObject;
                }).forEachOrdered((actionReportObject) -> {
                    LOG.debug("Workgroup added: {}", actionReportObject.getWorkgroup());
                });
            } else if (isWorkgroupEnabled) {
                
                HashMap queryParameters = new HashMap();
                queryParameters.put("pInsurerId", currentUser.getInsurer().getId());
                StringBuilder sb = new StringBuilder();
                sb.append("select id, name from workgroup where insurer_id = :pInsurerId ");
                if (selectedWorkgroupId != -1) {
                    sb.append("and id = :pWorkgroupId ");
                    queryParameters.put("pWorkgroupId", selectedWorkgroupId);
                }
                sb.append("order by name");
                
                List<Map> result = (List<Map>)reportDataService.getReportData(sb.toString(), queryParameters);
                result.stream().map((data) -> {
                    HandlerActionsReportObject actionReportObject = new HandlerActionsReportObject();
                    actionReportObject.setWorkgroup(data.get("name").toString());
                    actionReportObject.setId((Integer) data.get("id"));
                    return actionReportObject;
                }).map((actionReportObject) -> {
                    handlerActionReportObjects.add(actionReportObject);
                    return actionReportObject;
                }).forEachOrdered((actionReportObject) -> {
                    LOG.debug("Workgroup added: {}", actionReportObject.getWorkgroup());
                });
                
            } else if (isClaimOwnershipEnabled) { // this condition can be replaced with just else {} but having isClaimOwnershipEnabled check ensure this report will not work when both workgroup and claimOwner disabled.
                HandlerActionsReportObject workflowReportObject = new HandlerActionsReportObject();
                handlerActionReportObjects.add(workflowReportObject);
                LOG.debug("Empty Workgroup added.");
            }

            for (HandlerActionsReportObject obj : handlerActionReportObjects) {
                LOG.debug("Getting members of workgroup: {}",
                        obj.getWorkgroup());
                HashMap queryParameters = new HashMap();
                StringBuilder sb = new StringBuilder();
                if (isWorkgroupEnabled && isClaimOwnershipEnabled && selectedOwnerId == -1) {
                    // Workgroup enabled, no Claim Owner selected
                    queryParameters.put("pWorkgroupId", obj.getId());
                    LOG.debug("Added to parameter map: {}={}", "pWorkgroupId", obj.getId());
                    sb.append("select w.name as workgroup, u.id as id, u.first_name || ' ' || u.last_name as name, ")
                      .append("u.last_name from web_user u, web_user_workgroup wuw, workgroup w, web_user_role wur, ")
                      .append("web_user_user_role wuur where wuw.workgroup_id = :pWorkgroupId and u.id = wuw.user_id ")
                      .append("and w.id = wuw.workgroup_id and wuur.web_user_id = u.id and wuur.web_user_role_id=wur.id ")
                      .append("and wur.name='ROLE_INS_CH' and u.status = true ");
                        // Add any ex-members of workgroup that had claims assigned to them during the period
                    sb.append("union ")
                      .append("select w.name as workgroup, u.id as id, u.first_name || ' ' || u.last_name || ' (*)' as name, ")
                      .append("u.last_name from web_user u, workgroup w, claim c ")
                      .append("where w.id = :pWorkgroupId and c.workgroup_id = :pWorkgroupId ")
                      .append("and c.created_date between :pStartDate and :pEndDate ")
                      .append("and c.claim_owner_id = u.id and not exists ")
                      .append("(select * from web_user_workgroup where workgroup_id = :pWorkgroupId and user_id = c.claim_owner_id) ");
                    queryParameters.put("pStartDate", startDate);
                    queryParameters.put("pEndDate", endDate);
                } else if (isWorkgroupEnabled && isClaimOwnershipEnabled) {
                    // Workgroup and Claim Owner selected
                    queryParameters.put("pWorkgroupId", obj.getId());
                    queryParameters.put("pOwnerId", selectedOwnerId);
                    LOG.debug("Added to parameter map: {}={}", "pWorkgroupId", obj.getId());
                    sb.append("select w.name as workgroup, u.id as id, u.first_name || ' ' || u.last_name as name, u.last_name from web_user u, web_user_workgroup wuw, workgroup w where wuw.workgroup_id = :pWorkgroupId and u.id = wuw.user_id and w.id = wuw.workgroup_id ");
                    sb.append("and u.id = :pOwnerId ");
                    if (selectedWorkgroupId == -1) {
                        // Add workgroups no longer a member of but worked on during period
                        sb.append("union ")
                          .append("select w.name || ' (*)' as workgroup, u.id as id, u.first_name || ' ' || u.last_name as name, ")
                          .append("u.last_name from web_user u, workgroup w, claim c ")
                          .append("where w.id = :pWorkgroupId and c.workgroup_id = :pWorkgroupId ")
                          .append("and c.created_date between :pStartDate and :pEndDate ")
                          .append("and c.claim_owner_id = u.id and u.id = :pOwnerId and not exists ")
                          .append("(select * from web_user_workgroup where workgroup_id = :pWorkgroupId and user_id = c.claim_owner_id) ");
                        queryParameters.put("pStartDate", startDate);
                        queryParameters.put("pEndDate", endDate);
                    }
                } else if (isWorkgroupEnabled) { // 
                    queryParameters.put("pWorkgroupId", obj.getId());
                    sb.append("select w.name as workgroup from workgroup w where w.id = :pWorkgroupId ");
                    LOG.debug("Added to parameter map: {}={}", "pWorkgroupId",
                            obj.getId());
                } else if (isClaimOwnershipEnabled) { // this condition can be replaced with just else {} but having isClaimOwnershipEnabled check ensure this report will not work when both workgroup and claimOwner disabled.
                    queryParameters.put("pInsurerId", insurerId);
                    LOG.debug("Added to parameter map: {}={}", "pInsurerId",
                            insurerId);
                    sb.append("select u.id as id, u.first_name || ' ' || u.last_name as name, u.last_name from web_user u, web_user_role wur, web_user_user_role wuur where u.insurer_id = :pInsurerId and wuur.web_user_id = u.id and wuur.web_user_role_id=wur.id and wur.name='ROLE_INS_CH'");
                    if (selectedOwnerId != -1) {
                        queryParameters.put("pOwnerId", selectedOwnerId);
                        LOG.debug("Added to parameter map: {}={}", "pOwnerId", selectedOwnerId);
                        sb.append("and u.id = :pOwnerId ");
                    }
                }

                if (isClaimOwnershipEnabled) {
                    sb.append("order by last_name");
                } 
                
                LOG.debug("Querying for users with: {}", sb.toString());
                List result = reportDataService.getReportData(sb.toString(),
                        queryParameters);
                LOG.debug("Got {} results", result.size());
                boolean first = true;
                for (Object o : result) {
                    Map data = (Map) o;
                    if (!first) {
                        data.remove("workgroup");
                    } else {
                        first = false;
                    }
                    HandlerActionsStatusLineItem handlerActionItem = HandlerActionsStatusLineItem.getObject(data);
//                    LOG.debug("Getting stats for user: {}", handlerActionItem.getName());
                    // Now construct query to get claim owner stats
                    sb = new StringBuilder();
                    sb.append("select ");

                    /*
                     * No of new handler actions ClaimUnacknowledgedRouted
                     */
                    
                    sb.append("(select count(*) from claim c, audit_trail a where c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    if (selectedCHOId > 0) {
                        sb.append("and c.chorganisation_id = :pChoId ");
                    }
                    if (isClaimOwnershipEnabled) {
                        sb.append("and c.claim_owner_id = :pOwnerId ");
                    }
                    sb.append("and c.insurer_id = :pInsurerId ")
                        .append("and ((a.new_status='ClaimUnacknowledgedRouted' ")
                        .append("and a.created_date between :pStartDate and :pEndDate ) ")
                        .append("or (a.original_status='ClaimUnacknowledgedRouted' ") 
                        .append("and a.last_modified_date between :pStartDate and :pEndDate ")
                        .append("and a.reverted = true))) as countClaimUnacknowledgedRouted, " );

                    /*
                     * No of new handler actions ClaimPending
                     */
                    sb.append("(select count(*) from claim c, audit_trail a where c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    if (selectedCHOId > 0) {
                        sb.append("and c.chorganisation_id = :pChoId ");
                    }
                    if (isClaimOwnershipEnabled) {
                        sb.append("and c.claim_owner_id = :pOwnerId ");
                    }
                    sb.append("and c.insurer_id = :pInsurerId ")
                        .append("and ((a.new_status='ClaimPending' ")
                        .append("and a.created_date between :pStartDate and :pEndDate ) ")
                        .append("or (a.original_status='ClaimPending' ") 
                        .append("and a.last_modified_date between :pStartDate and :pEndDate ")
                        .append("and a.reverted = true))) as countClaimPending, " );

                    /*
                     * No of new handler actions ClaimRejectionContested
                     */
                    sb.append("(select count(*) from claim c, audit_trail a where c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    if (selectedCHOId > 0) {
                        sb.append("and c.chorganisation_id = :pChoId ");
                    }
                    if (isClaimOwnershipEnabled) {
                        sb.append("and c.claim_owner_id = :pOwnerId ");
                    }
                    sb.append("and c.insurer_id = :pInsurerId ")
                        .append("and ((a.new_status='ClaimRejectionContested' ")
                        .append("and a.created_date between :pStartDate and :pEndDate ) ")
                        .append("or (a.original_status='ClaimRejectionContested' ") 
                        .append("and a.last_modified_date between :pStartDate and :pEndDate ")
                        .append("and a.reverted = true))) as countClaimRejectionContested, " );
                    
                    /*
                     * No of new handler actions ClaimUpdatedByEngineer
                     */
                    sb.append("(select count(*) from claim c, audit_trail a where c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    if (selectedCHOId > 0) {
                        sb.append("and c.chorganisation_id = :pChoId ");
                    }
                    if (isClaimOwnershipEnabled) {
                        sb.append("and c.claim_owner_id = :pOwnerId ");
                    }
                    sb.append("and c.insurer_id = :pInsurerId ")
                        .append("and ((a.new_status='ClaimUpdatedByEngineer' ")
                        .append("and a.created_date between :pStartDate and :pEndDate ) ")
                        .append("or (a.original_status='ClaimUpdatedByEngineer' ") 
                        .append("and a.last_modified_date between :pStartDate and :pEndDate ")
                        .append("and a.reverted = true))) as countClaimUpdatedByEngineer, " );

                    /*
                     * No of new handler actions InvoiceEscalatedToHandler
                     */
                    sb.append("(select count(*) from claim c, audit_trail a where c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    if (selectedCHOId > 0) {
                        sb.append("and c.chorganisation_id = :pChoId ");
                    }
                    if (isClaimOwnershipEnabled) {
                        sb.append("and c.claim_owner_id = :pOwnerId ");
                    }
                    sb.append("and c.insurer_id = :pInsurerId ")
                        .append("and ((a.new_status='InvoiceEscalatedToHandler' ")
                        .append("and a.created_date between :pStartDate and :pEndDate ) ")
                        .append("or (a.original_status='InvoiceEscalatedToHandler' ") 
                        .append("and a.last_modified_date between :pStartDate and :pEndDate ")
                        .append("and a.reverted = true))) as countInvoiceEscalatedToHandler, " );

                    /*
                     * No of new handler actions
                     * ContestedInvoiceReferredToInsurer
                     */
                    sb.append("(select count(*) from claim c, audit_trail a where c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    if (selectedCHOId > 0) {
                        sb.append("and c.chorganisation_id = :pChoId ");
                    }
                    if (isClaimOwnershipEnabled) {
                        sb.append("and c.claim_owner_id = :pOwnerId ");
                    }
                    sb.append("and c.insurer_id = :pInsurerId ")
                        .append("and ((a.new_status='ContestedInvoiceReferredToInsurer' ")
                        .append("and a.created_date between :pStartDate and :pEndDate ) ")
                        .append("or (a.original_status='ContestedInvoiceReferredToInsurer' ") 
                        .append("and a.last_modified_date between :pStartDate and :pEndDate ")
                        .append("and a.reverted = true))) as countContestedInvoiceReferredToInsurer, " );

                    /*
                     * No of new handler actions InvoiceApprovedByBRE
                     */
                    sb.append("(select count(*) from claim c, audit_trail a where c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    if (selectedCHOId > 0) {
                        sb.append("and c.chorganisation_id = :pChoId ");
                    }
                    if (isClaimOwnershipEnabled) {
                        sb.append("and c.claim_owner_id = :pOwnerId ");
                    }
                    sb.append("and c.insurer_id = :pInsurerId ")
                        .append("and ((a.new_status='InvoiceApprovedByBRE' ")
                        .append("and a.created_date between :pStartDate and :pEndDate ) ")
                        .append("or (a.original_status='InvoiceApprovedByBRE' ") 
                        .append("and a.last_modified_date between :pStartDate and :pEndDate ")
                        .append("and a.reverted = true))) as countInvoiceApprovedByBRE, " );

                    /*
                     * No of new handler actions AwaitingLiabilityResolution
                     */
                    sb.append("(select count(*) from claim c, audit_trail a where c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    if (selectedCHOId > 0) {
                        sb.append("and c.chorganisation_id = :pChoId ");
                    }
                    if (isClaimOwnershipEnabled) {
                        sb.append("and c.claim_owner_id = :pOwnerId ");
                    }
                    sb.append("and c.insurer_id = :pInsurerId ")
                        .append("and ((a.new_status='AwaitingLiabilityResolution' ")
                        .append("and a.created_date between :pStartDate and :pEndDate ) ")
                        .append("or (a.original_status='AwaitingLiabilityResolution' ") 
                        .append("and a.last_modified_date between :pStartDate and :pEndDate ")
                        .append("and a.reverted = true))) as countAwaitingLiabilityResolution, " );

                    /*
                     * No of new handler actions AwaitingInvoicePayment
                     */
                    sb.append("(select count(*) from claim c, audit_trail a where c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    if (selectedCHOId > 0) {
                        sb.append("and c.chorganisation_id = :pChoId ");
                    }
                    if (isClaimOwnershipEnabled) {
                        sb.append("and c.claim_owner_id = :pOwnerId ");
                    }
                    sb.append("and c.insurer_id = :pInsurerId ")
                        .append("and ((a.new_status='AwaitingInvoicePayment' ")
                        .append("and a.created_date between :pStartDate and :pEndDate ) ")
                        .append("or (a.original_status='AwaitingInvoicePayment' ") 
                        .append("and a.last_modified_date between :pStartDate and :pEndDate ")
                        .append("and a.reverted = true))) as countAwaitingInvoicePayment " );
                    
                    if (isInsurerInvoiceUploadEnabled) {

                        /*
                         * No of new handler actions ManualInvoiceBREApproved
                         */
                        sb.append(", (select count(*) from claim c, audit_trail a where c.id=a.claim_id ");
                        if (isWorkgroupEnabled) {
                            sb.append("and c.workgroup_id = :pWorkgroupId ");
                        }
                        if (selectedCHOId > 0) {
                            sb.append("and c.chorganisation_id = :pChoId ");
                        }
                        if (isClaimOwnershipEnabled) {
                            sb.append("and c.claim_owner_id = :pOwnerId ");
                        }
                        sb.append("and c.insurer_id = :pInsurerId ")
                                .append("and ((a.new_status='ManualInvoiceBREApproved' ")
                                .append("and a.created_date between :pStartDate and :pEndDate ) ")
                                .append("or (a.original_status='ManualInvoiceBREApproved' ")
                                .append("and a.last_modified_date between :pStartDate and :pEndDate ")
                                .append("and a.reverted = true))) as countManualInvoiceBREApproved, ");

                        /*
                         * No of new handler actions ManualInvoiceBRERejected
                         */
                        sb.append("(select count(*) from claim c, audit_trail a where c.id=a.claim_id ");
                        if (isWorkgroupEnabled) {
                            sb.append("and c.workgroup_id = :pWorkgroupId ");
                        }
                        if (selectedCHOId > 0) {
                            sb.append("and c.chorganisation_id = :pChoId ");
                        }
                        if (isClaimOwnershipEnabled) {
                            sb.append("and c.claim_owner_id = :pOwnerId ");
                        }
                        sb.append("and c.insurer_id = :pInsurerId ")
                                .append("and ((a.new_status='ManualInvoiceBRERejected' ")
                                .append("and a.created_date between :pStartDate and :pEndDate ) ")
                                .append("or (a.original_status='ManualInvoiceBRERejected' ")
                                .append("and a.last_modified_date between :pStartDate and :pEndDate ")
                                .append("and a.reverted = true))) as countManualInvoiceBRERejected, ");

                        /*
                         * No of new handler actions ManualInvoiceContested
                         */
                        sb.append("(select count(*) from claim c, audit_trail a where c.id=a.claim_id ");
                        if (isWorkgroupEnabled) {
                            sb.append("and c.workgroup_id = :pWorkgroupId ");
                        }
                        if (selectedCHOId > 0) {
                            sb.append("and c.chorganisation_id = :pChoId ");
                        }
                        if (isClaimOwnershipEnabled) {
                            sb.append("and c.claim_owner_id = :pOwnerId ");
                        }
                        sb.append("and c.insurer_id = :pInsurerId ")
                                .append("and ((a.new_status='ManualInvoiceContested' ")
                                .append("and a.created_date between :pStartDate and :pEndDate ) ")
                                .append("or (a.original_status='ManualInvoiceContested' ")
                                .append("and a.last_modified_date between :pStartDate and :pEndDate ")
                                .append("and a.reverted = true))) as countManualInvoiceContested ");

                    }
                    
                    queryParameters = new HashMap();
                    if (isWorkgroupEnabled) {
                        queryParameters.put("pWorkgroupId", obj.getId());
                    }
                    if (selectedCHOId > 0) {
                        queryParameters.put("pChoId", selectedCHOId);
                    }
                    if (isClaimOwnershipEnabled) {
                        queryParameters.put("pOwnerId", handlerActionItem.getId());
                    }
                    
                    queryParameters.put("pInsurerId", currentUser.getInsurer().getId());
                    queryParameters.put("pStartDate", startDate);
                    queryParameters.put("pEndDate", endDate);
                    
                    List detailData = reportDataService.getReportData(sb.toString(), queryParameters);
                    // parse query results and add to workflowLineItem
                    if (detailData.size() > 0) {
                        handlerActionItem.updateObject((Map) detailData.get(0), isInsurerInvoiceUploadEnabled);
                        if (handlerActionItem.getWorkgroup().endsWith("(*)") || handlerActionItem.getName().endsWith("(*)")) {
                            hasStarred++;
                        }
                        obj.getOwner().add(handlerActionItem);
                    }
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            // Now build report parameters
            if (selectedCHOId > 0) {
                reportParameters.put("CHOName", getChorganisation(selectedCHOId).getName());
            }
            else {
                reportParameters.put("CHOName", "ALL");
            }
            reportParameters.put("insurerName", currentUser.getInsurer().getName());
            reportParameters.put("createdDate", DateHelper.getCurrentDateWithFormat("dd/MM/yyyy HH:mm:ss"));
            reportParameters.put("hasStarred", hasStarred);
            reportParameters.put("startDate", sdf.format(startDate));
            reportParameters.put("endDate", sdf.format(endDate));
            reportParameters.put("handlerActionLineItems", handlerActionReportObjects);
        } catch (Exception ex) {
            LOG.debug("Error thrown generating 'New Handler Actions' report: {}", ex.getMessage());
            throw ex;
        }

        return reportParameters;
    }

    @Override
    public String getReportTemplateFileName() {
//        return "template_NewIncomingHandlerWorkgroupAndOwnerActionsReport.xls";
        WebUser user = ((WebUser) externalParameter.get("CurrentUser"));
        if (isInsurerInvoiceUploadEnabled) {
            if (user.getInsurer().isWorkgroupEnable()) {
                return "template_NewIncomingHandlerWorkgroupAndOwnerActionsReport.xls";
            } else {
                return "template_NewIncomingHandlerOwnerOnlyActionsReport.xls";
            }
        } else {
            if (user.getInsurer().isWorkgroupEnable()) {
                return "template_NewIncomingHandlerWorkgroupAndOwnerActionsReport_InsurerInvoiceUploadDisabled.xls";
            } else {
                return "template_NewIncomingHandlerOwnerOnlyActionsReport_InsurerInvoiceUploadDisabled.xls";
            }
        }
    }

    @Override
    public ByteArrayOutputStream build() throws Exception {
        ReportBuilder builder = new ExcelReportBuilder();
        return builder.buildReport(this);
    }

    @Override
    public String getReportCode() {
        return "RPT058";
    }

    private Chorganisation getChorganisation(int orgId) {

        Chorganisation chorg = new Chorganisation();

        try {
            DetachedCriteria criteria = DetachedCriteria
                    .forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("id", orgId));
            chorg = (Chorganisation) baseDataService.getByCriteria(criteria);

        } catch (Exception e) {
            LOG.error("Error getting Chorganisation for id={}: {}", orgId,
                    e.getMessage());
        }

        return chorg;
    }

    private Insurer getInsurer(int orgId) {
        Insurer ins = new Insurer();

        try {

            DetachedCriteria criteria = DetachedCriteria
                    .forClass(Insurer.class);
            criteria.add(Restrictions.eq("id", orgId));
            ins = (Insurer) baseDataService.getByCriteria(criteria);

        } catch (Exception e) {
            LOG.error("Error getting Insurer for id={}: {}", orgId,
                    e.getMessage());
        }

        return ins;
    }

    private Workgroup getWorkgroup(int workgroupId) {
        Workgroup wg = new Workgroup();

        try {

            DetachedCriteria criteria = DetachedCriteria
                    .forClass(Workgroup.class);
            criteria.add(Restrictions.eq("id", workgroupId));
            wg = (Workgroup) baseDataService.getByCriteria(criteria);

        } catch (Exception e) {
            LOG.error("Error getting workgroup for id={}: {}", workgroupId,
                    e.getMessage());
        }

        return wg;
    }

    private WebUser getClaimOwner(int ownerId) {
        WebUser wu = new WebUser();
        try {
            DetachedCriteria criteria = DetachedCriteria
                    .forClass(WebUser.class);
            criteria.add(Restrictions.eq("id", ownerId));
            wu = (WebUser) baseDataService.getByCriteria(criteria);
        } catch (Exception e) {
            LOG.error("Error getting workgroup for id={}: {}", ownerId,
                    e.getMessage());
        }
        return wu;
    }
    
    @Override
    public short[] getColumnsToHide() {
        /*
         * please note there is 2 template used for this report eventhough we
         * can dynamically disable the column because when disable workgroup
         * column which hides other variable defined on that column eg. chox
         * logo and cho,insurer,created date parameter. So we are forced to use
         * 2 different template. Further investigation needed to work around. 
         */
        short[] columnsToHide = null;
        ArrayList<Short> columnsToHideList = new ArrayList<>();
        WebUser user = ((WebUser) externalParameter.get("CurrentUser"));
        if (user.getInsurer().isWorkgroupEnable() && user.getInsurer().isClaimOwnershipEnable()) { // if both claimownership and workgroup enabled
            if (!user.getInsurer().isEngineersEnable()) {
                columnsToHideList.add((short) 6);
            }
        } else if (user.getInsurer().isWorkgroupEnable()) { // if claimownership not enabled
            if (user.getInsurer().isEngineersEnable()) {
                columnsToHideList.add((short) 2);
            } else {
                columnsToHideList.add((short) 2);
                columnsToHideList.add((short) 6);
            }
        } else if (user.getInsurer().isClaimOwnershipEnable()) { // if workgroup not enabled 
            if (!user.getInsurer().isEngineersEnable()) {
                columnsToHideList.add((short) 5);
            }
        }
        
        if (!isInsurerInvoiceUploadEnabled) {
            if (user.getInsurer().isWorkgroupEnable()) {
                columnsToHideList.add((short) 12);
                columnsToHideList.add((short) 13);
                columnsToHideList.add((short) 14);
            } else {
                columnsToHideList.add((short) 11);
                columnsToHideList.add((short) 12);
                columnsToHideList.add((short) 13);
            }
        }
        if (columnsToHideList.size() > 0) {
            columnsToHide = ArrayUtils.toPrimitive(columnsToHideList.toArray(new Short[columnsToHideList.size()]));
        }
        
        return columnsToHide;
    }

    @Override
    public boolean isBrandingReportFormat() {
        return externalParameter.get("isBrandingReport")==null ? false : (Boolean)externalParameter.get("isBrandingReport");
    }

}
