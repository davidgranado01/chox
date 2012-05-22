<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
	<div class="status-info">
		This claim has been escalated to a Supervisor because
		<s:if test="numberOfTimesContestedWithCHOtoEscalate > 0"> it has been contested back to the Insurer <s:property
				value="numberOfTimesContestedWithCHOtoEscalate" /> times</s:if><s:if
			test="numberOfTimesContestedWithCHOtoEscalate  > 0 && daysSinceInvoiceUploadToEscalate  > 0"> and </s:if><s:if 
			test="daysSinceInvoiceUploadToEscalate  > 0">the invoice was uploaded <s:property
				value="daysSinceInvoiceUploadToEscalate" /> days ago</s:if>.
	</div>
</div>
