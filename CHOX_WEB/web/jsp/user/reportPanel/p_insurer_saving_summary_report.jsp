<%--
    Document   : p_insurer_admin_weekly_overview_param_panel
    Created on : 26-Jan-2009, 17:12:00
    Author     : Emmanuel
--%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">

    var reportName = 'InvoiceSavingSummaryReport-Excel';

    function openReport()
    {
        if(doFormValidation().form()){
            var queryString = $('#formReportParam').formSerialize();

            window.location= "exportExcelReport.action?" + "reportName=" + reportName + "&" + queryString;
        }
    }

    Ext.onReady(function(){

        var dateFromPicker = new Ext.form.DateField({
            name: 'DateStart',
            width: 120,
            allowBlank: false,
            format: 'd/m/Y',
            value: getTodayDate(),
            showWeekNumber: true
        });

        var dateToPicker = new Ext.form.DateField({
            name: 'DateEnd',
            width: 120,
            allowBlank: false,
            format: 'd/m/Y',
            value: getTodayDate(),
            showWeekNumber: true
        });

        dateFromPicker.render('dateFromDiv');
        dateToPicker.render('dateToDiv');

    });

    function doFormValidation(){

        var validateFlag = $("#formReportParam").validate(
        {
            errorLabelContainer: "#ACKmessageBox",
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

<form id="formReportParam" class="XXentity-form" name="formReportParam">

    <input id="insurerId" name="insurerId" type="hidden"/>
    
<fieldset class="x-fieldset">
    <legend>Invoice Saving Summary Report</legend>
    <div class="x-panel-bwrap chox-form-container">
        <div class="form-container">
            <div class="ReportActionMsg" align="justify">This report displays the amount saved on CHO invoices, comparing the original invoice amount as submitted by the CHO against the final settlement amount.</div>
                <table cellpadding="0" cellspacing="0" class="searchForm" style="width:99%;" border="0">

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
            
                <div class="chox-form-button" align="right">
                    <button type="button" onclick="javascript:openReport();">Generate Report</button>
                </div>
            <div id="INCmessageBox" class="errorBox"></div>
            <div id="submitResult" class="chox-form-submit-result"></div>
        </div>
        <div class="errorBox" id="ACKmessageBox"></div>
    </div>
</fieldset>
</form>