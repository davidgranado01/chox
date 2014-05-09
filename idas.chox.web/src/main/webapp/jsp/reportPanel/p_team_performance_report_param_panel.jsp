

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

        var teamPerformanceStore = new choxDataStore({
            url : "/prv/p/TeamDropDownActionByInsurer.action", 
            params : {"insurerId":insurerId, "site":''},
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
            forceSelection : true,
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

        var  sitePerformanceStore = new choxDataStore({
            url : "/prv/p/SiteDropDownActionByInsurer.action",
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
            forceSelection : true,
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
//	        var queryString = $('#formReportParam').formSerialize();
                var queryString = {};
                $.each($('#formReportParam').serializeArray(), function() {queryString[this.name] = this.value;});
	        generateReport(queryString);
    	}
    }

</script>
<fieldset class="x-fieldset">
    <legend>Site and Team Performance Report</legend>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report provides an insight into the performance of individual sites and teams in completing outstanding actions at all the various statuses that are the responsibility of the Insurer. The report also displays the average invoice payment time for each team. The dates below determine the  actions processed during the selected period as well as the invoices that have been paid in order to determine the average invoice payment time.</div>

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
                </table>

                <div class="chox-report-button">
                    <button type="button" id="TPRPPGenerateReportId"onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div></form>
</fieldset>
