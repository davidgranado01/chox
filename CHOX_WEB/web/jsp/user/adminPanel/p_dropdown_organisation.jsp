<%@ taglib prefix="s" uri="/struts-tags"%>

<p class="std-label">Company Name: </p>
<s:select
        id="organisationId"
        name="organisationId"
        list="organisationList"
        listKey="id"
        listValue="name"
        headerKey=""
        headerValue="--- ALL ---"
        onchange="javascript: doUseroleSelected();"
        emptyOption="false">
</s:select>