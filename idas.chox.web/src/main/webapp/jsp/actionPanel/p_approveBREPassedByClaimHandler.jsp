<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(document).ready(function(){
        doFormValidation();
    });

    function doRejectClaim(){

        registeAction('reject');

        if(doFormValidation().form()){

            if(!confirm('Are you sure you want to reject this claim?')){
                return false;
            }

            // $("#reasonOfRejectionIdHolder").val($("#InvoiceReasonOfRejectionId").val());

        }else{
            return false;
        }

        return true;
    }

    function doFormValidation(){
        var validateFlag = $("#approveBREPassedByClaimHandler").validate(
        {
            errorLabelContainer: "#ActionPanelMessageBox",
            rules: {
                actionName:{required:true},
                reasonOfRejectionId:{required:isRejected}
            },
            messages: {
                actionName:{required:"You must select action"},
                reasonOfRejectionId:{required:"You must choose a 'Reason For Rejection'"}
            }

        });

        return validateFlag;
    }

    function doSubmit(a){

        registeAction(a);

        // $("#InvoiceReasonOfRejectionId").val("");

        isClaimNumberInvalid();
        // isFormClaimNumberInvalid("approveBREPassedByClaimHandler");
        if(!doFormValidation().form()){
            return false;
        }
        return true;

    }

</script>

<form onsubmit="return true;" action="<%=request.getContextPath()%>/prv/approveBREPassedByClaimHandler.action"
      method="post" id="approveBREPassedByClaimHandler" name="approveBREPassedByClaimHandler">
    <fieldset class="x-fieldset">

        <legend>Invoice Escalated To Claim Handler - Action Required</legend>
        <s:hidden name="id" />
        <s:hidden id="actionName" name="actionName" />
        <!--
        <s:hidden id="reasonOfRejectionIdHolder" name="invoice.reasonOfRejectionId"/>
        !-->
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
                            <input type="submit" value="Reject Invoice"  onclick="return doRejectClaim();" />
                            <input type="submit" value="Clear For Payment" onclick="return doSubmit('accept');"  />
                            <input type="submit" value="Refer To Engineer" onclick="return doSubmit('InvReferEng');"  />
                        </td>
                    </tr>
                </table>
            </div>
            <div class="action-error-msg" id="ActionPanelMessageBox"></div>
        </div>
    </fieldset>
</form>