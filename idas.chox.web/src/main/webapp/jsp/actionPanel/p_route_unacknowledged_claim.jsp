<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    Ext.onReady(function(){

        var workgroupJsonReader = new Ext.data.JsonReader({
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
            reader : workgroupJsonReader
        });

        var workgroupCombo = new Ext.form.ComboBox({

            store: workgroupStore,
            renderTo: 'workgroupSelectionHolder',
            valueField: 'text',
            id: 'workgroupComboId',
            hiddenName: 'workgroupId',
            displayField:'value',
            typeAhead: true,
            autoWidth: true,
            listWidth: 200,
            width: 200,
            mode: 'local',
            triggerAction: 'all',
            forceSelection : true,
            emptyText: '--- Please Select ---',
            listeners: {
                blur: function () {
                        if(this.getRawValue() === "") {
                            this.clearValue(); this.reset();
                        }
                },
                specialkey:function (el, e) {
                            if(e.keyCode === e.ENTER) {
                                e.preventDefault();
                            }
                }
            }
        });

        workgroupStore.load({params : {"claimId":<s:property value="id"/>}});

        var rejectionDescField = new Ext.form.TextArea({
            name             : 'rejectionDescription',
            id               : 'rejecDescId',
            width            :  350,
            height           :  80,
            allowBlank       :  false,
            renderTo         : 'rejectionDescId',
            disabled         : '<s:property value="rejectButtonEnabled"/>' === 'false'
        });

    });

    function validateRejectionComboBox(){
        var msgBox = $("#RouteUnacknowledgedUnroutedClaimMessageBox");
        msgBox.text("");
        if ($("#reasonOfRejectionId").val() === "-1" || $("#rejecDescId").val() === "" ) {
            if($("#reasonOfRejectionId").val() === "-1")
                msgBox.text("You must choose a 'Reason For Rejection'").append('<br/>').show();
            if($("#rejecDescId").val() === "" && msgBox.text().indexOf("Supporting Rejection Notes") === -1 )
                msgBox.append("You must enter 'Supporting Rejection Notes'").show();     
            return false;
        } else {
            msgBox.text("").show();
            return true;
        }
    }
    
    function validateWorkgroupComboBox(){
        var msgBox = $("#RouteUnacknowledgedUnroutedClaimMessageBox");
        if ($("#workgroupComboId").val() === "--- Please Select ---") {
            msgBox.text("You must select a 'Workgroup'").show();
            return false;
        } else {
            msgBox.text("").show();
            return true;
        }
    }
    
    function doClaimUnacknowledgedFormSubmit(action){
        actionPanel.registerAction(action);
        if(action === 'rejectClaim' && validateRejectionComboBox()){
            Ext.MessageBox.confirm('Confirm', 'Are you sure you want to reject this claim?', rejectClaim );
        }else if(action === 'assignWorkgroup' && validateWorkgroupComboBox()){
            Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
            choxJqueryHttpSubmit($("form#routeUnacknowledgedUnroutedClaim"));
        }
        return false;
    }

    function rejectClaim(btn) {
        if (btn === 'yes')    {
            Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
            choxJqueryHttpSubmit($("form#routeUnacknowledgedUnroutedClaim"));
        }
        return false;
    }
    
    var reasonOfRejectionDescReader = new Ext.data.JsonReader({
        fields:[{name:'id'},{name:'description'}]
    });
    
    var reasonOfRejectionDescStore = new Ext.data.Store({
        data : Ext.util.JSON.decode('<s:property value="jsonReasonOfClaimRejectionDesc" escapeHtml="false"/>'),
        reader : reasonOfRejectionDescReader
    });
    
    function refreshDesc(id){
        reasonOfRejectionDescStore.each(function(rec) {
            if(id === rec.json.text){
                Ext.getCmp('rejecDescId').setValue(rec.json.value);
            }
        });
        if(id === -1 || id === '')
            Ext.getCmp('rejecDescId').setValue("");
    }

</script>
<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="routeUnacknowledgedUnroutedClaim" name="routeUnacknowledgedUnroutedClaim" action="<%=request.getContextPath()%>/prv/processClaim.action" method="POST">

        <fieldset class="x-fieldset">
            <legend>Claim Routing - Action Required</legend>
            <div>
                <div class="status-info">
                    Please select the 'Workgroup' in order to route the claim to the relevant handling team.<br>
                    <s:if test="rejectButtonEnabled">
                        Alternatively, if you would like to reject the claim back to the CHO, then select a 'Reason For Rejection'.
                    </s:if>
                        <s:elseif test="isSubscriberClaim">
                            This claim cannot be rejected as a Subscriber claim must be routed before it can be rejected.
                        </s:elseif>
                        <s:elseif test="isFixedFeeClaim">
                            <s:if test="slaExtDays > 0">
                                This claim cannot be rejected as the Fixed Fee notification <s:property value="fixedFeeSlaDays"/> day SLA + <s:property value="slaExtDays"/> day extension has passed.
                            </s:if>
                            <s:else>
                                This claim cannot be rejected as the Fixed Fee notification <s:property value="fixedFeeSlaDays"/> day SLA has passed.
                            </s:else>
                        </s:elseif>
                </div>
                <s:hidden name="id" id="claimId" />
                <s:hidden name="name" id="name" />
                <div class="status-control-set">
                    <table class="status-table">
                        <tr>
                            <td width="10%" align="right">
                                <label >Workgroup</label>
                            </td>
                            <td width="20%" align="left">
                                <div id="workgroupSelectionHolder"></div>
                            </td>
                            <td width="70%"></td>
                        </tr>
                        <tr>
                            <td width="10%" align="right">
                                <label >Reason For Rejection</label>
                            </td>
                            <td width="20%" align="left">
                                <div id="ReasonOfRejectionDiv">
                                    <s:if test="rejectButtonEnabled">
                                        <s:select
                                            name="reasonOfRejectionId"
                                            id="reasonOfRejectionId"
                                            list="reasonOfClaimRejectionsRestricted"
                                            listKey="id"
                                            listValue="rorName"
                                            onchange="refreshDesc(this.value)"
                                            headerKey="-1"
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
                                            onchange="refreshDesc(this.value)"
                                            headerKey="-1"
                                            disabled="true"
                                            headerValue="N/A"
                                            emptyOption="false">
                                        </s:select>

                                    </s:else>
                                </div>
                            </td>
                            <td width="70%"></td>
                        </tr>
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
                            <td colspan="3" class="choice" nowrap>
                                <input type="button" id="RUCAssignWorkgroupButtonId" value="Assign Workgroup" onclick="event.preventDefault(); doClaimUnacknowledgedFormSubmit('assignWorkgroup');"/>
                                <input type="button" id="RUCRejectClaimButtonId" value="Reject Claim" <s:if test="rejectButtonEnabled == false">disabled</s:if> onclick="event.preventDefault(); doClaimUnacknowledgedFormSubmit('rejectClaim');"/>
                            </td>
                        </tr>
                    </table>
                    <div id="RouteUnacknowledgedUnroutedClaimMessageBox" class="action-error-msg"></div>
                </div>
            </div>
        </fieldset>
    </form>
</div>