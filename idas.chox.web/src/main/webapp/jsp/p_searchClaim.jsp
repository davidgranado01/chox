<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="idas.chox.core.search.ClaimSearchCriteria" %> <!--  Needed to access the CLAIM_OWNER_NOT_ASSIGNED constant -->
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
    var liabilityStatusCombo = -1;
    var claimOwnerStore = -1;
    var claimOwnerCombo = -1;

    Ext.onReady(function(){

        new Ext.ToolTip({ target: 'help-open-items-icon', html: 'When ticked, claims with the status ClaimRejectionAccepted, InvoiceRejectionAccepted, ClaimClosed or PaymentReceived will be excluded from the list of search results.'});

        // The 'setValue' function on the combo box doesn't work
        // as, due to the asynchronous nature of this widget, the store may
        // not be loaded. Below is a patch to fix this problem.
        // Note: this code
        Ext.override(Ext.form.ComboBox, {
            setValue : function(v){
                //begin patch
                // Store not loaded yet? Set value when it *is* loaded.
                // Defer the setValue call until after the next load.
                if (this.store.getCount() == 0) {
                    this.store.on('load',
                    this.setValue.createDelegate(this, [v]), null, {single: true});
                    return;
                }
                //end patch
                var text = v;
                if(this.valueField){
                    var r = this.findRecord(this.valueField, v);
                    if(r){
                        text = r.data[this.displayField];
                    }else if(this.valueNotFoundText !== undefined){
                        text = this.valueNotFoundText;
                    }
                }
                this.lastSelectionText = text;
                if(this.hiddenField){
                    this.hiddenField.value = v;
                }
                Ext.form.ComboBox.superclass.setValue.call(this, text);
                this.value = v;
        }});


        var claimUploadDateFromPicker = new Ext.form.DateField({
            name: 'claimUploadDateFrom',
            renderTo: 'claimUploadDateFromDiv',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
//            hideMode: 'offsets',
            value: '<s:date format="dd/MM/yyyy" name="claimUploadDateFrom" />',
            showWeekNumber: true
        });

        var claimUploadDateToPicker = new Ext.form.DateField({
            name: 'claimUploadDateTo',
            renderTo: 'claimUploadDateToDiv',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="claimUploadDateTo" />',
            showWeekNumber: true
        });

        // This is REALLY weird, but we have to create an unused DateField first.
        // If this is not created, the next one we create and use (invoiceUploadDateFromPicker)
        // does not get displayed and screws up the table layout! But only for Insurers
        if (<s:property value="isInsurer" />)
            new Ext.form.DateField({});

        var invoiceUploadDateFromPicker = new Ext.form.DateField({
            name: 'invoiceUploadDateFrom',
            renderTo: 'invoiceUploadDateFromDiv',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="invoiceUploadDateFrom" />',
            showWeekNumber: true
        });

        // Another superflous call, this time only for CHOs.
        // Again, if this is not made then the invoiceUploadDateToPicker is not displayed
        // and the table column widths are screwed-up
        if (<s:property value="isCHO" />)
            new Ext.form.DateField({});

        var invoiceUploadDateToPicker = new Ext.form.DateField({
            name: 'invoiceUploadDateTo',
            renderTo: 'invoiceUploadDateToDiv',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="invoiceUploadDateTo" />',
            showWeekNumber: true
        });

        var hireDateFromPicker = new Ext.form.DateField({
            name: 'hireDateFrom',
            renderTo: 'hireDateFromDiv',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="hireDateFrom" />',
            showWeekNumber: true
        });

        var hireDateToPicker = new Ext.form.DateField({
            name: 'hireDateTo',
            renderTo: 'hireDateToDiv',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="hireDateTo" />',
            showWeekNumber: true
        });

        if(<s:property value="isCHO" />){
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
            reviewRequiredDateFromPicker.render('reviewRequiredDateFromDiv');
            reviewRequiredDateToPicker.render('reviewRequiredDateToDiv');
        }

        if(<s:property value="isCHO" /> || <s:property value="isChoxAdmin" />) {
            // Add insurers drop-down menu
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

             insurerCombo.render('searchScreenInsurerDropDownDiv');
        } // end of Insurer drop-down menu

        if(<s:property value="isInsurer" /> || <s:property value="isChoxAdmin" />) {
            // Add supplier/CHO drop-down menu
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
        }  // end of supplier/CHO drop-down menu

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
                reader : statusesJsonReader,
                listeners: {load: function() {
                   // Add a 'ACTIONS FOR HANDLERS' option for insurers - added in Phase3, Sprint2'
                    if(<s:property value="isInsurer" />) {
                       var actionsForHandlers = new Array();
                       // this next assignment is ugly and should be removed/refactored at some point
                       actionsForHandlers['text'] = '<%= ClaimSearchCriteria.STATUS_ACTIONS_FOR_HANDLERS %>';
                       actionsForHandlers['value'] = 'ACTIONS FOR HANDLERS';
                       this.insert(0, new Ext.data.Record(actionsForHandlers));
                   }
                }}
            });


        statusCombo = new Ext.form.ComboBox({
                store : statusesStore,
                width: 220,
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

       // Add liability statuses drop-down menu
        var liabilityStatusesJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                [
                    {name:'text'},
                    {name:'value'}
                ]
            });

        var liabilityStatuses = Ext.util.JSON.decode('<s:property value="liabilityStatusesJsonString" escape="false"/>');
        var liabilityStatusesStore = new Ext.data.Store({
                data : liabilityStatuses,
                reader : liabilityStatusesJsonReader
            });


        liabilityStatusCombo = new Ext.form.ComboBox({
                store : liabilityStatusesStore,
//                width: 220,
                valueField : 'value',
                id : 'liabilityStatusCombo',
                displayField :'text',
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
        liabilityStatusCombo.render('searchScreenLiabilityDropDownDiv');

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
                reader : claimOwnerReader,
                listeners: {load: function() {
                   // Add a 'NOT ASSIGNED' option for insurers - added in Phase3, Sprint2'
                   if(<s:property value="isInsurer" />) {
                       var notAssigned = new Array();
                       // this next assignment is ugly and should be removed/refactored at some point
                       notAssigned['id'] = '<%= ClaimSearchCriteria.CLAIM_OWNER_NOT_ASSIGNED %>';
                       notAssigned['name'] = 'NOT ASSIGNED';
                       this.insert(0, new Ext.data.Record(notAssigned));
                   }
                }}
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
                    text: 'Search',
                    handler: function(button, event) {
                                searchClaim();
                             }
        });

        new Ext.Button({
                    renderTo: 'resetButton',
                    text: 'Reset',
                    handler: function(button, event) {
                                clearForm();
                             }
        });

        // initialize drop-downs
        doInsurerSearchSelectOnChange();
