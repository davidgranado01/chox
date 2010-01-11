<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<s:select
    id="claimOwnerId"
    name="claimOwnerId"
    list="claimhandlers"
    listKey="id"
    listValue="name"
    headerKey="-1"
    headerValue="- Please Select -"
    emptyOption="false">
</s:select>