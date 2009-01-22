/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.GlobalConfiguration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class GlobalConfigurationServiceImpl extends DataService implements GlobalConfigurationService{

    public GlobalConfiguration getValueByParam(String s){
         
        GlobalConfiguration obj = new GlobalConfiguration();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(GlobalConfiguration.class);
            criteria.add(Restrictions.eq("parameter", s));
            
            obj = (GlobalConfiguration) getByCriteria(criteria);
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        
        
        
        
        return obj;
    }
}
