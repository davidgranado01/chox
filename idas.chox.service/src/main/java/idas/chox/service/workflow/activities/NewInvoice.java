/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;

/**
 *
 * @author emmanuel
 */
public class NewInvoice extends BaseActivity {

    
    @Override
    protected void validate(Claim claim) throws Exception {

        super.validate(claim);

        if(!claim.isTransient())
        {
            throw new Exception("An process new claim attempt failed due to claim is already exist.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
        //perform routing
    }

    @Override
    protected void onProcessCompleted(Claim claim) {
        super.onProcessCompleted(claim);
    }

    @Override
    protected String getCurrentStatus() {
        return ClaimStatus.AWAITING_INVOICE_DATA;
    }

    @Override
    protected String getNextStatus() {
        return "";
    }
}
