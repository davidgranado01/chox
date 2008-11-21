package chox.services;

import chox.data.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import chox.model.*;

public class VehicleHireServiceImpl implements VehicleHireService{

    public XMLParseResult saveVehicleHireForXMLUploader(XMLParseResult xmlParseResult){
        
        if((xmlParseResult.getClaim().getVehicleHire())!=null){
            
            (xmlParseResult.getClaim().getVehicleHire()).setCreatedBy(WebUserServiceImpl.getCurrentUser());
            (xmlParseResult.getClaim().getVehicleHire()).setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
            (xmlParseResult.getClaim().getVehicleHire()).setLastModifiedBy(WebUserServiceImpl.getCurrentUser());
            (xmlParseResult.getClaim().getVehicleHire()).setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());

            if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                try{
                    xmlParseResult.getCurrentSession().saveOrUpdate(xmlParseResult.getClaim().getVehicleHire());
                } catch (Exception e) {
                    xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                }
            }
        }
        
        
        return xmlParseResult;
    }      
}
