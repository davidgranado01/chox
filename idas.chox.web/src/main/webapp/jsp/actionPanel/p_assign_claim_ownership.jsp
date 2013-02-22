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
        isWorkgroupEnable = ('<s:property value="insurer.workgroupEnable"/>' == 'true');

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
            ({url : "<%= request.getContextPath()%>/prv/p/ClaimHandlerRoleUserDropDownAction2.action", method:'GET', params : {"workgroupId":selectedWorkgroupId, "insurerId":insurerId}}),
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
        
        var rejectionDescField = new Ext.form.TextArea({
            name             : 'rejectionDescription',
            id               : 'rejecDescId',
            width            :  350,
            height           :  80,
            allowBlank       :  false,
            renderTo         : 'rejectionDescId',
            disabled         : '<s:property value="rejectButtonEnabled"/>' == 'false'
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

            var workgroupStore = new Ext.data.Store({
                proxy : new Ext.data.HttpProxy
                ({url : "<%= request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer.action", method:'GET', params : {"orgId":insurerId}}),
                reader: wgrpJsonReader
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
                        doRenderClaimHandlerDropDown(workgroupCombo.getValue());
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
     
        // PREPARE RECORDS
        if ($("#claimClaimOwnerId").val()!=null && $("#claimClaimOwnerId").val()!=""){
            claimOwnerId = $("#claimClaimOwnerId").val();
        }

        if ($("#claimWorkgroupEnable").val()!=null && $("#claimWorkgroupEnable").val()!=""){
            isWorkgroupEnable = ($("#claimWorkgroupEnable").val() == 'true');
        }

        if (isWorkgroupEnable) {
            if($("#claimWorkgroupId").val()!=null && $("#claimWorkgroupId").val()!=""){
                selectedWorkgroupId = $("#claimWorkgroupId").val();
                $("#oasWorkgroupId").val(selectedWorkgroupId);
            }
        }

        // DECLARE FORM VALIDATION
        var form = $("form#formOwnershipAssignmentAction");

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
        if((isWorkgroupEnable && selectedWorkgroupId>0) || !isWorkgroupEnable){
            claimOwnerStore.removeAll();
            claimOwnerStore.load({ params : {"workgroupId":selectedWorkgroupId, "insurerId":insurerId}});
            claimOwnerCombo.reset();
        }
    }
    
    function validateComboBox(){
        var mesBox = $("#OwnershippAssignmentMessageBox");
        mesBox.empty();
        if ($("#claimOwnerComboId").val() == "--- Please Select ---") {
            mesBox.append("You must supply a value for 'Claim Owner'\n<br/>").show();
            return false;
        } else if ($("#workgroupComboId").val() == "--- Please Select ---") {
            mesBox.append("You must supply a value for 'Work Group'\n<br/>").show();
            return false;
        } else {
            mesBox.text("").show();
            return true;
        }
            
    }
    
    function doAssignOwnershipToFnolSubmit(){
        var coh = $("[name='claimOwnerId']");
        var mesBox = $("#OwnershippAssignmentMessageBox");
        if (coh.val() == "")
            coh.val(-1);
        actionPanel.registerAction("referFNOL");
         if ($("#workgroupComboId").val() != "--- Please Select ---") {
             mesBox.text("").show();
             Ext.get('claimDetailScreenDiv').mask("Reloading Claim ...");
             $("#formOwnershipAssignmentAction").submit();
         } else {
             mesBox.text("You must supply a value for 'Work Group'").show();
         }
        
    }
    
    function doAssignOwnershipRejectSubmit(){
        actionPanel.registerAction("rejectClaim");
        $("#OwnershippAssignmentMessageBox").text("");
            if($("#reasonOfRejectionId").val() == "-1" || $("#rejecDescId").val() == "" ) {
                 if($("#reasonOfRejectionId").val() == "-1")
                    $("#OwnershippAssignmentMessageBox").text("You must choose a 'Reason For Rejection'").append('<br/>').show();
                 if($("#rejecDescId").val() == "" && $("#OwnershippAssignmentMessageBox").text().indexOf("Supporting Rejection Notes") == -1 )
                        $("#OwnershippAssignmentMessageBox").append("You must enter 'Supporting Rejection Notes'").show();             
            } else {
                $("#OwnershippAssignmentMessageBox").text("").show();
                Ext.MessageBox.confirm('Confirm', 'Are you sure you want to reject this claim?', rejectClaim );
            }
    }
    
    function rejectClaim(btn) {
        if (btn == 'yes')    {
            Ext.get('claimDetailScreenDiv').mask("Reloading Claim ...");
            $("form#formOwnershipAssignmentAction").submit();
        }
    }

    function doAssignOwnershipSubmit(){
        actionPanel.registerAction("assignOwner");
        if (validateComboBox()) {
            Ext.get('claimDetailScreenDiv').mask("Reloading Claim ...");
            $("#formOwnershipAssignmentAction").submit();
        }
    }
    
    var reasonOfRejectionDescReader = new Ext.data.JsonReader({
        fields:[{name:'id'},{name:'description'}]
    });
    
    var reasonOfRejectionDescStore = new Ext.data.Store({
        data : Ext.util.JSON.decode('<s:property value="jsonReasonOfClaimRejectionDesc" escape="false"/>'),
        reader : reasonOfRejectionDescReader
    });
    
    function refreshDesc(id){
        reasonOfRejectionDescStore.each(function(rec) {
            if(id == rec.json.text){
                Ext.getCmp('rejecDescId').setValue(rec.json.value);
            }
        });
        if(id == -1 || id == '')
            Ext.getCmp('rejecDescId').setValue("");
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
                            <s:if test="insurerIsFnolEnabled && insurer.workgroupEnable">
                                Please assign the claim owner for this claim and click on the 'Assign Owner' button.
                                If the claim needs registering by FNOL, please use the 'Refer To FNOL' button (please note that the
                                FNOL team the claim is referred to is based on the Workgroup assigned to the claim).
                                If this claim has been assigned to the incorrect Workgroup, please use the 'Workgroup'
                                drop down menu below to re-assign the Workgroup before referring the claim to FNOL.<br>
                            </s:if>
                            <s:elseif test="insurerIsFnolEnabled && !insurer.workgroupEnable">
                                Please assign the claim owner for this claim and click on the 'Assign Owner' button.
                                If the claim needs registering by FNOL, please use the 'Refer To FNOL' button.<br>
                            </s:elseif>
                            <s:elseif test="!insurerIsFnolEnabled && insurer.workgroupEnable">
                                Please assign the claim owner for this claim and click on the 'Assign Owner' button.
                                If this claim has been assigned to the incorrect Workgroup, please use the 'Workgroup'
                                drop down menu below to re-assign the Workgroup.<br>
                            </s:elseif>
                            <s:else>
                                Please assign the claim owner for this claim and click on the 'Assign Owner' button.<br>
                            </s:else>
                            <s:if test="rejectButtonEnabled">
                                Alternatively, if you would like to reject the claim back to the CHO, then select a 'Reason For Rejection'.
                            </s:if>
                            <s:elseif test="isSubscriberClaim">
                                <s:if test="slaExtDays > 0">
                                    This claim cannot be rejected as the Subscriber notification 5 day SLA + <s:property value="slaExtDays"/> day extension has passed.
                                </s:if>
                                <s:else>
                                    This claim cannot be rejected as the Subscriber notification 5 day SLA has passed.
                                </s:else>
                            </s:elseif>
                            <s:elseif test="isFixedFeeClaim">
                                <s:if test="slaExtDays > 0">
                                    This claim cannot be rejected as the Fixed Fee notification 14 day SLA + <s:property value="slaExtDays"/> day extension has passed.
                                </s:if>
                                <s:else>
                                    This claim cannot be rejected as the Fixed Fee notification 14 day SLA has passed.
                                </s:else>
                            </s:elseif>
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
                                    <td align="right" width="10%">
                                        <label >Reason For Rejection</label>
                                    </td>
                                    <td align="left" width="20%">
                                        <div id="ReasonOfRejectionDiv">
                                            <s:if test="rejectButtonEnabled">
                                                <s:select
                                                    name="reasonOfRejectionId"
                                                    id="reasonOfRejectionId"
                                                    list="reasonOfClaimRejectionsRestricted"
                                                    listKey="id"
                                                    listValue="rorName"
                                                    headerKey="-1"
                                                    onchange="refreshDesc(this.value)"
                                                    headerValue="N/A"
                                                    emptyOption="false">
                                                </s:select>
                                            </s:if>
                                            <s:else>
                                                <s:select
                                                    name="reasonOfRejectionId"
                                                    id="reasonOfRejectionId"
                                                    list="reasonOfClaimRejectionsRestricted"
                                                    listKey="id"
                                                    listValue="rorName"
                                                    headerKey="-1"
                                                    headerValue="N/A"
                                                    onchange="refreshDesc(this.value)"
                                                    disabled="true"
                                                    emptyOption="false">
                                                </s:select>
                                            </s:else>
                                        </div>
                                    </td>
                                    <td width="70%"></td>
                                <tr>
                                <tr>
                                <td align="right" valign="top"><label class="std-label-ro">Supporting Rejection Note&nbsp;&nbsp;</label></td>
                                    <td>
                                        <div id="rejectionDescId"/>
                                    </td>
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
                                        <input type="button" id="ACOAAssignOwnerButtonId" value="Assign Owner" onclick="return doAssignOwnershipSubmit();"/>
                                        <s:if test="insurerIsFnolEnabled">
                                            <input type="button" id="ACOAReferToFnolButtonId" value="Refer to FNOL" onclick="return doAssignOwnershipToFnolSubmit();" />
                                        </s:if>
                                        <input type="button" id="ACOARejectClaimButtonId" value="Reject Claim" <s:if test="rejectButtonEnabled == false">disabled='true'</s:if> onclick="return doAssignOwnershipRejectSubmit();"/>
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