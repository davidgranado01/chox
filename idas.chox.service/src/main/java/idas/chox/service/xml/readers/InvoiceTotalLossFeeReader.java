package idas.chox.service.xml.readers;

import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import org.w3c.dom.*;

/**
 *
 * @author John
 */
public class InvoiceTotalLossFeeReader extends BaseEntityReader {
    protected static String sectionName = "Invoice Total Loss Fee";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        boolean isAllowToReadData = false;

        Element rootElement = claimResult.getElement();
        Element invoiceElement = XMLUtils.getElement(rootElement, "invoice");
        Element element = XMLUtils.getElement(invoiceElement, "total-loss");

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice) 
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.tpiIntervention)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.hireMonitoringAndNewInvoice)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.newSupplementaryInvoice)) {

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
        Element element = XMLUtils.getElement(invoiceElement, "total-loss");

        claimResult.getInvoice().setTotalLossFeeGross(XmlHelper.getBigDecimalFromNode(element, "gross"));
        claimResult.getInvoice().setTotalLossFeeNet(XmlHelper.getBigDecimalFromNode(element, "net"));
        claimResult.getInvoice().setTotalLossFeeVat(XmlHelper.getBigDecimalFromNode(element, "vat"));
    }

}
