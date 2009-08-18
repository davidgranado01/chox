<%@ taglib prefix="s" uri="/struts-tags"%>

<script>
    
        $(document).ready(function(){ 
            $("#lineOfBusinessId").val(lineOfBusinessId);
        });
        
</script>

<s:select
    id="lineOfBusinessId"                                 
    name="lineOfBusinessId" 
    list="lineofbusinesses" 
    listKey="id" 
    listValue="name" 
    headerKey=""
    headerValue="--- Please Select ---"
    emptyOption="false">
</s:select>