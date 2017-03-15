package idas.chox.service.xml.readers;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import idas.chox.core.hpi.Hpi;
import idas.chox.core.hpi.HpiException;
import idas.chox.core.hpi.HpiResponse;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.services.VehicleClassService;
import idas.chox.core.util.TextHelper;
import idas.chox.core.util.XmlHelper;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;

public class ClaimVehicleHireReader extends BaseEntityReader {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimVehicleHireReader.class);

    protected static String sectionName = "Vehicle Hire Details";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        Element rootElement = claimResult.getElement();
        Element element = XMLUtils.getElement(rootElement, "rental-vehicle");

        VehicleClassService vehicleClassService = this.getBordereauReaderContext().getVehicleClassService();

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_VS_INSURER_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXISTS_INSURER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_HIRE_MONITORING)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_HIRE_MONITORING_AND_NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUBSCRIBER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_FIXEDFEE_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_COLLABORATION_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_COLLABORATION_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_SUBSCRIBER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_FIXEDFEE_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_NEW_SUPPLEMENTARY_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUPPLEMENTARY_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING)) {

            claimResult.setCheckDataValid(true);

            NodeHelper.nodeValidate(sectionName, "vehicle-registration", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "vehicle-manufacturer", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "vehicle-model", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeVehicleClassValidate(sectionName, "vehicle-class", element, claimResult, getDataValidationParameter(), vehicleClassService);
            NodeHelper.nodeValidate(sectionName, "rental-start", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "rental-end", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "collection-reason", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "rental-days", element, claimResult, getDataValidationParameter());

            isAllowToReadData = claimResult.isCheckDataValid();
        } 

        return isAllowToReadData;
    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {
        boolean isNewVehicleHire = false;
        boolean isNewClaim = false;
        
        Element rootElement = claimResult.getElement();
        Element element = XMLUtils.getElement(rootElement, "rental-vehicle");
        VehicleClassService vehicleClassService = this.getBordereauReaderContext().getVehicleClassService();

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "vehicle-registration")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "vehicle-manufacturer")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "vehicle-model")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "vehicle-class")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "rental-start")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "rental-end")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "rental-days")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "collection-reason"))) {

            if (claimResult.getClaim().getVehicleHire() == null) {
                claimResult.getClaim().setVehicleHire(new VehicleHire());
                isNewVehicleHire = true;
            }

            if (claimResult.getClaimParseStatus() == ClaimParseStatus.NEW_CLAIM
                        || claimResult.getClaimParseStatus() == ClaimParseStatus.NEW_SUBSCRIBER_CLAIM
                        || claimResult.getClaimParseStatus() == ClaimParseStatus.NEW_FIXEDFEE_CLAIM
                        || claimResult.getClaimParseStatus() == ClaimParseStatus.NEW_COLLABORATION_CLAIM
                        || claimResult.getClaimParseStatus() == ClaimParseStatus.TPI_INTERVENTION
                        || claimResult.getClaimParseStatus() == ClaimParseStatus.INSURER_CLAIM
                        || claimResult.getClaimParseStatus() == ClaimParseStatus.NEW_SUPPLEMENTARY_INVOICE) {
                isNewClaim = true;
            }

            String vehicleClassName = XmlHelper.getNodeValue(element, "vehicle-class");
            if (vehicleClassName != null && vehicleClassName.length() > 0) {
                VehicleClass vehicleClass;
                vehicleClass = vehicleClassService.getVehicleClassByNodeName(element, "vehicle-class");
                claimResult.getClaim().getVehicleHire().setVehicleClass(vehicleClass);
            }

            String oldVrn = claimResult.getClaim().getVehicleHire().getVehicleRegistration();
            claimResult.getClaim().getVehicleHire().setVehicleRegistration(TextHelper.trimWhiteSpace(XmlHelper.getNodeValue(element, "vehicle-registration")));

            // If the VRN changes, we need to update the HPI information
            if (oldVrn != null && oldVrn.length() > 0 && !oldVrn.equals(claimResult.getClaim().getVehicleHire().getVehicleRegistration())) {
                // Perform HPI check
                try {
                    HpiResponse response = Hpi.getHpiInfo(claimResult.getClaim().getVehicleHire().getVehicleRegistration());
                    claimResult.getClaim().getVehicleHire().setHpiVehicleManufacturer(response.getManufacturer());
                    claimResult.getClaim().getVehicleHire().setHpiVehicleModel(response.getModel());
                    claimResult.getClaim().getVehicleHire().setHpiVehicleYear(response.getYear());
                    claimResult.getClaim().getVehicleHire().setHpiVehicleCapacity(response.getCapacity());
                    claimResult.getClaim().getVehicleHire().setHpiVehicleDoorplan(response.getDoorPlan());
                    claimResult.getClaim().getVehicleHire().setHpiVehicleTransmission(response.getTransmission());
                    claimResult.getClaim().getVehicleHire().setHpiFirstRegistration(response.getFirstRegistration());
                    claimResult.getClaim().getVehicleHire().setHpiError(null);
                } catch (HpiException ex) {
                    LOG.warn("Error getting HPI info for vrn '{}': {}", claimResult.getClaim().getCustomer().getVehicleRegistration(), ex.getMessage());
                    claimResult.getClaim().getVehicleHire().setHpiError(ex.getMessage());
                    claimResult.getClaim().getVehicleHire().setHpiVehicleManufacturer(null);
                    claimResult.getClaim().getVehicleHire().setHpiVehicleModel(null);
                    claimResult.getClaim().getVehicleHire().setHpiVehicleYear(null);
                    claimResult.getClaim().getVehicleHire().setHpiVehicleCapacity(null);
                    claimResult.getClaim().getVehicleHire().setHpiVehicleDoorplan(null);
                    claimResult.getClaim().getVehicleHire().setHpiVehicleTransmission(null);
                    claimResult.getClaim().getVehicleHire().setHpiFirstRegistration(null);
                }
            }

            claimResult.getClaim().getVehicleHire().setVehicleManufacturer(XmlHelper.getNodeValue(element, "vehicle-manufacturer"));
            claimResult.getClaim().getVehicleHire().setVehicleModel(XmlHelper.getNodeValue(element, "vehicle-model"));
            Date rentalStart = XmlHelper.getDateFromNode(element, "rental-start");
            LOG.debug("isNewVehicleHire={}, isNewClaim={}, rentalStart={}", new Object[]{isNewVehicleHire, isNewClaim, rentalStart});

            if ((isNewVehicleHire && !isNewClaim && rentalStart != null) ||
                    (!isNewVehicleHire && !isNewClaim && claimResult.getClaim().getVehicleHire().getRentalStart() == null
                        && rentalStart != null)) {
                LOG.debug("Raise on hire task? Setting flag in claim result");
                claimResult.setCheckForOnHireTask(true);
            }

            claimResult.getClaim().getVehicleHire().setRentalStart(rentalStart);
            claimResult.getClaim().getVehicleHire().setRentalEnd(XmlHelper.getDateFromNode(element, "rental-end"));

            if ( ClaimType.isTPI(claimResult.getClaim().getClaimType())
                        && claimResult.getClaim().getCustomer() != null
                        && claimResult.getClaim().getCustomer().getCourtesyCarEntitled() != null) {
                claimResult.getClaim().getVehicleHire().setCourtesyCarProvided(claimResult.getClaim().getCustomer().getCourtesyCarEntitled());
            } else {
                claimResult.getClaim().getVehicleHire().setCourtesyCarProvided(false);
            }

            int rentalDays = 0;

            if (XmlHelper.getIntegerFromNode(element, "rental-days") != null) {
                rentalDays = XmlHelper.getIntegerFromNode(element, "rental-days");
            }

            claimResult.getClaim().getVehicleHire().setDays(rentalDays);
            claimResult.getClaim().getVehicleHire().setCollectionReason(XmlHelper.getNodeValue(element, "collection-reason"));
        }
    }
}
