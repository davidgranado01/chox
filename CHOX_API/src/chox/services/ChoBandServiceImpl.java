/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.ChoBand;
import chox.data.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class ChoBandServiceImpl implements ChoBandService{

    public ChoBand getChoBandByChorganisationId(int orgId){
        
        Session currentSession = HibernateUtil.currentSession();      
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
