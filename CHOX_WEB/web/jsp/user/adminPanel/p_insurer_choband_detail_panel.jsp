<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<link href="<%= request.getContextPath()%>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/>        
<link href="<%= request.getContextPath()%>/css/ext-all.css" rel="stylesheet" type="text/css" media="all"/>

<script language="JavaScript">
        
        var selectedPanel = "InsurerChoBandMgmt"; 
        var selectOrgId = <s:property value="insurerId" />;
        var objectId = -1;
        var isNew = false;
        
        $(document).ready(function(){
            
            if(<s:property value="objectId"/><0){
                isNew = true;
            }
            
            doFormValidation();
            doRefreshCalculation();
            doPlugInTips();
            
        }); 

        function doPlugInTips(){
            new Ext.ToolTip({ target: 'help-averageLabourHoursPerHireDay', html: 'N.B How many hours the garage should work on the car per day'});
            new Ext.ToolTip({ target: 'help-averageLabourRate', html: 'N.B Average amount charged per hour for repair. This is based on an average amount charged for both preferred repairers and all other repairers.'});
            new Ext.ToolTip({ target: 'help-hireDayCeiling', html: 'N.B Maximum allowable hire days.'});
            new Ext.ToolTip({ target: 'help-hireNetCeiling', html: 'N.B Maximum amount allowed to be charged for hire only.'});
            new Ext.ToolTip({ target: 'help-hireRateChargeTolerance', html: 'N.B A figure allowing small deviations to the price charged per day for the hire based on the vehicle class.'});
            new Ext.ToolTip({ target: 'help-maxRepairValue', html: 'N.B Maximum amount allowed to be charged for repair of vehicle.'});
        }

        function doRefreshCalculation(){
            
            // C09 - Take Mobile Vehicle To Garage Variable
            var iCCDTakeVehicleToGarageDaysMobile = $("#CCDTakeVehicleToGarageDaysMobile").val();
            $(".chox-ttxt-readonly-TakeVehicleToGarageDaysMobile").val(iCCDTakeVehicleToGarageDaysMobile);
            
            // C10 - Take Non-Mobile Vehicle To Garage Variable (Days)
            var iCCDTakeVehicleToGarageDaysNonMobile = $("#CCDTakeVehicleToGarageDaysNonMobile").val();
            $(".chox-ttxt-readonly-TakeVehicleToGarageDaysNonMobile").val(iCCDTakeVehicleToGarageDaysNonMobile);
            
            // C11 - Engineer Inspection Delay Variable (Days)
            var iCCDEngineerInspectionDelayDays = $("#CCDEngineerInspectionDelayDays").val();
            $(".chox-ttxt-readonly-EngineerInspectionDelayVariable").val(iCCDEngineerInspectionDelayDays);

            // C12 - Collection of Vehicle from garage Variable (Days)
            var iCCDTakeVehicleOutDays = $("#CCDTakeVehicleOutDays").val();
            $(".chox-ttxt-readonly-CollectionofVehiclefromGarageVariable").val(iCCDTakeVehicleOutDays)
            
            doTtlLossAllowableTtlDuration();
            
            // doRepairDurationRuleforMobileVehicleWithECD();
            doRepairDurationRuleforMobileVehicleWithoutECD();
            // doRepairDurationRuleforNonMobileVehicleWithECD();
            doRepairDurationRuleforNonMobileVehicleWithoutECD();
        }

        function doFormValidation(){
            
            $(".chox-form-submit-result").html("");
            
            var validateFlag = $("#formUpdateInsurerChoBandDetail").validate(
            {
               errorLabelContainer: "#CDInsurerChoBandmessageBox",
               rules: {
                    name:{required:true},
                    isMobileDayAllowance:{required:true, number:true, min:0},
                    isNotMobileDayAllowance:{required:true, number:true, min:0},
                    takeVehicleToGarageDaysMobile:{required:true, number:true, min:0},
                    takeVehicleToGarageDaysNonMobile:{required:true, number:true, min:0},
                    weekendBufferDays:{required:true, number:true, min:0},
                    engineerInspectionDelayDays:{required:true, number:true, min:0},
                    offerMadeDays:{required:true, number:true, min:0},
                    receiptOfFinalStatementChequeDays:{required:true, number:true, min:0},
                    inspectionDelayDays:{required:true, number:true, min:0},
                    hireRateChargeTolerance:{required:true, number:true, min:0},
                    hireNetCeiling:{required:true, number:true, min:0},
                    hireDayCeiling:{required:true, number:true, min:0},
                    maxRepairValue:{required:true, number:true, min:0},
                    averageLabourRate:{required:true, number:true, min:0},
                    averageLabourHoursPerHireDay:{required:true, number:true, min:0},
                    takeVehicleOutDays:{required:true, number:true, min:0}
               },
               messages: {
                    name: {required:"You must supply a value for 'Name'" },
                    isMobileDayAllowance: {required:"You must supply a value for 'Mobile Day Allowance'", number:"'Mobile Day Allowance' must be numeric", min:"'Mobile Day Allowance' cannot be less than zero"},
                    isNotMobileDayAllowance: {required:"You must supply a value for 'Not Mobile Day Allowance'", number:"'Not Mobile Day Allowance' must be numeric", min:"'>Not Mobile Day Allowance' cannot be less than zero"},
                    takeVehicleToGarageDaysMobile: {required:"You must supply a value for 'Take Vehicle To Garage Days - Mobile'", number:"'Take Vehicle To Garage Days - Mobile' must be numeric", min:"'Take Vehicle To Garage Days - Mobile' cannot be less than zero"},
                    takeVehicleToGarageDaysNonMobile: {required:"You must supply a value for 'Take Vehicle To Garage Days - Non Mobile'", number:"'Take Vehicle To Garage Days - Non Mobile' must be numeric", min:"'Take Vehicle To Garage Days - Non Mobile' cannot be less than zero"},
                    weekendBufferDays: {required:"You must supply a value for 'Weekend Buffer Days'", number:"'Weekend Buffer Days' must be numeric", min:"'Weekend Buffer Days' cannot be less than zero"},
                    engineerInspectionDelayDays: {required:"You must supply a value for 'Engineer Inspection Delay Days'", number:"'Engineer Inspection Delay Days' must be numeric", min:"'Engineer Inspection Delay Days' cannot be less than zero"},
                    offerMadeDays: {required:"You must supply a value for 'Offer Made Days'", number:"'Offer Made Days' must be numeric", min:"'Offer Made Days' cannot be less than zero"},
                    receiptOfFinalStatementChequeDays: {required:"You must supply a value for 'Receipt Of Final Statement Cheque Days'", number:"'Receipt Of Final Statement Cheque Days' must be numeric", min:"'Receipt Of Final Statement Cheque Days' cannot be less than zero"},
                    inspectionDelayDays: {required:"You must supply a value for 'Inspection Delay Days'", number:"'Inspection Delay Days' must be numeric", min:"'Inspection Delay Days' cannot be less than zero"},
                    hireRateChargeTolerance: {required:"You must supply a value for 'Hire Rate Charge Tolerance'", number:"'Hire Rate Charge Tolerance' must be numeric", min:"'Hire Rate Charge Tolerance' cannot be less than zero"},
                    hireNetCeiling: {required:"You must supply a value for 'Hire Net Ceiling'", number:"'Hire Net Ceiling' must be numeric", min:"'Hire Net Ceiling' cannot be less than zero"},
                    hireDayCeiling: {required:"You must supply a value for 'Hire Day Ceiling'", number:"'Hire Day Ceiling' must be numeric", min:"'Hire Day Ceiling' cannot be less than zero"},
                    maxRepairValue: {required:"You must supply a value for 'Max Repair Value'", number:"'Max Repair Value' must be numeric", min:"'Max Repair Value' cannot be less than zero"},
                    averageLabourRate: {required:"You must supply a value for 'Average Labour Rate'", number:"'Average Labour Rate' must be numeric", min:"'Average Labour Rate' cannot be less than zero"},
                    averageLabourHoursPerHireDay: {required:"You must supply a value for 'Average Labour Hours Per Hire Day'", number:"'Average Labour Hours Per Hire Day' must be numeric", min:"'Average Labour Hours Per Hire Day' cannot be less than zero"},
                    takeVehicleOutDays: {required:"You must supply a value for 'Take Vehicle Out Days'", number:"'Take Vehicle Out Days' must be numeric", min:"'Take Vehicle Out Days' cannot be less than zero"}
               },
               submitHandler: function(form) {
                    
               }
            });

            return validateFlag;

        }
        
        function doInsurerChoBandSubmit(){
        
            var confirmationMsg = "Are you sure you wish to save the changes made?";
            
            if(isNew){
                confirmationMsg = "Are you sure you wish to add this BRE Band?";
            }        
        
            if(doFormValidation().form()){
            
                if(confirm(confirmationMsg)){

                    $("#admin_param_panel").block();

                    var op = { 
                        beforeSubmit:  onBeforeSubmit,
                        success:onSubmitResponseReceived,
                        timeout: 3000,
                        error: onSubmitError
                    };

                   $("#formUpdateInsurerChoBandDetail").ajaxSubmit(op);

                }
           }
        }
        
        function doInsurerChoBandBack(){
            doLoadParameter();
            $("#chobandDiv").load("loadAdminPanel.action?adminPanelName="+selectedPanel+"&selectOrgId="+selectOrgId);
        }
        
        function onBeforeSubmit(formData, jqForm, options) { 
        }
        
        function doDeleteChoBand(){
            
            doLoadParameter();
            
            if(confirm("Are you sure you want to delete this BRE Band?")){
                
                var apn = $.ajax({
                   url: "doDeleteCreditHireBand.action?objectId="+<s:property value="objectId"/>,
                   success: deleteSuccessfully
                });
            }
        }
        
        function deleteSuccessfully(responseText, statusText){
            
            $("#admin_param_panel").unblock(); 
            
            responseText = responseText.trim();
            var output = responseText.substring(2,responseText.length);
            
            if(responseText != "" && responseText != "1" && responseText.substring(0,2) == 'D:'){                
                doInsurerChoBandBack();
            }else{
                confirm(output);
            }
            
        }

         function onSubmitResponseReceived(responseText, statusText)  {

                doLoadParameter();
                var isError = false;
                response = eval('(' + responseText.trim() + ')');

                if(response)
                {
                    if(response.isValid){

                        if(response.resultType && response.resultType == 'New')
                        {
                            var newObjectId =  parseInt(response.result);
                            $("#chobandDiv").load("loadAdminPanel.action?adminPanelName=InsurerChoBandMgmt&selectOrgId=" + newObjectId);
                        }
                    }
                    else
                    {
                        isError = true;
                        propmtErrors(response.errors);
                    }
                }
                else
                {
                    isError = true;
                    propmtErrorMsg("Unknown Error Encountered, please try again.");
                }

               $("#admin_param_panel").unblock();
                if(!isError){
                    doInsurerChoBandBack();
                }
         }

        function onSubmitError(XMLHttpRequest, textStatus, errorThrown) {
            $("#admin_param_panel").unblock();
            propmtErrorMsg("Error");
        }
        
        function doLoadParameter(){
            objectId = $("#objectId").val();
        }

        function doTtlLossAllowableTtlDuration(){

            var ttl = 0;
            var iReceiptOfFinalStatementChequeDays = $("#CCDReceiptOfFinalStatementChequeDays").val();
            var iOfferMadeDays = $("#CCDOfferMadeDays").val();
            var iCCDInspectionDelayDays = $("#CCDInspectionDelayDays").val();
            
            ttl = parseFloat(iReceiptOfFinalStatementChequeDays) + parseFloat(iOfferMadeDays) + parseFloat(iCCDInspectionDelayDays);
            $("#iTtlLossAllowableTtlDuration").val(ttl);
            
        }
        
