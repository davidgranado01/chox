<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var claimTasksJsonReader;
    var claimTasksDataStore;
    var claimTasksGrid;
    var claimHideCompleted = true;
    var dateRenderer;
    var visibilityInternal = true;
    var isINS;
    var visibilityRoleCombo;

    Ext.onReady(function() {
        isINS = <s:property value="isInsurer" />;
        dateRenderer = Ext.util.Format.dateRenderer('d/m/Y');

        new Ext.form.DateField({
            fieldLabel: 'Due Date',
            name: 'dueDate',
            id: 'claimDueDateId',
            renderTo: 'dueDateDivId',
            minValue: new Date(),
            format: 'd/m/Y',
            width: 90,
            validationEvent : false
        });

        new Ext.form.DateField({
            fieldLabel: 'Payment Date',
            name: 'paymentDate',
            id: 'claimPaymentDateId',
            renderTo: 'paymentDateDivId',
            format: 'd/m/Y',
            width: 90
        });

        // Create Task type Combo
        var taskTypeReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                    [
                        {name: 'text'},
                        {name: 'value'}
                    ]
        });

        var taskTypeStore = new choxDataStore({
            url: '/prv/p/getTaskTypes.action',
            reader: taskTypeReader
        });

        var taskTypeCombo = new Ext.form.ComboBox({
            store: taskTypeStore,
            displayField: 'value',
            valueField: 'value',
            fieldLabel: 'Task Type',
            renderTo: 'taskTypeDivId',
            hiddenName: 'taskTypeCombo',
            id: 'claimTaskTypeComboId',
            triggerAction: 'all',
            width: 150,
            selectOnFocus: true,
            mode: 'local',
            editable: false,
            allowBlank: false,
            forceSelection: true,
            emptyText: 'Please select a task type...'
        });

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

        var paymentMethodCombo = new Ext.form.ComboBox({
            store: paymentMethodStore,
            width: 100,
            fieldLabel: 'Payment Method',
            renderTo: 'paymentMethodDivId',
            valueField: 'paymentMethodValue',
            id: 'paymentMethodComboId',
            hiddenName: 'paymentMethod',
            displayField: 'paymentMethodValue',
            typeAhead: true,
            mode: 'local',
            listWidth: 100,
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
                if (isINS) {
                    $("#paymentMethodLabelId").text("Actual Payment Method:");
                    $("#paymentDateLabelId").show();
                    $("#paymentDateDivId").show();
                    $("form#claimTaskForm #claimPaymentDateId").rules("add", {
                        required: true,
                        messages: {required: "Please Enter A 'Payment Date'"}
                    });         
                } else {
                    $("#paymentMethodLabelId").text("Requested Payment Method:");
                    $("#paymentDateLabelId").hide();
                    $("#paymentDateDivId").hide();
                    $("form#claimTaskForm #claimPaymentDateId").rules("remove");
                }
                $("#paymentMethodLabelId").show();
                $("#paymentMethodComboId").show();
                $("#paymentMethodDivId").show();
                if (visibilityRoleCombo){
                    // Not repositioned properly in IE6, so redraw (hack!)
                    visibilityRoleCombo.hide();
                    visibilityRoleCombo.show();
                }
            } else {
                $("#paymentMethodLabelId").hide();
                $("#paymentMethodDivId").hide();
                $("#paymentMethodComboId").hide();
                $("#paymentDateLabelId").hide();
                $("#paymentDateDivId").hide();
                $("form#claimTaskForm #claimPaymentDateId").rules("remove");
                if (visibilityRoleCombo){
                    // Not repositioned properly in IE6, so redraw (hack!)
                    visibilityRoleCombo.hide();
                    visibilityRoleCombo.show();
                }
            }
        });

        if (isINS) {
            // Create the visibility role combo used for insurer internal tasks only

            // to be removed to the server-side (also in p_claim_detail_task.jsp)
            var visibilityRoleReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                        [
                            {name: 'id'},
                            {name: 'webUserId'},
                            {name: 'webUserName'},
                            {name: 'webUserroleId'},
                            {name: 'webUserroleRole'},
                            {name: 'webUserroleName'},
                            {name: 'createdBy'},
                            {name: 'createdDate'}
                        ]
            });

            var visibilityRoleStore = new choxDataStore({
                url: '/prv/p/getAvailableUserRolesForTask.action',
                reader: visibilityRoleReader
            });

            visibilityRoleCombo = new Ext.form.ComboBox({
                hiddenName: 'claimVisibilityRoleCombo',
                id: 'claimVisibilityRoleComboId',
                renderTo: 'roleVisibilityDivId',
                mode: 'local',
                editable: false,
                allowBlank: false,
                selectOnFocus: true,
                typeAhead: true,
                triggerAction: 'all',
                forceSelection: true,
                store: visibilityRoleStore,
                valueField: 'webUserroleRole',
                displayField: 'webUserroleName',
                width: 150,
                listeners: {
                    select: {fn: function(combo, value) {
                            // Note: maybe we should also pass the visibility role?
                            // If so, need to add a listener to the visibilityRole combo
                            // to also update the task list depending on the role selected.
                            taskTypeStore.load({params: {visibility: 2}});
                        }
                    }
                }
            });
            visibilityRoleStore.load({params: {webUserId: <s:property value="authenticatedUser.id" />}}); // initially load with available user roles
        } // isINS


        // SET VALIDATION
        var form = $("form#claimTaskForm");
        form.validate(
                {
                    errorLabelContainer: "#claimTaskFormMsgBox",
                    rules: {
                        claimTaskDescription: {required: true},
                        dueDate: {required: true, dateITA:true},
//                        taskTypeCombo: {required: true},
                        claimVisibilityRoleCombo: {required: true}
                    },
                    messages:
                            {
                                claimTaskDescription: {required: "Please enter a 'Task Description'"},
                                dueDate: {required: "Please enter a 'Due Date'", dateITA:"You must supply valid date format for 'Due Date'"},
//                                taskTypeCombo: {required: "Please enter a 'Task Type'"},
                                claimVisibilityRoleCombo: {required: "Please enter a role to receive this task"}
                            }
                });

