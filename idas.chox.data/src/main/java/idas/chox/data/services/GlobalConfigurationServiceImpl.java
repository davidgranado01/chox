/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.data.services;

import idas.chox.core.model.GlobalConfiguration;
import idas.chox.core.services.GlobalConfigurationService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class GlobalConfigurationServiceImpl extends BaseDataService implements GlobalConfigurationService {

    public GlobalConfiguration getValueByParam(String s) {

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