/*
        function doRepairDurationRuleforMobileVehicleWithECD(){

            var ttl = 0;
            
            // SET VALUE
            var iCCDWeekendBufferDays = $("#CCDWeekendBufferDays").val();
            var iCCDTakeVehicleOutDays = $("#CCDTakeVehicleOutDays").val();
            var iCCDEngineerInspectionDelayDays = $("#CCDEngineerInspectionDelayDays").val();
            var iCCDTakeVehicleToGarageDaysMobile = $("#CCDTakeVehicleToGarageDaysMobile").val();

            ttl = parseFloat(iCCDWeekendBufferDays) + parseFloat(iCCDTakeVehicleToGarageDaysMobile) + parseFloat(iCCDEngineerInspectionDelayDays) + parseFloat(iCCDTakeVehicleOutDays);
            $("#iTotalAllowableDaysforMobileVehiclewithECD").val(ttl);
        }
*/
        function doRepairDurationRuleforMobileVehicleWithoutECD(){
            
            var iLabourCostTotalDay = 0;
            var ttl = 0;
            var iWeekendBufferDays = 0
            
            var iCCDTakeVehicleOutDays = $("#CCDTakeVehicleOutDays").val();
            var iCCDEngineerInspectionDelayDays = $("#CCDEngineerInspectionDelayDays").val();
            var iCCDTakeVehicleToGarageDaysMobile = $("#CCDTakeVehicleToGarageDaysMobile").val();
            var iCCDIsMobileDayAllowance = $("#CCDIsMobileDayAllowance").val();

            iLabourCostTotalDay = parseFloat(iCCDTakeVehicleOutDays)
                + parseFloat(iCCDEngineerInspectionDelayDays)
                + parseFloat(iCCDTakeVehicleToGarageDaysMobile)
                + parseFloat(iCCDIsMobileDayAllowance);

            iWeekendBufferDays = getWeekendBuffer(iLabourCostTotalDay);

            var ttl = parseFloat(iLabourCostTotalDay) + parseFloat(iWeekendBufferDays);
            $("#iTotalAllowableDaysforMobileVehicleWhereNoECDIsProvidedWoEcd").val(ttl);
            $("#iWeekendBufferDays_mwoecd").val(iWeekendBufferDays);
        }
        
