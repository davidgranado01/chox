<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="idas.chox.core.search.ClaimSearchCriteria" %> <!--  Needed to access the CLAIM_OWNER_NOT_ASSIGNED constant -->
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var insurerSearchScreenCombo;
    var insurerSearchScreenId = -1;
    var supplierSearchScreenCombo;
    var supplierSearchScreenId = -1;
    var workgroupSearchScreenCombo;
    var workgroupSearchScreenStore;
    var workgroupSearchScreenId = -1;
    var claimOwnerSearchScreenCombo;
    var claimOwnerSearchScreenStore;
    var supplierClaimOwnerSearchScreenCombo;
    var supplierClaimOwnerSearchScreenStore;
    
    var statusSearchScreenCombo ;
    var liabilityStatusSearchScreenCombo ;
    
    Ext.onReady(function(){
        
        var  defaultDropdownValue={'value':'--- ALL ---','text':-1};
        var  liabilityStatusdefaultDropdownValue={'value':'','text':'--- ALL ---'};
        var  statusdefaultDropdownValue={'value':'--- ALL ---','text':''};
        var  claimOwnerdefaultDropdownValue={'name':'--- ALL ---','id':-1};

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



        if(<s:property value="isInsurer" />){
            insurerSearchScreenId = <s:property value="UserOrganisationId"/>;
        }else if(<s:property value="isCHO" />){
            supplierSearchScreenId = <s:property value="UserOrganisationId"/>;
        }
        
        var supplierReferenceField=new Ext.form.TextField({
            id:"supplierReferenceId",
            name:"supplierReference",
            width:220,
            allowBlank:true,
            value:'<s:property value="supplierReference"/>',
            renderTo: 'supplierReferenceFieldId',
            listeners:{
                specialkey:function (el, e) {
                    if(e.keyCode == e.ENTER) {
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
            value:'<s:property value="claimNumber"/>',
            renderTo:'claimNumberFieldId',
            listeners:{
                specialkey:function (el, e) {
                    if(e.keyCode == e.ENTER) {
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
            value:'<s:property value="invoiceNumber"/>',
            renderTo:'invoiceNumberFieldId',
            listeners:{
                specialkey:function (el, e) {
                    if(e.keyCode == e.ENTER) {
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
            value:'<s:property value="customerVrn"/>',
            renderTo:'customerVrnFieldId',
            listeners:{
                specialkey:function (el, e) {
                    if(e.keyCode == e.ENTER) {
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
            value:'<s:property value="thirdPartyVrn"/>',
            renderTo:'thirdPartyVrnFieldId',
            listeners:{
                specialkey:function (el, e) {
                    if(e.keyCode == e.ENTER) {
                        searchClaim(true);
                    }
                }
            }
        });

        var openClaimsCheckBox = new Ext.form.Checkbox({
            name:'isOpenClaim',
            id:'isOpenClaimId',
            value:'<s:property value="isOpenClaim"/>',
            renderTo:'showOpenClaimsFieldId',
            checked: <s:property value="isOpenClaim"/>,
            listeners:{
                check:function (el, e) {
                    if(e.keyCode == e.ENTER) {
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
                    if(e.keyCode == e.ENTER) {
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
                    if(e.keyCode == e.ENTER) {
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
                    if(e.keyCode == e.ENTER) {
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
                    if(e.keyCode == e.ENTER) {
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
                    if(e.keyCode == e.ENTER) {
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
        if (<s:property value="isInsurer" />)
        new Ext.form.DateField({});

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
                    if(e.keyCode == e.ENTER) {
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
                    if(e.keyCode == e.ENTER) {
                        searchClaim(true);
                    }
                },
                select : function(){
                    searchClaim(true);
                }
            }
        });

        var hireDateFromPicker = new Ext.form.DateField({
            name: 'hireDateFrom',
            renderTo: 'hireDateFromDiv',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="hireDateFrom" />',
            showWeekNumber: true,
            listeners:{
                specialkey:function (el, e) {
                    if(e.keyCode == e.ENTER) {
                        searchClaim(true);
                    }
                },
                select : function(){
                    searchClaim(true);
                }
            }
        });

        var hireDateToPicker = new Ext.form.DateField({
            name: 'hireDateTo',
            renderTo: 'hireDateToDiv',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="hireDateTo" />',
            showWeekNumber: true,
            listeners:{
                specialkey:function (el, e) {
                    if(e.keyCode == e.ENTER) {
                        searchClaim(true);
                    }
                },
                select : function(){
                    searchClaim(true);
                }
            }
        });

        if(<s:property value="isCHO" />){
            var reviewRequiredDateFromPicker = new Ext.form.DateField({
                name: 'reviewRequiredDateFrom',
                width: 120,
                allowBlank: true,
                format: 'd/m/Y',
                value: '<s:date format="dd/MM/yyyy" name="hireDateTo" />',
                showWeekNumber: true,
                listeners:{
                    specialkey:function (el, e) {
                        if(e.keyCode == e.ENTER) {
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
                        if(e.keyCode == e.ENTER) {
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
        }

        if(!<s:property value="isInsurer" />) {
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
                reader : insurersJsonReader,
                listeners: {load: function() {this.insert(0, new Ext.data.Record(defaultDropdownValue));}}
            });

            insurerSearchScreenCombo = new Ext.form.ComboBox({
                store : insurersStore,
                width: 220,
                valueField : 'text',
                id : 'searchScreenInsurerComboId',
                displayField :'value',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                valueNotFoundText : '--- ALL ---',
                selectOnFocus : true,
                forceSelection : true,
                listeners: { 
                    blur: function () {
                        if(this.getRawValue() == "" ) {
                            this.reset();
                            doInsurerSearchSelectOnChange();
                        }
                    },
                    specialkey:function (el, e) {
                        if(e.keyCode == e.ENTER) {
                            searchClaim(true);
                        }
                    },
                    afterrender : function(){
                        this.setValue(<s:property value="insurerId" />);
                        insurerSearchScreenId = <s:property value="insurerId" />;
                    },
                    select : function(){
                        doInsurerSearchSelectOnChange();
                        searchClaim(true);
                    }
                }
            });

            insurerSearchScreenCombo.render('searchScreenInsurerDropDownDiv');
        } 

        if(!<s:property value="isCHO"/>) {
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
                reader : suppliersJsonReader,
                listeners: {load: function() {this.insert(0, new Ext.data.Record(defaultDropdownValue));}}
            });
            supplierSearchScreenCombo = new Ext.form.ComboBox({
                store : suppliersStore,
                width: 220,
                valueField : 'text',
                id : 'searchScreenSupplierComboId',
                displayField :'value',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                valueNotFoundText : '--- ALL ---',
                selectOnFocus : true,
                forceSelection : true,
                listeners: { 
                    blur: function () {
                        if(this.getRawValue() == "" ) {
                            this.reset();
                            doSupplierSearchSelectOnChange();
                        }
                    },
                    specialkey:function (el, e) {
                        if(e.keyCode == e.ENTER) {
                            searchClaim(true);
                        }
                    },
                    afterrender : function(){
                        this.setValue(<s:property value="supplierId" />);
                        supplierSearchScreenId = <s:property value="supplierId" />;
                    },
                    select : function(){
                        doSupplierSearchSelectOnChange();
                        searchClaim(true);
                    }
                }
            });
            supplierSearchScreenCombo.render('searchScreenSupplierDropDownDiv');
        }  

        if(!<s:property value="isInsurer" /> || (<s:property value="isInsurer" /> && <s:property value="insurerIsWorkgroupEnabled" />)) {
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
                ({url : "<%= request.getContextPath()%>/prv/p/SearchWorkgroupDropDownAction.action", method:'GET', params : {"orgId": insurerSearchScreenId}}),
                reader : wgrpJsonReader,
                listeners: {load: function() {this.insert(0, new Ext.data.Record(defaultDropdownValue));}}
            });
            workgroupSearchScreenStore.load({ params : {"orgId": insurerSearchScreenId}});
            workgroupSearchScreenCombo = new Ext.form.ComboBox({
                store : workgroupSearchScreenStore,
                width: 220,
                valueField : 'text',
                id : 'searchScreenWorkgroupComboId',
                displayField :'value',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                valueNotFoundText : '--- ALL ---',
                selectOnFocus : true,
                forceSelection : true,
                listeners: { 
                    blur: function () {
                        if(this.getRawValue() == "" ) {
                            this.reset();
                            doSearchWorkgroupOnChange();
                        }
                    },
                    specialkey:function (el, e) {
                        if(e.keyCode == e.ENTER) {
                            searchClaim(true);
                        }
                    },
                    afterrender : function(){
                        this.setValue(<s:property value="workgroupId" />);
                        workgroupSearchScreenId = <s:property value="workgroupId" />;
                    },
                    select : function(){
                        doSearchWorkgroupOnChange();
                        searchClaim(true);
                    }
                }
            });
            
            workgroupSearchScreenCombo.render('searchScreenWorkgroupDropDownDiv');
        }
        
        // Add claim owner combo box
       

        if(!<s:property value="isInsurer" /> || (<s:property value="isInsurer" /> && <s:property value="insurerIsClaimOwnershipEnabled" />)) {
        
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
                //                listeners: {load: function() {
                // Add a 'NOT ASSIGNED' option for insurers - added in Phase3, Sprint2'
                // Removed due to bug#214
                //                   if(<s:property value="isInsurer" />) {
                //                       var notAssigned = new Array();
                // this next assignment is ugly and should be removed/refactored at some point
                //                       notAssigned['id'] = '<%= ClaimSearchCriteria.CLAIM_OWNER_NOT_ASSIGNED%>';
                //                       notAssigned['name'] = 'NOT ASSIGNED';
                //                       this.insert(0, new Ext.data.Record(notAssigned));
                //                   }
                //                }},
                reader : claimOwnerReader,
                listeners: {load: function() {this.insert(0, new Ext.data.Record(claimOwnerdefaultDropdownValue));}}
            });
            claimOwnerSearchScreenStore.load({params : {"workgroupId": workgroupSearchScreenId,"insurerId": insurerSearchScreenId}});

            claimOwnerSearchScreenCombo = new Ext.form.ComboBox({
                store : claimOwnerSearchScreenStore,
                width: 220,
                valueField : 'id',
                id : 'searchScreenClaimOwnerComboId',
                displayField :'name',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                valueNotFoundText : '--- ALL ---',
                selectOnFocus : true,
                forceSelection : true,
                listeners: {
                    
                    blur: function () {
                        if(this.getRawValue() == "" ) {
                            this.reset();
                        }
                    },
                    specialkey:function (el, e) {
                        if(e.keyCode == e.ENTER) {
                            searchClaim(true);
                        }
                    },
                    afterrender : function(){
                        this.setValue(<s:property value="claimOwnerId"/>);
                    },
                    select : function(){
                        searchClaim(true);
                    }
                }
            });

            claimOwnerSearchScreenCombo.render('searchScreenClaimhandlerDownDiv');
        }
        
        
        // Add CHO claim owner combo box
      

        if(!<s:property value="isCHO" /> || (<s:property value="isCHO" /> && <s:property value="choIsClaimOwnershipEnabled" />)) {
        
        
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
                        if(<s:property value="isCHO" />) {
                            var notAssigned = new Array();
                            // this next assignment is ugly and should be removed/refactored at some point
                            notAssigned['id'] = '<%= ClaimSearchCriteria.CLAIM_OWNER_NOT_ASSIGNED%>';
                            notAssigned['name'] = 'NOT ASSIGNED';
                            this.insert(0, new Ext.data.Record(claimOwnerdefaultDropdownValue));
                            this.insert(1, new Ext.data.Record(notAssigned));
                            
                        }else{
                            this.insert(0, new Ext.data.Record(claimOwnerdefaultDropdownValue));

                        }
                    }},
                reader : supplierClaimOwnerReader
            });
            supplierClaimOwnerSearchScreenStore.load({params : {"supplierId": supplierSearchScreenId}});
            supplierClaimOwnerSearchScreenCombo = new Ext.form.ComboBox({
                store : supplierClaimOwnerSearchScreenStore,
                width: 220,
                valueField : 'id',
                id : 'searchScreenSupplierClaimOwnerComboId',
                displayField :'name',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                valueNotFoundText : '--- ALL ---',
                selectOnFocus : true,
                forceSelection : true,
                listeners: {
                
                    blur: function () {
                        if(this.getRawValue() == "" ) {
                            this.reset();
                        }
                    },
                    specialkey:function (el, e) {
                        if(e.keyCode == e.ENTER) {
                            searchClaim(true);
                        }
                    },
                    afterrender : function(){
                        this.setValue(<s:property value="supplierClaimOwnerId"/>);
                    },
                    select : function(){
                        searchClaim(true);
                    }
                }
            });

            supplierClaimOwnerSearchScreenCombo.render('searchScreenSupplierClaimOwnerDropDownDiv');
        }
        
        
        
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
                        actionsForHandlers['text'] = '<%= ClaimSearchCriteria.STATUS_ACTIONS_FOR_HANDLERS%>';
                        actionsForHandlers['value'] = 'ACTIONS FOR HANDLERS';
                        this.insert(0, new Ext.data.Record(statusdefaultDropdownValue));
                        this.insert(1, new Ext.data.Record(actionsForHandlers));
                        
                    }else{
                        this.insert(0, new Ext.data.Record(statusdefaultDropdownValue));
                    }
                }}
        });


        statusSearchScreenCombo = new Ext.form.ComboBox({
            store : statusesStore,
            width: 220,
            valueField : 'text',
            id : 'statusSearchScreenComboId',
            displayField :'value',
            typeAhead : true,
            mode : 'local',
            triggerAction : 'all',
            valueNotFoundText : '--- ALL ---',
            selectOnFocus : true,
            forceSelection : true,
            listeners: {
               
                blur: function () {
                    if(this.getRawValue() == "" ) {
                        this.reset();
                        statusChange();
                    }
                },
                specialkey:function (el, e) {
                    if(e.keyCode == e.ENTER) {
                        searchClaim(true);
                    }
                },
                afterrender : function(){
                    this.setValue('<s:property value="status"/>');
                },
                select : function(){
                    statusChange();
                    searchClaim(true);
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

        var liabilityStatuses = Ext.util.JSON.decode('<s:property value="liabilityStatusesJsonString" escape="false"/>');
        var liabilityStatusesStore = new Ext.data.Store({
            data : liabilityStatuses,
            reader : liabilityStatusesJsonReader,
            listeners: {load: function() {this.insert(0, new Ext.data.Record(liabilityStatusdefaultDropdownValue));}}
        });
        var defaultValueText;
        if('<s:property value="liabilityStatus"/>'){
            defaultValueText = '<s:property value="liabilityStatus"/>'
        }else{
            defaultValueText = '--- ALL ---';
        }
        liabilityStatusSearchScreenCombo = new Ext.form.ComboBox({
            store : liabilityStatusesStore,
            width: 220,
            valueField : 'value',
            id : 'liabilityStatusSearchScreenComboId',
            displayField :'text',
            typeAhead : true,
            mode : 'local',
            triggerAction : 'all',
            valueNotFoundText : defaultValueText,
            selectOnFocus : true,
            forceSelection : true,
            listeners: {
                
                blur: function () {
                    if(this.getRawValue() == "" ) {
                        this.reset();
                        statusChange();
                    }
                },
                specialkey:function (el, e) {
                    if(e.keyCode == e.ENTER) {
                        searchClaim(true);
                    }
                },
                afterrender : function(){
                    this.setValue('<s:property value="liabilityStatus"/>');
                },
                select : function(){
                    statusChange();
                    searchClaim(true);
                }
            }
        });
        liabilityStatusSearchScreenCombo.render('searchScreenLiabilityDropDownDiv');

        

       

        // Create the search and reset buttons
        new Ext.Button({
            renderTo: 'searchButton',
            text: 'Search',
            handler: function(button, event) {
                searchClaim(true);
            }
        }).focus();

        new Ext.Button({
            renderTo: 'resetButton',
            text: 'Reset',
            handler: function(button, event) {
                clearForm();
                searchClaim(false);
            }
        });

        setDefaultClaimOwner();
//        setDefaultSupplierClaimOwner();

    });

    function setSelectedInsurerId(){
    
        if (insurerSearchScreenCombo.getValue() != null && insurerSearchScreenCombo.getValue() != '') {
            insurerSearchScreenId = insurerSearchScreenCombo.getValue();
        }
        else {
            insurerSearchScreenId = -1;
            insurerSearchScreenCombo.reset();
        }
    }

    function setSelectedSupplierId(){
        
        if (supplierSearchScreenCombo.getValue() != null && supplierSearchScreenCombo.getValue() != '') {
            supplierSearchScreenId = supplierSearchScreenCombo.getValue();
        }
        else {
            supplierSearchScreenId = -1;
            supplierSearchScreenCombo.reset();
        }
    }


    function doInsurerSearchSelectOnChange(){
        setSelectedInsurerId();

        workgroupSearchScreenStore.load({ params : {"orgId": insurerSearchScreenId}});
        workgroupSearchScreenCombo.reset();
            
        doShowClaimHandler(-1, insurerSearchScreenId);
    }

    function doSupplierSearchSelectOnChange(){
        setSelectedSupplierId();
        doShowSupplierClaimHandler(supplierSearchScreenId);
    }

    function doSearchWorkgroupOnChange(){
        
        if (workgroupSearchScreenCombo.getValue() != null && workgroupSearchScreenCombo.getValue() != '') {
            workgroupSearchScreenId = workgroupSearchScreenCombo.getValue();
        }else{
            workgroupSearchScreenId = -1;
        }
        doShowClaimHandler(workgroupSearchScreenId, insurerSearchScreenId);
    }

    function doShowSupplierClaimHandler(selectedSupplierId){
        
        if(!<s:property value="isCHO"/>){
            supplierClaimOwnerSearchScreenStore.load({ params : {"supplierId":selectedSupplierId}});
            supplierClaimOwnerSearchScreenCombo.reset();
        }else if(<s:property value="isCHO"/> && <s:property value="choIsClaimOwnershipEnabled"/>){
            supplierClaimOwnerSearchScreenStore.load({ params : {"supplierId":selectedSupplierId}});
            supplierClaimOwnerSearchScreenCombo.reset();
            if(<s:property value="isOp"/>){
                supplierClaimOwnerSearchScreenCombo.setValue(<s:property value="AuthenticatedUser.id"/>);
            }  
        }
    }
    
    function doShowClaimHandler(selectedWorkgroupId, selectedInsurerId){
        
        if(!<s:property value="isInsurer"/>){ 
            claimOwnerSearchScreenStore.load({ params : {"workgroupId":selectedWorkgroupId,"insurerId":selectedInsurerId}});
            claimOwnerSearchScreenCombo.reset();
        }
        else if(<s:property value="isInsurer"/>  ){
    <s:if test="AuthenticatedUser.insurer.claimOwnershipEnable">
                claimOwnerSearchScreenStore.load({ params : {"workgroupId":selectedWorkgroupId,"insurerId":selectedInsurerId}});
                claimOwnerSearchScreenCombo.reset();
                if(<s:property value="isCH"/> && selectedWorkgroupId === -1){
                    claimOwnerSearchScreenCombo.setValue(<s:property value="AuthenticatedUser.id"/>);
                } 
    </s:if>
            
            }
        }

        function setDefaultClaimOwner() {
    
            if (<s:property value="isInsurer"/> ){
    <s:if test="AuthenticatedUser.insurer.claimOwnershipEnable && isCH"> 
                claimOwnerSearchScreenCombo.setValue(<s:property value="AuthenticatedUser.id"/>);
    </s:if>
            }
       
        }

        function setDefaultSupplierClaimOwner() {
    
            if (<s:property value="isCHO"/> && <s:property value="choIsClaimOwnershipEnabled"/> && <s:property value="isOp"/>){
                supplierClaimOwnerSearchScreenCombo.setValue(<s:property value="AuthenticatedUser.id"/>);
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
                if(this.id=='isOpenClaimId'){
                    this.checked = true;
                }else  if(this.id=='supplementaryInvoicedCheckBoxId'){
                    this.checked = false;
                }
            
            });
        
            statusSearchScreenCombo.reset();
            liabilityStatusSearchScreenCombo.reset();
        
        
            if (insurerSearchScreenCombo){
                insurerSearchScreenId = -1;
                insurerSearchScreenCombo.reset(); 
            }
            
            if (supplierSearchScreenCombo){
                supplierSearchScreenId = -1;
                supplierSearchScreenCombo.reset();
            }
        
            if(workgroupSearchScreenCombo){
                workgroupSearchScreenId = -1;
                if(<s:property value="isInsurer"/>){
                    workgroupSearchScreenStore.load({ params : {"orgId": <s:property value="UserOrganisationId"/>}}); 
                }else{
                    workgroupSearchScreenStore.load({ params : {"orgId": -1}});
                }
                workgroupSearchScreenCombo.reset();
            }
        
            if(claimOwnerSearchScreenCombo){
                if(<s:property value="isInsurer"/>){
                    claimOwnerSearchScreenStore.load({ params : {"workgroupId":-1,"insurerId": <s:property value="UserOrganisationId"/>}});
                }else{
                    claimOwnerSearchScreenStore.load({ params : {"workgroupId":-1,"insurerId": -1}});
                }
                claimOwnerSearchScreenCombo.reset();
            }
        
            if(supplierClaimOwnerSearchScreenCombo){
                if(<s:property value="isCHO"/>){
            
                    supplierClaimOwnerSearchScreenStore.load({ params : {"supplierId": <s:property value="UserOrganisationId"/>}});
                }else{
                    supplierClaimOwnerSearchScreenStore.load({ params : {"supplierId": -1}});
                }
                supplierClaimOwnerSearchScreenCombo.reset();
            }
       
            setDefaultClaimOwner();
//            setDefaultSupplierClaimOwner();
        }

        function statusChange(){
            if((statusSearchScreenCombo.getValue() !="AwaitingCarHireInfo") && <s:property value="isCHO" />){
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
                <td><div id="supplierReferenceFieldId"/><!--s:textfield name="supplierReference"/--></td>
                <td><label>Claim Number</label></td>
                <td><div id="claimNumberFieldId"/><!--s:textfield name="claimNumber"/--></td>
            </tr>
            <tr>
                <td><label>Invoice Number</label></td>
                <td><div id="invoiceNumberFieldId"/><!--s:textfield name="invoiceNumber"/--></td>
                <td><label>Show Open Claims Only <img id="help-open-items-icon" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt="" /></label></td>
                <td><div id="showOpenClaimsFieldId"/><!--s:checkbox name="isOpenClaim" value="true" /--></td>
            </tr>
            <tr>
                <td><label>Supplier VRN</label></td>
                <td><div id="customerVrnFieldId"/><!--s:textfield name="customerVrn"/--></td>
                <td><label>Insurer VRN</label></td>
                <td><div id="thirdPartyVrnFieldId"/><!--s:textfield name="thirdPartyVrn" /--></td>
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
                    <td nowrap><label>Show Claims With Supplementary Invoice(s) Only</label></td>
                    <td><div id="searchScreenSupplementaryInvoiceDiv"></div></td>
                </tr>
            </s:if>
            <s:elseif test="isInsurer && (insurerIsWorkgroupEnabled && !insurerIsClaimOwnershipEnabled)">
                <tr>
                    <td nowrap><label>Supplier Claim Owner</label></td>
                    <td><div id="searchScreenSupplierClaimOwnerDropDownDiv"></div></td>
                    <td nowrap><label>Show Claims With Supplementary Invoice(s) Only</label></td>
                    <td><div id="searchScreenSupplementaryInvoiceDiv"></div></td>
                </tr>
            </s:elseif>
            <s:elseif test="isInsurer && (!insurerIsWorkgroupEnabled && insurerIsClaimOwnershipEnabled)">
                <tr>
                    <td nowrap><label>Supplier Claim Owner</label></td>
                    <td><div id="searchScreenSupplierClaimOwnerDropDownDiv"></div></td>
                    <td nowrap><label>Show Claims With Supplementary Invoice(s) Only</label></td>
                    <td><div id="searchScreenSupplementaryInvoiceDiv"></div></td>
                </tr>
            </s:elseif>
            <s:else>
                <tr>
                    <td nowrap><label>Show Claims With Supplementary Invoice(s) Only</label></td>
                    <td><div id="searchScreenSupplementaryInvoiceDiv"></div></td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
            </s:else>
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