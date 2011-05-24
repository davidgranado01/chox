<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var auditTrailJsonReader;
    var auditTrailGrid;
    var auditTrailData;
    var auditGrid;
    var hideReverted = true;

    $(function(){

        auditTrailJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'reverted'},
                {name:'modifiedDate', type: 'date', dateFormat: 'd/m/Y H:i:s'},
                {name:'modifiedBy'},
                {name:'status'}
            ]
        });

        auditTrailData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getAuditTrails.action',method:'GET'}),
            reader:auditTrailJsonReader
        });

        var dateRenderer = Ext.util.Format.dateRenderer('d/m/Y H:i:s');
        auditTrailData.setDefaultSort('modifiedDate', 'desc');

        auditGrid = new Ext.grid.GridPanel({
            id: 'audit_trail_grid_id',
            listeners:  {cellclick:auditOnClick},
            store: auditTrailData,
            renderTo:'auditTrailGrid',
            enableHdMenu:false,
            loadMask:true,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Modified Date", width: 130, dataIndex: 'modifiedDate', sortable: true, resizable: true, renderer: dateRenderer},
                {header: "Modified By", width: 260, dataIndex: 'modifiedBy', sortable: true, resizable: true, renderer: renderStrike},
                {header: "Status", width: 500, dataIndex: 'status', sortable: true, resizable: true, renderer: renderStrike}
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
            + "<br/><br/><b>Status</b>: " + audit.get("status")

        propmtMsg(title, msg);
    }

    function renderStrike(value,p,rec, row) {
        var roffs = 30;
        var grid = Ext.getCmp('audit_trail_grid_id');
        var w = grid.getColumnModel().getTotalWidth() - roffs;
        var posY = (row*21)+11;
        if (rec.data.reverted) // Condition to strikethrough
            value = '<div style="position:absolute; top:'+posY+'px; width:'+w+'px; height:1px; background-color:#000000;"></div>'+value
        return value;
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
    <div class="chox-form-item">
        <input type="checkbox" value="Hide" id="hideRevertedToggleId" checked="true" onclick="return toggleReverted(this)"/>
        &nbsp;Hide Reverted Claim Cycle Entries<p>
    </div>

    <div id="auditTrailGrid"></div>
</div>