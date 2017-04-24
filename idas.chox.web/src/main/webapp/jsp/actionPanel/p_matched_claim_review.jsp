<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var claimId = -1;
    var insurerId = -1;
    var matchedClaimOwnerId = -1;
    var matchedWorkgroupId=-1;
    var matchedClaimOwnerIdField=-1;
    var matchedWorkgroupIdField=-1;
    var isWorkgroupEnable = false;
    var isClaimOwnershipEnable = false;
    var matchedWorkgroupCombo;
    var matchedClaimOwnerStore;
    var matchedClaimOwnerCombo;
    var matchedWgrpJsonReader;
    var matchedClaimOwnerReader;
    var matchedWorkgroupStore;

    Ext.onReady(function() {

        // The 'setValue' function on the combo box doesn't work
        // as, fue to the asynchronous nature of the widget, the store may
        // not be loaded. Below is a patch to fix this problem.
        Ext.override(Ext.form.ComboBox, {
            setValue : function(v){
                //begin patch
                // Store not loaded yet? Set value when it *is* loaded.
                // Defer the setValue call until after the next load.
                if (this.store.getCount() === 0) {
                    this.store.on('load', this.setValue.createDelegate(this, [v]), null, {single: true});
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
        claimId = '<s:property value="id"/>';
        isWorkgroupEnable = ('<s:property value="insurer.workgroupEnable"/>' === 'true');
        isClaimOwnershipEnable = ('<s:property value="insurer.claimOwnershipEnable"/>' === 'true');

        if(isWorkgroupEnable) {
            matchedWorkgroupId = '<s:property value="workgroup.id"/>';
            matchedWgrpJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'text'},
                    {name:'value'}
                ]
            });

            matchedWorkgroupStore = new choxDataStore({
                url : "/prv/p/WorkgroupDropDownActionByInsurer3.action", 
                params : {"claimId":claimId},
                reader: matchedWgrpJsonReader
            });

            matchedWorkgroupCombo = new Ext.form.ComboBox({
                store: matchedWorkgroupStore,
                width: 200,
                renderTo: 'matchedWorkgroupComboDiv',
                valueField: 'text',
//                value: matchedWorkgroupId,
                id: 'matchedWorkgroupComboId',
                hiddenName: 'matchedWorkgroupIdField',
                displayField:'value',
                typeAhead: true,
                mode: 'local',
                editable:false,
                triggerAction: 'all',
                emptyText: '--- Please Select ---',
                forceSelection: true,
                listWidth: 200,
                selectOnFocus: true,
                listeners: {
                    select:function() {
                        if(this.getRawValue() === "") {
                            this.clearValue();
                            this.reset();
                            matchedWorkgroupId = -1;
                            matchedClaimOwnerId = -1;
                            matchedWorkgroupIdField=matchedWorkgroupId;
                            doRenderMatchedClaimHandlerDropDown(matchedWorkgroupId);
                        }else {
                            matchedWorkgroupId=this.value;
                            matchedWorkgroupIdField=matchedWorkgroupId;
                            matchedClaimOwnerId = -1;
                            doRenderMatchedClaimHandlerDropDown(matchedWorkgroupId);
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
            matchedWorkgroupStore.load({ params : {"claimId":claimId}});
            matchedWorkgroupCombo.setValue(matchedWorkgroupId);
        }
        
        if (isClaimOwnershipEnable) {
            matchedClaimOwnerId = '<s:property value="claimOwner.id"/>';
            // Add claim owner combo box
            matchedClaimOwnerReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields: [
                    {name:'id'},
                    {name:'name'}
                ]
            });

            matchedClaimOwnerStore = new choxDataStore({
                url : "/prv/p/ClaimHandlerRoleUserDropDownAction2.action", 
                params : {"workgroupId":matchedWorkgroupId, "insurerId":insurerId},
                reader : matchedClaimOwnerReader
            });

            matchedClaimOwnerCombo = new Ext.form.ComboBox({
                store: matchedClaimOwnerStore,
                width: 200,
                renderTo: 'matchedClaimOwnerComboDiv',
                valueField: 'id',
                id: 'matchedClaimOwnerComboId',
                hiddenName: 'matchedClaimOwnerIdField',
                displayField:'name',
                typeAhead: true,
                mode: 'local',
                listWidth: 200,
                forceSelection: true,
                triggerAction: 'all',
                emptyText: '--- Please Select ---',
                listeners: {
                    select: function () {
                        if(this.getRawValue() === "") {
                            this.clearValue();
                            this.reset();
                            matchedClaimOwnerId = -1;
                            matchedClaimOwnerIdField=matchedClaimOwnerId;
                        }else {
                            matchedClaimOwnerId = this.value;
                            matchedClaimOwnerIdField=matchedClaimOwnerId;
                        }
                    },
                    specialkey:function (el, e) {
                            if(e.keyCode === e.ENTER) {
                                e.preventDefault();
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
            doRenderMatchedClaimHandlerDropDown(matchedWorkgroupId);
        }

        // SETUP FORM VALIDATION
     });

    function doMatchedClaimReviewSubmit(){
        if (validateComboBox()) {
            Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
            choxJqueryHttpSubmit($("form#formMatchedClaimReviewAction"));
        }
        return false;
    }

    function validateComboBox(){
        var mesBox = $("#MatchedOwnershipMessageBox");
        mesBox.empty();
        if ($("#matchedClaimOwnerComboId").val() === "--- Please Select ---") {
            mesBox.append("You must supply a value for 'Claim Owner'\n<br/>").show();
            return false;
        } else {
            mesBox.text("").show();
            return true;
        }
            
    }

    function doRenderMatchedClaimHandlerDropDown(workgroupId){
        if((isWorkgroupEnable && matchedWorkgroupId>0) || !isWorkgroupEnable){
            matchedClaimOwnerStore.removeAll();
            matchedClaimOwnerStore.load({ params : {"workgroupId":workgroupId, "insurerId":insurerId}});
            matchedClaimOwnerCombo.reset();
            if (matchedClaimOwnerId > 0) {
                matchedClaimOwnerCombo.setValue(matchedClaimOwnerId);
            }
        }
    }
    
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formMatchedClaimReviewAction" name="formMatchedClaimReviewAction">
        <fieldset class="x-fieldset">
            <legend>Claim Review and Reserve</legend>
            <s:hidden id="claimMatchedReview" name="name" value="claimMatchedReview"/>
            <div>
                <div>
                    <div class="status-info">
                        This claim has been automatically matched and an acknowledegment sent to the CHO.<br> Please review this case and if required set a reserve within your claims system.
                    </div>
                    <div class="status-control-set">
                        <table class="status-table" width="100%">
                            <s:if test="insurer.workgroupEnable">
                                <tr>
                                    <td align="right" width="10%"><label>Workgroup:</label></td>
                                    <td width="20%"><div id="matchedWorkgroupComboDiv"/></td>
                                    <td width="70%"></td>
                                </tr>
                            </s:if>
                            <s:if test="insurer.claimOwnershipEnable">
                                <tr>
                                    <td align="right" width="10%"><label>Claim Owner:</label></td>
                                    <td width="20%"><div id="matchedClaimOwnerComboDiv"></div></td>
                                    <td width="70%"></td>
                                </tr>
                            </s:if>
                            <tr>
                                <td width="10%" align="right">
                                    <label>Reserve Value (£):</label>
                                </td>
                                <td>
                                    <input type="text" class="chox-ttxt" name="reserveValue" id="MatchedIndemityAmountId" value="<s:property value="indemnityAmount" />" onkeyup="extractNumber(this,2,true);"/>
                                </td>
                                <td></td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <input id="assign" type="submit" value="Confirm Claim Reviewed and Reserve Set" onclick="return doMatchedClaimReviewSubmit();"/>
                                </td>
                                <td></td>
                            </tr>
                        </table>
                        <div class="action-error-msg" id="MatchedOwnershipMessageBox"></div>
                    </div>
                </div>
            </div>
        </fieldset>
    </form>
</div>