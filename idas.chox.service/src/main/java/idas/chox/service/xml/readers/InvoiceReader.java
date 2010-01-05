package idas.chox.service.xml.readers;

import idas.chox.core.model.Invoice;
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

        invoice.setTotalGross(XmlHelper.getBigDecimalFromNode(element, "gross"));
        invoice.setTotalNet(XmlHelper.getBigDecimalFromNode(element, "net"));
        invoice.setTotalVat(XmlHelper.getBigDecimalFromNode(element, "vat"));
        invoice.setTotalToPay(XmlHelper.getBigDecimalFromNode(element, "total-to-pay"));
        invoice.setOriginalTotalToPay(invoice.getTotalToPay());
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

        claimResult.getClaim().setInvoice(invoice);

    }
}
