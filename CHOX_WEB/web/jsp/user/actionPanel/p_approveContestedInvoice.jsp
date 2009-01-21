<%-- 
    Document   : p_approveContestedInvoice
    Created on : 01-Dec-2008, 02:28:00
    Author     : Emmanuel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<form onsubmit="return true;" action="user/approveContestedInvoice.action" method="post" 
      id="approveContestedInvoice" name="approveContestedInvoice">
    <fieldset class="x-fieldset">
        <legend>Contested Invoice - Action Required</legend>
        <s:hidden name="id" />
        <s:hidden id="actionName" name="actionName" />
        <div>
            <div class="status-info">
                Please review the 'History' tab for details on why the claim has been rejected and review the details/comments on the 'Notes' tab regarding the previous decision to reject. 
                Please decide on whether to progress the claim for payment or reject the claim. 
                Please provide appropriate notes on the 'Notes' tab regarding the decision made.
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
                            <input type="submit" value="Reject Invoice"  onclick="registeAction('reject');return confirm('Are you sure you want to reject this claim?')" />
                            <input type="submit" value="Clear For Payment" onclick="registeAction('accept')"  />  
                        </td>
                    </tr>
                </table>
            </div>
        </div> 
    </fieldset>
</form>
