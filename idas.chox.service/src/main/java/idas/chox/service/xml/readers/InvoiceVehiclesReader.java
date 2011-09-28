package idas.chox.service.xml.readers;

import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import org.w3c.dom.*;

public class InvoiceVehiclesReader extends BaseEntityReader {

    protected static String sectionName = "Invoice Vehicle";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        Element root = XMLUtils.getElement(claimResult.getElement(), "invoice");
        Element element = XMLUtils.getElement(root, "vehicles");

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice) 
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.tpiIntervention)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.hireMonitoringAndNewInvoice)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.newSupplementaryInvoice)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.insurerVsInsurerInvoice)) {

            isAllowToReadData = true;

            claimResult = NodeHelper.nodeValidate(sectionName, "day-rate", element, claimResult, getDataValidationParameter());
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
        Element element = XMLUtils.getElement(root, "vehicles");

        claimResult.getInvoice().setHireGross(XmlHelper.getBigDecimalFromNode(element, "gross"));
        claimResult.getInvoice().setHireNet(XmlHelper.getBigDecimalFromNode(element, "net"));
        claimResult.getInvoice().setHireVat(XmlHelper.getBigDecimalFromNode(element, "vat"));
        claimResult.getInvoice().setHireRateChargedPerDay(XmlHelper.getBigDecimalFromNode(element, "day-rate"));
    }
}
