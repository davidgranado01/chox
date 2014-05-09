<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <div class="status-info">
        This claim shares it's claim number with the following claim(s) :
        <div>
            <s:iterator value="otherDuplicatedClaims" status="listStatus">
                <a href="javascript:loadClaimDetail(<s:property value="id" />);" ><s:property value="choReference" /></a><s:if test="!#listStatus.last">,</s:if>
            </s:iterator>
        </div>
    </div> 
</div>
