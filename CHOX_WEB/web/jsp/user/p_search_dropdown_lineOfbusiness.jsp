<%@ taglib prefix="s" uri="/struts-tags"%>
<s:select
    id="lineOfBusiness"                                 
    name="lineOfBusiness" 
    list="lineofbusinesses" 
    listKey="id" 
    listValue="name" 
    headerKey="-1"
    headerValue="--- ALL ---"
    emptyOption="false">
</s:select>