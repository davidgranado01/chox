<%-- 
    Document   : p_insurer_admin_weekly_overview_param_panel
    Created on : 26-Jan-2009, 17:12:00
    Author     : Emmanuel
--%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
        
    var reportName = 'AverageSettlementAmount-Excel';

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
    <legend>Average Claim Settlement Amount Report</legend>
    <div class="x-panel-bwrap chox-form-container">      
        <div class="form-container">
            <div class="ReportActionMsg" align="justify">This report displays the average settlement amount, filtered by month, for each CHO using the CHOX system.</div>
            
                <table cellpadding="0" cellspacing="0" class="searchForm" style="width:99%;" border="0">     
              
<s:if test="isCHOXAdmin">
    <tr>
    <td nowrap><label>Insurer</label></td>
    <td>      
    <s:select 
    name="insurerId" 
    id="insurerId"
    list="insurers" 
    listKey="id"
    listValue="name"
    headerKey=""
    headerValue="--- ALL ---"
    emptyOption="false">
    </s:select>
    </td>
    </tr>     
</s:if>
<s:else><input id="insurerId" name="insurerId" type="hidden"/></s:else>

                    <tr>
                        <td nowrap width="30%"><label>Settlement Date From</label></td><td><div id="dateFromDiv" /></td>                       
                    </tr>    
                    <tr>
                        <td nowrap><label>Settlement Date To</label></td><td><div id="dateToDiv"/></td>                            
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