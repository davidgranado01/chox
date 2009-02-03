<%-- 
    Document   : p_invoice_summary_report_param_panel
    Created on : 30-Jan-2009, 17:12:00
    Author     : Emmanuel
--%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
        
    var reportName = 'InvoiceSummaryReport-Excel';

    function openReport()
    {        
        if(doFormValidation().form()){        
            var queryString = $('#formReportParam').formSerialize();  
            var popwin = window.open("exportExcelReport.action?" + "reportName=" + reportName + "&" + queryString, "Excel", "WIDTH=575,HEIGHT=500,RESIZABLE=No,SCROLLBARS=YES,TOOLBAR=NO,LEFT=200,TOP=100");
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
    <legend>Invoice Summary Report</legend>
    <div class="x-panel-bwrap chox-form-container">      
        <div class="form-container">
            <div class="status-info">
                Report Description
            </div>
            
                
                <table cellpadding="0" cellspacing="0" class="searchForm" style="width:99%;" border="0">                    
                    <tr>
                        <td nowrap><label>Date From</label></td><td colspan="2"><div id="dateFromDiv" /></td>
                        <td nowrap><label>Date To</label></td><td colspan="2"><div id="dateToDiv"/></td>                            
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