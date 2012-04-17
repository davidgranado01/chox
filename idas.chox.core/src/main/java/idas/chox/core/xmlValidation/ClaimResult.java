package idas.chox.core.xmlValidation;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Injury;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.Solicitor;
import idas.chox.core.model.Witness;

public class ClaimResult {

    private Claim claim;
    private Invoice invoice;
    private Element element;
    private ClaimParseStatus claimParseStatus;
    private boolean valid;
    private boolean dataValid;
    private boolean checkDataValid;
    private List<String> message = new ArrayList<String>();
    private boolean duplicateClaimInSameXmlFile;
    private ArrayList<Witness> witnesses;
    private ArrayList<Injury> injuries;
    private ArrayList<Solicitor> solicitors;

    public boolean isDuplicateClaimInSameXmlFile() {
        return duplicateClaimInSameXmlFile;
    }

    public void setDuplicateClaimInSameXmlFile(boolean duplicateClaimInSameXmlFile) {
        this.duplicateClaimInSameXmlFile = duplicateClaimInSameXmlFile;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public ArrayList<Injury> getInjuries() {
        return injuries;
    }

    public void setInjuries(ArrayList<Injury> injuries) {
        this.injuries = injuries;
    }

    public ArrayList<Solicitor> getSolicitors() {
        return solicitors;
    }

    public void setSolicitors(ArrayList<Solicitor> solicitors) {
        this.solicitors = solicitors;
    }

    public ArrayList<Witness> getWitnesses() {
        return witnesses;
    }

    public void setWitnesses(ArrayList<Witness> witnesses) {
        this.witnesses = witnesses;
    }

    public boolean isCheckDataValid() {
        return checkDataValid;
    }

    public void setCheckDataValid(boolean checkDataValid) {
        this.checkDataValid = checkDataValid;
    }

    public boolean isDataValid() {
        return dataValid;
    }

    public void setDataValid(boolean dataValid) {
        this.dataValid = dataValid;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public ClaimParseStatus getClaimParseStatus() {
        return claimParseStatus;
    }

    public void setClaimParseStatus(ClaimParseStatus claimParseStatus) {
        this.claimParseStatus = claimParseStatus;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getProcessStatus() {

        String processStatus = "Failed";

        if (this.dataValid && this.valid) {

            processStatus = "Uploaded";

            if (this.claimParseStatus.equals(ClaimParseStatus.EXIST_CLAIM)) {
                processStatus = "Updated";
            }

        }

        return processStatus;
    }

    public String getClaimStatus() {

        String sReturn = "N/A";

        if (this.claim != null && this.claim.getStatus() != null) {
            sReturn = this.claim.getStatus();
        }

        return sReturn;
    }

    public String getUploadedStatus() {
        return this.claimParseStatus.getDescription();
    }
}