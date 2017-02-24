package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author john
 */
public class ClaimMatchingEntry extends Entity implements Serializable, Versioned {
    private int matchStatus;
    private String insurerName;
    private String claimNumber;
    private String indemnityStance;
    private String liabilityStance;
    private String thirdPartyVehicleRegistration;
    private Date incidentDate;
    private BigDecimal liabilityInsurer;
    private Claim claim;
    private Insurer insurer;


    public int getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(int matchStatus) {
        this.matchStatus = matchStatus;
    }

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
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

    public String getThirdPartyVehicleRegistration() {
        return thirdPartyVehicleRegistration;
    }

    public void setThirdPartyVehicleRegistration(String thirdPartyVehicleRegistration) {
        this.thirdPartyVehicleRegistration = thirdPartyVehicleRegistration;
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

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }
}
