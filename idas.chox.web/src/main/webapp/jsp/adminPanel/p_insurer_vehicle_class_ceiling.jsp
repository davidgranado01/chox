<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var vehicleClassCeilingJsonReader;
    var vehicleClassCeiling_gridviewData;
    var vehicleClassCeiling_gridviewGrid;
    var vehicleCeilingEditSelectionDlg;

    $(function(){

        // ADD FORM
        var form = $("form#formVehicleClassCeilingDetail");

        form.validate(
        {
            errorLabelContainer: "#CDVehicleClassCeilingMessageBox",
            rules: {
                vehicleClassId:{min:0 },
                hireNetCeiling:{ required:true, number:true, min:0 },
                repairNetCeiling:{ required:true, number:true, min:0 }
            },
            messages: {
                vehicleClassId: {min:"You must select a 'Vehicle Class'" },
                hireNetCeiling: { required:"You must supply a value for 'Hire Net Ceiling'", number:"'Hire Net Ceiling' must be numeric", min:"'Hire Net Ceiling' cannot be less than zero" },
                repairNetCeiling: { required:"You must supply a value for 'Repair Net Ceiling'", number:"'Repair Net Ceiling' must be numeric", min:"'Repair Net Ceiling' cannot be less than zero" }
            }
        });

        ui.ajaxForm(form,function(responseText, statusText){

            var response = eval('(' + responseText.trim() + ')');

            if(response)
            {
                if(response.isValid){

                    if(response.resultType && response.resultType === 'New')
                    {
                        onVehicleClassPageRefresh();
                    }
                } else {
                    Ext.Msg.show({
                        title: 'Error',
                        msg:response.errors,
                        icon:Ext.Msg.ERROR,
                        buttons:Ext.Msg.OK,
                        width : 400
                    });
                    onVehicleClassPageRefresh();
                }
            }
        });

        var editForm = $("form#editVehicleClassCeilingDetail");
        editForm.validate(
        {
            errorLabelContainer: "#HMmessageBox",
            rules: {
                vehicleClassCeilingId:{min:0 },
                hireNetCeiling:{ required:true, number:true, min:0 },
                repairNetCeiling:{ required:true, number:true, min:0 }
            },
            messages: {
                vehicleClassCeilingId: {min:"You must select a 'Vehicle Class'" },
                hireNetCeiling: { required:"You must supply a value for 'Hire Net Ceiling'", number:"'Hire Net Ceiling' must be numeric", min:"'Hire Net Ceiling' cannot be less than zero" },
                repairNetCeiling: { required:"You must supply a value for 'Repair Net Ceiling'", number:"'Repair Net Ceiling' must be numeric", min:"'Repair Net Ceiling' cannot be less than zero" }
            }
        });
        ui.ajaxForm($("form#editVehicleClassCeilingDetail"), onVehicleClassPageRefresh);


        if(!vehicleCeilingEditSelectionDlg || vehicleCeilingEditSelectionDlg===null)
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
                        text:'Ok', handler: function(){

                        if($("form#editVehicleClassCeilingDetail").valid()){
                            var op = {
                                success: doVehicleClassCeilingPageRefresh,
                                timeout: 3000,
                                error: ui.onSubmitError
                            };

                            $("form#editVehicleClassCeilingDetail").ajaxSubmit(op);
                        }}
                    },{
                        text: 'Close', handler: function(){
                            vehicleCeilingEditSelectionDlg.hide();
                        }
                    }]
            });

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
            ({url: '<%= request.getContextPath()%>/prv/p/getSelectedInsurerVehicleClassCeiling.action', method:'POST'}),
            reader:vehicleClassCeiling_JsonReader
        });

        vehicleClassCeiling_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:vehicleClass_recordOnclickRemoveVehicleClassCeiling},
            store: vehicleClassCeiling_gridviewData,
            renderTo:'vehicleClassCeiling_gridviewGrid',
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Vehicle Class", width: 200, dataIndex: 'vehicleClassName', sortable: true, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>"+value+"</a>"; }},
                {header: "Hire Net Ceiling", width: 160, dataIndex: 'hireNetCeiling', sortable: true, resizable: true, renderer: function(value,p,r) {
                    return '£' + value.toFixed(2);
                }},
                {header: "Repair Net Ceiling", width: 160, dataIndex: 'repairNetCeiling', sortable: true, resizable: true, renderer: function(value,p,r) {
                    return '£' + value.toFixed(2);
                }},
                {header: "", width: 80, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>Remove</a>";}}
            ],
            height:295,
            width: 760
        });

        onVehicleClassPageRefresh();

    });
    
    function doVehicleClassCeilingPageRefresh(){

        vehicleCeilingEditSelectionDlg.hide();
        vehicleCeilingEditSelectionDlg = null;

        var tabIndex = 3;
        var target = "#admin_param_panel";
        var url = "<%= request.getContextPath()%>/prv/p/loadAdminPanel.action";
        var param = {"adminPanelName":"InsurerPanelMgmt", "tabIndex":tabIndex};

        <s:if test="isChoxAdmin">
            tabIndex = 6;
            var url = "<%= request.getContextPath()%>/prv/p/updateInsurerDetailPanel.action";
            var param = {"objectId":<s:property value="insurerId" />,"tabIndex":tabIndex};
        </s:if>

        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
            <s:if test="isChoxAdmin">
                insAdminTabs.activate(tabIndex); 
            </s:if><s:else >
                InsurerMainPanelTabs.activate(tabIndex);
            </s:else>
        });

    }

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
        vehicleClassCeiling_gridviewData.load({params:{insurerId:<s:property value="insurerId" />}});
    }

    function vehicleClass_recordOnclickRemoveVehicleClassCeiling(grid, rowIndex, columnIndex, e){

        var gridView = vehicleClassCeiling_gridviewGrid.getStore().getAt(rowIndex);

        if(columnIndex===3){
            var vehicleClassCeilingId = gridView.get("id");
            var url = "<%= request.getContextPath()%>/prv/p/doRemoveVehicleClassCeilingMapping.action";
            var param = {"vehicleClassCeilingId":vehicleClassCeilingId};
            ajax.loadHtml2(url, param, onVehicleClassPageRefresh);

        }else if(columnIndex===0){
            showEditVehicleClassCeiling(gridView);
        }

    }

    function showEditVehicleClassCeiling(gridView){
        vehicleCeilingEditSelectionDlg.show();

        $("form#editVehicleClassCeilingDetail input[name$='vehicleClassCeilingId']").val(gridView.get("id"));
        $("form#editVehicleClassCeilingDetail label#editVehicleClassName").html(gridView.get("vehicleClassName"));
        $("form#editVehicleClassCeilingDetail input[name$='hireNetCeiling']").val(gridView.get("hireNetCeiling").toFixed(2));
        $("form#editVehicleClassCeilingDetail input[name$='repairNetCeiling']").val(gridView.get("repairNetCeiling").toFixed(2));
    }

    function showVehicleClassDropDown() {
        var target = "#vehicleClassDropDownDiv";
        var url = "<%= request.getContextPath()%>/prv/p/VehicleClassDropDownAction.action";
        var param = {"insurerId":<s:property value="insurerId" />};
        ajax.loadHtml2(url,param,function(data){
            $(target).html(data);
        });
    }


