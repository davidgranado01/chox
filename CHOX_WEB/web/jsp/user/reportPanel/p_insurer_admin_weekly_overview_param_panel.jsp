<%-- 
    Document   : p_insurer_admin_weekly_overview_param_panel
    Created on : 26-Jan-2009, 17:12:00
    Author     : Emmanuel
--%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
        
    var reportName = 'InsurerAdminWeeklyOverviewReport-Excel';

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
<fieldset class="x-fieldset">
    <legend>Insurer Admin Weekly Overview Report</legend>
    <div class="x-panel-bwrap chox-form-container">      
        <div class="form-container">
            <div class="ReportActionMsg" align="justify">
This report provides an overview of claims activity reported by weekly segments, expsoing both a historical and current position regarding CHOX claims. The dates that require selection below refer to the dates you wish the report to include, remember the report is based on weekly segments with weeks running from Monday to Sunday.
            </div>
            
                <table cellpadding="0" cellspacing="0" class="searchForm" style="width:99%;" border="0">                    
<tr>
<td nowrap><label>Credit Hire Organisation</label></td>
<td>                            
<s:select 
    name="supplierId" 
    id="supplierId"
    list="suppliers" 
    listKey="id"
    listValue="name"
    headerKey=""
    headerValue="--- ALL ---"
    emptyOption="false">
    </s:select>
</td>                            
</tr>
                    <tr>
                        <td nowrap width="30%"><label>Date From</label></td><td><div id="dateFromDiv" /></td>                       
                    </tr>    
                    <tr>
                        <td nowrap><label>Date To</label></td><td><div id="dateToDiv"/></td>                            
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