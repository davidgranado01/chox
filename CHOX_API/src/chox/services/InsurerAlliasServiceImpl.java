/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Insurer;
import chox.model.InsurerAllias;
import chox.model.Insurer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Element;

public class InsurerAlliasServiceImpl extends DataService implements InsurerAlliasService {

    public InsurerAllias getInsurerByAlliasName(String s){
         
        InsurerAllias insurerallias = new InsurerAllias();
        
        try {
            
            Criteria criteria = getCurrentSession().createCriteria(InsurerAllias.class);
            criteria.add(Restrictions.eq("alliasName", s));
            insurerallias = (InsurerAllias) criteria.uniqueResult();
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        getCurrentSession().clear();
        getCurrentSession().disconnect();
        
        
        return insurerallias;
    }
}
