package idas.chox.web;

import idas.chox.core.model.Invoice;


public class ExcelInvoice {
    protected String claimStatus;
    protected String choReference;
    protected String thirdPartyClaimReference;
    protected Invoice invoice;

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


}
