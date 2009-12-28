<%@ taglib prefix="s" uri="/struts-tags"%>


<script type="text/javascript">

    function doSelectWorkgroupCou(){
        var insurerId = <s:property value="orgId"/>;
        var selectedWorkgroupId = -1;

        if($("#workgroupId").val()!=null && $("#workgroupId").val()!=""){
            selectedWorkgroupId = $("#workgroupId").val();
        }

        var target = "#couClaimHandlerRoleUserDropDownDiv";
        var url = "<%=request.getContextPath()%>/prv/p/ClaimHandlerRoleUserDropDownAction.action";
        var param = {"workgroupId":selectedWorkgroupId,"insurerId":insurerId};

        ajax.loadHtml(url,param,function(data){
            $(target).html(data);
        });
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