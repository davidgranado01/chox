package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.service.workflow.ActivityFactory;


public class InvoiceSaving extends BaseActivity {

//    private static final Logger LOG = LoggerFactory.getLogger(InvoiceSaving.class);
    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private String ruleId;
    private ActivityFactory activityFactory;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Parameter Setters">
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
    // </editor-fold>

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }


    @Override
    protected void doProcess(Claim claim) {
        claim.getInvoice().setInvoiceSavingRule(ruleId);
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        activityEventGenerator.generate(claim, this);
        // If this is a manual claim, we now need to change the chained activity
        if (ClaimType.isInsurerUpload(claim.getClaimType())) {
            setChainActivity(activityFactory.getActivity("updateManualInvoiceAgreeQuantum"));

        }
        getChainActivity().setWorkflowContext(getProcessContext());
        getChainActivity().processInBatch(claim);
    }

}