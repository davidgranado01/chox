<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
var partialInterimPayment = <s:property value="outstandingInterimPayment" />;
var interimPaymentMade = <s:property value="interimPaymentMade" />;
var confPayRec;

Ext.onReady(function(){
	
	var inFields = {
	        xtype: 'fieldset',
	        title: '<s:if test="interimPaymentReceived > 0.0">Please Confirm Additional Amount Received</s:if><s:else>Please Confirm Amount Received</s:else>',
	        layout: 'form',
	        collapsed: false,   
	        collapsible: false,
            height: 150,
	        defaultType:'numberfield',
	        defaults: {              
	            labelStyle: 'text-align: right;',
	            allowBlank:false,
	            width : 75,
	            allowNegative : false,
	            decimalPrecision : 2,
	            minValue : 0.01,
                minText: 'Must be > 0.00',
	            style: {
	                'text-align':'left'
	            }
	        },
	        items: [
	        {
	            fieldLabel: 'Amount Received',
	            id : 'amountReceivedId',
	            name: 'interimPaymentReceived',
	            blankText: 'Enter Amount Received'
	        },{
	            xtype : 'hidden',
	            id : 'PaymentDetailsFormNameId',
	            name : 'name',
	            value : 'fullPaymentNotReceived'
	        }
	        ]
    };
	
	var paymentDetailsForm = new choxExtJsFormPanel({
	    id: 'paymentDetails-form',
	    autoHeight: true,
	    labelWidth: 210,
	    frame:true,
	    title:'<div class="status-info">Insurer has made a payment of £<s:property value="finalPaymentOrTotal" /> against an amount outstanding of £<s:property value="projectedFinalPayment" />.\n\
              </br><s:if test="interimPaymentMade > 0.0">Note that an interim payment of £<s:property value="interimPaymentMade" />\
                      has been made against this claim <s:if test="interimPaymentReceived == 0">(Not Yet Received)</s:if><s:elseif test="interimPaymentReceived > 0 && outstandingInterimPayment" >(Only £<s:property value="interimPaymentReceived"/> Received)</s:elseif><s:else>(Received)</s:else>.</s:if></div>',
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
	                    url:contextPath +'/prv/p/fullPaymentNotReceived.action',
	                    success : function(f, a) {
	                        if ( a.result.success ){
	                        	confPayRec.hide();
                                if (a.result.message && a.result.message.length > 0) {
                                    Ext.MessageBox.alert('Info', a.result.message,function(){   
                                            loadClaimDetail(<s:property value="id" />);
//                                            window.location = contextPath + "/prv/openClaimDetail.action?id="+<s:property value="id" />+"&nonce=<%= session.getAttribute("SessionNonce")%>";
                                            return false;
                                    });  
                                } else {
                                        loadClaimDetail(<s:property value="id" />);
//                                        window.location = contextPath + "/prv/openClaimDetail.action?id="+<s:property value="id" />+"&nonce=<%= session.getAttribute("SessionNonce")%>";
                                }
	                        }
	                    },
	                    failure : function(f, a) {
	                    	 	confPayRec.hide();
                                Ext.MessageBox.alert('Error', a.result.message, function(){  
                                            loadClaimDetail(<s:property value="id" />);
//                                            window.location = contextPath + "/prv/openClaimDetail.action?id="+<s:property value="id" />+"&nonce=<%= session.getAttribute("SessionNonce")%>";
                                            return false;
                                });
	                    }
	                });
	            }
//                else console.log("Not valid");
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
    	
    	$("#formUpdatePaymentReceivedName").val(action);
    	if (action=='invoicePaymentReceived') {
            if (partialInterimPayment != undefined &&  partialInterimPayment > 0){
                Ext.Msg.show({
                    title:'Please Confirm',
                    msg: 'Please note that there is an interim payment on this claim which has not yet been marked as received. Marking the claim as \u2018Full Payment Received\u2018 will also mark this interim payment as received.',
                    buttons: {yes: 'Ok', no: 'Cancel'},   // or Ext.Msg.OKCANCEL
                    fn: function(btn){if(btn=='yes'){Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");choxJqueryHttpSubmit($("form#formUpdatePaymentReceived"));}else{return false;}}
                });
            }else{
                Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
//            	$("form#formUpdatePaymentReceived").submit();
                choxJqueryHttpSubmit($("form#formUpdatePaymentReceived"));
            }
        } else if (action == 'fullPaymentAmountNotReceived') {
        	confirmNotFullPayRec();
        } else {
                Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
//        	$("form#formUpdatePaymentReceived").submit();
                choxJqueryHttpSubmit($("form#formUpdatePaymentReceived"));
        }
    }
</script> 

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formUpdatePaymentReceived" name="formUpdatePaymentReceived">
        <fieldset class="x-fieldset">
            <legend>Update Payment Logged</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="formUpdatePaymentReceivedName" name="name"/>
            <div class="status-control-set">
                <s:if test="paymentLoggedOverDays && showPayNotReceivedButton && atInvoicePaymentLogged">
                    <div class="status-info">
                       Please click on the 'Full Payment Received' button when full payment for the invoice has been received from the Insurer.<br/><br/>
					   Please click on the ‘Payment Received But Not Full Amount’ button if the Insurer has made a payment but there is a balance outstanding 
					   on the invoice, this will return the claim to the Insurer for review and the amount received recorded on the invoice.<br/><br/>
					   If the payment has not been received then clicking on the 'Payment Not Received' button will return the claim to the Insurer for review.  
                    </div>
                </s:if>
                <s:elseif test="paymentLoggedOverDays && showPayNotReceivedButton && !atInvoicePaymentLogged">
                    <div class="status-info">
                       Please click on the 'Full Payment Received' button when full payment for the invoice has been received from the Insurer.<br/><br/>
					   If the payment has not been received then clicking on the 'Payment Not Received' button will return the claim to the Insurer for review.  
                    </div>
                </s:elseif>
                <s:elseif test="atInvoicePaymentLogged">
                    <div class="status-info">
                       Please click on the 'Full Payment Received' button when full payment for the invoice has been received from the Insurer.<br/><br/>
					   Please click on the ‘Payment Received But Not Full Amount’ button if the Insurer has made a payment but there is a balance outstanding 
					   on the invoice, this will return the claim to the Insurer for review and the amount received recorded on the invoice.
                    </div>
                </s:elseif>
                <s:else>
                    <div class="status-info">
                       Please click on the 'Full Payment Received' button when full payment for the invoice has been received from the Insurer.<br/><br/>
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
                            <s:if test='status.equals("InvoicePaymentLogged")'>
                                <input type="button" id="FullPaymentReceivedButtonId" value="Full Payment Received" onclick="doUpdatePaymentReceived('invoicePaymentReceived');" />
                            </s:if>
                            <s:else>
                                <input type="button" id="FullPaymentReceivedButtonId" value="Full Payment Received" onclick="doUpdatePaymentReceived('moveToInvoicePaymentLogged');" />
                            </s:else>
                            <s:if test="atInvoicePaymentLogged">
                                <input type="button" id="UPRPaymentReceivedButtonId" value="Payment Received But Not Full Amount" onclick="doUpdatePaymentReceived('fullPaymentAmountNotReceived');" />
                            </s:if>
                            <s:if test="paymentLoggedOverDays && showPayNotReceivedButton">
                                <input type="button" id="UPRPaymentNOTReceivedButtonId" value="Payment Not Received" onclick="doUpdatePaymentReceived('paymentNotReceived');" />
                            </s:if>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="action-error-msg" id="updatePaymentReceivedMessageBox"></div>
        </fieldset>
        <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    </form>
</div>