<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<s:if test="isClaimAnomalous && !isCHO">
    <div class="chox-claim-header x-panel-bwrap chox-form-container">
        <fieldset class="x-fieldset">
            <legend>Hire Update Notification/Warning</legend>
            <div class="status-warning listContainer">
                <ul>
                    <s:iterator value="notifications">
                        <li><s:property value="message"/> <s:if test="isClaimNotificationEditable">(<a href='javascript:removeNotification(<s:property value="id"/>);'>Remove note from list</a>)</s:if></li>
                    </s:iterator>
                </ul>
            </div>
            <s:if test="isClaimNotificationEditable">
                <input type="button" value="Remove All" onclick='javascript:removeNotification(-1);' />
            </s:if>
        </fieldset>
    </div>
</s:if>