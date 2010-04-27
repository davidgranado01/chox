<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>

    <head>
        <title><decorator:title default="IDAS-CHOX"/></title>
        <%@ include file="/includes/styles.jsp"%>
        <%@ include file="/includes/scripts.jsp"%>
        <decorator:head />

    </head>

    <body>
        <div class="outer" id="outerDiv">

            <div class="inner">

                <div id="chox-menu">

                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                        <tr valign="middle">
                            <td>
                                <img src="<%= request.getContextPath()%>/images/chox_logo_small.jpg" style="display: inline; float: left" alt="" />
                            </td>
                            <td width="100%" align="right">

                                <ul id="top-menu">
                                    <li><a href="<s:url action="inbox"/>">&nbsp;Home&nbsp;</a></li>
                                    <li><a href="<s:url action="openUserAccountSettings" />">|&nbsp;Settings&nbsp;</a></li>
                                    <s:if test="isCHO"><li><a href='<s:url action="uploadClaims"/>'>|&nbsp;XML Uploads&nbsp;</a></li></s:if>
                                    <s:if test="!isChoxAdmin"><li><a href="javascript:openHelpFile('<%= request.getContextPath()%>',<s:property value="roleTypeForHelpFile" />);">|&nbsp;Help&nbsp;</a></li></s:if>
                                    <li><a href="#" onmouseover="mopen('m2')" onmouseout="mclosetime()">|&nbsp;Support&nbsp;</a>
                                        <div id="m2" onmouseover="mcancelclosetime()" onmouseout="mclosetime()">
                                            <a href="javascript:openSupportFile('<%= request.getContextPath()%>');">Support Procedure</a>
                                            <a href="<s:url action="onlineSupport"/>">Online Support Form</a>
                                        </div></li>
                                    <li><a href="javascript:onOpenAbout();">|&nbsp;About CHOX&nbsp;</a></li>
                                    <li><a href="<%=request.getContextPath()%>/j_spring_security_logout" >|&nbsp;<b><s:property value="CurrentUserDesc" /></b> ( Log Off )</a></li>
                                </ul>

                                <div style="clear:both"></div>

                            </td>
                        </tr>
                    </table>
                </div>

                <decorator:body />

            </div>
        </div>

        <div class="footerText">
            ©2010 Sherwood Compliance Services Ltd | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','Copyright');" class="footerText">Copyright</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','PrivacyPolicy');" class="footerText">Privacy Policy</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','TermsOfService');" class="footerText">Terms of Service</a>
        </div>

    </body>
</html>
