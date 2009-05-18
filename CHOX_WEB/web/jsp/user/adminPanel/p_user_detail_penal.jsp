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

        function onSubmitResponseReceived(responseText, statusText){
            
            responseText = responseText.trim();
            var output = "Your changes have been saved.";
            
            if(responseText != "" && responseText != "1" && responseText.substring(0,9) == 'objectId:'){
                
                var newObjectId =  parseInt(responseText.substring(9,responseText.length));
                var orgTypeId = $("#orgTypeId").val();
                $("#admin_param_panel").load("updateUserDetailPanel.action?mode=Edit&objectId=" + newObjectId + "&orgTypeId=" + orgTypeId);
                
            }else{
                output = responseText;
                $(".chox-form-submit-result").html(output);
            }
            
            $("#admin_param_panel").unblock();
        }

        function onSubmitError(XMLHttpRequest, textStatus, errorThrown) {
            $("#admin_param_panel").unblock();
        }
        
        function doSubmit(){
            
            if(doFormValidation().form()){
                
                $("#admin_param_panel").block();
                
                var op = { 
                    beforeSubmit:  onBeforeSubmit,
                    success:       onSubmitResponseReceived,
                    timeout: 3000,
                    error: onSubmitError
                };

                $("#formUpdateUserDetail").ajaxSubmit(op);
            }
        }
        
        function doCancelBack(){
            $("#admin_param_panel").load("loadAdminPanel.action?adminPanelName=" + selectedPanel + "&selectOrgTypeId="+<s:property value="orgTypeId"/>);
        }
        
</script>

<div>
    <form id="formUpdateUserDetail" action="user/updateUserDetail.action" class="XXentity-form" onsubmit="return true;" method="post">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
    <input type="hidden" name="orgTypeId" id="orgTypeId" value='<s:property value="orgTypeId"/>'>
    
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
                            
                            <s:if test="isOrgSelectable">
                            
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
                            <s:else>
                                <input name="insurerId" id="insurerId" type="hidden" value="<s:property value="insurerId" />">
                            </s:else>                             
                        </s:if>

                        <s:if test="orgTypeId==3">
                            
                            <s:if test="isOrgSelectable">
                                
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
                            <s:else>
                                <input name="supplierId" id="supplierId" type="hidden" value="<s:property value="supplierId" />">
                            </s:else> 
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
                        <input type="button" value="Save Changes" onclick="javascript: return doSubmit();"/>
                        <input type="button" value="Cancel" class="cancel" onclick="javascript: return doCancelBack();" />
                    </div>
                        <div class="chox-form-submit-result"></div>                 
                </div>
            </fieldset>
            <div id="CDmessageBox" style="text-align:center"></div>  
        </form>
</div>

<s:if test="mode=='Edit'">
    <div>
            <s:action name="getUserroleMapping" executeResult="true">
                <s:param name="webUserId"><s:property value="id" /></s:param> 
                <s:param name="orgTypeId"><s:property value="orgTypeId" /></s:param>         
            </s:action>
    </div> 
</s:if>
