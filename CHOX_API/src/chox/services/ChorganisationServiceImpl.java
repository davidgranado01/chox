package chox.services;

import chox.model.*;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ChorganisationServiceImpl extends SecureDataService implements ChorganisationService{
    
    protected InsurerChorganisationService insurerChorganisationService;
    protected BreBandOrganisationService breBandOrganisationService;
    
    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) {
        this.insurerChorganisationService = insurerChorganisationService;
    }
    
    public void setBreBandOrganisationService(BreBandOrganisationService breBandOrganisationService) {
        this.breBandOrganisationService = breBandOrganisationService;
    }
    
    
    
    public boolean isChorgNameExist(String s){
        
        boolean isExist = false;
        
        if(getChorgByName(s)!=null){
            isExist = true;
        }
        
        return isExist;
        
    }
    
    public List<Chorganisation> getObjectsByInsurerId(int insurerId){
        List<Chorganisation> objects = new ArrayList<Chorganisation>();
        
        List<InsurerChorganisation> InsurerChorganisation = insurerChorganisationService.getObjects(insurerId, null);
        
        for(InsurerChorganisation object :InsurerChorganisation){
            if(object.getChorganisation().isStatus()){
                objects.add(object.getChorganisation());
            }
        }
        
        return objects;
    }
    
    public List<Chorganisation> getObjectsWithoutInsurer(int insurerId) {

        List<Chorganisation> objects = new ArrayList<Chorganisation>();

        try{

            List<Chorganisation> allchos = this.getActiveChorganisation();

            for(Chorganisation org : allchos){
                if(!insurerChorganisationService.isActiveObjectExist(insurerId, org.getId())){
                    objects.add(org);
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
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
    
    public Chorganisation getCurrentCHOrganisation() {
        Chorganisation chorg = new Chorganisation();
        WebUser thisUser = getCurrentUser();
        chorg.setId(thisUser.getChorganisation().getId());
        return chorg;
    }
    
    public List<Chorganisation> getActiveChorganisation(){
        
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
    
    public List<Chorganisation> getChorganisation(){
        
        List<Chorganisation> chorganisations = new ArrayList<Chorganisation>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.addOrder(Order.asc("name"));
            chorganisations = findByCriteria(criteria);
        
        } catch (Throwable e) {
           e.printStackTrace();
        }    
        
        return chorganisations;
    }
    
    public Chorganisation getObject(int id) {
        return (Chorganisation) get(Chorganisation.class, id);
    }
    
    public Chorganisation updateObject(Chorganisation object) {
        
        try {
            save(object);
        } catch (Throwable e) {
           e.printStackTrace();
        }      
        return object;
    }

}