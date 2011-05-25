package idas.chox.service.xml.readers;

import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.services.VehicleClassService;
import idas.chox.core.util.TextHelper;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import org.w3c.dom.*;

public class ClaimVehicleHireReader extends BaseEntityReader {

    protected static String sectionName = "Vehicle Hire Details";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        Element rootElement = claimResult.getElement();
        Element element = XMLUtils.getElement(rootElement, "rental-vehicle");

        VehicleClassService vehicleClassService = this.getBordereauRederContext().getVehicleClassService();

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice) 
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.tpiIntervention)
                ||claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.existClaim)) {

            isAllowToReadData = true;
            claimResult.setCheckDataValid(true);

            claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-registration", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-manufacturer", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-model", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeVehicleClassValidate(sectionName, "vehicle-class", element, claimResult, getDataValidationParameter(), vehicleClassService);
            claimResult = NodeHelper.nodeValidate(sectionName, "rental-start", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "rental-end", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "collection-reason", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "rental-days", element, claimResult, getDataValidationParameter());

            isAllowToReadData = claimResult.isCheckDataValid();
        } 

        return isAllowToReadData;
    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {

        Element rootElement = claimResult.getElement();
        Element element = XMLUtils.getElement(rootElement, "rental-vehicle");
        VehicleClassService vehicleClassService = this.getBordereauRederContext().getVehicleClassService();

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "vehicle-registration")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "vehicle-manufacturer")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "vehicle-model")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "vehicle-class")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "rental-start")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "rental-end")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "rental-days")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(element, "collection-reason"))) {

            if (claimResult.getClaim().getVehicleHire() == null) {
                claimResult.getClaim().setVehicleHire(new VehicleHire());
            }

            String vehicleClassName = XmlHelper.getNodeValue(element, "vehicle-class");
            if (vehicleClassName != null && vehicleClassName.length() > 0) {
                VehicleClass vehicleClass = null;
                vehicleClass = vehicleClassService.getVehicleClassByNodeName(element, "vehicle-class");
                claimResult.getClaim().getVehicleHire().setVehicleClass(vehicleClass);
            }

            claimResult.getClaim().getVehicleHire().setVehicleRegistration(TextHelper.trimWhiteSpace(XmlHelper.getNodeValue(element, "vehicle-registration")));
            claimResult.getClaim().getVehicleHire().setVehicleManufacturer(XmlHelper.getNodeValue(element, "vehicle-manufacturer"));
            claimResult.getClaim().getVehicleHire().setVehicleModel(XmlHelper.getNodeValue(element, "vehicle-model"));
            claimResult.getClaim().getVehicleHire().setRentalStart(XmlHelper.getDateFromNode(element, "rental-start"));
            claimResult.getClaim().getVehicleHire().setRentalEnd(XmlHelper.getDateFromNode(element, "rental-end"));

            if (claimResult.getClaim().isTpiClaim() && claimResult.getClaim().getCustomer() != null) {
                claimResult.getClaim().getVehicleHire().setCourtesyCarProvided(claimResult.getClaim().getCustomer().getCourtesyCarEntitled());
            }
            else
                claimResult.getClaim().getVehicleHire().setCourtesyCarProvided(false);

            int rentalDays = 0;

            if (XmlHelper.getIntegerFromNode(element, "rental-days") != null) {
                rentalDays = XmlHelper.getIntegerFromNode(element, "rental-days").intValue();
            }

            claimResult.getClaim().getVehicleHire().setDays(rentalDays);
            claimResult.getClaim().getVehicleHire().setCollectionReason(XmlHelper.getNodeValue(element, "collection-reason"));

        }
    }
}
