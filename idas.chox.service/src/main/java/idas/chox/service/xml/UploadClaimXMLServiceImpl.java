package idas.chox.service.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.data.services.*;
import idas.chox.core.model.Bordereau;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.BordereauService;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.core.xmlValidation.BordereauParseStatus;
import idas.chox.core.xmlValidation.BordereauResult;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.readers.BordereauReader;
import idas.chox.service.xml.validations.BordereauFileValidation;
import idas.chox.service.xml.validations.BordereauSchemaValidation;
import idas.chox.service.xml.validations.CHOReferenceValidation;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.transaction.annotation.Propagation;
import org.w3c.dom.Document;
import org.springframework.transaction.annotation.Transactional;

public class UploadClaimXMLServiceImpl extends SecureDataService implements UploadClaimXMLService {

    private AuditTrailService auditTrailService;
    private BordereauService bordereauService;
    private BordereauReader bordereauReader;
    private BordereauSchemaValidation bordereauSchemaValidation;
    private BordereauFileValidation bordereauFileValidation;
    private ActivityFactory activityFactory;


//    protected static Log logger = LogFactory.getLog("chox");
    private static final Logger LOG = LoggerFactory.getLogger(UploadClaimXMLServiceImpl.class);

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public BordereauResult processClaimXMLFile(final File file, final String fileName) {
        BordereauResult bordereauResult = new BordereauResult();
        try {
            bordereauResult = process(file, fileName, bordereauResult);
        } catch (Exception ex) {
//            logger.error(ex);
            LOG.warn("Exception thrown uploading XML file: {}", ex.getMessage());
            bordereauResult.getMessage().add(ex.getMessage());
//            ex.printStackTrace();
        }
        return bordereauResult;
    }

    private BordereauResult process(final File file, final String fileName, BordereauResult bordereauResult) throws Exception {

        doProcessBordereauResult(file, fileName, bordereauResult);
//
//        List<ClaimResult> claimResults = new ArrayList<ClaimResult>();
//
//        for (ClaimResult claimResult : bordereauResult.getClaimResult()) {
//            if (claimResult.isValid() && claimResult.isDataValid()) {
//                claimResults.add(claimResult);
//            } else {
//                getHibernateTemplate().evict(claimResult.getClaim());
//            }
//        }
//
//        for (ClaimResult claimResult : claimResults) {
//            if (claimResult.isValid() && claimResult.isDataValid()) {
//                saveXMLRecord(claimResult);
//            }
//        }

        bordereauResult.setBordereau(doBordereau(file, fileName, bordereauResult.getBordereauStatus(), bordereauResult.getBordereauParseStatusDescription()));
        bordereauService.saveBordereau(bordereauResult.getBordereau());

        return bordereauResult;
    }

    private BordereauResult doProcessBordereauResult(final File file, final String fileName, BordereauResult bordereauResult) {

        bordereauFileValidation.validate(file, fileName, bordereauResult);

        if (bordereauResult.isValid()) {

            Document document = DocumentHelper.getDocumentFromFile(file);
            bordereauSchemaValidation.validate(document, bordereauResult);

            if (bordereauResult.isValid()) {

                try {

                    bordereauReader.execute(document, bordereauResult);

                    int totalRecord = bordereauResult.getClaimResult().size();
                    int totalProcessed = 0;

                    // CHECK DUPLICATE CHO REFERENCE PER XML
                    CHOReferenceValidation choReferenceValidation = new CHOReferenceValidation();

                    for (ClaimResult claimResult : bordereauResult.getClaimResult()) {

                        choReferenceValidation.validate(claimResult);
                        LOG.debug("Processing claim '{}'.", claimResult.getClaim().getChoReference());
                        if (claimResult.isValid() && claimResult.isDataValid()) {
                            //CALL WORKFLOW LOGIC
                            try {
                                LOG.debug("claimResult for claim '{}' is valid.", claimResult.getClaim().getChoReference());

                                if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {
                                    LOG.debug("Processing newClaim activity.");
                                    Activity activity = activityFactory.getActivity("newClaim");
                                    activity.processInBatch(claimResult.getClaim());
                                    LOG.debug("newClaim activity completed.");
                                } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)) {
                                    LOG.debug("Processing newInvoice activity.");
                                    claimResult.getClaim().setInvoice(claimResult.getInvoice());
                                    Activity activity = activityFactory.getActivity("newInvoice");
                                    activity.processInBatch(claimResult.getClaim());
                                    LOG.debug("newInvoice activity completed.");
                                }else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.tpiIntervention)){
                                    LOG.debug("Processing Tpi Invoice activity.");
                                    Activity activity = activityFactory.getActivity("TpiClaim");
                                    activity.processInBatch(claimResult.getClaim());
                                    LOG.debug("TpiClaim activity completed.");
                                }
                                totalProcessed++;
                            } catch (Exception ex) {
                                LOG.debug("Exception caught processing claim '{}': {}", claimResult.getClaim().getChoReference(), ex.getMessage());
                                claimResult.setValid(false);
                                claimResult.getMessage().add(ex.getMessage());
                            }
                        } else {
                            LOG.debug("claimResult not valid for claim: isValid={} isDataValid={}", claimResult.isValid(), claimResult.isDataValid());
//                             if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)) {
//                                 claimResult.getClaim().setInvoice(null);
//                             }
//                             getHibernateTemplate().evict(claimResult.getClaim()); - for some reason this causes the claim status not to be saved!!
//                             LOG.debug("Claim evicted.");
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
                } catch (Exception ex) {
                    bordereauResult.setBordereauStatus(BordereauParseStatus.error);
                    bordereauResult.setBordereauParseStatusDescription("Invalid Schema");
                }

            } else {
                bordereauResult.setBordereauStatus(BordereauParseStatus.error);
                bordereauResult.setBordereauParseStatusDescription("Invalid Schema");
            }

        } else {

            bordereauResult.setBordereauStatus(BordereauParseStatus.error);
            bordereauResult.setBordereauParseStatusDescription("Error, Please try again");
        }

        return bordereauResult;
    }

//    @Transactional
//    private ClaimResult saveXMLRecord(ClaimResult claimResult) {
//
//        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)) {
//            auditTrailService.logAuditLog(claimResult.getClaim().getStatus(), claimResult.getClaim().getPreviousStatus(), claimResult.getClaim());
//
//        }
//        return claimResult;
//    }
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

   

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public void setBordereauService(BordereauService bordereauService) {
        this.bordereauService = bordereauService;
    }

    public void setBordereauReader(BordereauReader bordereauReader) {
        this.bordereauReader = bordereauReader;
    }

    public void setBordereauSchemaValidation(BordereauSchemaValidation bordereauSchemaValidation) {
        this.bordereauSchemaValidation = bordereauSchemaValidation;
    }

    public void setBordereauFileValidation(BordereauFileValidation bordereauFileValidation) {
        this.bordereauFileValidation = bordereauFileValidation;
    }

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }
}
