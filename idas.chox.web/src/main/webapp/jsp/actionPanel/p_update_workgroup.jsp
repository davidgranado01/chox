<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">

var selectedWorkgroupId = -1;
var insurerId = -1;
var isWorkgroupEnable = false;

Ext.onReady(function(){
   
    
    insurerId = '<s:property value="insurer.id"/>';
        
    
    if(<s:property value="insurer.workgroupEnable"/>) {
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

        var workgroupStore = new choxDataStore({
            url : "/prv/p/WorkgroupDropDownActionByInsurer.action", 
            params : {"claimId":<s:property value="id"/>},
            reader: wgrpJsonReader,
             listeners: {load : function() {workgroupCombo.setValue(selectedWorkgroupId);}}
        });

        var workgroupCombo = new Ext.form.ComboBox({
            store: workgroupStore,
            width: 200,
            renderTo: 'workgroupComboDiv',
            valueField: 'text',
            id: 'workgroupComboId',
            hiddenName: 'workgroupId',
            displayField:'value',
            typeAhead: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '--- Please Select ---',
            forceSelection: true,
            listWidth: 200,
            selectOnFocus: true,
            listeners: {
                specialkey:function (el, e) {
                            if(e.keyCode === e.ENTER) {
                                e.preventDefault();
                            }
                }
            }
        });
        workgroupStore.load({ params : {"claimId":<s:property value="id"/>}});
    }

});


function validateComboBox(){
    var mesBox = $("#WorkgroupAssignmentMessageBox");
    mesBox.empty();
    var selectedComboValue = Ext.getCmp('workgroupComboId').getValue();
    if (selectedComboValue === '' && selectedComboValue <= 0) {
        mesBox.append("You Must Select A 'Workgroup'.").show();
        return false;
    } else {
        mesBox.text("").show();
        return true;
    }
        
}

function assignClaimOwnershipSubmit(){
    actionPanel.registerAction("assignWorkgroup");
    if (validateComboBox()) {
        Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
//        $("#updateWorkgroupForm").submit();
        choxJqueryHttpSubmit($("form#updateWorkgroupForm"));
    }
}

</script>


<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form  id="updateWorkgroupForm" name="updateWorkgroupForm" action="<%=request.getContextPath()%>/prv/processClaim.action?" method="POST">
        <fieldset class="x-fieldset"><legend>Update Workgroup - Action Required</legend>
            <div>
                <div class="status-info">Please select a Workgroup for this claim and click on the ’Update Workgroup’ button.
                </div>
                <div class="status-control-set">
                    <s:hidden id="name" name="name" />
                    <table class="status-table" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td align="right" width="10%"><label>Workgroup : </label></td>
                            <td width="20%"><div id="workgroupComboDiv"/></td>
                            <td width="70%"></td>
                        </tr>
                        <tr>
                            <td colspan="3" class="" nowrap >
                                <input type="button" id="miAssignButton" value="Update Workgroup" onclick="return assignClaimOwnershipSubmit();"/>
                            </td>
                        </tr>
                            
                    </table>
                    <div class="chox-form-submit-result"></div>
                    <div class="action-error-msg" id="WorkgroupAssignmentMessageBox"></div>
                </div>
            </div>
        </fieldset>
    </form>
</div>