package idas.chox.web.scheduler;

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
import idas.chox.core.util.DateHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.workflow.activities.EcdUpdate;


public class ECDUpdateSchedulerJob extends EmailSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(ECDUpdateSchedulerJob.class);
    
    private ActivityFactory activityFactory;
    private String REG_ALPHANUMERIC = "^([\\d]|[a-z]|[A-Z]).*$";
    public static final String JOB_NAME = "ECD_UPDATE";
        
    @Secured({"ROLE_CHO", "ROLE_CHOX_ADMIN"})
    @Override
    protected Map<Integer, List<String>> doJob(Map<Integer, List<String>> xlsDataMap, String sender) {
        Set<Integer> rowNumbers = xlsDataMap.keySet();

        for (Integer row : rowNumbers) {
            // first row is header
            if (row.intValue() != 0) { // ignore first row - should contain header
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
                Date ecdDate = validateEcdDate(ecdDateString, statusString);

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
                        Activity activity = (EcdUpdate) activityFactory.getActivity("ecdUpdate");
                        ((EcdUpdate)activity).setEcdDate(ecdDate);
                        ((EcdUpdate)activity).setReason(ecdDelayReason);
                        ((EcdUpdate)activity).setSupportingNote(ecdDelaySuppNote);
                        ((EcdUpdate)activity).setUpdateInsurer(true);
                        activity.process(claim);
                        statusString.append("Success: Updated.");
                    } catch (AccessDeniedException ex) {
                        statusString.append("Failed: No Access to ECD Update Activity (Invalid Claim Status)");
                        LOG.warn("AccessDenied Exception thrown when adding new ECD via email scheduler ecd update job", ex);
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
                    if (cells.size() >= 4) { // We expect at least three columns
                        emailMsg.append(String.format("%-22s", cells.get(0).trim()));
                        emailMsg.append("    ");
                        emailMsg.append(cells.get(4).trim());
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
            claim = getClaimService().getClaimByCHOReferenceNumber(referenceNumber);

            if (claim == null) {
                LOG.debug("No Such Claim Reference {}", referenceNumber);
                statusString.append(" No Such Claim Reference.");
            }
        }
        return claim;
    }
    
    private Date validateEcdDate(String ecdDateString, StringBuilder statusString) {
        Date ecdDate = null;
        SimpleDateFormat sdf = DateHelper.getLocalDateFormat();
        sdf.setLenient(false);
        if (ecdDateString.isEmpty()) {
            statusString.append(" No ECD Date Provided.");
        } else if (ecdDateString.length() != sdf.toPattern().length()) {
            statusString.append(" Invalid Format For ECD Date.");
        } else {
            try {
                ecdDate = sdf.parse(ecdDateString);
            } catch (ParseException ex) {
                statusString.append(" Invalid Format For ECD Date.");
                LOG.warn("parse exception thrown for given date {}", ecdDateString, ex);
            }
        }
        return ecdDate;
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
    protected List<SchedulerJob> getEmailSchedulerJobs() {
        return getSchedulerJobService().getSchedulerJobs(JOB_NAME);
    }
}
