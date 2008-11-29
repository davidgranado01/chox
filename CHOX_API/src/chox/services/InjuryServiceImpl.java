package chox.services;

import chox.model.XMLParseResult;
import chox.model.Incident;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import chox.Util.XmlHelper;
import chox.Util.DateHelper;
import chox.model.Injury;

public class InjuryServiceImpl extends DataService implements InjuryService {

    public Injury getInjuryByIncident(Incident incident){
         
        List injuries = new ArrayList<Injury>();
        Injury injury = null;
        
        try {
            Criteria criteria = currentSession.createCriteria(Injury.class).add(Restrictions.eq("incident", incident));
            injuries = criteria.list();
            if(injuries.size()>0){
                injury = (Injury)injuries.get(0);
            }
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        currentSession.clear();
        currentSession.disconnect();
        
        return injury;
    }
    
    public XMLParseResult saveInjuryForXMLUploader(XMLParseResult xmlParseResult) {

        if ((xmlParseResult.getInjuries()) != null) {

            for (Integer i = 0; i < (xmlParseResult.getInjuries()).size(); i++) {

                ((xmlParseResult.getInjuries()).get(i)).setCreatedBy(getCurrentUser().getId());
                ((xmlParseResult.getInjuries()).get(i)).setCreatedDate(DateHelper.getCurrentTimeStamp());
                ((xmlParseResult.getInjuries()).get(i)).setLastModifiedBy(getCurrentUser().getId());
                ((xmlParseResult.getInjuries()).get(i)).setLastModifiedDate(DateHelper.getCurrentTimeStamp());

                if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                    try {
                        xmlParseResult.getCurrentSession().saveOrUpdate(((xmlParseResult.getInjuries()).get(i)));
                    } catch (Exception e) {
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                    }
                }
            }
        }

        return xmlParseResult;
    }

    public Injury getObject(int id) {
        return (Injury)currentSession.get(Injury.class, id);
    }

    public void updateObject(Injury injury) {

        currentSession.beginTransaction();
        currentSession.update(injury);
        currentSession.getTransaction().commit();
    }
}
