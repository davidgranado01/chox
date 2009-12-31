package idas.chox.service.xml.validations;

import idas.chox.data.services.SecureDataService;
import idas.chox.core.model.Injury;
import idas.chox.core.model.Solicitor;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import java.util.ArrayList;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;

public class InjurySolicitorValidation extends SecureDataService implements rulesInterface {

    protected static String sectionName = "Injury Solicitor";
    private DataValidationParameter dataValidationParameter;
    private ClaimService claimService;
    private ClaimResult claimResult;
    private Element rootElement;
    private Injury injury;
    // THIS PAGE ONLY
    private Element element;

    public void setInjury(Injury injury) {
        this.injury = injury;
    }

    public void setRootElement(Element rootElement) {
        this.rootElement = rootElement;
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

    public InjurySolicitorValidation(
            ClaimResult claimResult,
            DataValidationParameter dataValidationParameter,
            ClaimService claimService,
            Element rootElement,
            Injury injury) {

        setClaimResult(claimResult);
        setDataValidationParameter(dataValidationParameter);
        setClaimService(claimService);
        setRootElement(rootElement);
        setInjury(injury);
    }

    public ClaimResult execute() throws DOMException, XPathExpressionException, Exception {

        this.element = XMLUtils.getElement(rootElement, "solicitor");

        if (validate()) {
            process();
        }

        doPrintResult(false);
        return this.claimResult;
    }

    private boolean validate() throws DOMException, XPathExpressionException, Exception {

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {

            isAllowToReadData = true;
            this.claimResult.setCheckDataValid(true);

            this.claimResult = NodeHelper.nodeValidate(sectionName, "name", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address1", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address2", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address3", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address4", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address5", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "postcode", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "telephone", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "email", this.element, this.claimResult, this.dataValidationParameter);

            isAllowToReadData = this.claimResult.isDataValid();

        }

        return isAllowToReadData;
    }

    private void process() {

        ArrayList<Solicitor> solicitors = new ArrayList<Solicitor>();
        if (this.claimResult.getSolicitors() != null) {
            solicitors = this.claimResult.getSolicitors();
        }

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "name")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address1")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address2")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address3")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address4")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address5")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "postcode")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "telephone")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "email"))) {

            Solicitor solicitor = new Solicitor();

            solicitor.setAddress1(XmlHelper.getNodeValue(this.element, "address1"));
            solicitor.setAddress2(XmlHelper.getNodeValue(this.element, "address2"));
            solicitor.setAddress3(XmlHelper.getNodeValue(this.element, "address3"));
            solicitor.setAddress4(XmlHelper.getNodeValue(this.element, "address4"));
            solicitor.setAddress5(XmlHelper.getNodeValue(this.element, "address5"));
            solicitor.setEmail(XmlHelper.getEmailAddressFromNode(this.element, "email"));
            solicitor.setName(XmlHelper.getNodeValue(this.element, "name"));
            solicitor.setPostcode(XmlHelper.getNodeValue(this.element, "postcode"));
            solicitor.setTelephone(XmlHelper.getNodeValue(this.element, "telephone"));

            solicitors.add(solicitor);

        }

        if (solicitors.size() > 0) {
            this.claimResult.setSolicitors(solicitors);
        }

    }

    private void doPrintResult(boolean isAllowed) {

        if (isAllowed) {

            System.out.println("-------");
            System.out.println("::: ::: " + sectionName + "| Status :" + this.claimResult.isDataValid());

            if (this.claimResult.getSolicitors() != null) {

                for (Solicitor obj : this.claimResult.getSolicitors()) {

                    System.out.println("::: ::: " + sectionName + "| getAddress1 :" + obj.getAddress1());
                    System.out.println("::: ::: " + sectionName + "| getAddress2 :" + obj.getAddress2());
                    System.out.println("::: ::: " + sectionName + "| getAddress3 :" + obj.getAddress3());
                    System.out.println("::: ::: " + sectionName + "| getAddress4 :" + obj.getAddress4());
                    System.out.println("::: ::: " + sectionName + "| getAddress5 :" + obj.getAddress5());
                    System.out.println("::: ::: " + sectionName + "| getEmail :" + obj.getEmail());
                    System.out.println("::: ::: " + sectionName + "| getName :" + obj.getName());
                    System.out.println("::: ::: " + sectionName + "| getPostcode :" + obj.getPostcode());
                    System.out.println("::: ::: " + sectionName + "| getTelephone :" + obj.getTelephone());
                }

            } else {
                System.out.println(sectionName + "| NO SOLICITOR OBJECT HAVE FOUND!!");
            }
        }

    }
}
