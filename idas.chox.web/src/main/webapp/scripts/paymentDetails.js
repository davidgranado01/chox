var win;
Ext.onReady(function(){
    
    Ext.override(Ext.form.NumberField, {
        setValue: function(v) {
            var dp = this.decimalPrecision;
            if (dp < 0 || !this.allowDecimals) {
                dp = 0;
            }
            v = this.fixPrecision(v);
            v = Ext.isNumber(v) ? v : parseFloat(String(v).replace(this.decimalSeparator, "."));
            v = isNaN(v) ? '' : String(v.toFixed(dp)).replace(".", this.decimalSeparator);
            return Ext.form.NumberField.superclass.setValue.call(this, v);
        }
    });
    
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    var checkGroup = {
        xtype: 'fieldset',
        title: 'Penalty Charge',
        layout: 'form',
        collapsed: !panaltyChargeApplied,   
        collapsible: false,
        items: [{
            xtype: 'radiogroup',
            fieldLabel: 'Have penalty charges been paid?',
            columns: [.35, .25],
            width : 150,
            allowBlank: !panaltyChargeApplied,
            id : 'penaltyChargesPaidId',
            listeners: {
                change: function () {
                    
                    if(this.getValue()!=null && (this.getValue().getGroupValue()=='false')){
                        var totalPaidAmount = Ext.getCmp('totalPaidId').getValue();
                        var totalPenaltyAmount = Ext.getCmp('hirePenaltyId').getValue() + Ext.getCmp('repairPenaltyId').getValue();
                        Ext.getCmp('hirePenaltyId').setValue(0.00);
                        Ext.getCmp('repairPenaltyId').setValue(0.00);
                        Ext.getCmp('totalPaidId').setValue(totalPaidAmount-totalPenaltyAmount);
                    }else{
                        Ext.getCmp('hirePenaltyId').setValue(hirePenaltyChargePaid);
                        Ext.getCmp('repairPenaltyId').setValue(repairPenaltyChargePaid);
                        Ext.getCmp('totalPaidId').setValue(totalPaid);
                    }
                }
            },
            
            items: [
            {
                boxLabel: 'Yes', 
                name: 'penaltyChargesPaid',
                inputValue: true
                
            },

            {
                boxLabel: 'No', 
                name: 'penaltyChargesPaid',
                inputValue: false
                
            }
            ]
        }]
    };
    
    var textGroup = {
        xtype: 'fieldset',
        title: 'Payment Details',
        layout: 'form',
        collapsed: false,   
        collapsible: false,
        defaultType:'numberfield',
        defaults: {              
            labelStyle: 'text-align: right;',
            allowBlank:false,
            width : 100,
            allowNegative : false,
            decimalPrecision : 2,
            minValue : 0.00
        },
        items: [
        {
            fieldLabel: 'Hire Gross Paid',
            id : 'hireGrossId',
            name: 'hireGrossPaid',
            value: hireGrossPaid
        },{
            fieldLabel: 'Repair Gross Paid',
            id : 'repairGrossId',
            name: 'repairGrossPaid',
            value: repairGrossPaid
        },{
            fieldLabel: 'Engineer Fee Gross Paid',
            id : 'engineerGrossId',
            name: 'engineerFeeGrossPaid',
            value: engineerFeeGrossPaid
        }, {
            fieldLabel: 'Total Loss Fee Gross Paid',
            id : 'totalLossFeeGrossId',
            name: 'totalLossFeeGrossPaid',
            value: totalLossFeeGrossPaid
        },{
            fieldLabel: 'Storage Recovery Gross Paid',
            id : 'storageRecoveryGrossId',
            name: 'storageRecoveryGrossPaid',
            value: storageRecoveryGrossPaid
        },{
            fieldLabel: 'Hire Penalty Charges Paid',
            id : 'hirePenaltyId',
            name: 'hirePenaltyChargePaid',
            value: hirePenaltyChargePaid
        }, {
            fieldLabel: 'Repair Penalty Charges Paid',
            id : 'repairPenaltyId',
            name: 'repairPenaltyChargePaid',
            value: repairPenaltyChargePaid
        },{
            fieldLabel: 'Total Paid',
            id : 'totalPaidId',
            name: 'totalPaid',
            value: totalPaid
        },{
            xtype : 'hidden',
            id : 'nonceId',
            name : 'nonce',
            value : nonce
        }
        ]
    };
    
    var paymentDetailsForm = new Ext.FormPanel({
        autoHeight: true,
        labelWidth: 190,
        frame:true,
        title:'Payment Details',
        items:[
        checkGroup,
        textGroup
        ]
        
    });

    win = new Ext.Window({
        layout:'fit',
        width:450,
        autoHeight: true,
        closable:false,
        resizable : false,
        items : [
        paymentDetailsForm
        ],
        buttonAlign : 'center',
        buttons:[{
            text:'Confirm Payment Details',
            handler:function(){
                if(paymentDetailsForm.getForm().isValid()){
                    paymentDetailsForm.getForm().submit({
                        method:'POST',
                        waitTitle:'Connecting',
                        waitMsg:'Sending data...',
                        url:'/prv/p/updatePaymentDetails.action',
                        
                        success : function(f, a) {

                            if ( a.result.success ){
                                win.hide();
                                Ext.get('claimDetailScreenDiv').mask("Refereshing Claim Details ...");
                                var queryString = $('#logInvoicePayment').formSerialize();
                                window.location = contextPath+"/prv/processClaim.action?" + queryString;
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
            }
        },{
            text:'Cancel Payment Confirmation',
            handler:function(){
                paymentDetailsForm.getForm().reset();
                win.hide();
            }
        }]
    });

    
});

function confirmPaymentlogAction(){
        
    if(paymentDetailsConfirmationEnabled){
        win.show(document.body);
    }else{
        Ext.Msg.show({
            title      : 'Confirm',
            msg        : 'Clicking on this button indicates to the CHO that payment has been made on your internal claims system.  Click \'OK\' to confirm payment has been made.',
            width      : 800,
            buttons    : Ext.MessageBox.OKCANCEL,
            fn         : function(btn) {
                if(btn=='ok') {
                    var queryString = $('#logInvoicePayment').formSerialize();
                    window.location = contextPath + "/prv/processClaim.action?" + queryString;
                }
            }
        });
    }
        
}
        
 