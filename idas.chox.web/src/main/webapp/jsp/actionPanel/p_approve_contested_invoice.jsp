<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">


    Ext.onReady(function() {

        $("form#contestedInvoiceRefToInsurer").validate(
        {
            errorLabelContainer: "#contestedInvoiceRefToInsurerMessageBox",
            rules: {
                appContInvReasonOfRejectionId:{
                    required:true
                }
            },
            messages: {
                appContInvReasonOfRejectionId:{
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

    function docontestedInvoiceRefToInsurerSubmit(action){


        $("#contestedInvoiceRefToInsurernName").val(action);

        $("form#contestedInvoiceRefToInsurer #rejecDescId").rules("remove");
        $("form#contestedInvoiceRefToInsurer #appContInvReasonOfRejectionId").rules("remove");
        
        if(action==="rejectInvoice"){
            
            $("form#contestedInvoiceRefToInsurer #appContInvReasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });
            $("form#contestedInvoiceRefToInsurer #rejecDescId").rules("add", {
                required: true,
                messages: {required: "You must enter 'Supporting Rejection Notes'"}
            });

        }else{

            $("form#contestedInvoiceRefToInsurer #appContInvReasonOfRejectionId").val("");
            $("form#contestedInvoiceRefToInsurer #rejecDescId").val("");

        }

        if($("form#contestedInvoiceRefToInsurer").valid()){

            if (action==='rejectInvoice') {
                var reasonOfRejection = $("#appContInvReasonOfRejectionId").val();
                if (reasonOfRejection === <s:property value="invoiceLiabilityDisputeReasonId" /> && !Ext.MessageBox.confirm('Confirm', 'Where there is a dispute with liability and the invoice has been approved on a quantum basis, ensure that the \'Liability Status\' is up to date and click on the \'Agree Quantum\' button, the invoice will be allocated to a holding status until liability is resolved.  Are you sure you wish to proceed with the invoice rejection based on the information provided?',function(btn){if(btn==='yes'){Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");choxJqueryHttpSubmit($("form#contestedInvoiceRefToInsurer"));}else{return false;}})) {
                    return;
                }
                else if(reasonOfRejection !== <s:property value="invoiceLiabilityDisputeReasonId" /> && !Ext.MessageBox.confirm('Confirm', 'Are you sure you want to reject this claim?',function(btn){if(btn==='yes'){Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");choxJqueryHttpSubmit($("form#contestedInvoiceRefToInsurer"));}else{return false;}})){
                    return;
                }
            }
            else{
                Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
                choxJqueryHttpSubmit($("form#contestedInvoiceRefToInsurer"));
            }
            
        }
    }
    
    var reasonOfRejectionDescReader = new Ext.data.JsonReader({
        fields:[{name:'id'},{name:'description'}]
    });
    
    var reasonOfRejectionDescStore = new Ext.data.Store({
        data : Ext.util.JSON.decode('<s:property value="jsonReasonOfInvoiceRejectionDesc" escape="false"/>'),
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
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post"
          id="contestedInvoiceRefToInsurer" name="contestedInvoiceRefToInsurer">
        <fieldset class="x-fieldset">
            <legend>Contested Invoice - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="contestedInvoiceRefToInsurernName" name="name"/>
            <div>
                <div class="status-info">
                    <s:if test="insurerIsEngineersEnabled">
                        Please review the 'History' tab for details on why the claim has been rejected and review the details/comments on the 'Notes' tab regarding the previous decision to reject. Please decide on whether to agree the quantum for the invoice, refer the claim to an Engineer or reject the claim. Please provide appropriate notes on the 'Notes' tab regarding the decision made.
                    </s:if>
                    <s:else>
                        Please review the 'History' tab for details on why the claim has been rejected and review the details/comments on the 'Notes' tab regarding the previous decision to reject. Please decide on whether to agree the quantum for the invoice or reject the claim. Please provide appropriate notes on the 'Notes' tab regarding the decision made.
                    </s:else>
                </div>
                <div class="status-control-set">
                    <table>
                        <tr>
                            <td width="30%" nowrap>
                                <label>Reason For Rejection</label>
                            </td>
                            <td>
                                <s:select name="reasonOfRejectionId" id="appContInvReasonOfRejectionId"
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
                        <tr>
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
                                <input type="button" id="ACIRejectInvoiceButtonId" value="Reject Invoice"  onclick="return docontestedInvoiceRefToInsurerSubmit('rejectInvoice');" />
                                <input type="button" id="ACIClearForPaymentId" value="Agree Quantum" onclick="return docontestedInvoiceRefToInsurerSubmit('acceptInvoice');"  />
                                <s:if test="insurerIsEngineersEnabled">
                                    <input type="button" id="ACIReferToEngineerButtonId" value="Refer To Engineer" onclick="return docontestedInvoiceRefToInsurerSubmit('invoiceReferToEng');"  />
                                </s:if>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="contestedInvoiceRefToInsurerMessageBox"></div>
            </div>
        </fieldset>
        <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    </form>
</div>