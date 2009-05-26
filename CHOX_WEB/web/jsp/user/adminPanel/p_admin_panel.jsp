<%@ taglib uri="/struts-tags" prefix="s" %>

<head>        
    
    <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.validate.min.js"></script> 
    <script src="<%= request.getContextPath()%>/scripts/general.js" type="text/javascript"></script> 
    
    <script>
        
        function renderAdminParameterPanel(selectedPanel)
        {
            $("#admin_param_panel").load("loadAdminPanel.action?adminPanelName=" + selectedPanel);
        }
        
    </script>
</head>

<div class="x-panel-bwrap chox-form-container" id="xPenalMainRight">
    
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr valign="top">
            <td class="chox-admin-form-left-col">
                <fieldset class="x-fieldset">
                    <legend>Admin Panel</legend>                    
                    
                        <div style="height:580px;" class="x-panel-bwrap chox-form-container">
                            <ul>

<s:if test="adminAccessibility.isInsurerCompaniesAdminAccessibility">
                                <li><a href="javascript:renderAdminParameterPanel('InsurerOrgMgmt');">Insurance Companies</a></li>
</s:if>
<s:if test="adminAccessibility.isCreditHireOrgAdminAccessibility">                            
                                <li><a href="javascript:renderAdminParameterPanel('CreditHireOrgMgmt');">Credit Hire Organisations</a></li>
</s:if> 
<s:if test="adminAccessibility.isUserManagementAdminAccessibility">         
<li><a href="javascript:renderAdminParameterPanel('UserMgmt');">User Management</a></li>
</s:if>
                            </ul>
                        
                    </div>
                </fieldset>
            </td>
            <td width="70%" class="chox-form-right-col">
                <div id="admin_param_panel"></div>
            </td>
        </tr>
    </table>
    
    
</div>

