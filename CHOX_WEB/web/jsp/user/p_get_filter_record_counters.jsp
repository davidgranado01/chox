<%@ taglib uri="/struts-tags" prefix="s" %>

<ul class="inbox">
    <s:if test="filterAccessibility.isNewClaimsToBeroutedAccessible">
        <li><a href="javascript:showClaimByStatus('ClaimUnacknowledgedUnrouted', false);" >New Claims to be Routed (<s:property value="filterRecordCounter.newClaimsToBeroutedCount" />)</a></li>
    </s:if>                         
    <s:if test="filterAccessibility.isRejectedClaimsAccessible">
        <li><a href="javascript:showClaimByStatus('ClaimRejected', false);" >Rejected Claims (<s:property value="filterRecordCounter.rejectedClaimsCount" />)</a></li>
    </s:if> 
    <s:if test="filterAccessibility.isIncorrectInvoiceDataCalculationsAccessible">
        <li><a href="javascript:showClaimByStatus('InvoiceDataCalculationIncorrect', false);" >Incorrect Invoice Data Calculations (<s:property value="filterRecordCounter.incorrectInvoiceDataCalculationsCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isClaimsAwaitingHireMonitoringInformationAccessible">
        <li><a href="javascript:showClaimByStatus('AwaitingCarHireInfo', false);" >Claims Awaiting Hire Monitoring Information (<s:property value="filterRecordCounter.claimsAwaitingHireMonitoringInformationCount" />)</a></li>
    </s:if> 
    <s:if test="filterAccessibility.isClaimsAwaitingAcknowledgementAccessible">
        <li><a href="javascript:showClaimByStatus('ClaimUnacknowledgedRouted', true);" >Claims Awaiting Acknowledgement (<s:property value="filterRecordCounter.claimsAwaitingAcknowledgementCount" />)</a></li>
    </s:if> 
    <s:if test="filterAccessibility.isReSubmittedClaimsAwaitingAcknowledgementAccessible">
        <li><a href="javascript:showClaimByStatus('ClaimRejectionContested', true);" >Re-Submitted Claims Awaiting Acknowledgement (<s:property value="filterRecordCounter.reSubmittedClaimsAwaitingAcknowledgementCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isClaimPendingAccessible">
        <li><a href="javascript:showClaimByStatusWithSort('ClaimPending','statusModifiedDate', true);" >Claim Pending (<s:property value="filterRecordCounter.ClaimPendingCount" />)</a></li>
    </s:if>                        
    <s:if test="filterAccessibility.isHireUpdateAnomaliesAccessible">
        <li><a href="javascript:showClaimIsAnomalies(true);" >Hire Update Notifications/Warnings (<s:property value="filterRecordCounter.hireUpdateAnomaliesCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isClaimReferredToEngineerAccessible">
        <li><a href="javascript:showClaimByStatus('ClaimReferredToEngineer', false);" >Claim Referred To Engineer (<s:property value="filterRecordCounter.ClaimReferredToEngineerCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isClaimReferredToFNOLAccessible">
        <li><a href="javascript:showClaimByStatus('ClaimReferredToFNOL', false);" >Claims To Be Registered (<s:property value="filterRecordCounter.ClaimReferredToFNOLCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isClaimUpdatedByEngineerAccessible">
        <li><a href="javascript:showClaimByStatus('ClaimUpdatedByEngineer', true);" >Claims Updated By Engineer (<s:property value="filterRecordCounter.ClaimUpdatedByEngineerCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isContestedInvoicesReferredToCHOAccessible">
        <li><a href="javascript:showClaimByStatus('ContestedInvoiceReferredToCHO', false);" >Contested Invoices Referred To CHO (<s:property value="filterRecordCounter.contestedInvoicesReferredToCHOCount" />)</a></li>
    </s:if>    
    <s:if test="filterAccessibility.isApprovedInvoicesAwaitingPaymentAccessible">
        <li><a href="javascript:showClaimByStatus('AwaitingInvoicePayment', true);" >Approved Invoices Awaiting Payment (<s:property value="filterRecordCounter.approvedInvoicesAwaitingPaymentCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isEscalatedInvoicesAccessible">
        <li><a href="javascript:showClaimByStatus('InvoiceEscalated', false);" >Escalated Invoices (<s:property value="filterRecordCounter.escalatedInvoicesCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isInvoiceEscalatedToHandlerAccessible">
        <li><a href="javascript:showClaimByStatus('InvoiceEscalatedToHandler', true);" >Escalated Invoices To Handler (<s:property value="filterRecordCounter.escalatedInvoicesToHandlerCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isContestedInvoicesReferredToInsurerAccessible">
        <li><a href="javascript:showClaimByStatus('ContestedInvoiceReferredToInsurer', true);" >Contested Invoices Referred To Insurer (<s:property value="filterRecordCounter.contestedInvoicesReferredToInsurerCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isInvoicesApprovedByBREAccessible">
        <li><a href="javascript:showClaimByStatus('InvoiceApprovedByBRE', true);" >Invoices Approved By BRE (<s:property value="filterRecordCounter.invoicesApprovedByBRECount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isPenaltyChargesAppliedAccessible">
        <li><a href="javascript:showClaimIspenaltyChargeApplied();" >Penalty Charges To Be Applied (<s:property value="filterRecordCounter.PenaltyChargesAppliedCount" />)</a></li>
    </s:if>  
    <s:if test="filterAccessibility.isInvoiceReferredToClaimsHandlerAccessible">
        <li><a href="javascript:showClaimByStatus('InvoiceReferredToClaimsHandler', true);" >Invoice Referred By Engineer  (<s:property value="filterRecordCounter.InvoiceReferredToClaimsHandlerCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isInvoicePaymentLoggedAccessible">
        <li><a href="javascript:showClaimByStatus('InvoicePaymentLogged', false);" >Payments to be received (<s:property value="filterRecordCounter.InvoicePaymentLoggedCount" />)</a></li>
    </s:if>  
    <s:if test="filterAccessibility.isContestedInvoiceReferToEngAccessible">
        <li><a href="javascript:showClaimByStatus('InvoiceReferredToEngineer', false);" >Invoice Referred By Claim Handler (<s:property value="filterRecordCounter.ContestedInvoiceReferToEngAccessibleCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isClaimUnacknowledgedUnassignedAccessible">
        <li><a href="javascript:showClaimByStatus('ClaimUnacknowledgedUnassigned', true);" >New Claims to be Assigned (<s:property value="filterRecordCounter.ClaimUnacknowledgedUnassignedAccessible" />)</a></li>
    </s:if> 
</ul>                            


