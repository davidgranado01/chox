/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.GlobalConfiguration;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class GlobalConfigurationServiceImpl extends DataService implements GlobalConfigurationService{

    public GlobalConfiguration getValueByParam(String s){
         
        GlobalConfiguration obj = new GlobalConfiguration();
        
        try {
            
            Criteria criteria = getCurrentSession().createCriteria(GlobalConfiguration.class);
            criteria.add(Restrictions.eq("parameter", s));
            
            obj = (GlobalConfiguration) criteria.uniqueResult();
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        
        
        
        
        return obj;
    }
}
