<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<script type="text/javascript">

    var selectOrgId = <s:property value="selectOrgId" />;
    var vehicleClassCellingJsonReader;
    var vehicleClassCelling_gridviewData;
    var vehicleClassCelling_gridviewGrid;
    var vehicleCeilingEditSelectionDlg;
    
    $(document).ready(function(){
        doVehicleClassCellingFormValidation();
    });

    Ext.onReady(function(){

        if(!vehicleCeilingEditSelectionDlg)
        {
            vehicleCeilingEditSelectionDlg =  new Ext.Window({
                applyTo:'vccSelectionDlgHolder',
                width:400,
                height:200,
                modal: true,
                closeAction:'hide',
                plain: false,
                title: 'Edit Vehicle Class Ceiling',
                resizable : false,
                items: new Ext.Panel({
                    applyTo: 'vccSelectionPanel'
                }),
                buttons: [{
                        text:'Ok',
                        
                        handler:function(){

                            $("form#editVehicleClassCellingDetail").validate(
                            {
                                errorLabelContainer: "#HMmessageBox",
                                rules: {
                                    editVehicleClassId:{min:0 },
                                    editHireNetCelling:{ required:true, number:true, min:0.01 },
                                    editRepairNetCelling:{ required:true, number:true, min:0.01 }
                                },
                                messages: {
                                     editVehicleClassId: {min:"You must select a 'Vehicle Class'" },
                                     editHireNetCelling: { required:"You must supply a value for 'Hire Net Ceiling'", number:"'Hire Net Ceiling' must be numeric", min:"'Hire Net Ceiling' cannot be less than zero" },
                                     editRepairNetCelling: { required:"You must supply a value for 'Repair Net Ceiling'", number:"'Repair Net Ceiling' must be numeric", min:"'Repair Net Ceiling' cannot be less than zero" }
                                }
                            });

                            if($('form#editVehicleClassCellingDetail').valid()){

                                var parameter = "?vehicleClassCellingId="+$("#editVehicleClassId").val();
                                parameter += "&hireNetCeiling="+$("#editHireNetCelling").val();
                                parameter += "&repairNetCeiling="+$("#editRepairNetCelling").val();

                                $.ajax({
                                   url: "editVehicleClassCellingDetail.action"+parameter,
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
            listeners:  {cellclick:vehicleClass_recordOnclick },
            store: vehicleClassCelling_gridviewData,
            loadMask: true,
            columns: [
                {header: "Vehicle Class", width: 180, dataIndex: 'vehicleClassName', sortable: true, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='highlightItem'>"+value+"</a>" }},
                {header: "Hire Net Ceiling", width: 150, dataIndex: 'hireNetCelling', sortable: true, resizable: true},
                {header: "Repair Net Ceiling", width: 150, dataIndex: 'repairNetCelling', sortable: true, resizable: true},
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

    function vehicleClass_recordOnclick(grid, rowIndex, columnIndex, e){
        
        var gridView = vehicleClassCelling_gridviewGrid.getStore().getAt(rowIndex);
        var gridViewId = gridView.get("id");

        if(columnIndex==3){
            
            $.ajax({
               url: "doRemoveVehicleClassCellingMapping.action?vehicleClassCellingId="+gridViewId,
               success: onVehicleClassPageRefresh
            });
            
        }else if(columnIndex==0){
            showEditVehicleClassCeiling(gridView);
        }
    }

    function showEditVehicleClassCeiling(gridView){
        
        vehicleCeilingEditSelectionDlg.show(this);

        $("#editVehicleClassId").val(gridView.get("id"));
        $("#editVehicleClassName").html(gridView.get("vehicleClassName"));
        $("#editHireNetCelling").val(gridView.get("hireNetCelling"));
        $("#editRepairNetCelling").val(gridView.get("repairNetCelling"));

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
             hireNetCelling: { required:"You must supply a value for 'Hire Net Ceiling'", number:"'Hire Net Ceiling' must be numeric", min:"'Hire Net Ceiling' cannot be less than zero" },
             repairNetCelling: { required:"You must supply a value for 'Repair Net Ceiling'", number:"'Repair Net Ceiling' must be numeric", min:"'Repair Net Ceiling' cannot be less than zero" }
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
    <div id="VehicleClassCellingorganisationGird">
        <div class="gridViewHeader">
           <table width="100%">
            <tr>
                <td>
                    <fieldset class="x-fieldset"><legend>Add New Vehicle Class Ceiling</legend>
                        <div class="form-container">
                            <form id="formVehicleClassCellingDetail" action="vehicleClassCellingDetail.action" class="XXentity-form" onsubmit="return true;">

                                <input type="hidden" name="selectOrgId" value='<s:property value="selectOrgId"/>'>

                            <div id="vehicleClassDropDownDiv" class="chox-form-item"></div>
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Hire Net Ceiling</label>
                                <input id="hireNetCelling" name="hireNetCelling" value="<s:property value="hireNetCelling" />"/>
                            </div>
                            <div class="chox-form-item">
                                <label class="chox-form-std-label">Repair Net Ceiling</label>
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
                <div class="girdViewLabel">Vehicle Class Ceiling</div>
                <div id="vehicleClassCelling_gridviewGrid" style="height:360px; overflow:auto;"></div>
                </td>
            </tr>
            </table>
        </div>
    </div>

                <div id="vccSelectionDlgHolder" class="x-hidden">
                    
                    <div id="vccSelectionPanel">

                        <div class="form-container" style="height:300px">
                            
                            <form id="editVehicleClassCellingDetail" class="XXentity-form" action="">
                                <input id="editVehicleClassId" name="editVehicleClassId" type="hidden"/>
                                <div class="chox-form-item">
                                    <label class="chox-form-pop">Vehicle Class</label>
                                    <label id="editVehicleClassName"></label>
                                </div>
                                <div class="chox-form-item">
                                    <label class="chox-form-pop">Hire Net Ceiling</label>
                                    <input id="editHireNetCelling" name="editHireNetCelling"/>
                                </div>
                                <div class="chox-form-item">
                                    <label class="chox-form-pop">Repair Net Ceiling</label>
                                    <input id="editRepairNetCelling" name="editRepairNetCelling"/>
                                </div>
                            </form>

                        </div>

                    </div>
                                
                </div>


</div>