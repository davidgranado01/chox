<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var tasksJsonReader;
    var tasksDataStore;
    var tasksGrid;
    var dateRenderer;
    var hideCompleted = true;
    var showAssignedTasksOnly = true;
    var createNewTaskWindow;
    var visibilityCombo;
    var taskTypeStore;
    var isCHO;
    var createNewTaskForm;
    var start=0;
    var taskPanelRecordPerPage=20;

    var insurerId;
    var supplierId;

    var workgroupCombo;
    var workgroupStore;
    var workgroupId;
    var claimOwnerCombo;
    var claimOwnerStore;
    var supplierClaimOwnerCombo;
    var supplierClaimOwnerStore;
    // below variable will hold selected Workgroup records and reapply to the same combo box when corresponding(insurer) combo box changed.
    var selectedWorkgroupValues;
    // below variable will hold selected Supp. ClaimOwner records and reapply to the same combo box when corresponding(supplier) combo box changed.
    var selectedSuppClaimOwnerValues;
    // below variable will hold selected Ins. ClaimOwner records and reapply to the same combo box when corresponding(Insurer,Workgroup) combo box changed.
    var selectedInsClaimOwnerValues;
    var workgroupComboNumberOfSelectedRecord = 0;
    var claimOwnerComboNumberOfSelectedRecord = 0;
    var supplierClaimOwnerComboNumberOfSelectedRecord = 0;

    Ext.onReady(function(){

        <s:if test="isInsurer" >
        insurerId = '<s:property value="UserOrganisationId"/>'.split(",");
        </s:if>
        <s:elseif test="isCHO" >
        supplierId = '<s:property value="UserOrganisationId"/>'.split(",");
        </s:elseif>
        
        dateRenderer = Ext.util.Format.dateRenderer('d/m/Y');
        // LOAD RECORDS
        tasksJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount', root: 'results', fields:[
                {name:'id'},
                {name:'complete', type: 'boolean'},
                {name:'claimId'},
                {name:'choReference'},
                {name:'insurerOwner'},
                {name:'choOwner'},
                {name:'dueDate', type: 'date',  dateFormat: 'd/m/Y'},
                {name:'type'},
                {name:'description'},
                {name:'createdBy'},
                {name:'completedBy'},
                {name:'createdDate', type: 'date',  dateFormat: 'd/m/Y'},
                {name:'completedDate', type: 'date',  dateFormat: 'd/m/Y'}
            ]
        });

        <s:if test="isChoxAdmin" >
            tasksDataStore = new choxDataStore({
                url: '/prv/p/getTasks.action',
                reader:tasksJsonReader,
                remoteSort: true
            });
        </s:if>
        <s:else >
            tasksDataStore = new choxDataStore({
                url: '/prv/p/getVisibleTasks.action',
                reader:tasksJsonReader,
                remoteSort: true
                ,listeners: {
                             load: function(store, records, options) {
                                 if (store.baseParams.hideCompleted && store.baseParams.showAssignedTasksOnly) {
                                     updateTaskTabCount(store.getTotalCount(), store.reader.jsonData.colorCode);
                                 }
                             }
                         }
            });
        </s:else>

        tasksDataStore.setDefaultSort('dueDate', 'asc');

        // the check column is created using a custom plugin
        Ext.grid.CheckColumn = function(config){
            Ext.apply(this, config);
            if(!this.id){
                this.id = Ext.id();
            }
            this.renderer = this.renderer.createDelegate(this);
        };

        Ext.grid.CheckColumn.prototype = {
            init : function(grid){
                this.grid = grid;
                this.grid.on('render', function(){
                    var view = this.grid.getView();
                    view.mainBody.on('mousedown', this.onMouseDown, this);
                }, this);
            },
            onMouseDown : function(e, t){
                if(t.className && t.className.indexOf('x-grid3-cc-'+this.id) !== -1){
                    e.stopEvent();
                    var index = this.grid.getView().findRowIndex(t);
                    var record = this.grid.store.getAt(index);
                    record.set(this.dataIndex, !record.data[this.dataIndex]);
                }
            },
            renderer : function(v, p, record){
                p.css += ' x-grid3-check-col-td';
                return '<div class="x-grid3-check-col'+(v?'-on':'')+' x-grid3-cc-'+this.id+'"> </div>';
            }
        };

        var checkColumn = new Ext.grid.CheckColumn({
            header: 'Complete?',
            dataIndex: 'complete',
            width: 40,
            sortable: true,
            resizable: false
        });

        var checkBoxSelMod = new Ext.grid.CheckboxSelectionModel({singleSelect : true, header: ' '});

        var taskGridPagingBar = new Ext.PagingToolbar({
            pageSize: taskPanelRecordPerPage,
            store: tasksDataStore,
            displayInfo: true,
            displayMsg: 'Displaying Tasks {0} - {1} of {2}',
            emptyMsg: "No Tasks to display"
            ,plugins: new Ext.ux.ProgressBarPager()
        });

        
        var tbar = new Ext.Toolbar({
            items:[
                     {
                         text:'Mark As Complete',
                         id : 'markAsCompleteButtonId',
                         handler : markAsComplete,
                         disabled : <s:property value="isChoxAdmin" />
                     }
                    ,'-'
                    ,{
                        text:'Show Completed Tasks',
                        id : 'hideCompletedTasksButtonId',
                        enableToggle: true,
                        toggleHandler: function() {
                            toggleComplete(this);
                            if (this.pressed) 
                            {
                                this.setText('Hide Completed Tasks');
                            } else {
                                this.setText('Show Completed Tasks');
                            }
                        },
                        pressed: false
                    }
                    ,'-'
                    ,{
                        text:'Show All Tasks',
                        id : 'assignedTasksOnlyButtonId',
                        enableToggle: true,
                        toggleHandler: function() {
                            toggleShowAssignedTasksOnly(this);
                            if (this.pressed) 
                            {
                                this.setText('Show My Assigned Tasks Only');
                            } else {
                                var supplierClaimOwnerFilterCombo = Ext.getCmp('SupplierClaimOwnerComboId');
                                if (supplierClaimOwnerFilterCombo) {
                                    supplierClaimOwnerFilterCombo.reset();
                                    supplierClaimOwnerFilterCombo.clearValue();
                                }
                                this.setText('Show All Tasks');
                            }
                        },
                        pressed: false
                    }
                    ,'->'
                    ,{
                        text:'Export To Excel',
                        id : 'taskExportToExcelButtonId',
                        handler : doTaskExportExcel
                     }
                 ]
        });
        
        tasksGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:taskOnClick},
            store: tasksDataStore,
            renderTo:'tasksGridId',
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            viewConfig:{forceFit:true},
            selModel : checkBoxSelMod,
            bbar: taskGridPagingBar,
            tbar:tbar,
            loadMask: true,
            columns: [
                checkBoxSelMod,
                {id:'Id', header: "Supplier Ref", width: 75, sortable: true, dataIndex: 'choReference',
                    renderer:function(value,p,r){
                        return '<a href="javascript:loadClaimDetail('+r.data['claimId']+');">' + value + '</a>';}},
                {header: "Due Date", width: 75, dataIndex: 'dueDate', sortable: true, resizable: true, renderer: dateRenderer},
                {header: "Task Type", width: 100, dataIndex: 'type', sortable: true, resizable: true},
                {header: "Description", width: 200, dataIndex: 'description', sortable: true, resizable: true},
<s:if test="isInsurer || isChoxAdmin" >
                {header: "Insurer Claim Owner", width: 120, dataIndex: 'insurerOwner', sortable: true, resizable: true},
</s:if>
<s:else>
                {header: "CHO Claim Owner", width: 120, dataIndex: 'choOwner', sortable: true, resizable: true},
</s:else>
                {header: "Created Date", width: 75, dataIndex: 'createdDate', sortable: true, resizable: true, renderer: dateRenderer},
                {header: "Created By", width: 120, dataIndex: 'createdBy', sortable: true, resizable: true}
            ],
            width:'100%',
            autoHeight:true
        });

        tasksGrid.getView().getRowClass = function(record, index) {
            var today = new Date();
            today.setHours(0, 0, 0, 0);
            var difference = record.data.dueDate - today;
            var days = Math.round(difference/(1000*60*60*24));
            return (record.data.complete ? 'gray-row' : (days > 0 ? 'black-row' : (days < 0 ? 'red-row' : 'orange-row')));
        };

        var taskTypeReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'text'},
                {name:'value'}
            ]
        });

        taskTypeStore = new choxDataStore({
            url: '/prv/p/getTaskTypes.action',
            reader: taskTypeReader
        });

        var taskTypeCombo = new Ext.form.ComboBox({
            store: taskTypeStore,
            displayField: 'value',
            valueField: 'value',
            fieldLabel: 'Task Type',
            hiddenName: 'taskTypeCombo',
            id: 'taskTypeComboId',
            msgTarget : 'qtip',
            triggerAction: 'all',
            selectOnFocus: true,
            mode: 'local',
            editable: false,
            allowBlank: false,
            forceSelection: true,
            emptyText: 'Please select a task type...'
        });

        var visibilityOptions = [
            [1, 'Private'],
            [2, 'Internal'],
            [3, 'External']
        ];

        var paymentMethodData = [
            ['BACS'],
            ['CHAPS'],
            ['Cheque']
        ];

        var paymentMethodStore = new Ext.data.SimpleStore({
            id: 0,
            fields: ['paymentMethodValue'],
            data: paymentMethodData
        });

        isCHO = <s:property value="isCHO" />;
        
        var paymentMethodCombo = new Ext.form.ComboBox({
            store: paymentMethodStore,
            fieldLabel: (isCHO) ? 'Requested Payment Method' : 'Actual Payment Method',
            msgTarget : 'qtip',
            hidden : true,
            valueField: 'paymentMethodValue',
            id: 'paymentMethodComboId',
            hiddenName: 'paymentMethod',
            displayField: 'paymentMethodValue',
            typeAhead: true,
            mode: 'local',
            selectOnFocus: true,
            forceSelection: true,
            triggerAction: 'all',
            editable: false,
            allowBlank: false,
            emptyText: 'Please Select'
        });

        
        taskTypeCombo.on('select', function(box, record, index) {
            var selection = box.getValue();
            if (selection === 'Total Loss Payment') {
                if (!isCHO) {
                     Ext.getCmp('paymentDateId').show();
                     Ext.getCmp('paymentDateId').allowBlank = false;
                }
                Ext.getCmp('paymentMethodComboId').show();
                Ext.getCmp('paymentMethodComboId').allowBlank = false;
                createNewTaskForm.doLayout();
            } else {
                Ext.getCmp('paymentMethodComboId').hide();
                Ext.getCmp('paymentMethodComboId').allowBlank = true;
                if (!isCHO) {
                     Ext.getCmp('paymentDateId').hide();
                     Ext.getCmp('paymentDateId').allowBlank = true;
                }
                createNewTaskForm.doLayout();
            }
        });
        

        var visibilityRoleCombo;
        if (!isCHO) {
            var visibilityRoleReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'id'},
                    {name:'webUserId'},
                    {name:'webUserName'},
                    {name:'webUserroleId'},
                    {name:'webUserroleRole'},
                    {name:'webUserroleName'},
                    {name:'createdBy'},
                    {name:'createdDate'}
                ]
            });

            var visibilityRoleStore = new choxDataStore({
                url: '/prv/p/getAvailableUserRolesForTask.action',
                reader: visibilityRoleReader
            });


            visibilityRoleStore.load({params:{webUserId: <s:property value="authenticatedUser.id" />}}); // initially load with available user roles

            // Create the visibility role combo used for insurer internal tasks only
            visibilityRoleCombo = new Ext.form.ComboBox({
                fieldLabel: 'Visibility Role',
                msgTarget : 'qtip',
                hiddenName: 'visibilityRoleCombo',
                id: 'visibilityRoleComboId',
                hidden : true,
                mode: 'local',
                editable: false,
                allowBlank: false,
                selectOnFocus: true,
                typeAhead: true,
                triggerAction: 'all',
                value: 'ROLE_INS_CH',
                forceSelection: true,
                store: visibilityRoleStore,
                valueField:'webUserroleRole',
                displayField:'webUserroleName'
            });
        }

        visibilityCombo = new Ext.form.ComboBox({
            fieldLabel: 'Visibility',
            hiddenName: 'visibilityCombo',
            msgTarget : 'qtip',
            id: 'visibilityComboId',
            mode: 'local',
            editable: false,
            allowBlank: false,
            selectOnFocus: true,
            typeAhead: true,
            triggerAction: 'all',
            value: '1',
            forceSelection: true,
            store: new Ext.data.ArrayStore({
                id:0,
                fields: [
                    'myId',   //numeric value is the key
                    'myText' //the text value is the value
                ],
                data: visibilityOptions
            }),
            valueField:'myId',
            displayField:'myText',
            listeners: {
                select: { fn:function(combo, value) {
                        //                                        Ext.getCmp('visibilityRoleComboId').clearValue();
                        // If we are an Insurer and visibility is internal, then
                        //      - show the visibility role combo
                        if (!isCHO && this.value == 2) {
                            Ext.getCmp('visibilityRoleComboId').show();
                            Ext.getCmp('visibilityRoleComboId').getEl().up('div.x-form-item').show();
                            createNewTaskForm.doLayout();
                        }
                        else if (!isCHO) { // hide the combo
                            Ext.getCmp('visibilityRoleComboId').hide();
                            Ext.getCmp('visibilityRoleComboId').getEl().up('div.x-form-item').hide();
                            createNewTaskForm.doLayout();
                        }
                        // Note: maybe we should also pass the visibility role?
                        // If so, need to add a listener to the visibilityRole combo
                        // to also update the task list depending on the role selected.
                        taskTypeStore.load({params:{visibility: this.value}});
                    }
                }
            }

        });

        if (!isCHO) {
            createNewTaskForm = new Ext.FormPanel({
                hidden : <s:property value="isChoxAdmin" />,
                monitorValid: true,
                title : '<div style="text-align:center;">Task Management</div>',
                frame:true,
                id: 'createNewTaskFormId',
                renderTo : 'tasksCreateWindow',
                bodyStyle:'padding:5px 5px 0',
                height : 'auto',
                width:'100%',
                items : [{
                        layout: 'column',
                        height : 'auto',
                        width:'100%',
                        items : [{
                                    layout: 'form',
                                    height : 'auto',
                                    labelWidth: 110,
                                    columnWidth:.35,
                                    defaults: {width: 180},
                                    labelAlign: 'right',
                                    items : [
                                                new Ext.form.DateField({
                                                fieldLabel: 'Due Date',
                                                msgTarget : 'qtip',
                                                name: 'dueDate',
                                                id: 'dueDateId',
                                                allowBlank: false,
                                                format: 'd/m/Y',
                                                minValue: new Date()
                                                }),
                                                visibilityCombo,
                                                visibilityRoleCombo,
                                                taskTypeCombo,
                                                paymentMethodCombo,
                                                new Ext.form.DateField({
                                                    fieldLabel: 'Payment Date',
                                                    msgTarget : 'qtip',
                                                    name: 'paymentDate',
                                                    id: 'paymentDateId',
                                                    hidden : true,
                                                    allowBlank: false,
                                                    format: 'd/m/Y'
                                                })
                                            ]
                                }, 
                                {
                                    layout: 'form',
                                    height : 'auto',
                                    labelWidth: 110,
                                    columnWidth:.5,
                                    defaults: {width: 180},
                                    labelAlign: 'right',
                                    items : [
                                                {
                                                    fieldLabel: 'Description',
                                                    msgTarget : 'qtip',
                                                    name: 'description',
                                                    id: 'descriptionId',
                                                    allowBlank: false,
                                                    minLength: 5,
                                                    xtype: 'textarea',
                                                    maxLength: 256,
                                                    maxLengthText: 'maximum of 256 characters',
                                                    minLengthText: 'minimum of 5 characters',
                                                    width: 250
                                                },
                                                {
                                                    fieldLabel: 'Supplier Reference',
                                                    msgTarget : 'qtip',
                                                    xtype: 'textfield',
                                                    name: 'supplierRef',
                                                    id: 'supplierRefId',
                                                    allowBlank: false
                                                }
                                            ]
                                }
                            ]

                }],
                buttonAlign : 'left',
                buttons: [
                    {
                        text: 'Create',
                        formBind: true,
                        handler: function() {
                            var url = "/prv/p/createNewTask.action";
                            var visRole = Ext.getCmp('visibilityRoleComboId').getValue();
                            var description = Ext.getCmp('descriptionId').getValue();
                            var dDate =  dateRenderer(Ext.getCmp('dueDateId').getValue());
                            var pDate =  dateRenderer(Ext.getCmp('paymentDateId').getValue());
                            var pMethod = Ext.getCmp('paymentMethodComboId').getValue();
                            var tType =  Ext.getCmp('taskTypeComboId').getValue();
                            var vis = Ext.getCmp('visibilityComboId').getValue();
                            var choRef = Ext.getCmp('supplierRefId').getValue();

                            var param = {
                                taskDescription: description,
                                dueDate: dDate,
                                taskType: tType,
                                visibility: vis,
                                visibilityRole: visRole,
                                paymentMethod: pMethod,
                                paymentDate: pDate,
                                choReference: choRef
                            };

                            ajax.loadJson2(url, param, function(data){
                                if(data.resultType==='YesNo'){
                                    if(data.result==='yes'){
                                        loadTasks();
                                        createNewTaskForm.form.reset();
                                        Ext.getCmp('paymentMethodComboId').hide();
                                        Ext.getCmp('paymentDateId').hide();
                                        Ext.getCmp('visibilityRoleComboId').hide();
                                        createNewTaskForm.doLayout();
                                    }
                                }else if(data.resultType==='Message'){
                                    Ext.Msg.alert('Error creating new task',data.result);
                                }
                            });
                        }
                    }]
            });
        }
        else {
            createNewTaskForm = new Ext.FormPanel({
                monitorValid: true,
                title : '<div style="text-align:center;">Task Management</div>',
                frame:true,
                renderTo : 'tasksCreateWindow',
                id: 'createNewTaskFormId',
                bodyStyle:'padding:5px 5px 0',
                height : 'auto',
                width:'100%',
                items : [{
                            layout: 'column',
                            height : 'auto',
                            width:'100%',
                            items : [{
                                        layout: 'form',
                                        height : 'auto',
                                        labelWidth: 110,
                                        columnWidth:.35,
                                        defaults: {width: 180},
                                        labelAlign: 'right',
                                        items : [
                                                    new Ext.form.DateField({
                                                        fieldLabel: 'Due Date',
                                                        msgTarget : 'qtip',
                                                        name: 'dueDate',
                                                        id: 'dueDateId',
                                                        allowBlank: false,
                                                        minValue: new Date(),
                                                        format: 'd/m/Y'
                                                    }),
                                                    visibilityCombo,
                                                    taskTypeCombo,
                                                    paymentMethodCombo
                                        ]
                                }, 
                                {
                                        layout: 'form',
                                        height : 'auto',
                                        labelWidth: 110,
                                        columnWidth:.5,
                                        defaults: {width: 180},
                                        labelAlign: 'right',
                                        items : [
                                                   {
                                                        fieldLabel: 'Description',
                                                        msgTarget : 'qtip',
                                                        name: 'description',
                                                        id: 'descriptionId',
                                                        allowBlank: false,
                                                        minLength: 5,
                                                        xtype: 'textarea',
                                                        maxLength: 256,
                                                        maxLengthText: 'maximum of 256 characters',
                                                        minLengthText: 'minimum of 5 characters',
                                                        width: 250
                                                    },
                                                    {
                                                        fieldLabel: 'Supplier Reference',
                                                        xtype: 'textfield',
                                                        msgTarget : 'qtip',
                                                        name: 'supplierRef',
                                                        id: 'supplierRefId',
                                                        allowBlank: false
                                                    } 
                                        ]
                                }
                            ]
                }],
                buttonAlign : 'left',
                buttons: [
                    {
                        text: 'Create',
                        formBind: true,
                        handler: function() {
                            var url = "/prv/p/createNewTask.action";
                            var description = Ext.getCmp('descriptionId').getValue();
                            var dDate =  dateRenderer(Ext.getCmp('dueDateId').getValue());
                            var tType =  Ext.getCmp('taskTypeComboId').getValue();
                            var pMethod = Ext.getCmp('paymentMethodComboId').getValue();
                            var vis = Ext.getCmp('visibilityComboId').getValue();
                            var choRef = Ext.getCmp('supplierRefId').getValue();

                            var param = {
                                taskDescription: description,
                                dueDate: dDate,
                                taskType: tType,
                                paymentMethod: pMethod,
                                visibility: vis,
                                choReference: choRef
                            };

                            ajax.loadJson2(url, param, function(data){
                                if(data.resultType==='YesNo'){
                                    if(data.result==='yes'){
                                        loadTasks();
                                        createNewTaskForm.form.reset();
                                        Ext.getCmp('paymentMethodComboId').hide();
                                        createNewTaskForm.doLayout();
                                    }
                                }else if(data.resultType==='Message'){
                                    Ext.Msg.alert('Error creating new task',data.result);
                                }
                            });
                        }
                    }]
            });

        }

        // Add Workgroup drop-down menu
        var workgroupJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                    {name:'text'},
                    {name:'value'}
                ]
        });

        workgroupStore = new choxDataStore({
            url : "/prv/p/WorkgroupDropDownActionByInsurer2.action",
            params : {"orgId": insurerId},
            reader : workgroupJsonReader
            ,listeners: {load: function() {/*this.insert(0, new Ext.data.Record(defaultDropdownValue));*/
                    if(selectedWorkgroupValues && workgroupCombo) {workgroupCombo.reset();workgroupCombo.setValue(selectedWorkgroupValues);}
                }}
        });

        workgroupCombo = new Ext.ux.form.SuperBoxSelect({
                store : workgroupStore,
                width: 250,
                fieldLabel: 'Workgroup',
                disabled : !((!<s:property value="isInsurer"/>) || (<s:property value="isInsurer"/> && <s:property value="insurerIsWorkgroupEnabled"/>)),
            hidden : !((!<s:property value="isInsurer"/>) || (<s:property value="isInsurer"/> && <s:property value="insurerIsWorkgroupEnabled"/>)),
        valueField : 'text', id : 'workgroupComboId',
            displayField :'value',
            typeAhead : true,
            mode : 'local',
            triggerAction : 'all',
            emptyText: '--- ALL ---',
            removeValuesFromStore : false,
            selectOnFocus : true,
            forceSelection : true,
            listeners: {
            blur: function () {
                if(this.getValue() === "" ) {
                }
            },
            specialkey:function (el, e) {
                if(e.keyCode === e.ENTER) {
                    searchClaim(true);
                }
            },
            afterrender : function(){
                // Store not loaded yet? Set value when it *is* loaded.
                this.store.load({
                    params : {"orgId": insurerId},
                    callback: function() {
                        if ('<s:property value="workgroupIdsAsString" />') {
                            workgroupCombo.setValue('<s:property value="workgroupIdsAsString" />');
                            workgroupComboNumberOfSelectedRecord = '<s:property value="workgroupIdsAsString" />'.split(',').length;
                            // doLayoutSearchPanel();
                        }
                    }
                });
                workgroupId = '<s:property value="workgroupIdsAsString" />'.split(",");
            },
            select : function(){
                workgroupComboNumberOfSelectedRecord ++;
                doSearchWorkgroupOnChange();
                // doLayoutSearchPanel();
            },
            removeitem : function() {
                if (!this.getValue() && workgroupComboNumberOfSelectedRecord >= 1) {
                    workgroupComboNumberOfSelectedRecord = 0;
                    this.reset();
                    this.clearValue();
                    doSearchWorkgroupOnChange();
                } else if (workgroupComboNumberOfSelectedRecord >= 1) {
                    workgroupComboNumberOfSelectedRecord --;
                    doSearchWorkgroupOnChange();
                }
                // doLayoutSearchPanel();
            }
        }
    });

        function doSearchWorkgroupOnChange(){
            if (workgroupCombo.getValue() !== null && workgroupCombo.getValue() !== '') {
                workgroupId = workgroupCombo.getValue().split(",");
            }else{
                workgroupId = null;
            }
            doShowClaimHandler(workgroupId, insurerId);
        }

        function doShowClaimHandler(selectedWorkgroupId, selectedInsurerId){

            <s:if test="isInsurer!=true">
            claimOwnerStore.load({ params : {"workgroupId":selectedWorkgroupId,"insurerId":selectedInsurerId}});
            selectedInsClaimOwnerValues = claimOwnerCombo.getValue();
            </s:if>
            <s:elseif test="isInsurer">
            <s:if test="AuthenticatedUser.insurer.claimOwnershipEnable">
            claimOwnerStore.load({ params : {"workgroupId":selectedWorkgroupId,"insurerId":selectedInsurerId}});
            selectedInsClaimOwnerValues = claimOwnerCombo.getValue();
            <s:if test="isCH && selectedWorkgroupId == null" >
            claimOwnerCombo.setValue(<s:property value="AuthenticatedUser.id"/>);
            </s:if>
            </s:if>

            </s:elseif>
        }
        
        
        // Add claim owner combo box
        var claimOwnerReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                    {name:'id'},
                    {name:'name'}
                ]
        });

        claimOwnerStore = new choxDataStore({
            url : "/prv/p/SearchClaimHandlerRoleUserDropDownAction.action",
            params : {"workgroupId": workgroupId,"insurerId": insurerId},
            reader : claimOwnerReader,
            listeners: {load: function() {/*this.insert(0, new Ext.data.Record(claimOwnerdefaultDropdownValue));*/
                    if (selectedInsClaimOwnerValues && claimOwnerCombo) {claimOwnerCombo.reset();claimOwnerCombo.setValue(selectedInsClaimOwnerValues);}
                }}
        });

        claimOwnerCombo = new Ext.ux.form.SuperBoxSelect({
            store : claimOwnerStore,
            width: 250,
            fieldLabel: (<s:property value="isInsurer"/>) ? 'Claim Owner' : 'Insurer\'s Claim Owner',
            disabled : !((!<s:property value="isInsurer"/>) || (<s:property value="isInsurer"/> && <s:property value="insurerIsClaimOwnershipEnabled"/>)),
            hidden : !((!<s:property value="isInsurer"/>) || (<s:property value="isInsurer"/> && <s:property value="insurerIsClaimOwnershipEnabled"/>)),
            valueField : 'id',
            id : 'claimOwnerComboId',
            displayField :'name',
            typeAhead : true,
            mode : 'local',
            triggerAction : 'all',
            emptyText: '--- ALL ---',
            removeValuesFromStore : false,
            selectOnFocus : true,
            forceSelection : true,
            listeners: {
            specialkey:function (el, e) {
                if(e.keyCode === e.ENTER) {
                    searchClaim(true);
                }
            },
            afterrender : function(){
                // Store not loaded yet? Set value when it *is* loaded.
                this.store.load({
                    params : {"workgroupId": workgroupId,"insurerId": insurerId},
                    callback: function() {
                        if ('<s:property value="claimOwnerIdsAsString"/>') {
                            claimOwnerCombo.setValue('<s:property value="claimOwnerIdsAsString"/>');
                            claimOwnerComboNumberOfSelectedRecord = '<s:property value="claimOwnerIdsAsString"/>'.split(',').length;
                            // doLayoutSearchPanel();
                        }
                    }
                });
            },
            select : function(){
                claimOwnerComboNumberOfSelectedRecord ++;
                // doLayoutSearchPanel();
            },
            removeitem : function() {
                if (!this.getValue() && claimOwnerComboNumberOfSelectedRecord >=1) {
                    claimOwnerComboNumberOfSelectedRecord = 0;
                    this.reset();
                    this.clearValue();
                } else if (claimOwnerComboNumberOfSelectedRecord >= 1) {
                    claimOwnerComboNumberOfSelectedRecord --;
                }
                // doLayoutSearchPanel();
            }
        }
    });

        var supplierClaimOwnerReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                    {name:'id'},
                    {name:'name'}
                ]
        });

        supplierClaimOwnerStore = new choxDataStore({
            url : "/prv/p/SearchSupplierClaimOwnerDropDownAction.action",
            params : {"supplierId": supplierId},
            // Don't know if this is neded (search code for this already exists
            // - just uncomment this to add and it should work
            <%--listeners: {load: function() {--%>
            <%--        <s:if test="isCHO" >--%>
            <%--        var notAssigned = new Array();--%>
            <%--        // this next assignment is ugly and should be removed/refactored at some point--%>
            <%--        notAssigned['id'] = '<%= ClaimSearchCriteria.CLAIM_OWNER_NOT_ASSIGNED%>';--%>
            <%--        notAssigned['name'] = 'NOT ASSIGNED';--%>
            <%--        this.insert(0, new Ext.data.Record(notAssigned));--%>

            <%--        </s:if>--%>
            <%--        <s:else >--%>

            <%--        </s:else>--%>
            <%--        if (selectedSuppClaimOwnerValues && supplierClaimOwnerCombo) {supplierClaimOwnerCombo.reset();supplierClaimOwnerCombo.setValue(selectedSuppClaimOwnerValues);}--%>
            <%--    }},--%>
            reader : supplierClaimOwnerReader
        });

        supplierClaimOwnerCombo = new Ext.ux.form.SuperBoxSelect({
            store : supplierClaimOwnerStore,
            width: 250,
            fieldLabel: <s:property value="isCHO"/> ? 'Claim Owner' : 'Supplier Claim Owner',
            disabled : !((!<s:property value="isCHO"/>) || (<s:property value="isCHO"/> && <s:property value="choIsClaimOwnershipEnabled"/>)),
            hidden : !((!<s:property value="isCHO"/>) || (<s:property value="isCHO"/> && <s:property value="choIsClaimOwnershipEnabled"/>)),
        valueField : 'id',
            id : 'SupplierClaimOwnerComboId',
            displayField :'name',
            typeAhead : true,
            mode : 'local',
            triggerAction : 'all',
            emptyText: '--- ALL ---',
            removeValuesFromStore : false,
            selectOnFocus : true,
            forceSelection : true,
            listeners: {
            specialkey:function (el, e) {
                if(e.keyCode === e.ENTER) {
                    applyFilter();
                }
            },
            afterrender : function(){

                // Store not loaded yet? Set value when it *is* loaded.
                this.store.load({
                    params : {"supplierId": supplierId},
                    callback: function() {
                        if ('<s:property value="supplierClaimOwnerIdsAsString"/>') {
                            supplierClaimOwnerCombo.setValue('<s:property value="supplierClaimOwnerIdsAsString"/>');
                            supplierClaimOwnerComboNumberOfSelectedRecord = '<s:property value="supplierClaimOwnerIdsAsString"/>'.split(',').length;
                            // doLayoutSearchPanel();
                        }
                    }
                });
            },
            select : function(select){
                supplierClaimOwnerComboNumberOfSelectedRecord ++;
            },
            removeitem : function(select) {
                if (!this.getValue() && supplierClaimOwnerComboNumberOfSelectedRecord >=1) {
                    supplierClaimOwnerComboNumberOfSelectedRecord = 0;
                    this.reset();
                    this.clearValue();
                } else if (supplierClaimOwnerComboNumberOfSelectedRecord >=1) {
                    supplierClaimOwnerComboNumberOfSelectedRecord --;
                }
            }
        }
    });


    // Create the search and reset buttons
    var filterButton = new Ext.Button({
        text: 'Apply',
        scale : 'small',
        width : 100,
        style: {
            marginBottom: '0px',
            marginTop: '0px'
        },
        handler: applyFilter
    });

    var buttonPanel = new Ext.Panel({
        fbar : [filterButton],
        header : false,
        border: false,
        bodyStyle: 'background-color:transparent;height:0',
        mainBody: false,
        frame : false,
        width : 1140,
        height : 50,
        buttonAlign : 'right'
        ,margins : {top : 0}
    });

        <s:if test="isInsurer && insurerIsWorkgroupEnabled">
            workgroupCombo.render(workgroupComboDiv);
        </s:if>

        <s:if test="isInsurer && insurerIsClaimOwnershipEnabled">
            claimOwnerCombo.render(claimOwnerComboDiv);
        </s:if>

        <s:if test="isInsurer">
            buttonPanel.render(taskFilterButtonInsurerDiv);
        </s:if>

        <s:if test="isCHO && choIsClaimOwnershipEnabled">
            supplierClaimOwnerCombo.render(supplierClaimOwnerComboDiv);
            buttonPanel.render(taskFilterButtonCHODiv);
        </s:if>

        // Add tasks
        taskTypeStore.load({params:{visibility: 1}}); // initially load with 'private' visibility tasks
        loadTasks();
    });


    function taskOnClick(grid, rowIndex, columnIndex){
        if (columnIndex === 4) {
            var task = tasksGrid.getStore().getAt(rowIndex);
            var title="Task";
            var msg = "<b>Due Date</b>: " + dateRenderer(task.get("dueDate"));
            msg += "<br/><b>Insurer Claim Owner</b>: " + task.get("insurerOwner");
            msg += "<br/><b>CHO Claim Owner</b>: " + task.get("choOwner");
            msg += "<br/><b>Created Date</b>: " + dateRenderer(task.get("createdDate"));
            msg += "<br/><b>Created By</b>: " + task.get("createdBy");
            if (task.get('complete')) {
                msg += "<br/><b>Completed Date</b>: " + dateRenderer(task.get("completedDate"));
                msg += "<br/><b>Completed By</b>: " + task.get("completedBy");
            }
            msg += "<br/><b>Supplier Reference</b>: " + task.get("choReference") + "<br/>";
            msg += "<br/><b>Task Type</b>: " + task.get("type");
            msg += "<br/><b>Task Description";

            msg += "</b>: <br/>" + task.get("description");
            propmtMsg(title, msg);
        }
    }

    function loadTasks(){
        tasksDataStore.baseParams =  Ext.apply({hideCompleted : hideCompleted, showAssignedTasksOnly : showAssignedTasksOnly}, getSelectedSupplierClaimOwnerIds(), getSelectedWorkgroupIds(), getSelectedOwnerIds());
        tasksDataStore.load({params:{start:start, limit:taskPanelRecordPerPage}});
        if (!hideCompleted || !showAssignedTasksOnly) {
            updateTaskTab();
        }
    }

    function markAsComplete() {
       
        var selectedRecord = tasksGrid.getSelectionModel().getSelected();
        if (selectedRecord) {
            var selectedRecordId = selectedRecord.get('id');
            var url = "/prv/p/markTaskAsComplete.action";
            var param = {
                selectedTaskId: selectedRecordId
            };

            ajax.loadJson2(url, param, function(data){
                if(data.resultType==='Message'){
                    Ext.MessageBox.alert('Error Marking Task As Complete', data.result);
                }
            });
            setTimeout("loadTasks()", 100);
        } else {
            Ext.MessageBox.show({
                msg: 'No task selected. Please select a task.',
                width:300,
                buttons: Ext.MessageBox.OK,
                icon : Ext.MessageBox.INFO
            });
        }
    }

    function toggleComplete(el) {
        hideCompleted = !hideCompleted;
        loadTasks();
    }
    
    function toggleShowAssignedTasksOnly(el) {
        showAssignedTasksOnly = !showAssignedTasksOnly;
        loadTasks();
    }

    function getSelectedWorkgroupIds() {
        if (Ext.getCmp('workgroupComboId')){
            var workgroupIds = Ext.getCmp('workgroupComboId').getValue().split(",");
             return {workgroupIds : workgroupIds};
        }
    }

    function getSelectedOwnerIds() {
         if (Ext.getCmp('claimOwnerComboId')){
            var claimOwnerIds = Ext.getCmp('claimOwnerComboId').getValue().split(",");
            return {claimOwnerIds : claimOwnerIds};
        }
    }

    function getSelectedSupplierClaimOwnerIds() {
        if (Ext.getCmp('SupplierClaimOwnerComboId')) {
            var supplierClaimOwnerIds = Ext.getCmp('SupplierClaimOwnerComboId').getValue().split(',');
            return {supplierClaimOwnerIds : supplierClaimOwnerIds};
        }
    }

    function applyFilter(button, event) {
        showAssignedTasksOnly = false;
        var assignedTasksOnlyBtn = Ext.getCmp('assignedTasksOnlyButtonId');
        assignedTasksOnlyBtn.setText('Show My Assigned Tasks Only');
        assignedTasksOnlyBtn.pressed = true;
        loadTasks();
    }
