<%@ taglib prefix="s" uri="/struts-tags"%>

<s:select
    id="workgroup"                                 
    name="workgroup" 
    list="workgroups" 
    listKey="id" 
    listValue="name" 
    headerKey="-1"
    headerValue="--- ALL ---"
    emptyOption="false">
</s:select>