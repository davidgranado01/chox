<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    Ext.onReady(function(){
        
        Ext.MessageBox.show({
            title: 'Error',
            msg: 'You have been denied access and will now be logged out.',
            width:300,
            buttons: Ext.MessageBox.OK,
            icon : Ext.MessageBox.ERROR,
            fn: function redirectToAccessDeniedPage(){
                window.location = "<%= request.getContextPath()%>/j_spring_security_logout"; 
            }
        });
    })

</script>
