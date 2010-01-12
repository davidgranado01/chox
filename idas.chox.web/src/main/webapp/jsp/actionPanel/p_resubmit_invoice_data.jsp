<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<form id="resubmitInvoiceForm" name="resubmitInvoiceForm" onsubmit="return true;" action="<%=request.getContextPath()%>/prv/processClaim.action" method="post">
    <fieldset class="x-fieldset"><legend>Claim Data - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="name" name="name" value="resubmitInvoice"/>
        <div>
            <div class="status-info">
                Please review the 'History' tab for details on the why the claim has been rejected, amend details accordingly and re-submit.
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
                        <td><input type="submit" value="Re-Submit Claim/Invoice" /></td>
                    </tr>
                </table>                
            </div>
        </div>
    </fieldset> 
</form>