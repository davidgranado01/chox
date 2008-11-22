package chox.services;

import chox.model.*;
import java.util.ArrayList;

public class WitnessServiceImpl  extends DataService implements WitnessService{

    public XMLParseResult saveWitnessForXMLUploader(XMLParseResult xmlParseResult){
        
        ArrayList<Witness> witnesses = xmlParseResult.getWitnesses();
        
        
        if(witnesses!=null){
            
            for(Integer i = 0; i<witnesses.size(); i++){

                Witness witness = witnesses.get(i);

                witness.setCreatedBy(getCurrentUser().getId());
                witness.setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
                witness.setLastModifiedBy(getCurrentUser().getId());
                witness.setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());

                if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                    try{
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
