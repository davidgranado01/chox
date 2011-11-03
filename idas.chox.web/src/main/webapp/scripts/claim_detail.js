var switchClaimWindow;
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
    
    var mappedInsurersStore = new Ext.data.Store({
        data : mappedInsurers,
        reader : mappedInsurersJsonReader
    });
    
    
    var switchClaimToMulInsForm = new Ext.FormPanel({
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
            value: policyNumber,
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
            name : 'nonce',
            value : nonce
        }
        ],
        buttons:[{
            text:'Switch Claim',
            handler:function(){
                if(switchClaimToMulInsForm.getForm().isValid()){
                    switchClaimToMulInsForm.getEl().mask();
                    switchClaimToMulInsForm.getForm().submit({
                        method:'POST',
                        url : contextPath + "/prv/p/switchClaimToMulInsAction.action",
                        
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
    
});
function switchClaimToMultipleInsurer(){
    switchClaimWindow.show(document.body);
}