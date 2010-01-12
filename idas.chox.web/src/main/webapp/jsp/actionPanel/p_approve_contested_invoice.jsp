<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">


    $(function(){

        $("form#contestedInvoiceRefToInsurer").validate(
        {
            errorLabelContainer: "#contestedInvoiceRefToInsurerMessageBox",
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

    function docontestedInvoiceRefToInsurerSubmit(action){


        actionPanel.registerAction(action);

        $("form#contestedInvoiceRefToInsurer #reasonOfRejectionId").rules("remove");
        
        if(action=="rejectInvoice"){
            
            $("form#contestedInvoiceRefToInsurer #reasonOfRejectionId").rules("add", {
                required: true,
                messages: {required: "You must choose a 'Reason For Rejection'"}
            });
            
        }else{

            $("form#contestedInvoiceRefToInsurer #reasonOfRejectionId").val("");
            
        }

        if($("form#contestedInvoiceRefToInsurer").valid()){

            if(action=='rejectInvoice' && !confirm('Are you sure you want to reject this claim?')){
                return;
            }

            $("form#contestedInvoiceRefToInsurer").submit();
        }
    }
    
</script>

<form onsubmit="return true;" action="<%=request.getContextPath()%>/prv/processClaim.action" method="post"
      id="contestedInvoiceRefToInsurer" name="contestedInvoiceRefToInsurer">
    <fieldset class="x-fieldset">
        <legend>Contested Invoice - Action Required</legend>
        <s:hidden id="claimId" name="id" />
        <s:hidden id="name" name="name"/>
        <div>
            <div class="status-info">
                Please review the 'History' tab for details on why the claim has been rejected and review the details/comments on the 'Notes' tab regarding the previous decision to reject. Please decide on whether to progress the claim for payment, refer the claim to an Engineer or reject the claim. Please provide appropriate notes on the 'Notes' tab regarding the decision made.
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
                            <input type="submit" value="Reject Invoice"  onclick="return docontestedInvoiceRefToInsurerSubmit('rejectInvoice');" />
                            <input type="submit" value="Clear For Payment" onclick="return docontestedInvoiceRefToInsurerSubmit('acceptInvoice');"  />
                            <input type="submit" value="Refer To Engineer" onclick="return docontestedInvoiceRefToInsurerSubmit('invoiceReferToEng');"  />
                        </td>
                    </tr>
                </table>
            </div>
            <div class="action-error-msg" id="contestedInvoiceRefToInsurerMessageBox"></div>
        </div> 
    </fieldset>
</form>
