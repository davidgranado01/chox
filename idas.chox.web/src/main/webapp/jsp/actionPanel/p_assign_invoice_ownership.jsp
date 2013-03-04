<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var claimId = -1;
    var insurerId = -1;
    var claimOwnerId = -1;
    var workgroupId=-1;
    var claimOwnerIdField=-1;
    var workgroupIdField=-1;
    var isWorkgroupEnable = false;
    var workgroupCombo;
    var claimOwnerStore;
    var claimOwnerCombo;
    var wgrpJsonReader;
    var claimOwnerReader;
    var workgroupStore;

    Ext.onReady(function(){
    

        insurerId = '<s:property value="insurer.id"/>';
        claimId = '<s:property value="id"/>';
        isWorkgroupEnable = ('<s:property value="insurer.workgroupEnable"/>' == 'true');

        // Add claim owner combo box
        claimOwnerReader = new Ext.data.JsonReader({
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
            ({url : "<%= request.getContextPath()%>/prv/p/ClaimHandlerRoleUserDropDownAction2.action", method:'GET', params : {"workgroupId":workgroupId, "insurerId":insurerId}}),
            reader : claimOwnerReader
        });

        claimOwnerCombo = new Ext.form.ComboBox({
            store: claimOwnerStore,
            width: 200,
            renderTo: 'claimOwnerComboDiv',
            valueField: 'id',
            id: 'claimOwnerComboId',
            hiddenName: 'claimOwnerIdField',
            displayField:'name',
            typeAhead: true,
            mode: 'local',
            listWidth: 200,
            forceSelection: true,
            triggerAction: 'all',
            emptyText: '--- Please Select ---',
            forceSelection : true,
            listeners: {
                select: function () {
                    if(this.getRawValue() == "") {
                        this.clearValue();
                        this.reset();
                        claimOwnerId = -1;
                        claimOwnerIdField=claimOwnerId;
                    }else {
                        claimOwnerId = this.value;
                        claimOwnerIdField=claimOwnerId;
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
        }
    );

       

        if(isWorkgroupEnable) {

            wgrpJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'text'},
                    {name:'value'}
                ]
            });

            workgroupStore = new Ext.data.Store({
                proxy : new Ext.data.HttpProxy
                ({url : "<%= request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer.action", method:'GET', params : {"claimId":claimId}}),
                reader: wgrpJsonReader
            });

            workgroupCombo = new Ext.form.ComboBox({
                store: workgroupStore,
                width: 200,
                renderTo: 'workgroupComboDiv',
                valueField: 'text',
                id: 'workgroupComboId',
                hiddenName: 'workgroupIdField',
                displayField:'value',
                typeAhead: true,
                mode: 'local',
                editable:false,
                triggerAction: 'all',
                emptyText: '--- Please Select ---',
                forceSelection: true,
                listWidth: 200,
                selectOnFocus: true,
                forceSelection : true,
                listeners: {
                    select:function() {
                        if(this.getRawValue() == "") {
                            this.clearValue();
                            this.reset();
                            workgroupId = -1;
                            workgroupIdField=workgroupId;
                            doRenderClaimHandlerDropDown(workgroupId);
                        }else {
                            workgroupId=this.value;
                            workgroupIdField=workgroupId;
                            doRenderClaimHandlerDropDown(workgroupId);
                        }
                    }                    
                }
            });

            $.validator.addMethod("comboSelection",
            function(value) {
                if(value < 1) {
                    return false;
                }
                return true;
            }
        );
            workgroupStore.load({ params : {"claimId":claimId}});
        }

        var form = $("form#formOwnershipAssignmentAction");
        form.validate(
        {
            errorLabelContainer: "#OwnershippAssignmentMessageBox",
            rules: {
                workgroupIdField:{comboSelection:workgroupId },
                claimOwnerIdField:{claimOwnerSelection: claimOwnerId}
            },
            messages: {
                workgroupIdField: {comboSelection:"You must supply a value for 'Workgroup'"},
                claimOwnerIdField: {claimOwnerSelection:"You must supply a value for 'Claim Owner'"}
            }
        });
        doRenderClaimHandlerDropDown(workgroupId);
    });


    function doRenderClaimHandlerDropDown(workgroupId){
        if((isWorkgroupEnable && workgroupId>0) || !isWorkgroupEnable){
            claimOwnerStore.removeAll();
            claimOwnerStore.load({ params : {"workgroupId":workgroupId, "insurerId":insurerId}});
            claimOwnerCombo.reset();
        }
    }
    
    //XXX 'input drop down' validation does not work as it should - check if this can be reomoved when ExtJs will be upgraded
    function validateComboBox(){
    	var mesBox = $("#OwnershippAssignmentMessageBox");
    	if ($("#claimOwnerComboId").val() == "--- Please Select ---" || $("#workgroupComboId").val() == "--- Please Select ---") {
    		mesBox.empty();
    		if($("#claimOwnerComboId").val() == "--- Please Select ---")
    			mesBox.append("You must supply a value for 'Claim Owner'\n<br/>").show();
    		if($("#workgroupComboId").val() == "--- Please Select ---")
    			mesBox.append("You must supply a value for 'Workgroup'\n<br/>").show();
    		return false;
    	} else {
    		mesBox.text("").show();
    		return true;
    	}
    		
    }

    function doAssignOwnershipSubmit(){
        actionPanel.registerAction("assignOwner");
        var co = $("[name='claimOwnerIdField']");
        var wo = $("[name='workgroupIdField']");
    	var mesBox = $("#OwnershippAssignmentMessageBox");
    	if (co.val() == "")
    		co.val(-1);
    	if (wo.val() == "")
    		wo.val(-1);
    	
    	 if (validateComboBox()) {
         	$("#formOwnershipAssignmentAction").submit();
         } 
        
    }

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="formOwnershipAssignmentAction" name="formOwnershipAssignmentAction" action="<%=request.getContextPath()%>/prv/processClaim.action" method="POST" onsubmit="return true;">
        <div class="form-container">
            <fieldset class="x-fieldset">
                <legend>Invoice Ownership - Action Required</legend>
                <div>
                    <s:hidden id="id" name="id" />
                    <s:hidden id="name" name="name" />
                    <div>
                        <div class="status-info">
                        <s:if test="insurer.workgroupEnable">
                        Assign the Workgroup and Claim Owner by using the drop down menus provided below, selecting a Workgroup will determine which Claims Handlers are displayed in the Claim Owner drop down menu.
                        </s:if>
                        <s:else>
                        Assign the Claim Owner by using the drop down menus provided below.
                        </s:else>
                        </div>

                        <div class="status-control-set">
                            <table class="status-table" border="0" cellpadding="0" cellspacing="0">
                                <s:if test="insurer.workgroupEnable">
                                    <tr>
                                        <td align="right" width="10%"><label>Workgroup : </label></td>
                                        <td width="20%"><div id="workgroupComboDiv"/></td>
                                        <td width="70%"></td>
                                    </tr>
                                </s:if>
                                <tr>
                                    <td align="right" width="10%"><label>Claim Owner : </label></td>
                                    <td width="20%"><div id="claimOwnerComboDiv"></div></td>
                                    <td width="70%"></td>
                                </tr>

                                <tr>
                                    <td colspan="3">
                                        <div class="no-format">
                                            <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3" class="choice" nowrap >
                                        <input type="button" id="AIOAssignOwnerButtonId" value="Assign Owner" onclick="return doAssignOwnershipSubmit();"/>
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
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>