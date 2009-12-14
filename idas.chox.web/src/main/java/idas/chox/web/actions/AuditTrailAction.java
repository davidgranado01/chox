package idas.chox.web.actions;


import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Claim;
import idas.chox.core.services.AuditTrailService;
import idas.chox.web.viewdata.AuditTrailViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class AuditTrailAction extends BaseAction {

    private List<AuditTrailViewData> auditTrail;
    private AuditTrailService service;
    private int claimId;

     public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.auditTrail);
        return "{totalCount:" + this.auditTrail.size() + ",results:" + jObject.toString() + "}";
    }

    public List<AuditTrailViewData> getAuditTrail() {
        return auditTrail;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }
    
    public void setAuditTrailService(AuditTrailService service)
    {
        this.service = service;
    }
    
    @Override
    public String execute() {
    
        Claim claim = new Claim();
        claim.setId(claimId);
        List<AuditTrail> auditTrailsData = this.service.getAuditTrailByClaim(claimId);
        
        auditTrail = new ArrayList<AuditTrailViewData>();
        
        for(AuditTrail h : auditTrailsData)
        {
            auditTrail.add(new AuditTrailViewData(h));
        }
        
        return SUCCESS;
    }    
}
