<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var isChoxAdmin = false
    var insurerId = -1;
    var claimOwnerId = -1;
    var workgroupStore = -1;
    var workgroupCombo = -1;
    var insurerCombo = -1;
    var supplierCombo = -1;
    var statusCombo = -1;
    var claimOwnerStore = -1;
    var claimOwnerCombo = -1;

    Ext.onReady(function(){

        new Ext.ToolTip({ target: 'help-open-items-icon', html: 'When ticked, claims with the status ClaimRejectionAccepted, InvoiceRejectionAccepted, ClaimClosed or PaymentReceived will be excluded from the list of search results.'});


        var claimUploadDateFromPicker = new Ext.form.DateField({
            name: 'claimUploadDateFrom',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="claimUploadDateFrom" />',
            showWeekNumber: true
        });

        var claimUploadDateToPicker = new Ext.form.DateField({
            name: 'claimUploadDateTo',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="claimUploadDateTo" />',
            showWeekNumber: true
        });

        var invoiceUploadDateFromPicker = new Ext.form.DateField({
            name: 'invoiceUploadDateFrom',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="invoiceUploadDateFrom" />',
            showWeekNumber: true
        });

        var invoiceUploadDateToPicker = new Ext.form.DateField({
            name: 'invoiceUploadDateTo',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="invoiceUploadDateTo" />',
            showWeekNumber: true
        });

        var hireDateFromPicker = new Ext.form.DateField({
            name: 'hireDateFrom',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="hireDateFrom" />',
            showWeekNumber: true
        });

        var hireDateToPicker = new Ext.form.DateField({
            name: 'hireDateTo',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="hireDateTo" />',
            showWeekNumber: true
        });

        var reviewRequiredDateFromPicker = new Ext.form.DateField({
            name: 'reviewRequiredDateFrom',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="hireDateTo" />',
            showWeekNumber: true
        });

        var reviewRequiredDateToPicker = new Ext.form.DateField({
            name: 'reviewRequiredDateTo',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="hireDateTo" />',
            showWeekNumber: true
        });

        reviewRequiredDateFromPicker.on('change', onReveiwDateChange);
        reviewRequiredDateToPicker.on('change', onReveiwDateChange);

        claimUploadDateFromPicker.render('claimUploadDateFromDiv');
        claimUploadDateToPicker.render('claimUploadDateToDiv');
        invoiceUploadDateFromPicker.render('invoiceUploadDateFromDiv');
        invoiceUploadDateToPicker.render('invoiceUploadDateToDiv');
        hireDateFromPicker.render('hireDateFromDiv');
        hireDateToPicker.render('hireDateToDiv');

        if(<s:property value="isCHO" />){
            reviewRequiredDateFromPicker.render('reviewRequiredDateFromDiv');
            reviewRequiredDateToPicker.render('reviewRequiredDateToDiv');
        }

        if(<s:property value="isCHO" /> || <s:property value="isChoxAdmin" />) {
            // Add insurers drop-down menu
//console.log("Adding insurer drop-down.");
            var insurersJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                [
                    {name:'text'},
                    {name:'value'}
                ]
            });

            var myinsurers = Ext.util.JSON.decode('<s:property value="insurersJsonString" escape="false"/>');
            var insurersStore = new Ext.data.Store({
                data : myinsurers,
                reader : insurersJsonReader
            });
//console.log("Store created.");


            insurerCombo = new Ext.form.ComboBox({
                store : insurersStore,
                valueField : 'text',
                id : 'insurerCombo',
                displayField :'value',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                emptyText : '--- ALL ---',
                selectOnFocus : false,
                allowBlank : true,
                listeners: { select: doInsurerSearchSelectOnChange,
                                blur: function () {
                                        if(this.getRawValue() == "" ) {
                                            this.clearValue();
                                            doInsurerSearchSelectOnChange();
                                        }
                                      }
                }
             });

//             console.log("Rendering to div.");
             insurerCombo.render('searchScreenInsurerDropDownDiv');
        } else {
//            console.log("No insurer drop-down added.");
        } // end of Insurer drop-down menu

        if(<s:property value="isInsurer" /> || <s:property value="isChoxAdmin" />) {
            // Add supplier/CHO drop-down menu
//console.log("Adding supplier drop-down.");
            var suppliersJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                [
                    {name:'text'},
                    {name:'value'}
                ]
            });

            var mysuppliers = Ext.util.JSON.decode('<s:property value="suppliersJsonString" escape="false"/>');
            var suppliersStore = new Ext.data.Store({
                data : mysuppliers,
                reader : suppliersJsonReader
            });

            supplierCombo = new Ext.form.ComboBox({
                store : suppliersStore,
                valueField : 'text',
                id : 'supplierCombo',
                displayField :'value',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                emptyText : '--- ALL ---',
                selectOnFocus : false,
                allowBlank : true,
                listeners: { blur: function () {
                                        if(this.getRawValue() == "" ) {
                                            this.clearValue();
                                        }
                                      }
                }
            });
            supplierCombo.render('searchScreenSupplierDropDownDiv');
        } else {
//console.log("No supplier drop-down added.");
        } // end of supplier/CHO drop-down menu

