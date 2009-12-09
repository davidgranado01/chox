<!DOCTYPE html PUBLIC
"-//W3C//DTD XHTML 1.1 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    
    <head>
        
        
        <title><decorator:title default="Struts Starter"/> - CHOX v2.2 build 2008-11-26 </title>
        <link href="<%= request.getContextPath() %>/styles/main.css" rel="stylesheet" type="text/css" media="all"/>
        <link href="<%= request.getContextPath() %>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/>        
        <link href="<%= request.getContextPath() %>/css/ext-all.css" rel="stylesheet" type="text/css" media="all"/>
        
 	<script type="text/javascript" src="<%= request.getContextPath() %>/adapter/jquery/jquery-1.2.6.js"></script>
 	<script type="text/javascript" src="<%= request.getContextPath() %>/adapter/jquery/jquery.form.js"></script>
 	<script type="text/javascript" src="<%= request.getContextPath() %>/adapter/jquery/ext-jquery-adapter.js"></script>

        <script src="<%= request.getContextPath() %>/scripts/ext-base.js" type="text/javascript"></script>
        <script src="<%= request.getContextPath() %>/scripts/ext-all.js" type="text/javascript"></script> 
        <script src="<%= request.getContextPath() %>/scripts/Application.js" type="text/javascript"></script> 
        
        


        
        
        
        
        <decorator:head/>
        

    </head> 
    <body>
        
        
        <div id="container">
            <div id="header">
                <%@ include file="header.jsp" %> 
            </div>
            <div id="content">
                <div id="include">
                    <decorator:body/> 
                </div>
            </div>
            <div id="footer">
                <%@ include file="footer.jsp" %>
            </div>
        </div>
    </body>
    
</html>
