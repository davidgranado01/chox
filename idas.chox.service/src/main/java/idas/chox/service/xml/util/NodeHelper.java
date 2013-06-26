package idas.chox.service.xml.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.w3c.dom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.InsurerAlias;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.ChorganisationAliasService;
import idas.chox.core.services.InsurerAliasService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.core.util.TextHelper;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.xmlValidation.NodeRuleModel;
import idas.chox.core.model.ChorganisationAlias;
import idas.chox.service.xml.validations.DataValidationParameter;

public final class NodeHelper {

    private static final Logger LOG = LoggerFactory.getLogger(NodeHelper.class);
    private static String mandatoryDataErrorMsg = "No '%s' information supplied for '%s'. Please re-submit with this information.";
    private static String INCORRECT_DATA_LENGTH_ERROR_MSG = "Length for '%s' field is bigger than allowed limit of '%s' characters. Please amend and re-submit.";
    private static String IncorrectDataErrorMsg = "Invalid or incorrect character in '%s' for '%s'.";
    private static String mandatoryVehicleClassDataErrorMsg = "Selected Vehicle Class is invalid for '%s'";
    private static String IncorrectInsurerAlias = "Selected '%s' for 'Third Party Insurer' is invalid; this alias does not exist";
    private static String IncorrectChorganisationAlias = "Selected '%s' for 'Supplier Name' is invalid, this alias does not exist";
    public static final String REG_TIMESTAMP = "^\\d{4}-(0[0-9]|1[0,1,2])-([0-9]|[0,1,2][0-9]|3[0,1])[T]([0-9]{2}):([0-9]{2}):([0-9]{2})$";
    public static final String REG_DATETIME = "^(([0-9]|[0,1,2][0-9]|3[0,1])/(0[0-9]|1[0,1,2])/\\d{4}.*)|(\\d{4}-(0[0-9]|1[0,1,2])-([0-9]|[0,1,2][0-9]|3[0,1])[T]([0-9]{2}):([0-9]{2}):([0-9]{2})$)";
    public static final String REG_BOOLEAN = "^[ynYN]";
    public static final String REG_INTEGER = "^[0-9]+$";
    public static final String REG_BIGDECIMAL = "^\\-?(\\d+)*\\.?\\d*$";

    private NodeHelper() {
    }

    ;

    private static String getNodeRuleName(String sectionName, String nodeName) {
        return TextHelper.trimWhiteSpace(sectionName.toLowerCase() + "-" + nodeName);
    }

    private static NodeRuleModel getNodeRule(String sectionName, String nodeName,
            DataValidationParameter dataValidationParameter) {
        String NodeRuleName = getNodeRuleName(sectionName, nodeName);
        return dataValidationParameter.getValidationElementByField(NodeRuleName);
    }

