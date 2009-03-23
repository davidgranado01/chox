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
                supplierId:{
                    required:true
                },
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
                supplierId:{
                    required:"You must select 'Credit Hire Organisation'"
                },                
                DateStart: {
                    required:"A value must be supplied for 'Invoice Uploaded From'",
                    date:"You must supply a date value 'Invoice Uploaded From'"
                }, 
                DateEnd: {
                    required:"A value must be supplied for 'Invoice Uploaded To'",
                    date:"You must supply a date value 'Invoice Uploaded To'"
                }         
            }
        });

        return validateFlag;
    }
    
</script>

<form id="formReportParam" class="XXentity-form" name="formReportParam">
    
<fieldset class="x-fieldset">
    <legend>Overview Summary Report</legend>
    <div class="x-panel-bwrap chox-form-container">      
        <div class="form-container">
            
            <div class="ReportActionMsg" align="justify"> [MSG] </div>
            
                <table cellpadding="0" cellspacing="0" class="searchForm" style="width:99%;" border="0">  
<s:if test="!isCHO">             
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
    headerValue="--- Select ---"
    emptyOption="false">
    </s:select>
</td>                            
</tr> 
</s:if>
                    <tr>
                        <td nowrap width="30%"><label>Invoice Uploaded From</label></td><td><div id="dateFromDiv" /></td>                       
                    </tr>    
                    <tr>
                        <td nowrap><label>Invoice Uploaded To</label></td><td><div id="dateToDiv"/></td>                            
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