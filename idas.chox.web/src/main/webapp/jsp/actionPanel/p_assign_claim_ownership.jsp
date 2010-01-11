<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var selectedWorkgroupId = -1;
    var claimOwnerId = -1;
    var isWorkgroupEnable = false;

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
                claimOwnerId:{min:1}
            },
            messages: {
                oasWorkgroupId: {min:"You must supply a value for 'Workgroup'"},
                claimOwnerId: {min:"You must supply a value for 'Claim Owner'"}
            }
        });

        // RENDER CLAIM HANDLER DROP DOWN
        doRenderClaimHandlerDropDown(selectedWorkgroupId);

    });
    
    function doOwnershipAssignmentWorkgroupChange(){
        
        if($("#oasWorkgroupId").val()!=null){
            selectedWorkgroupId = $("#oasWorkgroupId").val();
        }
        claimOwnerId = -1;
        doRenderClaimHandlerDropDown(selectedWorkgroupId);
    
    }

    function doRenderClaimHandlerDropDown(selectedWorkgroupId){
        
        if(selectedWorkgroupId>0){
            
            var target = "#ownershipAssignmentClaimHandlerRoleUserDropDownDiv";
            var url = "<%=request.getContextPath()%>/prv/p/ClaimHandlerRoleUserDropDownAction.action";
            var param = {"workgroupId":selectedWorkgroupId ,"insurerId":<s:property value="insurer.id"/>};
            ajax.loadHtml(url, param, function(data){
                $(target).html(data);
                $("form#formOwnershipAssignmentAction #claimOwnerId").val(claimOwnerId);
            });

        }
    }

    function doAssignOwnershipToFnolSubmit(){
        actionPanel.registerAction("referFNOL");
        $("form#formOwnershipAssignmentAction #claimOwnerId").rules("remove");
    }

    function doAssignOwnershipSubmit(){
        actionPanel.registerAction("assignOwner");
        $("form#formOwnershipAssignmentAction #claimOwnerId").rules("add", {
            min:1
        })
    }

</script>

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
                        Please assign the claim owner for this claim and click on the 'Assign Owner' button. If the claim needs registering by FNOL, please use the 'Refer To FNOL' button. If this claim has been assigned to the incorrect Workgroup, please use the 'More Actions' drop down above, clicking on 'Re-assign Workgroup' to re-assign the claim's Workgroup.
                    </div>
                    <div class="status-control-set">
                        <table class="status-table" width="100%" border="0" cellpadding="0" cellspacing="0">

                            <s:if test="insurer.workgroupEnable">
                                <tr>
                                    <td><label>Workgroup</label></td>
                                    <td>
                                        <s:select name="oasWorkgroupId" id="oasWorkgroupId"
                                                  list="workgroups" headerKey="-1" listKey="id" listValue="name"
                                                  headerValue="- Please Select -" onchange="doOwnershipAssignmentWorkgroupChange()">
                                        </s:select>
                                    </td>
                                </tr>
                            </s:if>

                            <tr>
                                <td><label>Claim Owner</label></td>
                                <td><div id="ownershipAssignmentClaimHandlerRoleUserDropDownDiv"></div></td>
                            </tr>
                            <tr>
                                <td colspan="2" class="choice" nowrap>
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