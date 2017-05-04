<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    Ext.onReady(function() {

        $("form#invoiceReferredToClaimsHandler").validate(
        {
            errorLabelContainer: "#invoiceReferredToClaimsHandlerMessageBox",
            rules: {
                appInvRefEngReasonOfRejectionId:{
                    required:true
                }
            },
            messages: {
                appInvRefEngReasonOfRejectionId:{
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

    function doInvoiceReferredToClaimsHandlerSubmit(action){

        $("#invoiceReferredToClaimsHandlerName").val(action);
//        actionPanel.registerAction(action);
        $("form#invoiceReferredToClaimsHandler #rejecDescId").rules("remove");
        $("form#invoiceReferredToClaimsHandler #appInvRefEngReasonOfRejectionId").rules("remove");

        if(action==="rejectInvoice"){

            $("form#invoiceReferredToClaimsHandler #appInvRefEngReasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });
            $("form#invoiceReferredToClaimsHandler #rejecDescId").rules("add", {
                required: true,
                messages: {required: "You must enter 'Supporting Rejection Notes'"}
            });

        }else{

            $("form#invoiceReferredToClaimsHandler #appInvRefEngReasonOfRejectionId").val("");
            $("form#invoiceReferredToClaimsHandler #rejecDescId").val("");

        }

        if($("form#invoiceReferredToClaimsHandler").valid()){

            if (action==='rejectInvoice') {
                if(!Ext.MessageBox.confirm('Confirm', 'Are you sure you want to reject this claim?',function(btn){if(btn==='yes'){Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");choxJqueryHttpSubmit($("form#invoiceReferredToClaimsHandler"));}else{return false;}})){
                    return;
                }
            }
            else{
               Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
               choxJqueryHttpSubmit($("form#invoiceReferredToClaimsHandler"));
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
    <form action="<%=request.getContextPath()%>/prv/processClaim.action"
          method="post" id="invoiceReferredToClaimsHandler" name="invoiceReferredToClaimsHandler">
        <fieldset class="x-fieldset">
            <legend>Invoice Referred By Engineer - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="invoiceReferredToClaimsHandlerName" name="name"/>
            <div>
                <div class="status-info">
                    <s:if test="insurerIsEngineersEnabled">
                        This claim and its related invoice have been referred by an Engineer. Please review the invoice and claim information supplied along with the reason for referral, and choose whether to agree the quantum for the invoice, reject the invoice or refer the invoice to an Engineer.
                    </s:if>
                    <s:else>
                        This claim and its related invoice have been referred by an Engineer. Please review the invoice and claim information supplied along with the reason for referral, and choose whether to agree the quantum for the invoice or reject the invoice.
                    </s:else>
                </div>
                <div class="status-control-set">

                    <table width="100%">
                        <tr>
                            <td width="30%" nowrap>
                                <label>Reason For Rejection</label>
                            </td>
                            <td>
                                <s:select name="reasonOfRejectionId" id="appInvRefEngReasonOfRejectionId"
                                          list="reasonOfInvoiceRejections"
                                          listKey="id"
                                          listValue="rorName"
                                          onchange="refreshDesc(this.value)"
                                          headerKey=""
                                          headerValue="N/A"
                                          emptyOption="false"></s:select>
                            </td>
                            <td></td><td></td>
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
                                <input type="button" id="AIRBERejectInvoiceButtonId" value="Reject Invoice"  onclick="return doInvoiceReferredToClaimsHandlerSubmit('rejectInvoice');" />
<s:if test="invoiceSavingActive">
                                <input type="button" id="AIRBEClearForPaymentButtonId" value="Agree Quantum" onclick="return confirmInvoiceSavingsAction();"  />
</s:if>
<s:else>
                                <input type="button" id="AIRBEClearForPaymentButtonId" value="Agree Quantum" onclick="return doInvoiceReferredToClaimsHandlerSubmit('acceptInvoice');"  />
</s:else>
                                <s:if test="insurerIsEngineersEnabled">
                                    <input type="button" id="AIRBEReferToEngineerButtonId"value="Refer To Engineer" onclick="return doInvoiceReferredToClaimsHandlerSubmit('invoiceReferToEng');"  />
                                </s:if>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="invoiceReferredToClaimsHandlerMessageBox"></div>
            </div>
        </fieldset>
        <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    </form>
</div>