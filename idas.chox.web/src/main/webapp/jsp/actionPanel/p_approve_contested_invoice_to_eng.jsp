<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){

        $("form#invoiceReferredByEngForm").validate(
        {
            errorLabelContainer: "#invoiceReferredByEngMessageBox",
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

    
    function doInvoiceReferredByEngSubmit(action){


        actionPanel.registerAction(action);

        $("form#invoiceReferredByEngForm #reasonOfRejectionId").rules("remove");
        if(action=="rejectInvoice"){
            $("form#invoiceReferredByEngForm #reasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });
        }else{
            $("form#invoiceReferredByEngForm #reasonOfRejectionId").val("");
        }

        if($("#invoiceReferredByEngForm").valid()){

            if(action=='rejectInvoice' && !confirm('Are you sure you want to reject this claim?')){
                return;
            }

            $("form#invoiceReferredByEngForm").submit();
        }
    }

</script>

<form onsubmit="return true;" action="<%=request.getContextPath()%>/prv/processClaim.action" method="post"
      id="invoiceReferredByEngForm" name="invoiceReferredByEngForm">
    <fieldset class="x-fieldset">
        <legend>Referred Invoice - Action Required</legend>
        <s:hidden id="claimId" name="id" />
        <s:hidden id="name" name="name"/>
        <div>
            <div class="status-info">
                Please review the 'Notes' tab for the reason why the invoice has been referred for further attention. 
                Please decide on whether to progress the claim for payment, refer the claim back or reject the invoice.
                Please provide appropriate notes on the 'Notes' tab regarding the decision made.
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
                            <input type="submit" value="Reject Invoice"  onclick="return doInvoiceReferredByEngSubmit('rejectInvoice');" />
                            <input type="submit" value="Clear For Payment" onclick="return doInvoiceReferredByEngSubmit('acceptInvoice');"  />
                            <input type="submit" value="Refer To Claim Handler" onclick="return doInvoiceReferredByEngSubmit('invoiceReferToCH');"  />
                        </td>
                    </tr>
                </table>
            </div>
            <div class="action-error-msg" id="invoiceReferredByEngMessageBox"></div>
        </div> 
    </fieldset>
</form>