package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.services.AuditTrailService;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.web.viewdata.AuditTrailViewData;

public class AuditTrailAction extends ClaimModelAction<AuditTrail>{
    private static final Logger LOG = LoggerFactory.getLogger(AuditTrailAction.class);

    private List<AuditTrailViewData> auditTrail;
    private AuditTrailService service;
    private boolean hideReverted;

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
        List<AuditTrail> auditTrailData;
        
        if (hideReverted) {
            LOG.debug("Getting non-reverted audit trail...");
            auditTrailData = this.service.getAuditTrailByClaim(claimId);
        } else {
            LOG.debug("Getting all audit trail...");
            auditTrailData = this.service.getFullAuditTrailByClaim(claimId, true);
        }
        
        auditTrail = new ArrayList<AuditTrailViewData>();
        
        for (AuditTrail h : auditTrailData) {
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
        return new AuditTrail();
    }

    public boolean isHideReverted() {
        return hideReverted;
    }

    public void setHideReverted(boolean hideReverted) {
        this.hideReverted = hideReverted;
    }

    public boolean isHasReverted() {
        return service.hasRevertedEntries(claimId);
    }

}
