<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    Ext.onReady(function() {

        $("form#invoiceExcalatedForm").validate(
        {
            errorLabelContainer: "#invoiceExcalatedFormMessageBox",
            rules: {
                invEscReasonOfRejectionId:{
                    required:true
                }
            },
            messages: {
                invEscReasonOfRejectionId:{
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


    function doInvoiceExcalatedFormSubmit(action){

        $("#invoiceExcalatedFormName").val(action);
        
        $("form#invoiceExcalatedForm #rejecDescId").rules("remove");
        $("form#invoiceExcalatedForm #invEscReasonOfRejectionId").rules("remove");
        if(action==="rejectInvoice"){
            $("form#invoiceExcalatedForm #invEscReasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });
            $("form#invoiceExcalatedForm #rejecDescId").rules("add", {
                required: true,
                messages: {required: "You must enter 'Supporting Rejection Notes'"}
            });
        }else{
            $("form#invoiceExcalatedForm #invEscReasonOfRejectionId").val("");
            $("form#invoiceExcalatedForm #rejecDescId").val("");
        }

        if($("#invoiceExcalatedForm").valid()){

            if (action==='rejectInvoice') {
                if(!Ext.MessageBox.confirm('Confirm', 'Are you sure you want to reject this claim?',function(btn){if(btn==='yes'){Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");choxJqueryHttpSubmit($("form#invoiceExcalatedForm"));}else{return false;}})){
                    return;
                }
            }
            else{
                Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
                choxJqueryHttpSubmit($("form#invoiceExcalatedForm"));
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
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post"
          id="invoiceExcalatedForm" name="invoiceExcalatedForm">
        <fieldset class="x-fieldset">
            <legend>Escalated Invoice - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="invoiceExcalatedFormName" name="name"/>
            <div>
                <div class="status-info">
                    Please review the ‘BRE Results’ tab for details on why the claim has failed the validation rules.
                    Please decide on whether to refer the claim to a Claims Handler or reject the claim back to the CHO.
                    Please enter any relevant details/comments on the ‘Notes’ tab regarding the decision made.
                </div>
                <div class="status-control-set">
                    <table>
                        <tr>
                            <td width="30%" nowrap>
                                <label>Reason For Rejection</label>
                            </td>
                            <td>
                                <s:select name="reasonOfRejectionId" id="invEscReasonOfRejectionId"
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
                        <td align="left" valign="top"><label >Supporting Rejection Note&nbsp;&nbsp;</label></td>
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
                                <input type="button" id="IERejectInvoiceButtonId" value="Reject Invoice" onclick="javascript: return doInvoiceExcalatedFormSubmit('rejectInvoice');" />
                                <input type="button" id="IEReferToClaimsHandlerButtonId" value="Refer To Claims Handler" onclick="return doInvoiceExcalatedFormSubmit('invoiceReferToCH');"  />
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="invoiceExcalatedFormMessageBox"></div>
            </div>
        </fieldset>
        <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    </form>
</div>