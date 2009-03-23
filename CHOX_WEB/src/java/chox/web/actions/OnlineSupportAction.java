package chox.web.actions;

import chox.Util.DateHelper;
import chox.Util.EmailHelper;
import chox.model.Claim;
import chox.model.SupportMessage;
import chox.services.ClaimService;
import chox.services.SupportMessageService;

public class OnlineSupportAction extends BaseAction{
    
    private String[] recipients = {"choxsupport@sherwoodcompliance.co.uk"};
    //private String[] recipients = {"carlson.hoo@gmail.com","choxsupport@sherwoodcompliance.co.uk"};
    
    private SupportMessageService supportMessageService;
    private ClaimService claimService;
    private String iSupplierReference;
    private String iSubject;
    private String iMessage;
    private String actionResult;
    
    private static final String email_date_format = "dd MMMM yyyy";

    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }

    public String getIMessage() {
        return iMessage;
    }

    public void setIMessage(String iMessage) {
        this.iMessage = iMessage;
    }

    public String getISubject() {
        return iSubject;
    }

    public void setISubject(String iSubject) {
        this.iSubject = iSubject;
    }

    public String getISupplierReference() {
        return iSupplierReference;
    }

    public void setISupplierReference(String iSupplierReference) {
        this.iSupplierReference = iSupplierReference;
    }

    
    public String saveMessage() {
        
        SupportMessage message = new SupportMessage();
        message.setMessage(iMessage);
        message.setSubject(iSubject);
        message.setSupplierReference(iSupplierReference.trim());
        
        if(claimService.isClaimSupplierReferenceNumberExist(iSupplierReference)){
            Claim claim = claimService.getClaimByCHOReferenceNumber(iSupplierReference.trim());
            message.setClaimId(claim.getId());
        }
        
        boolean bFlag = false;

        try{
            EmailHelper emailHelper = new EmailHelper();
            String emailMessage = doConstructEmailMessage(message);
            bFlag = emailHelper.postMail(message.getSubject(), emailMessage, recipients);
        }catch(Exception ex){
            actionResult = "Please try again.";
        }
        
        if(bFlag){
            supportMessageService.updateObject(message);
            actionResult = "Your support request has been sent successfully. A member of the CHOX support team will be in touch shortly.";
        }else{
            actionResult = "Please try again.";
        }
        
        return SUCCESS;      
    }
    
    private String doConstructEmailMessage(SupportMessage message){
        
        // System.out.println("A:"+getAuthenticatedUser().getDisplayName()); 
        // System.out.println("B:"+DateHelper.getCurrentDate()); 
        
        StringBuffer emailMsg = new StringBuffer();
        emailMsg.append("======================================================================\n"); 
        emailMsg.append("Submitted By: " + getAuthenticatedUser().getDisplayName());
        emailMsg.append("\n");
        emailMsg.append("Date: " + DateHelper.getCurrentDateWithFormat(email_date_format));
        emailMsg.append("\n");
        emailMsg.append("======================================================================\n"); 
        emailMsg.append("Supplier Reference Number: " + message.getSupplierReference());
        emailMsg.append("\n");  
        emailMsg.append("Subject: " + message.getSubject());
        emailMsg.append("\n");  
        emailMsg.append("======================================================================\n");  
        emailMsg.append(message.getMessage());
        emailMsg.append("\n");
        emailMsg.append("======================================================================\n");  
        
        return emailMsg.toString();
    }
    
    public void setSupportMessageService(SupportMessageService supportMessageService) {
        this.supportMessageService = supportMessageService;
    }
    
    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
    
    @Override
    public String execute()
    {
        return SUCCESS;
    }  
    
}
