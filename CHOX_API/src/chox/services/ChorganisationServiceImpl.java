package chox.services;

import chox.model.*;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ChorganisationServiceImpl  extends SecureDataService implements ChorganisationService{
    
    protected InsurerChorganisationService insurerChorganisationService;
    
    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) {
        this.insurerChorganisationService = insurerChorganisationService;
    }
    
    public List<Chorganisation> getAvailableChorganisationByInsurer(int insurerId) {

        List<Chorganisation> objects = new ArrayList<Chorganisation>();

        try{

            List<Chorganisation> allchos = getActiveChorganisation();

            for(Chorganisation org : allchos){
                
                if(!insurerChorganisationService.isActiveInsurerChorganisationExist(insurerId, org.getId())){
                    
                    System.out.println("getAvailableChorganisationByInsurer TRUE : " + insurerId + "|" +org.getId() + "|" + org.getName());

                    objects.add(org);
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return objects;
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
    
    public void updateObject(Chorganisation object) {
        try {
            save(object);
        } catch (Throwable e) {
           e.printStackTrace();
        }          
    }

}