<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){
        var form = $("#formUpdateWitness");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        form.validate(
        {
            errorLabelContainer: "#witnessMsgBox",
            rules: {
                name:{required:true},
                email:{email:true}
            },
            messages: {
                name:{required:'Please supply a valid value for Name'},
                email:{email:'Please supply a valid email address for "Email"'}
            }
        });
        ui.ajaxForm(form,null,'html');
    });

</script>

<form id="formUpdateWitness" name="formUpdateWitness" action="<%=request.getContextPath()%>/prv/p/updateWitness.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <fieldset class="x-fieldset partial">
        <legend>Witness Details</legend>
        <div class="form-container" id="witnessDetailsWId">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Name<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" name="name" value="<s:property value="name" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 1</label>
                <input type="text" class="chox-ttxt" name="address1" value="<s:property value="address1" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 2</label>
                <input type="text" class="chox-ttxt" name="address2" value="<s:property value="address2" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 3</label>
                <input type="text" class="chox-ttxt" name="address3" value="<s:property value="address3" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 4</label>
                <input type="text" class="chox-ttxt" name="address4" value="<s:property value="address4" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Address 5</label>
                <input type="text" class="chox-ttxt" name="address5" value="<s:property value="address5" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Postcode</label>
                <input type="text" class="chox-ttxt" name="postcode" value="<s:property value="postcode" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Telephone Day</label>
                <input type="text" class="chox-ttxt" name="telephoneDay" value="<s:property value="telephoneDay" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Telephone Evening</label>
                <input type="text" class="chox-ttxt" name="telephoneEvening" value="<s:property value="telephoneEvening" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Email</label>
                <input type="text" class="chox-ttxt" name="email" value="<s:property value="email" />"/></div>
            <div class="chox-form-button"><input type="submit" value="Save Changes" /></div>
            <div id="witnessMsgBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    <!--s:token/-->
</form>