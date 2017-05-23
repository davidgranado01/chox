package idas.chox.service.xml.readers;

import org.w3c.dom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.InsurerAlias;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.InsurerAliasService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.core.util.TextHelper;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;


public class ClaimThirdPartyReader extends BaseEntityReader {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimThirdPartyReader.class);
    protected static String sectionName = "Third Party Details";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        Element rootElement = claimResult.getElement();
        Element claimElement = XMLUtils.getElement(rootElement, "claim");
        Element element = XMLUtils.getElement(claimElement, "third-party");

        VehicleClassService vehicleClassService = this.getBordereauReaderContext().getVehicleClassService();
        InsurerAliasService insurerAliasService = this.getBordereauReaderContext().getInsurerAliasService();
        InsurerChorganisationService insurerChorganisationService = this.getBordereauReaderContext().getInsurerChorganisationService();

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_COLLABORATION_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUBSCRIBER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_FIXEDFEE_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION)
                || (claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_INVOICE) && claimResult.getClaim().getClaimType() == ClaimType.INSURER_INVOICE)
                ) {

            claimResult.setCheckDataValid(true);

            NodeHelper.nodeInsurerAliasValidate(sectionName, "name", element, claimResult, getDataValidationParameter(), insurerAliasService, insurerChorganisationService);
            NodeHelper.nodeValidate(sectionName, "policy-number", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "claim-number", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "vehicle-registration", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "vehicle-manufacturer", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "vehicle-model", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeVehicleClassValidate(sectionName, "vehicle-class", element, claimResult, getDataValidationParameter(), vehicleClassService);
            NodeHelper.nodeValidate(sectionName, "title", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "firstnames", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "lastname", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "address1", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "address2", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "address3", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "address4", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "address5", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "postcode", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "telephone-day", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "telephone-evening", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "email", element, claimResult, getDataValidationParameter());

            isAllowToReadData = claimResult.isCheckDataValid();
        }

        return isAllowToReadData;
    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {
        Element rootElement = claimResult.getElement();
        Element claimElement = XMLUtils.getElement(rootElement, "claim");
        Element element = XMLUtils.getElement(claimElement, "third-party");

        VehicleClassService vehicleClassService = this.getBordereauReaderContext().getVehicleClassService();
        InsurerAliasService insurerAliasService = this.getBordereauReaderContext().getInsurerAliasService();

        if (claimResult.getClaim().getThirdParty() == null) {
            claimResult.getClaim().setThirdParty(new ThirdParty());
        }

        String vehicleClassName = XmlHelper.getNodeValue(element, "vehicle-class");
        if (vehicleClassName != null && vehicleClassName.length() > 0) {
            VehicleClass vehicleClass = vehicleClassService.getVehicleClassByNodeName(element, "vehicle-class");
            claimResult.getClaim().getThirdParty().setVehicleClass(vehicleClass);
        }

        String insurerAliasName = XmlHelper.getNodeValue(element, "name");
        if (insurerAliasName != null && insurerAliasName.length() > 0) {
            claimResult.getClaim().getThirdParty().setInsurerBrand(insurerAliasName.replaceAll("&amp;", "&"));
            InsurerAlias alias = insurerAliasService.getInsurerByAliasName(insurerAliasName);
            Insurer insurer = alias.getInsurer();
            claimResult.getClaim().getThirdParty().setInsurer(insurer);
            //Set claim Insurer equal to third party insurer
            if (getBordereauReaderContext().getSecurityInfoProvider().getIsCHO()) {
                claimResult.getClaim().setInsurer(insurer);
            }
        }

        String claimNumber = XmlHelper.getNodeValue(element, "claim-number");
        if (claimNumber != null && !claimNumber.isEmpty()) {
            claimNumber = claimNumber.trim();
        }
        claimResult.getClaim().setClaimNumber(claimNumber);
        claimResult.getClaim().getThirdParty().setPolicyNumber(XmlHelper.getNodeValue(element, "policy-number"));
        claimResult.getClaim().getThirdParty().setClaimReference(claimNumber);
        claimResult.getClaim().getThirdParty().setVehicleRegistration(TextHelper.trimWhiteSpace(XmlHelper.getNodeValue(element, "vehicle-registration")));
        claimResult.getClaim().getThirdParty().setVehicleManufacturer(XmlHelper.getNodeValue(element, "vehicle-manufacturer"));
        claimResult.getClaim().getThirdParty().setVehicleModel(XmlHelper.getNodeValue(element, "vehicle-model"));
        claimResult.getClaim().getThirdParty().setAddress1(XmlHelper.getNodeValue(element, "address1"));
        claimResult.getClaim().getThirdParty().setAddress2(XmlHelper.getNodeValue(element, "address2"));
        claimResult.getClaim().getThirdParty().setAddress3(XmlHelper.getNodeValue(element, "address3"));
        claimResult.getClaim().getThirdParty().setAddress4(XmlHelper.getNodeValue(element, "address4"));
        claimResult.getClaim().getThirdParty().setAddress5(XmlHelper.getNodeValue(element, "address5"));
        claimResult.getClaim().getThirdParty().setPostcode(XmlHelper.getNodeValue(element, "postcode"));
        claimResult.getClaim().getThirdParty().setTelephoneEvening(XmlHelper.getNodeValue(element, "telephone-evening"));
        claimResult.getClaim().getThirdParty().setTelephoneDay(XmlHelper.getNodeValue(element, "telephone-day"));
        claimResult.getClaim().getThirdParty().setEmail(XmlHelper.getEmailAddressFromNode(element, "email"));
        claimResult.getClaim().getThirdParty().setFirstName(XmlHelper.getNodeValue(element, "firstnames"));
        claimResult.getClaim().getThirdParty().setLastName(XmlHelper.getNodeValue(element, "lastname"));
        claimResult.getClaim().getThirdParty().setTitle(XmlHelper.getNodeValue(element, "title"));


    }
}
