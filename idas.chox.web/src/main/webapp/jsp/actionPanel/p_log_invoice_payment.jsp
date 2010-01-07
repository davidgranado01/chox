<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<form onsubmit="return true;" action="<%=request.getContextPath()%>/prv/logInvoicePayment.action" method="post" id="logInvoicePayment" name="logInvoicePayment">
    
    
    <fieldset class="x-fieldset"><legend>Invoice ready for payment - Action Required</legend>                
        
        <div>
            <div class="status-info">
                Please update the claim by recording that a payment has been logged against this claim.
            </div>
            <s:hidden name="id" />
                
            <div class="status-info-submit">
                <table>
                    <tr>
                        <td>
                            <div class="no-format">
                                <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                            </div>
                        </td>
                    </tr>
                    <tr>                        
                        <td>
                        <input type="submit" value="Invoice Payment Logged" />                  </td>
                    </tr>
                </table>                
            </div>
        </div>
    </fieldset> 
    
</form>    




