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
   
    
    insurerId = '<s:property value="insurer.id"/>';
    
    if (<s:property value="insurer.enableManualInvoiceWorkgroups"/> && <s:property value="insurer.workgroupEnable"/>) {
        isWorkgroupEnable = true;
    }
        
    if (<s:property value="insurer.enableManualInvoiceOwnership"/> && <s:property value="insurer.claimOwnershipEnable"/>) {
        isInvoiceOwnershipEnable = true;
    }
        

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
            ({url : "<%= request.getContextPath()%>/prv/p/ClaimHandlerRoleUserDropDownAction.action", method:'GET', params : {"workgroupId":selectedWorkgroupId, "insurerId":insurerId}}),
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
                        validateComboBox()
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
            ({url : "<%= request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer.action", method:'GET', params : {"claimId":<s:property value="id"/>}}),
            reader: wgrpJsonReader,
             listeners: {load : function() {workgroupCombo.setValue(selectedWorkgroupId);}}
        });

        workgroupCombo = new Ext.form.ComboBox({
            store: workgroupStore,
            width: 200,
            renderTo: 'workgroupComboDiv',
            valueField: 'text',
            id: 'workgroupComboId',
            hiddenName: 'oasWorkgroupId',
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
                <s:if test="insurer.enableManualInvoiceOwnership">
                    doRenderClaimHandlerDropDown(workgroupCombo.getValue());
                </s:if>
                },
                blur: function () {
                    if(this.getRawValue() == "") {
                        selectedWorkgroupId = '<s:property value="workgroup.id"/>';
                        this.clearValue(); 
                        workgroupCombo.setValue(selectedWorkgroupId);
                        if (isInvoiceOwnershipEnable) {
                           doRenderClaimHandlerDropDown(selectedWorkgroupId); 
                        }
                    }
                }
            }
        });
        workgroupStore.load({ params : {"claimId":<s:property value="id"/>}});
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
    if (isInvoiceOwnershipEnable && $("#claimOwnerComboId").val() == "--- Please Select ---" && 
            isWorkgroupEnable && $("#workgroupComboId").val() == "--- Please Select ---"){
        mesBox.append("You must supply a value for 'Workgroup'\n<br/>").show();
        mesBox.append("You must supply a value for 'Claim Owner'\n<br/>").show();
        return false;
    } else if (isInvoiceOwnershipEnable && $("#claimOwnerComboId").val() == "--- Please Select ---") {
        mesBox.append("You must supply a value for 'Claim Owner'\n<br/>").show();
        return false;
    } else if (isWorkgroupEnable && $("#workgroupComboId").val() == "--- Please Select ---") {
        mesBox.append("You must supply a value for 'Workgroup'\n<br/>").show();
        return false;
    } else {
        mesBox.text("").show();
        return true;
    }
        
}

function assignManualInvoiceSubmit(){
    $('form#updateManualInvoicePaymentForm input[id="name"]').val("assignManualInvoiceOwner")
    if (validateComboBox()) {
        Ext.get('claimDetailScreenDiv').mask("Reloading Claim ...");
        $("#updateManualInvoicePaymentForm").submit();
    }
}

</script>


<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form  id="updateManualInvoicePaymentForm" name="updateManualInvoicePaymentForm" onsubmit="return true;" action="<%=request.getContextPath()%>/prv/processClaim.action" method="POST">
            <fieldset class="x-fieldset"><legend>Manual Invoice Ownership - Action Required</legend>
                <div>
                    <s:hidden id="claimId" name="id" />
                    <s:hidden id="name" name="name" />
                        
                    <s:if test="insurer.enableManualInvoiceWorkgroups && insurer.enableManualInvoiceOwnership && insurer.workgroupEnable && insurer.claimOwnershipEnable">
                        <div class="status-info">
                            Please assign the Workgroup and claim owner for this claim and click on the 'Assign Owner' button.
                        </div>
                    </s:if>
                    <s:elseif test="insurer.enableManualInvoiceWorkgroups && insurer.workgroupEnable && (!insurer.enableManualInvoiceOwnership || !insurer.claimOwnershipEnable)">
                        <div class="status-info">
                            Please assign the Workgroup for this claim and click on the 'Assign Workgroup' button. 
                        </div>
                    </s:elseif>
                    <s:elseif test="(!insurer.enableManualInvoiceWorkgroups && !insurer.workgroupEnable) && insurer.enableManualInvoiceOwnership && insurer.claimOwnershipEnable">
                        <div class="status-info">
                            Please assign the claim owner for this claim and click on the 'Assign Owner' button.
                        </div>
                    </s:elseif>
                        
                    <div class="status-control-set">
                        <table class="status-table" border="0" cellpadding="0" cellspacing="0">
                            <s:if test="insurer.enableManualInvoiceWorkgroups && insurer.workgroupEnable">
                                <tr>
                                    <td align="right" width="10%"><label>Workgroup : </label></td>
                                    <td width="20%"><div id="workgroupComboDiv"/></td>
                                    <td width="70%"></td>
                                </tr>
                            </s:if>
                            <s:if test="insurer.enableManualInvoiceOwnership && insurer.claimOwnershipEnable">
                                <tr>
                                    <td align="right" width="10%"><label>Claim Owner : </label></td>
                                    <td width="20%"><div id="claimOwnerComboDiv"></div></td>
                                    <td width="70%"></td>
                                </tr>
                            </s:if>
                                
                            <s:if test="(insurer.enableManualInvoiceWorkgroups && insurer.enableManualInvoiceOwnership && insurer.claimOwnershipEnable && insurer.workgroupEnable)
                                  || !insurer.enableManualInvoiceWorkgroups && insurer.enableManualInvoiceOwnership && insurer.claimOwnershipEnable">
                                <tr>
                                    <td colspan="3" class="" nowrap >
                                        <input type="button" id="miAssignButton" value="Assign Owner" onclick="return assignManualInvoiceSubmit();"/>
                                    </td>
                                </tr>
                            </s:if>
                            <s:elseif test="insurer.enableManualInvoiceWorkgroups && !insurer.enableManualInvoiceOwnership && insurer.workgroupEnable">
                                <tr>
                                    <td colspan="3" class="" nowrap >
                                        <input type="button" id="miAssignButton" value="Assign Workgroup" onclick="return assignManualInvoiceSubmit();"/>
                                    </td>
                                </tr>
                            </s:elseif>
                        </table>
                        <div class="chox-form-submit-result"></div>
                        <div class="action-error-msg" id="OwnershippAssignmentMessageBox"></div>
                    </div>
                </div>
            </fieldset>
            <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>