/*
        function doRepairDurationRuleforNonMobileVehicleWithECD(){

            var ttl = 0;
            
            var iCCDWeekendBufferDays = $("#CCDWeekendBufferDays").val();
            var iCCDTakeVehicleToGarageDaysNonMobile = $("#CCDTakeVehicleToGarageDaysNonMobile").val();
            var iCCDEngineerInspectionDelayDays = $("#CCDEngineerInspectionDelayDays").val();
            var iCCDTakeVehicleOutDays = $("#CCDTakeVehicleOutDays").val();
            ttl = parseFloat(iCCDWeekendBufferDays) + parseFloat(iCCDTakeVehicleToGarageDaysNonMobile) + parseFloat(iCCDEngineerInspectionDelayDays) + parseFloat(iCCDTakeVehicleOutDays);
            $("#iTtlAllowableDaysforNonMobileVehicleWithECD").val(ttl);
            
        }
*/

        function doRepairDurationRuleforNonMobileVehicleWithoutECD(){

            var iLabourCostTotalDay = 0;
            var ttl = 0;
            var iWeekendBufferDays = 0

            var iCCDTakeVehicleToGarageDaysNonMobile = $("#CCDTakeVehicleToGarageDaysNonMobile").val();
            var iCCDEngineerInspectionDelayDays = $("#CCDEngineerInspectionDelayDays").val();
            var iCCDTakeVehicleOutDays = $("#CCDTakeVehicleOutDays").val();
            var iCCDIsNotMobileDayAllowance = $("#CCDIsNotMobileDayAllowance").val();

            iLabourCostTotalDay = parseFloat(iCCDTakeVehicleToGarageDaysNonMobile)
                + parseFloat(iCCDEngineerInspectionDelayDays)
                + parseFloat(iCCDTakeVehicleOutDays)
                + parseFloat(iCCDIsNotMobileDayAllowance);

            iWeekendBufferDays = getWeekendBuffer(iLabourCostTotalDay);
            var ttl = parseFloat(iLabourCostTotalDay) + parseFloat(iWeekendBufferDays);
            $("#iTtlAllowableDaysforNonMobileVehicleWoECD").val(ttl);
            $("#iWeekendBufferDays_nmwoecd").val(iWeekendBufferDays);

        }

        function getWeekendBuffer(iLabourCostTotalDay){

            var iWeekendBufferDay = 0;

            if(iLabourCostTotalDay<5){ iWeekendBufferDay = 0;
            }else if(iLabourCostTotalDay>=5 && iLabourCostTotalDay<12){ iWeekendBufferDay = 2;
            }else if(iLabourCostTotalDay>=12 && iLabourCostTotalDay<19){ iWeekendBufferDay = 4;
            }else if(iLabourCostTotalDay>=19 && iLabourCostTotalDay<26){ iWeekendBufferDay = 6;
            }else if(iLabourCostTotalDay>=26 && iLabourCostTotalDay<33){ iWeekendBufferDay = 8;
            }else if(iLabourCostTotalDay>=40 && iLabourCostTotalDay<47){ iWeekendBufferDay = 10;
            }else if(iLabourCostTotalDay>=47 && iLabourCostTotalDay<54){ iWeekendBufferDay = 12;
            }else if(iLabourCostTotalDay>=54 && iLabourCostTotalDay<61){ iWeekendBufferDay = 14;
            }else if(iLabourCostTotalDay>=61 && iLabourCostTotalDay<68){ iWeekendBufferDay = 16;
            }else if(iLabourCostTotalDay>=68 && iLabourCostTotalDay<75){ iWeekendBufferDay = 18;
            }else if(iLabourCostTotalDay>=75 && iLabourCostTotalDay<82){ iWeekendBufferDay = 20;
            }else if(iLabourCostTotalDay>=82 && iLabourCostTotalDay<89){ iWeekendBufferDay = 22;
            }else if(iLabourCostTotalDay>=89 && iLabourCostTotalDay<96){ iWeekendBufferDay = 24;
            }else if(iLabourCostTotalDay>=96 && iLabourCostTotalDay<103){ iWeekendBufferDay = 26;
            }else if(iLabourCostTotalDay>=103 && iLabourCostTotalDay<110){ iWeekendBufferDay = 28;
            }else if(iLabourCostTotalDay>=110 && iLabourCostTotalDay<117){ iWeekendBufferDay = 30;
            }else if(iLabourCostTotalDay>=117 && iLabourCostTotalDay<124){ iWeekendBufferDay = 32;
            }else if(iLabourCostTotalDay>=124 && iLabourCostTotalDay<131){ iWeekendBufferDay = 34;
            }else if(iLabourCostTotalDay>=131 && iLabourCostTotalDay<138){ iWeekendBufferDay = 36;
            }else if(iLabourCostTotalDay>=138 && iLabourCostTotalDay<145){ iWeekendBufferDay = 38;
            }else if(iLabourCostTotalDay>=145 && iLabourCostTotalDay<152){ iWeekendBufferDay = 40;
            }else if(iLabourCostTotalDay>=152 && iLabourCostTotalDay<159){ iWeekendBufferDay = 42;
            }else if(iLabourCostTotalDay>=159 && iLabourCostTotalDay<166){ iWeekendBufferDay = 44;
            }else if(iLabourCostTotalDay>=166 && iLabourCostTotalDay<173){ iWeekendBufferDay = 46;
            }else if(iLabourCostTotalDay>=173 && iLabourCostTotalDay<180){ iWeekendBufferDay = 48;
            }else if(iLabourCostTotalDay>=180 && iLabourCostTotalDay<187){ iWeekendBufferDay = 50;
            }else if(iLabourCostTotalDay>=187 && iLabourCostTotalDay<194){ iWeekendBufferDay = 52;
            }else if(iLabourCostTotalDay>=194 && iLabourCostTotalDay<201){ iWeekendBufferDay = 54;
            }else if(iLabourCostTotalDay>=201 && iLabourCostTotalDay<208){ iWeekendBufferDay = 56;
            }else if(iLabourCostTotalDay>=208 && iLabourCostTotalDay<215){ iWeekendBufferDay = 58;
            }else if(iLabourCostTotalDay>=215 && iLabourCostTotalDay<222){
                iWeekendBufferDay = 60;
            }

            return iWeekendBufferDay;
        }
        
