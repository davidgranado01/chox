package idas.chox.web.scheduler;


import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.DateHelper;
import org.springframework.security.access.annotation.Secured;

public class PenaltyChargeUpdateJob extends BaseUpdateJob {

    private static final Logger LOG = LoggerFactory.getLogger(PenaltyChargeUpdateJob.class);
    private static final String email_date_format = "dd MMMM yyyy";
    private ClaimService claimService;

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Secured({"ROLE_CHOX_ADMIN"})
    @Override
    protected Map<Integer, List<String>> doJob(Map<Integer, List<String>> xlsDataMap) {
        String referenceNumber = null;
        Set<Integer> rowNumbers = xlsDataMap.keySet();
        // This is specific for the excel file with two columns and
        // first row is a header.
        // We don't do update on first line and we assume we will always
        // have only two columns.
        for (Integer row : rowNumbers) {
            // first row is header
            if (row.intValue() != 0) { // ignore first row - should contain header
                List<String> cells = xlsDataMap.get(row);
                // this excel file should have at least two columns and we
                // iterate only through those two
                if (cells.size() < 1) {
                    //ignore row
                    LOG.debug("Ignoring row {} - only has {} cells.", row, cells.size());
                    continue;
                }
                String choReference = cells.get(0).trim();

                if (choReference != null && !choReference.equals("")) {
                    referenceNumber = choReference;
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
            emailMsg.append("CHO Reference               Status\n");
            emailMsg.append("----------------------------------\n");
            Set<Integer> rowNumbers = xlsDataMap.keySet();
            // This is specific for the excel file with two columns and
            // first row is a header.
            // We don't do update on first line and we assume we will always
            // have only two columns.
            for (Integer row : rowNumbers) {
                if (row.intValue() != 0) {
                    List<String> cells = xlsDataMap.get(row);
                    if (cells.size() >= 2) { // We expect at least two columns
                        emailMsg.append(cells.get(0).trim()).append("\t\t\t");
                        emailMsg.append(cells.get(1).trim()).append("\n");
                    }
                }
            }
  
        }
        LOG.debug("Message to send is: \n*********\n{}\n*********", emailMsg.toString());
        return emailMsg.toString();
    }
}