var switchClaimWindow;
var mappedInsurersStore;
var switchClaimToMulInsForm;
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
        fields: ['field1', 'field2'],
        data : daysArray
    });
        
    var slaExtensionForm = new Ext.FormPanel({
        id: 'slaExtensionForm-form',
        height : 150,
        frame:true,
        buttonAlign : 'center',
        labelAlign : 'right',
        labelWidth : 200,
        labelSeparator : '',
                
        items : [
        {
            xtype : 'combo',
            store: slaExtStore,
            width: 40,
            fieldLabel : 'How many days extension do you wish to offer to the Insurer?',
            valueField: 'field1',
            value : 1,
            id: 'maxAllowedSlaExtComboId',
//            hiddenName: 'slaExtDays',
            displayField:'field2',
            mode: 'local',
            triggerAction: 'all',
            forceSelection: true,
            listWidth: 40,
            selectOnFocus: true,
            editable : false
        }],
        buttons:[{
            text:'Apply',
            handler:function(){
                if(slaExtensionForm.getForm().isValid()){
//                    Ext.getCmp('extNonceId').setValue(nonce);
                    slaExtensionForm.getEl().mask();
                    slaExtensionForm.getForm().submit({
                        method:'POST',
                        url : contextPath + "/prv/p/updateSlaExtensionDays.action",
                        params:{slaExtDays : parseInt(appliedSlaExtDays) + parseInt(Ext.get('maxAllowedSlaExtComboId').getValue()), nonce : nonce},
                        success : function(f, a) {
                            
                            if ( a.result.success ){
                                slaExtensionWindow.hide();
                                Ext.get('claimDetailScreenDiv').mask("Refreshing claim details...");
                                window.location = contextPath+"/prv/openClaimDetail.action" ; 
                            }
                        },
                        failure : function(f, a) {
                            var msg='Unexpected error occured. Please contact Chox support.';
                            if(a.result.errors){
                                msg = a.result.errors;
                            }
                            Ext.MessageBox.show({
                                title: 'Error',
                                msg: msg,
                                width:300,
                                closable : false,
                                buttons: Ext.MessageBox.OK,
                                icon : Ext.MessageBox.ERROR,
                                fn : function(){
                                    slaExtensionWindow.hide();
                                    window.location = contextPath+"/prv/openClaimDetail.action" ;  
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
    
    switchClaimToMulInsForm = new Ext.FormPanel({
        id: 'switchClaimForm-form',
        height : 150,
        frame:true,
        //        buttonAlign : 'center',
        items : [
        {
            xtype : 'combo',
            name : 'insId',
            width : 180,
            typeAhead : false,
            fieldLabel : 'Insurer',
            labelStyle: 'text-align:right;',
            mode : 'local',
            blankText: 'Please select an Insurer',
            store : mappedInsurersStore,
            hiddenName : 'insId',
            displayField : 'value',
            valueField : 'text',
            allowBlank: false,
            triggerAction : 'all',
            editable : false
        },{
            xtype : 'textfield',
            fieldLabel: 'Policy Number',
            width : 180,
            labelStyle: 'text-align:right;',
            id : 'policyNumberId',
            name: 'policyNumber',
            allowBlank: false,
            blankText: 'Please enter a Policy Number'
        },{
            xtype : 'hidden',
            id : 'nameId',
            name : 'name',
            value : 'switchClaimToMulIns'
        },{
            xtype : 'hidden',
            id : 'nonceId',
            name : 'nonce'
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
//                        url : contextPath + "/prv/processClaim.action",
                        
                        success : function(f, a) {
                            
                            if ( a.result.success ){
                                Ext.MessageBox.show({
                                    title: 'Success',
                                    msg: 'Claim Switched Successfully',
                                    width:300,
                                    buttons: Ext.MessageBox.OK,
                                    fn : function(){
                                        switchClaimWindow.hide();
                                        window.location = contextPath+"/prv/openClaimDetail.action" ;  
                                    }
                                });
                            }
                        },
                        failure : function(f, a) {
                            var msg='Unexpected error occured. Please contact Chox support.';
                            if(a.result.errors){
                                msg = a.result.errors;
                            }
                            Ext.MessageBox.show({
                                title: 'Error',
                                msg: msg,
                                width:300,
                                closable : false,
                                buttons: Ext.MessageBox.OK,
                                icon : Ext.MessageBox.ERROR,
                                fn : function(){
                                    switchClaimWindow.hide();
                                    window.location = contextPath+"/prv/openClaimDetail.action" ;  
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
        width:360,
        height : 150,
        closable:false,
        resizable : false,
        items : [
        switchClaimToMulInsForm
        ]
    });
    
    slaExtensionWindow = new Ext.Window({
        layout:'fit',
        width:300,
        height : 100,
        closable:false,
        resizable : false,
        items : [
        slaExtensionForm
        ]
    });
    
});

function switchClaimToMultipleInsurer(){
    switchClaimWindow.show(document.body);
}

function setSlaExtension() {
    slaExtensionWindow.show(document.body);
}