package chox.services;

import chox.model.XMLParseResult;
import chox.model.Incident;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import chox.model.Injury;
import org.hibernate.criterion.DetachedCriteria;

public class InjuryServiceImpl extends DataService implements InjuryService {

    public Injury getInjuryByIncident(Incident incident){
         
        List injuries = new ArrayList<Injury>();
        Injury injury = null;
        
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Injury.class).add(Restrictions.eq("incident", incident));
            injuries = findByCriteria(criteria);
            if(injuries.size()>0){
                injury = (Injury)injuries.get(0);
            }
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        
        
        
        return injury;
    }
    
    public void saveObjectForXMLUploader(final XMLParseResult xmlParseResult) {
        if ((xmlParseResult.getInjuries()) != null) {
            for (Integer i = 0; i < (xmlParseResult.getInjuries()).size(); i++) {
                save(((xmlParseResult.getInjuries()).get(i)));
            }
        }        
    }

    public Injury getObject(int id) {
        return (Injury)get(Injury.class, id);
    }

    public void updateObject(Injury injury) {

        save(injury);
    }
    
    public Injury getObjectByIncidentId(Incident incident)
    {
        Injury injury = null;
        
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Injury.class).add(Restrictions.eq("incident", incident));
            injury = (Injury)getByCriteria(criteria);       
            
        } catch (Throwable e) {
           e.printStackTrace();
        }  
        return injury;
    }
}
