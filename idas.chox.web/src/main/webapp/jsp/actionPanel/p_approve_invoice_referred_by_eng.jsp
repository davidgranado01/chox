<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    $(function(){

        $("form#invoiceReferredToClaimsHandler").validate(
        {
            errorLabelContainer: "#invoiceReferredToClaimsHandlerMessageBox",
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

    function doInvoiceReferredToClaimsHandlerSubmit(action){


        actionPanel.registerAction(action);

        $("form#invoiceReferredToClaimsHandler #reasonOfRejectionId").rules("remove");

        if(action=="rejectInvoice"){

            $("form#invoiceReferredToClaimsHandler #reasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });

        }else{

            $("form#invoiceReferredToClaimsHandler #reasonOfRejectionId").val("");

        }

        if($("form#invoiceReferredToClaimsHandler").valid()){

            if(action=='rejectInvoice' && !confirm('Are you sure you want to reject this claim?')){
                return;
            }

            $("form#invoiceReferredToClaimsHandler").submit();
        }
    }

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form onsubmit="return true;" action="<%=request.getContextPath()%>/prv/processClaim.action"
          method="post" id="invoiceReferredToClaimsHandler" name="invoiceReferredToClaimsHandler">
        <fieldset class="x-fieldset">
            <legend>Invoices Referred By Engineer - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="name" name="name"/>
            <div>
                <div class="status-info">
                    This claim and its related invoice have been referred by an Engineer. Please review the invoice and claim information supplied along with the reason for referral, and choose whether to clear the invoice for payment, reject the invoice or refer the invoice to an Engineer.
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
                                <input type="submit" value="Reject Invoice"  onclick="return doInvoiceReferredToClaimsHandlerSubmit('rejectInvoice');" />
                                <input type="submit" value="Clear For Payment" onclick="return doInvoiceReferredToClaimsHandlerSubmit('acceptInvoice');"  />
                                <input type="submit" value="Refer To Engineer" onclick="return doInvoiceReferredToClaimsHandlerSubmit('invoiceReferToEng');"  />
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="invoiceReferredToClaimsHandlerMessageBox"></div>
            </div>
        </fieldset>
    </form>
</div>