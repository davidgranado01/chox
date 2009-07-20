package chox.xmlValidation.rules.enginee;

import chox.model.Witness;
import chox.services.ChoBandService;
import chox.services.ChorganisationService;
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

public class IncidentWitnessValidation extends SecureDataService implements rulesInterface{
    
    protected static String sectionName = "Incident Witness";
    private DataValidationParameter dataValidationParameter;
    private ClaimService claimService;
    private ClaimResult claimResult;
    
    // FOR THIS PAGE ONLY
    private Element element;
    ArrayList<Element> witnessElements;

    public void setClaimResult(ClaimResult claimResult) { this.claimResult = claimResult; }
    public void setClaimService(ClaimService claimService) { this.claimService = claimService; }
    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) { this.dataValidationParameter = dataValidationParameter; }
    
    public IncidentWitnessValidation(
            ClaimResult claimResult, 
            DataValidationParameter dataValidationParameter, 
            ClaimService claimService){
        
            setClaimResult(claimResult);
            setDataValidationParameter(dataValidationParameter);
            setClaimService(claimService);
    }
    
    public ClaimResult execute() throws DOMException, XPathExpressionException{
 
        this.element = XMLUtils.getElement(XMLUtils.getElement(XMLUtils.getElement(claimResult.getElement(), "claim"), "incident"), "witnesses");
        this.witnessElements = XMLUtils.getElements(this.element.getOwnerDocument(), this.element, "witness");
        
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
            
            for (Element e : this.witnessElements) {
                
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
    
    private void process(){
        
        if(this.claimResult.getClaim().getIncident().getWitness()==null){
            this.claimResult.getClaim().getIncident().setWitness(new Witness());
        }
        
        ArrayList<Witness> witnesses = new ArrayList<Witness>();
        for (Element e : this.witnessElements) {
            Witness witness = setWitness(e);
            
            if(witness!=null){
                witnesses.add(witness);
            }
        }
        
        if(witnesses.size()>0){
            this.claimResult.getClaim().getIncident().setWitness(witnesses.get(0));
        }
    }
    
    private Witness setWitness(Element e){
        
        Witness obj = null;
        
            if(XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "name")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address1"))
                || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address2")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address3"))
                || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address4")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "address5"))
                || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "postcode")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "telephone-day"))
                || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "telephone-evening")) || XmlHelper.isNotNull(XmlHelper.getNodeValue(e, "email"))
            ){
            
            obj = new  Witness();
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
            
            System.out.println("::: -------");
            System.out.println("::: "+sectionName + "| Status :"+this.claimResult.isDataValid());
            
            if(this.claimResult.getClaim().getIncident().getWitness()!=null){
                System.out.println("::: "+sectionName + "| getName :"+this.claimResult.getClaim().getIncident().getWitness().getName());
                System.out.println("::: "+sectionName + "| getAddress1 :"+this.claimResult.getClaim().getIncident().getWitness().getAddress1());
                System.out.println("::: "+sectionName + "| getAddress2 :"+this.claimResult.getClaim().getIncident().getWitness().getAddress2());
                System.out.println("::: "+sectionName + "| getAddress3 :"+this.claimResult.getClaim().getIncident().getWitness().getAddress3());
                System.out.println("::: "+sectionName + "| getAddress4 :"+this.claimResult.getClaim().getIncident().getWitness().getAddress4());
                System.out.println("::: "+sectionName + "| getAddress5 :"+this.claimResult.getClaim().getIncident().getWitness().getAddress5());
                System.out.println("::: "+sectionName + "| getEmail :"+this.claimResult.getClaim().getIncident().getWitness().getEmail());
                System.out.println("::: "+sectionName + "| getPostcode :"+this.claimResult.getClaim().getIncident().getWitness().getPostcode());
                System.out.println("::: "+sectionName + "| getTelephoneDay :"+this.claimResult.getClaim().getIncident().getWitness().getTelephoneDay());
                System.out.println("::: "+sectionName + "| getTelephoneEvening :"+this.claimResult.getClaim().getIncident().getWitness().getTelephoneEvening());
            }else{
                System.out.println(sectionName + "| NO WITNESS OBJECT HAVE FOUND!!");
            }
        }
    }  
}