<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">

function doUpdateManualInvoice(action){
    
    $('form#updateManualInvoicePaymentForm input[id="name"]').val(action)
    
    Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
//    $("form#updateManualInvoicePaymentForm").submit();
    choxJqueryHttpSubmit($("form#updateManualInvoicePaymentForm"));
    
}

</script>


<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form  id="updateManualInvoicePaymentForm" name="updateManualInvoicePaymentForm" action="<%=request.getContextPath()%>/prv/processClaim.action" method="POST">
            <fieldset class="x-fieldset"><legend>Insurer Invoice - Action Required</legend>
                <s:hidden id="claimId" name="id" />
                <s:hidden id="name" name="name" />
                <div class="status-control-set">
                    <s:if test="!status.equalsIgnoreCase('ManualInvoiceContested') && !pcOnly">
                        <div class="status-info">
                            If applicable please modify the invoice details to reflect any adjustments made to
                            the invoice following any negotiations made outside of the CHOX process/system.
                            Once quantum has been agreed please click on the ‘Agree Quantum' button.
                            However if the invoice has been contested with the CHO then click on the 'Invoice
                            Contested With CHO' button to move the claim to a holding status until an agreement
                            has been reached.
                        </div>
                    </s:if>
                    <s:else>
                        <div class="status-info">
                            If applicable please modify the invoice details to reflect any adjustments made to
                            the invoice following any negotiations made outside of the CHOX process/system. 
                            Once quantum has been agreed please click on the ‘Agree Quantum' button.
                        </div>
                    </s:else>
                    <div class="status-info-submit">
                        <table>
                            <s:if test="!status.equalsIgnoreCase('ManualInvoiceContested') && !pcOnly">
                                <tr>
                                    <td colspan="2" class="choice" nowrap="true">
                                        <input type="button" id="UMIPFormId" value="Agree Quantum" onclick="doUpdateManualInvoice('updateManualInvoiceAgreeQuantum');" />
                                        <input type="button" id="UMICFormId" value="Invoice Contested With CHO" onclick="doUpdateManualInvoice('updateManualInvoiceContested');" />
                                    </td>
                                </tr>
                            </s:if>
                            <s:else>
                                <tr>
                                    <td><input type="button" id="UMIPFormId" value="Agree Quantum" onclick="doUpdateManualInvoice('updateManualInvoiceAgreeQuantum');" /></td>
                                </tr>
                            </s:else>   
                        </table>
                    </div>
                </div>
            </fieldset>
            <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>-->
    </form>
</div>