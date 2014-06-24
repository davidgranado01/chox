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
    var searchColumsPanel;
    var queueDataStore;
    var queueGrid;
//    var claimsGridTitle;
    // This is used to load claimGrid page start param from session when queue selected and page reloaded/refereshed/back from claim detail page.
    var canLoadClaimsOnQueueSelection = true;
//    var isQueueSyncWithSearchField = Ext.state.Manager.get("syncWithSearchField");
    
    // below variable is hack to stop superBoxSelect call searchClaim Function multiple times when all recored cleard at once.
    var statusComboNumberOfSelectedRecord = 0;
    var claimTypesComboNumberOfSelectedRecord = 0;
    
    Ext.onReady(function(){

        new Ext.ToolTip({ target: 'help-open-items-icon', html: 'When ticked, claims with the status ClaimRejectionAccepted, InvoiceRejectionAccepted, ClaimClosed or PaymentReceived will be excluded from the list of search results.'});


            <s:if test="isInsurer" >
            insurerSearchScreenId = '<s:property value="UserOrganisationId"/>'.split(",");
            </s:if>
            <s:elseif test="isCHO" >
            supplierSearchScreenId = '<s:property value="UserOrganisationId"/>'.split(",");
            </s:elseif>
        
            var supplierReferenceField=new Ext.form.TextField({
                id:"supplierReferenceId",
                name:"supplierReference",
                width: 180,
                fieldLabel: 'Supplier Ref',
                allowBlank:true,
                value:'<s:property value="supplierReference" escapeJavaScript="true"/>',
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
                width: 180,
                fieldLabel: 'Claim Number',
                allowBlank:true,
                value:'<s:property value="claimNumber" escapeJavaScript="true"/>',
//                renderTo:'claimNumberFieldId',
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
                width: 180,
                fieldLabel: 'Invoice Number',
                allowBlank:true,
                value:'<s:property value="invoiceNumber" escapeJavaScript="true"/>',
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
                width: 180,
                fieldLabel: 'Supplier VRN',
                allowBlank:true,
                value:'<s:property value="customerVrn" escapeJavaScript="true"/>',
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
                width: 180,
                fieldLabel: 'Insurer VRN',
                allowBlank:true,
                value:'<s:property value="thirdPartyVrn" escapeJavaScript="true"/>',
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
                fieldLabel: 'Show Open Claims Only',
                labelStyle: 'width:150px',
                checked: <s:property value="showOpenClaimsOnly"/>,
                listeners:{
                    check:function (el, e) {
                        if(e.keyCode === e.ENTER) {
//                            searchClaim(true);
                        }
                    }
                }
            });
            
            var liabilityStatusUpdateNotification = new Ext.form.Checkbox({
                name:'liabilityStatusUpdated',
                id:'liabilityStatusUpdatedId',
                value:'<s:property value="liabilityStatusUpdated"/>',
                fieldLabel: 'Liability Status Updated',
                labelStyle: 'width:230px',
                checked: <s:property value="liabilityStatusUpdated"/>,
                listeners:{
                    check:function (el, e) {
                        if(e.keyCode === e.ENTER) {
//                            searchClaim(true);
                        }
                    }
                }
            });
            
            var supplementaryInvoicedClaimsCheckBox = new Ext.form.Checkbox({
                name:'isSupplementaryInvoiceOnly',
                id:'supplementaryInvoicedCheckBoxId',
                value:'<s:property value="isSupplementaryInvoiceOnly"/>',
                fieldLabel: 'Show Claims With Supp. Invoice(s) Only',
                labelStyle: 'width:230px',
                checked: <s:property value="isSupplementaryInvoiceOnly"/>,
                listeners:{
                    check:function (el, e) {
                        if(e.keyCode === e.ENTER) {
//                            searchClaim(true);
                        }
                    }
                }
            });

            var penaltyChargesAppliedCheckBox = new Ext.form.Checkbox({
                name:'penaltyChargesAppliedOnly',
                id:'penaltyChargesAppliedOnlyCheckBoxId',
                value:'<s:property value="penaltyChargesAppliedOnly"/>',
                fieldLabel: 'Show Claims With Penalty Charges Only',
                labelStyle: 'width:230px',
                checked: <s:property value="penaltyChargesAppliedOnly"/>,
                listeners:{
                    check:function (el, e) {
                        if(e.keyCode === e.ENTER) {
//                            searchClaim(true);
                        }
                    }
                }
            });
            
            var penaltyChargesToBeAppliedCheckBox = new Ext.form.Checkbox({
                name:'isPenaltyChargeApplied',
                id:'penaltyChargesToBeAppliedCheckBoxId',
                value:'<s:property value="isPenaltyChargeApplied"/>',
                fieldLabel: 'Penalty Charges To Be Applied',
                labelStyle: 'width:230px',
                checked: <s:property value="isPenaltyChargeApplied"/>,
                listeners:{
                    check:function (el, e) {
                        if(e.keyCode === e.ENTER) {
//                            searchClaim(true);
                        }
                    }
                }
            });

            var finalReviewChoCheckBox = new Ext.form.Checkbox({
                name:'finalReviewCho',
                id:'finalReviewChoCheckBoxId',
                disabled : <s:property value="isInsurer"/>,
                hidden : <s:property value="isInsurer"/>,
                value:'<s:property value="finalReviewCho"/>',
                fieldLabel: 'Final Review CHO',
                labelStyle: 'width:230px',
                checked: ('<s:property value="finalReviewCho"/>' === true) ? true : ('<s:property value="finalReviewCho"/>' === false) ? false : null,
                listeners:{
                    check:function (el, e) {
                        if(e.keyCode === e.ENTER) {
//                            searchClaim(true);
                        }
                    }
                }
            });
            
            var finalReviewInsCheckBox = new Ext.form.Checkbox({
                name:'finalReviewIns',
                id:'finalReviewInsCheckBoxId',
                disabled : <s:property value="isCHO"/>,
                hidden : <s:property value="isCHO"/>,
                value:'<s:property value="finalReviewIns"/>',
                fieldLabel: 'Final Review Insurer',
                labelStyle: 'width:230px',
                checked: ('<s:property value="finalReviewIns"/>' === true) ? true : ('<s:property value="finalReviewIns"/>' === false) ? false : null,
                listeners:{
                    check:function (el, e) {
                        if(e.keyCode === e.ENTER) {
//                            searchClaim(true);
                        }
                    }
                }
            });
            
            var anomaliesCheckBox = new Ext.form.Checkbox({
                name:'isAnomalies',
                id:'anomaliesCheckBoxId',
                value:'<s:property value="isAnomalies"/>',
                fieldLabel: 'Is Anomalies',
                labelStyle: 'width:150px',
                checked: <s:property value="isAnomalies"/>,
                listeners:{
                    check:function (el, e) {
                        if(e.keyCode === e.ENTER) {
//                            searchClaim(true);
                        }
                    }
                }
            });
            
            var escalatedToSupervisorCheckBox = new Ext.form.Checkbox({
                name:'escalatedToSupervisor',
                id:'escalatedToSupervisorCheckBoxId',
                value:'<s:property value="escalatedToSupervisor"/>',
                fieldLabel: 'Escalated To Supervisor',
                labelStyle: 'width:150px',
                checked: <s:property value="escalatedToSupervisor"/>,
                listeners:{
                    check:function (el, e) {
                        if(e.keyCode === e.ENTER) {
//                            searchClaim(true);
                        }
                    }
                }
            });
            
            var interimPaymentMadeCheckBox = new Ext.form.Checkbox({
                name:'isInterimPaymentMade',
                id:'interimPaymentMadeCheckBoxId',
                value:'<s:property value="isInterimPaymentMade"/>',
                fieldLabel: 'Is Interim Payment Made',
                labelStyle: 'width:150px',
                checked: <s:property value="isInterimPaymentMade"/>,
                listeners:{
                    check:function (el, e) {
                        if(e.keyCode === e.ENTER) {
//                            searchClaim(true);
                        }
                    }
                }
            });
            
            var claimUploadDateFromPicker = new Ext.form.DateField({
                name: 'claimUploadDateFrom',
                fieldLabel: 'Claim Upload Date From',
                labelStyle: 'width:150px',
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
//                        searchClaim(true);
                    }
                }
            });

            var claimUploadDateToPicker = new Ext.form.DateField({
                name: 'claimUploadDateTo',
                fieldLabel: 'Claim Upload Date To',
                labelStyle: 'width:150px',
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
//                        searchClaim(true);
                    }
                }
            });


            var statusModifiedDateFromPicker = new Ext.form.DateField({
                name: 'statusModifiedDateFrom',
                fieldLabel: 'Status Modified Date From',
                labelStyle: 'width:150px',
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
//                        searchClaim(true);
                    }
                }
            });

            var statusModifiedDateToPicker = new Ext.form.DateField({
                name: 'statusModifiedDateTo',
                fieldLabel: 'Status Modified Date To',
                labelStyle: 'width:150px',
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
//                        searchClaim(true);
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
                fieldLabel: 'Invoice Upload Date From',
                labelStyle: 'width:150px',
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
//                        searchClaim(true);
                    }
                }
            });


            var invoiceUploadDateToPicker = new Ext.form.DateField({
                name: 'invoiceUploadDateTo',
                fieldLabel: 'Invoice Upload Date To',
                labelStyle: 'width:150px',
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
//                        searchClaim(true);
                    }
                }
            });

            var rentalStartDatePicker = new Ext.form.DateField({
                name: 'rentalStartDate',
                fieldLabel: 'Hire Start Date From',
                labelStyle: 'width:150px',
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
//                        searchClaim(true);
                    }
                }
            });

            var rentalEndDatePicker = new Ext.form.DateField({
                name: 'rentalEndDate',
                fieldLabel: 'Hire Start Date To',
                labelStyle: 'width:150px',
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
//                        searchClaim(true);
                    }
                }
            });

            <%--<s:if test="isCHO" >--%>
            var reviewRequiredDateFromPicker = new Ext.form.DateField({
                name: 'reviewRequiredDateFrom',
                fieldLabel: 'Hire Monitoring Review Required Date From',
                labelStyle: 'width:160px',
                disabled : !<s:property value="isCHO"/>,
                hidden : !<s:property value="isCHO"/>,
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
//                        searchClaim(true);
                    }
                }
            });

            var reviewRequiredDateToPicker = new Ext.form.DateField({
                name: 'reviewRequiredDateTo',
                fieldLabel: 'Hire Monitoring Review Required Date To',
                labelStyle: 'width:160px',
                disabled : !<s:property value="isCHO"/>,
                hidden : !<s:property value="isCHO"/>,
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
//                        searchClaim(true);
                    }
                }
            });

            reviewRequiredDateFromPicker.on('change', onReveiwDateChange);
            reviewRequiredDateToPicker.on('change', onReveiwDateChange);
            <%--</s:if>--%>

            <%--<s:if test="isInsurer!=true" >--%>
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
                    width: 200,
                    valueField : 'text',
                    id : 'searchScreenInsurerComboId',
                    displayField :'value',
                    typeAhead : true,
                    fieldLabel: 'Insurer Name',
                    disabled : <s:property value="isInsurer"/>,
                    hidden : <s:property value="isInsurer"/>,
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
                            doLayoutSearchPanel();
