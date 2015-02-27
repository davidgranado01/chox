<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<div id="insurerBreDetailTab">

    <script type="text/javascript">

        var breband_gridviewJsonReader;
        var breband_gridviewDataStore;
        var breband_gridviewGrid;
        var breband_gridviewData;

        Ext.onReady(function(){

            breband_gridviewJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'id'},
                    {name:'name'},
                    {name:'insurerName'},
                    {name:'status'},
                    {name:'statusDesc'},
                    {name:'createdBy'},
                    {name:'createdDate', type: 'date', dateFormat:'d/m/Y H:i'}
                ]
            });

            breband_gridviewData = new choxDataStore({
                url: '/prv/p/getInsurerBreBand.action',
                reader:breband_gridviewJsonReader
            });

            breband_gridviewGrid = new Ext.grid.GridPanel({
                listeners:  {cellclick:breband_recordOnclick },
                store: breband_gridviewData,
                renderTo:'breband_gridviewGrid',
                enableHdMenu:false,
                enableColumnMove: false,
                layout:'fit',
                viewConfig:{forceFit:true},
                columns: [
                    {header: "Insurer", width: 100, dataIndex: 'insurerName', sortable: true, resizable: true},
                    {header: "Band", width: 240, dataIndex: 'name', sortable: true, resizable: true, renderer:function(value,p,r){
                            return "<a href='#' class='high-light-item'>" + value + "</a>";}},
                    {header: "Created By", width: 110, dataIndex: 'createdBy', sortable: true, resizable: true},
                    {header: "Created Date", width: 150, dataIndex: 'createdDate', sortable: true, resizable: true, renderer: Ext.util.Format.dateRenderer('d/m/Y H:i')}
                ],

                height:460,
                width: 760
            });

            breband_gridviewData.load({params:{insurerId:<s:property value="insurerId" />}});

        });

        function breband_recordOnclick(grid, rowIndex, columnIndex, e){
            if(columnIndex===1){
                var gridView = breband_gridviewGrid.getStore().getAt(rowIndex);
                var breBandId = gridView.get("id");
                var target = "div#insurerBreDetailTab";
                var url = "/prv/p/updateInsurerBreBandDetailPanel.action";
                var param = {"objectId":breBandId, "insurerId":<s:property value="insurerId" />};
                ajax.loadHtml2(url,param,function(data){
                    $(target).html(data);
                });
            }
        }

        function breband_createNewRecord(){
            var target = "div#insurerBreDetailTab";
            var url = "/prv/p/updateInsurerBreBandDetailPanel.action";
            var param = {"objectId":"-1","insurerId":<s:property value="insurerId" />};
            ajax.loadHtml2(url,param,function(data){
                $(target).html(data);
            });
        }

    </script>

    <div class="sub-admin-tab-css">

        <div class="status-info">
            The BRE tolerances/variable values are controlled here as well as which rules are turned on/off for the particular BRE Band being created/modified.  BRE Bands can be created specific to each CHO, or CHOs can be grouped into a single BRE Band and therefore share the same tolerances/variables as well as which rules are executed when the CHO submits an invoice.
        </div>

        <div class="grid-view-header">
            <table width="100%">
                <tr>
                    <td align="right">
                        <button type="button" onclick="javascript:breband_createNewRecord();">Add New Band</button>
                    </td>
                </tr>
            </table>
        </div>

        <div id="breband_gridviewGrid"></div>

    </div>

</div>