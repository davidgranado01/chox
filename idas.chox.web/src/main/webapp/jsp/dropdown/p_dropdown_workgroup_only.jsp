<%@ taglib prefix="s" uri="/struts-tags"%>

<s:select
    id="workgroupId" name="workgroupId"
    list="workgroups"
    listKey="id"
    listValue="name"
    headerKey=""
    headerValue="--- ALL ---"
    emptyOption="false">
</s:select>