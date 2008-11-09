package chox.services;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.regex.*;
import org.w3c.dom.*;
import com.filesystemsoftware.utils.XMLUtils;

public class XmlHelper {
    
    // REGULAR EXPRESSION
    public static final String REG_WORD = "^[0-9a-zA-Z]+$";
    public static final String REG_PHONE = "^[0-9, ,+,-]+$";
    //public static final String REG_BIGDECIMAL = "^[1-9]{1}([0-9]*)*?\\.[0-9]{1,2}$";
    public static final String REG_BIGDECIMAL = "";
    public static final String REG_EMAIL = "^([0-9a-zA-Z]+([_.-]?[0-9a-zA-Z]+)*@[0-9a-zA-Z]+[0-9,a-z,A-Z,.,-]*(.){1}[a-zA-Z]{2,4})+$";
    public static final String REG_INTEGER = "^[0-9]+$";
    public static final String REG_TIMESTAMP = "^\\d{4}-(0[0-9]|1[0,1,2])-([0-9]|[0,1,2][0-9]|3[0,1])[T]([0-9]{2}):([0-9]{2}):([0-9]{2})$";
    public static final String REG_DATE = "^\\d{4}-(0[0-9]|1[0,1,2])-([0-9]|[0,1,2][0-9]|3[0,1])$";
    
    // XML FILE MANDATORY FIELD - RENTAL
    public static final Boolean isMAN_Status = true;
    public static final Boolean isMAN_First_Contact = true;
    public static final Boolean isMAN_Managing_Repair = true; // NEW
    
    // XML FILE MANDATORY FIELD - SUPPLIER
    public static final Boolean isMAN_Supplier_Name = true;
    public static final Boolean isMAN_Supplier_Reference = true;
    
    // XML FILE MANDATORY FIELD - INVOICE SECTION
    public static final Boolean isMAN_Invoice_Net = true;
    public static final Boolean isMAN_Invoice_Vat = true;
    public static final Boolean isMAN_Invoice_Gross = true;
    public static final Boolean isMAN_Invoice_Vehicles_Net = true;
    public static final Boolean isMAN_Invoice_Vehicles_Vat = true;
    public static final Boolean isMAN_Invoice_Vehicles_Gross = true;    
    public static final Boolean isMAN_Invoice_Repair_Net = true;
    public static final Boolean isMAN_Invoice_Repair_Vat = true;
    public static final Boolean isMAN_Invoice_Repair_Gross = true;      
    public static final Boolean isMAN_Invoice_Claim_Handling_Fee_Net = true;
    public static final Boolean isMAN_Invoice_Claim_Handling_Fee_Vat = true;
    public static final Boolean isMAN_Invoice_Claim_Handling_Fee_Gross = true; 
    public static final Boolean isMAN_Invoice_Engineer_Fee_Net = true;
    public static final Boolean isMAN_Invoice_Engineer_Fee_Vat = true;
    public static final Boolean isMAN_Invoice_Engineer_Fee_Gross = true;
    public static final Boolean isMAN_Invoice_Storage_Recovery_Net = true;
    public static final Boolean isMAN_Invoice_Storage_Recovery_Vat = true;
    public static final Boolean isMAN_Invoice_Storage_Recovery_Gross = true;
    public static final Boolean isMAN_Invoice_Extras_Name = true;
    public static final Boolean isMAN_Invoice_Extras_Quantity = true;
    public static final Boolean isMAN_Invoice_Extras_Item_Cost = true; 
    
    // XML FILE MANDATORY FIELD - RENTAL VEHICLES
    public static final Boolean isMAN_RentalVehicles_Vehicle_Registration = true;    
    public static final Boolean isMAN_RentalVehicles_Vehicle_Manufacturer = true;
    public static final Boolean isMAN_RentalVehicles_Vehicle_Model = true;
    public static final Boolean isMAN_RentalVehicles_Vehicle_Class = true;
    public static final Boolean isMAN_RentalVehicles_Rental_Start = true;
    public static final Boolean isMAN_RentalVehicles_Rental_End = true;
    public static final Boolean isMAN_RentalVehicles_Rental_Days = true;
    public static final Boolean isMAN_RentalVehicles_Extras_Extra = true;

