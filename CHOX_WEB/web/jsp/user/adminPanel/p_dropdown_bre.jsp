<%@ taglib prefix="s" uri="/struts-tags"%>

<p class="std-label">BRE Band: </p>

<s:select
    id="breBandId"                                 
    name="breBandId" 
    list="breBands" 
    listKey="id" 
    listValue="name" 
    headerKey="-1"
    headerValue="-- Please Select --"    
    onchange="javascript: doBRESelectOnChange();"
    emptyOption="false">
</s:select>