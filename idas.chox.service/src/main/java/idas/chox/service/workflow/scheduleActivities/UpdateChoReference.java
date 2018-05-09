package idas.chox.service.workflow.scheduleActivities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.EmailAttachment;
import idas.chox.core.util.DateHelper;
import idas.chox.data.services.SecureDataService;

/**
 *
 * @author john
 */
public class UpdateChoReference extends BaseScheduleActivity {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateChoReference.class);
    private final List<String> statusMessages = new ArrayList<>();
    private Map<Integer, List<String>> xlsDataMap;
    private XlsFileParser xlsFileParser;

    public void setXlsFileParser(XlsFileParser xlsFileParser) {
        this.xlsFileParser = xlsFileParser;
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
                        int status = claimService.updateReservationToTicket(oldReference, newReference, ((SecureDataService)claimService).getSecurityInfoProvider().getCurrentUser().getChorganisation().getId(), from);
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
                            case 4:
                                statusString = "Failed - Ticket number already exists for Linked CHO";
                                break;
                            default:
                                statusString = "Failed - an internal error occurred";
                                break;
                        }

                        LOG.debug("CHO reference updated: {} -> {} : {} [{}]", new Object[]{oldReference, newReference, statusString, ((SecureDataService)claimService).getSecurityInfoProvider().getCurrentUser().getChorganisation().getId()});
                        if (xlsDataMap.get(row).size() < 3) {
                            xlsDataMap.get(row).add(statusString);
                        } else {
                            xlsDataMap.get(row).set(2, statusString);
                        }

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
            emailMsg.append("Original CHO Reference    New CHO Reference    Status\n");
            emailMsg.append("-----------------------------------------------------------------------------------------------\n");
            Set<Integer> rowNumbers = xlsDataMap.keySet();
            // This is specific for the excel file with two columns and
            // first row is a header.
            // We don't do update on first line and we assume we will always
            // have only two columns.
            rowNumbers.stream().filter((row) -> (row != 0)).map((row) -> xlsDataMap.get(row)).filter((cells) -> (cells.size() >= 3)).map((cells) -> {
                // We expect at least three columns
                emailMsg.append(String.format("%-22s", cells.get(0).trim()));
                return cells;
            }).map((cells) -> {
                emailMsg.append("    ");
                emailMsg.append(String.format("%-17s", cells.get(1).trim()));
                return cells;
            }).map((cells) -> {
                emailMsg.append("    ");
                emailMsg.append(cells.get(2).trim());
                return cells;
            }).forEachOrdered((_item) -> {
                emailMsg.append("\n");
            });

        } else {
            emailMsg.append("No attachement on email, please check and re-submit.\n");
            emailMsg.append("-----------------------------------------------------------------------------------------------\n");
        }
        LOG.debug("Message to send is: \n*********\n{}\n*********", emailMsg.toString());

        return emailMsg.toString();
    }

}
