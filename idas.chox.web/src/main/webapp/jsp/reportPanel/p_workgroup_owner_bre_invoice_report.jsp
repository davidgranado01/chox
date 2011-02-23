<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var reportName = 'WorkgroupOwnerBreInvoiceReport-Excel';

    Ext.onReady(function(){
        var insurerId = <s:property value="userOrganisationId"/>;
        ui.dateField('startDate',getTodayDate(),'dateFromDiv');
        ui.dateField('endDate',getTodayDate(),'dateToDiv');

        
        var claimOwnerWorkflowReader = new Ext.data.JsonReader({
                            totalProperty: 'totalCount',
                            root: 'results',
                            fields:
                            [
                                {name:'id'},
                                {name:'name'}
                            ]
        });

        var claimOwnerWorkflowStore = new Ext.data.Store({
                            proxy : new Ext.data.HttpProxy
                            ({url : "<%= request.getContextPath()%>/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", method:'GET', params : {"workgroupId":-1,"insurerId":insurerId}}),
                            reader : claimOwnerWorkflowReader
        });

        var claimOwnerWorkflowCombo = new Ext.form.ComboBox({
                            store : claimOwnerWorkflowStore,
                            width: 250,
                            renderTo: 'rptOwnerWorkflowOwnerSelectionHolder',
                            valueField : 'id',
                            id : 'ownerWorkflowOwnerComboId',
                            displayField :'name',
                            hiddenName: 'ownerId',
                            typeAhead : true,
                            mode : 'local',
                            emptyText : '--- All ---',
                            listeners: { blur: function () {
                                            if(this.getRawValue() == "" ) {
                                                this.clearValue(); this.reset();
                                               }
                               }}
        });

        if (<s:property value="insurerIsWorkgroupEnabled"/>) {
            var ownerWorkflowWorkgroupJsonReader = new Ext.data.JsonReader({
                                totalProperty: 'totalCount',
                                root: 'results',
                                fields:
                                [
                                    {name:'text'},
                                    {name:'value'}
                                ]
            });

            var  ownerWorkflowWorkgroupStore = new Ext.data.Store({
                                proxy : new Ext.data.HttpProxy
                                    ({url : "<%= request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer2.action", method:'GET'}),
                                reader :  ownerWorkflowWorkgroupJsonReader
            });

            var  ownerWorkflowWorkgroupCombo = new Ext.form.ComboBox({
                                store:  ownerWorkflowWorkgroupStore,
                                renderTo: 'rptOwnerWorkflowWrkgroupSelectionHolder',
                                valueField: 'text',
                                id: 'ownerWorkflowWorkgroupComboId',
                                hiddenName: 'workgroupId',
                                displayField:'value',
                                width: 250,
                                typeAhead: true,
//                                autoWidth: true,
                                mode: 'local',
                                emptyText: '--- All ---',
                                listeners: {select: function () {
                                                        var workgroupId = -1;
                                                        if (ownerWorkflowWorkgroupCombo.getValue() != null) {
                                                            workgroupId = ownerWorkflowWorkgroupCombo.getValue();
                                                        }
//                                                        var insurerId = $("#userInsurerId").val();
                                                        claimOwnerWorkflowCombo.reset();
                                                        claimOwnerWorkflowStore.removeAll();
                                                        claimOwnerWorkflowStore.load({ params : {"workgroupId":workgroupId,"insurerId":insurerId}});
                                                    },
                                            blur: function () {
                                                    if(this.getRawValue() == "" ) {
                                                        this.clearValue(); this.reset();
                                                        claimOwnerWorkflowCombo.reset();
                                                        claimOwnerWorkflowStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
                                                  }
                                }}
                        });
            ownerWorkflowWorkgroupStore.load();
        }
        claimOwnerWorkflowStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
        $("form#formReportParam").validate(
        {
            errorLabelContainer: "#formReportParamMessageBox",
            rules: {
                startDate:{
                    required:true,
                    date: true
                },
                endDate:{
                    required:true,
                    date: true
                }
            },
            messages: {
                startDate: {
                    required:"A value must be supplied for 'Date From'",
                    date:"You must supply a date value 'Date From'"
                },
                endDate: {
                    required:"A value must be supplied for 'Date To'",
                    date:"You must supply a date value 'Date To'"
                }
            }
        });
    });

    function openReport()
    {
//        if($("form#formReportParam").valid()){
            var queryString = $('#formReportParam').formSerialize();
            // If no workgroup selected, insert a '-1' into the query string
            if (queryString.indexOf('workgroupId=&') >= 0)
                queryString = queryString.replace('workgroupId=&', 'workgroupId=-1&')
            window.location= "<%=request.getContextPath()%>/prv/p/exportExcelReport.action?reportName=" + reportName + "&" + queryString;
//        }
    }

</script>
<fieldset class="x-fieldset">
    <legend>Workgroup and Owner BRE Invoice Approval Dispute Report</legend>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report provides an insight into the the number of invoices that pass the business rules and the resulting action taken, looking at whether the invoices are being paid or disputed, broken down by handler.  This report also provides a breakdown of why invoices are being disputed after being approved by the business rules.  The report will look at all invoices that were uploaded during the selected period from and to dates.</div>

                <table class="report-form">

                    <s:if test="insurerIsWorkgroupEnabled">
                        <tr>
                            <td nowrap><label>Workgroup</label></td>
                            <td>
                                <div id="rptOwnerWorkflowWrkgroupSelectionHolder"></div>
                            </td>
                        </tr>
                    </s:if>
                    <s:else>
                        <input type="hidden" id="workgroupId" name="workgroupId" value="-1"/>
                    </s:else>
                        <tr>
                            <td nowrap><label>Claim Owner</label></td>
                            <td>
                                <div id="rptOwnerWorkflowOwnerSelectionHolder"></div>
                            </td>
                        </tr>
                    <tr>
                        <td nowrap width="30%"><label>Period From</label></td><td><div id="dateFromDiv" /></td>
                    </tr>
                    <tr>
                        <td nowrap><label>Period To</label></td><td><div id="dateToDiv"/></td>
                    </tr>
                    
                </table>

                <div class="chox-report-button">
                    <button type="button" onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div></form>
</fieldset>
