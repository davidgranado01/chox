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
import org.hibernate.Session;
import chox.data.HibernateUtil;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

public class XmlProcessController {
    
    VehicleClassService vehicleClassService;
    
    public XmlProcessController()
    {
        vehicleClassService = new VehicleClassServiceImpl();
    }

    public static void main(String[] args) {

        try {
            
            // TEST CLAIM
            String sXMLPath1 = "C:/Users/Carlson/Desktop/TESTCASEFILE/TEST_CASE_CLAIM1.xml";
            String sUpdateType = "C";
            Boolean isAllowPartialUpload = true;

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(sXMLPath1));

            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();
            
            // TEST INVOICE            
            //String sXMLPath2 = "C:/Project Workplace/Greefinch/choxida/CHOX_STAGE_2_SUBMISSION.xml";
            String sXMLPath2 = "C:/Users/Carlson/Desktop/CHOX/Bord Test 9.xml";
            sUpdateType = "A";
            doc = docBuilder.parse(new File(sXMLPath2));

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
                        xmlParseResult = thisCtrl.xmlSchemaValidateProcess(xmlParseResult, doc, re, sUpdateType, isAllowPartialUpload);
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
    
    public ArrayList<XMLParseResult> XMLValidationProcess(File claimXMLFile, String sUpdateType, Boolean isAllowPartialUpload) {

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
                        xmlParseResult = xmlSchemaValidateProcess(xmlParseResult, doc, re, sUpdateType, isAllowPartialUpload);
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
    
    public XMLParseResult xmlSchemaValidateProcess(
            XMLParseResult xmlParseResult,
            Document doc,
            Element root,
            String sUploadType,
            Boolean isAllowPartialUpload) throws Exception {
                
        Session currentSession = SessionFactoryUtils.getSession(HibernateUtil.getSessionFactory(), true);
        currentSession.beginTransaction();
        
        xmlParseResult.setCurrentSession(currentSession);
        
        // VALIDATE AND GET RECORD FOR CLAIM OBJECT
        xmlParseResult = CHOoganisationSchemaValidation(xmlParseResult, root);
        xmlParseResult = RentalDriversSchemaValidation(xmlParseResult, root, doc);
        xmlParseResult = RentalClaimSchemaValidation(currentSession, xmlParseResult, root, doc);
        xmlParseResult = RentalRepairSchemaValidation(currentSession, xmlParseResult, root, doc);
        xmlParseResult = RentalVehiclesSchemaValidation(currentSession, xmlParseResult, root, doc);
        xmlParseResult = RentalInvoiceSchemaValidation(currentSession, xmlParseResult, root, doc);
        
        
        if(xmlParseResult.getIsSchemaValid() && xmlParseResult.getIsDataValid()){
            xmlParseResult = saveXMLRecord(xmlParseResult, sUploadType);
        }

        //currentSession = xmlParseResult.getCurrentSession();
        
        System.out.println("");
        System.out.println(" ** CHO Reference: " + xmlParseResult.getClaim().getChoReference());
        System.out.println(" ** getIsSchemaValid: " + xmlParseResult.getIsSchemaValid());
        System.out.println(" ** getSchemaValidationRemark: " + xmlParseResult.getSchemaValidationRemark());
        System.out.println(" ** getIsDataValid: " + xmlParseResult.getIsDataValid());
        System.out.println(" ** getDataValidationRemark: " + xmlParseResult.getDataValidationRemark());
        System.out.println("********************************************");
        
        if(xmlParseResult.getIsSchemaValid() && xmlParseResult.getIsDataValid()){
            currentSession.getTransaction().commit();
        }else{
            currentSession.getTransaction().rollback();
        }
        
        return xmlParseResult;
    }
    
    private  XMLParseResult saveXMLRecord(XMLParseResult xmlParseResult, String sUploadType){

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
        
        //if(sUploadType.equalsIgnoreCase("C")){
            xmlParseResult = ctService.saveCustomerForXMLUploader(xmlParseResult);
            
            xmlParseResult = tpService.saveThirdPartyForXMLUploader(xmlParseResult);
            xmlParseResult = icService.saveIncidentForXMLUploader(xmlParseResult);
            xmlParseResult = wnService.saveWitnessForXMLUploader(xmlParseResult);
            xmlParseResult = ijService.saveInjuryForXMLUploader(xmlParseResult);
            xmlParseResult = slService.saveSolicitorForXMLUploader(xmlParseResult);        
        //}
        
        xmlParseResult = erService.saveEngineerReportForXMLUploader(xmlParseResult);
        xmlParseResult = ivService.saveInvoiceForXMLUploader(xmlParseResult);
        xmlParseResult = vhService.saveVehicleHireForXMLUploader(xmlParseResult);
        xmlParseResult = csService.saveClaimForXMLUploader(xmlParseResult); 
        
        return xmlParseResult;
    }
    
    // VALIDATE SUPPLIER OR CHOORGANISATION DETAIL SECTION
    private  XMLParseResult CHOoganisationSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement) throws Exception {

        String claimHeaderNodeName = "Claim Header";
        String claimHeaderNode_RentalStatus = "rental-status";
        String claimHeaderNode_ManagiRepair = "managing-repair";
        String claimHeaderNode_FirstCOntact = "first-contact";
        String supplierNodeName = "supplier";
        String supplierNode_Name = "supplier-name";
        String supplierNode__Ref = "supplier-reference";

        // CONSTRUCT ERROR MESSAGE FORMAT
        String childNodeLabelMain = XmlHelper.contructureErrorMessage(claimHeaderNodeName, "");
        String childNodeLabel1 = XmlHelper.contructureErrorMessage(childNodeLabelMain, supplierNodeName);
        
        // RESET VALIDATION FLAG
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult.setIsCurrentDataValid(true);
        
        // CLAIM HEADER SECTION
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, claimHeaderNode_RentalStatus, XmlHelper.isMAN_Status, "", childNodeLabelMain);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, claimHeaderNode_ManagiRepair, XmlHelper.isMAN_Managing_Repair, "", childNodeLabelMain);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, claimHeaderNode_FirstCOntact, XmlHelper.isMAN_First_Contact, XmlHelper.REG_TIMESTAMP, childNodeLabelMain);
        
        // CHO ORGANISATION SECTION
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, supplierNodeName, childNodeLabelMain);
        
        if (xmlParseResult.getIsCurrentScheValid() && xmlParseResult.getIsCurrentDataValid()) {
            
            Element thisElement = XMLUtils.getElement(mainElement, supplierNodeName);

            // RESET VALIDATION FLAG
            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);

            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, supplierNode_Name, XmlHelper.isMAN_Supplier_Name, "", childNodeLabel1);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, supplierNode__Ref, XmlHelper.isMAN_Supplier_Reference, "", childNodeLabel1);

            if (xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()) {
                
                // RENTAL STATUS
                String strStatus = XmlHelper.getNodeValue(mainElement, claimHeaderNode_RentalStatus);
                Boolean bManagingRepair = XmlHelper.getBooleanFromNode(mainElement, claimHeaderNode_ManagiRepair);
                Timestamp tFirstContactDate = XmlHelper.getTimeStampFromNode(mainElement, claimHeaderNode_FirstCOntact);
                
                // CHO INFORMATION
                String strCHOReference = XmlHelper.getNodeValue(thisElement, supplierNode__Ref);

                Claim claim = new Claim();
                
                ClaimService thisCtrl = new ClaimServiceImpl();
                
                if(thisCtrl.isClaimReferenceNumberExist(strCHOReference)){
                    
                    System.out.println("");
                    ClaimService claimService = new ClaimServiceImpl();
                    claim = claimService.getClaimByCHOReferenceNumber(strCHOReference);

                }else{
                    
                    if(!strStatus.equalsIgnoreCase(XMLParseResult.IN_PROGRESS)){
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, "Incorrect Status", false);
                    }
                    
                    // SET CLAIM HEADER INFORMATION
                    claim.setManagingRepair(bManagingRepair);
                    claim.setPolicyHolderContactDate(tFirstContactDate);
                    claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED);
                    claim.setChoReference(strCHOReference);
                    
                    ChorganisationService chorgService = new ChorganisationServiceImpl();
                    claim.setChorganisation(chorgService.getCurrentCHOrganisation());
                }
                
                xmlParseResult.setClaim(claim);
            }
        }
        
        return xmlParseResult;
    }
    
    // VALIDATE DRIVER SECTION 
    private  XMLParseResult RentalDriversSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            Document doc) throws Exception {

        String parentNodeName = "rental";
        String mainNodeName = "drivers";
        String nodeName1 = "driver";

        // CONSTRUCT ERROR MESSAGE
        String childNodeLabelMain = XmlHelper.contructureErrorMessage(parentNodeName, "");
        String childNodeLabel1 = XmlHelper.contructureErrorMessage(childNodeLabelMain, mainNodeName);
        String childNodeLabel2 = XmlHelper.contructureErrorMessage(childNodeLabel1, nodeName1);
        
        // VALIDATE RENTAL SECTION
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, childNodeLabelMain);
        
        if (xmlParseResult.getIsCurrentScheValid()) {
            
            xmlParseResult.setIsCurrentScheValid(true);
            
            // GET DRIVERS SECTION - RETURN LIST
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            ArrayList<Element> driverElements = XMLUtils.getElements(doc, thisElement, nodeName1);
            
            // VALIDATE DRIVERS LIST
            xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, driverElements, nodeName1, childNodeLabel1);
            
            if (xmlParseResult.getIsCurrentScheValid()) {
                
                // VALIDATE EVERY DRIVER
                for (Element ee : driverElements) {

                    xmlParseResult.setIsCurrentDataValid(true);
                    xmlParseResult.setIsCurrentScheValid(true);

                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "title", XmlHelper.isMAN_Driver_Title, "", childNodeLabel2);
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "firstnames", XmlHelper.isMAN_Driver_Firstnames, "", childNodeLabel2);
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "lastname", XmlHelper.isMAN_Driver_Lastname, "", childNodeLabel2);
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address1", XmlHelper.isMAN_Driver_Address1, "", childNodeLabel2);
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address2", XmlHelper.isMAN_Driver_Address2, "", childNodeLabel2);
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address3", XmlHelper.isMAN_Driver_Address3, "", childNodeLabel2);
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address4", XmlHelper.isMAN_Driver_Address4, "", childNodeLabel2);
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address5", XmlHelper.isMAN_Driver_Address5, "", childNodeLabel2);
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "postcode", XmlHelper.isMAN_Driver_Postcode, "", childNodeLabel2);
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "telephone-day", XmlHelper.isMAN_Driver_Telephone_day, "", childNodeLabel2);
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "telephone-evening", XmlHelper.isMAN_Driver_Telephone_Evening, "", childNodeLabel2);
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "email", XmlHelper.isMAN_Driver_Email, XmlHelper.REG_EMAIL, childNodeLabel2);
                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "primary-driver", XmlHelper.isMAN_Driver_Primary_Driver, "", childNodeLabel2);

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
            Session currentSession,
            XMLParseResult xmlParseResult,
            Element root,
            Document doc) throws Exception {

        String parentNodeName = "rental";
        String nodeName = "claim";
        
        // CONSTRUCTE CLAIM ERROR MESSAGE
        String childNodeLabelMain = XmlHelper.contructureErrorMessage(parentNodeName, nodeName);
        
        // RESET FLAG VALIDATION
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, root, nodeName, childNodeLabelMain);
        
        // VALIDATE CLAIM HEADER SECTION
        if (xmlParseResult.getIsCurrentScheValid()) {
            Element claimNodeElement = XMLUtils.getElement(root, nodeName);
            xmlParseResult = ClaimDetail_CustomerSchemaValidation(xmlParseResult, claimNodeElement, childNodeLabelMain);
            xmlParseResult = ClaimDetail_ThirdPartySchemaValidation(xmlParseResult, claimNodeElement, childNodeLabelMain);
            xmlParseResult = ClaimDetail_IncidentSchemaValidation(xmlParseResult, claimNodeElement, doc, childNodeLabelMain);
        }

        return xmlParseResult;
    }

    private  XMLParseResult ClaimDetail_CustomerSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            String parentNodeName) throws Exception {

        String mainNodeName = "customer";
        String nodeName1 = "insurer";
        String nodeName2 = "vehicle";

        String childNodeLabelMain = XmlHelper.contructureErrorMessage(parentNodeName, mainNodeName);
        String childNodeLabel1 = XmlHelper.contructureErrorMessage(childNodeLabelMain, nodeName1);
        String childNodeLabel2 = XmlHelper.contructureErrorMessage(childNodeLabelMain, nodeName2);

        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, childNodeLabelMain);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, childNodeLabel1);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName2, childNodeLabel2);

        if (xmlParseResult.getIsCurrentScheValid()) {

            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
                    
            // INSURER
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "name", XmlHelper.isMAN_Claim_Customer_Insurer_name, "", childNodeLabel1);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "policy-number", XmlHelper.isMAN_Claim_Customer_Insurer_policyNumber, "", childNodeLabel1);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "claim-reference", XmlHelper.isMAN_Claim_Customer_Insurer_claimReference, "", childNodeLabel1);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "comprehensive", XmlHelper.isMAN_Claim_Customer_Insurer_comprehensive, "", childNodeLabel1);

            // VEHICLE
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vehicle-registration", XmlHelper.isMAN_Claim_Customer_Vehicle_Registration, XmlHelper.REG_VEHICLE_REG, childNodeLabel2);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vehicle-manufacturer", XmlHelper.isMAN_Claim_Customer_Vehicle_Manufacturer, "", childNodeLabel2);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vehicle-model", XmlHelper.isMAN_Claim_Customer_Vehicle_Model, "", childNodeLabel2);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vehicle-class", XmlHelper.isMAN_Claim_Customer_Vehicle_Class, "", childNodeLabel2);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "usable", XmlHelper.isMAN_Claim_Customer_Vehicle_Usable, "", childNodeLabel2);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "location", XmlHelper.isMAN_Claim_Customer_Vehicle_Location, "", childNodeLabel1);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "damage", XmlHelper.isMAN_Claim_Customer_Vehicle_Damage, "", childNodeLabel1);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "initial-ecd", XmlHelper.isMAN_Claim_Customer_Vehicle_InitialEcd, XmlHelper.REG_TIMESTAMP, childNodeLabel1);
            
            if (xmlParseResult.getIsCurrentScheValid() && xmlParseResult.getIsCurrentDataValid()) {
                
                Customer customer = new Customer();

                if(xmlParseResult.getClaim()!=null && xmlParseResult.getClaim().getCustomer()!=null){
                    customer = xmlParseResult.getClaim().getCustomer();
                }
                
                // GET INSURER INFORMATION
                InsurerService thisISCtrl = new InsurerServiceImpl();
                Insurer insurer = thisISCtrl.getInsurerByNodeName(thisElement, "name");
                
                if(insurer!=null){
                    xmlParseResult.getClaim().setInsurer(insurer);
                    customer.setInsurerId(insurer.getId());
                }else{
                    if(XmlHelper.isMAN_Claim_Customer_Insurer_name){
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, "Invalid insurer", false);
                    }
                }
                
                // GET VEHICLE CLASS ID
                VehicleClass vehicleclass = vehicleClassService.getVehicleClassByNodeName(thisElement, "vehicle-class");
                if(vehicleclass!=null){
                    customer.setVehicleClass(vehicleclass);
                }else{
                    if(XmlHelper.isMAN_Claim_Customer_Vehicle_Class){
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, "Invalid vehicle class", false);
                    }
                }

                customer.setPolicyNumber(XmlHelper.getNodeValue(thisElement, "policy-number"));
                customer.setClaimReference(XmlHelper.getNodeValue(thisElement, "claim-reference"));
                customer.setComprehensive(XmlHelper.getBooleanFromNode(thisElement, "comprehensive"));
                customer.setVehicleRegistration(XmlHelper.getNodeValue(thisElement, "vehicle-registration"));
                customer.setVehicleManufacturer(XmlHelper.getNodeValue(thisElement, "vehicle-manufacturer"));
                customer.setVehicleModel(XmlHelper.getNodeValue(thisElement, "vehicle-model"));
                customer.setIsUsable(XmlHelper.getBooleanFromNode(thisElement, "usable"));
                customer.setLocation(XmlHelper.getNodeValue(thisElement, "location"));
                customer.setDamage(XmlHelper.getNodeValue(thisElement, "damage"));
                customer.setInitialECD(XmlHelper.getTimeStampFromNode(thisElement, "initial-ecd"));
                
                xmlParseResult.getClaim().setCustomer(customer);
            }
        }

        return xmlParseResult;
    }

    private  XMLParseResult ClaimDetail_ThirdPartySchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            String parentNodeName) throws Exception {

        String mainNodeName = "third-party";
        String nodeName1 = "insurer";
        String nodeName2 = "vehicle";
        String nodeName3 = "driver";

        String childNodeLabelMain = XmlHelper.contructureErrorMessage(parentNodeName, mainNodeName);
        String childNodeLabel1 = XmlHelper.contructureErrorMessage(childNodeLabelMain, nodeName1);
        String childNodeLabel2 = XmlHelper.contructureErrorMessage(childNodeLabelMain, nodeName2);
        String childNodeLabel3 = XmlHelper.contructureErrorMessage(childNodeLabelMain, nodeName3);

        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, childNodeLabelMain);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, childNodeLabel1);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName2, childNodeLabel2);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName3, childNodeLabel3);

        if (xmlParseResult.getIsCurrentScheValid()) {

            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);

            // INSURER
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "name", XmlHelper.isMAN_Claim_ThirdParty_Insurer_Name, "", childNodeLabel1);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "policy-number", XmlHelper.isMAN_Claim_ThirdParty_Insurer_PolicyNumber, "", childNodeLabel1);
            
            // ONLY SET TO MANDATORY WHEN IN STAGE 2
            Boolean isClaimReferenceNumberMandatory = false;
            if(!(xmlParseResult.getClaim().getStatus().equalsIgnoreCase(XMLParseResult.IN_PROGRESS))){
                isClaimReferenceNumberMandatory = XmlHelper.isMAN_Claim_ThirdParty_Insurer_ClaimReference;
            }
            
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "claim-reference", isClaimReferenceNumberMandatory, "", childNodeLabel1);

            // VEHICLE
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vehicle-registration", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_Registration, XmlHelper.REG_VEHICLE_REG, childNodeLabel2);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vehicle-manufacturer", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_manufacturer, "", childNodeLabel2);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vehicle-model", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_model, "", childNodeLabel2);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vehicle-class", XmlHelper.isMAN_Claim_ThirdParty_Vehicle_class, "", childNodeLabel2);

            // DRIVER
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "title", XmlHelper.isMAN_Claim_ThirdParty_Driver_Title, "", childNodeLabel3);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "firstnames", XmlHelper.isMAN_Claim_ThirdParty_Driver_Firstnames, "", childNodeLabel3);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "lastname", XmlHelper.isMAN_Claim_ThirdParty_Driver_Lastname, "", childNodeLabel3);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "address1", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address1, "", childNodeLabel3);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "address2", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address2, "", childNodeLabel3);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "address3", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address3, "", childNodeLabel3);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "address4", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address4, "", childNodeLabel3);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "address5", XmlHelper.isMAN_Claim_ThirdParty_Driver_Address5, "", childNodeLabel3);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "postcode", XmlHelper.isMAN_Claim_ThirdParty_Driver_Postcode, "", childNodeLabel3);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "telephone-day", XmlHelper.isMAN_Claim_ThirdParty_Driver_TelephoneDay, "", childNodeLabel3);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "telephone-evening", XmlHelper.isMAN_Claim_ThirdParty_Driver_TelephoneEvening, "", childNodeLabel3);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "email", XmlHelper.isMAN_Claim_ThirdParty_Driver_Email, XmlHelper.REG_EMAIL, childNodeLabel3);
            
            if (xmlParseResult.getIsCurrentScheValid() && xmlParseResult.getIsCurrentDataValid()) {
                
                ThirdParty thirdparty = new ThirdParty();
                
                if(xmlParseResult.getClaim()!=null && xmlParseResult.getClaim().getThirdParty()!=null){
                    thirdparty = xmlParseResult.getClaim().getThirdParty();
                }
                
                // GET INSURER INFORMATION
                InsurerServiceImpl thisISCtrl = new InsurerServiceImpl();
                Insurer insurer = thisISCtrl.getInsurerByNodeName(thisElement, "name");
                if(insurer!=null){
                    thirdparty.setInsurer(insurer);
                }else{
                    if(XmlHelper.isMAN_Claim_ThirdParty_Insurer_Name){
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, "Invalid insurer", false);
                    }
                }
                
                 // GET VEHICLE CLASS ID
                VehicleClass vehicleclass = vehicleClassService.getVehicleClassByNodeName(thisElement, "vehicle-class");
                if(vehicleclass!=null){
                    thirdparty.setVehicleClass(vehicleclass);
                }else{
                    if(XmlHelper.isMAN_Claim_ThirdParty_Vehicle_class){
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, "Invalid vehicle class", false);
                    }
                }
                
                // SET THIRD PARTY INFORMATION
                thirdparty.setPolicyNumber(XmlHelper.getNodeValue(thisElement, "policy-number"));
                thirdparty.setClaimReference(XmlHelper.getNodeValue(thisElement, "claim-reference"));
                thirdparty.setVehicleRegistration(XmlHelper.getNodeValue(thisElement, "vehicle-registration"));
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
        String parentNodeName) throws Exception {

        String mainNodeName = "incident";

        String childNodeLabelMain = XmlHelper.contructureErrorMessage(parentNodeName, mainNodeName);

        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, childNodeLabelMain);

        if (xmlParseResult.getIsCurrentScheValid()) {
            
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "date", XmlHelper.isMAN_Claim_Incident_Date, XmlHelper.REG_TIMESTAMP, childNodeLabelMain);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "location", XmlHelper.isMAN_Claim_Incident_Location, "", childNodeLabelMain);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "police-involved", XmlHelper.isMAN_Claim_Incident_PoliceInvolved, "", childNodeLabelMain);
            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "description", XmlHelper.isMAN_Claim_Incident_Description, "", childNodeLabelMain);
            
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
                xmlParseResult = ClaimDetail_IncidentWitnessSchemaValidation(xmlParseResult, thisElement, doc, childNodeLabelMain);
                xmlParseResult = ClaimDetail_IncidentInjuriesSchemaValidation(xmlParseResult, thisElement, doc, childNodeLabelMain);
            }
        }
        
        return xmlParseResult;
    }
    
    private  XMLParseResult ClaimDetail_IncidentWitnessSchemaValidation(
        XMLParseResult xmlParseResult,
        Element mainElement,
        Document doc,
        String parentNodeName) throws Exception {
        
        String nodeName1 = "witnesses";
        String nodeName2 = "witness";
        
        // CONSTRUCT ERROR MESSAGE
        String childNodeLabel1 = XmlHelper.contructureErrorMessage(parentNodeName, nodeName1);
        String childNodeLabel2 = XmlHelper.contructureErrorMessage(childNodeLabel1, nodeName2);
        
        // RESET THE CHECKING FLAG
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        // VALIDATE WITNESSES MAIN NODE
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, parentNodeName);

        if (xmlParseResult.getIsCurrentScheValid()) {
            // RESET THE CHECKING FLAG
            xmlParseResult.setIsCurrentScheValid(true);
            xmlParseResult.setIsCurrentDataValid(true);
            
            // GET WITNESS NODE AND VALIDATE
            Element thisElement = XMLUtils.getElement(mainElement, nodeName1);
            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, nodeName2, childNodeLabel1);
            
            if (xmlParseResult.getIsCurrentScheValid()) {
                
                // GET WITNESS LIST
                ArrayList<Element> witnessElements = XMLUtils.getElements(doc, thisElement, nodeName2);
                xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, witnessElements, nodeName2, childNodeLabel1);

                if (xmlParseResult.getIsCurrentScheValid()) {

                   ArrayList<Witness> witnesses = new ArrayList<Witness>();

                    for (Element ee : witnessElements) {
                        
                        xmlParseResult.setIsCurrentDataValid(true);
                        xmlParseResult.setIsCurrentScheValid(true);

                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "name", XmlHelper.isMAN_Claim_Incident_Witness_name, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address1", XmlHelper.isMAN_Claim_Incident_Witness_address1, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address2", XmlHelper.isMAN_Claim_Incident_Witness_address2, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address3", XmlHelper.isMAN_Claim_Incident_Witness_address3, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address4", XmlHelper.isMAN_Claim_Incident_Witness_address4, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address5", XmlHelper.isMAN_Claim_Incident_Witness_address5, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "postcode", XmlHelper.isMAN_Claim_Incident_Witness_postcode, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "telephone-day", XmlHelper.isMAN_Claim_Incident_Witness_telephoneDay, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "telephone-evening", XmlHelper.isMAN_Claim_Incident_Witness_telephoneEvening, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "email", XmlHelper.isMAN_Claim_Incident_Witness_email, "", childNodeLabel2);

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
        String parentNodeName) throws Exception {
        
        String nodeName1 = "injuries";
        String nodeName2 = "injury";
        String subNodeName = "solicitor";
        
        // CONSTRUCT NODE ERROR MESSAGE
        String childNodeLabel1 = XmlHelper.contructureErrorMessage(parentNodeName, nodeName1);
        String childNodeLabel2 = XmlHelper.contructureErrorMessage(childNodeLabel1, nodeName2);
        
        // RESET VALIDATION FLAG
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);
        
        // VALIDATE INJURIES NODE
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, parentNodeName);
        
        if (xmlParseResult.getIsCurrentScheValid()) {
            
            // RESET VALIDATION FLAG
            xmlParseResult.setIsCurrentScheValid(true);
            xmlParseResult.setIsCurrentDataValid(true);
            
            // GET AND VALIDATE INJURY NODE
            Element thisElement = XMLUtils.getElement(mainElement, nodeName1);
            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, nodeName2, childNodeLabel1);
            
            if (xmlParseResult.getIsCurrentScheValid()) {
                
                // GET AND VALIDATE INJURY LIST NODE
                ArrayList<Element> injuriesElements = XMLUtils.getElements(doc, thisElement, nodeName2);
                xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, injuriesElements, nodeName2, childNodeLabel1);
                
                if (xmlParseResult.getIsCurrentScheValid()) {

                    ArrayList<Injury> injuries = new ArrayList<Injury>();
                    ArrayList<Solicitor> solicitors = new ArrayList<Solicitor>();
                    
                    // LOOP ALL THE INJURIES RECORD
                    for (Element ee : injuriesElements) {

                        xmlParseResult.setIsCurrentDataValid(true);
                        xmlParseResult.setIsCurrentScheValid(true);

                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "name", XmlHelper.isMAN_Claim_Incident_Injury_name, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address1", XmlHelper.isMAN_Claim_Incident_Injury_address1, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address2", XmlHelper.isMAN_Claim_Incident_Injury_address2, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address3", XmlHelper.isMAN_Claim_Incident_Injury_address3, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address4", XmlHelper.isMAN_Claim_Incident_Injury_address4, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address5", XmlHelper.isMAN_Claim_Incident_Injury_address5, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "postcode", XmlHelper.isMAN_Claim_Incident_Injury_postcode, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "telephone-day", XmlHelper.isMAN_Claim_Incident_Injury_telephoneDay, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "telephone-evening", XmlHelper.isMAN_Claim_Incident_Injury_telephoneEvening, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "email", XmlHelper.isMAN_Claim_Incident_Injury_email, "", childNodeLabel2);
                        
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
                            Element thisSubElement = XMLUtils.getElement(thisElement, subNodeName);
                            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, subNodeName, childNodeLabel2);
                            String childNodeLabel3 = XmlHelper.contructureErrorMessage(childNodeLabel1, subNodeName);
                            xmlParseResult.setIsCurrentDataValid(true);
                            xmlParseResult.setIsCurrentScheValid(true);

                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "name", XmlHelper.isMAN_Claim_Incident_Solicitor_name, "", childNodeLabel3);
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "address1", XmlHelper.isMAN_Claim_Incident_Solicitor_address1, "", childNodeLabel3);
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "address2", XmlHelper.isMAN_Claim_Incident_Solicitor_address2, "", childNodeLabel3);
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "address3", XmlHelper.isMAN_Claim_Incident_Solicitor_address3, "", childNodeLabel3);
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "address4", XmlHelper.isMAN_Claim_Incident_Solicitor_address4, "", childNodeLabel3);
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "address5", XmlHelper.isMAN_Claim_Incident_Solicitor_address5, "", childNodeLabel3);
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "postcode", XmlHelper.isMAN_Claim_Incident_Solicitor_postcode, "", childNodeLabel3);
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "telephone", XmlHelper.isMAN_Claim_Incident_Solicitor_telephone, "", childNodeLabel3);
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisSubElement, "email", XmlHelper.isMAN_Claim_Incident_Solicitor_email, "", childNodeLabel3);
                            
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
        Session currentSession,
        XMLParseResult xmlParseResult,
        Element mainElement,
        Document doc) throws Exception {

        String parentNodeName = "rental";
        String nodeName1 = "repair";
        String nodeName2 = "engineer-report";
        
        // CONSTRUCT ERROR MESSAGE
        String childNodeLabelMain = XmlHelper.contructureErrorMessage(parentNodeName, "");
        String childNodeLabel1 = XmlHelper.contructureErrorMessage(childNodeLabelMain, nodeName1);
        String childNodeLabel2 = XmlHelper.contructureErrorMessage(childNodeLabel1, nodeName2);
        
        // RESET VALIDATION FLAG
        xmlParseResult.setIsCurrentScheValid(true);
        
        Element repairElement = XMLUtils.getElement(mainElement, nodeName1);
        Element eReportElement = XMLUtils.getElement(mainElement, nodeName2);
        
        if(repairElement!=null && eReportElement!=null){
            
            // VALIDATE REPAIR AND ENGINEER REPORT
            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, childNodeLabelMain);
            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName2, childNodeLabel1);
        
            if (xmlParseResult.getIsCurrentScheValid()) {
                
                // GET AND VALIDATE ENGINEER REPROT
                ArrayList<Element> engineerReportElements = XMLUtils.getElements(doc, mainElement, nodeName2);
                xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, engineerReportElements, nodeName2, childNodeLabel1);
                
                if (xmlParseResult.getIsCurrentScheValid()) {
                    
                    // ArrayList<EngineerReport> engineerReports = new ArrayList<EngineerReport>();

                    for (Element ee : engineerReportElements) {
                    
                        xmlParseResult.setIsCurrentDataValid(true);
                        xmlParseResult.setIsCurrentScheValid(true);

                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "labour-amount", XmlHelper.isMAN_Repair_engineerReport_labour_Amount, XmlHelper.REG_BIGDECIMAL, childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "total-amount", XmlHelper.isMAN_Repair_engineerReport_total_Amount, XmlHelper.REG_BIGDECIMAL, childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "days", XmlHelper.isMAN_Repair_engineerReport_days, XmlHelper.REG_INTEGER, childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "name", XmlHelper.isMAN_Repair_engineerReport_name, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "company", XmlHelper.isMAN_Repair_engineerReport_company, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address1", XmlHelper.isMAN_Repair_engineerReport_address1, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address2", XmlHelper.isMAN_Repair_engineerReport_address2, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address3", XmlHelper.isMAN_Repair_engineerReport_address3, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address4", XmlHelper.isMAN_Repair_engineerReport_address4, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address5", XmlHelper.isMAN_Repair_engineerReport_address5, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "postcode", XmlHelper.isMAN_Repair_engineerReport_postcode, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "telephone", XmlHelper.isMAN_Repair_engineerReport_telephone, "", childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "email", XmlHelper.isMAN_Repair_engineerReport_email, XmlHelper.REG_EMAIL, childNodeLabel2);
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "usable", XmlHelper.isMAN_Repair_engineerReport_usable, "", childNodeLabel2);

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
                            // engineerReports.add(engineerReport);
 
                        }
                    }
                    /*
                    if(engineerReports.size()>0){
                        xmlParseResult.setEngineerReports(engineerReports);
                    }
                    */ 
                }
            }
        }
        return xmlParseResult;
    }
    
    // VALIDATE INVOICE SECTION 
    private  XMLParseResult RentalInvoiceSchemaValidation(
            Session currentSession,
            XMLParseResult xmlParseResult,
            Element mainElement,
            Document doc) throws Exception {

        String parentNodeName = "rental";
        String nodeName1 = "invoice";
        
        // CONSTRUCT NODE LOCATION ERROR MESSAGE
        String childNodeLabelMain = XmlHelper.contructureErrorMessage(parentNodeName, "");
        String childNodeLabel1 = XmlHelper.contructureErrorMessage(childNodeLabelMain, nodeName1);
        
        // RESET VALIDATION FLAG AND VALIDATE MAIN INVOICE NODE SECTION
        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, childNodeLabelMain);
        
        
        if (xmlParseResult.getIsCurrentScheValid()) {
            Element thisElement = XMLUtils.getElement(mainElement, nodeName1);
            xmlParseResult = RentalInvoiceDetailSchemaValidation(xmlParseResult, thisElement, doc, childNodeLabel1);
        }
        
        return xmlParseResult;
    }
    
    // VALIDATE INVOICE DETAIL SECTION
    private  XMLParseResult RentalInvoiceDetailSchemaValidation(
            XMLParseResult xmlParseResult,
            Element thisElement,
            Document doc,
            String parentNodeName) throws Exception {

        String subNodeName0 = "vehicles";
        String subNodeName1 = "extras";
        String subNodeName2 = "repair";
        String subNodeName3 = "storage-recovery";
        String subNodeName4 = "engineer-fee";
        String subNodeName5 = "supplier";

        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);

        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Net, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Vat, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Gross, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "less-handling-fee", XmlHelper.isMAN_Invoice_lessHandlingFee, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "less-discount", XmlHelper.isMAN_Invoice_lessDiscount, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "total-to-pay", XmlHelper.isMAN_Invoice_TotalToPay, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "date-invoiced", XmlHelper.isMAN_Invoice_DateInvoiced, XmlHelper.REG_TIMESTAMP, parentNodeName);
        
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, subNodeName0, parentNodeName);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, subNodeName1, parentNodeName);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, subNodeName2, parentNodeName);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, subNodeName3, parentNodeName);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, subNodeName4, parentNodeName);

        if (xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()) {

            String childNodeLabel0 = XmlHelper.contructureErrorMessage(parentNodeName, subNodeName0);
            String childNodeLabel1 = XmlHelper.contructureErrorMessage(parentNodeName, subNodeName1);
            String childNodeLabel2 = XmlHelper.contructureErrorMessage(parentNodeName, subNodeName2);
            String childNodeLabel3 = XmlHelper.contructureErrorMessage(parentNodeName, subNodeName3);
            String childNodeLabel4 = XmlHelper.contructureErrorMessage(parentNodeName, subNodeName4);
            String childNodeLabel5 = XmlHelper.contructureErrorMessage(parentNodeName, subNodeName5);
            
            // SET SUPPLIER INFORMATION
            Invoice invoice = new Invoice();
            invoice.setTotalGross(XmlHelper.getBigDecimalFromNode(thisElement, "gross"));
            invoice.setTotalNet(XmlHelper.getBigDecimalFromNode(thisElement, "net"));
            invoice.setTotalVat(XmlHelper.getBigDecimalFromNode(thisElement, "vat"));
            invoice.setTotalToPay(XmlHelper.getBigDecimalFromNode(thisElement, "total-to-pay"));
            invoice.setDiscount(XmlHelper.getBigDecimalFromNode(thisElement, "less-discount"));
            invoice.setDeductionForClaimsHandlingFee(XmlHelper.getBigDecimalFromNode(thisElement, "less-handling-fee"));
            invoice.setDateInvoiced(XmlHelper.getTimeStampFromNode(thisElement, "date-invoiced"));
            
            xmlParseResult.getClaim().setInvoice(invoice);

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
            
            // supplier
            xmlParseResult = InvoiceDetail_SupplierValidSchemaValidation(xmlParseResult, thisElement, childNodeLabel5, subNodeName5);
        }

        return xmlParseResult;
    }
    
    // VALIDATE INVOICE DETAIL - Vehicles SECTION
    private  XMLParseResult InvoiceDetail_VehiclesValidSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            String parentNodeName,
            String thisNodeName) {

        Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);

        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);

        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Vehicles_Net, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Vehicles_Vat, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Vehicles_Gross, XmlHelper.REG_BIGDECIMAL, parentNodeName);

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
            String parentNodeName,
            String thisNodeName) {

        Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);

        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);

        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Repair_Net, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Repair_Vat, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Repair_Gross, XmlHelper.REG_BIGDECIMAL, parentNodeName);

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
            String parentNodeName,
            String thisNodeName) {

        Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);

        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);

        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "handling-invoice-no", XmlHelper.isMAN_Invoice_Supplier_HandlingInvoiceNo, "", parentNodeName);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "handling-invoice-amount", XmlHelper.isMAN_Invoice_Supplier_HandlingInvoiceAmount, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "claim-invoice-no", XmlHelper.isMAN_Invoice_Supplier_ClaimInvoiceNo, "", parentNodeName);

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
            String parentNodeName,
            String thisNodeName) {

        Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);

        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);

        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Engineer_Fee_Net, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Engineer_Fee_Vat, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Engineer_Fee_Gross, XmlHelper.REG_BIGDECIMAL, parentNodeName);

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
            String parentNodeName,
            String thisNodeName) {

        Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);

        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);

        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "net", XmlHelper.isMAN_Invoice_Storage_Recovery_Net, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "vat", XmlHelper.isMAN_Invoice_Storage_Recovery_Vat, XmlHelper.REG_BIGDECIMAL, parentNodeName);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, "gross", XmlHelper.isMAN_Invoice_Storage_Recovery_Gross, XmlHelper.REG_BIGDECIMAL, parentNodeName);

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
            String parentNodeName,
            String thisNodeName) throws Exception {

        Element thisElement = XMLUtils.getElement(mainElement, thisNodeName);

        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);

        String subNodeName = "extra";

        ArrayList<Element> extraElements = XMLUtils.getElements(doc, thisElement, subNodeName);
        xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, extraElements, subNodeName, parentNodeName);

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
                
                String childNodeLabelMain = XmlHelper.contructureErrorMessage(parentNodeName, XmlHelper.getNodeValue(ee, "name"));
                
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "name", XmlHelper.isMAN_Invoice_Extras_Name, "", childNodeLabelMain);
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "quantity", XmlHelper.isMAN_Invoice_Extras_Quantity, XmlHelper.REG_INTEGER, childNodeLabelMain);
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "item-cost", XmlHelper.isMAN_Invoice_Extras_Item_Cost, XmlHelper.REG_BIGDECIMAL, childNodeLabelMain);

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
            Session currentSession,
            XMLParseResult xmlParseResult,
            Element mainElement,
            Document doc) throws Exception {

        String parentNodeName = "rental";
        String mainNodeName = "rental-vehicles";
        String childNodeName = "rental-vehicle";

        String childNodeLabelMain = XmlHelper.contructureErrorMessage(parentNodeName, "");
        String childNodeLabel1 = XmlHelper.contructureErrorMessage(childNodeLabelMain, mainNodeName);
        String childNodeLabel2 = XmlHelper.contructureErrorMessage(childNodeLabel1, childNodeName);

        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, mainNodeName, childNodeLabelMain);

        if (xmlParseResult.getIsCurrentScheValid()) {

            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "net", XmlHelper.isMAN_Invoice_Engineer_Fee_Net, XmlHelper.REG_BIGDECIMAL, parentNodeName);
            
            Element thisElement = XMLUtils.getElement(mainElement, mainNodeName);
            ArrayList<Element> rentalVehicleElements = XMLUtils.getElements(doc, thisElement, childNodeName);
            xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, rentalVehicleElements, childNodeName, childNodeLabel1);

            if (xmlParseResult.getIsCurrentScheValid()) {
                
                ArrayList<VehicleHire> vehiclehires = new ArrayList<VehicleHire>();
                xmlParseResult.setVehiclehires(vehiclehires);
                
                for (Element ee : rentalVehicleElements) {
                    xmlParseResult = RentalVehiclesDetailSchemaValidation(xmlParseResult, ee, doc, childNodeLabel2);
                    break; // ONLY ONE RECORD
                }
            }
        }
        
        return xmlParseResult;
    }

    private  XMLParseResult RentalVehiclesDetailSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            Document doc,
            String parentNodePath) throws Exception {
            
        VehicleHire vehiclehire = new VehicleHire();
        
        xmlParseResult.setIsCurrentDataValid(true);
        xmlParseResult.setIsCurrentScheValid(true);

        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "vehicle-registration", XmlHelper.isMAN_RentalVehicles_Vehicle_Registration, XmlHelper.REG_VEHICLE_REG, parentNodePath);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "vehicle-manufacturer", XmlHelper.isMAN_RentalVehicles_Vehicle_Manufacturer, "", parentNodePath);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "vehicle-model", XmlHelper.isMAN_RentalVehicles_Vehicle_Model, "", parentNodePath);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "vehicle-class", XmlHelper.isMAN_RentalVehicles_Vehicle_Class, "", parentNodePath);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "rental-start", XmlHelper.isMAN_RentalVehicles_Rental_Start, XmlHelper.REG_TIMESTAMP, parentNodePath);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "rental-end", XmlHelper.isMAN_RentalVehicles_Rental_End, XmlHelper.REG_TIMESTAMP, parentNodePath);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "rental-days", XmlHelper.isMAN_RentalVehicles_Rental_Days, XmlHelper.REG_INTEGER, parentNodePath);
        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "collection-reason", XmlHelper.isMAN_RentalVehicles_CollectionReason, "", parentNodePath);

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
                    xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, "INVALID VEHICLE CLASS", false);
                }
            }
            
            vehiclehire.setVehicleRegistration(XmlHelper.getNodeValue(mainElement, "vehicle-registration"));
            vehiclehire.setVehicleManufacturer(XmlHelper.getNodeValue(mainElement, "vehicle-manufacturer"));
            vehiclehire.setVehicleModel(XmlHelper.getNodeValue(mainElement, "vehicle-model"));
            vehiclehire.setRentalStart(XmlHelper.getTimeStampFromNode(mainElement, "rental-start"));
            vehiclehire.setRentalEnd(XmlHelper.getTimeStampFromNode(mainElement, "rental-end"));
            vehiclehire.setDays(XmlHelper.getIntegerFromNode(mainElement, "rental-days"));
            vehiclehire.setCollectionReason(XmlHelper.getNodeValue(mainElement, "collection-reason"));
        
            // PART 2 : EXTRA SECTION
            String nodeName1 = "extras";
            String nodeName2 = "extra";

            String childNodeLabel1 = XmlHelper.contructureErrorMessage(parentNodePath, nodeName1);
            String childNodeLabel2 = XmlHelper.contructureErrorMessage(childNodeLabel1, nodeName2);

            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);

            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, parentNodePath);

            if (xmlParseResult.getIsCurrentScheValid()) {

                xmlParseResult.setIsCurrentScheValid(true);
                
                // GET <EXTRAS></EXTRAS> ELEMENT
                Element thisElement = XMLUtils.getElement(mainElement, nodeName1);
                xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, nodeName2, childNodeLabel1);
                
                if (xmlParseResult.getIsCurrentScheValid()) {
                    
                    // GET <EXTRA></EXTRA> ELEMENT
                    ArrayList<Element> extraElements = XMLUtils.getElements(doc, thisElement, nodeName2);
                    xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, extraElements, nodeName2, childNodeLabel1);

                    if (xmlParseResult.getIsCurrentScheValid()) {
                        
                        for (Element ee : extraElements) {
                            
                            xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, nodeName2, XmlHelper.isMAN_RentalVehicles_Extras_Extra, "", childNodeLabel2);

                            String selectedExtra = ee.getTextContent();
                            
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
