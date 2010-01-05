package idas.chox.service.xml.readers;

import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import org.w3c.dom.*;

public class InvoiceEngineeringFeeReader extends BaseEntityReader {

    protected static String sectionName = "Invoice Engineering Fee";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        boolean isAllowToReadData = false;

        Element rootElement = claimResult.getElement();
        Element invoiceElement = XMLUtils.getElement(rootElement, "invoice");
        Element element = XMLUtils.getElement(invoiceElement, "engineer-fee");

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)) {

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
        Element rootElement = claimResult.getElement();
        Element invoiceElement = XMLUtils.getElement(rootElement, "invoice");
        Element element = XMLUtils.getElement(invoiceElement, "engineer-fee");

        claimResult.getClaim().getInvoice().setEngineerFeeGross(XmlHelper.getBigDecimalFromNode(element, "gross"));
        claimResult.getClaim().getInvoice().setEngineerFeeNet(XmlHelper.getBigDecimalFromNode(element, "net"));
        claimResult.getClaim().getInvoice().setEngineerFeeVat(XmlHelper.getBigDecimalFromNode(element, "vat"));
    }
}
