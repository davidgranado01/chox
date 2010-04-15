/*
Chox.billing.BillingPanel = Ext.extend( Ext.Panel, {
    initComponent:function() {

        Chox.billing.BillingPanel.superclass.initComponent.call(this);
    }
});
         */
var cb = Chox.billing;

function formatDate(value){
    return value ? value.dateFormat('d M, Y') : '';
}




////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
Chox.orgStore = new Ext.data.Store( {
    proxy : new Ext.data.HttpProxy( {
        url : Chox.appname + '/prv/p/listBillingOrgData.action'
    }),
    reader : new Ext.data.JsonReader( {
        fields : [ 'orgId', 'name' ],
        root : 'results'
    }),
    baseParams:{
        billingType:Chox.billing.billingmode
    },
    autoLoad : true
});

////////////////////////////////////////////////////////////////////////////////
///////////////////////////ADD SCHEDULE  ///////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////



Chox.billing.BillingForm=Ext.extend(Ext.FormPanel,{
    constructor:function(){


        Chox.billing.BillingForm.superclass.constructor.apply(this,arguments);
    },
    initComponent:function(){

        this.items = [{
            xtype : 'hidden',
            name : 'billingType',
            value : Chox.billing.billingmode
        },{

        }, {
            xtype : 'textfield',
            name : 'scheduleName',
            fieldLabel : 'Schedule Name',
            allowBlank: false
        }, {
            xtype : 'datefield',
            name : 'dateFrom',
            fieldLabel : 'From Date',
            format : 'd/m/Y',
            allowBlank: false
        }, {
            xtype : 'datefield',
            name : 'dateTo',
            fieldLabel : 'To Date',
            format : 'd/m/Y',
            allowBlank: false
        }];
        if ( Chox.billing.billingmode == 'insurer'){
            this.items[1] = {
                xtype : 'combo',
                name : 'insurerName',
                typeAhead : false,
                fieldLabel : 'Insurer',
                mode : 'local',
                store : Chox.orgStore,
                hiddenName : 'orgId',
                displayField : 'name',
                valueField : 'orgId',
                allowBlank: false
            };
        }else{
            this.items[1] = {
                xtype : 'combo',
                name : 'choName',
                typeAhead : false,
                fieldLabel : 'CHO',
                mode : 'local',
                store : Chox.orgStore,
                hiddenName : 'orgId',
                displayField : 'name',
                valueField : 'orgId',
                allowBlank: false
            };
        }
        Chox.billing.BillingForm.superclass.initComponent.call(this);
    },
    frame : true,
    bodyStyle : 'padding:10px',
    // url : Chox.appname + '/prv/p/addBill.action',
    buttons : [ {
        text : 'Save',
        handler : function() {
            Ext.getCmp('refbillingform').getForm().submit( {
                success : function(f, a) {

                    if ( a.result.success ){

                        //Ext.getCmp('refbillingstore').reload();
                        cb.billingWindowObj.hide();
                        cb.bstore.reload();
                    }

                },
                failure : function(f, a) {
                    
                }
            });

        }

    }, {
        text : 'Cancel',
        handler : function(){
    		cb.billingWindowObj.hide();
        }
    } ]

});



Chox.billing.billingWindow = Ext.extend(Ext.Window, {
    constructor:function(){
        this.items = [
        cb.billingFormObj
        ];
        this.title = 'New '+ Chox.billing.billingHeader1 + ' Schedule';
        Chox.billing.billingWindow.superclass.constructor.apply(this,arguments);
    },
    initComponents:function(){


        Chox.billing.billingWindow.superclass.initComponent.call(this);
    },
    
    modal : true,
    closeAction : 'hide',
    plain : false,
    resizable : false

});


////////////////////////////////////////////////////////////////////////////////
////////////////////////PAYMENT RECEIVED////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

