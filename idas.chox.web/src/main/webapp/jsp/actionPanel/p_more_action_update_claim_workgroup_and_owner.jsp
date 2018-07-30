<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var claimId = -1;
    var claimOwnerId = -1;
    var insurerId = -1;
    var isWorkgroupEnable = false;
    var selectedWorkgroupId = -1;
    var workgroupCombo;
    var claimOwnerStore;
    var claimOwnerCombo;

    Ext.onReady(function() {
        // GET CLAIM INFORMATION
        if($("#claimClaimOwnerId").val()!==null && $("#claimClaimOwnerId").val()!==""){
            claimOwnerId = $("#claimClaimOwnerId").val();
        }

        insurerId = '<s:property value="insurer.id"/>';
        claimId = '<s:property value="id"/>';
        isWorkgroupEnable = ('<s:property value="insurer.workgroupEnable"/>' === 'true');

        // Add claim owner combo box
        var claimOwnerReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'name'}
            ]
        });

        claimOwnerStore = new choxDataStore({
            url : "/prv/p/ClaimHandlerRoleUserDropDownAction2.action", 
            params : {"workgroupId":selectedWorkgroupId, "insurerId":insurerId},
            reader : claimOwnerReader
        });

        claimOwnerCombo = new Ext.form.ComboBox({
            store: claimOwnerStore,
            width: 200,
            renderTo: 'claimOwnerReassignComboDiv',
            valueField: 'id',
            id: 'claimOwnerReassignComboId',
            hiddenName: 'claimOwnerId',
            displayField:'name',
            typeAhead: true,
            mode: 'local',
            listWidth: 200,
            forceSelection: true,
            triggerAction: 'all',
            emptyText: '--- Please Select ---',
            listeners: {
                blur: function () {
                    if(this.getRawValue() === "") {
                        this.clearValue(); this.reset();
                        claimOwnerId = -1;
                    }
                },
                specialkey:function (el, e) {
                            if(e.keyCode === e.ENTER) {
                                e.preventDefault();
                            }
                }
            }
        });

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

            var workgroupStore = new choxDataStore({
                url : "/prv/p/WorkgroupDropDownActionByInsurer3.action",
                params : {"claimId":claimId},
                reader: wgrpJsonReader
            });

            workgroupCombo = new Ext.form.ComboBox({
                store: workgroupStore,
                width: 200,
                renderTo: 'workgroupReassignComboDiv',
                valueField: 'text',
                id: 'workgroupReassignComboId',
                hiddenName: 'oasWorkgroupId',
                displayField:'value',
                typeAhead: true,
                mode: 'local',
                triggerAction: 'all',
                forceSelection: true,
                listWidth: 200,
                selectOnFocus: true,
                autoLoad:true,
                listeners: {
                    select: function() {
                        doUpdateOwnershipShowClaimHandler(workgroupCombo.getValue());
                    },
                    blur: function () {
                        if(this.getRawValue() === "") {
                            selectedWorkgroupId = '<s:property value="workgroup.id"/>';
                            this.clearValue(); 
                            workgroupCombo.setValue(selectedWorkgroupId);
                            doUpdateOwnershipShowClaimHandler(selectedWorkgroupId);
                        }
                    }
                }
            });
            workgroupStore.load({ params : {"claimId":claimId}, callback: function () {
                            workgroupCombo.setValue(selectedWorkgroupId);}}
            );
        }


        doUpdateOwnershipShowClaimHandler(selectedWorkgroupId);
});

    function doUpdateOwnershipShowClaimHandler(selectedWorkgroupId){
        if((isWorkgroupEnable && selectedWorkgroupId>0) || !isWorkgroupEnable){
            claimOwnerStore.removeAll();
            claimOwnerStore.load({ params : {"workgroupId":selectedWorkgroupId, "insurerId":insurerId}});
            claimOwnerCombo.reset();
        }
    }

    function validateReassignComboBox(){
        var mesBox = $("#OwnershipReassignMessageBox");
        mesBox.empty();
        if ($("#claimOwnerReassignComboId").val() === "--- Please Select ---") {
            mesBox.append("You must supply a value for 'Claim Owner'\n<br/>").show();
            return false;
        } else {
            mesBox.text("").show();
            return true;
        }
            
    }

    function doReassignOwnershipSubmit(){
        actionPanel.registerAction("assignOwner");
        if (validateReassignComboBox()) {
            Ext.get('formOwnershipAction').mask("Reloading Claim...");
            choxJqueryHttpSubmit($("form#formOwnershipAction"));
            return false;
        }
        return false;
    }

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formOwnershipAction" name="formOwnershipAction">
        <fieldset class="x-fieldset">
            <s:if test="insurer.workgroupEnable">
            <legend>Update Workgroup/Claim Owner - Action Required</legend>
            </s:if>
            <s:else>
                <legend>Update Claim Owner - Action Required</legend>
            </s:else>
            <div>
                <s:hidden id="claimId" name="id" />
                <s:hidden id="nameId" name="name" value="assignOwner" />
                <input type="hidden" id="claimWorkgroupId" name="claimWorkgroupId" value="<s:property value="workgroup.id"/>">
                <input type="hidden" id="claimClaimOwnerId" name="claimClaimOwnerId" value="<s:property value="claimOwner.id"/>">
                <input type="hidden" id="claimWorkgroupEnable" name="claimWorkgroupEnable" value="<s:property value="insurer.workgroupEnable"/>">
                <div>
                    <div class="status-info">
                        <s:if test="insurer.workgroupEnable">
                        Update the Workgroup or Claim Owner by using the drop down menus provided below, selecting a Workgroup will determine which Claims Handlers are displayed in the Claim Owner drop down menu.
                        </s:if>
                        <s:else>
                        Update the Claim Owner by using the drop down menus provided below.    
                        </s:else>
                        </div>
                    <div class="status-control-set">
                        <table class="status-table" width="100%">
                            <s:if test="insurer.workgroupEnable">
                                <tr>
                                    <td align="right" width="10%"><label>Workgroup : </label></td>
                                    <td width="20%"><div id="workgroupReassignComboDiv"/></td>
                                    <td width="70%"></td>
                                </tr>
                            </s:if>
                            <tr>
                                <td align="right" width="10%"><label>Claim Owner : </label></td>
                                <td width="20%"><div id="claimOwnerReassignComboDiv"></div></td>
                                <td width="70%"></td>
                            </tr>
                            <tr>
                                <td>
                                    <input id="assign" type="button" value="Update" onclick="event.preventDefault(); doReassignOwnershipSubmit();"/>
                                </td>
                            </tr>
                        </table>
                        <div class="action-error-msg" id="OwnershipReassignMessageBox"></div>
                    </div>
                </div>
            </div>
        </fieldset>
    </form>
</div>