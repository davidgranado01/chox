package idas.chox.service.xml.readers;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Injury;
import idas.chox.core.model.ClaimType;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;

public class IncidentInjuriesReader extends BaseEntityReader {

    private static final Logger LOG = LoggerFactory.getLogger(IncidentInjuriesReader.class);
    protected static String sectionName = "Incident Injury";

    private Injury setInjury(ClaimResult claimResult, Element e) {

        Injury obj = null;
        if (claimResult.getClaim().getIncident() != null) {
            if (XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "name")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address1")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address2")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address3")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address4")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address5")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "postcode")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "telephone-day")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "telephone-evening")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "email"))) {
                LOG.debug("Found valid injury - creating object.");
                obj = new Injury();
                obj.setIncident(claimResult.getClaim().getIncident());
                claimResult.getClaim().getIncident().setInjury(obj);
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
                /*
                 *  added if check for bug#1114 'Injury Solicitor Fields Not Uploading For TPI Claims'
                 */
                Element solicitorElement = XMLUtils.getElement(e, "solicitor");
                if (XmlHelper.isNotNull(XmlHelper.getNodeValue(solicitorElement, "name")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(solicitorElement, "address1")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(solicitorElement, "address2")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(solicitorElement, "address3")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(solicitorElement, "address4")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(solicitorElement, "address5")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(solicitorElement, "postcode")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(solicitorElement, "telephone")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(solicitorElement, "email"))) {
                    LOG.info("Found injury solicitor without injury. Creating empty injury to hold solicitor object.");
                    obj = new Injury();
                    obj.setIncident(claimResult.getClaim().getIncident());
                    claimResult.getClaim().getIncident().setInjury(obj);
                }
                LOG.debug("Injury section not valid in XML.");
            }
        }
        return obj;

    }

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        Element element = XMLUtils.getElement(XMLUtils.getElement(claimResult.getElement(), "claim"), "incident");
        ArrayList<Element> injuryElements = XMLUtils.getElements(element.getOwnerDocument(), element, "injury");

        if (injuryElements == null) {
            LOG.debug("Injury elements is null");
        } else {
            LOG.debug("injuryElements contains {} elements.", injuryElements.size());
        }

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUBSCRIBER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_FIXEDFEE_CLAIM)
                || (claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_INVOICE) && claimResult.getClaim().getClaimType() == ClaimType.INSURER_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION)) {

            claimResult.setCheckDataValid(true);

            for (Element e : injuryElements) {

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
            LOG.debug("Allowed to read inury data: {}", isAllowToReadData);
        }

        return isAllowToReadData;
    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {

        Element rootElement = claimResult.getElement();
        Element claimElement = XMLUtils.getElement(rootElement, "claim");
        Element incidentElement = XMLUtils.getElement(claimElement, "incident");
        ArrayList<Element> injuryElements = XMLUtils.getElements(incidentElement.getOwnerDocument(), incidentElement, "injury");

        if (injuryElements != null) {
            LOG.debug("We have {} injury elements.", injuryElements.size());
        } else {
            LOG.debug("No injury elements returned from document.");
        }

        List<Injury> injuries = claimResult.getInjuries();
        if (injuries == null) {
            injuries = new ArrayList<Injury>();
        }

        for (Element e : injuryElements) {

            Injury injury = setInjury(claimResult, e);


            if (injury != null) {
                LOG.debug("Adding injury to injuries, with incident {} ", injury.getIncident());
                injuries.add(injury);
                LOG.debug("Injury name (before calling solicitor reader): {}", injury.getName());
                InjurySolicitorReader injurySolicitorReader = new InjurySolicitorReader();
                try {
                    injurySolicitorReader.execute(claimResult, e, this.getDataValidationParameter());
                } catch (Exception ex) {
                    LOG.error("Exception caught: {}", ex.getMessage());
                    if (ex.getCause() != null) {
                        LOG.error("Caused by: {}", ex.getCause().getMessage());
                    }
                    throw ex;
                }
                LOG.debug("Injury name (after calling solicitor reader): {}", injury.getName());
                break;
            }
        }
        if (injuries.size() > 0) {
            LOG.debug("Adding injuries to claim result");
            claimResult.setInjuries(injuries);
        }

    }
}
