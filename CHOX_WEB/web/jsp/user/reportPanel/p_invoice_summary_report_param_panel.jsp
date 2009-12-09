<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'InvoiceSummaryReport-Excel';
    
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

<fieldset class="x-fieldset">

    <legend>Invoice Summary Report</legend>

    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">
    
    <div class="x-panel-bwrap chox-form-container">      

        <div class="form-container">
            
            <div class="instruction-message">
                This report provides information at a high level regarding the financials of CHOX invoices, including details relating to penalty charges as a result of late payments. The dates that require selection below refer to the date the invoice was uploaded onto CHOX.
            </div>
            
                <table class="report-form">
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
            
                <div class="chox-report-button">
                    <button type="button" onclick="javascript:openReport();">Generate Report</button>
                </div>
        </div>
        <div id="acknowledge-message-box"></div>
    </div>
    </form>
</fieldset>