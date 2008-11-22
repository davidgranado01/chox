/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.ChoBand;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class ChoBandServiceImpl extends DataService implements ChoBandService{

    public ChoBand getChoBandByChorganisationId(int orgId){
        
        ChoBand band = new ChoBand();
        
        try {
            
            Criteria criteria = currentSession.createCriteria(ChoBand.class);
            criteria.add(Restrictions.eq("Id", orgId));
            band = (ChoBand) criteria.uniqueResult();
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        currentSession.clear();
        currentSession.disconnect();
        return band;
    }
    
    public ChoBand getChoBandByChorganisationIdAndInsurerId(int orgId, int insurerId){
        
        ChoBand band = new ChoBand();
        
        try {
            
            Criteria criteria = currentSession.createCriteria(ChoBand.class);
            criteria.add(Restrictions.eq("Id", orgId));
            band = (ChoBand) criteria.uniqueResult();
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        currentSession.clear();
        currentSession.disconnect();
        return band;
    }    
}
