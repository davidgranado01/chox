package idas.chox.service.xml;

import idas.chox.core.model.Bordereau;
import idas.chox.core.model.Claim;
import idas.chox.core.model.UploadedXMLClaimsDetail;
import idas.chox.core.services.BordereauService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.data.services.*;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.services.UploadedXMLClaimsDetailService;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.readers.BordereauReader;
import idas.chox.service.xml.validations.BordereauSchemaValidation;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.*;

public class UploadClaimXMLServiceImpl extends SecureDataService implements UploadClaimXMLService {

    private BordereauService bordereauService;
    private BordereauReader bordereauReader;
    private ActivityFactory activityFactory;
    private String errorMessage;
    private String successMessage;
    private UploadedXMLClaimsDetailService uploadedXMLClaimsDetailService;
    private BordereauSchemaValidation bordereauSchemaValidation;
    protected static String NEW_UPLOADED_XML_FILE_STATUS = "Waiting to be Processed";
    protected static String NEW_UPLOADED_XML_FILE_DESCRIPTION = "File is waiting to be processed";
    private static final Logger LOG = LoggerFactory.getLogger(UploadClaimXMLServiceImpl.class);

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public void setBordereauService(BordereauService bordereauService) {
        this.bordereauService = bordereauService;
    }

    public void setUploadedXMLClaimsDetailService(UploadedXMLClaimsDetailService claimsDetailService) {
        this.uploadedXMLClaimsDetailService = claimsDetailService;
    }

    public void setBordereauSchemaValidation(BordereauSchemaValidation bordereauSchemaValidation) {
        this.bordereauSchemaValidation = bordereauSchemaValidation;
    }

