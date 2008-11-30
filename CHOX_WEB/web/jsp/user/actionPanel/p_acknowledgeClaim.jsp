<%-- 
    Document   : p_acknowledgeClaim
    Created on : Nov 28, 2008, 11:54:54 AM
    Author     : Carlson 
    Updated    : Emmanuel 
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <p>Please enter details of the claim review and decide whether to acknowledge or reject the claim.  Please use the 'Notes' tab in order to communicate detailed comments you may have for the CHO.</p>
        
        Plus buttons to either acknowledge or reject claim.
        
        <div class="form-container">
            <form onsubmit="return true;" action="user/acknowledge.action" method="post" id="route" name="route">
                <s:hidden name="id" />
                
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Indemnity (Decimal)</label>
                    <input type="text" class="chox-ttxt" name="indemintyAmount" />
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">% Liability Accepted</label>
                    <input type="text" class="chox-ttxt" name="percentageLiabilityAccepted" />
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Claim Number</label>
                    <input type="text" class="chox-ttxt" name="claimNumber" />
                </div>                
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Quantum Dispute?</label>
                    <s:checkbox name="isQuantumDispute" />
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Engineer’s Claim Review Notes</label>
                    <textarea class="chox-tta" cols="20" rows="5" name="engineerClaimReviewNotes"></textarea>
                </div>                
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Invoice Review Required</label>
                    <s:checkbox name="isInvoiceReviewRequired" />
                </div>  
                <div class="chox-form-button">
                    <input type="submit" value="Acknowledge" />
                </div>
                <div class="chox-form-submit-result">&nbsp;</div>
            </form>
        </div>
    </body>
</html>