    // XML FILE MANDATORY FIELD - DRIVER
    public static final Boolean isMAN_Driver_Title = true;
    public static final Boolean isMAN_Driver_Firstnames = true;
    public static final Boolean isMAN_Driver_Lastname = true;
    public static final Boolean isMAN_Driver_Address1 = false;
    public static final Boolean isMAN_Driver_Address2 = false;
    public static final Boolean isMAN_Driver_Address3 = false;
    public static final Boolean isMAN_Driver_Address4 = false;
    public static final Boolean isMAN_Driver_Address5 = false;
    public static final Boolean isMAN_Driver_Postcode = false;
    public static final Boolean isMAN_Driver_Telephone_day = false;
    public static final Boolean isMAN_Driver_Telephone_Evening = false;
    public static final Boolean isMAN_Driver_Email = false;
    public static final Boolean isMAN_Driver_Primary_Driver = false;
        
    // XML FILE MANDATORY FIELD - CLAIM
    public static final Boolean isMAN_Claim_Customer_Insurer_name = true;
    public static final Boolean isMAN_Claim_Customer_Insurer_policyNumber = true;
    public static final Boolean isMAN_Claim_Customer_Insurer_claimReference = false;
    public static final Boolean isMAN_Claim_Customer_Insurer_comprehensive = true;
    public static final Boolean isMAN_Claim_Customer_Vehicle_Registration = true;
    public static final Boolean isMAN_Claim_Customer_Vehicle_Manufacturer = true;
    public static final Boolean isMAN_Claim_Customer_Vehicle_Model = true;
    public static final Boolean isMAN_Claim_Customer_Vehicle_Class = true;
    public static final Boolean isMAN_Claim_Customer_Vehicle_Usable = true;
    
    public static final Boolean isMAN_Claim_ReplacementVehicle_VehicleClass = true;
    public static final Boolean isMAN_Claim_ThirdParty_Insurer_Name = true;
    public static final Boolean isMAN_Claim_ThirdParty_Insurer_PolicyNumber = false;
    public static final Boolean isMAN_Claim_ThirdParty_Insurer_ClaimReference = false;
    public static final Boolean isMAN_Claim_ThirdParty_Vehicle_Registration = false;
    public static final Boolean isMAN_Claim_ThirdParty_Vehicle_manufacturer = false;
    public static final Boolean isMAN_Claim_ThirdParty_Vehicle_model = false;
    public static final Boolean isMAN_Claim_ThirdParty_Vehicle_class = false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Title = false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Firstnames = false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Lastname = false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Address1 = false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Address2 = false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Address3 = false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Address4 = false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Address5 = false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Postcode = false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_TelephoneDay = false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_TelephoneEvening = false;
    public static final Boolean isMAN_Claim_ThirdParty_Driver_Email = false;
    public static final Boolean isMAN_Claim_Incident_Date = true;
    public static final Boolean isMAN_Claim_Incident_Location = true;
    public static final Boolean isMAN_Claim_Incident_PoliceInvolved = true;
    public static final Boolean isMAN_Claim_Incident_Description = true;    
    
    // XML FILE MANDATORY FIELD - REPAIR
    public static final Boolean isMAN_Repair_engineerReport_labour_Amount = true;
    public static final Boolean isMAN_Repair_engineerReport_total_Amount = true;
    public static final Boolean isMAN_Repair_engineerReport_days = true;

    public static Boolean isValidDataType(String dataValue, String regExpression){
        Boolean bFlag = true;
        
        if(!regExpression.equalsIgnoreCase("")){

            Pattern p = Pattern.compile(regExpression);
            Matcher m = p.matcher(dataValue);
            
            if(!m.find()){
                bFlag = false;
            }
        }
        
        return bFlag;
    }
    
    public static String getNodeValue(Element root, String nodeName){
        return XMLUtils.getElementValue(root, nodeName);
    }
    
    public static String contructureDataMandatoryErrorMessage(String strPath, String nodeName){
        return ("|Cannot find or invalid value <"+strPath+":"+nodeName+">").toUpperCase();
    }
    
    public static String contructureIncorrectTypeErrorMessage(String strPath, String nodeName){
        return ("|Invalid Data Format for <"+strPath+":"+nodeName+">").toUpperCase();
    }
    
    public static String contructureSchemaErrorMessage(String strPath, String nodeName){
        return ("|Incorrect XML Schema for <"+strPath+":"+nodeName+"> element").toUpperCase();
    }
    
    public static String contructureErrorMessagePath(String strPath, String nodeName){
        String returnStr = strPath;
        if(!nodeName.equalsIgnoreCase("")){
            returnStr = returnStr + ":" + nodeName;
        }
        return returnStr;
    }
}
