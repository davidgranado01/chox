package idas.chox.service.xml.readers;

import org.w3c.dom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Witness;
import idas.chox.core.model.ClaimType;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.util.XmlHelper;
import idas.chox.service.xml.util.NodeHelper;

public class IncidentWitnessReader extends BaseEntityReader {

    private static final Logger LOG = LoggerFactory.getLogger(IncidentWitnessReader.class);
    protected static String sectionName = "Incident Witness";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        LOG.debug("Validating section '{}'.", sectionName);
        Element rootElement = claimResult.getElement();
        Element claimElement = XMLUtils.getElement(rootElement, "claim");
        Element incidentElement = XMLUtils.getElement(claimElement, "incident");
        Element witnessElement = XMLUtils.getElement(incidentElement, "witness");
        LOG.debug("witness elements retrieved: {}", witnessElement);

        boolean isAllowToReadData = false;
        LOG.debug("Current claim parse status is '{}'", claimResult.getClaimParseStatus());
        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_COLLABORATION_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUBSCRIBER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_FIXEDFEE_CLAIM)
                || (claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_INVOICE) && claimResult.getClaim().getClaimType() == ClaimType.INSURER_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION)) {

            claimResult.setCheckDataValid(true);
            NodeHelper.nodeValidate(sectionName, "name", witnessElement, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "address1", witnessElement, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "address2", witnessElement, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "address3", witnessElement, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "address4", witnessElement, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "address5", witnessElement, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "postcode", witnessElement, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "telephone-day", witnessElement, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "telephone-evening", witnessElement, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "email", witnessElement, claimResult, getDataValidationParameter());

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
        Element witnessElement = XMLUtils.getElement(incidentElement, "witness");

        if (claimResult.getClaim().getIncident() != null) {

            if (XmlHelper.isNotNull(XmlHelper.getNodeValue(witnessElement, "name"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(witnessElement, "address1"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(witnessElement, "address2"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(witnessElement, "address3"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(witnessElement, "address4"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(witnessElement, "address5"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(witnessElement, "postcode"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(witnessElement, "telephone-day"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(witnessElement, "telephone-evening"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(witnessElement, "email"))) {

                Witness witness = new Witness();
                witness.setIncident(claimResult.getClaim().getIncident());
                claimResult.getClaim().getIncident().setWitness(witness);
                witness.setName(XmlHelper.getNodeValue(witnessElement, "name"));
                witness.setAddress1(XmlHelper.getNodeValue(witnessElement, "address1"));
                witness.setAddress2(XmlHelper.getNodeValue(witnessElement, "address2"));
                witness.setAddress3(XmlHelper.getNodeValue(witnessElement, "address3"));
                witness.setAddress4(XmlHelper.getNodeValue(witnessElement, "address4"));
                witness.setAddress5(XmlHelper.getNodeValue(witnessElement, "address5"));
                witness.setEmail(XmlHelper.getEmailAddressFromNode(witnessElement, "email"));
                witness.setPostcode(XmlHelper.getNodeValue(witnessElement, "postcode"));
                witness.setTelephoneDay(XmlHelper.getNodeValue(witnessElement, "telephone-day"));
                witness.setTelephoneEvening(XmlHelper.getNodeValue(witnessElement, "telephone-evening"));
            } else {
                LOG.debug("Witness section not valid in XML.");
            }
        }
    }
}
