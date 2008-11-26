package chox.services;

import chox.Util.XmlHelper;
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
import org.hibernate.Session;
import chox.data.HibernateUtil;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import scsbre.engine.*;
import java.util.List;
import chox.Util.TextHelper;

public class XmlProcessController {
    
    VehicleClassService vehicleClassService;
    
    public XmlProcessController()
    {
        vehicleClassService = new VehicleClassServiceImpl();
    }

    public static void main(String[] args) {

        try {
            
            String sXMLPath1 = "C:/Users/Carlson/Desktop/CHOX/20081125_TESTXML.xml";
            Boolean isAllowPartialUpload = true;

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(sXMLPath1));

            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();
            
            doc = docBuilder.parse(new File(sXMLPath1));

            doc.getDocumentElement().normalize();
            root = doc.getDocumentElement();

            if (root != null && root.getTagName().equals("chox")) {

                ArrayList<XMLParseResult> xmlParseResults = new ArrayList<XMLParseResult>();
                ArrayList<Element> rentalElements = XMLUtils.getElements(doc, root, "rental");

                int count = 0;
                for (Element re : rentalElements) {

                    try {
                        count++;
                        
                        XmlProcessController thisCtrl = new XmlProcessController();

                        XMLParseResult xmlParseResult = new XMLParseResult();
                        xmlParseResult = thisCtrl.xmlSchemaValidateProcess(xmlParseResult, doc, re, isAllowPartialUpload);
                        xmlParseResults.add(xmlParseResult);
                        
                    } catch (Exception e) {
                        Logger.err.println("Error loading record " + count);
                        throw e;
                    }
                }
            }

        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    public ArrayList<XMLParseResult> XMLValidationProcess(File claimXMLFile, Boolean isAllowPartialUpload) {

        ArrayList<XMLParseResult> xmlParseResults = new ArrayList<XMLParseResult>();

        try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(claimXMLFile);
            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();

            if (root != null && root.getTagName().equals("chox")) {

                ArrayList<Element> rentalElements = XMLUtils.getElements(doc, root, "rental");

                int count = 0;
                for (Element re : rentalElements) {

                    try {
                        
                        count++;
                        XMLParseResult xmlParseResult = new XMLParseResult();
                        xmlParseResult = xmlSchemaValidateProcess(xmlParseResult, doc, re, isAllowPartialUpload);
                        xmlParseResults.add(xmlParseResult);

                    } catch (Exception e) {
                        Logger.err.println("Error loading record " + count);
                        throw e;
                    }
                }
            }

        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return xmlParseResults;
    }
    
    /*
     * MAIN METHOS TO PROCESS THE XML PARSE PASSING IN
     * 1. CLAIM IS INSERT MODE ONLY
     * 2. CUSTOMER DETAIL ONLY INSERT WHEN IS NEW CLAIM
     * 3. ENGINEER REPORT IS UPSERT MODE
     * 4. VEHICLE HIRE DETAIL IS UPSERT MODE
     * 5. INVOICE IS INSERT MODE AND ONLY WHEN THE CLAIM STATUS IS AwaitingInvoiceData
     */
    public XMLParseResult xmlSchemaValidateProcess(
            XMLParseResult xmlParseResult,
            Document doc,
            Element root,
            Boolean isAllowPartialUpload) throws Exception {
            
        System.out.println("START********************************************");
        
        Session currentSession = SessionFactoryUtils.getSession(HibernateUtil.getSessionFactory(), true);
        currentSession.beginTransaction();
        
        xmlParseResult.setCurrentSession(currentSession);
         
        // VALIDATE AND GET RECORD FOR CLAIM OBJECT AND CHECK THE CLAIM IS EXIST OR NOT 
        xmlParseResult = CHOoganisationSchemaValidation(xmlParseResult, root);
        System.out.println(" ** CHO REFERENCE: " + xmlParseResult.getClaim().getChoReference());

        // GET CLAIM INFORMATION IF IT IS NEW CLAIM TO BE INSERTED 
        if(!xmlParseResult.getIsClaimExist()){
            xmlParseResult = RentalDriversSchemaValidation(xmlParseResult, root, doc);
            xmlParseResult = RentalClaimSchemaValidation(xmlParseResult, root, doc);
        }
        
        // ALWAYS GET LATEST ENGINEER REPORT AND VEHICLE HIRE INFORMATION FROM BORDEREUR (UPSERT MODE)
        xmlParseResult = RentalRepairSchemaValidation(xmlParseResult, root, doc);
        xmlParseResult = RentalVehiclesSchemaValidation(xmlParseResult, root, doc);

        if(xmlParseResult.getClaim().getInvoice()!=null){
            xmlParseResult.setIsInvoiceExist(true);
        }
  
        /*
         * ONLY PROCESS THE INVOICE WHERE
         * 1. CLAIM IS EXIST IN DB 
         * 2. CLAIM STATUS = 'AwaitingInvoiceData'
         */
        if(xmlParseResult.getIsClaimExist() 
            && xmlParseResult.getClaim().getStatus().equalsIgnoreCase(ClaimStatus.AWAITING_INVOICE_DATA)
            && !xmlParseResult.getIsInvoiceExist()){
            
            xmlParseResult = RentalInvoiceSchemaValidation(xmlParseResult, root, doc);

            // EXECUTE BRE RULE
            if(xmlParseResult.getIsSchemaValid() && xmlParseResult.getIsDataValid()){
/*                
if(xmlParseResult.getClaim().getEngineerReport()==null){
    System.out.println("0 @@@@@@@@@ NULL");
}else{
    System.out.println("0 @@@@@@@@@ NOT NULL");
}
*/                        
                InvoiceService invoiceservice = new InvoiceServiceImpl();
                RulesEngineResponse validationResult = invoiceservice.XMLUploaderInvoiceValidation(constructeClaimForInvoiceValidation(xmlParseResult.getClaim()));
                String newClaimStatus = validationResult.getStatus().toString();
                xmlParseResult.getClaim().setStatus(newClaimStatus);
/*
if(xmlParseResult.getClaim().getEngineerReport()==null){
    System.out.println("1 @@@@@@@@@ NULL");
}else{
    System.out.println("1 @@@@@@@@@ NOT NULL");
}
*/                
                if(validationResult.getResults().size()>0){
                    // LOG ERROR MESSAGE TO SCREEN
                    xmlParseResult = appendInvoiceValidationErrorMessage(xmlParseResult, validationResult.getResults());
                }
            }
        }
        
        if(xmlParseResult.getIsSchemaValid() && xmlParseResult.getIsDataValid()){            
            xmlParseResult = saveXMLRecord(xmlParseResult);
        }
        
        
        
        System.out.println(" ** FINAL STATUS CODE: " + xmlParseResult.getUploadStatusCode());
        System.out.println(" ** FINAL STATUS: " + xmlParseResult.getUploadStatus());
        System.out.println(" ** CLAIM EXIST: " + xmlParseResult.getIsClaimExist());
        System.out.println(" ** INVOICE EXIST: " + xmlParseResult.getIsInvoiceExist());
        System.out.println(" ** CLAIM STATUS: " + xmlParseResult.getClaim().getStatus());
        System.out.println(" ** getIsSchemaValid: " + xmlParseResult.getIsSchemaValid());
        System.out.println(" ** getSchemaValidationRemark: " + xmlParseResult.getSchemaValidationRemark());
        System.out.println(" ** getIsDataValid: " + xmlParseResult.getIsDataValid());
        System.out.println(" ** getDataValidationRemark: " + xmlParseResult.getDataValidationRemark());
        
        
        if(xmlParseResult.getIsSchemaValid() && xmlParseResult.getIsDataValid()){
            currentSession.getTransaction().commit();
        }else{
            currentSession.getTransaction().rollback();
        }
        
        System.out.println("END  ********************************************");

        return xmlParseResult;
    }
    
    private Claim constructeClaimForInvoiceValidation(Claim claim){
        
        // INTERFACE MAPPING WITH BRE - WHERE HIRE MONITORING NOT EXIST
        Boolean isIsTotalLostCheck = false;
        if(claim.getHireMonitoringDetail()!=null){
            isIsTotalLostCheck = claim.getHireMonitoringDetail().isIsTotalLostCheck();
        }
        claim.getVehicleHire().setIsTotalLoss(isIsTotalLostCheck);

        // CONSTRUCTE DUMMY ENGINEERING REPORT WITH ALL VALUE IS ZERO WHEN ER NOT EXIST
        if(claim.getEngineerReport()==null){
            EngineerReport engineerreport = new EngineerReport();
            
            engineerreport.setDays(0);
            engineerreport.setLabourAmount(new BigDecimal("0.00"));
            engineerreport.setTotalAmount(new BigDecimal("0.00"));
            
            claim.setEngineerReport(engineerreport);
        }
        
        // SET VEHICLE CLASS TO NULL WHEN 
        if(claim.getThirdParty().getVehicleClass().getName().equalsIgnoreCase("Unattached")){
            claim.getThirdParty().setVehicleClass(null);
        }
        
        // SET VEHICLE CLASS TO NULL WHEN 
        if(claim.getCustomer().getVehicleClass().getName().equalsIgnoreCase("Unattached")){
            claim.getCustomer().setVehicleClass(null);
        }
        
        return claim;
    }
    
    private XMLParseResult appendInvoiceValidationErrorMessage(XMLParseResult xmlParseResult, List<RuleEvaluation> results){
        
        String existingErrorMsg = xmlParseResult.getDataValidationRemark();
        
        for(int iCount=0; iCount<results.size(); iCount++){
            
            RuleEvaluation rv = results.get(iCount);
            
            if(rv.getIsVisibleToCHO() && rv.getResult()==RuleEvaluationResult.RuleFailed){
                
                existingErrorMsg = XmlHelper.XMLResultDelimeterContructor(existingErrorMsg, rv.toString());
            }
        }
        
        xmlParseResult.setDataValidationRemark(existingErrorMsg);
        return xmlParseResult;
    }
    
    private XMLParseResult saveXMLRecord(XMLParseResult xmlParseResult){

        EngineerReportService erService = new EngineerReportServiceImpl();
        IncidentService icService = new IncidentServiceImpl();
        InjuryService ijService = new InjuryServiceImpl();
        CustomerService ctService = new CustomerServiceImpl();
        InvoiceService ivService = new InvoiceServiceImpl();
        ClaimService csService = new ClaimServiceImpl();
        WitnessService wnService = new WitnessServiceImpl();
        ThirdPartyService tpService = new ThirdPartyServiceImpl();
        VehicleHireService vhService = new VehicleHireServiceImpl();
        SolicitorService slService = new SolicitorServiceImpl();
        
        if(!xmlParseResult.getIsClaimExist()){
            xmlParseResult = ctService.saveCustomerForXMLUploader(xmlParseResult);
            xmlParseResult = tpService.saveThirdPartyForXMLUploader(xmlParseResult);
            xmlParseResult = icService.saveIncidentForXMLUploader(xmlParseResult);
            xmlParseResult = wnService.saveWitnessForXMLUploader(xmlParseResult);
            xmlParseResult = ijService.saveInjuryForXMLUploader(xmlParseResult);
            xmlParseResult = slService.saveSolicitorForXMLUploader(xmlParseResult);        
        }

        xmlParseResult = erService.saveEngineerReportForXMLUploader(xmlParseResult);
        xmlParseResult = vhService.saveVehicleHireForXMLUploader(xmlParseResult);
        xmlParseResult = ivService.saveInvoiceForXMLUploader(xmlParseResult);
        xmlParseResult = csService.saveClaimForXMLUploader(xmlParseResult); 
        
        return xmlParseResult;
    }
    
    // VALIDATE SUPPLIER OR CHOORGANISATION DETAIL SECTION
    private  XMLParseResult CHOoganisationSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement) throws Exception {
        
        String strSectionName = "Claim Header";
        
        // RESET VALIDATION FLAG
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult.setIsCurrentDataValid(true);
        
        // CLAIM HEADER SECTION
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "rental-status", XmlHelper.isMAN_Status, "", strSectionName, "Hire State");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "managing-repair", XmlHelper.isMAN_Managing_Repair, XmlHelper.REG_BOOLEAN, strSectionName, "Managing Repair");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "first-contact", XmlHelper.isMAN_First_Contact, XmlHelper.REG_TIMESTAMP, strSectionName, "Policy Holder Contact");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "credit-agreement", XmlHelper.isMAN_DateTimeCreditAgreementSigned, XmlHelper.REG_TIMESTAMP, strSectionName, "Credit Agreement Signed by Insurer");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "gta-notice-date", XmlHelper.isMAN_GTANoticeDate, XmlHelper.REG_TIMESTAMP, strSectionName, "GTA 4.1 Notice Date");
        
        Claim claim = new Claim();
        
        // CHO ORGANISATION SECTION
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "supplier", strSectionName, "");
        
        if (xmlParseResult.getIsCurrentScheValid() && xmlParseResult.getIsCurrentDataValid()) {
            
            Element thisElement = XMLUtils.getElement(mainElement, "supplier");

            // RESET VALIDATION FLAG
            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);

            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "supplier-name", XmlHelper.isMAN_Supplier_Name, "", strSectionName, "Supplier Name");
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "supplier-reference", XmlHelper.isMAN_Supplier_Reference, "", strSectionName, "Supplier Reference");
            
            if (xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()) {
                
                // RENTAL STATUS
                //String strStatus = XmlHelper.getNodeValue(mainElement, claimHeaderNode_RentalStatus);
                Boolean bManagingRepair = XmlHelper.getBooleanFromNode(mainElement, "managing-repair");
                Timestamp tFirstContactDate = XmlHelper.getTimeStampFromNode(mainElement, "first-contact");
                Timestamp tCreditAgreement = XmlHelper.getTimeStampFromNode(mainElement, "credit-agreement");
                Timestamp tGtaNoticeDate = XmlHelper.getTimeStampFromNode(mainElement, "gta-notice-date");
                
                // CHO INFORMATION
                String strCHOReference = XmlHelper.getNodeValue(thisElement, "supplier-reference");

                ClaimService thisCtrl = new ClaimServiceImpl();
                
                if(thisCtrl.isClaimReferenceNumberExist(strCHOReference)){
                    
                    // CLAIM EXIST
                    xmlParseResult.setIsClaimExist(true);
                    
                    // GET EXISTING CLAIM INFORMATION
                    ClaimService claimService = new ClaimServiceImpl();
                    claim = claimService.getClaimByCHOReferenceNumber(strCHOReference);
                    
                    // LOG CLAIM CURRENT STATUS
                    xmlParseResult.setSExistingClaimStatus(claim.getStatus());
                    
                        // HARDCODE CHO BAND INFORMATION
                        ChoBand choband = new ChoBand();
                        choband.setEngineerInspectionDelayDays(0);
                        choband.setHireDayCeiling(0);
                        choband.setHireNetCeiling(new BigDecimal("0.00"));
                        choband.setHireRateChargeTolerance(new BigDecimal("0.01"));
                        choband.setInspectionDelayDays(4);
                        choband.setIsMobileDayAllowance(2);
                        choband.setIsNotMobileDayAllowance(9);
                        choband.setMaxRepairValue(new BigDecimal("0.00"));
                        choband.setOfferMadeDays(7);
                        choband.setReceiptOfFinalStatementChequeDays(10);
                        choband.setTakeVehicleOutDays(0);
                        choband.setTakeVehicleToGarageDaysMobile(1);
                        choband.setTakeVehicleToGarageDaysNonMobile(3);
                        choband.setWeekendBufferDays(0);
                        claim.setChoband(choband);
                    
                }else{
                    
                    // SET CLAIM HEADER INFORMATION
                    claim.setManagingRepair(bManagingRepair);
                    claim.setPolicyHolderContactDate(tFirstContactDate);
                    claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
                    claim.setChoReference(strCHOReference);
                    claim.setCreditAgreementDate(tCreditAgreement);
                    claim.setGtaNoticeDate(tGtaNoticeDate);
                    claim.setIndemintyAmount(new BigDecimal("0.00"));
                    claim.setPercentageLiabilityAccepted(new BigDecimal("0.00"));
                    
                    ChorganisationService chorgService = new ChorganisationServiceImpl();
                    claim.setChorganisation(chorgService.getCurrentCHOrganisation());
                }
            }
        }
        
        xmlParseResult.setClaim(claim);
        
        return xmlParseResult;
    }
    
    // VALIDATE DRIVER SECTION 
    private  XMLParseResult RentalDriversSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            Document doc) throws Exception {
        
        String strSectionName = "Customer Details";

        // VALIDATE RENTAL SECTION
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "drivers", strSectionName, "");
        
        if (xmlParseResult.getIsCurrentScheValid()) {
            
            xmlParseResult.setIsCurrentScheValid(true);
            
            // GET DRIVERS SECTION - RETURN LIST
            Element thisElement = XMLUtils.getElement(mainElement, "drivers");
            ArrayList<Element> driverElements = XMLUtils.getElements(doc, thisElement, "driver");
            
            // VALIDATE DRIVERS LIST
            xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, driverElements, "driver", strSectionName, "");
            
            if (xmlParseResult.getIsCurrentScheValid()) {
                
                // VALIDATE EVERY DRIVER
                for (Element ee : driverElements) {

                    xmlParseResult.setIsCurrentDataValid(true);
                    xmlParseResult.setIsCurrentScheValid(true);

                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "title", XmlHelper.isMAN_Driver_Title, "", strSectionName, "Title");
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "firstnames", XmlHelper.isMAN_Driver_Firstnames, "", strSectionName, "First Name");
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "lastname", XmlHelper.isMAN_Driver_Lastname, "", strSectionName, "Surname");
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address1", XmlHelper.isMAN_Driver_Address1, "", strSectionName, "Address1");
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address2", XmlHelper.isMAN_Driver_Address2, "", strSectionName, "Address2");
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address3", XmlHelper.isMAN_Driver_Address3, "", strSectionName, "Address3");
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address4", XmlHelper.isMAN_Driver_Address4, "", strSectionName, "Address4");
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address5", XmlHelper.isMAN_Driver_Address5, "", strSectionName, "Address5");
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "postcode", XmlHelper.isMAN_Driver_Postcode, "", strSectionName, "Postcode");
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "telephone-day", XmlHelper.isMAN_Driver_Telephone_day, XmlHelper.REG_PHONE, strSectionName, "Telephone Day");
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "telephone-evening", XmlHelper.isMAN_Driver_Telephone_Evening, XmlHelper.REG_PHONE, strSectionName, "Telephone Evening");
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "email", XmlHelper.isMAN_Driver_Email, XmlHelper.REG_EMAIL, strSectionName, "Email");
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "primary-driver", XmlHelper.isMAN_Driver_Primary_Driver, "", strSectionName, "Primary Driver");

                    if (xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()) {
                        
                        Customer customer = new Customer();
                        
                        if(xmlParseResult.getClaim()!=null && xmlParseResult.getClaim().getCustomer()!=null){
                            customer = xmlParseResult.getClaim().getCustomer();
                        }
                        
                        customer.setTitle(XmlHelper.getNodeValue(ee, "title"));
                        customer.setFirstName(XmlHelper.getNodeValue(ee, "firstnames"));
                        customer.setLastName(XmlHelper.getNodeValue(ee, "lastname"));
                        customer.setAddress1(XmlHelper.getNodeValue(ee, "address1"));
                        customer.setAddress2(XmlHelper.getNodeValue(ee, "address2"));
                        customer.setAddress3(XmlHelper.getNodeValue(ee, "address3"));
                        customer.setAddress4(XmlHelper.getNodeValue(ee, "address4"));
                        customer.setAddress5(XmlHelper.getNodeValue(ee, "address5"));
                        customer.setPostcode(XmlHelper.getNodeValue(ee, "postcode"));
                        customer.setTelephoneDay(XmlHelper.getNodeValue(ee, "telephone-day"));
                        customer.setTelephoneEvening(XmlHelper.getNodeValue(ee, "telephone-evening"));
                        customer.setEmail(XmlHelper.getEmailAddressFromNode(ee, "email"));
                        customer.setIsPrimaryDriver(true);
                        
                        xmlParseResult.getClaim().setCustomer(customer);
                        
                    }
                }
            }
        }
        return xmlParseResult;
    }
    
    // VALIDATE CLAIM SECTION 
    private  XMLParseResult RentalClaimSchemaValidation(
            XMLParseResult xmlParseResult,
            Element root,
            Document doc) throws Exception {

        // RESET FLAG VALIDATION
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, root, "claim", "Claim Details", "");
        
        // VALIDATE CLAIM HEADER SECTION
        if (xmlParseResult.getIsCurrentScheValid()) {
            Element claimNodeElement = XMLUtils.getElement(root, "claim");
            xmlParseResult = ClaimDetail_CustomerSchemaValidation(xmlParseResult, claimNodeElement, "Customer Details");
            xmlParseResult = ClaimDetail_ThirdPartySchemaValidation(xmlParseResult, claimNodeElement, "Third Party Details");
            xmlParseResult = ClaimDetail_IncidentSchemaValidation(xmlParseResult, claimNodeElement, doc, "Incident Details");
        }
        
        return xmlParseResult;
    }

    private  XMLParseResult ClaimDetail_CustomerSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            String strSectionName) throws Exception {

        // String mainNodeName = "customer";
        // String nodeName1 = "insurer";
        // String nodeName2 = "vehicle";
        // String childNodeLabelMain = XmlHelper.contructureErrorMessage(parentNodeName, mainNodeName);
        // String childNodeLabel1 = XmlHelper.contructureErrorMessage(childNodeLabelMain, nodeName1);
        // String childNodeLabel2 = XmlHelper.contructureErrorMessage(childNodeLabelMain, nodeName2);

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
            //xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "total-loss", XmlHelper.isMAN_Claim_Customer_Vehicle_TotalLoss, XmlHelper.REG_TIMESTAMP, "Customer Vehicle Damage", "Initial ECD");
            
            if (xmlParseResult.getIsCurrentScheValid() && xmlParseResult.getIsCurrentDataValid()) {
                
                Customer customer = new Customer();

                if(xmlParseResult.getClaim()!=null && xmlParseResult.getClaim().getCustomer()!=null){
                    customer = xmlParseResult.getClaim().getCustomer();
                }
                
                // GET INSURER INFORMATION
                String insurerAlliasName = XmlHelper.getNodeValue(thisElement, "name");
                InsurerAlliasService thisISCtrl = new InsurerAlliasServiceImpl();
                InsurerAllias insurerallias = thisISCtrl.getInsurerByAlliasName(insurerAlliasName);

                if(insurerallias!=null){
                    if(insurerallias.getInsurer()!=null){
                        xmlParseResult.getClaim().setInsurer(insurerallias.getInsurer());
                        customer.setInsurerId((insurerallias.getInsurer()).getId());
                    }
                }else{
                    if(XmlHelper.isMAN_Claim_Customer_Insurer_name){
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, "Selected Insurer is invalid", false);
                    }
                }
                
                // GET VEHICLE CLASS ID
                VehicleClass vehicleclass = vehicleClassService.getVehicleClassByNodeName(thisElement, "vehicle-class");
                if(vehicleclass!=null){
                    customer.setVehicleClass(vehicleclass);
                }else{
                    if(XmlHelper.isMAN_Claim_Customer_Vehicle_Class){
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, "Selected Vehicle Class is invalid", false);
                    }
                }

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

    private  XMLParseResult ClaimDetail_ThirdPartySchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            String strSectionName) throws Exception {
            
        /*
        String mainNodeName = "third-party";
        String nodeName1 = "insurer";
        String nodeName2 = "vehicle";
        String nodeName3 = "driver";

        String childNodeLabelMain = XmlHelper.contructureErrorMessage(parentNodeName, mainNodeName);
        String childNodeLabel1 = XmlHelper.contructureErrorMessage(childNodeLabelMain, nodeName1);
        String childNodeLabel2 = XmlHelper.contructureErrorMessage(childNodeLabelMain, nodeName2);
        String childNodeLabel3 = XmlHelper.contructureErrorMessage(childNodeLabelMain, nodeName3);
        */
        
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
                InsurerAlliasService thisISCtrl = new InsurerAlliasServiceImpl();
                InsurerAllias insurerallias = thisISCtrl.getInsurerByAlliasName(insurerAlliasName);

                if(insurerallias!=null){
                    if(insurerallias.getInsurer()!=null){
                        thirdparty.setInsurer(insurerallias.getInsurer());
                    }                
                }else{
                    if(XmlHelper.isMAN_Claim_ThirdParty_Insurer_Name){
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, "Selected Insurer is invalid", false);
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

    private  XMLParseResult ClaimDetail_IncidentSchemaValidation(
        XMLParseResult xmlParseResult,
        Element mainElement,
        Document doc,
        String strSectionName) throws Exception {

        // String mainNodeName = "incident";
        // String childNodeLabelMain = XmlHelper.contructureErrorMessage(parentNodeName, mainNodeName);

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
                xmlParseResult = ClaimDetail_IncidentWitnessSchemaValidation(xmlParseResult, thisElement, doc, "Witness");
                xmlParseResult = ClaimDetail_IncidentInjuriesSchemaValidation(xmlParseResult, thisElement, doc, "Injury");
            }
        }
        
        return xmlParseResult;
    }
    
    private  XMLParseResult ClaimDetail_IncidentWitnessSchemaValidation(
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
    
    private  XMLParseResult ClaimDetail_IncidentInjuriesSchemaValidation(
        XMLParseResult xmlParseResult,
        Element mainElement,
        Document doc,
        String strSectionName) throws Exception {
        
        
        // String nodeName1 = "injuries";
        // String nodeName2 = "injury";
        // String subNodeName = "solicitor";
        // CONSTRUCT NODE ERROR MESSAGE
        // String childNodeLabel1 = XmlHelper.contructureErrorMessage(parentNodeName, nodeName1);
        // String childNodeLabel2 = XmlHelper.contructureErrorMessage(childNodeLabel1, nodeName2);
        
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
    
    // VALIDATE REPAIR SECTION 
    private  XMLParseResult RentalRepairSchemaValidation(
        XMLParseResult xmlParseResult,
        Element mainElement,
        Document doc) throws Exception {

        String strSectionName = "Engineer Report";
        
        /*
        String parentNodeName = "rental";
        String nodeName1 = "repair";
        String nodeName2 = "engineer-report";
        // CONSTRUCT ERROR MESSAGE
        String childNodeLabelMain = XmlHelper.contructureErrorMessage(parentNodeName, "");
        String childNodeLabel1 = XmlHelper.contructureErrorMessage(childNodeLabelMain, nodeName1);
        String childNodeLabel2 = XmlHelper.contructureErrorMessage(childNodeLabel1, nodeName2);
        */
        
        // RESET VALIDATION FLAG
        xmlParseResult.setIsCurrentScheValid(true);
        
        Element repairElement = XMLUtils.getElement(mainElement, "repair");
        Element eReportElement = XMLUtils.getElement(mainElement, "engineer-report");
        
        if(repairElement!=null && eReportElement!=null){
            
            // VALIDATE REPAIR AND ENGINEER REPORT
            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "repair", strSectionName, "");
            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "engineer-report", strSectionName, "");
        
            if (xmlParseResult.getIsCurrentScheValid()) {
                
                // GET AND VALIDATE ENGINEER REPROT
                ArrayList<Element> engineerReportElements = XMLUtils.getElements(doc, mainElement, "engineer-report");
                xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, engineerReportElements, "engineer-report", strSectionName, "");
                
                if (xmlParseResult.getIsCurrentScheValid()) {
                    
                    for (Element ee : engineerReportElements) {
                    
                        xmlParseResult.setIsCurrentDataValid(true);
                        xmlParseResult.setIsCurrentScheValid(true);

                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "labour-amount", XmlHelper.isMAN_Repair_engineerReport_labour_Amount, XmlHelper.REG_BIGDECIMAL, strSectionName, "Estimated Labour Amount");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "total-amount", XmlHelper.isMAN_Repair_engineerReport_total_Amount, XmlHelper.REG_BIGDECIMAL, strSectionName, "Estimated Total Repair Amount");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "days", XmlHelper.isMAN_Repair_engineerReport_days, XmlHelper.REG_INTEGER, strSectionName, "Estimated Days Under Repair");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "name", XmlHelper.isMAN_Repair_engineerReport_name, "", strSectionName, "Engineer Name");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "company", XmlHelper.isMAN_Repair_engineerReport_company, "", strSectionName, "Engineer Company");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address1", XmlHelper.isMAN_Repair_engineerReport_address1, "", strSectionName, "Engineer Address1");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address2", XmlHelper.isMAN_Repair_engineerReport_address2, "", strSectionName, "Engineer Address2");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address3", XmlHelper.isMAN_Repair_engineerReport_address3, "", strSectionName, "Engineer Address3");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address4", XmlHelper.isMAN_Repair_engineerReport_address4, "", strSectionName, "Engineer Address4");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address5", XmlHelper.isMAN_Repair_engineerReport_address5, "", strSectionName, "Engineer Address5");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "postcode", XmlHelper.isMAN_Repair_engineerReport_postcode, "", strSectionName, "Engineer Postcode");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "telephone", XmlHelper.isMAN_Repair_engineerReport_telephone, XmlHelper.REG_PHONE, strSectionName, "Engineer Telephone");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "email", XmlHelper.isMAN_Repair_engineerReport_email, XmlHelper.REG_EMAIL, strSectionName, "Engineer Email");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "usable", XmlHelper.isMAN_Repair_engineerReport_usable, "", strSectionName, "Usable");

                        if ((xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid())
                            && (XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "labour-amount"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "total-amount"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "days"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "name"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "company"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "address1"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "address2"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "address3"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "address4"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "address5"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "postcode"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "telephone"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "email"))
                            || XmlHelper.isNotNull(XmlHelper.getNodeValue(ee, "usable"))
                            )
                        ) {
                            
                            EngineerReport engineerReport = new EngineerReport();
                            
                            if(xmlParseResult.getIsClaimExist() && xmlParseResult.getClaim().getEngineerReport()!=null){
                                engineerReport = xmlParseResult.getClaim().getEngineerReport();
                            }
                            
                            engineerReport.setDays(XmlHelper.getIntegerFromNode(ee, "days"));
                            engineerReport.setLabourAmount(XmlHelper.getBigDecimalFromNode(ee, "labour-amount"));
                            engineerReport.setTotalAmount(XmlHelper.getBigDecimalFromNode(ee, "total-amount"));
                            
                            engineerReport.setName(XmlHelper.getNodeValue(ee, "name"));
                            engineerReport.setCompany(XmlHelper.getNodeValue(ee, "company"));
                            engineerReport.setAddress1(XmlHelper.getNodeValue(ee, "address1"));
                            engineerReport.setAddress2(XmlHelper.getNodeValue(ee, "address2"));
                            engineerReport.setAddress3(XmlHelper.getNodeValue(ee, "address3"));
                            engineerReport.setAddress4(XmlHelper.getNodeValue(ee, "address4"));
                            engineerReport.setAddress5(XmlHelper.getNodeValue(ee, "address5"));
                            engineerReport.setPostcode(XmlHelper.getNodeValue(ee, "postcode"));
                            engineerReport.setTelephone(XmlHelper.getNodeValue(ee, "telephone"));
                            engineerReport.setEmail(XmlHelper.getEmailAddressFromNode(ee, "email"));
                            engineerReport.setIsUsable(XmlHelper.getBooleanFromNode(ee, "usable"));
                            
                            xmlParseResult.getClaim().setEngineerReport(engineerReport);
                            break;
                        }
                    }
                }
            }
        }
        return xmlParseResult;
    }
    
    // VALIDATE INVOICE SECTION 
    private  XMLParseResult RentalInvoiceSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            Document doc) throws Exception {
            
        String strSectionName = "Invoice";
        
        // String parentNodeName = "rental";
        // String nodeName1 = "invoice";
        // CONSTRUCT NODE LOCATION ERROR MESSAGE
        // String childNodeLabelMain = XmlHelper.contructureErrorMessage(parentNodeName, "");
        // String childNodeLabel1 = XmlHelper.contructureErrorMessage(childNodeLabelMain, nodeName1);
        
        // RESET VALIDATION FLAG AND VALIDATE MAIN INVOICE NODE SECTION
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "invoice", strSectionName, "");
        
        if (xmlParseResult.getIsCurrentScheValid()) {
            Element thisElement = XMLUtils.getElement(mainElement, "invoice");
            xmlParseResult = RentalInvoiceDetailSchemaValidation(xmlParseResult, thisElement, doc, strSectionName);
        }
        return xmlParseResult;
    }
    
    // VALIDATE INVOICE DETAIL SECTION
    private  XMLParseResult RentalInvoiceDetailSchemaValidation(
            XMLParseResult xmlParseResult,
            Element thisElement,
            Document doc,
            String strSectionName) throws Exception {
            
        /*
        String subNodeName0 = "vehicles";
        String subNodeName1 = "extras";
        String subNodeName2 = "repair";
        String subNodeName3 = "storage-recovery";
        String subNodeName4 = "engineer-fee";
        String subNodeName5 = "supplier";
        */
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
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
        
        if (xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()) {
        
            /*
            String childNodeLabel0 = XmlHelper.contructureErrorMessage(strSectionName, subNodeName0);
            String childNodeLabel1 = XmlHelper.contructureErrorMessage(strSectionName, subNodeName1);
            String childNodeLabel2 = XmlHelper.contructureErrorMessage(strSectionName, subNodeName2);
            String childNodeLabel3 = XmlHelper.contructureErrorMessage(strSectionName, subNodeName3);
            String childNodeLabel4 = XmlHelper.contructureErrorMessage(strSectionName, subNodeName4);
            String childNodeLabel5 = XmlHelper.contructureErrorMessage(strSectionName, subNodeName5);
            */
            
            Invoice invoice = new Invoice();
            invoice.setTotalGross(XmlHelper.getBigDecimalFromNode(thisElement, "gross"));
            invoice.setTotalNet(XmlHelper.getBigDecimalFromNode(thisElement, "net"));
            invoice.setTotalVat(XmlHelper.getBigDecimalFromNode(thisElement, "vat"));
            invoice.setTotalToPay(XmlHelper.getBigDecimalFromNode(thisElement, "total-to-pay"));
            invoice.setDiscount(XmlHelper.getBigDecimalFromNode(thisElement, "less-discount"));
            invoice.setDeductionForClaimsHandlingFee(XmlHelper.getBigDecimalFromNode(thisElement, "less-handling-fee"));
            invoice.setDateInvoiced(XmlHelper.getTimeStampFromNode(thisElement, "date-invoiced"));
            
            xmlParseResult.getClaim().setInvoice(invoice);

            xmlParseResult = InvoiceDetail_VehiclesValidSchemaValidation(xmlParseResult, thisElement, strSectionName, "vehicles");
            xmlParseResult = InvoiceDetail_ExtrasValidSchemaValidation(xmlParseResult, thisElement, doc, "Extras", "extras");
            xmlParseResult = InvoiceDetail_RepairValidSchemaValidation(xmlParseResult, thisElement, strSectionName, "repair");
            xmlParseResult = InvoiceDetail_StorageRecoveryValidSchemaValidation(xmlParseResult, thisElement, strSectionName, "storage-recovery");
            xmlParseResult = InvoiceDetail_EngineerFeeValidSchemaValidation(xmlParseResult, thisElement, strSectionName, "engineer-fee");
            xmlParseResult = InvoiceDetail_SupplierValidSchemaValidation(xmlParseResult, thisElement, strSectionName, "supplier");
            
        }

        return xmlParseResult;
    }
    
    // VALIDATE INVOICE DETAIL - Vehicles SECTION
    private  XMLParseResult InvoiceDetail_VehiclesValidSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            String strSectionName,
            String thisNodeName) {

        Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);

        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);

        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Vehicles_Net, XmlHelper.REG_BIGDECIMAL, strSectionName, "Hire Net");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Vehicles_Vat, XmlHelper.REG_BIGDECIMAL, strSectionName, "Hire VAT");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Vehicles_Gross, XmlHelper.REG_BIGDECIMAL, strSectionName, "Hire Gross");

        if (xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()) {
            xmlParseResult.getClaim().getInvoice().setHireGross(XmlHelper.getBigDecimalFromNode(thisElement, "gross"));
            xmlParseResult.getClaim().getInvoice().setHireNet(XmlHelper.getBigDecimalFromNode(thisElement, "net"));
            xmlParseResult.getClaim().getInvoice().setHireVat(XmlHelper.getBigDecimalFromNode(thisElement, "vat"));
        }
        
        return xmlParseResult;
    }

    // VALIDATE INVOICE DETAIL - repair SECTION
    private  XMLParseResult InvoiceDetail_RepairValidSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            String strSectionName,
            String thisNodeName) {

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
        return xmlParseResult;
    }

    // VALIDATE INVOICE DETAIL - Supplier SECTION
    private  XMLParseResult InvoiceDetail_SupplierValidSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            String strSectionName,
            String thisNodeName) {

        Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);

        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);

        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "handling-invoice-no", XmlHelper.isMAN_Invoice_Supplier_HandlingInvoiceNo, "", strSectionName, "Supplier Claims Handling Invoice Number");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "handling-invoice-amount", XmlHelper.isMAN_Invoice_Supplier_HandlingInvoiceAmount, XmlHelper.REG_BIGDECIMAL, strSectionName, "Claims Handling Invoice Amount");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "claim-invoice-no", XmlHelper.isMAN_Invoice_Supplier_ClaimInvoiceNo, "", strSectionName, "Supplier Claim Invoice Number");

        if (xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()) {
            xmlParseResult.getClaim().getInvoice().setHandlingInvoiceNo(XmlHelper.getNodeValue(thisElement, "handling-invoice-no"));
            xmlParseResult.getClaim().getInvoice().setClaimsHandlingInvoiceAmount(XmlHelper.getBigDecimalFromNode(thisElement, "handling-invoice-amount"));
            xmlParseResult.getClaim().getInvoice().setClaimInvoiceNo(XmlHelper.getNodeValue(thisElement, "claim-invoice-no"));
        }
        return xmlParseResult;
    }
    
    // VALIDATE INVOICE DETAIL - Engineer Fee SECTION
    private  XMLParseResult InvoiceDetail_EngineerFeeValidSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            String strSectionName,
            String thisNodeName){

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
        return xmlParseResult;
    }

    // VALIDATE INVOICE DETAIL - Storage Recovery SECTION
    private  XMLParseResult InvoiceDetail_StorageRecoveryValidSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            String strSectionName,
            String thisNodeName) {

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

        return xmlParseResult;
    }
    
    // VALIDATE INVOICE DETAIL - Storage Recovery SECTION
    private  XMLParseResult InvoiceDetail_ExtrasValidSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            Document doc,
            String strSectionName,
            String thisNodeName) throws Exception {

        Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);

        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);

        ArrayList<Element> extraElements = XMLUtils.getElements(doc, thisElement, "extra");
        xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, extraElements, "extra", strSectionName, "");

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
        return xmlParseResult;
    }
    
    // VALIDATE INVOICE SECTION 
    private  XMLParseResult RentalVehiclesSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            Document doc) throws Exception {
        
        // String strSectionName = "rental";
        // String mainNodeName = "rental-vehicles";
        // String childNodeName = "rental-vehicle";
        // String childNodeLabelMain = XmlHelper.contructureErrorMessage(parentNodeName, "");
        // String childNodeLabel1 = XmlHelper.contructureErrorMessage(childNodeLabelMain, mainNodeName);
        // String childNodeLabel2 = XmlHelper.contructureErrorMessage(childNodeLabel1, childNodeName);
        
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "rental-vehicles", "Hire Vehicle Details", "");

        if (xmlParseResult.getIsCurrentScheValid()) {
            
            Element thisElement = XMLUtils.getElement(mainElement, "rental-vehicles");
            ArrayList<Element> rentalVehicleElements = XMLUtils.getElements(doc, thisElement, "rental-vehicle");
            xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, rentalVehicleElements, "rental-vehicle", "Hire Vehicle Details", "");

            if (xmlParseResult.getIsCurrentScheValid()) {
                
                for (Element ee : rentalVehicleElements) {
                    xmlParseResult = RentalVehiclesDetailSchemaValidation(xmlParseResult, ee, doc, "Hire Vehicle Details");
                    break;
                }
            }
        }
        
        return xmlParseResult;
    }

    private  XMLParseResult RentalVehiclesDetailSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            Document doc,
            String strSectionName) throws Exception {
            
        
        VehicleHire vehiclehire = new VehicleHire();
        
        if(xmlParseResult.getIsClaimExist() && xmlParseResult.getClaim().getVehicleHire()!=null){
            vehiclehire = xmlParseResult.getClaim().getVehicleHire();
        }
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "vehicle-registration", XmlHelper.isMAN_RentalVehicles_Vehicle_Registration, XmlHelper.REG_VEHICLE_REG, strSectionName, "Registration");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "vehicle-manufacturer", XmlHelper.isMAN_RentalVehicles_Vehicle_Manufacturer, "", strSectionName, "Manufacturer");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "vehicle-model", XmlHelper.isMAN_RentalVehicles_Vehicle_Model, "", strSectionName, "Model");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "vehicle-class", XmlHelper.isMAN_RentalVehicles_Vehicle_Class, "", strSectionName, "Replacement Vehicle Class");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "rental-start", XmlHelper.isMAN_RentalVehicles_Rental_Start, XmlHelper.REG_TIMESTAMP, strSectionName, "Hire Start");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "rental-end", XmlHelper.isMAN_RentalVehicles_Rental_End, XmlHelper.REG_TIMESTAMP, strSectionName, "Hire End");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "rental-days", XmlHelper.isMAN_RentalVehicles_Rental_Days, XmlHelper.REG_INTEGER, strSectionName, "Number Days Hire");
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "collection-reason", XmlHelper.isMAN_RentalVehicles_CollectionReason, "", strSectionName, "Reason For Collection");

        if ((xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid())
        && (XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "vehicle-registration"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "vehicle-manufacturer"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "vehicle-model"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "vehicle-class"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "rental-start"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "rental-end"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "rental-days"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "collection-reason"))
        )){
            
            // GET VEHICLE CLASS ID
            if(XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "vehicle-class"))){
                VehicleClass vehicleclass = vehicleClassService.getVehicleClassByNodeName(mainElement, "vehicle-class");

                if(vehicleclass!=null){
                    vehiclehire.setVehicleClass(vehicleclass);
                }else{
                    xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, "Selected Vehicle Class is invalid", false);
                }
            }
            
            vehiclehire.setVehicleRegistration(TextHelper.trimWhiteSpace(XmlHelper.getNodeValue(mainElement, "vehicle-registration")));
            vehiclehire.setVehicleManufacturer(XmlHelper.getNodeValue(mainElement, "vehicle-manufacturer"));
            vehiclehire.setVehicleModel(XmlHelper.getNodeValue(mainElement, "vehicle-model"));
            vehiclehire.setRentalStart(XmlHelper.getTimeStampFromNode(mainElement, "rental-start"));
            vehiclehire.setRentalEnd(XmlHelper.getTimeStampFromNode(mainElement, "rental-end"));
            vehiclehire.setDays(XmlHelper.getIntegerFromNode(mainElement, "rental-days"));
            vehiclehire.setCollectionReason(XmlHelper.getNodeValue(mainElement, "collection-reason"));
        
            // PART 2 : EXTRA SECTION
            String nodeName1 = "extras";
            String nodeName2 = "extra";

            // String childNodeLabel1 = XmlHelper.contructureErrorMessage(parentNodePath, nodeName1);
            // String childNodeLabel2 = XmlHelper.contructureErrorMessage(childNodeLabel1, nodeName2);

            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);

            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, strSectionName, "");

            if (xmlParseResult.getIsCurrentScheValid()) {

                xmlParseResult.setIsCurrentScheValid(true);
                
                // GET <EXTRAS></EXTRAS> ELEMENT
                Element thisElement = XMLUtils.getElement(mainElement, nodeName1);
                xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, nodeName2, strSectionName, "");
                
                if (xmlParseResult.getIsCurrentScheValid()) {
                    
                    // GET <EXTRA></EXTRA> ELEMENT
                    ArrayList<Element> extraElements = XMLUtils.getElements(doc, thisElement, nodeName2);
                    xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, extraElements, nodeName2, strSectionName, "");

                    if (xmlParseResult.getIsCurrentScheValid()) {
                        
                        for (Element ee : extraElements) {
                            
                            String selectedExtra = ee.getTextContent();
                            
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, nodeName2, XmlHelper.isMAN_RentalVehicles_Extras_Extra, "", strSectionName, selectedExtra);

                            if(selectedExtra.equalsIgnoreCase("CDW")){
                                vehiclehire.setCdwFee(true);
                            }else if(selectedExtra.equalsIgnoreCase("Admin")){
                                vehiclehire.setAdminFee(true);
                            }else if(selectedExtra.equalsIgnoreCase("Automatic")){
                                vehiclehire.setAutomaticFee(true);
                            }else if(selectedExtra.equalsIgnoreCase("Baby Seat")){
                                vehiclehire.setBabySeatFee(true);
                            }else if(selectedExtra.equalsIgnoreCase("Delivery Collection")){
                                vehiclehire.setDeliveryCollectionFee(true);
                            }else if(selectedExtra.equalsIgnoreCase("Dual Control")){
                                vehiclehire.setDualControlFee(true);
                            }else if(selectedExtra.equalsIgnoreCase("Estate")){
                                vehiclehire.setEstateFee(true);
                            }else if(selectedExtra.equalsIgnoreCase("Non-standard Risk Insurance Premium")){
                                vehiclehire.setNonStandardInsurancePremiumFee(true);
                            }else if(selectedExtra.equalsIgnoreCase("Roof Rack")){
                                vehiclehire.setRoofRackFee(true);
                            }else if(selectedExtra.equalsIgnoreCase("Sat Nav")){
                                vehiclehire.setSatNavFee(true);
                            }else if(selectedExtra.equalsIgnoreCase("Tow Bars")){
                                vehiclehire.setTowBarsFee(true);
                            }
                        }
                        xmlParseResult.getClaim().setVehicleHire(vehiclehire);
                    }
                }
            }
        }
        
        return xmlParseResult;
    }
}