</script>
<div class="sub-admin-tab-css">
    <div class="status-info">
        The maximum ceiling limits for both the Hire Net and Repair Net for the specific vehicle classes is managed here.  If a CHO submits an invoice where the Hire Net or Repair Net value(s) exceed the values held in the below table for the specific vehicle class in question, (non-fault vehicle's vehicle class) then the rule will fail.
    </div>

    <div id="VehicleClassCeilingorganisationGird">
        <div class="grid-view-header">
            <table width="100%">
                <tr>
                    <td>
                        <div class="admin-bre-band-detail-section">
                            <div class="section-name">Vehicle Class Ceiling</div>
                            <div class="form-container">
                                <form id="formVehicleClassCeilingDetail" name="formVehicleClassCeilingDetail" action="<%= request.getContextPath()%>/prv/p/addNewVehicleClassCeiling.action" class="XXentity-form" method="POST">
                                    <input id="insurerId" name="insurerId" type="hidden" value="<s:property value="insurerId"/>"/>
                                    <div id="vehicleClassDropDownDiv" class="chox-form-item"></div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Hire Net Ceiling<span class="mandatory">*</span></label>
                                        <input id="hireNetCeiling" name="hireNetCeiling" value="<s:property value="hireNetCeiling" />"/>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Repair Net Ceiling<span class="mandatory">*</span></label>
                                        <input id="repairNetCeiling" name="repairNetCeiling" value="<s:property value="repairNetCeiling" />"/>
                                    </div>
                                    <div class="chox-form-button">
                                        <input type="submit" value="Add New Vehicle Class"/>
                                    </div>
                                    <div class="chox-form-submit-result"></div>
                                    <div id="CDVehicleClassCeilingMessageBox" class="action-error-msg"></div>
    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    <!--s:token/-->
                                </form>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
        <div id="vehicleClassCeiling_gridviewGrid"/>
    </div>

    <div id="vccSelectionDlgHolder" class="x-hidden">
        <div id="vccSelectionPanel">
            <div class="form-container" style="height:300px; padding-bottom:30px">
                <form id="editVehicleClassCeilingDetail" name="editVehicleClassCeilingDetail" class="XXentity-form" action="<%= request.getContextPath()%>/prv/p/editVehicleClassCeilingDetail.action" method="post">
                    <input id="vehicleClassCeilingId" name="vehicleClassCeilingId" type="hidden"/>
                    <div class="chox-form-item">
                        <label class="chox-form-pop">Vehicle Class</label>
                        <label id="editVehicleClassName"></label>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-pop">Hire Net Ceiling</label>
                        <input type="text" id="hireNetCeiling" name="hireNetCeiling"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-pop">Repair Net Ceiling</label>
                        <input type="text" id="repairNetCeiling" name="repairNetCeiling"/>
                    </div>
    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    <!--s:token/-->
                </form>
            </div>
        </div>
    </div>
</div>