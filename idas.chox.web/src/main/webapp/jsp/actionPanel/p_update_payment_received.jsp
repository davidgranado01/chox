<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
var confPayRec;
Ext.onReady(function(){
	
	var amountReceived = <s:property value="partialInterimPayment" />;
	
	var inFields = {
	        xtype: 'fieldset',
	        title: 'Please Cofirm Amount Received',
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
	            fieldLabel: 'Amount Received',
	            id : 'hireGrossId',
	            name: 'hireGrossPaid',
	            value: amountReceived.toFixed(2),
	            blankText: 'Cofirm Amount Received'
	        }]
    };
	
	var paymentDetailsForm = new Ext.FormPanel({
	    id: 'paymentDetails-form',
	    autoHeight: true,
	    labelWidth: 210,
	    frame:true,
	    title:'<div class="status-info">Insurer has made a payment of £<s:property value="partialInterimPayment" /> against an amount outstanding of £<s:property value="totalToPay" />.</div>',
	    buttonAlign : 'center',
	    items : [inFields
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
	                        	confPayRec.hide();
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
	            confPayRec.hide();
	        }
	    }]
	    
		});
	
	
		
	confPayRec = new Ext.Window({
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

function confirmNotFullPayRec(){
	confPayRec.show(document.body);
}

    function doUpdatePaymentReceived(action) {
    	var interimParPayment = 0;
    	<s:if test="partialInterimPayment != null && partialInterimPayment >= 0 ">
    		interimParPayment = <s:property value="partialInterimPayment" />;
    	</s:if>
    	$("#formUpdatePaymentReceivedName").val(action);
    	if (action=='fullInvoicePaymentReceived') {
            if (interimParPayment != undefined &&  interimParPayment > 0){
            	Ext.MessageBox.confirm('Confirm', 'Please note that there is an interim payment on this claim which has not yet been marked as received, marking the claim as ‘Full Payment Received’ will also mark the interim payment as received.' 
            			,function(btn){if(btn=='yes'){$("form#formUpdatePaymentReceived").submit();}else{return false;}});
            }else{
            	$("form#formUpdatePaymentReceived").submit();
            }
        } else if (action == 'invoicePaymentReceived') {
        	confirmNotFullPayRec();
        } else {
        	$("form#formUpdatePaymentReceived").submit();
        }
        
    }
</script> 

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formUpdatePaymentReceived" name="formUpdatePaymentReceived">
        <fieldset class="x-fieldset">
            <legend>Update Payment Logged</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="formUpdatePaymentReceivedName" name="name"/>
             <s:hidden id="pLogged" name="paymentLogged" />
            <div class="status-control-set">
                <s:if test="paymentLoggedOverDays && showPayNotReceivedButton">
                    <div class="status-info">
                       Please click on the 'Full Payment Received' button when full payment for the invoice has been received from the Insurer.<br/><br/>
					   Please click on the ‘Payment Received But Not Full Amount’ button if the Insurer has made a payment but there is a balance outstanding 
					   on the invoice, this will return the claim to the Insurer for review and the amount received recorded on the invoice.<br/><br/>
					   If the payment has not been received then clicking on the 'Payment Not Received' button will return the claim to the Insurer for review.  
					   Note that this button will only be visible after 9 days.
                    </div>
                </s:if>
                <s:else>
                    <div class="status-info">
                       Please click on the 'Full Payment Received' button when full payment for the invoice has been received from the Insurer.<br/><br/>
					   Please click on the ‘Payment Received But Not Full Amount’ button if the Insurer has made a payment but there is a balance outstanding 
					   on the invoice, this will return the claim to the Insurer for review and the amount received recorded on the invoice.
                    </div>
                </s:else>

                <table class="status-table">
                    <tr>
                        <td colspan="3">
                            <div class="no-format"><span>Please specify how you wish to proceed &nbsp;&nbsp;</span></div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3">
                        	<input type="button" id="FullPaymentReceivedButtonId" value="Full Payment Received" onclick="doUpdatePaymentReceived('fullInvoicePaymentReceived');" />
                            <input type="button" id="UPRPaymentReceivedButtonId" value="Payment Received But Not Full Amount" onclick="doUpdatePaymentReceived('invoicePaymentReceived');" />
                            <s:if test="paymentLoggedOverDays && showPayNotReceivedButton">
                                <input type="button" id="UPRPaymentNOTReceivedButtonId" value="Payment Not Received" onclick="doUpdatePaymentReceived('revertClaim');" />
                            </s:if>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="action-error-msg" id="updatePaymentReceivedMessageBox"></div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    </form>
</div>