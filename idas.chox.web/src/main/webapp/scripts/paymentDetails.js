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
        hidden : (penaltyChargeApplied == false),
        collapsible: false,
        items: [{
            xtype: 'radiogroup',
            fieldLabel: 'Have penalty charges been paid?',
            columns: [.35, .25],
            width : 150,
//            allowBlank: !penaltyChargeApplied,
            id : 'penaltyChargesPaidId',
            blankText: 'Please select Yes or No',
            listeners: {
                change: function () {
                    if(this.getValue()!=null && (this.getValue().getGroupValue()=='false')){
                        Ext.getCmp('hirePenaltyId').setValue(0.00);
                        Ext.getCmp('repairPenaltyId').setValue(0.00);
                        Ext.getCmp('totalToPayId').setValue(totalToPay);
                        Ext.getCmp('repairPenaltyId').setReadOnly(true);
                        Ext.getCmp('hirePenaltyId').setReadOnly(true);
                        Ext.getCmp('finalPayId').setValue(outstandingPayment - hirePenaltyChargePaid - repairPenaltyChargePaid);
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
                        Ext.getCmp('totalToPayId').setValue(totalToPay);
                        Ext.getCmp('repairPenaltyId').setReadOnly(false);
                        Ext.getCmp('hirePenaltyId').setReadOnly(false);
                        Ext.getCmp('finalPayId').setValue(outstandingPayment);
                        Ext.getCmp('repairPenaltyId').getEl().applyStyles({
                            'text-align':'right',
                            background: '#ffffff'
                        });
                        Ext.getCmp('hirePenaltyId').getEl().applyStyles({
                            'text-align':'right',
                            background: '#ffffff'
                        });
                    }
                }, load: function (){
                	Ext.getCmp('penaltyChargesPaidId').setValue([true, false]);
                }
            },
            items: [
            {
                boxLabel: 'Yes', 
                name: 'penaltyChargesPaid',
                inputValue: true,
                checked: (penaltyChargeApplied == true)
            },
            {
                boxLabel: 'No', 
                name: 'penaltyChargesPaid',
                inputValue: false,
                checked: !(penaltyChargeApplied == true)
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
            blankText: 'Hire Gross Paid is required',
        	enableKeyEvents : true,
            listeners: {
                keyup: function() {
                	calculateFinalPayment();
                }
            }
        },{
            fieldLabel: 'Repair Gross Paid',
            id : 'repairGrossId',
            name: 'repairGrossPaid',
            value: repairGrossPaid,
            blankText: 'Repair Gross Paid is required',
        	enableKeyEvents : true,
            listeners: {
                keyup: function() {
                	calculateFinalPayment();
                }
            }
        },{
            fieldLabel: 'Engineer Fee Gross Paid',
            id : 'engineerGrossId',
            name: 'engineerFeeGrossPaid',
            value: engineerFeeGrossPaid,
            blankText: 'Engineer Fee Gross Paid is required',
        	enableKeyEvents : true,
            listeners: {
                keyup: function() {
                	calculateFinalPayment();
                }
            }
        }, {
            fieldLabel: 'Total Loss Fee Gross Paid',
            id : 'totalLossFeeGrossId',
            name: 'totalLossFeeGrossPaid',
            value: totalLossFeeGrossPaid,
            blankText: 'Total Loss Fee Gross Paid is required',
        	enableKeyEvents : true,
            listeners: {
                keyup: function() {
                	calculateFinalPayment();
                }
            }
        },{
            fieldLabel: 'Storage Recovery Gross Paid',
            id : 'storageRecoveryGrossId',
            name: 'storageRecoveryGrossPaid',
            value: storageRecoveryGrossPaid,
            blankText: 'Storage Recovery Gross Paid is required',
        	enableKeyEvents : true,
            listeners: {
                keyup: function() {
                	calculateFinalPayment();
                }
            }
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
                    calculateFinalPayment();
                },
                afterrender : function(){
                    if(!(penaltyChargeApplied == true)){
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
                    calculateFinalPayment();
                },
                afterrender : function(){
                    if(!(penaltyChargeApplied == true)){
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
            },
        	enableKeyEvents : true,
            listeners: {
                keyup: function() {
                	calculateFinalPayment();
                }
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
            },
        	enableKeyEvents : true,
            listeners: {
                keyup: function() {
                	calculateFinalPayment();
                }
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
            },
        	enableKeyEvents : true,
            listeners: {
                keyup: function() {
                	calculateFinalPayment();
                }
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
            },
        	enableKeyEvents : true,
            listeners: {
                keyup: function() {
                	calculateFinalPayment();
                }
            }
        },{
            fieldLabel: 'Total To Pay',
            id : 'totalToPayId',
            name: 'totalToPay',
            value: totalToPay,
            readOnly : true,
            listeners: {
                keyup: function() {
                    Ext.getCmp('totalToPayId').validate();
                },
                afterrender : function(){
                    this.getEl().applyStyles({
                        'text-align':'right',
                        background: '#e4e4e4'
                    });  
                }
            }
        },{
            fieldLabel: 'Interim Payments Made',
            id : 'interimPayId',
            name: 'interimPayId',
            value: interimPaymentAmount.toFixed(2),
            readOnly : true,
            hidden : interimPaymentAmount<=0,
            listeners: {
                keyup: function() {
                    Ext.getCmp('totalToPayId').validate();
                },
                afterrender : function(){
                    this.getEl().applyStyles({
                        'text-align':'right',
                        background: '#e4e4e4'
                    });  
                }
            }
        },{
            fieldLabel: 'Final Payment',
            id : 'finalPayId',
            name: 'finalPayment',
            value: outstandingPayment,
            blankText: 'Final Payment is required'
        },{
            xtype : 'hidden',
            id : 'nonceId',
            name : 'nonce',
            value : nonce
        },{
            xtype : 'hidden',
            id : 'name',
            name : 'name',
            value : 'invoicePaymentLogged'
        }
        ]
    };
    
    var paymentDetailsForm = new Ext.FormPanel({
        id: 'paymentDetails-form',
        autoHeight: true,
        labelWidth: 210,
        frame:true,
        title:'<div class="status-info">Please confirm that the below payment details are correct. If you need to modify the payment details you can do so by clicking on the \'Edit\' button.  Please note that the figures take into account the agreed liability %.</div>',
        buttonAlign : 'center',
        items : [
        checkGroup,
        textGroup
        ],
        buttons:[{
            text:'Edit',
            handler:function(){
	            	if(this.getText() == 'Reset'){
	            		paymentDetailsForm.getForm().reset();
	            		this.setText('Edit');
	            		disablePayFields();
	            	}else{
	            		paymentDetailsForm.enable();
	            		this.setText('Reset');
	            		enablePayFields();
	            	}
	                
	            }
        	},{
            text:'Ok',
            handler:function(){
                if(paymentDetailsForm.getForm().isValid()){
                    win.hide();
                    paymentDetailsForm.getForm().submit({
                        method:'POST',
//                        url:contextPath +'/prv/p/updatePaymentDetails.action',
                        url:contextPath +'/prv/p/updatePaymentDetails.action?'+$('#logInvoicePayment').formSerialize(),
                        success : function(f, a) {

                            if ( a.result.success ){
                                pageRefresh(); 
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
    
    function disablePayFields(){
    	Ext.getCmp('penaltyChargesPaidId').disable();
    	Ext.getCmp('repairGrossId').disable();
    	Ext.getCmp('hireGrossId').disable();
    	Ext.getCmp('engineerGrossId').disable();
    	Ext.getCmp('totalLossFeeGrossId').disable();
    	Ext.getCmp('storageRecoveryGrossId').disable();
    	Ext.getCmp('hirePenaltyId').disable();
    	Ext.getCmp('repairPenaltyId').disable();
    	Ext.getCmp('paymentDetailsClaimHandInvAmtId').disable();
    	Ext.getCmp('paymentDetailsDeductionClaimHandFeeId').disable();
    	Ext.getCmp('paymentDetailsCHODiscountId').disable();
    	Ext.getCmp('paymentDetailsCHODiscountId').disable();
    	Ext.getCmp('paymentDetailsInsurerDiscountId').disable();
    	Ext.getCmp('totalToPayId').disable();
    	Ext.getCmp('finalPayId').disable();
    	Ext.getCmp('interimPayId').disable();
    	
    }

    function enablePayFields(){
    	Ext.getCmp('penaltyChargesPaidId').enable();
    	Ext.getCmp('repairGrossId').enable();
    	Ext.getCmp('hireGrossId').enable();
    	Ext.getCmp('engineerGrossId').enable();
    	Ext.getCmp('totalLossFeeGrossId').enable();
    	Ext.getCmp('storageRecoveryGrossId').enable();
    	Ext.getCmp('hirePenaltyId').enable();
    	Ext.getCmp('repairPenaltyId').enable();
    	Ext.getCmp('paymentDetailsClaimHandInvAmtId').enable();
    	Ext.getCmp('paymentDetailsDeductionClaimHandFeeId').enable();
    	Ext.getCmp('paymentDetailsCHODiscountId').enable();
    	Ext.getCmp('paymentDetailsCHODiscountId').enable();
    	Ext.getCmp('paymentDetailsInsurerDiscountId').enable();
    	Ext.getCmp('totalToPayId').enable();
    	Ext.getCmp('finalPayId').enable();
    	Ext.getCmp('interimPayId').enable();
    }
    
    function calculateFinalPayment(){
    	var finaPayment = 
    	Ext.getCmp('repairGrossId').getValue() +
    	Ext.getCmp('hireGrossId').getValue() +
    	Ext.getCmp('engineerGrossId').getValue() +
    	Ext.getCmp('totalLossFeeGrossId').getValue() +
    	Ext.getCmp('storageRecoveryGrossId').getValue() +
    	Ext.getCmp('hirePenaltyId').getValue() +
    	Ext.getCmp('repairPenaltyId').getValue() +
    	Ext.getCmp('paymentDetailsClaimHandInvAmtId').getValue() +
    	Ext.getCmp('paymentDetailsDeductionClaimHandFeeId').getValue() +
    	Ext.getCmp('paymentDetailsCHODiscountId').getValue() +
    	Ext.getCmp('paymentDetailsCHODiscountId').getValue() -
        Ext.getCmp('interimPayId').getValue() +
    	Ext.getCmp('paymentDetailsInsurerDiscountId').getValue();
    	
    	Ext.getCmp('finalPayId').setValue(finaPayment);
    }
    
    disablePayFields();
    
});

function confirmPaymentLogAction(){
	win.show(document.body);
}


 