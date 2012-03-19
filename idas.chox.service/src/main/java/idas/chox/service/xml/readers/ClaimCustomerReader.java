package idas.chox.service.xml.readers;

import idas.chox.core.hpi.Hpi;
import idas.chox.core.hpi.HpiException;
import idas.chox.core.hpi.HpiResponse;
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

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUBSCRIBER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_UPLOAD)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING)) {
            LOG.debug("Validating new claim");
            isAllowToReadData = true;
            claimResult.setCheckDataValid(true);

            // INSURER            
            claimResult = NodeHelper.nodeValidate(sectionName, "name", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "policy-number", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "claim-number", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "comprehensive", element, claimResult, getDataValidationParameter());

            // VEHICLE
            claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-registration", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-manufacturer", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-model", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "year-of-manufacture", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeVehicleClassValidate(sectionName, "vehicle-class", element, claimResult, getDataValidationParameter(), getBordereauReaderContext().getVehicleClassService());
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
                vehicleClass = getBordereauReaderContext().getVehicleClassService().getVehicleClassByNodeName(element, "vehicle-class");
                claimResult.getClaim().getCustomer().setVehicleClass(vehicleClass);
            }

            LOG.debug("Setting InsurerName...");
            claimResult.getClaim().getCustomer().setInsurerName(XmlHelper.getNodeValue(element, "name"));
            LOG.debug("Setting PolicyNumber...");
            claimResult.getClaim().getCustomer().setPolicyNumber(XmlHelper.getNodeValue(element, "policy-number"));
            LOG.debug("Setting ClaimReference...");
            claimResult.getClaim().getCustomer().setClaimReference(XmlHelper.getNodeValue(element, "claim-number"));
            LOG.debug("Setting Comprehensive...");
            claimResult.getClaim().getCustomer().setComprehensive(XmlHelper.getBooleanFromNode(element, "comprehensive"));
            LOG.debug("Setting VehicleRegistration...");
            String oldVrn = claimResult.getClaim().getCustomer().getVehicleRegistration();
            claimResult.getClaim().getCustomer().setVehicleRegistration(TextHelper.trimWhiteSpace(XmlHelper.getNodeValue(element, "vehicle-registration")));
            // If the VRN changes, we need to update the HPI information
            if (oldVrn != null && oldVrn.length() > 0 && !oldVrn.equals(claimResult.getClaim().getCustomer().getVehicleRegistration())) {
                // Perform HPI check
                try {
                    HpiResponse response = Hpi.getHpiInfo(claimResult.getClaim().getCustomer().getVehicleRegistration());
                    claimResult.getClaim().getCustomer().setHpiVehicleManufacturer(response.getManufacturer());
                    claimResult.getClaim().getCustomer().setHpiVehicleModel(response.getModel());
                    claimResult.getClaim().getCustomer().setHpiVehicleYear(response.getYear());
                    claimResult.getClaim().getCustomer().setHpiVehicleCapacity(response.getCapacity());
                    claimResult.getClaim().getCustomer().setHpiVehicleDoorplan(response.getDoorPlan());
                    claimResult.getClaim().getCustomer().setHpiVehicleTransmission(response.getTransmission());
                    claimResult.getClaim().getCustomer().setHpiFirstRegistration(response.getFirstRegistration());
                    claimResult.getClaim().getCustomer().setHpiError(null);
                } catch (HpiException ex) {
                    LOG.warn("Error getting HPI info for vrn '{}': {}", claimResult.getClaim().getCustomer().getVehicleRegistration(), ex.getMessage());
                    claimResult.getClaim().getCustomer().setHpiError(ex.getMessage());
                    claimResult.getClaim().getCustomer().setHpiVehicleManufacturer(null);
                    claimResult.getClaim().getCustomer().setHpiVehicleModel(null);
                    claimResult.getClaim().getCustomer().setHpiVehicleYear(null);
                    claimResult.getClaim().getCustomer().setHpiVehicleCapacity(null);
                    claimResult.getClaim().getCustomer().setHpiVehicleDoorplan(null);
                    claimResult.getClaim().getCustomer().setHpiVehicleTransmission(null);
                    claimResult.getClaim().getCustomer().setHpiFirstRegistration(null);
                }

            }
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
