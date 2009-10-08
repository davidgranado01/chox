<%@ taglib prefix="s" uri="/struts-tags"%>

<p class="std-label">User Roles: </p>
<s:select
        id="userrolesId"                                 
        name="userrolesId" 
        list="userroleList" 
        listKey="id" 
        listValue="name" 
        headerKey=""
        headerValue="--- ALL ---"
        onchange="javascript: doUseroleSelected();"
        emptyOption="false">
</s:select>