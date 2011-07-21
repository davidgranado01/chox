<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var filterName;
    var title;

    function reloadQueues() {
        var orgCombo = Ext.ComponentMgr.get('filterOrgId');
        var selectedOrg = -1;
        if (orgCombo) {
            selectedOrg = orgCombo.getValue();
        }

        if (!selectedOrg)
            selectedOrg = -1;
        refreshFilterPanelByOrg(filterName, title, selectedOrg);
    }
    
    function updateFilter(key, gridTitle) {
        var orgCombo = Ext.ComponentMgr.get('filterOrgId');
        var selectedOrg = -1;
        if (orgCombo) {
            selectedOrg = orgCombo.getValue();
        }

        if (!selectedOrg)
            selectedOrg = -1;
        filterName = key;
        title = gridTitle;
        return executeFilterByOrg(key, gridTitle, selectedOrg);
    }

</script>

<div id="filterPanel">
    <ul class="inbox">
        <s:iterator value="filterViewDatas">
            <li><a href="javascript:updateFilter('<s:property value="key" />','<s:property value="gridTitle" />');" ><s:property value="description" /></a></li> 
        </s:iterator>
    </ul>
    <li><span height="10px">&nbsp;</span></li>
</div>