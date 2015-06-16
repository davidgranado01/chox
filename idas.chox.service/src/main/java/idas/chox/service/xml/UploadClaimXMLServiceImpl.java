package idas.chox.service.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.model.Bordereau;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.History;
import idas.chox.core.model.Task;
import idas.chox.core.model.TaskType;
import idas.chox.core.model.UploadedXMLClaimsDetail;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.BordereauService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.TaskService;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.services.UploadedXMLClaimsDetailService;
import idas.chox.core.services.UserService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.workflow.Activity;
import idas.chox.core.xmlValidation.BordereauParseStatus;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.data.services.SecureDataService;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.workflow.activities.ActivityEvent;
import idas.chox.service.workflow.activities.ActivityEventGenerator;
import idas.chox.service.workflow.activities.InsurerUpload;
import idas.chox.service.workflow.activities.NewInvoice;
import idas.chox.service.workflow.activities.NewSupplementaryInvoice;
import idas.chox.service.xml.readers.BordereauReader;
import idas.chox.service.xml.validations.BordereauSchemaValidation;

public class UploadClaimXMLServiceImpl extends SecureDataService implements UploadClaimXMLService {

    private static final Logger LOG = LoggerFactory.getLogger(UploadClaimXMLServiceImpl.class);
    private static final Object LOCK = new Object();
    private static final String NEW_UPLOADED_XML_FILE_STATUS = "Waiting to be Processed";
    private static final String NEW_UPLOADED_XML_FILE_DESCRIPTION = "File is waiting to be processed";

    private BordereauService bordereauService;
    private BordereauReader bordereauReader;
    private BreBandService breBandService;
    private ActivityFactory activityFactory;
    private String errorMessage;
    private String successMessage;
    private UploadedXMLClaimsDetailService uploadedXMLClaimsDetailService;
    private ClaimService claimService;
    private UserService userService;
    private TaskService taskService;
    private BordereauSchemaValidation bordereauSchemaValidation;
    protected ActivityEventGenerator activityEventGenerator;

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

    public void setActivityEventGenerator(ActivityEventGenerator activityEventGenerator) {
        this.activityEventGenerator = activityEventGenerator;
    }

    public BreBandService getBreBandService() {
        return breBandService;
    }

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setUploadedXMLClaimsDetailService(UploadedXMLClaimsDetailService claimsDetailService) {
        this.uploadedXMLClaimsDetailService = claimsDetailService;
    }

