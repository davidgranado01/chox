package chox.xmlValidation.rules.enginee;

import chox.model.Invoice;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;

public class InvoiceExtraValidation extends SecureDataService implements rulesInterface{
    
    protected static String sectionName = "Invoice Extra";
    private DataValidationParameter dataValidationParameter;
    private ClaimService claimService;
    private ChorganisationService chorganisationService;
    private ChoBandService choBandService;
    private ClaimResult claimResult;
    
    private ArrayList<Element> elements;
    private Element element;

    public void setElement(Element element) { this.element = element; }
    public void setElements(ArrayList<Element> elements) { this.elements = elements; }
    public void setChoBandService(ChoBandService choBandService) { this.choBandService = choBandService; }
    public void setChorganisationService(ChorganisationService chorganisationService) { this.chorganisationService = chorganisationService; }
    public void setClaimResult(ClaimResult claimResult) { this.claimResult = claimResult; }
    public void setClaimService(ClaimService claimService) { this.claimService = claimService; }
    public void setDataValidationParameter(DataValidationParameter dataValidationParameter) { this.dataValidationParameter = dataValidationParameter; }
    
    public InvoiceExtraValidation(
            ClaimResult claimResult, 
            DataValidationParameter dataValidationParameter, 
            ClaimService claimService, 
            ChorganisationService chorganisationService, 
            ChoBandService choBandService){
        
            setClaimResult(claimResult);
            setDataValidationParameter(dataValidationParameter);
            setClaimService(claimService);
            setChorganisationService(chorganisationService);
            setChoBandService(choBandService);    
    }
    
    public ClaimResult execute() throws DOMException, XPathExpressionException{
        
        Element root = XMLUtils.getElement(this.claimResult.getElement(), "invoice");
        this.element = XMLUtils.getElement(root, "extras");
        this.elements = XMLUtils.getElements(this.element.getOwnerDocument(), this.element, "extra");
            
        doPreInitialize();
                
        if(validate()){
            process();
        }
        
        doPrintResult(false);
        return claimResult;
    }
    
    private boolean validate() throws DOMException, XPathExpressionException{
        
        boolean isAllowToReadData = false;
        
        if((claimResult.getClaimParseStatus().equals(ClaimParseStatus.newInvoice))
                && ((XMLUtils.getElement(this.element, "extra").getTextContent()).trim().length()>0)){
            
            isAllowToReadData = true; 
            
            for (Element ee : this.elements) {

                String strExtraName = XmlHelper.getNodeValue(ee, "name");
                String strExtraFee =  strExtraName+" Fee";
                String strExtraQty =  strExtraName+" Quantity";

                this.claimResult = NodeHelper.nodeValidateDefaultDescription(sectionName, "name", this.element, claimResult, dataValidationParameter, strExtraName);
                this.claimResult = NodeHelper.nodeValidateDefaultDescription(sectionName, "quantity", this.element, claimResult, dataValidationParameter, strExtraQty);
                this.claimResult = NodeHelper.nodeValidateDefaultDescription(sectionName, "item-cost", this.element, claimResult, dataValidationParameter, strExtraFee);
            }
            
            if(!this.claimResult.isCheckDataValid()){
                isAllowToReadData = false;
                this.claimResult.setValid(false);
                this.claimResult.setDataValid(false);                 
            }
        }

        return isAllowToReadData;
    }
    
    private void process(){ 
        
        for (Element ee : this.elements) {
            
            String sExtra = XmlHelper.getNodeValue(ee, "name");
            Integer iQuantity = XmlHelper.getIntegerFromNode(ee, "quantity");
            BigDecimal dIntemCost = XmlHelper.getBigDecimalFromNode(ee, "item-cost");
            setExtraItem(sExtra, iQuantity, dIntemCost);   
            
        }
    }

