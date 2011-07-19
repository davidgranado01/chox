<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    Ext.onReady(function(){
        //        Ext.BLANK_IMAGE_URL = 'images/s.gif';

        var workgroupJsonReader = new Ext.data.JsonReader({
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
            ({url : "<%= request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer.action", method:'GET'}),
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
            //                                selectOnFocus: true,
            //                                forceSelection: true,
            //                                allowBlank: false
            listeners: {
                blur: function () {
                    if(this.getRawValue() == "") {
                        this.clearValue(); this.reset();
                    }
                }
            }
        });

        $.validator.addMethod("workgroupSelection",
        function(value) {
            if(value === "") {
                return false;
            }
            return true;
        }, "You must select a 'Workgroup'"
    );

        workgroupStore.load({params : {"claimId":<s:property value="id"/>}});


        var form = $("form#routeUnacknowledgedUnroutedClaim");
        form.validate(
        {
            errorLabelContainer: "#RouteUnacknowledgedUnroutedClaimMessageBox",
            rules: {
                workgroupId: {workgroupSelection: document.getElementById('workgroupComboId')},
                reasonOfRejectionId: {required: true}
            },
            messages: {
                workgroupId:{workgroupSelection:"You must select a 'Workgroup'."},
                reasonOfRejectionId: {required:"You must choose a 'Reason For Rejection'"}
            }
        });

    });

    function doClaimUnacknowledgedFormSubmit(action){
        actionPanel.registerAction(action);
        doClaimUnacknowledgedValidationSetup(action);

        if($("#routeUnacknowledgedUnroutedClaim").valid()){

            if(action === 'rejectClaim'){
                Ext.MessageBox.confirm('Confirm', 'Are you sure you want to reject this claim?', rejectClaim );
            }else if(action === 'assignWorkgroup'){
                Ext.get('claimDetailScreenDiv').mask("Reloading Claim ...");
                $("form#routeUnacknowledgedUnroutedClaim").submit();
            }
        }
    }

    function rejectClaim(btn) {
        if (btn == 'yes')    {
            Ext.get('claimDetailScreenDiv').mask("Reloading Claim ...");
            $("form#routeUnacknowledgedUnroutedClaim").submit();
        }
        return false;
    }

    function doClaimUnacknowledgedValidationSetup(action) {

        var settings = $('form#routeUnacknowledgedUnroutedClaim').validate().settings;
        // ADD NEW VALIDATION PER SUBMIT TYPE
        if(action === 'rejectClaim'){
            delete settings.rules.workgroupId;
            $("form#routeUnacknowledgedUnroutedClaim #reasonOfRejectionId").rules("add", {required: true});
        } else if(action === 'assignWorkgroup') {
            $("form#routeUnacknowledgedUnroutedClaim #reasonOfRejectionId").rules("remove");
            settings.rules.workgroupId = {workgroupSelection: document.getElementById('workgroupComboId')};
        }

    }

</script>
<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="routeUnacknowledgedUnroutedClaim" name="routeUnacknowledgedUnroutedClaim" action="<%=request.getContextPath()%>/prv/processClaim.action" method="POST">

        <fieldset class="x-fieldset">
            <legend>Claim Routing - Action Required</legend>
            <div>
                <div class="status-info">
                    Please select the 'Workgroup' in order to route the claim to the relevant handling team.<br>
                    Alternatively, if you would like to reject the claim back to the CHO, then select a 'Reason for Rejection'.
                </div>
                <s:hidden name="id" id="claimId" />
                <s:hidden name="name" id="name" />
                <!--s:hidden name="workgroupId" value="-1"/-->
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
                                <label >Reason for Rejection</label>
                            </td>
                            <td width="20%" align="left">
                                <div id="ReasonOfRejectionDiv">
                                    <s:select
                                        name="reasonOfRejectionId"
                                        id="reasonOfRejectionId"
                                        list="reasonOfClaimRejectionsRestricted"
                                        listKey="id"
                                        listValue="name"
                                        headerKey=""
                                        headerValue="N/A"
                                        emptyOption="false">
                                    </s:select>
                                </div>
                            </td>
                            <td width="70%"></td>
                        <tr>

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
                                <input type="button" id="RUCAssignWorkgroupButtonId" value="Assign Workgroup" onclick="doClaimUnacknowledgedFormSubmit('assignWorkgroup');"/>
                                <input type="button" id="RUCRejectClaimButtonId" value="Reject Claim" onclick="doClaimUnacknowledgedFormSubmit('rejectClaim');"/>
                            </td>
                        </tr>
                    </table>
                    <div id="RouteUnacknowledgedUnroutedClaimMessageBox" class="action-error-msg"></div>
                </div>
            </div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>