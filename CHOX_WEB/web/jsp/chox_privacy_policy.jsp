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

<span class="line_page_header">iDAS CHOX Privacy Policy</span>

<span class="line_item_css">Your use of this exchange service indicates to us that you have read and accept our privacy practices, as outlined in this Privacy Statement.<br/><br/>
Sherwood Compliance Services Ltd (SCS) is committed to protecting the rights and privacy of individuals with regard to the processing of personal data.</span>


<span class="line_header">1. DATA SECURITY </span>
<span class="line_item_css">
Reasonable steps are taken to maintain the security of data that we collect, including restricting the number of individuals 
who have physical access to our database servers, as well as installing electronic security systems that guard against unauthorised access. 
This being said, no data transmission over the World Wide Web can be guaranteed to be completely secure. Additionally, SCS cannot ensure or 
warrant the security of any information that you transmit to us, so you do so at your own risk. 
</span>

<span class="line_header">2. DATA ACCURACY </span>
<span class="line_item_css">SCS are committed to keeping accurate, up-to-date records to help ensure the integrity of the information maintained.  SCS will on its own initiative, or at your request, free of charge, replenish, rectify or erase any incomplete, inaccurate or outdated personal data retained by SCS in connection with the operation of this exchange service. 
</span>

<span class="line_header">3. DATA PROTECTION</span>
<span class="line_item_css">At all times SCS are legally obliged to collect, retain and process any personal information that you provide in accordance with the Data Protection Act 1998 and SCS also comply with the Privacy and Electronic Communications (EC Directive) Regulations 2003.
</span>

<span class="line_header">4. COOKIES</span>
<span class="line_item_css">Cookies are pieces of data created when you visit a site, and contain a unique, anonymous number. They do not contain any personal information about you and cannot be used to identify an individual user.  The use of cookies provides benefits to you, such as eliminating the need for you to enter your password frequently during a session. <br/><br/>
You can disable cookies on your computer by accessing the options menus in your browser. You should consult your web browser's provider if you have any questions regarding cookies. 
</span>

<span class="line_header">5. CHANGES TO THIS PRIVACY POLICY</span>
<span class="line_item_css">If SCS make any changes to this Privacy Policy these changes will be detailed on this page in order to ensure that you are fully aware of changes to SCS’s Privacy Policy at any point in time. Updates or modifications will be effective upon posting to this page.
</span>

<span class="line_header">6. CONSENT</span>
<span class="line_item_css">Any information you submit to SCS is provided on a voluntary basis. When you access this exchange service, the collection, use, and distribution of your information will be handled according to the terms and policies outlined in this Privacy Policy. If you object to any of the policies outlined in this Policy, SCS ask that you do not submit information to the exchange. 
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