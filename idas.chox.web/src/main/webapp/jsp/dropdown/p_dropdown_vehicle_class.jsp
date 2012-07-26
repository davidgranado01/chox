<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<label class="chox-form-std-label">Vehicle Class<span class="mandatory">*</span></label>
<s:select
    id="vehicleClassId"
    name="vehicleClassId"
    list="vehicleClasses"
    listKey="id"
    listValue="name"
    headerKey="-1"
    headerValue="-- Please Select --"
    emptyOption="false">
</s:select>