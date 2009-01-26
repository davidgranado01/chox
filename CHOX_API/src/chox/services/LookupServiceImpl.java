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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

/**
 *
 * @author Emmanuel
 */
public class LookupServiceImpl extends DataService implements LookupService,Serializable {

    public List getStatuses() {
        List items = new ArrayList<LookupItem>();
        for (String s : ClaimStatus.getStatus()) {
            items.add(new LookupItem(s, s));
        }

        return items;
    }

    public List getLineOfBusinesses() {

        DetachedCriteria criteria = DetachedCriteria.forClass(LineOfBusiness.class);
        return findByCriteria(criteria);
    }

    public List getSuppliers() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
        return findByCriteria(criteria);
    }

    public List getInsurers() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
        return findByCriteria(criteria);
    }

    public List getVehicleClasses() {
        DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClass.class).addOrder(Order.asc("name"));
        return findByCriteria(criteria);
    }
}
