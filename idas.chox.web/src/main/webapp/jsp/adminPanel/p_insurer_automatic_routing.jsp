<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var routing_gridviewJsonReader;
    var routing_gridviewDataStore;
    var routing_gridviewGrid;
    var routing_gridviewData;


    var routing_gridviewJsonReaderPrice;
    var routing_gridviewDataStorePrice;
    var routing_gridviewGridPrice;
    var routing_gridviewDataPrice;

    var automaticRoutingEditSelectionDlg;
    var autoRoutingWorkgroupStore;
    var autoRoutingWorkgroupCombo;

    var workEnable = '<s:property value="workgroupEnableFlg"/>';
    var autoRoutingStrategy = '<s:property value="automaticRoutingStrategy"/>';

    var selectedCho_gridviewGrid;
    var availableCho_gridviewGrid;
    var selectedCho_gridviewData;
    var availableCho_gridviewData;
    var selectedCho_gridviewJsonReader;
    var availableCho_gridviewJsonReader;

    Ext.onReady(function(){

        var autoRoutingWorkgroupJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
            {name: 'text'},
            {name: 'value'}
                ]
        });

        autoRoutingWorkgroupStore = new choxDataStore({
<s:if test="automaticRoutingStrategy == 1">
            url: "/prv/p/getAvailableAutoRoutingWorkgroups.action", 
            params: {insurerId : <s:property value="insurerId" />},
</s:if>
<s:else>
            url: "/prv/p/WorkgroupDropDownActionByInsurer2.action", 
            params: {orgId : <s:property value="insurerId" />},
</s:else>
            reader: autoRoutingWorkgroupJsonReader
        });

        autoRoutingWorkgroupCombo = new Ext.form.ComboBox({
            store: autoRoutingWorkgroupStore,
            width: 220,
            renderTo: 'autoRoutingWorkgroupDiv',
            valueField: 'text',
            id: 'autoRoutingWorkgroupComboId',
            displayField: 'value',
            hiddenName:'workgroupId',
            typeAhead: true,
            autoWidth: true,
            mode: 'local',
            triggerAction: 'all',
            emptyText: '--- Please Select ---',
<s:if test="automaticRoutingStrategy == 3">
                listeners: {
                    select: function() {
                                choMapping_loadGridViewList();
                            }
                },
</s:if>
            forceSelection: true
        });
        
        autoRoutingWorkgroupStore.load({ params : {insurerId : <s:property value="insurerId" />}});
        
        $.validator.addMethod("autoRoutingWorkgroupSelection",
            function(value) {
                if(value === "" || value < 1) {
                    return false;
                }
                return true;
            }
        );

    if(autoRoutingStrategy==1){ // By Policy number
            
            var form = $("form#formAutomaticRoutingDetail");
          
            form.validate(
            {
                ignore: [], // This is added to include validation on hidden fields. Extjs combo rendered as hidden field.
                errorLabelContainer: "#CDAutomaticRoutingMessageBox",
                rules: {
                    workgroupId : {autoRoutingWorkgroupSelection : true},
                    expression : {required:true}
                },
                messages: {
                    workgroupId : {autoRoutingWorkgroupSelection :"You must supply a value for 'Workgroup'"},
                    expression : {required:"You must supply a value for 'Regular Expression'"}
                }
            });




            ui.ajaxForm(form, doAutoRoutingPageRefresh);

        
            routing_gridviewJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'id'},
                    {name:'expression'},
                    {name:'insurerName'},
                    {name:'insurerId'},
                    {name:'workgroupName'},
                    {name:'workgroupId'},
                    {name:'createdBy'},
                    {name:'createdDate'}
                ]
            });

            routing_gridviewData = new choxDataStore({
                url: '/prv/p/getInsurerAutomaticRouting.action',
                reader:routing_gridviewJsonReader
            });

            routing_gridviewGrid = new Ext.grid.GridPanel({
                listeners:  {cellclick:routing_recordOnclick },
                store: routing_gridviewData,
                renderTo:'automaticRouting_gridviewGrid',
                enableHdMenu:false,
                enableColumnMove: false,
                layout:'fit',
                loadMask : true,
                viewConfig:{forceFit:true},
                columns: [
                    {header: "Insurer", width: 100, dataIndex: 'insurerName', sortable: true, resizable: true},
                    {header: "Workgroup", width: 100, dataIndex: 'workgroupName', sortable: true, resizable: true},
                    {header: "Regular Expression", width: 180, dataIndex: 'expression', sortable: true, resizable: true, renderer:function(value,p,r){
                            return "<a href='#' class='high-light-item'>" + value + "</a>";}},
                    {header: "Action", width: 80, dataIndex: 'Remove', sortable: true, resizable: true, renderer:function(value,p,r){
                            return "<a href='#' class='high-light-item'>Remove</a>";}},
                    {header: "Created By", width: 100, dataIndex: 'createdBy', sortable: true, resizable: true},
                    {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: true, resizable: true}
                ],
                height:340,
                width: 760
            });

  

            if(!automaticRoutingEditSelectionDlg || automaticRoutingEditSelectionDlg===null)
            {
                automaticRoutingEditSelectionDlg =  new Ext.Window({
                    applyTo:'autoRoutingSelectionDlgHolder',
                    width:400,
                    height:200,
                    layout:'fit',
                    modal:true,
                    closeAction:'hide',
                    plain: false,
                    title: 'Edit Automatic Routing Detail',
                    resizable : false,
                    items: new Ext.Panel({
                        applyTo: 'autoRoutingSelectionPanel'
                    }),
                    buttons: [{
                            text:'Ok', handler: function(){

                                var op = {
                                    success: doAutoRoutingPageRefresh,
                                    timeout: 3000,
                                    error: ui.onSubmitError
                                };
                                $("form#editAutoRoutingDetail").validate(
                                    {
                                        ignore: [], // This is added to include validation on hidden fields. Extjs combo rendered as hidden field.
                                        errorLabelContainer: "#CDAutomaticRoutingEditScreenMessageBox",
                                        rules: {
                                            expression : {required:true}
                                        },
                                        messages: {
                                            expression : {required:"You must supply a value for 'Regular Expression'"}
                                        }
                                    });
                                    if ($("form#editAutoRoutingDetail").valid()) {
//                                        $("form#editAutoRoutingDetail").ajaxSubmit(op);
                                        choxJqueryAjaxSubmit($("form#editAutoRoutingDetail"), op);
                                    }
                            }
                        },{
                            text: 'Close', handler: function(){
                                $("div#CDAutomaticRoutingEditScreenMessageBox").empty();
                                automaticRoutingEditSelectionDlg.hide();
                            }
                        }]
                });

            }


  
            routing_loadGridViewList();
        }
        else if(autoRoutingStrategy==2){ // By Vehicle Class Price


            var form = $("form#formAutomaticRoutingDetail");
            form.validate(
            {   
                ignore: [],// This is added to include validation on hidden fields. Extjs combo rendered as hidden field.
                errorLabelContainer: "#CDAutomaticRoutingMessageBox",
                rules: {
                    workgroupId :{autoRoutingWorkgroupSelection : true},
                    price:{required:true, number:true, max:99999999.99}
                },
                messages: {
                    workgroupId :{autoRoutingWorkgroupSelection :"You must supply a value for 'Workgroup'"},
                    price:{required:"You must supply a value for 'Price'"}
                }
            });




            ui.ajaxForm(form, doAutoRoutingPageRefreshPrice);


            routing_gridviewJsonReaderPrice = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'id'},
                    {name:'price'},
                    {name:'insurerName'},
                    {name:'insurerId'},
                    {name:'workgroupName'},
                    {name:'workgroupId'},
                    {name:'createdBy'},
                    {name:'createdDate'}
                ]
            });

            routing_gridviewDataPrice = new choxDataStore({
                url: '/prv/p/getInsurerAutomaticRoutingByPrice.action',
                reader:routing_gridviewJsonReaderPrice
            });

            routing_gridviewGridPrice = new Ext.grid.GridPanel({
                listeners:  {cellclick:routing_recordOnclickPrice },
                store: routing_gridviewDataPrice,
                renderTo:'automaticRouting_gridviewGridPrice',
                enableHdMenu:false,
                enableColumnMove: false,
                layout:'fit',
                loadMask : true,
                viewConfig:{forceFit:true},
                columns: [
                    {header: "Insurer", width: 100, dataIndex: 'insurerName', sortable: true, resizable: true},
                    {header: "Workgroup", width: 100, dataIndex: 'workgroupName', sortable: true, resizable: true},
                    {header: "Vehicle Class Price", width: 180, dataIndex: 'price', sortable: true, resizable: true},
                    {header: "Action", width: 80, dataIndex: 'Remove', sortable: true, resizable: true, renderer:function(value,p,r){
                            return "<a href='#' class='high-light-item'>Remove</a>";}},
                    {header: "Created By", width: 100, dataIndex: 'createdBy', sortable: true, resizable: true},
                    {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: true, resizable: true}
                ],
                height:320,
                width: 760
            });


            routing_loadGridViewListPrice();
        
        }
        else if(autoRoutingStrategy==3){ // By CHO Assignment
            availableCho_GridviewJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields: [
                    {name:'id'},
                    {name:'name'},
                    {name:'status'},
                    {name:'statusDesc'},
                    {name:'createdBy'},
                    {name:'createdDate'}
                ]
            });

            selectedCho_gridviewJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields: [
                    {name:'id'},
                    {name:'name'},
                    {name:'status'},
                    {name:'statusDesc'},
                    {name:'createdBy'},
                    {name:'createdDate'}
                ]
            });

            availableCho_gridviewData = new choxDataStore({
                url: '/prv/p/getAvailableChorganisation.action',
                reader:availableCho_GridviewJsonReader
            });

            selectedCho_gridviewData = new choxDataStore({
                url: '/prv/p/getSelectedChorganisation.action',
                reader:selectedCho_gridviewJsonReader
            });

            availableCho_gridviewGrid = new Ext.grid.GridPanel({
                listeners:  {cellclick:cho_recordOnclickAdd },
                store: availableCho_gridviewData,
                renderTo:'cho_available_gridviewGrid',
                enableHdMenu:false,
                enableColumnMove: false,
                layout:'fit',
                viewConfig:{forceFit:true},
                columns: [
                    {header: "Name", width: 190, dataIndex: 'name', sortable: true, resizable: true},
                    {header: "Active", width: 40, dataIndex: 'statusDesc', sortable: true, resizable: true},
                    {header: "", width: 30, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>Add</a>";}}
                ],
                height:380,
                width: 360
            });

            selectedCho_gridviewGrid = new Ext.grid.GridPanel({
                listeners:  {cellclick:cho_recordOnclickRemove },
                store: selectedCho_gridviewData,
                renderTo:'cho_selected_gridviewGrid',
                enableHdMenu:false,
                enableColumnMove: false,
                layout:'fit',
                viewConfig:{forceFit:true},
                columns: [
                    {header: "Name", width: 170, dataIndex: 'name', sortable: true, resizable: true},
                    {header: "Active", width: 40, dataIndex: 'statusDesc', sortable: true, resizable: true},
                    {header: "", width: 50, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>Remove</a>";}}
                ],
                height:380,
                width: 360
            });
        }

       
    });



    // Auto Routing based on Price


    function routing_loadGridViewListPrice(){
        routing_gridviewDataPrice.load({ params : { insurerId:<s:property value="insurerId" /> } });
    }
    
    function routing_recordOnclickPrice(grid, rowIndex, columnIndex, e){
        
        var gridView = routing_gridviewGridPrice.getStore().getAt(rowIndex);

        if(columnIndex===3){
            Ext.MessageBox.confirm('Confirm', 'Are you sure you want to remove this routing?',function(btn){
            if(btn==='yes'){
                var automaticRoutingId = gridView.get("id");
                var url = "/prv/p/deleteAutomaticRoutingDetailByPrice.action";
                var param = {"automaticRoutingId":automaticRoutingId};
                ajax.loadHtml2(url, param, doAutoRoutingPageRefreshPrice);
            }
            });
        }
    }

     function doAutoRoutingPageRefreshPrice(responseText, statusText){
        
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
            } else {    
                
                autoRoutingWorkgroupCombo.clearValue();
                autoRoutingWorkgroupStore.reload({ params : { insurerId:<s:property value="insurerId" /> } });
                if(autoRoutingStrategy==1){
                    $("form#formAutomaticRoutingDetail input[name$='expression']").val('');
                } else if(autoRoutingStrategy==2){
                    $("form#formAutomaticRoutingDetail input[name$='price']").val('');
                }
            }
        }
        routing_loadGridViewListPrice();

    }


    // Auto Routing based on Policy Number

    function routing_loadGridViewList(){
        routing_gridviewData.load({ params : { insurerId:<s:property value="insurerId" /> } });
    }

    function routing_recordOnclick(grid, rowIndex, columnIndex, e){

        var gridView = routing_gridviewGrid.getStore().getAt(rowIndex);

        if(columnIndex===3){
            Ext.MessageBox.confirm('Confirm', 'Are you sure you want to remove this routing?',function(btn){
            if(btn==='yes'){
                var automaticRoutingId = gridView.get("id");
                var url = "/prv/p/deleteAutomaticRoutingDetail.action";
                var param = {"automaticRoutingId":automaticRoutingId};
                ajax.loadHtml2(url, param, doAutoRoutingPageRefresh);
            }
            });
        }else if(columnIndex===2){
            showEditAutomaticRouting(gridView);
        }
    }

    function showEditAutomaticRouting(gridView){
        automaticRoutingEditSelectionDlg.show();
        $("form#editAutoRoutingDetail input[name$='automaticRoutingId']").val(gridView.get("id"));
        $("form#editAutoRoutingDetail label#editWorkgroupName").html(gridView.get("workgroupName"));
        $("form#editAutoRoutingDetail input[name$='expression']").val(gridView.get("expression"));
    }

    function doAutoRoutingPageRefresh(responseText, statusText){

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
            } else {
                if(autoRoutingStrategy==1){
                    autoRoutingWorkgroupCombo.clearValue();
                    autoRoutingWorkgroupStore.reload({ params : { insurerId:<s:property value="insurerId" /> } });
                    $("form#formAutomaticRoutingDetail input[name$='expression']").val('');
                } else if(autoRoutingStrategy==2){
                    autoRoutingWorkgroupCombo.clearValue();
                    autoRoutingWorkgroupStore.reload({ params : { insurerId:<s:property value="insurerId" /> } });
                }
            }
        }
        routing_loadGridViewList();
        if(automaticRoutingEditSelectionDlg || automaticRoutingEditSelectionDlg !== null) {
            automaticRoutingEditSelectionDlg.hide();
        }

    }
    
