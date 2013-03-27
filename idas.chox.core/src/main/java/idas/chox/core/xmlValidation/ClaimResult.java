package idas.chox.core.xmlValidation;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Injury;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.Solicitor;

public class ClaimResult {

    private Claim claim;
    private Invoice invoice;
    private Element element;
    private ClaimParseStatus claimParseStatus;
    private boolean valid;
    private boolean dataValid;
    private boolean checkDataValid;
    private boolean checkForRepairAnomalies;
    private boolean checkForTotalLossAnomalies;
    private List<String> message = new ArrayList<String>();
    private boolean duplicateClaimInSameXmlFile;
    private List<Injury> injuries;
    private List<Solicitor> solicitors;

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

    public List<Injury> getInjuries() {
        return injuries;
    }

    public void setInjuries(List<Injury> injuries) {
        this.injuries = injuries;
    }

    public List<Solicitor> getSolicitors() {
        return solicitors;
    }

    public void setSolicitors(List<Solicitor> solicitors) {
        this.solicitors = solicitors;
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

    public boolean isCheckForRepairAnomalies() {
        return checkForRepairAnomalies;
    }

    public void setCheckForRepairAnomalies(boolean checkForRepairAnomalies) {
        this.checkForRepairAnomalies = checkForRepairAnomalies;
    }

    public boolean isCheckForTotalLossAnomalies() {
        return checkForTotalLossAnomalies;
    }

    public void setCheckForTotalLossAnomalies(boolean checkForTotalLossAnomalies) {
        this.checkForTotalLossAnomalies = checkForTotalLossAnomalies;
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

            if (this.claimParseStatus.equals(ClaimParseStatus.EXIST_CLAIM)
                    || this.claimParseStatus.equals(ClaimParseStatus.EXISTS_INSURER_CLAIM)
                    || this.claimParseStatus.equals(ClaimParseStatus.EXIST_SUBSCRIBER_CLAIM)
                    || this.claimParseStatus.equals(ClaimParseStatus.EXIST_SUBSCRIBER_CLAIM)
                    || this.claimParseStatus.equals(ClaimParseStatus.EXIST_SUBSCRIBER_CLAIM)
                    || this.claimParseStatus.equals(ClaimParseStatus.EXIST_FIXEDFEE_CLAIM)) {
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