<%-- 
    Document   : p_approveContestedClaim
    Created on : 01-Dec-2008, 02:28:00
    Author     : Emmanuel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<form onsubmit="return true;" action="user/approveContestedClaim.action" method="post" 
      id="approveContestedClaim" name="approveContestedClaim">
    <fieldset class="x-fieldset">
        <legend>Contested Claim - Action Required</legend>
        <s:hidden name="id" />
        <div>
            <div class="status-info">
                Please review the CHO's notes against the reasoning for contesting the claim rejection and make a decision on whether to acknowledge or reject the claim.
            </div>
            <div class="status-control-set">
                <table>
                    <tr>
                        <td colspan="2">
                            <div class="no-format">
                                <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                <s:radio name="actionName" list="approveContestedClaimActions" />
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
