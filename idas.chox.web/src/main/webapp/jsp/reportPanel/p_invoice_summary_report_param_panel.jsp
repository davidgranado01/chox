<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'InvoiceSummaryReport-Excel';
    
    Ext.onReady(function(){
        var insurerId = <s:property value="userOrganisationId"/>;
        ui.unvalidatedDateField('DateStart',getTodayDate(),'dateFromDiv');
        ui.unvalidatedDateField('DateEnd',getTodayDate(),'dateToDiv');

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
                	max:"'Invoice Uploaded To' can't be before 'Invoice Uploaded From'",
                    required:"A value must be supplied for 'Invoice Uploaded From'",
                    dateITA:"You must supply valid date format for 'Invoice Uploaded From'"
                },
                DateEnd: {
                    required:"A value must be supplied for 'Invoice Uploaded To'",
                    dateITA:"You must supply valid date format for 'Invoice Uploaded To'"
                }
            }
        });


    <s:if test="isCHO" >


            var insurersJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'text'},
                    {name:'value'}
                ]
            });

            var myinsurers = Ext.util.JSON.decode('<s:property value="insurersJsonString" escape="false"/>');
            var insurersStore = new Ext.data.Store({
                data : myinsurers,
                reader : insurersJsonReader
            });


            var insurerCombo = new Ext.form.ComboBox({
                store : insurersStore,
                id : 'ISRPPInsurerComboId',
                renderTo: 'invoiceSummeryReportInsurerDropDownDiv',
                width: 220,
                valueField : 'text',
                hiddenName: 'insurerId',
                displayField :'value',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                emptyText : '--- ALL ---',
                selectOnFocus : false,
                forceSelection : true,
                allowBlank : true,
                listeners: { blur: function () {
                        if(this.getRawValue() == "" ) {
                            this.clearValue();
                        }
                    }
                }
            });


    </s:if>

    <s:if test="isInsurer" >

            var invSumRepClaimOwnerReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'id'},
                    {name:'name'}
                ]
            });

            var invSumRepClaimOwnerStore = new choxDataStore({
                url : "/prv/p/SearchClaimHandlerRoleUserDropDownAction.action",
                params : {"workgroupId":-1,"insurerId":insurerId},
                reader : invSumRepClaimOwnerReader,
                listeners: {load: function() {
                        var  defaultName={'name':'--- ALL ---','id':-1}
                        this.insert(0, new Ext.data.Record(defaultName));
                    }
                }
                            
            });

            var invSumRepClaimOwnerCombo = new Ext.form.ComboBox({
                store : invSumRepClaimOwnerStore,
                width: 250,
                renderTo: 'rptInvSumOwnerSelectionHolder',
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
                var invSumRepWorkgroupJsonReader = new Ext.data.JsonReader({
                    totalProperty: 'totalCount',
                    root: 'results',
                    fields:
                        [
                        {name:'text'},
                        {name:'value'}
                    ]
                });

                var  invSumRepWorkgroupStore = new choxDataStore({
                    url : "/prv/p/WorkgroupDropDownActionByInsurer2.action",
                    reader :  invSumRepWorkgroupJsonReader,
                    listeners: {load: function() {
                            var  defaultValue={'value':'--- ALL ---','text':-1}
                            this.insert(0, new Ext.data.Record(defaultValue));
                        }
                    }
                });

                var  invSumRepWorkgroupCombo = new Ext.form.ComboBox({
                    store:  invSumRepWorkgroupStore,
                    renderTo: 'rptInvSumWrkgroupSelectionHolder',
                    valueField: 'text',
                    id: 'invSumRepWorkgroupComboId',
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
                            if (invSumRepWorkgroupCombo.getValue() != null && invSumRepWorkgroupCombo.getValue() != '--- ALL ---' && invSumRepWorkgroupCombo.getValue() != "") {
                                workgroupId = invSumRepWorkgroupCombo.getValue();
                            }
                            invSumRepClaimOwnerCombo.reset();
//                             invSumRepClaimOwnerCombo.setValue('--- ALL ---');
                            invSumRepClaimOwnerStore.removeAll();
                            invSumRepClaimOwnerStore.load({ params : {"workgroupId":workgroupId,"insurerId":insurerId}});
                        },
                        blur: function () {
                            if(this.getRawValue() == "" ) {
                                this.clearValue(); this.reset();
                                invSumRepClaimOwnerCombo.reset();
                                invSumRepClaimOwnerStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
                            }
                        }
                    }
                });
                invSumRepWorkgroupStore.load();
        </s:if>
                invSumRepClaimOwnerStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});

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
                            var  defaultValue={'value':'--- ALL ---','text':-1}
                            this.insert(0, new Ext.data.Record(defaultValue));
                        }
                    }
                });

                var supplierCombo = new Ext.form.ComboBox({
                    store : suppliersStore,
                    id : 'ISRPPSupplierCombo',
                    renderTo: 'invoiceSummeryReportSupplierDropDownDiv',
                    width: 250,
                    valueField : 'text',
                    hiddenName: 'supplierId',
                    displayField :'value',
                    typeAhead : true,
                    mode : 'local',
                    triggerAction : 'all',
                    emptyText: '--- All ---',
                    emptyValue: '-1',
                    selectOnFocus : false,
                    allowBlank : true,
                    forceSelection : true,
                    listeners: { blur: function () {
                            if(this.getRawValue() == "" ) {
                                this.clearValue();
                                this.reset();
                            }
                        }
                    }
                });

    </s:if>


        });
    
        function openReport()
        {
            if($("form#formReportParam").valid()){
//                var queryString = $('#formReportParam').formSerialize();
                var queryString = {};
                $.each($('#formReportParam').serializeArray(), function() {queryString[this.name] = this.value;});
                generateReport(queryString);
            }
        }
    
    
