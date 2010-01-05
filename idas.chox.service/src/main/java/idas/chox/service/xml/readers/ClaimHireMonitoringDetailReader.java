package idas.chox.service.xml.readers;

import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import org.w3c.dom.*;

public class ClaimHireMonitoringDetailReader extends BaseEntityReader {

    protected static String sectionName = "Hire Monitoring Detail";

    @Override
    protected void process(ClaimResult claimResult) throws Exception {

        Element element = XMLUtils.getElement(claimResult.getElement(), "repair-details");

        HireMonitoringDetail hireMonitoringdtl = new HireMonitoringDetail();
        boolean isNotEmpty = false;

        if (claimResult.getClaim().getHireMonitoringDetail() != null) {
            hireMonitoringdtl = claimResult.getClaim().getHireMonitoringDetail();
        }

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "repairer"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setNameOfRepairer(XmlHelper.getNodeValue(element, "repairer"));
        }

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "inspection-booked-date"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setInspectionBookedDate(XmlHelper.getDateFromNode(element, "inspection-booked-date"));
        }

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "inspection-date"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setInspectionDate(XmlHelper.getDateFromNode(element, "inspection-date"));
        }

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "repair-book-in-date"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setRepairBookInDate(XmlHelper.getDateFromNode(element, "repair-book-in-date"));
            hireMonitoringdtl.setOriginalRepairBookInDate(XmlHelper.getDateFromNode(element, "repair-book-in-date"));
        }

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "repair-complete-date"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setRepairCompletionDate(XmlHelper.getDateFromNode(element, "repair-complete-date"));
        }

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "name-ime"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setNameOfIme(XmlHelper.getNodeValue(element, "name-ime"));
        }

        if (isNotEmpty) {
            claimResult.getClaim().setHireMonitoringDetail(hireMonitoringdtl);
        }
    }

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {
        boolean isAllowToReadData = false;

        Element element = XMLUtils.getElement(claimResult.getElement(), "repair-details");

        // AND ONLY FOR NEW CLAIM, EXISTING CLAIM, AND NEW INVOICE
        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim) ||
                claimResult.getClaimParseStatus().equals(ClaimParseStatus.existClaim) ||
                claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)) {

            isAllowToReadData = true;
            claimResult.setCheckDataValid(true);

            claimResult = NodeHelper.nodeValidate(sectionName, "repairer", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "inspection-booked-date", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "inspection-date", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "repair-book-in-date", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "repair-complete-date", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "name-ime", element, claimResult, getDataValidationParameter());

            if (!claimResult.isCheckDataValid()) {
                isAllowToReadData = false;
                claimResult.setValid(false);
                claimResult.setDataValid(false);
            }
        }

        return isAllowToReadData;
    }
}
