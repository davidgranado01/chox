<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

    <head>
        <s:if test="#parameters.devp || getText('development')">
            <%@ include file="/includes/scripts.jsp"%>
        </s:if>
        <s:else>
            <%@ include file="/includes/scripts-min.jsp"%>
        </s:else>

        <script type="text/javascript">
            var generalChoxVersion = '<s:property value="getText('version.number')" />';
        </script>
    </head>

<div id="userDetailsScreenId" class="chox-claim-header x-panel-bwrap chox-form-container">
    <s:if test="isShowMessage">
        <div class="status-info"><s:property value="message" /></div>
    </s:if>

    <fieldset class="x-fieldset">
        <legend>User Details</legend>
        <div class="form-container">
            <div class="chox-form-item">
                <label class="chox-form-std-label">First Name</label>
                <label class="std-data-ro"><s:property value="webUser.firstName" /></label></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Last Name</label>
                <label class="std-data-ro"><s:property value="webUser.lastName" /></label></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Email Address</label>
                <label class="std-data-ro"><s:property value="webUser.email" /></label></div>
            <s:if test="webUser.isCHOXAdmin()">
                <div class="chox-form-item">
                <label class="chox-form-std-label">Organisation</label>
                <label class="std-data-ro">Sherwood</label></div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                <label class="chox-form-std-label">Organisation</label>
                <label class="std-data-ro"><s:property value="webUser.organisationName" /></label></div>
            </s:else>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Contact Telephone No.</label>
                <label class="std-data-ro" id="contactTelephoneId" ><s:property value="webUser.telephone" /></label></div>
        </div>
    </fieldset>
                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                    <tr valign="top">
                        <td>
                             <fieldset class="x-fieldset">
                                <legend>Change Password</legend>
                                <div id="updatePasswordId">
                                    <s:action name="getUserChangePassword" namespace="/prv/p" executeResult="true">
                                    </s:action>
                                </div>
                            </fieldset>
                        </td>
                    </tr>
                    <s:if test="!webUser.isExpired">
                      <tr valign="top">
                        <td>
                            <fieldset class="x-fieldset">
                                <legend>Update Contact Telephone Number</legend>
                                <div id="updateTelephoneNumberId">
                                    <s:action name="getUserChangeContact" namespace="/prv/p" executeResult="true">
                                        <s:param name="redirect" value="<s:property value='redirect' />" />
                                    </s:action>
                                </div>
                            </fieldset>
                        </td>
                      </tr>
                    </s:if>
                </table>
</div>