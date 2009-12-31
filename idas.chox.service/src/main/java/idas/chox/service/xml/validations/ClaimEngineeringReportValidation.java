package idas.chox.service.xml.validations;

import idas.chox.core.model.EngineerReport;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;

public class ClaimEngineeringReportValidation implements rulesInterface {

    protected static String sectionName = "Engineering Report";
    private DataValidationParameter dataValidationParameter;
    private ClaimResult claimResult;
    private Element element;

    public void setClaimResult(ClaimResult claimResult) {
        this.claimResult = claimResult;
    }

    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) {
        this.dataValidationParameter = dataValidationParameter;
    }

    public ClaimEngineeringReportValidation(
            ClaimResult claimResult,
            DataValidationParameter dataValidationParameter) {

        setClaimResult(claimResult);
        setDataValidationParameter(dataValidationParameter);
    }

    public ClaimResult execute() throws DOMException, XPathExpressionException, Exception {

        this.element = XMLUtils.getElement(claimResult.getElement(), "engineer-report");

        if (validate()) {
            process();
        }

        doPrintResult(false);
        return claimResult;
    }

    private boolean validate() throws DOMException, XPathExpressionException, Exception {

        boolean isAllowToReadData = false;

        // AND ONLY FOR NEW CLAIM, EXISTING CLAIM, AND NEW INVOICE
        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim) ||
                claimResult.getClaimParseStatus().equals(ClaimParseStatus.existClaim) ||
                claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)) {

            isAllowToReadData = true;
            this.claimResult.setCheckDataValid(true);

            this.claimResult = NodeHelper.nodeValidate(sectionName, "labour-amount", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "total-amount", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "days", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "usable", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "name", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "company", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address1", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address2", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address3", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address4", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address5", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "postcode", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "telephone", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "email", this.element, claimResult, dataValidationParameter);

            isAllowToReadData = this.claimResult.isCheckDataValid();
        }

        return isAllowToReadData;
    }

    private void process() {

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "labour-amount")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "total-amount")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "days")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "name")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "company")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address1")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address2")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address3")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address4")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address5")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "postcode")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "telephone")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "email")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "usable"))) {

            EngineerReport engineerReport = new EngineerReport();

            if (this.claimResult.getClaim().getEngineerReport() != null) {
                engineerReport = this.claimResult.getClaim().getEngineerReport();
            }

            engineerReport.setDays(XmlHelper.getIntegerFromNode(this.element, "days"));
            engineerReport.setLabourAmount(XmlHelper.getBigDecimalFromNode(this.element, "labour-amount"));
            engineerReport.setTotalAmount(XmlHelper.getBigDecimalFromNode(this.element, "total-amount"));
            engineerReport.setName(XmlHelper.getNodeValue(this.element, "name"));
            engineerReport.setCompany(XmlHelper.getNodeValue(this.element, "company"));
            engineerReport.setAddress1(XmlHelper.getNodeValue(this.element, "address1"));
            engineerReport.setAddress2(XmlHelper.getNodeValue(this.element, "address2"));
            engineerReport.setAddress3(XmlHelper.getNodeValue(this.element, "address3"));
            engineerReport.setAddress4(XmlHelper.getNodeValue(this.element, "address4"));
            engineerReport.setAddress5(XmlHelper.getNodeValue(this.element, "address5"));
            engineerReport.setPostcode(XmlHelper.getNodeValue(this.element, "postcode"));
            engineerReport.setTelephone(XmlHelper.getNodeValue(this.element, "telephone"));
            engineerReport.setEmail(XmlHelper.getEmailAddressFromNode(this.element, "email"));
            engineerReport.setIsUsable(XmlHelper.getBooleanFromNode(this.element, "usable"));

            this.claimResult.getClaim().setEngineerReport(engineerReport);
        }

    }

    private void doPrintResult(boolean isAllowed) {

        if (isAllowed) {

            System.out.println("-------");
            System.out.println(sectionName + "| Status :" + this.claimResult.isDataValid());

            if (this.claimResult.getClaim().getEngineerReport() != null) {

                System.out.println(sectionName + "| getDays :" + this.claimResult.getClaim().getEngineerReport().getDays());
                System.out.println(sectionName + "| getLabourAmount :" + this.claimResult.getClaim().getEngineerReport().getLabourAmount());
                System.out.println(sectionName + "| getTotalAmount :" + this.claimResult.getClaim().getEngineerReport().getTotalAmount());
                System.out.println(sectionName + "| getName :" + this.claimResult.getClaim().getEngineerReport().getName());
                System.out.println(sectionName + "| getCompany :" + this.claimResult.getClaim().getEngineerReport().getCompany());
                System.out.println(sectionName + "| getAddress1 :" + this.claimResult.getClaim().getEngineerReport().getAddress1());
                System.out.println(sectionName + "| getAddress2 :" + this.claimResult.getClaim().getEngineerReport().getAddress2());
                System.out.println(sectionName + "| getAddress3 :" + this.claimResult.getClaim().getEngineerReport().getAddress3());
                System.out.println(sectionName + "| getAddress4 :" + this.claimResult.getClaim().getEngineerReport().getAddress4());
                System.out.println(sectionName + "| getAddress5 :" + this.claimResult.getClaim().getEngineerReport().getAddress5());
                System.out.println(sectionName + "| getPostcode :" + this.claimResult.getClaim().getEngineerReport().getPostcode());
                System.out.println(sectionName + "| getTelephone :" + this.claimResult.getClaim().getEngineerReport().getTelephone());
                System.out.println(sectionName + "| getEmail :" + this.claimResult.getClaim().getEngineerReport().getEmail());
                System.out.println(sectionName + "| isIsUsable :" + this.claimResult.getClaim().getEngineerReport().isIsUsable());

            } else {
                System.out.println(sectionName + "| NO ENGINEER REPORT OBJECT HAVE FOUND!!");
            }

        }

    }
}
