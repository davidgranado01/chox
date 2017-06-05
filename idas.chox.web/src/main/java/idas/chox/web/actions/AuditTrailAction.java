package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.services.AuditTrailService;
import idas.chox.service.security.TabAccessibility;
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
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(auditTrail);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting auditTrail to json string.");
        }
        return "{totalCount:" + this.auditTrail.size() + ",results:" + jsonString + "}";
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
        
        auditTrail = new ArrayList<>();
        
        for (AuditTrail h : auditTrailData) {
            auditTrail.add(new AuditTrailViewData(h));
        }

        return SUCCESS;
    }

    @Override
    String getTabName() {
        return TabAccessibility.TAB_AUDIT_TRAIL;
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
