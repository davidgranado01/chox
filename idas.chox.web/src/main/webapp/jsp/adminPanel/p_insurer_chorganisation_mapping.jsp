<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var selectOrgId = <s:property value="selectOrgId" />;
    
    var ins_cho_gridviewJsonReader;
    var ins_cho_choGridviewJsonReader;
    
    var ins_cho_a_gridviewDataStore;
    var ins_cho_a_gridviewGrid;
    var ins_cho_a_gridviewData;
    
    var ins_cho_s_gridviewDataStore;
    var ins_cho_s_gridviewGrid;
    var ins_cho_s_gridviewData;
    
    Ext.onReady(function(){
       
       ins_cho_choGridviewJsonReader = new Ext.data.JsonReader({
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
        
        ins_cho_gridviewJsonReader = new Ext.data.JsonReader({
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
		{name:'status'},
                {name:'statusDesc'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        ins_cho_a_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getAvailableInsurerChorganisation.action',method:'GET'}),
            reader:ins_cho_choGridviewJsonReader      
        });

        ins_cho_s_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getSelectedInsurerChorganisation.action',method:'GET'}),
            reader:ins_cho_gridviewJsonReader      
        });
        
        ins_cho_a_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:ins_cho_recordOnclickAdd },
            store: ins_cho_a_gridviewData,
            loadMask: true,
            columns: [
                {header: "Name", width: 220, dataIndex: 'name', sortable: true, resizable: true},
                {header: "", width: 60, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>Add</a>"}}                
            ],
            renderTo:'ins_cho_a_gridviewGrid',
            width:300, height: 540
            });

        ins_cho_s_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:ins_cho_recordOnclickRemove },
            store: ins_cho_s_gridviewData,
            loadMask: true,
            columns: [
                {header: "Name", width: 170, dataIndex: 'chorganisationName', sortable: true, resizable: true},
                {header: "Active", width: 50, dataIndex: 'chorganisationStatusDesc', sortable: true, resizable: true},
                {header: "", width: 60, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>Remove</a>"}}
            ],
            renderTo:'ins_cho_s_gridviewGrid',
            width:300, height: 540
            });
            
            ins_cho_loadGridViewList();
    }); 
    
    function ins_cho_recordOnclickAdd(grid, rowIndex, columnIndex, e){
        
        var gridView = ins_cho_a_gridviewGrid.getStore().getAt(rowIndex);  // Get the Record
        
        if(columnIndex==1){
            ins_cho_doAddnewCredirHire(gridView);
        }
    }
    
    function ins_cho_recordOnclickRemove(grid, rowIndex, columnIndex, e){
        
        var gridView = ins_cho_s_gridviewGrid.getStore().getAt(rowIndex);  // Get the Record
        
        if(columnIndex==2){
            ins_cho_triggerStatusInactiveRecord(gridView);
        }
    }
    
    function ins_cho_loadGridViewList(){
                
        ins_cho_a_gridviewData.load(
        {
            params:
            {
                insurerId:selectOrgId
            }
        });
        
        ins_cho_s_gridviewData.load(
        {
            params:
            {
                insurerId:selectOrgId
            }
        });
        
        $("#lineOfBusinessName").val("");
        
    }
    
    
    function ins_cho_doSelectChange(){
        ins_cho_loadGridViewList();
    }
    
    function ins_cho_doAddnewCredirHire(gridView){
        
        var gridViewId = gridView.get("id");
        
        $.ajax({
           url: "<%= request.getContextPath()%>/prv/p/doAddNewInsurerChorganisation.action?chorganisationId="+gridViewId+"&insurerId="+selectOrgId+uniqeToken(),
           success: ins_cho_doSelectChange
        });
    }
    
    function ins_cho_triggerStatusInactiveRecord(gridView){
        
        var gridViewId = gridView.get("id");
        
        if(confirm("Are you sure you want to remove this credit hire organisation?")){
         $.ajax({
           url: "<%= request.getContextPath()%>/prv/p/doRemoveInsurerChorganisation.action?objectId="+gridViewId+uniqeToken(),
           success: ins_cho_doSelectChange
         });
        }
    }
    
    
</script>

<div>
    <div id="organisationGird">
        <table width="100%">
            <tr>
            <td valign="top">
                <div class="girdViewLabel">Selected Credit Hire Organisations</div>
                <div id="ins_cho_s_gridviewGrid" class="girdViewObject"></div>
            </td>
            <td valign="top">
                <div class="girdViewLabel">Available Credit Hire Organisations</div>
                <div id="ins_cho_a_gridviewGrid" class="girdViewObject"></div>
            </td>
            </tr>
        </table>
    </div>

</div>