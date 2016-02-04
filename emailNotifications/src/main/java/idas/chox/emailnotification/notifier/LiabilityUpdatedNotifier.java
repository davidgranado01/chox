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
public class LiabilityUpdatedNotifier extends AbstractNotifier implements Notifier {

    protected static final String TEMPLATE_LOCATION = "templates/liability_updated_manual_claim_notification.vm";
    protected static final String SUBJECT = "%s Supplier Reference: %s Liability Updated Notification";

    protected static final String BASE_QUERY = //
            "select i.name as insurer_name, c.cho_reference, c.claim_number, c.percentage_liability_accepted, " +
                    "    c.liability_status, wu.first_name || ' ' || wu.last_name as claim_owner, w.name as workgroup, " +
                    "    inv.full_total_to_pay, inv.total_to_pay, co.comment as liability_status_note " +

                    "from claim c  " +
                    "    left outer join workgroup w on (c.workgroup_id = w.id) " +
                    "    left outer join web_user wu on (c.claim_owner_id = wu.id), " +
                    "    insurer i, comment co, invoice inv " +

                    "where c.insurer_id = %s and c.chorganisation_id = %s  " +
                    "    and i.id = c.insurer_id  " +
                    "    and claim_type in (10,14,15,16,17) " +
                    "    and c.invoice_id = inv.id " +
                    "    and c.id=co.claim_id " +
                    "    and co.comment like 'Supporting Liability Notes:%s' " +
                    "    and co.created_date between '%s' and '%s' " +
                    "    and c.liability_status_modified_date between '%s' and '%s' " +
                    "    and c.id not in " +
                    " ( select claim.id from claim, audit_trail audit " +
                    "where claim.insurer_id = %s and claim.chorganisation_id = %s " +
                    "    and claim.claim_type in (10,14,15,16,17) " +
                    "    and audit.claim_id = claim.id " +
                    "    and audit.new_status='AwaitingCarHireInfo' " +
                    "    and audit.original_status in('ClaimUnacknowledgedRouted','ClaimPending') " +
                    "    and audit.reverted = false and audit.created_date between '%s' and '%s');";

    @Override
    public void getAndProcessNotificationData(NotificationSettingsBean settings, Date dateFrom, Date dateTo) {

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateFromString = dateFormat.format(dateFrom);
        String dateToString = dateFormat.format(dateTo);

        String reportQuery = String.format(BASE_QUERY, settings.getInsurerId(), settings.getChoId(), PERCENTAGE, dateFromString, dateToString, dateFromString, dateToString, settings.getInsurerId(), settings.getChoId(), dateFromString, dateToString);
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
        data.put("liability_status", lookupLiabilityStatus(rs.getInt("liability_status")));
        data.put("liability_percentage", rs.getInt("percentage_liability_accepted"));
        data.put("liability_note", rs.getString("liability_status_note"));
        data.put("insurer_claim_owner", rs.getString("claim_owner"));
        data.put("workgroup", rs.getString("workgroup"));

        String subject = String.format(SUBJECT, data.get("insurer_name"), data.get("supplier_reference"));

        generateAndSendEmail(subject, TEMPLATE_LOCATION, data, emailTo);
    }

}
