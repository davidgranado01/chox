<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'PaymentReport-Excel';

    Ext.onReady(function(){

        if ( <s:property value="insurerIsWorkgroupEnabled" />) {

            var choPaymentWorkgroupJsonReader = new Ext.data.JsonReader({
                                totalProperty: 'totalCount',
                                root: 'results',
                                fields:
                                [
                                    {name:'text'},
                                    {name:'value'}
                                ]
            });

            var choPaymentWorkgroupStore = new Ext.data.Store({
                                proxy : new Ext.data.HttpProxy
                                    ({url : "<%= request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer2.action", method:'GET'}),
                                reader : choPaymentWorkgroupJsonReader
            });

            var choPaymentWorkgroupCombo = new Ext.form.ComboBox({
                                store: choPaymentWorkgroupStore,
                                renderTo: 'rptPaymentWorkgroupSelectionHolder',
                                valueField: 'text',
                                id: 'choPaymentWorkgroupComboId',
                                hiddenName: 'workgroupId',
                                displayField:'value',
                                typeAhead: true,
                                autoWidth: true,
                                mode: 'local',
                                emptyText: '--- All ---',
                                listeners: {blur: function () {
                                                if(this.getRawValue() == "" ) {
                                                    this.clearValue();
                                                }
                                           }
                                }
            });
            choPaymentWorkgroupStore.load();
        }

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
            // If no workgroup selected, insert a '-1' into the query string
            if (queryString.indexOf('workgroupId=&') >= 0)
                queryString = queryString.replace('workgroupId=&', 'workgroupId=-1&')
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

                    <s:if test="!isCHO && !isCH && insurerIsWorkgroupEnabled">
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
