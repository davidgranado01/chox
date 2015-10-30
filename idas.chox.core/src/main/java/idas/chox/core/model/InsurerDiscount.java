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
    private BigDecimal discountPercentage;
    private InsurerDiscountType insurerDiscountType;
    private boolean appliedToPenalties;
    private ClaimType claimType;

    public boolean isAppliedToPenalties() {
        return appliedToPenalties;
    }

    public void setAppliedToPenalties(boolean appliedToPenalties) {
        this.appliedToPenalties = appliedToPenalties;
    }

    public InsurerDiscountType getInsurerDiscountType() {
        return insurerDiscountType;
    }

    public void setInsurerDiscountType(InsurerDiscountType insurerDiscountType) {
        this.insurerDiscountType = insurerDiscountType;
    }

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

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discount) {
        this.discountPercentage = discount;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }
    
}
