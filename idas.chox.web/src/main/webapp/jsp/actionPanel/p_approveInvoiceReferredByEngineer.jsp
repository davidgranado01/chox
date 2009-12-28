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
            
           //  $("#reasonOfRejectionIdHolder").val($("#InvoiceReasonOfRejectionId").val());
        
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
        var validateFlag = $("#approveInvoiceRefferedByEngineer").validate(
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
        //$("#InvoiceReasonOfRejectionId").val("");
        if(!doFormValidation().form()){
            return false;
        }
                
        return true;
        
    } 
    
</script>

<form onsubmit="return true;" action="<%=request.getContextPath()%>/prv/approveInvoiceRefferedByEngineer.action"
    method="post" id="approveInvoiceRefferedByEngineer" name="approveInvoiceRefferedByEngineer">
    <fieldset class="x-fieldset">
        <legend>Invoices Referred By Engineer - Action Required</legend>
        <s:hidden name="id" />
        <s:hidden id="actionName" name="actionName" />
        <!--
        <s:hidden id="reasonOfRejectionIdHolder" name="invoice.reasonOfRejectionId"/>
        !-->
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
            <input type="submit" value="Reject Invoice"  onclick="return doRejectClaim();" />
            <input type="submit" value="Clear For Payment" onclick="return doSubmit('accept');"  />
            <input type="submit" value="Refer To Engineer" onclick="return doSubmit('InvReferEng');"  /> 
        </td>
    </tr>
</table>
            </div>
            <div class="errorBox" id="ActionPanelMessageBox"></div>
        </div> 
    </fieldset>
</form>