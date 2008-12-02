
<%-- 
    Document   : p_approveBREPassedClaim
    Created on : 02-Dec-2008, 17:11:54
    Author     : Emmanuel
--%>

<%@ taglib uri="/struts-tags" prefix="s" %>
    

<form onsubmit="return true;" action="user/approveBREPassedClaim.action" 
    method="post" id="approveBREPassedClaim" name="approveBREPassedClaim">
    <fieldset class="x-fieldset">
        <legend>BRE Approved Claim - Action Required</legend>
        <s:hidden name="id" />
        <div>
            <div class="status-info">             
             This claim and it's related invoice have been cleared by the CHOX approval system. 
             Please review the invoice and claim information supplied, and choose whether to clear the invoice for payment or reject the invoice "Clear for payment" or "Reject Invoice"  
            </div>
            <div class="status-control-set">
                <table>
                    <tr>
                        <td colspan="2">
                            <div class="no-format">
                                <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                <s:radio name="actionName" list="approveBREPassedClaimActions" />
                            </div>
                        </td>
                    </tr>
                    <tr> 
                        <td></td>
                        <td>
                            <input type="submit" value="Submit" />
                        </td>
                    </tr>
                </table>
            </div>
        </div> 
    </fieldset>
</form>