//                            loadQueueGrid(true);
//                            searchClaim(true);
                        },
                        removeitem : function() {
                            if (!this.getValue() && insurerComboNumberOfSelectedRecord >=1) {
                                insurerComboNumberOfSelectedRecord = 0;
                                this.reset();
                                this.clearValue();
                                doInsurerSearchSelectOnChange();
//                                loadQueueGrid(true);
//                                searchClaim(true);
                            } else if (insurerComboNumberOfSelectedRecord>1) {
                                insurerComboNumberOfSelectedRecord --;
                                doInsurerSearchSelectOnChange();
//                                loadQueueGrid(true);
//                                searchClaim(true);
                            }
                            doLayoutSearchPanel();  
                        }
                    }
                });

                <%--</s:if>--%> 

                <%--<s:if test="isCHO!=true">--%>
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
                    width: 200,
                    fieldLabel: 'Supplier Name',
                    disabled : <s:property value="isCHO"/>,
                    hidden : <s:property value="isCHO"/>,
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
//                            loadQueueGrid(true);
                            doLayoutSearchPanel();
//                            searchClaim(true);
                        },
                        removeitem : function() {
                            if (!this.getValue() && supplierComboNumberOfSelectedRecord >=1) {
                                supplierComboNumberOfSelectedRecord = 0;
                                this.reset();
                                this.clearValue();
                                doSupplierSearchSelectOnChange();
//                                loadQueueGrid(true);
//                                searchClaim(true);
                            } else if (supplierComboNumberOfSelectedRecord >=1) {
                                supplierComboNumberOfSelectedRecord --;
                                doSupplierSearchSelectOnChange();
//                                loadQueueGrid(true);
//                                searchClaim(true);
                            }
                            doLayoutSearchPanel(); 
                        }
                    }
                });
