package chox.Util;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.*;
import org.w3c.dom.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import com.filesystemsoftware.utils.XMLUtils;
import chox.model.*;

public class XmlHelper {
    
    // REGULAR EXPRESSION
    public static final String REG_BIGDECIMAL = "^\\-?(\\d+)*\\.?\\d*$";
    //public static final String REG_EMAIL = "^([0-9a-zA-Z]+([_.-]?[0-9a-zA-Z]+)*@[0-9a-zA-Z]+[0-9,a-z,A-Z,.,-]*(.){1}[a-zA-Z]{2,4})+$";
    public static final String REG_EMAIL = "";
    public static final String REG_INTEGER = "^[0-9]+$";
    public static final String REG_TIMESTAMP = "^\\d{4}-(0[0-9]|1[0,1,2])-([0-9]|[0,1,2][0-9]|3[0,1])[T]([0-9]{2}):([0-9]{2}):([0-9]{2})$";
    public static final String REG_DATE = "^\\d{4}-(0[0-9]|1[0,1,2])-([0-9]|[0,1,2][0-9]|3[0,1])$";
    //public static final String REG_VEHICLE_REG = "^((([A-Za-z, ]+[ ]{0,1}[0-9, ]+)|([0-9, ]+[ ]{0,1}[A-Za-z, ]+)))*$";
    public static final String REG_VEHICLE_REG = "^(?=.*[a-zA-Z])(?=.*[0-9]).*$";
    public static final String REG_PHONE = "";
    public static final String REG_BOOLEAN = "^[ynYN]";
    
    // CLAIM HEADER
    public static final Boolean isMAN_Driver_Primary_Driver = false;
    public static final Boolean isMAN_Supplier_Name= true;
    public static final Boolean isMAN_First_Contact= true;
    public static final Boolean isMAN_Status = true;
    public static final Boolean isMAN_Managing_Repair= true;
    public static final Boolean isMAN_Supplier_Reference= true;
    public static final Boolean isMAN_DateTimeCreditAgreementSigned = false;
    //public static final Boolean isMAN_GTANoticeDate = false;
    
