/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Invoice;
import idas.chox.core.services.InvoiceService;
import idas.chox.web.security.ApplicationAccessibility;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class ExtraAction extends BaseModelAction implements ModelDriven<Invoice>, Preparable {

    private InvoiceService service;
    private Invoice model;
    private Integer cdwQty;
    private Integer automaticQty;
    private Integer satNavQty;
    private Integer babySeatQty;
    private Integer towBarsQty;
    private Integer nonStandardInsurancePremiumQty;
    private Integer adminQty;
    private Integer roofRackQty;
    private Integer dualControlQty;
    private Integer deliveryCollectionQty;
    private Integer estateQty;

    public void setInvoiceService(InvoiceService service) {
        this.service = service;
    }

    public Invoice getModel() {
        return model;
    }

    public void prepare() throws Exception {
        if (objectId <= 0) {
            model = new Invoice();
        } else {
            model = service.getObject(objectId);
        }
    }

    public String updateModel() {
        try {
            model.setCdwQty(cdwQty);
            model.setAutomaticQty(automaticQty);
            model.setSatNavQty(satNavQty);
            model.setBabySeatQty(babySeatQty);
            model.setTowBarsQty(towBarsQty);
            model.setNonStandardInsurancePremiumQty(nonStandardInsurancePremiumQty);
            model.setAdminQty(adminQty);
            model.setRoofRackQty(roofRackQty);
            model.setDualControlQty(dualControlQty);
            model.setDeliveryCollectionQty(deliveryCollectionQty);
            model.setEstateQty(estateQty);
            this.service.updateObject(model);
            this.actionResult = "1";
        } catch (Exception ex) {
            this.actionResult = "ERROR :" + ex.getMessage();
        }
        return SUCCESS;
    }

    public String getJsonData() {
        JSONObject jObject = JSONObject.fromObject(this.model);
        return jObject.toString();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_INVOICE_DETAIL;
    }

    public Integer getEstateQty() {
        return estateQty;
    }

    public void setEstateQty(Integer estateQty) {
        this.estateQty = estateQty;
    }

    public Integer getCdwQty() {
        return cdwQty;
    }

    public void setCdwQty(Integer cdwQty) {
        this.cdwQty = cdwQty;
    }

    public Integer getAutomaticQty() {
        return automaticQty;
    }

    public void setAutomaticQty(Integer automaticQty) {
        this.automaticQty = automaticQty;
    }

    public Integer getSatNavQty() {
        return satNavQty;
    }

    public void setSatNavQty(Integer satNavQty) {
        this.satNavQty = satNavQty;
    }

    public Integer getBabySeatQty() {
        return babySeatQty;
    }

    public void setBabySeatQty(Integer babySeatQty) {
        this.babySeatQty = babySeatQty;
    }

    public Integer getTowBarsQty() {
        return towBarsQty;
    }

    public void setTowBarsQty(Integer towBarsQty) {
        this.towBarsQty = towBarsQty;
    }

    public Integer getNonStandardInsurancePremiumQty() {
        return nonStandardInsurancePremiumQty;
    }

    public void setNonStandardInsurancePremiumQty(Integer nonStandardInsurancePremiumQty) {
        this.nonStandardInsurancePremiumQty = nonStandardInsurancePremiumQty;
    }

    public Integer getAdminQty() {
        return adminQty;
    }

    public void setAdminQty(Integer adminQty) {
        this.adminQty = adminQty;
    }

    public Integer getRoofRackQty() {
        return roofRackQty;
    }

    public void setRoofRackQty(Integer roofRackQty) {
        this.roofRackQty = roofRackQty;
    }

    public Integer getDualControlQty() {
        return dualControlQty;
    }

    public void setDualControlQty(Integer dualControlQty) {
        this.dualControlQty = dualControlQty;
    }

    public Integer getDeliveryCollectionQty() {
        return deliveryCollectionQty;
    }

    public void setDeliveryCollectionQty(Integer deliveryCollectionQty) {
        this.deliveryCollectionQty = deliveryCollectionQty;
    }
}
