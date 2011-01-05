    var tasksJsonReader;
    var tasksDataStore;
    var tasksGrid;
    var dateRenderer;
    var hideCompleted = true;
    var createNewTaskWindow;
    var visibilityCombo;
    var taskTypeStore;


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
                    store: new Ext.data.ArrayStore({
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
//                        console.log("Add New Task");
                        var url = "<%=request.getContextPath()%>/prv/p/createNewTask.action";
                        var visRole = Ext.getCmp('visibilityRoleComboId').getValue();
//                        console.log("visibilityRole=" + visRole);
                        var description = Ext.getCmp('descriptionId').getValue();
//                        console.log("description=" + description);
                        var dDate =  dateRenderer(Ext.getCmp('dueDateId').getValue());
//                        console.log("dueDate=" + dDate);
                        var tType =  Ext.getCmp('taskTypeComboId').getValue();
//                        console.log("taskType=" + tType);
                        var vis = Ext.getCmp('visibilityComboId').getValue();
//                        console.log("visibility=" + vis);
                        var linkToClaim = Ext.getCmp('linkToClaimToggleId').getValue();
//                        console.log("linkToClaim=" + linkToClaim);
                        var choRef = Ext.getCmp('supplierRefId').getValue();
//                        console.log("choReference=" + choRef);
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
//        createNewTaskWindow.insert(1,createNewTaskForm);
