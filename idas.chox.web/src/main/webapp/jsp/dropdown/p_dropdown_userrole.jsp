<%@ taglib prefix="s" uri="/struts-tags"%>

<p class="std-label">User Roles: </p>
<s:select
        id="SelectedUserrolesId"
        name="SelectedUserrolesId"
        list="userroleList" 
        listKey="id" 
        listValue="name" 
        headerKey=""
        headerValue="--- ALL ---"
        onchange="javascript:loadGridViewList();"
        emptyOption="false">
</s:select>