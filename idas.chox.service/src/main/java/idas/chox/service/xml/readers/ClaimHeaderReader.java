package idas.chox.service.xml.readers;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.InsurerAlias;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerAliasService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import org.w3c.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.Date;
import javax.xml.xpath.XPathExpressionException;

public class ClaimHeaderReader extends BaseEntityReader {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimHeaderReader.class);
    protected static String sectionName = "Claim Header";
    // PAGE PARAMETERS
    Boolean managingRepair;
    Date firstContactDate;
    Date creditAgreementDate;
    Date gtaNoticeDate;
    String choReferenceNumber;
    private String rentalStatus;
    boolean isUpdateManagingRepair = false;

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

        Element rootElement = claimResult.getElement();
        Element element = XMLUtils.getElement(rootElement, "supplier");

        claimResult.setCheckDataValid(true);

        NodeHelper.nodeValidate(sectionName, "first-contact", claimResult.getElement(), claimResult, getDataValidationParameter());
        NodeHelper.nodeValidate(sectionName, "managing-repair", claimResult.getElement(), claimResult, getDataValidationParameter());
        NodeHelper.nodeValidate(sectionName, "agreement-signed", claimResult.getElement(), claimResult, getDataValidationParameter());
        NodeHelper.nodeValidate(sectionName, "gta-notice", claimResult.getElement(), claimResult, getDataValidationParameter());
        NodeHelper.nodeValidate(sectionName, "rental-status", claimResult.getElement(), claimResult, getDataValidationParameter());
        NodeHelper.nodeValidate(sectionName, "supplier-name", element, claimResult, getDataValidationParameter());
        NodeHelper.nodeValidate(sectionName, "supplier-reference", element, claimResult, getDataValidationParameter());

        if (NodeHelper.nodeValidateBoolean(sectionName, "first-contact", claimResult.getElement(), claimResult, getDataValidationParameter())) {
            firstContactDate = XmlHelper.getDateFromNode(claimResult.getElement(), "first-contact");
        }

        if (NodeHelper.nodeValidateBoolean(sectionName, "rental-status", claimResult.getElement(), claimResult, getDataValidationParameter())) {
            rentalStatus = XmlHelper.getNodeValue(claimResult.getElement(), "rental-status");
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
            choReferenceNumber = XmlHelper.getNodeValue(element, "supplier-reference");
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

        SecurityInfoProvider securityInfoProvider = getBordereauRederContext().getSecurityInfoProvider();
        ClaimService claimService = getBordereauRederContext().getClaimService();
        BreBandService breBandService = getBordereauRederContext().getBreBandService();
        /*
         * getting insurer from xml to check TPI is Activated
         */
        Element rootElements = claimResult.getElement();
        Element claimElements = XMLUtils.getElement(rootElements, "claim");
        Element elements = XMLUtils.getElement(claimElements, "third-party");
        String insurerAliasNames = XmlHelper.getNodeValue(elements, "name");

        Claim claim = new Claim();

        if (securityInfoProvider.getCurrentUser().getChorganisation().isThirdPartyInterventionActivated()) {
            if(!checkTpiServiceActivatedForThisClaimInsurer(insurerAliasNames)){
                claimResult.setClaimParseStatus(ClaimParseStatus.tpiNotAcceptedByInsurer);
                claimResult.setValid(false);
                claimResult.getMessage().add("This claim Insurer is not accepting TPI invoice. Please contact chox admin.");
                LOG.debug("CHO TRYING TO UPLOADING TPI INVOICE BUT INSURER IS NOT ACTIVATED AS TPI ACCEPTING INSURER.");
                claim.setChoReference(choReferenceNumber);
            }
            else if (!checkTpiServiceActivatedForThisClaimInsurerForThisRentalStatus(insurerAliasNames, rentalStatus)) {
                claimResult.setClaimParseStatus(ClaimParseStatus.tpiNotRecognized);
                claimResult.setValid(false);
                LOG.debug("CHO TRYING TO UPLOADING TPI INVOICE WITH WRONG VALUE IN HIRE STATE FILED.");
                claimResult.getMessage().add("The value provided for the Ôhire stateÕ is incorrect, it must be Ô" + getTPIidentificationStringForInsurer(insurerAliasNames) + "Õ for third party intervention claims against this Insurer");
                claim.setChoReference(choReferenceNumber);
            } else if (claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber)) {
                claimResult.setClaimParseStatus(ClaimParseStatus.existInvoice);
                //claimResult.setClaim(claimService.getClaimByCHOReferenceNumber(choReferenceNumber));
                claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);
                claimResult.setValid(false);
            } else {
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
                claim.setTpiClaim(true);

            }
        } else {
            // First check that this is not a TPI claim: verify rental status is either 'InProgress' or 'Complete' (or blank)
            // see bug#819 - Reserva - Prevent Reserva Cases Being Uploaded As Normal CHOX Cases
            if (rentalStatus != null && rentalStatus.length() > 0
                    && !rentalStatus.toLowerCase().equals("inprogress") && !rentalStatus.toLowerCase().equals("complete")) {
                claimResult.setClaimParseStatus(ClaimParseStatus.invalidSchema);
                claimResult.setValid(false);
                claimResult.getMessage().add("The value provided for the Ôhire stateÕ is incorrect, it must be either ÔInProgressÕ or ÔCompleteÕ.");
                LOG.error("Invalid rental status: '{}' - may be trying to upload a TPI invoice and TPI not activated for this insurer.", rentalStatus);

            }
            else {
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
            }
        } 
        claimResult.setClaim(claim);
    }

    public String getTPIidentificationStringForInsurer(String insurerAliasName) {
        Insurer insurer = null;
        InsurerAlias allias = null;
        InsurerAliasService insurerAlliasService = this.getBordereauRederContext().getInsurerAliasService();

        if (insurerAliasName != null && insurerAliasName.length() > 0) {
            allias = insurerAlliasService.getInsurerByAliasName(insurerAliasName);
            insurer = allias.getInsurer();
            return insurer.getTpiIdentificationString();
        }

        return null;
    }

    public boolean checkTpiServiceActivatedForThisClaimInsurerForThisRentalStatus(String insurerAliasNames, String rentalStatus) {
        boolean returnValue = false;
        Insurer insurer = null;
        InsurerAlias allias = null;
        InsurerAliasService insurerAlliasService = this.getBordereauRederContext().getInsurerAliasService();

        if (insurerAliasNames != null && insurerAliasNames.length() > 0) {
            allias = insurerAlliasService.getInsurerByAliasName(insurerAliasNames);
            if (allias == null) {
                return returnValue;
            }
            insurer = allias.getInsurer();
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

    public boolean checkTpiServiceActivatedForThisClaimInsurer(String insurerAliasNames) {
        boolean returnValue = false;
        Insurer insurer = null;
        InsurerAlias allias = null;
        InsurerAliasService insurerAlliasService = this.getBordereauRederContext().getInsurerAliasService();

        if (insurerAliasNames != null && insurerAliasNames.length() > 0) {
            allias = insurerAlliasService.getInsurerByAliasName(insurerAliasNames);
            if (allias == null) {
                return returnValue;
            }
            insurer = allias.getInsurer();
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
}
