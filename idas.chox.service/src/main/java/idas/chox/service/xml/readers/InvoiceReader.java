package idas.chox.service.xml.readers;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import java.math.BigDecimal;
import org.w3c.dom.*;

public class InvoiceReader extends BaseEntityReader {

    protected static String sectionName = "Invoice";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        Element element = XMLUtils.getElement(claimResult.getElement(), "invoice");

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)) {

            isAllowToReadData = true;

            claimResult.setCheckDataValid(true);
            claimResult = NodeHelper.nodeValidate(sectionName, "less-handling-fee", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "net", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "vat", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "gross", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "less-discount", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "total-to-pay", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "date-invoiced", element, claimResult, getDataValidationParameter());
            isAllowToReadData = claimResult.isCheckDataValid();

        }

        return isAllowToReadData;
    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {

        Element element = XMLUtils.getElement(claimResult.getElement(), "invoice");

        Invoice invoice = new Invoice();
        
        invoice.setCdwFee(BigDecimal.ZERO);
        invoice.setCdwQty(0);
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

        invoice.setTotalGross(XmlHelper.getBigDecimalFromNode(element, "gross"));
        invoice.setTotalNet(XmlHelper.getBigDecimalFromNode(element, "net"));
        invoice.setTotalVat(XmlHelper.getBigDecimalFromNode(element, "vat"));
        invoice.setFullTotalToPay(XmlHelper.getBigDecimalFromNode(element, "total-to-pay"));
        invoice.setOriginalFullTotalToPay(invoice.getFullTotalToPay());
        invoice.setOriginalTotalToPay(XmlHelper.getBigDecimalFromNode(element, "total-to-pay"));
        invoice.setDiscount(XmlHelper.getBigDecimalFromNode(element, "less-discount"));
        invoice.setDeductionForClaimsHandlingFee(XmlHelper.getBigDecimalFromNode(element, "less-handling-fee"));
        invoice.setDateInvoiced(XmlHelper.getDateFromNode(element, "date-invoiced"));
        invoice.setPenaltyAlertQty(0);
        invoice.setPenaltyCharge(BigDecimal.ZERO);

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

        claimResult.getClaim().setInvoice(invoice);
        claimResult.getClaim().updateLiabilityPayment();
        

    }

     
}
