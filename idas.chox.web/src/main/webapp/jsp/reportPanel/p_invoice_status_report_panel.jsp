<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var reportName = 'InvoiceStatusReport-Excel';


    Ext.onReady(function(){
        ui.dateField('DateStart',getTodayDate(),'startDateDiv');
       

    });


    function openReport()
    {
        if($("form#formReportParam").valid()){
            var queryString = $('#formReportParam').formSerialize();
            window.location= "<%=request.getContextPath()%>/prv/p/exportExcelReport.action?reportName=" + reportName + "&" + queryString;
        }
    }

    
</script>
<fieldset class="x-fieldset">
    <legend>Invoice Status Report</legend>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report produces a list of claims that require payment. The report results are per CHO and allow an Insurer to make payments in a more efficient manner.</div>

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
                            <td nowrap><label>Create Date</label></td>
                            <td>
                                <div id="startDateDiv" />
                            </td>
                        </tr>

                </table>

                <div class="chox-report-button">
                    <button type="button" onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div></form>
</fieldset>
