<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Export Failed</title>
    </head>
    <body>
    <br/><br/><br/>
    <h1>The Export To Excel feature is restricted to exporting a maximum of 5,000 claims, please refine your search. </h1>
        
   <br/>

  <a href="<%=request.getContextPath()%>/prv/inbox.action?showHistory=1">Back to Search Result</a>

    
    </body>
</html>