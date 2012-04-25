<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'NewIncomingHandlerActionsReport-Excel';
    
    Ext.onReady(function(){
        var insurerId = <s:property value="userOrganisationId"/>;
        ui.dateField('DateStart',getTodayDate(),'dateFromDiv');
        ui.dateField('DateEnd',getTodayDate(),'dateToDiv');

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
                    required:"A value must be supplied for 'Handler Task Created From'",
                    dateITA:"You must supply a date value 'Handler Task Created From'"
                },
                DateEnd: {
                    required:"A value must be supplied for 'Handler Task Created To'",
                    dateITA:"You must supply a date value 'Handler Task Created To'"
                }
            }
        });

		 var invSumRepClaimOwnerReader = new Ext.data.JsonReader({
		     totalProperty: 'totalCount',
		     root: 'results',
		     fields:
		         [
		         {name:'id'},
		         {name:'name'}
		     ]
		 });
		
		 var invSumRepClaimOwnerStore = new Ext.data.Store({
		     proxy : new Ext.data.HttpProxy
		     ({url : "<%=request.getContextPath()%>/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", method:'GET', params : {"workgroupId":-1,"insurerId":insurerId}}),
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
		        renderTo: 'nhrInvSumOwnerSelectionHolder',
		        id : 'nhRepOwnerCombo',
		        valueField : 'id',
		        displayField :'name',
		        hiddenName: 'ownerId',
		        valueNotFoundText : '--- ALL ---',
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
		                this.setValue('--- ALL ---');
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
		
		        var  invSumRepWorkgroupStore = new Ext.data.Store({
		            proxy : new Ext.data.HttpProxy
		            ({url : "<%=request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer2.action", method:'GET'}),
		            reader :  invSumRepWorkgroupJsonReader,
		            listeners: {load: function() {
		                    var  defaultValue={'value':'--- ALL ---','id':1}
		                    this.insert(0, new Ext.data.Record(defaultValue));
		                }
		            }
		        });
		
		        var  invSumRepWorkgroupCombo = new Ext.form.ComboBox({
		            store:  invSumRepWorkgroupStore,
		            renderTo: 'nhrWrkgroupHolder',
		            valueField: 'text',
		            id: 'nhRepWorkgroupComboId',
		            hiddenName: 'workgroupId',
		            displayField:'value',
		            width: 250,
		            valueNotFoundText : '--- ALL ---',
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
		                    invSumRepClaimOwnerCombo.setValue('--- ALL ---');
		                    invSumRepClaimOwnerStore.removeAll();
		                    invSumRepClaimOwnerStore.load({ params : {"workgroupId":workgroupId,"insurerId":insurerId}});
		                },
		                blur: function () {
		                    if(this.getRawValue() == "" ) {
		                        this.clearValue(); this.reset();
		                        invSumRepClaimOwnerCombo.reset();
		                        invSumRepClaimOwnerStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
		                    }
		                },
		                afterrender : function(){
		                    this.setValue('--- ALL ---');
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
		                    var  defaultValue={'value':'--- ALL ---','id':1}
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
		            valueNotFoundText : '--- ALL ---',
		            selectOnFocus : false,
		            allowBlank : true,
		            forceSelection : true,
		            listeners: { blur: function () {
		                    if(this.getRawValue() == "" ) {
		                        this.clearValue();
		                        this.reset();
		                    }
		                },
		                afterrender : function(){
		                    this.setValue('--- ALL ---');
		                }
		            }
		        });
        });
    
        function openReport()
        {
            if($("form#formReportParam").valid()){
                var queryString = $('#formReportParam').formSerialize();
                generateReport(queryString);
            }
        }
    
</script>

<fieldset class="x-fieldset">

    <legend>New Incoming Actions Report</legend>

    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">

        <div class="x-panel-bwrap chox-form-container">

            <div class="form-container">

				<div class="instruction-message">This report provides details
					on the number of new handler actions across a specified period,
					this allows an assessment of the volume of new work coming into
					handlers. The dates below determine the period from which the
					incoming work was received.
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
					<tr>
						<td nowrap><label>Claim Owner</label></td>
						<td>
							<div id="nhrInvSumOwnerSelectionHolder"></div>
						</td>
					</tr>
					<tr>
						<td nowrap width="30%"><label>Period From</label></td>
						<td><div id="dateFromDiv" /></td>
					</tr>
					<tr>
						<td nowrap><label>Period To</label></td>
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