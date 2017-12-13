<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    Ext.onReady(function() {

        $("form#formUpdateSupplierReferenceNumber").validate(
        {
            errorLabelContainer: "#ACKmUpdateSupplierReferenceNumberMessageBox",
            rules: {
                supplierReference:{
                    required:true
                }
            },
            messages: {
                supplierReference: {
                    required:"You must supply a value for 'Supplier Reference Number'",
                    textDigitOnly:"Invalid 'Supplier Reference' Format"
                }
            }
        });

    });

    function checkSupplierReferenceNumberDuplicationAndSubmit(supplierReference, claimId, form)
    {
        var url = "/prv/p/checkIsSupplierReferenceNumberDuplicated.action";
        var param = {
            choReference: supplierReference,
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
            }else if (data.result && data.resultType==='Message'){
                Ext.MessageBox.alert('Duplicate Supplier Reference', data.result);
            }else {
                Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
                choxJqueryHttpSubmit(form);
            }
        });

    }

    function doSubmitSupplierReferenceNumber(){

        if($("form#formUpdateSupplierReferenceNumber").valid()){

            var supplierReference = $("form#formUpdateSupplierReferenceNumber #supplierReference").val();
            var claimId = $("form#formUpdateSupplierReferenceNumber #claimId").val();
            var form = $("form#formUpdateSupplierReferenceNumber");

            if(supplierReference && supplierReference.length > 0){
                checkSupplierReferenceNumberDuplicationAndSubmit(supplierReference, claimId, form);
            }
        }
    }

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formUpdateSupplierReferenceNumber" name="formUpdateSupplierReferenceNumber">
        <fieldset class="x-fieldset">
            <legend>Update Supplier Reference Number</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="name" name="name" value="updateSupplierReference" />
            <div>
                <div class="status-control-set">
                    <table class="status-table">
                        <tr>
                            <td>
                                <label>Supplier Reference Number<span class="mandatory">*</span>:</label></td><td nowrap>
                                <input type="text" class="chox-ttxt" id="supplierReference" name="supplierReference" value="<s:property value="choReference" />"/>
                                <input type="button" id="MAUICNUpdateSupplierReferenceNumberButtonId" value="Update Supplier Reference Number" onclick="event.preventDefault(); doSubmitSupplierReferenceNumber();"/>
                            </td>
                            <td></td><td></td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="ACKmUpdateSupplierReferenceNumberMessageBox"></div>
            </div>
        </fieldset>
    </form>
</div>