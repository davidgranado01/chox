package idas.chox.service.xml.readers;

import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import org.w3c.dom.*;

public class InvoiceSupplierReader extends BaseEntityReader {

    protected static String sectionName = "Invoice Supplier";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        Element root = XMLUtils.getElement(claimResult.getElement(), "invoice");
        Element element = XMLUtils.getElement(root, "supplier");

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice) 
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.tpiIntervention)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.hireMoniteringAndNewInvoice)) {

            isAllowToReadData = true;

            claimResult = NodeHelper.nodeValidate(sectionName, "excess-collected", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "vat-collected", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "handling-invoice-no", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "handling-invoice-amount", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "claim-invoice-no", element, claimResult, getDataValidationParameter());

            if (!claimResult.isCheckDataValid()) {
                isAllowToReadData = false;
                claimResult.setValid(false);
                claimResult.setDataValid(false);
            }
        }

        return isAllowToReadData;
    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {
        Element root = XMLUtils.getElement(claimResult.getElement(), "invoice");
        Element element = XMLUtils.getElement(root, "supplier");

        claimResult.getInvoice().setHandlingInvoiceNo(XmlHelper.getNodeValue(element, "handling-invoice-no"));
        claimResult.getInvoice().setClaimsHandlingInvoiceAmount(XmlHelper.getBigDecimalFromNode(element, "handling-invoice-amount"));
        claimResult.getInvoice().setClaimInvoiceNo(XmlHelper.getNodeValue(element, "claim-invoice-no"));
        claimResult.getInvoice().setExcessAmountCollected(XmlHelper.getBigDecimalFromNode(element, "excess-collected"));
        claimResult.getInvoice().setVatAmountCollected(XmlHelper.getBigDecimalFromNode(element, "vat-collected"));
    }
}
