<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'TeamWorkflowReport-Excel';

    Ext.onReady(function(){
        new Ext.ToolTip({ target: 'help-service-commencing-icon', html: 'This refers to the Monday that you wish to select as the initial start date in order to calculate the ‘Time In Service’ and ‘Weeks In Service’ columns for this report.'});
        var insurerId = <s:property value="userOrganisationId"/>;
        ui.dateField('startDate',getTodayDate(),'dateFromDiv');
        ui.dateField('endDate',getTodayDate(),'dateToDiv');

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
            showWeekNumber: true
        });

        var teamWorkflowReader = new Ext.data.JsonReader({
                            totalProperty: 'totalCount',
                            root: 'results',
                            fields:
                            [
                                {name:'team'}
                            ]
        });

        var teamWorkflowStore = new choxDataStore({
                            url : "/prv/p/TeamDropDownActionByInsurer.action", 
                            params : {"insurerId":insurerId, "site":''},
                            reader : teamWorkflowReader
        });

        var teamWorkflowCombo = new Ext.form.ComboBox({
                            store : teamWorkflowStore,
                            width: 250,
                            renderTo: 'rptTeamSelectionHolder',
//                            valueField : 'team',
                            id : 'teamComboId',
                            displayField :'team',
                            hiddenName: 'team',
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
        teamWorkflowStore.load();

        var siteJsonReader = new Ext.data.JsonReader({
                                totalProperty: 'totalCount',
                                root: 'results',
                                fields:
                                [
                                    {name:'site'}
                                ]
        });

        var  siteWorkflowStore = new choxDataStore({
                                url : "/prv/p/SiteDropDownActionByInsurer.action",
                                reader :  siteJsonReader
        });

        var  siteWorkflowCombo = new Ext.form.ComboBox({
                                store:  siteWorkflowStore,
                                renderTo: 'rptSiteSelectionHolder',
//                                valueField: 'site',
                                id: 'siteComboId',
                                hiddenName: 'site',
                                displayField:'site',
                                width: 250,
                                typeAhead: true,
                                mode: 'local',
                                emptyText: '--- All ---',
                                forceSelection : true,
                                listeners: {select: function () {
                                                        var site = '';
                                                        if (siteWorkflowCombo.getValue() != null && siteWorkflowCombo.getValue() != '--- All ---') {
                                                            site = siteWorkflowCombo.getValue();
                                                        }
//                                                        var insurerId = $("#userInsurerId").val();
                                                        teamWorkflowCombo.reset();
                                                        teamWorkflowStore.removeAll();
                                                        teamWorkflowStore.load({ params : {"site":site,"insurerId":insurerId}});
                                                    },
                                            blur: function () {
                                                    if(this.getRawValue() == "" ) {
                                                        this.clearValue(); this.reset();
                                                        teamWorkflowCombo.reset();
                                                        teamWorkflowStore.load({ params : {"site":'',"insurerId":insurerId}});
                                                  }
                                }}
        });
        siteWorkflowStore.load();

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
                    dateITA: "You must supply a date value 'Service Commencing'"
                },
                startDate: {
                	max:"'Period to' can't be before 'Period From'",
                    required:"A value must be supplied for 'Period From'",
                    dateITA:"You must supply a date value 'Period From'"
                },
                endDate: {
                    required:"A value must be supplied for 'Period To'",
                    dateITA:"You must supply a date value 'Period To'"
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
    <legend>Site and Team Workflow Report</legend>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report provides an insight into claim processing efficiency and resource scheduling for a particular Site(s) or Team(s), depending on the selection made.The period from and to dates below determine the number of actions processed during the selected period and all the outstanding action columns are based on outstanding work as at the date selected in the 'Period To' field. Please note that this report looks at outstanding actions as at midnight yesterday.</div>

                <table class="report-form">

                        <tr>
                            <td nowrap><label>Site</label></td>
                            <td>
                                <div id="rptSiteSelectionHolder"></div>
                            </td>
                        </tr>
                        <tr>
                            <td nowrap><label>Team</label></td>
                            <td>
                                <div id="rptTeamSelectionHolder"></div>
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
                    <button type="button" id="TWRPPGenerateReportId" onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div></form>
</fieldset>
