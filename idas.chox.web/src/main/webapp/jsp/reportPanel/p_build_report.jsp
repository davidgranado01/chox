<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    function renderParameterPanel(report)
    {
        var target = "#param_panel";
        var url = "/prv/p/loadParameterPanel.action";
        var param = {"reportName":report};
        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
        });
    }

</script>
<div id="reportPanel">
    <div class="x-panel-bwrap chox-form-container">
        <table cellspacing="4" class="chox-report">
            <tr valign="top">
                <td class="chox-report-left-col">
                    <fieldset class="x-fieldset">
                        <legend>Report List</legend>
                       <!-- <div class="x-panel-bwrap chox-form-container">-->
                           <!-- <div class="instruction-message"> -->
                                Please select from the list of available reports
                           <!-- </div> -->
                            <div class="x-panel-bwrap chox-form-container">
                                <ul class='report-header-list'>
                                    <s:if test="reportAccessibility.insurerWeeklySummaryAccessibility">
                                        <li class='report-type-header'>General Reports</li>
                                        <li><a href="javascript:renderParameterPanel('AdminWeeklyOverviewReport-Excel');">Admin Weekly Overview Report</a></li>
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

                                     <s:if test="reportAccessibility.invoiceStatusReportAccessibility">
                                         <li><a href="javascript:renderParameterPanel('InvoiceStatusReport-Excel');">Invoice Status Report</a></li>
                                     </s:if>

                                    <s:if test="reportAccessibility.insurerAverageClaimSettlementReportAccessibility">
                                        <li><a href="javascript:renderParameterPanel('AverageSettlementAmount-Excel');">Average Claim Settlement Amount Report</a></li>
                                    </s:if>

                                    <s:if test="reportAccessibility.invoiceSavingSummaryReportAccessibility">
                                        <li><a href="javascript:renderParameterPanel('InvoiceSavingSummaryReport-Excel');">CHO Invoice Savings Summary</a></li>
                                    </s:if>

                                    <s:if test="reportAccessibility.breInvoiceApprovalDisputeReportAccessibility">
                                        <li><a href="javascript:renderParameterPanel('BreInvoiceApprovalDisputeReport-Excel');">BRE Invoice Approval Dispute Report</a></li>
                                    </s:if>
                                     <s:if test="reportAccessibility.teamSiteBreReportAccessibility && insurerIsWorkgroupEnabled">
                                        <li><a href="javascript:renderParameterPanel('TeamSiteBreInvoiceReport-Excel');">Site and Team BRE Invoice Approval Dispute Report</a></li>
                                    </s:if>
                                    <s:if test="reportAccessibility.workgroupOwnerBreReportAccessibility && insurerIsClaimOwnershipEnabled">
                                                <s:if test="insurerIsWorkgroupEnabled">
                                                    <li><a href="javascript:renderParameterPanel('WorkgroupOwnerBreInvoiceReport-Excel');">Workgroup and Owner BRE Invoice Approval Dispute Report</a></li>
                                                </s:if>
                                                <s:else>
                                                    <li><a href="javascript:renderParameterPanel('WorkgroupOwnerBreInvoiceReport-Excel');">Claim Owner BRE Invoice Approval Dispute Report</a></li>
                                                </s:else>
                                    </s:if>
                                    <s:if test="reportAccessibility.ownerWorkflowReportAccessibility || reportAccessibility.teamWorkflowReportAccessibility || reportAccessibility.claimStatusWorkflowReportAccessibility">
                                        <li class='report-type-header'>Workflow Reports</li>
                                        <s:if test="reportAccessibility.ownerWorkflowReportAccessibility">
                                            <s:if test="insurerIsWorkgroupEnabled">
                                                <li><a href="javascript:renderParameterPanel('OwnerWorkflowReport-Excel');">Workgroup and Claim Owner Workflow Report</a></li>
                                            </s:if>
                                            <s:else>
                                                <li><a href="javascript:renderParameterPanel('OwnerWorkflowReport-Excel');">Claim Owner Workflow Report</a></li>
                                            </s:else>
                                        </s:if>
                                        <s:if test="reportAccessibility.teamWorkflowReportAccessibility">
                                            <li><a href="javascript:renderParameterPanel('TeamWorkflowReport-Excel');">Site and Team Workflow Report</a></li>
                                        </s:if>
                                        <s:if test="reportAccessibility.claimStatusWorkflowReportAccessibility">
                                            <li><a href="javascript:renderParameterPanel('ClaimStatusWorkflowReport-Excel');">Claim Status Workflow Report</a></li>
                                        </s:if>
                                         <s:if test="reportAccessibility.newIncomingHandlerActionsAccessibility && (insurerIsClaimOwnershipEnabled || insurerIsWorkgroupEnabled)">
                                            <li><a href="javascript:renderParameterPanel('NewIncomingHandlerActionsReport-Excel');">New Incoming Actions Report</a></li>
                                        </s:if>
                                    </s:if>
                                    <s:if test="reportAccessibility.teamPerformanceReportAccessibility || reportAccessibility.ownerPerformanceReportAccessibility ">
                                        <li class='report-type-header'>Performance Reports</li>
                                        <s:if test="reportAccessibility.teamPerformanceReportAccessibility">
                                            <li><a href="javascript:renderParameterPanel('TeamPerformanceReport-Excel');">Site and Team Performance Report</a></li>
                                        </s:if>
                                        <s:if test="reportAccessibility.ownerPerformanceReportAccessibility">
                                            <s:if test="insurerIsWorkgroupEnabled">
                                                <li><a href="javascript:renderParameterPanel('OwnerPerformanceReport-Excel');">Workgroup and Claim Owner Performance Report</a></li>
                                            </s:if>
                                            <s:else>
                                                <li><a href="javascript:renderParameterPanel('OwnerPerformanceReport-Excel');">Claim Owner Performance Report</a></li>
                                            </s:else>
                                        </s:if>
                                        <s:if test="reportAccessibility.handlerPerformanceReportAccessibility && insurerIsClaimOwnershipEnabled">
                                            <li><a href="javascript:renderParameterPanel('HandlerPerformanceReport-Excel');">Handler Performance Report</a></li>
                                        </s:if>
                                    </s:if>

                                </ul>
                            </div>
                       <!--  </div> -->
                    </fieldset>

                </td>
                <td class="chox-report-right-col" id="param_panel"></td>
            </tr>
        </table>
    </div>
</div>