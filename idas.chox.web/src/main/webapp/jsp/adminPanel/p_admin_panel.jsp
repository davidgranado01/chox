<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    function renderAdminParameterPanel(selectedPanel)
    {
        var target = "#admin_param_panel";
        var url = "<%= request.getContextPath()%>/prv/p/loadAdminPanel.action";
        var param = {"adminPanelName":selectedPanel};
        ajax.loadHtml(url,param,function(data){
            $(target).html(data);
        });
    }

</script>

<div class="x-panel-bwrap chox-form-container">
    <table class="chox-admin">
        <tr>
            <td class="chox-admin-left-col">
                <div id="chox-admin-col-div">
                    <div id="header-title"><label>Admin Panel</label></div>
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
            </td>
            <td id="admin_param_panel" class="chox-admin-right-col"></td>
        </tr>
    </table>
</div>

