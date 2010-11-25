<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var rentalStartTimPicker = -1;
    $(function(){
        ui.dateField('dateInvoiced', '<s:date format="dd/MM/yyyy" name="dateInvoiced" />' ,'dateInvoicedPH');
        var form0= $("#formUpdateInvoiceRecalculationForm");
        var form = $("#formUpdateInvoiceForm");
        var form1 = $("#formUpdateExtrasFORM");
        var form2 = $("#formUpdateHireVehicle");
        var form3 = $("#formEngRptAction");
        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});
        var fsets1 =  $('legend',form1);
        fsets1.click(function(){ $(this).next().toggle();});
        fsets1.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets1.mouseout(function(){ $(this).css("cursor","normal");});
        var fsets2 =  $('legend',form2);
        fsets2.click(function(){ $(this).next().toggle();});
        fsets2.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets2.mouseout(function(){ $(this).css("cursor","normal");});
        var fsets3 =  $('legend', form3);
        fsets3.click(function(){ $(this).next().toggle();});
        fsets3.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets3.mouseout(function(){ $(this).css("cursor","normal");});
        ui.dateField('rentalStart', '<s:date format="dd/MM/yyyy" name="rentalStart" />' ,'rentalStartPH');
        ui.dateField('rentalEnd', '<s:date format="dd/MM/yyyy" name="rentalEnd" />' ,'rentalEndPH');
        rentalStartTimPicker = new Ext.form.TimeField({
            name: 'rentalStartTime',
            width: 100,
            allowBlank: true,
            validationEvent : false,
            increment: 15,
            format:'H:i',
            value: '<s:property value="rentalStartTime" />',
            renderTo:'rentalStartTimePH'
        });
        var rentalEndTimPicker = new Ext.form.TimeField({
            name: 'rentalEndTime',
            width: 100,
            allowBlank: true,
            validationEvent : false,
            increment: 15,
            format:'H:i',
            value: '<s:property value="rentalEndTime" />',
            renderTo:'rentalEndTimePH'
        });
        $.validator.addMethod('time', function (value) {
            return /^(\d{2}:\d{2})$/.test(value);
        });
        form0.validate(
        {
            errorLabelContainer: "#EngRptmessageBox",
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
                dateInvoiced :{required:true, date:true},
                cdwFee:{required:true, number:true},
                cdwQty:{required:true, digits:true},
                automaticFee:{required:true, number:true},
                automaticQty:{required:true, digits:true},
                additionalDriverFee:{required:true, number:true},
                additionalDriverQty:{required:true, digits:true},
                satNavFee:{required:true, number:true},
                satNavQty:{required:true, digits:true},
                estateFee:{required:true, number:true},
                estateQty:{required:true, digits:true},
                babySeatFee:{required:true, number:true},
                babySeatQty:{required:true, digits:true},
                towBarsFee:{required:true, number:true},
                towBarsQty:{required:true, digits:true},
                nonStandardInsurancePremiumFee:{required:true, number:true},
                nonStandardInsurancePremiumQty:{required:true, digits:true},
                adminFee:{required:true, number:true},
                adminQty:{required:true, digits:true},
                roofRackFee:{required:true, number:true},
                roofRackQty:{required:true, digits:true},
                dualControlFee:{required:true, number:true},
                dualControlQty:{required:true, digits:true},
                deliveryCollectionFee:{required:true, number:true},
                deliveryCollectionQty:{required: true, digits:true},
                vehicleManufacturer:{required:true},
                vehicleModel:{required:true},
                vehicleRegistration:{required:true},
                rentalStart:{date:true,required:true},
                rentalStartTime:{time:true,required:true},
                rentalEnd:{date:true,required:true},
                rentalEndTime:{time:true,required:true},
                vehicleClassId : { min:1 },
                days : { min:0, digits:true },
                labourAmount:{required:true, number:true},
                totalAmount:{required:true, number:true},
                estimatedDays:{required:true, digits:true}
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
                dateInvoiced :{ required:"You must supply a value for 'Date Invoiced'", date:"Invalid date format for Date Invoiced"},
                cdwFee :{required:"Please supply a valid value for 'Cdw Fee'", number:"Please supply a valid value for 'Cdw Fee'"},
                cdwQty:{required:"Please supply a valid value for 'Cdw Qty'", digits:"Please supply a valid value for 'Cdw Qty'"},
                automaticFee:{required:"Please supply a valid value for 'Automatic Fee'", number:"Please supply a valid value for 'Automatic Fee'"},
                automaticQty:{required:"Please supply a valid value for 'Automatic Qty'", digits:"Please supply a valid value for 'Automatic Qty'"},
                additionalDriverFee:{required:"Please supply a valid value for 'Additional Driver Fee'", number:"Please supply a valid value for 'Additional Driver Fee'"},
                additionalDriverQty:{required:"Please supply a valid value for 'Additional Driver Qty'", digits:"Please supply a valid value for 'Additional Driver Qty'"},
                satNavFee:{required:"Please supply a valid value for 'Satnav Fee'", number:"Please supply a valid value for 'Satnav Fee'"},
                satNavQty:{required:"Please supply a valid value for 'Satnav Qty'", digits:"Please supply a valid value for 'Satnav Qty'"},
                estateFee:{required:"Please supply a valid value for 'Estate Fee'", number:"Please supply a valid value for 'Estate Fee'"},
                estateQty:{required:"Please supply a valid value for 'Estate Qty'", digits:"Please supply a valid value for 'Estate Qty'"},
                babySeatFee:{required:"Please supply a valid value for 'Baby-seat Fee'", number:"Please supply a valid value for 'Baby-seat Fee'"},
                babySeatQty:{required:"Please supply a valid value for 'Baby-seat Qty'", digits:"Please supply a valid value for 'Baby-seat Qty'"},
                towBarsFee:{required:"Please supply a valid value for 'Tow-bars Fee'", number:"Please supply a valid value for 'Tow-bars Fee'"},
                towBarsQty:{required:"Please supply a valid value for 'Tow-bars Qty'", digits:"Please supply a valid value for 'Tow-bars Qty'"},
                nonStandardInsurancePremiumFee:{required:"Please supply a valid value for 'Non-Standard Insurance Premium Fee'", number:"Please supply a valid value for 'Non-Standard Insurance Premium Fee'"},
                nonStandardInsurancePremiumQty:{required:"Please supply a valid value for 'Non-Standard Insurance Premium Qty'", digits:"Please supply a valid value for 'Non-Standard Insurance Premium Qty'"},
                adminFee:{required:"Please supply a valid value for 'Admin Fee'", number:"Please supply a valid value for 'Admin Fee'"},
                adminQty:{required:"Please supply a valid value for 'Admin Qty'", digits:"Please supply a valid value for 'Admin Qty'"},
                roofRackFee:{required:"Please supply a valid value for 'Roofrack Fee'", number:"Please supply a valid value for 'Roofrack Fee'"},
                roofRackQty:{required:"Please supply a valid value for 'Roofrack Qty'", digits:"Please supply a valid value for 'Roofrack Qty'"},
                dualControlFee:{required:"Please supply a valid value for 'Dual Control Fee'", number:"Please supply a valid value for 'Dual Control Fee'"},
                dualControlQty:{required:"Please supply a valid value for 'Dual Control Qty'", digits:"Please supply a valid value for 'Dual Control Qty'"},
                deliveryCollectionFee:{required:"Please supply a valid value for 'Delivery Collection Fee'", number:"Please supply a valid value for 'Delivery Collection Fee'"},
                deliveryCollectionQty:{required:"Please supply a valid value for 'Delivery Collection Qty'", digits:"Please supply a valid value for 'Delivery Collection Qty'"},
                vehicleManufacturer:{required:"You must supply a value for 'Vehicle Manufacturer"},
                vehicleModel:{required:"You must supply a value for 'Vehicle Model'"},
                vehicleRegistration:{required:"You must supply a value for 'Vehicle Registration'"},
                rentalStart: {date:"Invalid date format for 'Hire Start (Date)'", required:"You must supply a value for 'Hire Start (Date)'"},
                rentalEnd: {date:"Invalid date format for 'Hire End (Date)'", required:"You must supply a value for 'Hire End (Date)'"},
                rentalStartTime: {time:"Invalid date format for 'Hire Start (Time)'", required:"You must supply a value for 'Hire Start (Time)'"},
                rentalEndTime: {time:"Invalid date format for 'Hire End (Time)'", required:"You must supply a value for 'Hire End (Time)'"},
                vehicleClassId:{min: "You must select a Vehicle Class"},
                days:{required:"You must supply a value for 'No. Days Hire'", min: "You must supply a value for 'No. Days Hire' that is greater than 0", digits: "You must supply a numeric value for 'No. Days Hire'"},
                labourAmount: {
                    number:"You must supply a numeric value for 'Estimated Labour Amount'",
                    required:"You must supply a value for 'stimated Labour Amount'"
                },
                totalAmount: {
                    required:"You must supply a value for 'Estimated Total Repair Amount'",
                    number:"You must supply a numeric value for 'Estimated Total Repair Amount'"
                },
                estimatedDays: {
                    required:"You must supply a value for 'Estimated Days Under Repair'",
                    digits:"You must supply a integer value for 'Estimated Days Under Repair'"
                }

            }
        });

        ui.ajaxForm(form0,updateHireMonitoringPanel,'html');
    });

    function updateHireMonitoringPanel() {
        var vehicleClassId = $('#vehicleClassComboId :selected').text();
        document.getElementById("hireMonitorVehicleClassId").innerHTML = vehicleClassId;
        var time = $('#rentalStart').val() + ' ' + rentalStartTimPicker.getValue();
        document.getElementById("hireMonitorHireStartId").innerHTML = time;
    }

