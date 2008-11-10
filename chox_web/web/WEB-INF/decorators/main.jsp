<!DOCTYPE html PUBLIC
"-//W3C//DTD XHTML 1.1 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <title><decorator:title default="Struts Starter"/></title>
        <link href="<s:url value='styles/main.css'/>" rel="stylesheet" type="text/css" media="all"/>
        <decorator:head/>
    </head>
    <body> 
        <table id="page-container" cellpadding="0" cellspacing="0"> 
            <tr> 
                <td colspan="2" id="page-header"> 
                    <%@ include file="header.jsp"%> 
                </td> 
            </tr><tr> 
                <td id="nav-container"> 
                    <%@ include file="navigation.jsp" %> 
                </td> 
                <td id="content-container"> 
                    <decorator:body/> 
                </td> 
            </tr><tr> 
                <td colspan="2" id="page-footer"> 
                    <%@ include file="footer.jsp" %> 
                </td> 
        </tr></table> 
    </body> 
</html>
