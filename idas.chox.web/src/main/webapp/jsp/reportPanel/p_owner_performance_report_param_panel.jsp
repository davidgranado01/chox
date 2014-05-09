<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var reportName = 'OwnerPerformanceReport-Excel';

    Ext.onReady(function(){
        var insurerId = <s:property value="userOrganisationId"/>;
        ui.dateField('startDate',getTodayDate(),'dateFromDiv');
        ui.dateField('endDate',getTodayDate(),'dateToDiv');

        var claimOwnerPerformanceReader = new Ext.data.JsonReader({
                   totalProperty: 'totalCount',
                   root: 'results',
                   fields:
                   [
                       {name:'id'},
                       {name:'name'}
                   ]
        });

        var claimOwnerPerformanceStore = new choxDataStore({
                  url : "/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", 
                  params : {"workgroupId":-1,"insurerId":insurerId},
                  reader : claimOwnerPerformanceReader,
                  listeners: {load: function() {
                                var  defaultName={'name':'--- All ---','id':-1}
                                this.insert(0, new Ext.data.Record(defaultName));
                }
            }
        });

        var claimOwnerPerformanceCombo = new Ext.form.ComboBox({
                            store : claimOwnerPerformanceStore,
                            width: 250,
                            renderTo: 'rptOwnerPerformanceOwnerSelectionHolder',
                            valueField : 'id',
                            displayField :'name',
                            hiddenName: 'ownerId',
                            typeAhead : true,
                            mode : 'local',
                            triggerAction: 'all',
                            emptyText : '--- All ---',
                            forceSelection : true,
                            listeners: { blur: function () {
                                            if(this.getRawValue() == "" ) {
                                                this.clearValue(); this.reset();
                                               }
                               }}
        });

        <s:if test="insurerIsWorkgroupEnabled">
            var ownerPerformanceWorkgroupJsonReader = new Ext.data.JsonReader({
                                totalProperty: 'totalCount',
                                root: 'results',
                                fields:
                                [
                                    {name:'text'},
                                    {name:'value'}
                                ]
            });

            var  ownerPerformanceWorkgroupStore = new choxDataStore({
                                url : "/prv/p/WorkgroupDropDownActionByInsurer2.action",
                                reader :  ownerPerformanceWorkgroupJsonReader,
                                listeners: {load: function() {
                                          var  defaultValue={'value':'--- All ---','text':-1}
                                          this.insert(0, new Ext.data.Record(defaultValue));
                }
            }
            });

            var  ownerPerformanceWorkgroupCombo = new Ext.form.ComboBox({
                                store:  ownerPerformanceWorkgroupStore,
                                renderTo: 'rptOwnerPerformanceWrkgroupSelectionHolder',
                                valueField: 'text',
                                hiddenName: 'workgroupId',
                                displayField:'value',
                                width: 250,
                                typeAhead: true,
//                                autoWidth: true,
                                mode: 'local',
                                triggerAction: 'all',
                                emptyText: '--- All ---',
                                forceSelection : true,
                                listeners: {select: function () {
                                                        var workgroupId = -1;
                                                        if (ownerPerformanceWorkgroupCombo.getValue() != null) {
                                                            workgroupId = ownerPerformanceWorkgroupCombo.getValue();
                                                        }
//                                                        var insurerId = $("#userInsurerId").val();
                                                        claimOwnerPerformanceCombo.reset();
                                                        claimOwnerPerformanceStore.removeAll();
                                                        claimOwnerPerformanceStore.load({ params : {"workgroupId":workgroupId,"insurerId":insurerId}});
                                                    },
                                            blur: function () {
                                                    if(this.getRawValue() == "" ) {
                                                        this.clearValue(); this.reset();
                                                        claimOwnerPerformanceCombo.reset();
                                                        claimOwnerPerformanceStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
                                                  }
                                }}
                        });
            ownerPerformanceWorkgroupStore.load();
        </s:if>
        claimOwnerPerformanceStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
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
    <legend>Workgroup and Claim Owner Performance Report</legend>
</s:if>
<s:else>
    <legend>Claim Owner Performance Report</legend>
</s:else>

    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report provides an insight into the performance of individual handlers in completing outstanding actions at all the various statuses that are the responsibility of the Insurer. The report also displays the average invoice payment time for each handler. The dates below determine the  actions processed during the selected period as well as the invoices that have been paid in order to determine the average invoice payment time.</div>

                <table class="report-form">

                    <s:if test="insurerIsWorkgroupEnabled">
                        <tr>
                            <td nowrap><label>Workgroup</label></td>
                            <td>
                                <div id="rptOwnerPerformanceWrkgroupSelectionHolder"></div>
                            </td>
                        </tr>
                    </s:if>
                    <s:else>
                        <input type="hidden" id="workgroupId" name="workgroupId" value="-1"/>
                    </s:else>
                        <tr>
                            <td nowrap><label>Claim Owner</label></td>
                            <td>
                                <div id="rptOwnerPerformanceOwnerSelectionHolder"></div>
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
                    <button type="button" id="OPRPPGenerateReportId"onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div></form>
</fieldset>
