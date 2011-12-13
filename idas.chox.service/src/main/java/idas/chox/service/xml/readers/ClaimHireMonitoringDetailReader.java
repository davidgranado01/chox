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

        if (XmlHelper.isNotNullDate(XmlHelper.getNodeValue(element, "inspection-booked-date"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setInspectionBookedDate(XmlHelper.getDateFromNode(element, "inspection-booked-date"));
        }

        if (XmlHelper.isNotNullDate(XmlHelper.getNodeValue(element, "inspection-date"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setInspectionDate(XmlHelper.getDateFromNode(element, "inspection-date"));
        }

        if (XmlHelper.isNotNullDate(XmlHelper.getNodeValue(element, "repair-book-in-date"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setRepairBookInDate(XmlHelper.getDateFromNode(element, "repair-book-in-date"));
            hireMonitoringdtl.setOriginalRepairBookInDate(XmlHelper.getDateFromNode(element, "repair-book-in-date"));
        }

        if (XmlHelper.isNotNullDate(XmlHelper.getNodeValue(element, "repair-authorised-date"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setRepairAuthorisedDate(XmlHelper.getDateFromNode(element, "repair-authorised-date"));
        }

        if (XmlHelper.isNotNullDate(XmlHelper.getNodeValue(element, "repair-started-date"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setRepairCommencedDate(XmlHelper.getDateFromNode(element, "repair-started-date"));
        }

        if (XmlHelper.isNotNullDate(XmlHelper.getNodeValue(element, "repair-complete-date"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setRepairCompletionDate(XmlHelper.getDateFromNode(element, "repair-complete-date"));
        }

        if (XmlHelper.isNotNullDate(XmlHelper.getNodeValue(element, "tl-offer-made-date"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setTotalLossOfferMadeDate(XmlHelper.getDateFromNode(element, "tl-offer-made-date"));
        }

        if (XmlHelper.isNotNullDate(XmlHelper.getNodeValue(element, "tl-offer-accepted-date"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setTotalLossOfferAcceptedDate(XmlHelper.getDateFromNode(element, "tl-offer-accepted-date"));
        }

        if (XmlHelper.isNotNullDate(XmlHelper.getNodeValue(element, "tl-cheque-issued"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setTotalLossOfferCheckIssuedDate(XmlHelper.getDateFromNode(element, "tl-cheque-issued"));
        }

        if (XmlHelper.isNotNullDate(XmlHelper.getNodeValue(element, "tl-cheque-received"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setTotalLossOfferCheckReceivedDate(XmlHelper.getDateFromNode(element, "tl-cheque-received"));
        }

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "labour-rate"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setLabourRate(XmlHelper.getBigDecimalFromNode(element, "labour-rate"));
        }

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "labour-hours"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setLabourHour(XmlHelper.getBigDecimalFromNode(element, "labour-hours"));
        }

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "labour-cost"))) {
            isNotEmpty = true;
            hireMonitoringdtl.setLabourCost(XmlHelper.getBigDecimalFromNode(element, "labour-cost"));
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
        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.newSubscriberClaim)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.existClaim)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.existSubscriberClaim)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.insurerVsInsurerInvoice)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.insurerUpload)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.tpiIntervention)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.hireMonitoringAndNewInvoice)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.newSupplementaryInvoice)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.hireMonitoring)) {

            isAllowToReadData = true;
            claimResult.setCheckDataValid(true);

            claimResult = NodeHelper.nodeValidate(sectionName, "repairer", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "inspection-booked-date", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "inspection-date", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "repair-book-in-date", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "repair-authorised-date", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "repair-started-date", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "repair-complete-date", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "name-ime", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "labour-rate", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "labour-hours", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "labour-cost", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "tl-offer-made-date", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "tl-offer-accepted-date", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "tl-cheque-received", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "tl-cheque-issued", element, claimResult, getDataValidationParameter());



            if (!claimResult.isCheckDataValid()) {
                isAllowToReadData = false;
                claimResult.setValid(false);
                claimResult.setDataValid(false);
            }
        }

        return isAllowToReadData;
    }
}
