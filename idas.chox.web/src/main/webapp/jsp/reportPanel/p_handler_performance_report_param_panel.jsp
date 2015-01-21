<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var reportName = 'HandlerPerformanceReport-Excel';

    Ext.onReady(function(){
        var insurerId = <s:property value="userOrganisationId"/>;
        ui.unvalidatedDateField('startDate',getTodayDate(),'dateFromDiv');
        ui.unvalidatedDateField('endDate',getTodayDate(),'dateToDiv');

        
        
        var claimHandlerPerformanceReader = new Ext.data.JsonReader({
                            totalProperty: 'totalCount',
                            root: 'results',
                            fields:
                            [
                                {name:'id'},
                                {name:'name'}
                            ]
        });

        var claimHandlerPerformanceStore = new choxDataStore({
                            url : "/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", 
                            params : {"workgroupId":-1,"insurerId":insurerId},
                            reader : claimHandlerPerformanceReader,
                            listeners: {load: function() {

                                          var  defaultName={'name':'--- All ---','id':-1};
                                          this.insert(0, new Ext.data.Record(defaultName));
                }
            }
        });

        var claimHandlerPerformanceCombo = new Ext.form.ComboBox({
                            store : claimHandlerPerformanceStore,
                            width: 250,
                            renderTo: 'rptHandlerPerformanceOwnerSelectionHolder',
                            valueField : 'id',
                            displayField :'name',
                            hiddenName: 'ownerId',
                            typeAhead : true,
                            mode : 'local',
                            triggerAction: 'all',
                            emptyText : '--- All ---',
                            forceSelection : true,
                            listeners: { blur: function () {
                                            if(this.getRawValue() === "" ) {
                                                this.clearValue(); this.reset();
                                               }
                               }}
        });

        
        claimHandlerPerformanceStore.load({ params : {"workgroupId":-1, "insurerId":insurerId}});
        $("form#formHandlerPerformanceReportParam").validate(
        {
            errorLabelContainer: "#formHandlerPerformanceReportParamMessageBox",
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
        if($("form#formHandlerPerformanceReportParam").valid()){
//            var queryString = $('#formHandlerPerformanceReportParam').formSerialize();
            var queryString = {};
            $.each($('#formHandlerPerformanceReportParam').serializeArray(), function() {queryString[this.name] = this.value;});
            generateReport(queryString);
        }
    }

</script>
<fieldset class="x-fieldset">
    <legend>Handler Performance Report</legend>
    <form id="formHandlerPerformanceReportParam" class="XXentity-form" name="formHandlerPerformanceReportParam" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report provides an insight into the performance of individual handlers in completing outstanding actions at all the various statuses that are the responsibility of the Insurer.  This report looks at the handler who processed the action and not necessarily the owner of the claim. The report also displays the average invoice payment time for each handler. The dates below determine the actions processed during the selected period as well as the invoices that have been paid in order to determine the average invoice payment time.</div>

                <table class="report-form">

                    <tr>
                        <td nowrap><label>Claim Handler</label></td>
                        <td>
                            <div id="rptHandlerPerformanceOwnerSelectionHolder"></div>
                        </td>
                    </tr>

                    <tr>
                        <td nowrap width="30%"><label>Period From</label><span class="mandatory">*</span></td><td><div id="dateFromDiv" /></td>
                    </tr>
                    <tr>
                        <td nowrap><label>Period To</label><span class="mandatory">*</span></td><td><div id="dateToDiv"/></td>
                    </tr>
                    <input type="hidden" id="workgroupId" name="workgroupId" value="-1"/>
                </table>

                <div class="chox-report-button">
                    <button type="button" id="HPRPPGenerateReportId"onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formHandlerPerformanceReportParamMessageBox" class="action-error-msg"></div>
        </div>
      </form>
</fieldset>
