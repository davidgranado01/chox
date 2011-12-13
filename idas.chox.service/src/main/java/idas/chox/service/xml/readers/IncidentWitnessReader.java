package idas.chox.service.xml.readers;

import idas.chox.core.model.Witness;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncidentWitnessReader extends BaseEntityReader {

    private static final Logger LOG = LoggerFactory.getLogger(IncidentWitnessReader.class);
    protected static String sectionName = "Incident Witness";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        LOG.debug("Validating section '{}'.", sectionName);
        Element rootElement = claimResult.getElement();
        Element claimElement = XMLUtils.getElement(rootElement, "claim");
        Element incidentElement = XMLUtils.getElement(claimElement, "incident");
        List<Element> witnessElements = XMLUtils.getElements(incidentElement.getOwnerDocument(), incidentElement, "witness");
        LOG.debug("witness elements retrieved: {}", witnessElements);

        boolean isAllowToReadData = false;
        LOG.debug("Current claim parse status is '{}'", claimResult.getClaimParseStatus());
        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.newSubscriberClaim)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.insurerUpload)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.tpiIntervention)) {

            isAllowToReadData = true;
            claimResult.setCheckDataValid(true);
            for (Element e : witnessElements) {
                LOG.debug("Validating witness name");
                claimResult = NodeHelper.nodeValidate(sectionName, "name", e, claimResult, getDataValidationParameter());
                claimResult = NodeHelper.nodeValidate(sectionName, "address1", e, claimResult, getDataValidationParameter());
                claimResult = NodeHelper.nodeValidate(sectionName, "address2", e, claimResult, getDataValidationParameter());
                claimResult = NodeHelper.nodeValidate(sectionName, "address3", e, claimResult, getDataValidationParameter());
                claimResult = NodeHelper.nodeValidate(sectionName, "address4", e, claimResult, getDataValidationParameter());
                claimResult = NodeHelper.nodeValidate(sectionName, "address5", e, claimResult, getDataValidationParameter());
                claimResult = NodeHelper.nodeValidate(sectionName, "postcode", e, claimResult, getDataValidationParameter());
                claimResult = NodeHelper.nodeValidate(sectionName, "telephone-day", e, claimResult, getDataValidationParameter());
                claimResult = NodeHelper.nodeValidate(sectionName, "telephone-evening", e, claimResult, getDataValidationParameter());
                claimResult = NodeHelper.nodeValidate(sectionName, "email", e, claimResult, getDataValidationParameter());

            }
            isAllowToReadData = claimResult.isCheckDataValid();
        }
        return isAllowToReadData;
    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {
        LOG.debug("Processing section '{}'.", sectionName);

        Element rootElement = claimResult.getElement();
        Element claimElement = XMLUtils.getElement(rootElement, "claim");
        Element incidentElement = XMLUtils.getElement(claimElement, "incident");
        List<Element> witnessElements = XMLUtils.getElements(incidentElement.getOwnerDocument(), incidentElement, "witness");

        ArrayList<Witness> witnesses = new ArrayList<Witness>();
        if (claimResult.getWitnesses() != null) {
            witnesses = claimResult.getWitnesses();
        }

        for (Element e : witnessElements) {
            Witness witness = setWitness(claimResult, e);

            if (witness != null) {
                witnesses.add(witness);
            }
        }

        if (witnesses.size() > 0) {
            claimResult.setWitnesses(witnesses);
        }
    }

    private Witness setWitness(ClaimResult claimResult, Element e) {

        Witness obj = null;

        if (claimResult.getClaim().getIncident() != null) {

            if (XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "name")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address1")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address2")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address3")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address4")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address5")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "postcode")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "telephone-day")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "telephone-evening")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "email"))) {

                obj = new Witness();
                obj.setIncident(claimResult.getClaim().getIncident());
                claimResult.getClaim().getIncident().setWitness(obj);
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
            } else {
                LOG.debug("Witness section not valid in XML.");
            }
        }
        return obj;
    }
}
