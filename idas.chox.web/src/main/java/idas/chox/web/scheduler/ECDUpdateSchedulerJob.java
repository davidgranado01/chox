package idas.chox.web.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.services.HireMonitoringEcdService;
import idas.chox.core.services.SchedulerPrivilegedUserService;
import idas.chox.core.util.DateHelper;


public class ECDUpdateSchedulerJob extends EmailSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(ECDUpdateSchedulerJob.class);
    private SchedulerPrivilegedUserService schedulerPrivilegedUserService;
    private HireMonitoringEcdService hireMonitoringEcdService;
    private SimpleDateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");
    private String REG_ALPHANUMERIC = "^([\\d]|[a-z]|[A-Z]).*$";
    

    @Override
    public void execute() throws JobExecutionException {
       
        try {
            handleHibernateTransactionIntricacies();
            super.setPrivilegedUsers(schedulerPrivilegedUserService.getECDUpdatePrivilegedUsers());
            super.execute();
        } catch (Exception ex) {
            LOG.error("exception thrown when processing ECD Update Sheduler job.", ex);
        } finally {
            releaseHibernateSessionConditionally();
        }

    }
    
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
                Claim claim = null;
                StringBuilder statusString = new StringBuilder();

                String referenceNumber = cells.get(0).trim();

                if (!regexExpressionChecker(REG_ALPHANUMERIC, referenceNumber)) {
                    statusString.append(" No Claim Reference Provided.");
                } else {
                    claim = getClaimService().getClaimByCHOReferenceNumber(referenceNumber);
                    boolean isValidStatus = false;

                    if (claim == null) {
                        LOG.debug("No Such Claim Reference {}", referenceNumber);
                        statusString.append(" No Such Claim Reference.");
                    } else {
                        for (String status : ClaimStatus.getPreInvoiceStatus()) {
                            if (!claim.getStatus().equals(status)) {
                                isValidStatus = true;
                                break;
                            }
                        }
                        if (!isValidStatus) {
                            statusString.append(" Invalid Claim Status '").append(claim.getStatus()).append("'.");
                        }
                    }
                }
                
                Date ecdDate = null;
                if (cells.get(1).trim().isEmpty()) {
                    statusString.append(" No ECD Date Provided.");
                } else {
                    try {
                        ecdDate = dateFormate.parse(cells.get(1).trim());
                    } catch (ParseException ex) {
                        statusString.append(" Invalid Format For ECD Date.");
                        LOG.error("parse exception thrown for given date {}", cells.get(1).trim(), ex);
                    }
                }

                String ecdDelayReason = cells.get(2).trim();
                if (!regexExpressionChecker(REG_ALPHANUMERIC, ecdDelayReason)) {
                    statusString.append(" No ECD Reason Provided.");
                }

                String ecdDelaySuppNote = cells.get(3).trim();
                if (!regexExpressionChecker(REG_ALPHANUMERIC, ecdDelaySuppNote)) {
                    statusString.append(" No Supporting Note Provided.");
                }

                if (statusString.toString().isEmpty()) {
                    try {
                        HireMonitoringEcd ecd = new HireMonitoringEcd();
                        ecd.setEcdDate(ecdDate);
                        ecd.setReason(ecdDelayReason);
                        ecd.setSupportingNote(ecdDelaySuppNote);
                        LOG.debug("ecd date {} ecd reason {} ecd supportnote {}", new Object[]{ecd.getEcdDate().toString(), ecd.getReason(), ecd.getSupportingNote()});
                        hireMonitoringEcdService.addNewHireMonitoringEcd(claim, ecd, true);
                        statusString.append("Success: Updated.");
                    } catch (Exception ex) {
                        statusString.append(" An Internal Error Occurred.");
                        LOG.error("Exception occured when adding new ECD via email scheduler ecd update job", ex);
                    }
                } else {
                    statusString.insert(0, "Failed:");
                }

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
        emailMsg.append("Subject: ").append(getEmailSubject()).append("\n");
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
  
        }
        LOG.debug("Message to send is: \n*********\n{}\n*********", emailMsg.toString());
        return emailMsg.toString();
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
    

    public void setSchedulerPrivilegedUserService(SchedulerPrivilegedUserService schedulerPrivilegedUserService) {
        this.schedulerPrivilegedUserService = schedulerPrivilegedUserService;
    }

    public void setHireMonitoringEcdService(HireMonitoringEcdService hireMonitoringEcdService) {
        this.hireMonitoringEcdService = hireMonitoringEcdService;
    }

    
}
