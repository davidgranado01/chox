<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var reportName = 'InvoiceSavingSummaryReport-Excel';

    Ext.onReady(function(){
        ui.dateField('DateStart',getTodayDate(),'dateFromDiv');
        ui.dateField('DateEnd',getTodayDate(),'dateToDiv');
    });

    function openReport()
    {
        if(doFormValidation().form()){
            var queryString = $('#formReportParam').formSerialize();

            window.location= "exportExcelReport.action?" + "reportName=" + reportName + "&" + queryString;
        }
    }
    
    function doFormValidation(){

        var validateFlag = $("#formReportParam").validate(
        {
            errorLabelContainer: "#acknowledge-message-box",
            rules: {
                chOrganisationId:{required:true},
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
                chOrganisationId:{required:"Please select a 'Credit Hire Organisation'"},
                DateStart: {
                    required:"A value must be supplied for 'Date From'",
                    date:"You must supply a date value 'Date From'"
                },
                DateEnd: {
                    required:"A value must be supplied for 'Date To'",
                    date:"You must supply a date value 'Date To'"
                }
            }
        });

        return validateFlag;
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
                    id="chOrganisationId"
                    list="suppliers"
                    listKey="id"
                    listValue="name"
                    headerKey=""
                    headerValue="- Please Select -"
                    emptyOption="false">
                    </s:select>
                </td>
                </tr>

                <tr>
                    <td nowrap width="30%"><label>Invoice Upload Date From</label></td><td><div id="dateFromDiv" /></td>
                </tr>

                <tr>
                    <td nowrap><label>Invoice Upload Date To</label></td><td><div id="dateToDiv"/></td>
                </tr>

            </table>
            
            <div class="chox-report-button">
                <button type="button" onclick="javascript:openReport();">Generate Report</button>
            </div>

        </div>

        <div id="acknowledge-message-box"></div>
    </div>
    
    </form>
    
</fieldset>
