<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'OwnerWorkflowReport-Excel';

    Ext.onReady(function(){
        new Ext.ToolTip({ target: 'help-service-commencing-icon', html: 'This refers to the Monday that you wish to select as the initial start date in order to calculate the ‘Time In Service’ and ‘Weeks In Service’ columns for this report.'});
        var insurerId = <s:property value="userOrganisationId"/>;
        ui.unvalidatedDateField('startDate',getTodayDate(),'dateFromDiv');
        ui.unvalidatedDateField('endDate',getTodayDate(),'dateToDiv');

        var serviceCommencingDatePicker = new Ext.form.DateField({
            id: 'serviceCommencingDate',
            name: 'serviceCommencingDate',
            renderTo: 'serviceCommencingDiv',
            width: 100,
            allowBlank: false,
            format: 'd/m/Y',
            disabledDays: [0,2,3,4,5,6],
            disabledDaysText: 'You must select a Monday',
//            hideMode: 'offsets',
            value: '03/01/2011',
            showWeekNumber: true,
            validationEvent : false
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

        var claimOwnerWorkflowStore = new choxDataStore({
                            url : "/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", 
                            params : {"workgroupId":-1,"insurerId":insurerId},
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

            var  ownerWorkflowWorkgroupStore = new choxDataStore({
                                url : "/prv/p/WorkgroupDropDownActionByInsurer2.action",
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
            	serviceCommencingDate:{
            		 required:true,
                     dateITA: true
            	},
                startDate:{
                	max:function(){
                        var sd = Ext.get('startDate').getValue().split("/");
                        var ed = Ext.get('endDate').getValue().split("/");
                        var time = new Date(sd[2],sd[1] - 1 ,sd[0]).getTime() - new Date(ed[2],ed[1] - 1 ,ed[0]).getTime();
                        if(time > 0)
                            return true;
                    },
                    required:true,
                    dateITA: true
                },
                endDate:{
                    required:true,
                    dateITA: true
                }
            },
            messages: {
            	serviceCommencingDate:{
                    required:"A value must be supplied for 'Service Commencing'",
                    dateITA: "You must supply valid date format for 'Service Commencing'"
                },
                startDate: {
                	max:"'Period to' can't be before 'Period From'",
                    required:"A value must be supplied for 'Period From'",
                    dateITA:"You must supply valid date format for 'Period From'"
                },
                endDate: {
                    required:"A value must be supplied for 'Period To'",
                    dateITA:"You must supply valid date format for 'Period To'"
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
            // If no workgroup selected, insert a '-1' into the query string
            if (queryString.workgroupId === "") {
                queryString.workgroupId = -1;
            }
//            if (queryString.indexOf('workgroupId=&') >= 0)
//                queryString = queryString.replace('workgroupId=&', 'workgroupId=-1&')
            generateReport(queryString);
        }
    }
   
</script>
<fieldset class="x-fieldset">
<s:if test="insurerIsWorkgroupEnabled">
    <legend>Workgroup and Claim Owner Workflow Report</legend>
</s:if>
<s:else>
    <legend>Claim Owner Workflow Report</legend>
</s:else>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report provides an insight into claim processing efficiency and resource scheduling for a particular Workgroup(s) or Claim Owner(s), depending on the selection made. The period from and to dates below determine the number of actions processed during the selected period and all the outstanding action columns are based on outstanding work as at the date selected in the 'Period To' field. Please note that this report looks at outstanding actions as at midnight yesterday.</div>

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
                        <td nowrap width="30%"><label>Period From</label><span class="mandatory">*</span></td><td><div id="dateFromDiv" /></td>
                    </tr>
                    <tr>
                        <td nowrap><label>Period To</label><span class="mandatory">*</span></td><td><div id="dateToDiv"/></td>
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
