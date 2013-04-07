<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
    var idIncrementer = 1;
    function doAddId(element){
        $(element).attr('id','inbox_queue-'+idIncrementer)
        Ext.state.Manager.set("recentlyClickedInboxQueueId", 'inbox_queue-'+idIncrementer);
        idIncrementer = idIncrementer+1;
    }
</script>
<div id="filterPanel">
    <ul class="inbox">
        <s:iterator value="filterViewDatas">
            <li><a onclick="return doAddId(this);" href="javascript:updateFilter('<s:property value="key" />','<s:property value="gridTitle" />');" ><s:property value="description" /></a></li> 
        </s:iterator>
    <li><span height="10px">&nbsp;</span></li>
    </ul>
</div>