package idas.chox.service.xml.readers;

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
import idas.chox.core.services.InsurerAliasService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import idas.chox.core.xmlValidation.RentalStatus;
import idas.chox.service.claim.ClaimObjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

public class ClaimHeaderReader extends BaseEntityReader {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimHeaderReader.class);
    private static String sectionName = "Claim Header";
    // PAGE PARAMETERS
    private Boolean managingRepair;
    private Date firstContactDate;
    private Date creditAgreementDate;
    private Date gtaNoticeDate;
    private String choReferenceNumber;
    private String supplierAliasName;
    private String rentalStatus;
    private boolean isUpdateManagingRepair = false;

    @Override
    public void execute(ClaimResult claimResult) throws DOMException, XPathExpressionException, Exception {

        LOG.debug("Validating claimResult");
        if (!validate(claimResult)) {
            LOG.debug("Validation failed: {}", claimResult.getProcessStatus());
        }
        // If the header fails validation, we'll still process
        // We need to do this as this is the header and we need to
        // check the claim status which is needed for further processing
        process(claimResult);
    }

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {
        LOG.debug("Validating Claim Header: claimResult is {}", claimResult);

        claimResult.setCheckDataValid(true);

        NodeHelper.nodeValidate(sectionName, "first-contact", claimResult.getElement(), claimResult, getDataValidationParameter());
        NodeHelper.nodeValidate(sectionName, "managing-repair", claimResult.getElement(), claimResult, getDataValidationParameter());
        NodeHelper.nodeValidate(sectionName, "agreement-signed", claimResult.getElement(), claimResult, getDataValidationParameter());
        NodeHelper.nodeValidate(sectionName, "gta-notice", claimResult.getElement(), claimResult, getDataValidationParameter());
        NodeHelper.nodeValidate(sectionName, "hire-state", claimResult.getElement(), claimResult, getDataValidationParameter());
        NodeHelper.nodeValidate(sectionName, "supplier-reference", claimResult.getElement(), claimResult, getDataValidationParameter());

        if (NodeHelper.nodeValidateBoolean(sectionName, "first-contact", claimResult.getElement(), claimResult, getDataValidationParameter())) {
            firstContactDate = XmlHelper.getDateFromNode(claimResult.getElement(), "first-contact");
        }

        if (NodeHelper.nodeValidateBoolean(sectionName, "hire-state", claimResult.getElement(), claimResult, getDataValidationParameter())) {
            rentalStatus = XmlHelper.getNodeValue(claimResult.getElement(), "hire-state");
            // Remove white space and convert to lower case
            rentalStatus = rentalStatus.trim().replaceAll("\\s+", "").toLowerCase();
        }

        if (RentalStatus.isInsurerUploadRentalStatus(rentalStatus) && getBordereauReaderContext().getSecurityInfoProvider().getIsINS()) {

            Integer insurerId = getBordereauReaderContext().getSecurityInfoProvider().getCurrentUser().getInsurer().getId();
            ChorganisationAliasService chorganisationAliasService = this.getBordereauReaderContext().getChorganisationAliasService();
            InsurerChorganisationService insurerChorganisationService = this.getBordereauReaderContext().getInsurerChorganisationService();
            claimResult = NodeHelper.nodeChorganisationAliasValidate(sectionName, "supplier-name", claimResult.getElement(),
                    claimResult, getDataValidationParameter(),
                    chorganisationAliasService, insurerChorganisationService,
                    insurerId);
            if (claimResult.isValid()) {
                supplierAliasName = XmlHelper.getNodeValue(claimResult.getElement(), "supplier-name");
            }
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

        if (NodeHelper.nodeValidateBoolean(sectionName, "supplier-reference", claimResult.getElement(), claimResult, getDataValidationParameter())) {
            choReferenceNumber = XmlHelper.getNodeValue(claimResult.getElement(), "supplier-reference");
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

        /*
         *  TPI PROCESS
         */
        if (securityInfoProvider.getIsINS() && !RentalStatus.isInsurerUploadRentalStatus(rentalStatus)) {
            LOG.warn("Invalid hire-state found for for Insurer Upload: {}", rentalStatus);
            claimResult.setClaimParseStatus(ClaimParseStatus.invalidSchema);
            claimResult.setValid(false);
            claimResult.getMessage().add("The value provided for the ‘hire state’ is incorrect. Valid value is ‘InsurerUpload’.");
            claim.setChoReference(choReferenceNumber);
            claimResult.setClaim(claim);
        } else if (securityInfoProvider.getIsINS()) {
            LOG.debug("Insurer Invoice upload found");
            processInsurerInvoice(claimResult, claim);
        } else if (securityInfoProvider.getCurrentUser().getChorganisation().isThirdPartyInterventionActivated()
                && !(RentalStatus.isValid(rentalStatus))) {
            claim.setClaimType(ClaimType.TPI);
            LOG.debug("TPI Claim found");
            LOG.debug("TPI is activated for this CHO");

            processTpiInvoice(claimResult, claim);

        } else { // Non TPI PROCESS
            LOG.debug("Non-TPI claim found");
            // First check that this is not a TPI claim: verify rental status is either 'InProgress' or 'Complete' (or blank)
            // see bug#819 - Reserva - Prevent Reserva Cases Being Uploaded As Normal CHOX Cases
            if (!RentalStatus.isValid(rentalStatus) || (securityInfoProvider.getIsCHO() && RentalStatus.isInsurerUploadRentalStatus(rentalStatus))) {
                LOG.warn("Invalid rental status: '{}' - may be trying to upload a TPI invoice and TPI not activated for this insurer.", rentalStatus);
                claimResult.setClaimParseStatus(ClaimParseStatus.invalidSchema);
                claimResult.setValid(false);
                claimResult.getMessage().add("The value provided for the ‘hire state’ is incorrect. Valid values are: ‘InProgress’, ‘Complete’, 'Off Hired', 'Supplementary Invoice', 'Hire Monitoring', Subscriber' or 'Insurer vs Insurer'.");
                claim.setChoReference(choReferenceNumber);
                claimResult.setClaim(claim);
            } else if (RentalStatus.isOffHiredRentalStatus(rentalStatus)) {
                /*
                 *   Process Off-Hired Invoice
                 */
                LOG.debug("PROCESSING HIREMONITORING AND INVOICE");
                processOffHiredInvoice(claimResult, claim);

            } else if (RentalStatus.isInProgressOrComplete(rentalStatus)) {
                /*
                 *   Process Normal chox claim
                 */
                LOG.debug("PROCESSING Normal Chox Claim");
                processNormalChoxClaim(claimResult, claim);
            } else if (RentalStatus.isInsurerVsInsurerRentalStatus(rentalStatus)) {
                /*
                 *   Process Insurer vs. Insurer chox claim
                 */
                LOG.debug("PROCESSING Insurer vs Insurer Chox Claim");
                processInsurerChoxClaim(claimResult, claim);
            } else if (RentalStatus.isSupplementaryInvoiceRentalStatus(rentalStatus)) {
                /*
                 *   Process Supplementary Invoice 
                 */
                LOG.debug("PROCESSING Supplementary Invoice");
                processSupplementaryInvoice(claimResult, claim);
            } else if (RentalStatus.isHireMonitoringRentalStatus(rentalStatus)) {
                /*
                 *   Process Hire Monitoring Only
                 */
                LOG.debug("PROCESSING Hire Monitering Claim");
                processHireMonitoring(claimResult, claim);
            } else if (RentalStatus.isSubscriberRentalStatus(rentalStatus)) {
                if (securityInfoProvider.getCurrentUser().getChorganisation().isEnableSubscriberClaims()) {
                    /*
                     *   Process Subscriber claim
                     */
                    LOG.debug("PROCESSING Subscriber Claim");
                    processSubscriberClaim(claimResult, claim);
                } else {
                    claimResult.setClaimParseStatus(ClaimParseStatus.invalidSchema);
                    claimResult.setValid(false);
                    claimResult.getMessage().add("Subscriber claims have not been activated. Please contact CHOX support if you wish to upload subscriber claims.");
                    claim.setChoReference(choReferenceNumber);
                    claimResult.setClaim(claim);
                    
                }
            }
        }

    }

    private void processInsurerInvoice(ClaimResult claimResult, Claim claim) {
        SecurityInfoProvider securityInfoProvider = getBordereauReaderContext().getSecurityInfoProvider();
        ClaimService claimService = getBordereauReaderContext().getClaimService();

        if (claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber)) {
            LOG.debug("Insurer trying to upload a claim that already exists: '{}'.", choReferenceNumber);
            claimResult.setClaimParseStatus(ClaimParseStatus.invalidClaimStatus);
            claimResult.setValid(false);
            claimResult.getMessage().add("This claim already exists.");
            claim.setChoReference(choReferenceNumber);

        } else {
            LOG.debug("Valid Insurer upload claim found.");
            claimResult.setClaimParseStatus(ClaimParseStatus.insurerUpload);
            if (managingRepair != null) {
                claim.setManagingRepair(managingRepair);
            }
            claim.setPolicyHolderContactDate(firstContactDate);
            // claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
            claim.setChoReference(choReferenceNumber);
            claim.setCreditAgreementDate(creditAgreementDate);
            claim.setGtaNoticeDate(gtaNoticeDate);
            claim.setIndemnityAmount(new BigDecimal("0.00"));
            claim.setPercentageLiabilityAccepted(new BigDecimal("100.00"));
            claim.setPercentageLiabilityCho(new BigDecimal("0.00"));
            claim.setInsurer(securityInfoProvider.getCurrentUser().getInsurer());
            claim.setClaimType(ClaimType.INSURER_UPLOAD);
            ChorganisationAliasService chorganisationAliasService = this.getBordereauReaderContext().getChorganisationAliasService();
            if (supplierAliasName != null && !supplierAliasName.isEmpty()) {
                ChorganisationAlias alias = chorganisationAliasService.getChorganisationByAliasName(supplierAliasName);
                Chorganisation chorganisation = alias.getChorganisation();
                //Set claim Insurer equal to third party insurer
                claim.setChorganisation(chorganisation);
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
            claimResult.setClaimParseStatus(ClaimParseStatus.tpiNotAcceptedByInsurer);
            claimResult.setValid(false);
            claimResult.getMessage().add("This Insurer does not accept TPI invoices. Please contact CHOX support.");
            claim.setChoReference(choReferenceNumber);
        } else if (!checkTpiServiceActivatedForInsurerAndRentalStatus(insurerAliasNames, rentalStatus)) {
            LOG.debug("CHO is trying to upload a TPI invoice with an invalid hire-state field");
            claimResult.setClaimParseStatus(ClaimParseStatus.tpiNotRecognized);
            claimResult.setValid(false);
            LOG.warn("CHO is trying to upload a TPI invoice with an invalid hire-state field: {}", getTPIidentificationStringForInsurer(insurerAliasNames));
            claimResult.getMessage().add("The value provided for the ‘hire state’ is incorrect, it must be ‘" + getTPIidentificationStringForInsurer(insurerAliasNames) + "’ for third party intervention claims against this Insurer");
            claim.setChoReference(choReferenceNumber);
        } else if (claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber)) {
            LOG.debug("Claim supplier reference already exists: {}", choReferenceNumber);
            claimResult.setClaimParseStatus(ClaimParseStatus.existInvoice);
            //claimResult.setClaim(claimService.getClaimByCHOReferenceNumber(choReferenceNumber));
            claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);
            claimResult.setValid(false);
        } else {
            LOG.debug("Valid TPI invoice claim found.");
            claimResult.setClaimParseStatus(ClaimParseStatus.tpiIntervention);
            if (managingRepair != null) {
                claim.setManagingRepair(managingRepair);
            }
            claim.setPolicyHolderContactDate(firstContactDate);
            // claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
            claim.setChoReference(choReferenceNumber);
            claim.setCreditAgreementDate(creditAgreementDate);
            claim.setGtaNoticeDate(gtaNoticeDate);
            claim.setIndemnityAmount(new BigDecimal("0.00"));
            claim.setPercentageLiabilityAccepted(new BigDecimal("100.00"));
            claim.setPercentageLiabilityCho(new BigDecimal("0.00"));
            claim.setChorganisation(securityInfoProvider.getCurrentUser().getChorganisation());
            claim.setClaimType(ClaimType.TPI);
        }

        claimResult.setClaim(claim);
    }

    private void processOffHiredInvoice(ClaimResult claimResult, Claim claim) {

        ClaimService claimService = getBordereauReaderContext().getClaimService();
        BreBandService breBandService = getBordereauReaderContext().getBreBandService();

        if (claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber)) {
            claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);

            if (claim.getInvoice() != null) {
                claimResult.setClaimParseStatus(ClaimParseStatus.existInvoice);
                claimResult.setValid(false);
            } /*
             *  if the hire state is off hired but claim is not in CLAIM_AWAITING_CAR_HIRE_INFO then set error message and do not process the claim.
             */ else if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO)) {
                claimResult.setClaimParseStatus(ClaimParseStatus.hireMonitoringAndNewInvoice);
                BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                claim.setBreBand(choBand);
                if (isUpdateManagingRepair && managingRepair != null) {
                    claim.setManagingRepair(managingRepair);
                }
            } else if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)) {

                claimResult.setClaimParseStatus(ClaimParseStatus.newInvoice);
                BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                claim.setBreBand(choBand);
                if (isUpdateManagingRepair && managingRepair != null) {
                    claim.setManagingRepair(managingRepair);
                }

            } else {
                LOG.warn("Invalid rental status: '{}' - For ‘Off Hired’ claims/invoices to be uploaded the claims must be in the ’AwaitingCarHireInfo’ status.", rentalStatus);
                claimResult.setClaimParseStatus(ClaimParseStatus.invalidClaimStatus);
                claimResult.setValid(false);
                claimResult.getMessage().add("For ‘Off Hired’ claims/invoices to be uploaded the claims must be in the ’AwaitingCarHireInfo’ status.");
                claim.setChoReference(choReferenceNumber);

            }

        } else {

            LOG.warn("Invalid new claim rental status: '{}' - For ‘Off Hired’ claims/invoices to be uploaded the claims must be in the ’AwaitingCarHireInfo’ status.", rentalStatus);
            claimResult.setClaimParseStatus(ClaimParseStatus.invalidClaimStatus);
            claimResult.setValid(false);
            claimResult.getMessage().add("For ‘Off Hired’ claims/invoices to be uploaded the claims must already exists in the system.");
            claim.setChoReference(choReferenceNumber);
        }

        claimResult.setClaim(claim);


    }

    private void processNormalChoxClaim(ClaimResult claimResult, Claim claim) {

        SecurityInfoProvider securityInfoProvider = getBordereauReaderContext().getSecurityInfoProvider();
        ClaimService claimService = getBordereauReaderContext().getClaimService();
        BreBandService breBandService = getBordereauReaderContext().getBreBandService();

        if (claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber)) {
            claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);
            if (claim.getInvoice() != null) {
                claimResult.setClaimParseStatus(ClaimParseStatus.existInvoice);
                claimResult.setValid(false);
            } else {
                if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)) {
                    claimResult.setClaimParseStatus(ClaimParseStatus.newInvoice);
                    BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                    claim.setBreBand(choBand);
                    if (isUpdateManagingRepair && managingRepair != null) {
                        claim.setManagingRepair(managingRepair);
                    }
                } else if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED)
                        || claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_PENDING)
                        || claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_REJECTION_ACCEPTED)) {
                    // NOT EDITABNLE CLAIM
                    claimResult.setClaimParseStatus(ClaimParseStatus.ClaimNotEditable);
                    claimResult.setValid(false);
                } else {
                    // EDITABLE CLAIM
                    claimResult.setClaimParseStatus(ClaimParseStatus.existClaim);
                }
            }
        } else {
            claimResult.setClaimParseStatus(ClaimParseStatus.newClaim);
            if (managingRepair != null) {
                claim.setManagingRepair(managingRepair);
            }
            claim.setPolicyHolderContactDate(firstContactDate);
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
            claim.setChoReference(choReferenceNumber);
            claim.setCreditAgreementDate(creditAgreementDate);
            claim.setGtaNoticeDate(gtaNoticeDate);
            claim.setIndemnityAmount(new BigDecimal("0.00"));
            claim.setPercentageLiabilityAccepted(new BigDecimal("0.00"));
            claim.setPercentageLiabilityCho(new BigDecimal("0.00"));
            claim.setChorganisation(securityInfoProvider.getCurrentUser().getChorganisation());
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
            LOG.debug("CHO is attempting to upload a Subscriber claim to an  Insurer");
            claimResult.setClaimParseStatus(ClaimParseStatus.subscriberNotAcceptedByInsurer);
            claimResult.setValid(false);
            claimResult.getMessage().add("The Insurer '" + insurerName + "'does not accept Subscriber claims. Please contact CHOX support.");
            claim.setChoReference(choReferenceNumber);
        } else if (claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber)) {
            claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);
            if (claim.getInvoice() != null) {
                claimResult.setClaimParseStatus(ClaimParseStatus.existInvoice);
                claimResult.setValid(false);
            } else {
                if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)) {
                    claimResult.setClaimParseStatus(ClaimParseStatus.newInvoice);
                    BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                    claim.setBreBand(choBand);
                    if (isUpdateManagingRepair && managingRepair != null) {
                        claim.setManagingRepair(managingRepair);
                    }
                } else if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED)
                        || claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_PENDING)
                        || claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_REJECTION_ACCEPTED)) {
                    // NOT EDITABNLE CLAIM
                    claimResult.setClaimParseStatus(ClaimParseStatus.ClaimNotEditable);
                    claimResult.setValid(false);
                } else {
                    // EDITABLE CLAIM
                    claimResult.setClaimParseStatus(ClaimParseStatus.existSubscriberClaim);
                }
            }
        } else {
            claimResult.setClaimParseStatus(ClaimParseStatus.newSubscriberClaim);
            if (managingRepair != null) {
                claim.setManagingRepair(managingRepair);
            }
            claim.setClaimType(ClaimType.SUBSCRIBER);
            claim.setPolicyHolderContactDate(firstContactDate);
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
            claim.setChoReference(choReferenceNumber);
            claim.setCreditAgreementDate(creditAgreementDate);
            claim.setGtaNoticeDate(gtaNoticeDate);
            claim.setIndemnityAmount(new BigDecimal("0.00"));
            claim.setPercentageLiabilityAccepted(new BigDecimal("0.00"));
            claim.setPercentageLiabilityCho(new BigDecimal("0.00"));
            claim.setChorganisation(securityInfoProvider.getCurrentUser().getChorganisation());
        }

        claimResult.setClaim(claim);

    }

    private void processInsurerChoxClaim(ClaimResult claimResult, Claim claim) {

        SecurityInfoProvider securityInfoProvider = getBordereauReaderContext().getSecurityInfoProvider();
        ClaimService claimService = getBordereauReaderContext().getClaimService();
        BreBandService breBandService = getBordereauReaderContext().getBreBandService();

        if (claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber)) {
            claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);
            if (claim.getInvoice() != null) {
                claimResult.setClaimParseStatus(ClaimParseStatus.existInvoice);
                claimResult.setValid(false);
            } else {
                if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)) {
                    claimResult.setClaimParseStatus(ClaimParseStatus.insurerVsInsurerInvoice);
                    BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                    claim.setBreBand(choBand);
                    if (isUpdateManagingRepair && managingRepair != null) {
                        claim.setManagingRepair(managingRepair);
                    }
                } else if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED)
                        || claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_PENDING)
                        || claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_REJECTION_ACCEPTED)) {
                    // NOT EDITABNLE CLAIM
                    claimResult.setClaimParseStatus(ClaimParseStatus.ClaimNotEditable);
                    claimResult.setValid(false);
                } else {
                    // EDITABLE CLAIM
                    claimResult.setClaimParseStatus(ClaimParseStatus.existClaim);
                }
            }
        } else {
            claimResult.setClaimParseStatus(ClaimParseStatus.newClaim);

            if (managingRepair != null) {
                claim.setManagingRepair(managingRepair);
            }

            claim.setPolicyHolderContactDate(firstContactDate);
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
            claim.setChoReference(choReferenceNumber);
            claim.setCreditAgreementDate(creditAgreementDate);
            claim.setGtaNoticeDate(gtaNoticeDate);
            claim.setIndemnityAmount(new BigDecimal("0.00"));
            claim.setPercentageLiabilityAccepted(new BigDecimal("0.00"));
            claim.setPercentageLiabilityCho(new BigDecimal("0.00"));
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

        if (customerClaimRef != null && !customerClaimRef.isEmpty() && !customerClaimRef.equalsIgnoreCase("N/A") && !customerClaimRef.equalsIgnoreCase("NA")) {

            if (!claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber)) {
                List<Claim> claimsWithSameCusClaimRef = claimService.getClaimsByCustomerClaimRef(customerClaimRef, securityInfoProvider.getCurrentUser().getChorganisation().getId());
                if (claimsWithSameCusClaimRef.size() > 0) {

                    Claim oldClaim = null;

                    if (claimsWithSameCusClaimRef.size() > 1) {
                        StringBuilder sb = new StringBuilder("");
                        List<Claim> duplicateCustomerRefSuppInvClaims = new ArrayList<Claim>();
                        List<Claim> duplicateCustomerRefClaimsWithInv = new ArrayList<Claim>();
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

                        if (duplicateCustomerRefSuppInvClaims.size() > 0 && duplicateCustomerRefSuppInvClaims.size() <= 1) {
                            LOG.warn("{} claims with same customer Claim-number found, choosen to use the one marked with Supplementary Invoiced 'true' and supp-ref {}", claimsWithSameCusClaimRef.size(), duplicateCustomerRefSuppInvClaims.get(0).getChoReference());
                            oldClaim = duplicateCustomerRefSuppInvClaims.get(0);
                        } else if (duplicateCustomerRefSuppInvClaims.size() > 1) {
                            LOG.warn("More than one Supplementary Invoice - {} Supplementary Invoiced claims with same customer Claim-number found, choosen to use the earliest one with supp-ref {}", duplicateCustomerRefSuppInvClaims.size(), duplicateCustomerRefSuppInvClaims.get(0).getChoReference());
                            oldClaim = duplicateCustomerRefSuppInvClaims.get(0);
                        } else if (duplicateCustomerRefClaimsWithInv.size() > 0 && duplicateCustomerRefClaimsWithInv.size() <= 1) {
                            LOG.warn("{} claims with same customer Claim-number found, choosen to use the one marked with Supplementary Invoiced 'true' and supp-ref {}", claimsWithSameCusClaimRef.size(), duplicateCustomerRefClaimsWithInv.get(0).getChoReference());
                            oldClaim = duplicateCustomerRefClaimsWithInv.get(0);
                        } else if (duplicateCustomerRefClaimsWithInv.size() > 1) {
                            LOG.warn("Invalid Supplementary Invoice - {} claims with same customer Claim-number found {}.", claimsWithSameCusClaimRef.size(), sb.toString());
                            claimResult.setClaimParseStatus(ClaimParseStatus.newSupplementaryInvoice);
                            claimResult.setValid(false);
                            claimResult.getMessage().add(claimsWithSameCusClaimRef.size() + " claims found with the same customer claim number (with supplier reference " + sb.toString() + "). Please mark one of the claims to identify the original invoice using the ‘More Actions’ menu to allow a Supplementary Invoice upload for this claim.");
                            claim.setChoReference(choReferenceNumber);
                        } else {
                            LOG.warn("Invalid Supplementary Invoice rental status: '{}' - For ‘supplementary invoice’ invoices to be uploaded the original claim must already have invoice attached.", rentalStatus);
                            claimResult.setClaimParseStatus(ClaimParseStatus.newSupplementaryInvoice);
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
                            claimResult.setClaimParseStatus(ClaimParseStatus.newSupplementaryInvoice);
                            claim.setChoReference(choReferenceNumber);
                            // Mark first claim as 'Original'
                            if (oldClaim.getClaimType() == ClaimType.GTA) {
                                oldClaim.setClaimType(ClaimType.GTA_ORIGINAL_INVOICE);
                            }
                            else if (oldClaim.getClaimType() == ClaimType.INSURER_VS_INSURER) {
                                oldClaim.setClaimType(ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE);
                            }
                            else if (oldClaim.getClaimType() == ClaimType.SUBSCRIBER) {
                                oldClaim.setClaimType(ClaimType.SUBSCRIBER_ORIGINAL_INVOICE);
                            } else {
                                LOG.error("Incorrect type for original claim '{}' (should be one of GTA, InsurerVsInsurer, Subscriber): {}", claim.getChoReference(), claim.getClaimType());
                                claimResult.setClaimParseStatus(ClaimParseStatus.newSupplementaryInvoice);
                                claimResult.setValid(false);
                                claimResult.getMessage().add("Unexpected type of claim found for original claim. Please contact CHOX support.");
                                claim.setChoReference(choReferenceNumber);
                            }

                        } else {
                            LOG.error("mapping failed between old and new claim");
                            claimResult.setClaimParseStatus(ClaimParseStatus.newSupplementaryInvoice);
                            claimResult.setValid(false);
                            claimResult.getMessage().add("Unexpected error encountered while mapping this invoice to already existing claim. Please contact CHOX support.");
                            claim.setChoReference(choReferenceNumber);
                        }

                    } else if (oldClaim != null) {

                        LOG.warn("Invalid Supplementary Invoice rental status: '{}' - For ‘supplementary invoice’ invoices to be uploaded the original claim must already have invoice attached.", rentalStatus);
                        claimResult.setClaimParseStatus(ClaimParseStatus.newSupplementaryInvoice);
                        claimResult.setValid(false);
                        claimResult.getMessage().add("No Invoice attached to original claim: for a Supplementary Invoice to be uploaded, the original claim must already have an Invoice attached.");
                        claim.setChoReference(choReferenceNumber);
                    }

                } else {
                    LOG.warn("Invalid Supplementary Invoice rental status: '{}' - For ‘supplementary invoice’ invoices to be uploaded the original claim must already exists in the system.", rentalStatus);
                    claimResult.setClaimParseStatus(ClaimParseStatus.newSupplementaryInvoice);
                    claimResult.setValid(false);
                    claimResult.getMessage().add("Original claim does not exist: for supplementary invoices, an original claim must already exist in the system when linking claims via the customer claim number.");
                    claim.setChoReference(choReferenceNumber);
                }
            } else {

                claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);
                if (claim.getInvoice() != null) {
                    if (ClaimType.isSupplementaryInvoice(claim.getClaimType())) {
                        claimResult.setClaimParseStatus(ClaimParseStatus.existingSupplementaryInvoice);
                        claimResult.setValid(false);
                    } else {
                        claimResult.setClaimParseStatus(ClaimParseStatus.existInvoice);
                        claimResult.setValid(false);
                    }
                } else {
                    claimResult.setClaimParseStatus(ClaimParseStatus.existClaim);

                }
            }

        } else {

            LOG.warn("Invalid Supplementary Invoice  - For ‘supplementary invoice’ invoices to be uploaded the customer claim reference should be present to upload against original claim.");
            claimResult.setClaimParseStatus(ClaimParseStatus.invalidSchema);
            claimResult.setValid(false);
            claimResult.getMessage().add("Customer claim number is not valid: for supplementary invoices, the customer claim number cannot be empty or contain ‘NA’ or ‘N/A’.");
            claim.setChoReference(choReferenceNumber);

        }

        claimResult.setClaim(claim);
    }

    private void processHireMonitoring(ClaimResult claimResult, Claim claim) {

        ClaimService claimService = getBordereauReaderContext().getClaimService();

        if (claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber)) {
            claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);

            if (claim.getInvoice() != null) {
                claimResult.setClaimParseStatus(ClaimParseStatus.existInvoice);
                claimResult.setValid(false);
            } /*
             *  if the hire state is hire monitor but claim is not in CLAIM_AWAITING_CAR_HIRE_INFO then set error message and do not process the claim.
             */ else if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO)) {
                claimResult.setClaimParseStatus(ClaimParseStatus.hireMonitoring);
                if (isUpdateManagingRepair && managingRepair != null) {
                    claim.setManagingRepair(managingRepair);
                }
            } else {
                LOG.warn("Invalid rental status: '{}' - For ‘hire monitoring’ claims to be uploaded the claims must be in the ’AwaitingCarHireInfo’ status.", rentalStatus);
                claimResult.setClaimParseStatus(ClaimParseStatus.invalidClaimStatus);
                claimResult.setValid(false);
                claimResult.getMessage().add("For ‘hire monitoring’ claims to be uploaded the claims must be in the ’AwaitingCarHireInfo’ status.");
                claim.setChoReference(choReferenceNumber);

            }

        } else {

            LOG.warn("Invalid hire state rental status: '{}' - For ‘hire monitoring’ claims to be uploaded the claims must be exists in the system", rentalStatus);
            claimResult.setClaimParseStatus(ClaimParseStatus.invalidClaimStatus);
            claimResult.setValid(false);
            claimResult.getMessage().add("For ‘hire monitoring’ claims to be uploaded the claims must already exists in the system.");
            claim.setChoReference(choReferenceNumber);
        }

        claimResult.setClaim(claim);


    }

    private String getTPIidentificationStringForInsurer(String insurerAliasName) {
        Insurer insurer = null;
        InsurerAlias alias = null;
        InsurerAliasService insurerAlliasService = this.getBordereauReaderContext().getInsurerAliasService();

        if (insurerAliasName != null && insurerAliasName.length() > 0) {
            alias = insurerAlliasService.getInsurerByAliasName(insurerAliasName);
            insurer = alias.getInsurer();
            return insurer.getTpiIdentificationString().trim().replaceAll("\\s+", "");
        }

        return null;
    }

    private boolean checkTpiServiceActivatedForInsurerAndRentalStatus(String insurerAliasNames, String rentalStatus) {
        boolean returnValue = false;
        Insurer insurer = null;
        InsurerAlias alias = null;
        InsurerAliasService insurerAlliasService = this.getBordereauReaderContext().getInsurerAliasService();

        if (insurerAliasNames != null && insurerAliasNames.length() > 0) {
            alias = insurerAlliasService.getInsurerByAliasName(insurerAliasNames);
            if (alias == null) {
                return returnValue;
            }
            insurer = alias.getInsurer();
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
        Insurer insurer = null;
        InsurerAlias alias = null;
        InsurerAliasService insurerAlliasService = this.getBordereauReaderContext().getInsurerAliasService();

        if (insurerAliasNames != null && insurerAliasNames.length() > 0) {
            alias = insurerAlliasService.getInsurerByAliasName(insurerAliasNames);
            if (alias == null) {
                return returnValue;
            }
            insurer = alias.getInsurer();
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
        Insurer insurer = null;
        InsurerAliasService insurerAlliasService = this.getBordereauReaderContext().getInsurerAliasService();

        if (insurerAliasName != null && insurerAliasName.length() > 0) {
            InsurerAlias alias = insurerAlliasService.getInsurerByAliasName(insurerAliasName);
            if (alias == null) {
                LOG.error("No insurer found with alias name '{}'", insurerAliasName);
                return false;
            }
            insurer = alias.getInsurer();
            if (insurer == null) {
                return false;
            }
        } else {
            LOG.error("No insurer name provided.");
            return false;
        }

        return insurer.isAllowSubscriberClaims();
    }
}
