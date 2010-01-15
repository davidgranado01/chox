<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<s:select
    id="workgroupId" name="workgroupId"
    list="workgroups"
    listKey="id"
    listValue="name"
    headerKey=""
    headerValue="--- ALL ---"
    emptyOption="false">
</s:select>