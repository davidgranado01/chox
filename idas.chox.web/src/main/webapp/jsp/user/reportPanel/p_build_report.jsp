<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    function renderParameterPanel(report)
    {
        var sLocaltion = "#param_panel";
        var sAction = "loadParameterPanel.action";
        var sparameters = "reportName=" + report;
        doSectionLoad(sLocaltion, sAction, sparameters);
    }

</script>

<div class="x-panel-bwrap chox-form-container">
    <table cellspacing="4" class="chox-report">
        <tr valign="top">
            <td class="chox-report-left-col">
                <fieldset class="x-fieldset">
                    <legend>Report List</legend>
                    <div class="x-panel-bwrap chox-form-container">
                        <div class="instruction-message">
                            Please select from the list of available reports
                        </div>
                        <div class="x-panel-bwrap chox-form-container">
                            <ul class='report-header-list'>
                                <s:if test="reportAccessibility.insurerWeeklySummaryAccessibility">
                                <li class='report-type-header'>General Reports</li>
                                <li><a href="javascript:renderParameterPanel('InsurerAdminWeeklyOverviewReport-Excel');">Admin Weekly Overview Report</a></li>
                                </s:if>
                                
                                <s:if test="reportAccessibility.overviewSummaryAccessibility || reportAccessibility.claimRejectionAccessibility">
                                <li class='report-type-header'>Claim Reports</li>
                                </s:if>

                                <s:if test="reportAccessibility.overviewSummaryAccessibility">
                                <li><a href="javascript:renderParameterPanel('OverviewSummary-Excel');">Claim Overview Summary Report</a></li>
                                </s:if>
                                
                                <s:if test="reportAccessibility.claimRejectionAccessibility">                                
                                <li><a href="javascript:renderParameterPanel('ClaimRejectedReport-Excel');">Claim Rejection Report</a></li>
                                </s:if>
                                
                                <li class='report-type-header'>Invoice Reports</li>
                                
                                <s:if test="reportAccessibility.invoiceReportAccessibility">
                                <li><a href="javascript:renderParameterPanel('InvoiceReport-Excel');">CHO Invoice Report</a></li>
                                </s:if>

                                <s:if test="reportAccessibility.invoiceSummaryAccessibility">
                                <li><a href="javascript:renderParameterPanel('InvoiceSummaryReport-Excel');">Invoice Summary Report</a></li>
                                </s:if>

                                <s:if test="reportAccessibility.insurerPaymentReportAccessibility">
                                <li><a href="javascript:renderParameterPanel('PaymentReport-Excel');">CHO Payment Bordereau</a></li>
                                </s:if>

                                <s:if test="reportAccessibility.insurerAverageClaimSettlementReportAccessibility">
                                <li><a href="javascript:renderParameterPanel('AverageSettlementAmount-Excel');">Average Claim Settlement Amount Report</a></li>
                                </s:if>

                                <s:if test="reportAccessibility.invoiceSavingSummaryReportAccessibility">
                                <li><a href="javascript:renderParameterPanel('InvoiceSavingSummaryReport-Excel');">CHO Invoice Savings Summary</a></li>
                                </s:if>
                                
                            </ul>
                        </div>
                    </div>  
                </fieldset>

            </td>
            <td class="chox-report-right-col" id="param_panel"></td>
        </tr>
    </table>
</div>

