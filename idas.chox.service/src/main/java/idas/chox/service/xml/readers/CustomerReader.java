package idas.chox.service.xml.readers;

import org.w3c.dom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Customer;
import idas.chox.core.model.ClaimType;
import idas.chox.core.util.XMLUtils;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.util.NodeHelper;
import idas.chox.core.util.XmlHelper;

public class CustomerReader extends BaseEntityReader {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerReader.class);

    protected static String sectionName = "Customer";

    @Override
    protected boolean validate(ClaimResult claimResult) throws Exception {
        LOG.debug("Validating Customer: claimResult is {}", claimResult);
        LOG.debug("Validating Customer: ClaimParseStatus is {}", claimResult.getClaimParseStatus());

        Element rootElement = claimResult.getElement();

        Element element = XMLUtils.getElement(rootElement, "driver");
        LOG.debug("Got driver element");

        boolean isAllowToReadData = false;

        if (claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_CLAIM)
                || (claimResult.getClaimParseStatus().equals(ClaimParseStatus.INSURER_INVOICE) && claimResult.getClaim().getClaimType() == ClaimType.INSURER_INVOICE)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_SUBSCRIBER_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.NEW_FIXEDFEE_CLAIM)
                || claimResult.getClaimParseStatus().equals(ClaimParseStatus.TPI_INTERVENTION)) {
            LOG.debug("Validating new claim");

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

            isAllowToReadData = claimResult.isCheckDataValid();
            LOG.debug("Validated new claim: {}", isAllowToReadData);

        }
        LOG.debug("Validating Customer: returning {}", isAllowToReadData);

        return isAllowToReadData;

    }

    @Override
    protected void process(ClaimResult claimResult) throws Exception {
        LOG.debug("Processing  customer element...");

        Element rootElement = claimResult.getElement();
        Element element = XMLUtils.getElement(rootElement, "driver");

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
