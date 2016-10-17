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

import idas.chox.core.model.ChoBillingBand;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.LookupItem;
import idas.chox.core.services.BillingBandService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.LookupService;
import idas.chox.service.ActionResponse;
import idas.chox.web.viewdata.BillingBandViewData;

/**
 *
 * @author john
 */
public class ChoBillingBandAction  extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(ChoBillingBandAction.class);
    private List<Chorganisation> chos;
    private LookupService lookupService;
    private ChorganisationService choService;
    private BillingBandService billingBandService;
    private List<BillingBandViewData> jsonData;
    private int billingChoId;
    private int billingBandId;
    private String choBillingBandName;
    private String triggerPoint;
    private BigDecimal choCostPerClaim;
    private boolean choExcludeSupplementary;
    
    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public void setChorganisationService(ChorganisationService choService) {
        this.choService = choService;
    }

    public void setBillingBandService(BillingBandService billingBandService) {
        this.billingBandService = billingBandService;
    }

    public void setBillingChoId(int billingChoId) {
        this.billingChoId = billingChoId;
    }
    
    public void setBillingBandId(int billingBandId) {
        this.billingBandId = billingBandId;
    }
    
    public void setChoBillingBandName(String choBillingBandName) {
        this.choBillingBandName = choBillingBandName;
    }
    
    public void setChoCostPerClaim(BigDecimal choCostPerClaim) {
        this.choCostPerClaim = choCostPerClaim.setScale(2);
    }
    
    public void setChoCostPerClaim(String choCostPerClaim) {
        this.choCostPerClaim = new BigDecimal(choCostPerClaim);
    }
    
    public void setTriggerPoint(String triggerPoint) {
        this.triggerPoint = triggerPoint;
    }
    public void setChoExcludeSupplementary(boolean choExcludeSupplementary) {
        this.choExcludeSupplementary = choExcludeSupplementary;
    }

    @Secured({"ROLE_CHOX_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }
    

    public String getChosJsonString() {
        List<LookupItem> luItems = new ArrayList<>(getMappedChos().size());
        for (Chorganisation cho : chos) {
            luItems.add(new LookupItem(cho.getId().toString(), cho.getName()));
        }
        return StringEscapeUtils.escapeEcmaScript("{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}");
    }
    

    private List getMappedChos() {

        if (chos == null) {
            chos = this.lookupService.getSuppliers(true);
        }

        return chos;
    }

    public String getBillingBands() {
            List<ChoBillingBand> choBillingBands;
            try {
                choBillingBands = billingBandService.getChoBillingBands();
                jsonData = new ArrayList<>();
                for (ChoBillingBand h : choBillingBands) {
                    jsonData.add(new BillingBandViewData(h));
                }
            } catch (Exception ex) {
                LOG.error("Exception thrown getting CHO Billing Bands: {}", ex.getMessage());
            }

        return SUCCESS;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(jsonData);
        return "{totalCount:" + this.jsonData.size() + ",results:" + jObject.toString() + "}";
    }
    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String addNewChoBillingBand() {
        actionResponse = new ActionResponse();
        setActionResponse(actionResponse);

        ChoBillingBand band = new ChoBillingBand();
        Chorganisation cho = choService.getChorganisation(billingChoId);
        band.setChorganisation(cho);
        band.setBandName(choBillingBandName);
        band.setCostPerClaim(choCostPerClaim);
        band.setExcludeSupplementary(choExcludeSupplementary);
        band.setTriggerStatus(triggerPoint);
        
        try {
            billingBandService.saveBillingBand(band);
        } catch (Exception ex) {
            LOG.warn("Error saving CHO Billing Band: {}", ex.getMessage());
            actionResponse.AddError("An internal error occurred trying to save the CHO Billing Band.");
        }
        return SUCCESS;
    }

    public String deleteChoBillingBand() {
        actionResponse = new ActionResponse();
        try {
            billingBandService.deleteBillingBand(billingBandService.getChoBillingBand(billingBandId));
        } catch (Exception ex) {
            LOG.warn("Error deleting CHO Billing Band: {}", ex.getMessage());
            actionResponse.AddError("An internal error occurred trying to delete the CHO Billing Band.");
        }
        return SUCCESS;
    }
}
