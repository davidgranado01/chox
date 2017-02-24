package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author john
 */
public class ClaimMatchingImportEntry implements Serializable, Versioned {
    private Integer id;
    private Integer version;
    private Date createdDate;
    private String insurerName;
    private String claimNumber;
    private String thirdPartyVehicleRegistration;
    private Date incidentDate;
    private String indemnityStance;
    private String liabilityStance;
    private BigDecimal liabilityInsurer;
    private Claim claim;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getInsurerName() {
        return insurerName;
    }

    public void setInsurerName(String insurerName) {
        this.insurerName = insurerName;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public Date getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(Date incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getIndemnityStance() {
        return indemnityStance;
    }

    public void setIndemnityStance(String indemnityStance) {
        this.indemnityStance = indemnityStance;
    }

    public String getLiabilityStance() {
        return liabilityStance;
    }

    public void setLiabilityStance(String liabilityStance) {
        this.liabilityStance = liabilityStance;
    }

    public BigDecimal getLiabilityInsurer() {
        return liabilityInsurer;
    }

    public void setLiabilityInsurer(BigDecimal liabilityInsurer) {
        this.liabilityInsurer = liabilityInsurer;
    }

    public String getThirdPartyVehicleRegistration() {
        return thirdPartyVehicleRegistration;
    }

    public void setThirdPartyVehicleRegistration(String thirdPartyVehicleRegistration) {
        this.thirdPartyVehicleRegistration = thirdPartyVehicleRegistration;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }
    
    public boolean isTransient() {
        return id == null || id <= 0;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entity)) {
            return false;
        }

        Entity other = (Entity) o;

        // if the id is missing, return false
        if (id == null) {
            return false;
        }

        // equivalence by id
        return id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        } else {
            return super.hashCode();
        }
    }
    
}
