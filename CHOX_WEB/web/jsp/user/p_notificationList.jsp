<%-- 
    Document   : p_notificationList
    Created on : 16-Sep-2009, 19:13:43
    Author     : emmanuel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<s:if test="isClaimAnomalous && !isCHO">
    <div class="chox-claim-header x-panel-bwrap chox-form-container">
        <fieldset class="x-fieldset">
            <legend>Hire Update Notification/Warning</legend>
            <div class="status-warning listContainer">
                <ul>
                    <s:iterator value="notifications">
                        <li><s:property value="message"/> (<a href='javascript:removeNotification(<s:property value="id"/>,<s:property value="claim.id"/>);'>Remove note from list</a>)</li>
                    </s:iterator>
                </ul>
            </div>
        </fieldset>
    </div>
</s:if>