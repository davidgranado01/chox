<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    Ext.onReady(function() {

        $("form#invoiceEscalatedToCh").validate(
        {
            errorLabelContainer: "#invoiceEscalatedToChMessageBox",
            rules: {
                appBrePassCHReasonOfRejectionId:{
                    required:true
                }
            },
            messages: {
                appBrePassCHReasonOfRejectionId:{
                    required:"You must select 'Reason For Rejection'"
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

    function doInvoiceEscalatedToChFormSubmit(action){


//        actionPanel.registerAction(action);
          
        $("#invoiceEscalatedToChName").val(action);

        $("form#invoiceEscalatedToCh #rejecDescId").rules("remove");
        $("form#invoiceEscalatedToCh #appBrePassCHReasonOfRejectionId").rules("remove");

        if(action==="rejectInvoice"){

            $("form#invoiceEscalatedToCh #appBrePassCHReasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });
            $("form#invoiceEscalatedToCh #rejecDescId").rules("add", {
                required: true,
                messages: {required: "You must enter 'Supporting Rejection Notes'"}
            });


        }else{

            $("form#invoiceEscalatedToCh #appBrePassCHReasonOfRejectionId").val("");
            $("form#invoiceEscalatedToCh #rejecDescId").val("");
        }

        if($("form#invoiceEscalatedToCh").valid()){
            if (action==='rejectInvoice') {
                var reasonOfRejection = $("#appBrePassCHReasonOfRejectionId").val();
                if (reasonOfRejection === <s:property value="invoiceLiabilityDisputeReasonId" /> && 
                    !Ext.MessageBox.confirm('Confirm', 'Where there is a dispute with liability and the invoice has been approved on a quantum basis, ensure that the \n\
                                            \'Liability Status\' is up to date and click on the \'Agree Quantum\' button, the invoice will be allocated to a holding status \n\
                                            until liability is resolved.  Are you sure you wish to proceed with the invoice rejection based on the information provided?',
                                            function(btn){ 
                                                  if(btn==='yes'){ 
                                                      Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
                                                      choxJqueryHttpSubmit($("form#invoiceEscalatedToCh"));
                                                  }else{ 
                                                      return false;
                                                  }
                                              }))
                {
                    return false;
                }
                else if(reasonOfRejection !== <s:property value="invoiceLiabilityDisputeReasonId" /> && 
                         !Ext.MessageBox.confirm('Confirm', 'Are you sure you want to reject this invoice?',
                                                  function(btn){
                                                      if(btn==='yes'){
                                                          Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
                                                          choxJqueryHttpSubmit($("form#invoiceEscalatedToCh"));
                                                      }else{
                                                          return false;
                                                      }
                                                  }))
                {
                    return false;
                }
            }
           else{
               Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
               choxJqueryHttpSubmit($("form#invoiceEscalatedToCh"));
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
          method="post" id="invoiceEscalatedToCh" name="invoiceEscalatedToCh">
        <fieldset class="x-fieldset">
            <legend>Invoice Escalated To Claim Handler - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="invoiceEscalatedToChName" name="name"/>
            <div>
                <div class="status-info">
                    <s:if test="insurerIsEngineersEnabled">
                        Please review the 'BRE Results' tab for details on why the claim has failed the validation rules. Please decide on whether to agree the quantum for the invoice, refer the claim to an Engineer or reject the claim back to the CHO. Please enter any relevant details/comments on the 'Notes' tab regarding the decision made.
                    </s:if>
                    <s:else>
                        Please review the 'BRE Results' tab for details on why the claim has failed the validation rules. Please decide on whether to agree the quantum for the invoice or reject the claim back to the CHO. Please enter any relevant details/comments on the 'Notes' tab regarding the decision made.
                    </s:else>
                </div>
                <div class="status-control-set">

                    <table width="100%">
                        <tr>
                            <td width="30%" nowrap>
                                <label>Reason For Rejection</label>
                            </td>
                            <td>
                                <s:select name="reasonOfRejectionId" id="appBrePassCHReasonOfRejectionId"
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
                                <input type="button" id="abpbchRejectInvoiceButtonId" value="Reject Invoice"  onclick="event.preventDefault(); doInvoiceEscalatedToChFormSubmit('rejectInvoice');" />
                                <input type="button" id="abpbchClearForPaymentButtonId"  value="Agree Quantum" onclick="event.preventDefault(); doInvoiceEscalatedToChFormSubmit('acceptInvoice');"  />
                                <s:if test="insurerIsEngineersEnabled">
                                    <input type="button"  id="abpbchReferToEngineerButtonId" value="Refer To Engineer" onclick="event.preventDefault(); doInvoiceEscalatedToChFormSubmit('invoiceReferToEng');"  />
                                </s:if>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="invoiceEscalatedToChMessageBox"></div>
            </div>
        </fieldset>
        <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    </form>
</div>