</script>

<div id="tasksCreateWindow"></div>
<s:if test="!isChoxAdmin && isManager">
    <s:if test="(isInsurer && (insurerIsWorkgroupEnabled || insurerIsClaimOwnershipEnabled))">
    <div class="x-panel-bwrap chox-form-container">
        <fieldset class="x-fieldset">
            <div class="dashboard" class="form-container">
                <table cellpadding="0" cellspacing="0" class="dashboard" border="0">
                    <legend>Task Filter</legend>
                    <s:if test="insurerIsWorkgroupEnabled">
                        <tr>
                            <th nowrap style="width:100%;"><label id="tipTitle1">Workgroup</label></th>
                            <td>
                                <div id="workgroupComboDiv"></div>
                            </td>
                        </tr>
                    </s:if>
                    <s:if test="insurerIsClaimOwnershipEnabled">
                        <tr>
                            <th nowrap style="width:100%;"><label id="tipTitle2">Claim Owner</label></th>
                            <td>
                                <div id="claimOwnerComboDiv"></div>
                            </td>
                        </tr>
                    </s:if>
                </table>
                <div id="taskFilterButtonInsurerDiv"></div>
            </div>
        </fieldset>
    </div>
    </s:if>
    <s:elseif test="(isCHO && choIsClaimOwnershipEnabled)">
    <div class="x-panel-bwrap chox-form-container">
        <fieldset class="x-fieldset">
            <div class="dashboard" class="form-container">
                <table cellpadding="0" cellspacing="0" class="dashboard" border="0">
                    <legend>Task Filter</legend>
                        <tr>
                            <th nowrap style="width:100%;"><label id="tipTitle2">Supplier Claim Owner</label></th>
                            <td>
                                <div id="supplierClaimOwnerComboDiv"></div>
                            </td>
                        </tr>
                </table>
                <div id="taskFilterButtonCHODiv"></div>
            </div>
        </fieldset>
    </div>
    </s:elseif>
</s:if>
<div id="tasksGridId"></div>

