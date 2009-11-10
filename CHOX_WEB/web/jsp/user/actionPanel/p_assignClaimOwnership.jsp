<%@ taglib uri="/struts-tags" prefix="s" %>

<%
String statusMsg = request.getParameter("statusMsg");
if(statusMsg==null){
    statusMsg = "";
}
%>

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

        // PAGE SETUP - BUTTON
        doAllowToChangeStatus();
        
        // PAGE SETUP - WORKGROUP ID
        if(isWorkgroupEnable){

            if($("#claimWorkgroupId").val()!=null && $("#claimWorkgroupId").val()!=""){
                selectedWorkgroupId = $("#claimWorkgroupId").val();
            }

            $("#ownershipAssignmentWorkgroupId").val(selectedWorkgroupId);
            
        }

        doOwnershipAssignmentShowClaimHandler(selectedWorkgroupId, insurerId);
        doAssignOwnershipFormValidation();

    });

    function doAllowToChangeStatus(){
        
        if(claimStatus=='ClaimUnacknowledgedUnassigned'){
            $("#assignAndProcess").attr("disabled", false);
        }else{
            $("#assignAndProcess").attr("disabled", true);
        }
        
    }
    
    function isWorkgroupFieldValid(){
        var bFlag = false;

        if(isWorkgroupEnable && $("#ownershipAssignmentWorkgroupId").val()<=0){
            bFlag = true;
        }

        return bFlag;
    }

    function doAssignOwnershipFormValidation(){
        
        var validateFlag = $("#formOwnershipAssignmentAction").validate(
        {
            errorLabelContainer: "#OwnershippAssignmentMessageBox",
            rules: {
                ownershipAssignmentWorkgroupId:{required:isWorkgroupFieldValid},
                claimOwnerId:{min:1}
            },
            messages: {
                ownershipAssignmentWorkgroupId: {required:"You must supply a value for 'Workgroup'"},
                claimOwnerId: {min:"You must supply a value for 'Claim Owner'"}
            }
        });

        return validateFlag;
        
    }

    function doOwnershipAssignmentWorkgroupChange(){

        if($("#ownershipAssignmentWorkgroupId").val()!=null){
            selectedWorkgroupId = $("#ownershipAssignmentWorkgroupId").val();
        }
        claimOwnerId = -1;
        doOwnershipAssignmentShowClaimHandler(selectedWorkgroupId, insurerId);
    
    }

    function doOwnershipAssignmentShowClaimHandler(selectedWorkgroupId, selectedInsurerId){
        $("#ownershipAssignmentClaimHandlerRoleUserDropDownDiv").load("ClaimHandlerRoleUserDropDownAction.action?workgroupId=" + selectedWorkgroupId + "&insurerId="+selectedInsurerId);
    }

    function doAssignOwnershipSubmit(a){

        registeAction(a);
        
        if(doAssignOwnershipFormValidation().form()){
            return true;
        }
        
        return false;
    }

</script>

<form onsubmit="return true;" action="user/ownershipAssignment.action" method="post" id="formOwnershipAssignmentAction" name="formOwnershipAssignmentAction">
    <fieldset class="x-fieldset">
        <legend>Claim Ownership - Action Required</legend>
        <div>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="actionName" name="actionName" value="assigned_routed" />

            <input type="hidden" id="claimWorkgroupId" name="claimWorkgroupId" value="<s:property value="workgroup.id"/>">
            <input type="hidden" id="claimClaimOwnerId" name="claimClaimOwnerId" value="<s:property value="claimOwner.id"/>">
            <input type="hidden" id="claimWorkgroupEnable" name="claimWorkgroupEnable" value="<s:property value="insurer.workgroupEnable"/>">

            <div>
                <div class="status-info">
                Please assign the claim owner for this claim and click on the 'Assign Owner' button. If this claim has been assigned to the incorrect Workgroup, please use the 'More Actions' drop down above, clicking on 'Re-assign Workgroup' to re-assign the claim's Workgroup.
                </div>
                <div class="status-control-set">
                    <table class="status-table" width="100%">
                    <s:if test="insurer.workgroupEnable">
                    <tr>
                        <td width="200px"><label width="200px">Workgroup</label></td>
                        <td width="100%">
                            <s:select name="ownershipAssignmentWorkgroupId" id="ownershipAssignmentWorkgroupId"
                            list="workgroups" headerKey="" listKey="id" listValue="name"
                            headerValue="-- Please Select --" onchange="doOwnershipAssignmentWorkgroupChange()">
                            </s:select>
                        </td>
                    </tr>
                    </s:if>
                    <s:else>
                        <input type="hidden" id="workgroupId" name="workgroupId" value=""/>
                    </s:else>
                    <tr>
                        <td><label>Claim Owner</label></td>
                        <td><div id="ownershipAssignmentClaimHandlerRoleUserDropDownDiv"></div></td>
                    </tr>
                    <tr>
                        <td colspan="2" class="choice" nowrap>
                            <input id="assignAndProcess" type="submit" value="Assign Owner" onclick="javascript:return doAssignOwnershipSubmit('assigned_routed');"/>
                        </td>
                    </tr>
                        
                    </table>

                    <div class="errorBox" id="OwnershippAssignmentMessageBox"></div>
                    <div id="ownership-submit-result" class="action_msg"><%= statusMsg%></div>
                </div>
            </div>

        </div>
    </fieldset>
</form>