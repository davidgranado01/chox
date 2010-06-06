<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    Ext.onReady(function(){
        Ext.MessageBox.alert('Error', 'Your session has expired due to more than 15 minutes of inactivity. Please login again', function() {
            <%session.invalidate();%>
            window.location = "<%= request.getContextPath()%>/j_spring_security_logout";
        });
    })

</script>
