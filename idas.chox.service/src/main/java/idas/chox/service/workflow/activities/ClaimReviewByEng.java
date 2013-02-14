package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.util.StringHelper;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.security.SecurityInfoProvider;

public class ClaimReviewByEng extends BaseActivity {

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private BigDecimal indemnityAmount;
    private BigDecimal percentageLiabilityAccepted;
    private boolean isQuantumDispute;
    private boolean isInvoiceReviewRequired;
    private String engineerClaimReviewNotes;
    private String supportingLiabilityNotes;
    private BigDecimal percentageLiabilityCho;
    private Date liabilityAgreedDate;
    private LiabilityStatus liabilityStatus;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Parameters">

    // </editor-fold>

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_INS_SCR") && !securityInfoProvider.isInRoleOf("ROLE_INS_MNG")
                && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to review claim.");
        }
    }

    @Override
    protected void beforeProcess(Claim claim) {
        claim.setIndemnityAmount(getIndemnityAmount());
        claim.setIsInvoiceReviewRequired(isIsInvoiceReviewRequired());
        claim.setIsQuantumDispute(isIsQuantumDispute());
    }

    @Override
    protected void doProcess(Claim claim) {

        boolean disablePrivateNotes = getCurrentUser().getInsurer() != null ? 
                        getCurrentUser().getInsurer().isDisablePrivateNotes() : 
                            getCurrentUser().getChorganisation() != null ?
                        getCurrentUser().getChorganisation().isDisablePrivateNotes() :
                            claim.getInsurer().isDisablePrivateNotes();

        if (StringHelper.isNotEmpty(getEngineerClaimReviewNotes())) {
            if(!disablePrivateNotes){
                claim.addComment(Comment.New(1, getEngineerClaimReviewNotes()));
            }else{
                claim.addComment(Comment.New(0, getEngineerClaimReviewNotes()));
            }
        }

        if (StringHelper.isNotEmpty(supportingLiabilityNotes)) {
            claim.addComment(Comment.New(0, "Supporting Liability Notes: " + supportingLiabilityNotes));
        }
        claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_REF_TO_ENG);
    }

    /**
     * @return the indemnityAmount
     */
    public BigDecimal getIndemnityAmount() {
        return indemnityAmount;
    }

    /**
     * @param indemnityAmount the indemnityAmount to set
     */
    public void setIndemnityAmount(BigDecimal indemnityAmount) {
        this.indemnityAmount = indemnityAmount;
    }

    /**
     * @return the percentageLiabilityAccepted
     */
    public BigDecimal getPercentageLiabilityAccepted() {
        return percentageLiabilityAccepted;
    }

    /**
     * @param percentageLiabilityAccepted the percentageLiabilityAccepted to set
     */
    public void setPercentageLiabilityAccepted(BigDecimal percentageLiabilityAccepted) {
        this.percentageLiabilityAccepted = percentageLiabilityAccepted;
    }

    /**
     * @return the isQuantumDispute
     */
    public boolean isIsQuantumDispute() {
        return isQuantumDispute;
    }

    /**
     * @param isQuantumDispute the isQuantumDispute to set
     */
    public void setIsQuantumDispute(boolean isQuantumDispute) {
        this.isQuantumDispute = isQuantumDispute;
    }

    /**
     * @return the isInvoiceReviewRequired
     */
    public boolean isIsInvoiceReviewRequired() {
        return isInvoiceReviewRequired;
    }

    /**
     * @param isInvoiceReviewRequired the isInvoiceReviewRequired to set
     */
    public void setIsInvoiceReviewRequired(boolean isInvoiceReviewRequired) {
        this.isInvoiceReviewRequired = isInvoiceReviewRequired;
    }

    /**
     * @return the engineerClaimReviewNotes
     */
    public String getEngineerClaimReviewNotes() {
        return engineerClaimReviewNotes;
    }

    /**
     * @param engineerClaimReviewNotes the engineerClaimReviewNotes to set
     */
    public void setEngineerClaimReviewNotes(String engineerClaimReviewNotes) {
        this.engineerClaimReviewNotes = engineerClaimReviewNotes;
    }

    /**
     * @return the percentageLiabilityCho
     */
    public BigDecimal getPercentageLiabilityCho() {
        return percentageLiabilityCho;
    }

    /**
     * @param percentageLiabilityCho the percentageLiabilityCho to set
     */
    public void setPercentageLiabilityCho(BigDecimal percentageLiabilityCho) {
        this.percentageLiabilityCho = percentageLiabilityCho;
    }

    /**
     * @return the liabilityAgreedDate
     */
    public Date getLiabilityAgreedDate() {
        return liabilityAgreedDate;
    }

    /**
     * @param liabilityAgreedDate the liabilityAgreedDate to set
     */
    public void setLiabilityAgreedDate(Date liabilityAgreedDate) {
        this.liabilityAgreedDate = liabilityAgreedDate;
    }

    /**
     * @return the liabilityStatus
     */
    public LiabilityStatus getLiabilityStatus() {
        return liabilityStatus;
    }

    /**
     * @param liabilityStatus the liabilityStatus to set
     */
    public void setLiabilityStatus(LiabilityStatus liabilityStatus) {
        this.liabilityStatus = liabilityStatus;
    }

    /**
     * @return the supportingLiabilityNotes
     */
    public String getSupportingLiabilityNotes() {
        return supportingLiabilityNotes;
    }

    /**
     * @param supportingLiabilityNotes the supportingLiabilityNotes to set
     */
    public void setSupportingLiabilityNotes(String supportingLiabilityNotes) {
        this.supportingLiabilityNotes = supportingLiabilityNotes;
    }
}
