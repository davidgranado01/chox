<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'NewIncomingHandlerActionsReport-Excel';
    var isClaimOwnerShipEnabled = <s:property value="insurerIsClaimOwnershipEnabled"/>;
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
                	max:"'Period to' can't be before 'Period From'",
                    required:"A value must be supplied for 'Period From'",
                    dateITA:"You must supply valid date format for 'Period From'"
                },
                DateEnd: {
                    required:"A value must be supplied for 'Period To'",
                    dateITA:"You must supply valid date format for 'Period To'"
                }
            }
        });
        
        <s:if test="insurerIsClaimOwnershipEnabled">
		 var incmgHnderRepClaimOwnerReader = new Ext.data.JsonReader({
		     totalProperty: 'totalCount',
		     root: 'results',
		     fields:
		         [
		         {name:'id'},
		         {name:'name'}
		     ]
		 });
		
		 var incmgHnderRepClaimOwnerStore = new choxDataStore({
		     url : "/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", 
                     params : {"workgroupId":-1,"insurerId":insurerId},
		        reader : incmgHnderRepClaimOwnerReader,
		        listeners: {load: function() {
		                var  defaultName={'name':'--- ALL ---','id':-1};
		                this.insert(0, new Ext.data.Record(defaultName));
		            }
		        }
		                    
		    });
		
		    var incmgHnderRepClaimOwnerCombo = new Ext.form.ComboBox({
		        store : incmgHnderRepClaimOwnerStore,
		        width: 250,
		        renderTo: 'nhrInvSumOwnerSelectionHolder',
		        id : 'nhRepOwnerCombo',
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
		                if(this.getRawValue() === "" ) {
		                    this.clearValue(); this.reset();
		                }
		            }
		        }
		    });
                    incmgHnderRepClaimOwnerStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
	 </s:if>
	 <s:if test="insurerIsWorkgroupEnabled">
		        var incmgHnderRepWorkgroupJsonReader = new Ext.data.JsonReader({
		            totalProperty: 'totalCount',
		            root: 'results',
		            fields:
		                [
		                {name:'text'},
		                {name:'value'}
		            ]
		        });
		
		        var  incmgHnderRepWorkgroupStore = new choxDataStore({
		            url : "/prv/p/WorkgroupDropDownActionByInsurer2.action",
		            reader :  incmgHnderRepWorkgroupJsonReader,
		            listeners: {load: function() {
		                    var  defaultValue={'value':'--- ALL ---','text':-1};
		                    this.insert(0, new Ext.data.Record(defaultValue));
		                }
		            }
		        });
		
		        var  incmgHnderRepWorkgroupCombo = new Ext.form.ComboBox({
		            store:  incmgHnderRepWorkgroupStore,
		            renderTo: 'nhrWrkgroupHolder',
		            valueField: 'text',
		            id: 'nhRepWorkgroupComboId',
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
		                    if (incmgHnderRepWorkgroupCombo.getValue() !== null && incmgHnderRepWorkgroupCombo.getValue() !== '--- ALL ---' && incmgHnderRepWorkgroupCombo.getValue() !== "") {
		                        workgroupId = incmgHnderRepWorkgroupCombo.getValue();
		                    }
                                    if (isClaimOwnerShipEnabled) {
                                        incmgHnderRepClaimOwnerCombo.reset();
                                        incmgHnderRepClaimOwnerCombo.setValue('--- ALL ---');
                                        incmgHnderRepClaimOwnerStore.removeAll();
                                        incmgHnderRepClaimOwnerStore.load({ params : {"workgroupId":workgroupId,"insurerId":insurerId}});
                                    }
		                },
		                blur: function () {
		                    if(this.getRawValue() === "" ) {
		                        this.clearValue(); this.reset();
                                        if (isClaimOwnerShipEnabled) {
                                            incmgHnderRepClaimOwnerCombo.reset();
                                            incmgHnderRepClaimOwnerStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
                                        }
		                    }
		                }
		            }
		        });
		        incmgHnderRepWorkgroupStore.load();
	  </s:if>
		
		        var suppliersJsonReader = new Ext.data.JsonReader({
		            totalProperty: 'totalCount',
		            root: 'results',
		            fields:
		                [
		                {name:'text'},
		                {name:'value'}
		            ]
		        });
		
		        var mysuppliers = Ext.util.JSON.decode('<s:property value="suppliersJsonString" escapeHtml="false"/>');
		        var suppliersStore = new Ext.data.Store({
		            data : mysuppliers,
		            reader : suppliersJsonReader,
		            listeners: {load: function() {
		                    var  defaultValue={'value':'--- ALL ---','text':-1};
		                    this.insert(0, new Ext.data.Record(defaultValue));
		                }
		            }
		        });
		
		        var supplierCombo = new Ext.form.ComboBox({
		            store : suppliersStore,
		            id : 'nhRepPSupplierCombo',
		            renderTo: 'nhrChoDropDownDiv',
		            width: 250,
		            valueField : 'text',
		            hiddenName: 'nhrSupplierId',
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
		                    if(this.getRawValue() === "" ) {
		                        this.clearValue();
		                        this.reset();
		                    }
		                }
		            }
		        });
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

    <legend>New Incoming Actions Report</legend>

    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">

        <div class="x-panel-bwrap chox-form-container">

            <div class="form-container">

				<div class="instruction-message">This report provides details on the number of 
                                    new actions across a specified period, this allows an assessment of 
                                    the volume of new work coming in. The dates below determine the period 
                                    from which the incoming work was received.
				</div>

				<table class="report-form">
				    <tr>
                        <td nowrap><label>Credit Hire Organisation</label></td>

                        <td>
                            <div id="nhrChoDropDownDiv"></div>
                        </td>
                    </tr>
                                        <s:if test="insurerIsWorkgroupEnabled">
					<tr>
						<td nowrap><label>Workgroup</label></td>
						<td>
							<div id="nhrWrkgroupHolder"></div>
						</td>
					</tr>
					</s:if>
                                        <s:if test="insurerIsClaimOwnershipEnabled">
					<tr>
						<td nowrap><label>Claim Owner</label></td>
						<td>
							<div id="nhrInvSumOwnerSelectionHolder"></div>
						</td>
					</tr>
                                        </s:if>
					<tr>
						<td nowrap width="30%"><label>Period From</label><span class="mandatory">*</span></td>
						<td><div id="dateFromDiv" /></td>
					</tr>
					<tr>
						<td nowrap><label>Period To</label><span class="mandatory">*</span></td>
						<td><div id="dateToDiv" /></td>
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