    public static final Boolean isMAN_Driver_Address1= true;
    public static final Boolean isMAN_Driver_Address2= false;
    public static final Boolean isMAN_Driver_Address3= false;
    public static final Boolean isMAN_Driver_Address4= false;
    public static final Boolean isMAN_Driver_Address5= false;
    public static final Boolean isMAN_Claim_Customer_Insurer_claimReference= false;
    public static final Boolean isMAN_Claim_Customer_Insurer_comprehensive= true;
    public static final Boolean isMAN_Driver_Email= false;
    public static final Boolean isMAN_Driver_Firstnames= false;
    public static final Boolean isMAN_Claim_Customer_Insurer_name= true;
    public static final Boolean isMAN_Claim_Customer_Insurer_policyNumber= true;
    public static final Boolean isMAN_Driver_Postcode= true;
    public static final Boolean isMAN_Driver_Lastname= true;
    public static final Boolean isMAN_Driver_Telephone_day= true;
    public static final Boolean isMAN_Driver_Telephone_Evening= false;
    public static final Boolean isMAN_Driver_Title= true;
    public static final Boolean isMAN_Claim_Customer_Vehicle_Class=true;
    public static final Boolean isMAN_Claim_Customer_Vehicle_Location= true;
    public static final Boolean isMAN_Claim_Customer_Vehicle_Manufacturer= true;
    public static final Boolean isMAN_Claim_Customer_Vehicle_Model= true;
    public static final Boolean isMAN_Claim_Customer_Vehicle_Registration= true;
    public static final Boolean isMAN_Claim_Customer_Vehicle_Damage= true;
    public static final Boolean isMAN_Claim_Customer_Vehicle_InitialEcd= false;
    public static final Boolean isMAN_Claim_Customer_Vehicle_TotalLoss= true;
    public static final Boolean isMAN_Claim_Customer_Vehicle_Usable= true;
    public static final Boolean isMAN_Repair_engineerReport_address1= false;
    public static final Boolean isMAN_Repair_engineerReport_address2= false;
    public static final Boolean isMAN_Repair_engineerReport_address3= false;
    public static final Boolean isMAN_Repair_engineerReport_address4= false;
    public static final Boolean isMAN_Repair_engineerReport_address5= false;
    public static final Boolean isMAN_Repair_engineerReport_company= false;
    public static final Boolean isMAN_Repair_engineerReport_email= false;
    public static final Boolean isMAN_Repair_engineerReport_name= false;
    public static final Boolean isMAN_Repair_engineerReport_postcode= false;
    public static final Boolean isMAN_Repair_engineerReport_telephone= false;
    public static final Boolean isMAN_Repair_engineerReport_days= false;
    public static final Boolean isMAN_Repair_engineerReport_labour_Amount= false;
    public static final Boolean isMAN_Repair_engineerReport_total_Amount= false;
    public static final Boolean isMAN_Repair_engineerReport_usable= false;
    public static final Boolean isMAN_Invoice_Extras_Item_Cost= true;
    public static final Boolean isMAN_Invoice_Extras_Quantity= true;
    public static final Boolean isMAN_Invoice_Extras_Name= true;
    public static final Boolean isMAN_RentalVehicles_Rental_End= true;
    public static final Boolean isMAN_RentalVehicles_Rental_Start= true;
    public static final Boolean isMAN_RentalVehicles_Vehicle_Manufacturer= true;
    public static final Boolean isMAN_RentalVehicles_Vehicle_Model= true;
    public static final Boolean isMAN_RentalVehicles_Rental_Days= true;
    public static final Boolean isMAN_RentalVehicles_CollectionReason= true;
    public static final Boolean isMAN_RentalVehicles_Vehicle_Registration= true;
    public static final Boolean isMAN_RentalVehicles_Vehicle_Class= true;
    public static final Boolean isMAN_Claim_Incident_Date= true;
    public static final Boolean isMAN_Claim_Incident_Description= true;
    public static final Boolean isMAN_Claim_Incident_Location= true;
    public static final Boolean isMAN_Claim_Incident_PoliceInvolved= false;
    public static final Boolean isMAN_Claim_Incident_Injury_address1= false;
    public static final Boolean isMAN_Claim_Incident_Injury_address2= false;
    public static final Boolean isMAN_Claim_Incident_Injury_address3= false;
    public static final Boolean isMAN_Claim_Incident_Injury_address4= false;
    public static final Boolean isMAN_Claim_Incident_Injury_address5= false;
    public static final Boolean isMAN_Claim_Incident_Injury_email= false;
    public static final Boolean isMAN_Claim_Incident_Injury_name= false;
    public static final Boolean isMAN_Claim_Incident_Injury_postcode= false;
    public static final Boolean isMAN_Claim_Incident_Injury_telephoneDay= false;
    public static final Boolean isMAN_Claim_Incident_Injury_telephoneEvening= false;
    public static final Boolean isMAN_Claim_Incident_Solicitor_address1= false;
    public static final Boolean isMAN_Claim_Incident_Solicitor_address2= false;
    public static final Boolean isMAN_Claim_Incident_Solicitor_address3= false;
    public static final Boolean isMAN_Claim_Incident_Solicitor_address4= false;
    public static final Boolean isMAN_Claim_Incident_Solicitor_address5= false;
    public static final Boolean isMAN_Claim_Incident_Solicitor_email= false;
    public static final Boolean isMAN_Claim_Incident_Solicitor_name= false;
    public static final Boolean isMAN_Claim_Incident_Solicitor_postcode= false;
    public static final Boolean isMAN_Claim_Incident_Solicitor_telephone= false;
    public static final Boolean isMAN_Invoice_Supplier_HandlingInvoiceAmount= false;
    public static final Boolean isMAN_Invoice_DateInvoiced= true;
    public static final Boolean isMAN_Invoice_Engineer_Fee_Gross= true;
    public static final Boolean isMAN_Invoice_Engineer_Fee_Net= true;
    public static final Boolean isMAN_Invoice_Engineer_Fee_Vat= true;
    public static final Boolean isMAN_Invoice_Vehicles_DayRate= true;
    public static final Boolean isMAN_Invoice_Vehicles_Gross= true;
    public static final Boolean isMAN_Invoice_Vehicles_Net= true;
    public static final Boolean isMAN_Invoice_Vehicles_Vat= true;
    public static final Boolean isMAN_Invoice_lessHandlingFee= true;
    public static final Boolean isMAN_Invoice_lessDiscount= true;
    public static final Boolean isMAN_Invoice_Repair_Gross= true;
    public static final Boolean isMAN_Invoice_Repair_Net= true;
    public static final Boolean isMAN_Invoice_Repair_Vat= true;
    public static final Boolean isMAN_Invoice_Storage_Recovery_Gross= true;
    public static final Boolean isMAN_Invoice_Storage_Recovery_Net= true;
    public static final Boolean isMAN_Invoice_Storage_Recovery_Vat= true;
    public static final Boolean isMAN_Invoice_Supplier_ClaimInvoiceNo= true;
    
