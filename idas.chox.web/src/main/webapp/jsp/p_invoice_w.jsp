<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
           
    $(function(){

        ui.dateField('dateInvoiced', '<s:date format="dd/MM/yyyy" name="dateInvoiced" />' ,'dateInvoicedPH');

        var form = $("#formUpdateInvoiceForm");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});
        
        form.validate(
        {
            errorLabelContainer: "#INVmessageBox",
            rules: {
                claimInvoiceNo :{required:true},
                hireRateChargedPerDay :{required:true, number:true, min:0},
                hireNet :{required:true, number:true, min:0},
                hireVat :{required:true, number:true, min:0},
                hireGross :{required:true, number:true, min:0},
                repairNet :{required:true, number:true, min:0},
                repairVat:{required:true, number:true, min:0},
                repairGross :{required:true, number:true, min:0},
                engineerFeeNet :{required:true, number:true, min:0},
                engineerFeeVat :{required:true, number:true, min:0},
                engineerFeeGross :{required:true, number:true, min:0},
                totalLossFeeNet :{number:true, min:0},
                totalLossFeeVat :{number:true, min:0},
                totalLossFeeGross :{number:true, min:0},
                storageRecoveryNet :{required:true, number:true, min:0},
                storageRecoveryVat :{required:true, number:true, min:0},
                storageRecoveryGross :{required:true, number:true, min:0},
                totalNet :{required:true, number:true, min:0},
                totalVat :{required:true, number:true, min:0},
                totalGross :{required:true, number:true, min:0},
                claimsHandlingInvoiceAmount :{required:true, number:true, min:0},
                deductionForClaimsHandlingFee :{required:true, number:true, max:0},
                discount :{required:true, number:true, max:0},
                fullTotalToPay :{required:true, number:true, min:0},
                excessAmountCollected :{required:true, number:true, min:0},
                vatAmountCollected :{required:true, number:true, min:0},
                dateInvoiced :{required:true, date:true}      
            },
            messages: {
                claimInvoiceNo :{required:"You must supply a value for 'Supplier Claim Invoice Number'"},
                hireRateChargedPerDay :{required:"You must supply a value for 'Hire Rate Charged Per Day'", number:"You must supply a numeric value for 'Hire Rate Charged Per Day'", min:"Hire Rate Charged Per Day must be greater or equal to zero"},
                hireNet :{required:"You must supply a value for 'Hire Net'", number:"You must supply a numeric value for 'Hire Net'", min:"Hire Net must be greater or equal to zero"},
                hireVat :{required:"You must supply a value for 'Hire Vat'", number:"You must supply a numeric value for 'Hire Vat'", min:"Hire Vat must be greater or equal to zero"},
                hireGross :{required:"You must supply a value for 'Hire Gross'", number:"You must supply a numeric value for 'Hire Gross'", min:"Hire Gross must be greater or equal to zero"},
                repairNet :{required:"You must supply a value for 'Repair Net'", number:"You must supply a numeric value for 'Repair Net'", min:"Repair Net must be greater or equal to zero"},
                repairVat:{required:"You must supply a value for 'Repair Vat'", number:"You must supply a numeric value for 'Repair Vat'", min:"Repair Vat must be greater or equal to zero"},
                repairGross :{required:"You must supply a value for 'Repair Gross'", number:"You must supply a numeric value for 'Repair Gross'", min:"Repair Gross must be greater or equal to zero"},
                engineerFeeNet :{required:"You must supply a value for 'Engineer Fee Net'", number:"You must supply a numeric value for 'Engineer Fee Net'", min:"Engineer Fee Net must be greater or equal to zero"},
                engineerFeeVat :{required:"You must supply a value for 'Engineer Fee Vat'", number:"You must supply a numeric value for 'Engineer Fee Vat'", min:"Engineer Fee Vat must be greater or equal to zero"},
                engineerFeeGross :{required:"You must supply a value for 'Engineer Fee Gross'", number:"You must supply a numeric value for 'Engineer Fee Gross'", min:"Engineer Fee Gross must be greater or equal to zero"},
                totalLossFeeNet :{number:"You must supply a numeric value for 'Total Loss Fee Net'", min:"Total Loss Fee Net must be greater or equal to zero"},
                totalLossFeeVat :{number:"You must supply a numeric value for 'Total Loss Fee Vat'", min:"Total Loss Fee Vat must be greater or equal to zero"},
                totalLossFeeGross :{number:"You must supply a numeric value for 'Total Loss Fee Gross'", min:"Total Loss Fee Gross must be greater or equal to zero"},
                storageRecoveryNet :{required:"You must supply a value for 'Storage Recovery Net'", number:"You must supply a numeric value for 'Storage Recovery Net'", min:"Storage Recovery Net must be greater or equal to zero"},
                storageRecoveryVat :{required:"You must supply a value for 'Storage Recovery Vat'", number:"You must supply a numeric value for 'Storage Recovery Vat'", min:"Storage Recovery Vat must be greater or equal to zero"},
                storageRecoveryGross :{required:"You must supply a value for 'Storage Recovery Gross'", number:"You must supply a numeric value for 'Storage Recovery Gross'", min:"Storage Recovery Gross must be greater or equal to zero"},
                totalNet :{required:"You must supply a value for 'Total Net'", number:"You must supply a numeric value for 'Total Net'", min:"Total Net must be greater or equal to zero"},
                totalVat :{required:"You must supply a value for 'Total Vat'", number:"You must supply a numeric value for 'Total Vat'", min:"Total Vat must be greater or equal to zero"},
                totalGross :{required:"You must supply a value for 'Total Gross'", number:"You must supply a numeric value for 'Total Gross'", min:"Total Gross must be greater or equal to zero"},
                claimsHandlingInvoiceAmount :{required:"You must supply a numeric value for 'Claims Handling Invoice Amount'", number:"You must supply a value for 'Claims Handling Invoice Amount'", min:"Claims Handling Invoice Amount must be greater or equal to zero"},
                deductionForClaimsHandlingFee :{required:"You must supply a value for 'Deduction For Claims Handling Fee'", number:"You must supply a numeric value for 'Deduction For Claims Handling Fee'", max:"Deduction For Claims Handling Fee must be less than or equal to zero"},
                discount :{required:"You must supply a value for 'Discount'", number:"You must supply a numeric value for 'Discount'", max:"Discount must be less or equal to zero"},
                fullTotalToPay :{required:"You must supply a value for 'Full Total Requested'", number:"You must supply a numeric value for 'Full Total Requested'", min:"Full Total Requested must be greater or equal to zero"},
                excessAmountCollected :{required:"You must supply a value for 'Excess Amount Collected'", number:"You must supply a numeric value for Excess Amount Collected", min:"Excess Amount Collected must be greater or equal to zero"},
                vatAmountCollected :{required:"You must supply a value for 'Vat Amount Collected'", number:"You must supply a numeric value for Vat Amount Collected", min:"Vat Amount Collected must be greater or equal to zero"},
                dateInvoiced :{ required:"You must supply a value for 'Date Invoiced'", date:"Invalid date format for Date Invoiced"}              
            } 
        }); 

        ui.ajaxForm(form,null,'html');
        
    });             
    
