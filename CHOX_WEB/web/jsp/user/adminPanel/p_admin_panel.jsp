<%@ taglib uri="/struts-tags" prefix="s" %>

<head>        
    <title></title>
    <script type="text/javascript" src="<%= request.getContextPath()%>/scripts/jquery/jquery.validate.min.js"></script>
    <script type="text/javascript">
        
        function renderAdminParameterPanel(selectedPanel)
        {
            var sLocaltion = "#admin_param_panel";
            var sAction = "loadAdminPanel.action";
            var sparameters = "adminPanelName=" + selectedPanel;
            doSectionLoad(sLocaltion, sAction, sparameters);
        }
        
    </script>
</head>

<div class="x-panel-bwrap chox-form-container" id="xPenalMainRight">
    <table cellpadding="0" cellspacing="0" width="100%" style="height:680px;">
        <tr valign="top">
            <td class="chox-admin-form-left-col">
                <fieldset class="x-fieldset" style="height:660px;">
                    <legend>Admin Panel</legend>
                    <div class="x-panel-bwrap chox-form-container" >
                            <ul>
                            <s:if test="adminAccessibility.isInsurerCompaniesAdminAccessibility">
                                <li><a href="javascript:renderAdminParameterPanel('ChoxPanelMgmt');">Insurance Companies</a></li>
                            </s:if>

                            <s:if test="adminAccessibility.isCreditHireOrgAdminAccessibility">
                                <li><a href="javascript:renderAdminParameterPanel('CreditHireOrgMgmt');">Credit Hire Organisations</a></li>
                            </s:if>

                            <s:if test="adminAccessibility.isInsurerBreManagementAdminAccessibility">
                                <li><a href="javascript:renderAdminParameterPanel('InsurerPanelMgmt');">CHOX Administration</a></li>
                            </s:if>

                            <s:if test="adminAccessibility.isUserManagementAdminAccessibility">
                                <li><a href="javascript:renderAdminParameterPanel('UserMgmt');">User Management</a></li>
                            </s:if>
                            </ul>
                    </div>
                </fieldset>
            </td>
            <td>
                <div id="admin_param_panel"></div>
            </td>
        </tr>
    </table>
</div>

