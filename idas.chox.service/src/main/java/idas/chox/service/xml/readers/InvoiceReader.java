package idas.chox.service.xml.readers;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import idas.chox.core.model.Invoice;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.util.XmlHelper;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;

public class InvoiceReader extends BaseEntityReader {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceReader.class);
    protected static String sectionName = "Invoice";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        Element element = XMLUtils.getElement(claimResult.getElement(), "invoice");

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_HIRE_MONITORING_AND_NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_NEW_SUPPLEMENTARY_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_VS_INSURER_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUPPLEMENTARY_INVOICE)) {


            claimResult.setCheckDataValid(true);
            NodeHelper.nodeValidate(sectionName, "less-handling-fee", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "net", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "vat", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "gross", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "less-discount", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "total-to-pay", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "date-invoiced", element, claimResult, getDataValidationParameter());
            isAllowToReadData = claimResult.isCheckDataValid();

        }

        LOG.debug("Claim '{}' isDataValid: {}", claimResult.getClaim().getChoReference(), claimResult.isDataValid());
        LOG.debug("Claim '{}' isValid: {}", claimResult.getClaim().getChoReference(), claimResult.isValid());
        LOG.debug("Returning isAllowToReadData={} for claim '{}'", isAllowToReadData, claimResult.getClaim().getChoReference());
        return isAllowToReadData;
    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {

        Element element = XMLUtils.getElement(claimResult.getElement(), "invoice");


        Invoice invoice = new Invoice();
        LOG.debug("New invoice created for claim '{}'.", claimResult.getClaim().getChoReference());
        invoice.setMiscellaneousFee(BigDecimal.ZERO);
        invoice.setMiscellaneousQty(0);
        invoice.setAdminFee(BigDecimal.ZERO);
        invoice.setAdminQty(0);
        invoice.setAdditionalDriverFee(BigDecimal.ZERO);
        invoice.setAdditionalDriverQty(0);
        invoice.setAutomaticFee(BigDecimal.ZERO);
        invoice.setAutomaticQty(0);
        invoice.setBabySeatFee(BigDecimal.ZERO);
        invoice.setBabySeatQty(0);
        invoice.setDeliveryCollectionFee(BigDecimal.ZERO);
        invoice.setDeliveryCollectionQty(0);
        invoice.setDualControlFee(BigDecimal.ZERO);
        invoice.setDualControlQty(0);
        invoice.setEstateFee(BigDecimal.ZERO);
        invoice.setEstateQty(0);
        invoice.setNonStandardInsurancePremiumFee(BigDecimal.ZERO);
        invoice.setNonStandardInsurancePremiumQty(0);
        invoice.setRoofRackFee(BigDecimal.ZERO);
        invoice.setRoofRackQty(0);
        invoice.setSatNavFee(BigDecimal.ZERO);
        invoice.setSatNavQty(0);
        invoice.setTowBarsFee(BigDecimal.ZERO);
        invoice.setTowBarsQty(0);
        invoice.setRepairAdminFee(BigDecimal.ZERO);
        invoice.setRepairAcquisitionFee(BigDecimal.ZERO);

        invoice.setTotalGross(XmlHelper.getBigDecimalFromNode(element, "gross"));
        invoice.setTotalNet(XmlHelper.getBigDecimalFromNode(element, "net"));
        invoice.setTotalVat(XmlHelper.getBigDecimalFromNode(element, "vat"));
        invoice.setFullTotalToPay(XmlHelper.getBigDecimalFromNode(element, "total-to-pay"));
        invoice.setDiscount(XmlHelper.getBigDecimalFromNode(element, "less-discount"));
        invoice.setDeductionForClaimsHandlingFee(XmlHelper.getBigDecimalFromNode(element, "less-handling-fee"));
        invoice.setDateInvoiced(XmlHelper.getDateFromNode(element, "date-invoiced"));
        invoice.setPenaltyBand(getBordereauReaderContext().getPenaltyChargeService().getFirstPenaltyBand(claimResult.getClaim()));
        invoice.setHirePenaltyCharge(BigDecimal.ZERO);
        invoice.setRepairPenaltyCharge(BigDecimal.ZERO);
        invoice.setTotalPenaltyCharge(BigDecimal.ZERO);
        invoice.setInsurerDiscount(BigDecimal.ZERO);
        invoice.setTotalGrossInsurerDiscount(BigDecimal.ZERO);
        invoice.setHireGrossInsurerDiscount(BigDecimal.ZERO);
        invoice.setRepairGrossInsurerDiscount(BigDecimal.ZERO);

        // PRE-DEFINED
        invoice.setHireGross(BigDecimal.ZERO);
        invoice.setHireNet(BigDecimal.ZERO);
        invoice.setHireVat(BigDecimal.ZERO);
        invoice.setHireRateChargedPerDay(BigDecimal.ZERO);
        invoice.setHandlingInvoiceNo("");
        invoice.setClaimsHandlingInvoiceAmount(BigDecimal.ZERO);
        invoice.setClaimInvoiceNo("");
        invoice.setExcessAmountCollected(BigDecimal.ZERO);
        invoice.setVatAmountCollected(BigDecimal.ZERO);
        invoice.setRepairGross(BigDecimal.ZERO);
        invoice.setRepairNet(BigDecimal.ZERO);
        invoice.setRepairVat(BigDecimal.ZERO);
        invoice.setStorageRecoveryGross(BigDecimal.ZERO);
        invoice.setStorageRecoveryNet(BigDecimal.ZERO);
        invoice.setStorageRecoveryVat(BigDecimal.ZERO);
        invoice.setEngineerFeeGross(BigDecimal.ZERO);
        invoice.setEngineerFeeNet(BigDecimal.ZERO);
        invoice.setEngineerFeeVat(BigDecimal.ZERO);
        invoice.setTotalLossFeeGross(BigDecimal.ZERO);
        invoice.setTotalLossFeeNet(BigDecimal.ZERO);
        invoice.setTotalLossFeeVat(BigDecimal.ZERO);
        invoice.setAutoPenaltyStart(new Date());
        claimResult.getClaim().setInvoice(invoice);
        getBordereauReaderContext().getClaimService().updateLiabilityPayment(claimResult.getClaim());
        // Now remove from invoice. This is necessary as some of the invoice sub-sections may not be valid.
        // We'll therefore store the invoice in the claimResult for now and add it back into the claim
        // once all subsections have been validated (and before the activity processing)
        claimResult.getClaim().setInvoice(null);
        claimResult.setInvoice(invoice);
    }
}
