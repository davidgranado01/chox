package idas.chox.web.scheduler;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.security.access.annotation.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionException;
import idas.chox.core.util.DateHelper;
import idas.chox.core.model.QueuedTicket;

public class ReferenceUpdateDbSchedulerJob extends DbSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(ReferenceUpdateDbSchedulerJob.class);

    private String queuedTicketUpdateReceivers;

    @Override
    public final void execute() throws JobExecutionException {
        try {
// TODO: investigate why we cannot access properties directly - if we do this we get null values
//            LOG.debug("queuedTicketUpdateReceivers: {}", queuedTicketUpdateReceivers);
//            LOG.debug("queuedTicketUpdateReceivers: {}", getQueuedTicketUpdateReceivers());
            LOG.debug("properties accessed using getters :{}, {}, {}, {}, {}, {}, {}, {}, {}, {}", 
                    new Object[]{getBccReceivers(), getEmailSubject(), getSmtpHostName(), getSmtpPort(),
                            getSmtpEmailUser(), getSmtpEmailPassword(), getErrorMessageReceivers(),
                            getUpdateUserName(),getUpdatePassword(),getQueuedTicketUpdateReceivers()});
            super.execute();
            List<QueuedTicket> queuedTickets = getClaimService().getQueuedTicket();
            if (queuedTickets.size() > 0) {
                    LOG.debug("total found QueuedTicket is {}", queuedTickets.size());
                    Map<Integer, List<String>> resultMap = doJob(queuedTickets);
                    String emailMessage = buildMessage(getEmailSubject(), resultMap);
                    sendMail(getQueuedTicketUpdateReceivers(), getBccReceivers(), "RE: " + getEmailSubject(), emailMessage.toString());
            } else {
                LOG.debug("no queuedTickets found");
            }
        } catch (Exception ex) {
            LOG.error("exception on ReferenceUpdateDbSchedulerJob", ex);
        }
    }

    @Secured({"ROLE_CHO"})
    @Override
    public final Map<Integer, List<String>> doJob(List<QueuedTicket> queuedTickets) {
        
        Map<Integer, List<String>> xlsDataMap = new HashMap<Integer, List<String>>();
        int i = 1;
        for (QueuedTicket queuedTicket : queuedTickets) {

            int status = getClaimService().updateQueuedTicket(queuedTicket,
                                    getSecurityInfoProvider().getCurrentUser().getChorganisation().getId());
            String statusString = null;
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
        return xlsDataMap;
    }

    public final String buildMessage(String subject, Map<Integer, List<String>> xlsDataMap) {
        StringBuilder emailMsg = new StringBuilder();
        emailMsg.append("======================================================================\n");
        emailMsg.append("Subject: Queued Tokens Update Results.").append("\n");
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

        }
        LOG.debug("Message to send is: \n*********\n{}\n*********", emailMsg.toString());
        return emailMsg.toString();
    }

    public void setQueuedTicketUpdateReceivers(String queuedTicketUpdateReceivers) {
        this.queuedTicketUpdateReceivers = queuedTicketUpdateReceivers;
    }
    public String getQueuedTicketUpdateReceivers() {
        return queuedTicketUpdateReceivers;
    }

}