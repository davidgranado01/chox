<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    function doSelectWorkgroup(){
        var insurerId = <s:property value="orgId"/>;
        var selectedWorkgroupId = -1;
        
        if($("#oasWorkgroupACOdropdownId").val()!==null && $("#oasWorkgroupACOdropdownId").val()!==""){
            selectedWorkgroupId = $("#oasWorkgroupACOdropdownId").val();
        }

        var target = "#claimOwnerClaimHandlerRoleUserDropDownDiv";
        var url = "/prv/p/ClaimHandlerRoleUserDropDownAction.action";
        var param = {"workgroupId":selectedWorkgroupId,"insurerId":insurerId};

        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
        });
    }
    
</script>

<s:select
    id="oasWorkgroupACOdropdownId"
    name="oasWorkgroupId"
    list="workgroups"
    listKey="id"
    listValue="name"
    headerKey="-1"
    headerValue="-- Please Select --"
    emptyOption="false"
    onchange="doSelectWorkgroup()">
</s:select>