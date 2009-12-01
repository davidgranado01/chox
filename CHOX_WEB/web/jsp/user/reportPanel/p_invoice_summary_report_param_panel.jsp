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
    <legend>Invoice Summary Report</legend>
    <div class="x-panel-bwrap chox-form-container">      
        <div class="form-container">
            
            <div class="ReportActionMsg" align="justify">This report provides information at a high level regarding the financials of CHOX invoices, including details relating to penalty charges as a result of late payments. The dates that require selection below refer to the date the invoice was uploaded onto CHOX.
            </div>
            
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
    headerValue="--- ALL ---"
    emptyOption="false">
    </s:select>
</td>                            
</tr> 
</s:if>
<s:else>
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
</s:else>
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