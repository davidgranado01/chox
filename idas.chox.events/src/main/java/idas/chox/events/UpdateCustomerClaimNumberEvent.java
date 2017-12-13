package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class UpdateCustomerClaimNumberEvent extends BaseActivityEvent {
    public UpdateCustomerClaimNumberEvent(){};

    public UpdateCustomerClaimNumberEvent(final Claim claim, String activityName, String oldCustomerClaimNumber) {
        super(claim, activityName);
        addAttribute("originalCustomerClaimNumber", oldCustomerClaimNumber);
        addAttribute("customerClaimNumber", claim.getCustomer().getClaimReference());
    }
}
