package chox.services;

import chox.Util.XmlHelper;
import java.io.File;
import org.w3c.dom.*;
import java.util.ArrayList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import com.filesystemsoftware.utils.XMLUtils;
import com.filesystemsoftware.utils.Logger;
import java.math.BigDecimal;
import chox.model.*;
import scsbre.engine.*;
import java.util.List;
import chox.Util.DocumentHelper;
import chox.data.UploadStatus;
import chox.xmlValidation.rules.claimValidation;
import chox.xmlValidation.rules.driverValidation;
import chox.xmlValidation.rules.invoiceValidation;
import chox.xmlValidation.rules.repairValidation;
import chox.xmlValidation.rules.vehicleValidation;
import org.hibernate.TransactionException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class UploadClaimXMLServiceImpl extends SecureDataService implements UploadClaimXMLService {
    
    private VehicleClassService vehicleClassService;
    private EngineerReportService engineerReportService;
    private IncidentService incidentService;
    private InjuryService injuryService;
    private CustomerService customerService;
    private InvoiceService invoiceService;
    private AuditTrailService auditTrailService;
    private ClaimService claimService;
    private WitnessService witnessService;
    private ThirdPartyService thirdPartyService;
    private VehicleHireService vehicleHireService;
    private SolicitorService solicitorService;
    private InsurerAlliasService insurerAlliasService;
    private ChorganisationService chorganisationService;
    private ChoBandService choBandService;
    private HistoryService historyService;
    private HireMonitoringEcdService hireMonitoringEcdService;
    private HireMonitoringDetailService hireMonitoringDetailService;
    
    public UploadClaimXMLServiceImpl()
    {        
    }
    
    /*
    public static void main(String[] args) {

        try {
            
            String sXMLPath1 = "C:/Users/Carlson/Desktop/CHOX.file/P2S6/23042009 INVOICE UPLOAD.V.2.xml";
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
                        
                        UploadClaimXMLServiceImpl thisCtrl = new UploadClaimXMLServiceImpl();

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
    */
    
    public ArrayList<XMLParseResult> processClaimXMLFile(File claimXMLFile, Boolean isAllowPartialUpload) {
        UploadClaimXMLServiceImpl thisCtrl = new UploadClaimXMLServiceImpl();
        return thisCtrl.processXML(claimXMLFile, isAllowPartialUpload);
    }
    
    public ArrayList<XMLParseResult> processXML(File claimXMLFile, Boolean isAllowPartialUpload) {
        
        ArrayList<XMLParseResult> xmlParseResults = new ArrayList<XMLParseResult>();

        try {
            
            Document doc = DocumentHelper.getDocumentFromFile(claimXMLFile);
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
  
        // VALIDATE AND GET RECORD FOR CLAIM OBJECT AND CHECK THE CLAIM IS EXIST OR NOT 
        xmlParseResult = claimValidation.ClaimHeaderSchemaValidation(xmlParseResult, root, claimService, choBandService, chorganisationService);

        // GET CLAIM INFORMATION IF IT IS NEW CLAIM TO BE INSERTED 
        if(!xmlParseResult.getIsClaimExist()){
            xmlParseResult = driverValidation.DriversSchemaValidation(xmlParseResult, root, doc);
            xmlParseResult = claimValidation.ClaimSchemaValidation(xmlParseResult, root, doc, vehicleClassService, insurerAlliasService);
        }
        
        xmlParseResult = repairValidation.RepairSchemaValidation(xmlParseResult, root, doc);
        xmlParseResult = vehicleValidation.RentalVehiclesSchemaValidation(xmlParseResult, root, doc, vehicleClassService);

        if(xmlParseResult.getClaim().getInvoice()!=null){
            xmlParseResult.setIsInvoiceExist(true);
        }
        
        boolean isNewInvoice = false;
        String oldClaimStatus = xmlParseResult.getClaim().getStatus();
        
        if(xmlParseResult.getIsClaimExist() 
            && xmlParseResult.getClaim().getStatus().equalsIgnoreCase(ClaimStatus.AWAITING_INVOICE_DATA)
            && !xmlParseResult.getIsInvoiceExist()){
            
            isNewInvoice = true;
            xmlParseResult = invoiceValidation.InvoicesSchemaValidation(xmlParseResult, root, doc);

            // EXECUTE BRE RULE
            if(xmlParseResult.getIsSchemaValid() && xmlParseResult.getIsDataValid()){
              
                // CHECK INITIAL ENGINEERING REPORT
                Boolean isEngReportExist = false;
                if(xmlParseResult.getClaim().getEngineerReport()!=null){
                    isEngReportExist = true;
                }
                
                VehicleClass cust_VehicleClass = xmlParseResult.getClaim().getCustomer().getVehicleClass();
                VehicleClass thirdVehicleClass = xmlParseResult.getClaim().getThirdParty().getVehicleClass();
                
                Claim BREClaim = constructeClaimForInvoiceValidation(xmlParseResult.getClaim());
                RulesEngineResponse validationResult = invoiceService.XMLUploaderInvoiceValidation(BREClaim);

                xmlParseResult.getClaim().getCustomer().setVehicleClass(cust_VehicleClass);
                xmlParseResult.getClaim().getThirdParty().setVehicleClass(thirdVehicleClass);
                
                historyService.logInvoiceValidationErrorMsg(validationResult, BREClaim);

                String newClaimStatus = validationResult.getStatus().toString();                
                xmlParseResult.getClaim().setStatus(newClaimStatus);
                
                if(!isEngReportExist){
                    xmlParseResult.getClaim().setEngineerReport(null);
                }

                if(validationResult.getResults().size()>0){
                    xmlParseResult = appendInvoiceValidationErrorMessage(xmlParseResult, validationResult.getResults());
                }
            }
            
        }else{
        
            // VALIDATE CLAIM OR INVOICE IS UNIQUE
            if(!xmlParseResult.getIsClaimExist()){
                
                // xmlParseResult = claimValidation.validateClaimInformation(xmlParseResult);
                
                if(xmlParseResult.getClaim().getThirdParty()!=null){
                    if(xmlParseResult.getClaim().getThirdParty().getClaimReference()!=null){
                        xmlParseResult.getClaim().setClaimNumber(xmlParseResult.getClaim().getThirdParty().getClaimReference());
                    }
                }
            }
        }
        
        if(xmlParseResult.getIsSchemaValid() && xmlParseResult.getIsDataValid()){            
            xmlParseResult = saveXMLRecord(xmlParseResult, isNewInvoice, oldClaimStatus);
        }
        
        xmlParseResult = UploadStatus.getUploadStatus(xmlParseResult);
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
        
        if(claimService.getCountOfClaimByVRN(claim.getCustomer().getVehicleRegistration(), claim.getId())>0){
           claim.getCustomer().setIsVehicleRegistrationExist(true);
        }
        
        // SET VEHICLE CLASS TO NULL WHEN 
        if(claim.getThirdParty().getVehicleClass()!=null){
            if(claim.getThirdParty().getVehicleClass().getName().equalsIgnoreCase("Unattached") 
                    || claim.getThirdParty().getVehicleClass().getName().equalsIgnoreCase("UNATTACHED")){
                claim.getThirdParty().setVehicleClass(null);
            }
        }
        
        // SET VEHICLE CLASS TO NULL WHEN 
        if(claim.getCustomer().getVehicleClass()!=null){
            if(claim.getCustomer().getVehicleClass().getName().equalsIgnoreCase("Unattached")
                    || claim.getCustomer().getVehicleClass().getName().equalsIgnoreCase("UNATTACHED")){
                claim.getCustomer().setVehicleClass(null);
            }
        }
        
        claim.setHireMonitoringEcd(hireMonitoringEcdService.getLatestHireMonitoringECDDate(claim));
        
        return claim;
    }

    private XMLParseResult appendInvoiceValidationErrorMessage(XMLParseResult xmlParseResult, List<RuleEvaluation> results){

        for(int iCount=0; iCount<results.size(); iCount++){
            
            RuleEvaluation rv = results.get(iCount);
            
            if(rv.getIsVisibleToCHO() && rv.getResult()==RuleEvaluationResult.RuleFailed){
                xmlParseResult.getDataValidationRemark().add(rv.toString());
            }
        }
        
        return xmlParseResult;
    }
    
    private XMLParseResult saveXMLRecord(XMLParseResult xmlParseResult, final boolean isNewInvoice, final String oldClaimStatus){       
        
        TransactionTemplate transactionTemplate = new TransactionTemplate(getTransactionManager());
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        try {
            final XMLParseResult readOnlyXmlParseResult = xmlParseResult;
            transactionTemplate.execute(
                    new TransactionCallbackWithoutResult() {

                        public void doInTransactionWithoutResult(TransactionStatus status) {

                            if (!readOnlyXmlParseResult.getIsClaimExist()) {
                                customerService.saveObjectForXMLUploader(readOnlyXmlParseResult);
                                thirdPartyService.saveObjectForXMLUploader(readOnlyXmlParseResult);
                                incidentService.saveObjectForXMLUploader(readOnlyXmlParseResult);
                                witnessService.saveObjectForXMLUploader(readOnlyXmlParseResult);
                                injuryService.saveObjectForXMLUploader(readOnlyXmlParseResult);
                                solicitorService.saveObjectForXMLUploader(readOnlyXmlParseResult);
                                hireMonitoringDetailService.saveObjectForXMLUploader(readOnlyXmlParseResult);
                            }

                            engineerReportService.saveObjectForXMLUploader(readOnlyXmlParseResult);
                            vehicleHireService.saveObjectForXMLUploader(readOnlyXmlParseResult);
                            invoiceService.saveObjectForXMLUploader(readOnlyXmlParseResult);
                            claimService.saveObjectForXMLUploader(readOnlyXmlParseResult);
                            
                            if (!readOnlyXmlParseResult.getIsClaimExist()) {
                                auditTrailService.logAuditLog(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, "", readOnlyXmlParseResult.getClaim());
                            }
                            
                            if(isNewInvoice){
                                auditTrailService.logAuditLog(readOnlyXmlParseResult.getClaim().getStatus(), oldClaimStatus, readOnlyXmlParseResult.getClaim());
                            }                        
                        }
                    });
        }
        catch(TransactionException e)
        {
            xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
        }      
                
        return xmlParseResult;
    }
    
    public void setVehicleClassService(VehicleClassService vehicleClassService) { this.vehicleClassService = vehicleClassService; }
    public void setEngineerReportService(EngineerReportService engineerReportService) { this.engineerReportService = engineerReportService; }
    public void setIncidentService(IncidentService incidentService) { this.incidentService = incidentService;}
    public void setInjuryService(InjuryService injuryService) { this.injuryService = injuryService; }
    public void setCustomerService(CustomerService customerService) { this.customerService = customerService; }
    public void setInvoiceService(InvoiceService invoiceService) { this.invoiceService = invoiceService; }
    public void setClaimService(ClaimService claimService) { this.claimService = claimService; }
    public void setWitnessService(WitnessService witnessService) { this.witnessService = witnessService; }
    public void setThirdPartyService(ThirdPartyService thirdPartyService) { this.thirdPartyService = thirdPartyService; }
    public void setVehicleHireService(VehicleHireService vehicleHireService) { this.vehicleHireService = vehicleHireService; }
    public void setSolicitorService(SolicitorService solicitorService) { this.solicitorService = solicitorService; }
    public void setAuditTrailService(AuditTrailService auditTrailService) { this.auditTrailService = auditTrailService; }
    public void setInsurerAlliasService(InsurerAlliasService insurerAlliasService) { this.insurerAlliasService = insurerAlliasService; }
    public void setChorganisationService(ChorganisationService chorganisationService) { this.chorganisationService = chorganisationService; }
    public void setChoBandService(ChoBandService choBandService) { this.choBandService = choBandService; }
    public void setHistoryService(HistoryService historyService) { this.historyService = historyService; }
    public void setHireMonitoringEcdService(HireMonitoringEcdService hireMonitoringEcdService) { this.hireMonitoringEcdService = hireMonitoringEcdService; }
    public void setHireMonitoringDetailService(HireMonitoringDetailService hireMonitoringDetailService) { this.hireMonitoringDetailService = hireMonitoringDetailService; }

}