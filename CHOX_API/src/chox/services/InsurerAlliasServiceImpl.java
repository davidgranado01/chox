/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.InsurerAllias;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class InsurerAlliasServiceImpl extends DataService implements InsurerAlliasService {

    public InsurerAllias getInsurerByAlliasName(String s) {

        InsurerAllias insurerallias = new InsurerAllias();
        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(InsurerAllias.class);
            criteria.add(Restrictions.eq("alliasName", s));
            insurerallias = (InsurerAllias) getByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return insurerallias;
    }
}
