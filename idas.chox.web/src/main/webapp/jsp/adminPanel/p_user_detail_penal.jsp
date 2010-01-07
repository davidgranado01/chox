<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var userDetailTabIndex = 0;
    var userDetailPanelTabs;
    var isNew = true;
    var isWorkgroupEnabled = true;

    $(function(){
        
        // CHECK PROCESS MODE
        isNew = isTrue($("#isNew").val());
        isWorkgroupEnabled = isTrue($("#isWorkgroupEnabled").val())

        // ADD REGULAR EXPRESSION FOR FORM VALIDATION
        $.validator.addMethod("regex", function(value, element, regexp) {
            var check = false;
            var re = new RegExp(regexp);
            return this.optional(element) || re.test(value);
        }, "Please check your input.");

        // USER DETAIL FORM VALIDATION
        var form = $("form#formUpdateUserDetail");
        form.validate(
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
            }
        });

        // USER PASSWORD FORM VALIDATION
        var userPasswordform = $("form#formUpdatePassword");
        userPasswordform.validate(
        {
            errorLabelContainer: "#CDPswMessageBox",
            rules: {
                password:{required:true, regex: "^.*(?=.{6,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$"},
                confirmNewPassword:{equalTo: "#password"}
            },
            messages: {
                password:{required:"You must supply a value for 'Password'", regex: "Incorrect Password Format"},
                confirmNewPassword:{equalTo: "Your passwords do not match"}
            }
        });
        ui.ajaxForm(userPasswordform);

        getUserDetailTabIndex();
        
        // GENERATE TAB
        userDetailPanelTabs = new Ext.TabPanel({
            renderTo: 'userDetailMainPanel',
            activeTab: userDetailTabIndex,
            height:610,
            width:730,
            border:true,
            items:[
                {contentEl:'userDetailTab', title:'User Detail', listeners: {activate: handleActivate}},
                {contentEl:'userPasswordTab', id:'userPasswordTabId', disabled:isNew, title:'Change Password', listeners: {activate: handleActivate}},
                {contentEl:'userRoleTab', id:'userRoleTabId', disabled:isNew, title:'User Roles', listeners: {activate: handleActivate}},
                {contentEl:'userWorkgroupTab', title:'Workgroups', disabled:(isNew || !isWorkgroupEnabled), listeners: {activate: handleActivate}}
            ]
        });
    });
    
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
        ajax.loadHtml(url,param,function(data){
            $(target).html(data);
        });
    }

    function doUserDetailSubmit(){        
        ui.ajaxForm($("form#formUpdateUserDetail"), doSubmitUserSucceed);
    }

    function doSubmitUserSucceed(responseText, statusText){
        var response = eval('(' + responseText.trim() + ')');
        if(response && response.isValid)
        {
            if(response.resultType && response.resultType == 'New'){
                var newObjectId = parseInt(response.result);
                var target = "#admin_param_panel";
                var url = "<%= request.getContextPath()%>/prv/p/updateUserDetailPanel.action";
                var param = {"objectId":newObjectId,"organisationTypeId":SelectedOrganisationTypeId};
                ajax.loadHtml(url,param,function(data){$(target).html(data);});
            }
        }
    }

</script>
<input name="tabIndex" id="tabIndex" type="hidden" value="<s:property value="tabIndex" />">
<input name="isNew" id="isNew" type="hidden" value="<s:property value="isNew" />">
<input name="isWorkgroupEnabled" id="isWorkgroupEnabled" type="hidden" value="<s:property value="isWorkgroupEnabled" />">
<input name="CurrentUserOrganisationId" id="CurrentUserOrganisationId" type="hidden" value="<s:property value="CurrentUserOrganisationId" />">

