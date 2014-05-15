<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    Ext.onReady(function() {

        $("form#formUpdateInsurerClaimNumber").validate(
        {
            errorLabelContainer: "#ACKmUpdateInsurerClaimNumbermessageBox",
            rules: {
                claimNumber:{
                    required:true
                }
            },
            messages: {
                claimNumber: {
                    required:"You must supply a value for 'Claim Number'",
                    textDigitOnly:"Invalid 'Claim Number' Format"
                }
            }
        });

    });

    function checkClaimNumberDuplicationAndSubmit(claimNumber, claimId, form)
    {
        var url = "/prv/p/checkIsClaimNumberDuplicated.action";
        var param = {
            claimNumber: claimNumber,
            claimId: claimId
        };

        ajax.loadJson2(url, param, function(data){
            if(data.result && data.resultType=='YesNo'){
                Ext.MessageBox.confirm('Confirm', data.result,function(btn){
                if(btn=='yes')
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

    }

    function doSubmitClaimNumber(){

        if($("form#formUpdateInsurerClaimNumber").valid()){

            var claimNumber = $("form#formUpdateInsurerClaimNumber #claimNumber").val();
            var claimId = $("form#formUpdateInsurerClaimNumber #claimId").val();
            var form = $("form#formUpdateInsurerClaimNumber");

            if(claimNumber && claimNumber.length > 0){
                checkClaimNumberDuplicationAndSubmit(claimNumber, claimId, form);
            }
        }
    }

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/updateClaimNumber.action" method="post" id="formUpdateInsurerClaimNumber" name="formUpdateInsurerClaimNumber">
        <fieldset class="x-fieldset">
            <legend>Insurer Claim Number</legend>
            <s:hidden id="claimId" name="id" />
            <div>
                <div class="status-control-set">
                    <table class="status-table">
                        <tr>
                            <td>
                                <label>Claim Number<span class="mandatory">*</span></label></td><td nowrap>
                                <input type="text" class="chox-ttxt" id="claimNumber" name="claimNumber" value="<s:property value="claimNumber" />"/>
                                <input type="button" id="MAUICNUpdateClaimNumberButtonId"value="Update Claim Number" onclick="javascript: return doSubmitClaimNumber()"/>
                            </td>
                            <td></td><td></td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="ACKmUpdateInsurerClaimNumbermessageBox"></div>
            </div>
        </fieldset>
    </form>
</div>