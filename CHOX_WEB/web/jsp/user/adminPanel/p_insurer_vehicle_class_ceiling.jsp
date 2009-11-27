<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<script type="text/javascript">

    var selectOrgId = <s:property value="selectOrgId" />;
    var vehicleClassCeilingJsonReader;
    var vehicleClassCeiling_gridviewData;
    var vehicleClassCeiling_gridviewGrid;
    var vehicleCeilingEditSelectionDlg;
    
    $(document).ready(function(){
        doVehicleClassCeilingFormValidation();
    });

    Ext.onReady(function(){

        if(!vehicleCeilingEditSelectionDlg)
        {
            vehicleCeilingEditSelectionDlg =  new Ext.Window({
                applyTo:'vccSelectionDlgHolder',
                width:400,
                height:200,
                layout:'fit',
                modal:true,
                closeAction:'hide',
                plain: false,
                title: 'Edit Vehicle Class Ceiling',
                resizable : false,
                items: new Ext.Panel({
                    applyTo: 'vccSelectionPanel'
                }),
                buttons: [{
                        text:'Ok', handler:function(){

                            $("form#editVehicleClassCeilingDetail").validate(
                            {
                                errorLabelContainer: "#HMmessageBox",
                                rules: {
                                    editVehicleClassId:{min:0 },
                                    editHireNetCeiling:{ required:true, number:true, min:0.01 },
                                    editRepairNetCeiling:{ required:true, number:true, min:0.01 }
                                },
                                messages: {
                                     editVehicleClassId: {min:"You must select a 'Vehicle Class'" },
                                     editHireNetCeiling: { required:"You must supply a value for 'Hire Net Ceiling'", number:"'Hire Net Ceiling' must be numeric", min:"'Hire Net Ceiling' cannot be less than zero" },
                                     editRepairNetCeiling: { required:"You must supply a value for 'Repair Net Ceiling'", number:"'Repair Net Ceiling' must be numeric", min:"'Repair Net Ceiling' cannot be less than zero" }
                                }
                            });

                            if($('form#editVehicleClassCeilingDetail').valid()){

                                var parameter = "?vehicleClassCeilingId="+$("#editVehicleClassId").val();
                                parameter += "&hireNetCeiling="+$("#editHireNetCeiling").val();
                                parameter += "&repairNetCeiling="+$("#editRepairNetCeiling").val();

                                $.ajax({
                                   url: "editVehicleClassCeilingDetail.action"+parameter,
                                   success: vehicleClassCeilingUpdateSuccess,
                                   error: vehicleClassCeilingUpdateError
                                });
                            }
                        }
                        
                    },{
                        text: 'Close',
                        handler: function(){
                            vehicleCeilingEditSelectionDlg.hide();
                        }
                    }]
            });
        }

        function vehicleClassCeilingUpdateError(){
            alert("Unexpected occured, please try again");
            vehicleCeilingEditSelectionDlg.hide();
        }

        function vehicleClassCeilingUpdateSuccess(){
            onVehicleClassPageRefresh();
            vehicleCeilingEditSelectionDlg.hide();
        }

       vehicleClassCeiling_JsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
            [
                {name:'id'},
                {name:'vehicleClassId'},
                {name:'vehicleClassName'},
		{name:'hireNetCeiling'},
                {name:'repairNetCeiling'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        vehicleClassCeiling_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: 'getSelectedInsurerVehicleClassCeiling.action',method:'POST'}),
            reader:vehicleClassCeiling_JsonReader
        });

        vehicleClassCeiling_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:vehicleClass_recordOnclick },
            store: vehicleClassCeiling_gridviewData,
            loadMask: true,
            columns: [
                {header: "Vehicle Class", width: 180, dataIndex: 'vehicleClassName', sortable: true, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>"+value+"</a>" }},
                {header: "Hire Net Ceiling", width: 150, dataIndex: 'hireNetCeiling', sortable: true, resizable: true},
                {header: "Repair Net Ceiling", width: 150, dataIndex: 'repairNetCeiling', sortable: true, resizable: true},
                {header: "", width: 90, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>Remove</a>"}}
            ],
            renderTo:'vehicleClassCeiling_gridviewGrid',
                width:590,
                autoHeight:true,
                enableHdMenu:false
            });

            onVehicleClassPageRefresh();
       
    });

    function onVehicleClassPageRefresh(){
        showVehicleClassDropDown();
        vehicleClassCeiling_loadGridViewList();
        refreshForm();
    }

    function refreshForm(){
        $("#vehicleClassId").val("");
        $("#hireNetCeiling").val("0.00");
        $("#repairNetCeiling").val("0.00");
    }

    function vehicleClassCeiling_loadGridViewList(){

        vehicleClassCeiling_gridviewData.load(
        {
            params:
            {
                insurerId:selectOrgId
            }
        });
    }

    function vehicleClass_recordOnclick(grid, rowIndex, columnIndex, e){
        
        var gridView = vehicleClassCeiling_gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");

        if(columnIndex==3){
            
            $.ajax({
               url: "doRemoveVehicleClassCeilingMapping.action?vehicleClassCeilingId="+gridViewId,
               success: onVehicleClassPageRefresh
            });
            
        }else if(columnIndex==0){
            showEditVehicleClassCeiling(gridView);
        }
    }

    function showEditVehicleClassCeiling(gridView){
        
        vehicleCeilingEditSelectionDlg.show();

        $("#editVehicleClassId").val(gridView.get("id"));
        $("#editVehicleClassName").html(gridView.get("vehicleClassName"));
        $("#editHireNetCeiling").val(gridView.get("hireNetCeiling"));
        $("#editRepairNetCeiling").val(gridView.get("repairNetCeiling"));

    }
    
    function showVehicleClassDropDown() {
        $("#vehicleClassDropDownDiv").load("VehicleClassDropDownAction.action?orgId=" + selectOrgId+uniqeToken());
        $("#breBandId").val(-1);
    }

    function doVehicleClassCeilingSubmit(){

         if(doVehicleClassCeilingFormValidation().form()){

                $(".form-container").block();

                var op = {
                    success:onVehicleClassCeilingSubmitResponseReceived,
                    timeout: 3000,
                    error: onVehicleClassCeilingSubmitError
                };

                $("#formVehicleClassCeilingDetail").ajaxSubmit(op);
                    
         }

    }

    function doVehicleClassCeilingFormValidation(){

        var validateFlag = $("#formVehicleClassCeilingDetail").validate(
        {
           errorLabelContainer: "#CDVehicleClassCeilingMessageBox",
           rules: {
                vehicleClassId:{min:0 },
                hireNetCeiling:{ required:true, number:true, min:0.01 },
                repairNetCeiling:{ required:true, number:true, min:0.01 }
           },
           messages: {
             vehicleClassId: {min:"You must select a 'Vehicle Class'" },
             hireNetCeiling: { required:"You must supply a value for 'Hire Net Ceiling'", number:"'Hire Net Ceiling' must be numeric", min:"'Hire Net Ceiling' cannot be less than zero" },
             repairNetCeiling: { required:"You must supply a value for 'Repair Net Ceiling'", number:"'Repair Net Ceiling' must be numeric", min:"'Repair Net Ceiling' cannot be less than zero" }
           },
           submitHandler: function(form) {}
        });

        return validateFlag;
    }
    
    function onVehicleClassCeilingSubmitResponseReceived(){
        onVehicleClassPageRefresh();
        $(".form-container").unblock();
    }

    function onVehicleClassCeilingSubmitError(){
        alert("Unexpected Error has been encountered, please try again.");
        $(".form-container").unblock();
    }

