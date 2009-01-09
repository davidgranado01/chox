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

<span class="line_page_header">iDAS CHOX Support</span>

<span class="line_item_css">Sherwood Compliance Services Ltd are committed to support all users of iDAS CHOX and have a
dedicated team who are on hand to answer both technical and user related queries.</span>

<span class="line_header">Usability Issues</span>
<span class="line_item_css">
Should you have a query in reference to the usability of iDAS CHOX, please contact one of our user
support specialists via the below email:<br/><br/>
<a href="mailto:choxsupport@sherwoodcompliance.co.uk"><b>choxsupport@sherwoodcompliance.co.uk</b></a><br/><br/>
Please specify the following:<br/>
<b><i>Organisation Name:</i></b><br/>
<b><i>Role:</i></b><br/>
<b><i>Screen Name:</i></b><br/>
<b><i>Screen Reference Area:</i></b><br/>
<b><i>Usability Note:</i></b><br/>
<b><i>Contact Details:</i></b><br/>
</span>

<span class="line_header">Technical Issues</span>
<span class="line_item_css">
Should you experience any technical problems when using iDAS CHOX such as system crashes or
unexpected errors, please contact a member of our technical help desk via the below email:<br/><br/>
<a href="mailto:choxsupport@sherwoodcompliance.co.uk"><b>choxsupport@sherwoodcompliance.co.uk</b></a><br/><br/>
Please specify the following:<br/>
<b><i>Organisation Name:</i></b><br/>
<b><i>Role:</i></b><br/>
<b><i>Screen Name:</i></b><br/>
<b><i>Screen Reference Area:</i></b><br/>
<b><i>Technical Note:</i></b><br/>
<b><i>System Message(s):</i></b><br/>
<b><i>Contact Details:</i></b><br/>
</span>

<span class="line_header">Issue Resolution</span>
<span class="line_item_css">
Our support specialists are on hand during operational hours, 5 days a week.
We endeavour to respond to support queries within 2 hours during operational hours.<br/><br/>
Operational hours are as follows:<br/>
<b>Monday to Friday: 09:00-17:00</b>
</span>

<span class="line_header">System Updates/Downtime</span>
<span class="line_item_css">
In the occurrence of scheduled system updates or unexpected system downtimes iDAS CHOX
technical help desk will notify all registered iDAS CHOX users via email with details on downtime
estimates and any other relevant information. An email will be sent notifying iDAS CHOX users when
the system is available for use again.
</span>
    </div>
</div>
        
    </div>
    
<div class="footerText">
©2009 Sherwood Compliance Services Ltd | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','Copyright');" class="footerText">Copyright</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','PrivacyPolicy');" class="footerText">Privacy Policy</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','TermsOfService');" class="footerText">Terms of Service</a></div>

    </body>
</html>