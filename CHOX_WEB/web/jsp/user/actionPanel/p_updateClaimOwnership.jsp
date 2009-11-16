<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var insurerId = <s:property value="insurer.id"/>;
    var claimStatus =  "<s:property value='status'/>";
    var selectedWorkgroupId = -1;
    var claimOwnerId = -1;
    var isWorkgroupEnable = false;
    var editMode = -1;

    $(document).ready(function(){

        // GET CLAIM INFORMATION
        if($("#claimClaimOwnerId").val()!=null && $("#claimClaimOwnerId").val()!=""){
            claimOwnerId = $("#claimClaimOwnerId").val();
        }

        if($("#claimWorkgroupEnable").val()!=null && $("#claimWorkgroupEnable").val()!=""){
            isWorkgroupEnable = $("#claimWorkgroupEnable").val();
        }

        // PAGE SETUP - WORKGROUP ID
        if(isWorkgroupEnable){
            
            if($("#claimWorkgroupId").val()!=null && $("#claimWorkgroupId").val()!=""){
                selectedWorkgroupId = $("#claimWorkgroupId").val();
            }

        }
        
        doUpdateOwnershipShowClaimHandler(selectedWorkgroupId, insurerId);
        doUpdateOwnershipFormValidation();

        $("#uosWorkgroupId").val(selectedWorkgroupId);
        
    });

    function isUpdateOwnershipWorkgroupFieldValid(){
        var bFlag = false;

        if(isWorkgroupEnable && $("#uosWorkgroupId").val()<=0){
            bFlag = true;
        }

        return bFlag;
    }

    function doUpdateOwnershipFormValidation(){

        var validateFlag = $("#formOwnershipAction").validate(
        {
            errorLabelContainer: "#OwnershipMessageBox",
            rules: {
                uosWorkgroupId:{required:isUpdateOwnershipWorkgroupFieldValid},
                claimOwnerId:{min:1}
            },
            messages: {
                uosWorkgroupId: {required:"You must supply a value for 'Workgroup'"},
                claimOwnerId: {min:"You must supply a value for 'Claim Owner'"}
            }
        });

        return validateFlag;

    }

    function doUpdateOwnershipWorkgroupChange(){

        if($("#uosWorkgroupId").val()!=null){
            selectedWorkgroupId = $("#uosWorkgroupId").val();
        }

        claimOwnerId = -1;
        doUpdateOwnershipShowClaimHandler(selectedWorkgroupId, insurerId);

    }

    function doUpdateOwnershipShowClaimHandler(selectedWorkgroupId, selectedInsurerId){
        $("#claimHandlerRoleUserDropDownDiv").load("ClaimHandlerRoleUserDropDownAction.action?workgroupId=" + selectedWorkgroupId + "&insurerId="+selectedInsurerId);
    }

    function doUpdateOwnershipSubmit(a){

        registeAction(a);

        if(doUpdateOwnershipFormValidation().form()){
            return true;
        }

        return false;
    }

</script>

<form onsubmit="return true;" action="user/updateOwnershipAssignment.action" method="post" id="formOwnershipAction" name="formOwnershipAction">
    <fieldset class="x-fieldset">
        <legend>Update Claim Ownership - Action Required</legend>
        
        <div>
            <s:hidden id="claimId" name="id" />
            <input type="hidden" id="claimWorkgroupId" name="claimWorkgroupId" value="<s:property value="workgroup.id"/>">
            <input type="hidden" id="claimClaimOwnerId" name="claimClaimOwnerId" value="<s:property value="claimOwner.id"/>">
            <input type="hidden" id="claimWorkgroupEnable" name="claimWorkgroupEnable" value="<s:property value="insurer.workgroupEnable"/>">
            <div>
                
                <div class="status-info">
                Re-assign the Claim Owner by selecting the relevant Workgroup and Claims Handler from the selections below.
                </div>
                
                <div class="status-control-set">
                    <table class="status-table" width="100%">

                    <!-- MANTIS ID:820
                    <s:if test="insurer.workgroupEnable">
                    <tr>
                        <td width="200px"><label>Workgroup</label></td>
                        <td width="100%">
                            <s:select name="uosWorkgroupId" id="uosWorkgroupId"
                            list="workgroups" headerKey="" listKey="id" listValue="name"
                            headerValue="-- Please Select --" onchange="doUpdateOwnershipWorkgroupChange()">
                            </s:select>
                        </td>
                    </tr>
                    </s:if>
                    <s:else>
                        <tr><td colspan="2">
                        <input type="hidden" id="uosWorkgroupId" name="uosWorkgroupId" value=""/>
                        </td></tr>
                    </s:else>
                    !-->
                    <tr>
                        <td colspan="2">
                        <input type="hidden" id="uosWorkgroupId" name="uosWorkgroupId" value=""/>
                        </td>
                    </tr>

                    <tr>
                        <td><label>Claim Owner</label></td>
                        <td><div id="claimHandlerRoleUserDropDownDiv"></div></td>
                    </tr>
                    <tr>
                        <td colspan="2" class="choice" nowrap>
                            <input id="assign" type="submit" value="Update Owner" onclick="javascript:return doUpdateOwnershipSubmit('assigned');"/>
                        </td>
                    </tr>

                    </table>
                    <div class="errorBox" id="OwnershipMessageBox"></div>
                </div>

            </div>

        </div>
            
    </fieldset>
</form>