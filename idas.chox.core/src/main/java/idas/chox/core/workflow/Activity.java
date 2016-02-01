package idas.chox.core.workflow;

import idas.chox.core.model.Claim;

public interface Activity {

    public void setWorkflowContext(WorkflowContext processContext);

    public void process(Claim claim) throws Exception;

    public void processInBatch(Claim claim) throws Exception;

    public void setChainActivity(Activity nextActivity);
    
    public String getMessage();
    
    /*
     * this method is implemented in BaseActivity class and in default it return false.
     * this below method added to identify, from where the activity process is called eg.  (1. from the UI  2. from XML upload ) 
     * claims in CLAIM_AWAITING_CAR_HIRE_INFO status can be processed to next status from either UI or XML.
     * if the implemented activity process contains different check for different call ( UI OR XML ), then this method can be used.
     * this will be always false if nothing set.
     * for example this method is set to true in doProcessBordereauResult(ClaimResult claimResult, List<String> choReferences) method in UploadClaimXMLServiceImpl class,
     * this is used to identify the caller in ClaimAwaitingCarHireInfo class.
     * 
     */
    public void setXmlActivityProcessing(boolean xmlActivityProcessing);
    
//    public void setClaim(Claim claim);
}
