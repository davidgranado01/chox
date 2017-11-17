<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    Ext.onReady(function() {

        $("form#approveBREPassedClaim").validate(
        {
            errorLabelContainer: "#approveBREPassedClaimMessageBox",
            rules: {
                appBrePassClaimReasonOfRejectionId:{
                    required:true
                }
            },
            messages: {
                appBrePassClaimReasonOfRejectionId:{
                    required:"You must select a 'Reason For Rejection'"
                }
            }
        });
        
        var rejectionDescField = new Ext.form.TextArea({
            name             : 'rejectionDescription',
            id               : 'rejecDescId',
            width            :  350,
            height           :  80,
            allowBlank       :  false,
            renderTo         : 'rejectionDescId'
        });

    });
    
    function doApproveBREPassedClaimSubmit(action){
        
        $("#approveBREPassedClaimName").val(action);

        $("form#approveBREPassedClaim #rejecDescId").rules("remove");
        $("form#approveBREPassedClaim #appBrePassClaimReasonOfRejectionId").rules("remove");
        if(action==="rejectInvoice"){
            $("form#approveBREPassedClaim #appBrePassClaimReasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });
            $("form#approveBREPassedClaim #rejecDescId").rules("add", {
                required: true,
                messages: {required: "You must enter  'Supporting Rejection Notes'"}
            });
        }else{
            $("form#approveBREPassedClaim #appBrePassClaimReasonOfRejectionId").val("");
            $("form#approveBREPassedClaim #rejecDescId").val("");
        }

        if($("#approveBREPassedClaim").valid()){

            if (action==='rejectInvoice') {
                var reasonOfRejection = $("#appBrePassClaimReasonOfRejectionId").val();
                
                if (reasonOfRejection === <s:property value="invoiceLiabilityDisputeReasonId" /> && !Ext.MessageBox.confirm('Confirm', 'Where there is a dispute with liability and the invoice has been approved on a quantum basis, ensure that the \'Liability Status\' is up to date and click on the \'Agree Quantum\' button, the invoice will be allocated to a holding status until liability is resolved.  Are you sure you wish to proceed with the invoice rejection based on the information provided?',function(btn){if(btn==='yes'){Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");choxJqueryHttpSubmit($("form#approveBREPassedClaim"));}else{return false;}})) {
                    return;
                }
                else if(reasonOfRejection !== <s:property value="invoiceLiabilityDisputeReasonId" />
                        && !Ext.MessageBox.confirm('Confirm', 'Are you sure you want to reject this invoice?',
                                    function(btn){
                                        if(btn==='yes'){
                                            Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
                                            choxJqueryHttpSubmit($("form#approveBREPassedClaim"));
                                        }else{return false;}
                                    })){
                    return;
                }
            }
            else{
                Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
                choxJqueryHttpSubmit($("form#approveBREPassedClaim"));
            }
            
        }
    } 
    
    var reasonOfRejectionDescReader = new Ext.data.JsonReader({
        fields:[{name:'id'},{name:'description'}]
    });
    
    var reasonOfRejectionDescStore = new Ext.data.Store({
        data : Ext.util.JSON.decode('<s:property value="jsonReasonOfInvoiceRejectionDesc" escapeHtml="false"/>'),
        reader : reasonOfRejectionDescReader
    });
    
    function refreshDesc(id){
        reasonOfRejectionDescStore.each(function(rec) {
            if(id === rec.json.text){
                Ext.getCmp('rejecDescId').setValue(rec.json.value);
            }
        });
        if(id === -1 || id === '')
            Ext.getCmp('rejecDescId').setValue("");
    }
    
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="approveBREPassedClaim" name="approveBREPassedClaim" action="<%=request.getContextPath()%>/prv/processClaim.action"
          method="POST" >
        <fieldset class="x-fieldset">
            <legend>BRE Approved Claim - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="approveBREPassedClaimName" name="name" />
            <div>
                <div class="status-info">
                    <s:if test="insurerIsEngineersEnabled">
                        This claim and its related invoice have been cleared by the CHOX approval system. Please review the invoice and claim information supplied, and choose whether to agree the quantum for the invoice, reject the invoice or refer the invoice to an Engineer.
                    </s:if>
                    <s:else>
                        This claim and its related invoice have been cleared by the CHOX approval system. Please review the invoice and claim information supplied, and choose whether to agree the quantum for the invoice or reject the invoice.
                    </s:else>
                </div>
                <div class="status-control-set">
                    <table>
                        <tr>
                            <td width="30%" nowrap>
                                <label>Reason For Rejection</label>
                            </td>
                            <td>
                                <s:select name="reasonOfRejectionId" id="appBrePassClaimReasonOfRejectionId"
                                          list="reasonOfInvoiceRejections"
                                          listKey="id"
                                          listValue="rorName"
                                          onchange="refreshDesc(this.value)"
                                          headerKey=""
                                          headerValue="N/A"
                                          emptyOption="false"></s:select>
                            </td>
                            <td></td><td></td><td></td>
                        </tr>
                        <td align="left" valign="top"><label class="std-label-ro">Supporting Rejection Note&nbsp;&nbsp;</label></td>
                            <td>
                                <div id="rejectionDescId"/>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="4">
                                <div class="no-format">
                                    <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="4" class="choice">
                                <input type="button" id="ABPCRejectInvoiceButtonId" value="Reject Invoice"  onclick="event.preventDefault(); doApproveBREPassedClaimSubmit('rejectInvoice');" />
                                <input type="button" id="ABPCClearForPaymentButtonId" value="Agree Quantum" onclick="event.preventDefault(); doApproveBREPassedClaimSubmit('acceptInvoice');"  />
                                <s:if test="insurerIsEngineersEnabled">
                                    <input type="button" id="ABPCReferToEngineerButtonId" value="Refer To Engineer" onclick="event.preventDefault(); doApproveBREPassedClaimSubmit('invoiceReferToEng');"  />
                                </s:if>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="approveBREPassedClaimMessageBox"></div>
            </div>
        </fieldset>
        <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    </form>
</div>