    @Override
            /*
             *  removed this Transactional annotation as this is no effect when processing claims in activity.
             */
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean doProcessBordereauResult(ClaimResult claimResult, List<String> choReferences) {

        try {

            bordereauReader.execute(claimResult);

        } catch (Exception ex) {
            LOG.error("Exception thrown in reading the Bordereau file");
            return false;
        }

        validate(claimResult, choReferences);
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
                } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.hireMonitoringAndNewInvoice)) {
                    LOG.debug("Processing hire monitering activity.");
                    claimResult.getClaim().setInvoice(claimResult.getInvoice());
                    Activity activity = activityFactory.getActivity("hireMonitering");
                    /*
                     *  this is set to true to identify the activity process is called from xml upload stage not from ui ( proceed button in ui).
                     */
                    activity.setXmlActivityProcessing(true);
                    activity.processInBatch(claimResult.getClaim());
                    LOG.debug("hire monitering and newInvoice activity completed.");
                }else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.tpiIntervention)) {
                    LOG.debug("Processing Tpi Invoice activity.");
                    claimResult.getClaim().setInvoice(claimResult.getInvoice());
                    Activity activity = activityFactory.getActivity("newInvoice");
                    activity.processInBatch(claimResult.getClaim());
                    LOG.debug("TpiClaim activity completed.");
                }
                return true;
            } catch (Exception ex) {
                LOG.debug("Exception caught processing claim '{}': {}", claimResult.getClaim().getChoReference(), ex.getMessage());
                claimResult.setValid(false);
                claimResult.getMessage().add(ex.getMessage());
                return false;
            }
        } else {
            LOG.debug("claimResult not valid for claim: isValid={} isDataValid={}", claimResult.isValid(), claimResult.isDataValid());
            return false;
        }
    }

    @Override
    public List<ClaimResult> formClaimResults(Document document) throws Exception {
        Element root = document.getDocumentElement();
        List<ClaimResult> claimElements = new ArrayList<ClaimResult>();

        List<Element> rentals = XMLUtils.getElements(document, root, "rental");

        if (rentals != null && rentals.size() > 0) {

            for (Element e : rentals) {

                ClaimResult claimResult = new ClaimResult();
                claimResult.setElement(e);
                claimResult.setCheckDataValid(true);
                claimResult.setDataValid(true);
                claimResult.setValid(true);
                claimElements.add(claimResult);
            }
        }
        return claimElements;
    }

    private void validate(ClaimResult claimResult, List<String> choReferences) {
        LOG.debug("Validating CHO references are unique");
//        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {

        if (claimResult.getClaim() != null) {

            // CHECK DUPLICATE
            if (claimResult.getClaim().getChoReference() != null && !claimResult.getClaim().getChoReference().equalsIgnoreCase("")) {

                if (choReferences.contains(claimResult.getClaim().getChoReference().toLowerCase().trim())) {
                    LOG.info("Duplicate Supplier Reference found: {}", claimResult.getClaim().getChoReference());
                    claimResult.setValid(false);
                    claimResult.setDuplicateClaimInSameXmlFile(true);
                    claimResult.getMessage().add("Duplicate Supplier Reference -  Supplier Reference already exists in bordereau");

                } else {
                    choReferences.add(claimResult.getClaim().getChoReference().toLowerCase().trim());
                }
            }


        }
    }

    public void setBordereauReader(BordereauReader bordereauReader) {
        this.bordereauReader = bordereauReader;
    }

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }

    @Override
    public boolean validateFile(File uploadedFile) {
        return false;
    }

    @Override
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean processFile(int bordereauId, Map session) {
        int totalRecord = 0;
        int totalProcessed = 0;
        Document document = null;
        List<ClaimResult> claimResults = null;
        List<UploadedXMLClaimsDetail> claimsDetails = new ArrayList<UploadedXMLClaimsDetail>();
        List<String> choReferences = new ArrayList<String>();

        if (!isValidBordereauId(bordereauId)) {
            return false;
        }

        Bordereau bordereau = getBordereauFromId(bordereauId);

        if (!isAutherisedUser(bordereau.getCreatedBy().getChorganisation().getId(), bordereau.getFileName())) {
            return false;
        }
        
        if(bordereau.isBeingProcessed()){
            setErrorMessage("This file is being processed by another user. Please wait until processing finished and referesh to see the processed claim details.");
            return false;
        }
        
        if (bordereau.isProcessed() && !bordereau.isValid()) {
            if (!bordereau.isValid()) {
                LOG.error("Invalid schema found in this file : {}", bordereau.getFileName());
                setErrorMessage("Invalid Schema.");
                return false;
            }
            LOG.error("this file have been processed already: {}", bordereau.getFileName());
            setErrorMessage("This bordereau has already been processed.");
            return false;
        }


        InputStream inputStream = new ByteArrayInputStream(bordereau.getFileBuffer());

        try {
            document = DocumentHelper.getDocumentFromStream(inputStream);
        } catch (Exception ex) {
            LOG.error("Exception thrown creating document from bordereau with id={} : {}", bordereau.getId(), ex.getMessage());
            setErrorMessage("Error occured while processing Bordereau.");
            return false;
        }

        setBordereauProcessingStatus(bordereau);

        try {
            claimResults = formClaimResults(document);
            totalRecord = claimResults.size();
        } catch (Exception ex) {
            LOG.error("Error thrown while getting claims from brodereau with is={} : {}", bordereau.getId(), ex.getMessage());
            setErrorMessage("An unexpected error occured while processing this Bordereau.");
            setBordreauProcessFilureStatus(bordereau);
            return false;
        }
        /*
         * processing claims begin here
         * each claim in claimResults is processed , saved then evicted from cache one by one.
         * xmlClaimsDetail is used to give live update to the front end by putting these details in session and for future reference it is saved in DB.
         */
        try {
            for (ClaimResult claimResult : claimResults) {
                UploadedXMLClaimsDetail xmlClaimsDetail = new UploadedXMLClaimsDetail();

                if (doProcessBordereauResult(claimResult, choReferences)) {
                    totalProcessed++;
                    xmlClaimsDetail.setValid(true);
                } else {
                    xmlClaimsDetail.setValid(false);
                }

                xmlClaimsDetail.setBordereauId(bordereau.getId());
                xmlClaimsDetail.setProcessStatus(claimResult.getProcessStatus());

                if (!claimResult.getMessage().isEmpty()) {
                    xmlClaimsDetail.setMessage(claimResult.getMessage().toString());
                } else {
                    xmlClaimsDetail.setMessage("");
                }

                xmlClaimsDetail.setRemark(claimResult.getUploadedStatus());
                if (claimResult.getClaim() != null && claimResult.getClaim().getChoReference() != null) {
                    xmlClaimsDetail.setChoReference(claimResult.getClaim().getChoReference());
                    if (claimResult.getClaim().getId() != null && claimResult.getClaimStatus() != null && !claimResult.getClaimStatus().equals("")) {
                        if (claimResult.isDuplicateClaimInSameXmlFile()) {
                            xmlClaimsDetail.setClaimId(0);
                            xmlClaimsDetail.setClaimStatus("N/A");
                        } else {
                            xmlClaimsDetail.setClaimId(claimResult.getClaim().getId());
                            xmlClaimsDetail.setClaimStatus(claimResult.getClaimStatus());
                        }
                        evictClaim(claimResult.getClaim());
                        LOG.debug("Claim evicted.");
                    } else {
                        xmlClaimsDetail.setClaimStatus("N/A");
                    }
                }
                claimsDetails.add(0, xmlClaimsDetail);
                synchronized (session) {
                    session.put("claimsDetails", claimsDetails);
                }
                LOG.debug("putting claimDetails into session total size is: {}", claimsDetails.size());
                LOG.debug("{} of {} claims have been processed", totalRecord, totalProcessed);
            }
        } catch (Throwable ex) {
            LOG.error("Unexpected error thrown while processing claim : {}", ex.getMessage());
            session.put("claimsDetails", null);
            setErrorMessage("An unexpected error has occured - please report to CHOX support.");
            setBordreauProcessFilureStatus(bordereau);
            return false;
        }

        /*
         * end of processing claim.
         */

        if (totalProcessed >= totalRecord) {
            bordereau.setStatus("All Uploaded");
            bordereau.setDescription("All claims have been uploaded successfully");
        } else if (totalProcessed < totalRecord && totalProcessed != 0) {
            bordereau.setStatus("Partially Uploaded");
            bordereau.setDescription(totalProcessed + " out of " + totalRecord + " claims have been uploaded");
        } else if (totalProcessed == 0) {
            bordereau.setStatus("All Rejected");
            bordereau.setDescription("All " + totalRecord + " claims have been rejected");
        }
        
        bordereau.setProcessed(true);
        setSuccessMessage("The Bordereau has been processed successfully.");
        uploadedXMLClaimsDetailService.saveUploadedXMLClaimsDetails(claimsDetails);
        bordereauService.saveBordereau(bordereau);
        bordereau.setBeingProcessed(false);
        LOG.debug("This file has been processed successfully: {}", bordereau.getFileName());
        return true;

    }

    @Override
    public boolean saveUploadedFile(File uploadedFile, String uploadedFileFileName) {
        
        List<ClaimResult> claimResults = null;
        Document document = null;
        FileInputStream streamIn = null;
        Bordereau bordereau = new Bordereau();
        
        try {
            streamIn = new FileInputStream(uploadedFile);
        } catch (FileNotFoundException ex) {
            setErrorMessage(ex.getMessage());
            return false;
        }

        byte fileContent[] = new byte[(int) uploadedFile.length()];
        try {
            streamIn.read(fileContent);
        } catch (IOException ex) {
            setErrorMessage(ex.getMessage());
            return false;
        }
        
        try {
            document = DocumentHelper.getDocumentFromFile(uploadedFile);
            if (document == null) {
                LOG.error("Could not create document from file : {}", uploadedFile.getAbsolutePath());
                setErrorMessage("File is not a valid file.");
                return false;
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown in saving file while writing to document : {}", ex.getMessage());
            bordereau.setStatus("Error");
            bordereau.setDescription("Invalid Schema");
            saveBordereau( bordereau, uploadedFile,  uploadedFileFileName,  fileContent);
            /*
             * returning true cos there is no error message to display. Bordereau file is set with error discription and error status.
             */
            return true;
        }
        bordereau.setValid(true);
        bordereauSchemaValidation.validate(document, bordereau);
        if (!bordereau.isValid()) {
            bordereau.setStatus("Error");
            bordereau.setDescription("Invalid Schema");
            saveBordereau( bordereau, uploadedFile,  uploadedFileFileName,  fileContent);
            /*
             * returning true cos there is no error message to display. Bordereau file is set with error discription and error status.
             */
            return true;
        } else {
            bordereau.setStatus(NEW_UPLOADED_XML_FILE_STATUS);
            bordereau.setDescription(NEW_UPLOADED_XML_FILE_DESCRIPTION);
        }
        try {
            claimResults = formClaimResults(document);
            bordereau.setTotalClaims(claimResults.size());
        } catch (Exception ex) {
            LOG.error("Error thrown while getting claims from document, error message is : {}", ex.getMessage());
            bordereau.setStatus("Error");
            bordereau.setDescription("Invalid Schema");
            saveBordereau( bordereau, uploadedFile,  uploadedFileFileName,  fileContent);
            /*
             * returning true cos there is no error message to display. Bordereau file is set with error discription and error status.
             */
            return true;
        }
        saveBordereau( bordereau, uploadedFile,  uploadedFileFileName,  fileContent);
        LOG.debug("Uploaded file '{}' has been saved successfully.", bordereau.getFileName());
        setSuccessMessage("File has been uploaded successfully");
        return true;

    }

    @Override
    public void evictClaim(Claim claim) {
        getHibernateTemplate().flush();
        getHibernateTemplate().evict(claim);
        LOG.debug("Claim evicted.");
    }
    
    private void saveBordereau(Bordereau bordereau,File uploadedFile, String uploadedFileFileName, byte fileContent[]){
        bordereau.setFileSize((Long) uploadedFile.length());
        bordereau.setFileName(uploadedFileFileName);
        bordereau.setFileBuffer(fileContent);
        bordereau.setProcessed(false);
        bordereauService.saveBordereau(bordereau);
    }

    private Bordereau getBordereauFromId(int bordereauId) {
        Bordereau bordereau = null;
        return bordereau = bordereauService.getBordereauById(bordereauId);
    }

    private boolean isValidBordereauId(int bordereauId) {
        if (bordereauId <= 0) {
            LOG.error("Bordereau not found: id={}", bordereauId);
            setErrorMessage("Bordereau not found.");
            return false;
        }
        return true;
    }

    private boolean isAutherisedUser(int choId, String fileName) {

        if (!getCurrentUser().getChorganisation().getId().equals(choId)) {
            LOG.error("Un authOrised user trying to process the file : file name :{}, user name : {}", fileName, getCurrentUser().getUserName());
            setErrorMessage("You do not have permission to process this file. Please contact CHOX support.");
            return false;
        }
        return true;
    }

    private void setBordereauProcessingStatus(Bordereau bordereau) {
        bordereau.setStatus("Processing..");
        bordereau.setBeingProcessed(true);
        bordereau.setDescription("File is being processed on the server");
        bordereauService.saveBordereau(bordereau);
    }

    private void setBordreauProcessFilureStatus(Bordereau bordereau) {
        bordereau.setStatus(NEW_UPLOADED_XML_FILE_STATUS);
        bordereau.setDescription(NEW_UPLOADED_XML_FILE_DESCRIPTION);
        bordereau.setBeingProcessed(false);
        bordereauService.saveBordereau(bordereau);
    }
}
