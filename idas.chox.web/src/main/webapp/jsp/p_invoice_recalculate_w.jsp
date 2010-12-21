<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var rentalStartTimePicker = -1;
    var rentalEndTimePicker = -1;
    var randomNumber=<s:property value="actionSelected"/>;
    var i=1;
    var isFormChanged = false;
    var formChange='<s:property value="formChanged"/>';
    var msg = 'You haven\'t saved your changes after Re-Calculating';
    var ashow,bshow,cshow,dshow=false;
    var a,b,c,d=1;
    var hire_vat_rate= '<s:property value="hire_vat_used"/>'*100;
    var repair_vat_rate= '<s:property value="repair_vat_used"/>'*100;
    var engineer_vat_rate= '<s:property value="engineerFee_vat_used"/>'*100;
    var totalLoss_vat_rate= '<s:property value="totalLossFee_vat_used"/>'*100;
    var storageRecovery_vat_rate= '<s:property value="storageRecovery_vat_used"/>'*100;
    

    $(function(){
        $(':input').change(function(){
            if(!isFormChanged){
                isFormChanged = true;
                formChange=1;
                document.getElementById('submitFormAction1').value=formChange;
            }
        });
        window.onbeforeunload = function(){

            if(randomNumber==20){
                isFormChanged=true;
            }
            if((randomNumber==20) && formChange>=1){
                return msg;
            }
        };
        createVehicleClassPriceHelpNote();
        ui.dateField('dateInvoiced', '<s:date format="dd/MM/yyyy" name="dateInvoiced" />' ,'dateInvoicedPH');
        var form0= $("#formUpdateInvoiceRecalculationForm");
        var form = $("#formUpdateInvoiceForm");
        var form1 = $("#formUpdateExtrasFORM");
        var form2 = $("#formUpdateHireVehicle");
        var form3 = $("#formEngRptAction");
        var form4 = $("#formSubmitButtons");
        var fsets = $('legend',form);
        fsets.click(function(){
            $(this).next().toggle();

            if(!bshow&&!cshow&&!dshow)
            {
                if(document.getElementById('hideAndShow').value==0){

                    $(form4).toggle();
                    if((++a)%2==0){
                        ashow=true;
                    }else{
                        ashow=false;
                    }
                }
            }
        });
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        var fsets1 =  $('legend',form1);
        fsets1.click(function(){
            $(this).next().toggle();
            if(!ashow&&!cshow&&!dshow)
            {
                if(document.getElementById('hideAndShow').value==0){

                    $(form4).toggle();
                    if((++b)%2==0){
                        bshow=true;
                    }else{
                        bshow=false;
                    }
                }
            }
        });
        fsets1.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets1.mouseout(function(){ $(this).css("cursor","normal");});

        var fsets2 =  $('legend',form2);
        fsets2.click(function(){ $(this).next().toggle();
            if(!ashow&&!bshow&&!dshow)
            {
                if(document.getElementById('hideAndShow').value==0){
                    $(form4).toggle();
                    if((++c)%2==0){
                        cshow=true;
                    }else{
                        cshow=false;
                    }
                }
            }});
        fsets2.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets2.mouseout(function(){ $(this).css("cursor","normal");});

        var fsets3 =  $('legend', form3);
        fsets3.click(function(){ $(this).next().toggle();
            if(!bshow&&!cshow&&!ashow)
            { 
                if(document.getElementById('hideAndShow').value==0){
                    $(form4).toggle();
                    if((++d)%2==0){
                        dshow=true;
                    }else{
                        dshow=false;
                    }
                }
            }});
        fsets3.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets3.mouseout(function(){ $(this).css("cursor","normal");});
        ui.dateField('rentalStart', '<s:date format="dd/MM/yyyy" name="rentalStart" />' ,'rentalStartPH');
        ui.dateField('rentalEnd', '<s:date format="dd/MM/yyyy" name="rentalEnd" />' ,'rentalEndPH');
        rentalStartTimePicker = new Ext.form.TimeField({
            name: 'rentalStartTime',
            width: 100,
            allowBlank: true,
            validationEvent : false,
            increment: 15,
            format:'H:i',
            value: '<s:property value="rentalStartTime" />',
            renderTo:'rentalStartTimePH'
        });
        rentalEndTimePicker = new Ext.form.TimeField({
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
                totalLossFeeNet :{required:true,number:true, min:0},
                totalLossFeeVat :{required:true,number:true, min:0},
                totalLossFeeGross :{required:true,number:true, min:0},
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
                days : { required:true,min:0, digits:true },
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
                totalLossFeeNet :{required:"You must supply a value for 'Total Loss Fee Net'", number:"You must supply a numeric value for 'Total Loss Fee Net'", min:"Total Loss Fee Net must be greater or equal to zero"},
                totalLossFeeVat :{required:"You must supply a value for 'Total Loss Fee Vat'", number:"You must supply a numeric value for 'Total Loss Fee Vat'", min:"Total Loss Fee Vat must be greater or equal to zero"},
                totalLossFeeGross :{required:"You must supply a value for 'Total Loss Fee Gross'", number:"You must supply a numeric value for 'Total Loss Fee Gross'", min:"Total Loss Fee Gross must be greater or equal to zero"},
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
                    required:"You must supply a value for 'Estimated Labour Amount'"
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

    function changeHireRate(){
        var vehicleClassId = $('#vehicleClassComboId :selected').text();
    <s:iterator value="allVehicleClassPriceMapper">
            if(vehicleClassId=='<s:property value="name"/>'){
                document.getElementById("HireRate").value = <s:property value="price"/>;
            }
    </s:iterator>

        }
        function updateHireMonitoringPanel() {
            var vehicleClassId = $('#vehicleClassComboId :selected').text();
            document.getElementById("hireMonitorVehicleClassId").innerHTML = vehicleClassId;
            var time = $('#rentalStart').val() + ' ' + rentalStartTimePicker.getValue();
            document.getElementById("hireMonitorHireStartId").innerHTML = time;
            if(randomNumber==20){
                $("#resultMessage").hide();
                Ext.MessageBox.alert('VAT Rates Used', 'Hire VAT: '+hire_vat_rate+'%<br/>  Repair VAT: '+repair_vat_rate+
                    '% <br/> Engineer Fee VAT: '+engineer_vat_rate+'% <br/>Total Loss Fee VAT: '+totalLoss_vat_rate+
                    '% <br/>Storage Recovery VAT:'+storageRecovery_vat_rate+'%');
            }else{$("#resultMessage").show();
                $("#resultMessage").fadeOut(10000);}
            
        }
        function resetForm(){

            formChange=0;
            randomNumber=30;
            return randomNumber;
        }
        function submitForm(){
            formChange=0;
            randomNumber=10;
            return randomNumber;
        }

        function recalculateForm(){

            randomNumber=20;
            if(formChange==1){
                document.getElementById('submitFormAction1').value=formChange;
            }
            return randomNumber;
        }

        function createVehicleClassPriceHelpNote(){
            var attachmentHtmlDesc = "";
            attachmentHtmlDesc = "<table cellpadding='0' cellspacing='0' border='0' class='remark-table1'>";
            attachmentHtmlDesc += "<tr><th width='25%'><b>Vehicle<br/>Class&nbsp</b></th><th width='25%'><b>Price</b></th><th width='35%'><b>Vehicle<br/>Class&nbsp</b></th><th width='15%'><b>Price</b></th></tr>";
    <s:iterator value="allVehicleClassPriceMapper">
            if(('<s:property value="name"/>'!="ACX") && ('<s:property value="name"/>'!="UNATTACHED"))
            {
                if(<s:property value="price"/>!=0 && ++i%2==0)
                {
                    attachmentHtmlDesc += '<tr>';
                    attachmentHtmlDesc += '<td><s:property value="name"/></td>';
                    attachmentHtmlDesc += '<td><s:property value="price"/></td>';
                }
                else if(<s:property value="price"/>!=0){
                    attachmentHtmlDesc += '<td><s:property value="name"/></td>';
                    attachmentHtmlDesc += '<td><s:property value="price"/></td>';
                    attachmentHtmlDesc += '</tr>';
                }
            }
    </s:iterator>

            attachmentHtmlDesc += "</table>";

            new Ext.ToolTip({
                target: 'attachmentTypeSpan',
                html: attachmentHtmlDesc,
                title: 'Hire Rates',
                autoHide: false,
                closable: true
            });
            
            Ext.QuickTips.init();
        }
</script>


<form id="formUpdateInvoiceRecalculationForm" name="formUpdateInvoiceRecalculationForm" action="<%=request.getContextPath()%>/prv/p/updateInvoiceRecalculation.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <input type="hidden" id="submitAction" name="actionSelected" value=""/>
    <input type="hidden" id="submitFormAction1" name="formChanged" value= "-1"/>
    <input type="hidden" id="hideAndShow" value= "0"/>

    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr valign="top">
            <td class="chox-form-left-col">
                <div>
                    <div id="formUpdateInvoiceForm"  class="XXentity-form">

                        <fieldset class="x-fieldset partial">
                            <legend>Invoice Detail</legend>

                            <div class="form-container" id="invoiceDetailWId">

                                <table>

                                    <tr>
                                    <div class="status-info">
                                        <span class="std-label-ro-small1-bold">N.B. </span>Figures in brackets indicate changes have been made<br/>to the invoice field(s) in question and the figures enclosed <br/>are the original values that were loaded into the system.
                                    </div>
                                    </tr>

                                    <tr>
                                        <td>

                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Supplier Claims Handling #</label>
                                                <input type="text" class="chox-ttxt"  name="handlingInvoiceNo" value="<s:property value="handlingInvoiceNo" />"/>
                                            </div>
                                        </td>
                                        <td>

                                        </td>

                                    </tr>
                                    <tr>
                                        <td>

                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Supplier Claim Invoice #<span class="mandatory">*</span></label>
                                                <input type="text" class="chox-ttxt"  name="claimInvoiceNo" title="Enter claim invoice no"value="<s:property value="claimInvoiceNo" />"/>
                                            </div>
                                        </td>
                                        <td>

                                        </td>
                                    </tr>

                                    <tr>
                                        <td>

                                            <div class="chox-form-item" >
                                                <label class="chox-form-std-label">Hire Rate Charged Per Day&nbsp;&nbsp;<img src="../images/help.png" id="attachmentTypeSpan" alt=""/><span class="mandatory">*</span></label>
                                                <input id="HireRate" type="text" class="chox-ttnum" name="hireRateChargedPerDay" value="<s:property value="hireRateChargedPerDay" />"/>
                                            </div>
                                        </td>
                                        <td>

                                            <div class="chox-form-item" ><s:if test="hireRateChargedPerDay!=hireRateChargedPerDay_original&&(hireRateChargedPerDay_original!=null)">
                                                    <label class="chox-ttnum-smalll" id="tooltip">(<s:property value="hireRateChargedPerDay_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>

                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Hire Net<span class="mandatory">*</span></label>
                                                <input type="text" id="hireNet" class="chox-ttnum" name="hireNet" value="<s:property value="hireNet" />" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="hireNet!=hireNet_original&&(hireNet_original!=null)">
                                                    <label class="chox-ttnum-smalll" >(<s:property value="hireNet_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>

                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Hire VAT<span class="mandatory">*</span></label>
                                                <input type="text" id="hireVat" class="chox-ttnum" name="hireVat" value="<s:property value="hireVat"  />" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="hireVat!=hireVat_original&&(hireVat_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="hireVat_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td>

                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Hire Gross<span class="mandatory">*</span></label>
                                                <input type="text" id="hireGross" class="chox-ttnum"  name="hireGross" value="<s:property value="hireGross" />" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="hireGross!=hireGross_original&&(hireGross_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="hireGross_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td>


                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Repair Net<span class="mandatory">*</span></label>
                                                <input type="text" id="repairNet" class="chox-ttnum"  name="repairNet" value="<s:property value="repairNet" />" /></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="repairNet!=repairNet_original&&(repairNet_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="repairNet_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Repair VAT<span class="mandatory">*</span></label>
                                                <input type="text" id="repairVat" class="chox-ttnum"  name="repairVat" value="<s:property value="repairVat" />" /></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="repairVat!=repairVat_original&&(repairVat_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="repairVat_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Repair Gross<span class="mandatory">*</span></label>
                                                <input type="text" id="repairGross" class="chox-ttnum"  name="repairGross" value="<s:property value="repairGross" />" /></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="repairGross!=repairGross_original&&(repairGross_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="repairGross_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Engineer Fee Net<span class="mandatory">*</span></label>
                                                <input type="text" id="engineerFeeNet" class="chox-ttnum"  name="engineerFeeNet" value="<s:property value="engineerFeeNet" />" /></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="engineerFeeNet!=engineerFeeNet_original&&(engineerFeeNet_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="engineerFeeNet_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Engineer Fee VAT<span class="mandatory">*</span></label>
                                                <input type="text" id="engineerFeeVat"class="chox-ttnum"  name="engineerFeeVat" value="<s:property value="engineerFeeVat" />" /></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="engineerFeeVat!=engineerFeeVat_original&&(engineerFeeVat_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="engineerFeeVat_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Engineer Fee Gross<span class="mandatory">*</span></label>
                                                <input type="text" id="engineerFeeGross" class="chox-ttnum"  name="engineerFeeGross" value="<s:property value="engineerFeeGross" />" /></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="engineerFeeGross!=engineerFeeGross_original&&(engineerFeeGross_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="engineerFeeGross_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Total Loss Fee Net<span class="mandatory">*</span></label>
                                                <input type="text" id="totalLossFeeNet" class="chox-ttnum"  name="totalLossFeeNet" value="<s:property value="totalLossFeeNet" />"/></div>

                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="totalLossFeeNet!=totalLossFeeNet_original&&(totalLossFeeNet_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="totalLossFeeNet_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Total Loss Fee VAT<span class="mandatory">*</span></label>
                                                <input type="text" id="totalLossFeeVat" class="chox-ttnum"  name="totalLossFeeVat" value="<s:property value="totalLossFeeVat" />"/></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="totalLossFeeVat!=totalLossFeeVat_original&&(totalLossFeeVat_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="totalLossFeeVat_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Total Loss Fee Gross<span class="mandatory">*</span></label>
                                                <input type="text" id="totalLossFeeGross" class="chox-ttnum"  name="totalLossFeeGross" value="<s:property value="totalLossFeeGross" />"/></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="totalLossFeeGross!=totalLossFeeGross_original&&(totalLossFeeGross_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="totalLossFeeGross_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Storage Recovery Net<span class="mandatory">*</span></label>
                                                <input type="text" id="storageRecoveryNet" class="chox-ttnum"  name="storageRecoveryNet" value="<s:property value="storageRecoveryNet" />" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="storageRecoveryNet!=storageRecoveryNet_original&&(storageRecoveryNet_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="storageRecoveryNet_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Storage Recovery VAT<span class="mandatory">*</span></label>
                                                <input type="text" id="storageRecoveryVat" class="chox-ttnum"  name="storageRecoveryVat" value="<s:property value="storageRecoveryVat" />" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="storageRecoveryVat!=storageRecoveryVat_original&&(storageRecoveryVat_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="storageRecoveryVat_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Storage Recovery Gross<span class="mandatory">*</span></label>
                                                <input type="text" id="storageRecoveryGross" class="chox-ttnum"  name="storageRecoveryGross" value="<s:property value="storageRecoveryGross" />" /></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="storageRecoveryGross!=storageRecoveryGross_original&&(storageRecoveryGross_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="storageRecoveryGross_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Total Net<span class="mandatory">*</span></label>
                                                <input type="text" id="totalNet" class="chox-ttnum"  name="totalNet" value="<s:property value="totalNet" />" /></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="totalNet!=totalNet_original&&(totalNet_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="totalNet_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Total Vat<span class="mandatory">*</span></label>
                                                <input type="text" id="totalVat" class="chox-ttnum"  name="totalVat" value="<s:property value="totalVat" />" /></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="totalVat!=totalVat_original&&(totalVat_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="totalVat_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Total Gross<span class="mandatory">*</span></label>
                                                <input type="text" id="totalGross" class="chox-ttnum"  name="totalGross" value="<s:property value="totalGross" />" /></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="totalGross!=totalGross_original&&(totalGross_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="totalGross_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Claims Handling Invoice Amount<span class="mandatory">*</span></label>
                                                <input type="text" id="claimsHandlingInvoiceAmount" class="chox-ttnum"  name="claimsHandlingInvoiceAmount" value="<s:property value="claimsHandlingInvoiceAmount" />" /></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="claimsHandlingInvoiceAmount!=claimsHandlingInvoiceAmount_original&&(claimsHandlingInvoiceAmount_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="claimsHandlingInvoiceAmount_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Deduction For Claims Handling Fee<span class="mandatory">*</span></label>
                                                <input type="text" id="deductionForClaimsHandlingFee" class="chox-ttnum"  name="deductionForClaimsHandlingFee" value="<s:property value="deductionForClaimsHandlingFee" />" /></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="deductionForClaimsHandlingFee!=deductionForClaimsHandlingFee_original&&(deductionForClaimsHandlingFee_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="deductionForClaimsHandlingFee_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Discount<span class="mandatory">*</span></label>
                                                <input type="text" id="discount" class="chox-ttnum"  name="discount" value="<s:property value="discount" />" /></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="discount!=discount_original&&(discount_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="discount_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>

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
                                        </td>
                                        <td>
                                            <label></label>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Hire Penalty Charge</label>
                                                <input type="text" id="hirePenaltyCharge" class="chox-ttnum"  name="hirePenaltyCharge" disabled="true" value="<s:property value="hirePenaltyCharge" />" /></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="hirePenaltyCharge!=hirePenaltyCharge_original&&(hirePenaltyCharge_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="hirePenaltyCharge_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>

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
                                        </td>
                                        <td>
                                            <label></label>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Repair Penalty Charge</label>
                                                <input type="text" id="repairPenaltyCharge" class="chox-ttnum"  name="repairPenaltyCharge" disabled="true" value="<s:property value="repairPenaltyCharge" />" /></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="repairPenaltyCharge!=repairPenaltyCharge_original&&(repairPenaltyCharge_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="repairPenaltyCharge_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Total Penalty Charge</label>
                                                <input type="text" id="totalPenaltyCharge" class="chox-ttnum"  name="totalPenaltyCharge" disabled="true" value="<s:property value="totalPenaltyCharge" />" /></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="totalPenaltyCharge!=totalPenaltyCharge_original&&(totalPenaltyCharge_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="totalPenaltyCharge_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Full Total Requested<span class="mandatory">*</span></label>
                                                <input type="text" id="fullTotalToPay" class="chox-ttnum"  name="fullTotalToPay" value="<s:property value="fullTotalToPay" />" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="fullTotalToPay!=fullTotalToPay_original&&(fullTotalToPay_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="fullTotalToPay_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label-big">
                                                    Total To Pay <span class="mandatory">*</span></label>
                                                <input type="text" id="totalToPay" class="chox-ttnum"   name="totalToPay" disabled="true" value="<s:property value="totalToPay" />" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="totalToPay!=totalToPay_original&&(totalToPay_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="totalToPay_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
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
                                        </td>
                                        <td>
                                            <label></label>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Excess Collected From Policyholder<span class="mandatory">*</span></label>
                                                <input type="text" class="chox-ttnum"  name="excessAmountCollected" value="<s:property value="excessAmountCollected" />"/></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="excessAmountCollected!=excessAmountCollected_original&&(excessAmountCollected_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="excessAmountCollected_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    VAT Collected From Policyholder<span class="mandatory">*</span></label>
                                                <input type="text" class="chox-ttnum"  name="vatAmountCollected" value="<s:property value="vatAmountCollected" />"/></div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" ><s:if test="vatAmountCollected!=vatAmountCollected_original&&(vatAmountCollected_original!=null)">
                                                    <label class="chox-ttnum-smalll">(<s:property value="vatAmountCollected_original" />)</label>
                                                </s:if></div>
                                        </td>
                                    </tr>
                                    <tr>

                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-la1">Date Invoiced<span class="mandatory">*</span></label>
                                                <label class="std-data-ro"><span id="dateInvoicedPH"></span></label>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item"><s:if test="CanShowOriginalInvoicedDate">
                                                    <label class="chox-ttnum-smalll">(<s:date format="dd/MM/yyyy" name="dateInvoiced_original" />)</label>
                                                </s:if> </div>

                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Invoice Uploaded Date</label>
                                                <label class="std-data-ro"><s:date name="createdDate" format="dd MMM yyyy" /></label>
                                                <s:if test="invoicedDays > 0">
                                                    <label class="std-data-ro"> (<s:property value="invoicedDays" /> days)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                        <td>
                                            <label></label>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label1">Time Invoice Has Been<br/>With CHO For Review</label>
                                                <label class="std-data-ro"><s:property value="daysWithCHOForReview" /></label>
                                            </div>
                                        </td>
                                        <td>
                                            <label></label>
                                        </td>
                                    </tr>

                                    <div class="chox-form-item">&nbsp;</div>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label1">Time Invoice Has Been<br/>With Insurer For Review</label>
                                                <label class="std-data-ro"><s:property value="daysWithInsurerForReview" /></label>
                                            </div>
                                        </td>
                                        <td>
                                            <label></label>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">&nbsp;</div>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label1">Time Claim Has Been<br/>Awaiting Liability Resolution</label>
                                                <label class="std-data-ro"><s:property value="daysAwaitingLiabilityResolution" /></label>
                                            </div>
                                        </td>
                                        <td>
                                            <label></label>
                                        </td>
                                    </tr>

                                </table>

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


                                <table>
                                    <tr>
                                        <td>

                                            <div class="chox-form-item" id="VehicleClass">
                                                <label class="chox-form-std-label">
                                                    Replacement Vehicle Class<span class="mandatory">*</span></label>
                                                    <s:select id="vehicleClassComboId" name="vehicleClassId" list="vehicleClasses" listKey="id" listValue="name" headerKey="-1" headerValue="--- SELECT ---" emptyOption="false" onchange="return changeHireRate()"></s:select>
                                            </div>
                                        </td>

                                        <td>
                                            <div class="chox-form-item">
                                                <s:if test="VehicleClassName!=VehicleClassName_original&&(VehicleClassName_original!=null)&&(VehicleClassName_original!=\"UNATTACHED\")">
                                                    <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="VehicleClassName_original" />)</label>
                                                </s:if>
                                            </div>


                                        </td>
                                    </tr>
                                    <tr>

                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Hire Start (Date)<span class="mandatory">*</span></label>
                                                <span id="rentalStartPH"></span></div>

                                        </td>

                                        <td>
                                            <div class="chox-form-item"><s:if test="canShowOriginalStartDate">
                                                    <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:date format="dd/MM/yyyy" name="rentalStart_original" /><span id="rentalStart_originalPH"></span>)</label>
                                                </s:if> </div>


                                        </td>
                                    </tr>
                                    <tr>

                                        <td>

                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Hire Start (Time)<span class="mandatory">*</span></label>
                                                <span id="rentalStartTimePH"></span></div>
                                        </td>

                                        <td>
                                            <div class="chox-form-item"><s:if test="rentalStartTime!=rentalStartTime_original&&(rentalStartTime_original!=null)">
                                                    <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="RentalStartTimeDisplayFormat" /><span id="rentalStartTime_originalPH"></span>)</label>
                                                </s:if> </div>


                                        </td>
                                    </tr>
                                    <tr>

                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Hire End (Date)<span class="mandatory">*</span></label>
                                                <span id="rentalEndPH"></span></div>
                                        </td>

                                        <td>
                                            <div class="chox-form-item"><s:if test="canShowOriginalEndDate">
                                                    <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:date format="dd/MM/yyyy" name="rentalEnd_original" /><span id="rentalEnd_originalPH"></span>)</label>
                                                </s:if> </div>


                                        </td>
                                    </tr>
                                    <tr>

                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Hire End (Time)<span class="mandatory">*</span></label>
                                                <span id="rentalEndTimePH"></span></div>

                                        </td>


                                        <td>
                                            <div class="chox-form-item"><s:if test="rentalEndTime!=rentalEndTime_original&&(rentalEndTime_original!=null)">
                                                    <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="RentalEndTimeDisplayFormat" /><span id="rentalEndTime_originalPH"></span>)</label>
                                                </s:if> </div>


                                        </td>


                                    </tr>
                                </table>



                                <div class="chox-form-item">
                                    <label class="chox-form-std-label">
                                        Reason For Collection</label>
                                    <input type="text" class="chox-ttxt" id="HVDReasonForCollection" name="collectionReason" value="<s:property value="collectionReason" />" /></div>
                                <table>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">

                                                <label class="chox-form-std-label">
                                                    No. Days Hire<span class="mandatory">*</span></label>
                                                <input type="text" class="chox-ttnum" id="HVDNNumberOfDaysHire" name="days" value="<s:property value="days" />" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item">


                                                <s:if test="days!=days_original&&(days_original!=null)">
                                                    <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="days_original" />)</label>
                                                </s:if>




                                            </div>
                                        </td>
                                    </tr>
                                </table>

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

                    <div>
                        <div id="formUpdateExtrasFORM" class="XXentity-form">

                            <fieldset class="x-fieldset partial">
                                <legend>Extras</legend>
                                <div class="form-container" id="extrasWId">

                                    <table>

                                        <tr>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <label class="chox-form-std-label">CDW Fee<span class="mandatory">*</span></label>
                                                    <input id="cdwFee" type="text" class="chox-ttnum" name="cdwFee" value="<s:property value="cdwFee"/>" />&nbsp;
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<s:if test="cdwFee!=cdwFee_original&&(cdwFee_original!=null)">(<s:property value="cdwFee_original" />)</s:if></label>&nbsp;
                                                </div>
                                            </td>

                                        </tr>

                                        <tr>
                                            <td>

                                                <div class="chox-form-item" id="cdwQty">
                                                    <label class="chox-form-std-label">CDW Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" name="cdwQty" value="<s:property value="cdwQty" />"/></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="cdwQty!=cdwQty_original&&(cdwQty_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="cdwQty_original" />)</label>
                                                    </s:if></div>


                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" >
                                                    <label class="chox-form-std-label">Automatic Fee<span class="mandatory">*</span></label>
                                                    <input id="automaticFee" type="text" class="chox-ttnum" name="automaticFee" value="<s:property value="automaticFee" />" /></div>

                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="automaticFee!=automaticFee_original&&(automaticFee_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="automaticFee_original" />)</label>
                                                    </s:if></div>


                                            </td>
                                        </tr>
                                        <tr>
                                            <td>


                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label" id="automaticQty">Automatic Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" name="automaticQty" value="<s:property value="automaticQty" />"/></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="automaticQty!=automaticQty_original&&(automaticQty_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="automaticQty_original" />)</label>
                                                    </s:if></div>


                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" >
                                                    <label class="chox-form-std-label">Additional Driver Fee<span class="mandatory">*</span></label>
                                                    <input id="additionalDriverFee" type="text" class="chox-ttnum" name="additionalDriverFee" value="<s:property value="additionalDriverFee" />" /></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="additionalDriverFee!=additionalDriverFee_original&&(additionalDriverFee_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="additionalDriverFee_original" />)</label>
                                                    </s:if></div>


                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="additionalDriverQty">
                                                    <label class="chox-form-std-label">Additional Driver Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" name="additionalDriverQty" value="<s:property value="additionalDriverQty" />"/></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="additionalDriverQty!=additionalDriverQty_original&&(additionalDriverQty_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="additionalDriverQty_original" />)</label>
                                                    </s:if></div>


                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Sat Nav Fee<span class="mandatory">*</span></label>
                                                    <input id="satNavFee" type="text" class="chox-ttnum" name="satNavFee" value="<s:property value="satNavFee" />" /></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="satNavFee!=satNavFee_original&&(satNavFee_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="satNavFee_original" />)</label>
                                                    </s:if></div>


                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="satNavQty">
                                                    <label class="chox-form-std-label">Sat Nav Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" name="satNavQty" value="<s:property value="satNavQty" />"/></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="satNavQty!=satNavQty_original&&(satNavQty_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="satNavQty_original" />)</label>
                                                    </s:if></div>


                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Estate Fee<span class="mandatory">*</span></label>
                                                    <input id="estateFee" type="text" class="chox-ttnum" name="estateFee" value="<s:property value="estateFee" />" /></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="estateFee!=estateFee_original&&(estateFee_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="estateFee_original" />)</label>
                                                    </s:if></div>


                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="estateQty">
                                                    <label class="chox-form-std-label">Estate Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" name="estateQty" value="<s:property value="estateQty" />" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="estateQty!=estateQty_original&&(estateQty_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="estateQty_original" />)</label>
                                                    </s:if></div>


                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Baby Seat Fee<span class="mandatory">*</span></label>
                                                    <input id="babySeatFee" type="text" class="chox-ttnum" name="babySeatFee" value="<s:property value="babySeatFee" />" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="babySeatFee!=babySeatFee_original&&(babySeatFee_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="babySeatFee_original" />)</label>
                                                    </s:if></div>


                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="babySeatQty">
                                                    <label class="chox-form-std-label">Baby Seat Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" name="babySeatQty" value="<s:property value="babySeatQty" />"/>&nbsp;&nbsp
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="babySeatQty!=babySeatQty_original&&(babySeatQty_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="babySeatQty_original" />)</label>
                                                    </s:if></div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Tow Bars Fee<span class="mandatory">*</span></label>
                                                    <input id="towBarFee" type="text" class="chox-ttnum" name="towBarsFee" value="<s:property value="towBarsFee" />" /></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="towBarsFee!=towBarsFee_original&&(towBarsFee_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="towBarsFee_original" />)</label>
                                                    </s:if></div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="towBarQty">
                                                    <label class="chox-form-std-label">Tow Bars Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" name="towBarsQty" value="<s:property value="towBarsQty" />"/></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="towBarsQty!=towBarsQty_original&&(towBarsQty_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="towBarsQty_original" />)</label>
                                                    </s:if></div>


                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Non-standard Risk Ins. Premium Fee<span class="mandatory">*</span></label>
                                                    <input id="nonStandPremiumFee" type="text" class="chox-ttnum" name="nonStandardInsurancePremiumFee" value="<s:property value="nonStandardInsurancePremiumFee" />" /></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="nonStandardInsurancePremiumFee!=nonStandardInsurancePremiumFee_original&&(nonStandardInsurancePremiumFee_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="nonStandardInsurancePremiumFee_original" />)</label>
                                                    </s:if></div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="nonStandPremiumQty">
                                                    <label class="chox-form-std-label">Non-standard Risk Ins. Premium Qty<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" name="nonStandardInsurancePremiumQty" value="<s:property value="nonStandardInsurancePremiumQty" />"/></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="nonStandardInsurancePremiumQty!=nonStandardInsurancePremiumQty_original&&(nonStandardInsurancePremiumQty_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="nonStandardInsurancePremiumQty_original" />)</label>
                                                    </s:if></div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">
                                                        Cover Note Required For<br/>Customer's Own Insurance Policy?</label>
                                                    <s:checkbox name="coverNoteRequired" /></div>
                                            </td>
                                            <td>

                                            </td>
                                        </tr>

                                        <div class="chox-form-item"><label class="chox-form-std-label">&nbsp;</label></div>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Admin Fee<span class="mandatory">*</span></label>
                                                    <input id="adminFee" type="text" class="chox-ttnum" name="adminFee" value="<s:property value="adminFee" />" /></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="adminFee!=adminFee_original&&(adminFee_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="adminFee_original" />)</label>
                                                    </s:if></div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="adminQty">
                                                    <label class="chox-form-std-label">Admin Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" name="adminQty" value="<s:property value="adminQty" />"/></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="adminQty!=adminQty_original&&(adminQty_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="adminQty_original" />)</label>
                                                    </s:if></div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Roof Rack Fee<span class="mandatory">*</span></label>
                                                    <input id="roofRackFee" type="text" class="chox-ttnum" name="roofRackFee" value="<s:property value="roofRackFee" />" /></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="roofRackFee!=roofRackFee_original&&(roofRackFee_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="roofRackFee_original" />)</label>
                                                    </s:if></div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="roofRackQty">
                                                    <label class="chox-form-std-label">Roof Rack Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" name="roofRackQty" value="<s:property value="roofRackQty" />"/></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="roofRackQty!=roofRackQty_original&&(roofRackQty_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="roofRackQty_original" />)</label>
                                                    </s:if></div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Dual Control Fee<span class="mandatory">*</span></label>
                                                    <input id="dualCtrlFee" type="text" class="chox-ttnum" name="dualControlFee" value="<s:property value="dualControlFee" />" /></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="dualControlFee!=dualControlFee_original&&(dualControlFee_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="dualControlFee_original" />)</label>
                                                    </s:if></div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label" id="dualCtrlQty">Dual Control Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" name="dualControlQty" value="<s:property value="dualControlQty" />"/></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="dualControlQty!=dualControlQty_original&&(dualControlQty_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="dualControlQty_original" />)</label>
                                                    </s:if></div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Delivery Collection Fee<span class="mandatory">*</span></label>
                                                    <input id="deliveryCollectionFee" type="text" class="chox-ttnum" name="deliveryCollectionFee" value="<s:property value="deliveryCollectionFee" />" /></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="deliveryCollectionFee!=deliveryCollectionFee_original&&(deliveryCollectionFee_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="deliveryCollectionFee_original" />)</label>
                                                    </s:if></div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="deliveryCollectionQty">
                                                    <label class="chox-form-std-label">Delivery Collection Fee Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" name="deliveryCollectionQty" value="<s:property value="deliveryCollectionQty" />"/></div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >                                                     <s:if test="deliveryCollectionQty!=deliveryCollectionQty_original&&(deliveryCollectionQty_original!=null)">
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="deliveryCollectionQty_original" />)</label>
                                                    </s:if></div>
                                            </td>
                                        </tr>
                                    </table>

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

                </div>
            </td>
        </tr>
    </table>
    <div id="formSubmitButtons"  class="XXentity-form">
        <div class="chox-form-button">
            <div id="EngRptmessageBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div class="chox-form-submit-result" id="resultMessage"><s:property value="actionResult" /></div>
            <table align="center">
                <tr >
                    <td>
                        <input type="submit" value="Re-Calculate" id="Re-CalculateAlltheChanges" onclick="submitAction.value = recalculateForm()"/>&nbsp&nbsp
                    </td>
                    <td>
                        <input type="submit" value="Save Changes" id="submitAllChanges" onclick="submitAction.value = submitForm()"/>&nbsp&nbsp
                    </td>
                    <td>
                        <input type="submit" value="Reset" id="resetAllChanges" class="cancel" onclick="submitAction.value= resetForm()"/>
                    </td>

                </tr>

            </table>

        </div>
        <br/>

        <div class="status-info">
            <span class="std-label-ro-small1-bold"> Re-Calculate: </span>This button will take any changes made to the invoice and automatically re-calculate all the totals to create a new 'Total To Pay'.  Please note there is no need to change VAT or Gross fields, only change Net fields as the re-calculate function will set these automatically.<br/>
            <span class="std-label-ro-small1-bold">Save Changes: </span>This saves any changes made to the invoice, if the re-calculate function has been used this button should be clicked in order to save the changes made.  Manual changes can still be made to the invoice without using the re-calculate function, once these changes have been made click on this button.<br/>
            <span class="std-label-ro-small1-bold">Reset: </span>This will reset any changes made to the invoice since changes were saved last.<br/>
        </div>



    </div>
    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
</form>

