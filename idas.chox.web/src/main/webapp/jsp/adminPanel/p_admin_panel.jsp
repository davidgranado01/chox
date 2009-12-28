<%@ taglib uri="/struts-tags" prefix="s" %>


<script type="text/javascript">

    function renderAdminParameterPanel(selectedPanel)
    {
        var sLocaltion = "#admin_param_panel";
        var sAction = "<%= request.getContextPath()%>/prv/p/loadAdminPanel.action";
        var sparameters = "adminPanelName=" + selectedPanel;
        doSectionLoad(sLocaltion, sAction, sparameters);
    }

</script>

<div class="x-panel-bwrap chox-form-container">
    <table class="chox-admin">
        <tr>
            <td class="chox-admin-left-col">
                <fieldset class="x-fieldset">
                    <legend>Admin Panel</legend>
                    <div>
                            <ul class='admin-header-list'>
                            <s:if test="adminAccessibility.isInsurerCompaniesAdminAccessibility">
                                <li><a href="javascript:renderAdminParameterPanel('ChoxInsurerMgmtPanel');">Insurance Companies</a></li>
                            </s:if>

                            <s:if test="adminAccessibility.isCreditHireOrgAdminAccessibility">
                                <li><a href="javascript:renderAdminParameterPanel('ChoxCreditHireMgmtPanel');">Credit Hire Organisations</a></li>
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
            <td id="admin_param_panel" class="chox-admin-right-col"></td>
        </tr>
    </table>
</div>

