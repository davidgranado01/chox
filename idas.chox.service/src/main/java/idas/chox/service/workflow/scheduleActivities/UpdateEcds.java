package idas.chox.service.workflow.scheduleActivities;

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

/**
 *
 * @author john
 */
public class UpdateEcds extends BaseScheduleActivity {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateEcds.class);
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
            if (!attachment.getName().endsWith("xls")) {
                LOG.debug("Incorrect attachment type found: '{}'", attachment.getName());
                continue;
            }
            xlsDataMap = xlsFileParser.processExcelFile(attachment.getContent());

            Set<Integer> rowNumbers = xlsDataMap.keySet();

            for (Integer row : rowNumbers) {
                // first row is header
                if (row != 0) { // ignore first row - should contain header
                    List<String> cells = xlsDataMap.get(row);

                    if (cells.size() < 4) {
                        //ignore row
                        LOG.info("Ignoring row {} - only has {} cells.", row, cells.size());
                        continue;
                    }

                    StringBuilder statusString = new StringBuilder();

                    /* Check is valid referenceNumber provided and claim is in valid status.*/
                    String referenceNumber = cells.get(0).trim();
                    Claim claim = validateClaimReferenceNumber(referenceNumber, statusString);

                    /* Check is valid ecdDate provided and parse the string date to java date.*/
                    String ecdDateString = cells.get(1).trim();
                    // if date contains a time component then remove
                    if (ecdDateString.matches("(.*)[0-1][0-9]:[0-5][0-9]")) {
                        ecdDateString = ecdDateString.substring(0, ecdDateString.length() - 6);
                    }

                    Date ecdDate = validateDate(ecdDateString, statusString, "ECD Date");

                    /* Check is valid ecdDelayReason provided and it has valid length(<=50 character).*/
                    String ecdDelayReason = cells.get(2).trim();
                    validateEcdDelayReason(ecdDelayReason, statusString);

                    /* Check is valid ecdDelaySuppNote provided.*/
                    String ecdDelaySuppNote = cells.get(3).trim();
                    validateEcdDelaySupportNote(ecdDelaySuppNote, statusString);

                    /* If validation passed add the new hire monitoring ECD.*/
                    if (statusString.toString().isEmpty()) {
                        try {
                            LOG.debug("ecd date {} ecd reason {} ecd supportnote {}", new Object[]{ecdDate.toString(), ecdDelayReason, ecdDelaySuppNote});
                            Activity activity = (idas.chox.service.workflow.activities.EcdUpdate) activityFactory.getActivity("ecdUpdate");
                            ((idas.chox.service.workflow.activities.EcdUpdate) activity).setEcdDate(ecdDate);
                            ((idas.chox.service.workflow.activities.EcdUpdate) activity).setReason(ecdDelayReason);
                            ((idas.chox.service.workflow.activities.EcdUpdate) activity).setSupportingNote(ecdDelaySuppNote);
                            ((idas.chox.service.workflow.activities.EcdUpdate) activity).setUpdateInsurer(true);
                            activity.process(claim);
                            statusString.append("Success: Updated.");
                        } catch (AccessDeniedException ex) {
                            statusString.append("Failed: No Access to ECD Update Activity (Invalid Claim Status)");
                            LOG.warn("AccessDenied Exception thrown when adding new ECD via email scheduler ecd update job");
                        } catch (Exception ex) {
                            if (ex.getMessage().equals("ECD Update Already Exists")) {
                                statusString.append("Failed: ECD Update Already Exists");
                            } else {
                                statusString.append("Failed: An Internal Error Occurred");
                                LOG.warn("Exception occurred when adding new ECD via email scheduler ecd update job", ex);
                            }
                        }
                    } else {
                        statusString.insert(0, "Failed:");
                    }

                    /* update the result message into column 5 for each row.*/
                    if (xlsDataMap.get(row).size() < 5) {
                        xlsDataMap.get(row).add(statusString.toString());
                    } else {
                        xlsDataMap.get(row).set(4, statusString.toString());
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

            rowNumbers.stream().filter((row) -> (row != 0)).map((row) -> xlsDataMap.get(row)).filter((cells) -> (cells.size() >= 4)).map((cells) -> {
                // We expect at least three columns
                emailMsg.append(String.format("%-22s", cells.get(0).trim()));
                return cells;
            }).map((cells) -> {
                emailMsg.append("    ");
                emailMsg.append(cells.get(4).trim());
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
    
    
    private void validateEcdDelayReason(String ecdDelayReason, StringBuilder statusString) {
        if (ecdDelayReason.isEmpty()) {
            statusString.append(" No ECD Delay Reason Provided.");
        } else if (!regexExpressionChecker(REG_ALPHANUMERIC, ecdDelayReason)) {
            statusString.append(" ECD Delay Reason Must Start With An Alpha-numeric Character.");
        } else if (ecdDelayReason.length() > 50) {
                statusString.append(" ECD Delay Reason Exceeds The Maximum Allowed Length Of 50 Characters.");
        }
    }
    
    private void validateEcdDelaySupportNote(String ecdDelaySuppNote, StringBuilder statusString) {
        if (ecdDelaySuppNote.isEmpty()) {
            statusString.append(" No Supporting Note Provided.");
        } else if (!regexExpressionChecker(REG_ALPHANUMERIC, ecdDelaySuppNote)) {
            statusString.append(" ECD Supporting Note Must Start With An Alpha-numeric Character.");
        }
    }


}
