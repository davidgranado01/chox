package idas.chox.service.xml.readers;

import idas.chox.core.model.Injury;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import java.util.ArrayList;
import org.w3c.dom.*;

public class IncidentInjuriesReader extends BaseEntityReader {

    protected static String sectionName = "Incident Injury";

    private Injury setInjury(ClaimResult claimResult, Element e) {

        Injury obj = null;

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "name")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address1")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address2")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address3")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address4")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address5")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "postcode")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "telephone-day")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "telephone-evening")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "email"))) {
            obj = new Injury();
            obj.setIncident(claimResult.getClaim().getIncident());
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
  
    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        Element element = XMLUtils.getElement(XMLUtils.getElement(XMLUtils.getElement(claimResult.getElement(), "claim"), "incident"), "injuries");
        ArrayList<Element> injuryElements = XMLUtils.getElements(element.getOwnerDocument(), element, "injury");

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {

            isAllowToReadData = true;
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

        }

        return isAllowToReadData;
    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {

        Element rootElement = claimResult.getElement();
        Element claimElement = XMLUtils.getElement(rootElement, "claim");
        Element incidentElement =XMLUtils.getElement(claimElement, "incident");
        Element element = XMLUtils.getElement(incidentElement, "injuries");
        ArrayList<Element> injuryElements = XMLUtils.getElements(element.getOwnerDocument(), element, "injury");

        ArrayList<Injury> injuries = new ArrayList<Injury>();
        if (claimResult.getInjuries() != null) {
            injuries = claimResult.getInjuries();
        }

        for (Element e : injuryElements) {

            Injury injury = setInjury(claimResult, e);

            if (injury != null) {
                injuries.add(injury);
                InjurySolicitorReader injurySolicitorReader = new InjurySolicitorReader();
                injurySolicitorReader.execute(claimResult, e, this.getDataValidationParameter());
                break;
            }
        }
        if (injuries.size() > 0) {
            claimResult.setInjuries(injuries);
        }

    }
}
