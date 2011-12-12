<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">


</script>


<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form  id="updateManualInvoicePaymentForm" name="updateManualInvoicePaymentForm" onsubmit="return true;" action="<%=request.getContextPath()%>/prv/processClaim.action" method="POST">
        <fieldset class="x-fieldset"><legend>Manual Invoice - Action Required</legend>
            <div>
                <s:hidden id="claimId" name="id" />
                <s:hidden id="name" name="name" value="updateManualInvoicePaid"/>
                <div class="status-info">
                    Please modify the invoice details to reflect any adjustments made to the invoice following any negotiations made outside of the CHOX process/system.  Once the payment has been made please click on the 'Manual Invoice Paid' button.
                </div>
                <div class="status-info-submit">
                    <table>
<!--                        <tr>
                            <td>
                                <div class="no-format">
                                    <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                </div>
                            </td>
                        </tr>-->
                        <tr>
                            <td><input type="submit" id="UMIPFormId"value="Manual Invoice Paid"  /></td>
                        </tr>
                    </table>
                </div>
            </div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    </form>
</div>