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
import org.hibernate.criterion.Order;

public class HistoryServiceImpl extends DataService implements HistoryService{
    
    /*
     * isShowAll : true > SHOW ALL RECORDS WITH TYPE IS ERROR AND INFO
     * isShowAll : false > SHOW ALL RECORDS WITH TYPE IS ERROR ONLY
     * isPublic : true > SHOW ALL RECORDS WITH IS_PUBLIC IS TRUE ONLY
     * isPublic : false > SHOW ALL RECORDS REGARDLESS THE IS_PUBLIC
     */
    
    public List<History> getHistoryByClaim(Claim claim, Boolean isShowAll, Boolean isPublic){
         
        List histories = new ArrayList<History>();
        
        try {
            Criteria criteria = currentSession.createCriteria(History.class);//.add(Restrictions.eq("claimId", claim.getId()));;
            criteria.createCriteria("claim").add(Restrictions.eq("id", claim.getId()));
            
            if(!isShowAll){
                criteria.add(Restrictions.eq("type", "ERROR"));
            }
            
            // SHOW TRUE RECORD ONLY IF IT IS NOT PUBLIC
            if(isPublic){
                criteria.add(Restrictions.eq("isPublic", true));
            }
            
            criteria.addOrder(Order.asc("claim.id"));
            criteria.addOrder(Order.asc("ruleId"));
            
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
