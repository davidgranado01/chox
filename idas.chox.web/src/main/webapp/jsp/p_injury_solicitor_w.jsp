<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    Ext.onReady(function() {
        var form = $("#formupdateSolicitor");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});
        
        ui.ajaxForm(form,null,'html');
        
        $("#solicitorSuccessBox").fadeOut(10000);
    }); 

</script>

<form id="formupdateSolicitor" action="<%=request.getContextPath()%>/prv/p/updateSolicitor.action" class="XXentity-form" name="formupdateSolicitor">
    <input name="claimId" type="hidden" value="<s:property value="claimId" />" />
    <fieldset class="x-fieldset partial">
        <legend>Injury Solicitor</legend>
        <div class="form-container" id="injurySolicitorWId">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Name</label>
                <input type="text" class="chox-ttxt" id="ISOLName" name="solicitor.name" value='<s:property value="solicitor.name" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 1</label>
                <input type="text" class="chox-ttxt" id="ISOLAddress1" name="solicitor.address1" value="<s:property value="solicitor.address1" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 2</label>
                <input type="text" class="chox-ttxt" id="ISOLAddress2" name="solicitor.address2" value="<s:property value="solicitor.address2" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 3</label>
                <input type="text" class="chox-ttxt" id="ISOLAddress3" name="solicitor.address3" value="<s:property value="solicitor.address3" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 4</label>
                <input type="text" class="chox-ttxt" id="ISOLAddress4" name="solicitor.address4" value="<s:property value="solicitor.address4" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 5</label>
                <input type="text" class="chox-ttxt" id="ISOLAddress5" name="solicitor.address5" value="<s:property value="solicitor.address5" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Postcode</label>
                <input type="text" class="chox-ttxt" id="ISOLPostcode" name="solicitor.postcode" value="<s:property value="solicitor.postcode" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Telephone Day</label>
                <input type="text" class="chox-ttxt" id="ISOLTelephone" name="solicitor.telephone" value="<s:property value="solicitor.telephone" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Email</label>
                <input type="text" class="chox-ttxt" id="ISOLEmail" name="solicitor.email" value="<s:property value="solicitor.email" />" /></div>
            <div class="chox-form-button">
                <input type="submit" id="ISOLSubmitButtonId" value="Save Changes" />
            </div>
            <div id="solicitorMsgBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div id="solicitorSuccessBox" class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
</form>