</script>

<div id="chobandDiv" name="chobandDiv">

    <form id="formUpdateInsurerChoBandDetail" action="user/updateInsurerChoBandDetail.action" class="XXentity-form" onsubmit="return true;">
    <input type="hidden" name="objectId" id="objectId" value='<s:property value="objectId"/>'>
    <input type="hidden" name="insurerId" id="insurerId" value='<s:property value="insurerId"/>'>

            <div class="form-container">

                <div class="label-block">
                    <label class="chox-form-std-label-longer">Name<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDName" name="name" value="<s:property value="name" />"/>
                </div>
                <div class="label-block">
                    <label class="chox-form-std-label-longer">Enable Vehicle Class Celling</label>
                    <s:checkbox name="vehicleClassCellingEnable" value="vehicleClassCellingEnable" />
                </div>
                
                <div style="height:500px; overflow:auto; padding-right:10px;" >

                    <div class="chox-form-button">
                    <input type="button" value="Save Changes" onclick="javascript: doInsurerChoBandSubmit();"/>
                        <s:if test="!isNew">
                        <input type="button" value="Delete" onclick="javascript: doDeleteChoBand();"/>
                        </s:if>
                    <input type="button" value="Cancel" class="cancel" onclick="javascript: doInsurerChoBandBack();" />
                    </div>
                    <div id="CDInsurerChoBandmessageBox" class="errorBox"></div>
                    <div class="chox-form-submit-result"></div>
                    
                    <fieldset class="x-fieldset"><legend>Total Loss Duration Rule</legend>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Receipt of Final Settlement Cheque Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDReceiptOfFinalStatementChequeDays" name="receiptOfFinalStatementChequeDays" value="<s:property value="receiptOfFinalStatementChequeDays" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Ttl. Loss Settlement Offer Process Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDOfferMadeDays" name="offerMadeDays" value="<s:property value="offerMadeDays" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Ttl. Loss Engineer Inspection Delay Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDInspectionDelayDays" name="inspectionDelayDays" value="<s:property value="inspectionDelayDays" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer"><b>Ttl. Loss Allowable Total Duration (Days)</b></label>
                            <input type="text" class="chox-ttxt-readonly" readonly="true" value="0" id="iTtlLossAllowableTtlDuration" name="iTtlLossAllowableTtlDuration"/>
                        </div>
                    </fieldset>

                    <fieldset class="x-fieldset"><legend>Labour Cost/hours Rule</legend>
