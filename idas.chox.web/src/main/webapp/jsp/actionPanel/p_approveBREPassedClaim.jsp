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
        var validateFlag = $("#approveBREPassedClaim").validate(
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

        //$("#reasonOfRejectionId").val("");
        
        isClaimNumberInvalid();
        if(!doFormValidation().form()){
            return false;
        }
        return true;
        
    } 
    
</script>

<form onsubmit="return true;" action="<%=request.getContextPath()%>/prv/approveBREPassedClaim.action"
      method="post" id="approveBREPassedClaim" name="approveBREPassedClaim">
    <fieldset class="x-fieldset">
        <legend>BRE Approved Claim - Action Required</legend>
        <s:hidden name="id" />
        <s:hidden id="actionName" name="actionName" />
        <!--
        <s:hidden id="reasonOfRejectionIdHolder" name="invoice.reasonOfRejectionId"/>
        !-->
        <div>
            <div class="status-info">             
                This claim and it's related invoice have been cleared by the CHOX approval system. Please review the invoice and claim information supplied, and choose whether to clear the invoice for payment, reject the invoice or refer the invoice to an Engineer.
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