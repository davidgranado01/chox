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
        
        // PAGE SETUP - WORKGROUP ID
        if(isWorkgroupEnable){
            if($("#claimWorkgroupId").val()!=null && $("#claimWorkgroupId").val()!=""){
                selectedWorkgroupId = $("#claimWorkgroupId").val();
            }            
        }

        doOwnershipAssignmentShowClaimHandler(selectedWorkgroupId, insurerId);
        doAssignOwnershipFormValidation();

    });
    
    function isWorkgroupFieldValid(){
        var bFlag = false;

        if(isWorkgroupEnable && $("#oasWorkgroupId").val()<=0){
            bFlag = true;
        }

        return bFlag;
    }

    function doAssignOwnershipFormValidation(){

        var validateFlag = $("#formOwnershipAssignmentAction").validate(
        {
            errorLabelContainer: "#OwnershippAssignmentMessageBox",
            rules: {
                oasWorkgroupId:{required:isWorkgroupFieldValid},
                claimOwnerId:{min:1}
            },
            messages: {
                oasWorkgroupId: {required:"You must supply a value for 'Workgroup'"},
                claimOwnerId: {min:"You must supply a value for 'Claim Owner'"}
            }
        });

        return validateFlag;
    }

    function doOwnershipAssignmentWorkgroupChange(){
        
        if($("#oasWorkgroupId").val()!=null){
            selectedWorkgroupId = $("#oasWorkgroupId").val();
        }
        claimOwnerId = -1;
        doOwnershipAssignmentShowClaimHandler(selectedWorkgroupId, insurerId);
    
    }

    function doOwnershipAssignmentShowClaimHandler(selectedWorkgroupId, selectedInsurerId){
        // Mantis Issue: 0000913
        if(selectedWorkgroupId>0){
            var sLocaltion = "#ownershipAssignmentClaimHandlerRoleUserDropDownDiv";
            var sAction = "ClaimHandlerRoleUserDropDownAction.action";
            var sparameters = "workgroupId=" + selectedWorkgroupId + "&insurerId="+selectedInsurerId;
            doSectionLoad(sLocaltion, sAction, sparameters);
        }
    }

    function doAssignOwnershipToFnolSubmit(a){
        registeAction(a);
        $("form#formOwnershipAssignmentAction #claimOwnerId").rules("remove");
        $("form#formOwnershipAssignmentAction #claimOwnerId").val("");
        $("#formOwnershipAssignmentAction").submit();
        return true;
    }

    function doAssignOwnershipSubmit(a){

        registeAction(a);

        $("form#formOwnershipAssignmentAction #claimOwnerId").rules("add", {
            min:1
        })

        if(doAssignOwnershipFormValidation().form()){
            return true;
        }

        return true;
    }

</script>

<form onsubmit="return true;" action="user/ownershipAssignment.action" method="post" id="formOwnershipAssignmentAction" name="formOwnershipAssignmentAction">
    <fieldset class="x-fieldset">
        <legend>Claim Ownership - Action Required</legend>
        <div>
            
            <s:hidden id="claimId" name="id" />
            <input type="hidden" id="claimWorkgroupId" name="claimWorkgroupId" value="<s:property value="workgroup.id"/>">
            <input type="hidden" id="claimClaimOwnerId" name="claimClaimOwnerId" value="<s:property value="claimOwner.id"/>">
            <input type="hidden" id="claimWorkgroupEnable" name="claimWorkgroupEnable" value="<s:property value="insurer.workgroupEnable"/>">
            <s:hidden id="actionName" name="actionName" />

            <div>
                <div class="status-info">
                Please assign the claim owner for this claim and click on the 'Assign Owner' button. If the claim needs registering by FNOL, please use the 'Refer To FNOL' button. If this claim has been assigned to the incorrect Workgroup, please use the 'More Actions' drop down above, clicking on 'Re-assign Workgroup' to re-assign the claim's Workgroup.
                </div>
                <div class="status-control-set">
                    <table class="status-table" width="100%" border="0" cellpadding="0" cellspacing="0">
                    <s:if test="insurer.workgroupEnable">
                    <tr>
                        <td><label width="200px">Workgroup</label></td>
                        <td>
                            <s:select name="oasWorkgroupId" id="oasWorkgroupId"
                            list="workgroups" headerKey="" listKey="id" listValue="name"
                            headerValue="-- Please Select --" onchange="doOwnershipAssignmentWorkgroupChange()">
                            </s:select>
                        </td>
                    </tr>
                    </s:if>
                    <s:else>
                        <input type="hidden" id="oasWorkgroupId" name="oasWorkgroupId" value=""/>
                    </s:else>
                    <tr>
                        <td><label>Claim Owner</label></td>
                        <td><div id="ownershipAssignmentClaimHandlerRoleUserDropDownDiv"></div></td>
                    </tr>
                    <tr>
                        <td colspan="2" class="choice" nowrap>
                            <input type="submit" value="Assign Owner" onclick="javascript:return doAssignOwnershipSubmit('assigned_routed');"/>
                            <input type="button" value="Refer to FNOL" onclick="javascript:return doAssignOwnershipToFnolSubmit('referFNOL');" />
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