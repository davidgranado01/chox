package idas.chox.service.xml.readers;

import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import org.w3c.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClaimCustomerMitigationReader extends BaseEntityReader {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimCustomerMitigationReader.class);

    protected static String sectionName = "Customer Mitigation Detail";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        boolean isAllowToReadData = false;

        Element claimElement = XMLUtils.getElement(claimResult.getElement(), "claim");
        Element customerElement = XMLUtils.getElement(claimElement, "customer");
        Element element = XMLUtils.getElement(customerElement, "mitigation");

        LOG.debug("Validating Claim Customer Mitigation");

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUBSCRIBER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_VS_INSURER_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_SUBSCRIBER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_UPLOAD)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING)) {
            LOG.debug("Validating Customer Mitigation");
            isAllowToReadData = true;
            claimResult.setCheckDataValid(true);

            // MITIGATION - moved to separate reader
            claimResult = NodeHelper.nodeValidate(sectionName, "access-another-vehicle", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "other-vehicle", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "other-vehicle-regular-user", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "entitled-courtesy-car", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "specific-required", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "why-specific", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "type-required", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "special-requirements", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "ave-daily-mileage", element, claimResult, getDataValidationParameter());

            // CHECK VEHICLE CLASS            
            isAllowToReadData = claimResult.isCheckDataValid();

        }

        LOG.debug("Validating Claim Customer Mitigation: returning {}", isAllowToReadData);

        return isAllowToReadData;
    }

    @Override
    protected void process(ClaimResult claimResult) {

        Element claimElement = XMLUtils.getElement(claimResult.getElement(), "claim");
        Element customerElement = XMLUtils.getElement(claimElement, "customer");
        Element element = XMLUtils.getElement(customerElement, "mitigation");
        LOG.debug("Processing claim customer mitigation element...");
        if (claimResult.getClaim().getCustomer() != null) {
            LOG.debug("Setting CanAccessOtherVehicle...");
            claimResult.getClaim().getCustomer().setCanAccessOtherVehicle(XmlHelper.getBooleanFromNode(element, "access-another-vehicle"));
            LOG.debug("Setting OtherVehicle...");
            claimResult.getClaim().getCustomer().setOtherVehicle(XmlHelper.getNodeValue(element, "other-vehicle"));
            LOG.debug("Setting OtherVehicleUsed...");
            claimResult.getClaim().getCustomer().setOtherVehicleUsed(XmlHelper.getBooleanFromNode(element, "other-vehicle-regular-user"));
            LOG.debug("Setting CourtesyCarEntitled...");
            claimResult.getClaim().getCustomer().setCourtesyCarEntitled(XmlHelper.getBooleanFromNode(element, "entitled-courtesy-car"));
            LOG.debug("Setting SpecificVehicleRequired...");
            claimResult.getClaim().getCustomer().setSpecificVehicleRequired(XmlHelper.getBooleanFromNode(element, "specific-required"));
            LOG.debug("Setting SpecificVehicleReason...");
            claimResult.getClaim().getCustomer().setSpecificVehicleReason(XmlHelper.getNodeValue(element, "why-specific"));
            LOG.debug("Setting TypeVehicleRequired...");
            claimResult.getClaim().getCustomer().setTypeVehicleRequired(XmlHelper.getNodeValue(element, "type-required"));
            LOG.debug("Setting SpecialRequirements...");
            claimResult.getClaim().getCustomer().setSpecialRequirements(XmlHelper.getNodeValue(element, "special-requirements"));
            LOG.debug("Setting AverageDailyMileage...");
            claimResult.getClaim().getCustomer().setAverageDailyMileage(XmlHelper.getNodeValue(element, "ave-daily-mileage"));
            LOG.debug("AverageDailyMileage set to: {}", XmlHelper.getNodeValue(element, "ave-daily-mileage"));
        }
    }

}
