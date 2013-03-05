package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.util.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlaExtension extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(SlaExtension.class);
    private int slaExtDays;


    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {

        if ((ClaimType.isSubscriber(claim.getClaimType()) && (slaExtDays > (DateHelper.SUBSCRIBER_SLA_DAYS + claim.getChorganisation().getMaxAllowedSlaExtForSubscriber())))
                || (ClaimType.isFixedFee(claim.getClaimType()) && (slaExtDays > (DateHelper.FIXED_FEE_SLA_DAYS + claim.getChorganisation().getMaxAllowedSlaExtForFixedFee())))) {
            LOG.error("The applied SLA extension days are not valid.");
            throw new Exception("Given SLA extension days are not correct");
        } else {
            claim.setSlaExtDays(slaExtDays);

            String newComment = claim.getSlaExtDays() + " day extension granted.";
            if (claim.getSlaExtDays() > 1) {
                newComment = claim.getSlaExtDays() + " days extension granted.";
            }
            Comment comment = Comment.New(0, newComment);
            claim.addComment(comment);
        }
    }

    public void setSlaExtDays(int slaExtDays) {
        this.slaExtDays = slaExtDays;
    }
}