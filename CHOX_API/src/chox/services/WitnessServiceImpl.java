package chox.services;

import chox.Util.XmlHelper;
import chox.Util.DateHelper;
import chox.model.*;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class WitnessServiceImpl extends DataService implements WitnessService {

    public Witness getWitnessByIncident(Incident incident){
        
        List witnesses = new ArrayList<Witness>();
        Witness witness = null;
        
        try {
            Criteria criteria = getCurrentSession().createCriteria(Witness.class).add(Restrictions.eq("incident", incident));
            witnesses = criteria.list();
            
            if(witnesses.size()>0){
                witness = (Witness)witnesses.get(0);
            }
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        
        
        
        return witness;
    }
    
    public Witness getObject(int id) {
        return (Witness) getCurrentSession().get(Witness.class, id);
    }

    public void updateObject(Witness witness) {

        getCurrentSession().beginTransaction();
        getCurrentSession().update(witness);
        getCurrentSession().getTransaction().commit();
    }

    public XMLParseResult saveWitnessForXMLUploader(XMLParseResult xmlParseResult) {

        ArrayList<Witness> witnesses = xmlParseResult.getWitnesses();


        if (witnesses != null) {

            for (Integer i = 0; i < witnesses.size(); i++) {

                Witness witness = witnesses.get(i);

                if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                    try {
                        xmlParseResult.getCurrentSession().saveOrUpdate(witness);
                    } catch (Exception e) {
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                    }
                //xmlParseResult.setWitnesses(witness);
                }
            }
        }
        return xmlParseResult;
    }
}
