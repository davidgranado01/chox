package chox.xmlValidation.rules;


import chox.Util.TextHelper;
import chox.Util.XmlHelper;
import org.w3c.dom.*;
import com.filesystemsoftware.utils.XMLUtils;
import chox.model.*;
import java.util.ArrayList;

public class repairValidation {


    // VALIDATE REPAIR SECTION 
    public static XMLParseResult RepairSchemaValidation(
        XMLParseResult xmlParseResult,
        Element mainElement,
        Document doc) throws Exception {

        String strSectionName = "Engineer Report";
        
        // RESET VALIDATION FLAG
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        Element repairElement = XMLUtils.getElement(mainElement, "repair");
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "repair", strSectionName, "");
        
        Element eReportElement = XMLUtils.getElement(mainElement, "engineer-report");
        Element eRepairDetailElement = XMLUtils.getElement(mainElement, "repair-details");
        
        // VALIDATE ENGINEER REPORT 
        if(xmlParseResult.getIsCurrentScheValid()){
            
            if(eReportElement!=null){
                xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "engineer-report", strSectionName, "");
                if (xmlParseResult.getIsCurrentScheValid()) {
                    
                    xmlParseResult = engineerReportValidation.EngineerReportSchemaValidation(xmlParseResult, mainElement, strSectionName);
                    // EngineerReportValidation(xmlParseResult, mainElement, strSectionName);
                }
            }
            
            if(eRepairDetailElement!=null){
                xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "repair-details", strSectionName, "");
                if (xmlParseResult.getIsCurrentScheValid()) {
                    repairValidation repairCtrl = new repairValidation();
                    xmlParseResult = repairCtrl.RepairDetailsValidation(xmlParseResult, mainElement, strSectionName);
                }
            }
            
        }
        
        return xmlParseResult;
    }    
    
    private XMLParseResult RepairDetailsValidation(XMLParseResult xmlParseResult, Element mainElement, String strSectionName){
        
        Element eRepairDetailElement = XMLUtils.getElement(mainElement, "repair-details");
        
        if (xmlParseResult.getIsCurrentScheValid()) {

            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);

            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eRepairDetailElement, "repairer", XmlHelper.isMAN_RepairDetail_Repairer, "", strSectionName, "Name of Repairer");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eRepairDetailElement, "inspection-booked-date", XmlHelper.isMAN_RepairDetail_inspectionBookedDate, XmlHelper.REG_TIMESTAMP, strSectionName, "Inspection Booked Date");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eRepairDetailElement, "inspection-date", XmlHelper.isMAN_RepairDetail_InspectionDate, XmlHelper.REG_TIMESTAMP, strSectionName, "Inspection Date");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eRepairDetailElement, "repair-book-in-date", XmlHelper.isMAN_RepairDetail_RepairBookInDate, XmlHelper.REG_TIMESTAMP, strSectionName, "Repair Book In Date");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eRepairDetailElement, "repair-complete-date", XmlHelper.isMAN_RepairDetail_RepairCompleteDate, XmlHelper.REG_TIMESTAMP, strSectionName, "Repair Completion Date");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eRepairDetailElement, "name-ime", XmlHelper.isMAN_RepairDetail_NameIme, "", strSectionName, "Name of IME");

            if ((xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid())
                && (XmlHelper.isNotNull(XmlHelper.getNodeValue(eRepairDetailElement, "repairer"))
                || XmlHelper.isNotNull(XmlHelper.getNodeValue(eRepairDetailElement, "inspection-booked-date"))
                || XmlHelper.isNotNull(XmlHelper.getNodeValue(eRepairDetailElement, "inspection-date"))
                || XmlHelper.isNotNull(XmlHelper.getNodeValue(eRepairDetailElement, "repair-book-in-date"))
                || XmlHelper.isNotNull(XmlHelper.getNodeValue(eRepairDetailElement, "repair-complete-date"))
                || XmlHelper.isNotNull(XmlHelper.getNodeValue(eRepairDetailElement, "name-ime"))
                )
            ) {

                HireMonitoringDetail hireMonitoringDetail = new HireMonitoringDetail();

                if(xmlParseResult.getIsClaimExist() && xmlParseResult.getClaim().getHireMonitoringDetail()!=null){
                    hireMonitoringDetail = xmlParseResult.getClaim().getHireMonitoringDetail();
                }

                if(XmlHelper.isNotNull(XmlHelper.getNodeValue(eRepairDetailElement, "repairer"))){
                    hireMonitoringDetail.setNameOfRepairer(XmlHelper.getNodeValue(eRepairDetailElement, "repairer"));
                }
                
                if(XmlHelper.isNotNull(XmlHelper.getNodeValue(eRepairDetailElement, "inspection-booked-date"))){
                    hireMonitoringDetail.setInspectionBookedDate(XmlHelper.getTimeStampFromNode(eRepairDetailElement, "inspection-booked-date"));
                }

                if(XmlHelper.isNotNull(XmlHelper.getNodeValue(eRepairDetailElement, "inspection-date"))){
                    hireMonitoringDetail.setInspectionDate(XmlHelper.getTimeStampFromNode(eRepairDetailElement, "inspection-date"));
                }
                
                if(XmlHelper.isNotNull(XmlHelper.getNodeValue(eRepairDetailElement, "repair-book-in-date"))){
                    hireMonitoringDetail.setRepairBookInDate(XmlHelper.getTimeStampFromNode(eRepairDetailElement, "repair-book-in-date"));
                }
                
                if(XmlHelper.isNotNull(XmlHelper.getNodeValue(eRepairDetailElement, "repair-complete-date"))){
                    hireMonitoringDetail.setRepairCompletionDate(XmlHelper.getTimeStampFromNode(eRepairDetailElement, "repair-complete-date"));
                }
                
                if(XmlHelper.isNotNull(XmlHelper.getNodeValue(eRepairDetailElement, "name-ime"))){
                    hireMonitoringDetail.setNameOfIme(XmlHelper.getNodeValue(eRepairDetailElement, "name-ime"));
                }
                
                xmlParseResult.getClaim().setHireMonitoringDetail(hireMonitoringDetail);
            }
        }
        
        return xmlParseResult;
    }
        
}
