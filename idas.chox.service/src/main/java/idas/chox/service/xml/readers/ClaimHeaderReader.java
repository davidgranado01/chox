package idas.chox.service.xml.readers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.MessageFormat;

import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

import static idas.chox.core.xmlValidation.RentalStatus.COLLABORATION;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ChorganisationAlias;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.InsurerAlias;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ChorganisationAliasService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.util.XmlHelper;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.xmlValidation.RentalStatus;
import idas.chox.service.claim.ClaimObjectService;
import idas.chox.service.xml.util.NodeHelper;

public class ClaimHeaderReader extends BaseEntityReader {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimHeaderReader.class);
    private static String existsLinkedChoErrorMsg = "This supplier reference already exists for linked CHO '%s'.";
    private static final String sectionName = "Claim Header";
    // PAGE PARAMETERS
    private Boolean managingRepair;
    private Date firstContactDate;
    private Date creditAgreementDate;
    private Date gtaNoticeDate;
    private String choReferenceNumber;
    private String supplierAliasName;
    private String hireState;
    private boolean isUpdateManagingRepair = false;
    private boolean isInsurerUpload = false;
    private Chorganisation chorganisation;
    private InsurerService insurerService;

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    @Override
    public void execute(ClaimResult claimResult) throws DOMException, XPathExpressionException, Exception {
        LOG.debug("Validating claimResult");
        if (!validate(claimResult)) {
            LOG.debug("Validation failed: {}", claimResult.getProcessStatus());
        }
        // If the header fails validation and we a cho ref, we'll still process
        // We need to do this as this is the header and we need to
        // check the claim status which is needed for further processing
        process(claimResult);
    }

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {
        LOG.debug("Validating Claim Header: claimResult is {}", claimResult);
        choReferenceNumber = "";
        hireState = "";
        supplierAliasName = "";

        claimResult.setCheckDataValid(true);
        NodeHelper.nodeValidate(sectionName, "hire-state", claimResult.getElement(), claimResult, getDataValidationParameter());
        NodeHelper.nodeValidate(sectionName, "supplier-reference", claimResult.getElement(), claimResult, getDataValidationParameter());

        if (NodeHelper.nodeValidateBoolean(sectionName, "hire-state", claimResult.getElement(), claimResult, getDataValidationParameter())) {
            hireState = XmlHelper.getNodeValue(claimResult.getElement(), "hire-state");
            // Remove white space and convert to lower case
            hireState = hireState.trim().replaceAll("\\s+", "").toLowerCase();
        }

        if (NodeHelper.nodeValidateBoolean(sectionName, "supplier-reference", claimResult.getElement(), claimResult, getDataValidationParameter())) {
            choReferenceNumber = XmlHelper.getNodeValue(claimResult.getElement(), "supplier-reference");
        }

        if (getBordereauReaderContext().getSecurityInfoProvider().getIsINS()) {
            Integer insurerId = getBordereauReaderContext().getSecurityInfoProvider().getCurrentUser().getInsurer().getId();
            ChorganisationAliasService chorganisationAliasService = this.getBordereauReaderContext().getChorganisationAliasService();
            InsurerChorganisationService insurerChorganisationService = this.getBordereauReaderContext().getInsurerChorganisationService();
            claimResult = NodeHelper.nodeChorganisationAliasValidate(sectionName, "supplier-name", claimResult.getElement(),
                    claimResult, getDataValidationParameter(),
                    chorganisationAliasService, insurerChorganisationService,
                    insurerId);
            if (claimResult.isValid()) {
                supplierAliasName = XmlHelper.getNodeValue(claimResult.getElement(), "supplier-name");
                ChorganisationAlias alias;
                try {
                    alias = chorganisationAliasService.getChorganisationByAliasName(supplierAliasName);
                } catch (Exception ex) {
                    alias = null;
                }
                chorganisation = alias != null ? alias.getChorganisation() : null;
            }
            isInsurerUpload = true;
        } else {
            Chorganisation cho = getBordereauReaderContext().getSecurityInfoProvider().getCurrentUser().getChorganisation();
            if (cho.getLinkedCho() != null) {
                // Check CHO Reference does not exist for the linked CHO
                Boolean claimExists = getBordereauReaderContext().getClaimService().isClaimSupplierReferenceNumberExistForChoExternal(choReferenceNumber, cho.getLinkedCho().getId());
                if (claimExists) {
                    claimResult.setValid(false);
                    claimResult.setDataValid(false);
                    claimResult.getMessage().add(String.format(existsLinkedChoErrorMsg, choReferenceNumber, cho.getLinkedCho().getName()));
                }
            }
        }

        NodeHelper.nodeValidate(sectionName, "first-contact", claimResult.getElement(), claimResult, getDataValidationParameter());
        NodeHelper.nodeValidate(sectionName, "managing-repair", claimResult.getElement(), claimResult, getDataValidationParameter());
        NodeHelper.nodeValidate(sectionName, "agreement-signed", claimResult.getElement(), claimResult, getDataValidationParameter());
        NodeHelper.nodeValidate(sectionName, "gta-notice", claimResult.getElement(), claimResult, getDataValidationParameter());

        if (NodeHelper.nodeValidateBoolean(sectionName, "first-contact", claimResult.getElement(), claimResult, getDataValidationParameter())) {
            firstContactDate = XmlHelper.getDateFromNode(claimResult.getElement(), "first-contact");
        }

        if (NodeHelper.nodeValidateBoolean(sectionName, "managing-repair", claimResult.getElement(), claimResult, getDataValidationParameter())) {
            managingRepair = XmlHelper.getBooleanFromNode(claimResult.getElement(), "managing-repair");

            if (!XMLUtils.getElementValue(claimResult.getElement(), "managing-repair").equalsIgnoreCase("") && XMLUtils.getElementValue(claimResult.getElement(), "managing-repair") != null) {
                isUpdateManagingRepair = true;
            }
        }

        if (NodeHelper.nodeValidateBoolean(sectionName, "agreement-signed", claimResult.getElement(), claimResult, getDataValidationParameter())) {
            creditAgreementDate = XmlHelper.getDateFromNode(claimResult.getElement(), "agreement-signed");
        }

        if (NodeHelper.nodeValidateBoolean(sectionName, "gta-notice", claimResult.getElement(), claimResult, getDataValidationParameter())) {
            gtaNoticeDate = XmlHelper.getDateFromNode(claimResult.getElement(), "gta-notice");
        }

        if (gtaNoticeDate == null) {
            gtaNoticeDate = DateHelper.getCurrentDateTime();
        }
        LOG.debug("Validating Customer: returning {}", claimResult.isValid());

        return claimResult.isValid();
    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {
        SecurityInfoProvider securityInfoProvider = getBordereauReaderContext().getSecurityInfoProvider();
        LOG.debug("Processing Claim Header");

        Claim claim = new Claim();
        try {
            RentalStatus rentalStatus = RentalStatus.fromString(hireState);
            switch (rentalStatus) {
                case COLLABORATION:
                    if (!isInsurerUpload) {
                        if (!securityInfoProvider.getCurrentUser().getChorganisation().isEnableCollaborationProtocolClaims()) {
                            claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_HIRE_STATE);
                            claimResult.setValid(false);
                            claimResult.getMessage().add("Collaboration Protocol claims have not been activated. Please contact CHOX support if you wish to upload Collaboration Protocol claims.");
                            claim.setChoReference(choReferenceNumber);
                            claimResult.setClaim(claim);
                            break;
                        }
                        claim.setClaimType(ClaimType.COLLABORATION_PROTOCOL);
                    } else {
                        claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_HIRE_STATE);
                        claimResult.setValid(false);
                        claimResult.getMessage().add("The value provided for the 'hire state' is incorrect. Valid values are: 'InProgress', 'Complete', 'Off Hired', 'Supplementary Invoice', 'Hire Monitoring' or 'Invoice Only'.");
                        claim.setChoReference(choReferenceNumber);
                        claimResult.setClaim(claim);
                        break;
                    }
                case INPROGRESS:
                case COMPLETE:
                    LOG.debug("PROCESSING Normal Chox Claim");
                    processNormalChoxClaim(claimResult, claim);
                    break;
                case OFFHIRED:
                    LOG.debug("PROCESSING HIREMONITORING AND INVOICE");
                    processOffHiredInvoice(claimResult, claim);
                    break;
                case HIREMONITORING:
                    LOG.debug("PROCESSING Hire Monitering Claim");
                    processHireMonitoring(claimResult, claim);
                    break;
                case SUPPLEMENTARYINVOICE:
                    LOG.debug("PROCESSING Supplementary Invoice");
                    processSupplementaryInvoice(claimResult, claim);
                    break;
                case INSURERVSINSURER:
                    if (!isInsurerUpload) {
                        LOG.debug("PROCESSING Insurer vs Insurer Chox Claim");
                        processInsurerVsInsurerClaim(claimResult, claim);
                    } else {
                        claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_HIRE_STATE);
                        claimResult.setValid(false);
                        claimResult.getMessage().add("The value provided for the 'hire state' is incorrect. Valid values are: 'InProgress', 'Complete', 'Off Hired', 'Supplementary Invoice', 'Hire Monitoring' or 'Invoice Only'.");
                        claim.setChoReference(choReferenceNumber);
                        claimResult.setClaim(claim);
                    }
                    break;
                case SUBSCRIBER:
                    if (!isInsurerUpload && securityInfoProvider.getCurrentUser().getChorganisation().isEnableSubscriberClaims()) {
                        /*
                         *   Process Subscriber claim
                         */
                        LOG.debug("PROCESSING Subscriber Claim");
                        processSubscriberClaim(claimResult, claim);
                    } else {
                        claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_HIRE_STATE);
                        claimResult.setValid(false);
                        if (!isInsurerUpload) {
                            claimResult.getMessage().add("Subscriber claims have not been activated. Please contact CHOX support if you wish to upload subscriber claims.");
                        } else {
                            claimResult.getMessage().add("The value provided for the 'hire state' is incorrect. Valid values are: 'InProgress', 'Complete', 'Off Hired', 'Supplementary Invoice', 'Hire Monitoring' or 'Invoice Only'.");
                        }
                        claim.setChoReference(choReferenceNumber);
                        claimResult.setClaim(claim);
                    }
                    break;
                case FIXEDFEE:
                    if (securityInfoProvider.getCurrentUser().isCHO()
                            && securityInfoProvider.getCurrentUser().getChorganisation().isEnableFixedFeeClaims()) {
                        /*
                         *   Process Subscriber claim
                         */
                        LOG.debug("PROCESSING Fixed Fee Claim");
                        processFixedFeeClaim(claimResult, claim);
                    } else {
                        claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_HIRE_STATE);
                        claimResult.setValid(false);
                        if (!isInsurerUpload) {
                            claimResult.getMessage().add("Fixed Fee claims have not been activated. Please contact CHOX support if you wish to upload fixed fee claims.");
                        } else {
                            claimResult.getMessage().add("The value provided for the 'hire state' is incorrect. Valid values are: 'InProgress', 'Complete', 'Off Hired', 'Supplementary Invoice', 'Hire Monitoring' or 'Invoice Only'.");
                        }
                        claim.setChoReference(choReferenceNumber);
                        claimResult.setClaim(claim);
                    }
                    break;
                case INVOICEONLY:
                case INSURERUPLOAD:
                    if (isInsurerUpload && securityInfoProvider.getCurrentUser().getInsurer().isInvoiceUploadEnabled()) {
                        LOG.debug("Insurer Invoice upload found");
                        processInsurerInvoice(claimResult, claim);
                    } else {
                        claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_HIRE_STATE);
                        claimResult.setValid(false);
                        if (isInsurerUpload) {
                            claimResult.getMessage().add("The value provided for the 'hire state' is incorrect. Valid values are: 'InProgress', 'Complete', 'Off Hired', 'Supplementary Invoice', 'Hire Monitoring' or 'Invoice Only'.");
                        } else {
                            claimResult.getMessage().add("The value provided for the 'hire state' is incorrect. Valid values are: 'InProgress', 'Complete', 'Off Hired', 'Supplementary Invoice', 'Hire Monitoring', 'Subscriber', 'Fixed Fee' or 'Insurer vs Insurer'.");
                        }
                        claim.setChoReference(choReferenceNumber);
                        claimResult.setClaim(claim);
                    }
                    break;
            }
        } catch (IllegalArgumentException ex) {
            // First Check if we have TPI activated
            if (securityInfoProvider.getCurrentUser().isCHO()
                    && securityInfoProvider.getCurrentUser().getChorganisation().isThirdPartyInterventionActivated()) {
                claim.setClaimType(ClaimType.TPI);
                LOG.debug("TPI Claim found");
                LOG.debug("TPI is activated for this CHO");

                processTpiInvoice(claimResult, claim);
            } else {
                LOG.warn("Invalid hire-state found: {}", hireState);
                claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_HIRE_STATE);
                claimResult.setValid(false);
                if (securityInfoProvider.getCurrentUser().isCHO()) {
                    String message = "The value provided for the ‘hire state’ is incorrect. Valid values are: ‘InProgress’, ‘Complete’, ‘Off Hired’, ‘Supplementary Invoice’, ‘Hire Monitoring’";
                    if (securityInfoProvider.getCurrentUser().isCHO()
                            && securityInfoProvider.getCurrentUser().getChorganisation().isEnableSubscriberClaims()) {
                        message = MessageFormat.format("{0}, ''Subscriber''", message);
                    }
                    if (securityInfoProvider.getCurrentUser().isCHO()
                            && securityInfoProvider.getCurrentUser().getChorganisation().isEnableFixedFeeClaims()) {
                        message = MessageFormat.format("{0}, ''Fixed Fee''", message);
                    }
                    message = MessageFormat.format("{0} or ''Insurer vs Insurer''.", message);
                    claimResult.getMessage().add(message);
                } else {
                    claimResult.getMessage().add("The value provided for the 'hire state' is incorrect. Valid values are: 'InProgress', 'Complete', 'Off Hired', 'Supplementary Invoice', 'Hire Monitoring' or 'Invoice Only'.");
                }
                claim.setChoReference(choReferenceNumber);
                claimResult.setClaim(claim);
            }
        }
    }

    private void processInsurerInvoice(ClaimResult claimResult, Claim claim) {
        SecurityInfoProvider securityInfoProvider = getBordereauReaderContext().getSecurityInfoProvider();
        ClaimService claimService = getBordereauReaderContext().getClaimService();

        int choId = chorganisation == null ? -1 : chorganisation.getId();
        if (claimService.isClaimSupplierReferenceNumberExistForCho(choReferenceNumber, choId)) {
            LOG.debug("Insurer trying to upload an invoice that already exists: '{}'.", choReferenceNumber);
            claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_CLAIM_STATUS);
            claimResult.setValid(false);
            claimResult.getMessage().add("This Invoice already exists.");
            claim.setChoReference(choReferenceNumber);
        } else if (claimService.isClaimSupplierReferenceNumberExistForChoExternal(choReferenceNumber, choId)) {
                LOG.info("Insurer trying to upload a manual claim that already exists: '{}'.", choReferenceNumber);
                claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_CLAIM_STATUS);
                claimResult.setValid(false);
                claimResult.getMessage().add("This supplier reference number already exists in the system. Please contact Valexa support.");
                claim.setChoReference(choReferenceNumber);
        } else {
            LOG.debug("Valid Insurer invoice found.");
            claimResult.setClaimParseStatus(ClaimParseStatus.INSURER_INVOICE);
            if (managingRepair != null) {
                claim.setManagingRepair(managingRepair);
            }
            claim.setPolicyHolderContactDate(firstContactDate);
            claim.setChoReference(choReferenceNumber);
            claim.setCreditAgreementDate(creditAgreementDate);
            claim.setGtaNoticeDate(gtaNoticeDate);
            claim.setIndemnityAmount(BigDecimal.ZERO.setScale(2));
            claim.setPercentageLiabilityAccepted(new BigDecimal("100.00"));
            claim.setPercentageLiabilityCho(BigDecimal.ZERO.setScale(2));
            claim.setInsurer(insurerService.getInsurer(securityInfoProvider.getCurrentUser().getInsurer().getId()));
            claim.setClaimType(ClaimType.INSURER_INVOICE);
            if (supplierAliasName != null && !supplierAliasName.isEmpty()) {
                // Check CHO allows insurer upload
                if (chorganisation != null && chorganisation.isInsurerUploadOnly()) {
                    //Set claim Insurer equal to third party insurer
                    claim.setChorganisation(chorganisation);
                } else {
                    claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_SCHEMA);
                    claimResult.setValid(false);
                    claimResult.getMessage().add("The CHO '" + chorganisation.getName() + "' does not allow Insurer uploaded claims. Please contact CHOX Admin.");
                }
            } else {
                LOG.info("SupplierAliasName is null or empty ");
                claimResult.setValid(false);
            }
        }
        claimResult.setClaim(claim);
    }

    private void processTpiInvoice(ClaimResult claimResult, Claim claim) {
        SecurityInfoProvider securityInfoProvider = getBordereauReaderContext().getSecurityInfoProvider();
        ClaimService claimService = getBordereauReaderContext().getClaimService();

        /*
         * getting insurer from xml to check TPI is Activated
         */
        Element rootElements = claimResult.getElement();
        Element claimElements = XMLUtils.getElement(rootElements, "claim");
        Element elements = XMLUtils.getElement(claimElements, "third-party");
        String insurerAliasNames = XmlHelper.getNodeValue(elements, "name");

        if (!checkTpiServiceActivatedForInsurer(insurerAliasNames)) {
            LOG.debug("CHO TRYING TO UPLOADING TPI INVOICE BUT INSURER IS NOT ACTIVATED AS TPI ACCEPTING INSURER.");
            claimResult.setClaimParseStatus(ClaimParseStatus.TPI_NOT_ACCEPTED_BY_INSURER);
            claimResult.setValid(false);
            claimResult.getMessage().add("This Insurer does not accept TPI invoices. Please contact CHOX support.");
            claim.setChoReference(choReferenceNumber);
        } else if (!checkTpiServiceActivatedForInsurerAndRentalStatus(insurerAliasNames, hireState)) {
            LOG.debug("CHO is trying to upload a TPI invoice with an invalid hire-state field");
            claimResult.setClaimParseStatus(ClaimParseStatus.TPI_NOT_RECOGNIZED);
            claimResult.setValid(false);
            LOG.warn("CHO is trying to upload a TPI invoice with an invalid hire-state field: {}", getTPIidentificationStringForInsurer(insurerAliasNames));
            String message = "The value provided for the ‘hire state’ is incorrect. Valid values are: ‘InProgress’, ‘Complete’, ‘Off Hired’, ‘Supplementary Invoice’, ‘Hire Monitoring’";
            if (securityInfoProvider.getCurrentUser().isCHO()
                    && securityInfoProvider.getCurrentUser().getChorganisation().isEnableSubscriberClaims()) {
                message = MessageFormat.format("{0}, ''Subscriber''", message);
            }
            if (securityInfoProvider.getCurrentUser().isCHO()
                    && securityInfoProvider.getCurrentUser().getChorganisation().isEnableFixedFeeClaims()) {
                message = MessageFormat.format("{0}, ''Fixed Fee''", message);
            }
            message = MessageFormat.format("{0} or ''Insurer vs Insurer''. Alternatively if this is a TPI claim, it must be ''{1}''  against this Insurer.",
                    message, getTPIidentificationStringForInsurer(insurerAliasNames));

            claimResult.getMessage().add(message);
            claim.setChoReference(choReferenceNumber);
        } else if (claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber)) {
            LOG.debug("Claim supplier reference already exists: {}", choReferenceNumber);
            claimResult.setClaimParseStatus(ClaimParseStatus.EXIST_INVOICE);
            claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);
            claimResult.setValid(false);
        } else {
            LOG.debug("Valid TPI invoice claim found.");
            claimResult.setClaimParseStatus(ClaimParseStatus.TPI_INTERVENTION);
            if (managingRepair != null) {
                claim.setManagingRepair(managingRepair);
            }
            claim.setPolicyHolderContactDate(firstContactDate);
            claim.setChoReference(choReferenceNumber);
            claim.setCreditAgreementDate(creditAgreementDate);
            claim.setGtaNoticeDate(gtaNoticeDate);
            claim.setIndemnityAmount(BigDecimal.ZERO.setScale(2));
            claim.setPercentageLiabilityAccepted(new BigDecimal("100.00"));
            claim.setPercentageLiabilityCho(BigDecimal.ZERO.setScale(2));
            claim.setChorganisation(securityInfoProvider.getCurrentUser().getChorganisation());
            claim.setClaimType(ClaimType.TPI);
        }
        claimResult.setClaim(claim);
    }

    private void processOffHiredInvoice(ClaimResult claimResult, Claim claim) {
        ClaimService claimService = getBordereauReaderContext().getClaimService();
        BreBandService breBandService = getBordereauReaderContext().getBreBandService();
        SecurityInfoProvider securityInfoProvider = getBordereauReaderContext().getSecurityInfoProvider();

        int choId = chorganisation == null ? -1 : chorganisation.getId();
        if (isInsurerUpload && !securityInfoProvider.getCurrentUser().getInsurer().isClaimUploadEnabled()) {
            LOG.warn("Invalid new claim rental status: '{}' - Insurer does not have Claim upload enabled", hireState);
            claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_HIRE_STATE);
            claimResult.setValid(false);
            claimResult.getMessage().add("This Insurer does not allow claim upload. Please contact CHOX support..");
            claim.setChoReference(choReferenceNumber);
        } else if ((!isInsurerUpload && claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber))
                || (isInsurerUpload && claimService.isClaimSupplierReferenceNumberExistForCho(choReferenceNumber, choId))) {
            if (isInsurerUpload) {
                claim = claimService.getClaimByChoIdAndCHOReferenceNumber(chorganisation.getId(), choReferenceNumber);
            } else {
                claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);
            }

            if (claim.getInvoice() != null) {
                if (isInsurerUpload) {
                    claimResult.setClaimParseStatus(ClaimParseStatus.INSURER_EXIST_INVOICE);
                } else {
                    claimResult.setClaimParseStatus(ClaimParseStatus.EXIST_INVOICE);
                }
                claimResult.setValid(false);
            } else if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO)) {
                if (isInsurerUpload) {
                    claimResult.setClaimParseStatus(ClaimParseStatus.INSURER_HIRE_MONITORING_AND_NEW_INVOICE);
                } else {
                    claimResult.setClaimParseStatus(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE);
                }
                BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                claim.setBreBand(choBand);
                if (isUpdateManagingRepair && managingRepair != null) {
                    claim.setManagingRepair(managingRepair);
                }
            } else if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)) {
                if (isInsurerUpload) {
                    claimResult.setClaimParseStatus(ClaimParseStatus.INSURER_INVOICE);
                } else {
                    claimResult.setClaimParseStatus(ClaimParseStatus.NEW_INVOICE);
                }
                BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                claim.setBreBand(choBand);
                if (isUpdateManagingRepair && managingRepair != null) {
                    claim.setManagingRepair(managingRepair);
                }

            } else {
                LOG.warn("Invalid rental status: '{}' - For 'Off Hired' claims/invoices to be uploaded the claims must be in the 'AwaitingCarHireInfo' status.", hireState);
                claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_CLAIM_STATUS);
                claimResult.setValid(false);
                claimResult.getMessage().add("For 'Off Hired' claims/invoices to be uploaded the claims must be in the 'AwaitingCarHireInfo' status.");
                claim.setChoReference(choReferenceNumber);

            }

        } else {
            /*
             *  if the hire state is off hired but claim is not in CLAIM_AWAITING_CAR_HIRE_INFO then set error message and do not process the claim.
             */
            LOG.warn("Invalid new claim rental status: '{}' - For 'Off Hired' claims/invoices to be uploaded the claims must be in the 'AwaitingCarHireInfo' status.", hireState);
            claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_CLAIM_STATUS);
            claimResult.setValid(false);
            claimResult.getMessage().add("For 'Off Hired' claims/invoices to be uploaded the claims must already exists in the system.");
            claim.setChoReference(choReferenceNumber);
        }

        claimResult.setClaim(claim);
    }

    private void processNormalChoxClaim(ClaimResult claimResult, Claim claim) {
        SecurityInfoProvider securityInfoProvider = getBordereauReaderContext().getSecurityInfoProvider();
        ClaimService claimService = getBordereauReaderContext().getClaimService();
        BreBandService breBandService = getBordereauReaderContext().getBreBandService();

        int choId = chorganisation == null ? -1 : chorganisation.getId();
        /*
         * getting insurer from xml to check TPI is Activated
         */
        String insurerAliasNames = null;
        if (claim.getClaimType() == ClaimType.COLLABORATION_PROTOCOL) {
            Element rootElements = claimResult.getElement();
            Element claimElements = XMLUtils.getElement(rootElements, "claim");
            Element elements = XMLUtils.getElement(claimElements, "third-party");
            insurerAliasNames = XmlHelper.getNodeValue(elements, "name");
        }

        if (claim.getClaimType() == ClaimType.COLLABORATION_PROTOCOL && !checkCollaborationProtocolActivatedForInsurer(insurerAliasNames)) {
            claimResult.setClaimParseStatus(ClaimParseStatus.COLLABORATION_NOT_ACCEPTED_BY_INSURER);
            claimResult.setValid(false);
            claimResult.getMessage().add("This Insurer does not accept claims under the Collaboration Protocol. Please contact CHOX support.");
            claim.setChoReference(choReferenceNumber);
        } else if (isInsurerUpload && !securityInfoProvider.getCurrentUser().getInsurer().isClaimUploadEnabled()) {
            LOG.warn("Invalid new claim rental status: '{}' - Insurer does not have Claim upload enabled", hireState);
            claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_HIRE_STATE);
            claimResult.setValid(false);
            claimResult.getMessage().add("This Insurer does not allow claim upload. Please contact CHOX support.");
            claim.setChoReference(choReferenceNumber);
        } else if ((isInsurerUpload && claimService.isClaimSupplierReferenceNumberExistForCho(choReferenceNumber, choId))
                || (!isInsurerUpload && claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber))) {
            if (isInsurerUpload) {
                claim = claimService.getClaimByChoIdAndCHOReferenceNumber(chorganisation.getId(), choReferenceNumber);
            } else {
                claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);
            }
            // Check its the same claim-type
            if ((isInsurerUpload && !ClaimType.isInsurerUpload(claim.getClaimType()))
                    || (!isInsurerUpload && !ClaimType.isGTA(claim.getClaimType()) && !ClaimType.isCollaborationProtocol(claim.getClaimType()))) {
                claimResult.setValid(false);
                claimResult.setClaimParseStatus(ClaimParseStatus.EXISTS_DIFFERENT_CLAIM_TYPE);
            } else {
                if (claim.getInvoice() != null) {
                    if (isInsurerUpload) {
                        claimResult.setClaimParseStatus(ClaimParseStatus.INSURER_EXIST_INVOICE);
                    } else {
                        claimResult.setClaimParseStatus(ClaimParseStatus.EXIST_INVOICE);
                    }
                    claimResult.setValid(false);
                } else {
                    if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)) {
                        if (isInsurerUpload) {
                            claimResult.setClaimParseStatus(ClaimParseStatus.INSURER_INVOICE);
                        } else {
                            claimResult.setClaimParseStatus(ClaimParseStatus.NEW_INVOICE);
                        }
                        BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                        claim.setBreBand(choBand);
                        if (isUpdateManagingRepair && managingRepair != null) {
                            claim.setManagingRepair(managingRepair);
                        }
                    } else if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED)
                            || claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_PENDING)
                            || claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_REJECTION_ACCEPTED)) {
                        // NOT EDITABNLE CLAIM
                        claimResult.setClaimParseStatus(ClaimParseStatus.CLAIM_NOT_EDITABLE);
                        claimResult.setValid(false);
                    } else {
                        // EDITABLE CLAIM
                        if (isInsurerUpload) {
                            claimResult.setClaimParseStatus(ClaimParseStatus.EXISTS_INSURER_CLAIM);
                        } else if (ClaimType.isCollaborationProtocol(claim.getClaimType())) {
                            claimResult.setClaimParseStatus(ClaimParseStatus.EXIST_COLLABORATION_CLAIM);
                        } else {
                            claimResult.setClaimParseStatus(ClaimParseStatus.EXIST_CLAIM);
                        }
                        if (isUpdateManagingRepair && managingRepair != null) {
                            claim.setManagingRepair(managingRepair);
                        }
                    }
                }
            }
        } else {
            if (isInsurerUpload) {
                LOG.debug("New Insurer Claim Found");
                claimResult.setClaimParseStatus(ClaimParseStatus.INSURER_CLAIM);
                claim.setInsurer(securityInfoProvider.getCurrentUser().getInsurer());
                claim.setClaimType(ClaimType.INSURER_CLAIM);
                if (supplierAliasName != null && !supplierAliasName.isEmpty()) {
                    if (chorganisation != null && chorganisation.isInsurerUploadOnly()) {
                        //Set claim Insurer equal to third party insurer
                        LOG.debug("CHO set for insurer claim: {}", chorganisation.getName());
                        claim.setChorganisation(chorganisation);
                        if (claimService.isClaimSupplierReferenceNumberExistForChoExternal(choReferenceNumber, choId)) {
                            LOG.info("Insurer trying to upload a manual claim that already exists: '{}'.", choReferenceNumber);
                            claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_CLAIM_STATUS);
                            claimResult.setValid(false);
                            claimResult.getMessage().add("This supplier reference number already exists in the system. Please contact Valexa support.");
                            claim.setChoReference(choReferenceNumber);
                        }
                    } else {
                        LOG.debug("CHO not found");
                        claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_SCHEMA);
                        claimResult.setValid(false);
                        claimResult.getMessage().add(MessageFormat.format(
                                "The CHO '{0}' does not allow Insurer uploaded claims. Please contact CHOX Admin.", chorganisation.getName()));
                    }
                } else {
                    LOG.debug("SupplierAliasName is null or empty ");
                    claimResult.setValid(false);
                }
            } else {
                if (ClaimType.isCollaborationProtocol(claim.getClaimType())) {
                    claimResult.setClaimParseStatus(ClaimParseStatus.NEW_COLLABORATION_CLAIM);
                } else {
                    claimResult.setClaimParseStatus(ClaimParseStatus.NEW_CLAIM);
                }
                claim.setChorganisation(securityInfoProvider.getCurrentUser().getChorganisation());
            }

            if (managingRepair != null) {
                claim.setManagingRepair(managingRepair);
            }
            claim.setPolicyHolderContactDate(firstContactDate);
            claim.setChoReference(choReferenceNumber);
            claim.setCreditAgreementDate(creditAgreementDate);
            claim.setGtaNoticeDate(gtaNoticeDate);
            claim.setIndemnityAmount(BigDecimal.ZERO.setScale(2));
            claim.setPercentageLiabilityAccepted(BigDecimal.ZERO.setScale(2));
            claim.setPercentageLiabilityCho(BigDecimal.ZERO.setScale(2));
        }

        claimResult.setClaim(claim);
    }

    private void processSubscriberClaim(ClaimResult claimResult, Claim claim) {
        SecurityInfoProvider securityInfoProvider = getBordereauReaderContext().getSecurityInfoProvider();
        ClaimService claimService = getBordereauReaderContext().getClaimService();
        BreBandService breBandService = getBordereauReaderContext().getBreBandService();

        /*
         * getting Insurer from xml and check Subscriber is Activated
         */
        Element rootElements = claimResult.getElement();
        Element claimElements = XMLUtils.getElement(rootElements, "claim");
        Element elements = XMLUtils.getElement(claimElements, "third-party");
        String insurerName = XmlHelper.getNodeValue(elements, "name");

        if (!checkSubscriberActivatedForInsurer(insurerName)) {
            LOG.debug("CHO is attempting to upload a Subscriber claim to an Insurer");
            claimResult.setClaimParseStatus(ClaimParseStatus.SUBSCRIBER_NOT_ACCEPTED_BY_INSURER);
            claimResult.setValid(false);
            claimResult.getMessage().add(MessageFormat.format("The Insurer ''{0}'' does not accept Subscriber claims. Please contact CHOX support.", insurerName));
            claim.setChoReference(choReferenceNumber);
        } else if (claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber)) {
            claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);
            if (claim.getInvoice() != null) {
                claimResult.setClaimParseStatus(ClaimParseStatus.EXIST_INVOICE);
                claimResult.setValid(false);
            } else if (!ClaimType.isSubscriber(claim.getClaimType())) {
                claimResult.setClaimParseStatus(ClaimParseStatus.EXISTS_DIFFERENT_CLAIM_TYPE);
                claimResult.setValid(false);
            } else {
                if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)) {
                    claimResult.setClaimParseStatus(ClaimParseStatus.NEW_INVOICE);
                    BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                    claim.setBreBand(choBand);
                    if (isUpdateManagingRepair && managingRepair != null) {
                        claim.setManagingRepair(managingRepair);
                    }
                } else if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED)
                        || claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_PENDING)
                        || claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_REJECTION_ACCEPTED)) {
                    // NOT EDITABNLE CLAIM
                    claimResult.setClaimParseStatus(ClaimParseStatus.CLAIM_NOT_EDITABLE);
                    claimResult.setValid(false);
                } else {
                    // EDITABLE CLAIM
                    claimResult.setClaimParseStatus(ClaimParseStatus.EXIST_SUBSCRIBER_CLAIM);
                    if (isUpdateManagingRepair && managingRepair != null) {
                        claim.setManagingRepair(managingRepair);
                    }
                }
            }
        } else {
            claimResult.setClaimParseStatus(ClaimParseStatus.NEW_SUBSCRIBER_CLAIM);
            if (managingRepair != null) {
                claim.setManagingRepair(managingRepair);
            }
            claim.setClaimType(ClaimType.SUBSCRIBER);
            claim.setPolicyHolderContactDate(firstContactDate);
            claim.setChoReference(choReferenceNumber);
            claim.setCreditAgreementDate(creditAgreementDate);
            claim.setGtaNoticeDate(gtaNoticeDate);
            claim.setIndemnityAmount(BigDecimal.ZERO.setScale(2));
            claim.setPercentageLiabilityAccepted(BigDecimal.ZERO.setScale(2));
            claim.setPercentageLiabilityCho(BigDecimal.ZERO.setScale(2));
            claim.setChorganisation(securityInfoProvider.getCurrentUser().getChorganisation());
        }

        claimResult.setClaim(claim);
    }

    private void processFixedFeeClaim(ClaimResult claimResult, Claim claim) {
        SecurityInfoProvider securityInfoProvider = getBordereauReaderContext().getSecurityInfoProvider();
        ClaimService claimService = getBordereauReaderContext().getClaimService();
        BreBandService breBandService = getBordereauReaderContext().getBreBandService();

        /*
         * getting Insurer from xml and check Subscriber is Activated
         */
        Element rootElements = claimResult.getElement();
        Element claimElements = XMLUtils.getElement(rootElements, "claim");
        Element elements = XMLUtils.getElement(claimElements, "third-party");
        String insurerName = XmlHelper.getNodeValue(elements, "name");

        if (!checkFixedFeeActivatedForInsurer(insurerName)) {
            LOG.debug("CHO is attempting to upload a Fixed Fee claim to an Insurer");
            claimResult.setClaimParseStatus(ClaimParseStatus.FIXEDFEE_NOT_ACCEPTED_BY_INSURER);
            claimResult.setValid(false);
            claimResult.getMessage().add(MessageFormat.format("The Insurer ''{0}'' does not accept Fixed Fee claims. Please contact CHOX support.", insurerName));
            claim.setChoReference(choReferenceNumber);
        } else if (claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber)) {
            claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);
            if (claim.getInvoice() != null) {
                claimResult.setClaimParseStatus(ClaimParseStatus.EXIST_INVOICE);
                claimResult.setValid(false);
            } else if (!ClaimType.isFixedFee(claim.getClaimType())) {
                claimResult.setClaimParseStatus(ClaimParseStatus.EXISTS_DIFFERENT_CLAIM_TYPE);
                claimResult.setValid(false);
            } else {
                if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)) {
                    claimResult.setClaimParseStatus(ClaimParseStatus.NEW_INVOICE);
                    BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                    claim.setBreBand(choBand);
                    if (isUpdateManagingRepair && managingRepair != null) {
                        claim.setManagingRepair(managingRepair);
                    }
                } else if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED)
                        || claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_PENDING)
                        || claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_REJECTION_ACCEPTED)) {
                    // NOT EDITABNLE CLAIM
                    claimResult.setClaimParseStatus(ClaimParseStatus.CLAIM_NOT_EDITABLE);
                    claimResult.setValid(false);
                } else {
                    // EDITABLE CLAIM
                    claimResult.setClaimParseStatus(ClaimParseStatus.EXIST_FIXEDFEE_CLAIM);
                    if (isUpdateManagingRepair && managingRepair != null) {
                        claim.setManagingRepair(managingRepair);
                    }
                }
            }
        } else {
            claimResult.setClaimParseStatus(ClaimParseStatus.NEW_FIXEDFEE_CLAIM);
            if (managingRepair != null) {
                claim.setManagingRepair(managingRepair);
            }
            claim.setClaimType(ClaimType.FIXED_FEE);
            claim.setPolicyHolderContactDate(firstContactDate);
            claim.setChoReference(choReferenceNumber);
            claim.setCreditAgreementDate(creditAgreementDate);
            claim.setGtaNoticeDate(gtaNoticeDate);
            claim.setIndemnityAmount(BigDecimal.ZERO.setScale(2));
            claim.setPercentageLiabilityAccepted(BigDecimal.ZERO.setScale(2));
            claim.setPercentageLiabilityCho(BigDecimal.ZERO.setScale(2));
            claim.setChorganisation(securityInfoProvider.getCurrentUser().getChorganisation());
        }

        claimResult.setClaim(claim);
    }

    private void processInsurerVsInsurerClaim(ClaimResult claimResult, Claim claim) {
        SecurityInfoProvider securityInfoProvider = getBordereauReaderContext().getSecurityInfoProvider();
        ClaimService claimService = getBordereauReaderContext().getClaimService();
        BreBandService breBandService = getBordereauReaderContext().getBreBandService();

        if (claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber)) {
            claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);
            if (claim.getInvoice() != null) {
                claimResult.setClaimParseStatus(ClaimParseStatus.EXIST_INVOICE);
                claimResult.setValid(false);
            } else if (!ClaimType.isInsurerVsInsurer(claim.getClaimType())) {
                claimResult.setClaimParseStatus(ClaimParseStatus.EXISTS_DIFFERENT_CLAIM_TYPE);
                claimResult.setValid(false);
            } else {
                if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)) {
                    claimResult.setClaimParseStatus(ClaimParseStatus.INSURER_VS_INSURER_INVOICE);
                    BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                    claim.setBreBand(choBand);
                    if (isUpdateManagingRepair && managingRepair != null) {
                        claim.setManagingRepair(managingRepair);
                    }
                } else if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED)
                        || claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_PENDING)
                        || claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_REJECTION_ACCEPTED)) {
                    // NOT EDITABNLE CLAIM
                    claimResult.setClaimParseStatus(ClaimParseStatus.CLAIM_NOT_EDITABLE);
                    claimResult.setValid(false);
                } else {
                    // EDITABLE CLAIM
                    claimResult.setClaimParseStatus(ClaimParseStatus.EXIST_CLAIM);
                    if (isUpdateManagingRepair && managingRepair != null) {
                        claim.setManagingRepair(managingRepair);
                    }
                }
            }
        } else {
            claimResult.setClaimParseStatus(ClaimParseStatus.NEW_CLAIM);

            if (managingRepair != null) {
                claim.setManagingRepair(managingRepair);
            }

            claim.setPolicyHolderContactDate(firstContactDate);
            claim.setChoReference(choReferenceNumber);
            claim.setCreditAgreementDate(creditAgreementDate);
            claim.setGtaNoticeDate(gtaNoticeDate);
            claim.setIndemnityAmount(BigDecimal.ZERO.setScale(2));
            claim.setPercentageLiabilityAccepted(new BigDecimal("0.00"));
            claim.setPercentageLiabilityCho(BigDecimal.ZERO.setScale(2));
            claim.setChorganisation(securityInfoProvider.getCurrentUser().getChorganisation());
            claim.setClaimType(ClaimType.INSURER_VS_INSURER);
        }

        claimResult.setClaim(claim);
    }

    private void processSupplementaryInvoice(ClaimResult claimResult, Claim claim) {
        SecurityInfoProvider securityInfoProvider = getBordereauReaderContext().getSecurityInfoProvider();
        ClaimService claimService = getBordereauReaderContext().getClaimService();
        ClaimObjectService claimObjectService = getBordereauReaderContext().getClaimObjectService();
        /*
         * getting claim number from xml to check claim already exists.
         */
        Element rootElement = claimResult.getElement();
        Element claimElement = XMLUtils.getElement(rootElement, "claim");
        Element element = XMLUtils.getElement(claimElement, "customer");
        String customerClaimRef = XmlHelper.getNodeValue(element, "claim-number");

        int choId = chorganisation == null ? -1 : chorganisation.getId();
        if (customerClaimRef != null && !customerClaimRef.isEmpty() && !customerClaimRef.equalsIgnoreCase("N/A") && !customerClaimRef.equalsIgnoreCase("NA")) {

            if ((!isInsurerUpload && !claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber))
                    || (isInsurerUpload && !claimService.isClaimSupplierReferenceNumberExistForCho(choReferenceNumber, choId))) {
                if (isInsurerUpload) {
                    claimResult.setClaimParseStatus(ClaimParseStatus.INSURER_NEW_SUPPLEMENTARY_INVOICE);
                } else {
                    claimResult.setClaimParseStatus(ClaimParseStatus.NEW_SUPPLEMENTARY_INVOICE);
                }

                List<Claim> claimsWithSameCusClaimRef;

                if (isInsurerUpload) {
                    claimsWithSameCusClaimRef = claimService.getInsurerClaimsByCustomerClaimRef(customerClaimRef, securityInfoProvider.getCurrentUser().getInsurer().getId());
                } else {
                    claimsWithSameCusClaimRef = claimService.getCHOClaimsByCustomerClaimRef(customerClaimRef, securityInfoProvider.getCurrentUser().getChorganisation().getId());
                }
                if (claimsWithSameCusClaimRef.size() > 0) {

                    Claim oldClaim = null;

                    if (claimsWithSameCusClaimRef.size() > 1) {
                        StringBuilder sb = new StringBuilder("");
                        List<Claim> duplicateCustomerRefSuppInvClaims = new ArrayList<>();
                        List<Claim> duplicateCustomerRefClaimsWithInv = new ArrayList<>();
                        int commaCount = 0;
                        for (Claim c : claimsWithSameCusClaimRef) {

                            if (ClaimType.isOriginalSupplementaryInvoice(c.getClaimType())) {
                                duplicateCustomerRefSuppInvClaims.add(c);
                            } else if (c.getInvoice() != null) {
                                duplicateCustomerRefClaimsWithInv.add(c);
                                if (commaCount > 0) {
                                    sb.append(", ").append(c.getChoReference());
                                } else {
                                    sb.append(c.getChoReference());
                                    commaCount++;
                                }
                            }
                        }

                        if (duplicateCustomerRefSuppInvClaims.size() == 1) {
                            LOG.warn("{} claims with same customer Claim-number found, choosen to use the one marked with Orig. Supplementary Invoiced 'true' and supp-ref {}", claimsWithSameCusClaimRef.size(), duplicateCustomerRefSuppInvClaims.get(0).getChoReference());
                            oldClaim = duplicateCustomerRefSuppInvClaims.get(0);
                        } else if (duplicateCustomerRefSuppInvClaims.size() > 1) {
                            LOG.warn("More than one Supplementary Invoice - {} Supplementary Invoiced claims with same customer Claim-number found, choosen to use the earliest one with supp-ref {}", duplicateCustomerRefSuppInvClaims.size(), duplicateCustomerRefSuppInvClaims.get(0).getChoReference());
                            oldClaim = duplicateCustomerRefSuppInvClaims.get(0);
                        } else if (duplicateCustomerRefClaimsWithInv.size() == 1) {
                            LOG.warn("{} claims with same customer Claim-number found, choosen to use the one marked with Orig. Supplementary Invoiced 'false' and supp-ref {}", claimsWithSameCusClaimRef.size(), duplicateCustomerRefClaimsWithInv.get(0).getChoReference());
                            oldClaim = duplicateCustomerRefClaimsWithInv.get(0);
                        } else if (duplicateCustomerRefClaimsWithInv.size() > 1) {
                            LOG.warn("Invalid Supplementary Invoice - {} claims with same customer Claim-number found {}.", claimsWithSameCusClaimRef.size(), sb.toString());
                            claimResult.setValid(false);
                            claimResult.getMessage().add(MessageFormat.format("{0} claims found with the same customer claim number (with supplier reference {1}). Please mark one of the claims to identify the original invoice using the ''More Actions'' menu to allow a Supplementary Invoice upload for this claim.", claimsWithSameCusClaimRef.size(), sb.toString()));
                            claim.setChoReference(choReferenceNumber);
                        } else {
                            LOG.warn("Invalid Supplementary Invoice rental status: '{}' - For 'supplementary invoice' invoices to be uploaded the original claim must already have invoice attached.", hireState);
                            claimResult.setValid(false);
                            claimResult.getMessage().add("No Invoice attached to original claim: for a Supplementary Invoice to be uploaded, the original claim must already have an Invoice attached.");
                            claim.setChoReference(choReferenceNumber);
                        }
                    } else {
                        oldClaim = claimsWithSameCusClaimRef.get(0);
                    }
                    /*
                     *  processing Supplementary Invoice.
                     */
                    if (oldClaim != null && oldClaim.getInvoice() != null) {
                        LOG.debug("Valid Supplementary Invoiced claim found.");
                        claim = claimObjectService.cloneClaimForSupplementaryInvoice(oldClaim);
                        if (claim != null) {
                            claim.setChoReference(choReferenceNumber);
                            // Mark first claim as 'Original'
                            if (oldClaim.getClaimType() == ClaimType.GTA) {
                                oldClaim.setClaimType(ClaimType.GTA_ORIGINAL_INVOICE);
                            } else if (oldClaim.getClaimType() == ClaimType.INSURER_CLAIM || oldClaim.getClaimType() == ClaimType.INSURER_INVOICE) {
                                oldClaim.setClaimType(ClaimType.INSURER_ORIGINAL_INVOICE);
                            } else if (oldClaim.getClaimType() == ClaimType.INSURER_VS_INSURER) {
                                oldClaim.setClaimType(ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE);
                            } else if (oldClaim.getClaimType() == ClaimType.SUBSCRIBER) {
                                oldClaim.setClaimType(ClaimType.SUBSCRIBER_ORIGINAL_INVOICE);
                            } else if (oldClaim.getClaimType() == ClaimType.FIXED_FEE) {
                                oldClaim.setClaimType(ClaimType.FIXED_FEE_ORIGINAL_INVOICE);
                            } else if (oldClaim.getClaimType() == ClaimType.COLLABORATION_PROTOCOL) {
                                oldClaim.setClaimType(ClaimType.COLLABORATION_PROTOCOL_ORIGINAL_INVOICE);
                            } else if (!ClaimType.isOriginalSupplementaryInvoice(oldClaim.getClaimType())) { // Not already marked as a supplimentary invoice
                                LOG.error("Incorrect type for original claim '{}' [for supplementary '{}'] - should be one of GTA, InsurerVsInsurer, Subscriber, Insurer Claim, Insurer Invoice, Collaboration Protocol: {}",
                                            new Object[]{oldClaim.getChoReference(), claim.getChoReference(), oldClaim.getClaimType()});
                                claimResult.setValid(false);
                                claimResult.getMessage().add("Unexpected type of claim found for original claim. Please contact CHOX support.");
                                claim.setChoReference(choReferenceNumber);
                            }
                        } else {
                            LOG.error("mapping failed between old and new claim");
                            claimResult.setValid(false);
                            claimResult.getMessage().add("Unexpected error encountered while mapping this invoice to already existing claim. Please contact CHOX support.");
//                            claim.setChoReference(choReferenceNumber);
                        }
                    } else if (oldClaim != null) {
                        LOG.warn("Invalid Supplementary Invoice rental status: '{}' - For 'supplementary invoice' invoices to be uploaded the original claim must already have invoice attached.", hireState);
                        claimResult.setValid(false);
                        claimResult.getMessage().add("No Invoice attached to original claim: for a Supplementary Invoice to be uploaded, the original claim must already have an Invoice attached.");
                        claim.setChoReference(choReferenceNumber);
                    }
                } else {
                    LOG.warn("Invalid Supplementary Invoice rental status: '{}' - For 'supplementary invoice' invoices to be uploaded the original claim must already exists in the system.", hireState);
                    claimResult.setValid(false);
                    claimResult.getMessage().add("Original claim does not exist: for supplementary invoices, an original claim must already exist in the system when linking claims via the customer claim number.");
                    claim.setChoReference(choReferenceNumber);
                }
            } else {
                if (isInsurerUpload) {
                    claim = claimService.getClaimByChoIdAndCHOReferenceNumber(chorganisation.getId(), choReferenceNumber);
                } else {
                    claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);
                }
                if (claim.getInvoice() != null) {
                    if (ClaimType.isSupplementaryInvoice(claim.getClaimType())) {
                        claimResult.setClaimParseStatus(ClaimParseStatus.EXISTING_SUPPLEMENTARY_INVOICE);
                        claimResult.setValid(false);
                    } else {
                        claimResult.setClaimParseStatus(ClaimParseStatus.EXIST_INVOICE);
                        claimResult.setValid(false);
                    }
                } else {
                    if (isInsurerUpload) {
                        claimResult.setClaimParseStatus(ClaimParseStatus.EXISTS_INSURER_CLAIM);
                    } else {
                        claimResult.setClaimParseStatus(ClaimParseStatus.EXIST_CLAIM);
                    }
                }
            }
        } else {
            LOG.warn("Invalid Supplementary Invoice  - For 'supplementary invoice' invoices to be uploaded the customer claim reference should be present to upload against original claim.");
            claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_SCHEMA);
            claimResult.setValid(false);
            claimResult.getMessage().add("Customer claim number is not valid: for supplementary invoices, the customer claim number cannot be empty or contain 'NA' or 'N/A'.");
            claim.setChoReference(choReferenceNumber);
        }

        claimResult.setClaim(claim);
    }

    private void processHireMonitoring(ClaimResult claimResult, Claim claim) {
        SecurityInfoProvider securityInfoProvider = getBordereauReaderContext().getSecurityInfoProvider();
        ClaimService claimService = getBordereauReaderContext().getClaimService();

        int choId = chorganisation == null ? -1 : chorganisation.getId();
        if (isInsurerUpload && !securityInfoProvider.getCurrentUser().getInsurer().isClaimUploadEnabled()) {
            LOG.warn("Invalid new claim rental status: '{}' - Insurer does not have Claim upload enabled", hireState);
            claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_HIRE_STATE);
            claimResult.setValid(false);
            claimResult.getMessage().add("This Insurer does not allow claim upload. Please contact CHOX support.");
            claim.setChoReference(choReferenceNumber);
        } else if ((!isInsurerUpload && claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber))
                || (!isInsurerUpload && claimService.isClaimSupplierReferenceNumberExistForCho(choReferenceNumber, choId))) {
            if (isInsurerUpload) {
                claim = claimService.getClaimByChoIdAndCHOReferenceNumber(chorganisation.getId(), choReferenceNumber);
            } else {
                claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);
            }

            if (claim.getInvoice() != null) {
                if (isInsurerUpload) {
                    claimResult.setClaimParseStatus(ClaimParseStatus.INSURER_EXIST_INVOICE);
                } else {
                    claimResult.setClaimParseStatus(ClaimParseStatus.EXIST_INVOICE);
                }
                claimResult.setValid(false);
            } else if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO)) {
                /*
                 *  if the hire state is hire monitor but claim is not in
                 *  CLAIM_AWAITING_CAR_HIRE_INFO then set error message and
                 *  do not process the claim.
                 */
                if (isInsurerUpload) {
                    claimResult.setClaimParseStatus(ClaimParseStatus.INSURER_HIRE_MONITORING);
                } else {
                    claimResult.setClaimParseStatus(ClaimParseStatus.HIRE_MONITORING);
                }
                if (isUpdateManagingRepair && managingRepair != null) {
                    claim.setManagingRepair(managingRepair);
                }
            } else {
                LOG.warn("Invalid rental status: '{}' - For 'hire monitoring' claims to be uploaded the claims must be in the 'AwaitingCarHireInfo' status.", hireState);
                claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_CLAIM_STATUS);
                claimResult.setValid(false);
                claimResult.getMessage().add("For 'hire monitoring' claims to be uploaded the claims must be in the 'AwaitingCarHireInfo' status.");
                claim.setChoReference(choReferenceNumber);
            }
        } else {
            LOG.warn("Invalid hire state rental status: '{}' - For 'hire monitoring' claims to be uploaded the claims must be exists in the system", hireState);
            claimResult.setClaimParseStatus(ClaimParseStatus.INVALID_CLAIM_STATUS);
            claimResult.setValid(false);
            claimResult.getMessage().add("For 'hire monitoring' claims to be uploaded the claims must already exists in the system.");
            claim.setChoReference(choReferenceNumber);
        }

        claimResult.setClaim(claim);
    }

    private String getTPIidentificationStringForInsurer(String insurerAliasName) {
        String tpiIdentificationString = null;

        if (insurerAliasName != null && insurerAliasName.length() > 0) {
            InsurerAlias alias = getBordereauReaderContext().getInsurerAliasService().getInsurerByAliasName(insurerAliasName);
            Insurer insurer = alias.getInsurer();
            tpiIdentificationString = insurer.getTpiIdentificationString().replaceAll("[^A-Za-z0-9]", "");
        }

        return tpiIdentificationString;
    }

    private boolean checkTpiServiceActivatedForInsurerAndRentalStatus(String insurerAliasNames, String rentalStatus) {
        boolean returnValue = false;

        if (insurerAliasNames != null && insurerAliasNames.length() > 0) {
            InsurerAlias alias = getBordereauReaderContext().getInsurerAliasService().getInsurerByAliasName(insurerAliasNames);
            if (alias == null) {
                return returnValue;
            }
            Insurer insurer = alias.getInsurer();
            if (insurer == null) {
                return returnValue;
            }
            if (insurer.isThirdPartyInterventionActivated() && insurer.getTpiIdentificationString().equalsIgnoreCase(rentalStatus)) {
                returnValue = true;
            }
        } else {
            return returnValue;
        }

        return returnValue;
    }

    private boolean checkTpiServiceActivatedForInsurer(String insurerAliasNames) {
        boolean returnValue = false;

        if (insurerAliasNames != null && insurerAliasNames.length() > 0) {
            InsurerAlias alias = getBordereauReaderContext().getInsurerAliasService().getInsurerByAliasName(insurerAliasNames);
            if (alias == null) {
                return returnValue;
            }
            Insurer insurer = alias.getInsurer();
            if (insurer == null) {
                return returnValue;
            }
            if (insurer.isThirdPartyInterventionActivated()) {
                returnValue = true;
            }
        } else {
            return returnValue;
        }

        return returnValue;
    }

    private boolean checkSubscriberActivatedForInsurer(String insurerAliasName) {
        Insurer insurer;

        if (insurerAliasName != null && insurerAliasName.length() > 0) {
            InsurerAlias alias = getBordereauReaderContext().getInsurerAliasService().getInsurerByAliasName(insurerAliasName);
            if (alias == null) {
                LOG.warn("No insurer found with alias name '{}'", insurerAliasName);
                return false;
            }
            insurer = alias.getInsurer();
            if (insurer == null) {
                return false;
            }
        } else {
            LOG.debug("No insurer name provided.");
            return false;
        }

        return insurer.isAllowSubscriberClaims();
    }

    private boolean checkFixedFeeActivatedForInsurer(String insurerAliasName) {
        Insurer insurer;

        if (insurerAliasName != null && insurerAliasName.length() > 0) {
            InsurerAlias alias = getBordereauReaderContext().getInsurerAliasService().getInsurerByAliasName(insurerAliasName);
            if (alias == null) {
                LOG.warn("No insurer found with alias name '{}'", insurerAliasName);
                return false;
            }
            insurer = alias.getInsurer();
            if (insurer == null) {
                return false;
            }
        } else {
            LOG.debug("No insurer name provided.");
            return false;
        }

        return insurer.isAllowFixedFeeClaims();
    }

    private boolean checkCollaborationProtocolActivatedForInsurer(String insurerAliasName) {
        Insurer insurer;

        if (insurerAliasName != null && insurerAliasName.length() > 0) {
            InsurerAlias alias = getBordereauReaderContext().getInsurerAliasService().getInsurerByAliasName(insurerAliasName);
            if (alias == null) {
                LOG.warn("No insurer found with alias name '{}'", insurerAliasName);
                return false;
            }
            insurer = alias.getInsurer();
            if (insurer == null) {
                return false;
            }
        } else {
            LOG.debug("No insurer name provided.");
            return false;
        }

        return insurer.isAllowCollaborationProtocolClaims();
    }
}
