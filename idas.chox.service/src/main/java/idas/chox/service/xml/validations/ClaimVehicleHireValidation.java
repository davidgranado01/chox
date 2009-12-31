package idas.chox.service.xml.validations;

import idas.chox.data.services.SecureDataService;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.services.VehicleClassService;
import idas.chox.core.util.TextHelper;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;

public class ClaimVehicleHireValidation extends SecureDataService implements rulesInterface {

    protected static String sectionName = "Vehicle Hire Details";
    private DataValidationParameter dataValidationParameter;
    private ClaimResult claimResult;
    private VehicleClassService vehicleClassService;
    private Element element;

    public void setClaimResult(ClaimResult claimResult) {
        this.claimResult = claimResult;
    }

    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) {
        this.dataValidationParameter = dataValidationParameter;
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public ClaimVehicleHireValidation(
            ClaimResult claimResult,
            DataValidationParameter dataValidationParameter,
            VehicleClassService vehicleClassService) {

        setClaimResult(claimResult);
        setDataValidationParameter(dataValidationParameter);
        setVehicleClassService(vehicleClassService);
    }

    public ClaimResult execute() throws DOMException, XPathExpressionException, Exception {

        this.element = XMLUtils.getElement(claimResult.getElement(), "rental-vehicle");

        if (validate()) {
            process();
        }

        doPrintResult(false);
        return claimResult;
    }

    private boolean validate() throws DOMException, XPathExpressionException, Exception {

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)) {

            isAllowToReadData = true;
            this.claimResult.setCheckDataValid(true);

            this.claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-registration", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-manufacturer", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-model", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeVehicleClassValidate(sectionName, "vehicle-class", this.element, this.claimResult, this.dataValidationParameter, this.vehicleClassService);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "rental-start", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "rental-end", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "collection-reason", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "rental-days", this.element, this.claimResult, this.dataValidationParameter);

            isAllowToReadData = this.claimResult.isCheckDataValid();

        } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim) || claimResult.getClaimParseStatus().equals(ClaimParseStatus.existClaim)) {

            isAllowToReadData = true;
            this.claimResult.setCheckDataValid(true);

            this.claimResult = NodeHelper.nodeValidateDefaultMandatoryValue(sectionName, "vehicle-registration", this.element, this.claimResult, this.dataValidationParameter, false);
            this.claimResult = NodeHelper.nodeValidateDefaultMandatoryValue(sectionName, "vehicle-manufacturer", this.element, this.claimResult, this.dataValidationParameter, false);
            this.claimResult = NodeHelper.nodeValidateDefaultMandatoryValue(sectionName, "vehicle-model", this.element, this.claimResult, this.dataValidationParameter, false);
            // this.claimResult = NodeHelper.nodeVehicleClassValidate(sectionName, "vehicle-class", this.element, this.claimResult, this.dataValidationParameter, vehicleClassService);
            this.claimResult = NodeHelper.nodeVehicleClassValidateDefaultMandatoryValue(sectionName, "vehicle-class", this.element, this.claimResult, this.dataValidationParameter, vehicleClassService, false);
            this.claimResult = NodeHelper.nodeValidateDefaultMandatoryValue(sectionName, "rental-start", this.element, this.claimResult, this.dataValidationParameter, false);
            this.claimResult = NodeHelper.nodeValidateDefaultMandatoryValue(sectionName, "rental-end", this.element, this.claimResult, this.dataValidationParameter, false);
            this.claimResult = NodeHelper.nodeValidateDefaultMandatoryValue(sectionName, "collection-reason", this.element, this.claimResult, this.dataValidationParameter, false);
            this.claimResult = NodeHelper.nodeValidateDefaultMandatoryValue(sectionName, "rental-days", this.element, this.claimResult, this.dataValidationParameter, false);

            isAllowToReadData = this.claimResult.isCheckDataValid();

        }

        return isAllowToReadData;
    }

    private void process() throws DOMException, XPathExpressionException {

        if (XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "vehicle-registration")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "vehicle-manufacturer")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "vehicle-model")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "vehicle-class")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "rental-start")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "rental-end")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "rental-days")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "collection-reason"))) {

            if (this.claimResult.getClaim().getVehicleHire() == null) {
                this.claimResult.getClaim().setVehicleHire(new VehicleHire());
            }

            String vehicleClassName = XmlHelper.getNodeValue(this.element, "vehicle-class");
            if (vehicleClassName != null && vehicleClassName.length() > 0) {
                VehicleClass vehicleClass = null;
                vehicleClass = vehicleClassService.getVehicleClassByNodeName(this.element, "vehicle-class");
                this.claimResult.getClaim().getVehicleHire().setVehicleClass(vehicleClass);
            }

            this.claimResult.getClaim().getVehicleHire().setVehicleRegistration(TextHelper.trimWhiteSpace(XmlHelper.getNodeValue(this.element, "vehicle-registration")));
            this.claimResult.getClaim().getVehicleHire().setVehicleManufacturer(XmlHelper.getNodeValue(this.element, "vehicle-manufacturer"));
            this.claimResult.getClaim().getVehicleHire().setVehicleModel(XmlHelper.getNodeValue(this.element, "vehicle-model"));
            this.claimResult.getClaim().getVehicleHire().setRentalStart(XmlHelper.getTimeStampFromNode(this.element, "rental-start"));
            this.claimResult.getClaim().getVehicleHire().setRentalEnd(XmlHelper.getTimeStampFromNode(this.element, "rental-end"));

            int rentalDays = 0;

            if (XmlHelper.getIntegerFromNode(this.element, "rental-days") != null) {
                rentalDays = XmlHelper.getIntegerFromNode(this.element, "rental-days").intValue();
            }

            // this.claimResult.getClaim().getVehicleHire().setDays(XmlHelper.getIntegerFromNode(this.element, "rental-days"));
            this.claimResult.getClaim().getVehicleHire().setDays(rentalDays);
            this.claimResult.getClaim().getVehicleHire().setCollectionReason(XmlHelper.getNodeValue(this.element, "collection-reason"));

            // ONLY CHECK VEHICLE CLASS DETAIL WHEN IT IS NEW INVOICE
            /*
            if(claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice)){
            ClaimVehicleHireExtraValidation vehicleHireExtraVal = new ClaimVehicleHireExtraValidation(claimResult, dataValidationParameter, this.element);
            this.claimResult = vehicleHireExtraVal.execute();
            }
             */
        }
    }

    private void doPrintResult(boolean isAllowed) {

        if (isAllowed) {

            System.out.println("-------");
            System.out.println(sectionName + "| Status :" + this.claimResult.isDataValid());

            if (this.claimResult.getClaim().getVehicleHire() != null) {

                System.out.println(sectionName + "| getVehicleRegistration :" + this.claimResult.getClaim().getVehicleHire().getVehicleRegistration());
                System.out.println(sectionName + "| getVehicleManufacturer :" + this.claimResult.getClaim().getVehicleHire().getVehicleManufacturer());
                System.out.println(sectionName + "| getVehicleModel :" + this.claimResult.getClaim().getVehicleHire().getVehicleModel());
                System.out.println(sectionName + "| getRentalStart :" + this.claimResult.getClaim().getVehicleHire().getRentalStart());
                System.out.println(sectionName + "| getRentalEnd :" + this.claimResult.getClaim().getVehicleHire().getRentalEnd());
                System.out.println(sectionName + "| getDays :" + this.claimResult.getClaim().getVehicleHire().getDays());
                System.out.println(sectionName + "| getCollectionReason :" + this.claimResult.getClaim().getVehicleHire().getCollectionReason());
                System.out.println(sectionName + "| isCdwFee :" + this.claimResult.getClaim().getVehicleHire().isCdwFee());
                System.out.println(sectionName + "| isAdminFee :" + this.claimResult.getClaim().getVehicleHire().isAdminFee());
                System.out.println(sectionName + "| isAutomaticFee :" + this.claimResult.getClaim().getVehicleHire().isAutomaticFee());
                System.out.println(sectionName + "| isBabySeatFee :" + this.claimResult.getClaim().getVehicleHire().isBabySeatFee());
                System.out.println(sectionName + "| isDeliveryCollectionFee :" + this.claimResult.getClaim().getVehicleHire().isDeliveryCollectionFee());
                System.out.println(sectionName + "| isDualControlFee :" + this.claimResult.getClaim().getVehicleHire().isDualControlFee());
                System.out.println(sectionName + "| isEstateFee :" + this.claimResult.getClaim().getVehicleHire().isEstateFee());
                System.out.println(sectionName + "| isNonStandardInsurancePremiumFee :" + this.claimResult.getClaim().getVehicleHire().isNonStandardInsurancePremiumFee());
                System.out.println(sectionName + "| isRoofRackFee :" + this.claimResult.getClaim().getVehicleHire().isRoofRackFee());
                System.out.println(sectionName + "| isSatNavFee :" + this.claimResult.getClaim().getVehicleHire().isSatNavFee());
                System.out.println(sectionName + "| isTowBarsFee :" + this.claimResult.getClaim().getVehicleHire().isTowBarsFee());

            } else {
                System.out.println(sectionName + "| NO VEHICLE HIRE OBJECT HAVE FOUND!!");
            }
        }
    }
}
