package idas.chox.service.xml.readers;

import org.w3c.dom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Incident;
import idas.chox.core.model.ClaimType;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;

public class ClaimIncidentReader extends BaseEntityReader {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimIncidentReader.class);

    protected static String sectionName = "Incident";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        Element element = XMLUtils.getElement(XMLUtils.getElement(claimResult.getElement(), "claim"), "incident");

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUBSCRIBER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_FIXEDFEE_CLAIM)
                || (claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_INVOICE) && claimResult.getClaim().getClaimType() == ClaimType.INSURER_INVOICE)) {

            claimResult.setCheckDataValid(true);
            // INCIDENT
            NodeHelper.nodeValidate(sectionName, "date", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "location", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "police-involved", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "description", element, claimResult, getDataValidationParameter());

            LOG.debug("Incident section validated with claimResult: {}", claimResult);
            LOG.debug("Incident section validated with isAllowToReadData: {}", claimResult.isCheckDataValid());
            isAllowToReadData = claimResult.isCheckDataValid();
        }
        return isAllowToReadData;
    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {

        Element element = XMLUtils.getElement(XMLUtils.getElement(claimResult.getElement(), "claim"), "incident");


        LOG.debug("Processing Incident section...");
        if (claimResult.getClaim().getIncident() == null) {
            LOG.debug("Creating new incident object");
            claimResult.getClaim().setIncident(new Incident());
        }

        LOG.debug("Setting incident date.");
        claimResult.getClaim().getIncident().setDate(XmlHelper.getDateFromDateTimeNode(element, "date"));
        LOG.debug("Setting incident time.");
        claimResult.getClaim().getIncident().setTime(XmlHelper.getTimeFromDateTimeNode(element, "date"));
        LOG.debug("Setting incident location.");
        claimResult.getClaim().getIncident().setLocation(XmlHelper.getNodeValue(element, "location"));
        LOG.debug("Setting incident police-involved.");
        claimResult.getClaim().getIncident().setIsPoliceInvolved(XmlHelper.getBooleanFromNode(element, "police-involved"));
        LOG.debug("Setting incident description.");
        claimResult.getClaim().getIncident().setIncidentDescription(XmlHelper.getNodeValue(element, "description"));
    }
}
