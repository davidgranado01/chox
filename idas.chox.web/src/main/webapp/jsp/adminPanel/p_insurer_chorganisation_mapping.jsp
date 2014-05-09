<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var insChoAvailable_gridviewJsonReader;
    var insChoAvailable_gridviewGrid;
    var insChoAvailable_gridviewData;

    var insChoSelected_gridviewJsonReader;
    var insChoSelected_gridviewGrid;
    var insChoSelected_gridviewData;

    Ext.onReady(function(){

        insChoAvailable_gridviewJsonReader = new Ext.data.JsonReader({
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

        insChoAvailable_gridviewData = new choxDataStore({
            url: '/prv/p/getAvailableInsurerChorganisation.action',
            reader:insChoAvailable_gridviewJsonReader
        });

        insChoAvailable_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:insCho_recordOnclickAddNewCreditHire},
            store: insChoAvailable_gridviewData,
            renderTo:'insChoAvailable_gridviewGrid',
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Name", width: 220, dataIndex: 'name', sortable: true, resizable: true},
                {header: "", width: 60, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){ return "<a href='#' class='high-light-item'>Add</a>";}}
            ],
            height:500, width: 360
        });

        insChoSelected_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'insurerId'},
                {name:'chorganisationId'},
                {name:'insurerName'},
                {name:'chorganisationName'},
                {name:'status'},
                {name:'statusDesc'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        insChoSelected_gridviewData = new choxDataStore({
            url: '/prv/p/getSelectedInsurerChorganisation.action',
            reader:insChoSelected_gridviewJsonReader
        });

        insChoSelected_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:insCho_recordOnclickRemoveCreditHire},
            store: insChoSelected_gridviewData,
            renderTo:'ins_cho_s_gridviewGrid',
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Name", width: 170, dataIndex: 'chorganisationName', sortable: true, resizable: true},
                {header: "Active", width: 50, dataIndex: 'statusDesc', sortable: true, resizable: true},
                {header: "", width: 60, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>Remove</a>";}}
            ],
            height:500,
            width: 360
        });

        insCho_loadGridViewList();
    });

    function insCho_loadGridViewList(){
        insChoAvailable_gridviewData.load({params:{insurerId:<s:property value="insurerId" />}});
        insChoSelected_gridviewData.load({params:{insurerId:<s:property value="insurerId" />}});
    }

    function insCho_recordOnclickAddNewCreditHire(grid, rowIndex, columnIndex, e){

        var gridView = insChoAvailable_gridviewGrid.getStore().getAt(rowIndex);
        if(columnIndex===1){
            var chorganisationId = gridView.get("id");
            var url = "/prv/p/doAddNewInsurerChorganisation.action";
            var param = {"insurerId":<s:property value="insurerId" />,"chorganisationId":chorganisationId};
            ajax.loadHtml2(url, param, doInsurerChorganisationPageRefresh);
        }

    }

    function insCho_recordOnclickRemoveCreditHire(grid, rowIndex, columnIndex, e){

        if(columnIndex===2){
            Ext.MessageBox.confirm('Confirm', 'Are you sure you want to remove this credit hire organisation?',function(btn){
            if(btn==='yes'){

                var gridView = insChoSelected_gridviewGrid.getStore().getAt(rowIndex);
                var insurerChorganisationId = gridView.get("id");
                var url = "/prv/p/doRemoveInsurerChorganisation.action";
                var param = {"insurerChorganisationId":insurerChorganisationId};

                ajax.loadHtml2(url, param, doInsurerChorganisationPageRefresh);
            }
          });  
        }
    }

    function doInsurerChorganisationPageRefresh(responseText, statusText){
       var response = eval('(' + responseText.trim() + ')');
       
       if(response){ 
            
           if(response.isValid){
              if(response.resultType && response.resultType === 'Message')
                {
                    Ext.MessageBox.show({
                    title: '',
                    msg: response.result,
                    width: 300,
                    buttons: Ext.MessageBox.OK,
                    icon : Ext.MessageBox.ERROR
                    });
                    return;
                }
            }
            
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
        insCho_loadGridViewList();
    }

</script>

<div class="sub-admin-tab-css">
    <div class="status-info">
        This tab dictates which CHOs can submit claims into the system against this particular Insurer.
    </div>

    <table width="100%">
        <tr>
            <td valign="top">
                <label class="gird-view-label">Selected Credit Hire Organisations</label>
                <div id="ins_cho_s_gridviewGrid"></div>
            </td>
            <td valign="top">
                <label class="gird-view-label">Available Credit Hire Organisations</label>
                <div id="insChoAvailable_gridviewGrid"></div>
            </td>
        </tr>
    </table>
</div>  