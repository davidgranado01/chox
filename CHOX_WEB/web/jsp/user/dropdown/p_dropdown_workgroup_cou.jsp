<%@ taglib prefix="s" uri="/struts-tags"%>


<script type="text/javascript">

    function doSelectWorkgroupCou(){

        var insurerId = <s:property value="orgId"/>;
        var selectedWorkgroupId = -1;

        if($("#workgroupId").val()!=null && $("#workgroupId").val()!=""){
            selectedWorkgroupId = $("#workgroupId").val();
        }

        var sLocaltion = "#couClaimHandlerRoleUserDropDownDiv";
        var sAction = "user/ClaimHandlerRoleUserDropDownAction.action";
        var sparameters = "workgroupId="+selectedWorkgroupId+"&insurerId="+insurerId;
        doSectionLoad(sLocaltion, sAction, sparameters);

    }

</script>

<s:select
    id="workgroupId"
    name="workgroupId"
    list="workgroups"
    listKey="id"
    listValue="name"
    headerKey=""
    headerValue="- Please Select -"
    emptyOption="false"
    onchange="doSelectWorkgroupCou()">
</s:select>