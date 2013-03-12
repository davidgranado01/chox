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


//        TpiInsurerStore = new Ext.data.Store( {
//            proxy : new Ext.data.HttpProxy( {
//                url : ChoxAppname + '/prv/p/listBillingOrgData.action'
//            }),
//            reader : new Ext.data.JsonReader( {
//                fields : [ 'orgId', 'name' ],
//                root : 'results'
//            }),
//            baseParams:{
//                billingType:Chox.billing.billingmode
//            }
//        });
//
//
//        TpiInsurerWorkgroupStore = new Ext.data.Store( {
//            proxy : new Ext.data.HttpProxy( {
//                url : ChoxAppname + '/prv/p/listBillingOrgData.action'
//            }),
//            reader : new Ext.data.JsonReader( {
//                fields : [ 'orgId', 'name' ],
//                root : 'results'
//            }),
//            baseParams:{
//                billingType:Chox.billing.billingmode
//            }
//        });
//
//        TpiInsurerWorkgroupOwnerStore = new Ext.data.Store( {
//            proxy : new Ext.data.HttpProxy( {
//                url : ChoxAppname + '/prv/p/listBillingOrgData.action'
//            }),
//            reader : new Ext.data.JsonReader( {
//                fields : [ 'orgId', 'name' ],
//                root : 'results'
//            }),
//            baseParams:{
//                billingType:Chox.billing.billingmode
//            }
//        });

        var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
        var tbar = new Ext.Toolbar({
            items:[{
                    text:'New ',
                    handler : function() {

                        Ext.MessageBox.alert('Status', 'Sorry this function is not yet implemented.' );
//                        cb.billingFormObj.getForm().reset();
//                        cb.billingWindowObj.show();
                    }
                },'-','',{
                    text:'Edit ',
                    handler : function(){
                        Ext.MessageBox.alert('Status', 'Sorry this function is not yet implemented.' );
                        //Ext.MessageBox.confirm('Confirm', 'Are you sure you want to delete this schedule?', deleteSchedule );
                    }
                },'-','',{
                    text:'Delete ',
                    handler : function() {
                        Ext.MessageBox.alert('Status', 'Sorry this function is not yet implemented.' );
//                        var selected = cb.schSel.getSelected();
//                        if( selected ){
//                            var rptName;
//                            if ( Chox.billing.billingmode =='insurer'){
//                                rptName = 'BillingInsurerReport-Excel';
//                            }else{
//                                rptName = 'BillingChoReport-Excel';
//                            }
//                            var rpthref = Chox.appname+ '/prv/p/exportExcelReport.action?reportName=' + rptName +'&' +Ext.urlEncode(selected.data);//+dtstr;
//
//                            location.href = rpthref;
//                        }
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

        insChoTpi_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getTpiActivatedInsurerChorganisation.action',method:'POST'}),
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
        //alert("load method called",'<s:property value="objectId" />');
        insChoTpi_gridviewData.load({params:{chorganisationId:<s:property value="objectId" />}});
    }

    function insCho_recordOnclickTpiMapping(grid, rowIndex, columnIndex, e){

         Ext.MessageBox.alert('Status', 'Sorry this function is not yet implemented.' );

//        var gridView = insChoTpi_gridviewGrid.getStore().getAt(rowIndex);
//        if(columnIndex==7){
//            var chorganisationId = gridView.get("id");
//            alert(chorganisationId);
//            var url = "<%= request.getContextPath()%>/prv/p/doRemoveTpiMapping.action";
//            var param = {"insurerId":<s:property value="insurerId" />,"chorganisationId":chorganisationId};
//            ajax.loadHtml2(url, param, doInsurerChorganisationPageRefresh);
//        }

    }

</script>



<div class="sub-admin-tab-css">
    <div class="status-info">
        This tab contains the rules for when a claim is uploaded to automatically assign the claim to a Workgroup and therefore avoid the manual routing of claims where the Insurer uses Workgroups.
    </div>


    <div id="insChoTpi_gridviewGrid"/>
</div>
