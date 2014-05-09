<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
<style type="text/css">

h1
{
color:red;
text-align:center;
}
p
{
text-align:center;

}
</style>
<script type="text/javascript">
    function maskClaimdetailsPage(){
        Ext.get('claimDetailScreenDiv').mask("Loading search result ...");
    }
</script>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Export Failed</title>
    </head>
    <body>
    <br/><br/><br/>
    <h1><s:property value="errorMessage"/></h1>
        
   <br/>

   <a href="javascript: loadInbox(true);" onclick="return maskClaimdetailsPage();">« Back to Search Results</a>
<br/><br/><br/><br/><br/><br/>
    
    </body>
</html>