<s:if test="automaticRoutingStrategy == 3">
    function cho_recordOnclickAdd(grid, rowIndex, columnIndex, e){
        if(autoRoutingWorkgroupCombo.getValue()<=0){
            Ext.MessageBox.alert('', 'Please select a Workgroup');
            return;
        }

        if(columnIndex===2){
            var gridView = availableCho_gridviewGrid.getStore().getAt(rowIndex);
            var gridViewId = gridView.get("id");
            var url = "/prv/p/doAddNewChorganisationAutoRoutingMapping.action";
            var param = {"chorganisationId":gridViewId, "workgroupId":autoRoutingWorkgroupCombo.getValue()};
            ajax.loadHtml2(url, param, afterChoMappingSubmit);
        }
    }

    function cho_recordOnclickRemove(grid, rowIndex, columnIndex, e){
        if(columnIndex===2){
            var gridView = selectedCho_gridviewGrid.getStore().getAt(rowIndex);
            var chorganisationId = gridView.get("id");
            var url = "/prv/p/doRemoveChorganisationAutoRoutingMapping.action";
            var param = {"chorganisationId":chorganisationId, "workgroupId":autoRoutingWorkgroupCombo.getValue()};
            ajax.loadHtml2(url, param, afterChoMappingSubmit);
        }
    }
    
    function afterChoMappingSubmit(responseText, statusText) {
        var response = eval('(' + responseText.trim() + ')');
       
        if(response) {
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
        choMapping_loadGridViewList();
    }
    
    function choMapping_loadGridViewList(){
        availableCho_gridviewData.load({params:{insurerId:<s:property value="insurerId" />}});
        selectedCho_gridviewData.load({params:{insurerId:<s:property value="insurerId" />,workgroupId:autoRoutingWorkgroupCombo.getValue()}});
    }
</s:if>

</script>

    <div class="sub-admin-tab-css">
        <div class="status-info">
            This tab contains the rules for when a claim is uploaded to automatically assign the claim to a Workgroup and therefore avoid the manual routing of claims where the Insurer uses Workgroups.
        </div>
<s:if test="automaticRoutingStrategy == 3">
        <div class="grid-view-header">
          <div class="admin-bre-band-detail-section">
            <table width="100%">
                <tr>
                    <td>
                        <div class="section-name">Automatic Routing By CHO Assignment</div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="chox-form-item" style="padding-bottom: 2px">
                            <label class="chox-form-std-label">Workgroup</label>
                            <div id = "autoRoutingWorkgroupDiv"></div>
                        </div>
                    </td>
                </tr>
            </table>
            <br>
            <table width="100%">
                <tr>
                    <td valign="top">
                        <label class="gird-view-label">Selected Credit Hire Organisations</label>
                        <div id="cho_selected_gridviewGrid"></div>
                    </td>
                    <td valign="top">
                        <label class="gird-view-label">Available Credit Hire Organisations</label>
                        <div id="cho_available_gridviewGrid"></div>
                    </td>
                </tr>
            </table>
          </div>
        </div>
</s:if>


<s:if test="automaticRoutingStrategy == 2">
        <div class="grid-view-header">
            <table width="100%">
                <tr>
                    <td>
                        <div class="admin-bre-band-detail-section">
                            <div class="section-name">Automatic Routing By Vehicle Class Price</div>
                            <div class="form-container">
                                <form id="formAutomaticRoutingDetail" name="formAutomaticRoutingDetail" action="<%= request.getContextPath()%>/prv/p/addNewAutomaticRoutingDetailByPrice.action" class="XXentity-form" method="POST">
                                    <input id="insurerId" name="insurerId" type="hidden" value="<s:property value="insurerId"/>"/>
                                    <div class="chox-form-item" style="padding-bottom: 2px">
                                        <label class="chox-form-std-label">Workgroup</label>
                                        <div id = "autoRoutingWorkgroupDiv"></div>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Vehicle Class Price</label>
                                        <input id="price" name="price" value="<s:property value="price" />"/>
                                    </div>
                                    <div class="chox-form-button">
                                        <input type="submit" value="Add New Price Value"/>
                                    </div>
                                    <div class="chox-form-submit-result"></div>
                                    <div id="CDAutomaticRoutingMessageBox" class="action-error-msg"></div>
                                </form>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
        <div id="automaticRouting_gridviewGridPrice"/>
</s:if>

<s:if test="automaticRoutingStrategy == 1">
        <div class="grid-view-header">
            <table width="100%">
                <tr>
                    <td>
                        <div class="admin-bre-band-detail-section">
                            <div class="section-name">Automatic Routing By Policy Number</div>
                            <div class="form-container">
                                <form id="formAutomaticRoutingDetail" name="formAutomaticRoutingDetail" action="<%= request.getContextPath()%>/prv/p/addNewAutomaticRoutingDetail.action" class="XXentity-form" method="POST">
                                    <input id="insurerId" name="insurerId" type="hidden" value="<s:property value="insurerId"/>"/>
                                    <div class="chox-form-item" style="padding-bottom: 2px">
                                        <label class="chox-form-std-label">Workgroup</label>
                                        <div id="autoRoutingWorkgroupDiv"></div>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Regular Expression</label>
                                        <input id="expression" name="expression" style="width: 220px" value="<s:property value="expression" />"/>
                                    </div>
                                    <div class="chox-form-button">
                                        <input type="submit" value="Add New Regular Expression"/>
                                    </div>
                                    <div class="chox-form-submit-result"></div>
                                    <div id="CDAutomaticRoutingMessageBox" class="action-error-msg"></div>
                                </form>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>

        <div id="automaticRouting_gridviewGrid"/>
    </div>

    <div id="autoRoutingSelectionDlgHolder" class="x-hidden">
        <div id="autoRoutingSelectionPanel">
            <div class="form-container" style="height:300px; padding-bottom:30px">
                <form id="editAutoRoutingDetail" name="editAutoRoutingDetail" class="XXentity-form" action="<%= request.getContextPath()%>/prv/p/editAutomaticRoutingDetail.action" method="post">
                    <input id="automaticRoutingId" name="automaticRoutingId" type="hidden"/>
                    <div class="chox-form-item" style="height:10px;"></div>
                    <div class="chox-form-item">
                        <label class="chox-form-pop">Workgroup</label>
                        <label id="editWorkgroupName"></label>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-pop">Regular Expression</label>
                        <input id="expression" name="expression"/>
                    </div>
                    <div id="CDAutomaticRoutingEditScreenMessageBox" class="action-error-msg"></div>
                </form>
            </div>
        </div>

    </div>
</s:if>
            </div>
