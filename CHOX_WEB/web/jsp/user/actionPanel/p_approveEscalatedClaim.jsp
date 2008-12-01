<%-- 
    Document   : p_approveClaim
    Created on : 01-Dec-2008, 02:28:00
    Author     : Emmanuel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<div class="status-info">
    Please review the 'History' tab for details on why the claim has been rejected.  
    Please decide on whether to progress the claim for payment or reject the claim. 
    Please enter the required details/comments on the 'Invoice Details' tab regarding the decision made.
</div>

<div class="form-container">
    <form onsubmit="return true;" action="user/approveEscalatedClaim.action" method="post" id="approveEscalatedClaim" name="approveEscalatedClaim">
        <s:hidden name="id" />
        <div class="chox-form-button">
            <input type="submit" value="Submit " />
        </div>
        <tr>
            <td><label class="chox-form-std-label">Approval</label></td>
            <td><s:radio name="actionName" list="actionNames" /></td>
        </tr>
        <div class="chox-form-submit-result">&nbsp;</div>
    </form>    
</div>