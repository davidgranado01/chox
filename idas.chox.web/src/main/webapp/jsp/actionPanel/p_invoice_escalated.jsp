<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){

        $("form#invoiceExcalatedForm").validate(
        {
            errorLabelContainer: "#invoiceExcalatedFormMessageBox",
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


    function doInvoiceExcalatedFormSubmit(action){

        actionPanel.registerAction(action);

        $("form#invoiceExcalatedForm #reasonOfRejectionId").rules("remove");
        if(action=="rejectInvoice"){
            $("form#invoiceExcalatedForm #reasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });
        }else{
            $("form#invoiceExcalatedForm #reasonOfRejectionId").val("");
        }

        if($("#invoiceExcalatedForm").valid()){

            if (action=='rejectInvoice') {
                var reasonOfRejection = $("#reasonOfRejectionId").val();
                if (reasonOfRejection == <s:property value="invoiceLiabilityDisputeReasonId" /> && !confirm("Where there is a dispute with liability and the invoice has been approved on a quantum basis, ensure that the 'Liability Status' is up to date and click on the 'Clear For Payment' button, the invoice will be allocated to a holding status until liability is resolved.  Are you sure you wish to proceed with the invoice rejection based on the information provided?")) {
                    return;
                }
                else if(reasonOfRejection != <s:property value="invoiceLiabilityDisputeReasonId" /> && !confirm('Are you sure you want to reject this claim?')){
                    return;
                }
            }

            $("form#invoiceExcalatedForm").submit();
        }
    }

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form onsubmit="return true;" action="<%=request.getContextPath()%>/prv/processClaim.action" method="post"
          id="invoiceExcalatedForm" name="invoiceExcalatedForm">
        <fieldset class="x-fieldset">
            <legend>Escalated Invoice - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="name" name="name"/>
            <div>
                <div class="status-info">
                    Please review the ‘History’ tab for details on why the claim has failed the validation rules.
                    Please decide on whether to refer the claim to a Claims Handler or reject the claim back to the CHO.
                    Please enter any relevant details/comments on the ‘Notes’ tab regarding the decision made.
                </div>
                <div class="status-control-set">
                    <table>
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
                                <input type="button" value="Reject Invoice" onclick="javascript: return doInvoiceExcalatedFormSubmit('rejectInvoice');" />
                                <input type="button" value="Refer To Claims Handler" onclick="return doInvoiceExcalatedFormSubmit('invoiceReferToCH');"  />
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="invoiceExcalatedFormMessageBox"></div>
            </div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    </form>
</div>