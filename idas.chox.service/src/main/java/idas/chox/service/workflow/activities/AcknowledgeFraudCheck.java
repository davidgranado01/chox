package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;

/**
 *
 * @author John
 */
public class AcknowledgeFraudCheck extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(AcknowledgeFraudCheck.class);


    @Override
    @Secured ({"ROLE_INS"})
    protected void doProcess(Claim claim) throws Exception {
        LOG.info("Acknowledge Keoghs Fraud Check on claim '{}' to Keoghs...", claim.getChoReference());
        claim.setFraudResultAcknowledged(true);
        claim.addComment(Comment.newComment(1, "The Keoghs Fraud Check result has been acknolwedged."));
    }
    
}