    public void setBordereauSchemaValidation(BordereauSchemaValidation bordereauSchemaValidation) {
        this.bordereauSchemaValidation = bordereauSchemaValidation;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public boolean doProcessBordereauResult(ClaimResult claimResult, List<String> choReferences) {

        try {
            claimResult.setValid(true);
            bordereauReader.execute(claimResult);
        } catch (Exception ex) {
            LOG.error("Exception thrown when reading the Bordereau file: {}", ex.getMessage(), ex);
            return false;
        }

        validate(claimResult, choReferences);

        if (claimResult.isValid() && claimResult.isDataValid()) {
            Claim claim = claimResult.getClaim();
            LOG.debug("Processing claim '{}'.", claim.getChoReference());
            //CALL WORKFLOW LOGIC
            try {
                LOG.debug("claimResult for claim '{}' is valid.", claimResult.getClaim().getChoReference());

                if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_CLAIM)
                        || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_COLLABORATION_CLAIM)
                        || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_CLAIM)
                        || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUBSCRIBER_CLAIM)
                        || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_FIXEDFEE_CLAIM)) {
                    LOG.debug("Processing '{}' activity.", claimResult.getClaimParseStatus());
                    Activity activity = activityFactory.getActivity("newClaim");
                    activity.processInBatch(claim);
                    LOG.debug("newClaim activity completed.");

                    if (claim.getChorganisation().isTaskManagementEnable()
                            && claim.getChorganisation().isAllowEngineersInspectionTask()
                            && !ClaimType.isInsurerUpload(claim.getClaimType())
                            && claim.getHireMonitoringDetail() != null && !claim.getHireMonitoringDetail().isIsNFInsurerManagingRepair()
                            && claim.isManagingRepair()) {
                        // Add Task to prompt for Engineers Inspection [requirement 8.6.4]
                        Task task = new Task();
                        task.setComplete(Boolean.FALSE);
                        task.setDescription("The details of the claim indicate that neither the CHO nor the Non-Fault Insurer are managing the repair. Please contact the TPI if an Engineers Inspection is required.");
                        task.setDueDate(DateHelper.getCurrentDateTime());
                        task.setType(TaskType.ENG_INSPECTION.getDescription());
                        task.setVisibility(2);
                        task.setInsurer(Boolean.FALSE);
                        task.setRaisedBy(userService.findByUserName("system"));
                        task.setClaim(claim);
                        try {
                            taskService.createNewTask(task);
                        } catch (Exception ex) {
                            LOG.error("Exception creating Engineer Inspection task (nobody Managing Repair)) for CHO on claim '{}': {}", claim.getChoReference(), ex);
                        }
                    }
                } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_INVOICE)
                        || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_VS_INSURER_INVOICE)
                        || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION)) {
                    // Check we have a BRE band
                    if (breBandService.getBreBand(claimResult.getClaim().getChorganisation().getId(), claimResult.getClaim().getInsurer().getId()) == null) {
                        throw new Exception("No BRE Band mapping. Please contact CHOX Support.");
                    }
                    LOG.debug("Processing newInvoice activity.");
                    claim.setInvoice(claimResult.getInvoice());
                    Activity activity = activityFactory.getActivity("newInvoice");
                    activity.processInBatch(claim);
                    RulesEngineResponse breResponse = ((NewInvoice) activity).getBreResponse();
                    for (History history : History.New(breResponse)) {
                        if (history.getType().equals("ERROR") && history.getIsPublic()) {
                            claimResult.getBreMessage().add(history.getNarrative());
                        }
                    }
                    LOG.debug("newInvoice activity completed.");
                } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUPPLEMENTARY_INVOICE)) {
                    // Check we have a BRE band
                    if (breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()) == null) {
                        throw new Exception("No BRE Band mapping. Please contact CHOX Support.");
                    }
                    LOG.debug("Processing supplementaryInvoice (activities NewSupplementaryInvoice followed by NewInvoice).");
                    claim.setInvoice(claimResult.getInvoice());
                    Activity activity = activityFactory.getActivity("supplementaryInvoice");
                    activity.processInBatch(claim);
                    RulesEngineResponse breResponse = ((NewSupplementaryInvoice) activity).getBreResponse();
                    for (History history : History.New(breResponse)) {
                        if (history.getType().equals("ERROR") && history.getIsPublic()) {
                            claimResult.getBreMessage().add(history.getNarrative());
                        }
                    }
                    LOG.debug("newInvoice activity completed.");
                } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_NEW_SUPPLEMENTARY_INVOICE)) {
                    // Check we have a BRE band
                    if (breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()) == null) {
                        throw new Exception("No BRE Band mapping. Please contact CHOX Support.");
                    }
                    LOG.debug("Processing supplementaryInsurerInvoice (activities NewSupplementaryInvoice followed by InsurerUpload).");
                    claim.setInvoice(claimResult.getInvoice());
                    LOG.debug("Invoice set for claim '{}': {}", claimResult.getClaim().getChoReference(), claimResult.getClaim().getInvoice());
                    Activity activity = activityFactory.getActivity("supplementaryInsurerInvoice");
                    activity.processInBatch(claim);
                    RulesEngineResponse breResponse = ((NewSupplementaryInvoice) activity).getBreResponse();
                    for (History history : History.New(breResponse)) {
                        if (history.getType().equals("ERROR") && history.getIsPublic()) {
                            claimResult.getBreMessage().add(history.getNarrative());
                        }
                    }
                    LOG.debug("supplementaryInsurerInvoice activity completed.");
                } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE)
                        || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_HIRE_MONITORING_AND_NEW_INVOICE)) {
                    // Check we have a BRE band
                    if (breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()) == null) {
                        throw new Exception("No BRE Band mapping. Please contact CHOX Support.");
                    }
                    LOG.debug("Processing hire monitoring and newInvoice activity.");

                    // Check we have an original or initial ECD. If not, we'll create one using the hire-end date
                    // N.B. Requested under Phase 5 Sprint 10 todo item 5.10.2 Hire Monitoring xml upload
                    checkECD(claim);

                    Activity activity = activityFactory.getActivity("awaitingCarHireInfo");
                    /*
                     *  this is set to true to identify the activity process is called from xml upload stage not from ui ( proceed button in ui).
                     */
                    activity.setXmlActivityProcessing(true);
                    activity.processInBatch(claim);
                    LOG.debug("hire monitering activity completed.");
                    claim.setInvoice(claimResult.getInvoice());
                    if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE)) {
                        activity = activityFactory.getActivity("newInvoice");
                        activity.processInBatch(claim);
                        RulesEngineResponse breResponse = ((NewInvoice) activity).getBreResponse();
                        for (History history : History.New(breResponse)) {
                            if (history.getType().equals("ERROR") && history.getIsPublic()) {
                                claimResult.getBreMessage().add(history.getNarrative());
                            }
                        }
                        LOG.debug("NewInvoice activity completed.");
                    } else {
                        checkECD(claim);
                        activity = activityFactory.getActivity("insurerUpload");
                        activity.setXmlActivityProcessing(true);
                        activity.processInBatch(claim);
                        RulesEngineResponse breResponse = ((InsurerUpload) activity).getBreResponse();
                        for (History history : History.New(breResponse)) {
                            if (history.getType().equals("ERROR")) {
                                claimResult.getBreMessage().add(history.getNarrative());
                            }
                        }

                        LOG.debug("Insurer upload activity completed.");

                    }
                } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING)
                        || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_HIRE_MONITORING)) {
                    LOG.debug("Processing hire monitering activity.");

                    // Check we have an original or initial ECD. If not, we'll create one using the hire-end date
                    // N.B. Requested under Phase 5 Sprint 10 todo item 5.10.2 Hire Monitoring xml upload
                    checkECD(claim);

                    Activity activity = activityFactory.getActivity("awaitingCarHireInfo");
                    /*
                     *  this is set to true to identify the activity process is called from xml upload stage not from ui ( proceed button in ui).
                     */
                    activity.setXmlActivityProcessing(true);
                    activity.processInBatch(claim);
                    LOG.debug("hire monitering activity completed.");

                } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_INVOICE)) {
                    // Check we have a BRE band
                    if (breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()) == null) {
                        throw new Exception("No BRE Band mapping. Please contact CHOX Support.");
                    }
                    LOG.debug("Processing insurer upload activity.");

                    // Check we have an original or initial ECD. If not, we'll create one using the hire-end date
                    claim.setInvoice(claimResult.getInvoice());
                    checkECD(claim);

                    Activity activity = activityFactory.getActivity("insurerUpload");
                    activity.setXmlActivityProcessing(true);
                    activity.processInBatch(claimResult.getClaim());
                    RulesEngineResponse breResponse = ((InsurerUpload) activity).getBreResponse();
                    for (History history : History.New(breResponse)) {
                        if (history.getType().equals("ERROR")) {
                            claimResult.getBreMessage().add(history.getNarrative());
                        }
                    }

                    LOG.debug("Insurer upload activity completed.");

                } else {
                    // If Claim has been updated but no activity has been called, we need to generate  a Claimupdate Event
                    if (claimResult.getProcessStatus().equals("Updated")) {
                        LOG.debug("Processed bordereau and no activity ran but claim updated: generatung ClaimUpdatedEvent");
                        activityEventGenerator.generate(claim, ActivityEvent.CLAIM_UPDATED_EVENT);
                    }
                }

                if (claimResult.isCheckForRepairAnomalies()) {
                    LOG.debug("Checking for repair anomalies.");
                    claimService.checkRepairBookedInDateAnomaly(claim);
                }

                if (claimResult.isCheckForTotalLossAnomalies()) {
                    LOG.debug("Checking for repair anomalies.");
                    claimService.checkTotalLossAnomaly(claim);
                }

            } catch (Exception ex) {
                if (claimResult.getClaim() != null) {
                    LOG.error("Exception caught processing claim '{}': ", claimResult.getClaim().getChoReference(), ex);
                } else {
                    LOG.error("Exception caught processing claim (no claim in claimResult): {}", ex.getMessage());
                }
                if (ex.getCause() != null) {
                    LOG.error("Caused by: {}", ex.getCause().getMessage());
                }
                LOG.debug("claimResult is : {}", claimResult);
                claimResult.setValid(false);
                claimResult.setDataValid(false);

                if (ex instanceof DataIntegrityViolationException) {
                    claimResult.getMessage().add("Some of the values provided for this claim/invoice are incorrect. Please contact Chox support.");
                    claimResult.getClaim().setId(0);
                } else if (ex.getMessage() != null) {
                    claimResult.getMessage().add(ex.getMessage());
                } else if (ex.getCause() != null && ex.getCause().getMessage() != null) {
                    claimResult.getMessage().add(ex.getCause().getMessage());
                } else {
                    claimResult.getMessage().add("No error message available.");
                }
                return false;
            }
        } else {
            LOG.debug("claimResult not valid for claim: isValid={} isDataValid={}", claimResult.isValid(), claimResult.isDataValid());
            return false;
        }

        return true;
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

    private Object getSessionLock(Map session) {
        Object result = session.get("SESSION_LOCK");
        if (result == null) {
            // only if there is no session-lock object in the session we apply the global lock
            synchronized (LOCK) {
                // as it can be that another thread has updated the session-lock object in the meantime, we have to read it again from the session and create it only if it is not there yet!
                result = session.get("SESSION_LOCK");
                if (result == null) {
                    result = new Object();
                    session.put("SESSION_LOCK", result);
                }
            }
        }
        LOG.debug("Returning session lock '{}'", result);
        return result;

    }

    @Override
    public boolean processFile(int bordereauId, Map session) {
        int noClaims = 0;
        int noProcessed = 0;
        int noSuccessfullyProcessed = 0;
        Document document;
        List<ClaimResult> claimResults;
        List<UploadedXMLClaimsDetail> claimsDetails = new CopyOnWriteArrayList<UploadedXMLClaimsDetail>();
        List<String> choReferences = new ArrayList<String>();

        if (!isValidBordereauId(bordereauId)) {
            return false;
        }
        LOG.debug("Processing bordereau with id={}", bordereauId);

        Bordereau bordereau = getBordereauFromId(bordereauId);
        WebUser user = bordereau.getCreatedBy();
        Integer orgId;
        if (user.isAnInsurer()) {
            orgId = user.getInsurer().getId();
        } else {
            orgId = user.getChorganisation().getId();
        }

        if (!isAutherisedUser(orgId, bordereau.getFileName())) {
            LOG.error("User (id={}) not authorised to process file.", user.getId());
            return false;
        }

        if (bordereau.isBeingProcessed()) {
            LOG.warn("Bordereau (id={}) is being processed by another user.", bordereauId);
            setErrorMessage("This file is being processed by another user. Please wait until processing finished and referesh to see the processed claim details.");
            return false;
        }

        if (!bordereau.isValid()) {
            LOG.error("Invalid schema found in file : {} [id={}]", bordereau.getFileName(), bordereau.getId());
            setErrorMessage("Invalid Schema.");
            return false;
        }
        if (bordereau.isProcessed()) {
            LOG.error("This file has already been processed: {} [id={}]", bordereau.getFileName(), bordereau.getId());
            setErrorMessage("This bordereau has already been processed.");
            return false;
        }

        InputStream inputStream = new ByteArrayInputStream(bordereau.getFileBuffer());

        try {
            document = DocumentHelper.getDocumentFromStream(inputStream);
        } catch (ParserConfigurationException ex) {
            LOG.error("Exception thrown creating document from bordereau with id={}:\n", bordereau.getId(), ex);
            setErrorMessage("Error occurred while processing Bordereau.");
            return false;
        } catch (SAXException ex) {
            LOG.error("Exception thrown creating document from bordereau with id={}:\n", bordereau.getId(), ex);
            setErrorMessage("Error occurred while processing Bordereau.");
            return false;
        } catch (IOException ex) {
            LOG.error("Exception thrown creating document from bordereau with id={}:\n", bordereau.getId(), ex);
            setErrorMessage("Error occurred while processing Bordereau.");
            return false;
        }

        setBordereauProcessingStatus(bordereau);

        try {
            claimResults = formClaimResults(document);
            noClaims = claimResults.size(); // (or) bordereau.getTotalClaims();
        } catch (Exception ex) {
            LOG.error("Error thrown while getting claims from brodereau with id={} ", bordereau.getId(), ex);
            setErrorMessage("An unexpected error occurred while processing this Bordereau.");
            setBordreauProcessFilureStatus(bordereau);
            return false;
        }
        /*
         * processing claims begin here each claim in claimResults is processed,
         * saved then evicted from cache one by one. xmlClaimsDetail is used
         * to give live update to the front end by putting these details in
         * session and for future reference it is saved in DB.
         */
        try {
            for (ClaimResult claimResult : claimResults) {
                UploadedXMLClaimsDetail xmlClaimsDetail = new UploadedXMLClaimsDetail();

                if (doProcessBordereauResult(claimResult, choReferences)) {
                    xmlClaimsDetail.setValid(true);
                    noSuccessfullyProcessed++;
                } else {
                    xmlClaimsDetail.setValid(false);
                }
                noProcessed++;
                setXmlClaimDetailsProperties(xmlClaimsDetail, bordereau, claimResult);
                claimsDetails.add(0, xmlClaimsDetail);
                LOG.debug("Synchronizing on session");
                synchronized (getSessionLock(session)) {
                    session.put("claimsDetails", claimsDetails);
                }
                LOG.debug("Finished synchronizing on session");
                LOG.debug("claimDetails added to session - total size of claimDetails is: {}", claimsDetails.size());
                LOG.debug("{} of {} claims have been processed", noProcessed, noClaims);
            }
        } catch (Exception ex) {
            LOG.error("Unexpected error thrown while processing claim : {}", ex.getMessage(), ex);
            if (ex.getCause() != null) {
                LOG.error("    Caused by: {}", ex.getCause().getMessage());
            }
            setErrorMessage("An unexpected error has occurred - please report to CHOX support.");
            setBordreauProcessFilureStatus(bordereau);
            return false;
        }

        /*
         * end of processing claim.
         */
        LOG.debug("Finished processing bordereau.");

        try {
            setBordereauProperties(noSuccessfullyProcessed, noClaims, bordereau);
            setSuccessMessage("The Bordereau has been processed successfully.");
            uploadedXMLClaimsDetailService.saveUploadedXMLClaimsDetails(claimsDetails);
            bordereauService.saveBordereau(bordereau);
            LOG.debug("The bordereau file '{}' has been processed successfully", bordereau.getFileName());
            return true;
        } catch (Exception ex) {
            /*
             * clearing session to save object(bordereau) in DB(Data Base) after
             * spring throws DataIntegrityViolationException. Eventhough this
             * method is not in transaction unit saving to DB(any objects) after
             * DataIntegrityViolationException gets failed. clearing session is the
             * only hack which i found. This need to be investigated throughly
             * and implemented the correct functionality.
             */

            getCurrentSession().clear();
            bordereau = (Bordereau) getSessionFactory().getCurrentSession().load(Bordereau.class, bordereau.getId());
            setBordereauProperties(noSuccessfullyProcessed, noClaims, bordereau);
            bordereauService.saveBordereau(bordereau);
            LOG.error("Unexpected error thrown while saving Bordereau : {}", ex.getMessage(), ex);
            setErrorMessage("An unexpected error has occurred - please report to CHOX support.");
            return false;
        }
    }

    @Override
    public boolean saveUploadedFile(File uploadedFile, String uploadedFileFileName) {
        List<ClaimResult> claimResults;
        Document document;
        FileInputStream streamIn;
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
        } catch (SAXException ex) {
            bordereau.setStatus("Error");
            bordereau.setDescription("Illegal Content");
            saveBordereau(bordereau, uploadedFile, uploadedFileFileName, fileContent);
            /*
             * returning true cos there is no error message to display. Bordereau file is set with error discription and error status.
             */
            throw new AccessDeniedException("Illegal content found in XML file");
        } catch (Exception ex) {
            LOG.error("Exception thrown in saving file while writing to document : {}", ex.getMessage());
            bordereau.setStatus("Error");
            bordereau.setDescription("Invalid Schema");
            saveBordereau(bordereau, uploadedFile, uploadedFileFileName, fileContent);
            /*
             * returning true cos there is no error message to display. Bordereau file is set with error discription and error status.
             */
            return true;
        }
        bordereau.setValid(true);
        bordereauSchemaValidation.validate(document, bordereau, getCurrentUser());
        if (!bordereau.isValid()) {
            if (bordereau.getMessage() != null && bordereau.getMessage().equals(BordereauSchemaValidation.NO_CLAIMS_FOUND)) {
                bordereau.setStatus("Failed");
                bordereau.setDescription("No Claims Found");
            } else {
                bordereau.setStatus("Error");
                bordereau.setDescription("Invalid Schema");
            }
            saveBordereau(bordereau, uploadedFile, uploadedFileFileName, fileContent);
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
            saveBordereau(bordereau, uploadedFile, uploadedFileFileName, fileContent);
            /*
             * returning true cos there is no error message to display. Bordereau file is set with error discription and error status.
             */
            return true;
        }
        saveBordereau(bordereau, uploadedFile, uploadedFileFileName, fileContent);
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

    @Override
    public UploadedXMLClaimsDetail processWebServiceClaim(InputStream stream) {

        Document document;
        List<ClaimResult> claimResults;
        List<String> choReferences = new ArrayList<String>();
        UploadedXMLClaimsDetail xmlClaimsDetail = new UploadedXMLClaimsDetail();

        try {

            document = DocumentHelper.getDocumentFromStream(stream);

        } catch (ParserConfigurationException ex) {
            LOG.error("Exception thrown creating document from webservice inputStream. Error Message is:{}", ex.getMessage());
            xmlClaimsDetail.setMessage("Error occurred while creating document from webservice inputStream.");
            return xmlClaimsDetail;
        } catch (SAXException ex) {
            LOG.error("Exception thrown creating document from webservice inputStream. Error Message is:{}", ex.getMessage());
            xmlClaimsDetail.setMessage("Error occurred while creating document from webservice inputStream.");
            return xmlClaimsDetail;
        } catch (IOException ex) {
            LOG.error("Exception thrown creating document from webservice inputStream. Error Message is:{}", ex.getMessage());
            xmlClaimsDetail.setMessage("Error occurred while creating document from webservice inputStream.");
            return xmlClaimsDetail;
        }

        try {

            claimResults = formClaimResults(document);

        } catch (Exception ex) {
            LOG.error("Error thrown while getting claimResult from webService document. Error Message is {}", ex.getMessage());
            xmlClaimsDetail.setMessage("An unexpected error occurred while getting claimResult from webService document.");
            return xmlClaimsDetail;
        }

        try {
            for (ClaimResult claimResult : claimResults) {

                if (doProcessBordereauResult(claimResult, choReferences)) {
                    xmlClaimsDetail.setValid(true);
                } else {
                    xmlClaimsDetail.setValid(false);
                }

                xmlClaimsDetail.setProcessStatus(claimResult.getProcessStatus());

                if (!claimResult.getMessage().isEmpty()) {
                    xmlClaimsDetail.setMessage(claimResult.getMessage().toString());
                } else {
                    xmlClaimsDetail.setMessage("");
                }

                if (!claimResult.getBreMessage().isEmpty()) {
                    xmlClaimsDetail.setBreFailureMessages(claimResult.getBreMessage().toString());
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
            }
            LOG.debug("webService claim has been processed successfully.");
            return xmlClaimsDetail;

        } catch (Exception ex) {
            LOG.error("Unexpected error thrown while processing Webservice claim : {}", ex.getMessage());
            xmlClaimsDetail.setMessage("An unexpected error has occurred - please report to CHOX support.");
            return xmlClaimsDetail;
        }
    }

    private void setBordereauProperties(int noSuccessfullyProcessed, int noClaims, Bordereau bordereau) {
        if (noSuccessfullyProcessed >= noClaims) {
            bordereau.setStatus(BordereauParseStatus.ALL_UPLOADED.getDescription());
            bordereau.setDescription("All claims have been successfully uploaded");
        } else if (noSuccessfullyProcessed < noClaims && noSuccessfullyProcessed != 0) {
            bordereau.setStatus(BordereauParseStatus.PARTIAL_UPLOAD.getDescription());
            bordereau.setDescription(noSuccessfullyProcessed + " out of " + noClaims + " claims have been uploaded");
        } else if (noSuccessfullyProcessed == 0) {
            bordereau.setStatus(BordereauParseStatus.ALL_REJECTED.getDescription());
            bordereau.setDescription("All " + noClaims + " claims have been rejected");
        }
        bordereau.setProcessed(true);
        bordereau.setBeingProcessed(false);
    }

    private void setXmlClaimDetailsProperties(UploadedXMLClaimsDetail xmlClaimsDetail, Bordereau bordereau, ClaimResult claimResult) {
        xmlClaimsDetail.setBordereauId(bordereau.getId());
        xmlClaimsDetail.setProcessStatus(claimResult.getProcessStatus());

        if (!claimResult.getMessage().isEmpty()) {
            xmlClaimsDetail.setMessage(claimResult.getMessage().toString());
        } else {
            xmlClaimsDetail.setMessage("");
        }

        if (claimResult.getClaim() != null && claimResult.getClaim().getHistories() != null) {
            LOG.debug("claim and histories is not null");
            String historiesMessage = "";

            for (History h : claimResult.getClaim().getHistories()) {

                if (h.getType().equalsIgnoreCase("Error") && (h.getIsPublic() || !getCurrentUser().isCHO())) {
                    String narrative = h.getNarrative().trim();
                    if (!narrative.substring(narrative.length() - 1).equals(".")) {
                        narrative = narrative + ".";
                    }
                    historiesMessage += narrative;
                }
            }
            xmlClaimsDetail.setBreFailureMessages(historiesMessage);
        } else {
            LOG.debug("claim and histories is null");
            xmlClaimsDetail.setBreFailureMessages("");
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
    }

    private void saveBordereau(Bordereau bordereau, File uploadedFile, String uploadedFileFileName, byte fileContent[]) {
        bordereau.setFileSize((Long) uploadedFile.length());
        bordereau.setFileName(uploadedFileFileName);
        bordereau.setFileBuffer(fileContent);
        bordereau.setProcessed(false);
        bordereauService.saveBordereau(bordereau);
    }

    private Bordereau getBordereauFromId(int bordereauId) {
        return bordereauService.getBordereauById(bordereauId);
    }

    private boolean isValidBordereauId(int bordereauId) {
        if (bordereauId <= 0) {
            LOG.warn("Bordereau not found: id={}", bordereauId);
            setErrorMessage("Bordereau not found.");
            return false;
        }
        return true;
    }

    private boolean isAutherisedUser(int orgId, String fileName) {

        if ((getCurrentUser().isAnInsurer() && !getCurrentUser().getInsurer().getId().equals(orgId))
                || (!getCurrentUser().isAnInsurer() && !getCurrentUser().getChorganisation().getId().equals(orgId))) {
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
        bordereau.setStatus("Error");
        bordereau.setBeingProcessed(false);
        bordereau.setProcessed(true);
        bordereau.setDescription("Error");
        bordereauService.saveBordereau(bordereau);
    }

    private void validate(ClaimResult claimResult, List<String> choReferences) {
        LOG.debug("Validating CHO references are unique");

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

    private void checkECD(Claim claim) {
        LOG.debug("Checking ECD is present...");
        // Check we have an original or initial ECD. If not, we'll create one using the hire-end date
        // N.B. Requested under Phase 5 Sprint 10 todo item 5.10.2 Hire Monitoring xml upload
        if (!ClaimType.isInsurerUpload(claim.getClaimType())
                && claim.getCustomer() != null && claim.getVehicleHire() != null && (claim.getCustomer().getInitialECD() == null && (claim.getHireMonitoringEcds() == null || claim.getHireMonitoringEcds().isEmpty()))) {
            LOG.debug("No ECD - using hire-end");

            List<HireMonitoringEcd> hireMonitoringEcds = claim.getHireMonitoringEcds();
            if (hireMonitoringEcds == null) {
                hireMonitoringEcds = new ArrayList<HireMonitoringEcd>();
                claim.setHireMonitoringEcds(hireMonitoringEcds);
            }
            HireMonitoringEcd ecd = new HireMonitoringEcd();
            ecd.setClaim(claim);
            ecd.setEcdDate(claim.getVehicleHire().getHireEnd());
            ecd.setReason("First ECD");
            ecd.setSequence(1);
            ecd.setSupportingNote("No original ECD supplied so hire end date used as first ECD supplied.");
            hireMonitoringEcds.add(ecd);
        } else {
            LOG.debug("No ECD added.");
        }
    }

}
