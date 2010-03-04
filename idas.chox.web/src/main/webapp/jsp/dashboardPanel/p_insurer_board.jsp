<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
        Ext.onReady(function(){
        
            new Ext.ToolTip({target: 'tip1',html: 'New claims uploaded by selected Credit Hire Organisation(s)'});
            new Ext.ToolTip({target: 'tip2',html: 'Claims accepted on CHOX by Insurer'});
            new Ext.ToolTip({target: 'tip3',html: 'Claims rejected on CHOX by Insurer'});
            new Ext.ToolTip({target: 'tip4',html: 'Insurer not liable for hire bill e.g. vehicle not actually provided'});
            new Ext.ToolTip({target: 'tip5',html: 'New invoices uploaded by selected Credit Hire Organisation'});
            new Ext.ToolTip({target: 'tip6',html: 'Monetary value of invoices uploaded by selected Credit Hire Organisation(s)'});
            new Ext.ToolTip({target: 'tip7',html: 'Number of invoices approved by Insurer following the result of the business rules'});
            new Ext.ToolTip({target: 'tip8',html: 'Value of invoices approved by Insurer following the result of the business rules'});
            new Ext.ToolTip({target: 'tip9',html: 'Number of invoices rejected by Insurer following the result of the business rules'});
            new Ext.ToolTip({target: 'tip10',html: 'Value of invoices rejected by Insurer following the result of the business rules'});
            new Ext.ToolTip({target: 'tip11',html: 'Number of invoices under review by Insurer '});
            new Ext.ToolTip({target: 'tip12',html: 'Value of invoices under review by Insurer '});
            new Ext.ToolTip({target: 'tip13',html: 'Number of invoices withdrawn by Credit Hire Organisation(s)'});
            new Ext.ToolTip({target: 'tip14',html: 'Value of invoices withdrawn by Credit Hire Organisation(s)'});
            new Ext.ToolTip({target: 'tip15',html: 'Number of claims where invoice payments have been made by Insurer'});
            new Ext.ToolTip({target: 'tip16',html: 'Value of claims where invoice payments have been made by Insurer'});
            new Ext.ToolTip({target: 'tip17',html: 'Number of claims where payment has been received by Credit Hire Organisation(s)'});
            new Ext.ToolTip({target: 'tip18',html: 'Value of claims where payment has been received by Credit Hire Organisation(s)'});
            new Ext.ToolTip({target: 'tip19',html: 'Value of penalty charges applied by Credit Hire Organisation(s)'});
            new Ext.ToolTip({target: 'tip99',html: 'Claims that have neither been accepted or rejected by Insurer'});
        
            Ext.QuickTips.init();
        });

        $(document).ready(function(){
        
            var thisList = $(".dbValue");

            for (i=0; i<thisList.size(); i=i+1)
            {
                var bValue = $(".dbValue:eq("+i+")").html();

                if(bValue.indexOf(".")>0){
                    $(".dbValue:eq("+i+")").html("£ "+formatNumber(bValue,2,',',".",'','','-',''));
                }else{
                    $(".dbValue:eq("+i+")").html(formatNumber(bValue,'',',','','','','-',''));
                }
            }

        });

        function formatNumber(num,dec,thou,pnt,curr1,curr2,n1,n2) {var x = Math.round(num * Math.pow(10,dec));if (x >= 0) n1=n2='';var y = (''+Math.abs(x)).split('');var z = y.length - dec; if (z<0) z--; for(var i = z; i < 0; i++) y.unshift('0'); if (z<0) z = 1; y.splice(z, 0, pnt); if(y[0] == pnt) y.unshift('0'); while (z > 3) {z-=3; y.splice(z,0,thou);}var r = curr1+n1+y.join('')+n2+curr2;return r;}
    </script>


<div>
    <table cellpadding="0" cellspacing="0" style="width:100%;" border="0" class="dashboard">
        <tr>
            <th nowrap><label id="tip99">Number of Claims Pending</label></th>
            <td nowrap></td>
            <td nowrap></td>
            <td nowrap><label class="dbValue"><s:property value="cData.noOfClaimNotificationsPending"/></label></td>
        </tr>
    </table>
</div>