//        doShowClaimHandler(-1, -1);

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

//        var noRecords = workgroupStore.getTotalCount();
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

        // If claimownership is switched on and a claims handler
        // has logged in, then set the claim owner drop-down to
        // the current user
        if(selectedWorkgroupId === -1)
            setDefaultClaimOwner();

    }

    function setDefaultClaimOwner() {
         var isInsurerUser = <s:property value="isInsurer"/>;
         if (!isInsurerUser) return;
         var isClaimOwnershipEnabled = '<s:property value="AuthenticatedUser.Insurer.claimOwnershipEnable"/>';

         if (isClaimOwnershipEnabled &&  <s:property value="isCH"/>) {
            claimOwnerCombo.setValue(<s:property value="AuthenticatedUser.id"/>);
         }
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
            this.checked = true;
        });
        workgroupCombo.reset();
        claimOwnerStore.removeAll();
        claimOwnerStore.load({ params : {"workgroupId":-1,"insurerId":insurerId}});
        claimOwnerCombo.reset();
        if (insurerCombo != -1)
            insurerCombo.reset();
        if (supplierCombo != -1)
            supplierCombo.reset();
        statusCombo.reset();
        setDefaultClaimOwner();
    }

    function statusChange(){
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
                <td><label>Show Open Claims Only <img id="help-open-items-icon" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt="" /></label></td>
                <td><s:checkbox name="isOpenClaim" value="true" /></td>
            </tr>
            <tr>
                <td><label>Supplier VRN</label></td>
                <td><s:textfield name="customerVrn"/></td>
                <td><label>Insurer VRN</label></td>
                <td><s:textfield name="thirdPartyVrn" /></td>
            </tr>
            <tr>
                <td nowrap><label>Claim Upload Date From</label></td>
                <td><div id="claimUploadDateFromDiv"></div></td>
                <td nowrap><label>Claim Upload Date To</label></td>
                <td><div id="claimUploadDateToDiv"></div></td>
            </tr>
            <tr>
                <td nowrap><label>Invoice Upload Date From</label></td>
                <td><div id="invoiceUploadDateFromDiv" ></div></td>
                <td nowrap><label>Invoice Upload Date To</label></td>
                <td><div id="invoiceUploadDateToDiv" ></div></td>
            </tr>
            <tr>
                <td nowrap><label>Hire Date From</label></td>
                <td><div id="hireDateFromDiv" ></div></td>
                <td nowrap><label>Hire Date To</label></td>
                <td><div id="hireDateToDiv"></div></td>
            </tr>
            <s:if test="isCHO">
                <tr>
                    <td nowrap><label>Hire Monitoring Review Required Date From</label></td>
                    <td><div id="reviewRequiredDateFromDiv" ></div></td>
                    <td nowrap><label>Hire Monitoring Review Required Date To</label></td>
                    <td><div id="reviewRequiredDateToDiv" ></div></td>
                </tr>
            </s:if>
            <s:else>
                <input type="hidden" name="reviewRequiredDateFrom" id="reviewRequiredDateFrom" />
                <input type="hidden" name="reviewRequiredDateTo" id="reviewRequiredDateTo" />
            </s:else>

            <tr>
                <s:if test="isCHO || isChoxAdmin">

                    <td nowrap><label>Insurer Name</label></td>
                    <td><div id="searchScreenInsurerDropDownDiv"></div></td>
                </s:if>
                <s:elseif test="isInsurer">
                    <td nowrap><label>Supplier Name</label></td>
                    <td><div id="searchScreenSupplierDropDownDiv"></div></td>
                </s:elseif>
                <td nowrap><label>Status</label></td>
                <td><div id="searchScreenStatusesDropDownDiv"></div></td>
            </tr>
            <tr>
                <s:if test="isInsurer">
                    <td nowrap><label>Workgroup</label></td>
                    <td><div id="searchScreenWorkgroupDropDownDiv"></div></td>
                    <td nowrap><label>Claim Owner</label></td>
                    <td><div id="searchScreenClaimhandlerDownDiv"></div></td>
                </s:if>
                <s:else>
                    <td nowrap><label>Insurer's Workgroup</label></td>
                    <td><div id="searchScreenWorkgroupDropDownDiv"></div></td>
                    <td nowrap><label>Insurer's Claim Owner</label></td>
                    <td><div id="searchScreenClaimhandlerDownDiv"></div></td>
                </s:else>
            </tr>
            <tr>
                <td nowrap><label>Liability Status</label></td>
                <td><div id="searchScreenLiabilityDropDownDiv"></div></td>
                <s:if test="isChoxAdmin">
                    <td nowrap><label>Supplier Name</label></td>
                    <td><div id="searchScreenSupplierDropDownDiv"></div></td>
                </s:if>
                <s:else>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </s:else>
            </tr>
        </table>
            <table>
                <tr>
                    <td width="355px"></td>
                    <td align="center" width="100px">
                        <div id="searchButton"></div>
                    </td>
                    <td align="center"  width="100px">
                        <div id="resetButton"></div>
                    </td>
                </tr>
            </table>
    </div>
</div>