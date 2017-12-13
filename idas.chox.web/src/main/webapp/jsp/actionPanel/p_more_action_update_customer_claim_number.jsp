<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    Ext.onReady(function() {

        $("form#formUpdateCustomerClaimNumber").validate(
        {
            errorLabelContainer: "#ACKmUpdateCustomerClaimNumberMessageBox",
            rules: {
                customerClaimNumer:{
                    required:true
                }
            },
            messages: {
                customerClaimNumer: {
                    required:"You must supply a value for 'Customer Claim Number'",
                    textDigitOnly:"Invalid 'Customer Claim Number' Format"
                }
            }
        });

    });

    function checkCustomerClaimNumberDuplicationAndSubmit(customerClaimNumber, claimId, form)
    {
        var url = "/prv/p/checkIsCustomerClaimNumberDuplicated.action";
        var param = {
            customerClaimNumber: customerClaimNumber,
            claimId: claimId
        };

        ajax.loadJson2(url, param, function(data){
            if(data.result && data.resultType==='YesNo'){
                Ext.MessageBox.confirm('Confirm', data.result,function(btn){
                if(btn==='yes')
                {
                    Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
                    choxJqueryHttpSubmit(form);
                }
                });
            }
            else {
                Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
                choxJqueryHttpSubmit(form);
            }
        });
        
        return false;

    }

    function doSubmitCustomerClaimNumber(){

        if($("form#formUpdateCustomerClaimNumber").valid()){

            var customerClaimNumber = $("form#formUpdateCustomerClaimNumber #customerClaimNumber").val();
            var claimId = $("form#formUpdateCustomerClaimNumber #claimId").val();
            var form = $("form#formUpdateCustomerClaimNumber");

            if(customerClaimNumber && customerClaimNumber.length > 0){
                checkCustomerClaimNumberDuplicationAndSubmit(customerClaimNumber, claimId, form);
            }
        }
        return false;
    }

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formUpdateCustomerClaimNumber" name="formUpdateCustomerClaimNumber">
        <fieldset class="x-fieldset">
            <legend>Customer Claim Number</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="name" name="name" value="updateCustomerClaimNumber" />
            <div>
                <div class="status-control-set">
                    <table class="status-table">
                        <tr>
                            <td>
                                <label>Customer Claim Number<span class="mandatory">*</span></label></td><td nowrap>
                                <input type="text" class="chox-ttxt" id="customerClaimNumber" name="customerClaimNumber" value="<s:property value="customerClaimNumber" />"/>
                                <input type="button" id="MAUICNUpdateCustomerClaimNumberButtonId"value="Update Customer Claim Number" onclick="event.preventDefault(); doSubmitCustomerClaimNumber();"/>
                            </td>
                            <td></td><td></td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="ACKmUpdateCustomerClaimNumberMessageBox"></div>
            </div>
        </fieldset>
    </form>
</div>