package idas.chox.service.xml.readers;

import idas.chox.core.hpi.Hpi;
import idas.chox.core.hpi.HpiException;
import idas.chox.core.hpi.HpiResponse;
import idas.chox.core.model.Customer;
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
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_FIXEDFEE_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_UPLOAD)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING)) {
            LOG.debug("Validating new claim");

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
            Customer customer = claimResult.getClaim().getCustomer();
            String vehicleClassName = XmlHelper.getNodeValue(element, "vehicle-class");
            if (vehicleClassName != null && vehicleClassName.length() > 0) {
                VehicleClass vehicleClass = getBordereauReaderContext().getVehicleClassService().getVehicleClassByNodeName(element, "vehicle-class");
                claimResult.getClaim().getCustomer().setVehicleClass(vehicleClass);
            }

            customer.setInsurerName(XmlHelper.getNodeValue(element, "name"));
            customer.setPolicyNumber(XmlHelper.getNodeValue(element, "policy-number"));
            customer.setClaimReference(XmlHelper.getNodeValue(element, "claim-number"));
            customer.setComprehensive(XmlHelper.getBooleanFromNode(element, "comprehensive"));
            String oldVrn = customer.getVehicleRegistration();
            customer.setVehicleRegistration(TextHelper.trimWhiteSpace(XmlHelper.getNodeValue(element, "vehicle-registration")));
            // If the VRN changes, we need to update the HPI information
            if (oldVrn != null && oldVrn.length() > 0 && !oldVrn.equals(customer.getVehicleRegistration())) {
                // Perform HPI check
                try {
                    HpiResponse response = Hpi.getHpiInfo(customer.getVehicleRegistration());
                    customer.setHpiVehicleManufacturer(response.getManufacturer());
                    customer.setHpiVehicleModel(response.getModel());
                    customer.setHpiVehicleYear(response.getYear());
                    customer.setHpiVehicleCapacity(response.getCapacity());
                    customer.setHpiVehicleDoorplan(response.getDoorPlan());
                    customer.setHpiVehicleTransmission(response.getTransmission());
                    customer.setHpiFirstRegistration(response.getFirstRegistration());
                    customer.setHpiError(null);
                } catch (HpiException ex) {
                    LOG.warn("Error getting HPI info for vrn '{}': {}", claimResult.getClaim().getCustomer().getVehicleRegistration(), ex.getMessage());
                    customer.setHpiError(ex.getMessage());
                    customer.setHpiVehicleManufacturer(null);
                    customer.setHpiVehicleModel(null);
                    customer.setHpiVehicleYear(null);
                    customer.setHpiVehicleCapacity(null);
                    customer.setHpiVehicleDoorplan(null);
                    customer.setHpiVehicleTransmission(null);
                    customer.setHpiFirstRegistration(null);
                }

            }
            
            customer.setVehicleManufacturer(XmlHelper.getNodeValue(element, "vehicle-manufacturer"));
            customer.setVehicleModel(XmlHelper.getNodeValue(element, "vehicle-model"));
            customer.setVehicleYear(XmlHelper.getNodeValue(element, "year-of-manufacture"));
            boolean currentIsUsable = customer.getIsUsable();
            customer.setIsUsable(XmlHelper.getBooleanFromNode(element, "usable"));
            // If 'usable' status has changed and this is not a new claim (or new customer)
            // then we need to flag for repair anomaly checking
            if (currentIsUsable != customer.getIsUsable()
                    && (claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING)
                        || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE))) {
                claimResult.setCheckForRepairAnomalies(true);
            }
            customer.setLocation(XmlHelper.getNodeValue(element, "location"));
            customer.setDamage(XmlHelper.getNodeValue(element, "damage"));
            customer.setInitialECD(XmlHelper.getDateFromNode(element, "initial-ecd"));
            Boolean currentIsTotalLoss = customer.getIsTotalLoss();
            customer.setIsTotalLoss(XmlHelper.getBooleanFromNode(element, "total-loss"));
            if (customer.getIsTotalLoss() && currentIsTotalLoss != null && !currentIsTotalLoss 
                    && (claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING)
                        || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE))) {
                claimResult.setCheckForTotalLossAnomalies(true);
            }
        }
    }
}
