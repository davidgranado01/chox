package idas.chox.data.services;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ProtocolVehicleClassCeiling;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.ProtocolVehicleClassCeilingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolVehicleClassCeilingServiceImpl extends SecureDataService implements ProtocolVehicleClassCeilingService {
    private static final Logger LOG = LoggerFactory.getLogger(ProtocolVehicleClassCeilingServiceImpl.class);
    
    @Override
    public List<VehicleClass> getAvailableProtocolVehicleClassCeilingByBreBand(int breBandId) {

        // GET ALL VEHICLE CLASS
        DetachedCriteria vehicleClassCirteria = DetachedCriteria.forClass(VehicleClass.class);

        // GET ALL  VEHICLE CLASS ASSIGNED TO INSURER
        DetachedCriteria protocolVehicleClassCeilingCirteria = DetachedCriteria.forClass(ProtocolVehicleClassCeiling.class);
        protocolVehicleClassCeilingCirteria.add(Restrictions.eq("breBand.id", breBandId));
        protocolVehicleClassCeilingCirteria.setProjection(Property.forName("vehicleClass.id"));

        vehicleClassCirteria.addOrder(Order.asc("name"));

        // FILTERED BY ASSIGNED  VEHICLE CLASS
        vehicleClassCirteria.add(Property.forName("id").notIn(protocolVehicleClassCeilingCirteria));

        // RETURN SEARCH RESULT
        return findByCriteria(vehicleClassCirteria);

    }

    @Override
    public List<ProtocolVehicleClassCeiling> getSelectedProtocolVehicleClassCeilingByBreBand(int breBandId) {
        DetachedCriteria protocolVehicleClassCeilingCirteria = DetachedCriteria.forClass(ProtocolVehicleClassCeiling.class);
        protocolVehicleClassCeilingCirteria.add(Restrictions.eq("breBand.id", breBandId));
        protocolVehicleClassCeilingCirteria.addOrder(Order.asc("vehicleClass"));
        return findByCriteria(protocolVehicleClassCeilingCirteria);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void saveProtocolVehicleClassCeiling(ProtocolVehicleClassCeiling protocolVehicleClassCeiling) {
        save(protocolVehicleClassCeiling);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void deleteProtocolVehicleClassCeiling(ProtocolVehicleClassCeiling protocolVehicleClassCeiling) {
        delete(protocolVehicleClassCeiling);
    }

    @Override
    public ProtocolVehicleClassCeiling getProtocolVehicleClassCeiling(int protocolVehicleClassCeilingId) {
        return (ProtocolVehicleClassCeiling) get(ProtocolVehicleClassCeiling.class, protocolVehicleClassCeilingId);
    }
    
    @Override
    public ProtocolVehicleClassCeiling getProtocolVehicleClassCeilingByVehicleClass(int vehicleClassId, int breBandId) {
        ProtocolVehicleClassCeiling protocolVehicleClassCeiling = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(ProtocolVehicleClassCeiling.class);
        criteria.add(Restrictions.eq("breBand.id", breBandId));
        criteria.add(Restrictions.eq("vehicleClass.id", vehicleClassId));
        protocolVehicleClassCeiling = (ProtocolVehicleClassCeiling) getByCriteria(criteria);
        return protocolVehicleClassCeiling;
    }
    
    @Override
    public ProtocolVehicleClassCeiling getProtocolVechileClassCeilingForClaim(Claim claim) {
        LOG.debug("Getting protocol vehicle class ceilinf for claim '{}' with vehicle class '{}'", claim.getChoReference(), claim.getVehicleHire().getVehicleClass());
        ProtocolVehicleClassCeiling protocolVehicleClassCeiling = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(ProtocolVehicleClassCeiling.class);
        criteria.add(Restrictions.eq("breBand", claim.getBreBand()));
        criteria.add(Restrictions.eq("vehicleClass", claim.getVehicleHire().getVehicleClass()));
        protocolVehicleClassCeiling = (ProtocolVehicleClassCeiling) getByCriteria(criteria);
        return protocolVehicleClassCeiling;
    }
}
