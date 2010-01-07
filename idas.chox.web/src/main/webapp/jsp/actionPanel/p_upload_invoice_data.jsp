<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

    <fieldset class="x-fieldset">
        <legend>Awaiting Invoice Data - Action Required</legend>
        <s:hidden name="id" />
        <div>
            <div class="status-info">
                Please upload the invoice details of the claim in order to progress the claim for payment.
                <br />
                <a href='<s:url action="uploadClaims" namespace="/prv"/>'>XML Uploads</a>&nbsp;&nbsp;
            </div>
            
        </div> 
    </fieldset>