Chox.billing.PaymentForm=Ext.extend(Ext.FormPanel,{
    constructor:function(){


        Chox.billing.PaymentForm.superclass.constructor.apply(this,arguments);
    },
    initComponent:function(){

        this.items = [{
            xtype : 'hidden',
            name : 'billingType',
            value : Chox.billing.billingmode

        }, {
            xtype : 'textfield',
            name : 'scheduleName',
            fieldLabel : 'Schedule Name',
            allowBlank: false

        },{
            xtype : 'checkbox',
            name : 'manual',
            fieldLabel : 'Enter Manual Payment',
            listeners:{
                check : function( chkbx,  checked ){
                    
                    cb.paymentFormObj.getComponent(3).setDisabled(!checked);
                }
            }

        },{

            xtype : 'textfield',
            name : 'amountReceived',
            fieldLabel : 'Manual Payment Amount'

        //disabled: true
        },{
            xtype : 'checkbox',
            name : 'reconciled',
            fieldLabel : 'Payment Received'
        }];

        Chox.billing.PaymentForm.superclass.initComponent.call(this);

    },
    frame : true,
    bodyStyle : 'padding:10px',

    buttons : [ {
        text : 'Save',
        handler : function() {

            cb.paymentFormObj.getForm().submit( {
                success : function(f, a) {
                   
                    if ( a.result.success ){
                   
                        cb.paymentWindowObj.hide();
                        cb.bstore.reload();
                        cb.bdetails.reload();
                    }

                },
                failure : function(f, a) {
                   
                },
                params :{
                    billingId:cb.schSel.getSelected().get('billingId')
                }
            });

        }

    }, {
        text : 'Cancel',
        handler : function(){
            cb.paymentWindowObj.hide();
        }
    } ],
    listeners:{
        render:function(frm){
            if ( cb.schSel.getSelected().get('manual') == false){
                cb.paymentFormObj.getComponent(3).setDisabled(true);
            }



        },
        beforehide:function(frm){
        //Ext.getCmp('imanualPaymentReceived').enable();
        }
    }

});

cb.paymentFormObj = new Chox.billing.PaymentForm({
    url:Chox.appname + '/prv/p/paymentReceived.action'
});



Chox.billing.PaymentWindow = Ext.extend(Ext.Window, {
    constructor:function(){
        this.items = [
        cb.paymentFormObj
        ];
        this.title = Chox.billing.billingHeader1 + ' Payment';
        Chox.billing.PaymentWindow.superclass.constructor.apply(this,arguments);
    },
    initComponents:function(){

        Chox.billing.PaymentWindow.superclass.initComponent.call(this);
    },
    modal : true,
    closeAction : 'hide',
    plain : false,
    resizable : false

});

cb.paymentWindowObj = new Chox.billing.PaymentWindow();

////////////////////////////////////////////////////////////////////////////////
////////////////////////SEARCH WINDOW////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

Chox.billing.SearchForm=Ext.extend(Ext.FormPanel,{
    constructor:function(){


        Chox.billing.SearchForm.superclass.constructor.apply(this,arguments);
    },
    initComponent:function(){

        this.items = [{
            xtype : 'hidden',
            name : 'billSearch',
            value : true

        }, {
            xtype : 'hidden',
            name : 'billingType',
            value : Chox.billing.billingmode

        }, {
            xtype : 'textfield',
            name : 'claimNumber',
            fieldLabel : 'Claim Number'

        }, {
            xtype : 'textfield',
            name : 'choReference',
            fieldLabel : 'CHO Reference'

        }];

        Chox.billing.SearchForm.superclass.initComponent.call(this);

    },
    frame : true,
    bodyStyle : 'padding:10px',

    buttons : [ {
        text : 'Search',
        handler : function() {
            
            cb.bstore.load({
                params:cb.searchFormObj.getForm().getValues()
            });
            cb.searchWindowObj.hide();
        }

    }, {
        text : 'Cancel',
        handler : function(){
            cb.searchWindowObj.hide();
        }
    } ],
    listeners:{
        render:function(frm){


        },
        beforehide:function(frm){

        }
    }

});

cb.searchFormObj = new Chox.billing.SearchForm();



Chox.billing.SearchWindow = Ext.extend(Ext.Window, {
    constructor:function(){
        this.items = [
        cb.searchFormObj
        ];
        this.title = 'Search Schedule';
        Chox.billing.SearchWindow.superclass.constructor.apply(this,arguments);
    },
    initComponents:function(){

        Chox.billing.SearchWindow.superclass.initComponent.call(this);
    },
    modal : true,
    closeAction : 'hide',
    plain : false,
    resizable : false

});

