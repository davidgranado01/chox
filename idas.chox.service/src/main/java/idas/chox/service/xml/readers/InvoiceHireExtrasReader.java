package idas.chox.service.xml.readers;

import idas.chox.core.model.ClaimType;
import idas.chox.core.model.ClaimType;
import java.math.BigDecimal;
import java.util.List;

import org.w3c.dom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Invoice;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;


public class InvoiceHireExtrasReader extends BaseEntityReader {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceHireExtrasReader.class);

    private static String sectionName = "Invoice Extra";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {
        Element rootElement = claimResult.getElement();
        Element invoiceElement = XMLUtils.getElement(rootElement, "invoice");
        Element element = XMLUtils.getElement(invoiceElement, "extras");

        List<Element> elements = null;
        
        if (element != null) {
            elements = XMLUtils.getElements(element.getOwnerDocument(), element, "extra");
        }

        boolean isAllowToReadData = false;

        if (((claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_INVOICE)) 
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_HIRE_MONITORING_AND_NEW_INVOICE) 
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION) 
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUPPLEMENTARY_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_NEW_SUPPLEMENTARY_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_VS_INSURER_INVOICE))
                && element != null && XMLUtils.getElement(element, "extra") != null
                && ((XMLUtils.getElement(element, "extra").getTextContent()).trim().length() > 0)) {

            isAllowToReadData = true;

            claimResult = NodeHelper.nodeValidateDefaultDescription(sectionName, "cover-note-required", element, claimResult, getDataValidationParameter(), "cover-note-required");
            for (Element ee : elements) {
                String strExtraName = XmlHelper.getNodeValue(ee, "name");
                String strExtraFee = new StringBuilder().append(strExtraName).append(" Fee").toString();
                String strExtraQty = new StringBuilder().append(strExtraName).append(" Quantity").toString();
                
                NodeHelper.nodeValidateDefaultDescription(sectionName, "name", ee, claimResult, getDataValidationParameter(), strExtraName);
                NodeHelper.nodeValidateDefaultDescription(sectionName, "quantity", ee, claimResult, getDataValidationParameter(), strExtraQty);
                NodeHelper.nodeValidateDefaultDescription(sectionName, "item-cost", ee, claimResult, getDataValidationParameter(), strExtraFee);
                
                /*
                 * Hard-coded check on Acquisition Fee and Overhead & Margin Fee charges for non-collaboration protocol claims
                 */
                BigDecimal dIntemCost = XmlHelper.getBigDecimalFromNode(ee, "item-cost");
                if (!ClaimType.isCollaborationProtocol(claimResult.getClaim().getClaimType())
                        && (strExtraName.equals("Acquisition") || strExtraName.equals("Overhead and Margin"))
                        && BigDecimal.ZERO.compareTo(dIntemCost) != 0) {
                    claimResult.setCheckDataValid(false);
                    claimResult.getMessage().add(String.format("An '%s' fee is being charged. This charge is only accepted on Collaboration Protocol claims. Please remove and re-submit without this charge.", strExtraName, sectionName));
                }
            }

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
        Element extrasElement = XMLUtils.getElement(invoiceElement, "extras");
        List<Element> elements = XMLUtils.getElements(extrasElement.getOwnerDocument(), extrasElement, "extra");

        invoice.setCoverNoteRequired(XmlHelper.getBooleanFromNode(extrasElement, "cover-note-required"));

        for (Element ee : elements) {
            String sExtra = XmlHelper.getNodeValue(ee, "name");
            Integer iQuantity = XmlHelper.getIntegerFromNode(ee, "quantity");
            BigDecimal dIntemCost = XmlHelper.getBigDecimalFromNode(ee, "item-cost");
            setExtraItem(invoice, sExtra, iQuantity, dIntemCost);
        }
    }

    private void setExtraItem(Invoice invoice, String nodeName, Integer iQuantity, BigDecimal dIntemCost) {
        if (nodeName.equalsIgnoreCase("Miscellaneous") || nodeName.equalsIgnoreCase("CDW")) {
            invoice.setMiscellaneousFee(dIntemCost);
            invoice.setMiscellaneousQty(iQuantity);
        } else if (nodeName.equalsIgnoreCase("Admin")) {
            invoice.setAdminFee(dIntemCost);
            invoice.setAdminQty(iQuantity);
        } else if (nodeName.equalsIgnoreCase("Automatic")) {
            invoice.setAutomaticFee(dIntemCost);
            invoice.setAutomaticQty(iQuantity);
        } else if (nodeName.equalsIgnoreCase("Additional Driver")) {
            invoice.setAdditionalDriverFee(dIntemCost);
            invoice.setAdditionalDriverQty(iQuantity);
        } else if (nodeName.equalsIgnoreCase("Baby Seat")) {
            invoice.setBabySeatFee(dIntemCost);
            invoice.setBabySeatQty(iQuantity);
        } else if (nodeName.equalsIgnoreCase("Delivery Collection")) {
            invoice.setDeliveryCollectionFee(dIntemCost);
            invoice.setDeliveryCollectionQty(iQuantity);
        } else if (nodeName.equalsIgnoreCase("Dual Control")) {
            invoice.setDualControlFee(dIntemCost);
            invoice.setDualControlQty(iQuantity);
        } else if (nodeName.equalsIgnoreCase("Estate")) {
            invoice.setEstateFee(dIntemCost);
            invoice.setEstateQty(iQuantity);
        } else if (nodeName.equalsIgnoreCase("Non-standard Risk Insurance Premium")) {
            invoice.setNonStandardInsurancePremiumFee(dIntemCost);
            invoice.setNonStandardInsurancePremiumQty(iQuantity);
        } else if (nodeName.equalsIgnoreCase("Roof Rack")) {
            invoice.setRoofRackFee(dIntemCost);
            invoice.setRoofRackQty(iQuantity);
        } else if (nodeName.equalsIgnoreCase("Sat Nav")) {
            invoice.setSatNavFee(dIntemCost);
            invoice.setSatNavQty(iQuantity);
        } else if (nodeName.equalsIgnoreCase("Tow Bars")) {
            invoice.setTowBarsFee(dIntemCost);
            invoice.setTowBarsQty(iQuantity);
        } else if (nodeName.equalsIgnoreCase("Acquisition")) {
            invoice.setAcquisitionFee(dIntemCost);
            invoice.setAcquisitionQty(iQuantity);
        } else if (nodeName.equalsIgnoreCase("Overhead and Margin")) {
            invoice.setOverheadFee(dIntemCost);
            invoice.setOverheadQty(iQuantity);
        }
    }

}