</script>


<form id="formUpdateInvoiceRecalculationForm" name="formUpdateInvoiceRecalculationForm" action="<%=request.getContextPath()%>/prv/p/updateInvoiceRecalculation.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr valign="top">
            <td class="chox-form-left-col">
                <div>
                    <div id="formUpdateInvoiceForm"  class="XXentity-form">

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
                            </div>
                        </fieldset>
                    </div>
                </div>
                <div>
                    <div id="formUpdateHireVehicle" class="XXentity-form">

                        <fieldset class="x-fieldset partial">
                            <legend>Hire Vehicle Details</legend>
                            <div class="form-container" id="hireVehicleDetailWId">
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">
                                        Manufacturer<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttxt" id="HVDManufacturer" name="vehicleManufacturer" value="<s:property value="vehicleManufacturer" />" /></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">
                                        Model<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttxt" id="HVDModel" name="vehicleModel" value="<s:property value="vehicleModel" />" /></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">
                                        Registration<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttxt" id="HVDRegistration"  name="vehicleRegistration" value="<s:property value="vehicleRegistration" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">
                                        Replacement Vehicle Class<span class="mandatory">*</span></label>
                                        <s:select id="vehicleClassComboId" name="vehicleClassId" list="vehicleClasses" listKey="id" listValue="name" headerKey="-1" headerValue="--- SELECT ---" emptyOption="false"></s:select>
                                </div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">
                                        Hire Start (Date)<span class="mandatory">*</span></label>
                                    <span id="rentalStartPH"></span></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">
                                        Hire Start (Time)<span class="mandatory">*</span></label>
                                    <span id="rentalStartTimePH"></span></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">
                                        Hire End (Date)<span class="mandatory">*</span></label>
                                    <span id="rentalEndPH"></span></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">
                                        Hire End (Time)<span class="mandatory">*</span></label>
                                    <span id="rentalEndTimePH"></span></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">
                                        Reason For Collection</label>
                                    <input type="text" class="chox-ttxt" id="HVDReasonForCollection" name="collectionReason" value="<s:property value="collectionReason" />" /></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">
                                        No. Days Hire<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" id="HVDNNumberOfDaysHire" name="days" value="<s:property value="days" />" /></div>
                                <hr width="80%"/>
                                <div>
                                    <a  href="http://www.hpicheck.com/" target="_blank"><img align="right" src="<%= request.getContextPath()%>/images/logo-hpi.png" style="display: inline;" alt="HPI" width="80" height="60" border="0"/></a>
                                    <label>HPI Check</label>
                                    <s:if test="hpiError != null">
                                        <label class="std-label-small">&nbsp;&nbsp;&nbsp;(HPI Check information not available)</label>
                                    </s:if>
                                    <table class="chox-table-form">
                                        <tr>
                                            <td><label class="std-label-ro">Vehicle Manufacturer</label></td>
                                            <td>&nbsp;</td>
                                            <td><label class="std-data-ro"><s:property value="hpiVehicleManufacturer" /></label></td>
                                        </tr>
                                        <tr>
                                            <td><label class="std-label-ro">Vehicle Model</label></td>
                                            <td>&nbsp;</td>
                                            <td><label class="std-data-ro"><s:property value="hpiVehicleModel" /></label></td>
                                        </tr>
                                        <tr>
                                            <td><label class="std-label-ro">Year of Manufacture</label></td>
                                            <td>&nbsp;</td>
                                            <td><label class="std-data-ro"><s:property value="hpiVehicleYear" /></label></td>
                                        </tr>
                                        <tr>
                                            <td><label class="std-label-ro">Date of Registration</label></td>
                                            <td>&nbsp;</td>
                                            <td><label class="std-data-ro"><s:property value="hpiFirstRegistration" /></label></td>
                                        </tr>
                                        <tr>
                                            <td><label class="std-label-ro">Engine Capacity</label></td>
                                            <td>&nbsp;</td>
                                            <td><label class="std-data-ro"><s:property value="hpiVehicleCapacity" /></label></td>
                                        </tr>
                                        <tr>
                                            <td><label class="std-label-ro">Door Plan</label></td>
                                            <td>&nbsp;</td>
                                            <td><label class="std-data-ro"><s:property value="hpiVehicleDoorplan" /></label></td>
                                        </tr>
                                        <tr>
                                            <td><label class="std-label-ro">Transmission</label></td>
                                            <td>&nbsp;</td>
                                            <td><label class="std-data-ro"><s:property value="hpiVehicleTransmission" /></label></td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </fieldset>
                    </div>
                </div>

            </td>
            <td>

                <div>
                    <div id="formUpdateExtrasFORM" class="XXentity-form">

                        <fieldset class="x-fieldset partial">
                            <legend>Extras</legend>
                            <div class="form-container" id="extrasWId">
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">CDW Fee<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="cdwFee" value="<s:property value="cdwFee" />"/>
                                </div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">CDW Quantity<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="cdwQty" value="<s:property value="cdwQty" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Automatic Fee<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="automaticFee" value="<s:property value="automaticFee" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Automatic Quantity<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="automaticQty" value="<s:property value="automaticQty" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Additional Driver Fee<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="additionalDriverFee" value="<s:property value="additionalDriverFee" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Additional Driver Quantity<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="additionalDriverQty" value="<s:property value="additionalDriverQty" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Sat Nav Fee<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="satNavFee" value="<s:property value="satNavFee" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Sat Nav Quantity<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="satNavQty" value="<s:property value="satNavQty" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Estate Fee<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="estateFee" value="<s:property value="estateFee" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Estate Quantity<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="estateQty" value="<s:property value="estateQty" />"/>
                                </div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Baby Seat Fee<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="babySeatFee" value="<s:property value="babySeatFee" />"/>
                                </div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Baby Seat Quantity<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="babySeatQty" value="<s:property value="babySeatQty" />"/>
                                </div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Tow Bars Fee<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="towBarsFee" value="<s:property value="towBarsFee" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Tow Bars Quantity<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="towBarsQty" value="<s:property value="towBarsQty" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Non-standard Risk Ins. Premium Fee<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="nonStandardInsurancePremiumFee" value="<s:property value="nonStandardInsurancePremiumFee" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Non-standard Risk Ins. Premium Qty<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="nonStandardInsurancePremiumQty" value="<s:property value="nonStandardInsurancePremiumQty" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">
                                        Cover Note Required For<br/>Customer's Own Insurance Policy?</label>
                                    <s:checkbox name="coverNoteRequired" /></div>
                                <div class="chox-form-item"><label class="chox-form-std-label">&nbsp;</label></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Admin Fee<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="adminFee" value="<s:property value="adminFee" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Admin Quantity<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="adminQty" value="<s:property value="adminQty" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Roof Rack Fee<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="roofRackFee" value="<s:property value="roofRackFee" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Roof Rack Quantity<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="roofRackQty" value="<s:property value="roofRackQty" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Dual Control Fee<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="dualControlFee" value="<s:property value="dualControlFee" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Dual Control Quantity<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="dualControlQty" value="<s:property value="dualControlQty" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Delivery Collection Fee<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="deliveryCollectionFee" value="<s:property value="deliveryCollectionFee" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Delivery Collection Fee Quantity<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-ttnum" name="deliveryCollectionQty" value="<s:property value="deliveryCollectionQty" />"/></div>
                            </div>
                        </fieldset>
                    </div>
                </div>
                <div>
                    <div id="formEngRptAction" class="XXentity-form">

                        <fieldset class="x-fieldset  partial">
                            <legend>Engineer Report</legend>
                            <div class="form-container" id="engineerReportWId">
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Estimated Labour Amount<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-tnum" name="labourAmount" value="<s:property value="labourAmount" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Estimated Total Repair Amount<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-tnum" name="totalAmount" value="<s:property value="totalAmount" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Estimated Days Under Repair<span class="mandatory">*</span></label>
                                    <input type="text" class="chox-tnum" name="estimatedDays" value="<s:property value="estimatedDays" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Usable?</label><s:checkbox name="isUsable" />
                                </div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Name</label>
                                    <input type="text" class="chox-ttxt" name="name" value="<s:property value="name" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Company</label>
                                    <input type="text" class="chox-ttxt" name="company" value="<s:property value="company" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Engineer Address 1</label>
                                    <input type="text" class="chox-ttxt" name="address1" value="<s:property value="address1" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Engineer Address 2</label>
                                    <input type="text" class="chox-ttxt" name="address2" value="<s:property value="address2" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Engineer Address 3</label>
                                    <input type="text" class="chox-ttxt" name="address3" value="<s:property value="address3" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Engineer Address 4</label>
                                    <input type="text" class="chox-ttxt" name="address4" value="<s:property value="address4" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Engineer Address 5</label>
                                    <input type="text" class="chox-ttxt" name="address5" value="<s:property value="address5" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Engineer Postcode</label>
                                    <input type="text" class="chox-ttxt" name="postcode" value="<s:property value="postcode" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Engineer Telephone</label>
                                    <input type="text" class="chox-ttxt" name="telephone" value="<s:property value="telephone" />"/></div>
                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">Engineer Email</label>
                                    <input type="text" class="chox-ttxt" name="email" value="<s:property value="email" />"/>
                                </div>
                            </div>
                        </fieldset>
                    </div>
                </div>
            </td>
        </tr>
    </table>
    <div class="chox-form-submit-result"></div>
    <div id="EngRptmessageBox" class="action-error-msg"><s:property value="actionError" /></div>
    <div class="chox-form-submit-result"><s:property value="actionResult" /></div>
    <div class="chox-form-button">
        <input type="submit" value="Save Changes" id="submitAllChanges"/>
    </div>
    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
</form>