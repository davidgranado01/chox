<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
        
        var selectedPanel = 'UserMgmt';
        
        $(document).ready(function(){
            doFormValidation(); 
        }); 
                
        function doFormValidation(){
                    
            var validateFlag = $("#formUpdateUserDetail").validate(
            {
               errorLabelContainer: "#CDmessageBox",                
               rules: {
                 email:{required:true, email: true},
                 firstName:{required:true},
                 lastName:{required:true},
                 password:{required:true},
                 confirmNewPassword:{equalTo: "#password"},
                 insurerId:{required:true},
                 supplierId:{required:true}
               },
               messages: {
                 email:{required:"You must supply a value for 'Email'", email: "Incorrect email format"},
                 firstName:{required:"You must supply a value for 'First Name'"},
                 lastName:{required:"You must supply a value for 'Last Name'"},
                 password:{required:"You must supply a value for 'Password'"},
                 confirmNewPassword:{equalTo: "Your passwords do not match"},
                 insurerId:{required:"Please select 'Insurer Company'"},
                 supplierId:{required:"Please select 'Credit Hire Organisation'"}
               },
                submitHandler: function(form) {
                    // $(form).ajaxSubmit(op);
                }
            });
            
            return validateFlag;
        }                  
        function onBeforeSubmit(formData, jqForm, options) {
            
        }

        function onSubmitResponseReceived(responseText, statusText)  {      
            responseText = responseText.trim();
            $(".chox-form-submit-result").html(responseText);
        }     

        function onSubmitError(XMLHttpRequest, textStatus, errorThrown) {
            // responseText = responseText.trim();
            // alert("Error" + responseText);  
        }
        
        function doSubmit(){
            if(doFormValidation().form()){
                var op = { 
                    beforeSubmit:  onBeforeSubmit,
                    success:       onSubmitResponseReceived,
                    timeout: 3000,
                    error: onSubmitError
                };

                $("#formUpdateUserDetail").ajaxSubmit(op);
            }
        }
        
        function doBack(){
            $("#admin_param_panel").load("loadAdminPanel.action?adminPanelName=" + selectedPanel + "&selectOrgTypeId="+<s:property value="orgTypeId"/>);
        }
        
</script>

<div>
    <form id="formUpdateUserDetail" action="user/updateUserDetail.action" class="XXentity-form" method="post" onsubmit="return true;">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
    <input type="hidden" name="orgTypeId" value='<s:property value="orgTypeId"/>'>
    
            <fieldset class="x-fieldset">
                <legend>User Details</legend>
                <div class="form-container">
                    
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Organisation Type</label>
                        <b><s:property value="orgTypeName" /></b>
                    </div>
                    
                    <s:if test="mode=='Edit'">
                        
                        <div class="chox-form-item">
                            <label class="chox-form-std-label">Organisation Name</label>
                            <b><s:property value="orgName" /></b>
                        </div>
                        
                        <div class="chox-form-item">
                            <label class="chox-form-std-label">Email</label>
                            <b><s:property value="email" /></b>
                        </div>
                        
                    </s:if>
                    <s:else>

                        <s:if test="orgTypeId==2">
                            
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Insurer Company<span class="mandatory">*</span></label>
                                <s:select 
                                id="insurerId"                                 
                                name="insurerId" 
                                list="insurers" 
                                listKey="id" 
                                listValue="name" 
                                headerKey=""
                                headerValue="--- ALL ---"
                                emptyOption="false">
                                </s:select>
                            </div>       
                            
                        </s:if>

                        <s:if test="orgTypeId==3">
                            
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Credit Hire Company<span class="mandatory">*</span></label>
                                <s:select 
                                id="supplierId"                                
                                name="supplierId" 
                                list="suppliers" 
                                listKey="id" 
                                listValue="name" 
                                headerKey=""
                                headerValue="--- ALL ---"
                                emptyOption="false">
                                </s:select>
                            </div>            
                            
                        </s:if>

                        
                        <div class="chox-form-item">
                            <label class="chox-form-std-label">Email<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDEmail" name="email" value="<s:property value="email" />"/>
                        </div>                        
                        
                    </s:else>
                    
 
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">First Name<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDFirstName" name="firstName" value="<s:property value="firstName" />"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Last Name<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDLastName" name="lastName" value="<s:property value="lastName" />"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Password<span class="mandatory">*</span></label>
                        <input type="password" class="chox-ttxt" id="password" name="password" value="<s:property value="password" />" size="10" maxlength="8"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Re-enter new password<span class="mandatory">*</span></label>
                        <input type="password" class="chox-ttxt" name="confirmNewPassword" id="confirmNewPassword" size="10" maxlength="8" value="<s:property value="password" />"/>
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
</div>

<s:if test="orgTypeId!=1 && mode=='Edit'">
    <div>
            <s:action name="getUserroleMapping" executeResult="true">
                <s:param name="webUserId"><s:property value="id" /></s:param> 
                <s:param name="orgTypeId"><s:property value="orgTypeId" /></s:param>         
            </s:action>
    </div>
</s:if>
