
<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">

        $(document).ready(function(){
            $("#formUpdateInjuryt").validate(
            {
                errorLabelContainer: "#InjuryMessageBox",                
                rules: {  
                    name:{required:true}
                },
                messages: {
                    name:{required:"Please supply a valid value for Name"}
                },
                submitHandler: function(form) {
                    $(form).ajaxSubmit(globalEntityFormOptions);
                }                
            }); 
        });  

</script>

<form id="formUpdateInjuryt" action="user/updateInjury.action" class="XXentity-form" name="formUpdateInjuryt">
    
    <fieldset class="x-fieldset">
        <legend>Injury</legend>
        <div style="display:none" class="form-container">
            <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
            <input type="hidden" name="incidentId" value='<s:property value="incidentId"/>'>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Name<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttxt" id="INJName" name="name" value='<s:property value="name" />'  /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 1</label>
            <input type="text" class="chox-ttxt" id="INJAddress1" name="address1" value='<s:property value="address1" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 2</label>
            <input type="text" class="chox-ttxt" id="INJAddress2" name="address2" value='<s:property value="address2" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 3</label>
            <input type="text" class="chox-ttxt" id="INJAddress3" name="address3" value='<s:property value="address3" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 4</label>
            <input type="text" class="chox-ttxt" id="INJAddress4" name="address4" value='<s:property value="address4" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 5</label>
            <input type="text" class="chox-ttxt" id="INJAddress5" name="address5" value='<s:property value="address5" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Postcode</label>
            <input type="text" class="chox-ttxt" id="INJPostcode" name="postcode" value='<s:property value="postcode" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Telephone Day</label>
            <input type="text" class="chox-ttxt" id="INJTelephoneDay" name="telephoneDay" value='<s:property value="telephoneDay" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Telephone Evening</label>
            <input type="text" class="chox-ttxt" id="INJTelephoneEvening" name="telephoneEvening" value='<s:property value="telephoneEvening" />' /></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Email</label>
            <input type="text" class="chox-ttxt" id="INJEmail" name="email" value='<s:property value="email" />' /></div>
            
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" />
            </div>
            <div class="chox-form-submit-result">&nbsp;</div>
            <div id="InjuryMessageBox" style="text-align:center"></div>  
        </div>
    </fieldset>
</form>