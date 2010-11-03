<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    Ext.onReady(function(){
        Ext.MessageBox.alert('Status', 'Claim with supplier Ref no:<s:property value="%{ChoRef}"/> successfully moved to <s:property value="%{InsurerName}"/>', function() {
             window.location = "<%= request.getContextPath()%>/prv/inbox.action";
        });
    })

</script>
