package idas.chox.data.services;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.BreBandOrganisation;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ChorganisationAlias;
import idas.chox.core.model.InsurerChorganisation;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.ChorganisationService;

public class ChorganisationServiceImpl extends SecureDataService implements ChorganisationService {
    private static final Logger LOG = LoggerFactory.getLogger(ChorganisationServiceImpl.class);

    private SecurityInfoProvider securityInfoProvider;


    @Override
    public Chorganisation getChorganisation(int chorganisationId) {
        return (Chorganisation) get(Chorganisation.class, chorganisationId);
    }

    @Override
    public List<Chorganisation> getChorganisations(String order) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);

        if (!order.equalsIgnoreCase("") && order != null) {
            criteria.addOrder(Order.asc(order));
        }

        return findByCriteria(criteria);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public Chorganisation updateChorganisation(Chorganisation chorganisation) {
        save(chorganisation);
        return chorganisation;
    }

    @Override
    public List<Chorganisation> getAvailableChorganisationsByInsurer(int insurerId) {

        // GET ALL CHORGANISATIONS
        DetachedCriteria chorganisationCirteria = DetachedCriteria.forClass(Chorganisation.class);
        chorganisationCirteria.add(Restrictions.eq("status", true));

        // GET ALL CHORGANISATIONS ASSIGNED TO INSURER
        DetachedCriteria insurerChorganisationCirteria = DetachedCriteria.forClass(InsurerChorganisation.class);
        insurerChorganisationCirteria.add(Restrictions.eq("insurer.id", insurerId));
        insurerChorganisationCirteria.add(Restrictions.eq("status", true));
        insurerChorganisationCirteria.setProjection(Property.forName("chorganisation.id"));

        // RETURN SEARCH RESULT
        chorganisationCirteria.add(Property.forName("id").notIn(insurerChorganisationCirteria));
        return findByCriteria(chorganisationCirteria);

    }

    @Override
    public List<Chorganisation> getActiveChorganisationsByInsurerWithoutBreBand(int insurerId) {

        // GET ALL ACTIVE CH ORGANISATION FILTER BY INSURER
        DetachedCriteria insurerChorganisationCirteria = DetachedCriteria.forClass(InsurerChorganisation.class);
        insurerChorganisationCirteria.add(Restrictions.eq("insurer.id", insurerId));
        insurerChorganisationCirteria.add(Restrictions.eq("status", true));

        // GET ALL CH ORGANISATION BY BRE BAND ASSIGNED TO THE INSURER
        DetachedCriteria breBandOrganisationCirteria = DetachedCriteria.forClass(BreBandOrganisation.class);
        breBandOrganisationCirteria.createAlias("this.breBand", "bre", CriteriaSpecification.INNER_JOIN);
        breBandOrganisationCirteria.add(Restrictions.eq("bre.insurer.id", insurerId));
        breBandOrganisationCirteria.setProjection(Property.forName("chorganisation.id"));

        // RETURN SEARCH RESULT
        insurerChorganisationCirteria.add(Property.forName("chorganisation.id").notIn(breBandOrganisationCirteria));
        insurerChorganisationCirteria.setProjection(Property.forName("chorganisation"));
        return findByCriteria(insurerChorganisationCirteria);
    }

    @Override
    public boolean isActiveChorganisationsByInsurerCreditHireWithBreBand(int insurerId, int chorganisationId) {

        // GET ALL ACTIVE CH ORGANISATION FILTER BY INSURER
        DetachedCriteria insurerChorganisationCirteria = DetachedCriteria.forClass(InsurerChorganisation.class);
        insurerChorganisationCirteria.add(Restrictions.eq("insurer.id", insurerId));
        insurerChorganisationCirteria.add(Restrictions.eq("chorganisation.id", chorganisationId));
        insurerChorganisationCirteria.add(Restrictions.eq("status", true));
        
        // GET ALL CH ORGANISATION BY BRE BAND ASSIGNED TO THE INSURER
        DetachedCriteria breBandOrganisationCirteria = DetachedCriteria.forClass(BreBandOrganisation.class);
        breBandOrganisationCirteria.createAlias("this.breBand", "bre", CriteriaSpecification.INNER_JOIN);
        breBandOrganisationCirteria.add(Restrictions.eq("bre.insurer.id", insurerId));
        breBandOrganisationCirteria.setProjection(Property.forName("chorganisation.id"));
        
        // RETURN SEARCH RESULT
        insurerChorganisationCirteria.add(Property.forName("chorganisation.id").in(breBandOrganisationCirteria));
        insurerChorganisationCirteria.setProjection(Property.forName("chorganisation"));

        if(findByCriteria(insurerChorganisationCirteria).size()<=0){
            return false;
        }

        return true;
    }

    @Override
    public boolean isCreditHireWithBreBand(int chorganisationId) {

        // GET ALL ACTIVE CH ORGANISATION FILTER BY INSURER
        DetachedCriteria chorganisationCirteria = DetachedCriteria.forClass(Chorganisation.class);
        chorganisationCirteria.add(Restrictions.eq("id", chorganisationId));

        // GET ALL CH ORGANISATION BY BRE BAND ASSIGNED TO THE INSURER
        DetachedCriteria breBandOrganisationCirteria = DetachedCriteria.forClass(BreBandOrganisation.class);
        breBandOrganisationCirteria.setProjection(Property.forName("chorganisation.id"));

        // RETURN SEARCH RESULT
        chorganisationCirteria.add(Property.forName("id").in(breBandOrganisationCirteria));

        if(findByCriteria(chorganisationCirteria).size()<=0){
            return false;
        }

        return true;
    }
    
    @Override
    public boolean isChorgNameExist(String s) {
        if (getChoAliasName(s) != null || getChorgByName(s) != null) {
            return true;
        }
        return false;
    }
    
    @Override
    public Chorganisation getChorgByName(String s) {

        Chorganisation object = null;

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("name", s));
            object = (Chorganisation) getByCriteria(criteria);

        } catch (Exception ex) {
            LOG.error("Error getting CHO by name '{}': {}", s, ex.getMessage());
        }

        return object;
    }
    
    @Override
    public ChorganisationAlias getChoAliasName(String s) {
        ChorganisationAlias object = null;
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(ChorganisationAlias.class);
            criteria.add(Restrictions.ilike("aliasName", s.replace(" ", "")));
            object = (ChorganisationAlias) getByCriteria(criteria);

        } catch (Exception ex) {
            LOG.error("Error getting CHO alias by name '{}': {}", s, ex.getMessage());
        }
        return object;
    }

    @Override
    public List<Chorganisation> getActiveChorganisation() {

        List<Chorganisation> chorganisations = new ArrayList<Chorganisation>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("status", true));
            chorganisations = findByCriteria(criteria);

        } catch (Exception ex) {
            LOG.error("Error getting Active CHOs by name: {}", ex.getMessage());
        }

        return chorganisations;
    }

    @Override
    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }

    @Override
    public SecurityInfoProvider getSecurityInfoProvider() {
        return securityInfoProvider;
    }

}
