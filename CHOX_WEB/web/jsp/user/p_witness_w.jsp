
<%@ taglib uri="/struts-tags" prefix="s" %>     

<script language="JavaScript">

        $(document).ready(function(){
            $("#formUpdateWitness").validate(
            {
                errorLabelContainer: "#WitnessMessageBox",                
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

<form id="formUpdateWitness" name="formUpdateWitness" action="user/updateWitness.action" class="XXentity-form">
   
    <fieldset class="x-fieldset">        
        <legend>Witness Details</legend>
            <div style="display:none" class="form-container">  
             <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
             <input type="hidden" name="incidentId" value='<s:property value="incidentId"/>'>    
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Name<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div>        
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 1</label>
            <input type="text" class="chox-ttxt" name="address1" value='<s:property value="address1" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 2</label>
            <input type="text" class="chox-ttxt" name="address2" value='<s:property value="address2" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 3</label>
            <input type="text" class="chox-ttxt" name="address3" value='<s:property value="address3" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 4</label>
            <input type="text" class="chox-ttxt" name="address4" value='<s:property value="address4" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Address 5</label>
            <input type="text" class="chox-ttxt" name="address5" value='<s:property value="address5" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Postcode</label>
            <input type="text" class="chox-ttxt" name="postcode" value='<s:property value="postcode" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Telephone Day</label>
            <input type="text" class="chox-ttxt" name="telephoneDay" value='<s:property value="telephoneDay" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Telephone Evening</label>
            <input type="text" class="chox-ttxt" name="telephoneEvening" value='<s:property value="telephoneEvening" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Email</label>
            <input type="text" class="chox-ttxt" name="email" value='<s:property value="email" />'/></div>
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" /><div class="chox-form-submit-result"></div>
            </div>
            <div id="WitnessMessageBox" class="errorBox"></div>
        </div>
    </fieldset>
</form>