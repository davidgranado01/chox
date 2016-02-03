var switchClaimWindow;
var closeClaimWindow;
var mappedInsurersStore;
var closeClaimReasonsStore;
var switchClaimToMulInsForm;
var closeClaimForm;
var daysArray = [];
var slaExtensionWindow;
var availableSlaExtensionDays = availableSlaExtensionDays;

Ext.onReady(function(){
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'under';
    var mappedInsurersJsonReader = new Ext.data.JsonReader({
        totalProperty: 'totalCount',
        root: 'results',
        fields:
        [
        {
            name:'text'
        },

        {
            name:'value'
        }
        ]
    });
    
    mappedInsurersStore = new Ext.data.Store({
        reader : mappedInsurersJsonReader
    });
    
    for (var i=1; i<=availableSlaExtensionDays; i++) {
        daysArray.push([i,i]);
    }
        
    var slaExtStore = new Ext.data.SimpleStore({
        fields : ['field1', 'field2'],
        data : daysArray
    });
        
    var slaExtensionForm = new choxExtJsFormPanel({
        id : 'slaExtensionForm-form',
        height : 150,
        frame : true,
        buttonAlign : 'center',
        labelAlign : 'right',
        labelWidth : 200,
        labelSeparator : '',
                
        items : [
        {
            xtype : 'combo',
            store : slaExtStore,
            width : 40,
            fieldLabel : 'How many days extension do you wish to offer to the Insurer?',
            valueField : 'field1',
            value : 1,
            id : 'maxAllowedSlaExtComboId',
            displayField : 'field2',
            mode : 'local',
            triggerAction : 'all',
            forceSelection : true,
            listWidth : 40,
            selectOnFocus : true,
            editable : false
        }],
        buttons:[{
            text:'Apply',
            handler:function(){
                if(slaExtensionForm.getForm().isValid()){
                    slaExtensionForm.getEl().mask();
                    slaExtensionForm.getForm().submit({
                        method:'POST',
                        url : contextPath + "/prv/p/updateSlaExtensionDays.action",
                        params:{slaExtDays : parseInt(appliedSlaExtDays) + parseInt(Ext.get('maxAllowedSlaExtComboId').getValue()), name:'slaExtensionDaysUpdate'},
                        success : function(f, a) {
                            
                            if ( a.result.success ){
                                slaExtensionWindow.hide();
                                Ext.get('claimDetailScreenDiv').mask("Refreshing claim details...");
                                loadClaimDetail();
                            }
                        },
                        failure : function(f, a) {
                            var msg='Unexpected error occurred. Please contact Chox support.';
                            if(a.result.errors){
                                msg = a.result.errors;
                            }
                            Ext.MessageBox.show({
                                title : 'Error',
                                msg : msg,
                                width : 300,
                                closable : false,
                                buttons : Ext.MessageBox.OK,
                                icon : Ext.MessageBox.ERROR,
                                fn : function(){
                                    slaExtensionWindow.hide();
                                    loadClaimDetail();
                                }
                            }); 
                        }
                    });
                }
            }
        },{
            text:'Cancel',
            handler:function(){
                slaExtensionForm.getForm().reset();
                slaExtensionWindow.hide();
            }
        }]
    });
    
    switchClaimToMulInsForm = new choxExtJsFormPanel({
        id : 'switchClaimForm-form',
        height : 150,
        frame : true,
        buttonAlign : 'center',
        items : [
        {
            xtype : 'combo',
            name : 'insId',
            width : 180,
            typeAhead : false,
            fieldLabel : 'Insurer',
            labelStyle : 'text-align:right;',
            mode : 'local',
            blankText : 'Please Select an Insurer',
            emptyText : 'Please Select an Insurer',
            store : mappedInsurersStore,
            hiddenName : 'insId',
            displayField : 'value',
            valueField : 'text',
            allowBlank : false,
            triggerAction : 'all',
            editable : false
        },{
            xtype : 'textfield',
            fieldLabel : 'Policy Number',
            width : 180,
            labelStyle : 'text-align:right;',
            id : 'policyNumberId',
            name : 'policyNumber',
            allowBlank : false,
            blankText : 'Please Enter a Policy Number'
        },{
            xtype : 'hidden',
            id : 'nameId',
            name : 'name',
            value : 'switchClaimToMulIns'
        }
        ],
        buttons:[{
            text:'Switch Claim',
            handler:function(){
                if(switchClaimToMulInsForm.getForm().isValid()){
                    switchClaimToMulInsForm.getEl().mask();
                    switchClaimToMulInsForm.getForm().submit({
                        method:'POST',
                        url : contextPath + "/prv/p/switchClaim.action",
                        
                        success : function(f, a) {
                            
                            if ( a.result.success ){
                                Ext.MessageBox.show({
                                    title: 'Success',
                                    msg: 'Claim Switched Successfully',
                                    width : 300,
                                    buttons : Ext.MessageBox.OK,
                                    fn : function(){
                                        switchClaimWindow.hide();
                                        loadClaimDetail();
                                    }
                                });
                            }
                        },
                        failure : function(f, a) {
                            var msg='Unexpected error occurred. Please contact Chox support.';
                            if(a.result.errors){
                                msg = a.result.errors;
                            }
                            Ext.MessageBox.show({
                                title : 'Error',
                                msg : msg,
                                width : 300,
                                closable : false,
                                buttons : Ext.MessageBox.OK,
                                icon : Ext.MessageBox.ERROR,
                                fn : function(){
                                    switchClaimWindow.hide();
                                    loadClaimDetail();
                                }
                            }); 
                        }
                    });
                }
            }
        },{
            text:'Cancel',
            handler:function(){
                switchClaimToMulInsForm.getForm().reset();
                switchClaimWindow.hide();
            }
        }]
    });
    
    switchClaimWindow = new Ext.Window({
        layout:'fit',
        width : 400,
        height : 150,
        closable :false,
        modal : true,
        resizable : false,
        items : [
        switchClaimToMulInsForm
        ]
    });
    
    slaExtensionWindow = new Ext.Window({
        layout : 'fit',
        width : 300,
        height : 100,
        closable : false,
        resizable : false,
        items : [
            slaExtensionForm
        ]
    });

    var closeClaimReasonsJsonReader = new Ext.data.JsonReader({
        totalProperty: 'totalCount',
        root: 'results',
        fields: [
            {name:'text'},
            {name:'value'}
        ]
    });
    
    closeClaimReasonsStore = new Ext.data.Store({
        reader : closeClaimReasonsJsonReader
    });
    
    closeClaimForm = new Ext.FormPanel({
        id: 'closeClaimForm-form',
        height : 180,
        width : 420,
        frame:true,
        buttonAlign : 'center',
        items : [
        {
            xtype : 'combo',
            name : 'closeReason',
            id : 'closeReasonComboId',
            width : 250,
            listWidth : 250,
            typeAhead : false,
            fieldLabel : 'Reason',
            labelStyle : 'text-align:right;',
            mode : 'local',
            emptyText : 'Please Select a Reason',
            blankText : 'Please Select a Reason',
            store : closeClaimReasonsStore,
            hiddenName : 'closeReason',
            displayField : 'text',
            valueField : 'text',
            allowBlank : false,
            triggerAction : 'all',
            listeners: {select: function(combo, record, index) {
                    Ext.getCmp('closureNoteTextId').setValue(closeClaimReasonsStore.getAt(index).get('value'));
                }},
            editable : false
        },{
            xtype: 'textarea',
            fieldLabel: 'Supporting Note',
            labelStyle : 'text-align:right;',
            msgTarget : 'qtip',
            name: 'closureNote',
            id: 'closureNoteTextId',
            allowBlank: true,
            minLength: 5,
            maxLength: 256,
            maxLengthText: 'maximum of 256 characters',
            minLengthText: 'minimum of 5 characters',
            width: 250,
            height : 50,
        },{
            xtype : 'hidden',
            id : 'nameId',
            name : 'name',
            value : 'closeClaim'
        }
        ],
        buttons:[{
            text:'Close Claim',
            handler:function(){
                if(closeClaimForm.getForm().isValid()){
                    closeClaimForm.getEl().mask();
                    var url = contextPath + "/prv/processClaim.action";
                    var form = $('<form action="' + url + '" method="post">' +
                        '<input type="hidden" name="name" value="closeClaim"/>' +
                        '<input type="hidden" name="closeReason" value="'+ Ext.getCmp('closeReasonComboId').getValue() +'"/>' +
                        '<input type="hidden" name="closeNote" value="'+ Ext.getCmp('closureNoteTextId').getValue() +'"/>' +
                        '</form>');
                    $('body').append(form);
                    choxJqueryHttpSubmit($(form));
                }
            }
        },{
            text:'Cancel',
            handler:function(){
                closeClaimForm.getForm().reset();
                closeClaimWindow.hide();
            }
        }]
    });

    closeClaimWindow = new Ext.Window({
        layout : 'fit',
        width : 420,
        height : 180,
        plain: false,
        title: 'Close Claim',
        modal : true,
        closable : false,
        resizable : false,
        items : [
            closeClaimForm
        ]
    });
    

});

function switchClaimToMultipleInsurer(){
    switchClaimWindow.show(document.body);
}

function closeClaimStatus(){
//    closeClaimForm.getForm().setValues([{id : 'closeNonceId', value : nonce}]);
    closeClaimWindow.show(document.body);
}

function setSlaExtension() {
    slaExtensionWindow.show(document.body);
}