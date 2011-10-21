<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var userDetailTabIndex = 0;
    var userDetailPanelTabs;
    var isNew = true;
    var isWorkgroupEnabled = true;
    var SelectedOrganisationTypeId = '<s:property value="organisationTypeId" />';

    Ext.onReady(function(){

        new Ext.ToolTip({ target: 'help-userName', html: 'Only allowed to enter alphanumeric characters and the following special characters: “.”, “@”, “_”, “-”. Spacing between characters is not allowed.'});

        // CHECK PROCESS MODE
        isNew = isTrue($("#isNew").val());
        isWorkgroupEnabled = isTrue($("#isWorkgroupEnabled").val())

        $.validator.addMethod("regex", function(value, element, regexp) {
            var check = false;
            var re = new RegExp(regexp);
            return this.optional(element) || re.test(value);
        }, "Please check your input.");

        // USER DETAIL FORM VALIDATION
        var form = $("form#formUpdateUserDetail");
        var passwordRegex = "^.*(?=.{" + '<s:property value="minPasswordLength" />' + ",})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$";
        form.validate(
        {
            errorLabelContainer: "#CDmessageBox",
            rules: {
                userName:{required:true, regex: "^[a-zA-Z0-9._@-]*$"},
                email:{required:true, email: true},
                firstName:{required:true},
                lastName:{required:true},
                insurerId:{required:true},
                supplierId:{required:true},
                password:{required:true, regex: passwordRegex},
                confirmNewPassword:{equalTo: "#password"}
            },
            messages: {
                userName:{required:"You must supply a value for 'User Name'", regex: "Incorrect User Name Format"},
                email:{required:"You must supply a value for 'Email'", email: "Incorrect email format"},
                firstName:{required:"You must supply a value for 'First Name'"},
                lastName:{required:"You must supply a value for 'Last Name'"},
                insurerId:{required:"Please select 'Insurer Company'"},
                supplierId:{required:"Please select 'Credit Hire Organisation'"},
                password:{required:"You must supply a value for 'Password'", regex: "Incorrect Password Format"},
                confirmNewPassword:{equalTo: "Your passwords do not match"}
            }
        });

        ui.ajaxForm($("form#formUpdateUserDetail"), function(responseText, statusText){

            var response = eval('(' + responseText.trim() + ')');

            if(response && response.isValid)
            {

                if(response.resultType && response.resultType == 'New'){

//                     alert("New User has been created");
                    Ext.MessageBox.alert('Status', 'New User has been created', function() {
                            var newObjectId = parseInt(response.result);
                            var target = "#admin_param_panel";
                            var url = "<%= request.getContextPath()%>/prv/p/updateUserDetailPanel.action";
                            var param = {"objectId":newObjectId,"organisationTypeId":SelectedOrganisationTypeId};
                            ajax.loadHtml2(url,param,function(data){
                                    $(target).html(data);
                            });
                    });

                }
                else {
                    Ext.MessageBox.alert('Status', 'User "' + '<s:property value="userName" />' + '"has been updated', function() {
                            var target = "#admin_param_panel";
                            var url = "<%= request.getContextPath()%>/prv/p/updateUserDetailPanel.action";
                            var param = {"objectId":<s:property value="objectId"/>,"organisationTypeId":SelectedOrganisationTypeId};
                            ajax.loadHtml2(url,param,function(data){
                                    $(target).html(data);
                            });
                    });
                }
            }
            else {
                Ext.MessageBox.alert('Error', 'Error updating user: '+ response.errors + '\nPlease try again.', function() {
                            var target = "#admin_param_panel";
                            var url = "<%= request.getContextPath()%>/prv/p/updateUserDetailPanel.action";
                            var param = {"objectId":<s:property value="objectId"/>,"organisationTypeId":SelectedOrganisationTypeId};
                            ajax.loadHtml2(url,param,function(data){
                                    $(target).html(data);
                            });
                });
            }
        });

        // USER PASSWORD FORM VALIDATION
        var userPasswordform = $("form#formUpdatePassword");
        userPasswordform.validate(
        {
            errorLabelContainer: "#CDPswMessageBox",
            rules: {
                password:{required:true, regex: passwordRegex},
                confirmNewPassword:{equalTo: "#password"}
            },
            messages: {
                password:{required:"You must supply a value for 'Password'", regex: "Incorrect Password Format"},
                confirmNewPassword:{equalTo: "Your passwords do not match"}
            }
        });

        ui.ajaxForm(userPasswordform, function(responseText, statusText){

            var response = eval('(' + responseText.trim() + ')');

            if(response && response.isValid)
            {
                Ext.MessageBox.alert('Status', 'The password for user "' + '<s:property value="userName" />' + '" has been changed.', confirmOk);
            }
        });

        getUserDetailTabIndex();

        // GENERATE TAB
        userDetailPanelTabs = new Ext.TabPanel({
            renderTo: 'userDetailMainPanel',
            activeTab: userDetailTabIndex,
            height:615,
            width:775,
            border:true,
            items:[
                {contentEl:'userDetailTab', title:'User Detail', listeners: {activate: handleActivate}},
                {contentEl:'userPasswordTab', id:'userPasswordTabId', disabled:isNew, title:'Change Password', listeners: {activate: handleActivate}},
                {contentEl:'userRoleTab', id:'userRoleTabId', disabled:isNew, title:'User Roles', listeners: {activate: handleActivate}, autoLoad: {url:"p/getUserroleMapping.action?webUserId="+'<s:property value="id" />'+"&organisationTypeId="+'<s:property value="organisationTypeId" />'+"&rdn="+getRandomNumber(), scripts:true}},
                {contentEl:'userWorkgroupTab', title:'Workgroups', disabled:(isNew || !isWorkgroupEnabled), listeners: {activate: handleActivate}, autoLoad: {url:"p/getUserWorkgroupMapping.action?webUserId="+'<s:property value="id" />'+"&organisationTypeId="+'<s:property value="organisationTypeId" />'+"&rdn="+getRandomNumber(), scripts:true}}
                
            ]
        });
    });

    function confirmOk(btn){
        $("#password").val("");
        $("#confirmNewPassword").val("");
    }

    function getUserDetailTabIndex(){
        if($("#tabIndex").val()!=null && $("#tabIndex").val()!=''){
            userDetailTabIndex = $("#tabIndex").val();
        }
    }

    function handleActivate(tab){
        userDetailTabIndex = 0;
        if(userDetailPanelTabs) { userDetailTabIndex = userDetailPanelTabs.items.indexOf(userDetailPanelTabs.getActiveTab()); }
    }

    function doUserCancelBack(){
        var target = "#admin_param_panel";
        var url = "<%= request.getContextPath()%>/prv/p/loadAdminPanel.action";
        var param = {"adminPanelName":"UserMgmt"};
        ajax.loadHtml2(url, param, function(data){
            $(target).html(data);
        });
    }

