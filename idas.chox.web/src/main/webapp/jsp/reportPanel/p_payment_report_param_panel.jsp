<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'PaymentReport-Excel';

    Ext.onReady(function(){

        var target = "div#rptPaymentWorkgroupSelectionHolder";
        var url = "<%=request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer.action";
        var param = {};

        ajax.loadHtml(url,param,function(data){
            $(target).html(data);
        });

        $("form#formReportParam").validate(
        {
            errorLabelContainer: "#formReportParamMessageBox",
            rules: {
                supplierId:{
                    required:true
                }

            },
            messages: {
                supplierId:{
                    required:"You must select 'Credit Hire Organisation'"
                }

            }
        });

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
    <legend>CHO Payment Bordereau</legend>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report produces a list of claims that require payment. The report results are per CHO and allow an Insurer to make payments in a more efficient manner.</div>

                <table class="report-form">

                    <s:if test="!isCHO && !isCH">
                        <tr>
                            <td nowrap><label>Workgroup</label></td>
                            <td>
                                <div id="rptPaymentWorkgroupSelectionHolder"></div>
                            </td>
                        </tr>
                    </s:if>
                    <s:else>
                        <input type="hidden" id="workgroupId" name="workgroupId" value="-1"/>
                    </s:else>

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
                                    headerValue="-- Please Select --"
                                    emptyOption="false">
                                </s:select>
                            </td>
                        </tr>
                    </s:if>
                </table>

                <div class="chox-report-button">
                    <button type="button" onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div></form>
</fieldset>
