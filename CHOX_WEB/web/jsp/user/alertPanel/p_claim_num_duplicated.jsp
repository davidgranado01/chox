<%-- 
    Document   : p_claim_num_duplicated
    Created on : 06-Feb-2009, 15:05:31
    Author     : Emmanuel
--%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<div class="status-info">
        This claim shares it's claim number with the following claim(s) :
        <div>
        <s:iterator value="otherDuplicatedClaims" status="listStatus">
            <a href="openClaimDetail.action?id=<s:property value="id" />" target="_blank"><s:property value="choReference" /></a><s:if test="!#listStatus.last">,</s:if>
       </s:iterator>
       </div>

</div> 
