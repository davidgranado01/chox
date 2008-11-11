<%-- 
    Document   : sidebar
    Created on : 07-Nov-2008, 14:53:53
    Author     : Emmanuel
--%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
    <table cellpadding="0" cellspacing="0"> 
        <tr> 
            <td> 
                <ui>
                    <li><a href="<s:url value="index.action"/>">Home</a></li>
                    <li><a href="<s:url value="UploadClaims.action"/>">Upload Claims</a></li>
                    <li><a href="<s:url value="Inbox.action"/>">Claims (?)</a></li>
                    <li><a href="#">Invoice (?)</a></li>                  
                </ui>
            </td> 
        </tr> 
        
    </table>
</html>