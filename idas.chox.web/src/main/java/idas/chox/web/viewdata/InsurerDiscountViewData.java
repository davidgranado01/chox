/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.viewdata;

import idas.chox.core.model.InsurerDiscount;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

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
    
    public InsurerDiscountViewData(InsurerDiscount insurerDiscount) {
    
        this.dateFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(insurerDiscount.getDateFrom());
        this.dateTo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(insurerDiscount.getDateTo());
        this.discount = insurerDiscount.getDiscountPercentage();
        this.discountId = insurerDiscount.getId();
        this.createdBy = insurerDiscount.getCreatedBy().getDisplayName();
        this.createdDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(insurerDiscount.getCreatedDate());
        this.choName = insurerDiscount.getChOrganisation().getName();
        
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
