<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    Ext.onReady(function(){
        Ext.MessageBox.alert('Information', 'You must add your contact details before you can continue.', function() {
            window.location = "<%= request.getContextPath()%>/prv/openUserAccount.action?redirect=true&nonce=<%= session.getAttribute("SessionNonce")%>";
        });
    })

</script>
