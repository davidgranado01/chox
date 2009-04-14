package chox.services;

import chox.model.*;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ChorganisationServiceImpl  extends SecureDataService implements ChorganisationService{

    public Chorganisation getCurrentCHOrganisation() {
        
        Chorganisation chorg = new Chorganisation();
        WebUser thisUser = getCurrentUser();
        chorg.setId(thisUser.getChorganisation().getId());
        return chorg;
        
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