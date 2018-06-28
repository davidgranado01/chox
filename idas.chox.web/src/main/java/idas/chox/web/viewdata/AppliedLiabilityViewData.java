package idas.chox.web.viewdata;

import java.math.BigDecimal;

import idas.chox.core.model.BreAppliedLiability;

/**
 *
 * @author john
 */
public class AppliedLiabilityViewData {
    private int id;
    private int claimTypeId;
    private String claimTypeName;
    private BigDecimal appliedLiability;
    private String applyToRepudiated;
    private boolean removed;

    public AppliedLiabilityViewData(){}

    public AppliedLiabilityViewData(BreAppliedLiability object) {
        if (object.getId() != null) {
            this.id = object.getId();
        }
        this.claimTypeId = object.getClaimType().getClaimTypeValue();
        this.claimTypeName = object.getClaimType().toString();
        this.applyToRepudiated = object.isApplyToRepudiated() ? "Yes" : "No";
        this.appliedLiability = object.getAppliedLiability().setScale(2);        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClaimTypeId() {
        return claimTypeId;
    }

    public void setClaimTypeId(int claimTypeId) {
        this.claimTypeId = claimTypeId;
    }

    public String getClaimTypeName() {
        return claimTypeName;
    }

    public void setClaimTypeName(String claimTypeName) {
        this.claimTypeName = claimTypeName;
    }

    public BigDecimal getAppliedLiability() {
        return appliedLiability;
    }

    public void setAppliedLiability(BigDecimal appliedLiability) {
        this.appliedLiability = appliedLiability;
    }

    public String getApplyToRepudiated() {
        return applyToRepudiated;
    }

    public void setApplyToRepudiated(String applyToRepudiated) {
        this.applyToRepudiated = applyToRepudiated;
    }


    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

}
