<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    Ext.onReady(function(){
        <%session.invalidate();%>
        Ext.MessageBox.show({
            title: 'Error',
            msg: 'You have been denied access and will now be logged out.',
            width:300,
            closable : false,
            buttons: Ext.MessageBox.OK,
            icon : Ext.MessageBox.ERROR,
            fn: logout
        });
    })

</script>
