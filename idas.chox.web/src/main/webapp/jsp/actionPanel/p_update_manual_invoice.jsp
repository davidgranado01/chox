<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">

var selectedWorkgroupId = -1;
var insurerId = -1;
var claimOwnerId = -1;
var isWorkgroupEnable = false;
var isInvoiceOwnershipEnable = false;
var workgroupCombo;
var claimOwnerStore;
var claimOwnerCombo;

Ext.onReady(function(){
//     Ext.override(Ext.form.ComboBox, {
//         setValue : function(v){
//             if (this.store.getCount() == 0) {
//                 this.store.on('load',
//                 this.setValue.createDelegate(this, [v]), null, {single: true});
//                 return;
//             }
//             var text = v;
//             if(this.valueField){
//                 var r = this.findRecord(this.valueField, v);
//                 if(r){
//                     text = r.data[this.displayField];
//                 }else if(this.valueNotFoundText !== undefined){
//                     text = this.valueNotFoundText;
//                 }
//             }
//             this.lastSelectionText = text;
//             if(this.hiddenField){
//                 this.hiddenField.value = v;
//             }
//             Ext.form.ComboBox.superclass.setValue.call(this, text);
//             this.value = v;
//         }});

    insurerId = '<s:property value="insurer.id"/>';
    isWorkgroupEnable = ('<s:property value="insurer.enableInvoiceWorkgroups"/>' == 'true');
    isInvoiceOwnershipEnable = ('<s:property value="insurer.enableInvoiceOwnership"/>' == 'true');

    // Add claim owner combo box
    if(isInvoiceOwnershipEnable) {
	    var claimOwnerReader = new Ext.data.JsonReader({
	        totalProperty: 'totalCount',
	        root: 'results',
	        fields:
	            [
	            {name:'id'},
	            {name:'name'}
	        ]
	    });
	
	    claimOwnerStore = new Ext.data.Store({
	        proxy : new Ext.data.HttpProxy
	        ({url : "<%= request.getContextPath()%>/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", method:'GET', params : {"workgroupId":selectedWorkgroupId, "insurerId":insurerId}}),
	        reader : claimOwnerReader
	    });
	
	    claimOwnerCombo = new Ext.form.ComboBox({
	        store: claimOwnerStore,
	        width: 200,
	        renderTo: 'claimOwnerComboDiv',
	        valueField: 'id',
	        id: 'claimOwnerComboId',
	        hiddenName: 'claimOwnerId',
	        displayField:'name',
	        typeAhead: true,
	        mode: 'local',
	        listWidth: 200,
	        forceSelection: true,
	        triggerAction: 'all',
	        emptyText: '--- Please Select ---',
	        forceSelection : true,
	        listeners: {
	            blur: function () {
	                if(this.getRawValue() == "") {
	                    this.clearValue(); this.reset();
	                    claimOwnerId = -1;
	                }
	            }
	        }
	    });
	    claimOwnerStore.load({ params : {"workgroupId":-1, "insurerId":insurerId}});
    }

    
    if(isWorkgroupEnable) {
        selectedWorkgroupId = '<s:property value="workgroup.id"/>';
        var wgrpJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'text'},
                {name:'value'}
            ]
        });

        var workgroupStore = new Ext.data.Store({
            proxy : new Ext.data.HttpProxy
            ({url : "<%= request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer2.action", method:'GET', params : {"orgId":insurerId}}),
            reader: wgrpJsonReader
        });

        workgroupCombo = new Ext.form.ComboBox({
            store: workgroupStore,
            width: 200,
            renderTo: 'workgroupComboDiv',
            valueField: 'text',
            id: 'workgroupComboId',
            hiddenName: 'manualInvoiceWorkgroupId',
            displayField:'value',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '--- Please Select ---',
            forceSelection: true,
            listWidth: 200,
            selectOnFocus: true,
            forceSelection : true,
            listeners: {select: function() {
            	<s:if test="insurer.enableInvoiceOwnership">
                    doRenderClaimHandlerDropDown(workgroupCombo.getValue());
                </s:if>
                },
                blur: function () {
                    if(this.getRawValue() == "") {
                        selectedWorkgroupId = '<s:property value="workgroup.id"/>';
                        this.clearValue(); 
                        workgroupCombo.setValue(selectedWorkgroupId);
                        doRenderClaimHandlerDropDown(selectedWorkgroupId);
                    }
                }
            }
        });
        workgroupStore.load({ params : {"orgId":insurerId}});
        workgroupCombo.setValue(selectedWorkgroupId);
    }

});

function doRenderClaimHandlerDropDown(selectedWorkgroupId){
    if((isWorkgroupEnable && selectedWorkgroupId>0) || !isWorkgroupEnable){
        claimOwnerStore.removeAll();
        claimOwnerStore.load({ params : {"workgroupId":selectedWorkgroupId, "insurerId":insurerId}});
        claimOwnerCombo.reset();
    }else{
         claimOwnerStore.removeAll();
         claimOwnerStore.load({ params : {"workgroupId":-1, "insurerId":insurerId}});
         claimOwnerCombo.reset();
    }
}

function validateComboBox(){
    var mesBox = $("#OwnershippAssignmentMessageBox");
    mesBox.empty();
    if (isInvoiceOwnershipEnable && $("#claimOwnerComboId").val() == "--- Please Select ---") {
        mesBox.append("You must supply a value for 'Claim Owner'\n<br/>").show();
        return false;
    } else if (isWorkgroupEnable && $("#workgroupComboId").val() == "--- Please Select ---") {
        mesBox.append("You must supply a value for 'Work Group'\n<br/>").show();
        return false;
    } else {
        mesBox.text("").show();
        return true;
    }
        
}

