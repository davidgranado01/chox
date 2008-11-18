package chox.services;

import chox.data.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import chox.model.*;
import java.util.ArrayList;

public class WitnessServiceImpl {

    public static XMLParseResult saveWitnessForXMLUploader(Session currentSession, XMLParseResult xmlParseResult){
        
        ArrayList<Witness> witnesses = xmlParseResult.getWitnesses();
        
        
        if(witnesses!=null){
            
            for(Integer i = 0; i<witnesses.size(); i++){

                Witness witness = witnesses.get(i);
                
                System.out.println("1:" + witness.getName());

                witness.setCreatedBy(WebUserServiceImpl.getCurrentUser());
                witness.setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
                witness.setLastModifiedBy(WebUserServiceImpl.getCurrentUser());
                witness.setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());

                if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                    try{
                        currentSession.saveOrUpdate(witness);
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
