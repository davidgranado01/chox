package idas.chox.web.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.QueuedTicket;
import idas.chox.core.model.SchedulerJob;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.DateHelper;

public class ReferenceUpdateDbSchedulerJob extends DbSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(ReferenceUpdateDbSchedulerJob.class);
    public static final String JOB_NAME = "DB_REFERENCE_UPDATE";
    private ClaimService claimService;

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }


    @Secured({"ROLE_CHO"})
    @Override
    public final Map<Integer, List<String>> doJob() {

        Map<Integer, List<String>> xlsDataMap = new HashMap<Integer, List<String>>();

        List<QueuedTicket> queuedTickets = claimService.getQueuedTicket();

        try {
            if (queuedTickets.size() > 0) {
                LOG.debug("total found QueuedTicket is {}", queuedTickets.size());

                int i = 1;
                for (QueuedTicket queuedTicket : queuedTickets) {

                    int status = claimService.updateQueuedTicket(queuedTicket,
                            getSecurityInfoProvider().getCurrentUser().getChorganisation().getId());
                    String statusString;
                    if (status == 0) {
                        statusString = "Updated";
                    } else if (status == 1) {
                        statusString = "Failed - Ticket number already exists";
                    } else if (status == 2) {
                        statusString = "Failed - Reservation number doesn't exist";
                    } else if (status == 3) {
                        statusString = "Failed - Reservation number doesn't exist (but Ticket number does)";
                    } else {
                        statusString = "Failed - an internal error occurred";
                    }
                    List<String> cellStringList = new ArrayList<String>();
                    cellStringList.add(queuedTicket.getOldReference());
                    cellStringList.add(queuedTicket.getNewReference());
                    cellStringList.add(statusString);
                    cellStringList.add(queuedTicket.getSender());
                    cellStringList.add(DateHelper.getSdf().format(queuedTicket.getCreatedDate()));
                    xlsDataMap.put(i++, cellStringList);
                    LOG.debug("CHO reference updated: {} -> {} : {} [{}]",
                            new Object[]{queuedTicket.getOldReference(), queuedTicket.getNewReference(),
                                statusString, getSecurityInfoProvider().getCurrentUser().getChorganisation().getId()});
                }

            } else {
                LOG.debug("no queuedTickets found");
            }
        } catch (Exception ex) {
            LOG.error("exception on ReferenceUpdateDbSchedulerJob", ex);
        }
        return xlsDataMap;
    }

    @Override
    public String buildMessage(String subject, Map<Integer, List<String>> xlsDataMap) {
        StringBuilder emailMsg = new StringBuilder();
        emailMsg.append("======================================================================\n");
        emailMsg.append("Subject: ").append(subject).append("\n");
        emailMsg.append("======================================================================\n\n");
        if (xlsDataMap != null) {
            emailMsg.append("Date Added    Sender                                 Original CHO Reference    New CHO Reference    Status\n");
            emailMsg.append("---------------------------------------------------------------------------------------------------------------------------------------------\n");
            Set<Integer> rowNumbers = xlsDataMap.keySet();
            // This is specific for the excel file with two columns and
            // first row is a header.
            // We don't do update on first line and we assume we will always
            // have only two columns.
            for (Integer row : rowNumbers) {
                if (row.intValue() != 0) {
                    List<String> cells = xlsDataMap.get(row);
                    if (cells.size() >= 3) { // We expect at least three columns
                        emailMsg.append(String.format("%-10s", cells.get(4).trim()));
                        emailMsg.append("    ");
                        emailMsg.append(String.format("%-35s", cells.get(3).trim()));
                        emailMsg.append("    ");
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