<%-- 
    Document   : p_report_main
    Created on : 26-Jan-2009, 17:12:00
    Author     : Emmanuel
--%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>        
    
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.validate.min.js"></script> 
    <script src="<%= request.getContextPath()%>/scripts/general.js" type="text/javascript"></script> 
    
    <script>
        
        function renderParameterPanel(report)
        {
            $("#param_panel").load("loadParameterPanel.action?reportName=" + report);
        }
        
    </script>
</head>

<div class="x-panel-bwrap chox-form-container">
    
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr valign="top">
            <td width="50%" class="chox-form-left-col">
                <fieldset class="x-fieldset">
                    <legend>Report List</legend>                    
                    <div class="x-panel-bwrap chox-form-container">
                        <div class="ReportActionMsg">
                            Please select from the list of available reports
                        </div>
                        <div style="height:400px;" class="x-panel-bwrap chox-form-container">
                            <ul>
                            <li class='reportTypeHeader'>General Reports</li>
                                <s:if test="reportAccessibility.insurerWeeklySummaryAccessibility">
                                <li><a href="javascript:renderParameterPanel('InsurerAdminWeeklyOverviewReport-Excel');">Admin Weekly Overview Report</a></li>
                                </s:if> 
                            <li class='reportTypeHeader'>Claim Reports</li>
                                <s:if test="reportAccessibility.overviewSummaryAccessibility">                                
                                <li><a href="javascript:renderParameterPanel('OverviewSummary-Excel');">Claim Overview Summary Report</a></li>
                                </s:if>
                                <s:if test="reportAccessibility.claimRejectionAccessibility">                                
                                <li><a href="javascript:renderParameterPanel('ClaimRejectedReport-Excel');">Claim Rejection Report</a></li>
                                </s:if>
                            <li class='reportTypeHeader'>Invoice Reports</li>
                                <s:if test="reportAccessibility.invoiceSummaryAccessibility">
                                <li><a href="javascript:renderParameterPanel('InvoiceSummaryReport-Excel');">Invoice Summary Report</a></li>
                                </s:if>
                                <s:if test="reportAccessibility.insurerPaymentReportAccessibility">                                
                                <li><a href="javascript:renderParameterPanel('PaymentReport-Excel');">CHO Payment Bordereau</a></li>
                                </s:if>
                                <s:if test="reportAccessibility.insurerAverageClaimSettlementReportAccessibility">                                
                                <li><a href="javascript:renderParameterPanel('AverageSettlementAmount-Excel');">Average Claim Settlement Amount Report</a></li>
                                </s:if>
                            </ul>
                        </div>
                    </div>  
                </fieldset>
            </td>
            <td width="50%" class="chox-form-right-col">
                <div id="param_panel"></div>
            </td>
        </tr>
    </table>
    
    
</div>

