package chox.services;

import chox.Util.XmlHelper;
import chox.Util.DateHelper;
import chox.model.*;

public class VehicleHireServiceImpl  extends DataService implements VehicleHireService{

    public XMLParseResult saveVehicleHireForXMLUploader(XMLParseResult xmlParseResult){
        
        if((xmlParseResult.getClaim().getVehicleHire())!=null){
            
            (xmlParseResult.getClaim().getVehicleHire()).setCreatedBy(getCurrentUser().getId());
            (xmlParseResult.getClaim().getVehicleHire()).setCreatedDate(DateHelper.getCurrentTimeStamp());
            (xmlParseResult.getClaim().getVehicleHire()).setLastModifiedBy(getCurrentUser().getId());
            (xmlParseResult.getClaim().getVehicleHire()).setLastModifiedDate(DateHelper.getCurrentTimeStamp());

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
