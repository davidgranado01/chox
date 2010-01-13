package idas.chox.web.actions;

import idas.chox.core.model.AuditTrail;
// import idas.chox.core.model.Claim;
import idas.chox.core.services.AuditTrailService;
import idas.chox.web.viewdata.AuditTrailViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import idas.chox.service.security.ApplicationAccessibility;

public class AuditTrailAction extends ClaimModelAction<AuditTrail>{

    private List<AuditTrailViewData> auditTrail;
    private AuditTrailService service;

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getJsonArrayData() {
        JSONArray jObject = JSONArray.fromObject(this.auditTrail);
        return "{totalCount:" + this.auditTrail.size() + ",results:" + jObject.toString() + "}";
    }

    public List<AuditTrailViewData> getAuditTrail() {
        return auditTrail;
    }

    public void setAuditTrailService(AuditTrailService service) {
        this.service = service;
    }

    public String getAuditTrails() {

        List<AuditTrail> auditTrailsData = this.service.getAuditTrailByClaim(claimId);
        auditTrail = new ArrayList<AuditTrailViewData>();
        
        for (AuditTrail h : auditTrailsData) {
            auditTrail.add(new AuditTrailViewData(h));
        }

        return SUCCESS;
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_AUDIT_TRAIL;
    }
    
    @Override
    protected AuditTrail loadModel() {
        /*
        if (claim.getA == null) {
            return new AuditTrail();
        } else {
            return claim.getCustomer();
        }
        */
        return new AuditTrail();
    }
}
