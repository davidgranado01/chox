<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    Ext.onReady(function(){



            new Ext.ToolTip({target: 'tip1',html: 'New Claims uploaded against selected Insurer(s) during the period'});
            new Ext.ToolTip({target: 'tip2',html: 'Claims Accepted on CHOX by the Insurer(s) during the period'});
            new Ext.ToolTip({target: 'tip3',html: 'Claim Rejections accepted by the CHO during the period'});
            new Ext.ToolTip({target: 'tip4',html: 'Total number of claims waiting to be processed'});
            new Ext.ToolTip({target: 'tip5',html: 'Insurer not liable for hire bill e.g. vehicle not actually provided'});
            new Ext.ToolTip({target: 'tip6',html: 'Number of invoices uploaded against selected insurer(s) during the period in question'});
            new Ext.ToolTip({target: 'tip7',html: 'Full original value of invoices uploaded against selected Insurer(s) in the period'});
            new Ext.ToolTip({target: 'tip8',html: 'Number of invoices cleared for payment by the insurer during the period in question (using Total To Pay amount)'});
            new Ext.ToolTip({target: 'tip9',html: 'Value of invoices cleared for payment by the insurer during the period in question (using Total To Pay amount)'});
            new Ext.ToolTip({target: 'tip10',html: 'Number of invoices rejections accepted by the CHO during the period in question'});
            new Ext.ToolTip({target: 'tip11',html: 'Value of invoices rejections accepted by the CHO during the period in question (using Total To Pay amount)'});
            new Ext.ToolTip({target: 'tip12',html: 'Number of invoices awaiting action by either the Insurer(s) or CHO'});
            new Ext.ToolTip({target: 'tip13',html: 'Value of invoices awaiting action by either the Insurer(s) or CHO'});
            new Ext.ToolTip({target: 'tip14',html: 'Number of invoices awaiting liability resolution by insurer'});
            new Ext.ToolTip({target: 'tip15',html: 'Value of invoices awaiting liability resolution (using Total To Pay amount)'});
            new Ext.ToolTip({target: 'tip16',html: 'Number of invoices withdrawn for selected Insurer(s)'});
            new Ext.ToolTip({target: 'tip17',html: 'Value of invoices withdrawn for selected insurer(s) (using Total To Pay amount)'});
            new Ext.ToolTip({target: 'tip18',html: 'Number of claims where invoice payments have been made by Insurer(s)'});
            new Ext.ToolTip({target: 'tip19',html: 'Value of claims where invoice payments have been made by the insurer(s) (using Total To Pay amount) '});
            new Ext.ToolTip({target: 'tip20',html: 'Number of claims where payment has been received'});
            new Ext.ToolTip({target: 'tip21',html: 'Value of claims where payment has been received (using Total To Pay amount)'});
            new Ext.ToolTip({target: 'tip22',html: 'Average invoice payment time (In days)'});
            new Ext.ToolTip({target: 'tip23',html: 'Number of invoices awaiting litigation outcome by CHO'});
            new Ext.ToolTip({target: 'tip24',html: 'Value of invoices awaiting litigation outcome (using Total To Pay amount)'});
            Ext.QuickTips.init();
            
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

<table cellpadding="0" cellspacing="0" class="dashboard" style="width:100%;" border="0">
    <tr><td nowrap>&nbsp;</td>                                                                      <th nowrap style="text-align:right;">Week To Date</th>                                                                      <th nowrap style="text-align:right;">Month To Date</th>                                                                 <th nowrap style="text-align:right;">Cumulative</th></tr>
    <tr><th nowrap><label id="tip1">Number of Claim Notifications Submitted</label></th>            <td nowrap><label class="dbValue"><s:property value="w2DData.noOfClaimNotificationsSubmitted"/></label></td>                <td nowrap><label class="dbValue"><s:property value="m2DData.noOfClaimNotificationsSubmitted"/></label></td>            <td nowrap><label class="dbValue"><s:property value="cData.noOfClaimNotificationsSubmitted"/></label></td></tr>
    <tr><th nowrap><label id="tip2">Total Claim Notifications Accepted</label></th>                 <td nowrap><label class="dbValue"><s:property value="w2DData.noOfClaimNotificationsAccepted"/></label></td>                 <td nowrap><label class="dbValue"><s:property value="m2DData.noOfClaimNotificationsAccepted"/></label></td>             <td nowrap><label class="dbValue"><s:property value="cData.noOfClaimNotificationsAccepted"/></label></td></tr>
    <tr><th nowrap><label id="tip3">Total Claim Rejections Accepted</label></th>                    <td nowrap><label class="dbValue"><s:property value="w2DData.noOfClaimNotificationsRejectionsAccepted"/></label></td>	<td nowrap><label class="dbValue"><s:property value="m2DData.noOfClaimNotificationsRejectionsAccepted"/></label></td>	<td nowrap><label class="dbValue"><s:property value="cData.noOfClaimNotificationsRejectionsAccepted"/></label></td></tr>
    <tr><th nowrap><label id="tip4">Number of Claims Waiting to be Processed</label></th>          <td nowrap>-</td>                                                                                                            <td nowrap>-</td>                                                                                                       <td nowrap><label class="dbValue"><s:property value="cData.noOfClaimsAwaitingToBeProcessed"/></label></td></tr>
    <tr><th nowrap><label id="tip5">Number of Claims Closed</label></th>                            <td nowrap><label class="dbValue"><s:property value="w2DData.noOfClaimNotificationsClosed"/></label></td>                   <td nowrap><label class="dbValue"><s:property value="m2DData.noOfClaimNotificationsClosed"/></label></td>               <td nowrap><label class="dbValue"><s:property value="cData.noOfClaimNotificationsClosed"/></label></td></tr>
    <tr><th nowrap><label id="tip6">Number of Invoices Submitted</label></th>                       <td nowrap><label class="dbValue"><s:property value="w2DData.noOfInvoicesSubmitted"/></label></td>                          <td nowrap><label class="dbValue"><s:property value="m2DData.noOfInvoicesSubmitted"/></label></td>                      <td nowrap><label class="dbValue"><s:property value="cData.noOfInvoicesSubmitted"/></label></td></tr>
    <tr><th nowrap><label id="tip7">Value of Invoices Submitted</label></th>                        <td nowrap><label class="dbValue"><s:property value="w2DData.valueOfInvoicesSubmitted"/></label></td>                       <td nowrap><label class="dbValue"><s:property value="m2DData.valueOfInvoicesSubmitted"/></label></td>                   <td nowrap><label class="dbValue"><s:property value="cData.valueOfInvoicesSubmitted"/></label></td></tr>
    <tr><th nowrap><label id="tip8">Number of Invoices Accepted</label></th>                        <td nowrap><label class="dbValue"><s:property value="w2DData.noOfInvoicesAccepted"/></label></td>                           <td nowrap><label class="dbValue"><s:property value="m2DData.noOfInvoicesAccepted"/></label></td>                       <td nowrap><label class="dbValue"><s:property value="cData.noOfInvoicesAccepted"/></label></td></tr>
    <tr><th nowrap><label id="tip9">Value of Invoices Accepted </label></th>                        <td nowrap><label class="dbValue"><s:property value="w2DData.valueOfInvoicesAccepted"/></label></td>                        <td nowrap><label class="dbValue"><s:property value="m2DData.valueOfInvoicesAccepted"/></label></td>                    <td nowrap><label class="dbValue"><s:property value="cData.valueOfInvoicesAccepted"/></label></td></tr>
    <tr><th nowrap><label id="tip10">Number of Invoices Rejected</label></th>                        <td nowrap><label class="dbValue"><s:property value="w2DData.noOfInvoicesRejected"/></label></td>                           <td nowrap><label class="dbValue"><s:property value="m2DData.noOfInvoicesRejected"/></label></td>                       <td nowrap><label class="dbValue"><s:property value="cData.noOfInvoicesRejected"/></label></td></tr>
    <tr><th nowrap><label id="tip11">Value of Invoices Rejected</label></th>                        <td nowrap><label class="dbValue"><s:property value="w2DData.valueOfInvoicesRejected"/></label></td>                        <td nowrap><label class="dbValue"><s:property value="m2DData.valueOfInvoicesRejected"/></label></td>                    <td nowrap><label class="dbValue"><s:property value="cData.valueOfInvoicesRejected"/></label></td></tr>
    <tr><th nowrap><label id="tip12">Number of Invoices Pending</label></th>                        <td nowrap>-</td>                                                                                                           <td nowrap>-</td>                                                                                                       <td nowrap><label class="dbValue"><s:property value="cData.noOfInvoicesPending"/></label></td></tr>
    <tr><th nowrap><label id="tip13">Value of Invoices Pending </label></th>                        <td nowrap>-</td>                                                                                                           <td nowrap>-</td>                                                                                                       <td nowrap><label class="dbValue"><s:property value="cData.valueOfInvoicesPending"/></label></td></tr>
    <tr><th nowrap><label id="tip14">Number of Invoices Awaiting Liability Resolution </label></th> <td nowrap>-</td>                                                                                                           <td nowrap>-</td>                                                                                                       <td nowrap><label class="dbValue"><s:property value="cData.noOfInvoicesAwaitingLiabilityResolution"/></label></td></tr>
    <tr><th nowrap><label id="tip15">Value of Invoices Awaiting Liability Resolution </label></th>  <td nowrap>-</td>                                                                                                           <td nowrap>-</td>                                                                                                       <td nowrap><label class="dbValue"><s:property value="cData.valueOfInvoicesAwaitingLiabilityResolution"/></label></td></tr>
    <tr><th nowrap><label id="tip23">Number of Invoices Awaiting Litigation Outcome</label></th>                         <td nowrap><label class="dbValue"><s:property value="w2DData.noOfInvoicesAwaitingLitigationOutcome"/></label></td>                             <td nowrap><label class="dbValue"><s:property value="m2DData.noOfInvoicesAwaitingLitigationOutcome"/></label></td>                         <td nowrap><label class="dbValue"><s:property value="cData.noOfInvoicesAwaitingLitigationOutcome"/></label></td></tr>
    <tr><th nowrap><label id="tip24">Value of Invoices Awaiting Litigation Outcome</label></th>                          <td nowrap><label class="dbValue"><s:property value="w2DData.valueOfInvoicesAwaitingLitigationOutcome"/></label></td>                          <td nowrap><label class="dbValue"><s:property value="m2DData.valueOfInvoicesAwaitingLitigationOutcome"/></label></td>                      <td nowrap><label class="dbValue"><s:property value="cData.valueOfInvoicesAwaitingLitigationOutcome"/></label></td></tr>
    <tr><th nowrap><label id="tip16">Number of Invoices Closed</label></th>                         <td nowrap><label class="dbValue"><s:property value="w2DData.noOfInvoicesClosed"/></label></td>                             <td nowrap><label class="dbValue"><s:property value="m2DData.noOfInvoicesClosed"/></label></td>                         <td nowrap><label class="dbValue"><s:property value="cData.noOfInvoicesClosed"/></label></td></tr>
    <tr><th nowrap><label id="tip17">Value of Invoices Closed</label></th>                          <td nowrap><label class="dbValue"><s:property value="w2DData.valueOfInvoicesClosed"/></label></td>                          <td nowrap><label class="dbValue"><s:property value="m2DData.valueOfInvoicesClosed"/></label></td>                      <td nowrap><label class="dbValue"><s:property value="cData.valueOfInvoicesClosed"/></label></td></tr>
    <tr><th nowrap><label id="tip18">Number of Payments Logged</label></th>                 <td nowrap><label class="dbValue"><s:property value="w2DData.noOfInvoicesPaymentLogged"/></label></td>                      <td nowrap><label class="dbValue"><s:property value="m2DData.noOfInvoicesPaymentLogged"/></label></td>                  <td nowrap><label class="dbValue"><s:property value="cData.noOfInvoicesPaymentLogged"/></label></td></tr>
    <tr><th nowrap><label id="tip19">Value of Payments Logged</label></th>                  <td nowrap><label class="dbValue"><s:property value="w2DData.valueOfInvoicesPaymentLogged"/></label></td>                   <td nowrap><label class="dbValue"><s:property value="m2DData.valueOfInvoicesPaymentLogged"/></label></td>               <td nowrap><label class="dbValue"><s:property value="cData.valueOfInvoicesPaymentLogged"/></label></td></tr>
    <tr><th nowrap><label id="tip20">Number of Payments Received</label></th>                <td nowrap><label class="dbValue"><s:property value="w2DData.noOfInvoicesPaymentReceived"/></label></td>                    <td nowrap><label class="dbValue"><s:property value="m2DData.noOfInvoicesPaymentReceived"/></label></td>                <td nowrap><label class="dbValue"><s:property value="cData.noOfInvoicesPaymentReceived"/></label></td></tr>
    <tr><th nowrap><label id="tip21">Value of Payments Received</label></th>                 <td nowrap><label class="dbValue"><s:property value="w2DData.valueOfInvoicesPaymentReceived"/></label></td>                 <td nowrap><label class="dbValue"><s:property value="m2DData.valueOfInvoicesPaymentReceived"/></label></td>             <td nowrap><label class="dbValue"><s:property value="cData.valueOfInvoicesPaymentReceived"/></label></td></tr>
    <tr><th nowrap><label id="tip22">Average Invoice Payment Time (Days)</label></th>                 <td nowrap><label ><s:property value="w2DData.avgInvoicePaymentTime"/></label></td>                 <td nowrap><label><s:property value="m2DData.avgInvoicePaymentTime"/></label></td>             <td nowrap><label><s:property value="cData.avgInvoicePaymentTime"/></label></td></tr>
</table>
