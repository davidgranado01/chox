<%@ taglib prefix="s" uri="/struts-tags"%>


<script type="text/javascript">
    
    function doSelectWorkgroup(){

        var insurerId = <s:property value="orgId"/>;
        var selectedWorkgroupId = -1;
        
        if($("#workgroupId").val()!=null && $("#workgroupId").val()!=""){
            selectedWorkgroupId = $("#workgroupId").val();
        }

        $('#coClaimHandlerRoleUserDropDownDiv').load("user/ClaimHandlerRoleUserDropDownAction.action?workgroupId="+selectedWorkgroupId+"&insurerId="+insurerId);

    }
    
</script>

<s:select
    id="workgroupId"
    name="workgroupId"
    list="workgroups"
    listKey="id"
    listValue="name"
    headerKey=""
    headerValue="--- ALL ---"
    emptyOption="false"
    onchange="doSelectWorkgroup()">
</s:select>