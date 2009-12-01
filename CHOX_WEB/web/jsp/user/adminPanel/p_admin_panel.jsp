<%@ taglib uri="/struts-tags" prefix="s" %>

<head>        
    <title></title>
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.validate.min.js"></script> 
    <script src="<%= request.getContextPath()%>/scripts/general.js" type="text/javascript"></script> 
    <script type="text/javascript">
        
        function renderAdminParameterPanel(selectedPanel)
        {
            var sLocaltion = "#admin_param_panel";
            var sAction = "loadAdminPanel.action";
            var sparameters = "adminPanelName=" + selectedPanel;
            doSectionLoad(sLocaltion, sAction, sparameters);
            //$("#admin_param_panel").load("loadAdminPanel.action?adminPanelName=" + selectedPanel+uniqeToken());
        }
        
    </script>
</head>

<div class="x-panel-bwrap chox-form-container" id="xPenalMainRight">

    <table cellpadding="0" cellspacing="0" width="100%" style="height:680px;">
        <tr valign="top">
            <td class="chox-admin-form-left-col" width="200px">
                <fieldset class="x-fieldset" style="height:660px;">
                    <legend>Admin Panel</legend>
                    <div class="x-panel-bwrap chox-form-container" >
                            <ul>

<s:if test="adminAccessibility.isInsurerCompaniesAdminAccessibility">
    <li><a href="javascript:renderAdminParameterPanel('InsurerOrgMgmt');">Insurance Companies</a></li>
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
            <td class="chox-form-right-col" style="width:100%;">
                <div id="admin_param_panel"></div>
            </td>

        </tr>
    </table>

</div>

