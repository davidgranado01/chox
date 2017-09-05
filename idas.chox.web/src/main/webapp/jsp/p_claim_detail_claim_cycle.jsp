<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var auditTrailJsonReader;
    var auditTrailGrid;
    var auditTrailData;
    var auditGrid;
    var hideReverted = true;

    Ext.onReady(function() {

        auditTrailJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'reverted'},
                {name:'modifiedDate', type: 'string', dateFormat:'timestamp'},
                {name:'modifiedBy'},
                {name:'status'}
            ]
        });

        auditTrailData = new choxDataStore({
            url: '/prv/p/getAuditTrails.action',
            reader:auditTrailJsonReader
        });

//        var dateRenderer = Ext.util.Format.dateRenderer('d/m/Y H:i');

        auditGrid = new Ext.grid.GridPanel({
            id: 'audit_trail_grid_id',
            listeners:  {cellclick:auditOnClick},
            store: auditTrailData,
            renderTo:'auditTrailGrid',
            enableHdMenu:false,
            loadMask:true,
            layout:'fit',
            viewConfig:{forceFit:true, getRowClass: function(record, rowIndex, rp, ds){ // rp = rowParams
              return (record.data.reverted ? 'strikethroughRow' : 'black-row' );
 //             return (record.data.reverted ? 'gray-row' : 'black-row' );
            }},
            columns: [
                {header: "Modified Date", width: 130, dataIndex: 'modifiedDate', sortable: false, resizable: true},
                {header: "Modified By", width: 260, dataIndex: 'modifiedBy', sortable: false, resizable: true},
                {header: "Status", width: 500, dataIndex: 'status', sortable: false, resizable: true }
            ],
            width:990,
            height:300
        });

        auditTrailData.load(
        {
            params:{claimId : <s:property value="claimId" />, hideReverted : hideReverted}
        });

    });

    function auditOnClick(grid, rowIndex){
        var audit = auditGrid.getStore().getAt(rowIndex);

        var title = "Claim Cycle";
        var msg = "<b>Modified Date</b>: " + audit.get("modifiedDate")
            + "<br/><b>Modified By</b>: " + audit.get("modifiedBy")
            + "<br/><br/><b>Status</b>: " + audit.get("status");

        propmtMsg(title, msg);
    }
    
    function loadAuditTrail(){
        auditTrailData.load(
        {
            params:{claimId : <s:property value="claimId" />, hideReverted : hideReverted}
        });
    }

    function toggleReverted(el) {
        
        hideReverted = !hideReverted;
        loadAuditTrail();
    }


</script>    
    <div class="claim-detail-tab">
    <s:if test="hasReverted">
        <div class="chox-form-item">
            <input type="checkbox" value="Hide" id="hideRevertedToggleId" checked="true" onclick="return toggleReverted(this);"/>
                &nbsp;Hide Reverted Claim Cycle Entries<p>
        </div>
    </s:if>

    <div id="auditTrailGrid"></div>
</div>