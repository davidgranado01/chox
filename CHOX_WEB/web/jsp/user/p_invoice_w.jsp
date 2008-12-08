<%@ taglib uri="/struts-tags" prefix="s" %>





<script language="JavaScript">
    
        
        
    $(document).ready(function(){        
        
        
        $("#formUpdateInvoiceForm").validate(
        {
            errorLabelContainer: "#INVmessageBox",                
            rules: {
                    
                handlingInvoiceNo :{required:true},
                claimInvoiceNo :{required:true},
                hireNet :{required:true, number:true, min:0},
                hireVat :{required:true, number:true, min:0},
                hireGross :{required:true, number:true, min:0},
                repairNet :{required:true, number:true, min:0},
                repairVat:{required:true, number:true, min:0},
                repairGross :{required:true, number:true, min:0},
                engineerFeeNet :{required:true, number:true, min:0},
                engineerFeeVat :{required:true, number:true, min:0},
                engineerFeeGross :{required:true, number:true, min:0},
                storageRecoveryNet :{required:true, number:true, min:0},
                storageRecoveryVat :{required:true, number:true, min:0},
                storageRecoveryGross :{required:true, number:true, min:0},
                totalNet :{required:true, number:true, min:0},
                totalVat :{required:true, number:true, min:0},
                totalGross :{required:true, number:true, min:0},
                claimsHandlingInvoiceAmount :{required:true, number:true, min:0},
                deductionForClaimsHandlingFee :{required:true, number:true, max:0},
                discount :{required:true, number:true, min:0},
                totalToPay :{required:true, number:true, min:0},
                
                excessAmountCollected :{required:true, number:true, min:0},
                vatAmountCollected :{required:true, number:true, min:0},
                dateInvoiced :{required:true, number:false, min:0},
                hireRateChargedPerDay :{required:true, number:true, min:0}
                    
            },
                
            messages: {
                    
                handlingInvoiceNo :{required:"You must supply a value for 'Supplier Claims Handling Number'"},
                claimInvoiceNo :{required:"You must supply a value for 'Supplier Claim Invoice Number'"},
                hireNet :{required:"You must supply a value for 'Hire Net'", number:"You must supply a numeric value for 'Hire Net'", min:"Hire Net must be greater or equal to zero"},
                hireVat :{required:"You must supply a value for 'Hire Vat'", number:"You must supply a numeric value for 'Hire Vat'", min:"Hire Vat must be greater or equal to zero"},
                hireGross :{required:"You must supply a value for 'Hire Gross'", number:"You must supply a numeric value for 'Hire Gross'", min:"Hire Gross must be greater or equal to zero"},
                repairNet :{required:"You must supply a value for 'Repair Net'", number:"You must supply a numeric value for 'Repair Net'", min:"Repair Net must be greater or equal to zero"},
                repairVat:{required:"You must supply a value for 'Repair Vat'", number:"You must supply a numeric value for 'Repair Vat'", min:"Repair Vat must be greater or equal to zero"},
                repairGross :{required:"You must supply a value for 'Repair Gross'", number:"You must supply a numeric value for 'Repair Gross'", min:"Repair Gross must be greater or equal to zero"},
                engineerFeeNet :{required:"You must supply a value for 'Engineer Fee Net'", number:"You must supply a numeric value for 'Engineer Fee Net'", min:"Engineer Fee Net must be greater or equal to zero"},
                engineerFeeVat :{required:"You must supply a value for 'Engineer Fee Vat'", number:"You must supply a numeric value for 'Engineer Fee Vat'", min:"Engineer Fee Vat must be greater or equal to zero"},
                engineerFeeGross :{required:"You must supply a value for 'Engineer Fee Gross'", number:"You must supply a numeric value for 'Engineer Fee Gross'", min:"Engineer Fee Gross must be greater or equal to zero"},
                storageRecoveryNet :{required:"You must supply a value for 'Storage Recovery Net'", number:"You must supply a numeric value for 'Storage Recovery Net'", min:"Storage Recovery Net must be greater or equal to zero"},
                storageRecoveryVat :{required:"You must supply a value for 'Storage Recovery Vat'", number:"You must supply a numeric value for 'Storage Recovery Vat'", min:"Storage Recovery Vat must be greater or equal to zero"},
                storageRecoveryGross :{required:"You must supply a value for 'Storage Recovery Gross'", number:"You must supply a numeric value for 'Storage Recovery Gross'", min:"Storage Recovery Gross must be greater or equal to zero"},
                totalNet :{required:"You must supply a value for 'Total Net'", number:"You must supply a numeric value for 'Total Net'", min:"Total Net must be greater or equal to zero"},
                totalVat :{required:"You must supply a value for 'Total Vat'", number:"You must supply a numeric value for 'Total Vat'", min:"Total Vat must be greater or equal to zero"},
                totalGross :{required:"You must supply a value for 'Total Gross'", number:"You must supply a numeric value for 'Total Gross'", min:"Total Gross must be greater or equal to zero"},
                claimsHandlingInvoiceAmount :{required:"You must supply a numeric value for 'Claims Handling Invoice Amount'", number:"You must supply a value for 'Claims Handling Invoice Amount'", min:"Claims Handling Invoice Amount must be greater or equal to zero"},
                deductionForClaimsHandlingFee :{required:"You must supply a value for 'Deduction For Claims Handling Fee'", number:"You must supply a numeric value for 'Deduction For Claims Handling Fee'", max:"Deduction For Claims Handling Fee must be less than or equal to zero"},
                discount :{required:"You must supply a value for 'Discount'", number:"You must supply a numeric value for 'Discount'", min:"Discount must be greater or equal to zero"},
                totalToPay :{required:"You must supply a value for 'Total To Pay'", number:"You must supply a numeric value for 'Total To Pay'", min:"Total To Pay must be greater or equal to zero"}                    
                    
            },
                
            submitHandler: function(form) {
                $(form).ajaxSubmit(globalEntityFormOptions);
            }                

                
        }); 
            
    });             
        



    
    
