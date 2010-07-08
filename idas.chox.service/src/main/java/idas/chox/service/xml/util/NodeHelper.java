package idas.chox.service.xml.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.w3c.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.InsurerAlias;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.InsurerAliasService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.core.util.TextHelper;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.validations.DataValidationParameter;
import idas.chox.core.xmlValidation.NodeRuleModel;

public class NodeHelper {
    private static final Logger LOG = LoggerFactory.getLogger(NodeHelper.class);

    private static String mandatoryDataErrorMsg = "No '%s' information supplied for '%s'. Please re-submit with this information.";
    private static String IncorrectDataErrorMsg = "Invalid or incorrect character in '%s' for '%s'.";
    private static String mandatoryVehicleClassDataErrorMsg = "Selected Vehicle Class is invalid for '%s'";
    private static String IncorrectInsurerAlias = "Selected '%s' for '%s' Insurer Alias is invalid";
    public static final String REG_TIMESTAMP = "^\\d{4}-(0[0-9]|1[0,1,2])-([0-9]|[0,1,2][0-9]|3[0,1])[T]([0-9]{2}):([0-9]{2}):([0-9]{2})$";
    public static final String REG_BOOLEAN = "^[ynYN]";
    public static final String REG_INTEGER = "^[0-9]+$";
    public static final String REG_BIGDECIMAL = "^\\-?(\\d+)*\\.?\\d*$";

    private static String getNodeRuleName(String sectionName, String nodeName) {
        return TextHelper.trimWhiteSpace(sectionName.toLowerCase() + "-" + nodeName);
    }

    private static NodeRuleModel getNodeRule(String sectionName, String nodeName, DataValidationParameter dataValidationParameter) {
        String NodeRuleName = getNodeRuleName(sectionName, nodeName);
        return dataValidationParameter.getValidationElementByField(NodeRuleName);
    }

