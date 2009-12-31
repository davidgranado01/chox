package idas.chox.service.xml.validations;

import idas.chox.data.services.SecureDataService;
import idas.chox.core.model.Injury;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import java.util.ArrayList;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;

public class IncidentInjuriesValidation extends SecureDataService implements rulesInterface {

    protected static String sectionName = "Incident Injury";
    private DataValidationParameter dataValidationParameter;
    private ClaimService claimService;
    private ClaimResult claimResult;
    // THIS PAGE ONLY
    private Element element;
    private ArrayList<Element> injuryElements;

    public void setClaimResult(ClaimResult claimResult) {
        this.claimResult = claimResult;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) {
        this.dataValidationParameter = dataValidationParameter;
    }

    public IncidentInjuriesValidation(
            ClaimResult claimResult,
            DataValidationParameter dataValidationParameter,
            ClaimService claimService) {

        setClaimResult(claimResult);
        setDataValidationParameter(dataValidationParameter);
        setClaimService(claimService);
    }

    public ClaimResult execute() throws DOMException, XPathExpressionException, Exception {

        this.element = XMLUtils.getElement(XMLUtils.getElement(XMLUtils.getElement(claimResult.getElement(), "claim"), "incident"), "injuries");
        this.injuryElements = XMLUtils.getElements(this.element.getOwnerDocument(), this.element, "injury");

        if (validate()) {
            process();
        }

        doPrintResult(false);
        return claimResult;
    }

    private boolean validate() throws DOMException, XPathExpressionException, Exception {

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {

            isAllowToReadData = true;
            this.claimResult.setCheckDataValid(true);

            for (Element e : this.injuryElements) {

                this.claimResult = NodeHelper.nodeValidate(sectionName, "name", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "address1", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "address2", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "address3", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "address4", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "address5", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "postcode", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "telephone-day", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "telephone-evening", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "email", e, this.claimResult, this.dataValidationParameter);

            }

            isAllowToReadData = this.claimResult.isCheckDataValid();

        }

        return isAllowToReadData;
    }

    private void process() throws DOMException, XPathExpressionException, Exception {

        ArrayList<Injury> injuries = new ArrayList<Injury>();
        if (this.claimResult.getInjuries() != null) {
            injuries = this.claimResult.getInjuries();
        }

        for (Element e : this.injuryElements) {

            Injury injury = setInjury(e);

            if (injury != null) {

                injuries.add(injury);

                InjurySolicitorValidation InjurySolicitorValidation = new InjurySolicitorValidation(claimResult, dataValidationParameter, claimService, e, injury);
                this.claimResult = InjurySolicitorValidation.execute();

                break;
            }

        }

        if (injuries.size() > 0) {
            this.claimResult.setInjuries(injuries);
        }

    }

    private Injury setInjury(Element e) {

        Injury obj = null;

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "name")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address1")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address2")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address3")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address4")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address5")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "postcode")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "telephone-day")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "telephone-evening")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "email"))) {
            obj = new Injury();
            obj.setIncident(this.claimResult.getClaim().getIncident());
            obj.setName(XmlHelper.getNodeValue(e, "name"));
            obj.setAddress1(XmlHelper.getNodeValue(e, "address1"));
            obj.setAddress2(XmlHelper.getNodeValue(e, "address2"));
            obj.setAddress3(XmlHelper.getNodeValue(e, "address3"));
            obj.setAddress4(XmlHelper.getNodeValue(e, "address4"));
            obj.setAddress5(XmlHelper.getNodeValue(e, "address5"));
            obj.setEmail(XmlHelper.getEmailAddressFromNode(e, "email"));
            obj.setPostcode(XmlHelper.getNodeValue(e, "postcode"));
            obj.setTelephoneDay(XmlHelper.getNodeValue(e, "telephone-day"));
            obj.setTelephoneEvening(XmlHelper.getNodeValue(e, "telephone-evening"));
        }

        return obj;

    }

    private void doPrintResult(boolean isAllowed) {

        if (isAllowed) {

            System.out.println("-------");
            System.out.println("::: " + sectionName + "| Status :" + this.claimResult.isDataValid());

            if (this.claimResult.getInjuries().size() > 0) {

                for (Injury injury : this.claimResult.getInjuries()) {
                    System.out.println("::: " + sectionName + "| getDate :" + injury.getName());
                    System.out.println("::: " + sectionName + "| getDate :" + injury.getAddress1());
                    System.out.println("::: " + sectionName + "| getDate :" + injury.getAddress2());
                    System.out.println("::: " + sectionName + "| getDate :" + injury.getAddress3());
                    System.out.println("::: " + sectionName + "| getDate :" + injury.getAddress4());
                    System.out.println("::: " + sectionName + "| getDate :" + injury.getAddress5());
                    System.out.println("::: " + sectionName + "| getDate :" + injury.getEmail());
                    System.out.println("::: " + sectionName + "| getDate :" + injury.getPostcode());
                    System.out.println("::: " + sectionName + "| getDate :" + injury.getTelephoneDay());
                    System.out.println("::: " + sectionName + "| getDate :" + injury.getTelephoneEvening());
                }

            } else {
                System.out.println(sectionName + "| NO INJURY OBJECT HAVE FOUND!!");
            }
        }
    }
}
