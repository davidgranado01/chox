<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ include file="invoiceSavings.jspf" %>

<script type="text/javascript">
    var rentalStartTimePicker = -1;
    var rentalEndTimePicker = -1;
    var randomNumber=<s:property value="actionSelected"/>;
    var isFormChanged = false;
    var formChange='<s:property value="formChanged"/>';
    var msg = 'You haven\'t saved your changes after Re-Calculating';
    var ashow=false,bshow=false,cshow=false,dshow=false;
    var a=1,b=1,c=1,d=1,i=1;
    var hire_vat_rate= '<s:property value="hire_vat_used"/>';
    var repair_vat_rate= '<s:property value="repair_vat_used"/>';
    var engineer_vat_rate= '<s:property value="engineerFee_vat_used"/>';
    var tpi_insurer_premium_vat_rate= '<s:property value="tpiInsurancePremiumVatUsed"/>';
    var totalLoss_vat_rate= '<s:property value="totalLossFee_vat_used"/>';
    var storageRecovery_vat_rate= '<s:property value="storageRecovery_vat_used"/>';
    var hireInsurerDiscountApplied = '<s:property value="hireInsurerDiscountCalculated"/>';
    var repairInsurerDiscountApplied = '<s:property value="repairInsurerDiscountCalculated"/>';
    var totalInsurerDiscountApplied = '<s:property value="totalInsurerDiscountCalculated"/>';
    var vcInvDetailCombo;
    
    var noteMessageDiv=null;
    var tpiClaimChk;
    <s:if test="tpiClaim">
        tpiClaimChk= true;
    </s:if><s:else >
        tpiClaimChk = false;
    </s:else>
    

    Ext.onReady(function(){

        $(':input').change(function(){
            if(!isFormChanged){
                isFormChanged = true;
                formChange=1;
                document.getElementById('submitFormAction1').value=formChange;
            }
        });
        window.onbeforeunload = function(){

            if(randomNumber===20){
                isFormChanged=true;
            }
            if((randomNumber===20) && formChange>=1){
                Ext.get('claimDetailScreenDiv').unmask();
                return msg;
            }
        };
        createVehicleClassPriceHelpNote();
        ui.unvalidatedDateField('dateInvoiced', '<s:date format="dd/MM/yyyy" name="dateInvoiced" />' ,'dateInvoicedPH');
        var form0= $("#formUpdateInvoiceRecalculationForm");
        var form = $("#formUpdateInvoiceForm");
        var form1 = $("#formUpdateExtrasFORM");
        var form2 = $("#formUpdateHireVehicle");
        var form3 = $("#formEngRptAction");
        var form4 = $("#formSubmitButtons");
        var form5;
        if (document.getElementById("formRepairExtras") !== null)
            form5 = $("#formRepairExtras");
        var fsets = $('legend',form);
        
        fsets.click(function(){
            $(this).next().toggle();
            if(document.getElementById('hideAndShow').value===1){
                ashow=true,bshow=true,cshow=true,dshow=true;
                a=2,b=2,c=2,d=2;
                document.getElementById('hideAndShow').value=2;
            }
            if(document.getElementById('hideAndShow').value===0){
                ashow=false,bshow=false,cshow=false,dshow=false;
                a=1,b=1,c=1,d=1;
                document.getElementById('hideAndShow').value=2;
            }
            if((++a)%2===0){
                ashow=true;
            }else{
                ashow=false;
            }
            
            if(!bshow&&!cshow&&!dshow)
            {
                $(form4).toggle();
            }
        });
        
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        var fsets1 =  $('legend',form1);
        fsets1.click(function(){
            $(this).next().toggle();

            if(document.getElementById('hideAndShow').value===1){
                ashow=true,bshow=true,cshow=true,dshow=true;
                a=2,b=2,c=2,d=2;
                document.getElementById('hideAndShow').value=2;
            }

            if(document.getElementById('hideAndShow').value===0){
                ashow=false,bshow=false,cshow=false,dshow=false;
                a=1,b=1,c=1,d=1;
                document.getElementById('hideAndShow').value=2;
            }

            if((++b)%2===0){
                bshow=true;
            }else{
                bshow=false;
            }
            
            if(!ashow&&!cshow&&!dshow)
            {
               

                $(form4).toggle();
                    
                
            }
        });
        fsets1.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets1.mouseout(function(){ $(this).css("cursor","normal");});

        var fsets2 =  $('legend',form2);
        fsets2.click(function(){ $(this).next().toggle();

            if(document.getElementById('hideAndShow').value===1){
                ashow=true,bshow=true,cshow=true,dshow=true;
                a=2,b=2,c=2,d=2;
                document.getElementById('hideAndShow').value=2;
            }

            if(document.getElementById('hideAndShow').value===0){
                ashow=false,bshow=false,cshow=false,dshow=false;
                a=1,b=1,c=1,d=1;
                document.getElementById('hideAndShow').value=2;
            }
            if((++c)%2===0){
                cshow=true;
            }else{
                cshow=false;
            }
            
            if(!ashow&&!bshow&&!dshow)
            {
                
                $(form4).toggle();
                    
            }
        });
        fsets2.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets2.mouseout(function(){ $(this).css("cursor","normal");});

        var fsets3 =  $('legend', form3);
        fsets3.click(function(){ $(this).next().toggle();

            if(document.getElementById('hideAndShow').value===1){
                ashow=true,bshow=true,cshow=true,dshow=true;
                a=2,b=2,c=2,d=2;
                document.getElementById('hideAndShow').value=2;
            }

            if(document.getElementById('hideAndShow').value===0){
                ashow=false,bshow=false,cshow=false,dshow=false;
                a=1,b=1,c=1,d=1;
                document.getElementById('hideAndShow').value=2;
            }
            if((++d)%2===0){
                dshow=true;
            }else{
                dshow=false;
            }
           
            if(!bshow&&!cshow&&!ashow)
            { 
                
                $(form4).toggle();
                    
                
            }
        });
        fsets3.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets3.mouseout(function(){ $(this).css("cursor","normal");});
        
        if (document.getElementById("formRepairExtras") !== null) {
            var fsets5 =  $('legend', form5);
            fsets5.click(function(){ $(this).next().toggle();

                if(document.getElementById('hideAndShow').value===1){
                    ashow=true,bshow=true,cshow=true,dshow=true;
                    a=2,b=2,c=2,d=2;
                    document.getElementById('hideAndShow').value=2;
                }

                if(document.getElementById('hideAndShow').value===0){
                    ashow=false,bshow=false,cshow=false,dshow=false;
                    a=1,b=1,c=1,d=1;
                    document.getElementById('hideAndShow').value=2;
                }
                if((++d)%2===0){
                    dshow=true;
                }else{
                    dshow=false;
                }
           
                if(!bshow&&!cshow&&!ashow)
                {
                    $(form4).toggle();
                }
            });
            fsets5.mouseover(function(){ $(this).css("cursor","pointer"); });
            fsets5.mouseout(function(){ $(this).css("cursor","normal");});
        }
        var rentalStartDatePicker = new Ext.form.DateField({
                name: 'rentalStart',
                renderTo: 'rentalStartPH',
                width: 100,
                allowBlank: true,
                format: 'd/m/Y',
                value: '<s:date format="dd/MM/yyyy" name="rentalStart" />',
                showWeekNumber: true,
                validationEvent : false
        });
       
        var rentalEndDatePicker = new Ext.form.DateField({
                name: 'rentalEnd',
                renderTo: 'rentalEndPH',
                width: 100,
                allowBlank: true,
                format: 'd/m/Y',
                value: '<s:date format="dd/MM/yyyy" name="rentalEnd" />',
                showWeekNumber: true,
                validationEvent : false
        });
            
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

        var vcInvDetailJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'text'},
                    {name:'value'}
                ]
        });

        var vcInvDetailStore = new choxDataStore({
                url : "/prv/p/getAvailableVehicleClasses.action",
                reader : vcInvDetailJsonReader
                ,listeners: {load: function() {
                    vcInvDetailCombo.setValue('<s:property value="vehicleClass.id"/>'); 
                    if (randomNumber === 10) {
                        var vehicleClassText = vcInvDetailCombo.getRawValue();
                        var time = document.formUpdateInvoiceRecalculationForm.rentalStart.value + ' ' + rentalStartTimePicker.getValue();
                        if (document.getElementById("hireMonitorVehicleClassId") !== null && document.getElementById("hireMonitorHireStartId") !== null) {
                            document.getElementById("hireMonitorVehicleClassId").innerHTML = vehicleClassText;
                            document.getElementById("hireMonitorHireStartId").innerHTML = time;
                        } else if (Ext.getCmp("vcHMComboId") !== null && document.getElementById("rentalStart") !== null && document.getElementById("rentalStartTimePickerHMVId") !== null) {
                            Ext.getCmp("vcHMComboId").setValue('<s:property value="vehicleClass.id"/>');
                            Ext.getCmp("rentalStart").setValue(document.formUpdateInvoiceRecalculationForm.rentalStart.value);
                            Ext.getCmp("rentalStartTimePickerHMVId").setValue(rentalStartTimePicker.getValue());
                        }
                    }
                }}
        });

        vcInvDetailCombo = new Ext.form.ComboBox({
                store: vcInvDetailStore,
                renderTo: 'vcInvDetailSelectionHolder',
                valueField: 'text',
                id: 'vcInvDetailComboId',
                hiddenName: 'vehicleClassId',
                displayField:'value',
                typeAhead: true,
                autoWidth: true,
                listWidth: 100,
                width: 100,
                mode: 'local',
                triggerAction: 'all',
                forceSelection : true,
                emptyText: '--- SELECT ---',
                listeners: {
                    select:function (field, newValue, oldValue ) {
                                
                                var vehicleClassId = field.getRawValue();
                                createVehicleClassPriceHelpNote();

                                <s:iterator value="allVehicleClassPriceMapper">
                                        if(vehicleClassId==='<s:property value="name"/>'){
                                            var price = parseFloat('<s:property value="price"/>');
                                            document.getElementById("HireRate").value = price.toFixed(2);
                                        }
                                </s:iterator>
                    }
                }
        });
        vcInvDetailStore.load();    
        
        $.validator.addMethod("vcInvDetailSelectionRule",
            function(value) {
                if(value === "" || value < 1) {
                    return false;
                }
                return true;
            }
        );
           

        form0.validate(
        {
            ignore: [],
            errorLabelContainer: "#EngRptmessageBox",
            rules: {
                vehicleManufacturer:{required:true},
                vehicleModel:{required:true},
                vehicleRegistration:{required:true},
                rentalStart:{dateITA:true,required:true},
                rentalStartTime:{time:true,required:true},
                rentalEnd:{dateITA:true,required:true},
                rentalEndTime:{time:true,required:true},
                vehicleClassId : { vcInvDetailSelectionRule : true },
                days : { required:true,min:0, digits:true },
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
                totalNet:{required:true, number:true, min:0},
                totalVat:{required:true, number:true, min:0},
                totalGross:{required:true, number:true, min:0},
                claimsHandlingInvoiceAmount:{required:true, number:true, min:0},
                deductionForClaimsHandlingFee:{required:true, number:true, max:0},
                discount:{required:true, number:true, max:0},
                fullTotalToPay:{required:true, number:true, min:0},
                excessAmountCollected:{required:true, number:true, min:0},
                vatAmountCollected:{required:true, number:true, min:0},
                dateInvoiced:{required:true, dateITA:true},
                collaborationFee:{required:true, number:true},
                collaborationQty:{required:true, digits:true},
                miscellaneousFee:{required:true, number:true},
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
                deliveryCollectionQty:{required: true, digits:true}
                
            },
            messages: {
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
                dateInvoiced :{ required:"You must supply a value for 'Date Invoiced'", dateITA:"Invalid date format for Date Invoiced"},
<s:if test="isCollaborationProtocolClaim">
                collaborationFee :{required:"Please supply a valid value for 'Collaboration Protocol Fee'", number:"Please supply a valid value for 'Collaboration Protocol Fee'"},
                collaborationQty :{required:"Please supply a valid value for 'Collaboration Protocol Quantity'", digits:"Please supply a valid value for 'Collaboration Protocol Quantity'"},
</s:if>
<s:if test="isSubscriberClaim">
                miscellaneousFee :{required:"Please supply a valid value for 'Acquisition Fee'", number:"Please supply a valid value for 'Acquisition Fee'"},
</s:if>
<s:else>
                miscellaneousFee :{required:"Please supply a valid value for 'Miscellaneous Fee'", number:"Please supply a valid value for 'Miscellaneous Fee'"},
</s:else>
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
                rentalStart: {dateITA:"Invalid date format for 'Hire Start (Date)'", required:"You must supply a value for 'Hire Start (Date)'"},
                rentalEnd: {dateITA:"Invalid date format for 'Hire End (Date)'", required:"You must supply a value for 'Hire End (Date)'"},
                rentalStartTime: {time:"Invalid date format for 'Hire Start (Time)'", required:"You must supply a value for 'Hire Start (Time)'"},
                rentalEndTime: {time:"Invalid date format for 'Hire End (Time)'", required:"You must supply a value for 'Hire End (Time)'"},
                vehicleClassId:{vcInvDetailSelectionRule : "You must select a Vehicle Class"},
                days:{required:"You must supply a value for 'No. Days Hire'", min: "You must supply a value for 'No. Days Hire' that is greater than 0", digits: "You must supply a numeric value for 'No. Days Hire'"}
            }
        });
        ui.ajaxForm(form0,updateHireMonitoringPanel,'html',unmaskRecalculationForm);
        
        $("#IDsuccessBox").fadeOut(10000);