    private void doPrintResult(boolean isAllowed){   
        
        if(isAllowed){
            System.out.println("-------");
            System.out.println(sectionName + "|is Claim Valid?: " + this.claimResult.isValid());
            System.out.println(sectionName + "|is Claim Data valid?: " + this.claimResult.isDataValid());
            System.out.println(sectionName + "|Claim Process Status: " + this.claimResult.getClaimParseStatus());
            System.out.println(sectionName + "|Claim Process Msg Size: " + this.claimResult.getMessage().size());
            System.out.println(sectionName + "|Claim Data >getCdwFee :"+claimResult.getClaim().getInvoice().getCdwFee());
            System.out.println(sectionName + "|Claim Data >getCdwQty :"+claimResult.getClaim().getInvoice().getCdwQty());
            System.out.println(sectionName + "|Claim Data >getAdminFee :"+claimResult.getClaim().getInvoice().getAdminFee());
            System.out.println(sectionName + "|Claim Data >getAdminQty :"+claimResult.getClaim().getInvoice().getAdminQty());
            System.out.println(sectionName + "|Claim Data >getAutomaticFee :"+claimResult.getClaim().getInvoice().getAutomaticFee());
            System.out.println(sectionName + "|Claim Data >getAutomaticQty :"+claimResult.getClaim().getInvoice().getAutomaticQty());
            System.out.println(sectionName + "|Claim Data >getBabySeatFee :"+claimResult.getClaim().getInvoice().getBabySeatFee());
            System.out.println(sectionName + "|Claim Data >getBabySeatQty :"+claimResult.getClaim().getInvoice().getBabySeatQty());
            System.out.println(sectionName + "|Claim Data >getDeliveryCollectionFee :"+claimResult.getClaim().getInvoice().getDeliveryCollectionFee());
            System.out.println(sectionName + "|Claim Data >getDeliveryCollectionQty :"+claimResult.getClaim().getInvoice().getDeliveryCollectionQty());
            System.out.println(sectionName + "|Claim Data >getDualControlFee :"+claimResult.getClaim().getInvoice().getDualControlFee());
            System.out.println(sectionName + "|Claim Data >getDualControlQty :"+claimResult.getClaim().getInvoice().getDualControlQty());
            System.out.println(sectionName + "|Claim Data >getEstateFee :"+claimResult.getClaim().getInvoice().getEstateFee());
            System.out.println(sectionName + "|Claim Data >getEstateQty :"+claimResult.getClaim().getInvoice().getEstateQty());
            System.out.println(sectionName + "|Claim Data >getNonStandardInsurancePremiumFee :"+claimResult.getClaim().getInvoice().getNonStandardInsurancePremiumFee());
            System.out.println(sectionName + "|Claim Data >getNonStandardInsurancePremiumQty :"+claimResult.getClaim().getInvoice().getNonStandardInsurancePremiumQty());
            System.out.println(sectionName + "|Claim Data >getRoofRackFee :"+claimResult.getClaim().getInvoice().getRoofRackFee());
            System.out.println(sectionName + "|Claim Data >getRoofRackQty :"+claimResult.getClaim().getInvoice().getRoofRackQty());
            System.out.println(sectionName + "|Claim Data >getSatNavFee :"+claimResult.getClaim().getInvoice().getSatNavFee());
            System.out.println(sectionName + "|Claim Data >getSatNavQty :"+claimResult.getClaim().getInvoice().getSatNavQty());
            System.out.println(sectionName + "|Claim Data >getTowBarsFee :"+claimResult.getClaim().getInvoice().getTowBarsFee());
            System.out.println(sectionName + "|Claim Data >getTowBarsQty :"+claimResult.getClaim().getInvoice().getTowBarsQty());
        }
    } 
    
