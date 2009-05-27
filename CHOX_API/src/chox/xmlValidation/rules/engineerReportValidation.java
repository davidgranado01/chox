/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.xmlValidation.rules;

import chox.Util.TextHelper;
import chox.Util.XmlHelper;
import org.w3c.dom.*;
import com.filesystemsoftware.utils.XMLUtils;
import chox.model.*;
import java.util.ArrayList;

public class engineerReportValidation {

    public static XMLParseResult EngineerReportSchemaValidation(
                XMLParseResult xmlParseResult, 
                Element mainElement, 
                String strSectionName){
        
        try{
            
            Element eReportElement = XMLUtils.getElement(mainElement, "engineer-report");

            if (xmlParseResult.getIsCurrentScheValid()) {

                xmlParseResult.setIsCurrentDataValid(true);
                xmlParseResult.setIsCurrentScheValid(true);

                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eReportElement, "labour-amount", XmlHelper.isMAN_Repair_engineerReport_labour_Amount, XmlHelper.REG_BIGDECIMAL, strSectionName, "Estimated Labour Amount");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eReportElement, "total-amount", XmlHelper.isMAN_Repair_engineerReport_total_Amount, XmlHelper.REG_BIGDECIMAL, strSectionName, "Estimated Total Repair Amount");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eReportElement, "days", XmlHelper.isMAN_Repair_engineerReport_days, XmlHelper.REG_INTEGER, strSectionName, "Estimated Days Under Repair");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eReportElement, "name", XmlHelper.isMAN_Repair_engineerReport_name, "", strSectionName, "Engineer Name");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eReportElement, "company", XmlHelper.isMAN_Repair_engineerReport_company, "", strSectionName, "Engineer Company");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eReportElement, "address1", XmlHelper.isMAN_Repair_engineerReport_address1, "", strSectionName, "Engineer Address1");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eReportElement, "address2", XmlHelper.isMAN_Repair_engineerReport_address2, "", strSectionName, "Engineer Address2");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eReportElement, "address3", XmlHelper.isMAN_Repair_engineerReport_address3, "", strSectionName, "Engineer Address3");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eReportElement, "address4", XmlHelper.isMAN_Repair_engineerReport_address4, "", strSectionName, "Engineer Address4");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eReportElement, "address5", XmlHelper.isMAN_Repair_engineerReport_address5, "", strSectionName, "Engineer Address5");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eReportElement, "postcode", XmlHelper.isMAN_Repair_engineerReport_postcode, "", strSectionName, "Engineer Postcode");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eReportElement, "telephone", XmlHelper.isMAN_Repair_engineerReport_telephone, XmlHelper.REG_PHONE, strSectionName, "Engineer Telephone");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eReportElement, "email", XmlHelper.isMAN_Repair_engineerReport_email, XmlHelper.REG_EMAIL, strSectionName, "Engineer Email");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, eReportElement, "usable", XmlHelper.isMAN_Repair_engineerReport_usable, "", strSectionName, "Usable");

                if ((xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid())
                    && (XmlHelper.isNotNull(XmlHelper.getNodeValue(eReportElement, "labour-amount"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(eReportElement, "total-amount"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(eReportElement, "days"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(eReportElement, "name"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(eReportElement, "company"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(eReportElement, "address1"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(eReportElement, "address2"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(eReportElement, "address3"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(eReportElement, "address4"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(eReportElement, "address5"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(eReportElement, "postcode"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(eReportElement, "telephone"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(eReportElement, "email"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(eReportElement, "usable"))
                    )
                ) {

                    EngineerReport engineerReport = new EngineerReport();

                    if(xmlParseResult.getIsClaimExist() && xmlParseResult.getClaim().getEngineerReport()!=null){
                        engineerReport = xmlParseResult.getClaim().getEngineerReport();
                    }

                    engineerReport.setDays(XmlHelper.getIntegerFromNode(eReportElement, "days"));
                    engineerReport.setLabourAmount(XmlHelper.getBigDecimalFromNode(eReportElement, "labour-amount"));
                    engineerReport.setTotalAmount(XmlHelper.getBigDecimalFromNode(eReportElement, "total-amount"));

                    engineerReport.setName(XmlHelper.getNodeValue(eReportElement, "name"));
                    engineerReport.setCompany(XmlHelper.getNodeValue(eReportElement, "company"));
                    engineerReport.setAddress1(XmlHelper.getNodeValue(eReportElement, "address1"));
                    engineerReport.setAddress2(XmlHelper.getNodeValue(eReportElement, "address2"));
                    engineerReport.setAddress3(XmlHelper.getNodeValue(eReportElement, "address3"));
                    engineerReport.setAddress4(XmlHelper.getNodeValue(eReportElement, "address4"));
                    engineerReport.setAddress5(XmlHelper.getNodeValue(eReportElement, "address5"));
                    engineerReport.setPostcode(XmlHelper.getNodeValue(eReportElement, "postcode"));
                    engineerReport.setTelephone(XmlHelper.getNodeValue(eReportElement, "telephone"));
                    engineerReport.setEmail(XmlHelper.getEmailAddressFromNode(eReportElement, "email"));
                    engineerReport.setIsUsable(XmlHelper.getBooleanFromNode(eReportElement, "usable"));

                    xmlParseResult.getClaim().setEngineerReport(engineerReport);
                }
            }
        
        } catch (Throwable t) {
            t.printStackTrace();
        }  
        
        return xmlParseResult;
    }
    
}
