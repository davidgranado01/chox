<%-- 
    Document   : p_approveContestedClaim
    Created on : 01-Dec-2008, 02:28:00
    Author     : Emmanuel
--%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<form onsubmit="return true;" action="user/approveContestedClaim.action" method="post" 
      id="approveContestedClaim" name="approveContestedClaim">
    <fieldset class="x-fieldset">
        <legend>Contested Claim - Action Required</legend>
        <s:hidden name="id" />
        <s:hidden id="actionName" name="actionName" />
        <div>
            <div class="status-info">
                Please review the CHO's notes against the reasoning for contesting the claim rejection and make a decision on whether to acknowledge or reject the claim.
            </div>
            <div class="status-control-set">
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
                            <input type="submit" value="Reject this claim"  onclick="registeAction('reject');return confirm('Are you sure you want to reject this claim?')" />
                            <input type="submit" value="Request invoice data" onclick="registeAction('accept')"  />  
                        </td>
                    </tr>
                </table>
            </div>
        </div> 
    </fieldset>
</form>
