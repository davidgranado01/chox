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
        hidden : !panaltyChargeApplied,
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
                        Ext.getCmp('repairPenaltyId').getEl().applyStyles({
                            'text-align':'right',
                            background: '#e4e4e4'
                        });
                        Ext.getCmp('hirePenaltyId').getEl().applyStyles({
                            'text-align':'right',
                            background: '#e4e4e4'
                        });
                    }else{
                        Ext.getCmp('hirePenaltyId').setValue(hirePenaltyChargePaid);
                        Ext.getCmp('repairPenaltyId').setValue(repairPenaltyChargePaid);
                        Ext.getCmp('totalPaidId').setValue(totalPaid);
                        Ext.getCmp('repairPenaltyId').setReadOnly(false);
                        Ext.getCmp('hirePenaltyId').setReadOnly(false);
                        Ext.getCmp('repairPenaltyId').getEl().applyStyles({
                            'text-align':'right',
                            background: '#ffffff'
                        });
                        Ext.getCmp('hirePenaltyId').getEl().applyStyles({
                            'text-align':'right',
                            background: '#ffffff'
                        });
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
            width : 80,
            allowNegative : false,
            decimalPrecision : 2,
            minValue : 0.00,
            style: {
                'text-align':'right'
            }
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
            listeners: {
                keyup: function() {
                    Ext.getCmp('repairPenaltyId').validate();
                },
                afterrender : function(){
                    if(!panaltyChargeApplied){
                        this.setReadOnly(true);
                        this.getEl().applyStyles({
                            'text-align':'right',
                            background: '#e4e4e4'
                        });  
                    }
                    
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
            listeners: {
                keyup: function() {
                    Ext.getCmp('hirePenaltyId').validate();
                },
                afterrender : function(){
                    if(!panaltyChargeApplied){
                        this.setReadOnly(true);
                        this.getEl().applyStyles({
                            'text-align':'right',
                            background: '#e4e4e4'
                        });  
                    }
                    
                }
            }
        },{
            fieldLabel: 'Claims Handling Invoice Amount',
            id : 'paymentDetailsClaimHandInvAmtId',
            name: 'paymentDetailsClaimHandInvAmt',
            value: paymentDetailsClaimHandInvAmt,
            hidden : paymentDetailsClaimHandInvAmt<=0,
            minValue : -999999999.99,
            readOnly : true,
            style: {
                'text-align':'right',
                background: '#e4e4e4'
            }
        },{
            fieldLabel: 'Deduction For Claims Handling Fee',
            id : 'paymentDetailsDeductionClaimHandFeeId',
            name: 'paymentDetailsDeductionClaimHandFee',
            value: paymentDetailsDeductionClaimHandFee,
            hidden : paymentDetailsDeductionClaimHandFee<=0,
            minValue : -999999999.99,
            readOnly : true,
            style: {
                'text-align':'right',
                background: '#e4e4e4'
            }
        },{
            fieldLabel: 'CHO Discount',
            id : 'paymentDetailsCHODiscountId',
            name: 'paymentDetailsCHODiscount',
            value: paymentDetailsCHODiscount,
            hidden : paymentDetailsCHODiscount>=0,
            minValue : -999999999.99,
            maxValue : 0.00,
            readOnly : true,
            allowNegative : true,
            style: {
                'text-align':'right',
                background: '#e4e4e4',
                color:'red'
            }
        },{
            fieldLabel: 'Insurer Discount',
            id : 'paymentDetailsInsurerDiscountId',
            name: 'paymentDetailsInsurerDiscount',
            value: paymentDetailsInsurerDiscount,
            hidden : paymentDetailsInsurerDiscount>=0,
            minValue : -999999999.99,
            maxValue : 0.00,
            readOnly : true,
            allowNegative : true,
            style: {
                'text-align':'right',
                background: '#e4e4e4',
                color:'red'
            }
        },{
            fieldLabel: 'Total Paid',
            id : 'totalPaidId',
            name: 'totalPaid',
            value: totalPaid,
            blankText: 'Total Paid is required'
        },{
            xtype : 'label',
            id : 'labelId',
            width : '100%',
            hidden : interimPaymentAmount<=0,
            html : '<div class="status-info-popup">An Interim Payment has been made on this claim to the amount of <label style="color:red">£ '+interimPaymentAmount.toFixed(2)+'</label></div>',
            style: {
                'text-align':'center'
            }
            
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
        labelWidth: 210,
        frame:true,
        title:'<div class="status-info">Please confirm that the below payment details are correct, if you need to modify the payment details you can do so.  Please note that the figures take into account the agreed liability %.</div>',
        buttonAlign : 'center',
        items : [
        checkGroup,
        textGroup
        ],
        buttons:[{
            text:'Ok',
            handler:function(){
                if(paymentDetailsForm.getForm().isValid()){
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
                            
                            Ext.MessageBox.show({
                                title: 'Error',
                                msg: a.result.errors,
                                width:300,
                                buttons: Ext.MessageBox.OK,
                                icon : Ext.MessageBox.ERROR
                            }); 
                        }
                    });
                }
            }
        },{
            text:'Cancel',
            handler:function(){
                paymentDetailsForm.getForm().reset();
                win.hide();
            }
        }]
        
    });
    
    win = new Ext.Window({
        layout:'fit',
        width:450,
        autoHeight: true,
        closable:false,
        resizable : false,
        items : [
        paymentDetailsForm
        ]
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
        
 