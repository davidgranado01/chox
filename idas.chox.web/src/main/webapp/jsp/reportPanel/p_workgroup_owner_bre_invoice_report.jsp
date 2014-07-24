<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var reportName = 'WorkgroupOwnerBreInvoiceReport-Excel';

    Ext.onReady(function(){
        var insurerId = <s:property value="userOrganisationId"/>;
        ui.unvalidatedDateField('startDate',getTodayDate(),'dateFromDiv');
        ui.unvalidatedDateField('endDate',getTodayDate(),'dateToDiv');

        
        var claimOwnerWorkflowReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'name'}
            ]
        });

        var claimOwnerWorkflowStore = new choxDataStore({
            url : "/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", 
            params : {"workgroupId":-1,"insurerId":insurerId},
            reader : claimOwnerWorkflowReader,
            listeners: {load: function() {

                    var  defaultName={'name':'--- All ---','id':-1}
                                          this.insert(0, new Ext.data.Record(defaultName));
                }
            }
                            
        });

        var claimOwnerWorkflowCombo = new Ext.form.ComboBox({
            store : claimOwnerWorkflowStore,
            width: 250,
            renderTo: 'rptBreOwnerWorkflowOwnerSelectionHolder',
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
            var ownerWorkflowWorkgroupJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'text'},
                    {name:'value'}
                ]
            });

            var  ownerWorkflowWorkgroupStore = new choxDataStore({
                url : "/prv/p/WorkgroupDropDownActionByInsurer2.action",
                reader :  ownerWorkflowWorkgroupJsonReader,
                listeners: {load: function() {

                        var  defaultValue={'value':'--- All ---','text':-1}
                                              this.insert(0, new Ext.data.Record(defaultValue));
                    }
                }
            });

            var  ownerWorkflowWorkgroupCombo = new Ext.form.ComboBox({
                store:  ownerWorkflowWorkgroupStore,
                renderTo: 'rptBreOwnerWorkflowWrkgroupSelectionHolder',
                valueField: 'text',
                id: 'ownerWorkflowWorkgroupComboId',
                hiddenName: 'workgroupId',
                displayField:'value',
                width: 250,
                typeAhead: true,
                emptyText: '--- All ---',
                emptyValue: '-1',
                mode: 'local',
                triggerAction : 'all',
                forceSelection : true,
                listeners: {select: function () {
                        var workgroupId = -1;
                        if (ownerWorkflowWorkgroupCombo.getValue() != null && ownerWorkflowWorkgroupCombo.getValue() != '--- All ---' && ownerWorkflowWorkgroupCombo.getValue() != "") {
                            workgroupId = ownerWorkflowWorkgroupCombo.getValue();
                        }
                        //                                                        var insurerId = $("#userInsurerId").val();
                        claimOwnerWorkflowCombo.reset();
                        claimOwnerWorkflowCombo.setValue('--- All ---');
                        claimOwnerWorkflowStore.removeAll();
                        claimOwnerWorkflowStore.load({ params : {"workgroupId":workgroupId,"insurerId":insurerId}});
                    },
                    blur: function () {
                        if(this.getRawValue() == "" ) {
                            this.clearValue(); this.reset();
                            claimOwnerWorkflowCombo.reset();
                            claimOwnerWorkflowStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
                        }
                    }
                }
            });
            ownerWorkflowWorkgroupStore.load();
    </s:if>
            claimOwnerWorkflowStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
        
        
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
                renderTo: 'rptBreOwnerWorkflowSupplierDropDownDivSelectionHolder',
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
                        }
                    }
                }
            });
        
        
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
	        if($("form#formReportParam").valid()){
//	            var queryString = $('#formReportParam').formSerialize();
                    var queryString = {};
                    $.each($('#formReportParam').serializeArray(), function() {queryString[this.name] = this.value;});
	            // If no workgroup selected, insert a '-1' into the query string
                    if (queryString.workgroupId === "") {
                        queryString.workgroupId = -1;
                    }
//	            if (queryString.indexOf('workgroupId=&') >= 0)
//	                queryString = queryString.replace('workgroupId=&', 'workgroupId=-1&')
	            generateReport(queryString);
	        }
        }

</script>
<fieldset class="x-fieldset">
<s:if test="insurerIsWorkgroupEnabled">
    <legend>Workgroup and Owner BRE Invoice Approval Dispute Report</legend>
</s:if>
<s:else>
    <legend>Claim Owner BRE Invoice Approval Dispute Report</legend>
</s:else>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report provides an insight into the the number of invoices that pass the business rules and the resulting action taken, looking at whether the invoices are being paid or disputed, broken down by handler.  This report also provides a breakdown of why invoices are being disputed after being approved by the business rules.  The report will look at all invoices that were uploaded during the selected period from and to dates.</div>

                <table class="report-form">

                    <tr>
                        <td nowrap><label>Credit Hire Organisation</label></td>
                        <td>
                            <div id="rptBreOwnerWorkflowSupplierDropDownDivSelectionHolder"></div>
                        </td>
                    </tr>
                    <s:if test="insurerIsWorkgroupEnabled">
                        <tr>
                            <td nowrap><label>Workgroup</label></td>
                            <td>
                                <div id="rptBreOwnerWorkflowWrkgroupSelectionHolder"></div>
                            </td>
                        </tr>
                    </s:if>
                    <s:else>
                        <input type="hidden" id="workgroupId" name="workgroupId" value="-1"/>
                    </s:else>
                    <tr>
                        <td nowrap><label>Claim Owner</label></td>
                        <td>
                            <div id="rptBreOwnerWorkflowOwnerSelectionHolder"></div>
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
                    <button type="button" id="WOBIRGenerateReportId"onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div></form>
</fieldset>
