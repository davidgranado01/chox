
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
        <s:hidden id="actionName" name="actionName" />
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
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" class="choice">                     
                            <input type="submit" value="Reject Invoice"  onclick="registeAction('reject');return confirm('Are you sure you want to reject this invoice?')" />
                            <input type="submit" value="Clear for payment" onclick="registeAction('accept')"  />   
                            <input type="submit" value="Refer To Engineer" onclick="registeAction('refer');"  /> 
                        </td>
                    </tr>
                </table>
            </div>
        </div> 
    </fieldset>
</form>