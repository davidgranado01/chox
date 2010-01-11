<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    $(document).ready(function(){
        doFormValidation();
    });
    
    function doRejectClaim(){
        
        actionPanel.registerAction('reject');
        
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
    
    function isRejected(){
        var sActionName = $("#actionName").val();
        if(sActionName=="reject"){
            return true;
        }
        return false;
    }
    
    function doFormValidation(){
        var validateFlag = $("#approveContestedInvoice").validate(
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
        actionPanel.registerAction(a);
        // $("#InvoiceReasonOfRejectionId").val("");
        if(!doFormValidation().form()){
            return false;
        }
        return true;
        
    } 
    
</script>

<form onsubmit="return true;" action="<%=request.getContextPath()%>/prv/ContestedInvoiceByCH.action" method="post"
      id="approveContestedInvoice" name="approveContestedInvoice">
    <fieldset class="x-fieldset">
        <legend>Referred Invoice - Action Required</legend>
        <s:hidden name="id" />
        <s:hidden id="actionName" name="actionName" />
        <!--
        <s:hidden id="reasonOfRejectionIdHolder" name="invoice.reasonOfRejectionId"/>
        !-->

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
                            <input type="submit" value="Reject Invoice"  onclick="return doRejectClaim();" />
                            <input type="submit" value="Clear For Payment" onclick="return doSubmit('accept');"  />
                            <input type="submit" value="Refer To Claim Handler" onclick="return doSubmit('referCH');"  />  
                        </td>
                    </tr>
                </table>
            </div>
            <div class="action-error-msg" id="ActionPanelMessageBox"></div>
        </div> 
    </fieldset>
</form>
