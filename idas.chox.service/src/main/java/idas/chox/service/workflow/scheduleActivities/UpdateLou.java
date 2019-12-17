package idas.chox.service.workflow.scheduleActivities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.EmailAttachment;
import idas.chox.core.util.DateHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.workflow.activities.LouUpdate;

/**
 *
 * @author john
 */
public class UpdateLou extends BaseScheduleActivity {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateLou.class);
    private final List<String> statusMessages = new ArrayList<>();
    private Map<Integer, List<String>> xlsDataMap;
    private ActivityFactory activityFactory;
    private XlsFileParser xlsFileParser;


    public void setXlsFileParser(XlsFileParser xlsFileParser) {
        this.xlsFileParser = xlsFileParser;
    }

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }

    @Override
    public boolean process(String body, List<EmailAttachment> attachments, String from, String subject) throws Exception {

        for (EmailAttachment attachment : attachments) {
            if (!attachment.getName().toLowerCase().endsWith("xls")) {
                LOG.debug("Incorrect attachment type found: '{}'", attachment.getName());
                continue;
            }
            xlsDataMap = xlsFileParser.processExcelFile(attachment.getContent());

            Set<Integer> rowNumbers = xlsDataMap.keySet();

            for (Integer row : rowNumbers) {
                // first row is header
                if (row != 0) { // ignore first row - should contain header
                    boolean update = false;
                    boolean repairDatesUpdated = false;
                    List<String> cells = xlsDataMap.get(row);

                    if (cells.size() < 1) {
                        //ignore row
                        LOG.debug("Ignoring row {} - only has {} cells.", row, cells.size());
                        continue;
                    }

                    // Ignore empty rows
                    if (cells.get(0).trim().isEmpty() && ((cells.size() > 1 && cells.get(1).trim().isEmpty()) || cells.size() <= 1) 
                            && ((cells.size() > 2 && cells.get(2).trim().isEmpty()) || cells.size() <= 2)
                            && ((cells.size() > 3 && cells.get(3).trim().isEmpty()) || cells.size() <= 3)
                            && ((cells.size() > 4 && cells.get(4).trim().isEmpty()) || cells.size() <= 4)
                            && ((cells.size() > 5 && cells.get(5).trim().isEmpty()) || cells.size() <= 5)
                            && ((cells.size() > 6 && cells.get(6).trim().isEmpty()) || cells.size() <= 6)
                            && ((cells.size() > 7 && cells.get(7).trim().isEmpty()) || cells.size() <= 7)
                            && ((cells.size() > 8 && cells.get(8).trim().isEmpty()) || cells.size() <= 8)
                            && ((cells.size() > 9 && cells.get(9).trim().isEmpty()) || cells.size() <= 9)
                            && ((cells.size() > 10 && cells.get(10).trim().isEmpty()) || cells.size() <= 10)
                            && ((cells.size() > 11 && cells.get(11).trim().isEmpty()) || cells.size() <= 11)
                            && ((cells.size() > 12 && cells.get(12).trim().isEmpty()) || cells.size() <= 12)
                            && ((cells.size() > 13 && cells.get(13).trim().isEmpty()) || cells.size() <= 13)
                            && ((cells.size() > 14 && cells.get(14).trim().isEmpty()) || cells.size() <= 14)
                            && ((cells.size() > 15 && cells.get(15).trim().isEmpty()) || cells.size() <= 15)
                            && ((cells.size() > 16 && cells.get(16).trim().isEmpty()) || cells.size() <= 16)
                            && ((cells.size() > 17 && cells.get(17).trim().isEmpty()) || cells.size() <= 17)
                            && ((cells.size() > 18 && cells.get(18).trim().isEmpty()) || cells.size() <= 18)
                            && ((cells.size() > 19 && cells.get(19).trim().isEmpty()) || cells.size() <= 19)
                            && ((cells.size() > 20 && cells.get(20).trim().isEmpty()) || cells.size() <= 20)
                            && ((cells.size() > 21 && cells.get(21).trim().isEmpty()) || cells.size() <= 21)
                            && ((cells.size() > 22 && cells.get(22).trim().isEmpty()) || cells.size() <= 22)
                            && ((cells.size() > 23 && cells.get(23).trim().isEmpty()) || cells.size() <= 23)
                            && ((cells.size() > 24 && cells.get(24).trim().isEmpty()) || cells.size() <= 24)) {
                        LOG.debug("Ignoring empty row no. {}.", row);
                        continue;
                    }

                    Activity activity = (LouUpdate) activityFactory.getActivity("louUpdate");
                    StringBuilder statusString = new StringBuilder();

                    /* Check is valid referenceNumber provided */
                    String referenceNumber = cells.get(0).trim();
                    Claim claim = validateClaimReferenceNumber(referenceNumber, statusString);

                    /* Check name of Repairer is valid */
                    if (cells.size() > 1 && !cells.get(1).isEmpty()) {
                        String repairerName = cells.get(1).trim();
                        if (repairerName.length() > 128) {
                            statusString.append(" The 'Name of Repairer' must be 128 characters or less.");
                        } else {
                            ((LouUpdate) activity).setRepairerName(repairerName);
                            update = true;
                        }
                    }

                    /* Check Inspection booked date provided is valid and parse the string date to java date */
                    if (cells.size() > 2 && !cells.get(2).isEmpty()) {
                        Date inspectionBookedDate = validateDate(cells.get(2).trim(), statusString, "Inspection Booked Date");
                        if (inspectionBookedDate != null) {
                            ((LouUpdate) activity).setInspectionBookedDate(inspectionBookedDate);
                            update = true;
                        }
                    }

                    /* Check Inspection date provided is valid and parse the string date to java date */
                    if (cells.size() > 3 && !cells.get(3).isEmpty()) {
                        Date inspectionDate = validateDate(cells.get(3).trim(), statusString, "Inspection Date");
                        if (inspectionDate != null) {
                            ((LouUpdate) activity).setInspectionDate(inspectionDate);
                            update = true;
                        }
                    }

                    /* Check repair authorised date provided is valid and parse the string date to java date */
                    if (cells.size() > 4 && !cells.get(4).isEmpty()) {
                        Date repairAuthorisedDate = validateDate(cells.get(4).trim(), statusString, "Date Repair Authorised");
                        if (repairAuthorisedDate != null) {
                            ((LouUpdate) activity).setRepairAuthorisedDate(repairAuthorisedDate);
                            update = true;
                        }
                    }

                    /* Check repair booked-in date provided is valid and parse the string date to java date */
                    if (cells.size() > 5 && !cells.get(5).isEmpty()) {
                        Date repairBookedInDate = validateDate(cells.get(5).trim(), statusString, "Repair Book-In Date");
                        if (repairBookedInDate != null) {
                            ((LouUpdate) activity).setRepairBookedInDate(repairBookedInDate);
                            update = true;
                            repairDatesUpdated = true;
                        }
                    }

                    /* Check repair commenced date provided is valid and parse the string date to java date */
                    if (cells.size() > 6 && !cells.get(6).isEmpty()) {
                        Date repairCommencedDate = validateDate(cells.get(6).trim(), statusString, "Date Repair Commenced");
                        if (repairCommencedDate != null) {
                            ((LouUpdate) activity).setRepairCommencedDate(repairCommencedDate);
                            update = true;
                        }
                    }

                    /* Check repair completion date provided is valid and parse the string date to java date */
                    if (cells.size() > 7 && !cells.get(7).isEmpty()) {
                        Date repairCompletionDate = validateDate(cells.get(7).trim(), statusString, "Repair Completion Date");
                        if (repairCompletionDate != null) {
                            ((LouUpdate) activity).setRepairCompletionDate(repairCompletionDate);
                            update = true;
                            repairDatesUpdated = true;
                        }
                    }

                    /* Check 'is total loss' provided is valid */
                    if (cells.size() > 8 && !cells.get(8).isEmpty()) {
                        boolean isTotalLoss = validateYesNoColumn(cells.get(8).trim(), statusString, "Is Total Loss?");
                        ((LouUpdate) activity).setTotalLoss(isTotalLoss);
                        update = true;
                    }

                    /* Check total loss offer made date provided is valid and parse the string date to java date */
                    if (cells.size() > 9 && !cells.get(9).isEmpty()) {
                        Date totalLossMadeDate = validateDate(cells.get(9).trim(), statusString, "Date Total Loss Offer Made");
                        if (totalLossMadeDate != null) {
                            ((LouUpdate) activity).setTotalLossMadeDate(totalLossMadeDate);
                            update = true;
                        }
                    }

                    /* Check total loss offer accepted date provided is valid and parse the string date to java date */
                    if (cells.size() > 10 && !cells.get(10).isEmpty()) {
                        Date totalLossAcceptedDate = validateDate(cells.get(10).trim(), statusString, "Date Total Loss Offer Accepted");
                        if (totalLossAcceptedDate != null) {
                            ((LouUpdate) activity).setTotalLossAcceptedDate(totalLossAcceptedDate);
                            update = true;
                        }
                    }

                    /* Check total loss offer issued date provided is valid and parse the string date to java date */
                    if (cells.size() > 11 && !cells.get(11).isEmpty()) {
                        Date totalLossIssuedDate = validateDate(cells.get(11).trim(), statusString, "Date Total Loss Cheque Issued");
                        if (totalLossIssuedDate != null) {
                            ((LouUpdate) activity).setTotalLossIssuedDate(totalLossIssuedDate);
                            update = true;
                        }
                    }

                    /* Check total loss offer received date provided is valid and parse the string date to java date */
                    if (cells.size() > 12 && !cells.get(12).isEmpty()) {
                        Date totalLossReceivedDate = validateDate(cells.get(12).trim(), statusString, "Date Total Loss Cheque Received");
                        if (totalLossReceivedDate != null) {
                            ((LouUpdate) activity).setTotalLossReceivedDate(totalLossReceivedDate);
                            update = true;
                        }
                    }

                    /* Check Name of IME provided is valid */
                    if (cells.size() > 13 && !cells.get(13).isEmpty()) {
                        String imeName = cells.get(13).trim();
                        if (imeName.length() > 200) {
                            statusString.append(" The 'Name of IME' must be 200 characters or less.");
                        } else {
                            ((LouUpdate) activity).setImeName(imeName);
                            update = true;
                        }
                    }

                    /* Check Labour Rate provided is valid */
                    if (cells.size() > 14 && !cells.get(14).isEmpty()) {
                        BigDecimal labourRate = validateNumeric(cells.get(14).trim(), statusString, "Labour Rate (Per Hour)");
                        if (labourRate != null) {
                            ((LouUpdate) activity).setLabourRate(labourRate);
                            update = true;
                        }
                    }
                    /* Check Labour Hours provided is valid */
                    if (cells.size() > 15 && !cells.get(15).isEmpty()) {
                        BigDecimal labourHours = validateNumeric(cells.get(15).trim(), statusString, "Labour Hours");
                        if (labourHours != null) {
                            ((LouUpdate) activity).setLabourHours(labourHours);
                            update = true;
                        }
                    }

                    /* Check Labour Cost provided is valid */
                    if (cells.size() > 16 && !cells.get(16).isEmpty()) {
                        BigDecimal labourCost = validateNumeric(cells.get(16).trim(), statusString, "Total Labour Cost");
                        if (labourCost != null) {
                            ((LouUpdate) activity).setLabourCost(labourCost);
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
                            ((LouUpdate) activity).setNonProvisionReason(npr);
                            update = true;
                        } else {
                            statusString.append(" Invalid 'Labour Information Non-Provision Reason' Provided.");
                        }
                    }

                    /* Check 'Repair Only (No Hire)?' column is valid, if present */
                    if (cells.size() > 18 && !cells.get(18).isEmpty()) {
                        boolean repairOnly = validateYesNoColumn(cells.get(18).trim(), statusString, "Repair Only (No Hire)?");
                        ((LouUpdate) activity).setRepairOnly(repairOnly);
                    }

                    /* Check 'CHO Managing Repair?' column is valid, if present */
                    if (cells.size() > 19 && !cells.get(19).isEmpty()) {
                        boolean choManagingRepair = validateYesNoColumn(cells.get(19).trim(), statusString, "CHO Managing Repair?");
                        ((LouUpdate) activity).setChoManagingRepair(choManagingRepair);
                    }

                    /* Check 'Non-Fault Insurer Managing Repair?' column is valid, if present */
                    if (cells.size() > 20 && !cells.get(20).isEmpty()) {
                        boolean insurerManagingRepair = validateYesNoColumn(cells.get(20).trim(), statusString, "Non-Fault Insurer Managing Repair?");
                        ((LouUpdate) activity).setInsurerManagingRepair(insurerManagingRepair);
                    }

                    /* Check 'Is Your Client VAT Registered?' column is valid, if present */
                    if (cells.size() > 21 && !cells.get(21).isEmpty()) {
                        boolean vatRegistered = validateYesNoColumn(cells.get(21).trim(), statusString, "Is Your Client VAT Registered?");
                        ((LouUpdate) activity).setVatRegistered(vatRegistered);
                    }

                    /* Check update insurer column is valid, if present */
                    if (cells.size() > 22 && !cells.get(22).isEmpty()) {
                        boolean updateInsurer = validateYesNoColumn(cells.get(22).trim(), statusString, "Update Insurer");
                        ((LouUpdate) activity).setUpdateInsurer(updateInsurer);
                    }

                    if (cells.size() > 23 && !cells.get(23).isEmpty()) {
                        String whoIsSendingPAVifTL = cells.get(23).trim().toLowerCase();
                        // "Customers Own Insurer", "CHO" or "At Fault Insurer"
                        final String customersOwnInsurer = "Customers Own Insurer";
                        final String cho = "CHO";
                        final String atFaultInsurer = "At Fault Insurer";
                        if (customersOwnInsurer.toLowerCase().equals(whoIsSendingPAVifTL)) {
                            ((LouUpdate) activity).setWhoIsSendingPAVifTL(customersOwnInsurer);
                            update = true;
                        } else if (cho.toLowerCase().equals(whoIsSendingPAVifTL)) {
                            ((LouUpdate) activity).setWhoIsSendingPAVifTL(cho);
                            update = true;
                        } else if (atFaultInsurer.toLowerCase().equals(whoIsSendingPAVifTL)) {
                            ((LouUpdate) activity).setWhoIsSendingPAVifTL(atFaultInsurer);
                            update = true;
                        } else {
                            statusString.append(" The 'Who is sending PAV if TL?' is not recognised.");
                        }
                    }

                    if (cells.size() > 24 && !cells.get(24).isEmpty()) {
                        Date engineersReportSentDate = validateDate(cells.get(24).trim(), statusString, "Date Engineers Report Sent");
                        if (engineersReportSentDate != null) {
                            ((LouUpdate) activity).setEngineersReportSentDate(engineersReportSentDate);
                            update = true;
                        }
                    }

                    // Check Repair Completion Date is after the booked-in date
                    if (repairDatesUpdated) {
                        Date bookedIn = null;
                        Date completionDate = null;

                        if (((LouUpdate) activity).getRepairCompletionDate() != null && ((LouUpdate) activity).getRepairBookedInDate() != null) {
                            bookedIn = ((LouUpdate) activity).getRepairBookedInDate();
                            completionDate = ((LouUpdate) activity).getRepairCompletionDate();
                        } else if (((LouUpdate) activity).getRepairCompletionDate() != null) { // Booked-in date is null
                            completionDate = ((LouUpdate) activity).getRepairCompletionDate();
                            if (claim != null && claim.getHireMonitoringDetail() != null) {
                                bookedIn = claim.getHireMonitoringDetail().getRepairBookInDate();
                            }
                        } else { // Repair Completion Date is null
                            bookedIn = ((LouUpdate) activity).getRepairBookedInDate();
                            if (claim != null && claim.getHireMonitoringDetail() != null) {
                                completionDate = claim.getHireMonitoringDetail().getRepairCompletionDate();
                            }
                        }

                        if (bookedIn != null && completionDate != null && ((LouUpdate) activity).getRepairCompletionDate() != null
                                && completionDate.compareTo(bookedIn) < 0) {
                            statusString.append("The Repair Completion Date cannot be before the Repair Book In Date");
                        } else if (bookedIn != null && completionDate != null
                                && bookedIn.compareTo(completionDate) > 0) {
                            statusString.append("The Repair Book In Date cannot be after the Repair Completion Date");
                        }
                    }

                    /* If validation passed add the new hire monitoring ECD.*/
                    if (statusString.toString().isEmpty() && update && claim != null) {
                        try {
                            activity.process(claim);
                            statusString.append("Success: Updated.");
                        } catch (AccessDeniedException ex) {
                            statusString.append("Failed: No Access to Hire Update Activity (Invalid Claim Status '")
                                    .append(claim.getStatus()).append("')");
                            LOG.warn("AccessDenied Exception thrown when updating Hire Start via email scheduler job");
                        } catch (Exception ex) {
                            statusString.append("Failed: An Internal Error Occurred.");
                            LOG.warn("Exception occurred when updating hire start via email scheduler job: {}", ex.getMessage());
                        }
                    } else if (statusString.toString().isEmpty() && !update && claim != null) {
                        statusString.insert(0, "Nothing to update.");
                    } else if (claim == null) {
                        statusString.insert(0, "Failed - No Such Claim: ");
                    } else {
                        statusString.insert(0, "Failed: ");
                    }

                    /* update the result message into column 24 for each row.*/
                    for (int i = cells.size(); i < 25; i++) {
                        cells.add("dummy column");
                    }
                    if (cells.size() == 25) {
                        cells.add(statusString.toString());
                    } else {
                        cells.set(25, statusString.toString());
                    }
                }
            }
        }

        return true;
    }

    @Override
    public String getResponse(String subject, String from) {
        StringBuilder emailMsg = new StringBuilder();
        
        emailMsg.append("======================================================================\n");
        emailMsg.append("Submitted By Email: ").append(from).append("\n");
        emailMsg.append("Date: ").append(DateHelper.getCurrentDateWithFormat(EMAIL_DATE_FORMAT)).append("\n");
        emailMsg.append("Subject: ").append(subject).append("\n");
        emailMsg.append("======================================================================\n\n");
        if (xlsDataMap != null) {
            emailMsg.append("Reference Number           Message\n");
            emailMsg.append("-----------------------------------------------------------------------------------------------\n");
            Set<Integer> rowNumbers = xlsDataMap.keySet();

            rowNumbers.stream().filter((row) -> (row != 0)).map((row) -> xlsDataMap.get(row)).filter((cells) -> (cells.size() >= 26)).map((cells) -> {
                // We expect at least twenty four columns
                emailMsg.append(String.format("%-22s", cells.get(0).trim()));
                return cells;
            }).map((cells) -> {
                emailMsg.append("\t\t");
                emailMsg.append(cells.get(25).trim());
                return cells;
            }).forEachOrdered((_item) -> {
                emailMsg.append("\n");
            });
  
        } else {
            emailMsg.append("No xls attachement found in email, please check and re-submit.\n");
            emailMsg.append("-----------------------------------------------------------------------------------------------\n");
        }
        LOG.debug("Message to send is: \n*********\n{}\n*********", emailMsg.toString());
        return emailMsg.toString();
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

}
