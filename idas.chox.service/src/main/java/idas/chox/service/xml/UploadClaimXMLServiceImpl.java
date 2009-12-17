package idas.chox.service.xml;

import idas.chox.data.services.*;
import idas.chox.core.model.AutomaticRouting;
import idas.chox.core.model.Bordereau;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.core.services.BordereauService;
import idas.chox.core.services.BusinessRulesEngService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerAliasService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.core.xmlValidation.BordereauParseStatus;
import idas.chox.core.xmlValidation.BordereauResult;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.service.xml.validations.BordereauDataValidation;
import idas.chox.service.xml.validations.BordereauFileValidation;
import idas.chox.service.xml.validations.BordereauVersionValidation;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class UploadClaimXMLServiceImpl extends SecureDataService implements UploadClaimXMLService {

    private VehicleClassService vehicleClassService;
    private AuditTrailService auditTrailService;
    private ClaimService claimService;
    private InsurerAliasService insurerAliasService;
    private ChorganisationService chorganisationService;
    private BreBandService choBandService;
    private InsurerChorganisationService insurerChorganisationService;
    private BordereauService bordereauService;
    private BusinessRulesEngService businessRulesEngService;
    private AutomaticRoutingService automaticRoutingService;

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
        return doProcessClaimXMLFile(file, fileName);
    }

    public BordereauResult processBordereau(final File file, final String fileName) {
        return doProcessBordereauResult(file, fileName);
    }

    public ClaimResult claimRouting(List<String> choReferences, ClaimResult claimResult) {

        System.out.println("========================================================================");
        System.out.println("END: is Claim Valid?: " + claimResult.isValid());
        System.out.println("END: is Claim Data valid?: " + claimResult.isDataValid());
        System.out.println("END: Claim Process Status: " + claimResult.getClaimParseStatus());
        System.out.println("END: Claim Cho Ref: " + claimResult.getClaim().getChoReference());
        System.out.println("END: Claim Status: " + claimResult.getClaim().getStatus());



        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {

            if (claimResult.getClaim() != null) {

                // CHECK DUPLICATE
                if (!claimResult.getClaim().getChoReference().equalsIgnoreCase("") && claimResult.getClaim().getChoReference() != null) {

                    if (choReferences.contains(claimResult.getClaim().getChoReference().toLowerCase().trim())) {
                        claimResult.setValid(false);
                        claimResult.getMessage().add("Supplier Reference is already exist");
                    }

                }

                // CHECK AUTOMATIC ROUTING AND OWNERSHIP
                if (claimResult.getClaim().getThirdParty() != null) {
                    if (claimResult.getClaim().getThirdParty().getInsurer() != null) {

                        // CHECK WORKGROUP ENABLE
                        if (claimResult.getClaim().getThirdParty().getInsurer().isWorkgroupEnable()) {
                            // >> WORKGROUP ENABLED

                            // CHECK AUTOMATIC ENABLE
                            if (claimResult.getClaim().getThirdParty().getInsurer().isAutoRoutingEnable()) {
                                claimResult = doAutomaticClaimRoutingEnable(claimResult);
                            }

                        } else {

                            // >> NOT WORKGROUP ENABLED
                            claimResult = doWorkgroupDisable(claimResult);

                        }

                        doClaimOwnership(claimResult);

                    }
                }
            }

        }
        return claimResult;
    }

    public ClaimResult doAutomaticClaimRoutingEnable(ClaimResult claimResult) {

        int insurerId = claimResult.getClaim().getThirdParty().getInsurer().getId();
        List<AutomaticRouting> automaticRoutingMapping = automaticRoutingService.getObjects(insurerId);

        if (automaticRoutingMapping.size() > 0) {

            String policyNumber = claimResult.getClaim().getThirdParty().getPolicyNumber().trim();

            if (policyNumber != null && !policyNumber.equalsIgnoreCase("")) {

                for (AutomaticRouting automaticRouting : automaticRoutingMapping) {

                    NodeHelper nodeHelper = new NodeHelper();
                    if (nodeHelper.isRegularExpressionCheckPass(automaticRouting.getExpression(), policyNumber.toUpperCase())) {

                        claimResult.getClaim().setWorkgroup(automaticRouting.getWorkgroup());
                        claimResult.getClaim().setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                        break;
                    }
                }
            }

        } else {
            claimResult.setValid(false);
            claimResult.getMessage().add("Automatic Routing Mapping is Not Defined, Please contact CHOX Admin");
        }

        return claimResult;
    }

    private ClaimResult doWorkgroupDisable(ClaimResult claimResult) {
        claimResult.getClaim().setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        return claimResult;
    }

    private ClaimResult doClaimOwnership(ClaimResult claimResult) {

        if (claimResult.getClaim().getThirdParty().getInsurer().isClaimOwnershipEnable() && claimResult.getClaim().getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED)) {
            claimResult.getClaim().setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        }

        return claimResult;
    }

    public BordereauResult doProcessClaimXMLFile(final File file, final String fileName) {
        try {

            BordereauResult bordereauResult = doProcessBordereauResult(file, fileName);

            List<ClaimResult> claimResults = new ArrayList<ClaimResult>();

            for (ClaimResult claimResult : bordereauResult.getClaimResult()) {
                if (claimResult.isValid() && claimResult.isDataValid()) {
                    claimResults.add(claimResult);
                } else {
                    getHibernateTemplate().evict(claimResult.getClaim());
                }
            }

            for (ClaimResult claimResult : claimResults) {

                // System.out.println("========================================================================");
                // System.out.println("END: is Claim Valid?: " + claimResult.isValid());
                // System.out.println("END: is Claim Data valid?: " + claimResult.isDataValid());
                // System.out.println("END: Claim Process Status: " + claimResult.getClaimParseStatus());
                // System.out.println("END: Claim Cho Ref: " + claimResult.getClaim().getChoReference());
                // System.out.println("END: Claim Status: " + claimResult.getClaim().getStatus());

                if (claimResult.isValid() && claimResult.isDataValid()) {
                    saveXMLRecord(claimResult);
                }
            }

            bordereauResult.setBordereau(doBordereau(file, fileName, bordereauResult.getBordereauStatus(), bordereauResult.getBordereauParseStatusDescription()));
            bordereauService.saveObj(bordereauResult.getBordereau());

            return bordereauResult;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public BordereauResult doProcessBordereauResult(final File file, final String fileName) {

        BordereauResult bordereauResult = new BordereauResult();

        bordereauResult = new BordereauFileValidation().validate(file, fileName, bordereauResult);

        if (bordereauResult.isValid()) {

            bordereauResult = new BordereauVersionValidation().validate(file, fileName, bordereauResult);

            if (bordereauResult.isValid()) {

                bordereauResult = new BordereauDataValidation().validate(file, fileName, bordereauResult, claimService, chorganisationService, choBandService, vehicleClassService, insurerAliasService, insurerChorganisationService, businessRulesEngService);

                int totalRecord = bordereauResult.getClaimResult().size();
                int totalProcessed = 0;

                // CHECK DUPLICATE CHO REFERENCE PER XML
                List<String> choReferences = new ArrayList<String>();

                for (ClaimResult claimResult : bordereauResult.getClaimResult()) {

                    // OTHER BUSINESS LOGIC
                    claimResult = claimRouting(choReferences, claimResult);

                    if (claimResult.isValid() && claimResult.isDataValid()) {
                        choReferences.add(claimResult.getClaim().getChoReference().toLowerCase().trim());
                        totalProcessed++;
                    }
                }

                if (totalProcessed >= totalRecord) {
                    bordereauResult.setBordereauStatus(BordereauParseStatus.allUploaded);
                    bordereauResult.setBordereauParseStatusDescription("All claims have been uploaded successfully");
                } else if (totalProcessed < totalRecord && totalProcessed != 0) {
                    bordereauResult.setBordereauStatus(BordereauParseStatus.partialUpload);
                    bordereauResult.setBordereauParseStatusDescription(totalProcessed + " out of " + totalRecord + " claims have been uploaded");
                } else if (totalProcessed == 0) {
                    bordereauResult.setBordereauStatus(BordereauParseStatus.allRejected);
                    bordereauResult.setBordereauParseStatusDescription("All " + totalRecord + " claims have been rejected");
                }

            } else {
                bordereauResult.setBordereauStatus(BordereauParseStatus.error);
                bordereauResult.setBordereauParseStatusDescription("Incorrect File Version");
            }

        } else {

            bordereauResult.setBordereauStatus(BordereauParseStatus.error);
            bordereauResult.setBordereauParseStatusDescription("Error, Please try again");
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

                            claimService.saveObjectForXMLUploader(readOnlyXmlParseResult);

                            if (readOnlyXmlParseResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {

                                auditTrailService.logAuditLog(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, "", readOnlyXmlParseResult.getClaim());

                                if (readOnlyXmlParseResult.getClaim().getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED)) {
                                    auditTrailService.logAuditLog(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, readOnlyXmlParseResult.getClaim(), 1);
                                } else if (readOnlyXmlParseResult.getClaim().getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)) {
                                    auditTrailService.logAuditLog(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, readOnlyXmlParseResult.getClaim(), 1);
                                    auditTrailService.logAuditLog(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, readOnlyXmlParseResult.getClaim(), 2);
                                }
                            }

                            if (readOnlyXmlParseResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)) {
                                auditTrailService.logAuditLog(readOnlyXmlParseResult.getClaim().getStatus(), readOnlyXmlParseResult.getClaim().getPreviousStatus(), readOnlyXmlParseResult.getClaim());

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

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public void setInsurerAliasService(InsurerAliasService insurerAliasService) {
        this.insurerAliasService = insurerAliasService;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public void setBreBandService(BreBandService choBandService) {
        this.choBandService = choBandService;
    }

    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) {
        this.insurerChorganisationService = insurerChorganisationService;
    }

    public void setBordereauService(BordereauService bordereauService) {
        this.bordereauService = bordereauService;
    }

    /**
     * @return the businessRulesEngService
     */
    public BusinessRulesEngService getBusinessRulesEngService() {
        return businessRulesEngService;
    }

    /**
     * @param businessRulesEngService the businessRulesEngService to set
     */
    public void setBusinessRulesEngService(BusinessRulesEngService businessRulesEngService) {
        this.businessRulesEngService = businessRulesEngService;
    }

    public void setAutomaticRoutingService(AutomaticRoutingService automaticRoutingService) {
        this.automaticRoutingService = automaticRoutingService;
    }
}
