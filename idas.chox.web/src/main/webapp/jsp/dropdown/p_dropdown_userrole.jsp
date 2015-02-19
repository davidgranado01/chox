<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<p class="std-label">User Roles: </p>
<s:select
        id="selectedUserRolesId"
        name="selectedUserRolesId"
        list="userRoleList" 
        listKey="id" 
        listValue="name" 
        headerKey="-1"
        headerValue="--- ALL ---"
        onchange="javascript:loadGridViewList();"
        emptyOption="false">
</s:select>