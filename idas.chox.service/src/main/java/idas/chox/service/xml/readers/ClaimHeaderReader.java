package idas.chox.service.xml.readers;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import org.w3c.dom.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class ClaimHeaderReader extends BaseEntityReader {

    protected static String sectionName = "Claim Header";
    // PAGE PARAMETERS
    Boolean managingRepair;
    Date firstContactDate;
    Date creditAgreementDate;
    Date gtaNoticeDate;
    String choReferenceNumber;
    boolean isUpdateManagingRepair = false;

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

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

        if (NodeHelper.nodeValidateBoolean(sectionName, "first-contact", claimResult.getElement(), getDataValidationParameter())) {
            firstContactDate = XmlHelper.getDateFromNode(claimResult.getElement(), "first-contact");
        }

        if (NodeHelper.nodeValidateBoolean(sectionName, "managing-repair", claimResult.getElement(), getDataValidationParameter())) {
            managingRepair = XmlHelper.getBooleanFromNode(claimResult.getElement(), "managing-repair");

            if (!XMLUtils.getElementValue(claimResult.getElement(), "managing-repair").equalsIgnoreCase("") && XMLUtils.getElementValue(claimResult.getElement(), "managing-repair") != null) {
                isUpdateManagingRepair = true;
            }

        }

        if (NodeHelper.nodeValidateBoolean(sectionName, "agreement-signed", claimResult.getElement(), getDataValidationParameter())) {
            creditAgreementDate = XmlHelper.getDateFromNode(claimResult.getElement(), "agreement-signed");
        }

        if (NodeHelper.nodeValidateBoolean(sectionName, "supplier-reference", claimResult.getElement(), getDataValidationParameter())) {
            choReferenceNumber = XmlHelper.getNodeValue(element, "supplier-reference");
        }

        if (NodeHelper.nodeValidateBoolean(sectionName, "gta-notice", claimResult.getElement(), getDataValidationParameter())) {
            gtaNoticeDate = XmlHelper.getDateFromNode(claimResult.getElement(), "gta-notice");
        }

        if (gtaNoticeDate == null) {
            gtaNoticeDate = DateHelper.getCurrentDateTime();
        }

        return claimResult.isValid();
    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {

        Claim claim = new Claim();
        ClaimService claimService = getBordereauRederContext().getClaimService();
        ChorganisationService chorganisationService = getBordereauRederContext().getChorganisationService();
        BreBandService breBandService = getBordereauRederContext().getBreBandService();
        if (claimService.isClaimSupplierReferenceNumberExist(choReferenceNumber)) {

            claim = claimService.getClaimByCHOReferenceNumber(choReferenceNumber);

            if (claim.getInvoice() != null) {

                claimResult.setClaimParseStatus(ClaimParseStatus.existInvoice);
                claimResult.setValid(false);

            } else {

                if (claim.getStatus().equalsIgnoreCase(ClaimStatus.AWAITING_INVOICE_DATA)) {

                    claimResult.setClaimParseStatus(ClaimParseStatus.newInvoice);
                    BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                    claim.setBreBand(choBand);

                    if (isUpdateManagingRepair && managingRepair != null) {
                        claim.setManagingRepair(managingRepair);
                    }

                } else if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED) ||
                        claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_PENDING) ||
                        claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_REJECTION_ACCEPTED)) {

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
            claim.setChorganisation(chorganisationService.getCurrentCHOrganisation());
        }

        claimResult.setClaim(claim);
    }
}
