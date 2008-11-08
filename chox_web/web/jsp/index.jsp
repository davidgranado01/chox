<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>

    <body>
      
        <div class='main' id='main'>
            
            <h1>Welcome to CHOX</h1>            
            
            <div>
                <s:action name="ListAllClaims" executeResult="true" />                
            </div>         


        </div>        

    </body>
</html>