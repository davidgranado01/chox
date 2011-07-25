<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    Ext.onReady(function(){
        Ext.MessageBox.alert('Error', 'You have been denied access and will now be logged out', function() {
            window.location = "<%= request.getContextPath()%>/j_spring_security_logout";
        });
    })

</script>