</script>

<form id="formUpdateInvoiceForm" name="formUpdateInvoiceForm" action="<%=request.getContextPath()%>/prv/p/updateInvoice.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <fieldset class="x-fieldset partial">
        <legend>Invoice Detail</legend>

        <div class="form-container" id="invoiceDetailWId">

            <div class="chox-form-item">
                <label class="chox-form-std-label">Supplier Claims Handling #</label>
                <input type="text" class="chox-ttxt"  name="handlingInvoiceNo" value="<s:property value="handlingInvoiceNo" />"/>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Supplier Claim Invoice #<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt"  name="claimInvoiceNo" value="<s:property value="claimInvoiceNo" />"/>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Hire Rate Charged Per Day<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="hireRateChargedPerDay" value="<s:property value="hireRateChargedPerDay" />"/>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Hire Net<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="hireNet" value="<s:property value="hireNet" />"/>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Hire VAT<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum" name="hireVat" value="<s:property value="hireVat"  />"/>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Hire Gross<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="hireGross" value="<s:property value="hireGross" />"/>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Repair Net<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="repairNet" value="<s:property value="repairNet" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Repair VAT<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="repairVat" value="<s:property value="repairVat" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Repair Gross<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="repairGross" value="<s:property value="repairGross" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Engineer Fee Net<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="engineerFeeNet" value="<s:property value="engineerFeeNet" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Engineer Fee VAT<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="engineerFeeVat" value="<s:property value="engineerFeeVat" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Engineer Fee Gross<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="engineerFeeGross" value="<s:property value="engineerFeeGross" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Total Loss Fee Net</label>
                <input type="text" class="chox-ttnum"  name="totalLossFeeNet" value="<s:property value="totalLossFeeNet" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Total Loss Fee VAT</label>
                <input type="text" class="chox-ttnum"  name="totalLossFeeVat" value="<s:property value="totalLossFeeVat" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Total Loss Fee Gross</label>
                <input type="text" class="chox-ttnum"  name="totalLossFeeGross" value="<s:property value="totalLossFeeGross" />"/></div>

                <div class="chox-form-item">
                <label class="chox-form-std-label">Storage Recovery Net<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="storageRecoveryNet" value="<s:property value="storageRecoveryNet" />"/>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Storage Recovery VAT<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="storageRecoveryVat" value="<s:property value="storageRecoveryVat" />"/>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Storage Recovery Gross<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="storageRecoveryGross" value="<s:property value="storageRecoveryGross" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Total Net<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="totalNet" value="<s:property value="totalNet" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Total Vat<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="totalVat" value="<s:property value="totalVat" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Total Gross<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="totalGross" value="<s:property value="totalGross" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Claims Handling Invoice Amount<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="claimsHandlingInvoiceAmount" value="<s:property value="claimsHandlingInvoiceAmount" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Deduction For Claims Handling Fee<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="deductionForClaimsHandlingFee" value="<s:property value="deductionForClaimsHandlingFee" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Discount<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="discount" value="<s:property value="discount" />"/></div>


            <div class="chox-form-item">
                <label class="chox-form-std-label">Hire Penalty Percentage</label>
                                        <s:select
                                            name="hirePenaltyPercentage"
                                            id="hirePenaltyPercentageId"
                                            list="#{'7.5%':'7.5%', '15.0%':'15.0%', 'Commercial':'Commercial'}"
                                            headerKey=""
                                            disabled="true"
                                            headerValue="Not Specified"
                                            emptyOption="false">
                                        </s:select>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Hire Penalty Charge</label>
                <input type="text" class="chox-ttnum"  name="hirePenaltyCharge" disabled="true" value="<s:property value="hirePenaltyCharge" />"/></div>


            <div class="chox-form-item">
                <label class="chox-form-std-label">Repair Penalty Percentage</label>
                                        <s:select
                                            name="repairPenaltyPercentage"
                                            id="repairPenaltyPercentageId"
                                            list="#{'2.5%':'2.5%', '5.0%':'5.0%'}"
                                            headerKey=""
                                            disabled="true"
                                            headerValue="Not Specified"
                                            emptyOption="false">
                                        </s:select>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Repair Penalty Charge</label>
                <input type="text" class="chox-ttnum"  name="repairPenaltyCharge" disabled="true" value="<s:property value="repairPenaltyCharge" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Total Penalty Charge</label>
                <input type="text" class="chox-ttnum"  name="totalPenaltyCharge" disabled="true" value="<s:property value="totalPenaltyCharge" />"/></div>

                <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Full Total Requested<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="fullTotalToPay" value="<s:property value="fullTotalToPay" />"/>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label-big">
                    Total To Pay <span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  disabled="true" name="totalToPay" value="<s:property value="totalToPay" />"/>
            </div>

        <s:if test="interimPaymentReceived">
            <div class="chox-form-item">
                <label class="chox-form-std-label">Interim Payment</label>
                <input type="text" class="chox-ttnum"  disabled="true" name="interimPayment" value="<s:property value="interimPayment" />"/><label class="chox-ttnum-red">(Payment Received)</label>
                <label class="std-label-ro-small">Note that the interim payment is NOT deducted from the 'Total To Pay'</label><br/>
            </div>
        </s:if>
        <s:elseif test="!interimPaymentReceived && interimPayment">
            <div class="chox-form-item">
                <label class="chox-form-std-label">Interim Payment</label>
                <input type="text" class="chox-ttnum"  disabled="true" name="interimPayment" value="<s:property value="interimPayment" />"/><label class="chox-ttnum-red">(Not Yet Received)</label>
                <label class="std-label-ro-small">Note that the interim payment is NOT deducted from the 'Total To Pay'</label><br/>
            </div>
        </s:elseif>
        <s:else>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Interim Payment</label>
                <input type="text" class="chox-ttnum"  disabled="true" name="totalinterimPaymentToPay" value="N/A"/>
            </div>
        </s:else>
            

            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Excess Collected From Policyholder<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="excessAmountCollected" value="<s:property value="excessAmountCollected" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    VAT Collected From Policyholder<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="vatAmountCollected" value="<s:property value="vatAmountCollected" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label">Date Invoiced<span class="mandatory">*</span></label>
                <label class="std-data-ro"><span id="dateInvoicedPH"></span></label>
            </div>

            <!--
            <div class="chox-form-item">
                <label class="chox-form-std-label">&nbsp;</label>
            <s:if test="invoicedDays > 0"><label class="std-data-ro"> (<s:property value="invoicedDays" /> days)</label></s:if>
        </div>
        !-->

            <div class="chox-form-item">
                <label class="chox-form-std-label">Invoice Uploaded Date</label>
                <label class="std-data-ro"><s:date name="createdDate" format="dd MMM yyyy" /></label>
                <s:if test="invoicedDays > 0">
                    <label class="std-data-ro"> (<s:property value="invoicedDays" /> days)</label>
                </s:if>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label1">Time Invoice Has Been<br/>With CHO For Review</label>
                <label class="std-data-ro"><s:property value="daysWithCHOForReview" /></label>
            </div>
            <div class="chox-form-item">&nbsp;</div>
            <div class="chox-form-item">
                <label class="chox-form-std-label1">Time Invoice Has Been<br/>With Insurer For Review</label>
                <label class="std-data-ro"><s:property value="daysWithInsurerForReview" /></label>
            </div>
            <div class="chox-form-item">&nbsp;</div>
            <div class="chox-form-item">
                <label class="chox-form-std-label1">Time Claim Has Been<br/>Awaiting Liability Resolution</label>
                <label class="std-data-ro"><s:property value="daysAwaitingLiabilityResolution" /></label>
            </div>

           <!--     <div class="chox-form-button">
                <input type="submit" value="Save Changes" id="submitInvoice"/>
            </div>  !-->
            <div id="INVmessageBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    <!--s:token/-->
</form>