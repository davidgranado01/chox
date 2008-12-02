
<%@ taglib uri="/struts-tags" prefix="s" %>





<form onsubmit="return true;" action="user/reSubmitRejectedClaim.action" method="post" id="route" name="route">
    
    
    
    
    <fieldset class="x-fieldset"><legend>Claim Data - Action Required</legend>                
        
        <div>
            <div class="status-info">
                Please review the 'History' tab for details on the why the claim has been rejected, amend details accordingly and re-submit.
            </div>
            <s:hidden name="id" />
                
            <div class="status-info-submit">
                <input type="submit" value="Re-submit Claim / Invoice" />
            </div>
        </div>
    </fieldset> 
        
</form>    
    


    
