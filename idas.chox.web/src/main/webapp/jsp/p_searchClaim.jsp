<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="idas.chox.core.search.ClaimSearchCriteria" %> <!--  Needed to access the CLAIM_OWNER_NOT_ASSIGNED constant -->
<%@ taglib uri="/struts-tags" prefix="s" %>
           
<script type="text/javascript">

    var insurerSearchScreenCombo;
    var insurerSearchScreenId;
    var supplierSearchScreenCombo;
    var supplierSearchScreenId;
    var workgroupSearchScreenCombo;
    var workgroupSearchScreenStore;
    var workgroupSearchScreenId;
    var claimOwnerSearchScreenCombo;
    var claimOwnerSearchScreenStore;
    var supplierClaimOwnerSearchScreenCombo;
    var supplierClaimOwnerSearchScreenStore;
    // below variable will hold selected Workgroup records and reapply to the same combo box when corresbonging(insurer) combo box changed. 
    var selectedWorkgroupValues;
    // below variable will hold selected Supp. ClaimOwner records and reapply to the same combo box when corresbonging(supplier) combo box changed.
    var selectedSuppClaimOwnerValues;
    // below variable will hold selected Ins. ClaimOwner records and reapply to the same combo box when corresbonging(Insurer,Workgroup) combo box changed.
    var selectedInsClaimOwnerValues;
    
    var statusSearchScreenCombo;
    var liabilityStatusSearchScreenCombo;
    var hireAndRepairSearchParamCombo;
    
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
                if (this.store.getCount() === 0) {
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



            <s:if test="isInsurer" >
            insurerSearchScreenId = '<s:property value="UserOrganisationId"/>'.split(",");
            </s:if>
            <s:elseif test="isCHO" >
            supplierSearchScreenId = '<s:property value="UserOrganisationId"/>'.split(",");
            </s:elseif>
        
            var supplierReferenceField=new Ext.form.TextField({
                id:"supplierReferenceId",
                name:"supplierReference",
                width:220,
                allowBlank:true,
                value:'<s:property value="supplierReference" escapeJavaScript="true"/>',
                renderTo: 'supplierReferenceFieldId',
                listeners:{
                    specialkey:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    }
                }
            });

            var claimNumberField=new Ext.form.TextField({
                id:"claimNumberId",
                name:"claimNumber",
                width:220,
                allowBlank:true,
                value:'<s:property value="claimNumber" escapeJavaScript="true"/>',
                renderTo:'claimNumberFieldId',
                listeners:{
                    specialkey:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    }
                }
            });

            var invoiceNumberField=new Ext.form.TextField({
                id:"invoiceNumberId",
                name:"invoiceNumber",
                width:220,
                allowBlank:true,
                value:'<s:property value="invoiceNumber" escapeJavaScript="true"/>',
                renderTo:'invoiceNumberFieldId',
                listeners:{
                    specialkey:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    }
                }
            });

            var customerVrnField=new Ext.form.TextField({
                id:"customerVrnId",
                name:"customerVrn",
                width:220,
                allowBlank:true,
                value:'<s:property value="customerVrn" escapeJavaScript="true"/>',
                renderTo:'customerVrnFieldId',
                listeners:{
                    specialkey:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    }
                }
            });

            var thirdPartyVrnField=new Ext.form.TextField({
                id:"thirdPartyVrnId",
                name:"thirdPartyVrn",
                width:220,
                allowBlank:true,
                value:'<s:property value="thirdPartyVrn" escapeJavaScript="true"/>',
                renderTo:'thirdPartyVrnFieldId',
                listeners:{
                    specialkey:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    }
                }
            });

            var openClaimsCheckBox = new Ext.form.Checkbox({
                name:'showOpenClaimsOnly',
                id:'showOpenClaimsOnlyId',
                value:'<s:property value="showOpenClaimsOnly"/>',
                renderTo:'showOpenClaimsFieldId',
                checked: <s:property value="showOpenClaimsOnly"/>,
                listeners:{
                    check:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    }
                }
            });
            var supplementaryInvoicedClaimsCheckBox = new Ext.form.Checkbox({
                name:'isSupplementaryInvoiceOnly',
                id:'supplementaryInvoicedCheckBoxId',
                value:'<s:property value="isSupplementaryInvoiceOnly"/>',
                renderTo:'searchScreenSupplementaryInvoiceDiv',
                checked: <s:property value="isSupplementaryInvoiceOnly"/>,
                listeners:{
                    check:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    }
                }
            });

            var penaltyChargesAppliedCheckBox = new Ext.form.Checkbox({
                name:'penaltyChargesAppliedOnly',
                id:'penaltyChargesAppliedOnlyCheckBoxId',
                value:'<s:property value="penaltyChargesAppliedOnly"/>',
                renderTo:'showPenaltyChargesAppliedFieldId',
                checked: <s:property value="penaltyChargesAppliedOnly"/>,
                listeners:{
                    check:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    }
                }
            });

            var claimUploadDateFromPicker = new Ext.form.DateField({
                name: 'claimUploadDateFrom',
                renderTo: 'claimUploadDateFromDiv',
                width: 120,
                allowBlank: true,
                format: 'd/m/Y',
                //            hideMode: 'offsets',
                value: '<s:date format="dd/MM/yyyy" name="claimUploadDateFrom" />',
                showWeekNumber: true,
                listeners:{
                    specialkey:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    },
                    select : function(){
                        searchClaim(true);
                    }
                }
            });

            var claimUploadDateToPicker = new Ext.form.DateField({
                name: 'claimUploadDateTo',
                renderTo: 'claimUploadDateToDiv',
                width: 120,
                allowBlank: true,
                format: 'd/m/Y',
                value: '<s:date format="dd/MM/yyyy" name="claimUploadDateTo" />',
                showWeekNumber: true,
                listeners:{
                    specialkey:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    },
                    select : function(){
                        searchClaim(true);
                    }
                }
            });


            var statusModifiedDateFromPicker = new Ext.form.DateField({
                name: 'statusModifiedDateFrom',
                renderTo: 'statusModifiedDateFromDiv',
                width: 120,
                allowBlank: true,
                format: 'd/m/Y',
                //            hideMode: 'offsets',
                value: '<s:date format="dd/MM/yyyy" name="statusModifiedDateFrom" />',
                showWeekNumber: true,
                listeners:{
                    specialkey:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    },
                    select : function(){
                        searchClaim(true);
                    }
                }
            });

            var statusModifiedDateToPicker = new Ext.form.DateField({
                name: 'statusModifiedDateTo',
                renderTo: 'statusModifiedDateToDiv',
                width: 120,
                allowBlank: true,
                format: 'd/m/Y',
                value: '<s:date format="dd/MM/yyyy" name="statusModifiedDateTo" />',
                showWeekNumber: true,
                listeners:{
                    specialkey:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    },
                    select : function(){
                        searchClaim(true);
                    }
                }
            });


            // This is REALLY weird, but we have to create an unused DateField first.
            // If this is not created, the next one we create and use (invoiceUploadDateFromPicker)
            // does not get displayed and screws up the table layout! But only for Insurers
            <s:if test="isInsurer" >
            new Ext.form.DateField({});
            </s:if>

            var invoiceUploadDateFromPicker = new Ext.form.DateField({
                name: 'invoiceUploadDateFrom',
                renderTo: 'invoiceUploadDateFromDiv',
                width: 120,
                allowBlank: true,
                format: 'd/m/Y',
                value: '<s:date format="dd/MM/yyyy" name="invoiceUploadDateFrom" />',
                showWeekNumber: true,
                listeners:{
                    specialkey:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    },
                    select : function(){
                        searchClaim(true);
                    }
                }
            });


            var invoiceUploadDateToPicker = new Ext.form.DateField({
                name: 'invoiceUploadDateTo',
                renderTo: 'invoiceUploadDateToDiv',
                width: 120,
                allowBlank: true,
                format: 'd/m/Y',
                value: '<s:date format="dd/MM/yyyy" name="invoiceUploadDateTo" />',
                showWeekNumber: true,
                listeners:{
                    specialkey:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    },
                    select : function(){
                        searchClaim(true);
                    }
                }
            });

            var rentalStartDatePicker = new Ext.form.DateField({
                name: 'rentalStartDate',
                renderTo: 'rentalStartDateDiv',
                width: 120,
                allowBlank: true,
                format: 'd/m/Y',
                value: '<s:date format="dd/MM/yyyy" name="rentalStartDate" />',
                showWeekNumber: true,
                listeners:{
                    specialkey:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    },
                    select : function(){
                        searchClaim(true);
                    }
                }
            });

            var rentalEndDatePicker = new Ext.form.DateField({
                name: 'rentalEndDate',
                renderTo: 'rentalEndDateDiv',
                width: 120,
                allowBlank: true,
                format: 'd/m/Y',
                value: '<s:date format="dd/MM/yyyy" name="rentalEndDate" />',
                showWeekNumber: true,
                listeners:{
                    specialkey:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    },
                    select : function(){
                        searchClaim(true);
                    }
                }
            });

            <s:if test="isCHO" >
            var reviewRequiredDateFromPicker = new Ext.form.DateField({
                name: 'reviewRequiredDateFrom',
                width: 120,
                allowBlank: true,
                format: 'd/m/Y',
                value: '<s:date format="dd/MM/yyyy" name="hireDateTo" />',
                showWeekNumber: true,
                listeners:{
                    specialkey:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    },
                    select : function(){
                        searchClaim(true);
                    }
                }
            });

            var reviewRequiredDateToPicker = new Ext.form.DateField({
                name: 'reviewRequiredDateTo',
                width: 120,
                allowBlank: true,
                format: 'd/m/Y',
                value: '<s:date format="dd/MM/yyyy" name="hireDateTo" />',
                showWeekNumber: true,
                listeners:{
                    specialkey:function (el, e) {
                        if(e.keyCode === e.ENTER) {
                            searchClaim(true);
                        }
                    },
                    select : function(){
                        searchClaim(true);
                    }
                }
            });

            reviewRequiredDateFromPicker.on('change', onReveiwDateChange);
            reviewRequiredDateToPicker.on('change', onReveiwDateChange);
            reviewRequiredDateFromPicker.render('reviewRequiredDateFromDiv');
            reviewRequiredDateToPicker.render('reviewRequiredDateToDiv');
            </s:if>

            <s:if test="isInsurer!=true" >
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
                // below variable is hack to stop superBoxSelect call searchClaim Function multiple times when all recored cleard at once.
                var insurerComboNumberOfSelectedRecord = 0;
                insurerSearchScreenCombo = new Ext.ux.form.SuperBoxSelect({
                    store : insurersStore,
                    width: 220,
                    valueField : 'text',
                    id : 'searchScreenInsurerComboId',
                    displayField :'value',
                    typeAhead : true,
                    renderTo : 'searchScreenInsurerDropDownDiv',
                    removeValuesFromStore : false,
                    mode : 'local',
                    triggerAction : 'all',
                    emptyText: '--- ALL ---',
                    selectOnFocus : true,
                    forceSelection : true,
                    listeners: { 
                        specialkey:function (el, e) {
                            if(e.keyCode === e.ENTER) {
                                searchClaim(true);
                            }
                        },
                        afterrender : function(){
                            if('<s:property value="insurerIdsAsString" />') {
                               this.setValue('<s:property value="insurerIdsAsString" />');
                               insurerSearchScreenId = '<s:property value="insurerIdsAsString" />'.split(",");
                               insurerComboNumberOfSelectedRecord = '<s:property value="insurerIdsAsString" />'.split(',').length;
                            }
                        },
                        select : function(){
                            insurerComboNumberOfSelectedRecord ++;
                            doInsurerSearchSelectOnChange();
                            searchClaim(true);
                        },
                        removeitem : function() {
                            if (!this.getValue() && insurerComboNumberOfSelectedRecord >=1) {
                                insurerComboNumberOfSelectedRecord = 0;
                                this.reset();
                                this.clearValue();
                                doInsurerSearchSelectOnChange();
                                searchClaim(true);
                            } else if (insurerComboNumberOfSelectedRecord>1) {
                                insurerComboNumberOfSelectedRecord --;
                                doInsurerSearchSelectOnChange();
                                searchClaim(true);
                            }
                              
                        }
                    }
                });

                </s:if> 

                <s:if test="isCHO!=true">
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
                // below variable is hack to stop superBoxSelect call searchClaim Function multiple times when all recored cleard at once.
                var supplierComboNumberOfSelectedRecord = 0;
                supplierSearchScreenCombo = new Ext.ux.form.SuperBoxSelect({
                    store : suppliersStore,
                    width: 220,
                    valueField : 'text',
                    id : 'searchScreenSupplierComboId',
                    displayField :'value',
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
                            if('<s:property value="supplierIdsAsString" />') {
                                this.setValue('<s:property value="supplierIdsAsString" />');
                                supplierSearchScreenId = '<s:property value="supplierIdsAsString" />'.split(",");
                                supplierComboNumberOfSelectedRecord = '<s:property value="supplierIdsAsString" />'.split(',').length;
                            }
                        },
                        select : function(){
                            supplierComboNumberOfSelectedRecord ++;
                            doSupplierSearchSelectOnChange();
                            searchClaim(true);
                        },
                        removeitem : function() {
                            if (!this.getValue() && supplierComboNumberOfSelectedRecord >=1) {
                                supplierComboNumberOfSelectedRecord = 0;
                                this.reset();
                                this.clearValue();
                                doSupplierSearchSelectOnChange();
                                searchClaim(true);
                            } else if (supplierComboNumberOfSelectedRecord >=1) {
                                supplierComboNumberOfSelectedRecord --;
                                doSupplierSearchSelectOnChange();
                                searchClaim(true);
                            }
                             
                        }
                    }
                });
                supplierSearchScreenCombo.render('searchScreenSupplierDropDownDiv');
                </s:if>  

                <s:if test="isInsurer!=true || (isInsurer && insurerIsWorkgroupEnabled)" >
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

                workgroupSearchScreenStore = new Ext.data.Store({
                    proxy : new Ext.data.HttpProxy
                    ({url : "<%= request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer2.action", method:'GET', params : {"orgId": insurerSearchScreenId}}),
                    reader : wgrpJsonReader
                    ,listeners: {load: function() {/*this.insert(0, new Ext.data.Record(defaultDropdownValue));*/
                        if(selectedWorkgroupValues && workgroupSearchScreenCombo) {workgroupSearchScreenCombo.reset();workgroupSearchScreenCombo.setValue(selectedWorkgroupValues);}    
                    }}
                });
