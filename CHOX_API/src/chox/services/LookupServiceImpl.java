/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.Chorganisation;
import chox.model.ClaimStatus;
import chox.model.Insurer;
import chox.model.LineOfBusiness;
import chox.model.LookupItem;
import chox.model.VehicleClass;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

/**
 *
 * @author Emmanuel
 */
public class LookupServiceImpl extends DataService implements LookupService {

    public List getStatuses() {
        List items = new ArrayList<LookupItem>();
        for (String s : ClaimStatus.getStatus()) {
            items.add(new LookupItem(s, s));
        }

        return items;
    }

    public List getLineOfBusinesses() {

        Criteria criteria = currentSession.createCriteria(LineOfBusiness.class);
        return criteria.list();
    }

    public List getSuppliers() {
        Criteria criteria = currentSession.createCriteria(Chorganisation.class);
        return criteria.list();
    }

    public List getInsurers() {
        Criteria criteria = currentSession.createCriteria(Insurer.class);
        return criteria.list();
    }

    public List getVehicleClasses() {
        Criteria criteria = currentSession.createCriteria(VehicleClass.class).addOrder(Order.asc("name"));
        return criteria.list();
    }
}
