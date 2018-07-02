<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    Ext.onReady(function(){
        Ext.MessageBox.show({
            title: 'CSRF Violation',
            msg: 'Request cannot be completed - please try again. If the problem persists, please contact CHOX suppport.',
            width:300,
            buttons: Ext.MessageBox.OK,
            icon : Ext.MessageBox.ERROR
            ,fn: function reloadThePage(){
                window.history.back();
            }
        });
    });

</script>
