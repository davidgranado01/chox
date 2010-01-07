<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(document).ready(function(){
        $("#claimOwnerId").val(claimOwnerId);
    });

</script>

<s:select
    id="searchClaimOwnerId"
    name="searchClaimOwnerId"
    list="claimhandlers"
    listKey="id"
    listValue="name"
    headerKey="-1"
    headerValue="--- ALL ---"
    emptyOption="false">
</s:select>