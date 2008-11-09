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
        xmlParseResult = RentalDriversSchemaValidation(xmlParseResult, root, doc);      // DONE
        
        if(!sUploadType.equalsIgnoreCase("C")){
            xmlParseResult = RentalRepairSchemaValidation(xmlParseResult, root, doc);
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
        
        String childNodeLabelMain = XmlHelper.contructureErrorMessagePath(mainNodeName, "");
 
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        // CHECK MAIN ELEMENT
        xmlParseResult = xmlNodeValidation(xmlParseResult, root, nodeName1, XmlHelper.isMAN_First_Contact, XmlHelper.REG_TIMESTAMP ,childNodeLabelMain);

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
        
        String childNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, "");
        String childNodeLabel1 = XmlHelper.contructureErrorMessagePath(childNodeLabelMain, mainNodeName);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, childNodeLabelMain);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            
            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);

            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, nodeName1, XmlHelper.isMAN_Supplier_Name, "", childNodeLabel1);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, nodeName2, XmlHelper.isMAN_Supplier_Reference, "", childNodeLabel1);
            
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
        
        String childNodeLabelMain = XmlHelper.contructureErrorMessagePath(mainNodeName, "");
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        // CHECK MAIN ELEMENT
        xmlParseResult = xmlNodeValidation(xmlParseResult, root, nodeName1, XmlHelper.isMAN_Status, XmlHelper.REG_WORD, childNodeLabelMain);
        
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
        
        String childNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, "");
        String childNodeLabel1 = XmlHelper.contructureErrorMessagePath(childNodeLabelMain, mainNodeName);
        String childNodeLabel2 = XmlHelper.contructureErrorMessagePath(childNodeLabel1, nodeName1);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, childNodeLabelMain);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            
            // DRIVER
            ArrayList<Element> driverElements = XMLUtils.getElements(doc, thisElement, nodeName1);
            
            xmlParseResult.setIsCurrentScheValid(true);
            xmlParseResult = xmlSchemaNodeListValidation(xmlParseResult, driverElements, nodeName1, childNodeLabel1);
            
            if(xmlParseResult.getIsCurrentScheValid()){
                
                ArrayList<Driver> drivers = new ArrayList<Driver>();
                
                for (Element ee : driverElements) {

                    xmlParseResult.setIsCurrentDataValid(true);
                    xmlParseResult.setIsCurrentScheValid(true);

                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "title", XmlHelper.isMAN_Driver_Title, "", childNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "firstnames", XmlHelper.isMAN_Driver_Firstnames, "", childNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "lastname", XmlHelper.isMAN_Driver_Lastname, "", childNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "address1", XmlHelper.isMAN_Driver_Address1, "", childNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "address2", XmlHelper.isMAN_Driver_Address2, "", childNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "address3", XmlHelper.isMAN_Driver_Address3, "", childNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "address4", XmlHelper.isMAN_Driver_Address4, "", childNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "address5", XmlHelper.isMAN_Driver_Address5, "", childNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "postcode", XmlHelper.isMAN_Driver_Postcode, "", childNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "telephone-day", XmlHelper.isMAN_Driver_Telephone_day, XmlHelper.REG_PHONE, childNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "telephone-evening", XmlHelper.isMAN_Driver_Telephone_Evening, XmlHelper.REG_PHONE, childNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "email", XmlHelper.isMAN_Driver_Email, XmlHelper.REG_EMAIL, childNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "primary-driver", XmlHelper.isMAN_Driver_Primary_Driver, "", childNodeLabel2);
                    
                    if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
                        
                        Driver d = new Driver();
                        d.setRentalID(xmlParseResult.getRental().getID());
                        d.setTitle(XmlHelper.getNodeValue(ee, "title"));
                        d.setFirstnames(XmlHelper.getNodeValue(ee, "firstnames"));
                        d.setLastname(XmlHelper.getNodeValue(ee, "lastname"));
                        d.setAddress1(XmlHelper.getNodeValue(ee, "address1"));
                        d.setAddress2(XmlHelper.getNodeValue(ee, "address2"));
                        d.setAddress3(XmlHelper.getNodeValue(ee, "address3"));
                        d.setAddress4(XmlHelper.getNodeValue(ee, "address4"));
                        d.setAddress5(XmlHelper.getNodeValue(ee, "address5"));
                        d.setPostcode(XmlHelper.getNodeValue(ee, "postcode"));
                        d.setTelephoneDay(XmlHelper.getNodeValue(ee, "telephone-day"));
                        d.setTelephoneEvening(XmlHelper.getNodeValue(ee, "telephone-evening"));
                        d.setEmail(XmlHelper.getNodeValue(ee, "email"));

                        String isPrimary = XmlHelper.getNodeValue(ee, "primary-driver");
                        if (isPrimary == null || isPrimary.trim().length() == 0) {
                            isPrimary = "n";
                        }
                        isPrimary = new String(isPrimary.toLowerCase().substring(0, 1));
                        d.setPrimaryDriver(isPrimary);
                        
                        drivers.add(d);
                    }
                }
                System.out.println ("$$$ DRIVER LIST : " + drivers.size());
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
        
        String childNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, nodeName);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, root, nodeName, childNodeLabelMain);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            Element claimNodeElement = XMLUtils.getElement(root, nodeName);
            xmlParseResult = ClaimDetail_CustomerSchemaValidation(xmlParseResult, claimNodeElement, doc, childNodeLabelMain);
            xmlParseResult = ClaimDetail_ReplacementVehicleSchemaValidation(xmlParseResult, claimNodeElement, doc, childNodeLabelMain);
            xmlParseResult = ClaimDetail_ThirdPartySchemaValidation(xmlParseResult, claimNodeElement, doc, childNodeLabelMain);
            xmlParseResult = ClaimDetail_IncidentSchemaValidation(xmlParseResult, claimNodeElement, doc, childNodeLabelMain);
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
        
        String childNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, mainNodeName);
        String childNodeLabel1 = XmlHelper.contructureErrorMessagePath(childNodeLabelMain, nodeName1);
        String childNodeLabel2 = XmlHelper.contructureErrorMessagePath(childNodeLabelMain, nodeName2);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, childNodeLabelMain);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, childNodeLabel1);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName2, childNodeLabel2);
        
        if(xmlParseResult.getIsCurrentScheValid()){

            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);

            // INSURER
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "name", XmlHelper.isMAN_Claim_Customer_Insurer_name, "", childNodeLabel1);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "policy-number", XmlHelper.isMAN_Claim_Customer_Insurer_policyNumber, "", childNodeLabel1);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "claim-reference", XmlHelper.isMAN_Claim_Customer_Insurer_claimReference, "", childNodeLabel1);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "comprehensive", XmlHelper.isMAN_Claim_Customer_Insurer_comprehensive, "", childNodeLabel1);

            // VEHICLE
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-registration", XmlHelper.isMAN_Claim_Customer_Vehicle_Registration, "", childNodeLabel2);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-manufacturer", XmlHelper.isMAN_Claim_Customer_Vehicle_Manufacturer, "", childNodeLabel2);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-model", XmlHelper.isMAN_Claim_Customer_Vehicle_Model, "", childNodeLabel2);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-class", XmlHelper.isMAN_Claim_Customer_Vehicle_Class, "", childNodeLabel2);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "usable", XmlHelper.isMAN_Claim_Customer_Vehicle_Usable, "", childNodeLabel2);            

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
        
        String childNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, mainNodeName);
        String childNodeLabel1 = XmlHelper.contructureErrorMessagePath(childNodeLabelMain, nodeName1);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, childNodeLabelMain);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, nodeName1, XmlHelper.isMAN_Claim_ReplacementVehicle_VehicleClass, "", childNodeLabel1);
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
        
        String childNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, mainNodeName);
        String childNodeLabel1 = XmlHelper.contructureErrorMessagePath(childNodeLabelMain, nodeName1);
        String childNodeLabel2 = XmlHelper.contructureErrorMessagePath(childNodeLabelMain, nodeName2);
        String childNodeLabel3 = XmlHelper.contructureErrorMessagePath(childNodeLabelMain, nodeName3);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, childNodeLabelMain);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, childNodeLabel1);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName2, childNodeLabel2);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName3, childNodeLabel3);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            
            // INSURER
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "name", XmlHelper.isMAN_Claim_ThirdParty_Insurer_Name, "", childNodeLabel1);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "policy-number", XmlHelper.isMAN_Claim_ThirdParty_Insurer_PolicyNumber, "", childNodeLabel1);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "claim-reference", XmlHelper.isMAN_Claim_ThirdParty_Insurer_ClaimReference, "", childNodeLabel1);
            
            // VEHICLE
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-registration", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_Registration, "", childNodeLabel2);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-manufacturer", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_manufacturer, "", childNodeLabel2);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-model", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_model, "", childNodeLabel2);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "vehicle-class", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_class, "", childNodeLabel2);
            
            // DRIVER
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "title", XmlHelper.isMAN_Claim_ThirdParty_Driver_Title, "", childNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "firstnames", XmlHelper.isMAN_Claim_ThirdParty_Driver_Firstnames, "", childNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "lastname", XmlHelper.isMAN_Claim_ThirdParty_Driver_Lastname, "", childNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "address1", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address1, "", childNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "address2", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address2, "", childNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "address3", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address3, "", childNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "address4", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address4, "", childNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "address5", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address5, "", childNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "postcode", XmlHelper.isMAN_Claim_ThirdParty_Driver_Postcode, "", childNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "telephone-day", XmlHelper.isMAN_Claim_ThirdParty_Driver_TelephoneDay, "", childNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "telephone-evening", XmlHelper.isMAN_Claim_ThirdParty_Driver_TelephoneEvening, "", childNodeLabel3);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "email", XmlHelper.isMAN_Claim_ThirdParty_Driver_Email, "", childNodeLabel3);
        }        
        return xmlParseResult;
    }    
    
    private static XMLParseResult ClaimDetail_IncidentSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element mainElement,
            Document doc,
            String parentNodeName) throws Exception {

        String mainNodeName = "incident";
        
        String childNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, mainNodeName);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, childNodeLabelMain);

        if(xmlParseResult.getIsCurrentScheValid()){
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "date", XmlHelper.isMAN_Claim_Incident_Date, XmlHelper.REG_TIMESTAMP, childNodeLabelMain);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "location", XmlHelper.isMAN_Claim_Incident_Location, "", childNodeLabelMain);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "police-involved", XmlHelper.isMAN_Claim_Incident_PoliceInvolved, "", childNodeLabelMain);
            xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, "description", XmlHelper.isMAN_Claim_Incident_Description, "", childNodeLabelMain);
        }
        return xmlParseResult;
    }   
    
    // VALIDATE REPAIR SECTION 
    private static XMLParseResult RentalRepairSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element mainElement,
            Document doc) throws Exception {
        
        String parentNodeName = "rental";
        String nodeName1 = "repair";
        String nodeName2 = "engineer-report";
        
        String childNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, "");
        String childNodeLabel1 = XmlHelper.contructureErrorMessagePath(childNodeLabelMain, nodeName1);
        String childNodeLabel2 = XmlHelper.contructureErrorMessagePath(childNodeLabel1, nodeName2);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, childNodeLabelMain);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName2, childNodeLabel1);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            
            ArrayList<Element> engineerReportElements = XMLUtils.getElements(doc, mainElement, nodeName2);
            xmlParseResult = xmlSchemaNodeListValidation(xmlParseResult, engineerReportElements, nodeName2, childNodeLabel1);
            
            if(xmlParseResult.getIsCurrentScheValid()){
                
                ArrayList<EngineerReport> engineerReports = new ArrayList<EngineerReport>();
                
                for (Element ee : engineerReportElements) {

                    xmlParseResult.setIsCurrentDataValid(true);
                    xmlParseResult.setIsCurrentScheValid(true);

                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "labour-amount", XmlHelper.isMAN_Driver_Title, XmlHelper.REG_BIGDECIMAL, childNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "total-amount", XmlHelper.isMAN_Driver_Firstnames, XmlHelper.REG_BIGDECIMAL, childNodeLabel2);
                    xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "days", XmlHelper.isMAN_Driver_Lastname, XmlHelper.REG_INTEGER, childNodeLabel2);                
                    
                    if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
                        
                        EngineerReport engineerReport = new EngineerReport();
                        // engineerReport.setClaimID(xmlParseResult.getID());
                        engineerReport.setDays(new BigDecimal(XmlHelper.getNodeValue(ee, "days")));
                        engineerReport.setLabourAmount(new BigDecimal(XmlHelper.getNodeValue(ee, "labour-amount")));
                        engineerReport.setTotalAmount(new BigDecimal(XmlHelper.getNodeValue(ee, "total-amount")));
                        
                        /*
                        engineerReport.setName();
                        engineerReport.setAddress1(address1);
                        engineerReport.setAddress2(address2);
                        engineerReport.setAddress3(address3);
                        engineerReport.setAddress4(address4);
                        engineerReport.setAddress5(address5);
                        engineerReport.setPostcode(postcode);
                        engineerReport.setTelephone(telephone);
                        engineerReport.setEmail(email);
                        engineerReport.setUsable(usable);
                        */
                        
                        engineerReports.add(engineerReport);
                    }
                }
                System.out.println ("$$$ ENGINEER REPORT : " + engineerReports.size());
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
        
        String childNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, "");
        String childNodeLabel1 = XmlHelper.contructureErrorMessagePath(childNodeLabelMain, nodeName1);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, childNodeLabelMain);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            Element thisElement = XMLUtils.getElement(mainElement, nodeName1);
            xmlParseResult = RentalInvoiceDetailSchemaValidation(xmlParseResult, thisElement, doc, childNodeLabel1);
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
 
            String childNodeLabel0 = XmlHelper.contructureErrorMessagePath(parentNodeName, subNodeName0);
            String childNodeLabel1 = XmlHelper.contructureErrorMessagePath(parentNodeName, subNodeName1);
            String childNodeLabel2 = XmlHelper.contructureErrorMessagePath(parentNodeName, subNodeName2);
            String childNodeLabel3 = XmlHelper.contructureErrorMessagePath(parentNodeName, subNodeName3);
            String childNodeLabel4 = XmlHelper.contructureErrorMessagePath(parentNodeName, subNodeName4);
            String childNodeLabel5 = XmlHelper.contructureErrorMessagePath(parentNodeName, subNodeName5);
            
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
            xmlParseResult = InvoiceDetail_VehiclesValidSchemaValidation(xmlParseResult, thisElement, childNodeLabel0, subNodeName0);
            
            // EXTRAS
            xmlParseResult = InvoiceDetail_ExtrasValidSchemaValidation(xmlParseResult, thisElement, doc, childNodeLabel1, subNodeName1);
            
            // REPAIR
            xmlParseResult = InvoiceDetail_RepairValidSchemaValidation(xmlParseResult, thisElement, childNodeLabel2, subNodeName2);
            
            // storage-recovery
            xmlParseResult = InvoiceDetail_StorageRecoveryValidSchemaValidation(xmlParseResult, thisElement, childNodeLabel3, subNodeName3);
            
            // engineer-fee
            xmlParseResult = InvoiceDetail_EngineerFeeValidSchemaValidation(xmlParseResult, thisElement, childNodeLabel4, subNodeName4);
            
            // claim-handling-fee
            //xmlParseResult = InvoiceDetail_ClaimHandlingFeeValidSchemaValidation(xmlParseResult, thisElement, childNodeLabel5, subNodeName5);

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
            
            ArrayList<RentalExtra> rentalExtras = new ArrayList<RentalExtra>();
            
            for (Element ee : extraElements) {

                xmlParseResult.setIsCurrentDataValid(true);
                xmlParseResult.setIsCurrentScheValid(true);

                xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "name", XmlHelper.isMAN_Invoice_Extras_Name, "", parentNodeName);
                xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "quantity", XmlHelper.isMAN_Invoice_Extras_Quantity, XmlHelper.REG_INTEGER, parentNodeName);
                xmlParseResult = xmlNodeValidation(xmlParseResult, ee, "item-cost", XmlHelper.isMAN_Invoice_Extras_Item_Cost, XmlHelper.REG_BIGDECIMAL, parentNodeName);

                if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
                    RentalExtra rentalExtra = new RentalExtra();
                    rentalExtra.setItemAmount(new BigDecimal(XmlHelper.getNodeValue(ee, "item-cost")));
                    rentalExtra.setQuantity(new BigDecimal(XmlHelper.getNodeValue(ee, "quantity")));
                    rentalExtra.setRentalID(xmlParseResult.getRental().getID());
                    rentalExtras.add(rentalExtra);
                }
            }
            
            System.out.println ("$$$ INVOICE DETAIL EXTRA : " + rentalExtras.size());
            
        }
        return xmlParseResult;
    }
    
    // VALIDATE INVOICE SECTION 
    private static XMLParseResult RentalVehiclesSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element mainElement,
            Document doc) throws Exception {
        
        String parentNodeName = "rental";
        String mainNodeName = "rental-vehicles";
        String childNodeName = "rental-vehicle";

        String childNodeLabelMain = XmlHelper.contructureErrorMessagePath(parentNodeName, "");
        String childNodeLabel1 = XmlHelper.contructureErrorMessagePath(childNodeLabelMain, mainNodeName);
        String childNodeLabel2 = XmlHelper.contructureErrorMessagePath(childNodeLabel1, childNodeName);

        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, childNodeLabelMain);

        if(xmlParseResult.getIsCurrentScheValid()){
            
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            ArrayList<Element> rentalVehicleElements = XMLUtils.getElements(doc, thisElement, childNodeName);
            xmlParseResult = xmlSchemaNodeListValidation(xmlParseResult, rentalVehicleElements, childNodeName, childNodeLabel1);
            
            if(xmlParseResult.getIsCurrentScheValid()){
                for (Element ee : rentalVehicleElements) {
                    xmlParseResult = RentalVehiclesDetailSchemaValidation(xmlParseResult, ee, doc, childNodeLabel2);
               }
            }
        }
        return xmlParseResult;
    } 
    
    private static XMLParseResult RentalVehiclesDetailSchemaValidation(
            XMLParseResult xmlParseResult, 
            Element mainElement,
            Document doc,
            String parentNodePath) throws Exception {
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);

        xmlParseResult = xmlNodeValidation(xmlParseResult, mainElement, "vehicle-registration", XmlHelper.isMAN_RentalVehicles_Vehicle_Registration, "", parentNodePath);
        xmlParseResult = xmlNodeValidation(xmlParseResult, mainElement, "vehicle-manufacturer", XmlHelper.isMAN_RentalVehicles_Vehicle_Manufacturer, "", parentNodePath);
        xmlParseResult = xmlNodeValidation(xmlParseResult, mainElement, "vehicle-model", XmlHelper.isMAN_RentalVehicles_Vehicle_Model, "", parentNodePath);
        xmlParseResult = xmlNodeValidation(xmlParseResult, mainElement, "vehicle-class", XmlHelper.isMAN_RentalVehicles_Vehicle_Class, "", parentNodePath);
        xmlParseResult = xmlNodeValidation(xmlParseResult, mainElement, "rental-start", XmlHelper.isMAN_RentalVehicles_Rental_Start, XmlHelper.REG_TIMESTAMP, parentNodePath);
        xmlParseResult = xmlNodeValidation(xmlParseResult, mainElement, "rental-end", XmlHelper.isMAN_RentalVehicles_Rental_End, XmlHelper.REG_TIMESTAMP, parentNodePath);
        xmlParseResult = xmlNodeValidation(xmlParseResult, mainElement, "rental-days", XmlHelper.isMAN_RentalVehicles_Rental_Days, XmlHelper.REG_INTEGER, parentNodePath);
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            
            RentalVehicle rentalvehicle = new RentalVehicle();
            rentalvehicle.setDays(new BigDecimal(XmlHelper.getNodeValue(mainElement, "rental-days")));
            rentalvehicle.setRentalEnd(XmlHelper.parseDate(XmlHelper.getNodeValue(mainElement, "rental-end")));
            rentalvehicle.setRentalStart(XmlHelper.parseDate(XmlHelper.getNodeValue(mainElement, "rental-start")));
            //rentalvehicle.setVehicleClassID(Long.parseLong(XmlHelper.getNodeValue(mainElement, "vehicle-class")));
            rentalvehicle.setVehicleManufacturer(XmlHelper.getNodeValue(mainElement, "vehicle-manufacturer"));
            rentalvehicle.setVehicleModel(XmlHelper.getNodeValue(mainElement, "vehicle-model"));
            rentalvehicle.setVehicleRegistration(XmlHelper.getNodeValue(mainElement, "vehicle-registration"));
            
            //rentalvehicle.setRentalID(XmlHelper.getNodeValue(mainElement, "rental-days");
            //rentalvehicle.setUuid(XmlHelper.getNodeValue(mainElement, "rental-days");
        }
        
        // PART 2 : EXTRA SECTION
        String nodeName1 = "extras";
        String nodeName2 = "extra";        
        
        String childNodeLabel1 = XmlHelper.contructureErrorMessagePath(parentNodePath, nodeName1);
        String childNodeLabel2 = XmlHelper.contructureErrorMessagePath(childNodeLabel1, nodeName2);
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, parentNodePath);
        
        if(xmlParseResult.getIsCurrentScheValid()){
            
            xmlParseResult.setIsCurrentScheValid(true);
            Element thisElement = XMLUtils.getElement(mainElement, nodeName1);
            xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, thisElement, nodeName2, childNodeLabel1);
            
            if(xmlParseResult.getIsCurrentScheValid()){
                
                ArrayList<Element> extraElements = XMLUtils.getElements(doc, thisElement, nodeName2);
                xmlParseResult = xmlSchemaNodeListValidation(xmlParseResult, extraElements, nodeName2, childNodeLabel1);
                
                if(xmlParseResult.getIsCurrentScheValid()){
                    for (Element ee : extraElements) {
                        xmlParseResult = xmlNodeValidation(xmlParseResult, thisElement, nodeName2, XmlHelper.isMAN_RentalVehicles_Extras_Extra, "", childNodeLabel2);
                    }
                }
            }
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
