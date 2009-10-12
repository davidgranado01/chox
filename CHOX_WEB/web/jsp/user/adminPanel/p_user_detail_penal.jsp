<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">

    var userDetailPanelTabs;
    var userDetailTabIndex = 0;
    var isNew = true;
    var isClaimHandler = false;
    var selectedPanel = 'UserMgmt';
    var orgTypeId;
    var isWorkgroupDisabled = true;
    
    function setupUserDetailPanels()
    {  
        
       userDetailPanelTabs = new Ext.TabPanel({
       renderTo: 'userDetailMainPanel',
       height:660,
       autoScroll :true,
       activeTab: userDetailTabIndex,
       items:[
           {contentEl:'userDetailTab', title:'User Detail', listeners: {activate: handleActivate}},
           {contentEl:'userPasswordTab', title:'Change Password', disabled:isNew, listeners: {activate: handleActivate}},
           {contentEl:'userRoleTab', title:'User Roles', disabled:isNew, listeners: {activate: handleActivate}},
           {contentEl:'userWorkgroupTab', title:'Workgroups', disabled:isWorkgroupDisabled, listeners: {activate: handleActivate}}
       ]
       });
    }
    
    function handleActivate(tab){
        userDetailTabIndex = 0;
        if(userDetailPanelTabs) { userDetailTabIndex = userDetailPanelTabs.items.indexOf(userDetailPanelTabs.getActiveTab()); }
    }
    
    // GET CLAIM DETAIL
    function checkMode(){
        var mode = "<s:property value="mode"/>";
        if(mode!=null && mode!="" && mode=='Edit'){
            isNew = false
        }
    }

    function getTabIndex(){
        var tabIndex = "<s:property value="tabIndex"/>";
        if(tabIndex!=null && tabIndex!=""){
            userDetailTabIndex = tabIndex;
        }
    }
    
    function checkClaimHandler(){
        var inp = "<s:property value="claimHandler"/>";
        if(inp!=null && inp!="" && inp=='true'){
            isClaimHandler = true;
        }
    }
    
    function getOrgTypeId(){
        var inp = "<s:property value="orgTypeId"/>";
        if(inp!=null && inp!=""){
            orgTypeId = inp;
        }
    }

    // PAGE
    $(document).ready(function(){
        
        getOrgTypeId();
        checkMode();
        getTabIndex();
        checkClaimHandler();
        
        if(!isNew && isClaimHandler && orgTypeId=="2" && <s:property value="workgroupEnabled"/>){
            isWorkgroupDisabled = false;
        }
        
        setupUserDetailPanels();
        
        $.validator.addMethod(
            "regex",
            function(value, element, regexp) {
                var check = false;
                var re = new RegExp(regexp);
                return this.optional(element) || re.test(value);
            },
            "Please check your input."
        );
        
        if(!isNew){
            doFormValidation();
        }else{
            doFormNewValidation();
        }
        
    });

    function getClaimHandlervalidation(){
        
        var bFlag = false;
        if(!isNew && isClaimHandler){
            bFlag = true;
        }
        return bFlag;
    }

    function doFormValidation(){
        
        $("#CDmessageBox").html("");
        $(".chox-form-submit-result").html("");
        
        var validateFlag = $("#formUpdateUserDetail").validate(
        {
            errorLabelContainer: "#CDmessageBox",
            rules: {
                email:{required:true, email: true},
                firstName:{required:true},
                lastName:{required:true},
                insurerId:{required:true},
                supplierId:{required:true}
            },
            messages: {
                email:{required:"You must supply a value for 'Email'", email: "Incorrect email format"},
                firstName:{required:"You must supply a value for 'First Name'"},
                lastName:{required:"You must supply a value for 'Last Name'"},
                insurerId:{required:"Please select 'Insurer Company'"},
                supplierId:{required:"Please select 'Credit Hire Organisation'"}
            },
            submitHandler: function(form) {
            }
        });

        return validateFlag;
    }

    function doFormNewValidation(){

        $("#CDmessageBox").html("");
        $(".chox-form-submit-result").html("");
        
        var validateFlag = $("#formUpdateUserDetail").validate(
        {
            errorLabelContainer: "#CDmessageBox",
            rules: {
                email:{required:true, email: true},
                firstName:{required:true},
                lastName:{required:true},
                insurerId:{required:true},
                supplierId:{required:true},
                password:{required:true, regex: "^.*(?=.{6,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$"},
                confirmNewPassword:{equalTo: "#password"}
            },
            messages: {
                email:{required:"You must supply a value for 'Email'", email: "Incorrect email format"},
                firstName:{required:"You must supply a value for 'First Name'"},
                lastName:{required:"You must supply a value for 'Last Name'"},
                insurerId:{required:"Please select 'Insurer Company'"},
                supplierId:{required:"Please select 'Credit Hire Organisation'"},
                password:{required:"You must supply a value for 'Password'", regex: "Incorrect Password Format"},
                confirmNewPassword:{equalTo: "Your passwords do not match"}
            },
            submitHandler: function(form) {
            }
        });

        return validateFlag;
    }
    
    function doPasswordFormValidation(){
        
        $("#CDPswMessageBox").html("");
        $("#chox-form-password-submit-result").html("");
        
        var validateFlag = $("#formUpdatePassword").validate(
        {
            errorLabelContainer: "#CDPswMessageBox",
            rules: {
                password:{required:true, regex: "^.*(?=.{6,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$"},
                confirmNewPassword:{equalTo: "#password"}
            },
            messages: {
                password:{required:"You must supply a value for 'Password'", regex: "Incorrect Password Format"},
                confirmNewPassword:{equalTo: "Your passwords do not match"}
            },
            submitHandler: function(form) {
            }
        });

        return validateFlag;
    }

    function onSubmitResponseReceived(responseText, statusText){

        var response = eval('(' + responseText.trim() + ')');
        var output = "Your changes have been saved.";
        
        $("#chox-form-submit-result").attr("class", "chox-form-submit-result")

        if(response && response.isValid){
            
            if(response.resultType == 'New' && response.result){
                var newObjectId =  parseInt(response.result);
                var orgTypeId = $("#orgTypeId").val();
                $("#admin_param_panel").load("updateUserDetailPanel.action?mode=Edit&objectId=" + newObjectId + "&orgTypeId=" + orgTypeId);
            }
            else
            {
                output = "Your changes have been saved.";
                $("#chox-form-submit-result").html(output);
            }
            
        }
        else if(response && response.errors){
            output = formErrorMessage(response.errors);
            $("#chox-form-submit-result").attr("class", "submit-error")
            $("#chox-form-submit-result").html(output);
        }
        else
        {
            output = "Unknown Error Encountered, please try again.";
            $("#chox-form-submit-result").attr("class", "submit-error");
        }
        $("#admin_param_panel").unblock();
    }

    function onSubmitError(XMLHttpRequest, textStatus, errorThrown) {
        $("#admin_param_panel").unblock();
    }

    function doSubmit(){
        
        var isValid = false;
        if(!isNew){
            isValid = doFormValidation().form();
        }else{
            isValid = doFormNewValidation().form();
        }

        if(isValid){

            $("#admin_param_panel").block();

            var op = {
                success: onSubmitResponseReceived,
                timeout: 3000,
                error: onSubmitError
            };

            $("#formUpdateUserDetail").ajaxSubmit(op);
        }
    }

    function doSubmitNewPassword(){

        if(doPasswordFormValidation().form()){

            $("#admin_param_panel").block();

            var op = {
                success:       onSubmitUpdatePasswordResponseReceived,
                timeout: 3000,
                error: onSubmitError
            };

            $("#formUpdatePassword").ajaxSubmit(op);
        }
    }

    function onSubmitUpdatePasswordResponseReceived(responseText, statusText){
        
        var response = eval('(' + responseText.trim() + ')');
        
        if(response && response.isValid)
        {
            $("#chox-form-password-submit-result").html(response.result);
        }
        else if(response && response.errors){
                output = formErrorMessage(response.errors);
                $("#chox-form-password-submit-result").attr("class", "action-error-msg")
                $("#chox-form-password-submit-result").html(output);
        }
        else
        {
                output = "Unknown Error Encountered, please try again.";
                $("#chox-form-password-submit-result").attr("class", "action-error-msg")
                $("#chox-form-password-submit-result").html(output);
        }
        $("#admin_param_panel").unblock();
    }
    
    function doCancelBack(){
        $("#admin_param_panel").load("loadAdminPanel.action?adminPanelName=" + selectedPanel + "&selectOrgTypeId="+<s:property value="orgTypeId"/>);
    }

