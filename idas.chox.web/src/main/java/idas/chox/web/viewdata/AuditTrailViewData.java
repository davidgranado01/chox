package idas.chox.web.viewdata;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.util.DateHelper;

public class AuditTrailViewData {

    private final int id;
    private final String modifiedDate;
    private final String modifiedBy;
    private final String status;
    private final boolean reverted;

    public AuditTrailViewData(AuditTrail auditTrail) {

        this.id = auditTrail.getId();
        this.modifiedDate = DateHelper.getLocalDateTimeFormatWithSecs().format(auditTrail.getUpdateDate());
        this.status = auditTrail.getNewStatus();
        this.reverted = auditTrail.getReverted();

        String orgName = "";
        WebUser user = auditTrail.getCreatedBy();
        if (user != null) {
            Chorganisation cho = user.getChorganisation();
            Insurer ins = user.getInsurer();

            if (ins != null) {
                orgName = String.format("(%1$s)", ins.getName());
            } else if (cho != null) {
                orgName = String.format("(%1$s)", cho.getName());
            }

            if ((user.getFirstName() != null && user.getFirstName().startsWith("~~"))
                    || (user.getLastName() != null && user.getLastName().startsWith("~~"))) {
                modifiedBy = String.format("GDPR: data removed %1$s", orgName);
            } else {
                modifiedBy = String.format("%1$s %2$s %3$s", user.getFirstName(), user.getLastName(), orgName);
            }
        } else {
            modifiedBy = "unknown";
        }
    }

    public int getId() {
        return id;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public String getStatus() {
        return status;
    }

    public boolean isReverted() {
        return reverted;
    }
}
