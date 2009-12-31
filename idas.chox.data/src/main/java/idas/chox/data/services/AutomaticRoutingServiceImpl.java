package idas.chox.data.services;

import idas.chox.core.model.AutomaticRouting;
import idas.chox.core.services.AutomaticRoutingService;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class AutomaticRoutingServiceImpl extends SecureDataService implements AutomaticRoutingService {

    public Boolean saveObj(AutomaticRouting obj) {

        Boolean bFlag = false;

        try {
            save(obj);
            bFlag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bFlag;
    }

    public AutomaticRouting getObject(int id) {
        return (AutomaticRouting) get(AutomaticRouting.class, id);
    }

    public List<AutomaticRouting> getObjects(int insurerId) {

        List<AutomaticRouting> objects = new ArrayList<AutomaticRouting>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRouting.class);
            criteria.add(Restrictions.eq("insurer.id", insurerId));
            objects = findByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return objects;
    }
}
