package idas.chox.service.workflow.activities;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.InvoiceSavingRule;
import idas.chox.core.services.InvoiceSavingRuleService;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;

public class InvoiceSaving extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceSaving.class);
    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private String rules;
    private ActivityFactory activityFactory;
    private InvoiceSavingRuleService invoiceSavingRuleService;
    private List<InvoiceSavingRule> invoiceSavingRules;
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Parameter Setters">
    public void setRules(String rules) {
        this.rules = rules;
    }
    // </editor-fold>

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }

    public void setInvoiceSavingRuleService(InvoiceSavingRuleService invoiceSavingRuleService) {
        this.invoiceSavingRuleService = invoiceSavingRuleService;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        // parse rules string
        int no50plus = 0;
        int no25plus = 0;
        invoiceSavingRules = new ArrayList<>();
        
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
            InvoiceSavingRule invoiceSavingRule = new InvoiceSavingRule();
            invoiceSavingRule.setInvoice(claim.getInvoice());
            invoiceSavingRule.setInvoiceSavingRule(tokens[0]);
            invoiceSavingRule.setSavingGroup(Integer.valueOf(tokens[1]));
            LOG.debug("Selected rules: {}-{}", tokens[0], tokens[1].equals("1") ? "<25%":  tokens[1].equals("2") ? "25-50%" : " > 50%");
            invoiceSavingRules.add(invoiceSavingRule);
        }
    }

    @Override
    protected void beforeProcess(Claim claim) throws Exception {
        // Remove any existing savings rules on invoice
        List<InvoiceSavingRule> invoiceSavingRulesToDelete;
        
        invoiceSavingRulesToDelete = invoiceSavingRuleService.getInvoiceSavingRules(claim.getInvoice());
        
        invoiceSavingRulesToDelete.stream().map((isr) -> {
            invoiceSavingRuleService.deleteInvoiceSavingRule(isr);
            return isr;
        }).forEachOrdered((isr) -> {
            LOG.debug("   removing rule {}", isr.getInvoiceSavingRule());
        });
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        // First remove any existing savings rules on the invoice
        List<InvoiceSavingRule> invoiceSavingRulesToDelete;
        
        invoiceSavingRulesToDelete = invoiceSavingRuleService.getInvoiceSavingRules(claim.getInvoice());
        if (!invoiceSavingRulesToDelete.isEmpty()) {
            LOG.error("Cannot save new rules as existing rules already exist.");
            throw new Exception("Internal error occured trying to save the selected Invoice Saving Rules.");
        }

        invoiceSavingRules.forEach((invoiceSavingRule) -> {
            try {
                LOG.debug("   adding rule {}", invoiceSavingRule.getInvoiceSavingRule());
                invoiceSavingRuleService.saveInvoiceSavingRule(invoiceSavingRule);
            } catch (Exception ex) {
                LOG.error("Exception thrown saving Invoice saving rule on invoice id={}: rule={}, group={}",
                        new Object[]{invoiceSavingRule.getInvoice().getId(), invoiceSavingRule.getInvoiceSavingRule(),
                            invoiceSavingRule.getSavingGroup()});
            }
        });
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        activityEventGenerator.getEvents(claim, this).forEach((event) -> {
            ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getEventBus().post(event);
        });
        // If this is a manual claim, we now need to change the chained activity
        // [for non-manual claims, chained activity is acceptInvoice]
        if (ClaimType.isInsurerUpload(claim.getClaimType())) {
            setChainActivity(activityFactory.getActivity("updateManualInvoiceAgreeQuantum")); 
        }
        getChainActivity().setWorkflowContext(getProcessContext());
        getChainActivity().processInBatch(claim);
    }

}