<%-- 
    Document   : insurer_board
    Created on : 02-Feb-2009, 16:19:35
    Author     : Emmanuel
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script>
    
    Ext.onReady(function(){
        
        new Ext.ToolTip({target: 'tip1',html: 'Description for Number of Claim Notifications Submitted'});
        new Ext.ToolTip({target: 'tip2',html: 'Description for Number of Claim Notifications Accepted'});
        new Ext.ToolTip({target: 'tip3',html: 'Description for Number of Claim Notifications Rejected'});
        new Ext.ToolTip({target: 'tip4',html: 'Description for Number of Claim Notifications Pending'});
        new Ext.ToolTip({target: 'tip5',html: 'Description for Number of Invoices Submitted'});
        new Ext.ToolTip({target: 'tip6',html: 'Description for Value of Invoices Submitted'});
        new Ext.ToolTip({target: 'tip7',html: 'Description for Number of Invoices Accepted'});
        new Ext.ToolTip({target: 'tip8',html: 'Description for Value of Invoices Accepted'});
        new Ext.ToolTip({target: 'tip9',html: 'Description for Number of Invoices Rejected'});
        new Ext.ToolTip({target: 'tip10',html: 'Description for Value of Invoices Rejected'});
        new Ext.ToolTip({target: 'tip11',html: 'Description for Number of Invoices Pending'});
        new Ext.ToolTip({target: 'tip12',html: 'Description for Value of Invoices Pending'});
        new Ext.ToolTip({target: 'tip13',html: 'Description for Number of Invoices Close'});
        new Ext.ToolTip({target: 'tip14',html: 'Description for Value of Invoices Close'});             
        new Ext.ToolTip({target: 'tip15',html: 'Description for Number of Invoices Payment Logged'});
        new Ext.ToolTip({target: 'tip16',html: 'Description for Value of Invoices Payment Logged'}); 
        Ext.QuickTips.init();
});    
    
</script>

