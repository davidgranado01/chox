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

            $("#workgroupId").val(selectedWorkgroupId);
            
        }

        doShowClaimHandler(selectedWorkgroupId);
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

        if(isWorkgroupEnable && $("#workgroupId").val()<=0){
            bFlag = true;
        }

        return bFlag;
    }

    function doAssignOwnershipFormValidation(){
        
        var validateFlag = $("#formOwnershipAction").validate(
        {
            errorLabelContainer: "#OwnershipMessageBox",
            rules: {
                workgroupId:{required:isWorkgroupFieldValid},
                claimOwnerId:{min:1}
            },
            messages: {
                workgroupId: {required:"You must supply a value for 'Workgroup'"},
                claimOwnerId: {min:"You must supply a value for 'Claim Owner'"}
            }
        });

        return validateFlag;
        
    }

    function doWorkgroupChange(){

        if($("#workgroupId").val()!=null){
            selectedWorkgroupId = $("#workgroupId").val();
        }
        
        claimOwnerId = -1;
        doShowClaimHandler(selectedWorkgroupId);
    
    }

    function doShowClaimHandler(selectedWorkgroupId){
        $("#claimHandlerRoleUserDropDownDiv").load("ClaimHandlerRoleUserDropDownAction.action?workgroupId=" + selectedWorkgroupId + "&insurerId="+insurerId);
    }

    function doAssignOwnershipSubmit(a){
        
        registeAction(a);
        
        if(doAssignOwnershipFormValidation().form()){
            return true;
        }
        
        return false;
    }

</script>

<form onsubmit="return true;" action="user/ownershipAssignment.action" method="post" id="formOwnershipAction" name="formOwnershipAction">
    <fieldset class="x-fieldset">
        <legend>Claim Ownership - Action Required</legend>
        <div>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="actionName" name="actionName" value="assigned" />

            <input type="hidden" id="claimWorkgroupId" name="claimWorkgroupId" value="<s:property value="workgroup.id"/>">
            <input type="hidden" id="claimClaimOwnerId" name="claimClaimOwnerId" value="<s:property value="claimOwner.id"/>">
            <input type="hidden" id="claimWorkgroupEnable" name="claimWorkgroupEnable" value="<s:property value="insurer.workgroupEnable"/>">

            <div>
                <div class="status-info">
                Please assign the correct workgroup and claim owner to this claim and then click on the 'Assign Owner' button. If this claim has been assigned to the wrong workgroup, please use the 'More Actions' drop down to display the 'Remove Workgroup Mapping' action panel.
                </div>
                <div class="status-control-set">
                    <table class="status-table" width="100%">
                    <s:if test="insurer.workgroupEnable">
                    <tr>
                        <td width="200px"><label width="200px">Workgroup</label></td>
                        <td width="100%">
                            <s:select name="workgroupId" id="workgroupId"
                            list="workgroups" headerKey="" listKey="id" listValue="name"
                            headerValue="-- Please Select --" onchange="doWorkgroupChange()">
                            </s:select>
                        </td>
                    </tr>
                    </s:if>
                    <s:else>
                        <input type="hidden" id="workgroupId" name="workgroupId" value=""/>
                    </s:else>
                    <tr>
                        <td><label>Claim Owner</label></td>
                        <td><div id="claimHandlerRoleUserDropDownDiv"></div></td>
                    </tr>
                    <tr>
                        <td colspan="2" class="choice" nowrap>
                            <input id="assignAndProcess" type="submit" value="Assign Owner" onclick="javascript:return doAssignOwnershipSubmit('assigned_routed');"/>
                        </td>
                    </tr>
                        
                    </table>

                    <div class="errorBox" id="OwnershipMessageBox"></div>
                    <div id="ownership-submit-result" class="action_msg"><%= statusMsg%></div>
                </div>
            </div>

        </div>
    </fieldset>
</form>