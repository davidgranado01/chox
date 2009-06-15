<%@ taglib uri="/struts-tags" prefix="s" %>

<ul class="inbox">
    <s:if test="filterAccessibility.isNewClaimsToBeroutedAccessible">
        <li><a href="javascript:showClaimByStatus('ClaimUnacknowledgedUnrouted');" >New Claims to be Routed (<s:property value="filterRecordCounter.newClaimsToBeroutedCount" />)</a></li>
    </s:if>                         
    <s:if test="filterAccessibility.isRejectedClaimsAccessible">
        <li><a href="javascript:showClaimByStatus('ClaimRejected');" >Rejected Claims (<s:property value="filterRecordCounter.rejectedClaimsCount" />)</a></li>
    </s:if> 
    <s:if test="filterAccessibility.isIncorrectInvoiceDataCalculationsAccessible">
        <li><a href="javascript:showClaimByStatus('InvoiceDataCalculationIncorrect');" >Incorrect Invoice Data Calculations (<s:property value="filterRecordCounter.incorrectInvoiceDataCalculationsCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isClaimsAwaitingHireMonitoringInformationAccessible">
        <li><a href="javascript:showClaimByStatus('AwaitingCarHireInfo');" >Claims Awaiting Hire Monitoring Information (<s:property value="filterRecordCounter.claimsAwaitingHireMonitoringInformationCount" />)</a></li>
    </s:if> 
    <s:if test="filterAccessibility.isClaimsAwaitingAcknowledgementAccessible">
        <li><a href="javascript:showClaimByStatus('ClaimUnacknowledgedRouted');" >Claims Awaiting Acknowledgement (<s:property value="filterRecordCounter.claimsAwaitingAcknowledgementCount" />)</a></li>
    </s:if> 
    <s:if test="filterAccessibility.isReSubmittedClaimsAwaitingAcknowledgementAccessible">
        <li><a href="javascript:showClaimByStatus('ClaimRejectionContested');" >Re-Submitted Claims Awaiting Acknowledgement (<s:property value="filterRecordCounter.reSubmittedClaimsAwaitingAcknowledgementCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isClaimPendingAccessible">
        <li><a href="javascript:showClaimByStatusWithSort('ClaimPending','statusModifiedDate');" >Claim Pending (<s:property value="filterRecordCounter.ClaimPendingCount" />)</a></li>
    </s:if>                        
    <s:if test="filterAccessibility.isHireUpdateAnomaliesAccessible">
        <li><a href="javascript:showClaimIsAnomalies();" >Hire Update Anomalies (<s:property value="filterRecordCounter.hireUpdateAnomaliesCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isClaimReferredToEngineerAccessible">
        <li><a href="javascript:showClaimByStatus('ClaimReferredToEngineer');" >Claim Referred To Engineer (<s:property value="filterRecordCounter.ClaimReferredToEngineerCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isClaimReferredToFNOLAccessible">
        <li><a href="javascript:showClaimByStatus('ClaimReferredToFNOL');" >Claims To Be Registered (<s:property value="filterRecordCounter.ClaimReferredToFNOLCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isClaimUpdatedByEngineerAccessible">
        <li><a href="javascript:showClaimByStatus('ClaimUpdatedByEngineer');" >Claims Updated By Engineer (<s:property value="filterRecordCounter.ClaimUpdatedByEngineerCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isContestedInvoicesReferredToCHOAccessible">
        <li><a href="javascript:showClaimByStatus('ContestedInvoiceReferredToCHO');" >Contested Invoices Referred To CHO (<s:property value="filterRecordCounter.contestedInvoicesReferredToCHOCount" />)</a></li>
    </s:if>    
    <s:if test="filterAccessibility.isApprovedInvoicesAwaitingPaymentAccessible">
        <li><a href="javascript:showClaimByStatus('AwaitingInvoicePayment');" >Approved Invoices Awaiting Payment (<s:property value="filterRecordCounter.approvedInvoicesAwaitingPaymentCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isEscalatedInvoicesAccessible">
        <li><a href="javascript:showClaimByStatus('InvoiceEscalated');" >Escalated Invoices (<s:property value="filterRecordCounter.escalatedInvoicesCount" />)</a></li>
    </s:if> 
    <s:if test="filterAccessibility.isContestedInvoicesReferredToInsurerAccessible">
        <li><a href="javascript:showClaimByStatus('ContestedInvoiceReferredToInsurer');" >Contested Invoices Referred To Insurer (<s:property value="filterRecordCounter.contestedInvoicesReferredToInsurerCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isInvoicesApprovedByBREAccessible">
        <li><a href="javascript:showClaimByStatus('InvoiceApprovedByBRE');" >Invoices Approved By BRE (<s:property value="filterRecordCounter.invoicesApprovedByBRECount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isPenaltyChargesAppliedAccessible">
        <li><a href="javascript:showClaimIspenaltyChargeApplied();" >Penalty Charges To Be Applied (<s:property value="filterRecordCounter.PenaltyChargesAppliedCount" />)</a></li>
    </s:if>  
    <s:if test="filterAccessibility.isInvoiceReferredToClaimsHandlerAccessible">
        <li><a href="javascript:showClaimByStatus('InvoiceReferredToClaimsHandler');" >Invoice Referred By Engineer  (<s:property value="filterRecordCounter.InvoiceReferredToClaimsHandlerCount" />)</a></li>
    </s:if>
    <s:if test="filterAccessibility.isInvoicePaymentLoggedAccessible">
        <li><a href="javascript:showClaimByStatus('InvoicePaymentLogged');" >Payments to be received (<s:property value="filterRecordCounter.InvoicePaymentLoggedCount" />)</a></li>
    </s:if>  
    <s:if test="filterAccessibility.isContestedInvoiceReferToEngAccessible">
        <li><a href="javascript:showClaimByStatus('InvoiceReferredToEngineer');" >Invoice Referred By Claim Handler (<s:property value="filterRecordCounter.ContestedInvoiceReferToEngAccessibleCount" />)</a></li>
    </s:if> 
</ul>                            


