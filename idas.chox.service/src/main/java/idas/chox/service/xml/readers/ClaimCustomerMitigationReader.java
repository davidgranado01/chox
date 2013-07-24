package idas.chox.service.xml.readers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import idas.chox.core.model.ClaimType;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.util.XmlHelper;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;

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
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_FIXEDFEE_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXISTS_INSURER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_HIRE_MONITORING)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_HIRE_MONITORING_AND_NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_VS_INSURER_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_COLLABORATION_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_COLLABORATION_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_SUBSCRIBER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_FIXEDFEE_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING)) {
            LOG.debug("Validating Customer Mitigation");
            claimResult.setCheckDataValid(true);

            // MITIGATION - moved to separate reader
            NodeHelper.nodeValidate(sectionName, "access-another-vehicle", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "other-vehicle", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "other-vehicle-regular-user", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "entitled-courtesy-car", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "specific-required", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "why-specific", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "type-required", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "special-requirements", element, claimResult, getDataValidationParameter());
            NodeHelper.nodeValidate(sectionName, "ave-daily-mileage", element, claimResult, getDataValidationParameter());

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
            /*  
             * For manual invoices canAccessOtherVehicle field value will be
             * mapped to isInvoiceReviewRequired and canAccessOtherVehicle field
             * should be left blank. (TO-DO-ITEM 7.1.2)
             */
            Boolean canAccessOtherVehicle = XmlHelper.getBooleanFromNode(element, "access-another-vehicle");
            if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_INVOICE) && claimResult.getClaim().getClaimType() == ClaimType.INSURER_INVOICE) {
                claimResult.getClaim().setIsInvoiceReviewRequired((canAccessOtherVehicle != null && canAccessOtherVehicle == true) ? true : false);
            }
            else {
                claimResult.getClaim().getCustomer().setCanAccessOtherVehicle(canAccessOtherVehicle);
            }
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