//                supplierSearchScreenCombo.render('searchScreenSupplierDropDownDiv');
                <%--</s:if>--%>  

                <%--<s:if test="isInsurer!=true || (isInsurer && insurerIsWorkgroupEnabled)" >--%>
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

                workgroupSearchScreenStore = new choxDataStore({
                    url : "/prv/p/WorkgroupDropDownActionByInsurer2.action", 
                    params : {"orgId": insurerSearchScreenId},
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
                    width: 200,
                    fieldLabel: 'Workgroup',
                    disabled : !((!<s:property value="isInsurer"/>) || (<s:property value="isInsurer"/> && <s:property value="insurerIsWorkgroupEnabled"/>)),
                    hidden : !((!<s:property value="isInsurer"/>) || (<s:property value="isInsurer"/> && <s:property value="insurerIsWorkgroupEnabled"/>)),
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
                            doLayoutSearchPanel();
//                            searchClaim(true);
                        },
                        removeitem : function() {
                            if (!this.getValue() && workgroupComboNumberOfSelectedRecord >= 1) {
                                workgroupComboNumberOfSelectedRecord = 0;
                                this.reset();
                                this.clearValue();
                                doSearchWorkgroupOnChange();
//                                searchClaim(true);
                            } else if (workgroupComboNumberOfSelectedRecord >= 1) {
                                workgroupComboNumberOfSelectedRecord --;
                                doSearchWorkgroupOnChange();
//                                searchClaim(true);
                            }
                            doLayoutSearchPanel();
                        }
                    }
                });
            
