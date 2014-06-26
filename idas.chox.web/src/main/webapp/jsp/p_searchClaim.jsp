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
    var approvedInvoiceOwnershipSearchParamCombo;
    var searchColumsPanel;
    var queueDataStore;
    var queueGrid;
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
                width: 160,
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
                width: 160,
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
                width: 160,
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
                width: 160,
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
                width: 160,
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
                disabled : <s:property value="isInsurer"/>,
                hidden : <s:property value="isInsurer"/>,
                value:'<s:property value="liabilityStatusUpdated"/>',
                fieldLabel: 'Liability Status Updated',
                labelStyle: 'width:245px',
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
                value:'<s:property value="supplementaryInvoiceOnly"/>',
                fieldLabel: 'Show Claims With Supp. Invoice(s) Only',
                labelStyle: 'width:245px',
                checked: <s:property value="supplementaryInvoiceOnly"/>,
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
                labelStyle: 'width:245px',
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
                disabled : <s:property value="isInsurer"/>,
                hidden : <s:property value="isInsurer"/>,
                value:'<s:property value="penaltyChargeApplied"/>',
                fieldLabel: 'Penalty Charges To Be Applied',
                labelStyle: 'width:245px',
                checked: <s:property value="penaltyChargeApplied"/>,
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
                labelStyle: 'width:150px',
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
                labelStyle: 'width:245px',
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
                value:'<s:property value="anomalies"/>',
                disabled : <s:property value="isCHO"/>,
                hidden : <s:property value="isCHO"/>,
                fieldLabel: 'Is Anomalies',
                labelStyle: 'width:150px',
                checked: <s:property value="anomalies"/>,
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
                disabled : <s:property value="isCHO"/>,
                hidden : <s:property value="isCHO"/>,
                value:'<s:property value="escalatedToSupervisor"/>',
                fieldLabel: 'Escalated To Supervisor',
                labelStyle: 'width:245px',
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
                disabled : <s:property value="isInsurer"/>,
                hidden : <s:property value="isInsurer"/>,
                value:'<s:property value="interimPaymentMade"/>',
                fieldLabel: 'Is Interim Payment Made',
                labelStyle: 'width:150px',
                checked: <s:property value="interimPaymentMade"/>,
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
                labelStyle: 'width:140px',
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
                labelStyle: 'width:140px',
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
                    width: 280,
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
                    width: 280,
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
                    width: 280,
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
                    width: 280,
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
                    width: 280,
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
                width: 280,
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
                width: 280,
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
                width: 280,
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
                        if ('<s:property value="hireAndRepairSearchParamAsString"/>') {
                            this.setValue('<s:property value="hireAndRepairSearchParamAsString"/>');
                            hireAndRepairSearchComboNumberOfSelectedRecord = '<s:property value="hireAndRepairSearchParamAsString"/>'.split(',').length;
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
 
            var approvedInvoiceOwnershipSearchParamData = [['Claims Handlers', 1], ['Payments Team', 2]];
            
            var approvedInvoiceOwnershipSearchParamStore = new Ext.data.ArrayStore({
                    fields: [
                       {name: 'text', type: 'string'},
                       {name: 'value', type: 'int'}
                    ]
            });
            
            approvedInvoiceOwnershipSearchParamStore.loadData(approvedInvoiceOwnershipSearchParamData);
            // below variable is hack to stop superBoxSelect call searchClaim Function multiple times when all recored cleard at once.
            var approvedInvoiceOwnershipSearchComboNumberOfSelectedRecord = 0;
            
            approvedInvoiceOwnershipSearchParamCombo = new Ext.ux.form.SuperBoxSelect({
                store : approvedInvoiceOwnershipSearchParamStore,
                width: 200,
                fieldLabel: 'Approved Invoices Ownership',
                valueField : 'value',
                id : 'approvedInvoiceOwnershipSearchParamComboId',
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
                        if ('<s:property value="approvedInvoiceOwnershipSearchParamAsString"/>') {
                            this.setValue('<s:property value="approvedInvoiceOwnershipSearchParamAsString"/>');
                            approvedInvoiceOwnershipSearchComboNumberOfSelectedRecord = '<s:property value="approvedInvoiceOwnershipSearchParamAsString"/>'.split(',').length;
                        }
                    },
                    select : function(){
                        approvedInvoiceOwnershipSearchComboNumberOfSelectedRecord ++;
                        statusChange();
                        doLayoutSearchPanel();
//                        searchClaim(true);
                    },
                    removeitem : function() {
                        if (!this.getValue() && approvedInvoiceOwnershipSearchComboNumberOfSelectedRecord >=1) {
                            approvedInvoiceOwnershipSearchComboNumberOfSelectedRecord = 0;
                            this.reset();
                            this.clearValue();
                            statusChange();
//                            searchClaim(true);
                        } else if (approvedInvoiceOwnershipSearchComboNumberOfSelectedRecord >= 1){
                            approvedInvoiceOwnershipSearchComboNumberOfSelectedRecord --;
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
                width: 280,
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
                scale : 'small',
                width : 100,
                handler: function(button, event) {
                    searchClaim(true);
                }
            });

            var resetButton = new Ext.Button({
                text: 'Reset',
                width : 100,
                scale : 'small',
                handler: function(button, event) {
                    if (!selectPreviouslySelectedQueue(true)) {
                        // If none of the queue is selected previously then reset the search form.
                        searchClaim(false);
                    }
                }
            });
            
            var leftColumn = {
                width:280,
                height : 'auto',
                style: {
                    paddingLeft:'10px'
                },
                layout: 'form',
                items: [supplierReferenceField, claimNumberField,
                            invoiceNumberField, customerVrnField, 
                            thirdPartyVrnField, reviewRequiredDateFromPicker, 
                            reviewRequiredDateToPicker,
                            supplementaryInvoicedClaimsCheckBox, penaltyChargesAppliedCheckBox,
                            penaltyChargesToBeAppliedCheckBox, liabilityStatusUpdateNotification, 
                            finalReviewInsCheckBox, escalatedToSupervisorCheckBox
                        ]
            };

            var middleColumn = {
                width:280,
                height : 'auto',
                layout: 'form',
                items: [claimUploadDateFromPicker, claimUploadDateToPicker,
                        statusModifiedDateFromPicker, statusModifiedDateToPicker,
                        invoiceUploadDateFromPicker, invoiceUploadDateToPicker, 
                        rentalStartDatePicker, rentalEndDatePicker, openClaimsCheckBox,
                        anomaliesCheckBox, interimPaymentMadeCheckBox,
                        finalReviewChoCheckBox]
            };

            var rightColumn = {
                width:400,
                height : 'auto',
                layout: 'form',
                items: [statusSearchScreenCombo, claimTypesSearchScreenCombo, 
                            insurerSearchScreenCombo, supplierSearchScreenCombo, 
                            workgroupSearchScreenCombo, claimOwnerSearchScreenCombo, 
                            supplierClaimOwnerSearchScreenCombo, liabilityStatusSearchScreenCombo,
                            approvedInvoiceOwnershipSearchParamCombo,
                            hireAndRepairSearchParamCombo]
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
                                if (options.searchScreenTrigger) { 
                                    selectPreviouslySelectedQueue(true);
                                } else { 
                                    selectPreviouslySelectedQueue(false);
                                }
                            }
                }
            });
            
            queueGrid = new Ext.grid.GridPanel({
                store: queueDataStore,
                id : 'QueueGridId',
                enableColumnMove: false,
                frame : true,
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
                height: 430,
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
                width : 980,
                frame : true,
                height : 370,
                autoScroll : true,
                items : [leftColumn,middleColumn,rightColumn],
                headerAsText : true,
//                headerCfg: {cls: 'search-panel-status-info'},
                title : '<div class="search-panel-status-info">Search Screen Information Panel</div>'
//                buttons : [searchButton, resetButton], // moved to separate panel.
//                buttonAlign : 'center'
            });
            
            var buttonPanel = new Ext.Panel({
                buttons : [searchButton, resetButton],
                frame : true,
                width : 980,
                height : 30,
                buttonAlign : 'center'
            });
            
            // We need to create another button panel to separate the search panel frame from search and reset button.
            // This is needed because when search panel size increase vertically we need to have separate frame to visually identify some search fields is hidden.
            var searchAndButtonPanel = new Ext.Panel({
                width : 1000,
                height : 430,
                frame : true,
                items : [searchColumsPanel, buttonPanel]
            });
            
            
            
            new Ext.Panel({
                layout : 'hbox',
                items : [queueGrid, searchAndButtonPanel],
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
        
        function selectPreviouslySelectedQueue(canLoadClaimsGridData) {
            
            var rowIndex = Ext.state.Manager.get("recentlyClickedQueueRowNumber");
            if (typeof rowIndex !== 'undefined') {
                if (canLoadClaimsGridData) {
                    queueGrid.getSelectionModel().selectRow(rowIndex);
                } else {
                    // remove rowSelect listner to avoid loading the claims grid data when the queue is selected.
                    queueGrid.getSelectionModel().removeListener('rowselect', onQueueSelection);
                    queueGrid.getSelectionModel().selectRow(rowIndex);
                    // add the rowSelect listner back to activate loading the claims grid when the queue is selected.
                    queueGrid.getSelectionModel().addListener('rowselect', onQueueSelection);
                }
                queueGrid.getView().focusRow(queueGrid.getSelectionModel().hasNext() ? rowIndex+1 : rowIndex);
                return true; // this line is not working. All the code below the above focusRow method line is not working. Need investigation.
            }
            return false;
        }
        
        function resetQueue(canLoadClaimsGridData) {
            
            if (canLoadClaimsGridData) {
                queueGrid.getSelectionModel().selectRow(0);
            } else {
                // remove rowSelect listner to avoid loading the claims grid data when the queue is selected.
                queueGrid.getSelectionModel().removeListener('rowselect', onQueueSelection);
                queueGrid.getSelectionModel().selectRow(0);
                Ext.state.Manager.set("recentlyClickedQueueRowNumber", 0);
                // add the rowSelect listner back to activate loading the claims grid when the queue is selected.
                queueGrid.getSelectionModel().addListener('rowselect', onQueueSelection);
            }
            
        }
        
        function onQueueSelection(rsm, rowIndex, record) { 

            var queueFilterName = record.get('key');
            var queueName = record.get('queueName');
            // populate the necessery search criteria in the search panel.
            updateSearchScreenFieldsWithQueueFilterCriteria(record);
            var baseParams = Ext.apply(getSearchParameters(), {"filterName" : queueFilterName, "gridTitle" : queueName});
            Ext.state.Manager.set("recentlyClickedQueueRowNumber", rowIndex);
            // load the claims grid data.
            doDataLoad(baseParams);
            // set the searchPanel information message
            setSearchPanelInfo(queueName);
            // we need layout the search panel here because incase if the size of the search panel increased 
            // as a result of setting up queue search criteria in the search panel.
            doLayoutSearchPanel();
            // The below line need to be investigated
            updateManualInvoiceBatchUpdate(queueFilterName);
        }
        
        function setSearchPanelInfo(msg) {
            if (searchColumsPanel) {
                searchColumsPanel.setTitle('<div class="search-panel-status-info">'+msg+'</div>');
            }
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
            var approvedInvoiceOwnershipSearchScreen = Ext.getCmp('approvedInvoiceOwnershipSearchParamComboId').getValue().split(",");

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
                hireAndRepairSearchParamIds : hireAndRepairSearchScreen,
                approvedInvoiceOwnershipSearchParamIds : approvedInvoiceOwnershipSearchScreen
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
            var searchBaseParam;
            if (canSearchForData) {
                resetQueue(false);
                searchBaseParam = Ext.apply(getSearchParameters(), {"gridTitle" : 'Custom Search Result'});
                // set the searchPanel information message
                setSearchPanelInfo('Custom Search Result');
            } else { 
                searchBaseParam = {canLoadData : canSearchForData, "gridTitle" : ''};
                clearForm();
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
            approvedInvoiceOwnershipSearchParamCombo.reset();
            approvedInvoiceOwnershipSearchParamCombo.clearValue();
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
