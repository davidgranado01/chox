<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
    function doSearchSelectWorkgroup(){

        insurerId = <s:property value="AuthenticatedUser.user.insurer.id"/>;

        var selectedWorkgroupId = -1;

        if($("#workgroup").val()!=null && $("#workgroup").val()!=""){
            selectedWorkgroupId = $("#workgroup").val();
        }

        doShowClaimHandler(selectedWorkgroupId, insurerId);
    }
    </script>
<s:select
    id="workgroup"                                 
    name="workgroup" 
    list="workgroups" 
    listKey="id" 
    listValue="name" 
    headerKey="-1"
    headerValue="--- ALL ---"
    emptyOption="false"
    onchange="doSearchSelectWorkgroup()">
</s:select>