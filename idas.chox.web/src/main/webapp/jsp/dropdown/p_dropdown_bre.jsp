<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<p class="std-label">BRE Band: </p>

<s:select
    id="breBandId"                                 
    name="breBandId" 
    list="breBands" 
    listKey="id" 
    listValue="name" 
    headerKey="-1"
    headerValue="-- Please Select --"    
    onload="javascript: doBRESelectOnChange();"
    onchange="javascript: doBRESelectOnChange();"
    emptyOption="false">
</s:select>