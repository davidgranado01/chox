package chox.xmlValidation.rules.enginee;

import chox.Util.TextHelper;
import chox.model.VehicleClass;
import chox.model.VehicleHire;
import chox.services.SecureDataService;
import chox.services.VehicleClassService;
import chox.services.ClaimResult;
import chox.xmlValidation.model.status.ClaimParseStatus;
import chox.xmlValidation.rules.DataValidationParameter;
import chox.xmlValidation.rules.Util.NodeHelper;
import chox.xmlValidation.rules.Util.XmlHelper;
import chox.xmlValidation.rules.rulesInterface;
import com.filesystemsoftware.utils.XMLUtils;
import java.util.ArrayList;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;

public class ClaimVehicleHireExtraValidation extends SecureDataService implements rulesInterface{
    
    protected static String sectionName = "Vehicle Hire Extra";
    private DataValidationParameter dataValidationParameter;
    private ClaimResult claimResult;
    
    // THIS PAGE ONLY
    private ArrayList<Element> elements;
    private Element element;
    private Element rootElement;

    public void setRootElement(Element rootElement) { this.rootElement = rootElement; }
    public void setClaimResult(ClaimResult claimResult) { this.claimResult = claimResult; }
    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) { this.dataValidationParameter = dataValidationParameter; }
    
    public ClaimVehicleHireExtraValidation(
            ClaimResult claimResult, 
            DataValidationParameter dataValidationParameter,
            Element rootElement){
        
            setClaimResult(claimResult);
            setDataValidationParameter(dataValidationParameter);
            setRootElement(rootElement);
    }
    
    public ClaimResult execute() throws DOMException, XPathExpressionException, Exception{

        this.elements = XMLUtils.getElements(this.rootElement.getOwnerDocument(), this.rootElement, "extra");        
        
        
        doPreInitialize();

        if(validate()){
            process();
        }

        doPrintResult(false);
        return claimResult;
    }
    
    private boolean validate() throws DOMException, XPathExpressionException, Exception{
        
        boolean isAllowToReadData = false;
   
            isAllowToReadData = true; 
            
            for (Element ee : this.elements) {
                this.claimResult = NodeHelper.nodeContentValidate(sectionName, "extra", ee, this.claimResult, this.dataValidationParameter);
            }
            
            isAllowToReadData = this.claimResult.isCheckDataValid();
        
        return isAllowToReadData;
    }
    
    private void process(){ 

        for (Element ee : this.elements) {
            String sExtra = ee.getTextContent();
            setExtraItem(sExtra);   
        }

    }

    private void doPrintResult(boolean isAllowed){
        /*
        if(isAllowed){
            
            System.out.println("-------");
            System.out.println(sectionName + "| Status :"+this.claimResult.isDataValid());
        }
        */
    } 
    
    private void setExtraItem(String extraItemName){
        
        if(extraItemName.equalsIgnoreCase("CDW")){
            this.claimResult.getClaim().getVehicleHire().setCdwFee(true);
        }else if(extraItemName.equalsIgnoreCase("Admin")){
            this.claimResult.getClaim().getVehicleHire().setAdminFee(true);
        }else if(extraItemName.equalsIgnoreCase("Automatic")){
            this.claimResult.getClaim().getVehicleHire().setAutomaticFee(true);
        }else if(extraItemName.equalsIgnoreCase("Baby Seat")){
            this.claimResult.getClaim().getVehicleHire().setBabySeatFee(true);
        }else if(extraItemName.equalsIgnoreCase("Delivery Collection")){
            this.claimResult.getClaim().getVehicleHire().setDeliveryCollectionFee(true);
        }else if(extraItemName.equalsIgnoreCase("Dual Control")){
            this.claimResult.getClaim().getVehicleHire().setDualControlFee(true);
        }else if(extraItemName.equalsIgnoreCase("Estate")){
            this.claimResult.getClaim().getVehicleHire().setEstateFee(true);
        }else if(extraItemName.equalsIgnoreCase("Non-standard Risk Insurance Premium")){
            this.claimResult.getClaim().getVehicleHire().setNonStandardInsurancePremiumFee(true);
        }else if(extraItemName.equalsIgnoreCase("Roof Rack")){
            this.claimResult.getClaim().getVehicleHire().setRoofRackFee(true);
        }else if(extraItemName.equalsIgnoreCase("Sat Nav")){
            this.claimResult.getClaim().getVehicleHire().setSatNavFee(true);
        }else if(extraItemName.equalsIgnoreCase("Tow Bars")){
            this.claimResult.getClaim().getVehicleHire().setTowBarsFee(true);
        }
    }
    
    private void doPreInitialize(){
        this.claimResult.getClaim().getVehicleHire().setCdwFee(false);
        this.claimResult.getClaim().getVehicleHire().setAdminFee(false);
        this.claimResult.getClaim().getVehicleHire().setAutomaticFee(false);
        this.claimResult.getClaim().getVehicleHire().setBabySeatFee(false);
        this.claimResult.getClaim().getVehicleHire().setDeliveryCollectionFee(false);
        this.claimResult.getClaim().getVehicleHire().setDualControlFee(false);
        this.claimResult.getClaim().getVehicleHire().setEstateFee(false);
        this.claimResult.getClaim().getVehicleHire().setNonStandardInsurancePremiumFee(false);
        this.claimResult.getClaim().getVehicleHire().setRoofRackFee(false);
        this.claimResult.getClaim().getVehicleHire().setSatNavFee(false);
        this.claimResult.getClaim().getVehicleHire().setTowBarsFee(false);            
    }
}