    public static ClaimResult nodeChorganisationAliasValidate(
            String sectionName,
            String nodeName,
            Element element,
            ClaimResult claimResult,
            DataValidationParameter dataValidationParameter,
            ChorganisationAliasService chorganisationAliasService,
            InsurerChorganisationService insurerChorganisationService,
            Integer insurerId) {

        boolean isValid = true;
        NodeRuleModel val = getNodeRule(sectionName, nodeName, dataValidationParameter);
        String value = XMLUtils.getElementValue(element, nodeName);
        // CHECK MANDATORY - VALUE IN XML IS EMPTY
        if (value == null || value.trim().isEmpty()) {
            isValid = false;
            claimResult.getMessage().add(String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
            LOG.debug("Mandatory data error: '{}'", String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
        } else {

            ChorganisationAlias alias = chorganisationAliasService.getChorganisationByAliasName(value);

            if (alias != null) {

                if (alias.getChorganisation() != null) {

                    if (!insurerChorganisationService.isMapped(insurerId, alias.getChorganisation().getId())) {
                        isValid = false;
                        claimResult.getMessage().add(String.format(IncorrectChorganisationAlias, value));
                    }

                } else {
                    isValid = false;
                    claimResult.getMessage().add(String.format(IncorrectChorganisationAlias, value));
                }

            } else {
                isValid = false;
                claimResult.getMessage().add(String.format(IncorrectChorganisationAlias, value));
            }

        }

        setStatus(claimResult, isValid);

        return claimResult;
    }

    public static ClaimResult nodeInsurerAliasValidate(
            String sectionName,
            String nodeName,
            Element element,
            ClaimResult claimResult,
            DataValidationParameter dataValidationParameter,
            InsurerAliasService insurerAliasService,
            InsurerChorganisationService insurerChorganisationService) {

        boolean isValid = true;
        NodeRuleModel val = getNodeRule(sectionName, nodeName, dataValidationParameter);
        String value = XMLUtils.getElementValue(element, nodeName);

        // CHECK MANDATORY - VALUE IN XML IS EMPTY
        if (value.trim().isEmpty()) {
            isValid = false;
            claimResult.getMessage().add(String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
            LOG.debug("Mandatory data error: '{}'", String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
        } else {

            InsurerAlias alias = insurerAliasService.getInsurerByAliasName(value);

            if (alias != null) {

                if (alias.getInsurer() != null) {

                    if (!insurerChorganisationService.isMapped(alias.getInsurer().getId(),
                            claimResult.getClaim().getChorganisation().getId())) {
                        isValid = false;
                        claimResult.getMessage().add(String.format(IncorrectInsurerAlias, value));
                    }

                } else {
                    isValid = false;
                    claimResult.getMessage().add(String.format(IncorrectInsurerAlias, value));
                }

            } else {
                isValid = false;
                claimResult.getMessage().add(String.format(IncorrectInsurerAlias, value));
            }

        }

        setStatus(claimResult, isValid);

        return claimResult;

    }

    public static boolean isDataMandatory(ClaimResult claimResult, NodeRuleModel value) {

        LOG.debug("checking inside isDataMandatory method...");
        if (claimResult.getClaimParseStatus() != null) {
            LOG.debug("        parse status: '{}'", claimResult.getClaimParseStatus());
            if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_CLAIM) && value.isNewClaimDataMandatory()) {
                return true;
            } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUBSCRIBER_CLAIM)
                    && value.isNewSubscriberClaimDataMandatory()) {
                return true;
            } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_FIXEDFEE_CLAIM)
                    && value.isNewFixedFeeClaimDataMandatory()) {
                return true;
            } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_CLAIM)
                    && value.isExistingClaimDataMandatory()) {
                return true;
            } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_SUBSCRIBER_CLAIM)
                    && value.isExistingSubscriberClaimDataMandatory()) {
                return true;
            } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_FIXEDFEE_CLAIM)
                    && value.isExistingFixedFeeClaimDataMandatory()) {
                return true;
            } else if ((claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_INVOICE)
                    || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_VS_INSURER_INVOICE))
                    && value.isNewInvoiceDataMandatory()) {
                return true;
            } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXIST_INVOICE)
                    && value.isExistingInvoiceDataMandatory()) {
                return true;
            } else if ((claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_INVOICE)
                    || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_EXIST_INVOICE)
                    || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_NEW_SUPPLEMENTARY_INVOICE)
                    || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_HIRE_MONITORING_AND_NEW_INVOICE))
                    && value.isInsurerInvoiceDataMandatory()) {
                return true;
            } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION)
                    && value.isTpiInterventionDataMandatory()) {
                return true;
            } else if ((claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_CLAIM)
                    || claimResult.getClaimParseStatus().equals(ClaimParseStatus.EXISTS_INSURER_CLAIM)
                    || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_HIRE_MONITORING))
                    && value.isInsurerClaimDataMandatory()) {
                LOG.debug("insurerClaimDataMandatory: {} - '{}'", value.getNodeName(), value.getNodeDesc());
                return true;
            } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING_AND_NEW_INVOICE)
                    && value.isOffHiredDataMandatory()) {
                return true;
            } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.HIRE_MONITORING)
                    && value.isHireMonitoringDataMandatory()) {
                return true;
            } else if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUPPLEMENTARY_INVOICE)
                    && value.isNewSupplementaryInvoiceMandatory()) {
                return true;
            }
            return false;
        } else {

            if (value.isNewClaimDataMandatory()) {
                LOG.debug("New Claim isDataMandatory is true ");
                return true;
            } else {
                LOG.debug("New Claim isDataMandatory is false ");
                return false;
            }

        }

    }

    private static ClaimResult coreNodevalidation(NodeRuleModel val, ClaimResult claimResult, String value,
            String sectionName, String nodeName) throws Exception {
        boolean isValid = true;
        LOG.debug("coreNodevalidation: validating value='{}' with NodeRuleModel='{}' in section '{}'",
                new Object[]{value, val, sectionName});

        if (isDataMandatory(claimResult, val) && value.trim().equalsIgnoreCase("")) {
            isValid = false;
            claimResult.getMessage().add(String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
            LOG.debug("Mandatory data error: '{}'", String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
        }

        if (!value.trim().equalsIgnoreCase("") && !isValidDataType(value, val.getDataType(), val.getRegExp())) {
            isValid = false;
            claimResult.getMessage().add(String.format(IncorrectDataErrorMsg, val.getNodeDesc(), sectionName));
            LOG.debug("Invalid element: incorrect data for element: {} (section '{}')", val.getNodeDesc(), sectionName);
        }

        if (!isDataLengthCorrect(value, val)) {
            LOG.debug("Invalid length: for {} ", val.getNodeName());
            isValid = false;
            claimResult.getMessage().add(String.format(INCORRECT_DATA_LENGTH_ERROR_MSG, nodeName, val.getLength()));
        }

        LOG.debug("coreNodevalidation: {}", isValid);

        return setStatus(claimResult, isValid);
    }

    private static ClaimResult setStatus(ClaimResult claimResult, boolean isValid) {
        if (!isValid) {
            claimResult.setCheckDataValid(false);
            claimResult.setDataValid(false);
            claimResult.setValid(false);
        }
        return claimResult;
    }

    public static boolean isDataLengthCorrect(String dataValue, NodeRuleModel nodeRuleModel) {
        boolean returnValue = true;
        LOG.debug("checking value length for {} [{}>{}] ", new Object[]{nodeRuleModel.getNodeName(), dataValue.trim().length(), nodeRuleModel.getLength()});
        if (nodeRuleModel.getLength() > 0) {
            if (dataValue.trim().length() > nodeRuleModel.getLength()) {
                returnValue = false;
            }
        }
        return returnValue;
    }

    public static ClaimResult nodeVehicleClassValidate(
            String sectionName,
            String nodeName,
            Element element,
            ClaimResult claimResult,
            DataValidationParameter dataValidationParameter,
            VehicleClassService vehicleClassService) {

        boolean isValid = true;
        NodeRuleModel val = getNodeRule(sectionName, nodeName, dataValidationParameter);
        String value = XMLUtils.getElementValue(element, nodeName);

        // CHECK MANDATORY - VALUE IN XML IS EMPTY
        if (isDataMandatory(claimResult, val) && value.trim().equalsIgnoreCase("")) {
            isValid = false;
            claimResult.getMessage().add(String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
            LOG.debug("Mandatory data error: '{}'",
                    String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
        }

        // CHECK MANDATORY - VALUE IN XML IS NOT EMPTY
        // CHECK THE VEHICLE CLASS FOR THE VALUE IS EXIST OR NOT
        if (!value.trim().equalsIgnoreCase("")) {

            VehicleClass vehicleClass = vehicleClassService.getVehicleClassByNodeName(element, nodeName);

            if (vehicleClass == null && isDataMandatory(claimResult, val)) {
                isValid = false;
                claimResult.getMessage().add(String.format(mandatoryVehicleClassDataErrorMsg, sectionName));
            }
        }

        setStatus(claimResult, isValid);

        return claimResult;
    }

    public static ClaimResult nodeValidate(
            String sectionName,
            String nodeName,
            Element element,
            ClaimResult claimResult,
            DataValidationParameter dataValidationParameter) throws Exception {

        LOG.debug("Validating node in section '{}': {}", sectionName, nodeName);
        NodeRuleModel val = getNodeRule(sectionName, nodeName, dataValidationParameter);
        String value = XMLUtils.getElementValue(element, nodeName);
        LOG.debug("NoduRuleModel value: {}", val.toString());
        return coreNodevalidation(val, claimResult, value, sectionName, nodeName);
    }

    public static boolean nodeValidateBoolean(
            String sectionName,
            String nodeName,
            Element element,
            ClaimResult claimResult,
            DataValidationParameter dataValidationParameter) throws Exception {

        NodeRuleModel val = getNodeRule(sectionName, nodeName, dataValidationParameter);
        String value = XMLUtils.getElementValue(element, nodeName);

        boolean bFlag = true;

        if (isDataMandatory(claimResult, val) && (value == null || value.trim().equalsIgnoreCase(""))) {
            bFlag = false;
        }

        if (!(value == null || value.trim().equalsIgnoreCase("")) && !isValidDataType(value, val.getDataType(), val.getRegExp())) {
            bFlag = false;
        }

        LOG.debug("Returning validation for nodeName '{}': {}", nodeName, bFlag);
        return bFlag;
    }

    public static ClaimResult nodeContentValidate(
            String sectionName,
            String nodeName,
            Element element,
            ClaimResult claimResult,
            DataValidationParameter dataValidationParameter) throws Exception {

        NodeRuleModel val = getNodeRule(sectionName, nodeName, dataValidationParameter);
        String value = element.getTextContent();
        LOG.debug("Validating content for nodeName '{}': {}", nodeName, value);
        return coreNodevalidation(val, claimResult, value, sectionName, nodeName);
    }

    public static ClaimResult nodeValidateDefaultDescription(
            String sectionName,
            String nodeName,
            Element element,
            ClaimResult claimResult,
            DataValidationParameter dataValidationParameter,
            String nodeDescription) throws Exception {

        NodeRuleModel val = getNodeRule(sectionName, nodeName, dataValidationParameter);
        if (!nodeDescription.trim().equalsIgnoreCase("")) {
            val.setNodeDesc(nodeDescription);
        }

        String value = XMLUtils.getElementValue(element, nodeName);
        LOG.debug("Validating default description for nodeName '{}': {}", nodeName, value);
        return coreNodevalidation(val, claimResult, value, sectionName, nodeName);
    }

    public static Boolean isValidDataType(String dataValue, String dataType, String regExp) throws Exception {
        Boolean bFlag = true;

        try {

            String regExpression = "";

            if (regExp.trim().equalsIgnoreCase("")) {

                if (dataType.equalsIgnoreCase("date")) {
                    regExpression = REG_TIMESTAMP;
                } else if (dataType.equalsIgnoreCase("char")) {
                    regExpression = REG_BOOLEAN;
                } else if (dataType.equalsIgnoreCase("int")) {
                    regExpression = REG_INTEGER;
                } else if (dataType.equalsIgnoreCase("numeric")) {
                    regExpression = REG_BIGDECIMAL;
                } else if (dataType.equalsIgnoreCase("datetime")) {
                    regExpression = REG_DATETIME;
                }

            } else {
                regExpression = regExp;
            }

            if (!regExpression.equalsIgnoreCase("")) {

                Pattern p = Pattern.compile(regExpression);
                Matcher m = p.matcher(dataValue);

                if (!m.find()) {
                    LOG.debug("Invalid data for regex '{}': {}", regExpression, dataValue);
                    bFlag = false;
                }
            }

        } catch (PatternSyntaxException e) {
            LOG.debug("PatternSyntaxException for dataType '{}' with value={}", dataType, dataValue);
            bFlag = false;
        }

        return bFlag;
    }

    public static boolean isRegularExpressionCheckPass(String regExpression, String value) {
        boolean bFlag = false;

        try {
            if (regExpression != null && !regExpression.isEmpty()) {
                Pattern p = Pattern.compile(regExpression);
                Matcher m = p.matcher(value);

                if (m.find()) {
                    bFlag = true;
                } else {
                    LOG.debug("Failed regex check with regex='{}', value='{}'", regExpression, value);
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown checking pattern '{}' against regex '{}': {}", new Object[]{value, regExpression, ex.getMessage()});
        }

        return bFlag;
    }
}
