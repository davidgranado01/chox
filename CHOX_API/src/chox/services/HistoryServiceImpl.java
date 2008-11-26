/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.Util.DateHelper;
import chox.model.History;
import chox.model.Claim;
import scsbre.engine.*;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;

public class HistoryServiceImpl extends DataService implements HistoryService{
    
    public List<History> getHistoryByClaim(Claim claim){
         
        List histories = new ArrayList<History>();
        
        try {
            Criteria criteria = currentSession.createCriteria(History.class).add(Restrictions.eq("claim", claim));
            histories = criteria.list();
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        currentSession.clear();
        currentSession.disconnect();
        
        return histories;
    }
    
    public void logInvoiceValidationErrorMsg(RulesEngineResponse reponse, Claim claim){
    
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
            history.setClaim(claim);
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
        
        history.setProcessDate(DateHelper.getCurrentTimeStamp());
        history.setCreatedBy(getCurrentUser().getId());
        history.setCreatedDate(DateHelper.getCurrentTimeStamp());
        history.setLastModifiedBy(getCurrentUser().getId());
        history.setLastModifiedDate(DateHelper.getCurrentTimeStamp());

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
