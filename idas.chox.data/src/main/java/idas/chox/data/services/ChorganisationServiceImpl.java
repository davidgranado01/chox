package idas.chox.data.services;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.InsurerChorganisation;
import idas.chox.core.services.BreBandOrganisationService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.InsurerChorganisationService;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ChorganisationServiceImpl extends SecureDataService implements ChorganisationService {

    public Chorganisation getChorganisation(int chorganisationId) {
        return (Chorganisation) get(Chorganisation.class, chorganisationId);
    }
    
    public List<Chorganisation> getChorganisations(String order) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
        
        if(!order.equalsIgnoreCase("") && order!=null){
            criteria.addOrder(Order.asc(order));
        }
        
        return findByCriteria(criteria);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Chorganisation updateChorganisation(Chorganisation chorganisation) {
        save(chorganisation);
        return chorganisation;
    }


    public List<Chorganisation> getAvailableChorganisationsByInsurer(int insurerId) {

        // GET ALL CHORGANISATIONS
        DetachedCriteria chorganisationCirteria = DetachedCriteria.forClass(Chorganisation.class);
        chorganisationCirteria.add(Restrictions.eq("status", true));

        // GET ALL CHORGANISATIONS ASSIGNED TO INSURER
        DetachedCriteria insurerChorganisationCirteria = DetachedCriteria.forClass(InsurerChorganisation.class);
        insurerChorganisationCirteria.add(Restrictions.eq("insurer.id", insurerId));
        insurerChorganisationCirteria.add(Restrictions.eq("status", true));
        insurerChorganisationCirteria.setProjection(Property.forName("chorganisation.id"));
        
        // FILTERED BY ASSIGNED CHORGANISATIONS
        chorganisationCirteria.add(Property.forName("id").notIn(insurerChorganisationCirteria));

        // RETURN SEARCH RESULT
        return findByCriteria(chorganisationCirteria);

    }





    
    protected InsurerChorganisationService insurerChorganisationService;
    protected BreBandOrganisationService choBandOrganisationService;

    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) {
        this.insurerChorganisationService = insurerChorganisationService;
    }

    public void setChoBandOrganisationService(BreBandOrganisationService choBandOrganisationService) {
        this.choBandOrganisationService = choBandOrganisationService;
    }

    public boolean isChorgNameExist(String s) {

        boolean isExist = false;

        if (getChorgByName(s) != null) {
            isExist = true;
        }

        return isExist;

    }
    
    public List<Chorganisation> getChorganisationsByInsurerId(int insurerId) {
        List<Chorganisation> objects = new ArrayList<Chorganisation>();

        List<InsurerChorganisation> InsurerChorganisation = insurerChorganisationService.getInsurerChorganisations(insurerId, null);

        for (InsurerChorganisation object : InsurerChorganisation) {
            if (object.getChorganisation().isStatus()) {
                objects.add(object.getChorganisation());
            }
        }

        return objects;
    }

    

    public Chorganisation getChorgByName(String s) {

        Chorganisation object = new Chorganisation();

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("name", s));
            object = (Chorganisation) getByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return object;
    }

    public List<Chorganisation> getActiveChorganisation() {

        List<Chorganisation> chorganisations = new ArrayList<Chorganisation>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("status", true));
            chorganisations = findByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return chorganisations;
    }

    public Chorganisation getCurrentCHOrganisation() {
        Chorganisation chorg = new Chorganisation();
        chorg.setId(getCurrentUser().getChorganisation().getId());
        return chorg;
    }
}