    public static final Boolean isMAN_Invoice_Supplier_ExceedCollected= false;
    public static final Boolean isMAN_Invoice_Supplier_VatCollected= false;
    
    public static final Boolean isMAN_Invoice_Supplier_HandlingInvoiceNo= false;
    public static final Boolean isMAN_Invoice_Gross= true;
    public static final Boolean isMAN_Invoice_Net= true;
    public static final Boolean isMAN_Invoice_TotalToPay= true;
    public static final Boolean isMAN_Invoice_Vat = true;
    
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Address1= false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Address2= false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Address3= false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Address4= false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Address5= false;
    public static final Boolean isMAN_Claim_ThirdParty_Insurer_ClaimReference= false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Email= false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Title= true;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Firstnames= true;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Lastname= true;
    public static final Boolean isMAN_Claim_ThirdParty_Insurer_Name= true;
    public static final Boolean isMAN_Claim_ThirdParty_Insurer_PolicyNumber= true;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Postcode= false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_TelephoneDay= false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_TelephoneEvening= false;
    public static final Boolean isMAN_Claim_ThirdParty_Vehicle_class= true;
    public static final Boolean isMAN_Claim_ThirdParty_Vehicle_manufacturer= false;
    public static final Boolean isMAN_Claim_ThirdParty_Vehicle_model= false;
    public static final Boolean isMAN_Claim_ThirdParty_Vehicle_Registration= true;
    
    public static final Boolean isMAN_Claim_Incident_Witness_address1= false;
    public static final Boolean isMAN_Claim_Incident_Witness_address2= false;
    public static final Boolean isMAN_Claim_Incident_Witness_address3= false;
    public static final Boolean isMAN_Claim_Incident_Witness_address4= false;
    public static final Boolean isMAN_Claim_Incident_Witness_address5= false;
    public static final Boolean isMAN_Claim_Incident_Witness_email= false;
    public static final Boolean isMAN_Claim_Incident_Witness_name= false;
    public static final Boolean isMAN_Claim_Incident_Witness_postcode= false;
    public static final Boolean isMAN_Claim_Incident_Witness_telephoneDay= false;
    public static final Boolean isMAN_Claim_Incident_Witness_telephoneEvening= false;
    public static final Boolean isMAN_RentalVehicles_Extras_Extra= true;

    public static Boolean isValidDataType(String dataValue, String regExpression, String nodeName){
        Boolean bFlag = true;
        
        if(!regExpression.equalsIgnoreCase("")){

            Pattern p = Pattern.compile(regExpression);
            Matcher m = p.matcher(dataValue);
                       
            if(!m.find()){
                bFlag = false;
            }
            
            if(nodeName.equalsIgnoreCase("vehicle-registration")){
                
                String vehicleReg = TextHelper.trimWhiteSpace(dataValue);

                if((vehicleReg.trim()).length()>8){
                    bFlag = false;
                }
            }
        }
        return bFlag;
    }
    
    // VALIDATION THE STRING PASSING IN IS NOT NULL AND EMPTY
    public static boolean isNotNull(String s){
        Boolean bFlag = false;
        if(s!=null && !s.equalsIgnoreCase("")){
            bFlag = true;
        }
        return bFlag;
    }
    
    public static String getNodeValue(Element root, String nodeName){
        String sOutput = "";
        if(isNotNull(XMLUtils.getElementValue(root, nodeName))){
            sOutput = XMLUtils.getElementValue(root, nodeName);
        }
        return sOutput;
    }
    
