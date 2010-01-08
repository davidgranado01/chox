<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<ul class="inbox">
    <s:iterator value="filters">
        <li><a href="javascript:executeFilter('<s:property value="key" />');" ><s:property value="description" /></a></li>
    </s:iterator>
</ul>                            


