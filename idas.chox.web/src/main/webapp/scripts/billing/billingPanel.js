
/*
Chox.billing.BillingPanel = Ext.extend( Ext.Panel, {
    initComponent:function() {
        console.log('billlingPanel '+ this.billingType);
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
    autoLoad : true,
    listeners : {
        load : function(thisstore, recs, opts) {
            console.log('combo date loaded');
        }
    }
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
                fieldLabel : 'Insurers',
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
                fieldLabel : 'Chox',
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
            console.log('save');
            
            console.log(Ext.getCmp('refbillingform').getForm());
            Ext.getCmp('refbillingform').getForm().submit( {
                success : function(f, a) {
                    console.log('ave');
                    if ( a.result.success ){
                        console.log('success');
                        //Ext.getCmp('refbillingstore').reload();
                        cb.billingWindowObj.hide();
                        cb.bstore.reload();
                    }
                //console.log(a.result);
                },
                failure : function(f, a) {
                    console.log(a.result);
                }
            });
            
        }

    }, {
        text : 'Cancel',
        handler : function(){
        // Chox.billing.billingWindow.hide();
        }
    } ]
  
});



Chox.billing.billingWindow = Ext.extend(Ext.Window, {
    constructor:function(){
        this.items = [
        cb.billingFormObj
        ];
        this.title = 'Add '+ Chox.billing.billingHeader1 + ' Schedule';
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
                    console.log(chkbx.getName()+' '+checked);
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
                    console.log('ave');
                    if ( a.result.success ){
                        console.log('success');                        
                        cb.paymentWindowObj.hide();
                        cb.bstore.reload();
                    }
                
                },
                failure : function(f, a) {
                    console.log(a.result);
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
            console.log('render');
            console.log(cb.schSel.getSelected().get('manual'));
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
    singleSelect:true
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
                text:'Add ',
                handler : function() {
                    console.log('add clicked');
                    cb.billingFormObj.getForm().reset();
                    cb.billingWindowObj.show();
                }
            },{
                text:'Delete ',
                handler : function(){
                    var selected = cb.schSel.getSelected();
                    if( selected ){
                        console.log(selected.get('billingId'));
                        Ext.Ajax.request({
                            url: Chox.appname + '/prv/p/deleteBill.action',
                            callback : function(options,success,response  ){
                                console.log('callback success '+success);
                                var resp = Ext.util.JSON.decode(response.responseText);
                                console.log('callback '+response.responseText);
                                cb.bstore.reload();
                                cb.bdetails.reload();
                                if(resp.success){
                                    console.log('delete success');
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
            },{
                text:'Download ',
                handler : function() {

                    var selected = cb.schSel.getSelected();
                    if( selected ){
                        //var df = selected.data.dateFrom.format('Y-m-d');
                        //var dt = selected.data.dateTo.format('Y-m-d');
                        //var dtstr = '&dateFrom='+df+'&dateTo='+dt;
                        //console.log(Chox.appname+ '/prv/p/exportExcelReport.action?reportName=BillingInsurerReport-Excel&' +Ext.urlEncode(selected.data)+dtstr);
                        var rptName;
                        if ( Chox.billing.billingmode =='insurer'){
                            rptName = 'BillingInsurerReport-Excel';
                        }else{
                            rptName = 'BillingChoReport-Excel';
                        }
                        var rpthref = Chox.appname+ '/prv/p/exportExcelReport.action?reportName=' + rptName +'&' +Ext.urlEncode(selected.data);//+dtstr;
                        console.log(rpthref);
                        location.href = rpthref
                    }
                }
            },{
                text:'Details',
                handler : function() {
                    var selected = cb.schSel.getSelected();
                    console.log(selected.get('billingId'));
                    cb.bdetails.load({
                        params:{
                            billingId:selected.get('billingId')
                        }
                    });
                }
            },{
                text:'Reconcile',
                handler : function() {
                    var selected = cb.schSel.getSelected();
                    if ( selected ){
                        console.log(selected.get('billingId'));
                        
                        cb.paymentWindowObj.show();
                        cb.paymentFormObj.getForm().loadRecord(selected);
                    }

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


////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
//
//
//
//
//  900
// 1300
//  850
// 3050
//
//
//
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

Chox.billing.BillingDetailStore = function(){
    Chox.billing.BillingDetailStore.superclass.constructor.apply(this, arguments);
}

Ext.extend(Chox.billing.BillingDetailStore,Ext.data.Store,{
    url : Chox.appname + '/prv/p/listBillingDetailGridData.action',

    reader : new Ext.data.JsonReader( {
        root : 'results',
        id : 'id'
    }, [ 'id', 'scheduleName', 'claimReferenceId', {
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
        //updateScheduleDetailStatus(store);
        },
        load : function( store, recarr, operation ){
        //updateScheduleDetailStatus(store);
        }
    }

});


function setReconciled(rec){
    rec.set('reconciled',true);
    rec.set('receivedDate',new Date().format("d/m/Y H:i:s"));
    if ( cb.billingmode == 'insurer'){
        rec.set('amountReceived',getBenefitValue());
    }else{
        rec.set('amountReceived',rec.get('itemAmount'));
    }
}

function setNotReconciled(rec){
    rec.set('reconciled',false);
    rec.set('receivedDate',new Date().format("d/m/Y H:i:s"));
    rec.set('amountReceived',0);
}
function retDate(){
    var dt = new Date();
    var dts = dt.format("d/m/Y H:i:s").toString();
    return dts+"";
}
        function getBenefitValue(){
            /*
            console.log('getbenefit value called ' + scheduleId);
            var rec = insurerScheduleStore.getById(scheduleId);
            return rec.get('benefitValue');
            */
           return 12.5;
        }

cb.bdetails = new Chox.billing.BillingDetailStore({
    baseParams:{
        billingType:Chox.billing.billingmode
    }
});
Chox.billing.BillingDetailGrid = Ext.extend( Ext.grid.GridPanel,{
    height : 420,
    initComponent:function(){
        this.title = Chox.billing.billingPageTitle + ' Details',
        this.tbar = new Ext.Toolbar({
            items:[{
                text:'Save '
            }]
        });
        this.bbar = new Ext.StatusBar();
        Chox.billing.BillingDetailGrid.superclass.initComponent.call(this);
        console.log('billing detail grid');
    },
    store: cb.bdetails,
    columns:[{
        header : 'Schedule',
        dataIndex : 'scheduleName'
    },{
        header : 'Claim Reference',
        dataIndex : 'claimReferenceId'
    },{
        header : 'Amount',
        dataIndex : 'itemAmount',
        renderer: 'gbMoney',
        align:'right'
    },{
        header : 'Received',
        dataIndex : 'amountReceived',
        renderer: 'gbMoney',
        align:'right'
    },{
        header : 'Received Date',
        dataIndex : 'receivedDate'

    },{
        header : 'Reconciled',
        dataIndex : 'reconciled'
    },{
        header : 'Comment',
        dataIndex : 'comment'
    }],
    listeners:{
        cellclick:function( grid, rowIndex, columnIndex,  e ){
            //console.log('r ' + rowIndex + ' c ' + columnIndex);
            if (columnIndex == 5 ){
                var rec = grid.store.getAt(rowIndex);
                console.log(rec.get('reconciled'));
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
        }
    }
});