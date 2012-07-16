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

    private String dateFrom;
    private String dateTo;
    private BigDecimal discount;
    private int discountId;
    private String createdBy;
    private String createdDate;
    private String choName;
    private boolean appliedToPenalties; 
    private String insurerDiscountType;
    private int choId;

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
        this.appliedToPenalties = insurerDiscount.isAppliedToPenalties();
        this.choId = insurerDiscount.getChOrganisation().getId();

    }

    public int getChoId() {
        return choId;
    }

    public void setChoId(int choId) {
        this.choId = choId;
    }

    public String getInsurerDiscountType() {
        return insurerDiscountType;
    }

    public void setInsurerDiscountType(String insurerDiscountType) {
        this.insurerDiscountType = insurerDiscountType;
    }

    public boolean isAppliedToPenalties() {
        return appliedToPenalties;
    }

    public void setAppliedToPenalties(boolean appliedToPenalties) {
        this.appliedToPenalties = appliedToPenalties;
    }

    public String getChoName() {
        return choName;
    }

    public void setChoName(String choName) {
        this.choName = choName;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
