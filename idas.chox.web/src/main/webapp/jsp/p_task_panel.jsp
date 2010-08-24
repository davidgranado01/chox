<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var tasksJsonReader;
    var tasksDataStore;
    var tasksGrid;
    var dateRenderer;
    var hideCompleted = true;
    var createNewTaskWindow;
    var visibilityCombo;
    var taskTypeStore;
    
    $(function(){
        dateRenderer = Ext.util.Format.dateRenderer('d/m/Y');
        // LOAD RECORDS
        tasksJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount', root: 'results', fields:[
                {name:'id'},
                {name:'complete', type: 'boolean'},
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

        tasksDataStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({url: '<%= request.getContextPath()%>/prv/p/getTasks.action',method:'POST'}),
            reader:tasksJsonReader
        });

        tasksDataStore.setDefaultSort('dueDate', 'desc');

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

        var checkBoxSelMod = new Ext.grid.CheckboxSelectionModel({singleSelect : true});

        tasksGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:taskOnClick},
            store: tasksDataStore,
            id: 'tasksGridId',
            renderTo:'tasksGridId',
            enableHdMenu:false,
//            autoScroll: true,
            layout:'fit',
            bofyBorder: false,
            viewConfig:{forceFit:true},
            selModel : checkBoxSelMod,
            columns: [
                checkBoxSelMod,
//                checkColumn,
                {header: "Supp. Ref.", width: 70, dataIndex: 'choReference', sortable: true, resizable: true},
                {header: "Due Date", width: 75, dataIndex: 'dueDate', sortable: true, resizable: true, renderer: dateRenderer},
//                {header: "Completed Date", width: 75, dataIndex: 'completedDate', sortable: true, resizable: true, renderer: dateRenderer},
                {header: "Task Type", width: 100, dataIndex: 'type', sortable: true, resizable: true},
                {header: "Created Date", width: 75, dataIndex: 'createdDate', sortable: true, resizable: true, renderer: dateRenderer},
                {header: "Description", width: 200, dataIndex: 'description', sortable: true, resizable: true},
                {header: "Created By", width: 120, dataIndex: 'createdBy', sortable: true, resizable: true}
            ],
            width:600,
            height:240
        });

        tasksGrid.getView().getRowClass = function(record, index) {
            var today = new Date();
            var difference = record.data.dueDate - today;
            var days = Math.round(difference/(1000*60*60*24));
            return (record.data.complete ? 'gray-row' : (days > 0 ? 'black-row' : (days < -5 ? 'redback-row' : 'red-row')));
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
                    valueField: 'text',
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

        taskTypeStore.load({params:{visibility: 1}}); // initially load with 'private' visibility tasks

        var visibilityOptions = [
            [1, 'Private'],
            [2, 'Internal'],
            [3, 'External']
        ];

        var visibilityRoleOptionsCHO = [
            ['ROLE_INS_MNG', 'Manager'],
            ['ROLE_INS_SCR', 'Special Claims Reviwer'],
            ['ROLE_INS_CR', 'Claims Router'],
            ['ROLE_INS_COM', 'Claims Ownership Manager'],
            ['ROLE_INS_CH', 'Claims Handler'],
        ];
        var visibilityRoleOptionsINS = [
            ['ROLE_INS_MNG', 'Manager'],
            ['ROLE_INS_SCR', 'Special Claims Reviwer'],
            ['ROLE_INS_CR', 'Claims Router'],
            ['ROLE_INS_PC', 'Payments Clerk'],
            ['ROLE_INS_FNOL', 'FNOL'],
            ['ROLE_INS_COM', 'Claims Ownership Manager'],
            ['ROLE_INS_CH', 'Claims Handler'],
            ['ROLE_INS_OPR', 'Operator']
        ];

        var visibilityRoleOptions;
        var isCHO = <s:property value="isCHO" />;
        if (isCHO)
            visibilityRoleOptions = visibilityRoleOptionsCHO;
        else
            visibilityRoleOptions = visibilityRoleOptionsINS;

        var visibilityRoleCombo = new Ext.form.ComboBox({
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
                    store: new Ext.data.SimpleStore({
                            id:0,
                            fields: [
                                'myId',   //numeric value is the key
                                'myText' //the text value is the value
                            ],
                        data: visibilityRoleOptions
                    }),
                    valueField:'myId',
                    displayField:'myText',
                    width: 130
                });

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
                    store: new Ext.data.SimpleStore({
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
                                        // If we are an Insurer and visibility is internal, or we are a CHO
                                        // and visibility is external, then show the visibility role combo
                                        if ((isCHO && this.value == 3) || (!isCHO && this.value == 2)) {
                                            Ext.getCmp('visibilityRoleComboId').show();
                                            Ext.getCmp('visibilityRoleComboId').getEl().up('div.x-form-item').show();
                                        }
                                        else {
                                            Ext.getCmp('visibilityRoleComboId').hide();
                                            Ext.getCmp('visibilityRoleComboId').getEl().up('div.x-form-item').hide();
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
        var createNewTaskForm = new Ext.FormPanel({
            monitorValid: true,
            frame:true,
//            height: 400,
            bodyStyle:'padding:5px 5px 0',
            labelWidth: 110, // label settings here cascade unless overridden
            labelAlign: 'right',
//            width: 350,
//            defaults: {width: 230},
            defaultType: 'textfield',
            items: [
                new Ext.form.DateField({
                    fieldLabel: 'Due Date',
                    name: 'dueDate',
                    id: 'dueDateId',
                    allowBlank: false,
                    format: 'd/m/Y',
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
                {
                    fieldLabel: 'Link To Claim?',
                    name: 'linkToClaimToggle',
                    id: 'linkToClaimToggleId',
                    xtype: 'checkbox',
                    checked: true,
                    listeners: {
                                check: function (checkBox, check) {
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
                    }
                },
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

                        ajax.loadJson(url, param, function(data){
                            if(data.resultType=='YesNo'){
                                if(data.result=='yes'){
                                    createNewTaskWindow.hide();
                                    Ext.Msg.alert('Task Created', 'A new task has been created.');
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

        createNewTaskWindow = new Ext.Window({
            title: 'Add New Task',
            hidden: true,
            closable:false,
            resizable: false,
//            width:600,
//            height:350,
//            plain:true,
            items  : [createNewTaskForm]
        });

        loadTasks();
        // This is a hack!!! The scroll bars are not dispayed in the
        // task grid until the grid is visible and has been refreshed,
        // so we'll add a timer to do this.'
        // ToDo: sort out scroll-bar problem and remove this timer
        setTimeout("gridRefresh()", 100);
    });

    function taskOnClick(grid, rowIndex, columnIndex){
        if (columnIndex == 5) {
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
//        else
//            console.log("Column" + columnIndex + " clicked.");
    }

    function gridRefresh() {

        if ($('#inboxPanelTabId').is(':visible')) {
            tasksGrid.getView().refresh();
        }
        else { // if the grid is not visible, no point in refreshing.
               // so we'll add another timed event to try again'
            setTimeout("gridRefresh()", 500);
        }
    }
    function loadTasks(){
        tasksDataStore.load({params:{hideCompleted : hideCompleted}});
//        tasksGrid.getView().refresh();
    }

    function markAsComplete() {
        var selectedRecordId = tasksGrid.getSelectionModel().getSelected().get('id');
//        console.log("Mark as Complete:" + selectedRecordId);
        var url = "<%=request.getContextPath()%>/prv/p/markTaskAsComplete.action";
        var param = {
            selectedTaskId: selectedRecordId
        };

        ajax.loadJson(url, param, function(data){
                if(data.resultType=='YesNo'){
                    if(data.result=='yes'){
                        loadTasks();
                    }
                }else if(data.resultType=='Message'){
                    Ext.Msg.alert('Error Marking Task As Complete',data.result);
                }
        });
        return false;
    }
    function addNewTask() {
        createNewTaskWindow.show();
//        visibilityCombo.selectByValue('1', true);
        // Set visibility to 'private' and hide the visibility role combo (and label)
        Ext.getCmp('visibilityRoleComboId').getEl().up('div.x-form-item').hide();
        visibilityCombo.setValue('1');
//        visibilityCombo.setValue(visibilityOptions[0][1]);
        Ext.getCmp('visibilityRoleComboId').hide();
        taskTypeStore.load({params:{visibility: 1}});
        Ext.getCmp('descriptionId').setValue("");
        Ext.getCmp('dueDateId').setValue("");
        Ext.getCmp('supplierRefId').setValue("");
        Ext.getCmp('taskTypeComboId').setValue("");
        return false;
    }

    function toggleComplete(el) {
        hideCompleted = !hideCompleted;
        loadTasks();
    }
</script>

<label id="taskPanelLabelId">Task Management</label>
<label class="emptyLabel">&nbsp;</label><br/>
&nbsp;<input type="submit" value="Mark As Complete" onclick="javascript: return markAsComplete();"/>
<label class="emptyLabel" style="float:right">&nbsp;</label>
<div style="float:right; font: 12px tahoma,arial,verdana,sans-serif">
    <input type="checkbox" id="showCompletedTaskToggleId" name="showCompletedTasks" value="Hide" checked="true" onchange="javascript: return toggleComplete(this)" />&nbsp;Hide Completed Tasks<p>
</div>
<label class="emptyLabel" style="float:right">&nbsp;</label>
<label class="emptyLabel" style="float:right">&nbsp;</label>
<label class="emptyLabel" style="float:right">&nbsp;</label>
<label class="emptyLabel" style="float:right">&nbsp;</label>
<label class="emptyLabel" style="float:right">&nbsp;</label>
<input type="submit" value="Add New Task" onclick="javascript: return addNewTask();" style="float:right"/>
<div id="tasksGridId"></div>