    public static ClaimResult nodeinsurerAliasValidate(
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
        if (value.trim().equalsIgnoreCase("")) {

            isValid = false;
            claimResult.getMessage().add(String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
            LOG.debug("Mandatory data error: '{}'", String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
        } else {

            InsurerAlias allias = insurerAliasService.getInsurerByAliasName(value);

            if (allias != null) {

                if (allias.getInsurer() != null) {

                    if (!insurerChorganisationService.isActiveObjectExist(allias.getInsurer().getId(), claimResult.getClaim().getChorganisation().getId())) {
                        isValid = false;
                        claimResult.getMessage().add(String.format(IncorrectInsurerAlias, value, sectionName));
                    }

                } else {
                    isValid = false;
                    claimResult.getMessage().add(String.format(IncorrectInsurerAlias, value, sectionName));
                }

            } else {
                isValid = false;
                claimResult.getMessage().add(String.format(IncorrectInsurerAlias, value, sectionName));
            }

        }

        setStatus(claimResult, isValid);

        return claimResult;

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
        if (val.isDataMandatory() && value.trim().equalsIgnoreCase("")) {
            isValid = false;
            claimResult.getMessage().add(String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
            LOG.debug("Mandatory data error: '{}'", String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
        }

        // CHECK MANDATORY - VALUE IN XML IS NOT EMPTY
        // CHECK THE VEHICLE CLASS FOR THE VALUE IS EXIST OR NOT
        if (!value.trim().equalsIgnoreCase("")) {

            VehicleClass vehicleClass = vehicleClassService.getVehicleClassByNodeName(element, nodeName);

            if (vehicleClass == null && val.isDataMandatory()) {
                isValid = false;
                claimResult.getMessage().add(String.format(mandatoryVehicleClassDataErrorMsg, sectionName));
            }
        }

        setStatus(claimResult, isValid);

        return claimResult;

    }

    public static ClaimResult nodeVehicleClassValidateDefaultMandatoryValue(
            String sectionName,
            String nodeName,
            Element element,
            ClaimResult claimResult,
            DataValidationParameter dataValidationParameter,
            VehicleClassService vehicleClassService,
            boolean newMandatory) {

        boolean isValid = true;
        NodeRuleModel val = getNodeRule(sectionName, nodeName, dataValidationParameter);
        String value = XMLUtils.getElementValue(element, nodeName);

        if (newMandatory) {
            val.setDataMandatory("t");
        } else {
            val.setDataMandatory("f");
        }

        // CHECK MANDATORY - VALUE IN XML IS EMPTY
        if (val.isDataMandatory() && value.trim().equalsIgnoreCase("")) {
            isValid = false;
            claimResult.getMessage().add(String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
            LOG.debug("Mandatory data error: '{}'", String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
        }

        // CHECK MANDATORY - VALUE IN XML IS NOT EMPTY
        // CHECK THE VEHICLE CLASS FOR THE VALUE IS EXIST OR NOT
        if (!value.trim().equalsIgnoreCase("")) {

            VehicleClass vehicleClass = vehicleClassService.getVehicleClassByNodeName(element, nodeName);

            if (vehicleClass == null && val.isDataMandatory()) {
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
        LOG.debug("Validating value: {}", value);
        return coreNodevalidation(val, claimResult, value, sectionName);
    }

    public static boolean nodeValidateBoolean(
            String sectionName,
            String nodeName,
            Element element,
            DataValidationParameter dataValidationParameter) throws Exception {

        NodeRuleModel val = getNodeRule(sectionName, nodeName, dataValidationParameter);
        String value = XMLUtils.getElementValue(element, nodeName);

        boolean bFlag = true;

        if (val.isDataMandatory() && value.trim().equalsIgnoreCase("")) {
            bFlag = false;
        }

        if (!value.trim().equalsIgnoreCase("") && !isValidDataType(value, val.getDataType(), val.getRegExp())) {
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
        return coreNodevalidation(val, claimResult, value, sectionName);
    }

    public static ClaimResult nodeValidateDefaultMandatoryValue(
            String sectionName,
            String nodeName,
            Element element,
            ClaimResult claimResult,
            DataValidationParameter dataValidationParameter,
            boolean newMandatory) throws Exception {

        NodeRuleModel val = getNodeRule(sectionName, nodeName, dataValidationParameter);
        if (newMandatory) {
            val.setDataMandatory("t");
        } else {
            val.setDataMandatory("f");
        }

        String value = XMLUtils.getElementValue(element, nodeName);
        LOG.debug("Validating default/mandatory value for nodeName '{}': {}", nodeName, value);
        return coreNodevalidation(val, claimResult, value, sectionName);

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
        return coreNodevalidation(val, claimResult, value, sectionName);
    }

    private static ClaimResult coreNodevalidation(NodeRuleModel val, ClaimResult claimResult, String value, String sectionName) throws Exception {

        boolean isValid = true;

        if (val.isDataMandatory() && value.trim().equalsIgnoreCase("")) {
            isValid = false;
            claimResult.getMessage().add(String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
            LOG.debug("Mandatory data error: '{}'", String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
        }

        if (!value.trim().equalsIgnoreCase("") && !isValidDataType(value, val.getDataType(), val.getRegExp())) {
            isValid = false;
            claimResult.getMessage().add(String.format(IncorrectDataErrorMsg, val.getNodeDesc(), sectionName));
            LOG.debug("Invalid element: incorrect data for element: {} (section '{}')", val.getNodeDesc(), sectionName);
        }

        setStatus(claimResult, isValid);
        return claimResult;
    }

    private static ClaimResult setStatus(ClaimResult claimResult, boolean isValid) {
        if (!isValid) {
            claimResult.setCheckDataValid(false);
            claimResult.setDataValid(false);
            claimResult.setValid(false);
        }
        return claimResult;
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

    public boolean isRegularExpressionCheckPass(String regExpression, String value) {

        boolean bFlag = false;

        if (!regExpression.equalsIgnoreCase("")) {

            Pattern p = Pattern.compile(regExpression);
            Matcher m = p.matcher(value);

            if (m.find()) {
                bFlag = true;
            }
            else
                LOG.debug("Failed regex check with regex='{}', value='{}'", regExpression, value);
        }



        return bFlag;
    }
}
