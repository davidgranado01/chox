package idas.chox.data.services;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Element;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.InsurerAlias;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.services.InsurerService;
import idas.chox.core.util.XmlHelper;

public class InsurerServiceImpl extends SecureDataService implements InsurerService {
    private static final Logger LOG = LoggerFactory.getLogger(InsurerServiceImpl.class);

    @Override
    public boolean isInsurerNameExist(String s) {
        if (getInsurerByName(s) != null || getInsurerByAliasName(s) != null) {
            return true;
        }
        return false;
    }

    @Override
    public Insurer getInsurerByName(String s) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
        criteria.add(Restrictions.eq("name", s));
        return (Insurer) getByCriteria(criteria);
    }
    
    public InsurerAlias getInsurerByAliasName(String s) {
        DetachedCriteria criteria = DetachedCriteria.forClass(InsurerAlias.class);
        criteria.add(Restrictions.eq("aliasName", s));
        return (InsurerAlias) getByCriteria(criteria);
    }

    @Override
    public Insurer getInsurerByNodeName(Element thisElement, String nodeName) {
        Insurer insurer = new Insurer();
        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(thisElement, nodeName))) {
            insurer = getInsurerByName(XmlHelper.getNodeValue(thisElement, nodeName));
        }
        return insurer;
    }

    @Override
    public Insurer getInsurer(int id) {
        return (Insurer) get(Insurer.class, id);
    }

    @Override
    public List<Insurer> getInsurers() {
        List<Insurer> insurers = new ArrayList<Insurer>();

        DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
        criteria.addOrder(Order.asc("name"));
        insurers = findByCriteria(criteria);

        return insurers;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void saveInsurer(Insurer insurer) {
        save(insurer);
    }

    @Override
    public VehicleClassCeiling getVechileClassCeilingForClaim(Claim claim) {
        LOG.debug("Getting vehicle class ceilinf for claim '{}' with vehicle class '{}'", claim.getChoReference(), claim.getCustomer().getVehicleClass());
        VehicleClassCeiling vehicleClassCeiling = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(VehicleClassCeiling.class);
        criteria.add(Restrictions.eq("insurer", claim.getInsurer()));
        criteria.add(Restrictions.eq("vehicleClass", claim.getCustomer().getVehicleClass()));
        vehicleClassCeiling = (VehicleClassCeiling) getByCriteria(criteria);
        return vehicleClassCeiling;
    }
}
