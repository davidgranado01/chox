package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import static com.opensymphony.xwork2.Action.SUCCESS;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.InsurerBillingBand;
import idas.chox.core.model.LookupItem;
import idas.chox.core.services.BillingBandService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.LookupService;
import idas.chox.service.ActionResponse;
import idas.chox.web.viewdata.BillingBandViewData;

/**
 *
 * @author john
 */
public class InsurerBillingBandAction  extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(InsurerBillingBandAction.class);
    private List<Insurer> insurers;
    private LookupService lookupService;
    private InsurerService insurerService;
    private BillingBandService billingBandService;
    private List<BillingBandViewData> jsonData;
    private int billingInsurerId;
    private int billingBandId;
    private String insurerBillingBandName;
    private String triggerPoint;
    private BigDecimal insurerCostPerClaim;
    private boolean insurerExcludeSupplementary;
    
    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public void setBillingBandService(BillingBandService billingBandService) {
        this.billingBandService = billingBandService;
    }

    public void setBillingInsurerId(int billingInsurerId) {
        this.billingInsurerId = billingInsurerId;
    }
    
    public void setBillingBandId(int billingBandId) {
        this.billingBandId = billingBandId;
    }
    
    public void setInsurerBillingBandName(String insurerBillingBandName) {
        this.insurerBillingBandName = insurerBillingBandName;
    }
    
    public void setInsurerCostPerClaim(BigDecimal insurerCostPerClaim) {
        this.insurerCostPerClaim = insurerCostPerClaim.setScale(2);
    }
    
    public void setInsurerCostPerClaim(String insurerCostPerClaim) {
        this.insurerCostPerClaim = new BigDecimal(insurerCostPerClaim);
    }
    
    public void setTriggerPoint(String triggerPoint) {
        this.triggerPoint = triggerPoint;
    }
    public void setInsurerExcludeSupplementary(boolean insurerExcludeSupplementary) {
        this.insurerExcludeSupplementary = insurerExcludeSupplementary;
    }

    @Secured({"ROLE_CHOX_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }
    

    public String getInsurersJsonString() {
        List<LookupItem> luItems = new ArrayList<>(getMappedInsurers().size());
        for (Insurer insurer : insurers) {
            luItems.add(new LookupItem(insurer.getId().toString(), insurer.getName()));
        }
        return StringEscapeUtils.escapeEcmaScript("{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}");
    }
    

    private List getMappedInsurers() {

        if (insurers == null) {
            insurers = this.lookupService.getInsurers();
        }

        return insurers;
    }

    public String getBillingBands() {
            List<InsurerBillingBand> insurerBillingBands;
            try {
                insurerBillingBands = billingBandService.getInsurerBillingBands();
                jsonData = new ArrayList<>();
                for (InsurerBillingBand h : insurerBillingBands) {
                    jsonData.add(new BillingBandViewData(h));
                }
            } catch (Exception ex) {
                LOG.error("Exception thrown getting Insurer Billing Bands: {}", ex.getMessage());
            }

        return SUCCESS;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(jsonData);
        return "{totalCount:" + this.jsonData.size() + ",results:" + jObject.toString() + "}";
    }
    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String addNewInsurerBillingBand() {
        actionResponse = new ActionResponse();
        setActionResponse(actionResponse);

        InsurerBillingBand band = new InsurerBillingBand();
        Insurer insurer = insurerService.getInsurer(billingInsurerId);
        band.setInsurer(insurer);
        band.setBandName(insurerBillingBandName);
        band.setCostPerClaim(insurerCostPerClaim);
        band.setExcludeSupplementary(insurerExcludeSupplementary);
        band.setTriggerStatus(triggerPoint);
        
        try {
            billingBandService.saveBillingBand(band);
        } catch (Exception ex) {
            LOG.warn("Error saving Insurer Billing Band: {}", ex.getMessage());
            actionResponse.AddError("An internal error occurred trying to save the Insurer Billing Band.");
        }
        return SUCCESS;
    }

    public String deleteInsurerBillingBand() {
        actionResponse = new ActionResponse();
        try {
            billingBandService.deleteBillingBand(billingBandService.getInsurerBillingBand(billingBandId));
        } catch (Exception ex) {
            LOG.warn("Error deleting Insurer Billing Band: {}", ex.getMessage());
            actionResponse.AddError("An internal error occurred trying to delete the Insurer Billing Band.");
        }
        return SUCCESS;
    }
}
