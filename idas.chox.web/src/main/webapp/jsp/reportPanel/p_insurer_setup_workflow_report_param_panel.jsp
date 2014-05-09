<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'ClaimStatusWorkflowReport-Excel';

    Ext.onReady(function(){
        
        ui.dateField('startDate',getTodayDate(),'dateFromDiv');
        ui.dateField('endDate',getTodayDate(),'dateToDiv');
        
        <s:if test="InsurerIsWorkgroupEnabled" >
                
            var workgroupBreakdownCheckbox = new Ext.form.Checkbox({
                    name:'workgroupBreakdownCheckbox',
                    id:'workgroupBreakdownCheckboxId',
                    renderTo:'workgroupBreakdownCheckboxDiv',
                    checked: false
             });
             
        </s:if>
            
        $("form#formReportParam").validate(
        {
            errorLabelContainer: "#formReportParamMessageBox",
            rules: {
                startDate:{
                	max:function(){
                		var sd = Ext.get('startDate').getValue().split("/");
                        var ed = Ext.get('endDate').getValue().split("/");
                        var time = new Date(sd[2],sd[1] - 1 ,sd[0]).getTime() - new Date(ed[2],ed[1] - 1 ,ed[0]).getTime();
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
                	max:"'Period to' can't be before 'Period From'",
                    required:"A value must be supplied for 'Period From'",
                    dateITA:"You must supply a date value 'Period From'"
                },
                endDate: {
                    required:"A value must be supplied for 'Period To'",
                    dateITA:"You must supply a date value 'Period To'"
                }
            }
        });

    });
    
    function openReport()
    {
        if($("form#formReportParam").valid()){
//            var queryString = $('#formReportParam').formSerialize();
            var queryString = {};
            $.each($('#formReportParam').serializeArray(), function() {queryString[this.name] = this.value;});
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
                    This report provides an insight into the outstanding work at all the various statuses that are the responsibility of the Insurer.  The report also displays the number of processed actions during the selected period.  The dates below determine the number of actions processed during the selected period and all the outstanding action columns are based on outstanding work as at the date selected in the 'Period To' field, the 'Period From' selection does not have an influence on the outstanding action figures.  You can include Workgroups on this report by ticking 'Include Workgroup Breakdown' below. 
                </div>

                <table class="report-form">
                    <tr>
                        <td nowrap width="30%"><label>Period From</label> Claim Status Workflow Report<span class="mandatory">*</span></td><td><div id="dateFromDiv" /></td>
                    </tr>
                    <tr>
                        <td nowrap><label>Period To</label> Claim Status Workflow Report<span class="mandatory">*</span></td><td><div id="dateToDiv"/></td>
                    </tr>
                    <s:if test="InsurerIsWorkgroupEnabled" >
                        <tr>
                            <td nowrap>Include Workgroup Breakdown</td><td><div id="workgroupBreakdownCheckboxDiv"/></td>
                        </tr>
                    </s:if>
                </table>

                <div class="chox-report-button">
                    <button type="button" id="ISWRPPGenerateReportId" onclick="javascript:openReport();">Generate Report</button>
                </div>
            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div>
    </form>
</fieldset>