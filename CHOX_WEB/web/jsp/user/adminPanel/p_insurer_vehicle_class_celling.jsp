<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<script type="text/javascript">

    var selectOrgId = <s:property value="selectOrgId" />;
    
    var vehicleClassCellingJsonReader;
    var vehicleClassCelling_gridviewData;
    var vehicleClassCelling_gridviewGrid;

    $(document).ready(function(){
        doVehicleClassCellingFormValidation();
    });
        
    Ext.onReady(function(){

       vehicleClassCelling_JsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
            [
                {name:'id'},
                {name:'vehicleClassId'},
                {name:'vehicleClassName'},
		{name:'hireNetCelling'},
                {name:'repairNetCelling'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        vehicleClassCelling_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: 'getSelectedInsurerVehicleClassCelling.action',method:'GET'}),
            reader:vehicleClassCelling_JsonReader
        });

        vehicleClassCelling_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:vehicleClass_recordOnclickRemove },
            store: vehicleClassCelling_gridviewData,
            loadMask: true,
            columns: [
                {header: "Name", width: 180, dataIndex: 'vehicleClassName', sortable: true, resizable: true},
                {header: "Hire Net Celling", width: 150, dataIndex: 'hireNetCelling', sortable: true, resizable: true},
                {header: "Repair Net Celling", width: 150, dataIndex: 'repairNetCelling', sortable: true, resizable: true},
                {header: "", width: 90, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>Remove</a>"}}
            ],
            renderTo:'vehicleClassCelling_gridviewGrid',
                width:590,
                autoHeight:true,
                enableHdMenu:false
            });

            onVehicleClassPageRefresh();
    });

    function onVehicleClassPageRefresh(){
        showVehicleClassDropDown();
        vehicleClassCelling_loadGridViewList();
        refreshForm();
    }

    function refreshForm(){
        $("#vehicleClassId").val("");
        $("#hireNetCelling").val("0.00");
        $("#repairNetCelling").val("0.00");
    }

    function vehicleClassCelling_loadGridViewList(){

        vehicleClassCelling_gridviewData.load(
        {
            params:
            {
                insurerId:selectOrgId
            }
        });
    }


    function vehicleClass_recordOnclickRemove(grid, rowIndex, columnIndex, e){
        
        var gridView = vehicleClassCelling_gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");

        if(columnIndex==3){
            
            $.ajax({
               url: "doRemoveVehicleClassCellingMapping.action?vehicleClassCellingId="+gridViewId,
               success: onVehicleClassPageRefresh
            });
            
        }
        
    }

    function showVehicleClassDropDown() {
        $("#vehicleClassDropDownDiv").load("VehicleClassDropDownAction.action?orgId=" + selectOrgId);
        $("#breBandId").val(-1);
    }

    function doVehicleClassCellingSubmit(){

         if(doVehicleClassCellingFormValidation().form()){

                $(".form-container").block();

                var op = {
                    success:onVehicleClassCellingSubmitResponseReceived,
                    timeout: 3000,
                    error: onVehicleClassCellingSubmitError
                };

                $("#formVehicleClassCellingDetail").ajaxSubmit(op);
                    
         }

    }

    function doVehicleClassCellingFormValidation(){

        var validateFlag = $("#formVehicleClassCellingDetail").validate(
        {
           errorLabelContainer: "#CDVehicleClassCellingMessageBox",
           rules: {
                vehicleClassId:{min:0 },
                hireNetCelling:{ required:true, number:true, min:0.01 },
                repairNetCelling:{ required:true, number:true, min:0.01 }
           },
           messages: {
             vehicleClassId: {min:"You must select a 'Vehicle Class'" },
             hireNetCelling: { required:"You must supply a value for 'Hire Net Celling'", number:"'Hire Net Celling' must be numeric", min:"'Hire Net Celling' cannot be less than zero" },
             repairNetCelling: { required:"You must supply a value for 'Repair Net Celling'", number:"'Repair Net Celling' must be numeric", min:"'Repair Net Celling' cannot be less than zero" }
           },
           submitHandler: function(form) {}
        });

        return validateFlag;
    }
    
    function onVehicleClassCellingSubmitResponseReceived(){
        onVehicleClassPageRefresh();
        $(".form-container").unblock();
    }

    function onVehicleClassCellingSubmitError(){
        alert("Unexpected Error has been encountered, please try again.");
        $(".form-container").unblock();
    }

</script>

<div>
    <div id="organisationGird">
        <div class="gridViewHeader">
           <table width="100%">
<tr>
    <td>
    
        <fieldset class="x-fieldset"><legend>Add New Vehicle Class Celling</legend>
            <div class="form-container">
                <form id="formVehicleClassCellingDetail" action="vehicleClassCellingDetail.action" class="XXentity-form" onsubmit="return true;">

                    <input type="hidden" name="selectOrgId" value='<s:property value="selectOrgId"/>'>
                    
                <div id="vehicleClassDropDownDiv" class="chox-form-item"></div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Hire Net Celling</label>
                    <input id="hireNetCelling" name="hireNetCelling" value="<s:property value="hireNetCelling" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Repair Net Celling</label>
                    <input id="repairNetCelling" name="repairNetCelling" value="<s:property value="repairNetCelling" />"/>
                </div>
                <div class="chox-form-button">
                    <input type="button" value='Add' onclick="javascript: return doVehicleClassCellingSubmit();"/>
                </div>
                <div id="CDVehicleClassCellingMessageBox" class="errorBox"></div>
                </form>
            </div>
        </fieldset>

    </td>
</tr>

<tr>
    <td valign="top">
    <div class="girdViewLabel">Vehicle Class Celling</div>
    <div id="vehicleClassCelling_gridviewGrid" style="height:300px; overflow:auto;"></div>
    </td>
</tr>
            </table>
        </div>
    </div>
</div>
