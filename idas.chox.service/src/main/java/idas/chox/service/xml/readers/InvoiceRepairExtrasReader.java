package idas.chox.service.xml.readers;

import java.math.BigDecimal;
import java.util.List;
import org.w3c.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Invoice;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;


public class InvoiceRepairExtrasReader extends BaseEntityReader {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceRepairExtrasReader.class);

    protected static String sectionName = "Invoice Extra";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {
        Element rootElement = claimResult.getElement();
        Element invoiceElement = XMLUtils.getElement(rootElement, "invoice");
        Element element = XMLUtils.getElement(invoiceElement, "repairExtras");
        List<Element> elements = null;
        
        if (element != null) {
            elements = XMLUtils.getElements(element.getOwnerDocument(), element, "extra");
        }

        boolean isAllowToReadData = false;

        if (((claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_INVOICE)) 
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION) 
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_UPLOAD)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUPPLEMENTARY_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_VS_INSURER_INVOICE))
                && element != null && XMLUtils.getElement(element, "extra") != null
                && ((XMLUtils.getElement(element, "extra").getTextContent()).trim().length() > 0)) {

            isAllowToReadData = true;

            LOG.debug("************Parsing extra elements**************");
            for (Element ee : elements) {
                String strExtraName = XmlHelper.getNodeValue(ee, "name");
                String strExtraFee = strExtraName + " Fee";
                LOG.debug("...parsing '{}' element", strExtraName);
                claimResult = NodeHelper.nodeValidateDefaultDescription(sectionName, "name", ee, claimResult, getDataValidationParameter(), strExtraName);
                claimResult = NodeHelper.nodeValidateDefaultDescription(sectionName, "item-cost", ee, claimResult, getDataValidationParameter(), strExtraFee);
                LOG.debug("......claimResult.isCheckDataValid = {}, isValid = {}", claimResult.isCheckDataValid(), claimResult.isValid());
                
            }

            LOG.debug("Returning claimResult.isCheckDataValid = {}, isValid = {}", claimResult.isCheckDataValid(), claimResult.isValid());
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

        // preInitialize(claimResult);
        Invoice invoice = claimResult.getInvoice();

        Element rootElement = claimResult.getElement();
        Element invoiceElement = XMLUtils.getElement(rootElement, "invoice");
        Element extrasElement = XMLUtils.getElement(invoiceElement, "repairExtras");
        if (extrasElement != null) {
            
            List<Element> elements = XMLUtils.getElements(extrasElement.getOwnerDocument(), extrasElement, "extra");

            for (Element ee : elements) {
                String sExtra = XmlHelper.getNodeValue(ee, "name");
                BigDecimal dIntemCost = XmlHelper.getBigDecimalFromNode(ee, "item-cost");
                setExtraItem(invoice, sExtra, dIntemCost);
            }
        }

    }

    private void setExtraItem(Invoice invoice, String nodeName, BigDecimal dIntemCost) {
        if (nodeName.equalsIgnoreCase("Repair Admin")) {
            invoice.setRepairAdminFee(dIntemCost);
        } else if (nodeName.equalsIgnoreCase("Repair Acquisition")) {
            invoice.setRepairAcquisitionFee(dIntemCost);
        }
    }
}