<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<p class="std-label">User Roles: </p>
<s:select
        id="selectedUserRolesId"
        name="selectedUserRolesId"
        list="userroleList" 
        listKey="id" 
        listValue="name" 
        headerKey=""
        headerValue="--- ALL ---"
        onchange="javascript:loadGridViewList();"
        emptyOption="false">
</s:select>