</script>

<fieldset class="x-fieldset">

    <legend>Invoice Summary Report</legend>

    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">

        <div class="x-panel-bwrap chox-form-container">

            <div class="form-container">

                <div class="instruction-message">
                    This report provides information at a high level regarding the financials of CHOX invoices, including details relating to penalty charges as a result of late payments. The dates that require selection below refer to the date the invoice was uploaded onto CHOX.
                </div>

                <table class="report-form">
                    <s:if test="!isCHO">
                        <s:if test="insurerIsWorkgroupEnabled">
                            <tr>
                                <td nowrap><label>Workgroup</label></td>
                                <td>
                                    <div id="rptInvSumWrkgroupSelectionHolder"></div>
                                </td>
                            </tr>
                        </s:if>
                        <tr>
                            <td nowrap><label>Claim Owner</label></td>
                            <td>
                                <div id="rptInvSumOwnerSelectionHolder"></div>
                            </td>
                        </tr>
                        <tr>
                            <td nowrap><label>Credit Hire Organisation</label></td>

                            <td>
                                <div id="invoiceSummeryReportSupplierDropDownDiv"></div>
                            </td>
                        </tr>
                    </s:if>
                    <s:else>
                        <tr>
                            <td nowrap><label>Insurer</label></td>
                            <td>
                                <div id="invoiceSummeryReportInsurerDropDownDiv"></div>
                            </td>
                        </tr>
                    </s:else>
                    <tr>
                        <td nowrap width="30%"><label>Invoice Uploaded From</label><span class="mandatory">*</span></td>
                        <td><div id="dateFromDiv" /></td>                       
                    </tr>    
                    <tr>
                        <td nowrap><label>Invoice Uploaded To</label><span class="mandatory">*</span></td>
                        <td><div id="dateToDiv"/></td>                            
                    </tr>                      
                </table>

                <div class="chox-report-button">
                    <button type="button" id="ISRPPGenerateReportButtonId"onclick="javascript:openReport();">Generate Report</button>
                </div>
            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div>
    </form>
</fieldset>