//                workgroupSearchScreenStore.load({ params : {"orgId": insurerSearchScreenId}});
                // below variable is hack to stop superBoxSelect call searchClaim Function multiple times when all recored cleard at once.
                var workgroupComboNumberOfSelectedRecord = 0;
                workgroupSearchScreenCombo = new Ext.ux.form.SuperBoxSelect({
                    store : workgroupSearchScreenStore,
                    width: 220,
                    valueField : 'text',
                    id : 'searchScreenWorkgroupComboId',
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
                                    params : {"orgId": insurerSearchScreenId},
                                    callback: function() {
                                        if ('<s:property value="workgroupIdsAsString" />') {
                                            workgroupSearchScreenCombo.setValue('<s:property value="workgroupIdsAsString" />');
                                            workgroupComboNumberOfSelectedRecord = '<s:property value="workgroupIdsAsString" />'.split(',').length;
                                        }
                                    }
                                });
                                workgroupSearchScreenId = '<s:property value="workgroupIdsAsString" />'.split(",");
                        },
                        select : function(){
                            workgroupComboNumberOfSelectedRecord ++;
                            doSearchWorkgroupOnChange();
                            searchClaim(true);
                        },
                        removeitem : function() {
                            if (!this.getValue() && workgroupComboNumberOfSelectedRecord >= 1) {
                                workgroupComboNumberOfSelectedRecord = 0;
                                this.reset();
                                this.clearValue();
                                doSearchWorkgroupOnChange();
                                searchClaim(true);
                            } else if (workgroupComboNumberOfSelectedRecord >= 1) {
                                workgroupComboNumberOfSelectedRecord --;
                                doSearchWorkgroupOnChange();
                                searchClaim(true);
                            }
                             
                        }
                    }
                });
            
                workgroupSearchScreenCombo.render('searchScreenWorkgroupDropDownDiv');
                </s:if>
        
            // Add claim owner combo box
       

                <s:if test="isInsurer!=true || (isInsurer && insurerIsClaimOwnershipEnabled)" >
        
                var claimOwnerReader = new Ext.data.JsonReader({
                    totalProperty: 'totalCount',
                    root: 'results',
                    fields:
                        [
                        {name:'id'},
                        {name:'name'}
                    ]
                });
        
                claimOwnerSearchScreenStore = new Ext.data.Store({
                    proxy : new Ext.data.HttpProxy
                    ({url : "<%= request.getContextPath()%>/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", method:'GET', params : {"workgroupId": workgroupSearchScreenId,"insurerId": insurerSearchScreenId}}),
                    reader : claimOwnerReader,
                    listeners: {load: function() {/*this.insert(0, new Ext.data.Record(claimOwnerdefaultDropdownValue));*/
                         if (selectedInsClaimOwnerValues && claimOwnerSearchScreenCombo) {claimOwnerSearchScreenCombo.reset();claimOwnerSearchScreenCombo.setValue(selectedInsClaimOwnerValues);}
                    }}
                });
                // below variable is hack to stop superBoxSelect call searchClaim Function multiple times when all recored cleard at once.
                var claimOwnerComboNumberOfSelectedRecord = 0;
                claimOwnerSearchScreenCombo = new Ext.ux.form.SuperBoxSelect({
                    store : claimOwnerSearchScreenStore,
                    width: 220,
                    valueField : 'id',
                    id : 'searchScreenClaimOwnerComboId',
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
                                    params : {"workgroupId": workgroupSearchScreenId,"insurerId": insurerSearchScreenId},
                                    callback: function() {
                                        if ('<s:property value="claimOwnerIdsAsString"/>') {
                                            claimOwnerSearchScreenCombo.setValue('<s:property value="claimOwnerIdsAsString"/>');
                                            claimOwnerComboNumberOfSelectedRecord = '<s:property value="claimOwnerIdsAsString"/>'.split(',').length;
                                        }
                                    }
                                });
                        },
                        select : function(){
                            claimOwnerComboNumberOfSelectedRecord ++;
                            searchClaim(true);
                        },
                        removeitem : function() {
                            if (!this.getValue() && claimOwnerComboNumberOfSelectedRecord >=1) {
                                claimOwnerComboNumberOfSelectedRecord = 0;
                                this.reset();
                                this.clearValue();
                                searchClaim(true); 
                            } else if (claimOwnerComboNumberOfSelectedRecord >= 1) {
                                claimOwnerComboNumberOfSelectedRecord --;
                                searchClaim(true); 
                            }
                            
                        }
                    }
                });

                claimOwnerSearchScreenCombo.render('searchScreenClaimhandlerDownDiv');
                </s:if>
        
        
            // Add CHO claim owner combo box
      

                <s:if test="isCHO!=true || (isCHO && choIsClaimOwnershipEnabled)" >
        
        
                var supplierClaimOwnerReader = new Ext.data.JsonReader({
                    totalProperty: 'totalCount',
                    root: 'results',
                    fields:
                        [
                        {name:'id'},
                        {name:'name'}
                    ]
                });
            
                supplierClaimOwnerSearchScreenStore = new Ext.data.Store({
                    proxy : new Ext.data.HttpProxy
                    ({url : "<%= request.getContextPath()%>/prv/p/SearchSupplierClaimOwnerDropDownAction.action", method:'GET', params : {"supplierId": supplierSearchScreenId}}),
                    // Don't know if this is neded (search code for this already exists
                    // - just uncomment this to add and it should work
                    listeners: {load: function() {
                <s:if test="isCHO" >
                            var notAssigned = new Array();
                            // this next assignment is ugly and should be removed/refactored at some point
                            notAssigned['id'] = '<%= ClaimSearchCriteria.CLAIM_OWNER_NOT_ASSIGNED%>';
                            notAssigned['name'] = 'NOT ASSIGNED';
                            this.insert(0, new Ext.data.Record(notAssigned));
                            
                </s:if>
                <s:else >

                </s:else>
                    if (selectedSuppClaimOwnerValues && supplierClaimOwnerSearchScreenCombo) {supplierClaimOwnerSearchScreenCombo.reset();supplierClaimOwnerSearchScreenCombo.setValue(selectedSuppClaimOwnerValues);}
                        }},
                    reader : supplierClaimOwnerReader
                });
                // below variable is hack to stop superBoxSelect call searchClaim Function multiple times when all recored cleard at once.
                var supplierClaimOwnerComboNumberOfSelectedRecord = 0;
                supplierClaimOwnerSearchScreenCombo = new Ext.ux.form.SuperBoxSelect({
                    store : supplierClaimOwnerSearchScreenStore,
                    width: 220,
                    valueField : 'id',
                    id : 'searchScreenSupplierClaimOwnerComboId',
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
                                    params : {"supplierId": supplierSearchScreenId},
                                    callback: function() {
                                        if ('<s:property value="supplierClaimOwnerIdsAsString"/>') {
                                            supplierClaimOwnerSearchScreenCombo.setValue('<s:property value="supplierClaimOwnerIdsAsString"/>');
                                            supplierClaimOwnerComboNumberOfSelectedRecord = '<s:property value="supplierClaimOwnerIdsAsString"/>'.split(',').length;
                                        }
                                    }
                                });
                        },
                        select : function(){
                            supplierClaimOwnerComboNumberOfSelectedRecord ++;
                            searchClaim(true);
                        },
                        removeitem : function() {
                            if (!this.getValue() && supplierClaimOwnerComboNumberOfSelectedRecord >=1) {
                                supplierClaimOwnerComboNumberOfSelectedRecord = 0;
                                this.reset();
                                this.clearValue();
                                searchClaim(true); 
                            } else if (supplierClaimOwnerComboNumberOfSelectedRecord >=1) {
                                supplierClaimOwnerComboNumberOfSelectedRecord --;
                                searchClaim(true); 
                            }
                        }
                    }
                });

                supplierClaimOwnerSearchScreenCombo.render('searchScreenSupplierClaimOwnerDropDownDiv');
                </s:if>
        
        

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
            <s:if test="isInsurer" >
                        var actionsForHandlers = new Array();
                        // this next assignment is ugly and should be removed/refactored at some point
                        actionsForHandlers['text'] = '<%= ClaimSearchCriteria.STATUS_ACTIONS_FOR_HANDLERS%>';
                        actionsForHandlers['value'] = 'ACTIONS FOR HANDLERS';
                        this.insert(0, new Ext.data.Record(actionsForHandlers));
                        
            </s:if>
            <s:else >
