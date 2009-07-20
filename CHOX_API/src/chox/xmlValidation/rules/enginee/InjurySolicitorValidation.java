package chox.xmlValidation.rules.enginee;

import chox.model.Solicitor;
import chox.services.ClaimService;
import chox.services.SecureDataService;
import chox.xmlValidation.model.ClaimResult;
import chox.xmlValidation.model.status.ClaimParseStatus;
import chox.xmlValidation.rules.DataValidationParameter;
import chox.xmlValidation.rules.Util.NodeHelper;
import chox.xmlValidation.rules.Util.XmlHelper;
import chox.xmlValidation.rules.rulesInterface;
import com.filesystemsoftware.utils.XMLUtils;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;

public class InjurySolicitorValidation extends SecureDataService implements rulesInterface{
    
    protected static String sectionName = "Injury Solicitor";
    private DataValidationParameter dataValidationParameter;
    private ClaimService claimService;
    private ClaimResult claimResult;
    private Element rootElement;
    
    // THIS PAGE ONLY
    private Element element;
    

    public void setRootElement(Element rootElement) { this.rootElement = rootElement; }
    public void setClaimResult(ClaimResult claimResult) { this.claimResult = claimResult; }
    public void setClaimService(ClaimService claimService) { this.claimService = claimService; }
    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) { this.dataValidationParameter = dataValidationParameter; }
    
    public InjurySolicitorValidation(
            ClaimResult claimResult, 
            DataValidationParameter dataValidationParameter, 
            ClaimService claimService, 
            Element rootElement){
        
            setClaimResult(claimResult);
            setDataValidationParameter(dataValidationParameter);
            setClaimService(claimService);
            setRootElement(rootElement);
    }
    
    public ClaimResult execute() throws DOMException, XPathExpressionException{
        
        this.element = XMLUtils.getElement(rootElement, "solicitor");
        
        if(validate()){
            process();
        }

        doPrintResult(false);
        return this.claimResult;
    }
    
    private boolean validate() throws DOMException, XPathExpressionException{
        
        boolean isAllowToReadData = false;
        
        if(claimResult.getClaimParseStatus().equals(ClaimParseStatus.newClaim)){
            
            isAllowToReadData = true;
            this.claimResult.setCheckDataValid(true);
            
            this.claimResult = NodeHelper.nodeValidate(sectionName, "name", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address1", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address2", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address3", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address4", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address5", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "postcode", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "telephone", this.element, this.claimResult, this.dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "email", this.element, this.claimResult, this.dataValidationParameter);
            
            isAllowToReadData = this.claimResult.isDataValid();
            
        }
        
        return isAllowToReadData;
    }

    private void process(){

        if(this.claimResult.getClaim().getIncident().getInjury().getSolicitor()==null){
            this.claimResult.getClaim().getIncident().getInjury().setSolicitor(new Solicitor());
        }

        if(XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "name"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address1"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address2"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address3"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address4"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "address5"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "postcode"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "telephone"))
            || XmlHelper.isNotNull(XmlHelper.getNodeValue(this.element, "email"))){

            this.claimResult.getClaim().getIncident().getInjury().getSolicitor().setInjury(this.claimResult.getClaim().getIncident().getInjury());
            this.claimResult.getClaim().getIncident().getInjury().getSolicitor().setAddress1(XmlHelper.getNodeValue(this.element, "address1"));
            this.claimResult.getClaim().getIncident().getInjury().getSolicitor().setAddress2(XmlHelper.getNodeValue(this.element, "address2"));
            this.claimResult.getClaim().getIncident().getInjury().getSolicitor().setAddress3(XmlHelper.getNodeValue(this.element, "address3"));
            this.claimResult.getClaim().getIncident().getInjury().getSolicitor().setAddress4(XmlHelper.getNodeValue(this.element, "address4"));
            this.claimResult.getClaim().getIncident().getInjury().getSolicitor().setAddress5(XmlHelper.getNodeValue(this.element, "address5"));
            this.claimResult.getClaim().getIncident().getInjury().getSolicitor().setEmail(XmlHelper.getEmailAddressFromNode(this.element, "email"));
            this.claimResult.getClaim().getIncident().getInjury().getSolicitor().setName(XmlHelper.getNodeValue(this.element, "name"));
            this.claimResult.getClaim().getIncident().getInjury().getSolicitor().setPostcode(XmlHelper.getNodeValue(this.element, "postcode"));
            this.claimResult.getClaim().getIncident().getInjury().getSolicitor().setTelephone(XmlHelper.getNodeValue(this.element, "telephone"));

        }

    }

    private void doPrintResult(boolean isAllowed){
        
        if(isAllowed){
            
            System.out.println("-------");
            System.out.println("::: ::: "+sectionName + "| Status :"+this.claimResult.isDataValid());
            
            if(this.claimResult.getClaim().getIncident().getInjury().getSolicitor()!=null){   
                
                System.out.println("::: ::: "+sectionName + "| getAddress1 :"+this.claimResult.getClaim().getIncident().getInjury().getSolicitor().getAddress1());
                System.out.println("::: ::: "+sectionName + "| getAddress2 :"+this.claimResult.getClaim().getIncident().getInjury().getSolicitor().getAddress2());
                System.out.println("::: ::: "+sectionName + "| getAddress3 :"+this.claimResult.getClaim().getIncident().getInjury().getSolicitor().getAddress3());
                System.out.println("::: ::: "+sectionName + "| getAddress4 :"+this.claimResult.getClaim().getIncident().getInjury().getSolicitor().getAddress4());
                System.out.println("::: ::: "+sectionName + "| getAddress5 :"+this.claimResult.getClaim().getIncident().getInjury().getSolicitor().getAddress5());
                System.out.println("::: ::: "+sectionName + "| getEmail :"+this.claimResult.getClaim().getIncident().getInjury().getSolicitor().getEmail());
                System.out.println("::: ::: "+sectionName + "| getName :"+this.claimResult.getClaim().getIncident().getInjury().getSolicitor().getName());
                System.out.println("::: ::: "+sectionName + "| getPostcode :"+this.claimResult.getClaim().getIncident().getInjury().getSolicitor().getPostcode());
                System.out.println("::: ::: "+sectionName + "| getTelephone :"+this.claimResult.getClaim().getIncident().getInjury().getSolicitor().getTelephone());
            }else{
            System.out.println(sectionName + "| NO SOLICITOR OBJECT HAVE FOUND!!");
            }
        }
        
    } 

}