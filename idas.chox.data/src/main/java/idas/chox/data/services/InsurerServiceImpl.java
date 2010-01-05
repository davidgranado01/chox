package idas.chox.data.services;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.services.InsurerService;
import idas.chox.core.util.XmlHelper;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Element;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class InsurerServiceImpl extends SecureDataService implements InsurerService {

    public boolean isInsurerNameExist(String s) {
        if (getInsurerByName(s) != null) {
            return true;
        }
        return false;
    }

    public Insurer getInsurerByName(String s) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
        criteria.add(Restrictions.eq("name", s));
        return (Insurer) getByCriteria(criteria);
    }

    public Insurer getInsurerByNodeName(Element thisElement, String nodeName) {
        Insurer insurer = new Insurer();
        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(thisElement, nodeName))) {
            insurer = getInsurerByName(XmlHelper.getNodeValue(thisElement, nodeName));
        }
        return insurer;
    }

    public Insurer getInsurer(int id) {
        return (Insurer) get(Insurer.class, id);
    }

    public List<Insurer> getInsurers() {

        List<Insurer> insurer = new ArrayList<Insurer>();


        DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
        criteria.addOrder(Order.asc("name"));
        insurer = findByCriteria(criteria);

        return insurer;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Insurer updateInsurer(Insurer object) {


        save(object);


        return object;
    }

    public VehicleClassCeiling getVechileClassCeilingForClaim(Claim claim) {
        VehicleClassCeiling vehicleClassCeiling = null;



        DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClassCeiling.class);
        criteria.add(Restrictions.eq("insurer", claim.getInsurer()));
        criteria.add(Restrictions.eq("vehicleClass", claim.getCustomer().getVehicleClass()));
        vehicleClassCeiling = (VehicleClassCeiling) getByCriteria(criteria);



        return vehicleClassCeiling;
    }
}
