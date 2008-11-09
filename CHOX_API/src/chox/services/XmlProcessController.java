package chox.services;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.*;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 
import com.filesystemsoftware.utils.XMLUtils;
import com.filesystemsoftware.utils.Logger;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.regex.*;


import chox.model.*;

public class XmlProcessController {

    public static void main(String[] args) {
    
        try {
            
            String sXMLPath1 = "C:/Project Workplace/Greefinch/choxida/CHOX_STAGE_1_SUBMISSION.xml";
            String sXMLPath2 = "C:/Project Workplace/Greefinch/choxida/CHOX_STAGE_2_SUBMISSION.xml";
            String sUpdateType = "A";
            
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File(sXMLPath2));
            
            doc.getDocumentElement().normalize ();
            Element root = doc.getDocumentElement();
            
            if (root != null && root.getTagName().equals("chox")) {
            
                ArrayList<XMLParseResult> xmlParseResults = new ArrayList<XMLParseResult>();
                ArrayList<Element> rentalElements = XMLUtils.getElements(doc, root, "rental");

                int count = 0;
                for (Element re : rentalElements) {
                    
                    try {
                        count++;
                        XMLParseResult xmlParseResult = new XMLParseResult();
                        xmlParseResult = isXMLSchemaAndDataValidate(xmlParseResult, doc, re, sUpdateType);
                    } catch (Exception e) {
                        Logger.err.println("Error loading record " + count);
                        throw e;
                    }
                }
            }
        
        }catch (SAXParseException err) {
            System.out.println ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
            System.out.println(" " + err.getMessage ());
        }catch (SAXException e) {
            Exception x = e.getException ();
            ((x == null) ? e : x).printStackTrace ();
        }catch (Throwable t) {
            t.printStackTrace ();
        }
    }
    
    private static XMLParseResult isXMLSchemaAndDataValidate(
            XMLParseResult xmlParseResult, 
            Document doc,
            Element root,
            String sUploadType) throws Exception {
        
        Rental rental = new Rental();
        xmlParseResult.setRental(rental);
        
        Boolean bClaimFlag__ = false;
        Boolean bInvoiceFlag = false;
        Boolean bFlag = false;
        
        xmlParseResult = isFirstContactSchemaExist(xmlParseResult, root, doc);
        xmlParseResult = isSupplierSchemaExist(xmlParseResult, root, doc);
        xmlParseResult = isInvoiceSchemaExist(xmlParseResult, root, doc);
        xmlParseResult = isRentalStatusSchemaExist(xmlParseResult, root, doc);
        xmlParseResult = isRentalVehiclesSchemaExist(xmlParseResult, root, doc);
        xmlParseResult = isDriversSchemaExist(xmlParseResult, root, doc);
        xmlParseResult = isClaimSchemaExist(xmlParseResult, root, doc);
        xmlParseResult = isRepairSchemaExist(xmlParseResult, root, doc);
        
        /*
        if(isFirstContactSchemaExist(root) 
            && isSupplierSchemaExist(root) 
            && isRentalStatusSchemaExist(root)
            && isDriversSchemaExist(root)
            && isClaimSchemaExist(root)){
            bClaimFlag__ = true;
        }
        
        if(isRepairSchemaExist(root) 
            && isInvoiceSchemaExist(root)
            && isRentalVehiclesSchemaExist(root)){
            bInvoiceFlag = true;
        }
        */
        
        if(bClaimFlag__){
            
            bFlag = true;
            //&& (bInvoiceFlag || (sUploadType!="I"))
            if(sUploadType=="I" && bInvoiceFlag){
                bFlag = false;
            }
        }
        
        System.out.println (" ** getIsSchemaValid: " + xmlParseResult.getIsSchemaValid());
        System.out.println (" ** getSchemaValidationRemark: " + xmlParseResult.getSchemaValidationRemark());
        System.out.println (" ** getIsDataValid: " + xmlParseResult.getIsDataValid());
        System.out.println (" ** getDataValidationRemark: " + xmlParseResult.getDataValidationRemark());
                
        System.out.println ("********************************************");
        
        
        return xmlParseResult;
    }

    // VALIDATE RENTAL CONTACT SECTION 
    private static XMLParseResult isFirstContactSchemaExist(
            XMLParseResult xmlParseResult, 
            Element root,
            Document doc) throws Exception {
        
        String mainNodeName = "rental";
        String nodeName1 = "first-contact";
        
        String clidNodeLabelMain = XmlHelper.contructureErrorMessagePath(mainNodeName, "");
 
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        // CHECK MAIN ELEMENT
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, root, nodeName1, XmlHelper.isMAN_First_Contact, XmlHelper.REG_TIMESTAMP ,clidNodeLabelMain);

        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            
        }
        return xmlParseResult;
    } 
    
    // VALIDATE SUPPLIER DETAIL SECTION
    private static XMLParseResult isSupplierSchemaExist(
            XMLParseResult xmlParseResult, 
            Element mainElement,
            Document doc){

        String parentNodeName = "rental";
        String mainNodeName = "supplier";
        String nodeName1 = "supplier-name";
        String nodeName2 = "supplier-reference";
        
        String clidNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, "");
        String clidNodeLabel1 = XmlHelper.contructureErrorMessagePath(clidNodeLabelMain, mainNodeName);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, mainElement, mainNodeName, clidNodeLabelMain);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            
            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);
        
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, nodeName1, XmlHelper.isMAN_Supplier_Name, "", clidNodeLabel1);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, nodeName2, XmlHelper.isMAN_Supplier_Reference, "", clidNodeLabel1);
            
            if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            }
        }
        
        return xmlParseResult;
    }    

    // VALIDATE RENTAL STATUS SECTION 
    private static XMLParseResult isRentalStatusSchemaExist(
            XMLParseResult xmlParseResult, 
            Element root,
            Document doc) throws Exception {
        
        String mainNodeName = "rental";
        String nodeName1 = "rental-status";
        
        String clidNodeLabelMain = XmlHelper.contructureErrorMessagePath(mainNodeName, "");
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        // CHECK MAIN ELEMENT
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, root, nodeName1, XmlHelper.isMAN_Status, XmlHelper.REG_WORD, clidNodeLabelMain);
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
        }
        
        return xmlParseResult;        
    }
        
    // VALIDATE DRIVER SECTION 
    private static XMLParseResult isDriversSchemaExist(
            XMLParseResult xmlParseResult, 
            Element mainElement,
            Document doc) throws Exception {
        
        String parentNodeName = "rental";
        String mainNodeName = "drivers";
        String nodeName1 = "driver";
        
        String clidNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, "");
        String clidNodeLabel1 = XmlHelper.contructureErrorMessagePath(clidNodeLabelMain, mainNodeName);
        String clidNodeLabel2 = XmlHelper.contructureErrorMessagePath(clidNodeLabel1, nodeName1);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, mainElement, mainNodeName, clidNodeLabelMain);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            
            // DRIVER
            ArrayList<Element> driverElements = XMLUtils.getElements(doc, thisElement, nodeName1);
            
            xmlParseResult.setIsCurrentScheValid(true);
            xmlParseResult = isSchemaNodeListExist(xmlParseResult, driverElements, nodeName1, clidNodeLabel1);
            
            if(xmlParseResult.getIsCurrentScheValid()){
                for (Element ee : driverElements) {

                    xmlParseResult.setIsCurrentDataValid(true);
                    xmlParseResult.setIsCurrentScheValid(true);

                    xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "title", XmlHelper.isMAN_Driver_Title, "", clidNodeLabel2);
                    xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "firstnames", XmlHelper.isMAN_Driver_Firstnames, "", clidNodeLabel2);
                    xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "lastname", XmlHelper.isMAN_Driver_Lastname, "", clidNodeLabel2);
                    xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "address1", XmlHelper.isMAN_Driver_Address1, "", clidNodeLabel2);
                    xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "address2", XmlHelper.isMAN_Driver_Address2, "", clidNodeLabel2);
                    xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "address3", XmlHelper.isMAN_Driver_Address3, "", clidNodeLabel2);
                    xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "address4", XmlHelper.isMAN_Driver_Address4, "", clidNodeLabel2);
                    xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "address5", XmlHelper.isMAN_Driver_Address5, "", clidNodeLabel2);
                    xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "postcode", XmlHelper.isMAN_Driver_Postcode, "", clidNodeLabel2);
                    xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "telephone-day", XmlHelper.isMAN_Driver_Telephone_day, XmlHelper.REG_PHONE, clidNodeLabel2);
                    xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "telephone-evening", XmlHelper.isMAN_Driver_Telephone_Evening, XmlHelper.REG_PHONE, clidNodeLabel2);
                    xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "email", XmlHelper.isMAN_Driver_Email, XmlHelper.REG_EMAIL, clidNodeLabel2);
                    xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "primary-driver", XmlHelper.isMAN_Driver_Primary_Driver, "", clidNodeLabel2);
                }
            }                
        }
        
        return xmlParseResult;
    }  
    
    // VALIDATE CLAIM SECTION 
    private static XMLParseResult isClaimSchemaExist(
            XMLParseResult xmlParseResult, 
            Element root,
            Document doc) throws Exception {
        
        String parentNodeName = "rental";
        String nodeName = "claim";
        
        String clidNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, nodeName);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, nodeName, clidNodeLabelMain);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            Element claimNodeElement = XMLUtils.getElement(root, nodeName);
            xmlParseResult = isCustomerSchemaNodeExist(xmlParseResult, claimNodeElement, doc, clidNodeLabelMain);
            xmlParseResult = isReplacementVehicleSchemaNodeExist(xmlParseResult, claimNodeElement, doc, clidNodeLabelMain);
            xmlParseResult = isThirdPartySchemaNodeExist(xmlParseResult, claimNodeElement, doc, clidNodeLabelMain);
            xmlParseResult = isIncidentSchemaNodeExist(xmlParseResult, claimNodeElement, doc, clidNodeLabelMain);
        }
        
        return xmlParseResult;
    }  
    
    private static XMLParseResult isCustomerSchemaNodeExist(
            XMLParseResult xmlParseResult, 
            Element mainElement,
            Document doc,
            String parentNodeName) throws Exception {
        
        String mainNodeName = "customer";
        String nodeName1 = "insurer";
        String nodeName2 = "vehicle";
        
        String clidNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, mainNodeName);
        String clidNodeLabel1 = XmlHelper.contructureErrorMessagePath(clidNodeLabelMain, nodeName1);
        String clidNodeLabel2 = XmlHelper.contructureErrorMessagePath(clidNodeLabelMain, nodeName2);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, mainElement, mainNodeName, clidNodeLabelMain);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, mainElement, nodeName1, clidNodeLabel1);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, mainElement, nodeName2, clidNodeLabel2);
        
         if(xmlParseResult.getIsCurrentScheValid()){
            
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            
            // INSURER
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "name", XmlHelper.isMAN_Claim_Customer_Insurer_name, "", clidNodeLabel1);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "policy-number", XmlHelper.isMAN_Claim_Customer_Insurer_policyNumber, "", clidNodeLabel1);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "claim-reference", XmlHelper.isMAN_Claim_Customer_Insurer_claimReference, "", clidNodeLabel1);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "comprehensive", XmlHelper.isMAN_Claim_Customer_Insurer_comprehensive, "", clidNodeLabel1);
            
            // VEHICLE
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vehicle-registration", XmlHelper.isMAN_Claim_Customer_Vehicle_Registration, "", clidNodeLabel2);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vehicle-manufacturer", XmlHelper.isMAN_Claim_Customer_Vehicle_Manufacturer, "", clidNodeLabel2);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vehicle-model", XmlHelper.isMAN_Claim_Customer_Vehicle_Model, "", clidNodeLabel2);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vehicle-class", XmlHelper.isMAN_Claim_Customer_Vehicle_Class, "", clidNodeLabel2);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "usable", XmlHelper.isMAN_Claim_Customer_Vehicle_Usable, "", clidNodeLabel2);            
            
         }
        return xmlParseResult;
    }
    
    private static XMLParseResult isReplacementVehicleSchemaNodeExist(
            XMLParseResult xmlParseResult, 
            Element mainElement,
            Document doc,
            String parentNodeName) throws Exception {
        
        String mainNodeName = "replacement-vehicle";
        String nodeName1 = "vehicle-class";
        
        String clidNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, mainNodeName);
        String clidNodeLabel1 = XmlHelper.contructureErrorMessagePath(clidNodeLabelMain, nodeName1);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, mainElement, mainNodeName, clidNodeLabelMain);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, nodeName1, XmlHelper.isMAN_Claim_ReplacementVehicle_VehicleClass, "", clidNodeLabel1);
        }
        return xmlParseResult;
    }
    
    private static XMLParseResult isThirdPartySchemaNodeExist(
            XMLParseResult xmlParseResult, 
            Element mainElement,
            Document doc,
            String parentNodeName) throws Exception {
        
        String mainNodeName = "third-party";
        String nodeName1 = "insurer";
        String nodeName2 = "vehicle";
        String nodeName3 = "driver";
        
        String clidNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, mainNodeName);
        String clidNodeLabel1 = XmlHelper.contructureErrorMessagePath(clidNodeLabelMain, nodeName1);
        String clidNodeLabel2 = XmlHelper.contructureErrorMessagePath(clidNodeLabelMain, nodeName2);
        String clidNodeLabel3 = XmlHelper.contructureErrorMessagePath(clidNodeLabelMain, nodeName3);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, mainElement, mainNodeName, clidNodeLabelMain);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, mainElement, nodeName1, clidNodeLabel1);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, mainElement, nodeName2, clidNodeLabel2);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, mainElement, nodeName3, clidNodeLabel3);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            
            // INSURER
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "name", XmlHelper.isMAN_Claim_ThirdParty_Insurer_Name, "", clidNodeLabel1);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "policy-number", XmlHelper.isMAN_Claim_ThirdParty_Insurer_PolicyNumber, "", clidNodeLabel1);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "claim-reference", XmlHelper.isMAN_Claim_ThirdParty_Insurer_ClaimReference, "", clidNodeLabel1);
            
            // VEHICLE
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vehicle-registration", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_Registration, "", clidNodeLabel2);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vehicle-manufacturer", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_manufacturer, "", clidNodeLabel2);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vehicle-model", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_model, "", clidNodeLabel2);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vehicle-class", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_class, "", clidNodeLabel2);
            
            // DRIVER
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "title", XmlHelper.isMAN_Claim_ThirdParty_Driver_Title, "", clidNodeLabel3);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "firstnames", XmlHelper.isMAN_Claim_ThirdParty_Driver_Firstnames, "", clidNodeLabel3);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "lastname", XmlHelper.isMAN_Claim_ThirdParty_Driver_Lastname, "", clidNodeLabel3);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "address1", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address1, "", clidNodeLabel3);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "address2", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address2, "", clidNodeLabel3);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "address3", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address3, "", clidNodeLabel3);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "address4", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address4, "", clidNodeLabel3);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "address5", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address5, "", clidNodeLabel3);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "postcode", XmlHelper.isMAN_Claim_ThirdParty_Driver_Postcode, "", clidNodeLabel3);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "telephone-day", XmlHelper.isMAN_Claim_ThirdParty_Driver_TelephoneDay, "", clidNodeLabel3);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "telephone-evening", XmlHelper.isMAN_Claim_ThirdParty_Driver_TelephoneEvening, "", clidNodeLabel3);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "email", XmlHelper.isMAN_Claim_ThirdParty_Driver_Email, "", clidNodeLabel3);
        }        
        return xmlParseResult;
    }    
    
    private static XMLParseResult isIncidentSchemaNodeExist(
            XMLParseResult xmlParseResult, 
            Element mainElement,
            Document doc,
            String parentNodeName) throws Exception {

        String mainNodeName = "incident";
        
        String clidNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, mainNodeName);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, mainElement, mainNodeName, clidNodeLabelMain);

        if(xmlParseResult.getIsCurrentScheValid()){
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "date", XmlHelper.isMAN_Claim_Incident_Date, XmlHelper.REG_TIMESTAMP, clidNodeLabelMain);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "location", XmlHelper.isMAN_Claim_Incident_Location, "", clidNodeLabelMain);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "police-involved", XmlHelper.isMAN_Claim_Incident_PoliceInvolved, "", clidNodeLabelMain);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "description", XmlHelper.isMAN_Claim_Incident_Description, "", clidNodeLabelMain);
        }
        return xmlParseResult;
    }   
    
    // VALIDATE CLAIM SECTION 
    private static XMLParseResult isRepairSchemaExist(
            XMLParseResult xmlParseResult, 
            Element root,
            Document doc) throws Exception {
        
        String nodeName = "repair";
        String subNodeName = "engineer-report";
        String strPath = "Repair:Engineer Report";
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, nodeName, "Repair");
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, subNodeName, "Engineer Report");
        
        if(xmlParseResult.getIsCurrentScheValid()){

            ArrayList<Element> repairElements = XMLUtils.getElements(doc, root, subNodeName);
            
            for (Element ee : repairElements) {
            
                xmlParseResult.setIsCurrentDataValid(true);
                xmlParseResult.setIsCurrentScheValid(true);
                
                xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "labour-amount", XmlHelper.isMAN_Driver_Title, XmlHelper.REG_BIGDECIMAL, strPath);
                xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "total-amount", XmlHelper.isMAN_Driver_Firstnames, XmlHelper.REG_BIGDECIMAL, strPath);
                xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "days", XmlHelper.isMAN_Driver_Lastname, XmlHelper.REG_INTEGER, strPath);
            }
        }
        return xmlParseResult;
    }  
    
    // VALIDATE INVOICE SECTION 
    private static XMLParseResult isInvoiceSchemaExist(XMLParseResult xmlParseResult, Element root, Document doc) throws Exception {
        
        String nodeName = "invoice";
        
        Boolean bFlag = false;
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, nodeName, "Invoice");
        
        if(xmlParseResult.getIsCurrentScheValid()){
            Element thisNodeElement = XMLUtils.getElement(root, nodeName);
            xmlParseResult = isInvoiceDetailValid(xmlParseResult, thisNodeElement, doc);
        }
        return xmlParseResult;
    }
    
    // VALIDATE INVOICE DETAIL SECTION
    private static XMLParseResult isInvoiceDetailValid(XMLParseResult xmlParseResult, Element thisElement, Document doc) throws Exception {
        
        String nodeName = "invoice";
       
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Net, XmlHelper.REG_BIGDECIMAL, "Invoice");
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Vat, XmlHelper.REG_BIGDECIMAL, "Invoice");
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Gross, XmlHelper.REG_BIGDECIMAL, "Invoice");
        
        /*
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisNodeElement, "net");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisNodeElement, "net", XmlHelper.isMAN_Invoice_Net, XmlHelper.REG_BIGDECIMAL);
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisNodeElement, "vat");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisNodeElement, "vat", XmlHelper.isMAN_Invoice_Vat, XmlHelper.REG_BIGDECIMAL);
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisNodeElement, "gross");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisNodeElement, "gross", XmlHelper.isMAN_Invoice_Gross, XmlHelper.REG_BIGDECIMAL);
        */
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
 
            BigDecimal bNet = new BigDecimal(getNodeValue(thisElement, "net"));
            BigDecimal bVat = new BigDecimal(getNodeValue(thisElement, "vat"));
            BigDecimal bGross = new BigDecimal(getNodeValue(thisElement, "gross"));
            
            System.out.println("Invoice Net :" + bNet);
            
            // SET SUPPLIER INFORMATION
            Invoice invoice = new Invoice();
            invoice.setNet(bNet);
            invoice.setGross(bGross);
            invoice.setVAT(bVat);
            
            // SET RENTAL INFORMATION
            Rental rental = xmlParseResult.getRental();
            rental.setInvoice(invoice);
            
            // VEHICLES
            xmlParseResult = isInvoiceDetail_VehiclesValid(xmlParseResult, thisElement);
            
            // REPAIR
            xmlParseResult = isInvoiceDetail_RepairValid(xmlParseResult, thisElement);
            
            // storage-recovery
            xmlParseResult = isInvoiceDetail_StorageRecoveryValid(xmlParseResult, thisElement);
            
            // engineer-fee
            xmlParseResult = isInvoiceDetail_EngineerFeeValid(xmlParseResult, thisElement);
            
            // claim-handling-fee
            xmlParseResult = isInvoiceDetail_ClaimHandlingFeeValid(xmlParseResult, thisElement);

            // extras
            xmlParseResult = isInvoiceDetail_ExtrasValid(xmlParseResult, thisElement, doc);
        }
        
        return xmlParseResult;
    } 
    
    // VALIDATE INVOICE DETAIL - Vehicles SECTION
    private static XMLParseResult isInvoiceDetail_VehiclesValid(XMLParseResult xmlParseResult, Element invoiceElement){
        
        String strPath = "Invoice:Vehicles";
        Element thisElement = XMLUtils.getElement(invoiceElement, "vehicles");
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Vehicles_Net, XmlHelper.REG_BIGDECIMAL, strPath);
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Vehicles_Vat, XmlHelper.REG_BIGDECIMAL, strPath);
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Vehicles_Gross, XmlHelper.REG_BIGDECIMAL, strPath);
        
        /*
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "net");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Vehicles_Net, XmlHelper.REG_BIGDECIMAL);
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "vat");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Vehicles_Vat, XmlHelper.REG_BIGDECIMAL);
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "gross");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Vehicles_Gross, XmlHelper.REG_BIGDECIMAL);
        */
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            BigDecimal bNet = new BigDecimal(getNodeValue(thisElement, "net"));
            BigDecimal bVat = new BigDecimal(getNodeValue(thisElement, "vat"));
            BigDecimal bGross = new BigDecimal(getNodeValue(thisElement, "gross"));
        }

        //TODO: SET TO DB
        
        return xmlParseResult;
    }

    // VALIDATE INVOICE DETAIL - repair SECTION
    private static XMLParseResult isInvoiceDetail_RepairValid(XMLParseResult xmlParseResult, Element invoiceElement){
        
        String strPath = "Invoice:Repair";
        Element thisElement = XMLUtils.getElement(invoiceElement, "repair");
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Repair_Net, XmlHelper.REG_BIGDECIMAL, strPath);
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Repair_Vat, XmlHelper.REG_BIGDECIMAL, strPath);
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Repair_Gross, XmlHelper.REG_BIGDECIMAL, strPath);
        
        /*
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "net");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Repair_Net, XmlHelper.REG_BIGDECIMAL);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "vat");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Repair_Vat, XmlHelper.REG_BIGDECIMAL);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "gross");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Repair_Gross, XmlHelper.REG_BIGDECIMAL);
        */
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){

            BigDecimal bNet = new BigDecimal(getNodeValue(thisElement, "net"));
            BigDecimal bVat = new BigDecimal(getNodeValue(thisElement, "vat"));
            BigDecimal bGross = new BigDecimal(getNodeValue(thisElement, "gross"));
        }
        
        //TODO: SET TO DB
        
        return xmlParseResult;
    }
    
    // VALIDATE INVOICE DETAIL - Claim Handling fee SECTION
    private static XMLParseResult isInvoiceDetail_ClaimHandlingFeeValid(XMLParseResult xmlParseResult, Element invoiceElement){
        
        String strPath = "Invoice:Claim Handling Fee";
        Element thisElement = XMLUtils.getElement(invoiceElement, "claim-handling-fee");
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Claim_Handling_Fee_Net, XmlHelper.REG_BIGDECIMAL, strPath);
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Claim_Handling_Fee_Vat, XmlHelper.REG_BIGDECIMAL, strPath);
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Claim_Handling_Fee_Gross, XmlHelper.REG_BIGDECIMAL, strPath);
        
        /*
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "net");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Claim_Handling_Fee_Net, XmlHelper.REG_BIGDECIMAL);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "vat");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Claim_Handling_Fee_Vat, XmlHelper.REG_BIGDECIMAL);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "gross");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Claim_Handling_Fee_Gross, XmlHelper.REG_BIGDECIMAL);
        */
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            BigDecimal bNet = new BigDecimal(getNodeValue(thisElement, "net"));
            BigDecimal bVat = new BigDecimal(getNodeValue(thisElement, "vat"));
            BigDecimal bGross = new BigDecimal(getNodeValue(thisElement, "gross"));
        }
        
        //TODO: SET TO DB
        
        return xmlParseResult;
    }

    // VALIDATE INVOICE DETAIL - Engineer Fee SECTION
    private static XMLParseResult isInvoiceDetail_EngineerFeeValid(XMLParseResult xmlParseResult, Element invoiceElement){
        
        String strPath = "Invoice:Enginee Fee";
        Element thisElement = XMLUtils.getElement(invoiceElement, "engineer-fee");
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Engineer_Fee_Net, XmlHelper.REG_BIGDECIMAL, strPath);
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Engineer_Fee_Vat, XmlHelper.REG_BIGDECIMAL, strPath);
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Engineer_Fee_Gross, XmlHelper.REG_BIGDECIMAL, strPath);
        
        /*
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "net");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Engineer_Fee_Net, XmlHelper.REG_BIGDECIMAL);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "vat");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Engineer_Fee_Vat, XmlHelper.REG_BIGDECIMAL);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "gross");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Engineer_Fee_Gross, XmlHelper.REG_BIGDECIMAL);
        */
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            BigDecimal bNet = new BigDecimal(getNodeValue(thisElement, "net"));
            BigDecimal bVat = new BigDecimal(getNodeValue(thisElement, "vat"));
            BigDecimal bGross = new BigDecimal(getNodeValue(thisElement, "gross"));
        }
        
        //TODO: SET TO DB
        
        return xmlParseResult;
    }

    // VALIDATE INVOICE DETAIL - Storage Recovery SECTION
    private static XMLParseResult isInvoiceDetail_StorageRecoveryValid(XMLParseResult xmlParseResult, Element invoiceElement){
        String strPath = "Invoice:Storage Recovery";
        Element thisElement = XMLUtils.getElement(invoiceElement, "storage-recovery");
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Storage_Recovery_Net, XmlHelper.REG_BIGDECIMAL, strPath);
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Storage_Recovery_Vat, XmlHelper.REG_BIGDECIMAL, strPath);
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Storage_Recovery_Gross, XmlHelper.REG_BIGDECIMAL, strPath);
        
        /*
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "net");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Storage_Recovery_Net, XmlHelper.REG_BIGDECIMAL);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "vat");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Storage_Recovery_Vat, XmlHelper.REG_BIGDECIMAL);
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "gross");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Storage_Recovery_Gross, XmlHelper.REG_BIGDECIMAL);
        */
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            BigDecimal bNet = new BigDecimal(getNodeValue(thisElement, "net"));
            BigDecimal bVat = new BigDecimal(getNodeValue(thisElement, "vat"));
            BigDecimal bGross = new BigDecimal(getNodeValue(thisElement, "gross"));
        }
        
        //TODO: SET TO DB
        
        return xmlParseResult;
    }
    
    // VALIDATE INVOICE DETAIL - Storage Recovery SECTION
    private static XMLParseResult isInvoiceDetail_ExtrasValid(
            XMLParseResult xmlParseResult, 
            Element invoiceElement, 
            Document doc) throws Exception {
        
        String strPath = "Invoice:Extras";
        Element thisElement = XMLUtils.getElement(invoiceElement, "extras");
        ArrayList<Element> extraElements = XMLUtils.getElements(doc, thisElement, "extra");

        for (Element ee : extraElements) {
            
            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);
            
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "name", XmlHelper.isMAN_Invoice_Extras_Name, "", strPath);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "quantity", XmlHelper.isMAN_Invoice_Extras_Quantity, XmlHelper.REG_INTEGER, strPath);
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, ee, "item-cost", XmlHelper.isMAN_Invoice_Extras_Item_Cost, XmlHelper.REG_BIGDECIMAL, strPath);
            
            /*
            xmlParseResult = isSchemaNodeExist(xmlParseResult, ee, "name");
            xmlParseResult = isSchemaValueValid(xmlParseResult, ee, "name", XmlHelper.isMAN_Invoice_Extras_Name, "");
            
            xmlParseResult = isSchemaNodeExist(xmlParseResult, ee, "quantity");
            xmlParseResult = isSchemaValueValid(xmlParseResult, ee, "quantity", XmlHelper.isMAN_Invoice_Extras_Quantity, XmlHelper.REG_INTEGER);
            
            xmlParseResult = isSchemaNodeExist(xmlParseResult, ee, "item-cost");
            xmlParseResult = isSchemaValueValid(xmlParseResult, ee, "item-cost", XmlHelper.isMAN_Invoice_Extras_Item_Cost, XmlHelper.REG_BIGDECIMAL);
            */
            
            if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
                BigDecimal thisExtraItemCost = new BigDecimal(getNodeValue(thisElement, "item-cost"));
                Integer thisExtraQuantity = new Integer(getNodeValue(thisElement, "quantity"));
                String thisExtraName = getNodeValue(thisElement, "name");
            }
        }
        
        return xmlParseResult;
    }
    
    // VALIDATE INVOICE SECTION 
    private static XMLParseResult isRentalVehiclesSchemaExist(
            XMLParseResult xmlParseResult, 
            Element root,
            Document doc) throws Exception {
        
        String nodeName = "rental-vehicles";
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        // CHECK MAIN ELEMENT
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, nodeName, "Rental Vehicles");

        if(xmlParseResult.getIsCurrentScheValid()){
        
               Element thisElement = XMLUtils.getElement(root, nodeName);
               ArrayList<Element> rentalVehicleElements = XMLUtils.getElements(doc, thisElement, "rental-vehicle");
               
               for (Element ee : rentalVehicleElements) {
                    xmlParseResult = isRentalVehiclesDetailExist(xmlParseResult, ee, doc);
               }
        }
        
        return xmlParseResult;
    } 
    
    private static XMLParseResult isRentalVehiclesDetailExist(
            XMLParseResult xmlParseResult, 
            Element thisElement,
            Document doc) throws Exception {
        String strPath = "Rental Vehicle";
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);

        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vehicle-registration", XmlHelper.isMAN_RentalVehicles_Vehicle_Registration, "", strPath);
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vehicle-manufacturer", XmlHelper.isMAN_RentalVehicles_Vehicle_Manufacturer, "", strPath);
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vehicle-model", XmlHelper.isMAN_RentalVehicles_Vehicle_Model, "", strPath);
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "vehicle-class", XmlHelper.isMAN_RentalVehicles_Vehicle_Class, "", strPath);
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "rental-start", XmlHelper.isMAN_RentalVehicles_Rental_Start, XmlHelper.REG_TIMESTAMP, strPath);
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "rental-end", XmlHelper.isMAN_RentalVehicles_Rental_End, XmlHelper.REG_TIMESTAMP, strPath);
        xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "rental-days", XmlHelper.isMAN_RentalVehicles_Rental_Days, XmlHelper.REG_INTEGER, strPath);
        
        /*
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "vehicle-registration");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "vehicle-registration", XmlHelper.isMAN_RentalVehicles_Vehicle_Registration, "");
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "vehicle-manufacturer");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "vehicle-manufacturer", XmlHelper.isMAN_RentalVehicles_Vehicle_Manufacturer, "");
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "vehicle-model");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "vehicle-model", XmlHelper.isMAN_RentalVehicles_Vehicle_Model, "");
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "vehicle-class");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "vehicle-class", XmlHelper.isMAN_RentalVehicles_Vehicle_Class, "");
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "rental-start");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "rental-start", XmlHelper.isMAN_RentalVehicles_Rental_Start, XmlHelper.REG_TIMESTAMP);
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "rental-end");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "rental-end", XmlHelper.isMAN_RentalVehicles_Rental_End, XmlHelper.REG_TIMESTAMP);        

        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "rental-days");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "rental-days", XmlHelper.isMAN_RentalVehicles_Rental_Days, XmlHelper.REG_INTEGER); 
        */
        
        String ExtraNodeName = "extras";
        Element extraElement = XMLUtils.getElement(thisElement, ExtraNodeName);
        
        // MAIN EXTRA ELEMENT
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, ExtraNodeName, "Extras");

        // MAIN EXTRA DETAIL ELEMENT
        ArrayList<Element> extraElements = XMLUtils.getElements(doc, extraElement, "extra");
        
        for (Element ee : extraElements) {
            xmlParseResult = isSchemaNodeValidation(xmlParseResult, thisElement, "extra", XmlHelper.isMAN_RentalVehicles_Extras_Extra, "", "Extras:Extra");
        }
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            //TODO:
        }
        
        return xmlParseResult;
    }

    private static String getNodeValue(Element root, String nodeName){
        return XMLUtils.getElementValue(root, nodeName);
    }
    
    private static XMLParseResult isSchemaNodeValidation(
            XMLParseResult xmlParseResult, 
            Element root, 
            String nodeName, 
            Boolean isMandatory,
            String regExpression,
            String strPath){
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
            
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, nodeName, strPath);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            xmlParseResult = isSchemaValueValid(xmlParseResult, root, nodeName, isMandatory, regExpression, strPath);
        }
        
        return xmlParseResult;
    }
    
    // VALIDATE THE ELEMENT
    private static XMLParseResult isSchemaNodeExist(XMLParseResult xmlParseResult, Element root, String nodeName,
            String strPath){

        Boolean bFlag = true;
        String SchemaValidationRemark = xmlParseResult.getSchemaValidationRemark();
        
        Element thisElement = XMLUtils.getElement(root, nodeName);
        
        if (thisElement == null) {
            
            SchemaValidationRemark = SchemaValidationRemark + XmlHelper.contructureSchemaErrorMessage(strPath, nodeName);
            bFlag = false;   
        }
        
        if(!bFlag){
            xmlParseResult.setSchemaValidationRemark(SchemaValidationRemark);
            xmlParseResult.setIsSchemaValid(bFlag);
            xmlParseResult.setIsCurrentScheValid(bFlag);
        }

        return xmlParseResult;
    }
    
    private static XMLParseResult isSchemaNodeListExist(
            XMLParseResult xmlParseResult, 
            ArrayList<Element> thisElements, 
            String nodeName, 
            String strPath){
        
        String SchemaValidationRemark = xmlParseResult.getSchemaValidationRemark();
        
        if(thisElements.size()<=0){
            SchemaValidationRemark = SchemaValidationRemark + XmlHelper.contructureSchemaErrorMessage(strPath, nodeName);
            xmlParseResult.setSchemaValidationRemark(SchemaValidationRemark);
            xmlParseResult.setIsSchemaValid(false);
            xmlParseResult.setIsCurrentScheValid(false);
        }
        
        return xmlParseResult;
    }
            
    private static XMLParseResult isSchemaValueValid(
            XMLParseResult xmlParseResult, 
            Element root, 
            String nodeName, 
            Boolean isMandatory,
            String regExpression,
            String strPath){
        
        Boolean bFlag = true;
        
        String DataValidationRemark = xmlParseResult.getDataValidationRemark();
        
        Element thisElement = XMLUtils.getElement(root, nodeName);

        if (thisElement != null) {
            
            String thisElementValue = XMLUtils.getElementValue(root, nodeName);
            
            if (thisElementValue == null || thisElementValue.trim().length() == 0){
                if(isMandatory){
                    DataValidationRemark = DataValidationRemark + XmlHelper.contructureDataMandatoryErrorMessage(strPath, nodeName);
                    bFlag = false;
                }
            }else{
                if(!XmlHelper.isValidDataType(thisElementValue, regExpression)){
                    DataValidationRemark = DataValidationRemark +  XmlHelper.contructureIncorrectTypeErrorMessage(strPath, nodeName);
                    bFlag = false;
                }
            }
            
            if(!bFlag){
                xmlParseResult.setDataValidationRemark(DataValidationRemark);
                xmlParseResult.setIsDataValid(bFlag);  
                xmlParseResult.setIsCurrentDataValid(bFlag);
            }
        }
        
        return xmlParseResult;
    }    
}
