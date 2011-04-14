<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'InsurerSetupWorkflowReport-Excel';

    Ext.onReady(function(){
        
        ui.dateField('startDate',getTodayDate(),'dateFromDiv');
        ui.dateField('endDate',getTodayDate(),'dateToDiv');

        $("form#formReportParam").validate(
        {
            errorLabelContainer: "#formReportParamMessageBox",
            rules: {
                startDate:{
                    required:true,
                    date: true
                },
                endDate:{
                    required:true,
                    date: true
                }
            },
            messages: {
                startDate: {
                    required:"A value must be supplied for 'Date From'",
                    date:"You must supply a date value 'Date From'"
                },
                endDate: {
                    required:"A value must be supplied for 'Date To'",
                    date:"You must supply a date value 'Date To'"
                }
            }
        });

    });
    
    function openReport()
    {
        if($("form#formReportParam").valid()){
            var queryString = $('#formReportParam').formSerialize();
            window.location = "<%=request.getContextPath()%>/prv/p/exportExcelReport.action?reportName=" + reportName + "&" + queryString;
        }
    }

</script>

<fieldset class="x-fieldset">

    <legend>Claim Status Workflow Report</legend>

    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">

        <div class="x-panel-bwrap chox-form-container">

            <div class="form-container">

                <div class="instruction-message">
                    This report provides an insight into the outstanding work at all the various statuses that are the responsibility of the Insurer.  The report also displays the number of processed tasks during the selected period.  The dates below determine the number of tasks processed during the selected period and all the outstanding task columns are based on outstanding work as at the date selected in the 'Period To' field, the 'Period From' selection does not have an influence on the outstanding task figures.
                </div>

                <table class="report-form">
                    <tr>
                        <td nowrap width="30%"><label>Period From</label></td><td><div id="dateFromDiv" /></td>
                    </tr>
                    <tr>
                        <td nowrap><label>Period To</label></td><td><div id="dateToDiv"/></td>
                    </tr>
                </table>

                <div class="chox-report-button">
                    <button type="button" id="ISWRPPGenerateReportId" onclick="javascript:openReport();">Generate Report</button>
                </div>
            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div>
    </form>
</fieldset>