//                workgroupSearchScreenCombo.render('searchScreenWorkgroupDropDownDiv');
                <%--</s:if>--%>
        
            // Add claim owner combo box
       

                <%--<s:if test="isInsurer!=true || (isInsurer && insurerIsClaimOwnershipEnabled)" >--%>
        
                var claimOwnerReader = new Ext.data.JsonReader({
                    totalProperty: 'totalCount',
                    root: 'results',
                    fields:
                        [
                        {name:'id'},
                        {name:'name'}
                    ]
                });
        
                claimOwnerSearchScreenStore = new choxDataStore({
                    url : "/prv/p/SearchClaimHandlerRoleUserDropDownAction.action", 
                    params : {"workgroupId": workgroupSearchScreenId,"insurerId": insurerSearchScreenId},
                    reader : claimOwnerReader,
                    listeners: {load: function() {/*this.insert(0, new Ext.data.Record(claimOwnerdefaultDropdownValue));*/
                         if (selectedInsClaimOwnerValues && claimOwnerSearchScreenCombo) {claimOwnerSearchScreenCombo.reset();claimOwnerSearchScreenCombo.setValue(selectedInsClaimOwnerValues);}
                    }}
                });
                // below variable is hack to stop superBoxSelect call searchClaim Function multiple times when all recored cleard at once.
                var claimOwnerComboNumberOfSelectedRecord = 0;
                claimOwnerSearchScreenCombo = new Ext.ux.form.SuperBoxSelect({
                    store : claimOwnerSearchScreenStore,
                    width: 200,
                    fieldLabel: (<s:property value="isInsurer"/>) ? 'Claim Owner' : 'Insurer\'s Claim Owner',
                    disabled : !((!<s:property value="isInsurer"/>) || (<s:property value="isInsurer"/> && <s:property value="insurerIsClaimOwnershipEnabled"/>)),
                    hidden : !((!<s:property value="isInsurer"/>) || (<s:property value="isInsurer"/> && <s:property value="insurerIsClaimOwnershipEnabled"/>)),
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
                            doLayoutSearchPanel();
//                            searchClaim(true);
                        },
                        removeitem : function() {
                            if (!this.getValue() && claimOwnerComboNumberOfSelectedRecord >=1) {
                                claimOwnerComboNumberOfSelectedRecord = 0;
                                this.reset();
                                this.clearValue();
//                                searchClaim(true); 
                            } else if (claimOwnerComboNumberOfSelectedRecord >= 1) {
                                claimOwnerComboNumberOfSelectedRecord --;
//                                searchClaim(true); 
                            }
                            doLayoutSearchPanel();
                        }
                    }
                });

//                claimOwnerSearchScreenCombo.render('searchScreenClaimhandlerDownDiv');
                <%--</s:if>--%>
        
        
            // Add CHO claim owner combo box
      

                <%--<s:if test="isCHO!=true || (isCHO && choIsClaimOwnershipEnabled)" >--%>
        
        
                var supplierClaimOwnerReader = new Ext.data.JsonReader({
                    totalProperty: 'totalCount',
                    root: 'results',
                    fields:
                        [
                        {name:'id'},
                        {name:'name'}
                    ]
                });
            
                supplierClaimOwnerSearchScreenStore = new choxDataStore({
                    url : "/prv/p/SearchSupplierClaimOwnerDropDownAction.action", 
                    params : {"supplierId": supplierSearchScreenId},
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
                    width: 200,
                    fieldLabel: <s:property value="isCHO"/> ? 'Claim Owner' : 'Supplier Claim Owner',
                    disabled : !((!<s:property value="isCHO"/>) || (<s:property value="isCHO"/> && <s:property value="choIsClaimOwnershipEnabled"/>)),
                    hidden : !((!<s:property value="isCHO"/>) || (<s:property value="isCHO"/> && <s:property value="choIsClaimOwnershipEnabled"/>)),
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
                            doLayoutSearchPanel();
//                            searchClaim(true);
                        },
                        removeitem : function() {
                            if (!this.getValue() && supplierClaimOwnerComboNumberOfSelectedRecord >=1) {
                                supplierClaimOwnerComboNumberOfSelectedRecord = 0;
                                this.reset();
                                this.clearValue();
//                                searchClaim(true); 
                            } else if (supplierClaimOwnerComboNumberOfSelectedRecord >=1) {
                                supplierClaimOwnerComboNumberOfSelectedRecord --;
//                                searchClaim(true); 
                            }
                            doLayoutSearchPanel();
                        }
                    }
                });

//                supplierClaimOwnerSearchScreenCombo.render('searchScreenSupplierClaimOwnerDropDownDiv');
                <%--</s:if>--%>
        
        

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
            
            statusSearchScreenCombo = new Ext.ux.form.SuperBoxSelect({
                store : statusesStore,
                width: 180,
                fieldLabel: 'Status',
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
                        doLayoutSearchPanel();
//                        searchClaim(true);
                    },
                    removeitem : function() { 
                        if (!this.getValue() && statusComboNumberOfSelectedRecord >= 1) {
                            statusComboNumberOfSelectedRecord = 0;
                            this.reset();
                            this.clearValue();
                            statusChange();
//                            searchClaim(true);
                         } else if (statusComboNumberOfSelectedRecord >= 1) {
                            statusComboNumberOfSelectedRecord --;
                            statusChange();
//                            searchClaim(true);  
                         }
                         doLayoutSearchPanel();
                    }
                }
            });

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
                width: 200,
                fieldLabel: 'Liability Status',
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
                        doLayoutSearchPanel();
