/*
Chox.billing.BillingPanel = Ext.extend( Ext.Panel, {
    initComponent:function() {

        Chox.billing.BillingPanel.superclass.initComponent.call(this);
    }
});
 */
var cb = Chox.billing;

function formatDate(value){
    return value ? value.dateFormat('d/M/Y') : '';
}




////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
Chox.orgStore = new choxDataStore({
    url : '/prv/p/listBillingOrgData.action',
    reader : new Ext.data.JsonReader({
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

////////////////////////////////////////////////////////////////////////////////
///////////////////////////Ext.StatusBar start///////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

Ext.StatusBar = Ext.extend(Ext.Toolbar, {

    cls : 'x-statusbar',

    busyIconCls : 'x-status-busy',

    busyText : 'Loading...',

    autoClear : 5000,

    // private
    activeThreadId : 0,

    // private
    initComponent : function(){
        if(this.statusAlign==='right'){
            this.cls += ' x-status-right';
        }
        Ext.StatusBar.superclass.initComponent.call(this);
    },

    // private
    afterRender : function(){
        Ext.StatusBar.superclass.afterRender.call(this);
    },

    setStatus : function(o){
        o = o || {};

        if(typeof o === 'string'){
            o = {
                text:o
            };
        }
        if(o.text !== undefined){
            this.setText(o.text);
        }
        if(o.iconCls !== undefined){
            this.setIcon(o.iconCls);
        }

        if(o.clear){
            var c = o.clear,
            wait = this.autoClear,
            defaults = {
                useDefaults: true, 
                anim: true
            };

            if(typeof c === 'object'){
                c = Ext.applyIf(c, defaults);
                if(c.wait){
                    wait = c.wait;
                }
            }else if(typeof c === 'number'){
                wait = c;
                c = defaults;
            }else if(typeof c === 'boolean'){
                c = defaults;
            }

            c.threadId = this.activeThreadId;
            this.clearStatus.defer(wait, this, [c]);
        }
        return this;
    },


    clearStatus : function(o){
        o = o || {};

        if(o.threadId && o.threadId !== this.activeThreadId){
            // this means the current call was made internally, but a newer
            // thread has set a message since this call was deferred.  Since
            // we don't want to overwrite a newer message just ignore.
            return this;
        }

        var text = o.useDefaults ? this.defaultText : '',
        iconCls = o.useDefaults ? (this.defaultIconCls ? this.defaultIconCls : '') : '';

        if(o.anim){
            this.statusEl.fadeOut({
                remove: false,
                useDisplay: true,
                scope: this,
                callback: function(){
                    this.setStatus({
                        text: text,
                        iconCls: iconCls
                    });
                    this.statusEl.show();
                }
            });
        }else{
            // hide/show the el to avoid jumpy text or icon
            this.statusEl.hide();
            this.setStatus({
                text: text,
                iconCls: iconCls
            });
            this.statusEl.show();
        }
        return this;
    },


    setText : function(text){
        this.activeThreadId++;
        this.text = text || '';
        if(this.rendered){
            this.statusEl.update(this.text);
        }
        return this;
    },


    getText : function(){
        return this.text;
    },


    setIcon : function(cls){
        this.activeThreadId++;
        cls = cls || '';

        if(this.rendered){
            if(this.currIconCls){
                this.statusEl.removeClass(this.currIconCls);
                this.currIconCls = null;
            }
            if(cls.length > 0){
                this.statusEl.addClass(cls);
                this.currIconCls = cls;
            }
        }else{
            this.currIconCls = cls;
        }
        return this;
    },


    showBusy : function(o){
        if(typeof o === 'string'){
            o = {
                text:o
            };
        }
        o = Ext.applyIf(o || {}, {
            text: this.busyText,
            iconCls: this.busyIconCls
        });
        return this.setStatus(o);
    },


    nextBlock : function(){
        var td = document.createElement("td");
        this.tr.appendChild(td);
        return td;
    },

    onRender : function(ct, position){
        if(!this.el){
            if(!this.autoCreate){
                this.autoCreate = {
                    cls: this.toolbarCls + ' x-small-editor'
                };
            }
            this.el = ct.createChild(Ext.apply({
                id: this.id
            },this.autoCreate), position);
        }


    },

    onLayout: function(ct, target){
        Ext.StatusBar.superclass.onLayout.call(this, ct, target);


        if( !this.tr ){
            this.tr = this.getLayout().leftTr;

            var right = this.statusAlign==='right';
            td = Ext.get(this.nextBlock());
            if(right){
                this.getLayout().rightTr.appendChild(td.dom);
            }else{
                td.insertBefore(this.tr.firstChild);
            }

            this.statusEl = td.createChild({
                cls: 'x-status-text ' + (this.iconCls || this.defaultIconCls || ''),
                html: this.text || this.defaultText || ''
            });
            this.statusEl.unselectable();

            this.spacerEl = td.insertSibling({
                tag: 'td',
                style: 'width:100%',
                cn: [{
                    cls:'ytb-spacer'
                }]
            }, right ? 'before' : 'after');
        }


    }
});
Ext.reg('statusbar', Ext.StatusBar);



////////////////////////////////////////////////////////////////////////////////
///////////////////////////Ext.StatusBar end  ///////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

Chox.billing.BillingForm =Ext.extend(Ext.FormPanel,{
    constructor:function(){
        Chox.billing.BillingForm.superclass.constructor.apply(this,arguments);
    },
    initComponent:function(){
        this.items = [{
            xtype : 'hidden',
            id : 'billingTypeId',
            name : 'billingType'
        }, {
            xtype : 'hidden',
            id : 'billingCsrfId',
            name : '_csrf'
        }, {
            xtype : 'combo',
            name : 'choName',
            typeAhead : false,
            fieldLabel : 'CHO',
            mode : 'local',
            store : Chox.orgStore,
            hiddenName : 'orgId',
            displayField : 'name',
            valueField : 'orgId',
            allowBlank: false,
            triggerAction : 'all'
  
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
        if ( Chox.billing.billingmode === 'insurer'){
            this.items[2] = {
                xtype : 'combo',
                name : 'insurerName',
                typeAhead : false,
                fieldLabel : 'Insurer',
                mode : 'local',
                store : Chox.orgStore,
                hiddenName : 'orgId',
                displayField : 'name',
                valueField : 'orgId',
                allowBlank: false,
                triggerAction : 'all'
            };
        }
        
        Chox.billing.BillingForm.superclass.initComponent.call(this);
    },
    frame : true,
    bodyStyle : 'padding:10px',
    buttons : [ {
        text : 'Save',
        handler : function() {
            Ext.getCmp('billingTypeId').setValue(Chox.billing.billingmode);
            Ext.getCmp('billingCsrfId').setValue(csrfTokenValue);
            Ext.getCmp('refbillingform').getForm().submit( {
                waitTitle :'Please wait',
                waitMsg :'Creating Bills...',
                success : function(f, a) {

                    if ( a.result.success ){
                        cb.billingWindowObj.hide();
                        cb.bstore.reload();
                    }
                },
                failure : function(f, a) {
                    var msg;
                    if(a.result.errors.scheduleName){
                        msg = a.result.errors.scheduleName;
                    }else if(a.result.errors.dateTo){
                        msg = a.result.errors.dateTo;
                    }else{
                        msg = 'Error in creating bills';
                    }
                    Ext.MessageBox.show({
                        title: 'Error',
                        msg: msg,
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    }); 
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
        this.width = 350;
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
        },{
            xtype : 'checkbox',
            name : 'reconciled',
            fieldLabel : 'Payment Received'
        },{
            xtype : 'hidden',
            id : 'billingCsrfId1',
            name : '_csrf'
        }];

        Chox.billing.PaymentForm.superclass.initComponent.call(this);
    },
    frame : true,
    bodyStyle : 'padding:10px',

    buttons : [ {
        text : 'Save',
        handler : function() {
            Ext.getCmp('billingCsrfId1').setValue(csrfTokenValue);
            cb.paymentFormObj.getForm().submit( {
                success : function(f, a) {
                    if ( a.result.success ){
                   
                        cb.paymentWindowObj.hide();
                        cb.bstore.reload();
                        cb.bdetails.reload();
                    }

                },
                //                failure : function(f, a) {
                //                },
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
            if ( cb.schSel.getSelected().get('manual') === false){
                cb.paymentFormObj.getComponent(3).setDisabled(true);
            }
        }
    }

});

cb.paymentFormObj = new Chox.billing.PaymentForm({
    id:'refpaymentFormObj',
    url:Chox.appname + '/prv/p/paymentReceived.action'
});



Chox.billing.PaymentWindow = Ext.extend(Ext.Window, {
    constructor:function(){
        this.width = 350;
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
    } ]
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
    width : 300,
    plain : false,
    resizable : false
});

cb.searchWindowObj = new Chox.billing.SearchWindow();

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////


Chox.billing.BillingStore = function(){
    Chox.billing.BillingStore.superclass.constructor.apply(this, arguments);
};

Ext.extend(Chox.billing.BillingStore,Ext.data.Store,{
    url: Chox.appname + '/prv/p/listBillingGridData.action',
    reader : new Ext.data.JsonReader({
        root : 'results'
    },
    [
    'billingId',
    'column1',
    'column2',
    'scheduleName',{
        name : 'dateFrom',
        type : 'string',
        dateFormat : 'd/m/Y'
    }, {
        name : 'dateTo',
        type : 'string',
        dateFormat : 'd/m/Y'
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
            billingDetailGrid.getTopToolbar().items.get('save_button_id').disable();
            cb.bdetails.load({
                params:{
                    billingId:0
                }
            });
        }
    }

});

cb.bstore = new Chox.billing.BillingStore({
    id:'refbillingstore',
    baseParams:Ext.apply({}, {billingType:Chox.billing.billingmode}, csrfParam)
});

function deleteSchedule(btn) {
    if (btn === 'yes')    {
        var selected = cb.schSel.getSelected();
        if( selected ){
            var box = Ext.MessageBox.wait('Deleting Bills','Please wait..');
            choxExtAjaxRequest({
                url: '/prv/p/deleteBill.action',
                callback : function(options,success,response  ){
                    var resp = Ext.util.JSON.decode(response.responseText);
                    cb.bstore.reload();
                    cb.bdetails.reload();
                    box.hide();
                },
                params: {
                    billingId: selected.get('billingId'),
                    billingType: Chox.billing.billingmode
                }
            });
        }
    }
}

Chox.billing.BillingGrid = Ext.extend( Ext.grid.GridPanel,{
    initComponent:function(){
        this.columns[1].header = Chox.billing.billingHeader1;
        this.title = Chox.billing.billingPageTitle;
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
                    var selected = cb.schSel.getSelected();
                    if( selected ){
                        if(cb.schSel.getSelected().get('reconciled')===true){
                            Ext.MessageBox.show({
                                title: '',
                                msg: 'Reconciled record can not be deleted.',
                                width:300,
                                buttons: Ext.MessageBox.OK,
                                icon : Ext.MessageBox.ERROR
                            });
                        } else {
                            Ext.MessageBox.confirm('Confirm', 'Are you sure you want to delete this schedule?', deleteSchedule ); 
                        }
                        
                    } else{
                        Ext.MessageBox.show({
                            title: '',
                            msg: 'No record has been selected.',
                            width:300,
                            buttons: Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                    
                }
            },{
                text:'Download ',
                handler : function() {
                    var selected = cb.schSel.getSelected();
                 
                    if( selected ){
                        var rptName;
                        if ( Chox.billing.billingmode ==='insurer'){
                            rptName = 'BillingInsurerReport-Excel';
                        }else{
                            rptName = 'BillingChoReport-Excel';
                        }
                        generateReport1(selected.data, rptName);
                    }else{
                        Ext.MessageBox.show({
                            title: '',
                            msg: 'No record has been selected.',
                            width:300,
                            buttons: Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                }
            },{
                text:'Details',
                handler : function() {
                    var selected = cb.schSel.getSelected();
                    if(selected){
                        
                        cb.bdetails.load({
                            params:{
                                billingId:selected.get('billingId')
                            }
                        });
                        
                    }else{
                        Ext.MessageBox.show({
                            title: '',
                            msg: 'No record has been selected.',
                            width:300,
                            buttons: Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        });
                    }
                }
            },{
                text:'Reconcile',
                handler : function() {
                    var selected = cb.schSel.getSelected();
                    if ( selected ){
                        // cb.paymentWindowObj.show();
                        cb.paymentFormObj.getForm().loadRecord(selected);
                        if (selected.get('reconciled')===true){
                            Ext.MessageBox.alert('', 'The payment has been made already.');
                        // cb.paymentFormObj.setDisabled(true);
                        }else{
                            cb.paymentWindowObj.show();
                        //cb.paymentFormObj.setDisabled(false);
                        }
                    }else{
                        Ext.MessageBox.show({
                            title: '',
                            msg: 'No record has been selected.',
                            width:300,
                            buttons: Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        }); 
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
    height: 240,
    width: 775,
    columns:[cb.schSel,{
        header : Chox.billing.billingHeader1,
        dataIndex : 'column1',
        sortable: true,
        width : 80
    },{
        header : 'Schedule Name',
        dataIndex : 'scheduleName',
        sortable: true,
        width : 80
    },{
        header : 'From',
        dataIndex : 'dateFrom',
        //        renderer: Ext.util.Format.dateRenderer('d/m/Y'),
        width : 80
    },{
        header : 'To',
        dataIndex : 'dateTo',
        //        renderer: Ext.util.Format.dateRenderer('d/m/Y'),
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
};

Ext.extend(Chox.billing.BillingDetailStore,Ext.data.Store,{
    
    proxy : new Ext.data.HttpProxy
    ({
        url : Chox.appname + '/prv/p/listBillingDetailGridData.action',
        timeout:1800000
    }), 
    reader : new Ext.data.JsonReader( {
        root : 'results'
    // id : 'billingDetailId'
    }, [ 'billingDetailId', 'scheduleName', 'claimReferenceId', {
        name : 'itemAmount',
        type : 'float'
    },{
        name : 'amountReceived',
        type : 'float'
    },{
        name : 'receivedDate',
        type : 'string',
        dateFormat : 'd/m/Y'
    },{
        name : 'triggerDate',
        type : 'string',
        dateFormat : 'd/m/Y'
    },{
        name : 'triggerPoint',
        type : 'string'
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
    rec.set('receivedDate',new Date());
    rec.set('amountReceived',rec.get('itemAmount'));
}

function setNotReconciled(rec){
    rec.set('reconciled',false);
    rec.set('receivedDate',"");
    rec.set('amountReceived',0);
}

function retDate(){
    var dt = new Date();
    var dts = dt.format("d/m/Y").toString();
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
    baseParams:Ext.apply({}, {billingType:Chox.billing.billingmode}, csrfParam)
});

Chox.billing.dtl_comment_edit = new Ext.form.TextField();
Chox.billing.dtl_received_edit = new Ext.form.NumberField();
Chox.billing.dtl_receivedDate_edit = new Ext.form.DateField({
    format: 'd/m/Y'
});

Chox.billing.BillingDetailGrid = Ext.extend( Ext.grid.EditorGridPanel,{
    height: 395,
    width: 775,
    loadMask: true,
    initComponent:function(){
        this.title = Chox.billing.billingPageTitle + ' Details';
        this.tbar = new Ext.Toolbar({
            items:[{
                text:'Save ',
                disabled : true,
                id : 'save_button_id',
                handler : function(button){
                    var x = cb.bstore.getById(cb.bdetails.billingId);
                    var mrecs = cb.bdetails.getModifiedRecords();
                    var ma = new Array();
                    for(var i = 0 ; i < mrecs.length ; i++){
                        ma[i] = mrecs[i].data;
                    }
                    if (ma.length < 1) {
                        Ext.MessageBox.show({
                            title: 'Error',
                            msg: 'No record changes found. Only the modified record(s) will be saved.',
                            width:300,
                            buttons: Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        });
                        button.disable();
                        return false;
                    }
                    
                    jstr = Ext.util.JSON.encode(ma);
                    
                    choxExtAjaxRequest({
                        url: '/prv/p/updateBillingDetail.action',
                        callback : function(options,success,response  ){

                            var resp = Ext.util.JSON.decode(response.responseText);

                            if(resp.success){
                                cb.bdetails.commitChanges();
                                cb.bstore.reload();
                                Ext.MessageBox.show({
                                    title: 'Success',
                                    msg: resp.message,
                                    width:200,
                                    buttons: Ext.MessageBox.OK
                                });
                                button.disable();
                            }
                        },
                        params: {
                            billingId:cb.bdetails.billingId,
                            jsonData:jstr,
                            billingType:Chox.billing.billingmode
                        }
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
        dataIndex : 'claimReferenceId',
        sortable: true
    },{
        header : 'Trigger Date',
        renderer: Ext.util.Format.dateRenderer('d/m/Y'),
        dataIndex : 'triggerDate'
    },{
        header : 'Trigger Point',
        dataIndex : 'triggerPoint',
        sortable: true
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
        editor: cb.dtl_receivedDate_edit,
        renderer: Ext.util.Format.dateRenderer('d/m/Y'),
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
        headerclick: function ( grid, columnIndex, e ) { 
            if (columnIndex === 4 ){
                if (grid.store.find('reconciled','false') > -1 ) {
                    grid.store.each(function(){
                        //this.beginEdit();
                        if ( this.get('reconciled') === false) {
                            setReconciled(this);
                        }
                    });
                }else {
                    if (grid.store.find('reconciled','true') > -1 ) {
                        grid.store.each(function() {
                            //this.beginEdit();
                            if ( this.get('reconciled') === true) {
                                setNotReconciled(this);
                            }
                        });
                    }
                }
            }
        },
        cellclick:function( grid, rowIndex, columnIndex,  e ) { 
            var x = cb.bstore.getById(cb.bdetails.billingId);
            var rec= grid.store.getAt(rowIndex);

            if (columnIndex === 4 ) {

                if ( rec.get('reconciled') === false ){
                    setReconciled(rec);
                } else {
                    setNotReconciled(rec);
                }
            }
            if (columnIndex === 5 ) {
                if(rec.get('reconciled') === true){
                    Ext.MessageBox.show({
                        title: '',
                        msg: 'Comment can not be added to reconciled record.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                    return false;
                }
            }
            this.getTopToolbar().items.get('save_button_id').enable();
            return true;
        }
    }
});

