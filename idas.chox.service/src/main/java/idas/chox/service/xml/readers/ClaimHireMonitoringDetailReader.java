package idas.chox.service.xml.readers;

import org.w3c.dom.Element;

import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.util.XmlHelper;
import idas.chox.service.xml.util.NodeHelper;
import java.util.Date;

public class ClaimHireMonitoringDetailReader extends BaseEntityReader {

    protected static String sectionName = "Hire Monitoring Detail";

    @Override
    protected void process(ClaimResult claimResult) throws Exception {

        Element element = XMLUtils.getElement(claimResult.getElement(), "repair-details");

        HireMonitoringDetail hireMonitoringdtl;
        boolean isNotEmpty = false;
        boolean isNew = false;
        if (claimResult.getClaim().getHireMonitoringDetail() != null) {
            hireMonitoringdtl = claimResult.getClaim().getHireMonitoringDetail();
        } else {
            hireMonitoringdtl = new HireMonitoringDetail();
            isNew = true;
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
            Date oldRepairBookInDate = hireMonitoringdtl.getRepairBookInDate();
            hireMonitoringdtl.setRepairBookInDate(XmlHelper.getDateFromNode(element, "repair-book-in-date"));
            // Check if changed and and flag for anomaly checking
            if (!isNew && hireMonitoringdtl.getRepairBookInDate() != null && (oldRepairBookInDate == null
                    || oldRepairBookInDate.compareTo(hireMonitoringdtl.getRepairBookInDate()) != 0)) {
                claimResult.setCheckForRepairAnomalies(true);
            }

            // If this is the first time the repair book-in date has been set, then save this 'original' value.
            if (hireMonitoringdtl.getOriginalRepairBookInDate() == null) {
                hireMonitoringdtl.setOriginalRepairBookInDate(XmlHelper.getDateFromNode(element, "repair-book-in-date"));
            }
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
            if (claimResult.getClaim().getCustomer() != null && claimResult.getClaim().getCustomer().getIsTotalLoss() != null) {
                hireMonitoringdtl.setIsTotalLostCheck(claimResult.getClaim().getCustomer().getIsTotalLoss());
            }
            claimResult.getClaim().setHireMonitoringDetail(hireMonitoringdtl);
        }
    }

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {
        boolean isAllowToReadData = false;

        Element element = XMLUtils.getElement(claimResult.getElement(), "repair-details");

        // AND ONLY FOR NEW CLAIM, EXISTING CLAIM, AND NEW INVOICE
        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUBSCRIBER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_FIXEDFEE_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXISTS_INSURER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_HIRE_MONITORING)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_HIRE_MONITORING_AND_NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_SUBSCRIBER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_FIXEDFEE_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_VS_INSURER_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUPPLEMENTARY_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING)) {

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
