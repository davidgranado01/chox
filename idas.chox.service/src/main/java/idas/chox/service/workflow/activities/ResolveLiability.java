package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.LiabilityStatus;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

public class ResolveLiability extends BaseActivity {

    private static final Logger log = Logger.getLogger(ResolveLiability.class);

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private String claimNumber;
    private BigDecimal percentageLiabilityAccepted;
    private int reasonOfRejectionId;
    private BigDecimal percentageLiabilityCho;
    private Date liabilityAgreedDate;
    private LiabilityStatus liabilityStatus;
    // </editor-fold>
    
    @Override
    protected void beforeProcess(Claim claim) throws Exception {
        log.debug("liabilityStatus " + liabilityStatus);
        log.debug("claim liab " + claim.getLiabilityStatus());
        if ( liabilityStatus != null &&! claim.getLiabilityStatus().equals(liabilityStatus)){
            claim.setPercentageLiabilityAccepted(percentageLiabilityAccepted);
            claim.setPercentageLiabilityCho(percentageLiabilityCho);
            claim.setLiabilityAgreedDate(liabilityAgreedDate);
            claim.setLiabilityStatus(liabilityStatus);
        }
    }


    @Override
    protected void doProcess(Claim claim) {
        log.debug("claim status " + claim.getLiabilityStatus());
        if ( claim.getLiabilityStatus() != null &&
            ( claim.getLiabilityStatus().equals(LiabilityStatus.LIABILITY_DISPUTED)
             || claim.getLiabilityStatus().equals(LiabilityStatus.LIABILITY_OUTSTANDING)
             || claim.getLiabilityStatus().equals(LiabilityStatus.LIABILITY_REPUDIATED))) {
            claim.setStatus(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
        }else{
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        }
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
        expectingStatuses.add(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
    }

    /**
     * @return the claimNumber
     */
    public String getClaimNumber() {
        return claimNumber;
    }

    /**
     * @param claimNumber the claimNumber to set
     */
    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
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
     * @return the reasonOfRejectionId
     */
    public int getReasonOfRejectionId() {
        return reasonOfRejectionId;
    }

    /**
     * @param reasonOfRejectionId the reasonOfRejectionId to set
     */
    public void setReasonOfRejectionId(int reasonOfRejectionId) {
        this.reasonOfRejectionId = reasonOfRejectionId;
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
    
}
