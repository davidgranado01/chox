package idas.chox.service.xml.readers;

import idas.chox.core.model.VehicleClass;
import idas.chox.core.util.TextHelper;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import org.w3c.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClaimCustomerReader extends BaseEntityReader {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimCustomerReader.class);

    protected static String sectionName = "Customer Detail";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        boolean isAllowToReadData = false;

        Element claimElement = XMLUtils.getElement(claimResult.getElement(), "claim");
        Element element = XMLUtils.getElement(claimElement, "customer");

        LOG.debug("Validating Claim Customer");

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim) 
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.tpiIntervention)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.hireMoniteringAndNewInvoice)) {
            LOG.debug("Validating new claim");
            isAllowToReadData = true;
            claimResult.setCheckDataValid(true);

            // INSURER            
            claimResult = NodeHelper.nodeValidate(sectionName, "name", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "policy-number", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "claim-reference", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "comprehensive", element, claimResult, getDataValidationParameter());

            // VEHICLE
            claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-registration", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-manufacturer", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-model", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "year-of-manufacture", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeVehicleClassValidate(sectionName, "vehicle-class", element, claimResult, getDataValidationParameter(), getBordereauRederContext().getVehicleClassService());
            claimResult = NodeHelper.nodeValidate(sectionName, "location", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "damage", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "usable", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "total-loss", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "initial-ecd", element, claimResult, getDataValidationParameter());

            // MITIGATION - moved to separate reader

            // CHECK VEHICLE CLASS            
            isAllowToReadData = claimResult.isCheckDataValid();

        }

        LOG.debug("Validating Claim Customer: returning {}", isAllowToReadData);

        return isAllowToReadData;
    }

    @Override
    protected void process(ClaimResult claimResult) {

        Element claimElement = XMLUtils.getElement(claimResult.getElement(), "claim");
        Element element = XMLUtils.getElement(claimElement, "customer");
        LOG.debug("Processing claim customer element...");
        if (claimResult.getClaim().getCustomer() != null) {

            String vehicleClassName = XmlHelper.getNodeValue(element, "vehicle-class");
            if (vehicleClassName != null && vehicleClassName.length() > 0) {
                LOG.debug("Setting VehicleClass...");
                VehicleClass vehicleClass = null;
                vehicleClass = getBordereauRederContext().getVehicleClassService().getVehicleClassByNodeName(element, "vehicle-class");
                claimResult.getClaim().getCustomer().setVehicleClass(vehicleClass);
            }

            LOG.debug("Setting InsurerName...");
            claimResult.getClaim().getCustomer().setInsurerName(XmlHelper.getNodeValue(element, "name"));
            LOG.debug("Setting PolicyNumber...");
            claimResult.getClaim().getCustomer().setPolicyNumber(XmlHelper.getNodeValue(element, "policy-number"));
            LOG.debug("Setting ClaimReference...");
            claimResult.getClaim().getCustomer().setClaimReference(XmlHelper.getNodeValue(element, "claim-reference"));
            LOG.debug("Setting Comprehensive...");
            claimResult.getClaim().getCustomer().setComprehensive(XmlHelper.getBooleanFromNode(element, "comprehensive"));
            LOG.debug("Setting VehicleRegistration...");
            claimResult.getClaim().getCustomer().setVehicleRegistration(TextHelper.trimWhiteSpace(XmlHelper.getNodeValue(element, "vehicle-registration")));
            LOG.debug("Setting VehicleManufacturer...");
            claimResult.getClaim().getCustomer().setVehicleManufacturer(XmlHelper.getNodeValue(element, "vehicle-manufacturer"));
            LOG.debug("Setting VehicleModel...");
            claimResult.getClaim().getCustomer().setVehicleModel(XmlHelper.getNodeValue(element, "vehicle-model"));
            LOG.debug("Setting VehicleYear...");
            claimResult.getClaim().getCustomer().setVehicleYear(XmlHelper.getNodeValue(element, "year-of-manufacture"));
            LOG.debug("Setting IsUsable...");
            claimResult.getClaim().getCustomer().setIsUsable(XmlHelper.getBooleanFromNode(element, "usable"));
            LOG.debug("Setting Location...");
            claimResult.getClaim().getCustomer().setLocation(XmlHelper.getNodeValue(element, "location"));
            LOG.debug("Setting Damage...");
            claimResult.getClaim().getCustomer().setDamage(XmlHelper.getNodeValue(element, "damage"));
            LOG.debug("Setting InitialECD...");
            claimResult.getClaim().getCustomer().setInitialECD(XmlHelper.getDateFromNode(element, "initial-ecd"));
            LOG.debug("Setting IsTotalLoss...");
            claimResult.getClaim().getCustomer().setIsTotalLoss(XmlHelper.getBooleanFromNode(element, "total-loss"));
        }
    }

}
