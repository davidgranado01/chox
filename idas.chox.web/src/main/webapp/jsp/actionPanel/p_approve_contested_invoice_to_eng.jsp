<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    Ext.onReady(function() {

        $("form#invoiceReferredByEngForm").validate(
        {
            errorLabelContainer: "#invoiceReferredByEngMessageBox",
            rules: {
                appContInvEngReasonOfRejectionId:{
                    required:true
                }
            },
            messages: {
                appContInvEngReasonOfRejectionId:{
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

    
    function doInvoiceReferredByEngSubmit(action){

        $("#invoiceReferredByEngFormName").val(action);

        $("form#invoiceReferredByEngForm #rejecDescId").rules("remove");
        $("form#invoiceReferredByEngForm #appContInvEngReasonOfRejectionId").rules("remove");

        if(action==="rejectInvoice"){
            $("form#invoiceReferredByEngForm #appContInvEngReasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });
            $("form#invoiceReferredByEngForm #rejecDescId").rules("add", {
                required: true,
                messages: {required: "You must enter 'Supporting Rejection Notes'"}
            });
        }else{
            
            $("form#invoiceReferredByEngForm #rejecDescId").val("");
            $("form#invoiceReferredByEngForm #appContInvEngReasonOfRejectionId").val("");
            
        }

        if($("#invoiceReferredByEngForm").valid()){

            if (action==='rejectInvoice') {
                if(!Ext.MessageBox.confirm('Confirm', 'Are you sure you want to reject this claim?',function(btn){if(btn==='yes'){Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");choxJqueryHttpSubmit($("form#invoiceReferredByEngForm"));}else{return false;}})){
                    return false;
                }
            }
            else{
                Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
                choxJqueryHttpSubmit($("form#invoiceReferredByEngForm"));
            }
            
        }
        return false;
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
          id="invoiceReferredByEngForm" name="invoiceReferredByEngForm">
        <fieldset class="x-fieldset">
            <legend>Referred Invoice - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="invoiceReferredByEngFormName" name="name"/>
            <div>
                <div class="status-info">
                    Please review the 'Notes' tab for the reason why the invoice has been referred for further attention.
                    Please decide on whether to refer the claim back to a Claims Handler or reject the invoice back to the CHO.
                    Please provide appropriate notes on the 'Notes' tab regarding the decision made.
                </div>
                <div class="status-control-set">
                    <table>
                        <tr>
                            <td width="30%" nowrap>
                                <label>Reason For Rejection</label>
                            </td>
                            <td>
                                <s:select name="reasonOfRejectionId" id="appContInvEngReasonOfRejectionId"
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
                                <input type="button" id="ACITERejectInvoiceButtonId" value="Reject Invoice"  onclick="event.preventDefault(); doInvoiceReferredByEngSubmit('rejectInvoice');" />
                                <input type="button"id="ACITEReferToClaimHandlerButtonId" value="Refer To Claim Handler" onclick="event.preventDefault(); doInvoiceReferredByEngSubmit('invoiceReferToCH');"  />
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="invoiceReferredByEngMessageBox"></div>
            </div>
        </fieldset>
        <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    </form>
</div>