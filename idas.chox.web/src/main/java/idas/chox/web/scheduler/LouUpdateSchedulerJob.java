package idas.chox.web.scheduler;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.Claim;
import idas.chox.core.model.SchedulerJob;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.workflow.activities.LouUpdate;


public class LouUpdateSchedulerJob extends ExcelEmailSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(LouUpdateSchedulerJob.class);
    
    private ActivityFactory activityFactory;
    private String REG_ALPHANUMERIC = "^([\\d]|[a-z]|[A-Z]).*$";
    public static final String JOB_NAME = "LOU_UPDATE";
    private ClaimService claimService;

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }


    @Secured({"ROLE_CHO", "ROLE_CHOX_ADMIN"})
    @Override
    protected Map<Integer, List<String>> doJob(Map<Integer, List<String>> xlsDataMap, String sender) {
        Set<Integer> rowNumbers = xlsDataMap.keySet();

        for (Integer row : rowNumbers) {
            // first row is header
            if (row.intValue() != 0) { // ignore first row - should contain header
                boolean update = false;
                List<String> cells = xlsDataMap.get(row);

                if (cells.size() < 1) {
                    //ignore row
                    LOG.info("Ignoring row {} - only has {} cells.", row, cells.size());
                    continue;
                }
                
                Activity activity = (LouUpdate) activityFactory.getActivity("louUpdate");
                StringBuilder statusString = new StringBuilder();

                /* Check is valid referenceNumber provided */
                String referenceNumber = cells.get(0).trim();
                Claim claim = validateClaimReferenceNumber(referenceNumber, statusString);
                
                /* Check name of Repairer is valid */
                if (cells.size() > 1 && !cells.get(1).isEmpty()) {
                    ((LouUpdate)activity).setRepairerName(cells.get(1).trim());
                    update = true;
                }

                /* Check Inspection booked date provided is valid and parse the string date to java date */
                if (cells.size() > 2 && !cells.get(2).isEmpty()) {
                    Date inspectionBookedDate = validateDate(cells.get(2).trim(), statusString, "Inspection Booked Date");
                    if (inspectionBookedDate != null) {
                        ((LouUpdate)activity).setInspectionBookedDate(inspectionBookedDate);
                        update = true;
                    }
                }

                /* Check Inspection date provided is valid and parse the string date to java date */
                if (cells.size() > 3 && !cells.get(3).isEmpty()) {
                    Date inspectionDate = validateDate(cells.get(3).trim(), statusString, "Inspection Date");
                    if (inspectionDate != null) {
                        ((LouUpdate)activity).setInspectionDate(inspectionDate);
                        update = true;
                    }
                }

                /* Check repair authorised date provided is valid and parse the string date to java date */
                if (cells.size() > 4 && !cells.get(4).isEmpty()) {
                    Date repairAuthorisedDate = validateDate(cells.get(4).trim(), statusString, "Date Repair Authorised");
                    if (repairAuthorisedDate != null) {
                        ((LouUpdate)activity).setRepairAuthorisedDate(repairAuthorisedDate);
                        update = true;
                    }
                }

                /* Check repair booked-in date provided is valid and parse the string date to java date */
                if (cells.size() > 5 && !cells.get(5).isEmpty()) {
                    Date repairBookedInDate = validateDate(cells.get(5).trim(), statusString, "Repair Book-In Date");
                    if (repairBookedInDate != null) {
                        ((LouUpdate)activity).setRepairBookedInDate(repairBookedInDate);
                        update = true;
                    }
                }

                /* Check repair commenced date provided is valid and parse the string date to java date */
                if (cells.size() > 6 && !cells.get(6).isEmpty()) {
                    Date repairCommencedDate = validateDate(cells.get(6).trim(), statusString, "Date Repair Commenced");
                    if (repairCommencedDate != null) {
                        ((LouUpdate)activity).setRepairCommencedDate(repairCommencedDate);
                        update = true;
                    }
                }

                /* Check repair completion date provided is valid and parse the string date to java date */
                if (cells.size() > 7 && !cells.get(7).isEmpty()) {
                    Date repairCompletionDate = validateDate(cells.get(7).trim(), statusString, "Repair Completion Date");
                    if (repairCompletionDate != null) {
                        ((LouUpdate)activity).setRepairCompletionDate(repairCompletionDate);
                        update = true;
                    }
                }

                /* Check 'is total loss' provided is valid */
                if (cells.size() > 8 && !cells.get(8).isEmpty()) {
                    boolean isTotalLoss = validateYesNoColumn(cells.get(8).trim(), statusString, "Is Total Loss?");
                    ((LouUpdate)activity).setTotalLoss(isTotalLoss);
                    update = true;
                }

                /* Check total loss offer made date provided is valid and parse the string date to java date */
                if (cells.size() > 9 && !cells.get(9).isEmpty()) {
                    Date totalLossMadeDate = validateDate(cells.get(9).trim(), statusString, "Date Total Loss Offer Made");
                    if (totalLossMadeDate != null) {
                        ((LouUpdate)activity).setTotalLossMadeDate(totalLossMadeDate);
                        update = true;
                    }
                }

                /* Check total loss offer accepted date provided is valid and parse the string date to java date */
                if (cells.size() > 10 && !cells.get(10).isEmpty()) {
                    Date totalLossAcceptedDate = validateDate(cells.get(10).trim(), statusString, "Date Total Loss Offer Accepted");
                    if (totalLossAcceptedDate != null) {
                        ((LouUpdate)activity).setTotalLossAcceptedDate(totalLossAcceptedDate);
                        update = true;
                    }
                }

                /* Check total loss offer issued date provided is valid and parse the string date to java date */
                if (cells.size() > 11 && !cells.get(11).isEmpty()) {
                    Date totalLossIssuedDate = validateDate(cells.get(11).trim(), statusString, "Date Total Loss Cheque Issued");
                    if (totalLossIssuedDate != null) {
                        ((LouUpdate)activity).setTotalLossIssuedDate(totalLossIssuedDate);
                        update = true;
                    }
                }

                /* Check total loss offer received date provided is valid and parse the string date to java date */
                if (cells.size() > 12 && !cells.get(12).isEmpty()) {
                    Date totalLossReceivedDate = validateDate(cells.get(12).trim(), statusString, "Date Total Loss Cheque Received");
                    if (totalLossReceivedDate != null) {
                        ((LouUpdate)activity).setTotalLossReceivedDate(totalLossReceivedDate);
                        update = true;
                    }
                }

                /* Check Name of IME provided is valid */
                if (cells.size() > 13 && !cells.get(13).isEmpty()) {
                    ((LouUpdate)activity).setImeName(cells.get(13).trim());
                    update = true;
                }

                /* Check Labour Rate provided is valid */
                if (cells.size() > 14 && !cells.get(14).isEmpty()) {
                    BigDecimal labourRate = validateNumeric(cells.get(14).trim(), statusString, "Labour Rate (Per Hour)");
                    if (labourRate != null) {
                        ((LouUpdate)activity).setLabourRate(labourRate);
                        update = true;
                    }
                }
                /* Check Labour Hours provided is valid */
                if (cells.size() > 15 && !cells.get(15).isEmpty()) {
                    BigDecimal labourHours = validateNumeric(cells.get(15).trim(), statusString, "Labour Hours");
                    if (labourHours != null) {
                        ((LouUpdate)activity).setLabourHours(labourHours);
                        update = true;
                    }
                }

                /* Check Labour Cost provided is valid */
                if (cells.size() > 16 && !cells.get(16).isEmpty()) {
                    BigDecimal labourCost = validateNumeric(cells.get(16).trim(), statusString, "Total Labour Cost");
                    if (labourCost != null) {
                        ((LouUpdate)activity).setLabourCost(labourCost);
                        update = true;
                    }
                }


                /* Check Non Provision Reason provided is valid */
                if (cells.size() > 17 && !cells.get(17).isEmpty()) {
                    String npr = cells.get(17).trim();
                    if (npr.equals("Point Blank Refusal")
                            || npr.equals("Faxed Garage")
                            || npr.equals("Information Not Available/No System Access")
                            || npr.equals("Non Contactable/Ring Through")
                            || npr.equals("Update Obtained By Other Source")) {
                        ((LouUpdate)activity).setNonProvisionReason(npr);
                        update = true;
                    } else {
                        statusString.append(" Invalid 'Labour Information Non-Provision Reason' Provided.");
                    }
                }

                /* Check 'Repair Only (No Hire)?' column is valid, if present */
                if (cells.size() > 18 && !cells.get(18).isEmpty()) {
                    boolean repairOnly = validateYesNoColumn(cells.get(18).trim(), statusString, "Repair Only (No Hire)?");
                    ((LouUpdate)activity).setRepairOnly(repairOnly);
                }

                /* Check 'CHO Managing Repair?' column is valid, if present */
                if (cells.size() > 19 && !cells.get(19).isEmpty()) {
                    boolean choManagingRepair = validateYesNoColumn(cells.get(19).trim(), statusString, "CHO Managing Repair?");
                    ((LouUpdate)activity).setChoManagingRepair(choManagingRepair);
                }

                /* Check 'Non-Fault Insurer Managing Repair?' column is valid, if present */
                if (cells.size() > 20 && !cells.get(20).isEmpty()) {
                    boolean insurerManagingRepair = validateYesNoColumn(cells.get(20).trim(), statusString, "Non-Fault Insurer Managing Repair?");
                    ((LouUpdate)activity).setInsurerManagingRepair(insurerManagingRepair);
                }

                /* Check 'Is Your Client VAT Registered?' column is valid, if present */
                if (cells.size() > 21 && !cells.get(21).isEmpty()) {
                    boolean vatRegistered = validateYesNoColumn(cells.get(21).trim(), statusString, "Is Your Client VAT Registered?");
                    ((LouUpdate)activity).setVatRegistered(vatRegistered);
                }

                /* Check update insurer column is valid, if present */
                if (cells.size() > 22 && !cells.get(22).isEmpty()) {
                    boolean updateInsurer = validateYesNoColumn(cells.get(22).trim(), statusString, "Update Insurer");
                    ((LouUpdate)activity).setUpdateInsurer(updateInsurer);
                }

                /* If validation passed add the new hire monitoring ECD.*/
                if (statusString.toString().isEmpty() && update) {
                    try {
                        activity.process(claim);
                        statusString.append("Success: Updated.");
                    } catch (AccessDeniedException ex) {
                        statusString.append("Failed: No Access to Hire Update Activity (Invalid Claim Status '")
                                .append(claim.getStatus()).append("')");
                        LOG.warn("AccessDenied Exception thrown when updating Hire Start via email scheduler job");
                    } catch (Exception ex) {
                        statusString.append("Failed: An Internal Error Occurred");
                        LOG.warn("Exception occurred when updating hire start via email scheduler job", ex);
                    }
                } else if (statusString.toString().isEmpty() && !update) {
                    statusString.insert(0, "Nothing to update.");
                } else {
                    statusString.insert(0, "Failed: ");
                }

                /* update the result message into column 24 for each row.*/
                for (int i=cells.size(); i < 23; i++) {
                    cells.add("dummy column");
                }
                if (cells.size() == 23) {
                    cells.add(statusString.toString());
                } else {
                    cells.set(23, statusString.toString());
                }
            }
        }
        return xlsDataMap;
    }

    @Override
    protected String buildMessage(String email, String subject, Map<Integer, List<String>> xlsDataMap) {
        
        StringBuilder emailMsg = new StringBuilder();
        emailMsg.append("======================================================================\n");
        emailMsg.append("Submitted By Email: ").append(email).append("\n");
        emailMsg.append("Date: ").append(DateHelper.getCurrentDateWithFormat(email_date_format)).append("\n");
        emailMsg.append("Subject: ").append(subject).append("\n");
        emailMsg.append("======================================================================\n\n");
        if (xlsDataMap != null) {
            emailMsg.append("Reference Number           Message\n");
            emailMsg.append("-----------------------------------------------------------------------------------------------\n");
            Set<Integer> rowNumbers = xlsDataMap.keySet();

            for (Integer row : rowNumbers) {
                if (row.intValue() != 0) {
                    List<String> cells = xlsDataMap.get(row);
                    if (cells.size() >= 24) { // We expect at least twenty four columns
                        emailMsg.append(String.format("%-22s", cells.get(0).trim()));
                        emailMsg.append("\t\t");
                        emailMsg.append(cells.get(23).trim());
                        emailMsg.append("\n");
                    }
                }
            }
  
        } else {
            emailMsg.append("No xls attachement found in email, please check and re-submit.\n");
            emailMsg.append("-----------------------------------------------------------------------------------------------\n");
        }
        LOG.debug("Message to send is: \n*********\n{}\n*********", emailMsg.toString());
        return emailMsg.toString();
    }

    private Claim validateClaimReferenceNumber(String referenceNumber, StringBuilder statusString) {

        Claim claim = null;
        if (!regexExpressionChecker(REG_ALPHANUMERIC, referenceNumber)) {
            statusString.append(" No Claim Reference Provided.");
        } else {
            claim = claimService.getClaimByCHOReferenceNumber(referenceNumber);

            if (claim == null) {
                LOG.debug("No Such Claim Reference {}", referenceNumber);
                statusString.append(" No Such Claim Reference.");
            }
        }
        return claim;
    }
    
    private Date validateDate(String hireStartDateString, StringBuilder statusString, String columnName) {
        Date hireStartDate = null;
        SimpleDateFormat sdf = DateHelper.getLocalDateFormat();
        sdf.setLenient(false);
        if (hireStartDateString.isEmpty()) {
            statusString.append(" No '").append(columnName).append("' provided.");
        } else if (hireStartDateString.length() != sdf.toPattern().length()) {
            statusString.append(" Invalid Format For '").append(columnName).append("'.");
        } else {
            try {
                hireStartDate = sdf.parse(hireStartDateString);
            } catch (ParseException ex) {
                statusString.append(" Invalid Format For '").append(columnName).append("'.");
                LOG.warn("Parse exception thrown for column {}: {}", columnName, hireStartDateString);
            }
        }
        return hireStartDate;
    }
    
    private BigDecimal validateNumeric(String valueString, StringBuilder statusString, String column) {
        BigDecimal value = null;
        
        if (valueString != null && !valueString.isEmpty()) {
            try {
                value = new BigDecimal(valueString);
            } catch (Exception ex) {
                statusString.append(" Invalid Format For '").append(column).append("'.");
            }
        }
        return value;
    }


    private boolean validateYesNoColumn(String updateInsurerString, StringBuilder statusString, String column) {
        boolean updateInsurer = false;
        
        if (updateInsurerString != null && !updateInsurerString.isEmpty()) {
            if (updateInsurerString.trim().equalsIgnoreCase("y") || updateInsurerString.trim().equalsIgnoreCase("yes")) {
                updateInsurer = true;
            } else if (!updateInsurerString.trim().equalsIgnoreCase("n") && !updateInsurerString.trim().equalsIgnoreCase("no")) {
                statusString.append(" Invalid Format for '").append(column).append("'.");
            }
        }
        return updateInsurer;
    }

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }
    
    private boolean regexExpressionChecker(String regex, String dataValue) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(dataValue);

        if (!m.find()) {
            LOG.debug("Invalid data for regex '{}': {}", regex, dataValue);
            return false;
        }
        return true;
    }

    @Override
    protected List<SchedulerJob> getSchedulerJobs() {
        return getSchedulerJobService().getSchedulerJobs(JOB_NAME);
    }


}
