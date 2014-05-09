<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'AverageSettlementAmount-Excel';
    
    Ext.onReady(function(){

        ui.dateField('DateStart',getTodayDate(),'dateFromDiv');
        ui.dateField('DateEnd',getTodayDate(),'dateToDiv');

        $("form#formReportParam").validate(
        {
            errorLabelContainer: "#formReportParamMessageBox",
            rules: {
                DateStart:{
                    required:true,
                    dateITA: true
                },
                DateEnd:{
                    required:true,
                    dateITA: true
                }
            },
            messages: {
                DateStart: {
                	max:function(){
                		var sd = Ext.get('DateStart').getValue().split("/");
                        var ed = Ext.get('DateEnd').getValue().split("/");
                        var time = new Date(sd[2],sd[1] - 1 ,sd[0]).getTime() - new Date(ed[2],ed[1] - 1 ,ed[0]).getTime();
                        if(time > 0)
                            return true;
                    },
                    required:"A value must be supplied for 'Date From'",
                    dateITA:"You must supply a date value 'Date From'"
                },
                DateEnd: {
                	max:"'Date to' can't be before 'Date From'",
                    required:"A value must be supplied for 'Date To'",
                    dateITA:"You must supply a date value 'Date To'"
                }
            }
        });

    });
    
    function openReport()
    {
        if($("form#formReportParam").valid()){
//            var queryString = $('#formReportParam').formSerialize();
            var queryString = {};
            $.each($('#formReportParam').serializeArray(), function() {queryString[this.name] = this.value;});
            generateReport(queryString);
        }
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
                        <td nowrap width="30%"><label>Settlement Date From</label><span class="mandatory">*</span></td><td><div id="dateFromDiv" /></td>                       
                    </tr>    
                    <tr>
                        <td nowrap><label>Settlement Date To</label><span class="mandatory">*</span></td><td><div id="dateToDiv"/></td>                            
                    </tr>  
                </table>
                <div class="chox-report-button">
                    <button type="button" id="IASRGenerateReportId"onclick="javascript:openReport();">Generate Report</button>
                </div> 
            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div></form>
</fieldset>
