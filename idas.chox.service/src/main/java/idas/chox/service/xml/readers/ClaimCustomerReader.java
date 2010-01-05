package idas.chox.service.xml.readers;

import idas.chox.core.model.VehicleClass;
import idas.chox.core.util.TextHelper;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import org.w3c.dom.*;

public class ClaimCustomerReader extends BaseEntityReader {

    protected static String sectionName = "Customer Detail";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        boolean isAllowToReadData = false;

        Element claimElement = XMLUtils.getElement(claimResult.getElement(), "claim");
        Element element = XMLUtils.getElement(claimElement, "customer");


        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {

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
            claimResult = NodeHelper.nodeVehicleClassValidate(sectionName, "vehicle-class", element, claimResult, getDataValidationParameter(), getBordereauRederContext().getVehicleClassService());
            claimResult = NodeHelper.nodeValidate(sectionName, "location", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "damage", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "usable", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "total-loss", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "initial-ecd", element, claimResult, getDataValidationParameter());

            // CHECK VEHICLE CLASS            
            isAllowToReadData = claimResult.isCheckDataValid();

        }

        return isAllowToReadData;
    }

    @Override
    protected void process(ClaimResult claimResult) {

        Element claimElement = XMLUtils.getElement(claimResult.getElement(), "claim");
        Element element = XMLUtils.getElement(claimElement, "customer");

        if (claimResult.getClaim().getCustomer() != null) {

            String vehicleClassName = XmlHelper.getNodeValue(element, "vehicle-class");
            if (vehicleClassName != null && vehicleClassName.length() > 0) {
                VehicleClass vehicleClass = null;
                vehicleClass = getBordereauRederContext().getVehicleClassService().getVehicleClassByNodeName(element, "vehicle-class");
                claimResult.getClaim().getCustomer().setVehicleClass(vehicleClass);
            }

            claimResult.getClaim().getCustomer().setInsurerName(XmlHelper.getNodeValue(element, "name"));
            claimResult.getClaim().getCustomer().setPolicyNumber(XmlHelper.getNodeValue(element, "policy-number"));
            claimResult.getClaim().getCustomer().setClaimReference(XmlHelper.getNodeValue(element, "claim-reference"));
            claimResult.getClaim().getCustomer().setComprehensive(XmlHelper.getBooleanFromNode(element, "comprehensive"));
            claimResult.getClaim().getCustomer().setVehicleRegistration(TextHelper.trimWhiteSpace(XmlHelper.getNodeValue(element, "vehicle-registration")));
            claimResult.getClaim().getCustomer().setVehicleManufacturer(XmlHelper.getNodeValue(element, "vehicle-manufacturer"));
            claimResult.getClaim().getCustomer().setVehicleModel(XmlHelper.getNodeValue(element, "vehicle-model"));
            claimResult.getClaim().getCustomer().setIsUsable(XmlHelper.getBooleanFromNode(element, "usable"));
            claimResult.getClaim().getCustomer().setLocation(XmlHelper.getNodeValue(element, "location"));
            claimResult.getClaim().getCustomer().setDamage(XmlHelper.getNodeValue(element, "damage"));
            claimResult.getClaim().getCustomer().setInitialECD(XmlHelper.getDateFromNode(element, "initial-ecd"));
            claimResult.getClaim().getCustomer().setIsTotalLoss(XmlHelper.getBooleanFromNode(element, "total-loss"));

        }
    }

}
