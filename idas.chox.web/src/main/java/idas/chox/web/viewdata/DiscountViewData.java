/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.viewdata;

import idas.chox.core.model.InsurerDiscount;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author seeni
 */
public class DiscountViewData {
    
    private Date dateFrom;
    private Date dateTo;
    private BigDecimal discount;
    private int discountId;
    private String createdBy;
    private String createdDate;
    
    public DiscountViewData(InsurerDiscount insurerDiscount) {
    
        this.dateFrom = insurerDiscount.getDateFrom();
        this.dateTo = insurerDiscount.getDateTo();
        this.discount = insurerDiscount.getDiscountAmount();
        this.discountId = insurerDiscount.getId();
        this.createdBy = insurerDiscount.getCreatedBy().getDisplayName();
        this.createdDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(insurerDiscount.getCreatedDate());
        
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
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