function doUpdateManualInvoice(action){
    
    actionPanel.registerAction(action);
    Ext.get('claimDetailScreenDiv').mask("Reloading Claim ...");
    $("form#updateManualInvoicePaymentForm").submit();
    
}

function assignClaimSubmit(){
	actionPanel.registerAction("assignInvoiceOwner");
    if (validateComboBox()) {
        Ext.get('claimDetailScreenDiv').mask("Reloading Claim ...");
        $("#updateManualInvoicePaymentForm").submit();
    }
}

</script>


<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form  id="updateManualInvoicePaymentForm" name="updateManualInvoicePaymentForm" onsubmit="return true;" action="<%=request.getContextPath()%>/prv/processClaim.action" method="POST">
        <fieldset class="x-fieldset"><legend>Manual Invoice - Action Required</legend>
            <div>
                <s:hidden id="claimId" name="id" />
                <s:hidden id="name" name="name" />
                
                <s:if test="status.equalsIgnoreCase('ManualInvoiceUnassigned')">
<%--                     <input type="hidden" id="claimWorkgroupId" name="claimWorkgroupId" value="<s:property value="workgroup.id"/>"> --%>
<%--                     <input type="hidden" id="claimClaimOwnerId" name="claimClaimOwnerId" value="<s:property value="claimOwner.id"/>"> --%>
<%--                     <input type="hidden" id="claimWorkgroupEnable" name="claimWorkgroupEnable" value="<s:property value="insurer.enableInvoiceWorkgroups"/>"> --%>
                
                    <s:if test="insurer.enableInvoiceWorkgroups && insurer.enableInvoiceOwnership">
	                    <div class="status-info">Please assign the
							Workgroup and claim owner for this claim and click on the 'Assign
							Claim' button.
						</div>
                    </s:if>
                    <s:elseif test="insurer.enableInvoiceWorkgroups && !insurer.enableInvoiceOwnership">
                        <div class="status-info">Please assign the
                            Please assign the Workgroup for this claim and click on the 'Assign Claim' button. 
                        </div>
                    </s:elseif>
                     <s:elseif test="!insurer.enableInvoiceWorkgroups && insurer.enableInvoiceOwnership">
                        <div class="status-info">Please assign the
                            Please assign the claim owner for this claim and click on the 'Assign Claim' button.
                        </div>
                    </s:elseif>
                    
                    
                     <div class="status-control-set">
                            <table class="status-table" border="0" cellpadding="0" cellspacing="0">
                                <s:if test="insurer.enableInvoiceWorkgroups">
                                    <tr>
                                        <td align="right" width="10%"><label>Workgroup : </label></td>
                                        <td width="20%"><div id="workgroupComboDiv"/></td>
                                        <td width="70%"></td>
                                    </tr>
                                </s:if>
                                <s:if test="insurer.enableInvoiceOwnership">
	                                <tr>
	                                    <td align="right" width="10%"><label>Claim Owner : </label></td>
	                                    <td width="20%"><div id="claimOwnerComboDiv"></div></td>
	                                    <td width="70%"></td>
	                                </tr>
                                </s:if>
                                <s:if test="insurer.enableInvoiceWorkgroups || insurer.enableInvoiceOwnership">
	                                <tr>
	                                    <td colspan="3" class="" nowrap >
	                                        <input type="button" id="miAssignButton" value="Assign Claim" onclick="return assignClaimSubmit();"/>
	                                    </td>
	                                </tr>
                                </s:if>
                            </table>
                    <div class="chox-form-submit-result"></div>
                    <div class="action-error-msg" id="OwnershippAssignmentMessageBox"></div>
                </s:if>
                <s:else>
		                <s:if test="!status.equalsIgnoreCase('ManualInvoiceContested')">
		                    <div class="status-info">
		                        If applicable please modify the invoice details to reflect any adjustments made to the invoice following any negotiations made outside of the CHOX process/system. 
		                        Once the payment has been made please click on the 'Manual Invoice Paid' button.  
		                        However if the invoice has been contested with the CHO then click on the 'Invoice Contested With CHO' button to move the claim to a holding status until an agreement has been reached.
		                    </div>
		                </s:if>
		                <s:else>
		                    <div class="status-info">
		                        Please modify the invoice details to reflect any adjustments made to the invoice following any negotiations made outside of the CHOX process/system.  Once the payment has been made please click on the 'Manual Invoice Paid' button.
		                    </div>
		                </s:else>
		                <div class="status-info-submit">
		                    <table>
		                <s:if test="!status.equalsIgnoreCase('ManualInvoiceContested')">
		                        <tr>
		                            <td colspan="2" class="choice" nowrap="true">
		                                <input type="button" id="UMIPFormId" value="Manual Invoice Paid" onclick="doUpdateManualInvoice('updateManualInvoicePaid');" />
		                                <input type="button" id="UMICFormId" value="Invoice Contested With CHO" onclick="doUpdateManualInvoice('updateManualInvoiceContested');" />
		                            </td>
		                        </tr>
		                </s:if>
		                <s:else>
		                        <tr>
		                            <td><input type="button" id="UMIPFormId" value="Manual Invoice Paid" onclick="doUpdateManualInvoice('updateManualInvoicePaid');" /></td>
		                        </tr>
		                </s:else>
	                </s:else>
                    </table>
                </div>
            </div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    </form>
</div>