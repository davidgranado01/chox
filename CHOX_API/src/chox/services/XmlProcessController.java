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
        
        xmlParseResult = isFirstContactSchemaExist(xmlParseResult, root);
        xmlParseResult = isSupplierSchemaExist(xmlParseResult, root);
        xmlParseResult = isRentalStatusSchemaExist(xmlParseResult, root);
        xmlParseResult = isInvoiceSchemaExist(xmlParseResult, root, doc);
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
    private static XMLParseResult isFirstContactSchemaExist(XMLParseResult xmlParseResult, Element root) throws Exception {
        
        String nodeName = "first-contact";
        Boolean isMandatory = true;
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        String regEx = "^\\d{4}-(0[0-9]|1[0,1,2])-([0-9]|[0,1,2][0-9]|3[0,1])[T]([0-9]{2}):([0-9]{2}):([0-9]{2})$";
        
        // CHECK MAIN ELEMENT
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, nodeName);
        xmlParseResult = isSchemaValueValid(xmlParseResult, root, nodeName, isMandatory, regEx);

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
    private static XMLParseResult isSupplierDetailValid(XMLParseResult xmlParseResult, Element thisElement){

        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "supplier-name");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "supplier-name", true, "");
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "supplier-reference");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "supplier-reference", true, "");
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            
            String supplierNameValue = getNodeValue(thisElement, "supplier-name");
            String supplierReferenceValue = getNodeValue(thisElement, "supplier-reference");
            
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
    private static XMLParseResult isRentalStatusSchemaExist(XMLParseResult xmlParseResult, Element root) throws Exception {
        
        String nodeName = "rental-status";
        String dataType = "String";
        Boolean isMandatory = false;
        Boolean bFlag = false;
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        // CHECK MAIN ELEMENT
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, nodeName);
        xmlParseResult = isSchemaValueValid(xmlParseResult, root, nodeName, isMandatory, "^[a-zA-Z]+$");
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
            Rental rental = xmlParseResult.getRental();
            rental.setRentalStatus(getNodeValue(root, nodeName));
            xmlParseResult.setRental(rental);
        }
        
        // RETURN RESULT
        return xmlParseResult;        

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
    private static XMLParseResult isInvoiceSchemaExist(XMLParseResult xmlParseResult, Element root, Document doc) throws Exception {
        
        String nodeName = "invoice";
        
        Boolean bFlag = false;
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, root, nodeName);
        
        // CHECK INVOICE DETAIL
        xmlParseResult = isInvoiceDetailValid(xmlParseResult, root, doc);
        
        return xmlParseResult;
    }
    
    // VALIDATE INVOICE DETAIL SECTION
    private static XMLParseResult isInvoiceDetailValid(XMLParseResult xmlParseResult, Element root, Document doc) throws Exception {
        
        String nodeName = "invoice";
        Element thisNodeElement = XMLUtils.getElement(root, nodeName);
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisNodeElement, "net");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisNodeElement, "net", true, "");
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisNodeElement, "vat");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisNodeElement, "vat", true, "");
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisNodeElement, "gross");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisNodeElement, "gross", true, "");
        
        if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
                       
            BigDecimal bNet = new BigDecimal(getNodeValue(thisNodeElement, "net"));
            BigDecimal bVat = new BigDecimal(getNodeValue(thisNodeElement, "vat"));
            BigDecimal bGross = new BigDecimal(getNodeValue(thisNodeElement, "gross"));
            
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
            xmlParseResult = isInvoiceDetail_VehiclesValid(xmlParseResult, thisNodeElement);
            
            // REPAIR
            xmlParseResult = isInvoiceDetail_RepairValid(xmlParseResult, thisNodeElement);
            
            // storage-recovery
            xmlParseResult = isInvoiceDetail_StorageRecoveryValid(xmlParseResult, thisNodeElement);
            
            // engineer-fee
            xmlParseResult = isInvoiceDetail_EngineerFeeValid(xmlParseResult, thisNodeElement);
            
            // claim-handling-fee
            xmlParseResult = isInvoiceDetail_ClaimHandlingFeeValid(xmlParseResult, thisNodeElement);

            // extras
            xmlParseResult = isInvoiceDetail_ExtrasValid(xmlParseResult, thisNodeElement, doc);
        }
        
        return xmlParseResult;
    } 
    
    // VALIDATE INVOICE DETAIL - Vehicles SECTION
    private static XMLParseResult isInvoiceDetail_VehiclesValid(XMLParseResult xmlParseResult, Element invoiceElement){
        
        Element thisElement = XMLUtils.getElement(invoiceElement, "vehicles");
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "net");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "net", true, "");
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "vat");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "vat", true, "");
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "gross");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "gross", true, "");
        
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
        
        Element thisElement = XMLUtils.getElement(invoiceElement, "repair");
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "net");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "net", true, "");
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "vat");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "vat", true, "");
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "gross");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "gross", true, "");
        
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
        
        Element thisElement = XMLUtils.getElement(invoiceElement, "claim-handling-fee");
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "net");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "net", true, "");
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "vat");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "vat", true, "");
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "gross");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "gross", true, "");
        
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
        
        Element thisElement = XMLUtils.getElement(invoiceElement, "engineer-fee");
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "net");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "net", true, "");
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "vat");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "vat", true, "");
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "gross");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "gross", true, "");
        
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
        
        Element thisElement = XMLUtils.getElement(invoiceElement, "storage-recovery");
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "net");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "net", true, "");
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "vat");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "vat", true, "");
        xmlParseResult = isSchemaNodeExist(xmlParseResult, thisElement, "gross");
        xmlParseResult = isSchemaValueValid(xmlParseResult, thisElement, "gross", true, "");
        
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
        
        Element thisElement = XMLUtils.getElement(invoiceElement, "extras");
        ArrayList<Element> extraElements = XMLUtils.getElements(doc, thisElement, "extra");

        for (Element ee : extraElements) {
            
            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);
        
            xmlParseResult = isSchemaNodeExist(xmlParseResult, ee, "name");
            xmlParseResult = isSchemaValueValid(xmlParseResult, ee, "name", true, "");
            
            xmlParseResult = isSchemaNodeExist(xmlParseResult, ee, "quantity");
            xmlParseResult = isSchemaValueValid(xmlParseResult, ee, "quantity", true, "");
            
            xmlParseResult = isSchemaNodeExist(xmlParseResult, ee, "item-cost");
            xmlParseResult = isSchemaValueValid(xmlParseResult, ee, "item-cost", true, "");
            
            if(xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()){
                BigDecimal thisExtraItemCost = new BigDecimal(getNodeValue(thisElement, "item-cost"));
                Integer thisExtraQuantity = new Integer(getNodeValue(thisElement, "quantity"));
                String thisExtraName = getNodeValue(thisElement, "name");
            }
        }
        
        return xmlParseResult;
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
            Boolean isMandatory,
            String regExpression){
        
        Boolean bFlag = true;
        
        String DataValidationRemark = xmlParseResult.getDataValidationRemark();
        
        Element thisElement = XMLUtils.getElement(root, nodeName);

        if (thisElement != null) {
            
            String thisElementValue = XMLUtils.getElementValue(root, nodeName);
            
            if (thisElementValue == null || thisElementValue.trim().length() == 0){
                
                if(isMandatory){
                    DataValidationRemark = DataValidationRemark + "|Cannot find or invalid value for <"+nodeName+">";
                    bFlag = false;
                }
                
            }else{
                if(!XmlHelper.isValidDataType(thisElementValue, regExpression)){
                    DataValidationRemark = DataValidationRemark + "|Invalid value for <"+nodeName+">";
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
