<%-- 
    Document   : p_resubmitOrAcceptContestedInvoice
    Created on : Dec 02, 2008, 11:39:12 AM
    Author     : Emmanuel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">



<form onsubmit="return true;" action="user/resubmitOrAcceptContestedInvoice.action" method="post" 
      id="resubmitOrAcceptContestedInvoice" name="resubmitOrAcceptContestedInvoice">
    <fieldset class="x-fieldset">
        <legend>Contested Invoice - Action Required</legend>
        <s:hidden name="id" />
        <div>
            <div class="status-info">
                Please review the Insurer's notes against rejection reasoning and decide whether to accept or reject the Insurer's rejection decision.  
                Please include supporting notes on the decision made using the 'Notes' tab.
            </div>
            <div class="status-control-set">
                <table>
                    <tr>
                        <td colspan="2">
                            <div class="no-format">
                                <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                <s:radio name="actionName" list="resubmitOrAcceptContestedInvoiceActions" />
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
