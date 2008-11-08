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

import chox.model.*;

public class XmlProcessController {

    public static void main(String[] args) {
    
        try {
            
            String sXMLPath1 = "C:/Project Workplace/Greefinch/choxida/CHOX_STAGE_1_SUBMISSION.xml";
            String sXMLPath2 = "C:/Project Workplace/Greefinch/choxida/CHOX_STAGE_2_SUBMISSION.xml";
            String sUpdateType = "A";
            
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File(sXMLPath1));
            
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
                        
                        // if(parseResult.getRentalStatus()){
                        // 
                        // parseResult = RentalValidationResult(parseResult, doc, re, sUpdateType);
                        // }else{
                        //     throw new Exception("Invalid XML File!");
                        // }
                        
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
        
        xmlParseResult = isSupplierSchemaExist(xmlParseResult, root);
        xmlParseResult = isFirstContactSchemaExist(xmlParseResult, root);
                
        /*
        System.out.println (" ");
        System.out.println ("********************************************");
        System.out.println ("first-contact : " + XMLUtils.getElementValue(root, "first-contact"));
        System.out.println ("--------------------------------------------");
        System.out.println (" ** isSupplierSchemaExist error :" + isSupplierSchemaExist(root) + "");
        System.out.println (" ** isFirstContactSchemaExist error :" + isFirstContactSchemaExist(root) + "");
        System.out.println (" ** isRentalStatusSchemaExist error :" + isRentalStatusSchemaExist(root) + "");
        System.out.println (" ** isDriversSchemaExist error :" + isDriversSchemaExist(root) + "");
        System.out.println (" ** isClaimSchemaExist error :" + isClaimSchemaExist(root) + "");
        System.out.println (" ** isRepairSchemaExist error :" + isRepairSchemaExist(root) + "");
        System.out.println (" ** isInvoiceSchemaExist error :" + isInvoiceSchemaExist(root) + "");
        System.out.println (" ** isRentalVehiclesSchemaExist error :" + isRentalVehiclesSchemaExist(root) + "");
        System.out.println ("-----------------------------------------------");
        
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
        System.out.println ("");
        
        return xmlParseResult;
    }

    // VALIDATE RENTAL CONTACT SECTION 
    private static XMLParseResult isFirstContactSchemaExist(XMLParseResult xmlParseResult, Element root) throws Exception {
        
        String nodeName = "first-contact";
        String dataType = "datetime";
        Boolean isMandatory = false;
        
        // CHECK MAIN ELEMENT
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, nodeName);
        xmlParseResult = isSchemaValueValid(xmlParseResult, root, nodeName, dataType, isMandatory, "Regular Expression");
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            Rental rental = xmlParseResult.getRental();
            rental.setFirstContact(new Timestamp(System.currentTimeMillis()));
            xmlParseResult.setRental(rental);
        }
        
        // RETURN RESULT
        return xmlParseResult;
    } 
    
    // VALIDATE SUPPLIER SECTION
    private static XMLParseResult isSupplierSchemaExist(XMLParseResult xmlParseResult, Element root) throws Exception {
        
        String nodeName = "supplier";
        
        // CHECK MAIN ELEMENT
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, nodeName);
        
        // CHECK SUPPLIER DETAIL
        xmlParseResult = isSupplierDetailValid(xmlParseResult, root);

        return xmlParseResult;
    }
    
    // VALIDATE SUPPLIER DETAIL SECTION
    private static XMLParseResult isSupplierDetailValid(XMLParseResult xmlParseResult, Element root){
                        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, "supplier-name");
        xmlParseResult = isSchemaValueValid(xmlParseResult, root, "supplier-name", "string", true, "Regular Expression");
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, "supplier-reference");
        xmlParseResult = isSchemaValueValid(xmlParseResult, root, "supplier-reference", "string", true, "Regular Expression");
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            
            String supplierNameValue = getNodeValue(root, "supplier-name");
            String supplierReferenceValue = getNodeValue(root, "supplier-reference");

            Rental rental = xmlParseResult.getRental();

            // SET SUPPLIER INFORMATION
            Supplier supplier = new Supplier();
            // TODO: SETUP SUPPLIER INFORMATION / Compare with DB

            // SET RENTAL INFORMATION
            // rental.setSupplierID(null);
            rental.setSupplierReference(supplierReferenceValue);
            rental.setSupplier(supplier);
            
        }
        
        return xmlParseResult;
    }    

    // VALIDATE RENTAL STATUS SECTION 
    private static Boolean isRentalStatusSchemaExist(XMLParseResult xmlParseResult, Element root) throws Exception {
        
        String nodeName = "rental-status";
        
        Boolean bFlag = false;
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, nodeName);
        
        return bFlag;
    }  
        
    // VALIDATE DRIVER SECTION 
    private static Boolean isDriversSchemaExist(XMLParseResult xmlParseResult, Element root) throws Exception {
        
        String nodeName = "driver";
        
        Boolean bFlag = false;
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, nodeName);
        
        return bFlag;
    }  
    
    // VALIDATE CLAIM SECTION 
    private static Boolean isClaimSchemaExist(XMLParseResult xmlParseResult, Element root) throws Exception {
        
        String nodeName = "claim";
        
        Boolean bFlag = false;
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, nodeName);
        
        return bFlag;
    }  
    
    // VALIDATE CLAIM SECTION 
    private static Boolean isRepairSchemaExist(XMLParseResult xmlParseResult, Element root) throws Exception {
        
        String nodeName = "repair";
        
        Boolean bFlag = false;
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, nodeName);
        
        return bFlag;
    }  
    
    // VALIDATE INVOICE SECTION 
    private static Boolean isInvoiceSchemaExist(XMLParseResult xmlParseResult, Element root) throws Exception {
        
        String nodeName = "invoice";
        
        Boolean bFlag = false;
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, nodeName);
        
        return bFlag;
    }
    
    // VALIDATE INVOICE SECTION 
    private static Boolean isRentalVehiclesSchemaExist(XMLParseResult xmlParseResult, Element root) throws Exception {
        
        String nodeName = "rental-vehicles";
        
        Boolean bFlag = false;
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, nodeName);
        
        return bFlag;
    } 
    
    private static String getNodeValue(Element root, String nodeName){
        return XMLUtils.getElementValue(root, nodeName);
    }
    
    // VALIDATE THE ELEMENT
    private static XMLParseResult isSchemaNodeExist(XMLParseResult xmlParseResult, Element root, String nodeName){
        
        Boolean bFlag = true;
        String SchemaValidationRemark = xmlParseResult.getSchemaValidationRemark();
        xmlParseResult.setIsCurrentScheValid(bFlag);
        
        Element thisElement = XMLUtils.getElement(root, nodeName);
        
        if (thisElement == null) {
            SchemaValidationRemark = SchemaValidationRemark + "|Cannot find <"+nodeName+"> element";
            bFlag = false;   
        }
        
        if(!bFlag){
            xmlParseResult.setSchemaValidationRemark(SchemaValidationRemark);
            xmlParseResult.setIsSchemaValid(bFlag);
            xmlParseResult.setIsCurrentScheValid(bFlag);
        }
   
        return xmlParseResult;
    }
    
    private static XMLParseResult isSchemaValueValid(
            XMLParseResult xmlParseResult, 
            Element root, 
            String nodeName, 
            String dataType, 
            Boolean isMandatory,
            String regExpression){
        
        Boolean bFlag = true;
        String DataValidationRemark = xmlParseResult.getDataValidationRemark();
        xmlParseResult.setIsCurrentDataValid(bFlag);
        
        Element thisElement = XMLUtils.getElement(root, nodeName);

        if (thisElement != null) {
            
            String thisElementValue = XMLUtils.getElementValue(root, nodeName);
            
            if(isMandatory){
                
                if (thisElementValue == null || thisElementValue.trim().length() == 0){
                    DataValidationRemark = DataValidationRemark + "|Cannot find or invalid value for <"+nodeName+">";
                    bFlag = false;
                }else{
                    if(!XmlHelper.isValidDataType(nodeName, dataType, regExpression)){
                        DataValidationRemark = DataValidationRemark + "|Invalid value for <"+nodeName+">";
                        bFlag = false;
                    }
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
