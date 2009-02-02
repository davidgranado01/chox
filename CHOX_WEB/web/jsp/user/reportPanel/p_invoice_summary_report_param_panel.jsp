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
        var queryString = $('#formReportParam').formSerialize();  
        var popwin = window.open("exportExcelReport.action?" + "reportName=" + reportName + "&" + queryString, "Excel", "WIDTH=575,HEIGHT=500,RESIZABLE=No,SCROLLBARS=YES,TOOLBAR=NO,LEFT=200,TOP=100");
    }
    
    Ext.onReady(function(){
        
        var dateFromPicker = new Ext.form.DateField({
            name: 'DateStart',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true
        });
        
        var dateToPicker = new Ext.form.DateField({
            name: 'DateEnd',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true
        });
        
        dateFromPicker.render('dateFromDiv');        
        dateToPicker.render('dateToDiv');
        
    }); 
        
</script>

<fieldset class="x-fieldset">
    <legend><s:property value="reportName"/></legend>
    <div class="x-panel-bwrap chox-form-container">      
        <div class="form-container">
            <form id="formReportParam" class="XXentity-form">
                <table cellpadding="0" cellspacing="0" class="searchForm" style="width:99%;" border="0">
                    
                    <tr>
                        <td nowrap><label>Date From</label></td><td colspan="2"><div id="dateFromDiv" /></td>
                        <td nowrap><label>Date To</label></td><td colspan="2"><div id="dateToDiv"/></td>                            
                    </tr>
                    <tr>
                        <s:if test="isInsurer">
                            <td><label>CHO</label></td><td colspan="2"><s:select name="OrgId" list="suppliers" listKey="id" listValue="name" headerKey="-1"
                                                                                     headerValue="--- ALL ---"
                                                                                 emptyOption="false"></s:select></td>
                        </s:if> 
                        <s:if test="isCHO">
                            <td><label>Insurer</label></td><td colspan="2"><s:select name="OrgId" list="insurers" listKey="id" listValue="name" headerKey="-1"
                                                                                         headerValue="--- ALL ---"
                                                                                     emptyOption="false"></s:select>  </td>
                        </s:if>   
                        
                        <td colspan="3">&nbsp;</td>
                    </tr>        
                </table>
            </form> 
            <div class="chox-form-button">
                <a href="javascript:openReport();">Open Report</a>
                
            </div>
            <div id="INCmessageBox" class="errorBox"></div>
            <div id="submitResult" class="chox-form-submit-result"></div>    
        </div>
        
    </div>
</fieldset>