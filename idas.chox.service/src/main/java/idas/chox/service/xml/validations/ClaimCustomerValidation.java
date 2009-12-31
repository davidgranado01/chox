package idas.chox.service.xml.validations;

import idas.chox.data.services.SecureDataService;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.core.util.TextHelper;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;

public class ClaimCustomerValidation extends SecureDataService implements rulesInterface {

    protected static String sectionName = "Customer Detail";
    private ClaimResult claimResult;
    private DataValidationParameter dataValidationParameter;
    private ClaimService claimService;
    private VehicleClassService vehicleClassService;
    // THIS PAGE ONLY
    private Element element;

    public void setClaimResult(ClaimResult claimResult) {
        this.claimResult = claimResult;
    }

    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) {
        this.dataValidationParameter = dataValidationParameter;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public ClaimCustomerValidation(
            ClaimResult claimResult,
            DataValidationParameter dataValidationParameter,
            ClaimService claimService,
            VehicleClassService vehicleClassService) {

        setClaimResult(claimResult);
        setDataValidationParameter(dataValidationParameter);
        setClaimService(claimService);
        setVehicleClassService(vehicleClassService);
    }

    public ClaimResult execute() throws DOMException, XPathExpressionException, Exception {

        this.element = XMLUtils.getElement(XMLUtils.getElement(claimResult.getElement(), "claim"), "customer");

        if (validate()) {
            process();
        }

        doPrintResult(false);
        return claimResult;
    }

    private boolean validate() throws Exception {

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {

            isAllowToReadData = true;
            this.claimResult.setCheckDataValid(true);

            // INSURER            
            this.claimResult = NodeHelper.nodeValidate(sectionName, "name", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "policy-number", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "claim-reference", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "comprehensive", this.element, this.claimResult, dataValidationParameter);

            // VEHICLE
            this.claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-registration", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-manufacturer", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-model", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeVehicleClassValidate(sectionName, "vehicle-class", this.element, this.claimResult, dataValidationParameter, vehicleClassService);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "location", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "damage", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "usable", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "total-loss", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "initial-ecd", this.element, this.claimResult, dataValidationParameter);

            // CHECK VEHICLE CLASS            
            isAllowToReadData = this.claimResult.isCheckDataValid();

        }

        return isAllowToReadData;
    }

    private void process() {

        if (this.claimResult.getClaim().getCustomer() != null) {

            String vehicleClassName = XmlHelper.getNodeValue(this.element, "vehicle-class");
            if (vehicleClassName != null && vehicleClassName.length() > 0) {
                VehicleClass vehicleClass = null;
                vehicleClass = vehicleClassService.getVehicleClassByNodeName(this.element, "vehicle-class");
                this.claimResult.getClaim().getCustomer().setVehicleClass(vehicleClass);
            }

            this.claimResult.getClaim().getCustomer().setInsurerName(XmlHelper.getNodeValue(this.element, "name"));
            this.claimResult.getClaim().getCustomer().setPolicyNumber(XmlHelper.getNodeValue(this.element, "policy-number"));
            this.claimResult.getClaim().getCustomer().setClaimReference(XmlHelper.getNodeValue(this.element, "claim-reference"));
            this.claimResult.getClaim().getCustomer().setComprehensive(XmlHelper.getBooleanFromNode(this.element, "comprehensive"));
            this.claimResult.getClaim().getCustomer().setVehicleRegistration(TextHelper.trimWhiteSpace(XmlHelper.getNodeValue(this.element, "vehicle-registration")));
            this.claimResult.getClaim().getCustomer().setVehicleManufacturer(XmlHelper.getNodeValue(this.element, "vehicle-manufacturer"));
            this.claimResult.getClaim().getCustomer().setVehicleModel(XmlHelper.getNodeValue(this.element, "vehicle-model"));
            this.claimResult.getClaim().getCustomer().setIsUsable(XmlHelper.getBooleanFromNode(this.element, "usable"));
            this.claimResult.getClaim().getCustomer().setLocation(XmlHelper.getNodeValue(this.element, "location"));
            this.claimResult.getClaim().getCustomer().setDamage(XmlHelper.getNodeValue(this.element, "damage"));
            this.claimResult.getClaim().getCustomer().setInitialECD(XmlHelper.getTimeStampFromNode(this.element, "initial-ecd"));
            this.claimResult.getClaim().getCustomer().setIsTotalLoss(XmlHelper.getBooleanFromNode(this.element, "total-loss"));

        }
    }

    private void doPrintResult(boolean isAllowed) {

        if (isAllowed) {

            System.out.println("-------");
            System.out.println(sectionName + "|Status :" + this.claimResult.isDataValid());

            if (this.claimResult.getClaim().getCustomer() != null) {

                System.out.println(sectionName + "| getInsurerName :" + this.claimResult.getClaim().getCustomer().getInsurerName());
                System.out.println(sectionName + "| getPolicyNumber :" + this.claimResult.getClaim().getCustomer().getPolicyNumber());
                System.out.println(sectionName + "| getClaimReference :" + this.claimResult.getClaim().getCustomer().getClaimReference());
                System.out.println(sectionName + "| getComprehensive :" + this.claimResult.getClaim().getCustomer().isComprehensive());
                System.out.println(sectionName + "| getVehicleRegistration :" + this.claimResult.getClaim().getCustomer().getVehicleRegistration());
                System.out.println(sectionName + "| getVehicleManufacturer :" + this.claimResult.getClaim().getCustomer().getVehicleManufacturer());
                System.out.println(sectionName + "| getVehicleModel :" + this.claimResult.getClaim().getCustomer().getVehicleModel());
                System.out.println(sectionName + "| getVehicleClass().Name :" + this.claimResult.getClaim().getCustomer().getVehicleClass());
                System.out.println(sectionName + "| getIsUsable :" + this.claimResult.getClaim().getCustomer().getIsUsable());
                System.out.println(sectionName + "| getLocation :" + this.claimResult.getClaim().getCustomer().getLocation());
                System.out.println(sectionName + "| getDamage :" + this.claimResult.getClaim().getCustomer().getDamage());
                System.out.println(sectionName + "| getInitialECD :" + this.claimResult.getClaim().getCustomer().getInitialECD());
                System.out.println(sectionName + "| getIsTotalLoss :" + this.claimResult.getClaim().getCustomer().getIsTotalLoss());
            } else {
                System.out.println(sectionName + "| NO CUSTOMER OBJECT HAVE FOUND!!");
            }
        }

    }
}
