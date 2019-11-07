<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
var eventLogsJsonReader;
var eventLogsDataStore;
var eventLogsGrid;

Ext.onReady(function () {

    // LOAD RECORDS
    eventLogsJsonReader = new Ext.data.JsonReader({
        totalProperty: 'totalCount',
        root: 'results',
        fields: [{
                name: 'id'
            },
            {
                name: 'createdBy'
            },
            {
                name: 'createdDate',
                type: 'date',
                dateFormat: 'd/m/Y H:i'
            },
            {
                name: 'activityName'
            },
            {
                name: 'eventName'
            },
            {
                name: 'claimStatus'
            }]
    });

    eventLogsDataStore = new choxDataStore({
        url: '/prv/p/getEventLogs.action',
        reader: eventLogsJsonReader
    });

    eventLogsDataStore.setDefaultSort('createdDate', 'asc');
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
        columns: [{
                header: "Created",
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
                header: "Activity Name",
                width: 440,
                dataIndex: 'activityName',
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

    debugger;
    loadEventLogs();
});


function eventLogOnClick(grid, rowIndex, columnIndex, e) {
    var eventLog = eventLogsGrid.getStore().getAt(rowIndex);
    var fileId = eventLog.get("id");

    if (columnIndex === 2) {
        var title = "Notes";
        var msg = "<b>Created Date</b>: " + eventLog.get("createdDate");
        msg += "<br/><b>Created By</b>: " + eventLog.get("createdBy");
        msg += "<br/><b>Message";

        if (eventLog.get("visibilityType") > 0) {
            msg += " (Private Note)";
        }
        msg += "</b>: <br/>" + eventLog.get("eventLog");
        propmtMsg(title, getFormatedMessage(msg));
    } else if (columnIndex === 4 && eventLog.get("delete") !== "") {
        deleteEventLog(fileId);
    } else if (columnIndex === 3 && eventLog.get("reviewRequired") === "Required") {
        acknowledgeEventLog(fileId);
    }
}

function getFormatedMessage(msg) {
    if (msg.length > 0) {
        msg = msg.replace('An invoice amendment has been made to the following fields:', 'An invoice amendment has been made to the following fields:<br/>');
        msg = msg.replace('Invoice Details:', '<b>Invoice Details: </b><br/>');
        msg = msg.replace('Hire Vehicle Details:', '<b>Hire Vehicle Details: </b><br/>');
        msg = msg.replace('Engineer Report:', '<b>Engineer Report: </b><br/>');
        var messageList = msg.split('). ');
        var messageHTML = "";
        if (messageList.length > 0) {
            for (var i = 0; i < messageList.length; i++) {
                if ((i + 1) < messageList.length) {
                    messageHTML += (messageList[i] + ')<br/>');
                } else {
                    messageHTML += (messageList[i]);
                }
            }
            return messageHTML;
        }
    } else {
        return "";
    }
}

function loadEventLogs() {
    eventLogsDataStore.load({
        params: {
            claimId: <s:property value = "claimId" />
        }
    });
    // setting notestabloaded = true, will enable notes tab grid panel to reload every time notes tab clicked.'
    // notesTabLoaded flag is used to find this page is loaded from p_claim_detail.jsp page.
    notesTabLoaded = true;
}
</script>

<div class="claim-detail-tab">
    <input name="claimId" id="claimId" type="hidden" value="<s:property value="claimId" />" />
    <input name="name" id="activityNameId" type="hidden" value="addNote" />

    <div id="eventLogsGrid"></div>
</div>