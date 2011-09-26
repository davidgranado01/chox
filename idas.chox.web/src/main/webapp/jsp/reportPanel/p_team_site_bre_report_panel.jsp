<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var reportName = 'TeamSiteBreInvoiceReport-Excel';
    //TeamSiteBreWorkflowReport

    Ext.onReady(function(){
        var insurerId = <s:property value="userOrganisationId"/>;
        ui.dateField('startDate',getTodayDate(),'dateFromDiv');
        ui.dateField('endDate',getTodayDate(),'dateToDiv');

        
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
            reader : teamWorkflowReader,
            listeners: {load: function() {

                    var  defaultTeam={'team':'--- All ---'}
                                          this.insert(0, new Ext.data.Record(defaultTeam));
                }
            }
        });

        var teamWorkflowCombo = new Ext.form.ComboBox({
            store : teamWorkflowStore,
            width: 250,
            renderTo: 'rptBreTeamSelectionHolder',
            //                            valueField : 'team',
            displayField :'team',
            hiddenName: 'team',
            typeAhead : true,
            mode : 'local',
            triggerAction : 'all',
            forceSelection : true,
            listeners: { blur: function () {
                    if(this.getRawValue() == "" ) {
                        this.clearValue(); this.reset();
                    }
                },
                afterrender : function(){
                    this.setValue('--- All ---');
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
            reader :  siteJsonReader,
            listeners: {load: function() {
                                          var defaultSite = {'site':'--- All ---'};
                                          this.insert(0, new Ext.data.Record(defaultSite));
                }
            }
        });

        var  siteWorkflowCombo = new Ext.form.ComboBox({
            store:  siteWorkflowStore,
            renderTo: 'rptBreSiteSelectionHolder',
            //                                valueField: 'site',
            hiddenName: 'site',
            displayField:'site',
            width: 250,
            typeAhead: true,
            triggerAction : 'all',
            mode: 'local',
            forceSelection : true,
            listeners: {select: function () { 
                    var site = '';
                    if (siteWorkflowCombo.getValue() != null && siteWorkflowCombo.getValue() != '--- All ---' && siteWorkflowCombo.getValue() != "") {
                        site = siteWorkflowCombo.getValue();
                    }
                    //                                                        var insurerId = $("#userInsurerId").val();
                    teamWorkflowCombo.reset();
                    teamWorkflowStore.removeAll();
                    teamWorkflowCombo.setValue('--- All ---');
                    teamWorkflowStore.load({ params : {"site":site,"insurerId":insurerId}});
                },
                blur: function () {
                    if(this.getRawValue() == "" ) {
                        this.clearValue(); this.reset();
                        teamWorkflowCombo.reset();
                        teamWorkflowStore.load({ params : {"site":'',"insurerId":insurerId}});
                    }
                },
                afterrender : function(){
                    this.setValue('--- All ---');
                }
            }
        });
        siteWorkflowStore.load();

        var suppliersJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'text'},
                {name:'value'}
            ]
        });

        var mysuppliers = Ext.util.JSON.decode('<s:property value="suppliersJsonString" escape="false"/>');
        var suppliersStore = new Ext.data.Store({
            data : mysuppliers,
            reader : suppliersJsonReader,
            listeners: {load: function() {
                                          var defaultSupplier = {'value':'--- All ---'};
                                          this.insert(0, new Ext.data.Record(defaultSupplier));
                }
            }
        });

        var supplierCombo = new Ext.form.ComboBox({
            store : suppliersStore,
            id : 'rptBreSupplierDropDownId',
            renderTo: 'rptBreSupplierDropDownDivSelectionHolder',
            width: 250,
            valueField : 'text',
            hiddenName: 'supplierId',
            displayField :'value',
            typeAhead : true,
            mode : 'local',
            triggerAction : 'all',
            selectOnFocus : false,
            allowBlank : true,
            forceSelection : true,
            listeners: { blur: function () {
                    if(this.getRawValue() == "" ) {
                        this.clearValue();
                    }
                },
                afterrender : function(){
                    this.setValue('--- All ---');
                }
            }
        });

        
        
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
        var queryString = $('#formReportParam').formSerialize();
        generateReport(queryString);
    }

</script>
<fieldset class="x-fieldset">
    <legend>Site and Team BRE Invoice Approval Dispute Report</legend>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report provides an insight into the number of invoices that pass the business rules and the resulting action taken, looking at whether the invoices are being paid or disputed, broken down by site and team.  This report also provides a breakdown of why invoices are being disputed after being approved by the business rules.  The report will look at all invoices that were uploaded during the selected period from and to dates.</div>

                <table class="report-form">
                    <tr>
                        <td nowrap><label>Credit Hire Organisation</label></td>
                        <td>
                            <div id="rptBreSupplierDropDownDivSelectionHolder"></div>
                        </td>
                    </tr>
                    <tr>
                        <td nowrap><label>Site</label></td>
                        <td>
                            <div id="rptBreSiteSelectionHolder"></div>
                        </td>
                    </tr>
                    <tr>
                        <td nowrap><label>Team</label></td>
                        <td>
                            <div id="rptBreTeamSelectionHolder"></div>
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
                    <button type="button" id="TSBRPGenerateReportId"onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div></form>
</fieldset>
