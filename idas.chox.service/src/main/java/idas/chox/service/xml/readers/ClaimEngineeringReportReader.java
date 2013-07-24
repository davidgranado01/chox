package idas.chox.service.xml.readers;

import org.w3c.dom.Element;

import idas.chox.core.model.EngineerReport;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;

public class ClaimEngineeringReportReader extends BaseEntityReader {

    protected static String sectionName = "Engineering Report";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        boolean isAllowToReadData = false;

        Element rootElement = claimResult.getElement();
        Element element = XMLUtils.getElement(rootElement, "engineer-report");

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
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_COLLABORATION_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_COLLABORATION_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_VS_INSURER_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING)) {

            claimResult.setCheckDataValid(true);

            NodeHelper.nodeValidate(sectionName, "labour-amount", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "total-amount", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "days", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "usable", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "name", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "company", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "address1", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "address2", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "address3", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "address4", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "address5", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "postcode", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "telephone", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "email", element, claimResult, getDataValidationParameter());

            isAllowToReadData = claimResult.isCheckDataValid();
        }

        return isAllowToReadData;
    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {

        Element rootElement = claimResult.getElement();
        Element element = XMLUtils.getElement(rootElement, "engineer-report");

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "labour-amount")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "total-amount")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "days")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "name")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "company")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "address1")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "address2")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "address3")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "address4")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "address5")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "postcode")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "telephone")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "email")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "usable"))) {

            EngineerReport engineerReport;

            if (claimResult.getClaim().getEngineerReport() != null) {
                engineerReport = claimResult.getClaim().getEngineerReport();
            } else {
                engineerReport = new EngineerReport();
            }

            engineerReport.setDays(XmlHelper.getIntegerFromNode(element, "days"));
            engineerReport.setLabourAmount(XmlHelper.getBigDecimalFromNode(element, "labour-amount"));
            engineerReport.setTotalAmount(XmlHelper.getBigDecimalFromNode(element, "total-amount"));
            engineerReport.setName(XmlHelper.getNodeValue(element, "name"));
            engineerReport.setCompany(XmlHelper.getNodeValue(element, "company"));
            engineerReport.setAddress1(XmlHelper.getNodeValue(element, "address1"));
            engineerReport.setAddress2(XmlHelper.getNodeValue(element, "address2"));
            engineerReport.setAddress3(XmlHelper.getNodeValue(element, "address3"));
            engineerReport.setAddress4(XmlHelper.getNodeValue(element, "address4"));
            engineerReport.setAddress5(XmlHelper.getNodeValue(element, "address5"));
            engineerReport.setPostcode(XmlHelper.getNodeValue(element, "postcode"));
            engineerReport.setTelephone(XmlHelper.getNodeValue(element, "telephone"));
            engineerReport.setEmail(XmlHelper.getEmailAddressFromNode(element, "email"));
            engineerReport.setIsUsable(XmlHelper.getBooleanFromNode(element, "usable"));

            claimResult.getClaim().setEngineerReport(engineerReport);
        }

    }
}
