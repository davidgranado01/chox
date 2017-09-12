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

import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.InsurerBillingBand;
import idas.chox.core.model.InsurerBillingBandMapping;
import idas.chox.core.model.LookupItem;
import idas.chox.core.services.BillingBandMappingService;
import idas.chox.core.services.BillingBandService;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.service.ActionResponse;
import idas.chox.web.viewdata.BillingBandViewData;

/**
 *
 * @author john
 */
public class InsurerBillingBandMappingAction  extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(InsurerBillingBandMappingAction.class);
    private List<Insurer> insurers;
    private LookupService lookupService;
    private BillingBandMappingService billingBandMappingService;
    private BillingBandService billingBandService;
    private ChorganisationService chorganisationService;
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

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
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

    public String getInsurersJsonString() {
        List<LookupItem> luItems = new ArrayList<>(getMappedInsurers().size());
        for (Insurer insurer : insurers) {
            luItems.add(new LookupItem(insurer.getId().toString(), insurer.getName()));
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(luItems);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting LookupItem to json string.");
        }
        return StringEscapeUtils.escapeEcmaScript("{totalCount:" + luItems.size() + ", results:" + jsonString + "}");
    }
    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String addInsurerBillingBandChorganisationMapping() {
        actionResponse = new ActionResponse();
        setActionResponse(actionResponse);
        InsurerBillingBandMapping m = new InsurerBillingBandMapping();
        m.setChorganisation(chorganisationService.getChorganisation(chorganisationId));
        m.setClaimType(ClaimType.values()[claimTypeId]);
        m.setInsurerBillingBand(billingBandService.getInsurerBillingBand(billingBandId));
        try {
            billingBandMappingService.saveInsurerBillingBandMapping(m);
        } catch (Exception ex) {
            LOG.warn("Error adding new Insurer Billing Band Mapping: {}", ex.getMessage());
            actionResponse.AddError("An internal error occurred trying to add the billing band mapping.");
        }
        return SUCCESS;
    }
    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String removeInsurerBillingBandChorganisationMapping() {
        actionResponse = new ActionResponse();
        setActionResponse(actionResponse);
        try {
            billingBandMappingService.deleteInsurerBillingBandMapping(billingBandMappingService.getInsurerBillingBandMapping(billingBandMappingId));
        } catch (Exception ex) {
            LOG.warn("Error deleting Insurer Billing Band Mapping: {}", ex.getMessage());
            actionResponse.AddError("An internal error occurred trying to remove the billing band mapping.");
        }
        return SUCCESS;
    }
    
    private List getMappedInsurers() {

        if (insurers == null) {
            insurers = this.lookupService.getInsurers();
        }

        return insurers;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String loadInsurerBillingBands() {
            try {
                List<InsurerBillingBand> insurerBillingBands = billingBandService.getInsurerBillingBands(insurerId);
                jsonData = new ArrayList<>();
                for (InsurerBillingBand h : insurerBillingBands) {
                    jsonData.add(new BillingBandViewData(h));
                }
            } catch (Exception ex) {
                LOG.error("Exception thrown getting Insurer Billing Bands: {}", ex.getMessage());
            }

        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String getAvailableInsurerBillingBandChorganisation() {
        List<InsurerBillingBandMapping> allAvailableMappings = billingBandMappingService.getAvailableInsurerBillingBandMappings(insurerId);
        List<InsurerBillingBandMapping> selectedInsurerMappings = billingBandMappingService.getInsurerBillingBandMappings(insurerId);
        availableMappings = new ArrayList<>();
        int count = 0;
        for (InsurerBillingBandMapping m : allAvailableMappings) {
            if (!mappingExists(selectedInsurerMappings, m)) {
                BillingBandTarget t = new BillingBandTarget();
                t.id = count++;
                t.chorganisationId = m.getChorganisation().getId();
                t.chorganisationName = m.getChorganisation().getName();
                t.claimType = m.getClaimType().ordinal();
                t.claimTypeDesc = m.getClaimType().toString();
                availableMappings.add(t);
            }
        }
        jsonDataType = 1;
        return SUCCESS;
    }

    private boolean mappingExists(List<InsurerBillingBandMapping> mappings, InsurerBillingBandMapping m) {
        for (InsurerBillingBandMapping m1 : mappings) {
            if (m1.getChorganisation() == m.getChorganisation() && m1.getClaimType() == m.getClaimType())
                return true;
        }
        return false;
    }
    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String getSelectedInsurerBillingBandChorganisation() {
        List<InsurerBillingBandMapping> selectedInsurerMappings = billingBandMappingService.getInsurerBillingBandMappings(insurerId, billingBandId);
        selectedMappings = new ArrayList<>(selectedInsurerMappings.size());
        for (InsurerBillingBandMapping m : selectedInsurerMappings) {
            BillingBandTarget t = new BillingBandTarget();
            t.id = m.getId();
            t.chorganisationId = m.getChorganisation().getId();
            t.chorganisationName = m.getChorganisation().getName();
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
                    LOG.error("Error converting jsonString to json string.");
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
        public int chorganisationId;
        public String chorganisationName;
        public int claimType;
        public String claimTypeDesc;

        public int getId() {
            return id;
        }

        public int getChorganisationId() {
            return chorganisationId;
        }

        public String getChorganisationName() {
            return chorganisationName;
        }

        public int getClaimType() {
            return claimType;
        }

        public String getClaimTypeDesc() {
            return claimTypeDesc;
        }
        
    }
}
