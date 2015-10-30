package idas.chox.web.viewdata;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;

import idas.chox.core.model.InsurerDiscount;

/**
 *
 * @author seeni
 */
public class InsurerDiscountViewData {

    private final String dateFrom;
    private final String dateTo;
    private final BigDecimal discount;
    private final int discountId;
    private final String createdBy;
    private final String createdDate;
    private final String choName;
    private final String appliedToPenalties; 
    private final String insurerDiscountType;
    private final int choId;
    private final String claimType;

    public InsurerDiscountViewData(InsurerDiscount insurerDiscount) {
        
        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.dateFrom = dateFormat.format(insurerDiscount.getDateFrom());
        this.dateTo = dateFormat.format(insurerDiscount.getDateTo());
        this.discount = insurerDiscount.getDiscountPercentage();
        this.discountId = insurerDiscount.getId();
        this.createdBy = insurerDiscount.getCreatedBy().getDisplayName();
        this.createdDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(insurerDiscount.getCreatedDate());
        this.choName = insurerDiscount.getChOrganisation().getName();
        this.insurerDiscountType = insurerDiscount.getInsurerDiscountType().toString();
        if (insurerDiscount.isAppliedToPenalties()) {
            this.appliedToPenalties = "Yes";
        } else {
            this.appliedToPenalties = "No";
        }
        this.choId = insurerDiscount.getChOrganisation().getId();
        this.claimType = insurerDiscount.getClaimType().toString();
    }

    public int getChoId() {
        return choId;
    }


    public String getInsurerDiscountType() {
        return insurerDiscountType;
    }

    public String getAppliedToPenalties() {
        return appliedToPenalties;
    }

    public String getChoName() {
        return choName;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public int getDiscountId() {
        return discountId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getClaimType() {
        return claimType;
    }

}
