/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author seeni
 */
public class InsurerDiscount extends Entity{
    
    private Insurer insurer;
    private Chorganisation chOrganisation;
    private Date dateFrom;
    private Date dateTo;
    private BigDecimal discountAmount;

    public Chorganisation getChOrganisation() {
        return chOrganisation;
    }

    public void setChOrganisation(Chorganisation chOrganisation) {
        this.chOrganisation = chOrganisation;
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

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discount) {
        this.discountAmount = discount;
    }
    
}
