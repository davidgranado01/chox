package idas.chox.service.workflow.scheduleActivities;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import idas.chox.core.model.Claim;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.workflow.ScheduleActivity;
import idas.chox.core.workflow.WorkflowContext;
import idas.chox.data.services.SecureDataService;
import idas.chox.service.workflow.event.ActivityEventGenerator;

/**
 *
 * @author john
 */
public abstract class BaseScheduleActivity implements ScheduleActivity {
    private static final Logger LOG = LoggerFactory.getLogger(BaseScheduleActivity.class);
    protected static final String EMAIL_DATE_FORMAT = "dd MMMM yyyy";
    protected static final String REG_ALPHANUMERIC = "^([\\d]|[a-z]|[A-Z]).*$";
    private static final String REG_TIME = "^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])?$";
//    private WorkflowContext processContext;
    @Autowired
    protected ActivityEventGenerator activityEventGenerator;
    protected ClaimService claimService;

    
    public void setActivityEventGenerator(ActivityEventGenerator activityEventGenerator) {
        this.activityEventGenerator = activityEventGenerator;
    }

    
    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    
//    public void setWorkflowContext(WorkflowContext processContext) {
//        this.processContext = processContext;
//    }

    
    @Override
    public String getResponse(String subject, String from) {
        return null;
    }
    
    
    protected BigDecimal validateNumeric(String valueString, StringBuilder statusString, String column) {
        BigDecimal value = null;
        
        if (valueString != null && !valueString.isEmpty()) {
            try {
                value = new BigDecimal(valueString);
            } catch (Exception ex) {
                statusString.append(" Invalid Format For '").append(column).append("'.");
            }
        }
        return value;
    }

    
    protected Claim validateClaimReferenceNumber(String referenceNumber, StringBuilder statusString) {

        Claim claim = null;
        if (!regexExpressionChecker(REG_ALPHANUMERIC, referenceNumber)) {
            statusString.append(" No Claim Reference Provided.");
        } else {
            ((SecureDataService)claimService).setSecurityInfoProvider(((SecureDataService)claimService).getSecurityInfoProvider());
            claim = claimService.getClaimByCHOReferenceNumber(referenceNumber);

            if (claim == null) {
                LOG.debug("No Such Claim Reference {}", referenceNumber);
                statusString.append(" No Such Claim Reference.");
            }
        }
        return claim;
    }

    
    protected Date validateDate(String dateString, StringBuilder statusString, String columnName) {
        Date date = null;
        SimpleDateFormat sdf = DateHelper.getLocalDateFormat();
        sdf.setLenient(false);
        if (dateString.isEmpty()) {
            statusString.append(" No '").append(columnName).append("' provided.");
        } else if (dateString.length() != sdf.toPattern().length()) {
            statusString.append(" Invalid Format For '").append(columnName).append("'.");
        } else {
            try {
                date = sdf.parse(dateString);
            } catch (ParseException ex) {
                statusString.append(" Invalid Format For '").append(columnName).append("'.");
                LOG.warn("Parse exception thrown for column {}: {}", columnName, dateString);
            }
        }
        return date;
    }
    
    
    protected String validateTime(String timeString, StringBuilder statusString) {
        if (!regexExpressionChecker(REG_TIME, timeString)) {
            statusString.append("  Invalid Format for Hire Start (Time).");
        } 
        
        return timeString;
    }


    protected boolean regexExpressionChecker(String regex, String dataValue) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(dataValue);

        if (!m.find()) {
            LOG.debug("Invalid data for regex '{}': {}", regex, dataValue);
            return false;
        }
        return true;
    }

}
