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
    
    Ext.apply(Ext.form.VTypes, {
        penaltyrangeText : 'Hire Penalty Charges Paid or Repair Penalty Charges Paid should be greater than zero.',
        penaltyrange : function(val, field) {
            if(Ext.getCmp('penaltyChargesPaidId') !=null && Ext.getCmp('penaltyChargesPaidId').getValue() !=null && (Ext.getCmp('penaltyChargesPaidId').getValue().getGroupValue()=='true')){
                if(val>0){
                    return true;
                }
                else if (field.nextField && Ext.getCmp(field.nextField).getValue()>0) {
                    return true;
                } 
                else if (field.beforeField && Ext.getCmp(field.beforeField).getValue()>0) {
                    return true;
                }else{
                    return false;
                }
            }else{
                return true;
            }
        }
    });
    
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    var checkGroup = {
        xtype: 'fieldset',
        title: 'Penalty Charge',
        layout: 'form',
        disabled : !panaltyChargeApplied,  
        collapsed: !panaltyChargeApplied,   
        collapsible: false,
        items: [{
            xtype: 'radiogroup',
            fieldLabel: 'Have penalty charges been paid?',
            columns: [.35, .25],
            width : 150,
            allowBlank: !panaltyChargeApplied,
            id : 'penaltyChargesPaidId',
            blankText: 'Please select Yes or No',
            listeners: {
                change: function () {
                    
                    if(this.getValue()!=null && (this.getValue().getGroupValue()=='false')){
                        var totalPenaltyAmount = hirePenaltyChargePaid + repairPenaltyChargePaid;
                        Ext.getCmp('hirePenaltyId').setValue(0.00);
                        Ext.getCmp('repairPenaltyId').setValue(0.00);
                        Ext.getCmp('totalPaidId').setValue(totalPaid-totalPenaltyAmount);
                        Ext.getCmp('repairPenaltyId').setReadOnly(true);
                        Ext.getCmp('hirePenaltyId').setReadOnly(true);
                    }else{
                        Ext.getCmp('hirePenaltyId').setValue(hirePenaltyChargePaid);
                        Ext.getCmp('repairPenaltyId').setValue(repairPenaltyChargePaid);
                        Ext.getCmp('totalPaidId').setValue(totalPaid);
                        Ext.getCmp('repairPenaltyId').setReadOnly(false);
                        Ext.getCmp('hirePenaltyId').setReadOnly(false);
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
            value: hireGrossPaid,
            blankText: 'Hire Gross Paid is required'
        },{
            fieldLabel: 'Repair Gross Paid',
            id : 'repairGrossId',
            name: 'repairGrossPaid',
            value: repairGrossPaid,
            blankText: 'Repair Gross Paid is required'
        },{
            fieldLabel: 'Engineer Fee Gross Paid',
            id : 'engineerGrossId',
            name: 'engineerFeeGrossPaid',
            value: engineerFeeGrossPaid,
            blankText: 'Engineer Fee Gross Paid is required'
        }, {
            fieldLabel: 'Total Loss Fee Gross Paid',
            id : 'totalLossFeeGrossId',
            name: 'totalLossFeeGrossPaid',
            value: totalLossFeeGrossPaid,
            blankText: 'Total Loss Fee Gross Paid is required'
        },{
            fieldLabel: 'Storage Recovery Gross Paid',
            id : 'storageRecoveryGrossId',
            name: 'storageRecoveryGrossPaid',
            value: storageRecoveryGrossPaid,
            blankText: 'Storage Recovery Gross Paid is required'
        },{
            fieldLabel: 'Hire Penalty Charges Paid',
            id : 'hirePenaltyId',
            name: 'hirePenaltyChargePaid',
            value: hirePenaltyChargePaid,
            blankText: 'Hire Penalty Charges Paid is required',
            vtype: 'penaltyrange',
            nextField : 'repairPenaltyId',
            enableKeyEvents : true,
            readOnly : true,
            listeners: {
                keyup: function() {
                    Ext.getCmp('repairPenaltyId').validate();
                }
            }
        }, {
            fieldLabel: 'Repair Penalty Charges Paid',
            id : 'repairPenaltyId',
            name: 'repairPenaltyChargePaid',
            value: repairPenaltyChargePaid,
            blankText: 'Repair Penalty Charges Paid is required',
            vtype: 'penaltyrange',
            beforeField : 'hirePenaltyId',
            enableKeyEvents : true,
            readOnly : true,
            listeners: {
                keyup: function() {
                    Ext.getCmp('hirePenaltyId').validate();
                }
            }
        },{
            fieldLabel: 'Total Paid',
            id : 'totalPaidId',
            name: 'totalPaid',
            value: totalPaid,
            blankText: 'Total Paid is required'
        },{
            xtype : 'hidden',
            id : 'nonceId',
            name : 'nonce',
            value : nonce
        }
        ]
    };
    
    var paymentDetailsForm = new Ext.FormPanel({
        id: 'paymentDetails-form',
        autoHeight: true,
        labelWidth: 190,
        frame:true,
        title:'<div class="status-info">Please confirm that the below payment details are correct and have been logged correctly, if you need to modify the details you can do so: </div>',
        items:[
        textGroup
        ],
        buttonAlign : 'center',
        buttons:[{
            text:'Confirm Payment Details',
            handler:function(){
                if(paymentDetailsForm.getForm().isValid()){
                    var sb = Ext.getCmp('form-statusbar');
                    sb.showBusy('Saving Payment details...');
                    paymentDetailsForm.getEl().mask();
                    paymentDetailsForm.getForm().submit({
                        method:'POST',
                        url:contextPath +'/prv/p/updatePaymentDetails.action',
                        
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
    
    if(panaltyChargeApplied){
        paymentDetailsForm.add(checkGroup);
        Ext.getCmp('repairPenaltyId').setReadOnly(false);
        Ext.getCmp('hirePenaltyId').setReadOnly(false);
    }

    win = new Ext.Window({
        layout:'fit',
        width:450,
        autoHeight: true,
        closable:false,
        resizable : false,
        items : [
        paymentDetailsForm
        ],
        bbar: new Ext.ux.StatusBar({
            id: 'form-statusbar',
            defaultText: '',
            plugins: new Ext.ux.ValidationStatus({
                form:'paymentDetails-form'
            })
        })
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
        
 