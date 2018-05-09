package idas.chox.service.workflow.scheduleActivities;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.EmailAttachment;
import idas.chox.core.util.DateHelper;

/**
 *
 * @author john
 */
public class TotalLossStopChaseTask extends BaseScheduleActivity {

    private static final Logger LOG = LoggerFactory.getLogger(TotalLossStopChaseTask.class);
    private final StringBuilder statusString = new StringBuilder();
    
    @Override
    public boolean process(String body, List<EmailAttachment> attachments, String from, String subject) throws Exception {
        // Subject is:  IMS TL Stop Chase Request: <ERAC FNOL reference number>
        //   - extract the cho ref number
        int i = subject.indexOf("Request:");
        if (i<1) { // lets try without the 
            i = subject.indexOf("Request")-1;
        }
        String choRef = subject.substring(i+8).trim();
        
        Claim claim = validateClaimReferenceNumber(choRef, statusString);
        if (claim != null) {
            String result = claimService.stopClaimChase(choRef);
            statusString.append(result);
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
        emailMsg.append(statusString);
        
        return emailMsg.toString();

    }
}