<div id="chox-admin-holder">

    <div id="chox-admin-col-div">
        <div id="header-title"><label>User Name:
                <s:if test="!isNew"><s:property value="displayName" /> (<s:property value="email" />)</s:if><s:else>Create New User</s:else>
            </label>
        </div>
        <div id="userDetailMainPanel"/>
    </div>

    <div id="userDetailTab" class="x-hide-display">
        <div class="sub-admin-tab-css">

            <form id="formUpdateUserDetail" action="<%= request.getContextPath()%>/prv/p/updateUserDetail.action" class="XXentity-form" onsubmit="return true;" method="post">
                <input name="organisationTypeId" id="organisationTypeId" type="hidden" value="<s:property value="organisationTypeId" />">
                <input type="hidden" name="objectId" id="objectId" value='<s:property value="objectId"/>'>
                <div class="form-container">
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Organisation Type</label>
                        <b>
                            <s:if test="OrganisationTypeId==1">Sherwood Organisation Users</s:if>
                            <s:elseif test="OrganisationTypeId==2">Insurer Organisation Users</s:elseif>
                            <s:elseif test="OrganisationTypeId==3">Credit Hire Organisation Users</s:elseif>
                        </b>
                    </div>

                    <s:if test="!isNew">

                        <!-- EDIT MODE !-->

                        <div class="chox-form-item">
                            <label class="chox-form-std-label">Company Name</label>
                            <b>
                                <s:if test="OrganisationTypeId==1">Sherwood</s:if>
                                <s:elseif test="OrganisationTypeId==2"><s:property value="insurer.name" /></s:elseif>
                                <s:elseif test="OrganisationTypeId==3"><s:property value="chorganisation.name" /></s:elseif>
                            </b>
                        </div>

                    </s:if>
                    <s:else>

                        <!-- ADD NEW MODE !-->

                        <s:if test="OrganisationTypeId==2">

                            <s:if test="CurrentUserOrganisationId=1">

                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Insurer Company<span class="mandatory">*</span></label>
                                    <s:select
                                        id="insurerId"
                                        name="insurerId"
                                        list="insurers"
                                        listKey="id"
                                        listValue="name"
                                        headerKey=""
                                        headerValue="- Please Select -"
                                        emptyOption="false">
                                    </s:select>
                                </div>

                            </s:if>
                            <s:else>
                                <input name="insurerId" id="insurerId" type="hidden" value="<s:property value="CurrentUserOrganisationId" />">
                            </s:else>

                        </s:if>

                        <s:if test="OrganisationTypeId==3">

                            <s:if test="CurrentUserOrganisationId=1">

                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Credit Hire Company<span class="mandatory">*</span></label>
                                    <s:select
                                        id="supplierId"
                                        name="supplierId"
                                        list="suppliers"
                                        listKey="id"
                                        listValue="name"
                                        headerKey=""
                                        headerValue="- Please Select -"
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
                        <label class="chox-form-std-label">User Name<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDUserName" name="userName" value="<s:property value="userName" />"/>
                    </div>

                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Email<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDEmail" name="email" value="<s:property value="email" />"/>
                    </div>

                    <div class="chox-form-item">
                        <label class="chox-form-std-label">First Name<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDFirstName" name="firstName" value="<s:property value="firstName" />"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Last Name<span class="mandatory">*</span></label>
                        <input type="text" class="chox-ttxt" id="CCDLastName" name="lastName" value="<s:property value="lastName" />"/>
                    </div>
                    <s:if test="isNew">
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
                        <label class="chox-form-std-label">Active</label><s:checkbox name="status" value="status" />
                    </div>
                    <div class="chox-form-button">
                        <input type="submit" value="Save Changes" onclick="javascript: return doUserDetailSubmit();"/>
                        <input type="button" value="Cancel" class="cancel" onclick="javascript: return doUserCancelBack();" />
                    </div>
                    <div class="chox-form-submit-result"></div>
                    <div id="CDmessageBox" class="action-error-msg"></div>
                </div>
            </form>
        </div>
    </div>

    <div id="userPasswordTab" class="x-hide-display">
        <div class="sub-admin-tab-css">
            <s:if test="!isNew">

                <div class="status-info">
                    N.B. Passwords are case sensitive. Must be at least 6 characters.<br/>
                    Must contain at least one lower case letter, one upper case letter, and one number.
                </div>
                <div style="padding-top:20px;">
                    <form id="formUpdatePassword" action="<%= request.getContextPath()%>/prv/p/updateUserPassword.action" class="XXentity-form" onsubmit="return true;" method="post">
                        <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
                        <input name="organisationTypeId" id="organisationTypeId" type="hidden" value="<s:property value="organisationTypeId" />">
                        <div class="form-container">
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Password<span class="mandatory">*</span></label>
                                <input type="password" class="chox-ttxt" id="password" name="password" size="20" maxlength="20"/>
                            </div>
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Re-enter Password<span class="mandatory">*</span></label>
                                <input type="password" class="chox-ttxt" name="confirmNewPassword" id="confirmNewPassword" size="20" maxlength="20"/>
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

    <div id="userRoleTab" class="x-hide-display">
        <s:if test="!isNew">
            <s:action name="getUserroleMapping" executeResult="true">
                <s:param name="webUserId"><s:property value="id" /></s:param>
                <s:param name="organisationTypeId"><s:property value="organisationTypeId" /></s:param>
            </s:action>
        </s:if>
    </div>

    <div id="userWorkgroupTab" class="x-hide-display">
        <s:if test="!isNew && isWorkgroupEnabled">
            <s:action name="getUserWorkgroupMapping" executeResult="true">
                <s:param name="webUserId"><s:property value="id" /></s:param>
                <s:param name="organisationTypeId"><s:property value="organisationTypeId" /></s:param>
            </s:action>
        </s:if>
    </div>

</div>