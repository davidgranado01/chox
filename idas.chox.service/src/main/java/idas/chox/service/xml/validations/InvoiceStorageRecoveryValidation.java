package idas.chox.service.xml.validations;

import idas.chox.data.services.SecureDataService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.xmlValidation.DataValidationParameter;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;

public class InvoiceStorageRecoveryValidation extends SecureDataService implements rulesInterface {

    protected static String sectionName = "Invoice Storage Recovery";
    private DataValidationParameter dataValidationParameter;
    private ClaimService claimService;
    private ChorganisationService chorganisationService;
    private BreBandService choBandService;
    private ClaimResult claimResult;
    private Element element;

    public void setElement(Element element) {
        this.element = element;
    }

    public void setBreBandService(BreBandService choBandService) {
        this.choBandService = choBandService;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public void setClaimResult(ClaimResult claimResult) {
        this.claimResult = claimResult;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) {
        this.dataValidationParameter = dataValidationParameter;
    }

    public InvoiceStorageRecoveryValidation(
            ClaimResult claimResult,
            DataValidationParameter dataValidationParameter,
            ClaimService claimService,
            ChorganisationService chorganisationService,
            BreBandService choBandService) {

        setClaimResult(claimResult);
        setDataValidationParameter(dataValidationParameter);
        setClaimService(claimService);
        setChorganisationService(chorganisationService);
        setBreBandService(choBandService);
    }

    public ClaimResult execute() throws DOMException, XPathExpressionException, Exception {

        Element root = XMLUtils.getElement(claimResult.getElement(), "invoice");
        this.element = XMLUtils.getElement(root, "storage-recovery");

        if (validate()) {
            process();
        }

        doPrintResult(false);
        return this.claimResult;
    }

    private boolean validate() throws DOMException, XPathExpressionException, Exception {

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)) {

            isAllowToReadData = true;

            this.claimResult = NodeHelper.nodeValidate(sectionName, "net", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "vat", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "gross", this.element, claimResult, dataValidationParameter);

            if (!this.claimResult.isCheckDataValid()) {
                isAllowToReadData = false;
                this.claimResult.setValid(false);
                this.claimResult.setDataValid(false);
            }

        }

        return isAllowToReadData;
    }

    private void process() {
        this.claimResult.getClaim().getInvoice().setStorageRecoveryGross(XmlHelper.getBigDecimalFromNode(this.element, "gross"));
        this.claimResult.getClaim().getInvoice().setStorageRecoveryNet(XmlHelper.getBigDecimalFromNode(this.element, "net"));
        this.claimResult.getClaim().getInvoice().setStorageRecoveryVat(XmlHelper.getBigDecimalFromNode(this.element, "vat"));
    }

    private void doPrintResult(boolean isAllowed) {

        if (isAllowed) {

            System.out.println("-------");
            System.out.println(sectionName + "| Status :" + this.claimResult.isDataValid());

            if (claimResult.getClaim().getInvoice() != null) {

                System.out.println(sectionName + "|Claim Data >getStorageRecoveryGross :" + claimResult.getClaim().getInvoice().getStorageRecoveryGross());
                System.out.println(sectionName + "|Claim Data >getStorageRecoveryNet :" + claimResult.getClaim().getInvoice().getStorageRecoveryNet());
                System.out.println(sectionName + "|Claim Data >getStorageRecoveryVat :" + claimResult.getClaim().getInvoice().getStorageRecoveryVat());

            } else {
                System.out.println(sectionName + "| NO INCIDENT OBJECT HAVE FOUND!!");
            }
        }
    }
}
