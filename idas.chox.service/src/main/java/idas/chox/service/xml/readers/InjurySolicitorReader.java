package idas.chox.service.xml.readers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Solicitor;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import idas.chox.service.xml.validations.DataValidationParameter;
import java.util.ArrayList;
import org.w3c.dom.*;

public class InjurySolicitorReader {
    private static final Logger LOG = LoggerFactory.getLogger(InjurySolicitorReader.class);

    protected static String sectionName = "Injury Solicitor";
    private DataValidationParameter dataValidationParameter;
    private Element parentElement;

    public void execute(ClaimResult claimResult, Element parentElement, DataValidationParameter dataValidationParameter) throws Exception {
        this.dataValidationParameter = dataValidationParameter;
        this.parentElement = parentElement;

        if (validate(claimResult)) {
            process(claimResult);
        }
    }

    protected boolean validate(ClaimResult claimResult) throws Exception {

        Element element = XMLUtils.getElement(parentElement, "solicitor");

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.newSubscriberClaim)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.insurerUpload)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.tpiIntervention)) {

            isAllowToReadData = true;
            claimResult.setCheckDataValid(true);

            claimResult = NodeHelper.nodeValidate(sectionName, "name", element, claimResult, dataValidationParameter);
            claimResult = NodeHelper.nodeValidate(sectionName, "address1", element, claimResult, dataValidationParameter);
            claimResult = NodeHelper.nodeValidate(sectionName, "address2", element, claimResult, dataValidationParameter);
            claimResult = NodeHelper.nodeValidate(sectionName, "address3", element, claimResult, dataValidationParameter);
            claimResult = NodeHelper.nodeValidate(sectionName, "address4", element, claimResult, dataValidationParameter);
            claimResult = NodeHelper.nodeValidate(sectionName, "address5", element, claimResult, dataValidationParameter);
            claimResult = NodeHelper.nodeValidate(sectionName, "postcode", element, claimResult, dataValidationParameter);
            claimResult = NodeHelper.nodeValidate(sectionName, "telephone", element, claimResult, dataValidationParameter);
            claimResult = NodeHelper.nodeValidate(sectionName, "email", element, claimResult, dataValidationParameter);

            isAllowToReadData = claimResult.isDataValid();

        }
        LOG.debug("Solicitor isAllowToReadData={}", isAllowToReadData);
        return isAllowToReadData;
    }

    protected void process(ClaimResult claimResult) throws Exception {

        Element element = XMLUtils.getElement(parentElement, "solicitor");
        LOG.debug("Processing solicitor element.");
        
        ArrayList<Solicitor> solicitors = claimResult.getSolicitors();
        if (solicitors == null) {
            solicitors = new ArrayList<Solicitor>();
        }

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "name")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "address1")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "address2")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "address3")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "address4")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "address5")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "postcode")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "telephone")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "email"))) {

            Solicitor solicitor = new Solicitor();
            claimResult.getClaim().getIncident().getInjury().setSolicitor(solicitor);

            solicitor.setName(XmlHelper.getNodeValue(element, "name"));
            solicitor.setAddress1(XmlHelper.getNodeValue(element, "address1"));
            solicitor.setAddress2(XmlHelper.getNodeValue(element, "address2"));
            solicitor.setAddress3(XmlHelper.getNodeValue(element, "address3"));
            solicitor.setAddress4(XmlHelper.getNodeValue(element, "address4"));
            solicitor.setAddress5(XmlHelper.getNodeValue(element, "address5"));
            solicitor.setEmail(XmlHelper.getEmailAddressFromNode(element, "email"));
            solicitor.setPostcode(XmlHelper.getNodeValue(element, "postcode"));
            solicitor.setTelephone(XmlHelper.getNodeValue(element, "telephone"));

            solicitors.add(solicitor);
            LOG.debug("Solicitor name: {}", solicitor.getName());
        }

        if (solicitors.size() > 0) {
            claimResult.setSolicitors(solicitors);
        }

    }
}
