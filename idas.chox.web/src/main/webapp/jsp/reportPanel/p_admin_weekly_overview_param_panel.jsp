<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'AdminWeeklyOverviewReport-Excel';
    
    Ext.onReady(function(){
        ui.dateField('DateStart',getTodayDate(),'dateFromDiv');
        ui.dateField('DateEnd',getTodayDate(),'dateToDiv');

        $("#formReportParam").validate(
        {
            errorLabelContainer: "#formReportParamMessageBox",
            rules: {
                DateStart:{required:true, date:true},
                DateEnd:{required:true, date:true}
            },
            messages: {
                DateStart: {required:"A value must be supplied for 'Date From'", date:"You must supply a date value 'Date From'"},
                DateEnd: {required:"A value must be supplied for 'Date To'", date:"You must supply a date value 'Date To'"
                }
            }
        });

    }); 

    function openReport()
    {
        if($("form#formReportParam").valid()){
            var queryString = $('#formReportParam').formSerialize();
            window.location = "<%=request.getContextPath()%>/prv/p/exportExcelReport.action?reportName=" + reportName + "&" + queryString;
        }
    }

    
</script>

<fieldset class="x-fieldset">

    <legend>Admin Weekly Overview Report</legend>

    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">

        <div class="x-panel-bwrap chox-form-container">

            <div class="form-container">

                <div class="instruction-message">
                    This report provides an overview of claims activity reported by weekly segments, expsoing both a historical and current position regarding CHOX claims. The dates that require selection below refer to the dates you wish the report to include, remember the report is based on weekly segments with weeks running from Monday to Sunday.
                </div>

                <table class="report-form">
                    <tr>
                        <s:if test="isInsurer">
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
                        </s:if>
                        <s:if test="isCHO">
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
                        </s:if>
                    </tr>
                    <tr>
                        <td nowrap width="30%"><label>Date From</label></td><td><div id="dateFromDiv" /></td>
                    </tr>
                    <tr>
                        <td nowrap><label>Date To</label></td><td><div id="dateToDiv"/></td>
                    </tr>
                </table>

                <div class="chox-report-button">
                    <button type="button" onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>

            <div id="formReportParamMessageBox" class="action-error-msg"></div>

        </div>

    </form>

</fieldset>