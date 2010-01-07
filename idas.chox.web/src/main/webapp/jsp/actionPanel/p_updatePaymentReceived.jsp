<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    function doUpdateClaim(){ 
        $("#formUpdatePaymentReceived").submit();        
    }

</script>

<form 
    onsubmit="return true;" 
    action="<%=request.getContextPath()%>/prv/updatePaymentReceived.action"
    method="post" 
    id="formUpdatePaymentReceived" 
    name="formUpdatePaymentReceived">
    <fieldset class="x-fieldset">
        <s:hidden id="claimId" name="id" />
        <s:hidden id="actionName" name="actionName" />      
        <legend>Update Payment Logged</legend>
        <div class="status-control-set">

            <div class="status-info">             
                Please click on the 'Payment Received' button below when the payment has been received from the Insurer.
            </div>

            <table class="status-table">
                <tr>
                    <td colspan="3">                
                        <div class="no-format"><span>Please specify how you wish to proceed &nbsp;&nbsp;</span></div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="button" value="Payment Received" onclick="javascript: doUpdateClaim();" /> 
                    </td>
                    <td></td><td></td>
                </tr>
            </table>
        </div>
        <div class="action-error-msg" id="ACKmessageBox"></div>
    </fieldset>
</form>
