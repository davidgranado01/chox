<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var claimOwnerId = -1;
    var isWorkgroupEnable = false;
    var selectedWorkgroupId = -1;
    var claimType;
    var enableManualInvoiceWorkgroups;
    
    Ext.onReady(function() {

    // GET CLAIM INFORMATION
        if($("#claimClaimOwnerId").val()!==null && $("#claimClaimOwnerId").val()!==""){
            claimOwnerId = $("#claimClaimOwnerId").val();
        }

        if($("#claimWorkgroupEnable").val()!==null && $("#claimWorkgroupEnable").val()!==""){
            isWorkgroupEnable = $("#claimWorkgroupEnable").val();
        }

        claimType = '<s:property value="claimType"/>';
        enableManualInvoiceWorkgroups = <s:property value="insurer.enableManualInvoiceWorkgroups"/>;
        if (claimType === 'Insurer Invoice' && isWorkgroupEnable && enableManualInvoiceWorkgroups == false) {
            isWorkgroupEnable = false;
        }
        if(isWorkgroupEnable){
            if($("#claimWorkgroupId").val()!==null && $("#claimWorkgroupId").val()!==""){
                selectedWorkgroupId = $("#claimWorkgroupId").val();

            }
        }

        $("#oasWorkgroupId").val(selectedWorkgroupId);


        // SETUP FORM VALIDATION
        var form = $("form#formOwnershipAction");
        form.validate(
        {
            errorLabelContainer: "#OwnershipMessageBox",
            rules: {
                oasWorkgroupId:{min:1},
                claimOwnerId:{min:1}
            },
            messages: {
                oasWorkgroupId: {min:"You must supply a value for 'Workgroup'"},
                claimOwnerId: {min:"You must supply a value for 'Claim Owner'"}
            }
        });
        choxJqueryHttpSubmit(form, function(){});
        doUpdateOwnershipShowClaimHandler(selectedWorkgroupId);
    });

    function doUpdateOwnershipWorkgroupChange(){
        if($("#oasWorkgroupId").val()!==null){
            selectedWorkgroupId = $("#oasWorkgroupId").val();
        }
        claimOwnerId = -1;
        doUpdateOwnershipShowClaimHandler(selectedWorkgroupId);
    }

    function doUpdateOwnershipShowClaimHandler(selectedWorkgroupId){
        var target = "#claimHandlerRoleUserDropDownDiv";
        var url = "/prv/p/ClaimHandlerRoleUserDropDownAction.action";
        var param = {"workgroupId":selectedWorkgroupId,"insurerId":<s:property value="insurer.id"/>};
        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
        });
    }

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formOwnershipAction" name="formOwnershipAction">
        <fieldset class="x-fieldset">
            <s:if test="(insurer.workgroupEnable && claimType.claimTypeValue!=10) || (claimType.claimTypeValue==10 && insurer.enableManualInvoiceWorkgroups)">
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
                    <s:if test="(insurer.workgroupEnable && claimType.claimTypeValue!=10) || (claimType.claimTypeValue==10 && insurer.enableManualInvoiceWorkgroups)">
                        Update the Workgroup or Claim Owner by using the drop down menus provided below, selecting a Workgroup will determine which Claims Handlers are displayed in the Claim Owner drop down menu.
                        </s:if>
                        <s:else>
                        Update the Claim Owner by using the drop down menus provided below.    
                        </s:else>
                        </div>
                    <div class="status-control-set">
                        <table class="status-table" width="100%">
                            <s:if test="(insurer.workgroupEnable && claimType.claimTypeValue!=10) || (claimType.claimTypeValue==10 && insurer.enableManualInvoiceWorkgroups)">
                                <tr>
                                    <td align="right" width="10%"><label>Workgroup:</label></td>
                                    <td width="20%">
                                        <s:select
                                            name="oasWorkgroupId"
                                            id="oasWorkgroupId"
                                            list="insurerWorkgroups"
                                            headerKey="-1"
                                            listKey="id"
                                            listValue="name"
                                            headerValue="-- Please Select --"
                                            onchange="doUpdateOwnershipWorkgroupChange()">
                                        </s:select>
                                    </td>
                                    <td width="70%"></td>
                                </tr>
                            </s:if>
                            <tr>
                                <td align="right" width="10%"><label>Claim Owner:</label></td>
                                <td width="20%"><div id="claimHandlerRoleUserDropDownDiv"></div></td>
                                <td width="70%"></td>
                            </tr>
                            <tr>
                                <td>
                                    <input id="assign" type="submit" value="Update"/>
                                </td>
                            </tr>
                        </table>
                        <div class="action-error-msg" id="OwnershipMessageBox"></div>
                    </div>
                </div>
            </div>
        </fieldset>
    </form>
</div>