package idas.chox.service.xml.util;


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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.w3c.dom.*;

public class NodeHelper {

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

        NodeRuleModel val = getNodeRule(sectionName, nodeName, dataValidationParameter);
        String value = XMLUtils.getElementValue(element, nodeName);

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
        return coreNodevalidation(val, claimResult, value, sectionName);
    }

    private static ClaimResult coreNodevalidation(NodeRuleModel val, ClaimResult claimResult, String value, String sectionName) throws Exception {

        boolean isValid = true;

        if (val.isDataMandatory() && value.trim().equalsIgnoreCase("")) {
            isValid = false;
            claimResult.getMessage().add(String.format(mandatoryDataErrorMsg, val.getNodeDesc(), sectionName));
        }

        if (!value.trim().equalsIgnoreCase("") && !isValidDataType(value, val.getDataType(), val.getRegExp())) {
            isValid = false;
            claimResult.getMessage().add(String.format(IncorrectDataErrorMsg, val.getNodeDesc(), sectionName));
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
                    bFlag = false;
                }
            }

        } catch (PatternSyntaxException e) {
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
        }



        return bFlag;
    }
}
