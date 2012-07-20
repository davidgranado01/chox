package idas.chox.data;

import idas.chox.core.model.Invoice;


public class ExcelInvoice {
    private String claimStatus;
    private String choReference;
    private String thirdPartyClaimReference;
    private Invoice invoice;
    private String hirePenaltyPercentageString;
    private String repairPenaltyPercentageString;

    public String getChoReference() {
        return choReference;
    }

    public void setChoReference(String choReference) {
        this.choReference = choReference;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getThirdPartyClaimReference() {
        return thirdPartyClaimReference;
    }

    public void setThirdPartyClaimReference(String thirdPartyClaimReference) {
        this.thirdPartyClaimReference = thirdPartyClaimReference;
    }

    public String getHirePenaltyPercentageString() {
        return hirePenaltyPercentageString;
    }

    public void setHirePenaltyPercentageString(String hirePenaltyPercentageString) {
        this.hirePenaltyPercentageString = hirePenaltyPercentageString;
    }

    public String getRepairPenaltyPercentageString() {
        return repairPenaltyPercentageString;
    }

    public void setRepairPenaltyPercentageString(String repairPenaltyPercentageString) {
        this.repairPenaltyPercentageString = repairPenaltyPercentageString;
    }
}