//                        this.insert(0, new Ext.data.Record(statusdefaultDropdownValue));
            </s:else>
                    }}
            });
            // below variable is hack to stop superBoxSelect call searchClaim Function multiple times when all recored cleard at once.
            var statusComboNumberOfSelectedRecord = 0;
            statusSearchScreenCombo = new Ext.ux.form.SuperBoxSelect({
                store : statusesStore,
                width: 220,
                valueField : 'text',
                id : 'statusSearchScreenComboId',
                displayField :'value',
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
                        if ('<s:property value="claimStatusesAsString"/>') {
                            this.setValue('<s:property value="claimStatusesAsString"/>'); 
                            statusComboNumberOfSelectedRecord = '<s:property value="claimStatusesAsString"/>'.split(',').length;
                        }
                        
                    },
                    select : function(){
                        statusComboNumberOfSelectedRecord++;
                        statusChange();
                        searchClaim(true);
                    },
                    removeitem : function() {
                        if (!this.getValue() && statusComboNumberOfSelectedRecord >= 1) {
                            statusComboNumberOfSelectedRecord = 0;
                            this.reset();
                            this.clearValue();
                            statusChange();
                            searchClaim(true);
                         } else if (statusComboNumberOfSelectedRecord >= 1) {
                            statusComboNumberOfSelectedRecord --;
                            statusChange();
                            searchClaim(true);  
                         }
                    }
                }
            });
            statusSearchScreenCombo.render('searchScreenStatusesDropDownDiv');

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

            var liabilityStatuses = Ext.util.JSON.decode('<s:property value="liabilityStatusesJsonStringWithNull" escape="false"/>');
            var liabilityStatusesStore = new Ext.data.Store({
                data : liabilityStatuses,
                reader : liabilityStatusesJsonReader
            });
            var defaultValueText;
            if('<s:property value="liabilityStatus"/>'){
                defaultValueText = '<s:property value="liabilityStatus"/>';
            }else{
                defaultValueText = '--- ALL ---';
            }
            // below variable is hack to stop superBoxSelect call searchClaim Function multiple times when all recored cleard at once.
            var liabilityStatusComboNumberOfSelectedRecord = 0;
            liabilityStatusSearchScreenCombo = new Ext.ux.form.SuperBoxSelect({
                store : liabilityStatusesStore,
                width: 220,
                valueField : 'value',
                id : 'liabilityStatusSearchScreenComboId',
                displayField :'text',
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
                        if ('<s:property value="liabilityStatusesValueAsString"/>') {
                            this.setValue('<s:property value="liabilityStatusesValueAsString"/>'); 
                            liabilityStatusComboNumberOfSelectedRecord = '<s:property value="liabilityStatusesValueAsString"/>'.split(',').length;
                        }
                    },
                    select : function(){
                        liabilityStatusComboNumberOfSelectedRecord ++;
                        statusChange();
                        searchClaim(true);
                    },
                    removeitem : function() {
                        if (!this.getValue() && liabilityStatusComboNumberOfSelectedRecord >= 1) {
                            liabilityStatusComboNumberOfSelectedRecord = 0;
                            this.reset();
                            this.clearValue();
                            statusChange();
                            searchClaim(true);
                            } else if (liabilityStatusComboNumberOfSelectedRecord >= 1){
                                liabilityStatusComboNumberOfSelectedRecord --;
                                statusChange();
                                searchClaim(true);  
                            }
                    }
                }
            });
            liabilityStatusSearchScreenCombo.render('searchScreenLiabilityDropDownDiv');

            var hireAndRepairSearchParamData = [['Hire Only', 1],['Repair Only', 2], ['Hire and Repair', 3], ['No Hire or Repair', 4]];
            
            var hireAndRepairSearchParamStore = new Ext.data.ArrayStore({
                    fields: [
                       {name: 'text', type: 'string'},
                       {name: 'value', type: 'int'}
                    ]
            });
            
            hireAndRepairSearchParamStore.loadData(hireAndRepairSearchParamData);
            // below variable is hack to stop superBoxSelect call searchClaim Function multiple times when all recored cleard at once.
            var hireAndRepairSearchComboNumberOfSelectedRecord = 0;
            
            hireAndRepairSearchParamCombo = new Ext.ux.form.SuperBoxSelect({
                store : hireAndRepairSearchParamStore,
                width: 220,
                valueField : 'value',
                id : 'hireAndRepairSearchParamComboId',
                displayField :'text',
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
                        if ('<s:property value="HireAndRepairSearchParamAsString"/>') {
                            this.setValue('<s:property value="HireAndRepairSearchParamAsString"/>');
                            hireAndRepairSearchComboNumberOfSelectedRecord = '<s:property value="HireAndRepairSearchParamAsString"/>'.split(',').length;
                        }
                    },
                    select : function(){
                        hireAndRepairSearchComboNumberOfSelectedRecord ++;
                        statusChange();
                        searchClaim(true);
                    },
                    removeitem : function() {
                        if (!this.getValue() && hireAndRepairSearchComboNumberOfSelectedRecord >=1) {
                            hireAndRepairSearchComboNumberOfSelectedRecord = 0;
                            this.reset();
                            this.clearValue();
                            statusChange();
                            searchClaim(true);
                        } else if (hireAndRepairSearchComboNumberOfSelectedRecord >= 1){
                            hireAndRepairSearchComboNumberOfSelectedRecord --;
                            statusChange();
                            searchClaim(true); 
                        }
                         
                    }
                }
            });
            hireAndRepairSearchParamCombo.render('hireAndRepairSearchParamDropDownDiv');
            
            // Add claim type drop-down menu
            var claimTypesJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'text'},
                    {name:'value'}
                ]
            });

            var claimTypes = Ext.util.JSON.decode('<s:property value="claimTypesJsonString" escape="false"/>');
            var claimTypesStore = new Ext.data.Store({
                data : claimTypes,
                reader : claimTypesJsonReader
            });
            if('<s:property value="claimType"/>'){
                defaultValueText = '<s:property value="claimType"/>';
            }else{
                defaultValueText = '--- ALL ---';
            }
            // below variable is hack to stop superBoxSelect call searchClaim Function multiple times when all recored cleard at once.
            var claimTypesComboNumberOfSelectedRecord = 0;
            claimTypesSearchScreenCombo = new Ext.ux.form.SuperBoxSelect({
                store : claimTypesStore,
                width: 220,
                valueField : 'value',
                id : 'claimTypesSearchScreenComboId',
                displayField :'text',
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
                        if ('<s:property value="ClaimTypesValueAsString"/>') {
                            this.setValue('<s:property value="ClaimTypesValueAsString"/>');
                            claimTypesComboNumberOfSelectedRecord = '<s:property value="ClaimTypesValueAsString"/>'.split(',').length;
                        }
                    },
                    select : function(){
                        claimTypesComboNumberOfSelectedRecord ++;
                        statusChange();
                        searchClaim(true);
                    },
                    removeitem : function() {
                        if (!this.getValue() && claimTypesComboNumberOfSelectedRecord >=1) {
                            claimTypesComboNumberOfSelectedRecord = 0;
                            this.reset();
                            this.clearValue();
                            statusChange();
                            searchClaim(true);
                            } else if (claimTypesComboNumberOfSelectedRecord >= 1){
                                claimTypesComboNumberOfSelectedRecord --;
                                statusChange();
                                searchClaim(true); 
                            }
                         
                    }
                }
            });
            claimTypesSearchScreenCombo.render('searchScreenClaimTypeDropDownDiv');

       

            // Create the search and reset buttons
            new Ext.Button({
                renderTo: 'searchButton',
                text: 'Search',
                scale : 'medium',
                width : 100,
                handler: function(button, event) {
                    searchClaim(true);
                }
            }).focus();

            new Ext.Button({
                renderTo: 'resetButton',
                text: 'Reset',
                width : 100,
                scale : 'medium',
                handler: function(button, event) {
                    clearForm();
                    searchClaim(false);
                }
            });
        });

        function setSelectedInsurerId(){
    
            if (insurerSearchScreenCombo.getValue() !== null && insurerSearchScreenCombo.getValue() !== '') {
                insurerSearchScreenId = insurerSearchScreenCombo.getValue().split(",");
            }
            else {
                insurerSearchScreenId = null;
            }
        }

        function setSelectedSupplierId(){
        
            if (supplierSearchScreenCombo.getValue() !== null && supplierSearchScreenCombo.getValue() !== '') {
                supplierSearchScreenId = supplierSearchScreenCombo.getValue().split(",");
            }
            else {
                supplierSearchScreenId = null;
            }
        }


        function doInsurerSearchSelectOnChange(){
            setSelectedInsurerId();
            if (workgroupSearchScreenStore) {
                workgroupSearchScreenStore.load({ params : {"orgId": insurerSearchScreenId}});
                selectedWorkgroupValues = workgroupSearchScreenCombo.getValue();
            }
            doShowClaimHandler(workgroupSearchScreenId, insurerSearchScreenId);
        }

        function doSupplierSearchSelectOnChange(){
            setSelectedSupplierId();
            doShowSupplierClaimHandler(supplierSearchScreenId);
        }

        function doSearchWorkgroupOnChange(){
        
            if (workgroupSearchScreenCombo.getValue() !== null && workgroupSearchScreenCombo.getValue() !== '') {
                workgroupSearchScreenId = workgroupSearchScreenCombo.getValue().split(",");
            }else{
                workgroupSearchScreenId = null;
            }
            doShowClaimHandler(workgroupSearchScreenId, insurerSearchScreenId);
        }

        function doShowSupplierClaimHandler(selectedSupplierId){
        <s:if test="isCHO!=true">
            supplierClaimOwnerSearchScreenStore.load({ params : {"supplierId":selectedSupplierId}});
            selectedSuppClaimOwnerValues = supplierClaimOwnerSearchScreenCombo.getValue();  
        </s:if>
        <s:elseif test="isCHO && choIsClaimOwnershipEnabled">
            supplierClaimOwnerSearchScreenStore.load({ params : {"supplierId":selectedSupplierId}});
            selectedSuppClaimOwnerValues = supplierClaimOwnerSearchScreenCombo.getValue(); 
        </s:elseif>
        }
    
        function doShowClaimHandler(selectedWorkgroupId, selectedInsurerId){
        
        <s:if test="isInsurer!=true">
            claimOwnerSearchScreenStore.load({ params : {"workgroupId":selectedWorkgroupId,"insurerId":selectedInsurerId}});
            selectedInsClaimOwnerValues = claimOwnerSearchScreenCombo.getValue();
        </s:if>
        <s:elseif test="isInsurer"> 
        <s:if test="AuthenticatedUser.insurer.claimOwnershipEnable">
                claimOwnerSearchScreenStore.load({ params : {"workgroupId":selectedWorkgroupId,"insurerId":selectedInsurerId}});
                selectedInsClaimOwnerValues = claimOwnerSearchScreenCombo.getValue();
            <s:if test="isCH && selectedWorkgroupId == null" >
                    claimOwnerSearchScreenCombo.setValue(<s:property value="AuthenticatedUser.id"/>);
            </s:if> 
        </s:if>
            
        </s:elseif>
        }

        function setDefaultClaimOwner() {
    
        <s:if test="isInsurer"> 
        <s:if test="AuthenticatedUser.insurer.claimOwnershipEnable && isCH"> 
                claimOwnerSearchScreenCombo.setValue(<s:property value="AuthenticatedUser.id"/>);
        </s:if>
        </s:if>
       
        }

        function setDefaultSupplierClaimOwner() {
    
        <s:if test="isCHO && choIsClaimOwnershipEnabled && isOp"> 
            supplierClaimOwnerSearchScreenCombo.setValue(<s:property value="AuthenticatedUser.id"/>);
        </s:if>
        }

        function clearForm(){


            $('#searchForm').contents().find(':input').each(function() {

                var type = this.type;
                var tag = this.tagName.toLowerCase();

                if (type === 'text' || type === 'password' || tag === 'textarea'){
                    this.value = "";
                }else if(tag === 'select'){
                    this.selectedIndex = 0;
                }
            });

            $('#searchForm').contents().find(':checkbox').each(function() {
                if(this.id==='showOpenClaimsOnlyId'){
                    this.checked = true;
                }else {
                    this.checked = false;
                }
            
            });
            claimTypesSearchScreenCombo.reset();
            claimTypesSearchScreenCombo.clearValue();
            hireAndRepairSearchParamCombo.reset();
            hireAndRepairSearchParamCombo.clearValue();
            statusSearchScreenCombo.reset();
            statusSearchScreenCombo.clearValue();
            liabilityStatusSearchScreenCombo.reset();
            liabilityStatusSearchScreenCombo.clearValue();
        
        
            if (insurerSearchScreenCombo){
                selectedInsClaimOwnerValues = null;
                insurerSearchScreenId = null;
                insurerSearchScreenCombo.reset();
                insurerSearchScreenCombo.clearValue();
            }
            
            if (supplierSearchScreenCombo){
                selectedSuppClaimOwnerValues = null;
                supplierSearchScreenId = null;
                supplierSearchScreenCombo.reset();
                supplierSearchScreenCombo.clearValue();
            }
        
            if(workgroupSearchScreenCombo){
                selectedWorkgroupValues = null;
                workgroupSearchScreenId = null;
        <s:if test="isInsurer">
                workgroupSearchScreenStore.load({ params : {"orgId": <s:property value="UserOrganisationId"/>}}); 
        </s:if>
        <s:else >
                workgroupSearchScreenStore.load({ params : {"orgId": null}});
        </s:else>
                workgroupSearchScreenCombo.reset();
                workgroupSearchScreenCombo.clearValue();
            }
        
            if(claimOwnerSearchScreenCombo){
        <s:if test="isInsurer">
                claimOwnerSearchScreenStore.load({ params : {"workgroupId":-1,"insurerId": <s:property value="UserOrganisationId"/>}});
        </s:if>
        <s:else >
                claimOwnerSearchScreenStore.load({ params : {"workgroupId":-1,"insurerId": -1}});
        </s:else>
                claimOwnerSearchScreenCombo.reset();
                claimOwnerSearchScreenCombo.clearValue();
            }
        
            if(supplierClaimOwnerSearchScreenCombo){
        <s:if test="isCHO">
            
                supplierClaimOwnerSearchScreenStore.load({ params : {"supplierId": <s:property value="UserOrganisationId"/>}});
        </s:if>
        <s:else >
                supplierClaimOwnerSearchScreenStore.load({ params : {"supplierId": -1}});
        </s:else>
                supplierClaimOwnerSearchScreenCombo.reset();
                supplierClaimOwnerSearchScreenCombo.clearValue();
            }
       
            setDefaultClaimOwner();
            //            setDefaultSupplierClaimOwner();
        }

        function statusChange(){
            if((statusSearchScreenCombo.getValue()!=="AwaitingCarHireInfo") && <s:property value="isCHO" />){
                $("input[name='reviewRequiredDateTo']").val("");
                $("input[name='reviewRequiredDateFrom']").val("");
            }
        }

        function onReveiwDateChange(){
            $("#status").val("AwaitingCarHireInfo");
        }

