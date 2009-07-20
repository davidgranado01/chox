package chox.xmlValidation.rules;


import chox.xmlValidation.rules.Util.XmlHelper;
import org.w3c.dom.*;
import com.filesystemsoftware.utils.XMLUtils;
import chox.model.*;
import java.util.ArrayList;

public class driverValidation {

    public static XMLParseResult DriversSchemaValidation(
                XMLParseResult xmlParseResult,
                Element mainElement,
                Document doc) throws Exception {
        
        
        String strSectionName = "Customer Details";
        
        try {
            
            xmlParseResult.setIsCurrentScheValid(true);
            
            xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "drivers", strSectionName, "");

            if (xmlParseResult.getIsCurrentScheValid()) {

                xmlParseResult.setIsCurrentScheValid(true);

                Element thisElement = XMLUtils.getElement(mainElement, "drivers");
                ArrayList<Element> driverElements = XMLUtils.getElements(doc, thisElement, "driver");

                // VALIDATE DRIVERS LIST
                xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, driverElements, "driver", strSectionName, "");

                if (xmlParseResult.getIsCurrentScheValid()) {

                    // VALIDATE EVERY DRIVER
                    for (Element ee : driverElements) {

                        xmlParseResult.setIsCurrentDataValid(true);
                        xmlParseResult.setIsCurrentScheValid(true);

                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "title", XmlHelper.isMAN_Driver_Title, "", strSectionName, "Title");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "firstnames", XmlHelper.isMAN_Driver_Firstnames, "", strSectionName, "First Name");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "lastname", XmlHelper.isMAN_Driver_Lastname, "", strSectionName, "Surname");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address1", XmlHelper.isMAN_Driver_Address1, "", strSectionName, "Address1");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address2", XmlHelper.isMAN_Driver_Address2, "", strSectionName, "Address2");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address3", XmlHelper.isMAN_Driver_Address3, "", strSectionName, "Address3");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address4", XmlHelper.isMAN_Driver_Address4, "", strSectionName, "Address4");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "address5", XmlHelper.isMAN_Driver_Address5, "", strSectionName, "Address5");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "postcode", XmlHelper.isMAN_Driver_Postcode, "", strSectionName, "Postcode");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "telephone-day", XmlHelper.isMAN_Driver_Telephone_day, XmlHelper.REG_PHONE, strSectionName, "Telephone Day");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "telephone-evening", XmlHelper.isMAN_Driver_Telephone_Evening, XmlHelper.REG_PHONE, strSectionName, "Telephone Evening");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "email", XmlHelper.isMAN_Driver_Email, XmlHelper.REG_EMAIL, strSectionName, "Email");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "primary-driver", XmlHelper.isMAN_Driver_Primary_Driver, "", strSectionName, "Primary Driver");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "age", XmlHelper.isMAN_Driver_Age, XmlHelper.REG_INTEGER, strSectionName, "Age");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "occupation", XmlHelper.isMAN_Driver_Occupation, "", strSectionName, "Occupation");
                        xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, ee, "policy-usage", XmlHelper.isMAN_Driver_PolicyUsage, "", strSectionName, "Policy Usage");

                        if (xmlParseResult.getIsCurrentDataValid() && xmlParseResult.getIsCurrentScheValid()) {

                            Customer customer = new Customer();

                            if(xmlParseResult.getClaim()!=null && xmlParseResult.getClaim().getCustomer()!=null){
                                customer = xmlParseResult.getClaim().getCustomer();
                            }

                            customer.setTitle(XmlHelper.getNodeValue(ee, "title"));
                            customer.setFirstName(XmlHelper.getNodeValue(ee, "firstnames"));
                            customer.setLastName(XmlHelper.getNodeValue(ee, "lastname"));
                            customer.setAddress1(XmlHelper.getNodeValue(ee, "address1"));
                            customer.setAddress2(XmlHelper.getNodeValue(ee, "address2"));
                            customer.setAddress3(XmlHelper.getNodeValue(ee, "address3"));
                            customer.setAddress4(XmlHelper.getNodeValue(ee, "address4"));
                            customer.setAddress5(XmlHelper.getNodeValue(ee, "address5"));
                            customer.setPostcode(XmlHelper.getNodeValue(ee, "postcode"));
                            customer.setTelephoneDay(XmlHelper.getNodeValue(ee, "telephone-day"));
                            customer.setTelephoneEvening(XmlHelper.getNodeValue(ee, "telephone-evening"));
                            customer.setEmail(XmlHelper.getEmailAddressFromNode(ee, "email"));
                            customer.setIsPrimaryDriver(true);
                            customer.setAge(XmlHelper.getIntegerFromNode(ee, "age"));
                            customer.setOccupation(XmlHelper.getNodeValue(ee, "occupation"));
                            customer.setPolicyUsage(XmlHelper.getNodeValue(ee, "policy-usage"));

                            xmlParseResult.getClaim().setCustomer(customer);

                        }
                    }
                }
            }
            
        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        return xmlParseResult;
    }

}
