package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.service.workflow.ActivityFactory;
import java.util.Arrays;
import java.util.List;


public class InvoiceSaving extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceSaving.class);
    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private String rules;
    private ActivityFactory activityFactory;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Parameter Setters">
    public void setRules(String rules) {
        this.rules = rules;
    }
    // </editor-fold>

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        // parse rules string
        int no50plus = 0;
        int no25plus = 0;
        
        List<String> selectedRules = Arrays.asList(rules.split("\\s*,\\s*"));
        if (selectedRules == null || selectedRules.isEmpty()) {
            throw new Exception("At least one rule must be selected.");
        }
        for(String selectedRule : selectedRules) {
            String[] tokens = selectedRule.split(":");
            if (tokens[1].equals("3")) no50plus++;
            if (tokens[1].equals("2")) no25plus++;
            
            if (no50plus > 1) {
                throw new Exception("Cannot select more than one rule at > 50%.");
            } else if (no50plus == 1 && no25plus > 2) {
                throw new Exception("Cannot select more than 2 25-50% rules when one > 50% selected.");
            } else if (no25plus > 4) {
                throw new Exception("Cannot select more than four rules at 25-50%.");
            }
            LOG.debug("Selected rules: {}-{}", tokens[0], tokens[1].equals("1") ? "<25%":  tokens[1].equals("2") ? "25-50%" : " > 50%");
        }
        throw new Exception("Implementation not finished");
    }

    @Override
    protected void doProcess(Claim claim) {
//        claim.getInvoice().setInvoiceSavingRule(null);
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        activityEventGenerator.generate(claim, this);
        // If this is a manual claim, we now need to change the chained activity
        // [for non-manual claims, chained activity is acceptInvoice]
        if (ClaimType.isInsurerUpload(claim.getClaimType())) {
            setChainActivity(activityFactory.getActivity("updateManualInvoiceAgreeQuantum")); 
        }
        getChainActivity().setWorkflowContext(getProcessContext());
        getChainActivity().processInBatch(claim);
    }

}