</script>

<div id="searchPanel" class="search-panel-holder">
    <div  id="searchFormHolder">
        <table id="searchForm" cellpadding="0" cellspacing="0" class="searchForm" border="0">
            <tr>
                <td><label>Supplier Reference</label></td>
                <td><div id="supplierReferenceFieldId"></div></td>
                <td><label>Claim Number</label></td>
                <td><div id="claimNumberFieldId"></div></td>
            </tr>
            <tr>
                <td><label>Invoice Number</label></td>
                <td><div id="invoiceNumberFieldId"></div></td>
                <td><label>Show Open Claims Only <img id="help-open-items-icon" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt="" /></label></td>
                <td><div id="showOpenClaimsFieldId"></div></td>
            </tr>
            <tr>
                <td><label>Supplier VRN</label></td>
                <td><div id="customerVrnFieldId"></div></td>
                <td><label>Insurer VRN</label></td>
                <td><div id="thirdPartyVrnFieldId"></div></td>
            </tr>
            <tr>
                <td nowrap><label>Claim Upload Date From</label></td>
                <td><div id="claimUploadDateFromDiv"></div></td>
                <td nowrap><label>Claim Upload Date To</label></td>
                <td><div id="claimUploadDateToDiv"></div></td>
            </tr>
            <tr>
                <td nowrap><label>Status Modified Date From</label></td>
                <td><div id="statusModifiedDateFromDiv"></div></td>
                <td nowrap><label>Status Modified Date To</label></td>
                <td><div id="statusModifiedDateToDiv"></div></td>
            </tr>
            <tr>
                <td nowrap><label>Invoice Upload Date From</label></td>
                <td><div id="invoiceUploadDateFromDiv" ></div></td>
                <td nowrap><label>Invoice Upload Date To</label></td>
                <td><div id="invoiceUploadDateToDiv" ></div></td>
            </tr>
            <tr>
                <td nowrap><label>Hire Start Date From</label></td>
                <td><div id="rentalStartDateDiv" ></div></td>
                <td nowrap><label>Hire Start Date To</label></td>
                <td><div id="rentalEndDateDiv"></div></td>
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
                <td nowrap><label>Liability Status</label></td>
                <td><div id="searchScreenLiabilityDropDownDiv"></div></td>
                <td nowrap><label>Status</label></td>
                <td><div id="searchScreenStatusesDropDownDiv"></div></td>
            </tr>
            <tr>
                <s:if test="isCHO">
                    <td nowrap><label>Insurer Name</label></td>
                    <td><div id="searchScreenInsurerDropDownDiv"></div></td>
                    <s:if test="choIsClaimOwnershipEnabled">
                        <td nowrap><label>Claim Owner</label></td>
                        <td><div id="searchScreenSupplierClaimOwnerDropDownDiv"></div></td>
                    </s:if>
                    <s:else>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </s:else>
                </s:if>
                <s:elseif test="isChoxAdmin">
                    <td nowrap><label>Insurer Name</label></td>
                    <td><div id="searchScreenInsurerDropDownDiv"></div></td>
                    <td nowrap><label>Supplier Name</label></td>
                    <td><div id="searchScreenSupplierDropDownDiv"></div></td>
                </s:elseif>
                <s:elseif test="isInsurer">
                    <s:if test="insurerIsWorkgroupEnabled">
                        <td nowrap><label>Workgroup</label></td>
                        <td><div id="searchScreenWorkgroupDropDownDiv"></div></td>
                    </s:if>
                    <s:if test="insurerIsClaimOwnershipEnabled">
                        <td nowrap><label>Claim Owner</label></td>
                        <td><div id="searchScreenClaimhandlerDownDiv"></div></td>
                    </s:if>
                    <s:if test="insurerIsWorkgroupEnabled && !insurerIsClaimOwnershipEnabled">
                        <td nowrap><label>Supplier Name</label></td>
                        <td><div id="searchScreenSupplierDropDownDiv"></div></td>
                    </s:if>
                    <s:elseif test="!insurerIsWorkgroupEnabled && insurerIsClaimOwnershipEnabled">
                        <td nowrap><label>Supplier Name</label></td>
                        <td><div id="searchScreenSupplierDropDownDiv"></div></td>
                    </s:elseif>
                    <s:elseif test="!insurerIsWorkgroupEnabled && !insurerIsClaimOwnershipEnabled">
                        <td nowrap><label>Supplier Name</label></td>
                        <td><div id="searchScreenSupplierDropDownDiv"></div></td>
                        <td nowrap><label>Supplier Claim Owner</label></td>
                        <td><div id="searchScreenSupplierClaimOwnerDropDownDiv"></div></td>
                    </s:elseif>
                </s:elseif>
            </tr>
            <tr>
                <s:if test="isCHO || isChoxAdmin">
                    <td nowrap><label>Insurer's Workgroup</label></td>
                    <td><div id="searchScreenWorkgroupDropDownDiv"></div></td>
                    <td nowrap><label>Insurer's Claim Owner</label></td>
                    <td><div id="searchScreenClaimhandlerDownDiv"></div></td>
                </s:if>
                <s:elseif test="isInsurer && insurerIsWorkgroupEnabled && insurerIsClaimOwnershipEnabled">
                    <td nowrap><label>Supplier Name</label></td>
                    <td><div id="searchScreenSupplierDropDownDiv"></div></td>
                    <td nowrap><label>Supplier Claim Owner</label></td>
                    <td><div id="searchScreenSupplierClaimOwnerDropDownDiv"></div></td>
                </s:elseif>
            </tr>
            <s:if test="isChoxAdmin">
                <tr>
                    <td nowrap><label>Supplier Claim Owner</label></td>
                    <td><div id="searchScreenSupplierClaimOwnerDropDownDiv"></div></td>
                    <td nowrap><label>Show Claims With Penalty Charges Only</label></td>
                    <td><div id="showPenaltyChargesAppliedFieldId"></div></td>
                </tr>
                <tr>
                    <td nowrap><label>Claim Type</label></td>
                    <td><div id="searchScreenClaimTypeDropDownDiv"></div></td>
                    <td nowrap><label>Show Claims With Supp. Invoice(s) Only</label></td>
                    <td><div id="searchScreenSupplementaryInvoiceDiv"></div></td>
                </tr>
                <tr>
                    <td nowrap><label>Hire & Repair Management Status</label></td>
                    <td><div id="hireAndRepairSearchParamDropDownDiv"></div></td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
            </s:if>
            <s:elseif test="isInsurer && (insurerIsWorkgroupEnabled && !insurerIsClaimOwnershipEnabled)">
                <tr>
                    <td nowrap><label>Supplier Claim Owner</label></td>
                    <td><div id="searchScreenSupplierClaimOwnerDropDownDiv"></div></td>
                    <td nowrap><label>Show Claims With Penalty Charges Only</label></td>
                    <td><div id="showPenaltyChargesAppliedFieldId"></div></td>
                </tr>
                <tr>
                    <td nowrap><label>Claim Type</label></td>
                    <td><div id="searchScreenClaimTypeDropDownDiv"></div></td>
                    <td nowrap><label>Show Claims With Supp. Invoice(s) Only</label></td>
                    <td><div id="searchScreenSupplementaryInvoiceDiv"></div></td>
                </tr>
                <tr>
                    <td nowrap><label>Hire & Repair Management Status</label></td>
                    <td><div id="hireAndRepairSearchParamDropDownDiv"></div></td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
            </s:elseif>
            <s:elseif test="isInsurer && (!insurerIsWorkgroupEnabled && insurerIsClaimOwnershipEnabled)">
                <tr>
                    <td nowrap><label>Supplier Claim Owner</label></td>
                    <td><div id="searchScreenSupplierClaimOwnerDropDownDiv"></div></td>
                    <td nowrap><label>Show Claims With Penalty Charges Only</label></td>
                    <td><div id="showPenaltyChargesAppliedFieldId"></div></td>
                </tr>
                <tr>
                    <td nowrap><label>Claim Type</label></td>
                    <td><div id="searchScreenClaimTypeDropDownDiv"></div></td>
                    <td nowrap><label>Show Claims With Supp. Invoice(s) Only</label></td>
                    <td><div id="searchScreenSupplementaryInvoiceDiv"></div></td>
                </tr>
                <tr>
                    <td nowrap><label>Hire & Repair Management Status</label></td>
                    <td><div id="hireAndRepairSearchParamDropDownDiv"></div></td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
            </s:elseif>
            <s:else>
                <tr>
                    <td nowrap><label>Claim Type</label></td>
                    <td><div id="searchScreenClaimTypeDropDownDiv"></div></td>
                    <td nowrap><label>Hire & Repair Management Status</label></td>
                    <td><div id="hireAndRepairSearchParamDropDownDiv"></div></td>
                </tr>
                <tr>
                    <td nowrap><label>Show Claims With Penalty Charges Only</label></td>
                    <td><div id="showPenaltyChargesAppliedFieldId"></div></td>
                    <td nowrap><label>Show Claims With Supp. Invoice(s) Only</label></td>
                    <td><div id="searchScreenSupplementaryInvoiceDiv"></div></td>
                </tr>
            </s:else>
        </table>
        <table>
            <tr>
                <td width="355px"></td>
                <td align="center" style="padding-left:20px;" width="100px">
                    <div id="searchButton"></div>
                </td>
                <td align="center" style="padding-left:20px;" width="100px">
                    <div id="resetButton"></div>
                </td>
            </tr>
        </table>
    </div>
</div>