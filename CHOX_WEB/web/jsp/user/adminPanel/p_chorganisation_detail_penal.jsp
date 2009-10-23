<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
        
        var selectedPanel = "CreditHireOrgMgmt";
        var isNew = false;

        $(document).ready(function(){
            
            
            var objectId = <s:property value="objectId"/>;
            
            if(objectId<0){
                isNew = true;
            }
            
            $.validator.addMethod(
                "regex",
                function(value, element, regexp) {
                    var check = false;
                    var re = new RegExp(regexp);
                    return this.optional(element) || re.test(value);
                }, "Please check your input."
            );
                
            doFormValidation(); 
        }); 
        
        function doFormValidation(){
                    
            var validateFlag = $("#formUpdateChorganisationDetail").validate(
            {
               errorLabelContainer: "#CDmessageBox",                
               rules: {
                 name:{
                     required:true
                 },
                 vatNo:{
                     required:true, number:true
                 },
                 companyNo:{
                     required:true, number:true
                 },
                 address1:{
                     required:true
                 },
                 address2:{
                     required:true
                 },
                 address4:{
                     required:true, regex: "^\\s*[a-zA-Z.,\\s]+\\s*$"
                 },
                 address5:{
                     required:true, regex: "^\\s*[a-zA-Z,.\\s]+\\s*$"
                 },
                 postcode:{
                     required:true
                 },  
                phone:{
                    regex:"^(\\(?\\+?[0-9]*\\)?)?[0-9_\\- \\(\\)]*$"
                }
               },
               messages: {
                 name:{
                     required:"You must supply a value for 'Name'"
                 },
                 vatNo:{
                     required:"You must supply a value for 'VAT No.'",
                     number:"'VAT No.' must be number"
                 },
                 companyNo:{
                     required:"You must supply a value for 'Company No.'",
                     number:"'Company No' must be number"
                 },
                 address1:{
                     required:"You must supply a value for 'Address 1'"
                 },
                 address2:{
                     required:"You must supply a value for 'Address 2'"
                 },
                 address4:{
                     required:"You must supply a value for 'County'",
                     regex:"'County' must be letters only"
                 },
                 address5:{
                     required:"You must supply a value for 'Country'", 
                     regex:"'Country' must be letters only"
                 },
                 postcode:{
                     required:"You must supply a value for 'Postcode'"
                 },  
                phone:{
                    regex:"'Telephone Number' must be numeric"
                }
                     
               },
               submitHandler: function(form) {
                    // $(form).ajaxSubmit(op);
               }
            });
            
            return validateFlag;
        }         
        
        function doSubmit(){
            
            var confirmationMsg = "Do you wish to accept changes?";
            
            if(isNew){
                confirmationMsg = "Are you sure you wish to add this credit hire?";
            }
            
            if(doFormValidation().form()){
                
                if(confirm(confirmationMsg)){
                
                    $("#admin_param_panel").block();

                    var op = { 
                        beforeSubmit:  onBeforeSubmit,
                        success:       onSubmitResponseReceived,
                        timeout: 3000,
                        error: onSubmitError
                    };

                    $("#formUpdateChorganisationDetail").ajaxSubmit(op);
                
                }
            }
        }
        
        function doBack(){
            $("#admin_param_panel").load("loadAdminPanel.action?adminPanelName=" + selectedPanel);
        }
        
        function onBeforeSubmit(formData, jqForm, options) { 
        }

         function onSubmitResponseReceived(responseText, statusText){

            var response = eval('(' + responseText.trim() + ')');
            var output = "Your changes have been saved";

            if(response && response.isValid){

                if(response.resultType == "New" && response.result)
                {
                    var newObjectId = parseInt(response.result);
                    $("#admin_param_panel").load("updateChorganisationDetailPanel.action?objectId=" + newObjectId);
                     propmtMsg("New credit hire organisation has been created");
                }
                else
                {
                    output = "Your changes have been saved.";
                }
            }
            else if(response && response.errors){
                
                output = formErrorMessage(response.errors);
            }
            else
            {
                output = "Unknown Error Encountered, please try again.";
            }
            $(".chox-form-submit-result").html(output);
            $("#admin_param_panel").unblock();
        }

        function onSubmitError(XMLHttpRequest, textStatus, errorThrown) {
            $("#admin_param_panel").unblock();
            alert("Error");  
        }

</script>

<form id="formUpdateChorganisationDetail" action="user/updateChorganisationDetail.action" class="XXentity-form" method="post" onsubmit="return true;">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
    
            <fieldset class="x-fieldset">
                <legend>Credit Hire Details</legend>
                <div class="form-container" style="height:632px;">
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Name<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDName" name="name" value="<s:property value="name" />"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">VAT No.<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDVatNo" name="vatNo" value="<s:property value="vatNo" />"/>
                    </div>        
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Company No.<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDCompanyNo" name="companyNo" value="<s:property value="companyNo" />"/>
                    </div>                          
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Address 1<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDAddress1" name="address1" value="<s:property value="address1" />"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Address 2<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDAddress2" name="address2" value="<s:property value="address2" />"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Address 3</label>
                        <input type="text" class="chox-ttxt" id="CCDAddress3" name="address3" value="<s:property value="address3" />"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Postcode<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDPostcode" name="postcode" value="<s:property value="postcode" />"/>
                    </div>                      
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">County<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDAddress4" name="address4" value="<s:property value="address4" />"/>
                    </div>  
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Country<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDAddress5" name="address5" value="<s:property value="address5" />"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Telephone Number</label>
                        <input type="text" maxlength="50" class="chox-ttxt" id="CCDPhone" name="phone" value="<s:property value="phone" />"/>
                    </div>                           
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Delegated Authority</label>
                        <s:checkbox name="delegatedAuthority" value="delegatedAuthority" />
                    </div>                      
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Active</label>
                        <s:checkbox name="status" value="status" />
                    </div>
                    <div class="chox-form-button">
                        <input type="button" value="Save Changes" onclick="javascript: doSubmit();"/>
                        <input type="button" value="Cancel" class="cancel" onclick="javascript: doBack();" />
                    </div>
                        <div id="CDmessageBox" class="errorBox"></div>
                        <div class="chox-form-submit-result"></div>          
                </div>
            </fieldset>
            <div id="CDmessageBox" style="text-align:center"></div>  
        </form>
