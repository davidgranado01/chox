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
import idas.chox.core.util.DateHelper;

public class ReferenceUpdateDbSchedulerJob extends DbSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(ReferenceUpdateDbSchedulerJob.class);
    public static final String JOB_NAME = "DB_REFERENCE_UPDATE";


    @Secured({"ROLE_CHO"})
    @Override
    public final Map<Integer, List<String>> doJob() {

        Map<Integer, List<String>> xlsDataMap = new HashMap<>();

        List<QueuedTicket> queuedTickets = claimService.getQueuedTicket();

        try {
            if (queuedTickets.size() > 0) {
                LOG.debug("total found QueuedTicket is {}", queuedTickets.size());
                // Start new transaction?
                handleHibernateTransactionIntricacies();

                int i = 1;
                for (QueuedTicket queuedTicket : queuedTickets) {

                    int status = claimService.updateQueuedTicket(queuedTicket,
                            getSecurityInfoProvider().getCurrentUser().getChorganisation().getId());
                    String statusString;
                    switch (status) {
                        case 0:
                            statusString = "Updated";
                            break;
                        case 1:
                            statusString = "Failed - Ticket number already exists";
                            break;
                        case 2:
                            statusString = "Failed - Reservation number doesn't exist";
                            break;
                        case 3:
                            statusString = "Failed - Reservation number doesn't exist (but Ticket number does)";
                            break;
                        default:
                            statusString = "Failed - an internal error occurred";
                            break;
                    }
                    List<String> cellStringList = new ArrayList<>();
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
                releaseHibernateSessionConditionally();

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
                if (row != 0) {
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