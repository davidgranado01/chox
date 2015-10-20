<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var historyData;
    var historyJsonReader;
    var historyGrid;

    Ext.onReady(function() {
        
        historyJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [{name:'createdBy'},
                {name:'createdDate', type: 'string', dateFormat:'timestamp'},
                {name:'narrative'},
                {name:'isOld'}]
        });

        historyData = new choxDataStore({
            url: '/prv/p/getHistories.action',
            reader:historyJsonReader
        });

//        historyData.setDefaultSort('createdDate', 'desc');
         
        historyGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:loadHistory },
            store: historyData,
            renderTo:'historyGrid',
            enableHdMenu:false,
            loadMask:true,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Created On", width: 130, dataIndex: 'createdDate', sortable: false, resizable: true},
                {header: "Created By", width: 200, dataIndex: 'createdBy', sortable: false, resizable: true},
                {header: "Message Text", width: 550, dataIndex: 'narrative', sortable: false, resizable: true}
            ],
            width:990,
            height:300
        });

        historyGrid.getView().getRowClass = function(record, index) {
            return (record.data.isOld ? 'gray-row' : 'black-row');
        };

        historyData.load({params:{claimId : <s:property value="claimId" />}});
        
    });

    function loadHistory(grid, rowIndex, columnIndex, e){
        var historyItem = historyGrid.getStore().getAt(rowIndex);
        var title = "BRE Result";
        var msg = "<b>Created Date</b>: " + historyItem.get("createdDate")
            + "<br/><b>Created By</b>: " + historyItem.get("createdBy")
            + "<br/><br/><b>Message</b>: <br/>" + historyItem.get("narrative");
        propmtMsg(title, msg);
    }
    
</script>
<div class="claim-detail-tab">
    <div id="historyGrid"></div>
</div>