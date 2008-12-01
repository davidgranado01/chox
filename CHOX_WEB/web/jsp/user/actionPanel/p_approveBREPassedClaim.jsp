


<%@ taglib uri="/struts-tags" prefix="s" %>

    <form onsubmit="return true;" action="user/approveBREPassedClaim.action" method="post" id="approveBREPassedClaim" name="approveBREPassedClaim">
    
    
    <fieldset class="x-fieldset">
        

        <div>
        
<div class="status-info">
    Please review the 'History' tab for details on why the claim has been rejected.  
    Please decide on whether to progress the claim for payment or reject the claim. 
    Please enter the required details/comments on the 'Invoice Details' tab regarding the decision made.
</div>
        
        
        
        
        <s:hidden name="id" />
        


        
        <div class="status-control-set-no-format">
        <span>Approval</span>   <s:radio name="actionName" list="actionNames" /> <input type="submit" value="Submit " />
    </div>
            
         
            
   


        
        
    </div>
    </fieldset>
    </form>    
