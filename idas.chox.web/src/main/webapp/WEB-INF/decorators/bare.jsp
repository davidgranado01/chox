<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<html>

    <head>
        <meta http-equiv="Content-type" content="text/html;charset=UTF-8" />
        <title><decorator:title default="CHOX"/></title>
        <s:if test="#parameters.devp || getText('development')">
            <%@ include file="/includes/styles.jsp"%>
            <script src="<%= request.getContextPath()%>/scripts/general.js" type="text/javascript"></script>
        </s:if>
        <s:else>
            <%@ include file="/includes/styles-min.jsp"%>
            <script src="<%= request.getContextPath()%>/scripts/general-min.js" type="text/javascript"></script>
        </s:else>
        <decorator:head />

    </head>

    <body>
        <div class="outer" id="outerDiv">

            <div class="inner">

                <div id="chox-menu">
                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                        <tr valign="middle">
                            <td>
                                <img src="<%= request.getContextPath()%>/images/chox_logo_small.jpg" style="display: inline; float: left; width: 77px; height: 22px" alt="CHOX Logo"/>
                            </td>
                            <td width="100%" align="right">
                                <div class="top-menu">
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>

                <decorator:body />

            </div>
        </div>

        <div class="footerText">
            &copy;2018 Audatex (UK) Limited | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','Copyright');" class="footerText">Copyright</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','PrivacyPolicy');" class="footerText">Privacy Policy</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','TermsOfService');" class="footerText">Terms of Service</a>
        </div>

    </body>
</html>
