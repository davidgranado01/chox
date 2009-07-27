<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
        
        var selectedPanel = 'UserMgmt';
        var lineOfBusinessId = $("#h_lineOfBusinessId").val();
        var passwordValidateErrorMsg = "";
        
        $(document).ready(function(){        

        $.validator.addMethod(
                "regex",
                function(value, element, regexp) {
                    var check = false;
                    var re = new RegExp(regexp);
                    return this.optional(element) || re.test(value);
                },
                "Please check your input."
        );
            doUserSearchSelectOnChange();
            doFormValidation(); 
        }); 
        
        function getClaimHandlervalidation(){
            var bFlag = false;
            if($("#h_mode").val() =='Edit' && $("#h_isClaimHandler").val()=='true'){
                bFlag = true;
            }
            return bFlag;
        }
        
        function doFormValidation(){
            
            var validateFlag = $("#formUpdateUserDetail").validate(
            {
               errorLabelContainer: "#CDmessageBox",
               rules: {
                 email:{required:true, email: true},
                 firstName:{required:true},
                 lastName:{required:true},
                 password:{required:true, regex: "^.*(?=.{6,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$"},
                 confirmNewPassword:{equalTo: "#password"},
                 insurerId:{required:true},
                 supplierId:{required:true},
                 lineOfBusinessId:{required:getClaimHandlervalidation()}
               },
               messages: {
                 email:{required:"You must supply a value for 'Email'", email: "Incorrect email format"},
                 firstName:{required:"You must supply a value for 'First Name'"},
                 lastName:{required:"You must supply a value for 'Last Name'"},
                 password:{required:"You must supply a value for 'Password'", regex: "Incorrect Password Format"},
                 confirmNewPassword:{equalTo: "Your passwords do not match"},
                 insurerId:{required:"Please select 'Insurer Company'"},
                 supplierId:{required:"Please select 'Credit Hire Organisation'"},
                 lineOfBusinessId:{required:"Please select 'Line of Business'"}
               },
               submitHandler: function(form) {
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
            confirm("New user setup successful. Please assign a role(s) to the new user.");
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

    function doUserSearchSelectOnChange(){
        
        var selectedInsurerId = -1;
        
        if($("#insurerId").val()!=null && $("#insurerId").val()!=''){
            selectedInsurerId = $("#insurerId").val();
        }
        
        $("#userDetailScreenChobandDropDownDiv").load("LineOfBusinessDropDownByIdAction.action?orgId=" + selectedInsurerId);
        
    }

    
</script>

<div>
    
    <form id="formUpdateUserDetail" action="user/updateUserDetail.action" class="XXentity-form" onsubmit="return true;" method="post">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
    <input type="hidden" name="orgTypeId" id="orgTypeId" value='<s:property value="orgTypeId"/>'>
    
    <input type="hidden" name="h_isClaimHandler" id="h_isClaimHandler" value='<s:property value="claimHandler"/>'>
    <input type="hidden" name="h_mode" id="h_mode" value='<s:property value="mode"/>'>
    <input type="hidden" name="h_lineOfBusinessId" id="h_lineOfBusinessId" value='<s:property value="lineOfBusiness.id" />'>
    
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
                    
                    <s:if test="orgTypeId==2">
                        <input name="insurerId" id="insurerId" type="hidden" value="<s:property value="insurer.id" />">
                    </s:if>
                    
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Email</label>
                        <input type="text" class="chox-ttxt" id="CCDEmail" name="email" value="<s:property value="email" />"/>
                    </div>
                    
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">&nbsp;</label>
                        <span class="column_remark">Please note this (Email) will be the user's Username</span>
                    </div>
                    
                </s:if>
                <s:else>

                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Email<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDEmail" name="email" value="<s:property value="email" />"/>
                    </div>

                    <div class="chox-form-item">
                        <label class="chox-form-std-label">&nbsp;</label>
                        <span class="column_remark">Please note this (Email) will be the user's Username</span>
                    </div>
                    
                    <s:if test="orgTypeId==2">
                            
                        <s:if test="isOrgSelectable">
                                
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Insurer Company<span class="mandatory">*</span></label>
                                <s:select id="insurerId" name="insurerId" 
                                    list="insurers" 
                                    listKey="id" 
                                    listValue="name" 
                                    headerKey=""
                                    headerValue="--- ALL ---"
                                    onchange="javascript: doUserSearchSelectOnChange();"
                                    emptyOption="false">
                                </s:select>
                            </div>  
                    
                        </s:if>
                        <s:else>
                            
                            <s:if test="isChoxAdmin">
                                <input name="insurerId" id="insurerId" type="hidden" value="<s:property value="insurer.id" />">  
                            </s:if>
                            <s:else>
                                <input name="insurerId" id="insurerId" type="hidden" value="<s:property value="insurerId" />">  
                            </s:else>
                            
                            
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
     
                </s:else>
                
                <s:if test="orgTypeId==2 && claimHandler">
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Line Of Business</label>
                        <div id="userDetailScreenChobandDropDownDiv"></div> 
                    </div>
                </s:if>                 

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
                    <input type="password" class="chox-ttxt" id="password" name="password" value="<s:property value="password" />" size="10" maxlength="10"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">&nbsp;</label>
                    <span class="column_remark">Must be at least 6 characters. Must contain at least one one lower case letter, one upper case letter.</span>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Re-enter Password<span class="mandatory">*</span></label>
                    <input type="password" class="chox-ttxt" name="confirmNewPassword" id="confirmNewPassword" size="10" maxlength="10" value="<s:property value="password" />"/>
                </div>  
                <div class="chox-form-item">
                        <label class="chox-form-std-label">&nbsp;</label>
                        <span class="column_remark">N.B. Passwords are case sensitive.</span>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Active</label>
                    <s:checkbox name="status" value="status" />
                </div>
                <div class="chox-form-button">
                    <input type="button" value="Save Changes" onclick="javascript: return doSubmit();"/>
                    <input type="button" value="Cancel" class="cancel" onclick="javascript: return doCancelBack();" />
                </div>
                <div class="chox-form-submit-result"></div>                 
                </div>
            </fieldset>
            <div id="CDmessageBox" style="text-align:center" class="errorBox"></div>  
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
