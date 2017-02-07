package idas.chox.web.viewdata;

import idas.chox.core.model.ClaimMatchingBand;
import java.math.BigDecimal;

/**
 *
 * @author john
 */
public class ClaimMatchingBandViewData {
    private int id;
    private int claimTypeId;
    private String claimTypeName;
    private BigDecimal minimumLiability;
    private String autoAcknowledge;
    private String vehicleClasses = "";
    private boolean removed;

    public ClaimMatchingBandViewData(){}

    public ClaimMatchingBandViewData(ClaimMatchingBand object) {
        if (object.getId() != null) {
            this.id = object.getId();
        }
        this.claimTypeId = object.getClaimType().getClaimTypeValue();
        this.claimTypeName = object.getClaimType().toString();
        this.autoAcknowledge = object.isAutoAcknowledge() ? "Yes" : "No";
        this.minimumLiability = object.getLiabilityPercentage();
        
        if (object.isbClass()){ vehicleClasses += ",B";}
        if (object.isCmClass()){ vehicleClasses += ",CM";}
        if (object.isCpClass()){ vehicleClasses += ",CP";}
        if (object.isCsClass()){ vehicleClasses += ",CS";}
        if (object.isCvClass()){ vehicleClasses += ",CV";}
        if (object.isfClass()){ vehicleClasses += ",F";}
        if (object.ismClass()){ vehicleClasses += ",M";}
        if (object.isNtClass()){ vehicleClasses += ",NT";}
        if (object.ispClass()){ vehicleClasses += ",P";}
        if (object.isPtClass()){ vehicleClasses += ",PT";}
        if (object.isPvClass()){ vehicleClasses += ",PV";}
        if (object.isRvClass()){ vehicleClasses += ",RV";}
        if (object.issClass()){ vehicleClasses += ",S";}
        if (object.isSpClass()){ vehicleClasses += ",SP";}
        if (object.istClass()){ vehicleClasses += ",T";}
        if (object.isuClass()){ vehicleClasses += ",UNATTACHED";}
        
        if (vehicleClasses.length() > 1){ vehicleClasses = vehicleClasses.substring(1);} // remove leading ,
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

    public BigDecimal getMinimumLiability() {
        return minimumLiability;
    }

    public void setMinimumLiability(BigDecimal minimumLiability) {
        this.minimumLiability = minimumLiability;
    }

    public String getAutoAcknowledge() {
        return autoAcknowledge;
    }

    public void setAutoAcknowledge(String autoAcknowledge) {
        this.autoAcknowledge = autoAcknowledge;
    }

    public String getVehicleClasses() {
        return vehicleClasses;
    }

    public void setVehicleClasses(String vehicleClasses) {
        this.vehicleClasses = vehicleClasses;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

}
