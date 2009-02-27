package chox.web.viewdata;

import chox.model.Chorganisation;
import chox.model.AuditTrail;
import chox.model.Insurer;
import chox.model.WebUser;
import java.text.Format;
import java.text.SimpleDateFormat;

public class AuditTrailViewData {

    private int id;
    private String modifiedDate;
    private String modifiedBy;
    private String status;
    
    public AuditTrailViewData(AuditTrail auditTrail) {
        
        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.id = auditTrail.getId();
        this.modifiedDate = dateFormat.format(auditTrail.getUpdateDate());
        
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
    
    
}
