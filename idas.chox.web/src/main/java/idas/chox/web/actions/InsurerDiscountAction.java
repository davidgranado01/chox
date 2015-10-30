package idas.chox.web.actions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import idas.chox.core.model.*;
import idas.chox.core.services.InsurerDiscountService;
import idas.chox.core.services.LookupService;
import idas.chox.web.viewdata.InsurerDiscountViewData;
import org.apache.commons.lang3.StringEscapeUtils;

public class InsurerDiscountAction extends BaseAction implements ModelDriven<InsurerDiscount>, Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(InsurerDiscountAction.class);
    private LookupService lookupService;
    private InsurerDiscountService insurerDiscountService;
    private List<Chorganisation> suppliers;
    private InsurerDiscount model;
    private int discountId;
    private int insurerId;
    private int choId;
    private String jsonData;

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public int getChoId() {
        return choId;
    }

    public void setChoId(int choid) {
        this.choId = choid;
    }

    public void setInsurerDiscountService(InsurerDiscountService insurerDiscountService) {
        this.insurerDiscountService = insurerDiscountService;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @Override
    public String execute() throws Exception {

        LOG.debug("called in execute method");
        return SUCCESS;
    }

    public List<Chorganisation> getSuppliers() {
        if (suppliers == null) {
            if (getIsChoxAdmin()) {
                suppliers = this.lookupService.getSuppliers(this.insurerId,false); // inlucde manual CHO.
            } else if (getIsInsurer()) {
                suppliers = this.lookupService.getSuppliers(false); // inlucde manual CHO.
            }
        }
        return suppliers;
    }

    public String getSuppliersJsonString() {
        List<LookupItem> luItems = new ArrayList<>(getSuppliers().size());
        for (Chorganisation supplier : suppliers) {
            luItems.add(new LookupItem(supplier.getId().toString(), supplier.getName()));
        }
        return StringEscapeUtils.escapeEcmaScript("{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}");
    }

    public String getClaimTypesJsonString() {
        List<LookupItem> claimTypesList = new ArrayList<>();
        for (ClaimType claimType : ClaimType.getMainClaimTypes()) {
            claimTypesList.add(new LookupItem(claimType.toString(), Integer.toString(claimType.getClaimTypeValue())));
        }
        return "{totalCount:" + claimTypesList.size() + ", results:" + JSONArray.fromObject(claimTypesList).toString() + "}";
    }

    public String getInsurerDiscountTypeJsonString() {
        List<LookupItem> luItems = new ArrayList<>(InsurerDiscountType.values().length);
        for (InsurerDiscountType insurerDiscountType : InsurerDiscountType.values()) {
            luItems.add(new LookupItem(insurerDiscountType.toString(), Integer.toString(insurerDiscountType.getInsurerDiscountTypeValue())));
        }
        return "{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}";
    }
    
    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String addOrUpdateDiscount() throws Exception {
        Map result = new HashMap();
        if (getIsInsurer()) {
            insurerId = getAuthenticatedUser().getInsurer().getId();
        }
        if(model.getDiscountPercentage().compareTo(BigDecimal.ZERO)<=0){
            Map error = new HashMap();
            LOG.info("Discount Percentage can not be less than or equal to 0",model.getDateFrom(),model.getDateTo());
            result.put("success", Boolean.FALSE);
            error.put("discountPercentage", "Discount Percentage can not be less than or equal to 0");
            result.put("errors",error);
        }
        else if (model.getDateFrom().after(model.getDateTo())) {
            Map error = new HashMap();
            LOG.info("date from {} is not earlier than date to {}",model.getDateFrom(),model.getDateTo());
            result.put("success", Boolean.FALSE);
            error.put("dateFrom", "'Date From' should be earlier than 'Date To'");
            result.put("errors",error);
        } else {
            try {
                checkVersion(Arrays.asList(model));
                result = insurerDiscountService.addOrUpdateDiscount(insurerId, choId, model);
            } catch (Exception ex) {
                Map error = new HashMap();
                LOG.error("Exception in addDiscount(): ", ex);
                result.put("success", Boolean.FALSE);
                error.put("error", "Unexpected error occurred, Please contact Chox support.");
                result.put("errors",error);
            }
        }
        JSONObject jsonObject = JSONObject.fromObject(result);
        setJsonData(jsonObject.toString());
        LOG.debug("Returning json string: '{}'", jsonObject.toString());
        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String listDiscountGridData() {
        List<InsurerDiscountViewData> viewList = new ArrayList<>();
        List<InsurerDiscount> discountList;
        if (getIsInsurer()) {
            insurerId = getAuthenticatedUser().getInsurer().getId();
        }

        discountList = insurerDiscountService.getInsurerDiscount(choId, insurerId);
        for (InsurerDiscount object : discountList) {
            InsurerDiscountViewData dvd = new InsurerDiscountViewData(object);
            LOG.debug(dvd.toString());
            viewList.add(dvd);
        }

        setJsonData("{totalCount:" + viewList.size() + ", results:" + JSONArray.fromObject(viewList).toString() + "}");
        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String deleteInsurerDiscount() {
        try {
            LOG.debug("Delete insurer discount");
            if (model.getId() != null && model.getId() > 0) {
                if (getIsInsurer() && model.getInsurer().getId() != getAuthenticatedUser().getInsurer().getId().intValue()) {
                    throw new AccessDeniedException("Cannot delete Insurer Discount that does not belong to you.");
                }
                checkVersion(Arrays.asList(model));
                Map hm = insurerDiscountService.deleteInsurerDiscount(model);
                JSONObject jsonObject = JSONObject.fromObject(hm);
                setJsonData(jsonObject.toString());
                LOG.debug("Back from deleteInsurerDiscount");
            }

        } catch (Exception ex) {
            Map hm = new HashMap();
            LOG.error("Exception in deleteInsurerDiscount: ", ex);
            hm.put("success", Boolean.FALSE);
            JSONObject jsonObject = JSONObject.fromObject(hm);
            setJsonData(jsonObject.toString());
        }
        return SUCCESS;
    }

    @Override
    public InsurerDiscount getModel() {
        return model;
    }

    public void setModel(InsurerDiscount model) {
        this.model = model;
    }

    @Override
    public void prepare() throws Exception {
        try {
            model = new InsurerDiscount();
            if (discountId > 0) {
                model = insurerDiscountService.getInsurerDiscount(discountId);
                addModelToSession(Arrays.asList(model));
            }
        } catch (Exception ex) {
            handleException(ex);
        }
    }
}
