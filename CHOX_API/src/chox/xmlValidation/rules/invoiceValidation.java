package chox.xmlValidation.rules;

import chox.xmlValidation.rules.Util.XmlHelper;
import org.w3c.dom.*;
import com.filesystemsoftware.utils.XMLUtils;
import chox.model.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class invoiceValidation{
    
    // VALIDATE INVOICE SECTION 
    public static  XMLParseResult InvoicesSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            Document doc) throws Exception {
            
        String strSectionName = "Invoice";
        
        // RESET VALIDATION FLAG AND VALIDATE MAIN INVOICE NODE SECTION
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "invoice", strSectionName, "");
        
        if (xmlParseResult.getIsCurrentScheValid()) {
            Element thisElement = XMLUtils.getElement(mainElement, "invoice");
            invoiceValidation invoiceCtrl = new invoiceValidation();
            xmlParseResult = invoiceCtrl.InvoiceSchemaValidation(xmlParseResult, thisElement, doc, strSectionName);
        }
        return xmlParseResult;
    }
    
    private XMLParseResult InvoiceSchemaValidation(
                XMLParseResult xmlParseResult,
                Element thisElement,
                Document doc,
                String strSectionName) throws Exception {
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        try {
            
            // VALIDATE INVOICE SUB SECTION
            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, "vehicles", strSectionName, "");
            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, "extras", strSectionName, "");
            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, "repair", strSectionName, "");
            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, "storage-recovery", strSectionName, "");
            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, "engineer-fee", strSectionName, "");
            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, "supplier", strSectionName, "");

            // VALIDATE INVOICE MAIN NODE VALUE
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Net, XmlHelper.REG_BIGDECIMAL, strSectionName, "Total Net");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Vat, XmlHelper.REG_BIGDECIMAL, strSectionName, "Total VAT");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Gross, XmlHelper.REG_BIGDECIMAL, strSectionName, "Total Gross");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "less-handling-fee", XmlHelper.isMAN_Invoice_lessHandlingFee, XmlHelper.REG_BIGDECIMAL, strSectionName, "Less Claims Handling Fee");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "less-discount", XmlHelper.isMAN_Invoice_lessDiscount, XmlHelper.REG_BIGDECIMAL, strSectionName, "Less Discount");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "total-to-pay", XmlHelper.isMAN_Invoice_TotalToPay, XmlHelper.REG_BIGDECIMAL, strSectionName, "Total to Pay");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "date-invoiced", XmlHelper.isMAN_Invoice_DateInvoiced, XmlHelper.REG_TIMESTAMP, strSectionName, "Date Invoiced");

            if (xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid() 
                    && xmlParseResult.getIsSchemaValid() && xmlParseResult.getIsDataValid()) {

                xmlParseResult.setIsNewInvoiceExit(true);

                Invoice invoice = new Invoice();
                invoice.setTotalGross(XmlHelper.getBigDecimalFromNode(thisElement, "gross"));
                invoice.setTotalNet(XmlHelper.getBigDecimalFromNode(thisElement, "net"));
                invoice.setTotalVat(XmlHelper.getBigDecimalFromNode(thisElement, "vat"));
                invoice.setTotalToPay(XmlHelper.getBigDecimalFromNode(thisElement, "total-to-pay"));
                invoice.setDiscount(XmlHelper.getBigDecimalFromNode(thisElement, "less-discount"));
                invoice.setDeductionForClaimsHandlingFee(XmlHelper.getBigDecimalFromNode(thisElement, "less-handling-fee"));
                invoice.setDateInvoiced(XmlHelper.getTimeStampFromNode(thisElement, "date-invoiced"));
                invoice.setPenaltyAlertQty(0);
                invoice.setPenaltyCharge(BigDecimal.ZERO);

                xmlParseResult.getClaim().setInvoice(invoice);

                invoiceValidation invoiceCtrl = new invoiceValidation();

                xmlParseResult = invoiceCtrl.VehiclesValidSchemaValidation(xmlParseResult, thisElement, strSectionName, "vehicles");
                xmlParseResult = invoiceCtrl.ExtrasValidSchemaValidation(xmlParseResult, thisElement, doc, "Extras", "extras");
                xmlParseResult = invoiceCtrl.RepairValidSchemaValidation(xmlParseResult, thisElement, strSectionName, "repair");
                xmlParseResult = invoiceCtrl.StorageRecoveryValidSchemaValidation(xmlParseResult, thisElement, strSectionName, "storage-recovery");
                xmlParseResult = invoiceCtrl.EngineerFeeValidSchemaValidation(xmlParseResult, thisElement, strSectionName, "engineer-fee");
                xmlParseResult = invoiceCtrl.SupplierValidSchemaValidation(xmlParseResult, thisElement, strSectionName, "supplier");

            }
            
        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        return xmlParseResult;
    }

    private  XMLParseResult VehiclesValidSchemaValidation(
                XMLParseResult xmlParseResult,
                Element mainElement,
                String strSectionName,
                String thisNodeName) throws Exception {

        try {
            
            Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);

            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);

            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Vehicles_Net, XmlHelper.REG_BIGDECIMAL, strSectionName, "Hire Net");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Vehicles_Vat, XmlHelper.REG_BIGDECIMAL, strSectionName, "Hire VAT");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Vehicles_Gross, XmlHelper.REG_BIGDECIMAL, strSectionName, "Hire Gross");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "day-rate", XmlHelper.isMAN_Invoice_Vehicles_DayRate, XmlHelper.REG_BIGDECIMAL, strSectionName, "Hire Rate Charged Per Day");

            if (xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()) {
                xmlParseResult.getClaim().getInvoice().setHireGross(XmlHelper.getBigDecimalFromNode(thisElement, "gross"));
                xmlParseResult.getClaim().getInvoice().setHireNet(XmlHelper.getBigDecimalFromNode(thisElement, "net"));
                xmlParseResult.getClaim().getInvoice().setHireVat(XmlHelper.getBigDecimalFromNode(thisElement, "vat"));
                xmlParseResult.getClaim().getInvoice().setHireRateChargedPerDay(XmlHelper.getBigDecimalFromNode(thisElement, "day-rate"));
            }
        
        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        return xmlParseResult;
    }    

    private  XMLParseResult ExtrasValidSchemaValidation(
                XMLParseResult xmlParseResult,
                Element mainElement,
                Document doc,
                String strSectionName,
                String thisNodeName) throws Exception {
        
        try {
            
            Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);

            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);

            ArrayList<Element> extraElements = XMLUtils.getElements(doc, thisElement, "extra");
            xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, extraElements, "extra", strSectionName, "");

            String strExtraNode = XMLUtils.getElement(thisElement, "extra").getTextContent();

            if (xmlParseResult.getIsCurrentScheValid()) {

                xmlParseResult.getClaim().getInvoice().setCdwFee(new BigDecimal("0.00"));
                xmlParseResult.getClaim().getInvoice().setCdwQty(0);
                xmlParseResult.getClaim().getInvoice().setAdminFee(new BigDecimal("0.00"));
                xmlParseResult.getClaim().getInvoice().setAdminQty(0);
                xmlParseResult.getClaim().getInvoice().setAutomaticFee(new BigDecimal("0.00"));
                xmlParseResult.getClaim().getInvoice().setAutomaticQty(0);   
                xmlParseResult.getClaim().getInvoice().setBabySeatFee(new BigDecimal("0.00"));
                xmlParseResult.getClaim().getInvoice().setBabySeatQty(0); 
                xmlParseResult.getClaim().getInvoice().setDeliveryCollectionFee(new BigDecimal("0.00"));
                xmlParseResult.getClaim().getInvoice().setDeliveryCollectionQty(0); 
                xmlParseResult.getClaim().getInvoice().setDualControlFee(new BigDecimal("0.00"));
                xmlParseResult.getClaim().getInvoice().setDualControlQty(0);    
                xmlParseResult.getClaim().getInvoice().setEstateFee(new BigDecimal("0.00"));
                xmlParseResult.getClaim().getInvoice().setEstateQty(0);  
                xmlParseResult.getClaim().getInvoice().setNonStandardInsurancePremiumFee(new BigDecimal("0.00"));
                xmlParseResult.getClaim().getInvoice().setNonStandardInsurancePremiumQty(0);  
                xmlParseResult.getClaim().getInvoice().setRoofRackFee(new BigDecimal("0.00"));
                xmlParseResult.getClaim().getInvoice().setRoofRackQty(0);  
                xmlParseResult.getClaim().getInvoice().setSatNavFee(new BigDecimal("0.00"));
                xmlParseResult.getClaim().getInvoice().setSatNavQty(0);  
                xmlParseResult.getClaim().getInvoice().setTowBarsFee(new BigDecimal("0.00"));
                xmlParseResult.getClaim().getInvoice().setTowBarsQty(0);  

                if(strExtraNode.length()>0){
                    for (Element ee : extraElements) {

                        xmlParseResult.setIsCurrentDataValid(true);
                        xmlParseResult.setIsCurrentScheValid(true);

                        String strExtraName = XmlHelper.getNodeValue(ee, "name");
                        String strExtraFee =  strExtraName+" Fee";
                        String strExtraQty =  strExtraName+" Quantity";

                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "name", XmlHelper.isMAN_Invoice_Extras_Name, "", strSectionName, strExtraName);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "quantity", XmlHelper.isMAN_Invoice_Extras_Quantity, XmlHelper.REG_INTEGER, strSectionName, strExtraQty);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "item-cost", XmlHelper.isMAN_Invoice_Extras_Item_Cost, XmlHelper.REG_BIGDECIMAL, strSectionName, strExtraFee);

                        if (xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()) {

                            String selectedExtra = XmlHelper.getNodeValue(ee, "name");
                            Integer iQuantity = XmlHelper.getIntegerFromNode(ee, "quantity");
                            BigDecimal dIntemCost = XmlHelper.getBigDecimalFromNode(ee, "item-cost");

                            if(selectedExtra.equalsIgnoreCase("CDW")){
                                xmlParseResult.getClaim().getInvoice().setCdwFee(dIntemCost);
                                xmlParseResult.getClaim().getInvoice().setCdwQty(iQuantity);                        
                            }else if(selectedExtra.equalsIgnoreCase("Admin")){
                                xmlParseResult.getClaim().getInvoice().setAdminFee(dIntemCost);
                                xmlParseResult.getClaim().getInvoice().setAdminQty(iQuantity);
                            }else if(selectedExtra.equalsIgnoreCase("Automatic")){
                                xmlParseResult.getClaim().getInvoice().setAutomaticFee(dIntemCost);
                                xmlParseResult.getClaim().getInvoice().setAutomaticQty(iQuantity);   
                            }else if(selectedExtra.equalsIgnoreCase("Baby Seat")){
                                xmlParseResult.getClaim().getInvoice().setBabySeatFee(dIntemCost);
                                xmlParseResult.getClaim().getInvoice().setBabySeatQty(iQuantity);                         
                            }else if(selectedExtra.equalsIgnoreCase("Delivery Collection")){
                                xmlParseResult.getClaim().getInvoice().setDeliveryCollectionFee(dIntemCost);
                                xmlParseResult.getClaim().getInvoice().setDeliveryCollectionQty(iQuantity);                         
                            }else if(selectedExtra.equalsIgnoreCase("Dual Control")){
                                xmlParseResult.getClaim().getInvoice().setDualControlFee(dIntemCost);
                                xmlParseResult.getClaim().getInvoice().setDualControlQty(iQuantity);                            
                            }else if(selectedExtra.equalsIgnoreCase("Estate")){
                                xmlParseResult.getClaim().getInvoice().setEstateFee(dIntemCost);
                                xmlParseResult.getClaim().getInvoice().setEstateQty(iQuantity);                          
                            }else if(selectedExtra.equalsIgnoreCase("Non-standard Risk Insurance Premium")){
                                xmlParseResult.getClaim().getInvoice().setNonStandardInsurancePremiumFee(dIntemCost);
                                xmlParseResult.getClaim().getInvoice().setNonStandardInsurancePremiumQty(iQuantity);                          
                            }else if(selectedExtra.equalsIgnoreCase("Roof Rack")){
                                xmlParseResult.getClaim().getInvoice().setRoofRackFee(dIntemCost);
                                xmlParseResult.getClaim().getInvoice().setRoofRackQty(iQuantity);                          
                            }else if(selectedExtra.equalsIgnoreCase("Sat Nav")){
                                xmlParseResult.getClaim().getInvoice().setSatNavFee(dIntemCost);
                                xmlParseResult.getClaim().getInvoice().setSatNavQty(iQuantity);                          
                            }else if(selectedExtra.equalsIgnoreCase("Tow Bars")){
                                xmlParseResult.getClaim().getInvoice().setTowBarsFee(dIntemCost);
                                xmlParseResult.getClaim().getInvoice().setTowBarsQty(iQuantity);                          
                            }
                        }
                    }
                }
            }
        
        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        return xmlParseResult;
    }

    private  XMLParseResult RepairValidSchemaValidation(
                XMLParseResult xmlParseResult,
                Element mainElement,
                String strSectionName,
                String thisNodeName) throws Exception {
        
        try {        
            
            Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);

            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);

            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Repair_Net, XmlHelper.REG_BIGDECIMAL, strSectionName, "Repair Net");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Repair_Vat, XmlHelper.REG_BIGDECIMAL, strSectionName, "Repair VAT");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Repair_Gross, XmlHelper.REG_BIGDECIMAL, strSectionName, "Repair Gross");

            if (xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()) {
                xmlParseResult.getClaim().getInvoice().setRepairGross(XmlHelper.getBigDecimalFromNode(thisElement, "gross"));
                xmlParseResult.getClaim().getInvoice().setRepairNet(XmlHelper.getBigDecimalFromNode(thisElement, "net"));
                xmlParseResult.getClaim().getInvoice().setRepairVat(XmlHelper.getBigDecimalFromNode(thisElement, "vat"));
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        return xmlParseResult;
    }

    private  XMLParseResult StorageRecoveryValidSchemaValidation(
                XMLParseResult xmlParseResult,
                Element mainElement,
                String strSectionName,
                String thisNodeName) throws Exception {
        
        try {        
            
            Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);

            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);

            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Storage_Recovery_Net, XmlHelper.REG_BIGDECIMAL, strSectionName, "Storage Recovery Net");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Storage_Recovery_Vat, XmlHelper.REG_BIGDECIMAL, strSectionName, "Storage Recovery VAT");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Storage_Recovery_Gross, XmlHelper.REG_BIGDECIMAL, strSectionName, "Storage Recovery Gross");

            if (xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()) {            
                xmlParseResult.getClaim().getInvoice().setStorageRecoveryGross(XmlHelper.getBigDecimalFromNode(thisElement, "gross"));
                xmlParseResult.getClaim().getInvoice().setStorageRecoveryNet(XmlHelper.getBigDecimalFromNode(thisElement, "net"));
                xmlParseResult.getClaim().getInvoice().setStorageRecoveryVat(XmlHelper.getBigDecimalFromNode(thisElement, "vat"));
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        return xmlParseResult;
    } 
    
    private XMLParseResult EngineerFeeValidSchemaValidation(
                XMLParseResult xmlParseResult,
                Element mainElement,
                String strSectionName,
                String thisNodeName) throws Exception {
        
        try { 
            
            Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);

            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);

            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Engineer_Fee_Net, XmlHelper.REG_BIGDECIMAL, strSectionName, "Engineer Fee Net");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Engineer_Fee_Vat, XmlHelper.REG_BIGDECIMAL, strSectionName, "Engineer Fee VAT");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Engineer_Fee_Gross, XmlHelper.REG_BIGDECIMAL, strSectionName, "Engineer Fee Gross");

            if (xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()) {            
                xmlParseResult.getClaim().getInvoice().setEngineerFeeGross(XmlHelper.getBigDecimalFromNode(thisElement, "gross"));
                xmlParseResult.getClaim().getInvoice().setEngineerFeeNet(XmlHelper.getBigDecimalFromNode(thisElement, "net"));
                xmlParseResult.getClaim().getInvoice().setEngineerFeeVat(XmlHelper.getBigDecimalFromNode(thisElement, "vat"));
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        return xmlParseResult;
    }

    private  XMLParseResult SupplierValidSchemaValidation(
                XMLParseResult xmlParseResult,
                Element mainElement,
                String strSectionName,
                String thisNodeName) throws Exception {
        
        try { 
                 
            Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);

            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);

            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "handling-invoice-no", XmlHelper.isMAN_Invoice_Supplier_HandlingInvoiceNo, "", strSectionName, "Supplier Claims Handling Invoice Number");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "handling-invoice-amount", XmlHelper.isMAN_Invoice_Supplier_HandlingInvoiceAmount, XmlHelper.REG_BIGDECIMAL, strSectionName, "Claims Handling Invoice Amount");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "claim-invoice-no", XmlHelper.isMAN_Invoice_Supplier_ClaimInvoiceNo, "", strSectionName, "Supplier Claim Invoice Number");

            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "excess-collected", XmlHelper.isMAN_Invoice_Supplier_ExceedCollected, "", strSectionName, "Excess Amount Collected From Policyholder");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vat-collected", XmlHelper.isMAN_Invoice_Supplier_VatCollected, "", strSectionName, "VAT Amount Collected From Policyholder");

            if (xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()) {
                xmlParseResult.getClaim().getInvoice().setHandlingInvoiceNo(XmlHelper.getNodeValue(thisElement, "handling-invoice-no"));
                xmlParseResult.getClaim().getInvoice().setClaimsHandlingInvoiceAmount(XmlHelper.getBigDecimalFromNode(thisElement, "handling-invoice-amount"));
                xmlParseResult.getClaim().getInvoice().setClaimInvoiceNo(XmlHelper.getNodeValue(thisElement, "claim-invoice-no"));
                xmlParseResult.getClaim().getInvoice().setExcessAmountCollected(XmlHelper.getBigDecimalFromNode(thisElement, "excess-collected"));
                xmlParseResult.getClaim().getInvoice().setVatAmountCollected(XmlHelper.getBigDecimalFromNode(thisElement, "vat-collected"));
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        return xmlParseResult;
    }
    
}
