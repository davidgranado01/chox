package idas.chox.web.scheduler;


import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.SchedulerJob;
import idas.chox.core.util.DateHelper;

public class ReferenceUpdateEmailSchedulerJob extends ExcelEmailSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(ReferenceUpdateEmailSchedulerJob.class);
    public static final String JOB_NAME = "REFERENCE_UPDATE";

    @Secured({"ROLE_CHO"})
    @Override
    protected Map<Integer, List<String>> doJob(Map<Integer, List<String>> xlsDataMap, String sender) {
        Set<Integer> rowNumbers = xlsDataMap.keySet();
        // This is specific for the excel file with two columns and
        // first row is a header.
        // We don't do update on first line and we assume we will always
        // have only two columns.
        for (Integer row : rowNumbers) {
            // first row is header
            if (row != 0) { // ignore first row - should contain header
                List<String> cells = xlsDataMap.get(row);
                // this excel file should have at least two columns and we
                // iterate only through those two
                if (cells.size() < 2) {
                    //ignore row
                    LOG.debug("Ignoring row {} - only has {} cells.", row, cells.size());
                    continue;
                }
                String oldReference = cells.get(0).trim();
                String newReference = cells.get(1).trim();

                if (oldReference != null && !oldReference.isEmpty() && newReference != null && !newReference.isEmpty()) {
                    int status = claimService.updateReservationToTicket(oldReference, newReference, getSecurityInfoProvider().getCurrentUser().getChorganisation().getId(),sender);
                    String statusString;
                    if (status == 0) {
                        statusString = "Updated";
                    }
                    else if (status == 1) {
                        statusString = "Failed - Ticket number already exists";
                    }
                    else if (status == 2) {
                        statusString = "Failed - Reservation number doesn't exist";
                    }
                    else if (status == 3) {
                        statusString = "Failed - Reservation number doesn't exist (but Ticket number does)";
                    }
                    else if (status == 4) {
                        statusString = "Failed - Ticket number already exists for Linked CHO";
                    }
                    else {
                        statusString = "Failed - an internal error occurred";
                    }
                    
                
                    LOG.debug("CHO reference updated: {} -> {} : {} [{}]", new Object[]{oldReference, newReference, statusString, getSecurityInfoProvider().getCurrentUser().getChorganisation().getId()});
                    if (xlsDataMap.get(row).size() < 3) {
                        xlsDataMap.get(row).add(statusString);
                    } else {
                        xlsDataMap.get(row).set(2, statusString);
                    }

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
        emailMsg.append("Date: ").append(DateHelper.getCurrentDateWithFormat(EMAIL_DATE_FORMAT)).append("\n");
        emailMsg.append("Subject: ").append(subject).append("\n");
        emailMsg.append("======================================================================\n\n");
        if (xlsDataMap != null) {
            emailMsg.append("Original CHO Reference    New CHO Reference    Status\n");
            emailMsg.append("-----------------------------------------------------------------------------------------------\n");
            Set<Integer> rowNumbers = xlsDataMap.keySet();
            // This is specific for the excel file with two columns and
            // first row is a header.
            // We don't do update on first line and we assume we will always
            // have only two columns.
            for (Integer row : rowNumbers) {
                if (row != 0) {
                    List<String> cells = xlsDataMap.get(row);
                    if (cells.size() >= 3) { // We expect at least three columns
                        emailMsg.append(String.format("%-22s", cells.get(0).trim()));
                        emailMsg.append("    ");
                        emailMsg.append(String.format("%-17s", cells.get(1).trim()));
                        emailMsg.append("    ");
                        emailMsg.append(cells.get(2).trim());
                        emailMsg.append("\n");
                    }
                }
            }
  
        } else {
            emailMsg.append("No attachement on email, please check and re-submit.\n");
            emailMsg.append("-----------------------------------------------------------------------------------------------\n");
        }
        LOG.debug("Message to send is: \n*********\n{}\n*********", emailMsg.toString());
        return emailMsg.toString();
    }

    @Override
    protected List<SchedulerJob> getSchedulerJobs() {
        return getSchedulerJobService().getSchedulerJobs(JOB_NAME);
    }
}