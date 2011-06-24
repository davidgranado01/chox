<%-- 
    Document   : p_duplicated_supplementary_invoice_claim
    Created on : Jun 22, 2011, 5:27:04 PM
    Author     : seeni
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<div class="status-info">
    This claim has a Supplementary Invoice :
    <div>
        <s:iterator value="duplicatedSupplementaryInvoice" status="listStatus">
            <a href="<%=request.getContextPath()%>/prv/openClaimDetail.action?id=<s:property value="id" />" target="_blank"><s:property value="choReference" /></a><s:if test="!#listStatus.last">,</s:if>
        </s:iterator>
    </div>
</div> 