    /*
     * No <Field Name> supplied for <Bordereur Section>. Please resubmit with this information
     */
    public static String contructureDataMandatoryErrorMessage(String strSectionName, String strFieldName){
        String sMsg = String.format("No %s information supplied for %s. Please re-submit with this information.", strFieldName, strSectionName);
        return sMsg; //XMLResultDelimeterContructor("", sMsg);
    }
 
    public static String contructureIncorrectTypeErrorMessage(String strSectionName, String strFieldName){
        String sMsg = String.format("Invalid or incorrect character in %s for %s", strSectionName, strFieldName);
        return sMsg; //XMLResultDelimeterContructor("", sMsg);
    }
    
    public static String contructureSchemaErrorMessage(String strSectionName, String nodeDesc){
        
        String sMsg = String.format("Incorrect XML Schema in %s for %s.", strSectionName, nodeDesc);
        
        if(nodeDesc.trim().length()<=0){
            sMsg = String.format("Incorrect XML Schema in %s.", strSectionName);
        }
        
        //String sMsg = String.format("Incorrect XML Schema in %s for %s.", strSectionName, nodeDesc, strSectionName);
        return sMsg; //XMLResultDelimeterContructor("", sMsg);
    }
    
    public static String XMLResultDelimeterContructor(String orgStr, String newStr){
        String strReturn = "|" + newStr;
        if(isNotNull(orgStr)){
            strReturn = orgStr + strReturn;
        }
        return strReturn;
    }
        
    public static String getEmailAddressFromNode(Element thisElement, String thisNodeName){
        int iMaxEmailLenght = 64;
        
        String outEmail = XMLUtils.getElementValue(thisElement, thisNodeName);
        
        if(outEmail.length()<iMaxEmailLenght){
            iMaxEmailLenght = outEmail.length();
        }
        
        return outEmail.substring(0, iMaxEmailLenght);
    }

    public static Integer getIntegerFromNode(Element thisElement, String thisNodeName){
        Integer bOutput = 0;
        String sOutput = XMLUtils.getElementValue(thisElement, thisNodeName);
        if(sOutput!=null && !sOutput.equalsIgnoreCase("") && sOutput.length()>0){
            bOutput = Integer.parseInt(sOutput);
        }
        return bOutput;
    }
        
    public static BigDecimal getBigDecimalFromNode(Element thisElement, String thisNodeName){
        BigDecimal bOutput = new BigDecimal("0.00");
        String sOutput = XMLUtils.getElementValue(thisElement, thisNodeName);
        if(sOutput!=null && !sOutput.equalsIgnoreCase("") && sOutput.length()>0){
            bOutput = new BigDecimal(sOutput);
        }
        return bOutput;
    }
    
    public static double getDoubleFromNode(Element thisElement, String thisNodeName){
        double bOutput = 0.00;
        String sOutput = XMLUtils.getElementValue(thisElement, thisNodeName);
        if(sOutput!=null && !sOutput.equalsIgnoreCase("") && sOutput.length()>0){
            bOutput = Double.parseDouble(sOutput);
        }
        return bOutput;
    }
    
    public static Boolean getBooleanFromNode(Element thisElement, String thisNodeName){ 
        
        Boolean returnBoolean = false;
        String thisNodeValue = XMLUtils.getElementValue(thisElement, thisNodeName);
        
        if(thisNodeValue!=null && thisNodeValue.equalsIgnoreCase("y")){
            returnBoolean = true;
        }
        
        return returnBoolean;
        
    }
            
    public static Timestamp getTimeStampFromNode(Element thisElement, String thisNodeName){ 
        String thisNodeValue = XMLUtils.getElementValue(thisElement, thisNodeName);
        
        Timestamp returnTimeStamp = null;
        
        if(thisNodeValue!= null && !thisNodeValue.equalsIgnoreCase("")){
            returnTimeStamp = parseDate(thisNodeValue);
        }
        
        return returnTimeStamp;
    }
    