</script>

<div>
    <div id="VehicleClassCeilingorganisationGird">
        <div class="gridViewHeader">
           <table width="100%">
            <tr>
                <td>
                    <fieldset class="x-fieldset"><legend>Add New Vehicle Class Ceiling</legend>
                        <div class="form-container">
                            <form id="formVehicleClassCeilingDetail" action="vehicleClassCeilingDetail.action" class="XXentity-form" onsubmit="return true;">

                                <input type="hidden" name="selectOrgId" value='<s:property value="selectOrgId"/>'>

                            <div id="vehicleClassDropDownDiv" class="chox-form-item"></div>
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Hire Net Ceiling</label>
                                <input id="hireNetCeiling" name="hireNetCeiling" value="<s:property value="hireNetCeiling" />"/>
                            </div>
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Repair Net Ceiling</label>
                                <input id="repairNetCeiling" name="repairNetCeiling" value="<s:property value="repairNetCeiling" />"/>
                            </div>
                            <div class="chox-form-button">
                                <input type="button" value='Add' onclick="javascript: return doVehicleClassCeilingSubmit();"/>
                            </div>
                            <div id="CDVehicleClassCeilingMessageBox" class="errorBox"></div>
                            </form>
                        </div>
                    </fieldset>

                </td>
            </tr>

            <tr>
                <td valign="top">
                <div class="girdViewLabel">Vehicle Class Ceiling</div>
                <div id="vehicleClassCeiling_gridviewGrid" style="height:360px; overflow:auto;"></div>
                </td>
            </tr>
            </table>
        </div>
    </div>

                <div id="vccSelectionDlgHolder">
                    
                    <div id="vccSelectionPanel">

                        <div class="form-container" style="height:300px">
                            
                            <form id="editVehicleClassCeilingDetail" class="XXentity-form" action="">
                                <input id="editVehicleClassId" name="editVehicleClassId" type="hidden"/>
                                <div class="chox-form-item">
                                    <label class="chox-form-pop">Vehicle Class</label>
                                    <label id="editVehicleClassName"></label>
                                </div>
                                <div class="chox-form-item">
                                    <label class="chox-form-pop">Hire Net Ceiling</label>
                                    <input id="editHireNetCeiling" name="editHireNetCeiling"/>
                                </div>
                                <div class="chox-form-item">
                                    <label class="chox-form-pop">Repair Net Ceiling</label>
                                    <input id="editRepairNetCeiling" name="editRepairNetCeiling"/>
                                </div>
                            </form>

                        </div>

                    </div>
                                
                </div>


</div>