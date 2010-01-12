<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var historyData;
    var historyJsonReader;
    var historyGrid;

    $(function(){
        
        historyJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'createdBy'},
                {name:'createdDate'},
                {name:'narrative'}
            ]
        });

        historyData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getHistories.action',method:'POST'}),
            reader:historyJsonReader
        });

        historyGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:loadHistory },
            store: historyData,
            renderTo:'historyGrid',
            enableHdMenu:false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Created On", width: 110, dataIndex: 'createdDate', sortable: false, resizable: true},
                {header: "Created By", width: 110, dataIndex: 'createdBy', sortable: false, resizable: true},
                {header: "Message Text", width: 650, dataIndex: 'narrative', sortable: false, resizable: true}
            ],
            autoWidth:true,
            height:300
        });

        historyData.load({params:{claimId : <s:property value="claimId" />}});
        
    });

    function loadHistory(grid, rowIndex, columnIndex, e){
        var historyItem = historyGrid.getStore().getAt(rowIndex);
        var title = "History";
        var msg = "<b>Created Date</b>: " + historyItem.get("createdDate")
            + "<br/><b>Created By</b>: " + historyItem.get("createdBy")
            + "<br/><br/><b>Message</b>: <br/>" + historyItem.get("narrative")
        propmtMsg(title, msg);
    }
    
</script>
<div class="claim-detail-tab">
    <div id="historyGrid"></div>
</div>