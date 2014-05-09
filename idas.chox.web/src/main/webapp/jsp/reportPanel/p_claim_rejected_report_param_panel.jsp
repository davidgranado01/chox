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
                	max:function(){
                		var sd = Ext.get('DateStart').getValue().split("/");
                        var ed = Ext.get('DateEnd').getValue().split("/");
                        var time = new Date(sd[2],sd[1] - 1 ,sd[0]).getTime() - new Date(ed[2],ed[1] - 1 ,ed[0]).getTime();
                        if(time > 0)
                            return true;
                    },
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
                	max:"'Date To' can't be before 'Date From'",
                    required:"A value must be supplied for 'Claim Uploaded Date From'",
                    dateITA:"You must supply a date value 'Claim Uploaded Date From'"
                },
                DateEnd: {
                    required:"A value must be supplied for 'Claim Uploaded Date To'",
                    dateITA:"You must supply a date value 'Claim Uploaded Date To'"
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

    <legend>Claim Rejected Report</legend>

    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">

        <div class="x-panel-bwrap chox-form-container">

            <div class="form-container">

				<div class="instruction-message">
				    This report provides
					information on why claims have been rejected in CHOX at the claim
					notification stage. The dates that require selection below refer to
					the date the claim was uploaded onto CHOX.
				</div>

				<table class="report-form">
                    <tr>
						<td nowrap width="30%"><label>Claim Uploaded Date From</label><span class="mandatory">*</span></td>
						<td><div id="dateFromDiv" /></td>
					</tr>
                    <tr>
                        <td nowrap><label>Claim Uploaded Date To</label><span class="mandatory">*</span></td><td><div id="dateToDiv"/></td>
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