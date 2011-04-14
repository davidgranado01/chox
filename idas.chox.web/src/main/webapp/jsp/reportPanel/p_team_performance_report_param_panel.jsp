

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var reportName = 'TeamPerformanceReport-Excel';

    Ext.onReady(function(){
        var insurerId = <s:property value="userOrganisationId"/>;
        ui.dateField('startDate',getTodayDate(),'dateFromDiv');
        ui.dateField('endDate',getTodayDate(),'dateToDiv');

        var teamPerformanceReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'team'}
            ]
         
        });

        var teamPerformanceStore = new Ext.data.Store({
            proxy : new Ext.data.HttpProxy
            ({url : "<%= request.getContextPath()%>/prv/p/TeamDropDownActionByInsurer.action", method:'GET', params : {"insurerId":insurerId, "site":''}}),
            reader : teamPerformanceReader,
            listeners: {load: function() {

                                          var  defaultTeam={'team':'--- All ---'}
                                          this.insert(0, new Ext.data.Record(defaultTeam));
                }
            }
        });

        var teamPerformanceCombo = new Ext.form.ComboBox({
            store : teamPerformanceStore,
            width: 250,
            renderTo: 'rptTeamSelectionHolder',
            //                            valueField : 'team',
            id : 'teamComboId',
            displayField :'team',
            hiddenName: 'team',
            typeAhead : true,
            editable:true,
            allowBlank: true,
            autoSelect: false,
            lazyInit: false,
            triggerAction: 'all',
            mode : 'local',
            emptyText : '--- All ---',
            listeners: { blur: function () {
                    if(this.getRawValue() == "" ) {
                        this.clearValue(); this.reset();
                    }
                }}
        });
        teamPerformanceStore.load();

        var siteJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'site'}
            ]
        });

        var  sitePerformanceStore = new Ext.data.Store({
            proxy : new Ext.data.HttpProxy
            ({url : "<%= request.getContextPath()%>/prv/p/SiteDropDownActionByInsurer.action", method:'GET'}),
            reader :  siteJsonReader,
            listeners: {load: function() {
                                          var defaultSite = {'site':'--- All ---'};
                                          this.insert(0, new Ext.data.Record(defaultSite));
                }
            }
        });

        var  sitePerformanceCombo = new Ext.form.ComboBox({
            store:  sitePerformanceStore,
            renderTo: 'rptSiteSelectionHolder',
            //                                valueField: 'site',
            id: 'siteComboId',
            hiddenName: 'site',
            displayField:'site',
            width: 250,
            editable:true,
            allowBlank: true,
            autoSelect: false,
            lazyInit: false,
            triggerAction: 'all',
            //typeAhead: true,
            mode: 'local',
            emptyText: '--- All ---',
            listeners: {select: function () {
                    var site = '';
                    if (sitePerformanceCombo.getValue() != null && sitePerformanceCombo.getValue() != '--- All ---') {
                        site = sitePerformanceCombo.getValue();
                    }
                    //                                                        var insurerId = $("#userInsurerId").val();
                    teamPerformanceCombo.reset();
                    teamPerformanceStore.removeAll();
                    teamPerformanceStore.load({ params : {"site":site,"insurerId":insurerId}});
                },
                blur: function () {
                    if(this.getRawValue() == "" ) {
                        this.clearValue(); this.reset();
                        teamPerformanceCombo.reset();
                        teamPerformanceStore.load({ params : {"site":'',"insurerId":insurerId}});
                    }
                }}
        });
        sitePerformanceStore.load();

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
    <legend>Site and Team Performance Report</legend>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report provides an insight into the performance of individual sites and teams in completing outstanding tasks at all the various statuses that are the responsibility of the Insurer. The report also displays the average invoice payment time for each team. The dates below determine the  tasks processed during the selected period as well as the invoices that have been paid in order to determine the average invoice payment time.</div>

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
                </table>

                <div class="chox-report-button">
                    <button type="button" id="TPRPPGenerateReportId"onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div></form>
</fieldset>
