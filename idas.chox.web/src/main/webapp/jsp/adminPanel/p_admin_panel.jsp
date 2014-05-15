<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    function renderBillingPanel(billType)
    {
        var paramStr = {"billingType":billType};
        Ext.get("admin_param_panel").load(choxUpdateEl({
            url: "/prv/p/loadBillingPanel.action",
            params: paramStr,
            text: "Loading billing panel"
        }));
    }

    function renderAdminParameterPanel(selectedPanel)
    {
        var target = "#admin_param_panel";
        var url = "/prv/p/loadAdminPanel.action";
        var param = {"adminPanelName":selectedPanel};
        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
        });
    }
    
</script>

<div class="x-panel-bwrap chox-form-container">
    <div class="chox-admin">
        <div id="chox-admin-left-col">
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
                    <s:if test="adminAccessibility.isBillingAdminAccessibility">
                        <li><a href="javascript:renderBillingPanel('insurer');">Insurer Billing</a></li>
                    </s:if>
                    <s:if test="adminAccessibility.isBillingAdminAccessibility">
                        <li><a href="javascript:renderBillingPanel('cho');">CHO Billing</a></li>
                    </s:if>
                </ul>
            </div>
        </div>
        <div id="chox-admin-right-col">
            <div id="admin_param_panel" ></div>
        </div>
    </div>
</div>