//console.log("Creating workgroup drop-down menu");
        // Add Workgroup drop-down menu
        var wgrpJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                [
                    {name:'text'},
                    {name:'value'}
                ]
            });

        workgroupStore = new Ext.data.Store({
                proxy : new Ext.data.HttpProxy
                ({url : "<%= request.getContextPath()%>/prv/p/SearchWorkgroupDropDownAction.action", method:'GET', params : {"orgId":insurerId}}),
                reader : wgrpJsonReader
        });

        workgroupCombo = new Ext.form.ComboBox({
            store : workgroupStore,
            valueField : 'text',
            id : 'workgroupCombo',
            displayField :'value',
            typeAhead : true,
            mode : 'local',
            triggerAction : 'all',
            emptyText : '--- ALL ---',
            selectOnFocus : true,
            allowBlank : true,
            listeners: { select: doSearchWorkgroupOnChange,
                         blur: function () {
                                        if(this.getRawValue() == "" ) {
                                            this.clearValue(); this.reset();
                                            doShowClaimHandler(-1, insurerId);
                                        }
                               }}
        });

        workgroupCombo.render('searchScreenWorkgroupDropDownDiv');

        // Add statuses drop-down menu
//console.log("Adding statuses drop-down.");
        var statusesJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                [
                    {name:'text'},
                    {name:'value'}
                ]
            });

        var statuses = Ext.util.JSON.decode('<s:property value="statusesJsonString" escape="false"/>');
        var statusesStore = new Ext.data.Store({
                data : statuses,
                reader : statusesJsonReader
            });


        statusCombo = new Ext.form.ComboBox({
                store : statusesStore,
                valueField : 'text',
                id : 'statusCombo',
                displayField :'value',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                emptyText : '--- ALL ---',
                selectOnFocus : false,
                allowBlank : true,
                listeners: {change: statusChange,
                            blur: function () {
                                        if(this.getRawValue() == "" ) {
                                            this.clearValue();
                                        }
                                      }
                }
            });
        statusCombo.render('searchScreenStatusesDropDownDiv');

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

        claimOwnerStore = new Ext.data.Store({
                proxy : new Ext.data.HttpProxy
                ({url : "<%= request.getContextPath()%>/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", method:'GET', params : {"workgroupId":-1,"insurerId":-1}}),
                reader : claimOwnerReader
        });

        claimOwnerCombo = new Ext.form.ComboBox({
            store : claimOwnerStore,
            valueField : 'id',
            id : 'claimOwnerCombo',
            displayField :'name',
            typeAhead : true,
            mode : 'local',
            triggerAction : 'all',
            emptyText : '--- ALL ---',
            selectOnFocus : true,
            allowBlank : true,
            listeners: { blur: function () {
                                        if(this.getRawValue() == "" ) {
                                            this.clearValue(); this.reset();
                                        }
                               }}
        });

        claimOwnerCombo.render('searchScreenClaimhandlerDownDiv');

        // Create the search and reset buttons
        new Ext.Button({
                    renderTo: 'searchButton',
//                    applyTo: 'searchButton',
                    text: 'Search',
                    handler: function(button, event) {
                                searchClaim();
                             }
        });

        new Ext.Button({
                    renderTo: 'resetButton',
//                    applyTo: 'resetButton',
                    text: 'Reset',
                    handler: function(button, event) {
                                clearForm();
                             }
        });

        // initialize drop-downs
        doInsurerSearchSelectOnChange();
        doShowClaimHandler(-1, -1);

    });

    function setSelectedInsurerId(){
        var isInsurerUser = <s:property value="isInsurer"/>;
//        insurerId = -1;
        if(isInsurerUser){
            insurerId = '<s:property value="OrganisationId"/>';
        }else{
            if (insurerCombo.getValue() != null && insurerCombo.getValue() != '') {
//                console.log("Setting insurerId: " + insurerCombo.getValue());
                insurerId = insurerCombo.getValue();
            }
            else {
//                console.log("No insurerId to set!");
                insurerId = -1;
                insurerCombo.reset();
            }
        }
    }


    function doInsurerSearchSelectOnChange(){
        setSelectedInsurerId();
//console.log("Loading workgroup combo.");

        workgroupStore.removeAll();
        workgroupStore.load({ params : {"orgId":insurerId}});
        workgroupCombo.reset();
//        var noRecords = workgroupStore.getTotalCount();
//console.log("doInsurerSearchSelectOnChange workgroup has " + noRecords + " records.");
        doShowClaimHandler(-1, insurerId);
    }

    function doSearchWorkgroupOnChange(){
        setSelectedInsurerId();
        var workgroupId = -1;

        var noRecords = workgroupStore.getTotalCount();
//console.log("doSearchWorkgroupOnChange: workgroup has " + noRecords + " records.");

        if (workgroupCombo.getValue() != null) {
            workgroupId = workgroupCombo.getValue();
        }
 

        doShowClaimHandler(workgroupId, insurerId);
    }

    function doShowClaimHandler(selectedWorkgroupId, selectedInsurerId){
//console.log("Loading claim owner combo.");

        claimOwnerCombo.reset();
        claimOwnerStore.removeAll();
        claimOwnerStore.load({ params : {"workgroupId":selectedWorkgroupId,"insurerId":selectedInsurerId}});

    }

    function clearForm(){

        $('#searchForm').contents().find(':input').each(function() {
            var type = this.type;
            var tag = this.tagName.toLowerCase();

            if (type == 'text' || type == 'password' || tag == 'textarea'){
                this.value = "";
            }else if(tag == 'select'){
                this.selectedIndex = 0;
            }
        });

        $('#searchForm').contents().find(':checkbox').each(function() {
            this.checked = false;
        });
        claimOwnerCombo.reset();
        workgroupCombo.reset();
        if (insurerCombo != -1)
            insurerCombo.reset();
        if (supplierCombo != -1)
            supplierCombo.reset();
        statusCombo.reset();

    }

    function statusChange(){
//console.log("statusChange() called.");
        if((statusCombo.getValue() !="AwaitingCarHireInfo") && <s:property value="isCHO" />){
            $("input[name='reviewRequiredDateTo']").val("");
            $("input[name='reviewRequiredDateFrom']").val("");
        }
    }

    function onReveiwDateChange(){
        $("#status").val("AwaitingCarHireInfo");
    }

