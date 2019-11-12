<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
var eventLogsJsonReader;
var eventLogsDataStore;
var eventLogsGrid;

Ext.onReady(function () {

    // LOAD RECORDS
    eventLogsJsonReader = new Ext.data.JsonReader({
        idProperty: 'id',
        totalProperty: 'totalCount',
        root: 'results',
        fields: [{
                name: 'id'
            }, {
                name: 'createdBy'
            }, {
                name: 'createdDate',
                type: 'date',
                dateFormat: 'd/m/Y H:i'
            }, {
                name: 'activityName'
            }, {
                name: 'eventName'
            }, {
                name: 'claimStatus'
            }]
    });

    eventLogsDataStore = new choxDataStore({
        url: '/prv/p/getEventLogs.action',
        reader: eventLogsJsonReader
    });

    eventLogsDataStore.setDefaultSort('createdDate', 'desc');
    var dateRenderer = Ext.util.Format.dateRenderer('d/m/Y H:i');

    eventLogsGrid = new Ext.grid.GridPanel({
        listeners: {
            cellclick: eventLogOnClick
        },
        loadMask: true,
        store: eventLogsDataStore,
        renderTo: 'eventLogsGrid',
        enableHdMenu: false,
        layout: 'fit',
        frame: true,
        columns: [{
                header: "Created Date",
                width: 260,
                dataIndex: 'createdDate',
                sortable: true,
                resizable: true,
                renderer: dateRenderer
            },
            {
                header: "Created By",
                width: 440,
                dataIndex: 'createdBy',
                sortable: true,
                resizable: true
            },
            {
                header: "Event Name",
                width: 440,
                dataIndex: 'eventName',
                sortable: true,
                resizable: true
            },
            {
                header: "Activity Name",
                width: 440,
                dataIndex: 'activityName',
                sortable: true,
                resizable: true
            },
            {
                header: "Claim Status",
                width: 440,
                dataIndex: 'claimStatus',
                sortable: true,
                resizable: true
            }
        ],
        viewConfig: {
            forceFit: true
        },
        width: 990,
        height: 300
    });

    loadEventLogs();
});


function eventLogOnClick(grid, rowIndex, columnIndex, e) {
    var eventLog = eventLogsGrid.getStore().getAt(rowIndex);
    var eventLogId = eventLog.get("id");

    // show the mask when loading data
    eventLogsGrid.loadMask.show();
    choxExtAjaxRequest({
        url: '/prv/p/getEventLogAttributes.action',
        callback : function(options,success,response) {
            var attributes = Ext.util.JSON.decode(response.responseText);
            var title = "Notes";
            var msg = "<b>Created Date</b>: " + eventLog.get("createdDate");
            msg += "<br/><b>Created By</b>: " + eventLog.get("createdBy");
            msg += "<br/><b>Activity Name</b>: " + eventLog.get("activityName");
            msg += "<br/><b>Event Name</b>: " + eventLog.get("eventName");
            msg += "<br/><b>Claim Status</b>: " + eventLog.get("claimStatus");
            msg += "<br/><b>Attributes</b></br/>";

            // hide the load mask of the event
            eventLogsGrid.loadMask.hide();
            propmtMsg(title, getFormatedMessage(msg, attributes));
        },
        params: {
            eventLogId: eventLogId
        }
    });
}

function getFormatedMessage(msg, attributes) {
    for (var key in attributes) {
        if (attributes.hasOwnProperty(key)) {
            msg += key + ": " + attributes[key] + '<br />'
        }
    }

    return msg;
}

function loadEventLogs() {
    eventLogsDataStore.load({
        params: {
            claimId: <s:property value = "claimId" />
        }
    });
}
</script>

<div class="claim-detail-tab">
    <input name="claimId" id="claimId" type="hidden" value="<s:property value="claimId" />" />
    <input name="name" id="activityNameId" type="hidden" value="addNote" />

    <div id="eventLogsGrid"></div>
</div>