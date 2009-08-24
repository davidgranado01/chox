
<%@ taglib uri="/struts-tags" prefix="s" %>



<script language="JavaScript">


    $(document).ready(function(){

        $("#formUpdateThirdParty").validate(
        {
           errorLabelContainer: "#TPmessageBox",                
           rules: {
             title:{
                 required:true
             },
             firstName:{
                 required:true
             },
             lastName:{
                 required:true
             },
             insurerId:{                     
                 min:1
             },
             policyNumber:{
                 required:true
             },
             vehicleRegistration:{
                 required:true
             }
           },
           messages: {
             title: {
               required:"You must supply a value for 'Title'"
             }, 
             firstName: {
               required:"You must supply a value for 'First Name'"
             },
             lastName: {
               required:"You must supply a value for 'Last Name'"
             },  
             insurerId:{
                 min: "You must select an Insurer"
             },
             policyNumber: {
               required:"You must supply a value for 'Policy Number'"
             },  
             vehicleRegistration: {
               required:"You must supply a value for 'Vehicle Registration'"
             }           
           },
            submitHandler: function(form) {
                $(form).ajaxSubmit(globalEntityFormOptions);
            }
        });
    }); 






        </script>



<form id="formUpdateThirdParty" action="user/updateThirdParty.action" class="XXentity-form">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <fieldset class="x-fieldset">
        <legend>Third-Party Details</legend>
        <div style="display:none" class="form-container">            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Title<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttxt" name="title" value="<s:property value="title" />"/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                First Name(s)<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttxt" name="firstName" value="<s:property value="firstName" />"/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Surname<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttxt" name="lastName" value="<s:property value="lastName" />"/></div>   
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
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Insurer<span class="mandatory">*</span></label>
             <s:select name="insurerId" 
                        list="insurers" 
                        listKey="id" 
                        listValue="name"
                        headerKey="-1"
                        headerValue="--SELECT--"
                        emptyOption="false"></s:select></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Policy Number<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttxt" name="policyNumber" value="<s:property value="policyNumber" />"/></div>   
<!--            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Claim Number<span class="mandatory">*</span>
                </label>
            <input type="text" class="chox-ttxt" name="claimReference" value="<s:property value="claimReference" />"/></div> 
!-->            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Vehicle Manufacturer</label>
            <input type="text" class="chox-ttxt" name="vehicleManufacturer" value="<s:property value="vehicleManufacturer" />"/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Vehicle Model</label>
            <input type="text" class="chox-ttxt" name="vehicleModel" value="<s:property value="vehicleModel" />"/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Vehicle Registration Number<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttxt" name="vehicleRegistration" value="<s:property value="vehicleRegistration" />"/></div>   
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Vehicle Class</label>
                <s:select name="vehicleClassId"  
                          list="vehicleClasses"  
                          listKey="id"  
                          listValue="name" 
                          headerKey="-1" 
                          headerValue="--SELECT--" 
                          emptyOption="false"></s:select>                               
                </div>   
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" /></div>
   
            
            <div id="TPmessageBox" class="errorBox"></div>
            <div class="chox-form-submit-result">&nbsp;</div>    
            
            </div>
            
            
    </fieldset>
</form>