</script>

<div id="userDetailMainPanel" class="adminTabCss"></div>

<div id="userDetailTab" class="x-hide-display">
    
    <div class="subAdminTabCss">

        <form id="formUpdateUserDetail" action="user/updateUserDetail.action" class="XXentity-form" onsubmit="return true;" method="post">
            
            <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
            <input type="hidden" name="orgTypeId" id="orgTypeId" value='<s:property value="orgTypeId"/>'>
            <input type="hidden" name="h_isClaimHandler" id="h_isClaimHandler" value='<s:property value="claimHandler"/>'>
            
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
                                      list="insurers" listKey="id" listValue="name"
                                      headerKey="" headerValue="--- ALL ---" emptyOption="false">
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

                <div class="chox-form-item">
                    <label class="chox-form-std-label">First Name<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDFirstName" name="firstName" value="<s:property value="firstName" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Last Name<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDLastName" name="lastName" value="<s:property value="lastName" />"/>
                </div>
                
                <s:if test="mode=='New'">
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Password<span class="mandatory">*</span></label>
                        <input type="password" class="chox-ttxt" id="password" name="password" size="20" maxlength="20"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Re-enter Password<span class="mandatory">*</span></label>
                        <input type="password" class="chox-ttxt" name="confirmNewPassword" id="confirmNewPassword" size="20" maxlength="20"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">&nbsp;</label>
                        <span class="column_remark">N.B. Passwords are case sensitive. Must be at least 6 characters.<br/>
                        Must contain at least one lower case letter, one upper case letter, and one number. </span>
                    </div>
                </s:if>
                
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Active</label>
                    <s:checkbox name="status" value="status" />
                </div>
                <div class="chox-form-button">
                    <input type="button" value="Save Changes" onclick="javascript: return doSubmit();"/>
                    <input type="button" value="Cancel" class="cancel" onclick="javascript: return doCancelBack();" />
                </div>
                <div class="chox-form-submit-result" id="chox-form-submit-result"></div>
            </div>
        </form>
        <div id="CDmessageBox" class="submit-error"></div>
