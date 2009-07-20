package chox.xmlValidation.rules;

import chox.Util.TextHelper;
import chox.xmlValidation.rules.Util.XmlHelper;
import org.w3c.dom.*;
import com.filesystemsoftware.utils.XMLUtils;
import chox.model.*;
import chox.services.VehicleClassService;
import java.util.ArrayList;

public class vehicleValidation {

    // VALIDATE INVOICE SECTION 
    public static XMLParseResult RentalVehiclesSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            Document doc, 
            VehicleClassService vehicleClassService) throws Exception {

        xmlParseResult.setIsCurrentScheValid(true);
        xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, "rental-vehicles", "Hire Vehicle Details", "");

        if (xmlParseResult.getIsCurrentScheValid()) {
            
            
            Element thisElement = XMLUtils.getElement(mainElement, "rental-vehicles");
            ArrayList<Element> rentalVehicleElements = XMLUtils.getElements(doc, thisElement, "rental-vehicle");
            xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, rentalVehicleElements, "rental-vehicle", "Hire Vehicle Details", "");

            if (xmlParseResult.getIsCurrentScheValid()) {
                
                for (Element ee : rentalVehicleElements) {
                    vehicleValidation vehicleCtrl = new vehicleValidation();
                    xmlParseResult = vehicleCtrl.VehicleSchemaValidation(xmlParseResult, ee, doc, vehicleClassService);
                    break;
                }
            }
        }
        
        return xmlParseResult;
    }
    
    private XMLParseResult VehicleSchemaValidation(
            XMLParseResult xmlParseResult,
            Element mainElement,
            Document doc, 
            VehicleClassService vehicleClassService) throws Exception {
            
        String strSectionName = "Hire Vehicle Details";
        
        try{
     
            VehicleHire vehiclehire = new VehicleHire();

            if(xmlParseResult.getIsClaimExist() && xmlParseResult.getClaim().getVehicleHire()!=null){
                vehiclehire = xmlParseResult.getClaim().getVehicleHire();
            }

            xmlParseResult.setIsCurrentDataValid(true);
            xmlParseResult.setIsCurrentScheValid(true);

            if(xmlParseResult.getIsClaimExist() && xmlParseResult.getClaim().getStatus().equalsIgnoreCase(ClaimStatus.AWAITING_INVOICE_DATA)){

                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "vehicle-registration", XmlHelper.isMAN_RentalVehicles_Vehicle_Registration, XmlHelper.REG_VEHICLE_REG, strSectionName, "Registration");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "vehicle-manufacturer", XmlHelper.isMAN_RentalVehicles_Vehicle_Manufacturer, "", strSectionName, "Manufacturer");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "vehicle-model", XmlHelper.isMAN_RentalVehicles_Vehicle_Model, "", strSectionName, "Model");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "vehicle-class", XmlHelper.isMAN_RentalVehicles_Vehicle_Class, "", strSectionName, "Replacement Vehicle Class");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "rental-start", XmlHelper.isMAN_RentalVehicles_Rental_Start, XmlHelper.REG_TIMESTAMP, strSectionName, "Hire Start");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "rental-end", XmlHelper.isMAN_RentalVehicles_Rental_End, XmlHelper.REG_TIMESTAMP, strSectionName, "Hire End");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "rental-days", XmlHelper.isMAN_RentalVehicles_Rental_Days, XmlHelper.REG_INTEGER, strSectionName, "Number Days Hire");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "collection-reason", XmlHelper.isMAN_RentalVehicles_CollectionReason, "", strSectionName, "Reason For Collection");
                
            }else{

                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "vehicle-registration", false, XmlHelper.REG_VEHICLE_REG, strSectionName, "Registration");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "vehicle-manufacturer", false, "", strSectionName, "Manufacturer");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "vehicle-model", false, "", strSectionName, "Model");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "vehicle-class", false, "", strSectionName, "Replacement Vehicle Class");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "rental-start", false, XmlHelper.REG_TIMESTAMP, strSectionName, "Hire Start");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "rental-end", false, XmlHelper.REG_TIMESTAMP, strSectionName, "Hire End");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "rental-days", false, XmlHelper.REG_INTEGER, strSectionName, "Number Days Hire");
                xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, mainElement, "collection-reason", false, "", strSectionName, "Reason For Collection");            
            }

            if ((xmlParseResult.getIsCurrentDataValid() 
                    && xmlParseResult.getIsCurrentScheValid())
                    && (XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "vehicle-registration"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "vehicle-manufacturer"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "vehicle-model"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "vehicle-class"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "rental-start"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "rental-end"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "rental-days"))
                    || XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "collection-reason"))
            )){

                // GET VEHICLE CLASS ID
                if(XmlHelper.isNotNull(XmlHelper.getNodeValue(mainElement, "vehicle-class"))){
                    VehicleClass vehicleclass = vehicleClassService.getVehicleClassByNodeName(mainElement, "vehicle-class");

                    if(vehicleclass!=null){
                        vehiclehire.setVehicleClass(vehicleclass);
                    }else{
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, "Selected Vehicle Class is invalid", false);
                    }
                }

                vehiclehire.setVehicleRegistration(TextHelper.trimWhiteSpace(XmlHelper.getNodeValue(mainElement, "vehicle-registration")));
                vehiclehire.setVehicleManufacturer(XmlHelper.getNodeValue(mainElement, "vehicle-manufacturer"));
                vehiclehire.setVehicleModel(XmlHelper.getNodeValue(mainElement, "vehicle-model"));
                vehiclehire.setRentalStart(XmlHelper.getTimeStampFromNode(mainElement, "rental-start"));
                vehiclehire.setRentalEnd(XmlHelper.getTimeStampFromNode(mainElement, "rental-end"));
                vehiclehire.setDays(XmlHelper.getIntegerFromNode(mainElement, "rental-days"));
                vehiclehire.setCollectionReason(XmlHelper.getNodeValue(mainElement, "collection-reason"));


                // PART 2 : EXTRA SECTION
                String nodeName1 = "extras";
                String nodeName2 = "extra";

                xmlParseResult.setIsCurrentDataValid(true);
                xmlParseResult.setIsCurrentScheValid(true);

                xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, mainElement, nodeName1, strSectionName, "");

                if (xmlParseResult.getIsCurrentScheValid()) {

                    xmlParseResult.setIsCurrentScheValid(true);

                    // GET <EXTRAS></EXTRAS> ELEMENT
                    Element thisElement = XMLUtils.getElement(mainElement, nodeName1);
                    xmlParseResult = XmlHelper.xmlSchemaNodeValidation(xmlParseResult, thisElement, nodeName2, strSectionName, "");

                    if (xmlParseResult.getIsCurrentScheValid()) {

                        // GET <EXTRA></EXTRA> ELEMENT
                        ArrayList<Element> extraElements = XMLUtils.getElements(doc, thisElement, nodeName2);
                        xmlParseResult = XmlHelper.xmlSchemaNodeListValidation(xmlParseResult, extraElements, nodeName2, strSectionName, "");

                        if (xmlParseResult.getIsCurrentScheValid()) {

                            for (Element ee : extraElements) {

                                String selectedExtra = ee.getTextContent();

                                if(!selectedExtra.equalsIgnoreCase("")){
                                    xmlParseResult = XmlHelper.xmlNodeValidation(xmlParseResult, thisElement, nodeName2, XmlHelper.isMAN_RentalVehicles_Extras_Extra, "", strSectionName, selectedExtra);
                                }

                                if(selectedExtra.equalsIgnoreCase("CDW")){
                                    vehiclehire.setCdwFee(true);
                                }else if(selectedExtra.equalsIgnoreCase("Admin")){
                                    vehiclehire.setAdminFee(true);
                                }else if(selectedExtra.equalsIgnoreCase("Automatic")){
                                    vehiclehire.setAutomaticFee(true);
                                }else if(selectedExtra.equalsIgnoreCase("Baby Seat")){
                                    vehiclehire.setBabySeatFee(true);
                                }else if(selectedExtra.equalsIgnoreCase("Delivery Collection")){
                                    vehiclehire.setDeliveryCollectionFee(true);
                                }else if(selectedExtra.equalsIgnoreCase("Dual Control")){
                                    vehiclehire.setDualControlFee(true);
                                }else if(selectedExtra.equalsIgnoreCase("Estate")){
                                    vehiclehire.setEstateFee(true);
                                }else if(selectedExtra.equalsIgnoreCase("Non-standard Risk Insurance Premium")){
                                    vehiclehire.setNonStandardInsurancePremiumFee(true);
                                }else if(selectedExtra.equalsIgnoreCase("Roof Rack")){
                                    vehiclehire.setRoofRackFee(true);
                                }else if(selectedExtra.equalsIgnoreCase("Sat Nav")){
                                    vehiclehire.setSatNavFee(true);
                                }else if(selectedExtra.equalsIgnoreCase("Tow Bars")){
                                    vehiclehire.setTowBarsFee(true);
                                }
                            }
                            xmlParseResult.getClaim().setVehicleHire(vehiclehire);
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
