<%-- 
    Document   : p_logInvoicePayment
    Created on : 02-Dec-2008, 18:25:55
    Author     : Emmanuel
--%>


<%@ taglib uri="/struts-tags" prefix="s" %>

<form onsubmit="return true;" action="user/logInvoicePayment.action" method="post" id="logInvoicePayment" name="logInvoicePayment">
    
    
    <fieldset class="x-fieldset"><legend>Invoice ready for payment - Action Required</legend>                
        
        <div>
            <div class="status-info">
                Please update the claim by recording that a payment has been logged against the claim in question.
            </div>
            <s:hidden name="id" />
            
            <div class="status-info-submit">
                <input type="submit" value="Invoice Payment Logged" />
            </div>
        </div>
    </fieldset> 
    
</form>    




