       
<%@ taglib uri="/struts-tags" prefix="s" %>






    <script language="JavaScript">
        

        $(document).ready(function(){
            
            $("#formUpdateCustomerDetails").validate(
            {
               errorLabelContainer: "#CDmessageBox",                
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
                 address1:{
                     required:true
                 },
                 postcode:{
                     required:true
                 },
                 telephoneDay:{
                     required:true
                 },   
                 vehicleClassId:{
                     min:1
                 },
                 insurerId:{                     
                     min:1
                 },
                 vehicleManufacturer:{
                     required:true
                 },
                 policyNumber:{
                     required:true
                 },
                 vehicleRegistration:{
                     required:true
                 },
                 location:{
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
                 address1: {
                   required:"You must supply a value for 'Address 1'"
                 },  
                 postcode: {
                   required:"You must supply a value for 'Postcode'"
                 },  
                 telephoneDay: {
                   required:"You must supply a value for 'Telephone Day'"
                 }, 
                 vehicleClassId:{
                     min: "You must select a Vehicle Class"
                 }, 
                 insurerId:{
                     min: "You must select an Insurer"
                 },
                 vehicleManufacturer: {
                   required:"You must supply a value for 'Vehicle Manufacturer'"
                 },  
                 policyNumber: {
                   required:"You must supply a value for 'Policy Number'"
                 },  
                 vehicleRegistration: {
                   required:"You must supply a value for 'Vehicle Registration'"
                 },  
                 location: {
                   required:"You must supply a value for 'Vehicle Location'"
                 }
               },
                submitHandler: function(form) {
                    $(form).ajaxSubmit(globalEntityFormOptions);
                }
            });
        }); 
        




        
        </script>








<form id="formUpdateCustomerDetails" action="user/updateCustomer.action" class="XXentity-form">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    
    
    
    
            <fieldset class="x-fieldset">
                <legend>Customer Details</legend>
                <div style="display:none" class="form-container">
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Title</label>
                    <input type="text" class="chox-ttxt" id="CCDTitle" name="title" value='<s:property value="title" />'/></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        First Name(s)</label>
                    <input type="text" class="chox-ttxt" id="CCDFirstName" name="firstName" value='<s:property value="firstName" />'/></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Surname</label>
                    <input type="text" class="chox-ttxt" id="CCDSurname" name="lastName"  value='<s:property value="lastName" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Address 1</label>
                    <input type="text" class="chox-ttxt" id="CCDAddress1"  name="address1" value='<s:property value="address1" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Address 2</label>
                    <input type="text" class="chox-ttxt" id="CCDAddress2" name="address2" value='<s:property value="address2" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Address 3</label>
                    <input type="text" class="chox-ttxt" id="CCDAddress3" name="address3" value='<s:property value="address3" />'/></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Address 4</label>
                    <input type="text" class="chox-ttxt" id="CCDAddress4" name="address4" value='<s:property value="address4" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Address 5</label>
                    <input type="text" class="chox-ttxt" id="CCDAddress5" name="address5" value='<s:property value="address5" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Postcode</label>
                    <input type="text" class="chox-ttxt" id="CCDPostcode" name="postcode" value='<s:property value="postcode" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Telephone Day</label>
                    <input type="text" class="chox-ttxt" id="CCDTeleponeDay" name="telephoneDay" value='<s:property value="telephoneDay" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Telepone Evening</label>
                    <input type="text" class="chox-ttxt" id="CCDTeleponeEvening" name="telephoneEvening"  value='<s:property value="telephoneEvening" />'/></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Email</label>
                    <input type="text" class="chox-ttxt" id="CCDEmail" name="email" value='<s:property value="email" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Insurer</label>
                        <s:select name="insurerId" 
                        list="insurers" 
                        listKey="id" 
                        listValue="name"
                        headerKey="-1"
                        headerValue="--SELECT--"
                        emptyOption="false"></s:select></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Policy Number</label>
                    <input type="text" class="chox-ttxt" id="CCDPolicyNumber" name="policyNumber" value='<s:property value="policyNumber" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                            Claim Number
                        </label>
                    <input type="text" class="chox-ttxt" id="CCDClaimNumber" name="claimReference" value='<s:property value="claimReference" />' /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Comprehensive</label>
                    <s:checkbox name="comprehensive" /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Vehicle Manufacturer</label>
                    <input type="text" class="chox-ttxt" id="CCDVehicleManufacturer" name="vehicleManufacturer" value='<s:property value="vehicleManufacturer" />' /></div>
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
                     <div class="chox-form-item">
                    <label class="chox-form-std-label">Vehicle Registration Number</label>
                        <input type="text" class="chox-ttxt" id="CCDVehicleRegistration" name="vehicleRegistration" value='<s:property value="vehicleRegistration" />' />                  
                    </div>                      

                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                        Vehicle Location</label>
                    <input type="text" class="chox-ttxt" id="CCDVehicleLocation"  name="location" value='<s:property value="location" />' /></div>
                    <div class="chox-form-button">
                        <input type="submit" value="Save Changes" />

                    </div>   
                        
                        <div id="CDmessageBox" class="errorBox"></div>
                        <div class="chox-form-submit-result"></div>                    
                </div>
            </fieldset>
        </form>
