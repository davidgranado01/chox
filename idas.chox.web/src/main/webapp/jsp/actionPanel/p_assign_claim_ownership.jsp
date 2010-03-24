<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var selectedWorkgroupId = -1;
    var insurerId = -1;
    var claimOwnerId = -1;
    var isWorkgroupEnable = false;
    var workgroupCombo;
    var claimOwnerStore;
    var claimOwnerCombo;

    Ext.onReady(function(){
//        Ext.BLANK_IMAGE_URL = 'images/s.gif';
    // The 'setValue' function on the combo box doesn't work
    // as, fue to the asynchronous nature of the widget, the store may
    // not be loaded. Below is a patch to fix this problem.
    Ext.override(Ext.form.ComboBox, {
        setValue : function(v){
            //begin patch
            // Store not loaded yet? Set value when it *is* loaded.
            // Defer the setValue call until after the next load.
            if (this.store.getCount() == 0) {
                this.store.on('load',
                this.setValue.createDelegate(this, [v]), null, {single: true});
                return;
            }
            //end patch
            var text = v;
            if(this.valueField){
                var r = this.findRecord(this.valueField, v);
                if(r){
                    text = r.data[this.displayField];
                }else if(this.valueNotFoundText !== undefined){
                    text = this.valueNotFoundText;
                }
            }
            this.lastSelectionText = text;
            if(this.hiddenField){
                this.hiddenField.value = v;
            }
            Ext.form.ComboBox.superclass.setValue.call(this, text);
            this.value = v;
        }});

        insurerId = '<s:property value="insurer.id"/>';
        isWorkgroupEnable = '<s:property value="insurer.workgroupEnable"/>';


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

        claimOwnerStore = new Ext.data.Store({
                proxy : new Ext.data.HttpProxy
                ({url : "<%= request.getContextPath()%>/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", method:'GET', params : {"workgroupId":selectedWorkgroupId, "insurerId":insurerId}}),
                reader : claimOwnerReader
        });

        claimOwnerCombo = new Ext.form.ComboBox({
                                store: claimOwnerStore,
                                renderTo: 'claimOwnerComboDiv',
                                valueField: 'id',
                                id: 'claimOwnerComboId',
                                hiddenName: 'claimOwnerId',
                                displayField:'name',
                                typeAhead: true,
                                mode: 'local',
                                listWidth: 165,
                                triggerAction: 'all',
                                emptyText: '--- Please Select ---',
                                listeners: {
                                            blur: function () {
                                                if(this.getRawValue() == "") {
                                                    this.clearValue(); this.reset();
                                                    claimOwnerId = -1;
                                                }
                                            }
                                           }
        });
        $.validator.addMethod("claimOwnerSelection",
                            function(value) {
                                if(value === "") {
                                    return false;
                                }
                                return true;
                            }, "You must select a 'Claim Owner'"
        );

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
                ({url : "<%= request.getContextPath()%>/prv/p/SearchWorkgroupDropDownAction.action", method:'GET', params : {"orgId":insurerId}}),
                reader: wgrpJsonReader
            });

            workgroupCombo = new Ext.form.ComboBox({
                                store: workgroupStore,
                                renderTo: 'workgroupComboDiv',
                                valueField: 'text',
                                id: 'workgroupComboId',
                                hiddenName: 'oasWorkgroupId',
                                displayField:'value',
                                typeAhead: true,
                                mode: 'local',
                                triggerAction: 'all',
                                emptyText: '--- Please Select ---',
                                listWidth: 165,
                                selectOnFocus: true,
                                listeners: {select: function() {
                                                doRenderClaimHandlerDropDown(workgroupCombo.getValue());
                                            },
                                            blur: function () {
                                                if(this.getRawValue() == "") {
                                                    selectedWorkgroupId = '<s:property value="workgroup.id"/>';
                                                    this.clearValue(); workgroupCombo.setValue(selectedWorkgroupId);;
                                                    doRenderClaimHandlerDropDown(selectedWorkgroupId);
                                                }
                                            }
                                           }
            });
            workgroupStore.load({ params : {"orgId":insurerId}});
            workgroupCombo.setValue(selectedWorkgroupId);
        }

