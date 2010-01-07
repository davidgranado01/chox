<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<s:select
    id="workgroup"                                 
    name="workgroup" 
    list="workgroups" 
    listKey="id" 
    listValue="name" 
    headerKey="-1"
    headerValue="--- ALL ---"
    emptyOption="false" onchange="javascript:doSearchWorkgroupOnChange();">
</s:select>