<!DOCTYPE html PUBLIC
"-//W3C//DTD XHTML 1.1 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <title><decorator:title default="Struts Starter"/> - CHOX v0.1 build 2008-11-12 </title>
        <link href="<s:url value='styles/main.css'/>" rel="stylesheet" type="text/css" media="all"/>
        <link href="<s:url value='css/ext-all.css'/>" rel="stylesheet" type="text/css" media="all"/>
        <link href="<s:url value='css/xtheme-gray.css'/>" rel="stylesheet" type="text/css" media="all"/>
        <script src="<s:url value='scripts/ext-base.js'/>" type="text/javascript"></script>
        <script src="<s:url value='scripts/ext-all.js'/>" type="text/javascript"></script> 
        <script src="<s:url value='scripts/Application.js'/>" type="text/javascript"></script> 
        <decorator:head/>
    </head>
    <body>
        <div id="container">
            <div id="header">
                <%@ include file="header.jsp" %> 
            </div>
            <div id="navigation">
                <%@ include file="navigation.jsp" %> 
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
