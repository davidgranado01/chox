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
public class ReferFraudCheck extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ReferFraudCheck.class);

    
    @Override
    @Secured ({"ROLE_INS"})
    protected void doProcess(Claim claim) throws Exception {
        LOG.info("Refer claim '{}' to Keoghs...", claim.getChoReference());
        claim.setSentToKeoghs(true);
        claim.addComment(Comment.newComment(1, "This claim has been referred to Keoghs."));
        // TODO: send email
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
