<%-- 
    Document   : p_invoice_summary_report_param_panel
    Created on : 30-Jan-2009, 17:12:00
    Author     : Emmanuel
--%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
        
    var reportName = 'OverviewSummary-Excel';

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
            allowBlank: true,
            format: 'd/m/Y',
            value: getTodayDate(),
            showWeekNumber: true
        });
        
        var dateToPicker = new Ext.form.DateField({
            name: 'DateEnd',
            width: 120,
            allowBlank: true,
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
                    required:"A value must be supplied for 'Claim Uploaded From'",
                    date:"You must supply a date value 'Claim Uploaded From'"
                }, 
                DateEnd: {
                    required:"A value must be supplied for 'Claim Uploaded To'",
                    date:"You must supply a date value 'Claim Uploaded To'"
                }         
            }
        });

        return validateFlag;
    }
    
</script>

<form id="formReportParam" class="XXentity-form" name="formReportParam">
    
<fieldset class="x-fieldset">
    <legend>Claim Overview Summary Report</legend>
    <div class="x-panel-bwrap chox-form-container">      
        <div class="form-container">
            
            <div class="ReportActionMsg" align="justify">
<s:if test="isCHO">
This report shows a high level summary of claims across all Insurers and per Insurer. Displaying information such as average invoice values, average hire durations and average cycle times.    
</s:if>      
<s:else>
This report shows a high level summary of claims across all CHOs and per CHO. Displaying information such as average invoice values, average hire durations and average cycle times.    
</s:else>      
             </div>
            
                <table cellpadding="0" cellspacing="0" class="searchForm" style="width:99%;" border="0">  
                    <tr>
                        <td nowrap width="30%"><label>Claim Uploaded Date From</label></td><td><div id="dateFromDiv" /></td>                       
                    </tr>    
                    <tr>
                        <td nowrap><label>Claim Uploaded Date To</label></td><td><div id="dateToDiv"/></td>                            
                    </tr>                      
                </table>
            
            <div align="right" class="chox-form-button">
                <button type="button" onclick="javascript:openReport();">Generate Report</button>                
            </div>
            <div id="INCmessageBox" class="errorBox"></div>
            <div id="submitResult" class="chox-form-submit-result"></div>    
        </div>
        <div class="errorBox" id="ACKmessageBox"></div>
    </div>
</fieldset>
</form> 