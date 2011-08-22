<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'OwnerWorkflowReport-Excel';

    Ext.onReady(function(){
        new Ext.ToolTip({ target: 'help-service-commencing-icon', html: 'This refers to the Monday that you wish to select as the initial start date in order to calculate the ‘Time In Service’ and ‘Weeks In Service’ columns for this report.'});
        var insurerId = <s:property value="userOrganisationId"/>;
        ui.dateField('startDate',getTodayDate(),'dateFromDiv');
        ui.dateField('endDate',getTodayDate(),'dateToDiv');

        var serviceCommencingDatePicker = new Ext.form.DateField({
            name: 'serviceCommencingDate',
            renderTo: 'serviceCommencingDiv',
            width: 120,
            allowBlank: false,
            format: 'd/m/Y',
            disabledDays: [0,2,3,4,5,6],
            disabledDaysText: 'You must select a Monday',
//            hideMode: 'offsets',
            value: '03/01/2011',
            showWeekNumber: true
        });

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
                            triggerAction : 'all',
                            forceSelection : true,
                            listeners: { blur: function () {
                                            if(this.getRawValue() == "" ) {
                                                this.clearValue(); this.reset();
                                               }
                               }}
        });

        <s:if test="insurerIsWorkgroupEnabled">
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
                                triggerAction : 'all',
                                forceSelection : true,
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
        </s:if>
        claimOwnerWorkflowStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
        $("form#formReportParam").validate(
        {
            errorLabelContainer: "#formReportParamMessageBox",
            rules: {
                startDate:{
                    required:true,
                    dateITA: true
                },
                endDate:{
                    required:true,
                    dateITA: true
                }
            },
            messages: {
                startDate: {
                    required:"A value must be supplied for 'Date From'",
                    dateITA:"You must supply a date value 'Date From'"
                },
                endDate: {
                    required:"A value must be supplied for 'Date To'",
                    dateITA:"You must supply a date value 'Date To'"
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
    <legend>Claim Owner Workflow Report</legend>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report provides an insight into claim processing efficiency and resource scheduling for a particular Workgroup(s) or Claim Owner(s), depending on the selection made. The period from and to dates below determine the number of tasks processed during the selected period and all the outstanding task columns are based on outstanding work as at the date selected in the 'Period To' field. Please note that this report looks at outstanding tasks as at midnight yesterday.</div>

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
                    <tr>
                        <td nowrap width="30%"><label>Service Commencing<span class="mandatory">*</span><img id="help-service-commencing-icon" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt="" /></label></td><td><div id="serviceCommencingDiv" /></td>
                    </tr>

                </table>

                <div class="chox-report-button">
                    <button type="button" id="OWRPPGenerateReportId"onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div></form>
</fieldset>
