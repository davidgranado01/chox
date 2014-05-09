<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    function doSelectWorkgroupCou(){
        var insurerId = <s:property value="orgId"/>;
        var selectedWorkgroupId = -1;

        if($("#workgroupHtmlId").val()!=null && $("#workgroupHtmlId").val()!=""){
            selectedWorkgroupId = $("#workgroupHtmlId").val();
        }

        var target = "#couClaimHandlerRoleUserDropDownDiv";
        var url = "/prv/p/ClaimHandlerRoleUserDropDownAction.action";
        var param = {"workgroupId":selectedWorkgroupId,"insurerId":insurerId};

        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
        });
    }

</script>

<s:select
    id="workgroupHtmlId"
    name="workgroupId"
    list="workgroups"
    listKey="id"
    listValue="name"
    headerKey="-1"
    headerValue="-- Please Select --"
    emptyOption="false"
    onchange="doSelectWorkgroupCou()">
</s:select>