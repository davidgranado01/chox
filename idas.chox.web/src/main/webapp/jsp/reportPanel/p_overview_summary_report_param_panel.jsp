<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'OverviewSummary-Excel';
    
    Ext.onReady(function(){
        ui.dateField('DateStart',getTodayDate(),'dateFromDiv');
        ui.dateField('DateEnd',getTodayDate(),'dateToDiv');

        $("form#formReportParam").validate(
        {
            errorLabelContainer: "#formReportParamMessageBox",
            rules: {
                DateStart:{
                    required:true,
                    dateITA: true
                },
                DateEnd:{
                    required:true,
                    dateITA: true
                }
            },
            messages: {
                DateStart: {
                    required:"A value must be supplied for 'Claim Uploaded From'",
                    dateITA:"You must supply a date value 'Claim Uploaded From'"
                },
                DateEnd: {
                    required:"A value must be supplied for 'Claim Uploaded To'",
                    dateITA:"You must supply a date value 'Claim Uploaded To'"
                }
            }
        });

    });


    function openReport()
    {
        if($("form#formReportParam").valid()){
            var queryString = $('#formReportParam').formSerialize();
            window.location= "<%=request.getContextPath()%>/prv/p/exportExcelReport.action?reportName=" + reportName + "&" + queryString;
        }
    }

</script>

<fieldset class="x-fieldset">
    <legend>Claim Overview Summary Report</legend>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">
        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">
                    <s:if test="isCHO">
                        This report shows a high level summary of claims across all Insurers and per Insurer. Displaying information such as average invoice values, average hire durations and average cycle times.
                    </s:if>
                    <s:else>
                        This report shows a high level summary of claims across all CHOs and per CHO. Displaying information such as average invoice values, average hire durations and average cycle times.
                    </s:else>
                </div>

                <table class="report-form">
                    <tr>
                        <td nowrap width="30%"><label>Claim Uploaded Date From</label></td><td><div id="dateFromDiv" /></td>                       
                    </tr>    
                    <tr>
                        <td nowrap><label>Claim Uploaded Date To</label></td><td><div id="dateToDiv"/></td>                            
                    </tr>                      
                </table>

                <div class="chox-report-button">
                    <button type="button" id="OSRPPGenerateReportId" onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div>
    </form>
</fieldset>