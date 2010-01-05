package idas.chox.service.xml.readers;

import idas.chox.core.model.Incident;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import org.w3c.dom.*;

public class ClaimIncidentReader extends BaseEntityReader {

    protected static String sectionName = "Incident";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        Element element = XMLUtils.getElement(XMLUtils.getElement(claimResult.getElement(), "claim"), "incident");

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {

            isAllowToReadData = true;
            claimResult.setCheckDataValid(true);
            // INCIDENT
            claimResult = NodeHelper.nodeValidate(sectionName, "date", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "location", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "police-involved", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "description", element, claimResult, getDataValidationParameter());

            isAllowToReadData = claimResult.isCheckDataValid();
        }
        return isAllowToReadData;
    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {

        Element element = XMLUtils.getElement(XMLUtils.getElement(claimResult.getElement(), "claim"), "incident");


        if (claimResult.getClaim().getIncident() == null) {
            claimResult.getClaim().setIncident(new Incident());
        }

        claimResult.getClaim().getIncident().setDate(XmlHelper.getDateFromNode(element, "date"));
        claimResult.getClaim().getIncident().setLocation(XmlHelper.getNodeValue(element, "location"));
        claimResult.getClaim().getIncident().setIsPoliceInvolved(XmlHelper.getBooleanFromNode(element, "police-involved"));
        claimResult.getClaim().getIncident().setIncidentDescription(XmlHelper.getNodeValue(element, "description"));
    }
}
