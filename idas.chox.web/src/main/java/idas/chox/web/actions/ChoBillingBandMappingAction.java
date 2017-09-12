package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.opensymphony.xwork2.Action.SUCCESS;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.LookupItem;
import idas.chox.core.model.ChoBillingBand;
import idas.chox.core.model.ChoBillingBandMapping;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ClaimType;
import idas.chox.core.services.BillingBandMappingService;
import idas.chox.core.services.BillingBandService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.LookupService;
import idas.chox.service.ActionResponse;
import idas.chox.web.viewdata.BillingBandViewData;


/**
 *
 * @author john
 */
public class ChoBillingBandMappingAction  extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(ChoBillingBandMappingAction.class);
    private List<Chorganisation> chos;
    private LookupService lookupService;
    private BillingBandMappingService billingBandMappingService;
    private BillingBandService billingBandService;
    private InsurerService insurerService;
    private int insurerId;
    private int billingBandId;
    private List<BillingBandViewData> jsonData;
    private List<BillingBandTarget> availableMappings;
    private List<BillingBandTarget> selectedMappings;
    private int jsonDataType = 0;
    private int chorganisationId;
    private int claimTypeId;
    private int billingBandMappingId;

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public void setBillingBandMappingService(BillingBandMappingService billingBandMappingService) {
        this.billingBandMappingService = billingBandMappingService;
    }

    public void setBillingBandService(BillingBandService billingBandService) {
        this.billingBandService = billingBandService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public void setBillingBandId(int billingBandId) {
        this.billingBandId = billingBandId;
    }

    public void setChorganisationId(int chorganisationId) {
        this.chorganisationId = chorganisationId;
    }

    public void setClaimTypeId(int claimTypeId) {
        this.claimTypeId = claimTypeId;
    }

    public void setBillingBandMappingId(int billingBandMappingId) {
        this.billingBandMappingId = billingBandMappingId;
    }

    public String getChosJsonString() {
        List<LookupItem> luItems = new ArrayList<>(getMappedChos().size());
        for (Chorganisation cho : chos) {
            luItems.add(new LookupItem(cho.getId().toString(), cho.getName()));
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(luItems);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting CHO luItems to json string.");
        }
        return StringEscapeUtils.escapeEcmaScript("{totalCount:" + luItems.size() + ", results:" + jsonString + "}");
    }
    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String addChoBillingBandInsurerMapping() {
        actionResponse = new ActionResponse();
        setActionResponse(actionResponse);
        ChoBillingBandMapping m = new ChoBillingBandMapping();
        m.setInsurer(insurerService.getInsurer(insurerId));
        m.setClaimType(ClaimType.values()[claimTypeId]);
        m.setChoBillingBand(billingBandService.getChoBillingBand(billingBandId));
        try {
            billingBandMappingService.saveChoBillingBandMapping(m);
        } catch (Exception ex) {
            LOG.warn("Error adding new CHO Billing Band Mapping: {}", ex.getMessage());
            actionResponse.AddError("An internal error occurred trying to add the billing band mapping.");
        }
        return SUCCESS;
    }
    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String removeChoBillingBandInsurerMapping() {
        actionResponse = new ActionResponse();
        setActionResponse(actionResponse);
        try {
            billingBandMappingService.deleteChoBillingBandMapping(billingBandMappingService.getChoBillingBandMapping(billingBandMappingId));
        } catch (Exception ex) {
            LOG.warn("Error deleting CHO Billing Band Mapping: {}", ex.getMessage());
            actionResponse.AddError("An internal error occurred trying to remove the billing band mapping.");
        }
        return SUCCESS;
    }
    
    private List getMappedChos() {

        if (chos == null) {
            chos = this.lookupService.getSuppliers(true);
        }

        return chos;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String loadChoBillingBands() {
            try {
                List<ChoBillingBand> choBillingBands = billingBandService.getChoBillingBands(chorganisationId);
                jsonData = new ArrayList<>();
                for (ChoBillingBand h : choBillingBands) {
                    jsonData.add(new BillingBandViewData(h));
                }
            } catch (Exception ex) {
                LOG.error("Exception thrown getting CHO Billing Bands: {}", ex.getMessage());
            }

        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String getAvailableChoBillingBandInsurer() {
        List<ChoBillingBandMapping> allAvailableMappings = billingBandMappingService.getAvailableChoBillingBandMappings(chorganisationId);
        List<ChoBillingBandMapping> selectedChoMappings = billingBandMappingService.getChoBillingBandMappings(chorganisationId);
        availableMappings = new ArrayList<>();
        int count = 0;
        for (ChoBillingBandMapping m : allAvailableMappings) {
            if (!mappingExists(selectedChoMappings, m)) {
                BillingBandTarget t = new BillingBandTarget();
                t.id = count++;
                t.insurerId = m.getInsurer().getId();
                t.insurerName = m.getInsurer().getName();
                t.claimType = m.getClaimType().ordinal();
                t.claimTypeDesc = m.getClaimType().toString();
                availableMappings.add(t);
            }
        }
        jsonDataType = 1;
        return SUCCESS;
    }

    private boolean mappingExists(List<ChoBillingBandMapping> mappings, ChoBillingBandMapping m) {
        for (ChoBillingBandMapping m1 : mappings) {
            if (m1.getInsurer() == m.getInsurer() && m1.getClaimType() == m.getClaimType())
                return true;
        }
        return false;
    }
    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String getSelectedChoBillingBandInsurer() {
        List<ChoBillingBandMapping> selectedChoMappings = billingBandMappingService.getChoBillingBandMappings(chorganisationId, billingBandId);
        selectedMappings = new ArrayList<>(selectedChoMappings.size());
        for (ChoBillingBandMapping m : selectedChoMappings) {
            BillingBandTarget t = new BillingBandTarget();
            t.id = m.getId();
            t.insurerId = m.getInsurer().getId();
            t.insurerName = m.getInsurer().getName();
            t.claimType = m.getClaimType().ordinal();
            t.claimTypeDesc = m.getClaimType().toString();
            selectedMappings.add(t);
        }
        jsonDataType = 2;
        return SUCCESS;
    }
   
    public String getJsonData() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        int size;
        
        switch (jsonDataType) {
            case 1:
                size = availableMappings.size();
                try {
                    jsonString = mapper.writeValueAsString(availableMappings);
                } catch (JsonProcessingException ex) {
                    LOG.error("Error converting availableMappings to json string.");
                }
                break;
            case 2:
                size = selectedMappings.size();
                try {
                    jsonString = mapper.writeValueAsString(selectedMappings);
                } catch (JsonProcessingException ex) {
                    LOG.error("Error converting selectedMappings to json string.");
                }
                break;
            default:
                size = jsonData.size();
                try {
                    jsonString = mapper.writeValueAsString(jsonData);
                } catch (JsonProcessingException ex) {
                    LOG.error("Error converting jsonData to json string.");
                }
                break;
        }
        jsonDataType = 0;
        return "{totalCount:" + size + ",results:" + jsonString + "}";
    }

    @Secured({"ROLE_CHOX_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }


    public class BillingBandTarget {
        public int id;
        public int insurerId;
        public String insurerName;
        public int claimType;
        public String claimTypeDesc;

        public int getId() {
            return id;
        }

        public int getInsurerId() {
            return insurerId;
        }

        public String getInsurerName() {
            return insurerName;
        }

        public int getClaimType() {
            return claimType;
        }

        public String getClaimTypeDesc() {
            return claimTypeDesc;
        }
        
    }
}
