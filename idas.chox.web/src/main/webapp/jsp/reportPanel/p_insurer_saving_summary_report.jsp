<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var reportName = 'InvoiceSavingSummaryReport-Excel';

    Ext.onReady(function(){

        ui.dateField('DateStart',getTodayDate(),'dateFromDiv');
        ui.dateField('DateEnd',getTodayDate(),'dateToDiv');

        $("form#formReportParam").validate(
        {
            errorLabelContainer: "#formReportParamMessageBox",
            rules: {
                chOrganisationId:{required:true},
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
                chOrganisationId:{required:"Please select a 'Credit Hire Organisation'"},
                DateStart: {
                	max:function(){
                		var sd = Ext.get('DateStart').getValue().split("/");
                        var ed = Ext.get('DateEnd').getValue().split("/");
                        var time = new Date(sd[2],sd[1] - 1 ,sd[0]).getTime() - new Date(ed[2],ed[1] - 1 ,ed[0]).getTime();
                        if(time > 0)
                            return true;
                    },
                    required:"A value must be supplied for 'Date From'",
                    dateITA:"You must supply a date value 'Date From'"
                },
                DateEnd: {
                	max:"'Date to' can't be before 'Date From'",
                    required:"A value must be supplied for 'Date To'",
                    dateITA:"You must supply a date value 'Date To'"
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

    <legend>Invoice Saving Summary Report</legend>

    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">

        <input id="insurerId" name="insurerId" type="hidden"/>

        <div class="x-panel-bwrap chox-form-container">

            <div class="form-container">

                <div class="instruction-message">
                    This report displays the amount saved on CHO invoices, comparing the original invoice amount as submitted by the CHO against the final settlement amount.
                </div>

                <table class="report-form">

                    <tr>
                        <td nowrap><label>Credit Hire Organisation</label></td>
                        <td>
                            <s:select
                                name="chOrganisationId"
                                id="ISSRchOrganisationId"
                                list="suppliers"
                                listKey="id"
                                listValue="name"
                                headerKey=""
                                headerValue="-- Please Select --"
                                emptyOption="false">
                            </s:select>
                        </td>
                    </tr>

                    <tr>
                        <td nowrap width="30%"><label>Invoice Upload Date From</label><span class="mandatory">*</span></td><td><div id="dateFromDiv" /></td>
                    </tr>

                    <tr>
                        <td nowrap><label>Invoice Upload Date To</label><span class="mandatory">*</span></td><td><div id="dateToDiv"/></td>
                    </tr>

                </table>

                <div class="chox-report-button">
                    <button type="button" id="ISSRGenerateReportId"onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>

            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div>

    </form>

</fieldset>
