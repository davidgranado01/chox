<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    Ext.onReady(function(){
        Ext.MessageBox.show({
            title: 'Error',
            msg: 'An internal error occurred. Please try again.',
            width:300,
            closable : false,
            buttons: Ext.MessageBox.OK,
            icon : Ext.MessageBox.ERROR,
            fn: function reloadPage(){
                window.location.replace(document.referrer);
            }
        });
    })

</script>
