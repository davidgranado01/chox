<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    $(function(){

        $("form#formMakeInterimPayment").validate(
        {
            errorLabelContainer: "#ACKmMakeInterimPaymentMessageBox",
            rules: {
                interimPayment:{
                    required:true,
                    number:true
                }
            },
            messages: {
                interimPayment: {
                    required:"You must supply a value for 'Interim Payment'",
                    number:"Invalid 'Interim Payment' Format"
                }
            }
        });

        if (<s:property value="interimPaymentReceived || false" />) {
            $('#interimPayment').attr("disabled", true);
            $('#submitInterimPayment').attr("disabled", true);
        }
    });
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/makeInterimPayment.action" method="post" id="formMakeInterimPayment" name="formMakeInterimPayment">
        <fieldset class="x-fieldset">
            <legend>Make Interim Payment</legend>
            <s:hidden id="claimId" name="id" />
            <div>
                <div class="status-control-set">
                    <table class="status-table">
                        <tr>
                            <td><label>Interim Payment Amount<span class="mandatory">*</span></label></td>
                            <td nowrap>
                                £&nbsp;<input type="text" class="chox-ttxt" id="interimPayment" name="interimPayment" value="<s:property value="interimPayment" />"/>
                                <input type="submit" value="Make Interim Payment" id="submitInterimPayment"/>
                            </td>
                            <td></td><td></td>
                        </tr>
                        <s:if test="interimPaymentReceived">
                            <tr><td colspan="3"><label>An interim payment has already beend made and received</label></td></tr>
                        </s:if>
                        <s:elseif test="!interimPaymentReceived && interimPayment">
                            <tr><td colspan="3"><label>Note that an interim payment of £<s:property value="interimPayment" /> has already been made.</label></td></tr>
                        </s:elseif>
                    </table>
                </div>
                <div class="action-error-msg" id="ACKmMakeInterimPaymentMessageBox"></div>
            </div>
        </fieldset>
        <s:token/>
    </form>
</div>
