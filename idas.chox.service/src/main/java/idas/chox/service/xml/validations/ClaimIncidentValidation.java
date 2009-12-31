package idas.chox.service.xml.validations;

import idas.chox.data.services.SecureDataService;
import idas.chox.core.model.Incident;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;

public class ClaimIncidentValidation extends SecureDataService implements rulesInterface {

    protected static String sectionName = "Incident";
    private DataValidationParameter dataValidationParameter;
    private ClaimService claimService;
    private ClaimResult claimResult;
    private Element element;

    public void setClaimResult(ClaimResult claimResult) {
        this.claimResult = claimResult;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) {
        this.dataValidationParameter = dataValidationParameter;
    }

    public ClaimIncidentValidation(
            ClaimResult claimResult,
            DataValidationParameter dataValidationParameter,
            ClaimService claimService) {

        setClaimResult(claimResult);
        setDataValidationParameter(dataValidationParameter);
        setClaimService(claimService);
    }

    public ClaimResult execute() throws DOMException, XPathExpressionException, Exception {

        this.element = XMLUtils.getElement(XMLUtils.getElement(claimResult.getElement(), "claim"), "incident");

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

            // INCIDENT
            this.claimResult = NodeHelper.nodeValidate(sectionName, "date", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "location", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "police-involved", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "description", this.element, claimResult, dataValidationParameter);

            isAllowToReadData = this.claimResult.isCheckDataValid();
        }

        return isAllowToReadData;
    }

    private void process() throws DOMException, XPathExpressionException, Exception {

        if (this.claimResult.getClaim().getIncident() == null) {
            this.claimResult.getClaim().setIncident(new Incident());
        }

        this.claimResult.getClaim().getIncident().setDate(XmlHelper.getTimeStampFromNode(this.element, "date"));
        this.claimResult.getClaim().getIncident().setLocation(XmlHelper.getNodeValue(this.element, "location"));
        this.claimResult.getClaim().getIncident().setIsPoliceInvolved(XmlHelper.getBooleanFromNode(this.element, "police-involved"));
        this.claimResult.getClaim().getIncident().setIncidentDescription(XmlHelper.getNodeValue(this.element, "description"));

        // WITNESSES
        IncidentWitnessValidation incidentWitnessValidation = new IncidentWitnessValidation(claimResult, dataValidationParameter, claimService);
        this.claimResult = incidentWitnessValidation.execute();

        // INJURIES
        IncidentInjuriesValidation incidentInjuriesValidation = new IncidentInjuriesValidation(claimResult, dataValidationParameter, claimService);
        this.claimResult = incidentInjuriesValidation.execute();


    }

    private void doPrintResult(boolean isAllowed) {
        if (isAllowed) {

            System.out.println("-------");
            System.out.println(sectionName + "| Status :" + this.claimResult.isDataValid());

            if (this.claimResult.getClaim().getIncident() != null) {
                System.out.println(sectionName + "| getDate :" + this.claimResult.getClaim().getIncident().getDate());
                System.out.println(sectionName + "| getLocation :" + this.claimResult.getClaim().getIncident().getLocation());
                System.out.println(sectionName + "| getIsPoliceInvolved :" + this.claimResult.getClaim().getIncident().isIsPoliceInvolved());
                System.out.println(sectionName + "| getIncidentDescription :" + this.claimResult.getClaim().getIncident().getIncidentDescription());
            } else {
                System.out.println(sectionName + "| NO INCIDENT OBJECT HAVE FOUND!!");
            }

        }
    }
}
