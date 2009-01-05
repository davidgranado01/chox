<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>


<head>
    <title>IDAS-CHOX</title>
    

    <link href="<%= request.getContextPath()%>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/>        
    <link href="<%= request.getContextPath()%>/css/ext-all.css" rel="stylesheet" type="text/css" media="all"/>
    <script src="<%= request.getContextPath()%>/scripts/general.js" type="text/javascript"></script> 
</head>

<html>
    
    <body>
        
<div class="outer" id="outerDiv">
        
<div class="inner">
    <div id="chox-menu">
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
            <tr valign="middle">
                <td>
                    <img src="<%= request.getContextPath()%>/images/chox_logo_small.jpg" style="display: inline; float: left" />
                </td>
                <td width="100%" align="right">
                    <div class="top-menu">
                        <a href="javascript:window.close();"><b>Close</b></a>
                    </div>
                </td>
            </tr>
        </table>
    </div>
    <div>

<span class="line_page_header">iDAS CHOX Copyright</span>

<span class="line_item_css">©2008 Sherwood Compliance Services Ltd (SCS). SCS’s content is the intellectual property of Sherwood Compliance Services Ltd. Any copying, republication or redistribution of SCS’s content by caching, framing or other means is prohibited without the prior consent of SCS in writing. SCS shall not be liable for any errors or delays in content. The Sherwood Compliance Services logo and the iDAS CHOX logo are trademarks of SCS.
</span>
    </div>
</div>
        
    </div>

<div class="footerText">        
This is a Sherwood Compliance Services Ltd proprietary system. No use is allowed without appropriate authorisation.<br/>
Unauthorised use of this system will constitute a breach of Sherwood Compliance Services Ltd policy and prosecution under pertinent legislation will apply.<br/><br/>
©2008 Sherwood Compliance Services Ltd | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','Copyright');" class="footerText">Copyright</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','PrivacyPolicy');" class="footerText">Privacy Policy</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','TermsOfService');" class="footerText">Terms of Service</a></div>
</div>

    </body>
</html>