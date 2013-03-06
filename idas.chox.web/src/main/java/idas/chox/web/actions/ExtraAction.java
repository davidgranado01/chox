package idas.chox.web.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Invoice;
import idas.chox.service.security.TabAccessibility;

/**
 *
 * @author Emmanuel
 */
public class ExtraAction extends ClaimModelAction<Invoice> {

    private static final Logger LOG = LoggerFactory.getLogger(ExtraAction.class);
    private Integer miscellaneousQty;
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

    @Override
    public Invoice loadModel() {

        Invoice invoice = getClaim().getInvoice();
        if (invoice != null) {
            return invoice;
        }
        return new Invoice();
    }

    @Override
    public String updateModel() {
        model.setMiscellaneousQty(miscellaneousQty);
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
        claim.setInvoice(model);
        LOG.debug("claim is saved and calling super.updatemodel");
        return super.updateModel();
    }
    
    @Override
    public void validate() {
        if (claim != null) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("ExtraAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.debug("ExtraAction validate success");
        }
        else {
            LOG.debug(" ExtraAction validation is not done as claim is null");
        }
    }
  
    @Override
    String getTabName() {
        return TabAccessibility.TAB_INVOICE_DETAIL;
    }

    public Integer getEstateQty() {
        return estateQty;
    }

    public void setEstateQty(Integer estateQty) {
        this.estateQty = estateQty;
    }

    public Integer getMiscellaneousQty() {
        return miscellaneousQty;
    }

    public void setMiscellaneousQty(Integer miscellaneousQty) {
        this.miscellaneousQty = miscellaneousQty;
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