<s:if test="penaltyChargesAppled">
	refreshPenaltyChargeForm();
    Ext.MessageBox.alert('Information', "The Invoice has been updated and penalty charges have previously been applied. You may now wish to adjust the penalty charges to conform to the new Invoice values.");
</s:if>
<s:if test="modelSaved">
	refreshPenaltyChargeForm();
</s:if>

    // For invoiceSavings functionality, need to update action on 'Agree Invoice' button as
    // total gross may have been updated (only need to do this on reload by a 'Save' command)
    var ieVersion = get_MSIE_version();
<s:if test="invoiceSavingActive">
    if ( $( "#UMIPFormId" ).length ){
        if (ieVersion==9 || ieVersion==8){
            $("#UMIPFormId").removeAttr("onclick").unbind('click');
            $("#UMIPFormId").click(function(){return confirmInvoiceSavingsAction();});
        }else{
            $("#UMIPFormId").attr("onclick","return confirmInvoiceSavingsAction();");
        }
    }
</s:if>
<s:else>
    if ( $( "#UMIPFormId" ).length ){
        if (ieVersion==9 || ieVersion==8){
            $("#UMIPFormId").removeAttr("onclick").unbind('click');
            $("#UMIPFormId").click(function(){return doUpdateManualInvoice('updateManualInvoiceAgreeQuantum');});
        }else{
            $("#UMIPFormId").attr("onclick","doUpdateManualInvoice('updateManualInvoiceAgreeQuantum');");
        }
    }