    private void setExtraItem(String nodeName, Integer iQuantity, BigDecimal dIntemCost){
    
        if(nodeName.equalsIgnoreCase("CDW")){
            this.claimResult.getClaim().getInvoice().setCdwFee(dIntemCost);
            this.claimResult.getClaim().getInvoice().setCdwQty(iQuantity);                        
        }else if(nodeName.equalsIgnoreCase("Admin")){
            this.claimResult.getClaim().getInvoice().setAdminFee(dIntemCost);
            this.claimResult.getClaim().getInvoice().setAdminQty(iQuantity);
        }else if(nodeName.equalsIgnoreCase("Automatic")){
            this.claimResult.getClaim().getInvoice().setAutomaticFee(dIntemCost);
            this.claimResult.getClaim().getInvoice().setAutomaticQty(iQuantity);   
        }else if(nodeName.equalsIgnoreCase("Baby Seat")){
            this.claimResult.getClaim().getInvoice().setBabySeatFee(dIntemCost);
            this.claimResult.getClaim().getInvoice().setBabySeatQty(iQuantity);                         
        }else if(nodeName.equalsIgnoreCase("Delivery Collection")){
            this.claimResult.getClaim().getInvoice().setDeliveryCollectionFee(dIntemCost);
            this.claimResult.getClaim().getInvoice().setDeliveryCollectionQty(iQuantity);                         
        }else if(nodeName.equalsIgnoreCase("Dual Control")){
            this.claimResult.getClaim().getInvoice().setDualControlFee(dIntemCost);
            this.claimResult.getClaim().getInvoice().setDualControlQty(iQuantity);                            
        }else if(nodeName.equalsIgnoreCase("Estate")){
            this.claimResult.getClaim().getInvoice().setEstateFee(dIntemCost);
            this.claimResult.getClaim().getInvoice().setEstateQty(iQuantity);                          
        }else if(nodeName.equalsIgnoreCase("Non-standard Risk Insurance Premium")){
            this.claimResult.getClaim().getInvoice().setNonStandardInsurancePremiumFee(dIntemCost);
            this.claimResult.getClaim().getInvoice().setNonStandardInsurancePremiumQty(iQuantity);                          
        }else if(nodeName.equalsIgnoreCase("Roof Rack")){
            this.claimResult.getClaim().getInvoice().setRoofRackFee(dIntemCost);
            this.claimResult.getClaim().getInvoice().setRoofRackQty(iQuantity);                          
        }else if(nodeName.equalsIgnoreCase("Sat Nav")){
            this.claimResult.getClaim().getInvoice().setSatNavFee(dIntemCost);
            this.claimResult.getClaim().getInvoice().setSatNavQty(iQuantity);                          
        }else if(nodeName.equalsIgnoreCase("Tow Bars")){
            this.claimResult.getClaim().getInvoice().setTowBarsFee(dIntemCost);
            this.claimResult.getClaim().getInvoice().setTowBarsQty(iQuantity);                          
        }
        
    }
    
    private void doPreInitialize(){
    System.out.println(">>> InvoiceExtraValidation 001");
        this.claimResult.getClaim().getInvoice().setCdwFee(new BigDecimal("0.00"));
        this.claimResult.getClaim().getInvoice().setCdwQty(0);
        this.claimResult.getClaim().getInvoice().setAdminFee(new BigDecimal("0.00"));
        this.claimResult.getClaim().getInvoice().setAdminQty(0);
        this.claimResult.getClaim().getInvoice().setAutomaticFee(new BigDecimal("0.00"));
        this.claimResult.getClaim().getInvoice().setAutomaticQty(0);   
        this.claimResult.getClaim().getInvoice().setBabySeatFee(new BigDecimal("0.00"));
        this.claimResult.getClaim().getInvoice().setBabySeatQty(0); 
        this.claimResult.getClaim().getInvoice().setDeliveryCollectionFee(new BigDecimal("0.00"));
        this.claimResult.getClaim().getInvoice().setDeliveryCollectionQty(0); 
        this.claimResult.getClaim().getInvoice().setDualControlFee(new BigDecimal("0.00"));
        this.claimResult.getClaim().getInvoice().setDualControlQty(0);    
        this.claimResult.getClaim().getInvoice().setEstateFee(new BigDecimal("0.00"));
        this.claimResult.getClaim().getInvoice().setEstateQty(0);  
        this.claimResult.getClaim().getInvoice().setNonStandardInsurancePremiumFee(new BigDecimal("0.00"));
        this.claimResult.getClaim().getInvoice().setNonStandardInsurancePremiumQty(0);  
        this.claimResult.getClaim().getInvoice().setRoofRackFee(new BigDecimal("0.00"));
        this.claimResult.getClaim().getInvoice().setRoofRackQty(0);  
        this.claimResult.getClaim().getInvoice().setSatNavFee(new BigDecimal("0.00"));
        this.claimResult.getClaim().getInvoice().setSatNavQty(0);  
        this.claimResult.getClaim().getInvoice().setTowBarsFee(new BigDecimal("0.00"));
        this.claimResult.getClaim().getInvoice().setTowBarsQty(0); 
        System.out.println(">>> InvoiceExtraValidation 002");
    }

}