</script>




<form id="formUpdateInvoiceForm" name="formUpdateInvoiceForm" action="user/updateInvoice.action" class="XXentity-form">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
    <fieldset class="x-fieldset">
        <legend>Invoice Detail</legend>
        
        
        
        <div style="display:none" class="form-container">
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Supplier Claims Handling #</label>
            <input type="text" class="chox-ttxt"  name="handlingInvoiceNo" value='<s:property value="handlingInvoiceNo" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Supplier Claim Invoice #</label>
            <input type="text" class="chox-ttxt"  name="claimInvoiceNo" value='<s:property value="claimInvoiceNo" />'/></div>                                             
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Hire Net</label>
            <input type="text" class="chox-tnum" name="hireNet" value='<s:property value="hireNet" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Hire Vat</label>
            <input type="text" class="required" minlength="2" name="hireVat" value='<s:property value="hireVat"  />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Hire Gross</label>
            <input type="text" class="chox-tnum"  name="hireGross" value='<s:property value="hireGross" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Repair Net</label>
            <input type="text" class="chox-tnum"  name="repairNet" value='<s:property value="repairNet" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Repair Vat</label>
            <input type="text" class="chox-tnum"  name="repairVat" value='<s:property value="repairVat" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Repair Gross</label>
            <input type="text" class="chox-tnum"  name="repairGross" value='<s:property value="repairGross" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Fee Net</label>
            <input type="text" class="chox-tnum"  name="engineerFeeNet" value='<s:property value="engineerFeeNet" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engieer Fee Vat</label>
            <input type="text" class="chox-tnum"  name="engineerFeeVat" value='<s:property value="engineerFeeVat" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Fee Gross</label>
            <input type="text" class="chox-tnum"  name="engineerFeeGross" value='<s:property value="engineerFeeGross" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Storage Recovery Net
                </label>
            <input type="text" class="chox-tnum"  name="storageRecoveryNet" value='<s:property value="storageRecoveryNet" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Storage Recovery Vat</label>
            <input type="text" class="chox-tnum"  name="storageRecoveryVat" value='<s:property value="storageRecoveryVat" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Storage Recovery Gross</label>
            <input type="text" class="chox-tnum"  name="storageRecoveryGross" value='<s:property value="storageRecoveryGross" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Total Net</label>
            <input type="text" class="chox-tnum"  name="totalNet" value='<s:property value="totalNet" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Total Vat</label>
            <input type="text" class="chox-tnum"  name="totalVat" value='<s:property value="totalVat" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Total Gross</label>
            <input type="text" class="chox-tnum"  name="totalGross" value='<s:property value="totalGross" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Claims Handling Invoice Amount</label>
            <input type="text" class="chox-tnum"  name="claimsHandlingInvoiceAmount" value='<s:property value="claimsHandlingInvoiceAmount" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Deduction For Claims Handling Fee</label>
            <input type="text" class="chox-tnum"  name="deductionForClaimsHandlingFee" value='<s:property value="deductionForClaimsHandlingFee" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Discount</label>
            <input type="text" class="chox-tnum"  name="discount" value='<s:property value="discount" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Total To Pay</label>
                <input type="text" class="chox-tnum"  name="totalToPay" value='<s:property value="totalToPay" />'/>
            </div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Excess Amount Collected From Policyholder</label>
            <input type="text" class="chox-tnum"  name="excessAmountCollected" value='<s:property value="excessAmountCollected" />'/></div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                VAT Amount Collected From Policyholder</label>
            <input type="text" class="chox-tnum"  name="vatAmountCollected" value='<s:property value="vatAmountCollected" />'/></div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Date Invoiced</label>
            <input type="text" class="chox-tnum"  name="dateInvoiced" value='<s:property value="dateInvoiced" />'/></div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Hire Rate Charge Per Day</label>
            <input type="text" class="chox-tnum"  name="hireRateChargedPerDay" value='<s:property value="hireRateChargedPerDay" />'/></div>
            
            
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" id="submitInvoice"/>
            </div>
            
            <div id="INVmessageBox" style="text-align:center"></div>  
            <div class="chox-form-submit-result">&nbsp;</div>                                              
        </div>
    </fieldset>
</form>