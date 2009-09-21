package chox.xmlValidation.rules.enginee;

import chox.Util.TextHelper;
import chox.model.InsurerAllias;
import chox.model.ThirdParty;
import chox.model.VehicleClass;
import chox.services.ClaimService;
import chox.services.InsurerAlliasService;
import chox.services.InsurerChorganisationService;
import chox.services.SecureDataService;
import chox.services.VehicleClassService;
import chox.services.ClaimResult;
import chox.xmlValidation.model.status.ClaimParseStatus;
import chox.xmlValidation.rules.DataValidationParameter;
import chox.xmlValidation.rules.Util.NodeHelper;
import chox.xmlValidation.rules.Util.XmlHelper;
import chox.xmlValidation.rules.rulesInterface;
import com.filesystemsoftware.utils.XMLUtils;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;

public class ClaimThirdPartyValidation extends SecureDataService implements rulesInterface{
    
    protected static String sectionName = "Third Party Details";
    private DataValidationParameter dataValidationParameter;
    private ClaimService claimService;
    private VehicleClassService vehicleClassService;
    private InsurerAlliasService insurerAlliasService;
    private InsurerChorganisationService insurerChorganisationService;
    private ClaimResult claimResult;
    private Element element;

    public void setVehicleClassService(VehicleClassService vehicleClassService) { this.vehicleClassService = vehicleClassService; }
    public void setInsurerAlliasService(InsurerAlliasService insurerAlliasService) { this.insurerAlliasService = insurerAlliasService; }
    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) { this.insurerChorganisationService = insurerChorganisationService; }
    public void setClaimResult(ClaimResult claimResult) { this.claimResult = claimResult; }
    public void setClaimService(ClaimService claimService) { this.claimService = claimService; }
    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) { this.dataValidationParameter = dataValidationParameter; }
    
    public ClaimThirdPartyValidation(
            ClaimResult claimResult, 
            DataValidationParameter dataValidationParameter, 
            ClaimService claimService,
            VehicleClassService vehicleClassService,
            InsurerAlliasService insurerAlliasService,
            InsurerChorganisationService insurerChorganisationService){
        
            setClaimResult(claimResult);
            setDataValidationParameter(dataValidationParameter);
            setClaimService(claimService);
            setVehicleClassService(vehicleClassService);
            setInsurerAlliasService(insurerAlliasService);
            setInsurerChorganisationService(insurerChorganisationService);
    }
    
    public ClaimResult execute() throws DOMException, XPathExpressionException{
        
        this.element = XMLUtils.getElement(XMLUtils.getElement(claimResult.getElement(), "claim"), "third-party");
        
        if(validate()){
            process();
        }
        
        doPrintResult(false);
        return claimResult;
    }
    
    private boolean validate(){
        
        boolean isAllowToReadData = false;
        
        if(claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)){
            
            isAllowToReadData = true;
            this.claimResult.setCheckDataValid(true);

            this.claimResult = NodeHelper.nodeinsurerAliasValidate(sectionName, "name", this.element, claimResult, dataValidationParameter, insurerAlliasService, insurerChorganisationService);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "policy-number", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "claim-reference", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-registration", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-manufacturer", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "vehicle-model", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeVehicleClassValidate(sectionName, "vehicle-class", this.element, claimResult, dataValidationParameter, vehicleClassService);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "title", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "firstnames", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "lastname", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address1", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address2", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address3", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address4", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address5", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "postcode", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "telephone-day", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "telephone-evening", this.element, claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "email", this.element, claimResult, dataValidationParameter);
            
            isAllowToReadData = this.claimResult.isCheckDataValid();

        }
        
        return isAllowToReadData;
    }
    
    private void process(){ 
        
        if(this.claimResult.getClaim().getThirdParty()==null){
            this.claimResult.getClaim().setThirdParty(new ThirdParty());
        }
        
        String vehicleClassName = XmlHelper.getNodeValue(this.element, "vehicle-class");
        if(vehicleClassName!=null && vehicleClassName.length()>0){
            VehicleClass vehicleClass = null;
            vehicleClass = vehicleClassService.getVehicleClassByNodeName(this.element, "vehicle-class");
            this.claimResult.getClaim().getThirdParty().setVehicleClass(vehicleClass);
        }
        
        String insurerAliasName = XmlHelper.getNodeValue(this.element, "name");
        if(insurerAliasName!=null && insurerAliasName.length()>0){
            InsurerAllias allias = insurerAlliasService.getInsurerByAlliasName(insurerAliasName);
            this.claimResult.getClaim().getThirdParty().setInsurer(allias.getInsurer());
        }
        
        String claimNumber = XmlHelper.getNodeValue(this.element, "claim-reference");
        this.claimResult.getClaim().setClaimNumber(claimNumber);
        this.claimResult.getClaim().getThirdParty().setPolicyNumber(XmlHelper.getNodeValue(this.element, "policy-number"));
        this.claimResult.getClaim().getThirdParty().setClaimReference(claimNumber);
        this.claimResult.getClaim().getThirdParty().setVehicleRegistration(TextHelper.trimWhiteSpace(XmlHelper.getNodeValue(this.element, "vehicle-registration")));
        this.claimResult.getClaim().getThirdParty().setVehicleManufacturer(XmlHelper.getNodeValue(this.element, "vehicle-manufacturer"));
        this.claimResult.getClaim().getThirdParty().setVehicleModel(XmlHelper.getNodeValue(this.element, "vehicle-model"));
        this.claimResult.getClaim().getThirdParty().setAddress1(XmlHelper.getNodeValue(this.element, "address1"));
        this.claimResult.getClaim().getThirdParty().setAddress2(XmlHelper.getNodeValue(this.element, "address2"));
        this.claimResult.getClaim().getThirdParty().setAddress3(XmlHelper.getNodeValue(this.element, "address3"));
        this.claimResult.getClaim().getThirdParty().setAddress4(XmlHelper.getNodeValue(this.element, "address4"));
        this.claimResult.getClaim().getThirdParty().setAddress5(XmlHelper.getNodeValue(this.element, "address5"));
        this.claimResult.getClaim().getThirdParty().setPostcode(XmlHelper.getNodeValue(this.element, "postcode"));
        this.claimResult.getClaim().getThirdParty().setTelephoneEvening(XmlHelper.getNodeValue(this.element, "telephone-evening"));
        this.claimResult.getClaim().getThirdParty().setTelephoneDay(XmlHelper.getNodeValue(this.element, "telephone-day"));
        this.claimResult.getClaim().getThirdParty().setEmail(XmlHelper.getEmailAddressFromNode(this.element, "email"));
        this.claimResult.getClaim().getThirdParty().setFirstName(XmlHelper.getNodeValue(this.element, "firstnames"));
        this.claimResult.getClaim().getThirdParty().setLastName(XmlHelper.getNodeValue(this.element, "lastname"));
        this.claimResult.getClaim().getThirdParty().setTitle(XmlHelper.getNodeValue(this.element, "title"));

    }

    private void doPrintResult(boolean isAllowed){
        
        if(isAllowed){
            
            System.out.println("-------");
            System.out.println(sectionName + "| Status :"+this.claimResult.isDataValid());
            
            if(this.claimResult.getClaim().getThirdParty()!=null){
                
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getPolicyNumber());
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getClaimReference());
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getVehicleRegistration());
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getVehicleManufacturer());
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getVehicleModel());
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getAddress1());
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getAddress2());
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getAddress3());
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getAddress4());
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getAddress5());
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getPostcode());
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getTelephoneEvening());
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getTelephoneDay());
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getEmail());
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getFirstName());
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getLastName());
                System.out.println(sectionName + "| getInsurerName :"+this.claimResult.getClaim().getThirdParty().getTitle());   
                
            }else{
                
                System.out.println(sectionName + "| NO THIRD PARTY OBJECT HAVE FOUND!!");
                
            }
        }
    }    
}
