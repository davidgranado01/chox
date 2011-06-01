package idas.chox.service.xml.readers;

import idas.chox.core.model.Invoice;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import org.w3c.dom.*;

public class InvoiceRepairReader extends BaseEntityReader {

    protected static String sectionName = "Invoice Repair";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        Element root = XMLUtils.getElement(claimResult.getElement(), "invoice");
        Element element = XMLUtils.getElement(root, "repair");

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice) 
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.tpiIntervention)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.hireMoniteringAndNewInvoice)) {

            isAllowToReadData = true;

            claimResult = NodeHelper.nodeValidate(sectionName, "net", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "vat", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "gross", element, claimResult, getDataValidationParameter());

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
        Element element = XMLUtils.getElement(root, "repair");

        Invoice invoice = claimResult.getInvoice();

        invoice.setRepairGross(XmlHelper.getBigDecimalFromNode(element, "gross"));
        invoice.setRepairNet(XmlHelper.getBigDecimalFromNode(element, "net"));
        invoice.setRepairVat(XmlHelper.getBigDecimalFromNode(element, "vat"));
    }
}
