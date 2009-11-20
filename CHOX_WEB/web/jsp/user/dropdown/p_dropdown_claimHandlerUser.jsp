<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">

    $(document).ready(function(){
        $("#claimOwnerId").val(claimOwnerId);
    });

</script>

<s:select
    id="claimOwnerId"
    name="claimOwnerId"
    list="claimhandlers"
    listKey="id"
    listValue="name"
    headerKey="-1"
    headerValue="--- ALL ---"
    emptyOption="false">
</s:select>