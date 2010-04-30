package idas.chox.bre;

import idas.chox.bre.mock.EngineRuleMock;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.model.ClaimStatus;
import java.io.IOException;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class RulesEngineResponseTest extends TestCase {

    @BeforeClass
    public static void setUpClass() throws Exception {}
    
    @AfterClass
    public static void tearDownClass() throws Exception {}

    private RulesEngineResponse setDefaultRulesEngineResponse(){
        
        RulesEngineResponse response = new RulesEngineResponse();

        for(Integer i = 0; i < 20; i++){
            response.addRuleEvaulation(setRuleEvaluation(i, ClaimStatus.INVOICE_ESCALATED));
        }
        
        return response;
        
    }

    private RuleEvaluation setRuleEvaluation(Integer RuleId, String claimFailedStatus){

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(new EngineRuleMock(RuleId.toString(), claimFailedStatus));
        res.setResult(RuleEvaluationResult.RulePassed);

        return res;
        
    }

    @Test
    public void testSkipped_InvoiceApprovedByBRE() throws IOException {
        
        RulesEngineResponse response = setDefaultRulesEngineResponse();
        assertTrue(response.getStatus(true).equals(ClaimStatus.INVOICE_APPROVED_BY_BRE));
        
    }
    
    @Test
    public void testSkipped_INVOICE_ESCALATED_TO_CH() throws IOException {

        RulesEngineResponse response = setDefaultRulesEngineResponse();

        RuleEvaluation rev = response.getResults().get(5);
        rev.setResult(RuleEvaluationResult.RuleFailed);
        rev.setRelatedRule(new EngineRuleMock("5", ClaimStatus.INVOICE_ESCALATED_TO_CH));

        assertTrue(response.getStatus(true).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));

    }

    @Test
    public void testSkipped_InvoiceEscalated() throws IOException {

        RulesEngineResponse response = setDefaultRulesEngineResponse();

        RuleEvaluation rev_5 = response.getResults().get(5);
        rev_5.setResult(RuleEvaluationResult.RuleFailed);
        rev_5.setRelatedRule(new EngineRuleMock("5", ClaimStatus.INVOICE_ESCALATED_TO_CH));

        RuleEvaluation rev_6 = response.getResults().get(6);
        rev_6.setResult(RuleEvaluationResult.RuleFailed);
        rev_6.setRelatedRule(new EngineRuleMock("6", ClaimStatus.INVOICE_ESCALATED));
        
        assertTrue(response.getStatus(true).equals(ClaimStatus.INVOICE_ESCALATED));

    }

    @Test
    public void testSkipped_InvoiceDataCalculationIncorrect() throws IOException {

        RulesEngineResponse response = setDefaultRulesEngineResponse();

        RuleEvaluation rev_5 = response.getResults().get(5);
        rev_5.setResult(RuleEvaluationResult.RuleFailed);
        rev_5.setRelatedRule(new EngineRuleMock("5", ClaimStatus.INVOICE_ESCALATED_TO_CH));

        RuleEvaluation rev_6 = response.getResults().get(6);
        rev_6.setResult(RuleEvaluationResult.RuleFailed);
        rev_6.setRelatedRule(new EngineRuleMock("6", ClaimStatus.INVOICE_ESCALATED));

        RuleEvaluation rev_7 = response.getResults().get(7);
        rev_7.setResult(RuleEvaluationResult.RuleFailed);
        rev_7.setRelatedRule(new EngineRuleMock("7", ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        
        assertTrue(response.getStatus(true).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));

    }
    
}