    public static Timestamp parseDate(String t)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.YEAR, Integer.parseInt(t.substring(0, 4)));
        c.set(Calendar.MONTH, Integer.parseInt(t.substring(5, 7)) - 1);
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(t.substring(8, 10)));
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t.substring(11, 13)));
        c.set(Calendar.MINUTE, Integer.parseInt(t.substring(14, 16)));
        c.set(Calendar.SECOND, Integer.parseInt(t.substring(17)));

        return new Timestamp(c.getTimeInMillis());
    }    
    
    public static XMLParseResult setErrorMessage(XMLParseResult xmlParseResult, String errorMessage, Boolean isSchemaError){
                
        if(isSchemaError){
            xmlParseResult.getSchemaValidationRemark().add(errorMessage);
            xmlParseResult.setIsSchemaValid(false);
            xmlParseResult.setIsCurrentScheValid(false);
        }else{
            xmlParseResult.getDataValidationRemark().add(errorMessage);
            xmlParseResult.setIsDataValid(false);
            xmlParseResult.setIsCurrentDataValid(false);
        }
        
        return xmlParseResult;
    }
    
    public static XMLParseResult xmlNodeValidation(
            XMLParseResult xmlParseResult,
            Element root,
            String nodeName,
            Boolean isMandatory,
            String regExpression,
            String strSectionName, 
            String nodeNameDesc) {
        
        xmlParseResult = xmlSchemaNodeValidation(xmlParseResult, root, nodeName, strSectionName, nodeNameDesc);

        if (xmlParseResult.getIsCurrentScheValid()) {
            xmlParseResult = xmlSchemaValueValidation(xmlParseResult, root, nodeName, isMandatory, regExpression, strSectionName, nodeNameDesc);
        }

        return xmlParseResult;
    }
    
    // VALIDATE THE ELEMENT
    public static XMLParseResult xmlSchemaNodeValidation(
        XMLParseResult xmlParseResult, 
        Element root, 
        String nodeName,
        String strSectionName, 
        String strNodeDesc) {

        Boolean bFlag = true;
        Element thisElement = XMLUtils.getElement(root, nodeName);

        if (thisElement == null) {
            bFlag = false;
        }

        if (!bFlag) {
            xmlParseResult.getSchemaValidationRemark().add(XmlHelper.contructureSchemaErrorMessage(strSectionName, strNodeDesc));
            xmlParseResult.setIsSchemaValid(bFlag);
            xmlParseResult.setIsCurrentScheValid(bFlag);
        }

        return xmlParseResult;
    }

    public static XMLParseResult xmlSchemaNodeListValidation(
            XMLParseResult xmlParseResult,
            ArrayList<Element> thisElements,
            String nodeName,
            String strSectionName,
            String strNodeDesc) {

        if (thisElements.size() <= 0) {
            xmlParseResult.getSchemaValidationRemark().add(XmlHelper.contructureSchemaErrorMessage(strSectionName, strNodeDesc));
            xmlParseResult.setIsSchemaValid(false);
            xmlParseResult.setIsCurrentScheValid(false);
        }

        return xmlParseResult;
    }

    public static XMLParseResult xmlSchemaValueValidation(
            XMLParseResult xmlParseResult,
            Element root,
            String nodeName,
            Boolean isMandatory,
            String regExpression,
            String strSectionName,
            String nodeNameDesc) {

        Boolean bFlag = true;

        //String DataValidationRemark = xmlParseResult.getDataValidationRemark();

        Element thisElement = XMLUtils.getElement(root, nodeName);

        if (thisElement != null) {

            String thisElementValue = XMLUtils.getElementValue(root, nodeName);
            String errorMsg = "";
            
            if (thisElementValue == null || thisElementValue.trim().length() == 0) {
                if (isMandatory) {
                    errorMsg = XmlHelper.contructureDataMandatoryErrorMessage(strSectionName, nodeNameDesc);
                    bFlag = false;
                }
            } else {
                if (!XmlHelper.isValidDataType(thisElementValue, regExpression, nodeName)) {
                    errorMsg = XmlHelper.contructureIncorrectTypeErrorMessage(strSectionName, nodeNameDesc);
                    bFlag = false;
                }
            }
            
            if (!bFlag) {
                xmlParseResult.getDataValidationRemark().add(errorMsg);
                xmlParseResult.setIsDataValid(bFlag);
                xmlParseResult.setIsCurrentDataValid(bFlag);
            }
        }
        return xmlParseResult;
    }       
}
