<%-- 
    Document   : EncodePassword
    Created on : 30-Jul-2009, 11:51:00
    Author     : emmanuel
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery-1.2.6.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Encode Password</title>
        
         <s:if test="isChoxAdmin">
        <script>
            
           $(function(){

              $("a#EncodeAllUser").click(function()
              {
                  if(confirm('Are you sure you want to do this?'))
                  {
                      $.get('user/encodeAllUserPassword.action',function(response){
                         alert(response);
                      });
                  }
                  return false;

              });
              
              
            });
            
        </script>
        </s:if>
    </head>

      <s:if test="isChoxAdmin">
     
            <h2>Important! Do not execute more that once.</h2>
            <a href="#" id="EncodeAllUser">Encode All User Password</a>
      
      </s:if>

</html>
