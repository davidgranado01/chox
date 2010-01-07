<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<form onsubmit="return true;" action="<%=request.getContextPath()%>/prv/resubmitOrAcceptContestedInvoice.action" method="post"
      id="resubmitOrAcceptContestedInvoice" name="resubmitOrAcceptContestedInvoice">
    <fieldset class="x-fieldset">
        <legend>Contested Invoice - Action Required</legend>
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
                            <input type="submit" value="Reject rejection decision and resubmit claim"  onclick="registeAction('reject');" />
                            <input type="submit" value="Accept rejection decision" onclick="registeAction('accept')"  /> 
                        </td>
                    </tr>
                </table>
            </div>
        </div> 
    </fieldset>
</form>
