<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    Ext.onReady(function(){
        Ext.MessageBox.show({
            title: 'Internal Error Occurred',
            msg: 'Request can not be completed. Please try again.',
            width:300,
            buttons: Ext.MessageBox.OK,
            icon : Ext.MessageBox.ERROR
            ,fn: function reloadThePage(){
                window.history.back();
            }
        });
    })

</script>
