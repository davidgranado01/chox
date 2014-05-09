<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'OverviewSummary-Excel';
    
    Ext.onReady(function(){
        ui.dateField('DateStart',getTodayDate(),'dateFromDiv');
        ui.dateField('DateEnd',getTodayDate(),'dateToDiv');
        var insurerId = <s:property value="userOrganisationId"/>;

        $("form#formReportParam").validate(
        {
            errorLabelContainer: "#formReportParamMessageBox",
            rules: {
                DateStart:{
                	max:function(){
                        var sd = Ext.get('DateStart').getValue().split("/");
                        var ed = Ext.get('DateEnd').getValue().split("/");
                        var time = new Date(sd[2],sd[1] - 1 ,sd[0]).getTime() - new Date(ed[2],ed[1] - 1 ,ed[0]).getTime();
                        if(time > 0)
                            return true;
                    },
                    required:true,
                    dateITA: true
                },
                DateEnd:{
                    required:true,
                    dateITA: true
                }
            },
            messages: {
                DateStart: {
                	max:"'Date to' can't be before 'Date From'",
                    required:"A value must be supplied for 'Claim Uploaded Date From'",
                    dateITA:"You must supply a date value 'Claim Uploaded Date From'"
                },
                DateEnd: {
                    required:"A value must be supplied for 'Claim Uploaded Date To'",
                    dateITA:"You must supply a date value 'Claim Uploaded Date To'"
                }
            }
        });
        
        
        <s:if test="isInsurer" >

            var overviewSumRepClaimOwnerReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'id'},
                    {name:'name'}
                ]
            });

            var overviewSumRepClaimOwnerStore = new choxDataStore({
                url : "/prv/p/SearchClaimHandlerRoleUserDropDownAction.action",
                params : {"workgroupId":-1,"insurerId":insurerId},
                reader : overviewSumRepClaimOwnerReader,
                listeners: {load: function() {
                        var  defaultName={'name':'--- ALL ---','id':-1}
                        this.insert(0, new Ext.data.Record(defaultName));
                    }
                }
                            
            });

            var overviewSumRepClaimOwnerCombo = new Ext.form.ComboBox({
                store : overviewSumRepClaimOwnerStore,
                width: 250,
                renderTo: 'rptOverviewSumOwnerSelectionHolder',
                valueField : 'id',
                displayField :'name',
                hiddenName: 'ownerId',
                emptyText: '--- All ---',
                emptyValue: '-1',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                forceSelection : true,
                listeners: { blur: function () {
                        if(this.getRawValue() == "" ) {
                            this.clearValue(); this.reset();
                        }
                    }
                }
            });

        <s:if test="insurerIsWorkgroupEnabled">
                var overviewSumRepWorkgroupJsonReader = new Ext.data.JsonReader({
                    totalProperty: 'totalCount',
                    root: 'results',
                    fields:
                        [
                        {name:'text'},
                        {name:'value'}
                    ]
                });

                var  overviewSumRepWorkgroupStore = new choxDataStore({
                    url : "/prv/p/WorkgroupDropDownActionByInsurer2.action",
                    reader :  overviewSumRepWorkgroupJsonReader,
                    listeners: {load: function() {
                            var  defaultValue={'value':'--- ALL ---','text':-1}
                            this.insert(0, new Ext.data.Record(defaultValue));
                        }
                    }
                });

                var  overviewSumRepWorkgroupCombo = new Ext.form.ComboBox({
                    store:  overviewSumRepWorkgroupStore,
                    renderTo: 'rptOverviewSumWrkgroupSelectionHolder',
                    valueField: 'text',
                    id: 'overviewSumRepWorkgroupComboId',
                    hiddenName: 'workgroupId',
                    displayField:'value',
                    width: 250,
                    emptyText: '--- All ---',
                    emptyValue: '-1',
                    typeAhead: true,
                    mode: 'local',
                    triggerAction : 'all',
                    forceSelection : true,
                    listeners: {select: function () {
                            var workgroupId = -1;
                            if (overviewSumRepWorkgroupCombo.getValue() != null && overviewSumRepWorkgroupCombo.getValue() != '--- ALL ---' && overviewSumRepWorkgroupCombo.getValue() != "") {
                                workgroupId = overviewSumRepWorkgroupCombo.getValue();
                            }
                            //                                                        var insurerId = $("#userInsurerId").val();
                            overviewSumRepClaimOwnerCombo.reset();
                            overviewSumRepClaimOwnerStore.removeAll();
                            overviewSumRepClaimOwnerStore.load({ params : {"workgroupId":workgroupId,"insurerId":insurerId}});
                        },
                        blur: function () {
                            if(this.getRawValue() == "" ) {
                                this.clearValue(); this.reset();
                                overviewSumRepClaimOwnerCombo.reset();
                                overviewSumRepClaimOwnerStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
                            }
                        }
                    }
                });
                overviewSumRepWorkgroupStore.load();
        </s:if>
                overviewSumRepClaimOwnerStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});

    </s:if>

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
    <legend>Claim Overview Summary Report</legend>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">
        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">
                    <s:if test="isCHO">
                        This report shows a high level summary of claims across all Insurers and per Insurer. Displaying information such as average invoice values, average hire durations and average cycle times.
                    </s:if>
                    <s:elseif test="insurerUploadEnabled">
                        This report shows a high level summary of claims across all CHOs and per CHO. Displaying information such as average invoice values, average hire durations and average cycle times.<br /><br />
                        Please note CHOs setup for Insurer uploaded claims and invoices are excluded from this report.
                    </s:elseif>
                    <s:else>
                        This report shows a high level summary of claims across all CHOs and per CHO. Displaying information such as average invoice values, average hire durations and average cycle times.
                    </s:else>
                </div>

                <table class="report-form">
                    <s:if test="!isCHO">
                        <s:if test="insurerIsWorkgroupEnabled">
                            <tr>
                                <td nowrap><label>Workgroup</label></td>
                                <td>
                                    <div id="rptOverviewSumWrkgroupSelectionHolder"></div>
                                </td>
                            </tr>
                        </s:if>
                        <tr>
                            <td nowrap><label>Claim Owner</label></td>
                            <td>
                                <div id="rptOverviewSumOwnerSelectionHolder"></div>
                            </td>
                        </tr>
                    </s:if>
                    <tr>
                        <td nowrap width="30%">
                            <label>Claim Uploaded Date From</label><span class="mandatory">*</span>
                        </td>
                        <td><div id="dateFromDiv"></div></td>
                    </tr>    
                    <tr>
                        <td nowrap>
                            <label>Claim Uploaded Date To</label><span class="mandatory">*</span>
                        </td>
                        <td><div id="dateToDiv"></div></td>                            
                    </tr>                      
                </table>

                <div class="chox-report-button">
                    <button type="button" id="OSRPPGenerateReportId" onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div>
    </form>
</fieldset>