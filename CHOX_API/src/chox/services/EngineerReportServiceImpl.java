package chox.services;

import chox.model.*;

public class EngineerReportServiceImpl implements EngineerReportService{

    public XMLParseResult saveEngineerReportForXMLUploader(XMLParseResult xmlParseResult){
        
        if((xmlParseResult.getClaim().getEngineerReport())!=null){
                
            (xmlParseResult.getClaim().getEngineerReport()).setCreatedBy(WebUserServiceImpl.getCurrentUser());
            (xmlParseResult.getClaim().getEngineerReport()).setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
            (xmlParseResult.getClaim().getEngineerReport()).setLastModifiedBy(WebUserServiceImpl.getCurrentUser());
            (xmlParseResult.getClaim().getEngineerReport()).setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());

            if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                try{
                    xmlParseResult.getCurrentSession().saveOrUpdate(xmlParseResult.getClaim().getEngineerReport());
                } catch (Exception e) {
                    xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                }
            }
        }
        
        return xmlParseResult;
    }  
}