//        ui.ajaxForm(form, loadClaimTasks);


        // LOAD RECORDS
        claimTasksJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount', root: 'results', fields: [
                {name: 'id'},
                {name: 'complete', type: 'boolean'},
                {name: 'claimId'},
                {name: 'choReference'},
                {name: 'dueDate', type: 'date', dateFormat: 'd/m/Y'},
                {name: 'type'},
                {name: 'description'},
                {name: 'createdBy'},
                {name: 'toBeCompletedBy'},
                {name: 'completedBy'},
                {name: 'createdDate', type: 'date', dateFormat: 'd/m/Y'},
                {name: 'completedDate', type: 'date', dateFormat: 'd/m/Y'}
            ]
        });

    <s:if test="isChoxAdmin" >
            claimTasksDataStore = new choxDataStore({
                url: '/prv/p/getClaimTasks.action',
                reader: claimTasksJsonReader
            });
            $('#claimTaskMarkId').attr('disabled', 'disabled');
            $('#claimTaskCreateId').attr('disabled', 'disabled');
    </s:if>
    <s:else >
            claimTasksDataStore = new choxDataStore({
                url: '/prv/p/getClaimVisibleTasks.action',
                reader: claimTasksJsonReader
            });
    </s:else>

            claimTasksDataStore.setDefaultSort('dueDate', 'asc');

            var checkBoxSelMod = new Ext.grid.CheckboxSelectionModel({singleSelect: true, header: ' '});

            claimTasksGrid = new Ext.grid.GridPanel({
                listeners: {cellclick: claimTaskOnClick},
                store: claimTasksDataStore,
                id: 'claimTasksGridId',
                renderTo: 'claimTasksDivId',
                enableHdMenu: false,
                layout: 'fit',
                enableColumnMove: false,
                viewConfig: {forceFit: true},
                selModel: checkBoxSelMod,
                columns: [
                    checkBoxSelMod,
                    {header: "Due Date", width: 75, dataIndex: 'dueDate', sortable: true, resizable: true, renderer: dateRenderer},
                    {header: "Task Type", width: 100, dataIndex: 'type', sortable: true, resizable: true},
                    {header: "Description", width: 200, dataIndex: 'description', sortable: true, resizable: true},
                    {header: "Created Date", width: 75, dataIndex: 'createdDate', sortable: true, resizable: true, renderer: dateRenderer},
                    {header: "Created By", width: 120, dataIndex: 'createdBy', sortable: true, resizable: true},
                    {header: "Assigned To", width: 80, dataIndex: 'toBeCompletedBy', sortable: true, resizable: true},
                    {header: "Completed By", width: 120, dataIndex: 'completedBy', sortable: true, resizable: true}
                ],
                width: 990,
                height: 200
            });

            claimTasksGrid.getView().getRowClass = function(record, index) {
                var today = new Date();
                today.setHours(0, 0, 0, 0);
                var difference = record.data.dueDate - today;
                var days = Math.round(difference / (1000 * 60 * 60 * 24));
                return (record.data.complete ? 'gray-row' : (days > 0 ? 'black-row' : (days < 0 ? 'red-row' : 'orange-row')));
            };

            $("#paymentMethodLabelId").hide();
            $("#paymentMethodDivId").hide();
            $("#paymentMethodComboId").hide();
            $("#paymentDateLabelId").hide();
            $("#paymentDateDivId").hide();

            taskTypeStore.load({params: {visibility: 1}});
            loadClaimTasks();
            // This is a hack!!! The default value for the visibility role is not set
            // so we'll do this in a timer'
            // ToDo: sort this out and do it ptoperly (by using the on load fucntion of the store)
            setTimeout(function() {
                setDefaultVisibilityRole();
            }, 400);

        });

        function setDefaultVisibilityRole() {
            if (Ext.getCmp('claimVisibilityRoleComboId'))
                Ext.getCmp('claimVisibilityRoleComboId').setValue('ROLE_INS_CH');
        }

        function claimTaskOnClick(grid, rowIndex, columnIndex) {
            if (columnIndex === 3) {
                var task = claimTasksGrid.getStore().getAt(rowIndex);
                var title = "Task";
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

        function loadClaimTasks() {
            claimTasksDataStore.load({params: {hideCompleted: claimHideCompleted, claimId: <s:property value="claimId"/>}});
            taskTabLoaded = true;
        }

        function markAsComplete() {
            var selectedRecord = claimTasksGrid.getSelectionModel().getSelected();
            if (selectedRecord) {
                var selectedRecordId = selectedRecord.get('id');
                var url = "/prv/p/markTaskAsComplete.action";
                var param = {
                    selectedTaskId: selectedRecordId
                };

                ajax.loadJson2(url, param, function(data) {
                    if (data.resultType === 'Message') {
                        Ext.MessageBox.alert('Error Marking Task As Complete', data.result);
                    }
                });

                setTimeout(function() {
                    loadClaimTasks();
                }, 100);
//          loadClaimTasks();
            }
            return false;
        }

        function validTaskCombo() {
            var msgBox = $("#claimTaskFormMsgBox");
            if ($("#claimTaskTypeComboId").val() === "Please select a task type...") {
                if ($("#claimTaskFormMsgBox").html().indexOf("Please enter a 'Task Type'") === -1)
                    msgBox.append("Please enter a 'Task Type'\n<br/>").show();
                $('#claimTaskForm').valid();
                msgBox.append(" ").show();
                return false;
            } else {
                msgBox.text("").show();
                return true;
            }
        }

        function validPaymentMethodCombo() {
            var msgBox = $("#claimTaskFormMsgBox");
            if ($("#claimTaskTypeComboId").val() === "Total Loss Payment" && $("#paymentMethodComboId").val() === "Please Select") {
                if (isINS && $("#claimTaskFormMsgBox").html().indexOf("Please enter an 'Actual Payment Method'") === -1) {
                    msgBox.append("Please enter an 'Actual Payment Method'\n<br/>").show();
                }else if (!isINS && $("#claimTaskFormMsgBox").html().indexOf("Please enter a 'Requested Payment Method'") === -1) {
                    msgBox.append("Please enter a 'Requested Payment Method'\n<br/>").show();
                }
                $('#claimTaskForm').valid();
                msgBox.append(" ").show();
                return false;
            } else {
                msgBox.text("").show();
                return true;
            }
        }

        function addNewTask() {

            if (validTaskCombo() && validPaymentMethodCombo() && $('#claimTaskForm').valid()) {
                var url = "/prv/p/createNewTask.action";
                var description = $('#claimTaskDescriptionId').val();
                var dDate = dateRenderer(Ext.getCmp('claimDueDateId').getValue());
                var tType = Ext.getCmp('claimTaskTypeComboId').getValue();
                var pMethod = $("#paymentMethodComboId").val();
                var pDate = dateRenderer(Ext.getCmp('claimPaymentDateId').getValue());
                var vis;
                if (visibilityInternal)
                    vis = 2;
                else
                    vis = 3;

                var param;
                if (Ext.getCmp('claimVisibilityRoleComboId')) {
                    visRole = Ext.getCmp('claimVisibilityRoleComboId').getValue();
                    param = {
                        taskDescription: description,
                        dueDate: dDate,
                        taskType: tType,
                        visibility: vis,
                        visibilityRole: visRole,
                        linkToClaim: true,
                        paymentMethod: pMethod,
                        paymentDate: pDate,
                        claimId: <s:property value="claimId" />
                    };
                } else {
                    param = {
                        taskDescription: description,
                        dueDate: dDate,
                        taskType: tType,
                        visibility: vis,
                        linkToClaim: true,
                        paymentMethod: pMethod,
                        paymentDate: pDate,
                        claimId: <s:property value="claimId" />
                    };
                }

                ajax.loadJson2(url, param, function(data) {
                    if (data.resultType === 'YesNo') {
                        if (data.result === 'yes') {
                            Ext.Msg.alert('Task Created', 'A new task has been created.');
                            // Form elements will be reset, so we reset our state
                            claimHideCompleted = true;
                            visibilityInternal = true;
                            if (Ext.getCmp('claimVisibilityRoleComboId')) {
                                $('form#claimTaskForm #claimVisibilityRoleComboId').rules("add", {
                                    required: true,
                                    messages: {required: "Please select a task type to add this task"}}
                                );
                                Ext.getCmp('claimVisibilityRoleComboId').show();
                            }
                            $("form#claimTaskForm").each(function() {
                                this.reset();
                            });

                            setDefaultVisibilityRole();
                            loadClaimTasks();
                            Ext.getCmp('claimTaskTypeComboId').reset();
                        }
                    } else if (data.resultType === 'Message') {
                        Ext.Msg.alert('Error creating new task', data.result);
                    }
                });
            }
        }

        function toggleComplete(form) {
            claimHideCompleted = !claimHideCompleted;
            loadClaimTasks();
            $('input[name=hideCompleted]').attr('checked', claimHideCompleted);
            return true;
        }

        function toggleVisibility() {

            visibilityInternal = $('input[name=visibilityType]:checked', '#claimTaskForm').val() === 1;
            if (isINS && visibilityInternal) {
                // Show the visibility role combo
                Ext.getCmp('claimVisibilityRoleComboId').show();
               Ext.getCmp('claimVisibilityRoleComboId').setValue('ROLE_INS_CH');
                // ...and add back the validation rule
                $('form#claimTaskForm #claimVisibilityRoleComboId').rules("add", {
                    required: true,
                    messages: {required: "Please enter a role to receive this task"}});
            }
            else if (isINS) {
                // Hide the visibility role combo
                Ext.getCmp('claimVisibilityRoleComboId').hide();
                // ...and remove the validation
                // $('form#claimTaskForm #claimVisibilityRoleComboId').rules("remove");
            }
        }
</script>

<div class="claim-detail-tab">

    <form id="claimTaskForm" name="claimTaskForm" action="<%= request.getContextPath()%>/prv/p/createNewTask.action" method="POST">
        <div class="form-container">
            <input name="claimId" id="claimId" type="hidden" value="<s:property value="claimId" />">
            <fieldset class="x-fieldset">
                <legend>Add New Task</legend>
                <table width="100%" border="0" cellspacing="0" cellpadding="5">
                    <tr>
                        <td width="35%">
                            <!-- Table on left side -->
                            <table width="100%" border="0" cellspacing="0" cellpadding="1">
                                <tr>
                                    <td align="right" width="45%"><div class="chox-form-item"><label>Due Date:</label></div></td>
                                    <td width="55%"><div class="chox-form-item" id="dueDateDivId"/></td>
                                </tr>
                                <tr>
                                    <td align="right"><div class="chox-form-item"><label>Task Type:</label></div></td>
                                    <td><div class="chox-form-item" id="taskTypeDivId"/></td>
                                </tr>
                                <tr>
                                    <td align="right"><div class="chox-form-item"><label id="paymentMethodLabelId">Requested Payment Method:</label></div></td>
                                    <td><div class="chox-form-item" id="paymentMethodDivId"/></td>
                                </tr>
                                <tr>
                                    <td align="right"><div class="chox-form-item"><label id="paymentDateLabelId">Payment Date:</label></div></td>
                                    <td><div class="chox-form-item" id="paymentDateDivId" /></td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="chox-form-item">
                                            <input type="button" id="claimTaskCreateId" value="Add Task" onclick="addNewTask();"/>
                                        </div>
                                    </td>
                                </tr>
                            </table>

                        </td>
                        <td width="65%">
                            <!-- Table on right side -->
                            <table width="100%" border="0" cellspacing="0" cellpadding="1">
                                <tr>
                                    <td width="33%" align="right" valign="top"><div class="chox-form-item"><label>Task Description:&nbsp;</label></div></td>
                                    <td colspan="2"><s:textarea cols="60" rows="4" id="claimTaskDescriptionId" name="claimTaskDescription" /></td>
                                </tr>
                                <tr><td colspan="3"><label style="line-height: 5px" >&nbsp;</label></td></tr>
                                <tr>
                                    <td></td>
                                    <td width="50%" >
                                        <div class="chox-form-item">
                                            <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityTypeId" value="0" title="External" onClick="toggleVisibility();"/> External Task</span>
                                            <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityTypeId" value="1" title="Internal" checked="true" onClick="toggleVisibility();"/> Internal Task</span>
                                        </div>
                                    </td>
                                    <td align="left"><div class="chox-form-item" id="roleVisibilityDivId"/></td>
                                </tr>
                            </table>

                        </td>
                    </tr>
                </table>
                <div class="action-error-msg" id="claimTaskFormMsgBox"></div>
            </fieldset>
        </div>
    </form>
    <div class="chox-form-item">
        <input type="button" value="Mark As Complete" id="claimTaskMarkId" onclick="return markAsComplete();"/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </div>
    <div class="chox-form-item">
        <span class="input-radio"><input type="checkbox" name="hideCompleted" id="hideCompletedId" checked="true" onClick="return toggleComplete();"/> Hide Completed Tasks</span>
    </div>
    <div id="claimTasksDivId"></div>
</div>