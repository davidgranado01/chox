<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var insChoTpi_gridviewJsonReader;
    var insChoTpi_gridviewGrid;
    var insChoTpi_gridviewData;
    var ChoxAppname = '<%= request.getContextPath()%>';
    var TpiInsurerStore;
    var TpiInsurerWorkgroupStore;
    var TpiInsurerWorkgroupOwnerStore;

    Ext.onReady(function(){
        var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
        var tbar = new Ext.Toolbar({
            items:[{
                    text:'New ',
                    handler : function() {

                        Ext.MessageBox.alert('Status', 'Sorry this function is not yet implemented.' );
                    }
                },'-','',{
                    text:'Edit ',
                    handler : function(){
                        Ext.MessageBox.alert('Status', 'Sorry this function is not yet implemented.' );
                    }
                },'-','',{
                    text:'Delete ',
                    handler : function() {
                        Ext.MessageBox.alert('Status', 'Sorry this function is not yet implemented.' );
                    }
                }
            ]
        });
      

        insChoTpi_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'chorganisationName'},
                {name:'insurerName'},
                {name:'tpiClaimOnly'},
                {name:'tpiClaimOwnerName'},
                {name:'tpiIdentifier'},
                {name:'tpiRegexString'},
                {name:'tpiWorkgroupName'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        insChoTpi_gridviewData = new choxDataStore({
            url: '/prv/p/getTpiActivatedInsurerChorganisation.action',
            reader:insChoTpi_gridviewJsonReader
        });

        insChoTpi_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:insCho_recordOnclickTpiMapping},
            store: insChoTpi_gridviewData,
            renderTo:'insChoTpi_gridviewGrid',
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            viewConfig:{forceFit:true},
            sm:sm,
            tbar:tbar,
            columns: [
                sm,
                {header: "TPI Insurer Name", width: 120, dataIndex: 'insurerName', tabTip:'CHO Details', sortable: true, resizable: true},
                {header: "TPI Routed Workgroup", width: 140, dataIndex: 'tpiWorkgroupName', sortable: true, resizable: true},
                {header: "TPI Routed Claim Owner", width: 140, dataIndex: 'tpiClaimOwnerName', sortable: true, resizable: true},
                {header: "TPI Claim Identifier", width: 120, dataIndex: 'tpiIdentifier', sortable: true, resizable: true},
                {header: "TPI Exception Regex", width: 130, dataIndex: 'tpiRegexString', sortable: true, resizable: true},
                {header: "TPI Claim Only", width: 120, dataIndex: 'tpiClaimOnly', sortable: true, resizable: true}
                
            ],
            height:490, width: 770
        });

        

        insCho_loadGridViewList();

    });

    function insCho_loadGridViewList(){
        insChoTpi_gridviewData.load({params:{chorganisationId:<s:property value="objectId" />}});
    }

    function insCho_recordOnclickTpiMapping(grid, rowIndex, columnIndex, e){
         Ext.MessageBox.alert('Status', 'Sorry this function is not yet implemented.' );
    }

</script>



<div class="sub-admin-tab-css">
    <div class="status-info">
        This tab contains the rules for when a claim is uploaded to automatically assign the claim to a Workgroup and therefore avoid the manual routing of claims where the Insurer uses Workgroups.
    </div>


    <div id="insChoTpi_gridviewGrid"/>
</div>
