package idas.chox.core.model;

import java.io.Serializable;

/**
 *
 * @author john
 */
public class PaidInvoiceEntry extends Entity implements Serializable, Versioned {
    private String insurerName;
    private String choReference;
    private String claimNumber;

    public String getInsurerName() {
        return insurerName;
    }

    public void setInsurerName(String insurerName) {
        this.insurerName = insurerName;
    }

    public String getChoReference() {
        return choReference;
    }

    public void setChoReference(String choReference) {
        this.choReference = choReference;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

}
