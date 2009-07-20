package chox.xmlValidation.rules.enginee;

import chox.model.Injury;
import chox.services.ClaimService;
import chox.services.SecureDataService;
import chox.xmlValidation.model.ClaimResult;
import chox.xmlValidation.model.status.ClaimParseStatus;
import chox.xmlValidation.rules.DataValidationParameter;
import chox.xmlValidation.rules.Util.NodeHelper;
import chox.xmlValidation.rules.Util.XmlHelper;
import chox.xmlValidation.rules.rulesInterface;
import com.filesystemsoftware.utils.XMLUtils;
import java.util.ArrayList;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;

public class IncidentInjuriesValidation extends SecureDataService implements rulesInterface{
    
    protected static String sectionName = "Incident Injury";
    private DataValidationParameter dataValidationParameter;
    private ClaimService claimService;
    private ClaimResult claimResult;
    
    // THIS PAGE ONLY
    private Element element;
    private ArrayList<Element> injuryElements;
    
    public void setClaimResult(ClaimResult claimResult) { this.claimResult = claimResult; }
    public void setClaimService(ClaimService claimService) { this.claimService = claimService; }
    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) { this.dataValidationParameter = dataValidationParameter; }
    
    public IncidentInjuriesValidation(
            ClaimResult claimResult, 
            DataValidationParameter dataValidationParameter, 
            ClaimService claimService){
        
            setClaimResult(claimResult);
            setDataValidationParameter(dataValidationParameter);
            setClaimService(claimService);
    }
    
    public ClaimResult execute() throws DOMException, XPathExpressionException{
 
        this.element = XMLUtils.getElement(XMLUtils.getElement(XMLUtils.getElement(claimResult.getElement(), "claim"), "incident"), "injuries");
        this.injuryElements = XMLUtils.getElements(this.element.getOwnerDocument(), this.element, "injury");
            
        if(validate()){
            process();
        }
        
        doPrintResult(false);
        return claimResult;
    }
    
    private boolean validate() throws DOMException, XPathExpressionException{
        
        boolean isAllowToReadData = false;
        
        if(claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)){
            
            isAllowToReadData = true;
            this.claimResult.setCheckDataValid(true);

            for (Element e : this.injuryElements) {
                
                this.claimResult = NodeHelper.nodeValidate(sectionName, "name", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "address1", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "address2", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "address3", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "address4", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "address5", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "postcode", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "telephone-day", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "telephone-evening", e, this.claimResult, this.dataValidationParameter);
                this.claimResult = NodeHelper.nodeValidate(sectionName, "email", e, this.claimResult, this.dataValidationParameter);
            
            }
            
            isAllowToReadData = this.claimResult.isCheckDataValid();

        }
        
        return isAllowToReadData;
    }
    
    private void process() throws DOMException, XPathExpressionException{ 
    
        if(this.claimResult.getClaim().getIncident().getInjury()==null){
            this.claimResult.getClaim().getIncident().setInjury(new Injury());
        }
        
        for (Element e : this.injuryElements) {
            Injury injury = setInjury(e);
            
            if(injury!=null){
                
                this.claimResult.getClaim().getIncident().setInjury(injury);       
                
                InjurySolicitorValidation InjurySolicitorValidation = new InjurySolicitorValidation(claimResult, dataValidationParameter, claimService, e);
                this.claimResult = InjurySolicitorValidation.execute();   
                
                break;
            }
            
        }
        
    }

    private Injury setInjury(Element e){
        
        Injury obj = null;
        
        if(XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "name")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address1"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address2")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address3"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address4")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address5"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "postcode")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "telephone-day"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "telephone-evening")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "email"))
        ){
            obj = new  Injury();
            obj.setIncident(this.claimResult.getClaim().getIncident());
            obj.setName(XmlHelper.getNodeValue(e, "name"));
            obj.setAddress1(XmlHelper.getNodeValue(e, "address1"));
            obj.setAddress2(XmlHelper.getNodeValue(e, "address2"));
            obj.setAddress3(XmlHelper.getNodeValue(e, "address3"));
            obj.setAddress4(XmlHelper.getNodeValue(e, "address4"));
            obj.setAddress5(XmlHelper.getNodeValue(e, "address5"));
            obj.setEmail(XmlHelper.getEmailAddressFromNode(e, "email"));
            obj.setPostcode(XmlHelper.getNodeValue(e, "postcode"));
            obj.setTelephoneDay(XmlHelper.getNodeValue(e, "telephone-day"));
            obj.setTelephoneEvening(XmlHelper.getNodeValue(e, "telephone-evening"));
        }   
        
        return obj;
        
    }
    
    private void doPrintResult(boolean isAllowed){
        
        if(isAllowed){
            
            System.out.println("-------");
            System.out.println("::: "+sectionName + "| Status :"+this.claimResult.isDataValid());
            
            if(this.claimResult.getClaim().getIncident().getInjury()!=null){
                System.out.println("::: "+sectionName + "| getDate :"+this.claimResult.getClaim().getIncident().getInjury().getName());
                System.out.println("::: "+sectionName + "| getDate :"+this.claimResult.getClaim().getIncident().getInjury().getAddress1());
                System.out.println("::: "+sectionName + "| getDate :"+this.claimResult.getClaim().getIncident().getInjury().getAddress2());
                System.out.println("::: "+sectionName + "| getDate :"+this.claimResult.getClaim().getIncident().getInjury().getAddress3());
                System.out.println("::: "+sectionName + "| getDate :"+this.claimResult.getClaim().getIncident().getInjury().getAddress4());
                System.out.println("::: "+sectionName + "| getDate :"+this.claimResult.getClaim().getIncident().getInjury().getAddress5());
                System.out.println("::: "+sectionName + "| getDate :"+this.claimResult.getClaim().getIncident().getInjury().getEmail());
                System.out.println("::: "+sectionName + "| getDate :"+this.claimResult.getClaim().getIncident().getInjury().getPostcode());
                System.out.println("::: "+sectionName + "| getDate :"+this.claimResult.getClaim().getIncident().getInjury().getTelephoneDay());
                System.out.println("::: "+sectionName + "| getDate :"+this.claimResult.getClaim().getIncident().getInjury().getTelephoneEvening());     
            }else{
                System.out.println(sectionName + "| NO INJURY OBJECT HAVE FOUND!!");
            }
        }
    } 
}