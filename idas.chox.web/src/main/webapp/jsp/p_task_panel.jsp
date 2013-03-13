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

    Ext.onReady(function(){
        var createNewTaskWindowHeight = 280;

        dateRenderer = Ext.util.Format.dateRenderer('d/m/Y');
        // LOAD RECORDS
        tasksJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount', root: 'results', fields:[
                {name:'id'},
                {name:'complete', type: 'boolean'},
                {name:'claimId'},
                {name:'choReference'},
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
            tasksDataStore = new Ext.data.Store({
                proxy: new Ext.data.HttpProxy({url: '<%= request.getContextPath()%>/prv/p/getTasks.action',method:'POST'}),
                reader:tasksJsonReader,
                remoteSort: true
            });
        </s:if>
        <s:else >
            tasksDataStore = new Ext.data.Store({
                proxy: new Ext.data.HttpProxy({url: '<%= request.getContextPath()%>/prv/p/getVisibleTasks.action',method:'POST'}),
                reader:tasksJsonReader,
                remoteSort: true
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
                if(t.className && t.className.indexOf('x-grid3-cc-'+this.id) != -1){
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

        var pagingBar = new Ext.PagingToolbar({
            pageSize: taskPanelRecordPerPage,
            store: tasksDataStore,
            displayInfo: true,
            displayMsg: 'Displaying Tasks {0} - {1} of {2}',
            emptyMsg: "No Tasks to display"
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
                        text:'Add New Task',
                        id : 'addNewTaskButtonId',
                        handler : addNewTask,
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
                                this.setText('Show All Tasks');
                            }
                        },
                        pressed: false,
                        disabled : !<s:property value="isUserHasManagerRole" />
                    }
                    ,'->'
                    ,{
                        text:'Export To Excel',
                        id : 'taskExportToExcelButtonId',
                        handler : doTaskExportExcel
                     }
                 ]
        });
        
        
   if (document.getElementById('tasksGridId')) {

        tasksGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:taskOnClick},
            store: tasksDataStore,
            // id: 'tasksGridId',
            renderTo:'tasksGridId',
            enableHdMenu:false,
            enableColumnMove: false,
            autoScroll: true,
            layout:'fit',
            //            bofyBorder: false,
            viewConfig:{forceFit:true},
            selModel : checkBoxSelMod,
            bbar: pagingBar,
            tbar:tbar,
            title : '<div style="text-align:center;">Task Management</div>',
            loadMask: true,
            columns: [
                checkBoxSelMod,
                //                checkColumn,
                {id:'Id', header: "Supplier Ref", width: 75, sortable: true, dataIndex: 'choReference',
                    renderer:function(value,p,r){
                        return '<a href="<%=request.getContextPath()%>/prv/openClaimDetail.action?id=' + r.data['claimId'] + '&tab=' + currentTabIndex + '">' + value + '</a>'}},
                //                {header: "Supp. Ref.", width: 70, dataIndex: 'choReference', sortable: true, resizable: true},
                {header: "Due Date", width: 75, dataIndex: 'dueDate', sortable: true, resizable: true, renderer: dateRenderer},
                //                {header: "Completed Date", width: 75, dataIndex: 'completedDate', sortable: true, resizable: true, renderer: dateRenderer},
                {header: "Task Type", width: 100, dataIndex: 'type', sortable: true, resizable: true},
                {header: "Description", width: 200, dataIndex: 'description', sortable: true, resizable: true},
                {header: "Created Date", width: 75, dataIndex: 'createdDate', sortable: true, resizable: true, renderer: dateRenderer},
                {header: "Created By", width: 120, dataIndex: 'createdBy', sortable: true, resizable: true}
            ],
            width:630,
            //            minHeight: 200,
            //            autoHeight: true
            //            maxHeight: 200
            //            autoWidth: true,
            height:220
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

        taskTypeStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({url: '<%= request.getContextPath()%>/prv/p/getTaskTypes.action',method:'POST'}),
            reader: taskTypeReader
        });

        var taskTypeCombo = new Ext.form.ComboBox({
            store: taskTypeStore,
            displayField: 'value',
            valueField: 'value',
            fieldLabel: 'Task Type',
            hiddenName: 'taskTypeCombo',
            id: 'taskTypeComboId',
            triggerAction: 'all',
            width: 250,
            selectOnFocus: true,
            mode: 'local',
            editable: false,
            allowBlank: false,
            forceSelection: true,
            emptyText: 'Please select a task type...'
        });

        //        taskTypeStore.load({params:{visibility: 1}}); // initially load with 'private' visibility tasks

        var visibilityOptions = [
            [1, 'Private'],
            [2, 'Internal'],
            [3, 'External']
        ];

        var linkToClaimToggle = new Ext.form.Checkbox ({
            fieldLabel: 'Link To Claim?',
            name: 'linkToClaimToggle',
            id: 'linkToClaimToggleId',
            checked: true,
            handler: function (checkBox, check) {
                if (check) {
                    Ext.getCmp('supplierRefId').enable();
                    Ext.getCmp('supplierRefId').show();
                    Ext.getCmp('supplierRefId').getEl().up('div.x-form-item').show();
                }
                else {
                    Ext.getCmp('supplierRefId').disable();
                    Ext.getCmp('supplierRefId').setValue('');
                    Ext.getCmp('supplierRefId').hide();
                    Ext.getCmp('supplierRefId').getEl().up('div.x-form-item').hide();
                }
            }
        });

        isCHO = <s:property value="isCHO" />;
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

            var visibilityRoleStore = new Ext.data.Store({
                proxy: new Ext.data.HttpProxy({url: '<%= request.getContextPath()%>/prv/p/getAvailableUserRolesForTask.action',method:'POST'}),
                reader: visibilityRoleReader
            });


            visibilityRoleStore.load({params:{webUserId: <s:property value="authenticatedUser.id" />}}); // initially load with available user roles

            // Create the visibility role combo used for insurer internal tasks only
            visibilityRoleCombo = new Ext.form.ComboBox({
                fieldLabel: 'Visibility Role',
                //                    hideLabel: true,
                hiddenName: 'visibilityRoleCombo',
                id: 'visibilityRoleComboId',
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
                displayField:'webUserroleName',
                width: 170
            });
            createNewTaskWindowHeight = 300;
        }

        visibilityCombo = new Ext.form.ComboBox({
            fieldLabel: 'Visibility',
            hiddenName: 'visibilityCombo',
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
            width: 90,
            listeners: {
                select: { fn:function(combo, value) {
                        //                                        Ext.getCmp('visibilityRoleComboId').clearValue();
                        // If we are an Insurer and visibility is internal, then
                        //      - show the visibility role combo
                        if (!isCHO && this.value == 2) {
                            Ext.getCmp('visibilityRoleComboId').show();
                            Ext.getCmp('visibilityRoleComboId').getEl().up('div.x-form-item').show();
                        }
                        else if (!isCHO) { // hide the combo
                            Ext.getCmp('visibilityRoleComboId').hide();
                            Ext.getCmp('visibilityRoleComboId').getEl().up('div.x-form-item').hide();
                        }
                        // if external task, claim is mandatory otherwise optional
                        if (this.value == 3) {
                            // ToDo: claim option ticked and disabled, supp ref box displayed
                            Ext.getCmp('linkToClaimToggleId').setValue(true);
                            Ext.getCmp('linkToClaimToggleId').disable()
                        }
                        else {
                            // ToDo: claim option ticked and enabled, supp ref box displayed
                            Ext.getCmp('linkToClaimToggleId').setValue(true);
                            Ext.getCmp('linkToClaimToggleId').enable()
                        }
                        // Note: maybe we should also pass the visibility role?
                        // If so, need to add a listener to the visibilityRole combo
                        // to also update the task list depending on the role selected.
                        taskTypeStore.load({params:{visibility: this.value}});
                    }
                }
            }

        });

        //        visibilityCombo.setValue(visibilityOptions[0][1]);
        if (!isCHO) {
            createNewTaskForm = new Ext.FormPanel({
                monitorValid: true,
                frame:true,
                id: 'createNewTaskFormId',
                bodyStyle:'padding:5px 5px 0',
                labelWidth: 110, // label settings here cascade unless overridden
                labelAlign: 'right',
                //            width: 450,
                //            height: 600,
                defaults: {width: 230},
                defaultType: 'textfield',
                items: [
                    new Ext.form.DateField({
                        fieldLabel: 'Due Date',
                        name: 'dueDate',
                        id: 'dueDateId',
                        allowBlank: false,
                        format: 'd/m/Y',
                        minValue: new Date(),
                        width: 90
                    }),
                    visibilityCombo,
                    visibilityRoleCombo,
                    taskTypeCombo,
                    {
                        fieldLabel: 'Description',
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
                    linkToClaimToggle,
                    {
                        fieldLabel: 'Supplier Reference',
                        name: 'supplierRef',
                        id: 'supplierRefId',
                        allowBlank: false,
                        width: 80,
                        disabled: false
                    }],
                buttons: [
                    {
                        text: 'Create',
                        formBind: true,
                        handler: function() {
                            var url = "<%=request.getContextPath()%>/prv/p/createNewTask.action";
                            var visRole = Ext.getCmp('visibilityRoleComboId').getValue();
                            var description = Ext.getCmp('descriptionId').getValue();
                            var dDate =  dateRenderer(Ext.getCmp('dueDateId').getValue());
                            var tType =  Ext.getCmp('taskTypeComboId').getValue();
                            var vis = Ext.getCmp('visibilityComboId').getValue();
                            var linkToClaim = Ext.getCmp('linkToClaimToggleId').getValue();
                            var choRef = Ext.getCmp('supplierRefId').getValue();

                            var param = {
                                taskDescription: description,
                                dueDate: dDate,
                                taskType: tType,
                                visibility: vis,
                                visibilityRole: visRole,
                                linkToClaim: linkToClaim,
                                choReference: choRef
                            };

                            ajax.loadJson2(url, param, function(data){
                                if(data.resultType=='YesNo'){
                                    if(data.result=='yes'){
                                        createNewTaskWindow.hide();
//                                        Ext.Msg.alert('Task Created', 'A new task has been created.');
                                        loadTasks();
                                    }
                                }else if(data.resultType=='Message'){
                                    Ext.Msg.alert('Error creating new task',data.result);
                                }
                            });
                        }
                    }, {
                        text:'Cancel',
                        handler:function(){
                            createNewTaskWindow.hide();
                        }
                    }]
            });
        }
        else {
            createNewTaskForm = new Ext.FormPanel({
                monitorValid: true,
                frame:true,
                id: 'createNewTaskFormId',
                bodyStyle:'padding:5px 5px 0',
                labelWidth: 110, // label settings here cascade unless overridden
                labelAlign: 'right',
                //            width: 350,
                //            height: 400,
                //            defaults: {width: 230},
                defaultType: 'textfield',
                items: [
                    new Ext.form.DateField({
                        fieldLabel: 'Due Date',
                        name: 'dueDate',
                        id: 'dueDateId',
                        allowBlank: false,
                        minValue: new Date(),
                        format: 'd/m/Y',
                        width: 90
                    }),
                    visibilityCombo,
                    taskTypeCombo,
                    {
                        fieldLabel: 'Description',
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
                    linkToClaimToggle,
                    {
                        fieldLabel: 'Supplier Reference',
                        name: 'supplierRef',
                        id: 'supplierRefId',
                        allowBlank: false,
                        width: 80,
                        disabled: false
                    }],
                buttons: [
                    {
                        text: 'Create',
                        formBind: true,
                        handler: function() {
                            var url = "<%=request.getContextPath()%>/prv/p/createNewTask.action";
                            var description = Ext.getCmp('descriptionId').getValue();
                            var dDate =  dateRenderer(Ext.getCmp('dueDateId').getValue());
                            var tType =  Ext.getCmp('taskTypeComboId').getValue();
                            var vis = Ext.getCmp('visibilityComboId').getValue();
                            var linkToClaim = Ext.getCmp('linkToClaimToggleId').getValue();
                            var choRef = Ext.getCmp('supplierRefId').getValue();

                            var param = {
                                taskDescription: description,
                                dueDate: dDate,
                                taskType: tType,
                                visibility: vis,
                                linkToClaim: linkToClaim,
                                choReference: choRef
                            };

                            ajax.loadJson2(url, param, function(data){
                                if(data.resultType=='YesNo'){
                                    if(data.result=='yes'){
                                        createNewTaskWindow.hide();
//                                        Ext.Msg.alert('Task Created', 'A new task has been created.');
                                        loadTasks();
                                    }
                                }else if(data.resultType=='Message'){
                                    Ext.Msg.alert('Error creating new task',data.result);
                                }
                            });
                        }
                    }, {
                        text:'Cancel',
                        handler:function(){
                            createNewTaskWindow.hide();
                        }
                    }]
            });

        }
        createNewTaskWindow = new Ext.Window({
            title: 'Add New Task',
            hidden: true,
            closable:false,
            resizable: true,
            //            constrain: true,
            layout: 'fit',
            width: 420,
            height: createNewTaskWindowHeight,
            //            plain:true,
            items  : [createNewTaskForm]
        });

        taskTypeStore.load({params:{visibility: 1}}); // initially load with 'private' visibility tasks
        loadTasks();
        // This is a hack!!! The scroll bars are not dispayed in the
        // task grid until the grid is visible and has been refreshed,
        // so we'll add a timer to do this.'
        // ToDo: sort out scroll-bar problem and remove this timer
//        setTimeout("gridRefresh()", 100);
      }
    });

    function taskOnClick(grid, rowIndex, columnIndex){
        if (columnIndex == 4) {
            var task = tasksGrid.getStore().getAt(rowIndex);
            var title="Task";
            var msg = "<b>Due Date</b>: " + dateRenderer(task.get("dueDate"));
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
        tasksDataStore.baseParams = {hideCompleted : hideCompleted, showAssignedTasksOnly : showAssignedTasksOnly}
        tasksDataStore.load({params:{start:start, limit:taskPanelRecordPerPage}});
    }

    function markAsComplete() {
       
        var selectedRecord = tasksGrid.getSelectionModel().getSelected();
        if (selectedRecord) {
            var selectedRecordId = selectedRecord.get('id');
            var url = "<%=request.getContextPath()%>/prv/p/markTaskAsComplete.action";
            var param = {
                selectedTaskId: selectedRecordId
            };

            ajax.loadJson2(url, param, function(data){
                if(data.resultType=='Message'){
                    Ext.MessageBox.alert('Error Marking Task As Complete', data.result);
                }
            });
            setTimeout("loadTasks()", 100);
        } else {
            Ext.MessageBox.show({
//                title: 'Error',
                msg: 'No task selected. Please select a task.',
                width:300,
                buttons: Ext.MessageBox.OK,
                icon : Ext.MessageBox.INFO
            });
        }
    }

    function addNewTask() {
    
        taskTypeStore.load({params:{visibility: 1}});
        
        Ext.getCmp('linkToClaimToggleId').enable()
        createNewTaskWindow.show();
        Ext.getCmp('createNewTaskFormId').getForm().reset();
        // Hide the visibility role combo (and label)
        if (Ext.getCmp('visibilityRoleComboId')) {
            Ext.getCmp('visibilityRoleComboId').getEl().up('div.x-form-item').hide();
            Ext.getCmp('visibilityRoleComboId').hide();
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
    
</script>

<div id="tasksGridId" class="chox-form-item" style="float:none; width: 100%"></div>

