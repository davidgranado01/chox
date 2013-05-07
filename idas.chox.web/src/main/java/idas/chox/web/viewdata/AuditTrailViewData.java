package idas.chox.web.viewdata;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.util.DateHelper;

public class AuditTrailViewData {

    private int id;
    private String modifiedDate;
    private String modifiedBy;
    private String status;
    private boolean reverted;

    public AuditTrailViewData(AuditTrail auditTrail) {

        this.id = auditTrail.getId();
        this.modifiedDate = DateHelper.getLocalDateTimeFormatWithSecs().format(auditTrail.getUpdateDate());

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

            this.modifiedBy = String.format("%1$s %2$s %3$s", user.getFirstName(), user.getLastName(), orgName);
            this.status = auditTrail.getNewStatus();
            this.reverted = auditTrail.getReverted();
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
