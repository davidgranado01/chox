package idas.chox.service.workflow.activities;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;

/**
 *
 * @author john
 */
public class UpdateSupplierReference extends BaseActivity {

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private String supplierReference;
    // </editor-fold>

    public String getSupplierReference() {
        return supplierReference;
    }

    public void setSupplierReference(String supplierReference) {
        this.supplierReference = StringEscapeUtils.unescapeHtml4(Jsoup.clean(supplierReference.trim(), Whitelist.none()));
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if ((supplierReference != null && supplierReference.equals(claim.getClaimNumber()))
                || (supplierReference == null) && (claim.getChoReference()== null || claim.getChoReference().isEmpty())) {
            throw new Exception("Supplier Reference Number has not been updated as it has not changed");
        }
    }
    
    @Override
    protected void doProcess(Claim claim) {
        String originalChoReference = claim.getChoReference();
        claim.addComment(Comment.newComment(0, "Supplier Reference updated from '" + originalChoReference + "' to '" + supplierReference + "'"));
        claim.setChoReference(supplierReference);
        // Set the supplier refeence to the original value so that this can be used in the event
        setSupplierReference(originalChoReference);
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        activityEventGenerator.getEvents(claim, this).forEach((event) -> {
            ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getEventBus().post(event);
        });
    }
    
}