</script>
<input name="tabIndex" id="tabIndex" type="hidden" value="<s:property value="tabIndex" />"/>
<input name="isNew" id="isNew" type="hidden" value="<s:property value="isNew" />"/>
<input name="isWorkgroupEnabled" id="isWorkgroupEnabled" type="hidden" value="<s:property value="isWorkgroupEnabled" />"/>
<input name="CurrentUserOrganisationId" id="CurrentUserOrganisationId" type="hidden" value="<s:property value="CurrentUserOrganisationId" />"/>

<div id="chox-admin-holder">

    <div id="chox-admin-col-div" style="width:780px" >
        <div id="header-title"><label>User Name:
                <s:if test="!isNew"><s:property value="userName" /> (<s:property value="fullName" />)</s:if><s:else>Create New User</s:else>
            </label>
        </div>
        <div id="userDetailMainPanel"></div>

    <div id="userDetailTab" class="x-hide-display">
        <div class="sub-admin-tab-css">

            <form autocomplete="off" id="formUpdateUserDetail" name="formUpdateUserDetail" action="<%= request.getContextPath()%>/prv/p/updateUserDetail.action" onsubmit="return true;" class="XXentity-form" method="post">
                <input name="organisationTypeId" id="organisationTypeId" type="hidden" value="<s:property value="organisationTypeId" />">
                <input type="hidden" name="objectId" id="objectId" value='<s:property value="objectId"/>'>
                <div class="form-container">
                    <div class="chox-form-item">
                        <label class="chox-form-std-label" style="width: 260px;">Organisation Type</label>
                        <b>
                            <s:if test="OrganisationTypeId==1">Sherwood Organisation Users</s:if>
                            <s:elseif test="OrganisationTypeId==2">Insurer Organisation Users</s:elseif>
                            <s:elseif test="OrganisationTypeId==3">Credit Hire Organisation Users</s:elseif>
                        </b>
                    </div>

                    <s:if test="!isNew">

                        <!-- EDIT MODE !-->

                        <div class="chox-form-item">
                            <label class="chox-form-std-label" style="width: 260px;">Company Name</label>
                            <b>
                                <s:if test="OrganisationTypeId==1">Sherwood</s:if>
                                <s:elseif test="OrganisationTypeId==2"><s:property value="Insurer.name" /></s:elseif>
                                <s:elseif test="OrganisationTypeId==3"><s:property value="Chorganisation.name" /></s:elseif>
                            </b>
                        </div>

                    </s:if>
                    <s:else>

                        <!-- ADD NEW MODE !-->

                        <s:if test="OrganisationTypeId==2">

                            <s:if test="CurrentUserOrganisationId==1">

                                <div class="chox-form-item">
                                    <label class="chox-form-std-label" style="width: 260px;">Insurer Company<span class="mandatory">*</span></label>
                                    <s:select
                                        id="insurerId"
                                        name="insurerId"
                                        list="insurers"
                                        listKey="id"
                                        listValue="name"
                                        headerKey=""
                                        headerValue="-- Please Select --"
                                        emptyOption="false">
                                    </s:select>
                                </div>

                            </s:if>
                            <s:else>
                                <input name="insurerId" id="insurerId" type="hidden" value="<s:property value="CurrentUserOrganisationId" />">
                            </s:else>

                        </s:if>

                        <s:if test="OrganisationTypeId==3">

                            <s:if test="CurrentUserOrganisationId==1">

                                <div class="chox-form-item">
                                    <label class="chox-form-std-label" style="width: 260px;">Credit Hire Company<span class="mandatory">*</span></label>
                                    <s:select
                                        id="supplierId"
                                        name="supplierId"
                                        list="suppliers"
                                        listKey="id"
                                        listValue="name"
                                        headerKey=""
                                        headerValue="-- Please Select --"
                                        emptyOption="false">
                                    </s:select>
                                </div>
                            </s:if>
                            <s:else>
                                <input name="supplierId" id="supplierId" type="hidden" value="<s:property value="CurrentUserOrganisationId" />">
                            </s:else>

                        </s:if>

                    </s:else>

                    <div class="chox-form-item">
                        <label class="chox-form-std-label" style="width: 260px;">User Name<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" style="width: 200px;" id="CCDUserName" name="userName" value="<s:property value="userName" />"/><img id="help-userName" class="help-icon" src="<%= request.getContextPath()%>/images/help.png"/>
                    </div>

                    <div class="chox-form-item">
                        <label class="chox-form-std-label" style="width: 260px;">Email<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" style="width: 200px;" id="CCDEmail" name="email" value="<s:property value="email" />"/>
                    </div>

                    <div class="chox-form-item">
                        <label class="chox-form-std-label" style="width: 260px;">First Name<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" style="width: 200px;" id="CCDFirstName" name="firstName" value="<s:property value="firstName" />"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label" style="width: 260px;">Last Name<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" style="width: 200px;" id="CCDLastName" name="lastName" value="<s:property value="lastName" />"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label" style="width: 260px;">Contact Telephone</label>
                        <input type="text" class="chox-ttxt" style="width: 200px;" id="CCDTelephone" name="telephone" value="<s:property value="telephone" />"/>
                    </div>
                    <s:if test="isNew">
                        <div class="chox-form-item">
                            <label class="chox-form-std-label" style="width: 260px;">Password<span class="mandatory">*</span></label>
                            <input type="password" class="chox-ttxt" style="width: 200px;" id="password" name="password" size="20" maxlength="20"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label" style="width: 260px;">Re-enter Password<span class="mandatory">*</span></label>
                            <input type="password" class="chox-ttxt" style="width: 200px;" name="confirmNewPassword" id="confirmNewPassword" size="20" maxlength="20"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label" style="width: 260px;">&nbsp;</label>
                            <span class="column-remark">N.B. Passwords are case sensitive. Must be at least <s:property value="minPasswordLength" /> characters.<br/>
                                Must contain at least one lower case letter, one upper case letter, and one number. </span>
                        </div>
                    </s:if>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label" style="width: 260px;">Active</label><s:checkbox name="status" value="status" />
                    </div>
                    <div class="chox-form-button">
                        <input type="submit" value="Save Changes"/>
                        <input type="button" value="Cancel" class="cancel" onclick="javascript: return doUserCancelBack();" />
                    </div>
                    <div class="chox-form-submit-result"></div>
                    <div id="CDmessageBox" class="action-error-msg"></div>
                </div>
               <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
            </form>
        </div>
    </div>

    <div id="userPasswordTab" class="x-hide-display">
        <div class="sub-admin-tab-css">
            <s:if test="!isNew">

                <div class="status-info">
                    N.B. Passwords are case sensitive. Must be at least <s:property value="minPasswordLength" /> characters.<br/>
                    Must contain at least one lower case letter, one upper case letter, and one number.
                </div>
                <div style="padding-top:20px;">
                    <form autocomplete="off" id="formUpdatePassword" action="<%= request.getContextPath()%>/prv/p/updateUserPassword.action" class="XXentity-form" onsubmit="return true;" method="post">
                        <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
                        <input name="organisationTypeId" id="organisationTypeId" type="hidden" value="<s:property value="organisationTypeId" />">
                       <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
                        <div class="form-container">
                            <div class="chox-form-item">
                                <label class="chox-form-std-label" style="width: 260px;">Password<span class="mandatory">*</span></label>
                                <input type="password" class="chox-ttxt" style="width: 200px;" id="password" name="password" size="20" maxlength="20"/>
                            </div>
                            <div class="chox-form-item">
                                <label class="chox-form-std-label" style="width: 260px;">Re-enter Password<span class="mandatory">*</span></label>
                                <input type="password" class="chox-ttxt" style="width: 200px;" name="confirmNewPassword" id="confirmNewPassword" size="20" maxlength="20"/>
                            </div>
                            <div class="chox-form-button">
                                <input type="submit" value="Save Password"/>
                            </div>
                            <div class="chox-form-submit-result"></div>
                            <div id="CDPswMessageBox" class="action-error-msg"></div>
                        </div>
                    </form>
                </div>

            </s:if>
        </div>
    </div>

    <div id="userRoleTab" class="x-hide-display"></div>

    <div id="userWorkgroupTab" class="x-hide-display"></div>

</div>
</div>