<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    Ext.onReady(function() {
        var form = $("#formUpdateWitness");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        ui.ajaxForm(form,null,'html');
        $("#witnessSuccessBox").fadeOut(10000);
    });

</script>

<form id="formUpdateWitness" name="formUpdateWitness" action="<%=request.getContextPath()%>/prv/p/updateWitness.action" class="XXentity-form">
    <!--<input type="hidden" name="claimId" value='<s:property value="claimId"/>'>-->
    <fieldset class="x-fieldset partial">
        <legend>Witness Details</legend>
        <div class="form-container" id="witnessDetailsWId">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Name</label>
                <input type="text" class="chox-ttxt" name="name" id="witnessNameFieldId"value="<s:property value="name" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 1</label>
                <input type="text" class="chox-ttxt" name="address1" id="witnessAddress1FieldId" value="<s:property value="address1" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 2</label>
                <input type="text" class="chox-ttxt" name="address2" id="witnessAddress2FieldId" value="<s:property value="address2" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 3</label>
                <input type="text" class="chox-ttxt" name="address3" id="witnessAddress3FieldId" value="<s:property value="address3" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 4</label>
                <input type="text" class="chox-ttxt" name="address4" id="witnessAddress4FieldId" value="<s:property value="address4" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 5</label>
                <input type="text" class="chox-ttxt" name="address5" id="witnessAddress5FieldId" value="<s:property value="address5" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Postcode</label>
                <input type="text" class="chox-ttxt" name="postcode" id="witnessPostcodeFieldId" value="<s:property value="postcode" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Telephone Day</label>
                <input type="text" class="chox-ttxt" name="telephoneDay" id="witnessTextFieldId" value="<s:property value="telephoneDay" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Telephone Evening</label>
                <input type="text" class="chox-ttxt" name="telephoneEvening" id="witnessTelephoneEveningFieldId" value="<s:property value="telephoneEvening" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Email</label>
                <input type="text" class="chox-ttxt" name="email" id="witnessEmailFieldId" value="<s:property value="email" />"/></div>
            <div class="chox-form-button"><input type="submit" id="witnessSubmitButtonId" value="Save Changes" /></div>
            <div id="witnessMsgBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div id="witnessSuccessBox" class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
    <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    <!--s:token/-->
</form>