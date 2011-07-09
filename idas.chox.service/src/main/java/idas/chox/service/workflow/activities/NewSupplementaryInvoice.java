/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow.activities;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.BreBandService;
import java.util.Date;
import java.util.List;
import org.springframework.security.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author seeni
 */
public class NewSupplementaryInvoice extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(NewTpiClaim.class);

    @Override
    protected void validate(Claim claim) throws Exception {

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO")) {
            throw new AccessDeniedException("Not in correct role to create a claim.");
        }
    }

    @Override
    protected void beforeProcess(Claim claim) {
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {

        BreBandService breBandService = getWorkflowContext().getBreBandService();
        if (claim.getStatus() == null) {
            List<Claim> claims = getWorkflowContext().getClaimService().getClaimsByCustomerClaimRef(claim.getCustomer().getClaimReference(), getWorkflowContext().getSecurityInfoProvider().getCurrentUser().getChorganisation().getId());
            claim.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
            claim.setSupplementaryInvoicedClaim(true);
            claim.setStatusModifiedDate(new Date());
            BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
            claim.setBreBand(choBand);
            if (claims.size() >= 1) {
                Claim claim1 = claims.get(0);
                Comment comment = Comment.New(0, "This is a supplementary Invoice. The original claim's supplier reference is "+claim1.getChoReference()+".");
                claim.addComment(comment);
            } else {
                Comment comment = Comment.New(0, "This is a supplementary Invoice.");
                claim.addComment(comment);
            }
            if (claim.getChorganisation().getPhone() != null && claim.getChorganisation().getPhone().length() > 0) {

                Comment comment = Comment.New(0, "CHO contact number is " + claim.getChorganisation().getPhone());
                claim.addComment(comment);
            }
            claim.setNotOriginalSupplementaryInvoicedClaim(true);
        }

    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(null);
    }

    @Override
    protected String getCurrentStatus() {
        return "";
    }
}
