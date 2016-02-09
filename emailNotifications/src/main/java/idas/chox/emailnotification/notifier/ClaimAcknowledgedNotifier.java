package idas.chox.emailnotification.notifier;

import idas.chox.emailnotification.config.NotificationSettingsBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ClaimAcknowledgedNotifier extends AbstractNotifier implements Notifier {

    protected static final String TEMPLATE_LOCATION = "templates/acknowledged_manual_claim_notification.vm";
    protected static final String SUBJECT = "%s Supplier Reference: %s Claim Acknowledged Notification";
    protected static final String BASE_QUERY = //
            "select i.name as insurer_name, c.cho_reference, c.claim_number, c.percentage_liability_accepted, " +
                    "    getLiabilityStatus(c.liability_status) as liability_status, wu.first_name || ' ' || wu.last_name as claim_owner, w.name as workgroup, " +
                    "    (select  array_to_string(array_agg(substring(co.comment from 28) ), ' ')  from comment co where co.claim_id = c.id and co.created_date between (at.created_date - interval '1 second') and (at.created_date + interval '1 seconds') and co.comment like 'Supporting Liability Note%%') as comment " +

                    "from claim c " +
                    "    left outer join workgroup w on (c.workgroup_id = w.id) " +
                    "    left outer join web_user wu on (c.claim_owner_id = wu.id), " +
                    "    insurer i,  audit_trail at " +

                    "where c.insurer_id = %s and c.chorganisation_id = %s " +
                    "    and i.id = c.insurer_id " +
                    "    and claim_type in (10,14,15,16,17) " +
                    "    and at.claim_id = c.id " +
                    "    and at.new_status='AwaitingCarHireInfo' " +
                    "    and at.original_status in('ClaimUnacknowledgedRouted','ClaimPending') " +
                    "    and at.reverted = false and at.created_date between '%s' and '%s';";

    @Override
    public void getAndProcessNotificationData(NotificationSettingsBean settings, Date dateFrom, Date dateTo) {

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateFromString = dateFormat.format(dateFrom);
        String dateToString = dateFormat.format(dateTo);

        String reportQuery = String.format(BASE_QUERY, settings.getInsurerId(), settings.getChoId(), dateFromString, dateToString);
        this.runReport(settings, reportQuery);

    }

    @Override
    protected void processRecord(ResultSet rs, List<String> recipients) throws SQLException {
        String[] emailTo = (String[]) recipients.toArray();

        // Prepare data fields
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("insurer_name", rs.getString("insurer_name"));
        data.put("supplier_reference", rs.getString("cho_reference"));
        data.put("insurer_claim_number", rs.getString("claim_number"));
        data.put("liability_status", rs.getString("liability_status"));
        data.put("liability_percentage", rs.getInt("percentage_liability_accepted"));
        data.put("insurer_claim_owner", rs.getString("claim_owner"));
        data.put("workgroup", rs.getString("workgroup"));

        String comment = rs.getString("comment");
        if (comment == null) {
            comment = "";
        }
        data.put("liability_note", comment);

        String subject = String.format(SUBJECT, data.get("insurer_name"), data.get("supplier_reference"));

        generateAndSendEmail(subject, TEMPLATE_LOCATION, data, emailTo);
    }

}