</div>
</div>

<div id="userPasswordTab" class="x-hide-display">
    <div class="subAdminTabCss">
    <s:if test="mode=='Edit'">

            <div class="status-info">
                N.B. Passwords are case sensitive. Must be at least 6 characters.<br/>
                Must contain at least one lower case letter, one upper case letter, and one number. 
            </div>    
            <div style="padding-top:20px;">
            <form id="formUpdatePassword" action="user/updateUserPassword.action" class="XXentity-form" onsubmit="return true;" method="post">
                <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
                <input type="hidden" name="orgTypeId" id="orgTypeId" value='<s:property value="orgTypeId"/>'>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Password<span class="mandatory">*</span></label>
                    <input type="password" class="chox-ttxt" id="password" name="password" size="20" maxlength="20"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Re-enter Password<span class="mandatory">*</span></label>
                    <input type="password" class="chox-ttxt" name="confirmNewPassword" id="confirmNewPassword" size="20" maxlength="20"/>
                </div>
                <div class="chox-form-button">
                    <input type="button" id="changePassword" value="Save Password" onclick="javascript:return doSubmitNewPassword();" />
                </div>
                <div id="chox-form-password-submit-result" class="action_msg"></div>
            </form>
            </div> 
            <div id="CDPswMessageBox" style="text-align:center" class="errorBox"></div>
    </s:if>
    </div>
</div>

<div id="userRoleTab" class="x-hide-display">
    <div class="subAdminTabCss">
    <s:if test="mode=='Edit'">
        <div>
            <s:action name="getUserroleMapping" executeResult="true">
                <s:param name="webUserId"><s:property value="id" /></s:param>
                <s:param name="orgTypeId"><s:property value="orgTypeId" /></s:param>
            </s:action>
        </div>
    </s:if>
    </div>
</div>

<div id="userWorkgroupTab" class="x-hide-display">
    <div class="subAdminTabCss">
    <s:if test="mode=='Edit'">
        <div>
            <s:action name="getUserWorkgroupMapping" executeResult="true">
                <s:param name="webUserId"><s:property value="id" /></s:param>
                <s:param name="insurerId"><s:property value="insurer.id" /></s:param>
            </s:action>
        </div>
    </s:if>
    </div>
</div>