cb.searchWindowObj = new Chox.billing.SearchWindow();

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////


Chox.billing.BillingStore = function(){
    Chox.billing.BillingStore.superclass.constructor.apply(this, arguments);
}

Ext.extend(Chox.billing.BillingStore,Ext.data.Store,{
    url: Chox.appname + '/prv/p/listBillingGridData.action',
    reader : new Ext.data.JsonReader({
        root : 'results',
        id: 'billingId'
    },
    [
    'billingId',
    'column1',
    'column2',
    'scheduleName',{
        name : 'dateFrom',
        type : 'date',
        //dateFormat : 'timestamp'
        dateFormat : 'd/m/Y H:i:s'
    }, {
        name : 'dateTo',
        type : 'date',
        //dateFormat : 'timestamp'
        dateFormat : 'd/m/Y H:i:s'
    },

    {
        name : 'invoiceAmount',
        type : 'float'
    },
    {
        name : 'amountReceived',
        type : 'float'
    },
    {
        name : 'reconciled',
        type : 'boolean'
    },
    {
        name : 'manual',
        type : 'boolean'
    }
    ]
    ),
    autoLoad: true
});

cb.schSel = new Ext.grid.CheckboxSelectionModel({
    singleSelect:true,
    listeners:{
        rowdeselect : function ( selmo, rowIndex, record ){

            cb.bdetails.load({
                params:{
                    billingId:0
                }
            });


        },
        selectionchange : function(selmodel){
            
        }
    }

});

cb.bstore = new Chox.billing.BillingStore({
    id:'refbillingstore',
    baseParams:{
        billingType:Chox.billing.billingmode
    }
})

Chox.billing.BillingGrid = Ext.extend( Ext.grid.GridPanel,{
    initComponent:function(){
        this.columns[1].header = Chox.billing.billingHeader1;
        this.title = Chox.billing.billingPageTitle,
        this.tbar = new Ext.Toolbar({
            items:[{
                text:'New ',
                handler : function() {

                    cb.billingFormObj.getForm().reset();
                    cb.billingWindowObj.show();
                }
            },{
                text:'Delete ',
                handler : function(){
                  if (confirm('Are you sure you want to delete this schedule?')) {
                    var selected = cb.schSel.getSelected();
                    if( selected ){

                        Ext.Ajax.request({
                            url: Chox.appname + '/prv/p/deleteBill.action',
                            callback : function(options,success,response  ){

                                var resp = Ext.util.JSON.decode(response.responseText);

                                cb.bstore.reload();
                                cb.bdetails.reload();
                                if(resp.success){

                                }
                            },
                            params: {
                                billingId: selected.get('billingId'),
                                billingType: Chox.billing.billingmode
                            }
                        //jsonData:jstr
                        });
                    }
                  }
                }
            },{
                text:'Download ',
                handler : function() {

                    var selected = cb.schSel.getSelected();
                    if( selected ){
                        var rptName;
                        if ( Chox.billing.billingmode =='insurer'){
                            rptName = 'BillingInsurerReport-Excel';
                        }else{
                            rptName = 'BillingChoReport-Excel';
                        }
                        var rpthref = Chox.appname+ '/prv/p/exportExcelReport.action?reportName=' + rptName +'&' +Ext.urlEncode(selected.data);//+dtstr;
                        
                        location.href = rpthref
                    }
                }
            },{
                text:'Details',
                handler : function() {
                    var selected = cb.schSel.getSelected();

                    cb.bdetails.load({
                        params:{
                            billingId:selected.get('billingId')
                        }
                    });
                    if ( selected.get('reconciled')== true){
                    //cb.bdetails.setDisabled(true);
                    }else{
                //cb.bdetails.setDisabled(false);
                }
                }
            },{
                text:'Reconcile',
                handler : function() {
                    var selected = cb.schSel.getSelected();
                    if ( selected ){


                        cb.paymentWindowObj.show();
                        cb.paymentFormObj.getForm().loadRecord(selected);
                        if (selected.get('reconciled')==true){
                            cb.paymentFormObj.setDisabled(true);
                        }else{
                            cb.paymentFormObj.setDisabled(false);
                        }
                    }


                }
            },{
                text:'Search',
                handler : function() {
                    cb.searchWindowObj.show();
                }
            }]
        });
        this.bbar = new Ext.StatusBar();
        Chox.billing.BillingGrid.superclass.initComponent.call(this);

    },
    store: cb.bstore,
    height: 220,
    columns:[cb.schSel,{
        header : Chox.billing.billingHeader1,
        dataIndex : 'column1',
        width : 80
    },{
        header : 'Schedule Name',
        dataIndex : 'scheduleName',
        width : 80
    },{
        header : 'From',
        dataIndex : 'dateFrom',
        renderer: formatDate,
        width : 80
    },{
        header : 'To',
        dataIndex : 'dateTo',
        renderer: formatDate,
        width : 80
    },{
        header : 'Invoice',
        dataIndex : 'invoiceAmount',
        renderer: 'gbMoney',
        align : 'right',
        width : 80
    },{
        header : 'Received',
        dataIndex : 'amountReceived',
        renderer: 'gbMoney',
        align : 'right',
        width : 80
    },{
        header : 'Reconciled',
        dataIndex : 'reconciled',
        width : 80
    },{
        header : 'Manual',
        dataIndex : 'manual',
        width : 80
    }],

    sm : cb.schSel,
    autoShow:false

});



