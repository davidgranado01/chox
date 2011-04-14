<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
    function confirmPaymentlogAction(){

        Ext.Msg.show({
            title      : 'Confirm',
            msg        : 'Clicking on this button indicates to the CHO that payment has been made on your internal claims system.  Click \'OK\' to confirm payment has been made.',
            width      : 800,
            buttons    : Ext.MessageBox.OKCANCEL,
            fn         : function(btn) {
                            if(btn=='ok') {
                                var queryString = $('#logInvoicePayment').formSerialize();
                               window.location = "<%=request.getContextPath()%>/prv/processClaim.action?" + queryString;
                                           }
                                       }
              });
    }
</script>
<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="logInvoicePayment" action="post" >
        <fieldset class="x-fieldset"><legend>Invoice ready for payment - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="name" name="name" value="invoicePaymentLogged"/>
            <div>
                <div class="status-info">
                    Please update the claim by recording that a payment has been logged against this claim.
                </div>
                <div class="status-info-submit">
                    <table>
                        <tr>
                            <td>
                                <div class="no-format">
                                    <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td><input type="button" id="LIPInvoicePaymentLoggedButtonId"value="Invoice Payment Logged" onclick="confirmPaymentlogAction();"/></td>
                        </tr>
                    </table>
                </div>
            </div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>