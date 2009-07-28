package chox.xmlValidation.rules.enginee;

import chox.xmlValidation.rules.*;
import chox.Util.DateHelper;
import chox.xmlValidation.rules.Util.XmlHelper;
import org.w3c.dom.*;
import com.filesystemsoftware.utils.XMLUtils;
import chox.model.*;
import chox.services.ChoBandService;
import chox.services.ChorganisationService;
import chox.services.ClaimService;
import chox.services.SecureDataService;
import chox.xmlValidation.model.ClaimResult;
import chox.xmlValidation.model.status.ClaimParseStatus;
import chox.xmlValidation.rules.Util.NodeHelper;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.xml.xpath.XPathExpressionException;

public class ClaimHeaderValidation extends SecureDataService implements rulesInterface{
    
    protected static String sectionName = "Claim Header";
    private DataValidationParameter dataValidationParameter;
    private ClaimService claimService;
    private ChorganisationService chorganisationService;
    private ChoBandService choBandService;
    private ClaimResult claimResult;
    private Element element;

    public void setChoBandService(ChoBandService choBandService) { this.choBandService = choBandService; }
    public void setChorganisationService(ChorganisationService chorganisationService) { this.chorganisationService = chorganisationService; }
    public void setClaimResult(ClaimResult claimResult) { this.claimResult = claimResult; }
    public void setClaimService(ClaimService claimService) { this.claimService = claimService; }
    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) { this.dataValidationParameter = dataValidationParameter; }
    
    public ClaimHeaderValidation(ClaimResult claimResult, DataValidationParameter dataValidationParameter, 
            ClaimService claimService, ChorganisationService chorganisationService, ChoBandService choBandService){
            setClaimResult(claimResult);
            setDataValidationParameter(dataValidationParameter);
            setClaimService(claimService);
            setChorganisationService(chorganisationService);
            setChoBandService(choBandService);    
    }
    
    public ClaimResult execute() throws DOMException, XPathExpressionException{

        this.element = XMLUtils.getElement(claimResult.getElement(), "supplier");
        
        validate();
        process();
        doPrintResult(true);
        
        return claimResult;
    }
    
    private void validate(){
        
        this.claimResult.setCheckDataValid(true);
        this.claimResult = NodeHelper.nodeValidate(sectionName, "first-contact", claimResult.getElement(), claimResult, dataValidationParameter);
        this.claimResult = NodeHelper.nodeValidate(sectionName, "managing-repair", claimResult.getElement(), claimResult, dataValidationParameter);
        this.claimResult = NodeHelper.nodeValidate(sectionName, "agreement-signed", claimResult.getElement(), claimResult, dataValidationParameter);
        this.claimResult = NodeHelper.nodeValidate(sectionName, "gta-notice", claimResult.getElement(), claimResult, dataValidationParameter);
        this.claimResult = NodeHelper.nodeValidate(sectionName, "rental-status", claimResult.getElement(), claimResult, dataValidationParameter);
        this.claimResult = NodeHelper.nodeValidate(sectionName, "supplier-name", this.element, claimResult, dataValidationParameter);
        this.claimResult = NodeHelper.nodeValidate(sectionName, "supplier-reference", this.element, claimResult, dataValidationParameter);
        
    }
    
    private void process(){
        
        Boolean managingRepair = XmlHelper.getBooleanFromNode(claimResult.getElement(), "managing-repair");
        Timestamp firstContactDate = XmlHelper.getTimeStampFromNode(claimResult.getElement(), "first-contact");
        Timestamp creditAgreementDate = XmlHelper.getTimeStampFromNode(claimResult.getElement(), "agreement-signed");
        Timestamp gtaNoticeDate = XmlHelper.getTimeStampFromNode(claimResult.getElement(), "gta-notice");
        String choReferenceNumber = XmlHelper.getNodeValue(this.element, "supplier-reference");
        
        if(gtaNoticeDate==null){
            gtaNoticeDate = DateHelper.getCurrentTimeStamp();
        }

        Claim claim = new Claim();
        
        if(claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber)){
            
            claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);
            
            if(claim.getInvoice() != null){

                claimResult.setClaimParseStatus(ClaimParseStatus.existInvoice);
                claimResult.setValid(false);
                
            }else{
                
                if(claim.getStatus().equalsIgnoreCase(ClaimStatus.AWAITING_INVOICE_DATA)){
                    
                    claimResult.setClaimParseStatus(ClaimParseStatus.newInvoice);
                    ChoBand choBand = choBandService.getChoBandByChorganisationIdAndInsurerId(claim.getChorganisation().getId(), claim.getInsurer().getId());
                    claim.setChoband(choBand);

                }else if(claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED) ||
                    claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_PENDING) ||
                    claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_REJECTION_ACCEPTED)){
                    
                    // NOT EDITABNLE CLAIM
                    claimResult.setClaimParseStatus(ClaimParseStatus.ClaimNotEditable);
                    claimResult.setValid(false);
                    
                }else{
                    
                    // EDITABLE CLAIM
                    claimResult.setClaimParseStatus(ClaimParseStatus.existClaim);
                    
                }
            }
            
       }else{
            
            claimResult.setClaimParseStatus(ClaimParseStatus.newClaim);
            claim.setManagingRepair(managingRepair);
            claim.setPolicyHolderContactDate(firstContactDate);
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
            claim.setChoReference(choReferenceNumber);
            claim.setCreditAgreementDate(creditAgreementDate);
            claim.setGtaNoticeDate(gtaNoticeDate);
            claim.setIndemnityAmount(new BigDecimal("0.00"));
            claim.setPercentageLiabilityAccepted(new BigDecimal("0.00"));
            claim.setChorganisation(chorganisationService.getCurrentCHOrganisation());
            
        }
        
        claimResult.setClaim(claim);
    }
    
    
    private void doPrintResult(boolean isAllowed){
        
        if(isAllowed){
            
            System.out.println("-------");
            System.out.println(sectionName + "| " + XmlHelper.getNodeValue(this.element, "supplier-name"));
            
            if(this.claimResult.getClaim()!=null){

                System.out.println(sectionName + "| getManagingRepair :"+this.claimResult.getClaim().getManagingRepair());
                System.out.println(sectionName + "| getPolicyHolderContactDate :"+this.claimResult.getClaim().getPolicyHolderContactDate());
                System.out.println(sectionName + "| getStatus :"+this.claimResult.getClaim().getStatus());
                System.out.println(sectionName + "| getChoReference :"+this.claimResult.getClaim().getChoReference());
                System.out.println(sectionName + "| getCreditAgreementDate :"+this.claimResult.getClaim().getCreditAgreementDate());
                System.out.println(sectionName + "| getGtaNoticeDate :"+this.claimResult.getClaim().getGtaNoticeDate());
                System.out.println(sectionName + "| getIndemnityAmount :"+this.claimResult.getClaim().getIndemnityAmount());
                System.out.println(sectionName + "| getPercentageLiabilityAccepted :"+this.claimResult.getClaim().getPercentageLiabilityAccepted());  
            }
        }   
    }
    
    /*
    public static XMLParseResult ClaimHeaderSchemaValidation(
                XMLParseResult xmlParseResult,
                Element mainElement,
                ClaimService claimService,
                ChoBandService choBandService,
                ChorganisationService chorganisationService) throws Exception {
        
        Claim claim = new Claim();
        
        String strSectionName = "Claim Header";
        
        // RESET VALIDATION FLAG
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult.setIsCurrentDataValid(true);
        
        // CLAIM HEADER SECTION
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "rental-status", XmlHelper.isMAN_Status, "", strSectionName, "Hire State");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "managing-repair", XmlHelper.isMAN_Managing_Repair, XmlHelper.REG_BOOLEAN, strSectionName, "Managing Repair");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "first-contact", XmlHelper.isMAN_First_Contact, XmlHelper.REG_TIMESTAMP, strSectionName, "Policy Holder Contact");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "agreement-signed", XmlHelper.isMAN_DateTimeCreditAgreementSigned, XmlHelper.REG_TIMESTAMP, strSectionName, "Credit Agreement Signed by Insurer");
       
        Boolean bGatNotice = false;
        if(xmlParseResult.getIsClaimExist()){
            bGatNotice = true;
        }
        
        // MANDATORY ONLY IN SECOND STAGE
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "gta-notice", bGatNotice, XmlHelper.REG_TIMESTAMP, strSectionName, "GTA 4.1 Notice Date");

        // CHO ORGANISATION SECTION
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "supplier", strSectionName, "");

        Element thisElement = XMLUtils.getElement(mainElement, "supplier");

        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "supplier-name", XmlHelper.isMAN_Supplier_Name, "", strSectionName, "Supplier Name");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "supplier-reference", XmlHelper.isMAN_Supplier_Reference, "", strSectionName, "Supplier Reference");
        
        // CHO INFORMATION
        String strCHOReference = XmlHelper.getNodeValue(thisElement, "supplier-reference");
        claim.setChoReference(strCHOReference);
        
        if (xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()) {

            // RENTAL STATUS
            Boolean bManagingRepair = XmlHelper.getBooleanFromNode(mainElement, "managing-repair");
            Timestamp tFirstContactDate = XmlHelper.getTimeStampFromNode(mainElement, "first-contact");
            Timestamp tCreditAgreement = XmlHelper.getTimeStampFromNode(mainElement, "agreement-signed");
            Timestamp tGtaNoticeDate = XmlHelper.getTimeStampFromNode(mainElement, "gta-notice");

            if(XmlHelper.getNodeValue(mainElement, "gta-notice").equalsIgnoreCase("")){
                tGtaNoticeDate = DateHelper.getCurrentTimeStamp();
            }

            // CHECK SUPPLIER REFERENCE
            if(claimService.isClaimSupplierReferenceNumberExist(strCHOReference)){

                // CONFIGURATION TO CHECK XML UPLOAD STATUS
                xmlParseResult.setIsClaimExist(true);

                // GET EXISTING CLAIM INFORMATION                   
                claim = claimService.getClaimByCHOReferenceNumber(strCHOReference);
                
                // LOG CLAIM CURRENT STATUS
                xmlParseResult.setSExistingClaimStatus(claim.getStatus());
                ChoBand choBand = choBandService.getChoBandByChorganisationIdAndInsurerId(claim.getChorganisation().getId(), claim.getInsurer().getId());
                
                claim.setChoband(choBand);

                }else{

                    claim.setManagingRepair(bManagingRepair);
                    claim.setPolicyHolderContactDate(tFirstContactDate);
                    claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
                    claim.setChoReference(strCHOReference);
                    claim.setCreditAgreementDate(tCreditAgreement);
                    claim.setGtaNoticeDate(tGtaNoticeDate);
                    claim.setIndemnityAmount(new BigDecimal("0.00"));
                    claim.setPercentageLiabilityAccepted(new BigDecimal("0.00"));                    
                    claim.setChorganisation(chorganisationService.getCurrentCHOrganisation());
                }
            }
        
        xmlParseResult.setClaim(claim);
        
        return xmlParseResult;
    }

    // DONE MIGTATED AND CHECKED BUSINESS LOGIC    
    public static XMLParseResult ClaimSchemaValidation(
            XMLParseResult xmlParseResult,
            Element root,
            Document doc,
            VehicleClassService vehicleClassService,
            InsurerAlliasService insurerAlliasService,
            InsurerChorganisationService insurerChorganisationService) throws Exception {   
        
        try {
        
            xmlParseResult.setIsCurrentScheValid(true);
            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, root, "claim", "Claim Details", "");

            if (xmlParseResult.getIsCurrentScheValid()) {
                Element claimNodeElement = XMLUtils.getElement(root, "claim");
                
                //ClaimValidation claimCtrl = new ClaimValidation();
                //xmlParseResult = claimCtrl.CustomerSchemaValidation(xmlParseResult, claimNodeElement, "Customer Details", vehicleClassService);
                //xmlParseResult = claimCtrl.ThirdPartySchemaValidation(xmlParseResult, claimNodeElement, "Third Party Details", vehicleClassService, insurerAlliasService, insurerChorganisationService);
                //xmlParseResult = claimCtrl.IncidentSchemaValidation(xmlParseResult, claimNodeElement, doc, "Incident Details");
                
            }
        
        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        return xmlParseResult;
    }
    

    private  XMLParseResult CustomerSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            String strSectionName, 
            VehicleClassService vehicleClassService) throws Exception {

        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "customer", strSectionName, "");
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "insurer", strSectionName, "");
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "vehicle", strSectionName, "");

        if (xmlParseResult.getIsCurrentScheValid()) {

            Element thisElement = XMLUtils.getElement(mainElement, "customer");
                    
            // INSURER
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "name", XmlHelper.isMAN_Claim_Customer_Insurer_name, "", strSectionName, "Insurer");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "policy-number", XmlHelper.isMAN_Claim_Customer_Insurer_policyNumber, "", strSectionName, "Policy Number");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "claim-reference", XmlHelper.isMAN_Claim_Customer_Insurer_claimReference, "", strSectionName, "Claim Number");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "comprehensive", XmlHelper.isMAN_Claim_Customer_Insurer_comprehensive, "", strSectionName, "Comprehensive");

            // VEHICLE
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vehicle-registration", XmlHelper.isMAN_Claim_Customer_Vehicle_Registration, XmlHelper.REG_VEHICLE_REG, strSectionName, "Vehicle Registration Number");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vehicle-manufacturer", XmlHelper.isMAN_Claim_Customer_Vehicle_Manufacturer, "", strSectionName, "Vehicle Manufacturer");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vehicle-model", XmlHelper.isMAN_Claim_Customer_Vehicle_Model, "", strSectionName, "Vehicle Model");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vehicle-class", XmlHelper.isMAN_Claim_Customer_Vehicle_Class, "", strSectionName, "Vehicle Class");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "location", XmlHelper.isMAN_Claim_Customer_Vehicle_Location, "", strSectionName, "Vehicle Location");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "usable", XmlHelper.isMAN_Claim_Customer_Vehicle_Usable, "", "Customer Vehicle Damage", "Usable");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "damage", XmlHelper.isMAN_Claim_Customer_Vehicle_Damage, "", "Customer Vehicle Damage", "Description");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "initial-ecd", XmlHelper.isMAN_Claim_Customer_Vehicle_InitialEcd, XmlHelper.REG_TIMESTAMP, "Customer Vehicle Damage", "Initial ECD");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "total-loss", XmlHelper.isMAN_Claim_Customer_Vehicle_TotalLoss, XmlHelper.REG_BOOLEAN, "Customer Vehicle Damage", "Total Loss");
            
            if (xmlParseResult.getIsCurrentScheValid() && xmlParseResult.getIsCurrentDataValid()) {
                
                Customer customer = new Customer();

                if(xmlParseResult.getClaim()!=null && xmlParseResult.getClaim().getCustomer()!=null){
                    customer = xmlParseResult.getClaim().getCustomer();
                }
                
                // GET VEHICLE CLASS ID
                VehicleClass vehicleclass = vehicleClassService.getVehicleClassByNodeName(thisElement, "vehicle-class");
                
                if(vehicleclass!=null){
                    customer.setVehicleClass(vehicleclass);
                }else{
                    if(XmlHelper.isMAN_Claim_Customer_Vehicle_Class){
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, "Selected Vehicle Class is invalid for Customer Details", false);
                    }
                }
                
                customer.setInsurerName(XmlHelper.getNodeValue(thisElement, "name"));
                customer.setPolicyNumber(XmlHelper.getNodeValue(thisElement, "policy-number"));
                customer.setClaimReference(XmlHelper.getNodeValue(thisElement, "claim-reference"));
                customer.setComprehensive(XmlHelper.getBooleanFromNode(thisElement, "comprehensive"));
                customer.setVehicleRegistration(TextHelper.trimWhiteSpace(XmlHelper.getNodeValue(thisElement, "vehicle-registration")));
                customer.setVehicleManufacturer(XmlHelper.getNodeValue(thisElement, "vehicle-manufacturer"));
                customer.setVehicleModel(XmlHelper.getNodeValue(thisElement, "vehicle-model"));
                customer.setIsUsable(XmlHelper.getBooleanFromNode(thisElement, "usable"));
                customer.setLocation(XmlHelper.getNodeValue(thisElement, "location"));
                customer.setDamage(XmlHelper.getNodeValue(thisElement, "damage"));
                customer.setInitialECD(XmlHelper.getTimeStampFromNode(thisElement, "initial-ecd"));
                customer.setIsTotalLoss(XmlHelper.getBooleanFromNode(thisElement, "total-loss"));
                xmlParseResult.getClaim().setCustomer(customer);
                
            }
        }

        return xmlParseResult;
    }
    
    // DONE MIGTATED AND CHECKED BUSINESS LOGIC   
    private  XMLParseResult ThirdPartySchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            String strSectionName,
            VehicleClassService vehicleClassService,
            InsurerAlliasService insurerAlliasService,
            InsurerChorganisationService insurerChorganisationService) throws Exception {
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "third-party", strSectionName, "");
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "insurer", strSectionName, "");
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "vehicle", strSectionName, "");
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "driver", strSectionName, "");

        if (xmlParseResult.getIsCurrentScheValid()) {

            Element thisElement = XMLUtils.getElement(mainElement, "third-party");

            // INSURER
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "name", XmlHelper.isMAN_Claim_ThirdParty_Insurer_Name, "", strSectionName, "Insurer");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "policy-number", XmlHelper.isMAN_Claim_ThirdParty_Insurer_PolicyNumber, "", strSectionName, "Policy Number");
            
            // ONLY SET TO MANDATORY WHEN IN STAGE 2
            Boolean isClaimReferenceNumberMandatory = false;
            if(xmlParseResult.getIsClaimExist()){
                isClaimReferenceNumberMandatory = XmlHelper.isMAN_Claim_ThirdParty_Insurer_ClaimReference;
            }
            
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "claim-reference", isClaimReferenceNumberMandatory, "", strSectionName, "Claim Number");

            // VEHICLE
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vehicle-registration", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_Registration, XmlHelper.REG_VEHICLE_REG, strSectionName, "Vehicle Registration Number");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vehicle-manufacturer", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_manufacturer, "", strSectionName, "Vehicle Manufacturer");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vehicle-model", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_model, "", strSectionName, "Vehicle Model");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vehicle-class", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_class, "", strSectionName, "Vehicle Class");

            // DRIVER
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "title", XmlHelper.isMAN_Claim_ThirdParty_Driver_Title, "", strSectionName, "Title");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "firstnames", XmlHelper.isMAN_Claim_ThirdParty_Driver_Firstnames, "", strSectionName, "Firstnames");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "lastname", XmlHelper.isMAN_Claim_ThirdParty_Driver_Lastname, "", strSectionName, "Surname");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "address1", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address1, "", strSectionName, "Address1");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "address2", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address2, "", strSectionName, "Address2");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "address3", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address3, "", strSectionName, "Address3");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "address4", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address4, "", strSectionName, "Address4");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "address5", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address5, "", strSectionName, "Address5");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "postcode", XmlHelper.isMAN_Claim_ThirdParty_Driver_Postcode, "", strSectionName, "Postcode");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "telephone-day", XmlHelper.isMAN_Claim_ThirdParty_Driver_TelephoneDay, XmlHelper.REG_PHONE, strSectionName, "Telephone Day");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "telephone-evening", XmlHelper.isMAN_Claim_ThirdParty_Driver_TelephoneEvening, XmlHelper.REG_PHONE, strSectionName, "Telephone Evening");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "email", XmlHelper.isMAN_Claim_ThirdParty_Driver_Email, XmlHelper.REG_EMAIL, strSectionName, "Email");
            
            if (xmlParseResult.getIsCurrentScheValid() && xmlParseResult.getIsCurrentDataValid()) {
                
                ThirdParty thirdparty = new ThirdParty();
                
                if(xmlParseResult.getClaim()!=null && xmlParseResult.getClaim().getThirdParty()!=null){
                    thirdparty = xmlParseResult.getClaim().getThirdParty();
                }
                
                // GET INSURER INFORMATION
                String insurerAlliasName = XmlHelper.getNodeValue(thisElement, "name");
                InsurerAllias insurerallias = insurerAlliasService.getInsurerByAlliasName(insurerAlliasName);
                
                if(insurerallias!=null){ 
                    if(insurerallias.getInsurer()!=null){
                        if(!insurerChorganisationService.isActiveObjectExist(insurerallias.getInsurer().getId(), xmlParseResult.getClaim().getChorganisation().getId())){
                            xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, "Selected Third Party Insurer is invalid", false);
                        }else{
                            thirdparty.setInsurer(insurerallias.getInsurer());
                        }
                        
                    }
                }else{
                    if(XmlHelper.isMAN_Claim_ThirdParty_Insurer_Name){
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, "Selected Third Party Insurer is invalid", false);
                    }
                }
                
                
                
                 // GET VEHICLE CLASS ID
                VehicleClass vehicleclass = vehicleClassService.getVehicleClassByNodeName(thisElement, "vehicle-class");
                
                if(vehicleclass!=null){
                    thirdparty.setVehicleClass(vehicleclass);
                }else{
                    if(XmlHelper.isMAN_Claim_ThirdParty_Vehicle_class){
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, "Selected Vehicle Class is invalid", false);
                    }
                }
                
                // SET THIRD PARTY INFORMATION
                thirdparty.setPolicyNumber(XmlHelper.getNodeValue(thisElement, "policy-number"));
                thirdparty.setClaimReference(XmlHelper.getNodeValue(thisElement, "claim-reference"));
                thirdparty.setVehicleRegistration(TextHelper.trimWhiteSpace(XmlHelper.getNodeValue(thisElement, "vehicle-registration")));
                thirdparty.setVehicleManufacturer(XmlHelper.getNodeValue(thisElement, "vehicle-manufacturer"));
                thirdparty.setVehicleModel(XmlHelper.getNodeValue(thisElement, "vehicle-model"));
                thirdparty.setAddress1(XmlHelper.getNodeValue(thisElement, "address1"));
                thirdparty.setAddress2(XmlHelper.getNodeValue(thisElement, "address2"));
                thirdparty.setAddress3(XmlHelper.getNodeValue(thisElement, "address3"));
                thirdparty.setAddress4(XmlHelper.getNodeValue(thisElement, "address4"));
                thirdparty.setAddress5(XmlHelper.getNodeValue(thisElement, "address5"));
                thirdparty.setPostcode(XmlHelper.getNodeValue(thisElement, "postcode"));
                thirdparty.setTelephoneEvening(XmlHelper.getNodeValue(thisElement, "telephone-evening"));
                thirdparty.setTelephoneDay(XmlHelper.getNodeValue(thisElement, "telephone-day"));
                thirdparty.setEmail(XmlHelper.getEmailAddressFromNode(thisElement, "email"));
                thirdparty.setFirstName(XmlHelper.getNodeValue(thisElement, "firstnames"));
                thirdparty.setLastName(XmlHelper.getNodeValue(thisElement, "lastname"));
                thirdparty.setTitle(XmlHelper.getNodeValue(thisElement, "title"));
                
                xmlParseResult.getClaim().setThirdParty(thirdparty);
            }
        }
        return xmlParseResult;
    }
    
    // DONE MIGTATED AND CHECKED BUSINESS LOGIC   
    private  XMLParseResult IncidentSchemaValidation(
        XMLParseResult xmlParseResult,
        Element mainElement,
        Document doc,
        String strSectionName) throws Exception {

        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "incident", strSectionName, "");

        if (xmlParseResult.getIsCurrentScheValid()) {
            
            Element thisElement = XMLUtils.getElement(mainElement, "incident");
            
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "date", XmlHelper.isMAN_Claim_Incident_Date, XmlHelper.REG_TIMESTAMP, strSectionName, "Incident Date");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "location", XmlHelper.isMAN_Claim_Incident_Location, "", strSectionName, "Incident Location");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "police-involved", XmlHelper.isMAN_Claim_Incident_PoliceInvolved, "", strSectionName, "Police Involved");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "description", XmlHelper.isMAN_Claim_Incident_Description, "", strSectionName, "Description");
            
            if (xmlParseResult.getIsCurrentScheValid() && xmlParseResult.getIsCurrentDataValid()) {
                
                // VALIDATE AND RETRIEVE INCIDENT VALUE
                Incident incident = new Incident();
                
                if(xmlParseResult.getClaim()!=null && xmlParseResult.getClaim().getIncident()!=null){
                    incident = xmlParseResult.getClaim().getIncident();
                }
                
                incident.setDate(XmlHelper.getTimeStampFromNode(thisElement, "date"));
                incident.setLocation(XmlHelper.getNodeValue(thisElement, "location"));
                incident.setIsPoliceInvolved(XmlHelper.getBooleanFromNode(thisElement, "police-involved"));
                incident.setIncidentDescription(XmlHelper.getNodeValue(thisElement, "description"));
                
                xmlParseResult.getClaim().setIncident(incident);
                
                // SET SUB NODE FOR INCIDENT (WITNESS, INJURY, AND SOLICITOR
                // xmlParseResult = IncidentWitnessSchemaValidation(xmlParseResult, thisElement, doc, "Witness");
                xmlParseResult = IncidentInjuriesSchemaValidation(xmlParseResult, thisElement, doc, "Injury");
            }
        }
        
        return xmlParseResult;
    }

    private XMLParseResult IncidentInjuriesSchemaValidation(
        XMLParseResult xmlParseResult,
        Element mainElement,
        Document doc,
        String strSectionName) throws Exception {

        // RESET VALIDATION FLAG
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        // VALIDATE INJURIES NODE
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "injuries", strSectionName, "");
        
        if (xmlParseResult.getIsCurrentScheValid()) {
            
            // RESET VALIDATION FLAG
            xmlParseResult.setIsCurrentScheValid(true);
            xmlParseResult.setIsCurrentDataValid(true);
            
            // GET AND VALIDATE INJURY NODE
            Element thisElement = XMLUtils.getElement(mainElement, "injuries");
            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, "injury", strSectionName, "");
            
            if (xmlParseResult.getIsCurrentScheValid()) {
                
                // GET AND VALIDATE INJURY LIST NODE
                ArrayList<Element> injuriesElements = XMLUtils.getElements(doc, thisElement, "injury");
                xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, injuriesElements, "injury", strSectionName, "");
                
                if (xmlParseResult.getIsCurrentScheValid()) {

                    ArrayList<Injury> injuries = new ArrayList<Injury>();
                    ArrayList<Solicitor> solicitors = new ArrayList<Solicitor>();
                    
                    // LOOP ALL THE INJURIES RECORD
                    for (Element ee : injuriesElements) {

                        xmlParseResult.setIsCurrentDataValid(true);
                        xmlParseResult.setIsCurrentScheValid(true);

                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "name", XmlHelper.isMAN_Claim_Incident_Injury_name, "", strSectionName, "Injury Name");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address1", XmlHelper.isMAN_Claim_Incident_Injury_address1, "", strSectionName, "Address1");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address2", XmlHelper.isMAN_Claim_Incident_Injury_address2, "", strSectionName, "Address2");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address3", XmlHelper.isMAN_Claim_Incident_Injury_address3, "", strSectionName, "Address3");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address4", XmlHelper.isMAN_Claim_Incident_Injury_address4, "", strSectionName, "Address4");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address5", XmlHelper.isMAN_Claim_Incident_Injury_address5, "", strSectionName, "Address5");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "postcode", XmlHelper.isMAN_Claim_Incident_Injury_postcode, "", strSectionName, "Postcode");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "telephone-day", XmlHelper.isMAN_Claim_Incident_Injury_telephoneDay, XmlHelper.REG_PHONE, strSectionName, "Telephone Day");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "telephone-evening", XmlHelper.isMAN_Claim_Incident_Injury_telephoneEvening, XmlHelper.REG_PHONE, strSectionName, "Telephone Evening");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "email", XmlHelper.isMAN_Claim_Incident_Injury_email, "", strSectionName, "Email");
                        
                        Injury injury = new Injury();
                        
                        // INJURY RECORD DATA ALL CORRECT
                        if ((xmlParseResult.getIsCurrentDataValid() 
                                && xmlParseResult.getIsCurrentScheValid())
                                && (XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "name"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "address1"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "address2"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "address3"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "address4"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "address5"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "postcode"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "telephone-day"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "telephone-evening"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "email")))
                        ) {

                            // INJURY
                            injury.setIncident(xmlParseResult.getClaim().getIncident());
                            injury.setAddress1(XmlHelper.getNodeValue(ee, "address1"));
                            injury.setAddress2(XmlHelper.getNodeValue(ee, "address2"));
                            injury.setAddress3(XmlHelper.getNodeValue(ee, "address3"));
                            injury.setAddress4(XmlHelper.getNodeValue(ee, "address4"));
                            injury.setAddress5(XmlHelper.getNodeValue(ee, "address5"));
                            injury.setEmail(XmlHelper.getEmailAddressFromNode(ee, "email"));
                            injury.setName(XmlHelper.getNodeValue(ee, "name"));
                            injury.setPostcode(XmlHelper.getNodeValue(ee, "postcode"));
                            injury.setTelephoneDay(XmlHelper.getNodeValue(ee, "telephone-day"));
                            injury.setTelephoneEvening(XmlHelper.getNodeValue(ee, "telephone-evening"));
                            injuries.add(injury);
                            
                            // CHECK SOLICITOR
                            String strSectionNameSolicitor = "Injury Solicitor";
                            Element thisSubElement = XMLUtils.getElement(thisElement, "solicitor");
                            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, "solicitor", strSectionNameSolicitor, "");
                            
                            // RESET VALIDATION FLAG
                            xmlParseResult.setIsCurrentDataValid(true);
                            xmlParseResult.setIsCurrentScheValid(true);

                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "name", XmlHelper.isMAN_Claim_Incident_Solicitor_name, "", strSectionNameSolicitor, "Injury Solicitor Name");
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "address1", XmlHelper.isMAN_Claim_Incident_Solicitor_address1, "", strSectionNameSolicitor, "Address1");
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "address2", XmlHelper.isMAN_Claim_Incident_Solicitor_address2, "", strSectionNameSolicitor, "Address2");
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "address3", XmlHelper.isMAN_Claim_Incident_Solicitor_address3, "", strSectionNameSolicitor, "Address3");
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "address4", XmlHelper.isMAN_Claim_Incident_Solicitor_address4, "", strSectionNameSolicitor, "Address4");
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "address5", XmlHelper.isMAN_Claim_Incident_Solicitor_address5, "", strSectionNameSolicitor, "Address5");
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "postcode", XmlHelper.isMAN_Claim_Incident_Solicitor_postcode, "", strSectionNameSolicitor, "Postcode");
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "telephone", XmlHelper.isMAN_Claim_Incident_Solicitor_telephone, XmlHelper.REG_PHONE, strSectionNameSolicitor, "Telephone");
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "email", XmlHelper.isMAN_Claim_Incident_Solicitor_email, "", strSectionNameSolicitor, "Email");
                            
                            // SOLICITOR RECORD DATA ALL CORRECT
                            if( (XmlHelper.isNotNull(XmlHelper.getNodeValue(thisSubElement, "address1"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(thisSubElement, "address2"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(thisSubElement, "address3"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(thisSubElement, "address4"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(thisSubElement, "address5"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(thisSubElement, "email"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(thisSubElement, "name"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(thisSubElement, "postcode"))
                                || XmlHelper.isNotNull(XmlHelper.getNodeValue(thisSubElement, "telephone")))
                                && (xmlParseResult.getIsCurrentDataValid() 
                                && xmlParseResult.getIsCurrentScheValid())
                                ){
                                    // SOLICITOR
                                    Solicitor solicitor = new Solicitor();
                                    solicitor.setInjury(injury);
                                    solicitor.setAddress1(XmlHelper.getNodeValue(thisSubElement, "address1"));
                                    solicitor.setAddress2(XmlHelper.getNodeValue(thisSubElement, "address2"));
                                    solicitor.setAddress3(XmlHelper.getNodeValue(thisSubElement, "address3"));
                                    solicitor.setAddress4(XmlHelper.getNodeValue(thisSubElement, "address4"));
                                    solicitor.setAddress5(XmlHelper.getNodeValue(thisSubElement, "address5"));
                                    solicitor.setEmail(XmlHelper.getEmailAddressFromNode(thisSubElement, "email"));
                                    solicitor.setName(XmlHelper.getNodeValue(thisSubElement, "name"));
                                    solicitor.setPostcode(XmlHelper.getNodeValue(thisSubElement, "postcode"));
                                    solicitor.setTelephone(XmlHelper.getNodeValue(thisSubElement, "telephone"));
                                    solicitors.add(solicitor);
                            }
                        }
                    }
                    
                    if(injuries.size()>0){
                        xmlParseResult.setInjuries(injuries);
                    }
                    
                    if(solicitors.size()>0){
                        xmlParseResult.setSolicitors(solicitors);
                    }
                }
            }
        }
        return xmlParseResult;
    }    
    
    /*
    // DONE MIGTATED AND CHECKED BUSINESS LOGIC   
    private XMLParseResult IncidentWitnessSchemaValidation(
        XMLParseResult xmlParseResult,
        Element mainElement,
        Document doc,
        String strSectionName) throws Exception {
        
        // String nodeName1 = "witnesses";
        // String nodeName2 = "witness";
        // CONSTRUCT ERROR MESSAGE
        // String childNodeLabel1 = XmlHelper.contructureErrorMessage(parentNodeName, nodeName1);
        // String childNodeLabel2 = XmlHelper.contructureErrorMessage(childNodeLabel1, nodeName2);
        
        // RESET THE CHECKING FLAG
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        // VALIDATE WITNESSES MAIN NODE
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "witnesses", strSectionName, "");

        if (xmlParseResult.getIsCurrentScheValid()) {
            
            // RESET THE CHECKING FLAG
            xmlParseResult.setIsCurrentScheValid(true);
            xmlParseResult.setIsCurrentDataValid(true);
            
            // GET WITNESS NODE AND VALIDATE
            Element thisElement = XMLUtils.getElement(mainElement, "witnesses");
            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, "witness", strSectionName, "");
            
            if (xmlParseResult.getIsCurrentScheValid()) {
                
                // GET WITNESS LIST
                ArrayList<Element> witnessElements = XMLUtils.getElements(doc, thisElement, "witness");
                xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, witnessElements, "witness", strSectionName, "");

                if (xmlParseResult.getIsCurrentScheValid()) {

                   ArrayList<Witness> witnesses = new ArrayList<Witness>();

                    for (Element ee : witnessElements) {
                        
                        xmlParseResult.setIsCurrentDataValid(true);
                        xmlParseResult.setIsCurrentScheValid(true);

                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "name", XmlHelper.isMAN_Claim_Incident_Witness_name, "", strSectionName, "Witness Name");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address1", XmlHelper.isMAN_Claim_Incident_Witness_address1, "", strSectionName, "Address1");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address2", XmlHelper.isMAN_Claim_Incident_Witness_address2, "", strSectionName, "Address2");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address3", XmlHelper.isMAN_Claim_Incident_Witness_address3, "", strSectionName, "Address3");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address4", XmlHelper.isMAN_Claim_Incident_Witness_address4, "", strSectionName, "Address4");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address5", XmlHelper.isMAN_Claim_Incident_Witness_address5, "", strSectionName, "Address5");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "postcode", XmlHelper.isMAN_Claim_Incident_Witness_postcode, "", strSectionName, "Postcode");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "telephone-day", XmlHelper.isMAN_Claim_Incident_Witness_telephoneDay, XmlHelper.REG_PHONE, strSectionName, "Telephone Day");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "telephone-evening", XmlHelper.isMAN_Claim_Incident_Witness_telephoneEvening, XmlHelper.REG_PHONE, strSectionName, "Telephone Evening");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "email", XmlHelper.isMAN_Claim_Incident_Witness_email, "", strSectionName, "Email");

                        if ((xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid())
                            && (
                            XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "name"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "address1"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "address2"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "address3"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "address4"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "address5"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "postcode"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "telephone-day"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "telephone-evening"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "email"))
                            )
                        ) {
                            
                            Witness witness = new Witness();
                            
                            witness.setIncident(xmlParseResult.getClaim().getIncident());
                            witness.setAddress1(XmlHelper.getNodeValue(ee, "address1"));
                            witness.setAddress2(XmlHelper.getNodeValue(ee, "address2"));
                            witness.setAddress3(XmlHelper.getNodeValue(ee, "address3"));
                            witness.setAddress4(XmlHelper.getNodeValue(ee, "address4"));
                            witness.setAddress5(XmlHelper.getNodeValue(ee, "address5"));
                            witness.setEmail(XmlHelper.getEmailAddressFromNode(ee, "email"));
                            witness.setName(XmlHelper.getNodeValue(ee, "name"));
                            witness.setPostcode(XmlHelper.getNodeValue(ee, "postcode"));
                            witness.setTelephoneDay(XmlHelper.getNodeValue(ee, "telephone-day"));
                            witness.setTelephoneEvening(XmlHelper.getNodeValue(ee, "telephone-evening"));
                            
                            witnesses.add(witness);
                        }
                    }
                    
                    if(witnesses.size()>0){
                        xmlParseResult.setWitnesses(witnesses);
                    }
                }
            }
        }
        
        return xmlParseResult;
    }
    */
}