Chox.billing.BillingDetailStore = function(){
    this.billingId = 0;
    Chox.billing.BillingDetailStore.superclass.constructor.apply(this, arguments);
}

Ext.extend(Chox.billing.BillingDetailStore,Ext.data.Store,{

    url : Chox.appname + '/prv/p/listBillingDetailGridData.action',

    reader : new Ext.data.JsonReader( {
        root : 'results',
        id : 'billingDetailId'
    }, [ 'billingDetailId', 'scheduleName', 'claimReferenceId', {
        name : 'itemAmount',
        type : 'float'
    },{
        name : 'amountReceived',
        type : 'float'
    },{
        name : 'receivedDate',
        type : 'string',
        dateFormat : 'd/m/Y H:i:s'
    }, 'comment', {
        name : 'reconciled',
        type : 'bool'
    } ]),
    autoLoad : true,
    listeners : {
        update : function( store, record, operation ){
            updateBillingDetailStatus(store);
        },
        load : function( store, recarr, opt ){

            if ( cb.schSel.getSelected() ){
                store.billingId =cb.schSel.getSelected().get('billingId');

            }else{
                store.billingId =0;
            }


            updateBillingDetailStatus(store);
        },
        clear : function ( store ){
            
            updateBillingDetailStatus(store);
        }
    }

});


function setReconciled(rec){
    rec.set('reconciled',true);
    rec.set('receivedDate',new Date().format("d/m/Y H:i:s"));
    rec.set('amountReceived',rec.get('itemAmount'));

}

function setNotReconciled(rec){
    rec.set('reconciled',false);
    rec.set('receivedDate',"");
    rec.set('amountReceived',0);
}

function retDate(){
    var dt = new Date();
    var dts = dt.format("d/m/Y H:i:s").toString();
    return dts+"";
}

function updateBillingDetailStatus(store){

    Ext.getCmp('id_lbl_count').setText('No of Claims : '+store.getCount());

    Ext.getCmp('id_lbl_received').setText('Amount Received : ' + Ext.util.Format.gbMoney(store.sum('amountReceived')));
    if ( cb.schSel.hasSelection() && store.getCount() > 0 ){

        Ext.getCmp('id_lbl_invoice').setText('Invoice Amount : '+ Ext.util.Format.gbMoney(cb.schSel.getSelected().get('invoiceAmount')));
    }else{
        Ext.getCmp('id_lbl_invoice').setText('Invoice Amount : '+ Ext.util.Format.gbMoney(0));
    }

}

cb.bdetails = new Chox.billing.BillingDetailStore({
    baseParams:{
        billingType:Chox.billing.billingmode
    }
});

Chox.billing.dtl_comment_edit = new Ext.form.TextField();
Chox.billing.dtl_received_edit = new Ext.form.NumberField();

