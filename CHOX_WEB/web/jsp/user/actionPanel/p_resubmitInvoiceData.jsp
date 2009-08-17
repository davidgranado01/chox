
<%@ taglib uri="/struts-tags" prefix="s" %>





<form onsubmit="return true;" action="user/reSubmitRejectedClaim.action" method="post" id="route" name="route">
    
    
    
    
    <fieldset class="x-fieldset"><legend>Claim Data - Action Required</legend>                
        
        <div>
            <div class="status-info">
                Please review the 'History' tab for details on the why the claim has been rejected, amend details accordingly and re-submit.
            </div>
            <s:hidden name="id" />
                
            <div class="status-info-submit">
                <table>
                    <tr>
                        <td>
                            <div class="no-format">
                                <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                            </div>
                        </td>
                    </tr>
                    <tr>                        
                        <td>
                           <input type="submit" value="Re-Submit Claim/Invoice" />                    </td>
                    </tr>
                </table>                
            </div>
        </div>
    </fieldset> 
        
</form>    
    


    
