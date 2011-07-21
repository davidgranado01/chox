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

        var claimOwnerPerformanceStore = new Ext.data.Store({
                            proxy : new Ext.data.HttpProxy
                            ({url : "<%= request.getContextPath()%>/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", method:'GET', params : {"workgroupId":-1,"insurerId":insurerId}}),
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

            var  ownerPerformanceWorkgroupStore = new Ext.data.Store({
                                proxy : new Ext.data.HttpProxy
                                    ({url : "<%= request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer2.action", method:'GET'}),
                                reader :  ownerPerformanceWorkgroupJsonReader,
                                listeners: {load: function() {

                                          var  defaultValue={'value':'--- All ---','id':1}
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
    <legend>Claim Owner Performance Report</legend>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report provides an insight into the performance of individual handlers in completing outstanding tasks at all the various statuses that are the responsibility of the Insurer. The report also displays the average invoice payment time for each handler. The dates below determine the  tasks processed during the selected period as well as the invoices that have been paid in order to determine the average invoice payment time.</div>

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
                        <td nowrap width="30%"><label>Period From</label></td><td><div id="dateFromDiv" /></td>
                    </tr>
                    <tr>
                        <td nowrap><label>Period To</label></td><td><div id="dateToDiv"/></td>
                    </tr>

                </table>

                <div class="chox-report-button">
                    <button type="button" id="OPRPPGenerateReportId"onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div></form>
</fieldset>