Chox.billing.BillingDetailGrid = Ext.extend( Ext.grid.EditorGridPanel,{
    height : 420,
    initComponent:function(){
        this.title = Chox.billing.billingPageTitle + ' Details',
        this.tbar = new Ext.Toolbar({
            items:[{
                text:'Save ',
                handler : function(){
                    var x = cb.bstore.getById(cb.bdetails.billingId);
                    if ( x.get('reconciled') == true){

                        return ;
                    }
                    var mrecs = cb.bdetails.getModifiedRecords();
                    var ma = new Array()
                    for(var i = 0 ; i < mrecs.length ; i++){
                        ma[i] = mrecs[i].data;
                    }
                    jstr = Ext.util.JSON.encode(ma);

                    Ext.Ajax.request({
                        url: Chox.appname + '/prv/p/updateBillingDetail.action',
                        callback : function(options,success,response  ){

                            var resp = Ext.util.JSON.decode(response.responseText);

                            if(resp.success){
                                cb.bdetails.commitChanges();
                                cb.bstore.reload();
                            }
                        },
                        params: {
                            billingId:cb.bdetails.billingId,
                            //requestJson: jstr
                            jsonData:jstr,
                            billingType:Chox.billing.billingmode

                        }
                    //jsonData:jstr
                    });

                }
            }]
        });
        this.bbar= new Ext.StatusBar({
            id: 'my-status',

            // defaults to use when the status is cleared:
            defaultText: '',
            defaultIconCls: 'default-icon',

            // values to set initially:
            text: '',
            iconCls: 'ready-icon',

            // any standard Toolbar items:
            items: [{
                id : 'id_lbl_count',
                xtype: 'label',
                name: 'received'
            },'&nbsp; - &nbsp;',{
                id : 'id_lbl_received',
                xtype: 'label',
                name: 'received'
            },'&nbsp; - &nbsp;',{
                id : 'id_lbl_invoice',
                xtype: 'label',
                name: 'invoice'
            }]
        });
        Chox.billing.BillingDetailGrid.superclass.initComponent.call(this);

    },
    enableColumnMove:false,
    store: cb.bdetails,
    columns:[{
        header : 'Claim Reference',
        dataIndex : 'claimReferenceId'
    },{
        header : 'Gross Amount',
        dataIndex : 'itemAmount',
        renderer: 'gbMoney',
        align:'right'
    },{
        header : 'Received',
        dataIndex : 'amountReceived',
        renderer: 'gbMoney',
        editor: cb.dtl_received_edit,
        align:'right'
    },{
        header : 'Received Date',
        dataIndex : 'receivedDate'

    },{
        header : 'Reconciled...',
        dataIndex : 'reconciled'
    },{
        header : 'Comment',
        dataIndex : 'comment',
        editor:cb.dtl_comment_edit
    }],
    listeners:{
        headerclick: function ( grid, columnIndex, e ){
            if (columnIndex == 4 ){

                if (grid.store.find('reconciled','false') > -1 ){

                    grid.store.each(function(){
                        //this.beginEdit();
                        if ( this.get('reconciled') == false){
                            setReconciled(this);
                        }

                    });

                }else {
                    if (grid.store.find('reconciled','true') > -1 ){
                        grid.store.each(function(){
                            //this.beginEdit();
                            if ( this.get('reconciled') == true){
                                setNotReconciled(this);
                            }
                        });

                    }
                }

            }
        },
        cellclick:function( grid, rowIndex, columnIndex,  e ){

            var x = cb.bstore.getById(cb.bdetails.billingId);

            if ( x.get('reconciled') == true){

                e.cancel = true;
                return false;
            }
            if (columnIndex == 4 ){
                var rec = grid.store.getAt(rowIndex);

                if ( rec.get('reconciled') == false ){
                    setReconciled(rec);
                } else{
                    setNotReconciled(rec);
                /*
                    rec.set('reconciled',true);
                    rec.set('receivedDate',retDate());
                    rec.set('paymentAmount',getBenefitValue(rec.get('insurerScheduleId')))
                             */
                }
            }
        },
        beforeedit:function(e){

            var x = cb.bstore.getById(cb.bdetails.billingId);

            if ( x.get('reconciled') == true){
                e.cancel = true;
                return false;
            }

        }

    }
});

