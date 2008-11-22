<%@ page import="org.acegisecurity.AuthenticationException" %>
<%@ page import="org.acegisecurity.ui.AbstractProcessingFilter" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    
    <head>
        <title>Logon</title>              
        
    </head>
    <body>
        
        <s:if test="#parameters.size()>0">
            Error :  
            <s:property value="#session['ACEGI_SECURITY_LAST_EXCEPTION'].message" /><br/>
            <br/>
        </s:if>
        
        <form action="<%=request.getContextPath()%>/j_acegi_security_check" method="POST" >
            
            <div id="loginPanel">
                <table>
                    <tr>
                        <td align="right">User Name</td><td> <input type="text" name="j_username" /> </td>
                    </tr>
                   <tr>
                        <td align="right">Password</td><td><input type="password" name="j_password" /></td>
                    </tr>
                    <tr>
                        <td colspan="2" align="right"><input type="submit" value="Logon" /></td>
                    </tr>
                </table>
            
            </div>   
        </form>
        
        <ul class="filterlist">
            <li><b>User:</b> op@cho.com <br /><b>Password:</b> 1234 <br /><b>Role:</b> ROLE_CHO_OPR (CHO Operative)</li>
            <li><b>User:</b> ch@ins.com <br /><b>Password:</b> 1234 <br /><b>Role:</b> ROLE_INS_CH (Claim Handler)</li>
        </ul>
        
    </body>
</html>
