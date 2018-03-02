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
public class UpdateCustomerClaimNumber extends BaseActivity {
    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private String customerClaimNumber;
    // </editor-fold>

    public String getCustomerClaimNumber() {
        return customerClaimNumber;
    }

    public void setCustomerClaimNumber(String customerClaimNumber) {
        this.customerClaimNumber = StringEscapeUtils.unescapeHtml4(Jsoup.clean(customerClaimNumber.trim(), Whitelist.none()));
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if ((customerClaimNumber != null && customerClaimNumber.equals(claim.getCustomer().getClaimReference()))
                || (customerClaimNumber == null) && (claim.getCustomer().getClaimReference() == null || claim.getCustomer().getClaimReference().isEmpty())) {
            throw new Exception("Customer Claim Number has not been updated as it has not changed");
        }
    }
    
    @Override
    protected void doProcess(Claim claim) {
        String originalCustomerClaimNumber = claim.getCustomer().getClaimReference();
        claim.getCustomer().setClaimReference(customerClaimNumber);
        // Set the customerClaimNumber to the original value so that this can be used in the event
        setCustomerClaimNumber(originalCustomerClaimNumber);
    }
    
}