</script>

<div id="searchPanel" class="search-panel-holder">
    <div>
        <table id="searchForm" cellpadding="0" cellspacing="0" class="searchForm" border="0">
            <tr>
                <td><label>Supplier Reference</label></td>
                <td><s:textfield name="supplierReference"/></td>
                <td><label>Claim Number</label></td>
                <td><s:textfield name="claimNumber"/></td>
            </tr>
            <tr>
                <td><label>Invoice Number</label></td>
                <td><s:textfield name="invoiceNumber"/></td>
                <td><label>Show Open Claims Only <img id="help-open-items-icon" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt="" /></label></td><td><s:checkbox name="isOpenClaim" value="true" /></td>
            </tr>
            <tr>
                <td><label>Supplier VRN</label></td><td><s:textfield name="customerVrn"/></td>
                <td><label>Insurer VRN</label></td><td><s:textfield name="thirdPartyVrn" /></td>
            </tr>
            <tr>
                <td nowrap><label>Claim Upload Date From</label></td><td><div id="claimUploadDateFromDiv" ></div></td>
                <td nowrap><label>Claim Upload Date To</label></td><td><div id="claimUploadDateToDiv"></div></td>
            </tr>
            <tr>
                <td nowrap><label>Invoice Upload Date From</label></td><td><div id="invoiceUploadDateFromDiv" ></div></td>
                <td nowrap><label>Invoice Upload Date To</label></td><td><div id="invoiceUploadDateToDiv" ></div></td>
            </tr>
            <tr>
                <td nowrap><label>Hire Date From</label></td><td><div id="hireDateFromDiv" ></div></td>
                <td nowrap><label>Hire Date To</label></td><td><div id="hireDateToDiv"></div></td>
            </tr>
            <s:if test="isCHO">
                <tr>
                    <td nowrap><label>Hire Monitoring Review Required Date From</label></td><td><div id="reviewRequiredDateFromDiv" ></div></td>
                    <td nowrap><label>Hire Monitoring Review Required Date To</label></td><td><div id="reviewRequiredDateToDiv" ></div></td>
                </tr>
            </s:if>
            <s:else>
                <input type="hidden" name="reviewRequiredDateFrom" id="reviewRequiredDateFrom" />
                <input type="hidden" name="reviewRequiredDateTo" id="reviewRequiredDateTo" />
            </s:else>

            <tr>
                <s:if test="isCHO || isChoxAdmin">

                    <td><label>Insurer Name</label></td>
                    <td><div id="searchScreenInsurerDropDownDiv"></div></td>
                </s:if>
                <s:elseif test="isInsurer">
                    <td><label>Supplier Name</label></td>
                    <td><div id="searchScreenSupplierDropDownDiv"></div>
                    </td>
                </s:elseif>
                <td><label>Status</label></td>
                <td><div id="searchScreenStatusesDropDownDiv"></div></td>
            </tr>
            <s:if test="isInsurer">
                <tr>
                    <td><label>Workgroup</label></td>
                    <td><div id="searchScreenWorkgroupDropDownDiv"></div></td>
                    <td><label>Claim Owner</label></td>
                    <td><div id="searchScreenClaimhandlerDownDiv"></div></td>
                </tr>
            </s:if>
            <s:else>
                <tr>
                    <td><label>Insurer's Workgroup</label></td>
                    <td><div id="searchScreenWorkgroupDropDownDiv"></div></td>
                    <td><label>Insurer's Claim Owner</label></td>
                    <td><div id="searchScreenClaimhandlerDownDiv"></div></td>
                </tr>
            </s:else>

            <tr>
                <s:if test="isChoxAdmin">
                    <td><label>Supplier Name</label></td>
                    <td><div id="searchScreenSupplierDropDownDiv"></div></td>
                    <td><label></label></td><td></td>
                </s:if>
            </tr>

        </table>
        <style type="text/css">
        </style>
        <!--div class="buttonPanel" id="buttonDiv"-->
            <div id="searchButton" style="position: relative; left: 410px; top: 10px;"></div>
            <div id="resetButton" style="position: relative; left: 495px; top: -11px;"></div>
                <!--input type="button" onclick="javascript:searchClaim();" value="Search" /-->
                <!--input type="reset" onclick="javascript:clearForm();" value="Reset" /-->
            <!--/div -->
        <!--/div-->
    </div>
</div>