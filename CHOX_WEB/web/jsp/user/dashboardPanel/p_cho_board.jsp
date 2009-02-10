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

<table cellpadding="0" cellspacing="0" class="dashboard" style="width:100%;" border="0">
    <tr><td nowrap>&nbsp;</td>                                                               <th nowrap style="text-align:right;">Week To Date</th>                                                                                           <th nowrap style="text-align:right;">Month To Date</th>                                                                          <th nowrap style="text-align:right;">Cumulative</th></tr>
    <tr><th nowrap><label id="tip1">Number of Claim Notifications Submitted</label></th>	<td nowrap><label><s:property value="w2DData.noOfClaimNotificationsSubmitted"/></label></td>	<td nowrap><label><s:property value="m2DData.noOfClaimNotificationsSubmitted"/></label></td>	<td nowrap><label><s:property value="cData.noOfClaimNotificationsSubmitted"/></label></td></tr>
    <tr><th nowrap><label id="tip2">Number of Claim Notifications Accepted</label></th>	<td nowrap><label><s:property value="w2DData.noOfClaimNotificationsAccepted"/></label></td>	<td nowrap><label><s:property value="m2DData.noOfClaimNotificationsAccepted"/></label></td>	<td nowrap><label><s:property value="cData.noOfClaimNotificationsAccepted"/></label></td></tr>
    <tr><th nowrap><label id="tip3">Number of Claim Notifications Rejected</label></th>	<td nowrap><label><s:property value="w2DData.noOfClaimNotificationsRejected"/></label></td>	<td nowrap><label><s:property value="m2DData.noOfClaimNotificationsRejected"/></label></td>	<td nowrap><label><s:property value="cData.noOfClaimNotificationsRejected"/></label></td></tr>
    <tr><th nowrap><label id="tip4">Number of Claim Notifications Pending</label></th>	<td nowrap><label><s:property value="w2DData.noOfClaimNotificationsPending"/></label></td>         <td nowrap><label><s:property value="m2DData.noOfClaimNotificationsPending"/></label></td>         <td nowrap><label><s:property value="cData.noOfClaimNotificationsPending"/></label></td></tr>
    <tr><th nowrap><label id="tip5">Number of Invoices Submitted</label></th>                 <td nowrap><label><s:property value="w2DData.noOfInvoicesSubmitted"/></label></td>                 <td nowrap><label><s:property value="m2DData.noOfInvoicesSubmitted"/></label></td>                 <td nowrap><label><s:property value="cData.noOfInvoicesSubmitted"/></label></td></tr>
    <tr><th nowrap><label id="tip6">Value of Invoices Submitted</label></th>                  <td nowrap><label>£<s:property value="w2DData.valueOfInvoicesSubmitted"/></label></td>              <td nowrap><label>£<s:property value="m2DData.valueOfInvoicesSubmitted"/></label></td>              <td nowrap><label>£<s:property value="cData.valueOfInvoicesSubmitted"/></label></td></tr>
    <tr><th nowrap><label id="tip7">Number of Invoices Accepted</label></th>                  <td nowrap><label><s:property value="w2DData.noOfInvoicesAccepted"/></label></td>                  <td nowrap><label><s:property value="m2DData.noOfInvoicesAccepted"/></label></td>                  <td nowrap><label><s:property value="cData.noOfInvoicesAccepted"/></label></td></tr>
    <tr><th nowrap><label id="tip8">Value of Invoices Accepted </label></th>                   <td nowrap><label>£<s:property value="w2DData.valueOfInvoicesAccepted"/></label></td>               <td nowrap><label>£<s:property value="m2DData.valueOfInvoicesAccepted"/></label></td>               <td nowrap><label>£<s:property value="cData.valueOfInvoicesAccepted"/></label></td></tr>
    <tr><th nowrap><label id="tip9">Number of Invoices Rejected</label></th>                  <td nowrap><label><s:property value="w2DData.noOfInvoicesRejected"/></label></td>                  <td nowrap><label><s:property value="m2DData.noOfInvoicesRejected"/></label></td>                  <td nowrap><label><s:property value="cData.noOfInvoicesRejected"/></label></td></tr>
    <tr><th nowrap><label id="tip10">Value of Invoices Rejected</label></th>                   <td nowrap><label>£<s:property value="w2DData.valueOfInvoicesRejected"/></label></td>               <td nowrap><label>£<s:property value="m2DData.valueOfInvoicesRejected"/></label></td>               <td nowrap><label>£<s:property value="cData.valueOfInvoicesRejected"/></label></td></tr>
    <tr><th nowrap><label id="tip11">Number of Invoices Pending</label></th>                   <td nowrap><label><s:property value="w2DData.noOfInvoicesPending"/></label></td>                   <td nowrap><label><s:property value="m2DData.noOfInvoicesPending"/></label></td>                   <td nowrap><label><s:property value="cData.noOfInvoicesPending"/></label></td></tr>
    <tr><th nowrap><label id="tip12">Value of Invoices Pending </label></th>                    <td nowrap><label>£<s:property value="w2DData.valueOfInvoicesPending"/></label></td>                <td nowrap><label>£<s:property value="m2DData.valueOfInvoicesPending"/></label></td>                <td nowrap><label>£<s:property value="cData.valueOfInvoicesPending"/></label></td></tr>
    <tr><th nowrap><label id="tip13">Number of Invoices Closed</label></th>                     <td nowrap><label><s:property value="w2DData.noOfInvoicesClosed"/></label></td>                    <td nowrap><label><s:property value="m2DData.noOfInvoicesClosed"/></label></td>                    <td nowrap><label><s:property value="cData.noOfInvoicesClosed"/></label></td></tr>
    <tr><th nowrap><label id="tip14">Value of Invoices Closed</label></th>                      <td nowrap><label>£<s:property value="w2DData.valueOfInvoicesClosed"/></label></td>                 <td nowrap><label>£<s:property value="m2DData.valueOfInvoicesClosed"/></label></td>                 <td nowrap><label>£<s:property value="cData.valueOfInvoicesClosed"/></label></td></tr>
    <tr><th nowrap><label id="tip15">Number of Invoices Payment Logged</label></th>                     <td nowrap><label><s:property value="w2DData.noOfInvoicesPaymentLogged"/></label></td>                    <td nowrap><label><s:property value="m2DData.noOfInvoicesPaymentLogged"/></label></td>                    <td nowrap><label><s:property value="cData.noOfInvoicesPaymentLogged"/></label></td></tr>
    <tr><th nowrap><label id="tip16">Value of Invoices Payment Logged</label></th>                      <td nowrap><label>£<s:property value="w2DData.valueOfInvoicesPaymentLogged"/></label></td>                 <td nowrap><label>£<s:property value="m2DData.valueOfInvoicesPaymentLogged"/></label></td>                 <td nowrap><label>£<s:property value="cData.valueOfInvoicesPaymentLogged"/></label></td></tr>
</table>
