<%-- 
    Document   : p_approveClaim
    Created on : 01-Dec-2008, 02:28:00
    Author     : Emmanuel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<form onsubmit="return true;" action="user/approveEscalatedInvoice.action" method="post" 
      id="approveEscalatedInvoice" name="approveEscalatedInvoice">
    <fieldset class="x-fieldset">
        <legend>Escalated Invoice - Action Required</legend>
        <s:hidden name="id" />
        <s:hidden id="actionName" name="actionName" />
        <div>
            <div class="status-info">
                Please review the 'History' tab for details on why the claim has been rejected. 
                Please decide on whether to progress the claim for payment or reject the claim. 
                Please enter the required details/comments on the 'Invoice Details' tab regarding the decision made.
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
                            <input type="submit" value="Reject Invoice"  onclick="registeAction('reject');return confirm('Are you sure you want to reject this invoice?')" />
                            <input type="submit" value="Clear for payment" onclick="registeAction('accept')"  />   
                        </td>
                    </tr>
                </table>
            </div>
        </div> 
    </fieldset>
</form>
