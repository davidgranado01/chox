/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.xml.readers;

import idas.chox.core.model.Customer;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;
import org.w3c.dom.Element;

public class CustomerReader extends BaseEntityReader {

    protected static String sectionName = "Customer";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {

        Element rootElement = claimResult.getElement();
        Element driversElement = XMLUtils.getElement(rootElement, "drivers");
        Element element = XMLUtils.getElement(driversElement, "driver");

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)) {

            isAllowToReadData = true;
            claimResult.setCheckDataValid(true);

            claimResult = NodeHelper.nodeValidate(sectionName, "title", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "firstnames", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "lastname", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "address1", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "address2", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "address3", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "address4", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "address5", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "postcode", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "telephone-day", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "telephone-evening", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "email", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "age", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "occupation", element, claimResult, getDataValidationParameter());
            claimResult = NodeHelper.nodeValidate(sectionName, "policy-usage", element, claimResult, getDataValidationParameter());
//            claimResult = NodeHelper.nodeValidate(sectionName, "primary-driver", element, claimResult, getDataValidationParameter());

            isAllowToReadData = claimResult.isCheckDataValid();

        }

        return isAllowToReadData;

    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {

        Element rootElement = claimResult.getElement();
        Element driversElement = XMLUtils.getElement(rootElement, "drivers");
        Element element = XMLUtils.getElement(driversElement, "driver");

        if (claimResult.getClaim().getCustomer() == null) {
            claimResult.getClaim().setCustomer(new Customer());
        }

        claimResult.getClaim().getCustomer().setTitle(XmlHelper.getNodeValue(element, "title"));
        claimResult.getClaim().getCustomer().setFirstName(XmlHelper.getNodeValue(element, "firstnames"));
        claimResult.getClaim().getCustomer().setLastName(XmlHelper.getNodeValue(element, "lastname"));
        claimResult.getClaim().getCustomer().setAddress1(XmlHelper.getNodeValue(element, "address1"));
        claimResult.getClaim().getCustomer().setAddress2(XmlHelper.getNodeValue(element, "address2"));
        claimResult.getClaim().getCustomer().setAddress3(XmlHelper.getNodeValue(element, "address3"));
        claimResult.getClaim().getCustomer().setAddress4(XmlHelper.getNodeValue(element, "address4"));
        claimResult.getClaim().getCustomer().setAddress5(XmlHelper.getNodeValue(element, "address5"));
        claimResult.getClaim().getCustomer().setPostcode(XmlHelper.getNodeValue(element, "postcode"));
        claimResult.getClaim().getCustomer().setTelephoneDay(XmlHelper.getNodeValue(element, "telephone-day"));
        claimResult.getClaim().getCustomer().setTelephoneEvening(XmlHelper.getNodeValue(element, "telephone-evening"));
        claimResult.getClaim().getCustomer().setEmail(XmlHelper.getEmailAddressFromNode(element, "email"));
        claimResult.getClaim().getCustomer().setIsPrimaryDriver(true);
        claimResult.getClaim().getCustomer().setAge(XmlHelper.getIntegerFromNode(element, "age"));
        claimResult.getClaim().getCustomer().setOccupation(XmlHelper.getNodeValue(element, "occupation"));
        claimResult.getClaim().getCustomer().setPolicyUsage(XmlHelper.getNodeValue(element, "policy-usage"));

    }
}
