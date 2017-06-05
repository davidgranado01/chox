package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.enums.AuditReviewClaimType;
import idas.chox.core.model.LookupItem;

public class ClaimAuditReviewAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimAuditReviewAction.class);

    private String jsonData;

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getAuditReviewClaimTypes() {
        List<LookupItem> luItems = new ArrayList<>(AuditReviewClaimType.values().length);
        for (AuditReviewClaimType auditReviewClaimType : AuditReviewClaimType.values()) {
            luItems.add(new LookupItem(auditReviewClaimType.getValue().toString(), auditReviewClaimType.getDescription()));
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(luItems);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting insurerAutomaticRoutingsByPrice to json string.");
        }
        setJsonData("{totalCount:" + AuditReviewClaimType.values().length + ",results:" + jsonString + "}");
        return SUCCESS;
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

}
