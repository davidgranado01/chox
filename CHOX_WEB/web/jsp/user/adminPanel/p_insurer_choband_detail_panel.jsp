<%@ taglib uri="/struts-tags" prefix="s" %>

<link href="<%= request.getContextPath()%>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/>        
<link href="<%= request.getContextPath()%>/css/ext-all.css" rel="stylesheet" type="text/css" media="all"/>

<script language="JavaScript">
        
        var selectedPanel = "InsurerChoBandMgmt"; 
        var selectOrgId = <s:property value="insurerId" />;
        var objectId = -1;
        
        $(document).ready(function(){
            doFormValidation();
        }); 
        
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
        
            if(doFormValidation().form()){
            
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
        
        function onSubmitResponseReceived(responseText, statusText){
            
            doLoadParameter();
            
            responseText = responseText.trim();
            var output = "Your changes have been saved.";
            
            if(responseText != "" && responseText != "1" && responseText.substring(0,9) == 'objectId:'){
                
                var newObjectId =  parseInt(responseText.substring(9,responseText.length));
                $("#chobandDiv").load("updateInsurerChoBandDetailPanel.action?objectId=" + newObjectId + "&insurerId=" + selectOrgId);
                
            }else{
                
                output = responseText;
                $(".chox-form-submit-result").html(output);
                
            }
            
            $("#admin_param_panel").unblock();
        }

        function onSubmitError(XMLHttpRequest, textStatus, errorThrown) {
            $("#admin_param_panel").unblock();
            alert("Error");  
        }
        
        function doLoadParameter(){
            objectId = $("#objectId").val();
        }

</script>

<div style="height:600px;  overflow:auto;" id="chobandDiv" name="chobandDiv">

    <form id="formUpdateInsurerChoBandDetail" action="user/updateInsurerChoBandDetail.action" class="XXentity-form" onsubmit="return true;">
    <input type="hidden" name="objectId" id="objectId" value='<s:property value="objectId"/>'>
    <input type="hidden" name="insurerId" id="insurerId" value='<s:property value="insurerId"/>'>
    
            <div class="form-container">
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Name<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDName" name="name" value="<s:property value="name" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Mobile Day Allowance<span class="mandatory">*</span></label>
                    <input size="5" maxlength="5" type="text" class="chox-ttxt" id="CCDIsMobileDayAllowance" name="isMobileDayAllowance" value="<s:property value="isMobileDayAllowance" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Not Mobile Day Allowance<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDIsNotMobileDayAllowance" name="isNotMobileDayAllowance" value="<s:property value="isNotMobileDayAllowance" />"/>
                </div> 
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Take Vehicle To Garage Days - Mobile<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDTakeVehicleToGarageDaysMobile" name="takeVehicleToGarageDaysMobile" value="<s:property value="takeVehicleToGarageDaysMobile" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Take Vehicle To Garage Days - Non Mobile<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDTakeVehicleToGarageDaysNonMobile" name="takeVehicleToGarageDaysNonMobile" value="<s:property value="takeVehicleToGarageDaysNonMobile" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Take Vehicle Out Days<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDTakeVehicleOutDays" name="takeVehicleOutDays" value="<s:property value="takeVehicleOutDays" />"/>
                </div>                
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Weekend Buffer Days<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDWeekendBufferDays" name="weekendBufferDays" value="<s:property value="weekendBufferDays" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Engineer Inspection Delay Days<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDEngineerInspectionDelayDays" name="engineerInspectionDelayDays" value="<s:property value="engineerInspectionDelayDays" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Offer Made Days<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDOfferMadeDays" name="offerMadeDays" value="<s:property value="offerMadeDays" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Receipt Of Final Statement Cheque Days<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDReceiptOfFinalStatementChequeDays" name="receiptOfFinalStatementChequeDays" value="<s:property value="receiptOfFinalStatementChequeDays" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Average Labour Rate<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDAverageLabourRate" name="averageLabourRate" value="<s:property value="averageLabourRate" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Average Labour Hours Per Hire Day<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDAverageLabourHoursPerHireDay" name="averageLabourHoursPerHireDay" value="<s:property value="averageLabourHoursPerHireDay" />"/>
                </div>            
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Inspection Delay Days<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDInspectionDelayDays" name="inspectionDelayDays" value="<s:property value="inspectionDelayDays" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Hire Day Ceiling<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDHireDayCeiling" name="hireDayCeiling" value="<s:property value="hireDayCeiling" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Hire Net Ceiling<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDHireNetCeiling" name="hireNetCeiling" value="<s:property value="hireNetCeiling" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Hire Rate Charge Tolerance<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDHireRateChargeTolerance" name="hireRateChargeTolerance" value="<s:property value="hireRateChargeTolerance" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label-longer">Max Repair Value<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDMaxRepairValue" name="maxRepairValue" value="<s:property value="maxRepairValue" />"/>
                </div>
                <input type="hidden" class="chox-ttxt" id="CCDisActive" name="isActive" value="true"/>
                <div class="chox-form-button">
                    <input type="button" value="Save Changes" onclick="javascript: doInsurerChoBandSubmit();"/>
                    <s:if test="!isNew">
                    <input type="button" value="Delete" onclick="javascript: doDeleteChoBand();"/>
                </s:if>
                    <input type="button" value="Cancel" class="cancel" onclick="javascript: doInsurerChoBandBack();" />
                </div>
                <div id="CDInsurerChoBandmessageBox" class="errorBox"></div>
                <div class="chox-form-submit-result"></div>  
            </div>
        </form>
</div>
