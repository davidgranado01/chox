<%-- 
    Document   : p_claimAwaitingCarHireInfo
    Created on : Nov 28, 2008, 11:56:08 AM
    Author     : Carlson
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<p>Please complete the 'Hire Monitoring' tab with required details regarding the hire of the vehicle.</p>

       <div class="form-container">
            <form onsubmit="return true;" action="user/submitHireMonitoringDetail.action" method="post" id="route" name="route">
                <s:hidden name="id" />
                <div class="chox-form-button">
                    <input type="submit" value="Submit " />
                </div>
                <div class="chox-form-submit-result">&nbsp;</div>
            </form>
        </div>