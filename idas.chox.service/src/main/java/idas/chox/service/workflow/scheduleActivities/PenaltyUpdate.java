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
import idas.chox.service.workflow.ActivityFactory;

/**
 *
 * @author john
 */
public class PenaltyUpdate extends BaseScheduleActivity {

    private static final Logger LOG = LoggerFactory.getLogger(PenaltyUpdate.class);
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
                if (cells.size() < 1) {
                    //ignore row
                    LOG.debug("Ignoring row {} - only has {} cells.", row, cells.size());
                    continue;
                }
                String choReference = cells.get(0).trim();

                if (choReference != null && !choReference.isEmpty()) {
                    ((SecureDataService)claimService).setSecurityInfoProvider(((SecureDataService)claimService).getSecurityInfoProvider());
                    boolean isUpdateSuccessful = claimService.setPenaltyStartToDateInvoiced(choReference);
                    if (isUpdateSuccessful) {
                        LOG.debug("Penalty Start Date updated for CHO reference '{}'", choReference);
                        if (xlsDataMap.get(row).size() == 1) {
                            xlsDataMap.get(row).add("Updated");
                        } else {
                            xlsDataMap.get(row).set(1, "Updated");
                        }
                    } else {
                        LOG.debug("Error updating Penalty Start Date for claim: {} ", choReference);
                        if (xlsDataMap.get(row).size() == 1) {
                            xlsDataMap.get(row).add("Failed");
                        } else {
                            xlsDataMap.get(row).set(1, "Failed");
                        }
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
            emailMsg.append("CHO Reference               Status\n");
            emailMsg.append("----------------------------------\n");
            Set<Integer> rowNumbers = xlsDataMap.keySet();
            // This is specific for the excel file with two columns and
            // first row is a header.
            // We don't do update on first line and we assume we will always
            // have only two columns.
            rowNumbers.stream().filter((row) -> (row != 0)).map((row) -> xlsDataMap.get(row)).filter((cells) -> (cells.size() >= 2)).map((cells) -> {
                // We expect at least two columns
                emailMsg.append(cells.get(0).trim()).append("\t\t\t");
                return cells;
            }).forEachOrdered((cells) -> {
                emailMsg.append(cells.get(1).trim()).append("\n");
            });
  
        } else {
            emailMsg.append("No attachement on email, please check and re-submit.\n");
            emailMsg.append("-----------------------------------------------------------------------------------------------\n");
        }
        LOG.debug("Message to send is: \n*********\n{}\n*********", emailMsg.toString());
        return emailMsg.toString();
    }


}
