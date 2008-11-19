package chox.services;

import chox.data.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import chox.model.*;

public class VehicleHireServiceImpl {

    public static XMLParseResult saveVehicleHireForXMLUploader(Session currentSession, XMLParseResult xmlParseResult){
        
        if((xmlParseResult.getVehiclehires())!=null){
            
            for(Integer i=0; i<(xmlParseResult.getVehiclehires()).size(); i++){
                
                ((xmlParseResult.getVehiclehires()).get(i)).setCreatedBy(WebUserServiceImpl.getCurrentUser());
                ((xmlParseResult.getVehiclehires()).get(i)).setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
                ((xmlParseResult.getVehiclehires()).get(i)).setLastModifiedBy(WebUserServiceImpl.getCurrentUser());
                ((xmlParseResult.getVehiclehires()).get(i)).setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());

                if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                    try{
                        currentSession.saveOrUpdate(((xmlParseResult.getVehiclehires()).get(i)));
                    } catch (Exception e) {
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                    }
                }
            }
        }
        
        return xmlParseResult;
    }      
}
