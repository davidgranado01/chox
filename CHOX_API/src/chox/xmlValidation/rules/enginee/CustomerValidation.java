/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.xmlValidation.rules.enginee;

import chox.Util.TextHelper;
import chox.model.Customer;
import chox.services.ChoBandService;
import chox.services.ChorganisationService;
import chox.services.ClaimService;
import chox.services.SecureDataService;
import chox.services.ClaimResult;
import chox.xmlValidation.model.status.ClaimParseStatus;
import chox.xmlValidation.rules.DataValidationParameter;
import chox.xmlValidation.rules.Util.NodeHelper;
import chox.xmlValidation.rules.Util.XmlHelper;
import chox.xmlValidation.rules.rulesInterface;
import com.filesystemsoftware.utils.XMLUtils;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

public class CustomerValidation extends SecureDataService implements rulesInterface{
    
    protected static String sectionName = "Customer";
    private DataValidationParameter dataValidationParameter;
    private ClaimService claimService;
    private ClaimResult claimResult;
    private Element element;
    
    public void setClaimResult(ClaimResult claimResult) { this.claimResult = claimResult; }
    public void setClaimService(ClaimService claimService) { this.claimService = claimService; }
    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) { this.dataValidationParameter = dataValidationParameter; }
    
    public CustomerValidation(ClaimResult claimResult, DataValidationParameter dataValidationParameter, 
            ClaimService claimService){
        
        setClaimResult(claimResult);
        setDataValidationParameter(dataValidationParameter);
        setClaimService(claimService); 
    }
    
    public ClaimResult execute() throws DOMException, XPathExpressionException{
        
        Element rootElement = XMLUtils.getElement(claimResult.getElement(), "drivers");
        this.element = XMLUtils.getElement(rootElement, "driver");
        
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
            
            this.claimResult = NodeHelper.nodeValidate(sectionName, "title", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "firstnames", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "lastname", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address1", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address2", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address3", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address4", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "address5", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "postcode", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "telephone-day", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "telephone-evening", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "email", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "age", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "occupation", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "policy-usage", this.element, this.claimResult, dataValidationParameter);
            this.claimResult = NodeHelper.nodeValidate(sectionName, "primary-driver", this.element, this.claimResult, dataValidationParameter);
            
            isAllowToReadData = this.claimResult.isCheckDataValid();

        }

        return isAllowToReadData;
        
    }    
    
    private void process(){
        
        if(this.claimResult.getClaim().getCustomer()==null){
            this.claimResult.getClaim().setCustomer(new Customer());
        }
        
        this.claimResult.getClaim().getCustomer().setTitle(XmlHelper.getNodeValue(this.element, "title"));
        this.claimResult.getClaim().getCustomer().setFirstName(XmlHelper.getNodeValue(this.element, "firstnames"));
        this.claimResult.getClaim().getCustomer().setLastName(XmlHelper.getNodeValue(this.element, "lastname"));
        this.claimResult.getClaim().getCustomer().setAddress1(XmlHelper.getNodeValue(this.element, "address1"));
        this.claimResult.getClaim().getCustomer().setAddress2(XmlHelper.getNodeValue(this.element, "address2"));
        this.claimResult.getClaim().getCustomer().setAddress3(XmlHelper.getNodeValue(this.element, "address3"));
        this.claimResult.getClaim().getCustomer().setAddress4(XmlHelper.getNodeValue(this.element, "address4"));
        this.claimResult.getClaim().getCustomer().setAddress5(XmlHelper.getNodeValue(this.element, "address5"));
        this.claimResult.getClaim().getCustomer().setPostcode(XmlHelper.getNodeValue(this.element, "postcode"));
        this.claimResult.getClaim().getCustomer().setTelephoneDay(XmlHelper.getNodeValue(this.element, "telephone-day"));
        this.claimResult.getClaim().getCustomer().setTelephoneEvening(XmlHelper.getNodeValue(this.element, "telephone-evening"));
        this.claimResult.getClaim().getCustomer().setEmail(XmlHelper.getEmailAddressFromNode(this.element, "email"));
        this.claimResult.getClaim().getCustomer().setIsPrimaryDriver(true);
        this.claimResult.getClaim().getCustomer().setAge(XmlHelper.getIntegerFromNode(this.element, "age"));
        this.claimResult.getClaim().getCustomer().setOccupation(XmlHelper.getNodeValue(this.element, "occupation"));
        this.claimResult.getClaim().getCustomer().setPolicyUsage(XmlHelper.getNodeValue(this.element, "policy-usage"));
        
    }
    
    private void doPrintResult(boolean isAllowed){
        if(isAllowed){
            
            System.out.println("-------");
            System.out.println(sectionName + "| Status :"+this.claimResult.isDataValid());
            
            if(this.claimResult.getClaim().getCustomer()!=null){
                
                System.out.println(sectionName + "| getTitle :"+this.claimResult.getClaim().getCustomer().getTitle());
                System.out.println(sectionName + "| getFirstName :"+this.claimResult.getClaim().getCustomer().getFirstName());
                System.out.println(sectionName + "| getLastName :"+this.claimResult.getClaim().getCustomer().getLastName());
                System.out.println(sectionName + "| getAddress1 :"+this.claimResult.getClaim().getCustomer().getAddress1());
                System.out.println(sectionName + "| getAddress2 :"+this.claimResult.getClaim().getCustomer().getAddress2());
                System.out.println(sectionName + "| getAddress3 :"+this.claimResult.getClaim().getCustomer().getAddress3());
                System.out.println(sectionName + "| getAddress4 :"+this.claimResult.getClaim().getCustomer().getAddress4());
                System.out.println(sectionName + "| getAddress5 :"+this.claimResult.getClaim().getCustomer().getAddress5());
                System.out.println(sectionName + "| getPostcode :"+this.claimResult.getClaim().getCustomer().getPostcode());
                System.out.println(sectionName + "| getTelephoneDay :"+this.claimResult.getClaim().getCustomer().getTelephoneDay());
                System.out.println(sectionName + "| getTelephoneEvening :"+this.claimResult.getClaim().getCustomer().getTelephoneEvening());
                System.out.println(sectionName + "| getEmail :"+this.claimResult.getClaim().getCustomer().getEmail());
                System.out.println(sectionName + "| isIsPrimaryDriver :"+this.claimResult.getClaim().getCustomer().isIsPrimaryDriver());
                System.out.println(sectionName + "| getAge :"+this.claimResult.getClaim().getCustomer().getAge());
                System.out.println(sectionName + "| getOccupation :"+this.claimResult.getClaim().getCustomer().getOccupation());
                System.out.println(sectionName + "| getPolicyUsage :"+this.claimResult.getClaim().getCustomer().getPolicyUsage());
            }else{
                System.out.println(sectionName + "| NO CUSTOMER OBJECT HAVE FOUND!!");
            }
        }
    }
}
