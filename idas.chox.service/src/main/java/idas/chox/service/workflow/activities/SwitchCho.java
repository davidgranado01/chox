package idas.chox.service.workflow.activities;

import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.InsurerChorganisationService;

public class SwitchCho extends BaseActivity {
    
    private static final Logger LOG = LoggerFactory.getLogger(SwitchCho.class);
    private InsurerChorganisationService insurerChorganisationService;
    private Chorganisation oldCho;
    private BreBandService breBandService;

    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) {
        this.insurerChorganisationService = insurerChorganisationService;
    }

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }
  
    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        if (claim.getInvoice() != null) {
            throw new AccessDeniedException("Cannot switch CHO as it has an invoice attached.");
        }
        if (claim.getChorganisation().getLinkedCho()== null) {
            throw new AccessDeniedException("Cannot switch CHO as no linked CHO is defined.");
        }
        boolean exists = claimService.isClaimSupplierReferenceNumberExistForChoExternal(claim.getChoReference(), claim.getChorganisation().getLinkedCho().getId());

        if (exists) {
            throw new Exception("Cannot switch CHO as the CHO already has a claim with the same CHO Reference Number.");
        }
        
        Chorganisation newCho = claim.getChorganisation().getLinkedCho();
        if (ClaimType.isSubscriber(claim.getClaimType())) {
            // Make sure the new Insurer accepts subscriber claims
            if (!newCho.isEnableSubscriberClaims()) {
                LOG.debug("The linked CHO '{}' does not allow Subscriber claims.", newCho.getName());
                throw new Exception("The linked CHO does not allow Subscriber claims.");
            }
        } else if (ClaimType.isFixedFee(claim.getClaimType())) {
            // Make sure the new Insurer accepts fixed fee claims
            if (!newCho.isEnableFixedFeeClaims()) {
                LOG.debug("The linked CHO '{}' does not allow Fixed Fee claims.", newCho.getName());
                throw new Exception("The linked CHO does not allow Fixed Fee claims.");
            }
        } else if (ClaimType.isTPI(claim.getClaimType())) {
            // Make sure the new Insurer accepts tpi claims
            if (!newCho.isThirdPartyInterventionActivated()) {
                LOG.debug("The linked CHO '{}' does not allow TPI claims.", newCho.getName());
                throw new Exception("The linked CHO does not allow TPI claims.");
            }
        } else if (ClaimType.isCollaborationProtocol(claim.getClaimType())) {
            // Make sure the new Insurer accepts Collaboration Protocol claims
            if (!newCho.isEnableCollaborationProtocolClaims()) {
                LOG.debug("The linked CHO '{}' does not allow Collaboration Protocol claims.", newCho.getName());
                throw new Exception("The linked CHO does not allow Collaboration Protocol claims.");
            }
        }
        
        if (insurerChorganisationService.getInsurerChorganisations(claim.getInsurer().getId(), newCho.getId()).size() <= 0) {
            LOG.warn("The linked CHO '{}' is not mapped to the Insurer '{}'.", newCho.getName(), claim.getInsurer().getName());
            throw new Exception("The linked CHO '" + newCho.getName() + "' is not mapped to the Insurer'" + claim.getInsurer().getName() + "'.");
        }
    }

    
    @Override
    protected void doProcess(Claim claim) {
        LOG.debug("Switching CHO for claim: {} (id={})", claim.getChoReference(), claim.getId());

        oldCho = claim.getChorganisation();
        Chorganisation newCho = oldCho.getLinkedCho();
        LOG.debug("Switching CHO for claim with CHO reference '{}' to {}", claim.getChoReference(), newCho.getName());
        
        claim.setChorganisation(newCho);
        claim.setSupplierClaimOwner(null);
        LOG.debug("Switching CHO : Claim details has been updated");

        String newComment = "Claim switched from '" + oldCho.getName() + "' to '" + newCho.getName() +"'";
        Comment comment = Comment.newComment(0, newComment);
        claim.addComment(comment);
        setMessage(newComment);
        LOG.debug("Switching Claim: Comment has been updated");

        // Add note containing the new CHO telephone number
        if (newCho.getPhone() != null && newCho.getPhone().length() > 0) {
                comment = Comment.newComment(0, "New CHO contact number is " + claim.getChorganisation().getPhone());
                claim.addComment(comment);
        }

        // Set Claim BRE band
        BreBand choBand = breBandService.getBreBand(newCho.getId(), claim.getInsurer().getId());
        claim.setBreBand(choBand);
        
        // Add General Note (specified in BRE band)
        if (choBand != null && choBand.getClaimUploadNote() != null && !choBand.getClaimUploadNote().trim().isEmpty()) {
            comment = Comment.newComment(0, choBand.getClaimUploadNote());
            claim.addComment(comment);
        }
    
    }

    public Chorganisation getOldCho() {
        return oldCho;
    }
}
