<%-- 
    Document   : p_resubmitOrAcceptContestedInvoice
    Created on : Dec 02, 2008, 11:39:12 AM
    Author     : Emmanuel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<script language="JavaScript">
    
    $(document).ready(function(){
            
        $("#resubmitOrAcceptContestedInvoice").validate(
        {
            errorLabelContainer: "#ActionPanelMessageBox",                
            rules: {
                actionName:{required:true}
            },
            messages: {
                
                actionName:{required:"You must select action"}
            }
            
        });
    });
    
</script>

<form onsubmit="return true;" action="user/resubmitOrAcceptContestedInvoice.action" method="post" 
      id="resubmitOrAcceptContestedInvoice" name="resubmitOrAcceptContestedInvoice">
    <fieldset class="x-fieldset">
        <legend>Contested Invoice - Action Required</legend>
        <s:hidden name="id" />
        <s:hidden id="actionName" name="actionName" />
        <div>
            <div class="status-info">
                Please review the 'Notes' tab for details regarding the rejection reasoning made by the Insurer. 
                Decide whether to accept or reject the Insurer's rejection decision.  
                Rejection of the Insurer's decision will require a modification to the claim details and/or the attachment of a Payment Pack.
                Please include supporting notes on the decision made using the 'Notes' tab.
            </div>
            <div>
                <table>
                    <tr>
                        <td>
                            <div>
                                <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                            </div>
                        </td>
                    </tr>
                    <tr>                         
                        <td>
                            <input type="submit" value="Reject Decision and Resubmit"  onclick="registeAction('reject');" />
                            <input type="submit" value="Accept Rejection Decision" onclick="registeAction('accept')"  /> 
                        </td>
                    </tr>
                </table>
            </div>
            <div class="errorBox" id="ActionPanelMessageBox"></div>
        </div> 
    </fieldset>
</form>
