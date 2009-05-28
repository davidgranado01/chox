<%@ taglib prefix="s" uri="/struts-tags"%>

<p class="std-label">BRE: </p>

<s:select
    id="breBandId"                                 
    name="breBandId" 
    list="breBands" 
    listKey="id" 
    listValue="name" 
    onchange="javascript: doBRESelectOnChange();"
    emptyOption="false">
</s:select>