</s:else>

    }); // Ext.onReady

        function get_MSIE_version(){
            var ua = window.navigator.userAgent;
            var msie = ua.indexOf ( "MSIE " );

            if ( msie > 0 )      // If Internet Explorer, return version number
                return parseInt (ua.substring (msie+5, ua.indexOf (".", msie )));
            else                 // If another browser, return 0
                return 0;
	}

    function updateHireMonitoringPanel() {
            ashow=true;
            bshow=true;
            cshow=true;
            dshow=true;
            a=2;
            b=2;
            c=2;
            d=2;
            document.getElementById('hideAndShow').value=2;
            
            if(randomNumber===20){
                $("#resultMessage").hide();
                var vatCalculationText = '&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp'
                    +'&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp'
                    +'&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<br>'
                    +'Hire VAT: '+parseFloat(hire_vat_rate).toFixed(2)+'%<br/>'
                    +'Repair VAT: '+parseFloat(repair_vat_rate).toFixed(2)+'% <br/> '
                    +'Engineer Fee VAT: '+parseFloat(engineer_vat_rate).toFixed(2)+'% <br/>' 
                    +'Total Loss Fee VAT: '+parseFloat(totalLoss_vat_rate).toFixed(2)+'% <br/>' 
                    +'Storage Recovery VAT: '+parseFloat(storageRecovery_vat_rate).toFixed(2)+'% <br/>';
                            
            <s:if test="tpiClaim">
                        vatCalculationText = vatCalculationText + 'Insurance Premium VAT: '+parseFloat(tpi_insurer_premium_vat_rate).toFixed(2)+ '% <br/>';
            </s:if>
                
                if (hireInsurerDiscountApplied > 0) {
                    vatCalculationText = vatCalculationText + 'Hire Insurer Discount: '+parseFloat(hireInsurerDiscountApplied).toFixed(2)+'% <br/>';
                }
                if (repairInsurerDiscountApplied > 0) {
                    vatCalculationText = vatCalculationText + 'Repair Insurer Discount: '+parseFloat(repairInsurerDiscountApplied).toFixed(2)+'% <br/>';
                }
                if (totalInsurerDiscountApplied > 0) {
                    vatCalculationText = vatCalculationText + 'Total Insurer Discount: '+parseFloat(totalInsurerDiscountApplied).toFixed(2)+'% <br/>';
                }
                Ext.MessageBox.alert('VAT Rates Used', vatCalculationText);
            }else{$("#resultMessage").show();
                $("#resultMessage").fadeOut(10000);
            }
            
        }
        function showNoteMessage(){
            if(!noteMessageDiv){
                noteMessageDiv = Ext.get('NoteMessage');
                noteMessageDiv.addClass('status-info-recalculation');
                // class="status-info-recalculation"
                noteMessageDiv.createChild('<span class="std-label-ro-small1-bold">N.B. </span>Figures in brackets indicate changes have been made<br/>to the invoice field(s) in question and the figures enclosed <br/>are the original values that were loaded into the system.');
            }

            // myDiv1.show();
        }
        function resetForm(){
            formChange=0;
            randomNumber=30;
            $('#formUpdateInvoiceRecalculationForm').contents().find(':input').each(function() {
                $(this).rules( "remove" );
            });
            Ext.get('formUpdateInvoiceRecalculationForm').mask('Please wait...');
            return randomNumber;
        }

        function unmaskRecalculationForm() {
            Ext.get('formUpdateInvoiceRecalculationForm').unmask();
            ui.promptErrorMsg("An unexpected error occurred. Please try again. If this problem persists, please contact CHOX support.");
        }
        function submitForm(){
            if(tpiClaimChk){
                var settings = $('form#formUpdateInvoiceRecalculationForm').validate().settings;
                delete settings.rules.vehicleManufacturer;
                delete settings.rules.vehicleModel;
                delete settings.rules.vehicleRegistration;
                delete settings.rules.rentalStart;
                delete settings.rules.rentalStartTime;
                delete settings.rules.rentalEnd;
                delete settings.rules.rentalEndTime;
                delete settings.rules.vehicleClassId;
                delete settings.rules.days;
            }
            formChange=0;
            document.getElementById('invoiceSubmitAction').value=10;
 
            if($("form#formUpdateInvoiceRecalculationForm").valid()){
                Ext.get('formUpdateInvoiceRecalculationForm').mask('Please wait...');
//                $("form#formUpdateInvoiceRecalculationForm").submit();
            }

//            return false;
        }

        function recalculateForm(){
            
            if(tpiClaimChk){
                var settings = $('form#formUpdateInvoiceRecalculationForm').validate().settings;
                delete settings.rules.vehicleManufacturer;
                delete settings.rules.vehicleModel;
                delete settings.rules.vehicleRegistration;
                delete settings.rules.rentalStart;
                delete settings.rules.rentalStartTime;
                delete settings.rules.rentalEnd;
                delete settings.rules.rentalEndTime;
                delete settings.rules.vehicleClassId;
                delete settings.rules.days;
            }

            randomNumber=20;
            if(formChange===1){
                document.getElementById('submitFormAction1').value=formChange;
            }
            document.getElementById('invoiceSubmitAction').value=20;
            if($("form#formUpdateInvoiceRecalculationForm").valid()){
                Ext.get('formUpdateInvoiceRecalculationForm').mask('Please wait...');
                $("form#formUpdateInvoiceRecalculationForm").submit();
            }
            return false;
        }

        function refreshPenaltyChargeForm(){
        	//we check if apply penalty charge form is on the page 
			var form = $("form#applyPenaltyCharge");
			if (form !== undefined && form.length){
				var param = {"id":'<s:property value="claimId" />'};
				var url = "/prv/p/updatePenaltyCharges.action";
				var alertPanelDiv = $("#penaltyAlertPanelId");
				if(alertPanelDiv !== undefined && alertPanelDiv.length){
					url = "/prv/p/getAlertPanel.action";
		            ajax.loadHtml2(url,param,function(data){
		            	alertPanelDiv.html(data);
		            });
				} else {
					ajax.loadHtml2(url,param,function(data){
		                $("#moreActionPanel").html(data);
		            });
				}
			}
        }       
        
        function restrictTwoDecimalPlaces(obj){
            var temp=parseFloat(obj.value);
            obj.value = temp.toFixed(2);
        }


        function createVehicleClassPriceHelpNote(){
            var attachmentHtmlDesc = "";
            attachmentHtmlDesc = "<table cellpadding='0' cellspacing='0' border='0' class='remark-table1'>";
            attachmentHtmlDesc += "<tr><th width='25%'><b>Vehicle<br/>Class&nbsp</b></th><th width='25%'><b>Price</b></th><th width='35%'><b>Vehicle<br/>Class&nbsp</b></th><th width='15%'><b>Price</b></th></tr>";
    <s:iterator value="allVehicleClassPriceMapper">
            if(('<s:property value="name"/>'!=="ACX") && ('<s:property value="name"/>'!=="UNATTACHED"))
            {
                if(<s:property value="price"/>!==0 && ++i%2===0)
                {
                    attachmentHtmlDesc += '<tr>';
                    attachmentHtmlDesc += '<td><s:property value="name"/></td>';
                    attachmentHtmlDesc += '<td><s:property value="price"/></td>';
                }
                else if(<s:property value="price"/>!==0){
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

<!--<div class="x-panel-bwrap chox-form-container">
<label id="expandAllInvoiceId" onclick="expandInvoiceDetails(true);" title="Expand All" style="cursor:pointer;font: 11px tahoma,arial,verdana,sans-serif;">+Expand All</label>-->
<form id="formUpdateInvoiceRecalculationForm" name="formUpdateInvoiceRecalculationForm" method="post" action="<%=request.getContextPath()%>/prv/p/updateInvoiceRecalculation.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'/>
    <input type="hidden" id="invoiceSubmitAction" name="actionSelected" value=""/>
    <input type="hidden" id="submitFormAction1" name="formChanged" value= "-1"/>
    <input type="hidden" id="hideAndShow" value= "0"/>

    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr valign="top">
            <td class="chox-form-left-col">
                <div>
                    <div id="formUpdateInvoiceForm"  class="XXentity-form">

                        <fieldset class="x-fieldset partial">
                            <legend>Invoice Detail</legend>
                            <div class="inv-form-container" id="invoiceDetailWId">
                                <div  id="NoteMessage"></div>
                                <table>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Invoice with Payments Team?</label>
                                                <label class="std-data-ro"><s:property value="paymentTeamDesc" /></label>
                                            </div>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Supplier Claims Handling #</label>
                                                <input type="text" class="chox-ttxt" id="invoiceRecalculatehandlingInvoiceNoId" name="handlingInvoiceNo" value="<s:property value="handlingInvoiceNo" />"/>
                                            </div>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Supplier Claim Invoice #</label>
                                                <input type="text" class="chox-ttxt" id="invoiceRecalculateclaimInvoiceNoId" name="claimInvoiceNo" title="Enter claim invoice no"value="<s:property value="claimInvoiceNo" />"/>
                                            </div>
                                        </td>
                                        <td></td>
                                    </tr>

                                    <tr>
                                        <td>
                                            <div class="chox-form-item" >
                                                <label class="chox-form-std-label">Hire Rate Charged Per Day&nbsp;&nbsp;<img src="../images/help.png" id="attachmentTypeSpan" alt=""/><span class="mandatory">*</span></label>
                                                <input id="HireRate" type="text" class="chox-ttnum" id="invoiceRecalculatehireRateChargedPerDayId" name="hireRateChargedPerDay" value="<s:property value="hireRateChargedPerDay" />" onkeyup="extractNumber(this,2,true);"/>
                                            </div>
                                        </td>
                                        <td>

                                            <div class="chox-form-item" >
                                                <s:if test="hireRateChargedPerDay!=hireRateChargedPerDayOriginal&&(hireRateChargedPerDayOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll" id="tooltip">(<s:property value="hireRateChargedPerDayOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>

                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Hire Net<span class="mandatory">*</span></label>
                                                <input type="text" id="hireNet" class="chox-ttnum" name="hireNet" value="<s:property value="hireNet" />" onkeyup="extractNumber(this,2,true);"/>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="hireNet!=hireNetOriginal&&(hireNetOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script> 
                                                    <label class="chox-ttnum-smalll" >(<s:property value="hireNetOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>

                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Hire VAT<span class="mandatory">*</span></label>
                                                <input type="text" id="hireVat" class="chox-ttnum" name="hireVat" value="<s:property value="hireVat"  />" onkeyup="extractNumber(this,2,true);"/>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="hireVat!=hireVatOriginal&&(hireVatOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="hireVatOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Hire Gross<span class="mandatory">*</span></label>
                                                <input type="text" id="hireGross" class="chox-ttnum"  name="hireGross" value="<s:property value="hireGross" />"  onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="hireGross!=hireGrossOriginal&&(hireGrossOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="hireGrossOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Repair Net<span class="mandatory">*</span></label>
                                                <input type="text" id="repairNet" class="chox-ttnum"  name="repairNet" value="<s:property value="repairNet" />"  onkeyup="extractNumber(this,2,true);"  />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="repairNet!=repairNetOriginal&&(repairNetOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="repairNetOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Repair VAT<span class="mandatory">*</span></label>
                                                <input type="text" id="repairVat" class="chox-ttnum"  name="repairVat" value="<s:property value="repairVat" />"   onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="repairVat!=repairVatOriginal&&(repairVatOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="repairVatOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Repair Gross<span class="mandatory">*</span></label>
                                                <input type="text" id="repairGross" class="chox-ttnum"  name="repairGross" value="<s:property value="repairGross" />"  onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="repairGross!=repairGrossOriginal&&(repairGrossOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="repairGrossOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Engineer Fee Net<span class="mandatory">*</span></label>
                                                <input type="text" id="engineerFeeNet" class="chox-ttnum"  name="engineerFeeNet" value="<s:property value="engineerFeeNet" />"  onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="engineerFeeNet!=engineerFeeNetOriginal&&(engineerFeeNetOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="engineerFeeNetOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Engineer Fee VAT<span class="mandatory">*</span></label>
                                                <input type="text" id="engineerFeeVat"class="chox-ttnum"  name="engineerFeeVat" value="<s:property value="engineerFeeVat" />" onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="engineerFeeVat!=engineerFeeVatOriginal&&(engineerFeeVatOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="engineerFeeVatOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Engineer Fee Gross<span class="mandatory">*</span></label>
                                                <input type="text" id="engineerFeeGross" class="chox-ttnum"  name="engineerFeeGross" value="<s:property value="engineerFeeGross" />"  onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="engineerFeeGross!=engineerFeeGrossOriginal&&(engineerFeeGrossOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="engineerFeeGrossOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Total Loss Fee Net<span class="mandatory">*</span></label>
                                                <input type="text" id="totalLossFeeNet" class="chox-ttnum"  name="totalLossFeeNet" value="<s:property value="totalLossFeeNet" />"  onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="totalLossFeeNet!=totalLossFeeNetOriginal&&(totalLossFeeNetOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="totalLossFeeNetOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Total Loss Fee VAT<span class="mandatory">*</span></label>
                                                <input type="text" id="totalLossFeeVat" class="chox-ttnum"  name="totalLossFeeVat" value="<s:property value="totalLossFeeVat" />"  onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="totalLossFeeVat!=totalLossFeeVatOriginal&&(totalLossFeeVatOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="totalLossFeeVatOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Total Loss Fee Gross<span class="mandatory">*</span></label>
                                                <input type="text" id="totalLossFeeGross" class="chox-ttnum"  name="totalLossFeeGross" value="<s:property value="totalLossFeeGross" />" onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="totalLossFeeGross!=totalLossFeeGrossOriginal&&(totalLossFeeGrossOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="totalLossFeeGrossOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Storage Recovery Net<span class="mandatory">*</span></label>
                                                <input type="text" id="storageRecoveryNet" class="chox-ttnum"  name="storageRecoveryNet" value="<s:property value="storageRecoveryNet" />"   onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="storageRecoveryNet!=storageRecoveryNetOriginal&&(storageRecoveryNetOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="storageRecoveryNetOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Storage Recovery VAT<span class="mandatory">*</span></label>
                                                <input type="text" id="storageRecoveryVat" class="chox-ttnum"  name="storageRecoveryVat" value="<s:property value="storageRecoveryVat" />"   onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="storageRecoveryVat!=storageRecoveryVatOriginal&&(storageRecoveryVatOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="storageRecoveryVatOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Storage Recovery Gross<span class="mandatory">*</span></label>
                                                <input type="text" id="storageRecoveryGross" class="chox-ttnum"  name="storageRecoveryGross" value="<s:property value="storageRecoveryGross" />"   onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="storageRecoveryGross!=storageRecoveryGrossOriginal&&(storageRecoveryGrossOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="storageRecoveryGrossOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Total Net<span class="mandatory">*</span></label>
                                                <input type="text" id="totalNet" class="chox-ttnum"  name="totalNet" value="<s:property value="totalNet" />"   onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="totalNet!=totalNetOriginal&&(totalNetOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="totalNetOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Total Vat<span class="mandatory">*</span></label>
                                                <input type="text" id="totalVat" class="chox-ttnum"  name="totalVat" value="<s:property value="totalVat" />"   onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="totalVat!=totalVatOriginal&&(totalVatOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="totalVatOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Total Gross<span class="mandatory">*</span></label>
                                                <input type="text" id="totalGross" class="chox-ttnum"  name="totalGross" value="<s:property value="totalGross" />"   onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="totalGross!=totalGrossOriginal&&(totalGrossOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="totalGrossOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Claims Handling Invoice Amount<span class="mandatory">*</span></label>
                                                <input type="text" id="claimsHandlingInvoiceAmount" class="chox-ttnum"  name="claimsHandlingInvoiceAmount" value="<s:property value="claimsHandlingInvoiceAmount" />"   onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="claimsHandlingInvoiceAmount!=claimsHandlingInvoiceAmountOriginal&&(claimsHandlingInvoiceAmountOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="claimsHandlingInvoiceAmountOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Deduction For Claims Handling Fee<span class="mandatory">*</span></label>
                                                <input type="text" id="deductionForClaimsHandlingFee" class="chox-ttnum"  name="deductionForClaimsHandlingFee" value="<s:property value="deductionForClaimsHandlingFee" />"   onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="deductionForClaimsHandlingFee!=deductionForClaimsHandlingFeeOriginal&&(deductionForClaimsHandlingFeeOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="deductionForClaimsHandlingFeeOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">CHO Discount<span class="mandatory">*</span></label>
                                                <s:if test="discount<0">
                                                    <input type="text" id="discount" class="chox-ttnum" style="color:red; font-weight:bold;" name="discount" value="<s:property value="discount" />"   onkeyup="extractNumber(this,2,true);" /> 
                                                </s:if>
                                                <s:else>
                                                    <input type="text" id="discount" class="chox-ttnum" name="discount" value="<s:property value="discount" />"   onkeyup="extractNumber(this,2,true);" />
                                                </s:else>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="discount!=discountOriginal&&(discountOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="discountOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Hire Penalty Percentage</label>
                                                <input type="text" class="chox-ttnum"  name="hirePenaltyPercentage" disabled="true" value="<s:property value="hirePenaltyPercentage" />"  />
                                            </div>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Hire Penalty Charge</label>
                                                <input type="text" id="hirePenaltyCharge" class="chox-ttnum"  name="hirePenaltyCharge" disabled="true" value="<s:property value="hirePenaltyCharge" />"  onkeyup="extractNumber(this,2,true);"  />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="hirePenaltyCharge!=hirePenaltyChargeOriginal&&(hirePenaltyChargeOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="hirePenaltyChargeOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>

                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Repair Penalty Percentage</label>
                                                <input type="text" class="chox-ttnum"  name="repairPenaltyPercentage" disabled="true" value="<s:property value="repairPenaltyPercentage" />"  />
                                             </div>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Repair Penalty Charge</label>
                                                <input type="text" id="repairPenaltyCharge" class="chox-ttnum"  name="repairPenaltyCharge" disabled="true" value="<s:property value="repairPenaltyCharge" />"   onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="repairPenaltyCharge!=repairPenaltyChargeOriginal&&(repairPenaltyChargeOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="repairPenaltyChargeOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Total Penalty Charge</label>
                                                <input type="text" id="totalPenaltyCharge" class="chox-ttnum"  name="totalPenaltyCharge" disabled="true" value="<s:property value="totalPenaltyCharge" />"   onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="totalPenaltyCharge!=totalPenaltyChargeOriginal&&(totalPenaltyChargeOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="totalPenaltyChargeOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">
                                                    Insurer Discount</label>
                                                <s:if test="insurerDiscount<0">
                                                    <input type="text" id="insurer_discount" class="chox-ttnum" style="color:red; font-weight:bold;" name="insurerDiscount" readonly="true" value="<s:property value="insurerDiscount" />" />
                                                </s:if>
                                                <s:else>
                                                    <input type="text" id="insurer_discount" class="chox-ttnum" name="insurerDiscount" readonly="true" value="<s:property value="insurerDiscount" />" />
                                                </s:else>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="insurerDiscount!=insurerDiscountOriginal&&(insurerDiscountOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="insurerDiscountOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <input type="hidden" id="totalGrossInsurerDiscountAmountId" name="totalGrossInsurerDiscount" value="<s:property value="totalGrossInsurerDiscount" />" />
                                    <input type="hidden" id="repairGrossInsurerDiscountAmountId" name="repairGrossInsurerDiscount" value="<s:property value="repairGrossInsurerDiscount" />" />
                                    <input type="hidden" id="hireGrossInsurerDiscountAmountId" name="hireGrossInsurerDiscount" value="<s:property value="hireGrossInsurerDiscount" />" />
                                    
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Full Total Requested<span class="mandatory">*</span></label>
                                                <input type="text" id="fullTotalToPay" class="chox-ttnum"  name="fullTotalToPay" value="<s:property value="fullTotalToPay" />"   onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="fullTotalToPay!=fullTotalToPayOriginal&&(fullTotalToPayOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="fullTotalToPayOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label-big">Total To Pay <span class="mandatory">*</span></label>
                                                <input type="text" id="totalToPay" class="chox-ttnum"   name="totalToPay" disabled="true" value="<s:property value="totalToPay" />"   onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="totalToPay!=totalToPayOriginal&&(totalToPayOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="totalToPayOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <s:if test="interimPaymentReceivedFullAndFinal">
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Interim Payment</label>
                                                    <input type="text" class="chox-ttnum"  disabled="true" name="interimPayment" value="<s:property value="interimPaymentMade" />"  onkeyup="extractNumber(this,2,true);" /><label class="std-data-ro-red-invrecalc"> (Received & Accepted as Full & Final)</label>
                                                    <label class="std-label-ro-small">Note that the interim payment is NOT deducted from the 'Total To Pay'</label><br/>
                                                </div>
                                            </s:if>
                                            <s:elseif test="outstandingInterimPayment > 0 && interimPaymentReceived > 0">
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Interim Payment</label>
                                                    <input type="text" class="chox-ttnum"  disabled="true" name="interimPayment" value="<s:property value="interimPaymentMade" />"  onkeyup="extractNumber(this,2,true);" />
                                                    <label class="std-data-ro-red-invrecalc">(Only £<s:property value="interimPaymentReceived" /> Received)</label><br/>
                                                    <label class="std-label-ro-small">Note that the interim payment is NOT deducted from the 'Total To Pay'</label><br/>
                                                </div>
                                            </s:elseif>
                                            <s:elseif test="interimPaymentReceived > 0">
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Interim Payment</label>
                                                    <input type="text" class="chox-ttnum"  disabled="true" name="interimPayment" value="<s:property value="interimPaymentMade" />"  onkeyup="extractNumber(this,2,true);" />
                                                    <label class="std-data-ro-red-invrecalc">(Received)</label><br/>
                                                    <label class="std-label-ro-small">Note that the interim payment is NOT deducted from the 'Total To Pay'</label><br/>
                                                </div>
                                            </s:elseif>
                                            <s:elseif test="interimPaymentMade > 0">
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Interim Payment</label>
                                                    <input type="text" class="chox-ttnum"  disabled="true" name="interimPayment" value="<s:property value="interimPaymentMade" />"  onkeyup="extractNumber(this,2,true);" />
						    <label class="std-data-ro-red-invrecalc">(Not Yet Received)</label><br/>
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
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Excess Collected From Policyholder<span class="mandatory">*</span></label>
                                                <input type="text" class="chox-ttnum"  name="excessAmountCollected" value="<s:property value="excessAmountCollected" />"  onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="excessAmountCollected!=excessAmountCollectedOriginal&&(excessAmountCollectedOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="excessAmountCollectedOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">VAT Collected From Policyholder<span class="mandatory">*</span></label>
                                                <input type="text" class="chox-ttnum"  name="vatAmountCollected" value="<s:property value="vatAmountCollected" />"  onkeyup="extractNumber(this,2,true);" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="chox-form-item" >
                                                <s:if test="vatAmountCollected!=vatAmountCollectedOriginal&&(vatAmountCollectedOriginal!=null)">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:property value="vatAmountCollectedOriginal" />)</label>
                                                </s:if>
                                            </div>
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
                                            <div class="chox-form-item">
                                                <s:if test="CanShowOriginalInvoicedDate">
                                                    <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                    <label class="chox-ttnum-smalll">(<s:date format="dd/MM/yyyy" name="dateInvoicedOriginal" />)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Invoice Uploaded Date</label>
                                                <label class="std-data-ro"><s:date name="invoiceCreatedDate" format="dd MMM yyyy" /></label>
                                                <s:if test="invoicedDays > 0 && !penaltyChargeDateModified">
                                                    <label class="std-data-ro"> (<s:property value="invoicedDays" /> days)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                        <td></td>
                                    </tr>
                                <s:if test="penaltyChargeDateModified">
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label">Penalty Charge Start Date</label>
                                                <label class="std-data-ro"><s:date name="penaltyChargeDate" format="dd MMM yyyy" /></label>
                                                <s:if test="invoicedDays > 0">
                                                    <label class="std-data-ro"> (<s:property value="invoicedDays" /> days)</label>
                                                </s:if>
                                            </div>
                                        </td>
                                        <td></td>
                                    </tr>
                                </s:if>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label1">Time Invoice Has Been<br/>With CHO For Review</label>
                                                <label class="std-data-ro"><s:property value="daysWithCHOForReview" /></label>
                                            </div>
                                        </td>
                                        <td><label><label></label></label></td>
                                    </tr>
                                    <tr>
                                        <td><div class="chox-form-item">&nbsp;</div></td>
                                        <td></td>
                                    </tr>
                                    
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label1">Time Invoice Has Been<br/>With Insurer For Review</label>
                                                <label class="std-data-ro"><s:property value="daysWithInsurerForReview" /></label>
                                            </div>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="chox-form-item">&nbsp;</div>
                                            <div class="chox-form-item">
                                                <label class="chox-form-std-label1">Time Claim Has Been<br/>Awaiting Liability Resolution</label>
                                                <label class="std-data-ro"><s:property value="daysAwaitingLiabilityResolution" /></label>
                                            </div>
                                        </td>
                                        <td></td>
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
                            <div class="inv-form-container" id="hireVehicleDetailWId">
                                <s:if test="!tpiClaim">
                                    <table>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Manufacturer<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttxt" id="HVDManufacturer" name="vehicleManufacturer" value="<s:property value="vehicleManufacturer" />" />
                                                </div>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Model<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttxt" id="HVDModel" name="vehicleModel" value="<s:property value="vehicleModel" />" />
                                                </div>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Registration<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttxt" id="HVDRegistration"  name="vehicleRegistration" value="<s:property value="vehicleRegistration" />"/>
                                                </div>
                                            </td>
                                            <td></td>
                                        </tr>

                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="VehicleClass">
                                                    <label class="chox-form-std-label">Replacement Vehicle Class<span class="mandatory">*</span></label>
                                                    <div id="vcInvDetailSelectionHolder"></div>
                                                </div>
                                            </td>

                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="VehicleClassName!=VehicleClassNameOriginal&&(VehicleClassNameOriginal!=null)&&(VehicleClassNameOriginal!=\"UNATTACHED\")">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">(<s:property value="VehicleClassNameOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Hire Start (Date)<span class="mandatory">*</span></label>
                                                    <span id="rentalStartPH"></span>
                                                </div>

                                            </td>

                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="canShowOriginalStartDate">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">(<s:date format="dd/MM/yyyy" name="rentalStartOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Hire Start (Time)<span class="mandatory">*</span></label>
                                                    <span id="rentalStartTimePH"></span>
                                                </div>
                                            </td>

                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="rentalStartTime!=rentalStartTimeOriginal&&(rentalStartTimeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">(<s:property value="RentalStartTimeDisplayFormat" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Hire End (Date)<span class="mandatory">*</span></label>
                                                    <span id="rentalEndPH"></span>
                                                </div>
                                            </td>

                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="canShowOriginalEndDate">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">(<s:date format="dd/MM/yyyy" name="rentalEndOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>

                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Hire End (Time)<span class="mandatory">*</span></label>
                                                    <span id="rentalEndTimePH"></span>
                                                </div>
                                            </td>

                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="rentalEndTime!=rentalEndTimeOriginal&&(rentalEndTimeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">(<s:property value="RentalEndTimeDisplayFormat" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>

                                        </tr>

                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Reason For Collection</label>
                                                    <input type="text" class="chox-ttxt" id="HVDReasonForCollection" name="collectionReason" value="<s:property value="collectionReason" />" />
                                                </div>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">No. Days Hire<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" id="HVDNNumberOfDaysHire" name="days" value="<s:property value="days" />" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="days!=daysOriginal&&(daysOriginal!=null)">
                                                        <label class="chox-ttnum-smalll">(<s:property value="daysOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>

                                </s:if>
                                <s:else>
                                    
                                    <table>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Courtesy Car Provided?</label>
                                                    <s:checkbox name="courtesyCarProvided" />
                                                </div>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Manufacturer</label>
                                                    <input type="text" class="chox-ttxt" id="HVDManufacturer" name="vehicleManufacturer" value="<s:property value="vehicleManufacturer" />" />
                                                </div>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Model</label>
                                                    <input type="text" class="chox-ttxt" id="HVDModel" name="vehicleModel" value="<s:property value="vehicleModel" />" />
                                                </div>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Registration</label>
                                                    <input type="text" class="chox-ttxt" id="HVDRegistration"  name="vehicleRegistration" value="<s:property value="vehicleRegistration" />"/>
                                                </div>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>

                                                <div class="chox-form-item" id="VehicleClass">
                                                    <label class="chox-form-std-label">Replacement Vehicle Class</label>
                                                    <div id="vcInvDetailSelectionHolder"></div>
                                                </div>
                                            </td>

                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="VehicleClassName!=VehicleClassNameOriginal&&(VehicleClassNameOriginal!=null)&&(VehicleClassNameOriginal!=\"UNATTACHED\")">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">(<s:property value="VehicleClassNameOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>

                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Hire Start (Date)</label>
                                                    <span id="rentalStartPH"></span>
                                                </div>
                                            </td>

                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="canShowOriginalStartDate">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">(<s:date format="dd/MM/yyyy" name="rentalStartOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>

                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Hire Start (Time)</label>
                                                    <span id="rentalStartTimePH"></span>
                                                </div>
                                            </td>

                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="rentalStartTime!=rentalStartTimeOriginal&&(rentalStartTimeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">(<s:property value="RentalStartTimeDisplayFormat" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Hire End (Date)</label>
                                                    <span id="rentalEndPH"></span>
                                                </div>
                                            </td>

                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="canShowOriginalEndDate">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">(<s:date format="dd/MM/yyyy" name="rentalEndOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Hire End (Time)</label>
                                                    <span id="rentalEndTimePH"></span>
                                                </div>
                                            </td>

                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="rentalEndTime!=rentalEndTimeOriginal&&(rentalEndTimeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">(<s:property value="RentalEndTimeDisplayFormat" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Reason For Collection</label>
                                                    <input type="text" class="chox-ttxt" id="HVDReasonForCollection" name="collectionReason" value="<s:property value="collectionReason" />" />
                                                </div>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">

                                                    <label class="chox-form-std-label">No. Days Hire</label>
                                                    <input type="text" class="chox-ttnum" id="HVDNNumberOfDaysHire" name="days" value="<s:property value="days" />" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="days!=daysOriginal&&(daysOriginal!=null)">
                                                        <label class="chox-ttnum-smalll">(<s:property value="daysOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>


                                </s:else>


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
                                <div class="inv-form-container" id="extrasWId">

                                    <table>
<s:if test="isCollaborationProtocolClaim">
                                        <tr>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <label class="chox-form-std-label">Collaboration Protocol Fee<span class="mandatory">*</span></label>
                                                    <input id="collaborationFee" type="text" class="chox-ttnum" name="collaborationFee" value="<s:property value="collaborationFee"/>"   onkeyup="extractNumber(this,2,true);" />&nbsp;
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<s:if test="collaborationFee!=collaborationFeeOriginal&&(collaborationFeeOriginal!=null)">(<s:property value="collaborationFeeOriginal" />)<script type="text/javascript" language="JavaScript">showNoteMessage();</script></s:if></label>&nbsp;
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <label class="chox-form-std-label">Collaboration Protocol Quantity<span class="mandatory">*</span></label>
                                                    <input id="collaborationQty" type="text" class="chox-ttnum" name="collaborationQty" value="<s:property value="collaborationQty"/>"   onkeyup="extractNumber(this,2,true);" />&nbsp;
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<s:if test="collaborationQty!=collaborationQtyOriginal&&(collaborationQtyOriginal!=null)">(<s:property value="collaborationQtyOriginal" />)<script type="text/javascript" language="JavaScript">showNoteMessage();</script></s:if></label>&nbsp;
                                                </div>
                                            </td>
                                        </tr>
</s:if>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="isSubscriberClaim">
                                                        <label class="chox-form-std-label">Acquisition Fee<span class="mandatory">*</span></label>
                                                    </s:if>
                                                    <s:else>
                                                        <label class="chox-form-std-label">Miscellaneous Costs<span class="mandatory">*</span></label>
                                                    </s:else>
                                                    <input id="cdwFee" type="text" class="chox-ttnum" name="miscellaneousFee" value="<s:property value="miscellaneousFee"/>"   onkeyup="extractNumber(this,2,true);" />&nbsp;
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<s:if test="miscellaneousFee!=miscellaneousFeeOriginal&&(miscellaneousFeeOriginal!=null)">(<s:property value="miscellaneousFeeOriginal" />)<script type="text/javascript" language="JavaScript">showNoteMessage();</script></s:if></label>&nbsp;
                                                </div>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>
                                                <div class="chox-form-item" >
                                                    <label class="chox-form-std-label">Automatic Fee<span class="mandatory">*</span></label>
                                                    <input id="automaticFee" type="text" class="chox-ttnum" name="automaticFee" value="<s:property value="automaticFee" />"   onkeyup="extractNumber(this,2,true);" />
                                                </div>
                                            </td>
                                            
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="automaticFee!=automaticFeeOriginal&&(automaticFeeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="automaticFeeOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label" id="automaticQty">Automatic Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" id="invoiceRecalculateautomaticQtyId"  name="automaticQty" value="<s:property value="automaticQty" />"/>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="automaticQty!=automaticQtyOriginal&&(automaticQtyOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="automaticQtyOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" >
                                                    <label class="chox-form-std-label">Additional Driver Fee<span class="mandatory">*</span></label>
                                                    <input id="additionalDriverFee" type="text" class="chox-ttnum" name="additionalDriverFee" value="<s:property value="additionalDriverFee" />"  onkeyup="extractNumber(this,2,true);"  />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="additionalDriverFee!=additionalDriverFeeOriginal&&(additionalDriverFeeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="additionalDriverFeeOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="additionalDriverQty">
                                                    <label class="chox-form-std-label">Additional Driver Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" id="invoiceRecalculateadditionalDriverQtyId"  name="additionalDriverQty" value="<s:property value="additionalDriverQty" />"/>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="additionalDriverQty!=additionalDriverQtyOriginal&&(additionalDriverQtyOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="additionalDriverQtyOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Sat Nav Fee<span class="mandatory">*</span></label>
                                                    <input id="satNavFee" type="text" class="chox-ttnum" id="invoiceRecalculatesatNavFeeId"  name="satNavFee" value="<s:property value="satNavFee" />"   onkeyup="extractNumber(this,2,true);" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="satNavFee!=satNavFeeOriginal&&(satNavFeeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="satNavFeeOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="satNavQty">
                                                    <label class="chox-form-std-label">Sat Nav Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" id="invoiceRecalculatesatNavQtyId" name="satNavQty" value="<s:property value="satNavQty" />"/>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="satNavQty!=satNavQtyOriginal&&(satNavQtyOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="satNavQtyOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Estate Fee<span class="mandatory">*</span></label>
                                                    <input id="estateFee" type="text" class="chox-ttnum" name="estateFee" value="<s:property value="estateFee" />"   onkeyup="extractNumber(this,2,true);" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="estateFee!=estateFeeOriginal&&(estateFeeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="estateFeeOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="estateQty">
                                                    <label class="chox-form-std-label">Estate Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" id="invoiceRecalculateestateQtyId" name="estateQty" value="<s:property value="estateQty" />" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="estateQty!=estateQtyOriginal&&(estateQtyOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="estateQtyOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Baby Seat Fee<span class="mandatory">*</span></label>
                                                    <input id="babySeatFee" type="text" class="chox-ttnum" name="babySeatFee" value="<s:property value="babySeatFee" />"  onkeyup="extractNumber(this,2,true);"  />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="babySeatFee!=babySeatFeeOriginal&&(babySeatFeeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="babySeatFeeOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="babySeatQty">
                                                    <label class="chox-form-std-label">Baby Seat Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" id="invoiceRecalculatebabySeatQtyId" name="babySeatQty" value="<s:property value="babySeatQty" />"/>&nbsp;&nbsp
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="babySeatQty!=babySeatQtyOriginal&&(babySeatQtyOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="babySeatQtyOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Tow Bars Fee<span class="mandatory">*</span></label>
                                                    <input id="towBarFee" type="text" class="chox-ttnum" name="towBarsFee" value="<s:property value="towBarsFee" />"  onkeyup="extractNumber(this,2,true);"  />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="towBarsFee!=towBarsFeeOriginal&&(towBarsFeeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="towBarsFeeOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="towBarQty">
                                                    <label class="chox-form-std-label">Tow Bars Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" id="invoiceRecalculatetowBarsQtyId" name="towBarsQty" value="<s:property value="towBarsQty" />"/>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="towBarsQty!=towBarsQtyOriginal&&(towBarsQtyOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="towBarsQtyOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Non-standard Risk Ins. Premium Fee<span class="mandatory">*</span></label>
                                                    <input id="nonStandPremiumFee" type="text" class="chox-ttnum" name="nonStandardInsurancePremiumFee" value="<s:property value="nonStandardInsurancePremiumFee" />"   onkeyup="extractNumber(this,2,true);" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="nonStandardInsurancePremiumFee!=nonStandardInsurancePremiumFeeOriginal&&(nonStandardInsurancePremiumFeeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="nonStandardInsurancePremiumFeeOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="nonStandPremiumQty">
                                                    <label class="chox-form-std-label">Non-standard Risk Ins. Premium Qty<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" id="invoiceRecalculatenonStandardInsurancePremiumQtyId" name="nonStandardInsurancePremiumQty" value="<s:property value="nonStandardInsurancePremiumQty" />"/>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="nonStandardInsurancePremiumQty!=nonStandardInsurancePremiumQtyOriginal&&(nonStandardInsurancePremiumQtyOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="nonStandardInsurancePremiumQtyOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Cover Note Required For<br/>Customer's Own Insurance Policy?</label>
                                                    <s:checkbox name="coverNoteRequired" />
                                                </div>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                               <div class="chox-form-item"><label class="chox-form-std-label">&nbsp;</label></div> 
                                            </td>
                                            <td></td>
                                        </tr>
                                        
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Admin Fee<span class="mandatory">*</span></label>
                                                    <input id="adminFee" type="text" class="chox-ttnum" name="adminFee" value="<s:property value="adminFee" />"   onkeyup="extractNumber(this,2,true);" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="adminFee!=adminFeeOriginal&&(adminFeeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="adminFeeOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="adminQty">
                                                    <label class="chox-form-std-label">Admin Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" id="invoiceRecalculateadminQtyId" name="adminQty" value="<s:property value="adminQty" />"/>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="adminQty!=adminQtyOriginal&&(adminQtyOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="adminQtyOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Roof Rack Fee<span class="mandatory">*</span></label>
                                                    <input id="roofRackFee" type="text" class="chox-ttnum" name="roofRackFee" value="<s:property value="roofRackFee" />"   onkeyup="extractNumber(this,2,true);" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="roofRackFee!=roofRackFeeOriginal&&(roofRackFeeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="roofRackFeeOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="roofRackQty">
                                                    <label class="chox-form-std-label">Roof Rack Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" id="invoiceRecalculateroofRackQtyId"  name="roofRackQty" value="<s:property value="roofRackQty" />"/>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >  
                                                    <s:if test="roofRackQty!=roofRackQtyOriginal&&(roofRackQtyOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="roofRackQtyOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Dual Control Fee<span class="mandatory">*</span></label>
                                                    <input id="dualCtrlFee" type="text" class="chox-ttnum" name="dualControlFee" value="<s:property value="dualControlFee" />"  onkeyup="extractNumber(this,2,true);"  />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="dualControlFee!=dualControlFeeOriginal&&(dualControlFeeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="dualControlFeeOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label" id="dualCtrlQty">Dual Control Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum" id="invoiceRecalculatedualControlQtyId" name="dualControlQty" value="<s:property value="dualControlQty" />"/>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="dualControlQty!=dualControlQtyOriginal&&(dualControlQtyOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="dualControlQtyOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Delivery Collection Fee<span class="mandatory">*</span></label>
                                                    <input id="deliveryCollectionFee" type="text" class="chox-ttnum" name="deliveryCollectionFee" value="<s:property value="deliveryCollectionFee" />"   onkeyup="extractNumber(this,2,true);" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="deliveryCollectionFee!=deliveryCollectionFeeOriginal&&(deliveryCollectionFeeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="deliveryCollectionFeeOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item" id="deliveryCollectionQty">
                                                    <label class="chox-form-std-label">Delivery Collection Fee Quantity<span class="mandatory">*</span></label>
                                                    <input type="text" class="chox-ttnum"id="invoiceRecalculatedeliveryCollectionQtyId"  name="deliveryCollectionQty" value="<s:property value="deliveryCollectionQty" />"/>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="deliveryCollectionQty!=deliveryCollectionQtyOriginal&&(deliveryCollectionQtyOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="deliveryCollectionQtyOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </fieldset>
                        </div>
                    </div>
                <s:if test="isSubscriberClaim">
                    <div>
                        <div id="formRepairExtras" class="XXentity-form">
                            <fieldset class="x-fieldset  partial">
                                <legend>Repair Extras</legend>

                                <div class="inv-form-container" id="formRepairExtrasWId">
                                    <table>
                                        <s:if test="!isInsurer">
                                            <tr>
                                                <div class="status-info-recalculation"><span class="std-label-ro-small1-bold">N.B.</span> Please note that the below figures are not used as part of <br/>the ’Re-Calculate’ function and serve the purposes of information only.
                                                </div> 
                                            </tr>
                                        </s:if>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Repair Admin Fee<span class="mandatory">*</span></label>
                                                    <input id="deliveryCollectionFee" type="text" class="chox-ttnum" name="repairAdminFee" value="<s:property value="repairAdminFee" />"   onkeyup="extractNumber(this,2,true);" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item"  >
                                                    <s:if test="repairAdminFee!=repairAdminFeeOriginal&&(repairAdminFeeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="repairAdminFeeOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr> 
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Repair Acquisition Fee<span class="mandatory">*</span></label>
                                                    <input id="deliveryCollectionFee" type="text" class="chox-ttnum" name="repairAcquisitionFee" value="<s:property value="repairAcquisitionFee" />"   onkeyup="extractNumber(this,2,true);" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="repairAcquisitionFee!=repairAcquisitionFeeOriginal&&(repairAcquisitionFeeOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="repairAcquisitionFeeOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Repair Parts<span class="mandatory">*</span></label>
                                                    <input id="deliveryCollectionFee" type="text" class="chox-ttnum" name="repairParts" value="<s:property value="repairParts" />"   onkeyup="extractNumber(this,2,true);" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="repairParts!=repairPartsOriginal&&(repairPartsOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="repairPartsOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Repair Labour<span class="mandatory">*</span></label>
                                                    <input id="deliveryCollectionFee" type="text" class="chox-ttnum" name="repairLabour" value="<s:property value="repairLabour" />"   onkeyup="extractNumber(this,2,true);" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="repairLabour!=repairLabourOriginal&&(repairLabourOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="repairLabourOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Repair Materials<span class="mandatory">*</span></label>
                                                    <input id="deliveryCollectionFee" type="text" class="chox-ttnum" name="repairMaterials" value="<s:property value="repairMaterials" />"   onkeyup="extractNumber(this,2,true);" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="repairMaterials!=repairMaterialsOriginal&&(repairMaterialsOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="repairMaterialsOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="chox-form-item">
                                                    <label class="chox-form-std-label">Repair Specialist<span class="mandatory">*</span></label>
                                                    <input id="deliveryCollectionFee" type="text" class="chox-ttnum" name="repairSpecialist" value="<s:property value="repairSpecialist" />"   onkeyup="extractNumber(this,2,true);" />
                                                </div>
                                            </td>
                                            <td>
                                                <div class="chox-form-item">
                                                    <s:if test="repairSpecialist!=repairSpecialistOriginal&&(repairSpecialistOriginal!=null)">
                                                        <script type="text/javascript" language="JavaScript">showNoteMessage();</script>
                                                        <label class="chox-ttnum-smalll">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(<s:property value="repairSpecialistOriginal" />)</label>
                                                    </s:if>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </fieldset>
                        </div>
                    </div>
                </s:if>
                    <div>
                        <div id="formEngRptAction" class="XXentity-form">

                            <fieldset class="x-fieldset  partial">
                                <legend>Engineer Report</legend>
                                <div class="inv-form-container" id="engineerReportWId">
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Estimated Labour Amount</label>
                                        <input type="text" class="chox-tnum" id="invoiceRecalculatelabourAmountId"  name="labourAmount" value="<s:property value="labourAmount" />"  onkeyup="extractNumber(this,2,true);" />
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Estimated Total Repair Amount</label>
                                        <input type="text" class="chox-tnum" id="invoiceRecalculatetotalAmountId" name="totalAmount" value="<s:property value="totalAmount" />"  onkeyup="extractNumber(this,2,true);" />
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Estimated Days Under Repair</label>
                                        <input type="text" class="chox-tnum" id="invoiceRecalculateestimatedDaysId" name="estimatedDays" value="<s:property value="estimatedDays" />"/>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Usable?</label><s:checkbox name="isUsable" />
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Name</label>
                                        <input type="text" class="chox-ttxt" id="invoiceRecalculatenameId"  name="name" value="<s:property value="name" />"/>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Company</label>
                                        <input type="text" class="chox-ttxt" id="invoiceRecalculatecompanyId" name="company" value="<s:property value="company" />"/>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Engineer Address 1</label>
                                        <input type="text" class="chox-ttxt" id="invoiceRecalculateaddress1Id" name="address1" value="<s:property value="address1" />"/>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Engineer Address 2</label>
                                        <input type="text" class="chox-ttxt" id="invoiceRecalculateaddress2Id" name="address2" value="<s:property value="address2" />"/>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Engineer Address 3</label>
                                        <input type="text" class="chox-ttxt" id="invoiceRecalculateaddress3Id" name="address3" value="<s:property value="address3" />"/>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Engineer Address 4</label>
                                        <input type="text" class="chox-ttxt" id="invoiceRecalculateaddress4Id" name="address4" value="<s:property value="address4" />"/>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Engineer Address 5</label>
                                        <input type="text" class="chox-ttxt" id="invoiceRecalculateaddress5Id" name="address5" value="<s:property value="address5" />"/>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Engineer Postcode</label>
                                        <input type="text" class="chox-ttxt" id="invoiceRecalculatepostcodeId" name="postcode" value="<s:property value="postcode" />"/>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Engineer Telephone</label>
                                        <input type="text" class="chox-ttxt" id="invoiceRecalculatetelephoneId" name="telephone" value="<s:property value="telephone" />"/>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Engineer Email</label>
                                        <input type="text" class="chox-ttxt" id="invoiceRecalculateemailId" name="email" value="<s:property value="email" />"/>
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
            <div id="IDsuccessBox" class="chox-form-submit-result" id="resultMessage"><s:property value="actionResult" /></div>
            <table align="center">
                <tr >
                    <td>
                        <input type="submit" style="width: 88px; margin-right:30px" value="Re-Calculate" id="Re-CalculateAlltheChanges" onclick="return recalculateForm();"/>
                    </td>
                    <td>
                        <input type="submit" value="Save Changes" id="submitAllChanges" onclick="submitForm();"/>
                    </td>
                    <td>
                        <input type="submit" style="width: 88px; margin-left:30px" value="Reset" id="resetAllChanges" class="cancel" onclick="invoiceSubmitAction.value= resetForm();"/>
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
</form>

