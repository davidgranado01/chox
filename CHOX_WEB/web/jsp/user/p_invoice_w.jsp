<%@ taglib uri="/struts-tags" prefix="s" %>





<script language="JavaScript">
    
        
        
    $(document).ready(function(){        
        
        
        var dateInvoicedDatePicker = new Ext.form.DateField({
            name: 'dateInvoiced',
            width: 185,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true,
            validationEvent : false,
            value: '<s:date format="dd/MM/yyyy" name="dateInvoiced" />',
            renderTo:'dateInvoicedPH'
        });          
        
        $("#formUpdateInvoiceForm").validate(
        {
            errorLabelContainer: "#INVmessageBox",                
            rules: {
                    
                handlingInvoiceNo :{required:true},
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
                dateInvoiced :{required:true, date:true}
                    
            },

            messages: {
                    
                handlingInvoiceNo :{required:"You must supply a value for 'Supplier Claims Handling Number'"},
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
                storageRecoveryNet :{required:"You must supply a value for 'Storage Recovery Net'", number:"You must supply a numeric value for 'Storage Recovery Net'", min:"Storage Recovery Net must be greater or equal to zero"},
                storageRecoveryVat :{required:"You must supply a value for 'Storage Recovery Vat'", number:"You must supply a numeric value for 'Storage Recovery Vat'", min:"Storage Recovery Vat must be greater or equal to zero"},
                storageRecoveryGross :{required:"You must supply a value for 'Storage Recovery Gross'", number:"You must supply a numeric value for 'Storage Recovery Gross'", min:"Storage Recovery Gross must be greater or equal to zero"},
                totalNet :{required:"You must supply a value for 'Total Net'", number:"You must supply a numeric value for 'Total Net'", min:"Total Net must be greater or equal to zero"},
                totalVat :{required:"You must supply a value for 'Total Vat'", number:"You must supply a numeric value for 'Total Vat'", min:"Total Vat must be greater or equal to zero"},
                totalGross :{required:"You must supply a value for 'Total Gross'", number:"You must supply a numeric value for 'Total Gross'", min:"Total Gross must be greater or equal to zero"},
                claimsHandlingInvoiceAmount :{required:"You must supply a numeric value for 'Claims Handling Invoice Amount'", number:"You must supply a value for 'Claims Handling Invoice Amount'", min:"Claims Handling Invoice Amount must be greater or equal to zero"},
                deductionForClaimsHandlingFee :{required:"You must supply a value for 'Deduction For Claims Handling Fee'", number:"You must supply a numeric value for 'Deduction For Claims Handling Fee'", max:"Deduction For Claims Handling Fee must be less than or equal to zero"},
                discount :{required:"You must supply a value for 'Discount'", number:"You must supply a numeric value for 'Discount'", min:"Discount must be greater or equal to zero"},
                totalToPay :{required:"You must supply a value for 'Total To Pay'", number:"You must supply a numeric value for 'Total To Pay'", min:"Total To Pay must be greater or equal to zero"},
                excessAmountCollected :{required:"You must supply a value for 'Excess Amount Collected'", number:"You must supply a numeric value for Excess Amount Collected", min:"Excess Amount Collected must be greater or equal to zero"},
                vatAmountCollected :{required:"You must supply a value for 'Vat Amount Collected'", number:"You must supply a numeric value for Vat Amount Collected", min:"Vat Amount Collected must be greater or equal to zero"},
                dateInvoiced :{ required:"You must supply a value for 'Date Invoiced'", date:"Invalid date format for Date Invoiced"}             
                    
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
                Supplier Claims Handling #<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttxt"  name="handlingInvoiceNo" value='<s:property value="handlingInvoiceNo" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Supplier Claim Invoice #<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttxt"  name="claimInvoiceNo" value='<s:property value="claimInvoiceNo" />'/></div>                                             
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">Hire Rate Charged Per Day<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum" name="hireRateChargedPerDay" value='<s:property value="hireRateChargedPerDay" />'/></div>
            <br/>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Hire Net<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum" name="hireNet" value='<s:property value="hireNet" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Hire Vat<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum" minlength="2" name="hireVat" value='<s:property value="hireVat"  />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Hire Gross<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="hireGross" value='<s:property value="hireGross" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Repair Net<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="repairNet" value='<s:property value="repairNet" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Repair Vat<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="repairVat" value='<s:property value="repairVat" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Repair Gross<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="repairGross" value='<s:property value="repairGross" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Fee Net<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="engineerFeeNet" value='<s:property value="engineerFeeNet" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engieer Fee Vat<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="engineerFeeVat" value='<s:property value="engineerFeeVat" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Fee Gross<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="engineerFeeGross" value='<s:property value="engineerFeeGross" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Storage Recovery Net<span class="mandatory">*</span>
                </label>
            <input type="text" class="chox-ttnum"  name="storageRecoveryNet" value='<s:property value="storageRecoveryNet" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Storage Recovery Vat<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="storageRecoveryVat" value='<s:property value="storageRecoveryVat" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Storage Recovery Gross<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="storageRecoveryGross" value='<s:property value="storageRecoveryGross" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Total Net<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="totalNet" value='<s:property value="totalNet" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Total Vat<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="totalVat" value='<s:property value="totalVat" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Total Gross<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="totalGross" value='<s:property value="totalGross" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Claims Handling Invoice Amount<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="claimsHandlingInvoiceAmount" value='<s:property value="claimsHandlingInvoiceAmount" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Deduction For Claims Handling Fee<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="deductionForClaimsHandlingFee" value='<s:property value="deductionForClaimsHandlingFee" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Discount<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="discount" value='<s:property value="discount" />'/></div>
            
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Total To Pay<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttnum"  name="totalToPay" value='<s:property value="totalToPay" />'/>
            </div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Excess Collected From Policyholder<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="excessAmountCollected" value='<s:property value="excessAmountCollected" />'/></div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                VAT Collected From Policyholder<span class="mandatory">*</span></label>
            <input type="text" class="chox-ttnum"  name="vatAmountCollected" value='<s:property value="vatAmountCollected" />'/></div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Date Invoiced<span class="mandatory">*</span></label>
            <span id="dateInvoicedPH"></span></div>
            
            
            
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" id="submitInvoice"/>
            </div>
            
            <div id="INVmessageBox" style="text-align:center"></div>  
            <div class="chox-form-submit-result">&nbsp;</div>                                              
        </div>
    </fieldset>
</form>