<div class="status-info">
<b>N.B Labour Cost Calculation:</b><br/>
(((Labour Cost/Current Average Labour Rate Per Hour)/Productive Labour Hours in Garage Per Hire Day) + Take Mobile Vehicle To Garage Variable or Take Non-Mobile Vehicle to Garage Variable (Depending on Mobile/Non-Mobile Vehicle) + Engineer Inspection Delay Variable + Collection of Vehicle from Garage Variable + Weekend Buffer (Automatically Calculated for Expected Number of Weekends))
<br/><br/>
<b>Labour Hours Calculation:</b><br/>
((Labour Hours/Productive Labour Hours in Garage Per Hire Day) + Take Mobile Vehicle to Garage or Take Non-Mobile Vehicle to Garage (Depending on Mobile/Non Mobile Vehicle) + Engineer Inspection Delay Variable + Collection of Vehicle from Garage Variable + Weekend Buffer (Automatically Calculated for Expected Number of Weekends))
</div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Take Mobile Vehicle To Garage Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDTakeVehicleToGarageDaysMobile" name="takeVehicleToGarageDaysMobile" value="<s:property value="takeVehicleToGarageDaysMobile" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Take Non-Mobile Vehicle To Garage Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDTakeVehicleToGarageDaysNonMobile" name="takeVehicleToGarageDaysNonMobile" value="<s:property value="takeVehicleToGarageDaysNonMobile" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Engineer Inspection Delay Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDEngineerInspectionDelayDays" name="engineerInspectionDelayDays" value="<s:property value="engineerInspectionDelayDays" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Collection of Vehicle from Garage Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDTakeVehicleOutDays" name="takeVehicleOutDays" value="<s:property value="takeVehicleOutDays" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Current Average Labour Rate Per Hour (£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDAverageLabourRate" name="averageLabourRate" value="<s:property value="averageLabourRate" />" onchange="javascript:doRefreshCalculation();"/><img id="help-averageLabourRate" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt="Help"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Productive Labour hours Per Hire Day (Hours)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDAverageLabourHoursPerHireDay" name="averageLabourHoursPerHireDay" value="<s:property value="averageLabourHoursPerHireDay" />" onchange="javascript:doRefreshCalculation();"/><img id="help-averageLabourHoursPerHireDay" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt="Help"/>
                        </div>
                        
                    </fieldset>

                    <fieldset class="x-fieldset"><legend>Repair Duration Rule for Mobile Vehicle With ECD</legend>
<div class="status-info">
<b>Repair Duration Calculation:</b><br/>
ECD + Take Mobile Vehicle To Garage Variable + Collection of Vehicle from Garage Variable + Weekend Buffer (Automatically Calculated for Expected Number of Weekends)
</div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Take Mobile Vehicle To Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-TakeVehicleToGarageDaysMobile" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Engineer Inspection Delay Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-EngineerInspectionDelayVariable" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Collection of Vehicle from Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-CollectionofVehiclefromGarageVariable" readonly="true"/>
                        </div>
                        <!--
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer"><b>Ttl Allowable Days for Mobile Vehicle With ECD</b></label>
                            <input type="text" class="chox-ttxt-readonly" readonly="true" id="iTotalAllowableDaysforMobileVehiclewithECD" name="iTotalAllowableDaysforMobileVehiclewithECD"/>
                        </div>
                        !-->
                    </fieldset>
                    
                    <fieldset class="x-fieldset"><legend>Repair Duration Rule for Mobile Vehicle Without ECD</legend>
<div class="status-info">
<b>ECD Calculation:</b><br/>
Mobile Vehicle ECD Variable + Take Mobile Vehicle To Garage Variable + Collection of Vehicle from Garage Variable + Weekend Buffer (Automatically Calculated for Expected Number of Weekends)  
<br/><br/>
N.B Where No ECD is provided by the CHO the ECD variable is used, acting as an artificial ECD.
</div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Mobile Vehicle ECD Variable (Days)<span class="mandatory">*</span></label>
                            <input size="5" maxlength="5" type="text" class="chox-ttxt" id="CCDIsMobileDayAllowance" name="isMobileDayAllowance" value="<s:property value="isMobileDayAllowance" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>                        
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Take Mobile Vehicle To Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-TakeVehicleToGarageDaysMobile" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Engineer Inspection Delay Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-EngineerInspectionDelayVariable" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Collection of Vehicle from Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-CollectionofVehiclefromGarageVariable" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Weekend Buffer (Days)</label>
                            <input type="text" class="chox-ttxt-readonly" id="iWeekendBufferDays_mwoecd" name="iWeekendBufferDays_mwoecd" value="4" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer"><b>Ttl Allowable Days for Mobile Vehicle Without ECD</b></label>
                            <input type="text" class="chox-ttxt-readonly" readonly="true" id="iTotalAllowableDaysforMobileVehicleWhereNoECDIsProvidedWoEcd" name="iTotalAllowableDaysforMobileVehicleWhereNoECDIsProvidedWoEcd"/>
                        </div>
                    </fieldset>

                    <fieldset class="x-fieldset"><legend>Repair Duration Rule for Non-Mobile Vehicle with ECD</legend>
<div class="status-info">
<b>Repair Duration Calculation:</b><br/>
ECD + Take Non-Mobile Vehicle To Garage Variable + Collection of Vehicle from Garage Variable + Weekend Buffer (Automatically Calculated for Expected Number of Weekends)
</div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Engineer Inspection Delay Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-EngineerInspectionDelayVariable" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Take Non-Mobile Vehicle To Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-TakeVehicleToGarageDaysNonMobile" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Collection of Vehicle from Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-CollectionofVehiclefromGarageVariable" readonly="true"/>
                        </div>
                        <!--
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer"><b>Ttl Allowable Days for Non-Mobile Vehicle With ECD</b></label>
                            <input type="text" class="chox-ttxt-readonly" readonly="true" id="iTtlAllowableDaysforNonMobileVehicleWithECD" name="iTtlAllowableDaysforNonMobileVehicleWithECD"/>
                        </div>
                        !-->
                    </fieldset>

                    <fieldset class="x-fieldset"><legend>Repair Duration Rule for Non-Mobile Vehicle without ECD</legend>
<div class="status-info">
<b>N.B ECD Calculation:</b><br/>
Non-Mobile Vehicle ECD Variable + Take Non-Mobile Vehicle To Garage Variable + Collection of Vehicle from Garage Variable + Weekend Buffer (Automatically Calculated for Expected Number of Weekends)
<br/><br/>
N.B Where No ECD is provided by the CHO the ECD variable is used, acting as an artificial ECD.
</div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Non-Mobile Vehicle ECD Variable (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDIsNotMobileDayAllowance" name="isNotMobileDayAllowance" value="<s:property value="isNotMobileDayAllowance" />" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                       <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Engineer Inspection Delay Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-EngineerInspectionDelayVariable" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Take Non-Mobile Vehicle To Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-TakeVehicleToGarageDaysNonMobile" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Collection of Vehicle from Garage Variable (Days)</label>
                            <input type="text" class="chox-ttxt-readonly-CollectionofVehiclefromGarageVariable" readonly="true"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Weekend Buffer (Days)</label>
                            <input type="text" class="chox-ttxt-readonly" id="iWeekendBufferDays_nmwoecd" name="iWeekendBufferDays_nmwoecd" value="4" onchange="javascript:doRefreshCalculation();"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer"><b>Ttl Allowable Days for Non-Mobile Vehicle Without ECD</b></label>
                            <input type="text" class="chox-ttxt-readonly" readonly="true" id="iTtlAllowableDaysforNonMobileVehicleWoECD" name="iTtlAllowableDaysforNonMobileVehicleWoECD"/>
                        </div>
                    </fieldset>
                    
                    <fieldset class="x-fieldset"><legend>Hire Tolerances</legend>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Hire Day Ceiling (Days)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDHireDayCeiling" name="hireDayCeiling" value="<s:property value="hireDayCeiling" />" onchange="javascript:doRefreshCalculation();"/><img id="help-hireDayCeiling" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt="Help"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Hire Net Ceiling (£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDHireNetCeiling" name="hireNetCeiling" value="<s:property value="hireNetCeiling" />" onchange="javascript:doRefreshCalculation();"/><img id="help-hireNetCeiling" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt="Help"/>
                        </div>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Hire Rate Charge Per Day Tollerance (£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDHireRateChargeTolerance" name="hireRateChargeTolerance" value="<s:property value="hireRateChargeTolerance" />" onchange="javascript:doRefreshCalculation();"/><img id="help-hireRateChargeTolerance" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt="Help"/>
                        </div>
                    </fieldset>

                    <fieldset class="x-fieldset"><legend>Repair Tolerances</legend>
                        <div class="chox-form-item">
                            <label class="chox-form-std-label-longer">Maximum Repair Net Ceiling (£)<span class="mandatory">*</span></label>
                            <input type="text" class="chox-ttxt" id="CCDMaxRepairValue" name="maxRepairValue" value="<s:property value="maxRepairValue" />" onchange="javascript:doRefreshCalculation();"/><img id="help-maxRepairValue" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt="Help"/>
                        </div>
                    </fieldset>

                    <input type="hidden" class="chox-ttxt" id="CCDisActive" name="isActive" value="true"/>

                </div>
                
            </div>
        </form>
</div>
