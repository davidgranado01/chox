<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    $(function(){

        $("form#approveBREPassedClaim").validate(
        {
            errorLabelContainer: "#approveBREPassedClaimMessageBox",
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
    
    function doApproveBREPassedClaimSubmit(action){
        
        actionPanel.registerAction(action);

        $("form#approveBREPassedClaim #reasonOfRejectionId").rules("remove");
        if(action=="rejectInvoice"){
            $("form#approveBREPassedClaim #reasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });
        }else{
            $("form#approveBREPassedClaim #reasonOfRejectionId").val("");
        }

        if($("#approveBREPassedClaim").valid()){

            if (action=='rejectInvoice') {
                var reasonOfRejection = $("#reasonOfRejectionId").val();
                if (reasonOfRejection == <s:property value="invoiceLiabilityDisputeReasonId" /> && !confirm("Where there is a dispute with liability and the invoice has been approved on a quantum basis, ensure that the 'Liability Status' is up to date and click on the 'Clear For Payment' button, the invoice will be allocated to a holding status until liability is resolved.  Are you sure you wish to proceed with the invoice rejection based on the information provided?")) {
                    return;
                }
                else if(reasonOfRejection != <s:property value="invoiceLiabilityDisputeReasonId" /> && !confirm('Are you sure you want to reject this claim?')){
                    return;
                }
            }

            $("form#approveBREPassedClaim").submit();
        }
    } 
    
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="approveBREPassedClaim" name="approveBREPassedClaim" action="<%=request.getContextPath()%>/prv/processClaim.action"
          method="POST" >
        <fieldset class="x-fieldset">
            <legend>BRE Approved Claim - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="name" name="name" />
            <div>
                <div class="status-info">
                    <s:if test="insurerIsEngineersEnabled">
                        This claim and it's related invoice have been cleared by the CHOX approval system. Please review the invoice and claim information supplied, and choose whether to clear the invoice for payment, reject the invoice or refer the invoice to an Engineer.
                    </s:if>
                    <s:else>
                        This claim and it's related invoice have been cleared by the CHOX approval system. Please review the invoice and claim information supplied, and choose whether to clear the invoice for payment or reject the invoice.
                    </s:else>
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
                                <input type="button" value="Reject Invoice"  onclick="return doApproveBREPassedClaimSubmit('rejectInvoice');" />
                                <input type="button" value="Clear For Payment" onclick="return doApproveBREPassedClaimSubmit('acceptInvoice');"  />
                                <s:if test="insurerIsEngineersEnabled">
                                    <input type="button" value="Refer To Engineer" onclick="return doApproveBREPassedClaimSubmit('invoiceReferToEng');"  />
                                </s:if>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="approveBREPassedClaimMessageBox"></div>
            </div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    </form>
</div>