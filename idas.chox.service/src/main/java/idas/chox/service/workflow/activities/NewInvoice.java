/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import java.util.List;

/**
 *
 * @author emmanuel
 */
public class NewInvoice extends BaseActivity {

    @Override
    protected void doProcess(Claim claim) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
    }

    


}
