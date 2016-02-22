package idas.chox.emailnotification.notifier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class InvoiceContestedNotifier extends AbstractNotifier implements Notifier {

    private static final String TEMPLATE_LOCATION = "templates/invoice_contested_manual_claim_notification.vm";
    private static final String SUBJECT = "%s Supplier Reference: %s Invoice Contested Notification";

    private static final String BASE_QUERY = //
            "select i.name as insurer_name, c.cho_reference, c.claim_number, c.percentage_liability_accepted, " +
                    "    getLiabilityStatus(c.liability_status) as liability_status, wu.first_name || ' ' || wu.last_name as claim_owner, w.name as workgroup, " +
                    "    inv.full_total_to_pay, inv.total_to_pay " +

                    "from claim c  " +
                    "    left outer join workgroup w on (c.workgroup_id = w.id) " +
                    "    left outer join web_user wu on (c.claim_owner_id = wu.id), " +
                    "    insurer i, audit_trail at, invoice inv " +

                    "where c.insurer_id = :insId and c.chorganisation_id = :choId  " +
                    "    and i.id = c.insurer_id  " +
                    "    and claim_type in (10,14,15,16,17) " +
                    "    and at.claim_id = c.id  " +
                    "    and c.invoice_id = inv.id " +
                    "    and at.new_status = 'ManualInvoiceContested'  " +
                    "    and at.original_status in ('ManualInvoiceBRERejected', 'ManualInvoiceBREApproved') " +
                    "    and at.reverted = false and at.created_date between :startDate and :endDate";


    @Override
    public String getQueryString() { return BASE_QUERY;}


    @Override
    protected void processRecord(ResultSet rs, List<String> recipients, Boolean enableEmails) throws SQLException {
        String[] emailTo = (String[]) recipients.toArray();

        // Prepare data fields
        Map<String, Object> data = new HashMap<>();
        data.put("insurer_name", rs.getString("insurer_name"));
        data.put("supplier_reference", rs.getString("cho_reference"));
        data.put("insurer_claim_number", getResultString(rs, "claim_number"));
        data.put("liability_status", rs.getString("liability_status"));
        data.put("liability_percentage", rs.getBigDecimal("percentage_liability_accepted"));
        data.put("total_requested", rs.getBigDecimal("full_total_to_pay"));
        data.put("total_to_pay", rs.getBigDecimal("total_to_pay"));
        data.put("insurer_claim_owner", getResultString(rs, "claim_owner"));
        data.put("workgroup", getResultString(rs, "workgroup"));

        String subject = String.format(SUBJECT, data.get("insurer_name"), data.get("supplier_reference"));

        logEmail(subject, emailTo);
        if (enableEmails) {
            generateAndSendEmail(subject, TEMPLATE_LOCATION, data, emailTo);
        }
    }

}
