<%-- 
    Document   : p_invoiceDataCalculationIncorrect
    Created on : Nov 28, 2008, 11:56:54 AM
    Author     : Carlson
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<p>Please review the 'History' tab for details on the why the claim has been rejected, amend details accordingly and re-submit.</p>

<div class="form-container">
    <form onsubmit="return true;" action="user/reSubmitRejectedClaim.action" method="post" id="route" name="route">
        <s:hidden name="id" />
        <div class="chox-form-button">
            <input type="submit" value="Submit " />
        </div>
        <div class="chox-form-submit-result">&nbsp;</div>
    </form>
</div>