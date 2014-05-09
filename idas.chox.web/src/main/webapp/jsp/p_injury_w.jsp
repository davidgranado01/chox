<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    Ext.onReady(function() {
        var form = $("#formUpdateInjury");
        
        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        form.validate(
        {
            errorLabelContainer: "#injuryMsgBox",
            rules: {
                email:{email:true}
            },
            messages: {
                email:{email:"You must supply a valid email address for 'Email'"}
            }
        });
        ui.ajaxForm(form,null,'html');
        $("#injurySuccessBox").fadeOut(10000);
    });
</script>

<form id="formUpdateInjury" action="<%=request.getContextPath()%>/prv/p/updateInjury.action" class="XXentity-form" name="formUpdateInjury">
    <input name="claimId" type="hidden" value="<s:property value="claimId" />" />
    <fieldset class="x-fieldset partial">
        <legend>Injury</legend>
        <div class="form-container" id="injuryWId">

            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Name</label>
                <input type="text" class="chox-ttxt" id="INJName" name="name" value="<s:property value="name" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 1</label>
                <input type="text" class="chox-ttxt" id="INJAddress1" name="address1" value="<s:property value="address1" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 2</label>
                <input type="text" class="chox-ttxt" id="INJAddress2" name="address2" value="<s:property value="address2" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 3</label>
                <input type="text" class="chox-ttxt" id="INJAddress3" name="address3" value="<s:property value="address3" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 4</label>
                <input type="text" class="chox-ttxt" id="INJAddress4" name="address4" value="<s:property value="address4" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 5</label>
                <input type="text" class="chox-ttxt" id="INJAddress5" name="address5" value="<s:property value="address5" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Postcode</label>
                <input type="text" class="chox-ttxt" id="INJPostcode" name="postcode" value="<s:property value="postcode" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Telephone Day</label>
                <input type="text" class="chox-ttxt" id="INJTelephoneDay" name="telephoneDay" value="<s:property value="telephoneDay" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Telephone Evening</label>
                <input type="text" class="chox-ttxt" id="INJTelephoneEvening" name="telephoneEvening" value="<s:property value="telephoneEvening" />" /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Email</label>
                <input type="text" class="chox-ttxt" id="INJEmail" name="email" value="<s:property value="email" />" /></div>

            <div class="chox-form-button">
                <input type="submit" id="INJSubmitButtonId"value="Save Changes" />
            </div>
            <div id="injuryMsgBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div id="injurySuccessBox" class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
    <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    <!--s:token/-->
</form>