<%-- 
    Document   : p_UploadInvoiceData
    Created on : 02-Dec-2008, 17:11:54
    Author     : Emmanuel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

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