//        doRenderClaimHandlerDropDown(selectedWorkgroupId);

    });


    $(function(){

        // PREPARE RECORDS
        if($("#claimClaimOwnerId").val()!=null && $("#claimClaimOwnerId").val()!=""){
            claimOwnerId = $("#claimClaimOwnerId").val();
        }

        if($("#claimWorkgroupEnable").val()!=null && $("#claimWorkgroupEnable").val()!=""){
            isWorkgroupEnable = $("#claimWorkgroupEnable").val();
        }

        if(isWorkgroupEnable){
            if($("#claimWorkgroupId").val()!=null && $("#claimWorkgroupId").val()!=""){
                selectedWorkgroupId = $("#claimWorkgroupId").val();
                $("#oasWorkgroupId").val(selectedWorkgroupId);
            }
        }

        // DECLARE FORM VALIDATION
        var form = $("form#formOwnershipAssignmentAction");

        form.validate(
        {
            errorLabelContainer: "#OwnershippAssignmentMessageBox",
            rules: {
                oasWorkgroupId:{min:1},
                claimOwnerId:{claimOwnerSelection: document.getElementById('claimOwnerComboId')}
            },
            messages: {
                oasWorkgroupId: {min:"You must supply a value for 'Workgroup'"},
                claimOwnerId: {claimOwnerSelection:"You must supply a value for 'Claim Owner'"}
            }
        });

        // RENDER CLAIM HANDLER DROP DOWN
        doRenderClaimHandlerDropDown(selectedWorkgroupId);

    });

    function doOwnershipAssignmentWorkgroupChange(){
        if (workgroupCombo.getValue() != null && workgroupCombo.getValue() != '') {
            selectedWorkgroupId = workgroupCombo.getValue();
        }
        claimOwnerId = -1;
        doRenderClaimHandlerDropDown(selectedWorkgroupId);

    }

    function doRenderClaimHandlerDropDown(selectedWorkgroupId){
        if(selectedWorkgroupId>0){
            claimOwnerStore.removeAll();
            claimOwnerStore.load({ params : {"workgroupId":selectedWorkgroupId, "insurerId":insurerId}});
            claimOwnerCombo.reset();
        }
    }

    function doAssignOwnershipToFnolSubmit(){
        actionPanel.registerAction("referFNOL");
        $("form#formOwnershipAssignmentAction #claimOwnerId").rules("remove");
    }

    function doAssignOwnershipSubmit(){
        actionPanel.registerAction("assignOwner");
        $("form#formOwnershipAssignmentAction #claimOwnerId").rules("add", {
            claimOwnerSelection: document.getElementById('claimOwnerComboId')
        })
    }

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="formOwnershipAssignmentAction" name="formOwnershipAssignmentAction" action="<%=request.getContextPath()%>/prv/processClaim.action" method="POST" onsubmit="return true;">
        <div class="form-container">
            <fieldset class="x-fieldset">
                <legend>Claim Ownership - Action Required</legend>
                <div>
                    <s:hidden id="id" name="id" />
                    <s:hidden id="name" name="name" />
                    <input type="hidden" id="claimWorkgroupId" name="claimWorkgroupId" value="<s:property value="workgroup.id"/>">
                    <input type="hidden" id="claimClaimOwnerId" name="claimClaimOwnerId" value="<s:property value="claimOwner.id"/>">
                    <input type="hidden" id="claimWorkgroupEnable" name="claimWorkgroupEnable" value="<s:property value="insurer.workgroupEnable"/>">
                    <div>
                        <div class="status-info">
                            Please assign the claim owner for this claim and click on the 'Assign Owner' button. If the claim needs registering by FNOL, please use the 'Refer To FNOL' button (please note that the FNOL team the claim is referred to is based on the Workgroup assigned to the claim). If this claim has been assigned to the incorrect Workgroup, please use the 'More Actions' drop down above, clicking on 'Re-assign Workgroup' to re-assign the claim's Workgroup.
                        </div>
                        <div class="status-control-set">
                            <table border="0" cellpadding="0" cellspacing="0">
                                <s:if test="insurer.workgroupEnable">
                                    <tr>
                                        <td align="right"><label>Workgroup : </label></td>
                                        <td><div id="workgroupComboDiv"/></td>
                                    </tr>
                                </s:if>
                                <tr>
                                    <td align="right"><label>Claim Owner : </label></td>
                                    <td><div id="claimOwnerComboDiv"></div></td>
                                </tr>
                                <tr><td>&nbsp;</td></tr>
                                <tr>
                                    <td colspan="2" class="choice" nowrap align="center">
                                        <input type="submit" value="Assign Owner" onclick="javascript:return doAssignOwnershipSubmit();"/>
                                        <input type="submit" value="Refer to FNOL" onclick="javascript:return doAssignOwnershipToFnolSubmit();" />
                                    </td>
                                </tr>
                            </table>
                            <div class="chox-form-submit-result"></div>
                            <div class="action-error-msg" id="OwnershippAssignmentMessageBox"></div>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>
    </form>
</div>