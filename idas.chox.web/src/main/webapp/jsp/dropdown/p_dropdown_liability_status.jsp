<%-- 
    Document   : p_dropdown_liability_status
    Created on : Feb 11, 2010, 10:01:14 AM
    Author     : abrar
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<s:select
    id="liabilityStatus"
    name="liabilityStatus"
    list="dropDownMap"
    headerKey="-1"
    headerValue="--- ALL ---"
    emptyOption="false"
    tooltip="Update Liability">
</s:select>