//                        searchClaim(true);
                    },
                    removeitem : function() {
                        if (!this.getValue() && liabilityStatusComboNumberOfSelectedRecord >= 1) {
                            liabilityStatusComboNumberOfSelectedRecord = 0;
                            this.reset();
                            this.clearValue();
                            statusChange();
//                            searchClaim(true);
                            } else if (liabilityStatusComboNumberOfSelectedRecord >= 1){
                                liabilityStatusComboNumberOfSelectedRecord --;
                                statusChange();
//                                searchClaim(true);  
                            }
                            doLayoutSearchPanel();
                    }
                }
            });

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
                width: 200,
                fieldLabel: 'Hire & Repair Management Status',
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
                        doLayoutSearchPanel();
//                        searchClaim(true);
                    },
                    removeitem : function() {
                        if (!this.getValue() && hireAndRepairSearchComboNumberOfSelectedRecord >=1) {
                            hireAndRepairSearchComboNumberOfSelectedRecord = 0;
                            this.reset();
                            this.clearValue();
                            statusChange();
//                            searchClaim(true);
                        } else if (hireAndRepairSearchComboNumberOfSelectedRecord >= 1){
                            hireAndRepairSearchComboNumberOfSelectedRecord --;
                            statusChange();
//                            searchClaim(true); 
                        }
                        doLayoutSearchPanel();
                    }
                }
            });
            
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
            claimTypesComboNumberOfSelectedRecord = 0;
            claimTypesSearchScreenCombo = new Ext.ux.form.SuperBoxSelect({
                store : claimTypesStore,
                width: 180,
                fieldLabel: 'Claim Type',
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
//                        loadQueueGrid(true);
                        doLayoutSearchPanel();
//                        searchClaim(true);
                    },
                    removeitem : function() {
                        if (!this.getValue() && claimTypesComboNumberOfSelectedRecord >=1) {
                            claimTypesComboNumberOfSelectedRecord = 0;
                            this.reset();
                            this.clearValue();
                            statusChange();
//                            loadQueueGrid(true);
//                            searchClaim(true);
                            } else if (claimTypesComboNumberOfSelectedRecord >= 1){
                                claimTypesComboNumberOfSelectedRecord --;
                                statusChange();
//                                loadQueueGrid(true);
//                                searchClaim(true); 
                            }
                            doLayoutSearchPanel();
                    }
                }
            });

            // Create the search and reset buttons
            var searchButton = new Ext.Button({
                text: 'Search',
                scale : 'medium',
                width : 100,
                handler: function(button, event) {
                    searchClaim(true);
                }
            });

            var resetButton = new Ext.Button({
                text: 'Reset',
                width : 100,
                scale : 'medium',
                handler: function(button, event) {
                    clearForm();
                    loadQueueGrid(false);
                    var rowIndex = Ext.state.Manager.get("recentlyClickedQueueRowNumber");
                    if (typeof rowIndex !== 'undefined') { 
                        queueGrid.getSelectionModel().selectRow(rowIndex);
                    }
//                    searchClaim(true);
                }
            });
            
            var leftColumn = {
                width:310,
                height : 'auto',
                style: {
                    paddingLeft:'10px'
                },
                layout: 'form',
                items: [supplierReferenceField,claimNumberField,
                            invoiceNumberField, customerVrnField, 
                            thirdPartyVrnField, 
                            statusSearchScreenCombo, claimTypesSearchScreenCombo, 
                            reviewRequiredDateFromPicker, reviewRequiredDateToPicker, 
                            supplementaryInvoicedClaimsCheckBox]
            };

            var middleColumn = {
                width:280,
                height : 'auto',
                layout: 'form',
                items: [claimUploadDateFromPicker, claimUploadDateToPicker,
                        statusModifiedDateFromPicker, statusModifiedDateToPicker,
                        invoiceUploadDateFromPicker, invoiceUploadDateToPicker, 
                        rentalStartDatePicker, rentalEndDatePicker, openClaimsCheckBox,
                        anomaliesCheckBox, escalatedToSupervisorCheckBox,
                        interimPaymentMadeCheckBox]
            };

            var rightColumn = {
                width:350,
                height : 'auto',
                layout: 'form',
                items: [insurerSearchScreenCombo, supplierSearchScreenCombo, 
                            workgroupSearchScreenCombo, claimOwnerSearchScreenCombo, 
                            supplierClaimOwnerSearchScreenCombo, liabilityStatusSearchScreenCombo, 
                            hireAndRepairSearchParamCombo, penaltyChargesAppliedCheckBox, 
                            penaltyChargesToBeAppliedCheckBox, liabilityStatusUpdateNotification,
                            finalReviewChoCheckBox, finalReviewInsCheckBox]
            };
            
            var queueReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'queueName'},
                    {name:'queueClaimsCount'},
                    {name:'key'},
                    {name:'description'},
                    {name:'searchParam'},
                    {name:'claimSearchCriteria'}
                ]
            });
            
            queueDataStore = new choxDataStore({
                url: '/prv/p/getQueues.action',
                reader: queueReader,
                timeout:1800000,
                listeners:  {
                    load :  function(store, records, options) {
//                                console.log(options);
                                if (options.searchScreenTrigger) { 
//                                    console.log("setting isSearchScreenSearch to true");
                                    isSearchScreenSearch = true;
                                } else { 
//                                    console.log("setting isSearchScreenSearch to false");
                                    isSearchScreenSearch = false;
                                }
                                selectPreviouslySelectedQueue();
                            }
                },
            });
            
            queueGrid = new Ext.grid.GridPanel({
                store: queueDataStore,
                id : 'QueueGridId',
                enableColumnMove: false,
                enableHdMenu:false,
                selModel : new Ext.grid.RowSelectionModel({ // best practice to use selectionModel instead of grid rowClick listner.
                                listeners:  {rowselect : onQueueSelection}
                           }),
                columns: [
                    {
                        id       :'queueNameId',
                        sortable : false, 
                        dataIndex: 'description'
                    }
                ],
                stripeRows: true,
                autoExpandColumn: 'queueNameId',
                height: 420,
                width: 300,
//                tbar : syncWithSearchPanelToolBar,
                loadMask : {msg:"Loading Queues..."},
                view : new Ext.grid.GridView({ // this is to hide the vertical bar space when the vertical bar is not shown.
                            forceFit    : true,
                            scrollOffset: 0,
                            getTotalWidth: function() {
                                return "auto";
                            }
                })
            });

           
            searchColumsPanel = new Ext.Panel({
                layout : 'hbox',
                width : 970,
                frame : true,
                height : 420,
                autoScroll : true,
                items : [leftColumn,middleColumn,rightColumn],
                buttons : [searchButton, resetButton],
                buttonAlign : 'center'
            });
            
            new Ext.Panel({
                layout : 'hbox',
                items : [queueGrid, searchColumsPanel],
                renderTo : 'searchPanel',
                listeners:  {afterrender : loadQueueGrid}
            });

        });

        function loadQueueGrid(searchScreenTrigger) {
            queueDataStore.baseParams = getSearchParameters();
            if (searchScreenTrigger === true) {
                queueDataStore.load({params: {'searchScreenTrigger' : true, 'syncWithSearchCriteria' : false}});
            } else {
                queueDataStore.load({params: {'searchScreenTrigger' : false, 'syncWithSearchCriteria' : false}});
            }
        }
        
        function deSelectQueue() {
            var rowIndex = Ext.state.Manager.get("recentlyClickedQueueRowNumber");
            Ext.state.Manager.set("recentlyClickedQueueRowNumber", null);
            if (typeof rowIndex !== 'undefined') { 
                queueGrid.getSelectionModel().deselectRow(rowIndex);
            }
        }
        
