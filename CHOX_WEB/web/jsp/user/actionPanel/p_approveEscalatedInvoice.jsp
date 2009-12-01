<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<script language="JavaScript">
    
    $(document).ready(function(){
        doFormValidation();
    });
    
    function doRejectClaim(){
        
        registeAction('reject');
        
        if(doFormValidation().form()){
            
            if(!confirm('Are you sure you want to reject this claim?')){
                return false;
            }
            
            $("#reasonOfRejectionIdHolder").val($("#InvoiceReasonOfRejectionId").val());
            
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
        var validateFlag = $("#approveEscalatedInvoice").validate(
        {
            errorLabelContainer: "#ActionPanelMessageBox",                
            rules: {
                actionName:{required:true},
                InvoiceReasonOfRejectionId:{required:isRejected}
            },
            messages: {
                actionName:{required:"You must select action"},
                InvoiceReasonOfRejectionId:{required:"You must choose a 'Reason For Rejection'"}       
            }
            
        });
        
        return validateFlag;
    }
    
    function doSubmit(a){
        
        registeAction(a);
        $("#InvoiceReasonOfRejectionId").val("");
        if(!doFormValidation().form()){
            return false;
        }
        return true;
        
    } 
    
</script>

<form onsubmit="return true;" action="user/approveEscalatedInvoice.action" method="post" 
      id="approveEscalatedInvoice" name="approveEscalatedInvoice">
    <fieldset class="x-fieldset">
        <legend>Escalated Invoice - Action Required</legend>
        <s:hidden name="id" />
        <s:hidden id="actionName" name="actionName" />
        <s:hidden id="reasonOfRejectionIdHolder" name="invoice.reasonOfRejectionId"/>
        
        <div>
            <div class="status-info">
                Please review the 'History' tab for details on why the claim has been rejected. 
                Please decide on whether to progress the claim for payment or reject the claim. 
                Please enter any relevant details/comments on the 'Notes' tab regarding the decision made.
            </div>
            <div class="status-control-set">
                <table>
    <tr>
        <td width="30%" nowrap>
            <label>Reason for Rejection</label>
        </td>
        <td>
            <s:select name="InvoiceReasonOfRejectionId" id="InvoiceReasonOfRejectionId"
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
                            <input type="submit" value="Refer To Claims Handler" onclick="return doSubmit('referCH');"  />  
                        </td>
                    </tr>
                </table>
            </div>
            <div class="errorBox" id="ActionPanelMessageBox"></div>
        </div> 
    </fieldset>
</form>
