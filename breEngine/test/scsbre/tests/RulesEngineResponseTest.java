package scsbre.tests;

import java.io.IOException;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.RulesEngineResponse;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestEngineRule;
import static org.junit.Assert.*;

public class RulesEngineResponseTest extends TestCase {

    @BeforeClass
    public static void setUpClass() throws Exception {}
    
    @AfterClass
    public static void tearDownClass() throws Exception {}

    private RulesEngineResponse setDefaultRulesEngineResponse(){
        
        RulesEngineResponse response = new RulesEngineResponse();

        for(Integer i = 0; i < 20; i++){
            response.addRuleEvaulation(setRuleEvaluation(i, ClaimStatus.InvoiceEscalated));
        }
        
        return response;
        
    }

    private RuleEvaluation setRuleEvaluation(Integer RuleId, ClaimStatus claimFailedStatus){

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(new TestEngineRule(RuleId.toString(), claimFailedStatus));
        res.setResult(RuleEvaluationResult.RulePassed);

        return res;
        
    }

    @Test
    public void testSkipped_InvoiceApprovedByBRE() throws IOException {
        
        RulesEngineResponse response = setDefaultRulesEngineResponse();
        assertTrue(response.getStatus().equals(ClaimStatus.InvoiceApprovedByBRE));
        
    }
    
    @Test
    public void testSkipped_InvoiceEscalatedToHandler() throws IOException {

        RulesEngineResponse response = setDefaultRulesEngineResponse();

        RuleEvaluation rev = response.getResults().get(5);
        rev.setResult(RuleEvaluationResult.RuleFailed);
        rev.setRelatedRule(new TestEngineRule("5", ClaimStatus.InvoiceEscalatedToHandler));

        assertTrue(response.getStatus().equals(ClaimStatus.InvoiceEscalatedToHandler));

    }

    @Test
    public void testSkipped_InvoiceEscalated() throws IOException {

        RulesEngineResponse response = setDefaultRulesEngineResponse();

        RuleEvaluation rev_5 = response.getResults().get(5);
        rev_5.setResult(RuleEvaluationResult.RuleFailed);
        rev_5.setRelatedRule(new TestEngineRule("5", ClaimStatus.InvoiceEscalatedToHandler));

        RuleEvaluation rev_6 = response.getResults().get(6);
        rev_6.setResult(RuleEvaluationResult.RuleFailed);
        rev_6.setRelatedRule(new TestEngineRule("6", ClaimStatus.InvoiceEscalated));
        
        assertTrue(response.getStatus().equals(ClaimStatus.InvoiceEscalated));

    }

    @Test
    public void testSkipped_InvoiceDataCalculationIncorrect() throws IOException {

        RulesEngineResponse response = setDefaultRulesEngineResponse();

        RuleEvaluation rev_5 = response.getResults().get(5);
        rev_5.setResult(RuleEvaluationResult.RuleFailed);
        rev_5.setRelatedRule(new TestEngineRule("5", ClaimStatus.InvoiceEscalatedToHandler));

        RuleEvaluation rev_6 = response.getResults().get(6);
        rev_6.setResult(RuleEvaluationResult.RuleFailed);
        rev_6.setRelatedRule(new TestEngineRule("6", ClaimStatus.InvoiceEscalated));

        RuleEvaluation rev_7 = response.getResults().get(7);
        rev_7.setResult(RuleEvaluationResult.RuleFailed);
        rev_7.setRelatedRule(new TestEngineRule("7", ClaimStatus.InvoiceDataCalculationIncorrect));
        
        assertTrue(response.getStatus().equals(ClaimStatus.InvoiceDataCalculationIncorrect));

    }
    
}
