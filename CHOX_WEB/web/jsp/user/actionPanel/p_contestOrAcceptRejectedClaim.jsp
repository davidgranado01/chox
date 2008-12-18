<%-- 
    Document   :p_contestOrAcceptRejectedClaim
    Created on : Dec 02, 2008, 11:39:12 AM
    Author     : Emmanuel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<form onsubmit="return true;" action="user/contestOrAcceptRejectedClaim.action" method="post" 
      id="contestOrAcceptRejectedClaim" name="contestOrAcceptRejectedClaim">
    <fieldset class="x-fieldset">
        <legend>Rejected Claim - Action Required</legend>
        <s:hidden name="id" />
        <s:hidden id="actionName" name="actionName" />
        <div>
            <div class="status-info">
               Please review the Insurer's notes against rejection reasoning and decide whether to accept or reject the Insurer's rejection decision.  
               Please include supporting notes on the decision made using the 'Notes' tab.
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
                            <input type="submit" value="Contest this claim"  onclick="registeAction('reject');" />
                            <input type="submit" value="Accept rejection decision" onclick="registeAction('accept')"  />   
                        </td>
                    </tr>
                </table>
            </div>
            <div class="errorBox" id="ActionPanelMessageBox"></div>
        </div> 
    </fieldset>
</form>