<table cellpadding="0" cellspacing="0" class="dashboard" style="width:99%;" border="0">
    <tr><td>&nbsp;</td>                                                               <td>Week To Date</td>                                                                                           <td>Month To Date</td>                                                                          <td>Cumulative</td></tr>
    <tr><th nowrap>Number of Claim Notifications Submitted<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png" id="tip1"/></th>	<td><label class="std-data-ro"><s:property value="w2DData.noOfClaimNotificationsSubmitted"/></label></td>	<td><label class="std-data-ro"><s:property value="m2DData.noOfClaimNotificationsSubmitted"/></label></td>	<td><label class="std-data-ro"><s:property value="cData.noOfClaimNotificationsSubmitted"/></label></td></tr>
    <tr><th nowrap>Number of Claim Notifications Accepted<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png"  id="tip2"/></th>	<td><label class="std-data-ro"><s:property value="w2DData.noOfClaimNotificationsAccepted"/></label></td>	<td><label class="std-data-ro"><s:property value="m2DData.noOfClaimNotificationsAccepted"/></label></td>	<td><label class="std-data-ro"><s:property value="cData.noOfClaimNotificationsAccepted"/></label></td></tr>
    <tr><th nowrap>Number of Claim Notifications Rejected<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png"  id="tip3"/></th>	<td><label class="std-data-ro"><s:property value="w2DData.noOfClaimNotificationsRejected"/></label></td>	<td><label class="std-data-ro"><s:property value="m2DData.noOfClaimNotificationsRejected"/></label></td>	<td><label class="std-data-ro"><s:property value="cData.noOfClaimNotificationsRejected"/></label></td></tr>
    <tr><th nowrap>Number of Claim Notifications Pending<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png"  id="tip4"/></th>	<td><label class="std-data-ro"><s:property value="w2DData.noOfClaimNotificationsPending"/></label></td>         <td><label class="std-data-ro"><s:property value="m2DData.noOfClaimNotificationsPending"/></label></td>         <td><label class="std-data-ro"><s:property value="cData.noOfClaimNotificationsPending"/></label></td></tr>
    <tr><th nowrap>Number of Invoices Submitted<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png"  id="tip5"/></th>                 <td><label class="std-data-ro"><s:property value="w2DData.noOfInvoicesSubmitted"/></label></td>                 <td><label class="std-data-ro"><s:property value="m2DData.noOfInvoicesSubmitted"/></label></td>                 <td><label class="std-data-ro"><s:property value="cData.noOfInvoicesSubmitted"/></label></td></tr>
    <tr><th nowrap>Value of Invoices Submitted<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png"  id="tip6"/></th>                  <td><label class="std-data-ro">£<s:property value="w2DData.valueOfInvoicesSubmitted"/></label></td>              <td><label class="std-data-ro">£<s:property value="m2DData.valueOfInvoicesSubmitted"/></label></td>              <td><label class="std-data-ro">£<s:property value="cData.valueOfInvoicesSubmitted"/></label></td></tr>
    <tr><th nowrap>Number of Invoices Accepted<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png"  id="tip7"/></th>                  <td><label class="std-data-ro"><s:property value="w2DData.noOfInvoicesAccepted"/></label></td>                  <td><label class="std-data-ro"><s:property value="m2DData.noOfInvoicesAccepted"/></label></td>                  <td><label class="std-data-ro"><s:property value="cData.noOfInvoicesAccepted"/></label></td></tr>
    <tr><th nowrap>Value of Invoices Accepted<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png"  id="tip8"/></th>                   <td><label class="std-data-ro">£<s:property value="w2DData.valueOfInvoicesAccepted"/></label></td>               <td><label class="std-data-ro">£<s:property value="m2DData.valueOfInvoicesAccepted"/></label></td>               <td><label class="std-data-ro">£<s:property value="cData.valueOfInvoicesAccepted"/></label></td></tr>
    <tr><th nowrap>Number of Invoices Rejected<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png"  id="tip9"/></th>                  <td><label class="std-data-ro"><s:property value="w2DData.noOfInvoicesRejected"/></label></td>                  <td><label class="std-data-ro"><s:property value="m2DData.noOfInvoicesRejected"/></label></td>                  <td><label class="std-data-ro"><s:property value="cData.noOfInvoicesRejected"/></label></td></tr>
    <tr><th nowrap>Value of Invoices Rejected<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png"  id="tip10"/></th>                   <td><label class="std-data-ro">£<s:property value="w2DData.valueOfInvoicesRejected"/></label></td>               <td><label class="std-data-ro">£<s:property value="m2DData.valueOfInvoicesRejected"/></label></td>               <td><label class="std-data-ro">£<s:property value="cData.valueOfInvoicesRejected"/></label></td></tr>
    <tr><th nowrap>Number of Invoices Pending<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png"  id="tip11"/></th>                   <td><label class="std-data-ro"><s:property value="w2DData.noOfInvoicesPending"/></label></td>                   <td><label class="std-data-ro"><s:property value="m2DData.noOfInvoicesPending"/></label></td>                   <td><label class="std-data-ro"><s:property value="cData.noOfInvoicesPending"/></label></td></tr>
    <tr><th nowrap>Value of Invoices Pending<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png"  id="tip12"/></th>                    <td><label class="std-data-ro">£<s:property value="w2DData.valueOfInvoicesPending"/></label></td>                <td><label class="std-data-ro">£<s:property value="m2DData.valueOfInvoicesPending"/></label></td>                <td><label class="std-data-ro">£<s:property value="cData.valueOfInvoicesPending"/></label></td></tr>
    <tr><th nowrap>Number of Invoices Closed<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png"  id="tip13"/></th>                     <td><label class="std-data-ro"><s:property value="w2DData.noOfInvoicesClosed"/></label></td>                    <td><label class="std-data-ro"><s:property value="m2DData.noOfInvoicesClosed"/></label></td>                    <td><label class="std-data-ro"><s:property value="cData.noOfInvoicesClosed"/></label></td></tr>
    <tr><th nowrap>Value of Invoices Closed<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png"  id="tip14"/></th>                      <td><label class="std-data-ro">£<s:property value="w2DData.valueOfInvoicesClosed"/></label></td>                 <td><label class="std-data-ro">£<s:property value="m2DData.valueOfInvoicesClosed"/></label></td>                 <td><label class="std-data-ro">£<s:property value="cData.valueOfInvoicesClosed"/></label></td></tr>
    <tr><th nowrap>Number of Invoices Payment Logged<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png"  id="tip15"/></th>                     <td><label class="std-data-ro"><s:property value="w2DData.noOfInvoicesPaymentLogged"/></label></td>                    <td><label class="std-data-ro"><s:property value="m2DData.noOfInvoicesPaymentLogged"/></label></td>                    <td><label class="std-data-ro"><s:property value="cData.noOfInvoicesPaymentLogged"/></label></td></tr>
    <tr><th nowrap>Value of Invoices Payment Logged<img class="tip-target" alt="" src="<%=request.getContextPath()%>/images/tip.png"  id="tip16"/></th>                      <td><label class="std-data-ro">£<s:property value="w2DData.valueOfInvoicesPaymentLogged"/></label></td>                 <td><label class="std-data-ro">£<s:property value="m2DData.valueOfInvoicesPaymentLogged"/></label></td>                 <td><label class="std-data-ro">£<s:property value="cData.valueOfInvoicesPaymentLogged"/></label></td></tr>
</table>



