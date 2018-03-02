package idas.chox.service.workflow.activities;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import idas.chox.core.model.Claim;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;

/**
 *
 * @author john
 */
public class UpdateInsurerClaimNumber extends BaseActivity {

//    private static final Logger LOG = LoggerFactory.getLogger(UpdateCaseWithSolicitor.class);
    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private String claimNumber;
    // </editor-fold>

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = StringEscapeUtils.unescapeHtml4(Jsoup.clean(claimNumber.trim(), Whitelist.none()));
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if ((claimNumber != null && claimNumber.equals(claim.getClaimNumber()))
                || (claimNumber == null) && (claim.getClaimNumber() == null || claim.getClaimNumber().isEmpty())) {
            throw new Exception("Insurer Claim Number has not been updated as it has not changed");
        }
    }
    
    @Override
    protected void doProcess(Claim claim) {
        claim.setClaimNumber(claimNumber);
    }
    
}
