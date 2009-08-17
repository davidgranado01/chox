package chox.services;

import java.io.File;
import chox.model.*;
import chox.xmlValidation.model.BordereauResult;
import chox.xmlValidation.model.ClaimResult;
import chox.xmlValidation.model.status.BordereauParseStatus;
import chox.xmlValidation.model.status.ClaimParseStatus;
import chox.xmlValidation.rules.BordereauDataValidation;
import chox.xmlValidation.rules.BordereauFileValidation;
import chox.xmlValidation.rules.BordereauVersionValidation;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.hibernate.TransactionException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
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
    private InsurerChorganisationService insurerChorganisationService;
    private BordereauService bordereauService;

    public static void main(String[] args) {

        try {

            String xmlFile = "C:/Project Workplace/Greefinch/Sherwood/testXML/UnitTest01.xml";

            File testFile = new File(xmlFile);
            UploadClaimXMLServiceImpl ctrl = new UploadClaimXMLServiceImpl();
            ctrl.processClaimXMLFile(testFile, testFile.getName());

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public BordereauResult processClaimXMLFile(final File file, final String fileName) {

        BordereauResult bordereauResult;
        TransactionTemplate transactionTemplate = new TransactionTemplate(getTransactionManager());
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        bordereauResult = (BordereauResult)transactionTemplate.execute(
                new TransactionCallback() {

                    public Object doInTransaction(TransactionStatus arg0) {
                       return doProcessClaimXMLFile(file, fileName);
                    }
                });

        return bordereauResult;

    }

    public BordereauResult doProcessClaimXMLFile(final File file, final String fileName) {

        BordereauResult bordereauResult = new BordereauResult();

        try {

            bordereauResult = new BordereauFileValidation().validate(file, fileName, bordereauResult);
            String BordereauParseStatusDescription = "Error";
            
            // System.out.println(":::: 00 "+bordereauResult.isValid());
            
            if (bordereauResult.isValid()) {

                bordereauResult = new BordereauVersionValidation().validate(file, fileName, bordereauResult);
                
                // System.out.println(":::: 02 "+bordereauResult.isValid());
                
                if (bordereauResult.isValid()) {

                    bordereauResult = new BordereauDataValidation().validate(file, fileName, bordereauResult, claimService, chorganisationService, choBandService, vehicleClassService, insurerAlliasService, insurerChorganisationService, hireMonitoringEcdService, invoiceService, historyService);
                    
                    // System.out.println(":::: 03 "+bordereauResult.isValid());
                    
                    int totalRecord = bordereauResult.getClaimResult().size();
                    int totalProcessed = 0;

                    for (ClaimResult claimResult : bordereauResult.getClaimResult()) {

                        // System.out.println("========================================================================");
                        // System.out.println("END: is Claim Valid?: " + claimResult.isValid());
                        // System.out.println("END: is Claim Data valid?: " + claimResult.isDataValid());
                        // System.out.println("END: Claim Process Status: " + claimResult.getClaimParseStatus());
                        // System.out.println("END: Claim Cho Ref: " + claimResult.getClaim().getChoReference());
                        // System.out.println("END: Claim Status: " + claimResult.getClaim().getStatus());

                        if (claimResult.isValid() && claimResult.isDataValid()) {
                            saveXMLRecord(claimResult);
                            totalProcessed++;
                        }
                    }

                    if (totalProcessed >= totalRecord) {
                        bordereauResult.setBordereauStatus(BordereauParseStatus.allUploaded);
                        BordereauParseStatusDescription = "All claims have been uploaded successfully";
                    } else if (totalProcessed < totalRecord && totalProcessed != 0) {
                        bordereauResult.setBordereauStatus(BordereauParseStatus.partialUpload);
                        BordereauParseStatusDescription = totalProcessed + " out of " + totalRecord + " claims have been uploaded";
                    } else if (totalProcessed == 0) {
                        bordereauResult.setBordereauStatus(BordereauParseStatus.allRejected);
                        BordereauParseStatusDescription = "All " + totalRecord + " claims have been rejected";
                    }

                } else {

                    bordereauResult.setBordereauStatus(BordereauParseStatus.error);
                    BordereauParseStatusDescription = "Incorrect File Version";

                }

                bordereauResult.setBordereau(doBordereau(file, fileName, bordereauResult.getBordereauStatus(), BordereauParseStatusDescription));
                bordereauService.saveObj(bordereauResult.getBordereau());

            } else {
                
                bordereauResult.setBordereauStatus(BordereauParseStatus.error);
                BordereauParseStatusDescription = "Error, Please try again";
                
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }

        return bordereauResult;
    }

    private ClaimResult saveXMLRecord(ClaimResult claimResult) {

        TransactionTemplate transactionTemplate = new TransactionTemplate(getTransactionManager());
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        try {

            final ClaimResult readOnlyXmlParseResult = claimResult;

            transactionTemplate.execute(
                    new TransactionCallbackWithoutResult() {

                        public void doInTransactionWithoutResult(TransactionStatus status) {

                            if (readOnlyXmlParseResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {
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

                            if (readOnlyXmlParseResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {
                                auditTrailService.logAuditLog(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, "", readOnlyXmlParseResult.getClaim());
                            }

                            if (readOnlyXmlParseResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)) {
                                auditTrailService.logAuditLog(readOnlyXmlParseResult.getClaim().getStatus(), readOnlyXmlParseResult.getClaim().getPreviousStatus(), readOnlyXmlParseResult.getClaim());
                                
                                if(readOnlyXmlParseResult.getHistory().size()>0){
                                    historyService.saveHistories(readOnlyXmlParseResult.getHistory());
                                }
                                
                            }

                        }
                    });
        } catch (TransactionException e) {
            // xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
        }

        return claimResult;
    }

    private Bordereau doBordereau(File file, String fileName, Object status, String BordereauParseStatusDescription) throws FileNotFoundException, IOException {

        Bordereau bordereau = new Bordereau();
        FileInputStream streamIn = new FileInputStream(file);
        byte fileContent[] = new byte[(int) file.length()];
        streamIn.read(fileContent);

        bordereau.setFileName(fileName);
        if (status != null) {
            bordereau.setStatus(status.toString());
        }

        bordereau.setFileBuffer(fileContent);
        bordereau.setDescription(BordereauParseStatusDescription);
        return bordereau;
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public void setEngineerReportService(EngineerReportService engineerReportService) {
        this.engineerReportService = engineerReportService;
    }

    public void setIncidentService(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    public void setInjuryService(InjuryService injuryService) {
        this.injuryService = injuryService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setWitnessService(WitnessService witnessService) {
        this.witnessService = witnessService;
    }

    public void setThirdPartyService(ThirdPartyService thirdPartyService) {
        this.thirdPartyService = thirdPartyService;
    }

    public void setVehicleHireService(VehicleHireService vehicleHireService) {
        this.vehicleHireService = vehicleHireService;
    }

    public void setSolicitorService(SolicitorService solicitorService) {
        this.solicitorService = solicitorService;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public void setInsurerAlliasService(InsurerAlliasService insurerAlliasService) {
        this.insurerAlliasService = insurerAlliasService;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public void setChoBandService(ChoBandService choBandService) {
        this.choBandService = choBandService;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    public void setHireMonitoringEcdService(HireMonitoringEcdService hireMonitoringEcdService) {
        this.hireMonitoringEcdService = hireMonitoringEcdService;
    }

    public void setHireMonitoringDetailService(HireMonitoringDetailService hireMonitoringDetailService) {
        this.hireMonitoringDetailService = hireMonitoringDetailService;
    }

    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) {
        this.insurerChorganisationService = insurerChorganisationService;
    }

    public void setBordereauService(BordereauService bordereauService) {
        this.bordereauService = bordereauService;
    }
}