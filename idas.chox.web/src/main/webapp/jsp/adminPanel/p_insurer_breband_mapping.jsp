<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var breband_mapping_gridviewJsonReader;
    var breband_mapping_gridviewDataStore;
    var breband_mapping_gridviewGrid;
    var breband_mapping_gridviewData;
    var selectBandId = -1;

    var breband_gridviewJsonReader;
    var breband_choGridviewJsonReader;

    var breband_a_gridviewDataStore;
    var breband_a_gridviewGrid;
    var breband_a_gridviewData;

    var breband_s_gridviewDataStore;
    var breband_s_gridviewGrid;
    var breband_s_gridviewData;

    Ext.onReady(function(){

        breband_choGridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'name'},
                {name:'status'},
                {name:'statusDesc'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        breband_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'insurerId'},
                {name:'chorganisationId'},
                {name:'insurerName'},
                {name:'chorganisationName'},
                {name:'chorganisationStatus'},
                {name:'chorganisationStatusDesc'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        breband_a_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getAvailableInsurerBreBandChorganisation.action',method:'POST'}),
            reader:breband_choGridviewJsonReader
        });

        breband_s_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getSelectedInsurerBreBandChorganisation.action',method:'POST'}),
            reader:breband_gridviewJsonReader
        });

        breband_a_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:breband_recordOnclickAdd },
            store: breband_a_gridviewData,
            renderTo:'breband_a_gridviewGrid',
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Name", width: 180, dataIndex: 'name', sortable: true, resizable: true},
                {header: "", width: 70, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>Add</a>"}}
            ],
            height:430,
            width: 360
        });

        breband_s_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:breband_recordOnclickRemove },
            store: breband_s_gridviewData,
            renderTo:'breband_s_gridviewGrid',
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Name", width: 180, dataIndex: 'chorganisationName', sortable: true, resizable: true},
                {header: "Active", width: 50, dataIndex: 'chorganisationStatusDesc', sortable: true, resizable: true},
                {header: "", width: 60, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>Remove</a>"}}
            ],
            height:430,
            width: 360
        });

        onBreBandPageRefresh()
    });

    function onBreBandPageRefresh(){
        showBreDropDown();
        brebandMapping_loadGridViewList();
    }

    function brebandMapping_loadGridViewList(){
        breband_a_gridviewData.load({params:{insurerId:<s:property value="insurerId" />}});
        if($("#breBandId").val() != undefined)
        	breband_s_gridviewData.load({params:{insurerId:<s:property value="insurerId" />,breBandId:$("#breBandId").val()}});
    }

    function breband_recordOnclickAdd(grid, rowIndex, columnIndex, e){

        if($("#breBandId").val()<=0){
            Ext.MessageBox.alert('', 'Please select a BRE Band');
            return;
        }

        if(columnIndex==1){
            var gridView = breband_a_gridviewGrid.getStore().getAt(rowIndex);
            var gridViewId = gridView.get("id");
            var url = "<%= request.getContextPath()%>/prv/p/doAddNewBandChorganisationMapping.action";
            var param = {"chorganisationId":gridViewId, "breBandId":$("#breBandId").val()};
            ajax.loadHtml2(url, param, afterBreBandMappingSubmit);
        }
    }

    function breband_recordOnclickRemove(grid, rowIndex, columnIndex, e){

        if(columnIndex==2){
            var gridView = breband_s_gridviewGrid.getStore().getAt(rowIndex);
            var breBandChorganisationId = gridView.get("id");
            var url = "<%= request.getContextPath()%>/prv/p/doRemoveBandChorganisationMapping.action";
            var param = {"breBandChorganisationId":breBandChorganisationId};
            ajax.loadHtml2(url, param, afterBreBandMappingSubmit);
        }

    }

    function doBRESelectOnChange(){
        brebandMapping_loadGridViewList();
    }

    function showBreDropDown() {
        var target = "div#breBandDropDownDiv";
        var url = "<%= request.getContextPath()%>/prv/p/BreBandDropDownAction.action";
        var param = {"insurerId":<s:property value="insurerId" />};
        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
           
        });
    }
    
    function afterBreBandMappingSubmit(responseText, statusText) {
       var response = eval('(' + responseText.trim() + ')');
       
       if(response)
        {
            if(!response.isValid){
               $.each(response.errors, function() {
                    Ext.MessageBox.show({
                        title: '',
                        msg: this.toString(),
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }); 
            } 
            
        }
        brebandMapping_loadGridViewList();
    }

</script>

<div class="sub-admin-tab-css">

    <div class="status-info">
        This tab allows you to assign CHOs to their relevant BRE Bands that have been created.  Please note that you can assign more than one CHO to a BRE Band but a CHO can only be assigned to one BRE Band.
    </div>

    <div class="grid-view-header">
        <table width="100%">
            <tr>
                <td>
                    <div class="label-block">
                        <div id="breBandDropDownDiv" class="label-block"/>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="label-block">
                        <div id="CDBrebandMappingMessageBox" class="action-error-msg"/>
                    </div>
                </td>
            </tr>
        </table>
    </div>

    <table width="100%">
        <tr>
            <td valign="top">
                <label class="gird-view-label">Selected Credit Hire Organisations</label>
                <div id="breband_s_gridviewGrid"></div>
            </td>
            <td valign="top">
                <label class="gird-view-label">Available Credit Hire Organisations</label>
                <div id="breband_a_gridviewGrid"></div>
            </td>
        </tr>
    </table>


</div>