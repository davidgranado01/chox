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

        var teamWorkflowReader = new Ext.data.JsonReader({
                            totalProperty: 'totalCount',
                            root: 'results',
                            fields:
                            [
                                {name:'team'}
                            ]
        });

        var teamWorkflowStore = new Ext.data.Store({
                            proxy : new Ext.data.HttpProxy
                            ({url : "<%= request.getContextPath()%>/prv/p/TeamDropDownActionByInsurer.action", method:'GET', params : {"insurerId":insurerId, "site":''}}),
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

        var  siteWorkflowStore = new Ext.data.Store({
                                proxy : new Ext.data.HttpProxy
                                    ({url : "<%= request.getContextPath()%>/prv/p/SiteDropDownActionByInsurer.action", method:'GET'}),
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
            var queryString = $('#formReportParam').formSerialize();
            window.location= "<%=request.getContextPath()%>/prv/p/exportExcelReport.action?reportName=" + reportName + "&" + queryString;
    }
   
</script>
<fieldset class="x-fieldset">
    <legend>Site and Team Workflow Report</legend>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report provides an insight into claim processing efficiency and resource scheduling for a particular Site(s) or Team(s), depending on the selection made.The period from and to dates below determine the number of tasks processed during the selected period and all the outstanding task columns are based on outstanding work as at the date selected in the 'Period To' field. Please note that this report looks at outstanding tasks as at midnight yesterday.</div>

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
                    <button type="button" id="TWRPPGenerateReportId" onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div></form>
</fieldset>
