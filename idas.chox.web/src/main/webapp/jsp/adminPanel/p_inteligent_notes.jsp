<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

var iNotesProxy = new Ext.data.HttpProxy({
    url: '<%= request.getContextPath()%>/prv/p/getInsurersReasonsOfRejection.action',
    method: 'post'
});

rorGridViewDataStore = new Ext.data.Store({
    proxy: iNotesProxy,
    reader: iNotesJsonReader
});

var iNotesJsonReader = new Ext.data.JsonReader({
    totalProperty: 'totalCount',
    root: 'results',
    fields:
        [
         {name:'id'},
         {name:'name'},
         {name:'status'}
         ]
});

new Ext.grid.GridPanel({
    listeners:  {cellclick:editReasonOfRejection},
    store: rorGridViewDataStore,
    renderTo:'rorGridViewPanel',
    enableHdMenu:false,
    layout:'fit',
    viewConfig:{forceFit:true},
    columns: [
        {header: "Name", width: 160, dataIndex: 'name', sortable: true, resizable: true, renderer:function(value,p,r){
                return "<a href='#' class='high-light-item'>"+value+"</a>" }},
        {header: "Active", width: 80, dataIndex: 'status', sortable: true, resizable: true, 
            renderer: booleanLink}
    ],
    height:243,
    width: 760
});

</script>
<div class="sub-admin-tab-css">
    <div class="status-info">
        To be confirmed!!!
    </div>

    <div id="inteligentNotesPageId">
       <div id="inteligentNotesPage"/>
    </div>

</div>