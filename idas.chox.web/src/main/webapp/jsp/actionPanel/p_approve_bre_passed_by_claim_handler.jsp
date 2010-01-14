<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){

        $("form#invoiceEscalatedToCh").validate(
        {
            errorLabelContainer: "#invoiceEscalatedToChMessageBox",
            rules: {
                reasonOfRejectionId:{
                    required:true
                }
            },
            messages: {
                reasonOfRejectionId:{
                    required:"You must select reason of rejection"
                }
            }
        });

    });

    function doInvoiceEscalatedToChFormSubmit(action){


        actionPanel.registerAction(action);

        $("form#invoiceEscalatedToCh #reasonOfRejectionId").rules("remove");

        if(action=="rejectInvoice"){

            $("form#invoiceEscalatedToCh #reasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });

        }else{

            $("form#invoiceEscalatedToCh #reasonOfRejectionId").val("");
            
        }

        if($("form#invoiceEscalatedToCh").valid()){

            if(action=='rejectInvoice' && !confirm('Are you sure you want to reject this claim?')){
                return;
            }

            $("form#invoiceEscalatedToCh").submit();
        }
    }

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form onsubmit="return true;" action="<%=request.getContextPath()%>/prv/processClaim.action"
          method="post" id="invoiceEscalatedToCh" name="invoiceEscalatedToCh">
        <fieldset class="x-fieldset">
            <legend>Invoice Escalated To Claim Handler - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="name" name="name"/>
            <div>
                <div class="status-info">
                    Please review the 'History' tab for details on why the claim has failed the validation rules. Please decide on whether to progress the claim for payment, refer the claim to an Engineer or reject the claim back to the CHO. Please enter any relevant details/comments on the 'Notes' tab regarding the decision made.
                </div>
                <div class="status-control-set">

                    <table width="100%">
                        <tr>
                            <td width="30%" nowrap>
                                <label>Reason for Rejection</label>
                            </td>
                            <td>
                                <s:select name="reasonOfRejectionId" id="reasonOfRejectionId"
                                          list="reasonOfInvoiceRejections"
                                          listKey="id"
                                          listValue="name"
                                          headerKey=""
                                          headerValue="N/A"
                                          emptyOption="false"></s:select>
                            </td>
                            <td></td><td></td>
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
                                <input type="submit" value="Reject Invoice"  onclick="return doInvoiceEscalatedToChFormSubmit('rejectInvoice');" />
                                <input type="submit" value="Clear For Payment" onclick="return doInvoiceEscalatedToChFormSubmit('acceptInvoice');"  />
                                <input type="submit" value="Refer To Engineer" onclick="return doInvoiceEscalatedToChFormSubmit('invoiceReferToEng');"  />
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="invoiceEscalatedToChMessageBox"></div>
            </div>
        </fieldset>
    </form>
</div>