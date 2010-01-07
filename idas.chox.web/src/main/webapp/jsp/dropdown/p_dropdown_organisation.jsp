<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<p class="std-label">Company Name: </p>
<s:select
        id="SelectedOrganisationId"
        name="SelectedOrganisationId"
        list="organisationList"
        listKey="id"
        listValue="name"
        headerKey=""
        headerValue="--- ALL ---"
        onchange="javascript:loadGridViewList();"
        emptyOption="false">
</s:select>