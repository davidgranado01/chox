<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'OverviewSummary-Excel';
    
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

<fieldset class="x-fieldset">
    <legend>Claim Overview Summary Report</legend>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">
    <div class="x-panel-bwrap chox-form-container">      
        <div class="form-container">
            
            <div class="instruction-message">
<s:if test="isCHO">
This report shows a high level summary of claims across all Insurers and per Insurer. Displaying information such as average invoice values, average hire durations and average cycle times.    
</s:if>      
<s:else>
This report shows a high level summary of claims across all CHOs and per CHO. Displaying information such as average invoice values, average hire durations and average cycle times.    
</s:else>      
             </div>
            
                <table class="report-form">
                    <tr>
                        <td nowrap width="30%"><label>Claim Uploaded Date From</label></td><td><div id="dateFromDiv" /></td>                       
                    </tr>    
                    <tr>
                        <td nowrap><label>Claim Uploaded Date To</label></td><td><div id="dateToDiv"/></td>                            
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
