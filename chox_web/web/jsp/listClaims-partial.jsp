<%-- 
    Document   : listClaims-partial
    Created on : 07-Nov-2008, 13:30:17
    Author     : Emmanuel
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<s:if test="results.size()==0">
    <s:text name="text.noClaims" />
</s:if>
<s:else>
    <s:iterator id="next" value="Results">
        <h3><s:property value="#next.TpClaimReference" /></h3>
    </s:iterator>
</s:else>