<table cellpadding="0" cellspacing="0" class="dashboard" style="width:100%;" border="0">
    <tr><td nowrap>&nbsp;</td>                                                              <th nowrap style="text-align:right;">Week To Date</th>                                                                      <th nowrap style="text-align:right;">Month To Date</th>                                                                 <th nowrap style="text-align:right;">Cumulative</th></tr>
    <tr><th nowrap><label id="tip1">Number of Claim Notifications Submitted</label></th>    <td nowrap><label class="dbValue"><s:property value="w2DData.noOfClaimNotificationsSubmitted"/></label></td>                <td nowrap><label class="dbValue"><s:property value="m2DData.noOfClaimNotificationsSubmitted"/></label></td>            <td nowrap><label class="dbValue"><s:property value="cData.noOfClaimNotificationsSubmitted"/></label></td></tr>
    <tr><th nowrap><label id="tip2">Total Claim Notifications Accepted</label></th>         <td nowrap><label class="dbValue"><s:property value="w2DData.noOfClaimNotificationsAcceptedAccumulative"/></label></td>	<td nowrap><label class="dbValue"><s:property value="m2DData.noOfClaimNotificationsAcceptedAccumulative"/></label></td>	<td nowrap><label class="dbValue"><s:property value="cData.noOfClaimNotificationsAcceptedAccumulative"/></label></td></tr>
    <tr><th nowrap><label id="tip3">Total Claim Notifications Rejected</label></th>         <td nowrap><label class="dbValue"><s:property value="w2DData.noOfClaimNotificationsRejectedAccumulative"/></label></td>	<td nowrap><label class="dbValue"><s:property value="m2DData.noOfClaimNotificationsRejectedAccumulative"/></label></td>	<td nowrap><label class="dbValue"><s:property value="cData.noOfClaimNotificationsRejectedAccumulative"/></label></td></tr>    
    <tr><th nowrap><label id="tip4">Number of Claims Pending</label></th>                    <td nowrap><label class="dbValue"><s:property value="w2DData.noOfClaimNotificationsPending"/></label></td>                   <td nowrap><label class="dbValue"><s:property value="m2DData.noOfClaimNotificationsPending"/></label></td>               <td nowrap><label class="dbValue"><s:property value="cData.noOfClaimNotificationsPending"/></label></td></tr>
    <tr><th nowrap><label id="tip4">Number of Claims Closed</label></th>                    <td nowrap><label class="dbValue"><s:property value="w2DData.noOfClaimNotificationsClosed"/></label></td>                   <td nowrap><label class="dbValue"><s:property value="m2DData.noOfClaimNotificationsClosed"/></label></td>               <td nowrap><label class="dbValue"><s:property value="cData.noOfClaimNotificationsClosed"/></label></td></tr>
    <tr><th nowrap><label id="tip5">Number of Invoices Submitted</label></th>               <td nowrap><label class="dbValue"><s:property value="w2DData.noOfInvoicesSubmitted"/></label></td>                          <td nowrap><label class="dbValue"><s:property value="m2DData.noOfInvoicesSubmitted"/></label></td>                      <td nowrap><label class="dbValue"><s:property value="cData.noOfInvoicesSubmitted"/></label></td></tr>
    <tr><th nowrap><label id="tip6">Value of Invoices Submitted</label></th>                <td nowrap><label class="dbValue"><s:property value="w2DData.valueOfInvoicesSubmitted"/></label></td>                       <td nowrap><label class="dbValue"><s:property value="m2DData.valueOfInvoicesSubmitted"/></label></td>                   <td nowrap><label class="dbValue"><s:property value="cData.valueOfInvoicesSubmitted"/></label></td></tr>
    <tr><th nowrap><label id="tip7">Number of Invoices Accepted</label></th>                <td nowrap><label class="dbValue"><s:property value="w2DData.noOfInvoicesAccepted"/></label></td>                           <td nowrap><label class="dbValue"><s:property value="m2DData.noOfInvoicesAccepted"/></label></td>                       <td nowrap><label class="dbValue"><s:property value="cData.noOfInvoicesAccepted"/></label></td></tr>
    <tr><th nowrap><label id="tip8">Value of Invoices Accepted </label></th>                <td nowrap><label class="dbValue"><s:property value="w2DData.valueOfInvoicesAccepted"/></label></td>                        <td nowrap><label class="dbValue"><s:property value="m2DData.valueOfInvoicesAccepted"/></label></td>                    <td nowrap><label class="dbValue"><s:property value="cData.valueOfInvoicesAccepted"/></label></td></tr>
    <tr><th nowrap><label id="tip9">Number of Invoices Rejected</label></th>                <td nowrap><label class="dbValue"><s:property value="w2DData.noOfInvoicesRejected"/></label></td>                           <td nowrap><label class="dbValue"><s:property value="m2DData.noOfInvoicesRejected"/></label></td>                       <td nowrap><label class="dbValue"><s:property value="cData.noOfInvoicesRejected"/></label></td></tr>
    <tr><th nowrap><label id="tip10">Value of Invoices Rejected</label></th>                <td nowrap><label class="dbValue"><s:property value="w2DData.valueOfInvoicesRejected"/></label></td>                        <td nowrap><label class="dbValue"><s:property value="m2DData.valueOfInvoicesRejected"/></label></td>                    <td nowrap><label class="dbValue"><s:property value="cData.valueOfInvoicesRejected"/></label></td></tr>
    <tr><th nowrap><label id="tip11">Number of Invoices Pending</label></th>                <td nowrap><label class="dbValue"><s:property value="w2DData.noOfInvoicesPending"/></label></td>                            <td nowrap><label class="dbValue"><s:property value="m2DData.noOfInvoicesPending"/></label></td>                        <td nowrap><label class="dbValue"><s:property value="cData.noOfInvoicesPending"/></label></td></tr>
    <tr><th nowrap><label id="tip12">Value of Invoices Pending </label></th>                <td nowrap><label class="dbValue"><s:property value="w2DData.valueOfInvoicesPending"/></label></td>                         <td nowrap><label class="dbValue"><s:property value="m2DData.valueOfInvoicesPending"/></label></td>                     <td nowrap><label class="dbValue"><s:property value="cData.valueOfInvoicesPending"/></label></td></tr>
    <tr><th nowrap><label id="tip13">Number of Invoices Closed</label></th>                 <td nowrap><label class="dbValue"><s:property value="w2DData.noOfInvoicesClosed"/></label></td>                             <td nowrap><label class="dbValue"><s:property value="m2DData.noOfInvoicesClosed"/></label></td>                         <td nowrap><label class="dbValue"><s:property value="cData.noOfInvoicesClosed"/></label></td></tr>
    <tr><th nowrap><label id="tip14">Value of Invoices Closed</label></th>                  <td nowrap><label class="dbValue"><s:property value="w2DData.valueOfInvoicesClosed"/></label></td>                          <td nowrap><label class="dbValue"><s:property value="m2DData.valueOfInvoicesClosed"/></label></td>                      <td nowrap><label class="dbValue"><s:property value="cData.valueOfInvoicesClosed"/></label></td></tr>
    <tr><th nowrap><label id="tip15">Number of Invoices Payment Logged</label></th>         <td nowrap><label class="dbValue"><s:property value="w2DData.noOfInvoicesPaymentLogged"/></label></td>                      <td nowrap><label class="dbValue"><s:property value="m2DData.noOfInvoicesPaymentLogged"/></label></td>                  <td nowrap><label class="dbValue"><s:property value="cData.noOfInvoicesPaymentLogged"/></label></td></tr>
    <tr><th nowrap><label id="tip16">Value of Invoices Payment Logged</label></th>          <td nowrap><label class="dbValue"><s:property value="w2DData.valueOfInvoicesPaymentLogged"/></label></td>                   <td nowrap><label class="dbValue"><s:property value="m2DData.valueOfInvoicesPaymentLogged"/></label></td>               <td nowrap><label class="dbValue"><s:property value="cData.valueOfInvoicesPaymentLogged"/></label></td></tr>
    <tr><th nowrap><label id="tip17">Number of Payments Received by CHO</label></th>       <td nowrap><label class="dbValue"><s:property value="w2DData.noOfInvoicesPaymentReceived"/></label></td>                    <td nowrap><label class="dbValue"><s:property value="m2DData.noOfInvoicesPaymentReceived"/></label></td>                <td nowrap><label class="dbValue"><s:property value="cData.noOfInvoicesPaymentReceived"/></label></td></tr>
    <tr><th nowrap><label id="tip18">Value of Payments Received by CHO</label></th>        <td nowrap><label class="dbValue"><s:property value="w2DData.valueOfInvoicesPaymentReceived"/></label></td>                 <td nowrap><label class="dbValue"><s:property value="m2DData.valueOfInvoicesPaymentReceived"/></label></td>             <td nowrap><label class="dbValue"><s:property value="cData.valueOfInvoicesPaymentReceived"/></label></td></tr>
    <tr><th nowrap><label id="tip19">Total Value of Penalty Charges Applied</label></th>    <td nowrap><label class="dbValue"><s:property value="w2DData.totalValueOfPenaltyChargesApplied"/></label></td>              <td nowrap><label class="dbValue"><s:property value="m2DData.totalValueOfPenaltyChargesApplied"/></label></td>          <td nowrap><label class="dbValue"><s:property value="cData.totalValueOfPenaltyChargesApplied"/></label></td></tr>    
</table>