//        function reSelectQueue() {
//            var rowIndex = Ext.state.Manager.get("recentlyClickedQueueRowNumber");
//            if (typeof rowIndex !== 'undefined') {
//                queueGrid.getSelectionModel().deselectRow(rowIndex);
//                queueGrid.getSelectionModel().selectRow(rowIndex);
//            }
//        }
        
        function selectPreviouslySelectedQueue() {
//            console.log("In selectPreviouslySelectedQueue method");
            var rowIndex = Ext.state.Manager.get("recentlyClickedQueueRowNumber");
            if (typeof rowIndex !== 'undefined') { 
//                console.log("rowIndex is present, that means queue is previously selected.");
                // if it is loaded from session then the claims grid is loaded using session base param when rendered. No need to load it from here.
                if (!isSearchScreenSearch) {
                    canLoadClaimsOnQueueSelection = false;
//                    console.log("canLoadClaimsOnQueueSelection set to false to prevent duplicate claim grid data loading.");
                }
                queueGrid.getSelectionModel().selectRow(rowIndex);
                // code after the below lines are not executed, need investigation.
                queueGrid.getView().focusRow(queueGrid.getSelectionModel().hasNext() ? rowIndex+1 : rowIndex);
            } else {
                if (isSearchScreenSearch) { 
//                    console.log("isSearchScreenSearch is true so change isSearchScreenSearch to false and return without further action.");
                    isSearchScreenSearch = false;
                    return;
                }
            }
        }
        
        function onQueueSelection(rsm, rowIndex, record) { 
//            console.log("In onQueueSelection method.");
//            claimsGridTitle = "Queue: "+ record.get('queueName');
            var baseParams;
            if (canLoadClaimsOnQueueSelection) {
//                console.log("canLoadClaimsOnQueueSelection check passed, that means claim grid can be loaded on queue click");
                var queueFilterName = record.get('key');
//                    console.log("isQueueSyncWithSearchField is true, so need to update the search screen with queue search criteria.");
                if (isSearchScreenSearch) { 
//                        console.log("isSearchScreenSearch is true so change isSearchScreenSearch to false.");
                    isSearchScreenSearch = false;
//                        return;
                } else {
                    updateSearchScreenFieldsWithQueueFilterCriteria(record);
                }
                baseParams = Ext.apply(getSearchParameters(), {"filterName" : queueFilterName});
                
                Ext.state.Manager.set("recentlyClickedQueueRowNumber", rowIndex);
                doDataLoad(baseParams);
            } else { 
//                console.log("canLoadClaimsOnQueueSelection check failed, that means claims grid can not be loaded this time by the queue selection.");
                // if it is loaded from session then the claims grid is loaded using session base param when rendered. No need to load it from here.
//                baseParams = Ext.state.Manager.get("claims_grid_baseParams");
                // change canLoadClaimsOnQueueSelection to true so next time the claims grid is loaded when queue is clicked.
                canLoadClaimsOnQueueSelection = true;
                if (isSearchScreenSearch) { 
//                    console.log("isSearchScreenSearch is true so setting it to false and return without further action.");
                    isSearchScreenSearch = false;
                    return;
                }
            }
            
            // The below line need to be investigated
            updateManualInvoiceBatchUpdate(queueFilterName);
        }

        function updateSearchScreenFieldsWithQueueFilterCriteria(record) {

            clearForm();
            var statuses = record.get('claimSearchCriteria').claimStatusesAsString;
            statusComboNumberOfSelectedRecord = statuses.split(',').length;
//            console.log('statusComboNumberOfSelectedRecord  count is = ' +statusComboNumberOfSelectedRecord);
            if (statuses) { 
//                console.log('statuses not empty so setting the value = ' + statuses);
                statusSearchScreenCombo.setValue(statuses);
            }
            
            var claimType = record.get('claimSearchCriteria').claimTypesValueAsString;
            claimTypesComboNumberOfSelectedRecord = claimType.split(',').length;
            if (claimType) {
//                console.log('claimType not empty so setting the value = ' + claimType);
                claimTypesSearchScreenCombo.setValue(claimType);
            }
            
            var isLiabilityUpdated = record.get('claimSearchCriteria').liabilityStatusUpdated;
            if (isLiabilityUpdated) {
                Ext.getCmp('liabilityStatusUpdatedId').setValue(true);
            }
            
            var isPenaltyChargesApplied = record.get('claimSearchCriteria').isPenaltyChargeApplied;
            if (isPenaltyChargesApplied) {
                Ext.getCmp('penaltyChargesToBeAppliedCheckBoxId').setValue(true);
            }
            
            var finalReviewCho = record.get('claimSearchCriteria').finalReviewCho;
            if (finalReviewCho) {
                Ext.getCmp('finalReviewChoCheckBoxId').setValue(true);
            }
            
            var finalReviewIns = record.get('claimSearchCriteria').finalReviewIns;
            if (finalReviewIns) {
                Ext.getCmp('finalReviewInsCheckBoxId').setValue(true);
            }
            
            var anomalies = record.get('claimSearchCriteria').isAnomalies;
            if (anomalies) {
                Ext.getCmp('anomaliesCheckBoxId').setValue(true);
            }
            
            var escalatedToSupervisor = record.get('claimSearchCriteria').escalatedToSupervisor;
            if (escalatedToSupervisor) {
                Ext.getCmp('escalatedToSupervisorCheckBoxId').setValue(true);
            }
            
            var interimPaymentMade = record.get('claimSearchCriteria').isInterimPaymentMade;
            if (interimPaymentMade) {
                Ext.getCmp('interimPaymentMadeCheckBoxId').setValue(true);
            }
            
        }
        
        function getSearchParameters() {
            
            var supplierReference = Ext.query('*[name$=supplierReference]')[0].value;
            if (Ext.getCmp('searchScreenSupplierComboId'))
                var supplierIds = Ext.getCmp('searchScreenSupplierComboId').getValue().split(",");

            if (Ext.getCmp('searchScreenInsurerComboId'))
                var insurerIds = Ext.getCmp('searchScreenInsurerComboId').getValue().split(",");

            var invoiceNumber = Ext.query('*[name$=invoiceNumber]')[0].value;
            var claimNumber = Ext.query('*[name$=claimNumber]')[0].value;
            var thirdPartyVrn = Ext.query('*[name$=thirdPartyVrn]')[0].value;
            var claimUploadDateFrom = Ext.query('*[name$=claimUploadDateFrom]')[0].value;
            var claimUploadDateTo = Ext.query('*[name$=claimUploadDateTo]')[0].value;

            var statusModifiedDateFrom = Ext.query('*[name$=statusModifiedDateFrom]')[0].value;
            var statusModifiedDateTo = Ext.query('*[name$=statusModifiedDateTo]')[0].value;

            var invoiceUploadDateFrom = Ext.query('*[name$=invoiceUploadDateFrom]')[0].value;
            var invoiceUploadDateTo = Ext.query('*[name$=invoiceUploadDateTo]')[0].value;
            var rentalStartDate = Ext.query('*[name$=rentalStartDate]')[0].value;
            var rentalEndDate = Ext.query('*[name$=rentalEndDate]')[0].value;
            var statuses = Ext.getCmp('statusSearchScreenComboId').getValue().split(",");

            if (Ext.getCmp('searchScreenWorkgroupComboId'))
                var workgroupIds = Ext.getCmp('searchScreenWorkgroupComboId').getValue().split(",");

            var reviewRequiredDateFrom = Ext.query('*[name$=reviewRequiredDateFrom]')[0].value;
            var reviewRequiredDateTo = Ext.query('*[name$=reviewRequiredDateTo]')[0].value;

            if (Ext.getCmp('searchScreenClaimOwnerComboId'))
                var claimOwnerIds = Ext.getCmp('searchScreenClaimOwnerComboId').getValue().split(",");

            if (Ext.getCmp('searchScreenSupplierClaimOwnerComboId'))
                var supplierClaimOwnerIds = Ext.getCmp('searchScreenSupplierClaimOwnerComboId').getValue().split(",");

            var customerVrn = Ext.query('*[name$=customerVrn]')[0].value;
            var showOpenClaimsOnly = Ext.query('*[name$=showOpenClaimsOnly]')[0].checked;
            var liabilityStatusUpdated = Ext.query('*[name$=liabilityStatusUpdated]')[0].checked;
            var isSupplementaryInvoiceOnly = Ext.query('*[name$=isSupplementaryInvoiceOnly]')[0].checked;
            var penaltyChargesAppliedOnly = Ext.query('*[name$=penaltyChargesAppliedOnly]')[0].checked;
            var penaltyChargesToBeApplied = Ext.query('*[name$=isPenaltyChargeApplied]')[0].checked;
            var finalReviewIns = Ext.query('*[name$=finalReviewIns]')[0].checked;
            var finalReviewCho = Ext.query('*[name$=finalReviewCho]')[0].checked;
            var anomalies = Ext.query('*[name$=isAnomalies]')[0].checked;
            var escalatedToSupervisor = Ext.query('*[name$=escalatedToSupervisor]')[0].checked;
            var interimPaymentMade = Ext.query('*[name$=isInterimPaymentMade]')[0].checked;
            var liabilityStatuses = Ext.getCmp('liabilityStatusSearchScreenComboId').getValue().split(",");
            var claimTypes = Ext.getCmp('claimTypesSearchScreenComboId').getValue().split(",");
            var hireAndRepairSearchScreen = Ext.getCmp('hireAndRepairSearchParamComboId').getValue().split(",");

            return {
                filterName : '',
                canLoadData : true,
                supplierReference : supplierReference,
                supplierIds : supplierIds,
                insurerIds : insurerIds,
                invoiceNumber : invoiceNumber,
                claimNumber : claimNumber,
                thirdPartyVrn : thirdPartyVrn,
                claimUploadDateFrom : claimUploadDateFrom,
                claimUploadDateTo : claimUploadDateTo,
                statusModifiedDateFrom : statusModifiedDateFrom,
                statusModifiedDateTo :  statusModifiedDateTo,
                invoiceUploadDateFrom : invoiceUploadDateFrom,
                invoiceUploadDateTo : invoiceUploadDateTo,
                rentalStartDate : rentalStartDate,
                rentalEndDate : rentalEndDate,
                statuses : statuses,
                workgroupIds : workgroupIds,
                reviewRequiredDateFrom : reviewRequiredDateFrom,
                reviewRequiredDateTo : reviewRequiredDateTo,
                claimOwnerIds : claimOwnerIds,
                supplierClaimOwnerIds : supplierClaimOwnerIds,
                customerVrn : customerVrn,
                showOpenClaimsOnly : showOpenClaimsOnly,
                finalReviewIns : finalReviewIns,
                finalReviewCho : finalReviewCho,
                liabilityStatusUpdated : liabilityStatusUpdated,
                penaltyChargesAppliedOnly : penaltyChargesAppliedOnly,
                isPenaltyChargeApplied : penaltyChargesToBeApplied,
                isAnomalies : anomalies,
                escalatedToSupervisor : escalatedToSupervisor,
                isInterimPaymentMade : interimPaymentMade,
                liabilityStatuses : liabilityStatuses,
                claimTypes : claimTypes,
                isSupplementaryInvoiceOnly : isSupplementaryInvoiceOnly,
                hireAndRepairSearchParamIds : hireAndRepairSearchScreen
            };
        }
        
        function doLayoutSearchPanel() {
            if (searchColumsPanel) {
                searchColumsPanel.doLayout();
            }
        }
        
        function searchClaim(canSearchForData) {
            /*
             *  if canSearchForData is false then no data will be returned. this is mainly used to reset the search screen form.
             */
            var searchBaseParam = {canLoadData : canSearchForData};
            if (canSearchForData) {
                searchBaseParam = getSearchParameters();
            }
            doDataLoad(searchBaseParam);
        }
        
        function doDataLoad(baseParams) { 
//            console.log("doDataLoad method loading the grid");
            claimStore.baseParams = baseParams;
            claimStore.load({params:{start: 0, limit: recordPerPage}});
        }
        
        function setSelectedInsurerId() {
            if (insurerSearchScreenCombo.getValue() !== null && insurerSearchScreenCombo.getValue() !== '') {
                insurerSearchScreenId = insurerSearchScreenCombo.getValue().split(",");
            }
            else {
                insurerSearchScreenId = null;
            }
        }

        function setSelectedSupplierId() {
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

            $('#searchPanel').contents().find(':input').each(function() {

                var type = this.type;
                var tag = this.tagName.toLowerCase();

                if (type === 'text' || type === 'password' || tag === 'textarea'){
                    this.value = "";
                }else if(tag === 'select'){
                    this.selectedIndex = 0;
                }
            });

            $('#searchPanel').contents().find(':checkbox').each(function() {
                if(this.id==='showOpenClaimsOnlyId'){
                    this.checked = true;
                } else {
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

<div id="searchPageHolder" class="search-panel-holder">
    <div id="searchPanel"></div>
</div>
