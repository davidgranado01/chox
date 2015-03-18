<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<html>

    <head>
        <meta http-equiv="Content-type" content="text/html;charset=UTF-8" />
        <title><decorator:title default="CHOX"/></title>
	<script type="text/javascript">
                var contextPath = '<%= request.getContextPath()%>';
                var brandingType = '<s:property value="brandingType"/>';
                var isPartialBranding = (brandingType === 'Partial') ? true : false;
                var isFullBranding = (brandingType === 'Full') ? true : false;
                var isBrandingClaim = '<s:property value="brandingClaim"/>';

	</script>
        <s:if test="#parameters.devp || getText('development')">
            <%@ include file="/includes/styles.jsp"%>
            <%@ include file="/includes/scripts.jsp"%>
        </s:if>
        <s:else>
            <%@ include file="/includes/styles-min.jsp"%>
            <%@ include file="/includes/scripts-min.jsp"%>
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
            &copy;2015 Valexa Technologies Limited | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','Copyright');" class="footerText">Copyright</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','PrivacyPolicy');" class="footerText">Privacy Policy</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','TermsOfService');" class="footerText">Terms of Service</a>
        </div>

    </body>
</html>
