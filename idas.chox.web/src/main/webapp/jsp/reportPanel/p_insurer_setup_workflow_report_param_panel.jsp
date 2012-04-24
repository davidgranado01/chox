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
                	max:function(){
                		var sd = Ext.get('startDate').getValue().split("/");
                        var ed = Ext.get('endDate').getValue().split("/");
                        var time = new Date(sd[0],sd[1],sd[2]).getTime() - new Date(ed[0],ed[1],ed[2]).getTime();
                        if(time > 0)
                            return true;
                    },
                    required:true,
                    dateITA: true
                },
                endDate:{
                    required:true,
                    dateITA: true
                }
            },
            messages: {
                startDate: {
                	max:"'Date to' can't be before 'Date From'",
                    required:"A value must be supplied for 'Date From'",
                    dateITA:"You must supply a date value 'Date From'"
                },
                endDate: {
                    required:"A value must be supplied for 'Date To'",
                    dateITA:"You must supply a date value 'Date To'"
                }
            }
        });

    });
    
    function openReport()
    {
        if($("form#formReportParam").valid()){
            var queryString = $('#formReportParam').formSerialize();
            generateReport(queryString);
        }
    }

</script>

<fieldset class="x-fieldset">

    <legend>Claim Status Workflow Report</legend>

    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">

        <div class="x-panel-bwrap chox-form-container">

            <div class="form-container">

                <div class="instruction-message">
                    This report provides an insight into the outstanding work at all the various statuses that are the responsibility of the Insurer.  The report also displays the number of processed actions during the selected period.  The dates below determine the number of actions processed during the selected period and all the outstanding action columns are based on outstanding work as at the date selected in the 'Period To' field, the 'Period From' selection does not have an influence on the outstanding action figures.
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