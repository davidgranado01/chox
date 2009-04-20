<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
        
        var selectedPanel = "InsurerOrgMgmt";
        
        $(document).ready(function(){
            doFormValidation(); 
        }); 
        
        function doFormValidation(){
                    
            var validateFlag = $("#formUpdateInsurerDetail").validate(
            {
               errorLabelContainer: "#CDmessageBox",                
               rules: {
                 name:{
                     required:true
                 },
                 adminHandlingCharge:{
                     required:true, number:true, min:0
                 }
               },
               messages: {
                 name: {
                   required:"You must supply a value for 'Name'"
                 }, 
                 adminHandlingCharge: {
                   required:"You must supply a value for 'Admin Handling Charge'",
                   number:"'Admin Handling Charge' must be numeric",
                   min:"'Admin Handling Charge' cannot be less than zero"
                 }
                     
               },
                submitHandler: function(form) {
                    // $(form).ajaxSubmit(op);
                }
            });
            
            return validateFlag;
        }
        
        function doSubmit(){
            
            if(doFormValidation().form()){
                var op = { 
                    beforeSubmit:  onBeforeSubmit,
                    success:       onSubmitResponseReceived,
                    timeout: 3000,
                    error: onSubmitError
                };

                $("#formUpdateInsurerDetail").ajaxSubmit(op);
            }
        }
        
        function doBack(){
            $("#admin_param_panel").load("loadAdminPanel.action?adminPanelName=" + selectedPanel);
        }
        
        function onBeforeSubmit(formData, jqForm, options) { 
        }

        function onSubmitResponseReceived(responseText, statusText)  {      
            responseText = responseText.trim();
            $(".chox-form-submit-result").html(responseText);
        }    

        function onSubmitError(XMLHttpRequest, textStatus, errorThrown) {
            alert("Error");  
        }
        
</script>

<form id="formUpdateInsurerDetail" action="user/updateInsurerDetail.action" class="XXentity-form" onsubmit="return true;">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
    
            <fieldset class="x-fieldset">
                <legend>Insurer Details</legend>
                <div class="form-container">
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Name<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDName" name="name" value="<s:property value="name" />"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Admin Handling Charge<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDAdminHandlingCharge" name="adminHandlingCharge" value="<s:property value="adminHandlingCharge" />"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Status</label>
                        <s:checkbox name="status" value="status" />
                    </div>                   
                    <div class="chox-form-button">
                        <input type="submit" value="Save Changes" onclick="javascript: doSubmit();"/>
                        <input type="submit" value="Cancel" class="cancel" onclick="javascript: doBack();" />
                    </div>
                        <div id="CDmessageBox" class="errorBox"></div>
                        <div class="chox-form-submit-result"></div>                    
                </div>
            </fieldset>
            <div id="CDmessageBox" style="text-align:center"></div>  
        </form>
