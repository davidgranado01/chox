<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'ClaimRejectedReport-Excel';

    Ext.onReady(function(){
        
        ui.dateField('DateStart',getTodayDate(),'dateFromDiv');
        ui.dateField('DateEnd',getTodayDate(),'dateToDiv');

        $("form#formReportParam").validate(
        {
            errorLabelContainer: "#formReportParamMessageBox",
            rules: {
                DateStart:{
                    required:true,
                    date: true
                },
                DateEnd:{
                    required:true,
                    date: true
                }
            },
            messages: {
                DateStart: {
                    required:"A value must be supplied for 'Created Date From'",
                    date:"You must supply a date value 'Created Date From'"
                },
                DateEnd: {
                    required:"A value must be supplied for 'Created Date To'",
                    date:"You must supply a date value 'Created Date To'"
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

    <legend>Claim Rejected Report</legend>

    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">

        <div class="x-panel-bwrap chox-form-container">

            <div class="form-container">

                <div class="instruction-message">
                    This report provides information on why claims have been rejected in CHOX at the claim notification stage. The dates that require selection below refer to the date the claim was uploaded onto CHOX.
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
                    <button type="button" id="CRRPPGenerateReportId"onclick="javascript:openReport();">Generate Report</button>
                </div>
            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div>
    </form>
</fieldset>