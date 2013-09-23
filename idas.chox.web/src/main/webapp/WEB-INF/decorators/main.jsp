<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>

<html>

    <head>
        <meta http-equiv="Content-type" content="text/html;charset=UTF-8" />
        <title><decorator:title default="CHOX"/></title>
	       <s:if test="#parameters.devp || getText('development')">
	           	<%@ include file="/includes/styles.jsp"%>
	        	<%@ include file="/includes/scripts.jsp"%>
	       </s:if>
	       <s:else>
	           	<%@ include file="/includes/styles-min.jsp"%>
	        	<%@ include file="/includes/scripts-min.jsp"%>
	       </s:else>
        <decorator:head />

	<script type="text/javascript">
		var generalChoxVersion = '<s:property value="getText('version.number')" />';
	</script>
    </head>

    <body>
        <input type="hidden" id="uniqueNonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
        
        <div class="outer" id="outerDiv">

            <div class="inner">

                <div id="chox-menu">

                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                        <tr valign="middle">
                            <td>
                                <img src="<%= request.getContextPath()%>/images/chox_logo_small.jpg" style="display: inline; float: left; width: 77px; height: 22px" alt="CHOX Logo" />
                            </td>
                            <td width="100%" align="right">

                                <ul id="top-menu">
                                    <li><a id="decoratorMainPageHomePageId" href="<s:url action="inbox" includeParams="none"><s:param name="showHistory">10</s:param><s:param name="nonce"><%= session.getAttribute("SessionNonce")%></s:param></s:url>">&nbsp;Home&nbsp;</a></li>
                                    <li><a id="decoratorMainPageSettingsId" href="<s:url action="openUserAccountSettings" includeParams="none"/>">|&nbsp;Settings&nbsp;</a></li>
                                    <s:if test="!isChoxAdmin"><li><a href="javascript:openHelpFile('<%= request.getContextPath()%>',<s:property value="roleTypeForHelpFile" />, <s:property value="bespokeHelpFileType" />);">|&nbsp;Help&nbsp;</a></li></s:if>
                                        <li><a id="decoratorMainPageSupportFormId" href="#" onmouseover="mopen('m2')" onmouseout="mclosetime()">|&nbsp;Support&nbsp;</a>
                                            <div id="m2" onmouseover="mcancelclosetime()" onmouseout="mclosetime()">
                                                <a href="javascript:openSupportFile('<%= request.getContextPath()%>','<s:property value="supportFile" />');">Support Procedure</a>
                                            <s:if test="isSupportEnabled">
                                                <a href="javascript:openFile('<%= request.getContextPath()%>/prv/onlineSupport.action');">Online Support Form</a>
                                            </s:if>
                                            <s:else>
                                                <a href="#" onclick="javascript:
                                                   Ext.MessageBox.show({
                                                   title: '',
                                                   msg: 'Online support form is not available.',
                                                   width:300,
                                                   buttons: Ext.MessageBox.OK,
                                                   icon : Ext.MessageBox.INFO
                                                   }); ">Online Support Form</a>
                                            </s:else>
                                        </div></li>
                                    <li><a id="decoratorMainPageAboutChoxId"href="javascript:onOpenAbout();">|&nbsp;About CHOX&nbsp;</a></li>
                                    <li><a id="decoratorMainPageLogoffId" href="<%=request.getContextPath()%>/j_spring_security_logout" >|&nbsp;<b><s:property value="CurrentUserDesc" /></b> ( Log Off )</a></li>
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
            &copy;2013 Sherwood Technology Solutions Ltd | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','Copyright');" class="footerText">Copyright</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','PrivacyPolicy');" class="footerText">Privacy Policy</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','TermsOfService');" class="footerText">Terms of Service</a>
        </div>

    </body>
</html>
