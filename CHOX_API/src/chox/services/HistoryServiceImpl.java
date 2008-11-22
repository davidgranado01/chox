/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.History;
import org.hibernate.Session;
import chox.data.HibernateUtil;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import scsbre.engine.*;
import java.util.List;

public class HistoryServiceImpl extends DataService implements HistoryService{
    
    public void logInvoiceValidationErrorMsg(RulesEngineResponse reponse, int claimId){
    
        List<RuleEvaluation> results = reponse.getResults();
        HistoryService hisService = new HistoryServiceImpl();
        
        for(int iCount=0; iCount<results.size(); iCount++){
            
            RuleEvaluation rv = results.get(iCount);
            IBusinessRule rBusinessRule = rv.getRelatedRule();
            
            String sType = "INFO";
            if(rv.getResult()==RuleEvaluationResult.RuleFailed){
                sType = "ERROR";
            }

            History history = new History();
            history.setClaimId(claimId);
            history.setIsPublic(rv.getIsVisibleToCHO());
            history.setNarrative(rv.toString()+':'+rv.getResult());
            history.setType(sType);
            history.setRuleId(rBusinessRule.getRuleId());
            history.setIsSystem(true);
            hisService.saveHistory(history);
        }
        
    }
    
    public Boolean saveHistory(History history){
        
        Boolean bFlag = true;
        
        currentSession.beginTransaction();
        
        history.setProcessDate(generalServiceImpl.getCurrentTimeStamp());
        history.setCreatedBy(getCurrentUser().getId());
        history.setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
        history.setLastModifiedBy(getCurrentUser().getId());
        history.setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());

        try{
            currentSession.saveOrUpdate(history);
        } catch (Exception e) {
            currentSession.getTransaction().rollback();
        }finally{
            currentSession.getTransaction().commit();
        }

        return bFlag;
    }
}
