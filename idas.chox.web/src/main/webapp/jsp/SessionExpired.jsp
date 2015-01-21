<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    Ext.onReady(function(){
        Ext.MessageBox.show({
            title: 'Error',
            msg: 'Your session has expired. Please login again.',
            width:300,
            buttons: Ext.MessageBox.OK,
            icon : Ext.MessageBox.ERROR,
            fn: function redirectToLoginPage(){
               window.location = "<%= request.getContextPath()%>/login.action"; 
//                logout();
            }
        });
    });

</script>
