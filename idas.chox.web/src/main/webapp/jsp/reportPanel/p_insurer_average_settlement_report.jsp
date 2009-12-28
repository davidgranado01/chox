<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'AverageSettlementAmount-Excel';
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
<fieldset class="x-fieldset">
    <legend>Average Claim Settlement Amount Report</legend>
<form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">

    <div class="x-panel-bwrap chox-form-container">      
        <div class="form-container">
            <div class="instruction-message">This report displays the average settlement amount, filtered by month, for each CHO using the CHOX system.</div>
            
                <table class="report-form">

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
                <div class="chox-report-button">
                    <button type="button" onclick="javascript:openReport();">Generate Report</button>
                </div> 
        </div>
        <div id="acknowledge-message-box"></div>
    </div></form>
</fieldset>
