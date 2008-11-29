<%-- 
    Document   : p_claimUnacknowledgeUnrouted
    Created on : Nov 28, 2008, 11:54:01 AM
    Author     : Carlson
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<h2>Please select the 'Line of Business' in order to route the claim to the relevant handling team.</h2>

<div class="form-container">
    <form onsubmit="return true;" action="user/route.action" method="post" id="route" name="route">
        <s:hidden name="id" />
        <div class="chox-form-item">
            <label class="chox-form-std-label">Line of Business</label>
            <s:select name="lineOfBusiness.id" list="lineOfBusinesses" 
                      headerKey="1" listKey="id" listValue="name"
                      headerValue="-- Please Select --"></s:select>                                           
        </div>
        <div class="chox-form-button">
            <input type="submit" value="Route" />
        </div>
        <div class="chox-form-submit-result">&nbsp;</div>
    </form>
</div>