package idas.chox.web.actions;

import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.ArrayList;
import java.util.List;


import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.InsurerBillingBand;
import idas.chox.core.model.LookupItem;
import idas.chox.core.services.BillingBandMappingService;
import idas.chox.core.services.BillingBandService;
import idas.chox.core.services.LookupService;
import idas.chox.web.viewdata.BillingBandViewData;
import org.springframework.security.access.annotation.Secured;

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
    private int insurerId;
    private List<BillingBandViewData> jsonData;

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public void setBillingBandMappingService(BillingBandMappingService billingBandMappingService) {
        this.billingBandMappingService = billingBandMappingService;
    }

    public void setBillingBandService(BillingBandService billingBandService) {
        this.billingBandService = billingBandService;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
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

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(jsonData);
        return "{totalCount:" + this.jsonData.size() + ",results:" + jObject.toString() + "}";
    }

    @Secured({"ROLE_CHOX_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }

}
