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
public class QuantumAgreedNotifier extends AbstractNotifier implements Notifier {

    protected static final String TEMPLATE_LOCATION = "templates/quantum_agreed_manual_claim_notification.vm";
    protected static final String SUBJECT = "%s Supplier Reference: %s Quantum Agreeed Notification";
    protected static final String BASE_QUERY = //
            "select i.name as insurer_name, c.cho_reference, c.claim_number, c.percentage_liability_accepted, " +
                    "    c.liability_status, wu.first_name || ' ' || wu.last_name as claim_owner, w.name as workgroup, " +
                    "    inv.full_total_to_pay, inv.total_to_pay " +

                    "from claim c  " +
                    "    left outer join workgroup w on (c.workgroup_id = w.id) " +
                    "    left outer join web_user wu on (c.claim_owner_id = wu.id), " +
                    "    insurer i, audit_trail at, invoice inv " +

                    "where c.insurer_id = %s and c.chorganisation_id = %s  " +
                    "    and i.id = c.insurer_id  " +
                    "    and claim_type in (10,14,15,16,17) " +
                    "    and at.claim_id = c.id  " +
                    "    and c.invoice_id = inv.id " +
                    "    and at.new_status in('AwaitingLiabilityResolution','AwaitingInvoicePayment')  " +
                    "    and at.original_status in('ManualInvoiceBRERejected','ManualInvoiceContested','ManualInvoiceBREApproved') " +
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
        data.put("liability_status", lookupLiabilityStatus(rs.getInt("liability_status")));
        data.put("liability_percentage", rs.getInt("percentage_liability_accepted"));
        data.put("insurer_claim_owner", rs.getString("claim_owner"));
        data.put("total_requested", rs.getString("full_total_to_pay"));
        data.put("total_to_pay", rs.getString("total_to_pay"));
        data.put("workgroup", rs.getString("workgroup"));

        String subject = String.format(SUBJECT, data.get("insurer_name"), data.get("supplier_reference"));

        generateAndSendEmail(subject, TEMPLATE_LOCATION, data, emailTo);
    }

}
