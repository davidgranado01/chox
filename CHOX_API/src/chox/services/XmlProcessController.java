package chox.services;

import java.io.File;
import org.w3c.dom.*;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 
import com.filesystemsoftware.utils.XMLUtils;
import com.filesystemsoftware.utils.Logger;
import java.math.BigDecimal;
import java.sql.Timestamp;
import chox.model.*;

public class XmlProcessController {

    public static void main(String[] args) {
    
        try {
            
            String sXMLPath1 = "C:/Project Workplace/Greefinch/choxida/CHOX_STAGE_1_SUBMISSION.xml";
            String sXMLPath2 = "C:/Project Workplace/Greefinch/choxida/CHOX_STAGE_2_SUBMISSION.xml";
            String sUpdateType = "A";
            Boolean isAllowPartialUpload = true;
            
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
                        xmlParseResult = xmlSchemaValidateProcess(xmlParseResult, doc, re, sUpdateType);
                        xmlParseResults.add(xmlParseResult);
                        
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
    
    public static ArrayList<XMLParseResult> XMLValidationProcess(Document doc, String sUpdateType, Boolean isAllowPartialUpload) {
    
        ArrayList<XMLParseResult> xmlParseResults = new ArrayList<XMLParseResult>();
        
        try {
            
            doc.getDocumentElement().normalize ();
            Element root = doc.getDocumentElement();
            
            if (root != null && root.getTagName().equals("chox")) {
            
                ArrayList<Element> rentalElements = XMLUtils.getElements(doc, root, "rental");

                int count = 0;
                for (Element re : rentalElements) {
                    
                    try {
                        count++;
                        XMLParseResult xmlParseResult = new XMLParseResult();
                        xmlParseResult = xmlSchemaValidateProcess(xmlParseResult, doc, re, sUpdateType);
                        xmlParseResults.add(xmlParseResult);
                        
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
        
        return xmlParseResults;
    }
    
    private static XMLParseResult xmlSchemaValidateProcess(
            XMLParseResult xmlParseResult, 
            Document doc,
            Element root,
            String sUploadType) throws Exception {
        
        Rental rental = new Rental();
        xmlParseResult.setRental(rental);
        
        // DO NOT CHANGE THE SEQUENCE
        xmlParseResult = RentalStatusSchemaValidation(xmlParseResult, root, doc);       // DONE
        xmlParseResult = RentalFirstContactSchemaValidation(xmlParseResult, root, doc); // DONE
        xmlParseResult = RentalSupplierSchemaValidation(xmlParseResult, root, doc);     // DONE
        
            System.out.println (" ** RentalStatusSchemaValidation:"+xmlParseResult.getRental().getRentalStatus());
            System.out.println (" ** RentalFirstContactSchemaValidation:"+xmlParseResult.getRental().getFirstContact());
            System.out.println (" ** RentalSupplierSchemaValidation:"+xmlParseResult.getRental().getSupplierReference());
            System.out.println (" ** RentalSupplierSchemaValidation:"+xmlParseResult.getRental().getSupplier().getName());
        
        xmlParseResult = RentalClaimSchemaValidation(xmlParseResult, root, doc);
        
            
        xmlParseResult = RentalDriversSchemaValidation(xmlParseResult, root, doc);
        xmlParseResult = RentalRepairSchemaValidation(xmlParseResult, root, doc);
        
        if(!sUploadType.equalsIgnoreCase("C")){
            xmlParseResult = RentalVehiclesSchemaValidation(xmlParseResult, root, doc);
            xmlParseResult = RentalInvoiceSchemaValidation(xmlParseResult, root, doc);
        }
        
            System.out.println (" ** getIsSchemaValid: " + xmlParseResult.getIsSchemaValid());
            System.out.println (" ** getSchemaValidationRemark: " + xmlParseResult.getSchemaValidationRemark());
            System.out.println (" ** getIsDataValid: " + xmlParseResult.getIsDataValid());
            System.out.println (" ** getDataValidationRemark: " + xmlParseResult.getDataValidationRemark());
            System.out.println ("********************************************");
        
        return xmlParseResult;
    }

    // VALIDATE RENTAL CONTACT SECTION 
    private static XMLParseResult RentalFirstContactSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element root,
            Document doc) throws Exception {
        
        String mainNodeName = "rental";
        String nodeName1 = "first-contact";
        
        String clidNodeLabelMain = XmlHelper.contructureErrorMessagePath(mainNodeName, "");
 
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        // CHECK MAIN ELEMENT
        xmlParseResult = xmlNodeValidation(xmlParseResult, root, nodeName1, XmlHelper.isMAN_First_Contact, XmlHelper.REG_TIMESTAMP ,clidNodeLabelMain);

        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            
            String strFirstContactDateTime = XmlHelper.getNodeValue(root, nodeName1);
            Timestamp tFirstContactDateTime = XmlHelper.parseDate(strFirstContactDateTime);
            
            Rental rental = xmlParseResult.getRental();
            rental.setFirstContact(tFirstContactDateTime);
            xmlParseResult.setRental(rental);
        }
        return xmlParseResult;
    } 
    
    // VALIDATE SUPPLIER DETAIL SECTION
    private static XMLParseResult RentalSupplierSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element mainElement,
            Document doc) throws Exception {

        String parentNodeName = "rental";
        String mainNodeName = "supplier";
        String nodeName1 = "supplier-name";
        String nodeName2 = "supplier-reference";
        
        String clidNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, "");
        String clidNodeLabel1 = XmlHelper.contructureErrorMessagePath(clidNodeLabelMain, mainNodeName);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, clidNodeLabelMain);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            
            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);

            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, nodeName1, XmlHelper.isMAN_Supplier_Name, "", clidNodeLabel1);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, nodeName2, XmlHelper.isMAN_Supplier_Reference, "", clidNodeLabel1);
            
            if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
                
                String strSupplierName = XmlHelper.getNodeValue(thisElement, nodeName1);
                String strSupplierReference = XmlHelper.getNodeValue(thisElement, nodeName2);
                
                Rental rental = new Rental();
                
                if(isRentalExist(strSupplierReference)){
                    // rental = ;
                    
                    // RETRIEVE THE DATA SET TO RENTAL OBJECT BEFORE AND 
                    // rental.setFirstContact(xmlParseResult.getRental().getFirstContact());
                    // rental.setRentalStatus(xmlParseResult.getRental().getRentalStatus());
                    
                }else{
                    
                    // SET SUPPLIER OBJECT
                    Supplier supplier = new Supplier();
                    supplier.setName(strSupplierName);
                    
                    rental = xmlParseResult.getRental();
                    rental.setSupplier(supplier);
                    rental.setSupplierReference(strSupplierReference);
                }
                
                xmlParseResult.setRental(rental);
            }
        }
        
        return xmlParseResult;
    }    

    // VALIDATE RENTAL STATUS SECTION 
    private static XMLParseResult RentalStatusSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element root,
            Document doc) throws Exception {
        
        String mainNodeName = "rental";
        String nodeName1 = "rental-status";
        
        String clidNodeLabelMain = XmlHelper.contructureErrorMessagePath(mainNodeName, "");
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        // CHECK MAIN ELEMENT
        xmlParseResult = xmlNodeValidation(xmlParseResult, root, nodeName1, XmlHelper.isMAN_Status, XmlHelper.REG_WORD, clidNodeLabelMain);
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            
            String rentalStatus = XmlHelper.getNodeValue(root, nodeName1);
            Rental rental = xmlParseResult.getRental();
            rental.setRentalStatus(rentalStatus);
            xmlParseResult.setRental(rental);
        }
        
        return xmlParseResult;        
    }
        
    // VALIDATE DRIVER SECTION 
    private static XMLParseResult RentalDriversSchemaValidation(
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
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, clidNodeLabelMain);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            
            // DRIVER
            ArrayList<Element> driverElements = XMLUtils.getElements(doc, thisElement, nodeName1);
            
            xmlParseResult.setIsCurrentScheValid(true);
            xmlParseResult = xmlSchemaNodeListValidation(xmlParseResult, driverElements, nodeName1, clidNodeLabel1);
            
            if(xmlParseResult.getIsCurrentScheValid()){
                for (Element ee : driverElements) {

                    xmlParseResult.setIsCurrentDataValid(true);
                    xmlParseResult.setIsCurrentScheValid(true);

                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "title", XmlHelper.isMAN_Driver_Title, "", clidNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "firstnames", XmlHelper.isMAN_Driver_Firstnames, "", clidNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "lastname", XmlHelper.isMAN_Driver_Lastname, "", clidNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "address1", XmlHelper.isMAN_Driver_Address1, "", clidNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "address2", XmlHelper.isMAN_Driver_Address2, "", clidNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "address3", XmlHelper.isMAN_Driver_Address3, "", clidNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "address4", XmlHelper.isMAN_Driver_Address4, "", clidNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "address5", XmlHelper.isMAN_Driver_Address5, "", clidNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "postcode", XmlHelper.isMAN_Driver_Postcode, "", clidNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "telephone-day", XmlHelper.isMAN_Driver_Telephone_day, XmlHelper.REG_PHONE, clidNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "telephone-evening", XmlHelper.isMAN_Driver_Telephone_Evening, XmlHelper.REG_PHONE, clidNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "email", XmlHelper.isMAN_Driver_Email, XmlHelper.REG_EMAIL, clidNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "primary-driver", XmlHelper.isMAN_Driver_Primary_Driver, "", clidNodeLabel2);
                }
            }                
        }
        
        return xmlParseResult;
    }  
    
    // VALIDATE CLAIM SECTION 
    private static XMLParseResult RentalClaimSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element root,
            Document doc) throws Exception {
        
        String parentNodeName = "rental";
        String nodeName = "claim";
        
        String clidNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, nodeName);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, root, nodeName, clidNodeLabelMain);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            Element claimNodeElement = XMLUtils.getElement(root, nodeName);
            xmlParseResult = ClaimDetail_CustomerSchemaValidation(xmlParseResult, claimNodeElement, doc, clidNodeLabelMain);
            xmlParseResult = ClaimDetail_ReplacementVehicleSchemaValidation(xmlParseResult, claimNodeElement, doc, clidNodeLabelMain);
            xmlParseResult = ClaimDetail_ThirdPartySchemaValidation(xmlParseResult, claimNodeElement, doc, clidNodeLabelMain);
            xmlParseResult = ClaimDetail_IncidentSchemaValidation(xmlParseResult, claimNodeElement, doc, clidNodeLabelMain);
        }
        
        return xmlParseResult;
    }  
    
    private static XMLParseResult ClaimDetail_CustomerSchemaValidation(
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
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, clidNodeLabelMain);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, clidNodeLabel1);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName2, clidNodeLabel2);
        
        if(xmlParseResult.getIsCurrentScheValid()){

            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);

            // INSURER
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "name", XmlHelper.isMAN_Claim_Customer_Insurer_name, "", clidNodeLabel1);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "policy-number", XmlHelper.isMAN_Claim_Customer_Insurer_policyNumber, "", clidNodeLabel1);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "claim-reference", XmlHelper.isMAN_Claim_Customer_Insurer_claimReference, "", clidNodeLabel1);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "comprehensive", XmlHelper.isMAN_Claim_Customer_Insurer_comprehensive, "", clidNodeLabel1);

            // VEHICLE
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-registration", XmlHelper.isMAN_Claim_Customer_Vehicle_Registration, "", clidNodeLabel2);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-manufacturer", XmlHelper.isMAN_Claim_Customer_Vehicle_Manufacturer, "", clidNodeLabel2);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-model", XmlHelper.isMAN_Claim_Customer_Vehicle_Model, "", clidNodeLabel2);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-class", XmlHelper.isMAN_Claim_Customer_Vehicle_Class, "", clidNodeLabel2);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "usable", XmlHelper.isMAN_Claim_Customer_Vehicle_Usable, "", clidNodeLabel2);            

        }
        
        return xmlParseResult;
    }
    
    private static XMLParseResult ClaimDetail_ReplacementVehicleSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element mainElement,
            Document doc,
            String parentNodeName) throws Exception {
        
        String mainNodeName = "replacement-vehicle";
        String nodeName1 = "vehicle-class";
        
        String clidNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, mainNodeName);
        String clidNodeLabel1 = XmlHelper.contructureErrorMessagePath(clidNodeLabelMain, nodeName1);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, clidNodeLabelMain);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, nodeName1, XmlHelper.isMAN_Claim_ReplacementVehicle_VehicleClass, "", clidNodeLabel1);
        }
        return xmlParseResult;
    }
    
    private static XMLParseResult ClaimDetail_ThirdPartySchemaValidation(
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
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, clidNodeLabelMain);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, clidNodeLabel1);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName2, clidNodeLabel2);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName3, clidNodeLabel3);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            
            // INSURER
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "name", XmlHelper.isMAN_Claim_ThirdParty_Insurer_Name, "", clidNodeLabel1);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "policy-number", XmlHelper.isMAN_Claim_ThirdParty_Insurer_PolicyNumber, "", clidNodeLabel1);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "claim-reference", XmlHelper.isMAN_Claim_ThirdParty_Insurer_ClaimReference, "", clidNodeLabel1);
            
            // VEHICLE
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-registration", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_Registration, "", clidNodeLabel2);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-manufacturer", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_manufacturer, "", clidNodeLabel2);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-model", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_model, "", clidNodeLabel2);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-class", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_class, "", clidNodeLabel2);
            
            // DRIVER
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "title", XmlHelper.isMAN_Claim_ThirdParty_Driver_Title, "", clidNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "firstnames", XmlHelper.isMAN_Claim_ThirdParty_Driver_Firstnames, "", clidNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "lastname", XmlHelper.isMAN_Claim_ThirdParty_Driver_Lastname, "", clidNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "address1", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address1, "", clidNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "address2", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address2, "", clidNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "address3", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address3, "", clidNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "address4", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address4, "", clidNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "address5", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address5, "", clidNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "postcode", XmlHelper.isMAN_Claim_ThirdParty_Driver_Postcode, "", clidNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "telephone-day", XmlHelper.isMAN_Claim_ThirdParty_Driver_TelephoneDay, "", clidNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "telephone-evening", XmlHelper.isMAN_Claim_ThirdParty_Driver_TelephoneEvening, "", clidNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "email", XmlHelper.isMAN_Claim_ThirdParty_Driver_Email, "", clidNodeLabel3);
        }        
        return xmlParseResult;
    }    
    
    private static XMLParseResult ClaimDetail_IncidentSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element mainElement,
            Document doc,
            String parentNodeName) throws Exception {

        String mainNodeName = "incident";
        
        String clidNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, mainNodeName);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, clidNodeLabelMain);

        if(xmlParseResult.getIsCurrentScheValid()){
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "date", XmlHelper.isMAN_Claim_Incident_Date, XmlHelper.REG_TIMESTAMP, clidNodeLabelMain);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "location", XmlHelper.isMAN_Claim_Incident_Location, "", clidNodeLabelMain);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "police-involved", XmlHelper.isMAN_Claim_Incident_PoliceInvolved, "", clidNodeLabelMain);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "description", XmlHelper.isMAN_Claim_Incident_Description, "", clidNodeLabelMain);
        }
        return xmlParseResult;
    }   
    
    // VALIDATE CLAIM SECTION 
    private static XMLParseResult RentalRepairSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element root,
            Document doc) throws Exception {
        
        String nodeName = "repair";
        String subNodeName = "engineer-report";
        String strPath = "Repair:Engineer Report";
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, root, nodeName, "Repair");
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, root, subNodeName, "Engineer Report");
        
        if(xmlParseResult.getIsCurrentScheValid()){

            ArrayList<Element> repairElements = XMLUtils.getElements(doc, root, subNodeName);
            
            for (Element ee : repairElements) {
            
                xmlParseResult.setIsCurrentDataValid(true);
                xmlParseResult.setIsCurrentScheValid(true);
                
                xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "labour-amount", XmlHelper.isMAN_Driver_Title, XmlHelper.REG_BIGDECIMAL, strPath);
                xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "total-amount", XmlHelper.isMAN_Driver_Firstnames, XmlHelper.REG_BIGDECIMAL, strPath);
                xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "days", XmlHelper.isMAN_Driver_Lastname, XmlHelper.REG_INTEGER, strPath);
            }
        }
        return xmlParseResult;
    }  
    
    // VALIDATE INVOICE SECTION 
    private static XMLParseResult RentalInvoiceSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element mainElement, 
            Document doc) throws Exception {
        
        String parentNodeName = "rental";
        String nodeName1 = "invoice";
        
        Boolean bFlag = false;
        
        String clidNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, "");
        String clidNodeLabel1 = XmlHelper.contructureErrorMessagePath(clidNodeLabelMain, nodeName1);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, clidNodeLabelMain);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            Element thisElement = XMLUtils.getElement(mainElement, nodeName1);
            xmlParseResult = RentalInvoiceDetailSchemaValidation(xmlParseResult, thisElement, doc, clidNodeLabel1);
        }
        return xmlParseResult;
    }
    
    // VALIDATE INVOICE DETAIL SECTION
    private static XMLParseResult RentalInvoiceDetailSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element thisElement, 
            Document doc,
            String parentNodeName) throws Exception {
        
        String subNodeName0 = "vehicles";
        String subNodeName1 = "extras";
        String subNodeName2 = "repair";
        String subNodeName3 = "storage-recovery";
        String subNodeName4 = "engineer-fee";
        String subNodeName5 = "claim-handling-fee";
                
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Net, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Vat, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Gross, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, thisElement, subNodeName0, parentNodeName);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, thisElement, subNodeName1, parentNodeName);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, thisElement, subNodeName2, parentNodeName);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, thisElement, subNodeName3, parentNodeName);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, thisElement, subNodeName4, parentNodeName);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, thisElement, subNodeName5, parentNodeName);
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
 
            String clidNodeLabel0 = XmlHelper.contructureErrorMessagePath(parentNodeName, subNodeName0);
            String clidNodeLabel1 = XmlHelper.contructureErrorMessagePath(parentNodeName, subNodeName1);
            String clidNodeLabel2 = XmlHelper.contructureErrorMessagePath(parentNodeName, subNodeName2);
            String clidNodeLabel3 = XmlHelper.contructureErrorMessagePath(parentNodeName, subNodeName3);
            String clidNodeLabel4 = XmlHelper.contructureErrorMessagePath(parentNodeName, subNodeName4);
            String clidNodeLabel5 = XmlHelper.contructureErrorMessagePath(parentNodeName, subNodeName5);
            
            /*
            BigDecimal bNet = new BigDecimal(XmlHelper.getNodeValue(thisElement, "net"));
            BigDecimal bVat = new BigDecimal(XmlHelper.getNodeValue(thisElement, "vat"));
            BigDecimal bGross = new BigDecimal(XmlHelper.getNodeValue(thisElement, "gross"));
            
            // SET SUPPLIER INFORMATION
            Invoice invoice = new Invoice();
            invoice.setNet(bNet);
            invoice.setGross(bGross);
            invoice.setVAT(bVat);
            
            // SET RENTAL INFORMATION
            Rental rental = xmlParseResult.getRental();
            rental.setInvoice(invoice);
            */
            
            // VEHICLES
            xmlParseResult = InvoiceDetail_VehiclesValidSchemaValidation(xmlParseResult, thisElement, clidNodeLabel0, subNodeName0);
            
            // EXTRAS
            xmlParseResult = InvoiceDetail_ExtrasValidSchemaValidation(xmlParseResult, thisElement, doc, clidNodeLabel1, subNodeName1);
            
            // REPAIR
            xmlParseResult = InvoiceDetail_RepairValidSchemaValidation(xmlParseResult, thisElement, clidNodeLabel2, subNodeName2);
            
            // storage-recovery
            xmlParseResult = InvoiceDetail_StorageRecoveryValidSchemaValidation(xmlParseResult, thisElement, clidNodeLabel3, subNodeName3);
            
            // engineer-fee
            xmlParseResult = InvoiceDetail_EngineerFeeValidSchemaValidation(xmlParseResult, thisElement, clidNodeLabel4, subNodeName4);
            
            // claim-handling-fee
            //xmlParseResult = InvoiceDetail_ClaimHandlingFeeValidSchemaValidation(xmlParseResult, thisElement, clidNodeLabel5, subNodeName5);

        }
        
        return xmlParseResult;
    } 
    
    // VALIDATE INVOICE DETAIL - Vehicles SECTION
    private static XMLParseResult InvoiceDetail_VehiclesValidSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element mainElement, 
            String parentNodeName, 
            String thisNodeName){
        
        Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Vehicles_Net, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Vehicles_Vat, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Vehicles_Gross, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            BigDecimal bNet = new BigDecimal(XmlHelper.getNodeValue(thisElement, "net"));
            BigDecimal bVat = new BigDecimal(XmlHelper.getNodeValue(thisElement, "vat"));
            BigDecimal bGross = new BigDecimal(XmlHelper.getNodeValue(thisElement, "gross"));
        }        
        return xmlParseResult;
    }

    // VALIDATE INVOICE DETAIL - repair SECTION
    private static XMLParseResult InvoiceDetail_RepairValidSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element mainElement, 
            String parentNodeName, 
            String thisNodeName){
        
        Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Repair_Net, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Repair_Vat, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Repair_Gross, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){

            // BigDecimal bNet = new BigDecimal(XmlHelper.getNodeValue(thisElement, "net"));
            // BigDecimal bVat = new BigDecimal(XmlHelper.getNodeValue(thisElement, "vat"));
            // BigDecimal bGross = new BigDecimal(XmlHelper.getNodeValue(thisElement, "gross"));
        }
        
        return xmlParseResult;
    }
    
    // VALIDATE INVOICE DETAIL - Claim Handling fee SECTION
    private static XMLParseResult InvoiceDetail_ClaimHandlingFeeValidSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element mainElement, 
            String parentNodeName, 
            String thisNodeName){
        
        Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Claim_Handling_Fee_Net, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Claim_Handling_Fee_Vat, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Claim_Handling_Fee_Gross, XmlHelper.REG_BIGDECIMAL, parentNodeName);
                
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            // BigDecimal bNet = new BigDecimal(XmlHelper.getNodeValue(thisElement, "net"));
            // BigDecimal bVat = new BigDecimal(XmlHelper.getNodeValue(thisElement, "vat"));
            // BigDecimal bGross = new BigDecimal(XmlHelper.getNodeValue(thisElement, "gross"));
        }
        
        return xmlParseResult;
    }

    // VALIDATE INVOICE DETAIL - Engineer Fee SECTION
    private static XMLParseResult InvoiceDetail_EngineerFeeValidSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element mainElement, 
            String parentNodeName, 
            String thisNodeName){
        
        Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Engineer_Fee_Net, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Engineer_Fee_Vat, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Engineer_Fee_Gross, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            // BigDecimal bNet = new BigDecimal(XmlHelper.getNodeValue(thisElement, "net"));
            // BigDecimal bVat = new BigDecimal(XmlHelper.getNodeValue(thisElement, "vat"));
            // BigDecimal bGross = new BigDecimal(XmlHelper.getNodeValue(thisElement, "gross"));
        }
        
        return xmlParseResult;
    }

    // VALIDATE INVOICE DETAIL - Storage Recovery SECTION
    private static XMLParseResult InvoiceDetail_StorageRecoveryValidSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element mainElement, 
            String parentNodeName, 
            String thisNodeName){

        Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Storage_Recovery_Net, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Storage_Recovery_Vat, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Storage_Recovery_Gross, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            // BigDecimal bNet = new BigDecimal(XmlHelper.getNodeValue(thisElement, "net"));
            // BigDecimal bVat = new BigDecimal(XmlHelper.getNodeValue(thisElement, "vat"));
            // BigDecimal bGross = new BigDecimal(XmlHelper.getNodeValue(thisElement, "gross"));
        }
        
        return xmlParseResult;
    }
    
    // VALIDATE INVOICE DETAIL - Storage Recovery SECTION
    private static XMLParseResult InvoiceDetail_ExtrasValidSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element mainElement, 
            Document doc,
            String parentNodeName, 
            String thisNodeName) throws Exception {
        
        Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);

        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        String subNodeName = "extra";
        
        ArrayList<Element> extraElements = XMLUtils.getElements(doc, thisElement, subNodeName);
        xmlParseResult = xmlSchemaNodeListValidation(xmlParseResult, extraElements, subNodeName, parentNodeName);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            
            for (Element ee : extraElements) {

                xmlParseResult.setIsCurrentDataValid(true);
                xmlParseResult.setIsCurrentScheValid(true);

                xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "name", XmlHelper.isMAN_Invoice_Extras_Name, "", parentNodeName);
                xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "quantity", XmlHelper.isMAN_Invoice_Extras_Quantity, XmlHelper.REG_INTEGER, parentNodeName);
                xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "item-cost", XmlHelper.isMAN_Invoice_Extras_Item_Cost, XmlHelper.REG_BIGDECIMAL, parentNodeName);

                if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){

                }
            }
        }
        return xmlParseResult;
    }
    
    // VALIDATE INVOICE SECTION 
    private static XMLParseResult RentalVehiclesSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element root,
            Document doc) throws Exception {
        
        String nodeName = "rental-vehicles";
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        // CHECK MAIN ELEMENT
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, root, nodeName, "Rental Vehicles");

        if(xmlParseResult.getIsCurrentScheValid()){
        
               Element thisElement = XMLUtils.getElement(root, nodeName);
               ArrayList<Element> rentalVehicleElements = XMLUtils.getElements(doc, thisElement, "rental-vehicle");
               
               for (Element ee : rentalVehicleElements) {
                    xmlParseResult = RentalVehiclesDetailSchemaValidation(xmlParseResult, ee, doc);
               }
        }
        
        return xmlParseResult;
    } 
    
    private static XMLParseResult RentalVehiclesDetailSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element thisElement,
            Document doc) throws Exception {
        String strPath = "Rental Vehicle";
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);

        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-registration", XmlHelper.isMAN_RentalVehicles_Vehicle_Registration, "", strPath);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-manufacturer", XmlHelper.isMAN_RentalVehicles_Vehicle_Manufacturer, "", strPath);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-model", XmlHelper.isMAN_RentalVehicles_Vehicle_Model, "", strPath);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-class", XmlHelper.isMAN_RentalVehicles_Vehicle_Class, "", strPath);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "rental-start", XmlHelper.isMAN_RentalVehicles_Rental_Start, XmlHelper.REG_TIMESTAMP, strPath);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "rental-end", XmlHelper.isMAN_RentalVehicles_Rental_End, XmlHelper.REG_TIMESTAMP, strPath);
        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "rental-days", XmlHelper.isMAN_RentalVehicles_Rental_Days, XmlHelper.REG_INTEGER, strPath);
                
        String ExtraNodeName = "extras";
        Element extraElement = XMLUtils.getElement(thisElement, ExtraNodeName);
        
        // MAIN EXTRA ELEMENT
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, thisElement, ExtraNodeName, "Extras");

        // MAIN EXTRA DETAIL ELEMENT
        ArrayList<Element> extraElements = XMLUtils.getElements(doc, extraElement, "extra");
        
        for (Element ee : extraElements) {
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "extra", XmlHelper.isMAN_RentalVehicles_Extras_Extra, "", "Extras:Extra");
        }
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            //TODO:
        }
        
        return xmlParseResult;
    }


    
    private static XMLParseResult xmlNodeValidation(
            XMLParseResult xmlParseResult, 
            Element root, 
            String nodeName, 
            Boolean isMandatory,
            String regExpression,
            String strPath){
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
            
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, root, nodeName, strPath);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            xmlParseResult = xmlSchemaValueValidation(xmlParseResult, root, nodeName, isMandatory, regExpression, strPath);
        }
        
        return xmlParseResult;
    }
    
    // VALIDATE THE ELEMENT
    private static XMLParseResult xmlSchemaNodeValidation(XMLParseResult xmlParseResult, Element root, String nodeName,
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
    
    private static XMLParseResult xmlSchemaNodeListValidation(
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
            
    private static XMLParseResult xmlSchemaValueValidation(
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
    
    private static Boolean isRentalExist(String supplierReferenceNumber){
        return false;
    }
    
}
