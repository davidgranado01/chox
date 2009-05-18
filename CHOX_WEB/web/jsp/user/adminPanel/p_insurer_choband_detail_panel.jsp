<%@ taglib uri="/struts-tags" prefix="s" %>

<link href="<%= request.getContextPath()%>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/>        
<link href="<%= request.getContextPath()%>/css/ext-all.css" rel="stylesheet" type="text/css" media="all"/>

<script language="JavaScript">
        
        // var selectedPanel = "InsurerOrgMgmt";
        var adminCurrentTabIndex;
        var adminTabs;
        
        $(document).ready(function(){
            doFormValidation();
        }); 
        
        function doFormValidation(){

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
                    averageLabourHoursPerHireDay:{required:true, number:true, min:0}
               },
               messages: {
                    name: {required:"You must supply a value for 'Name'" },
                    isMobileDayAllowance: {required:"You must supply a value for 'Mobile Day Allowance'", number:"'Mobile Day Allowance' must be numeric", min:"'Mobile Day Allowance' cannot be less than zero"},
                    isNotMobileDayAllowance: {required:"You must supply a value for '>Not Mobile Day Allowance'", number:"'>Not Mobile Day Allowance' must be numeric", min:"'>Not Mobile Day Allowance' cannot be less than zero"},
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
                    averageLabourHoursPerHireDay: {required:"You must supply a value for 'Average Labour Hours Per Hire Day'", number:"'Average Labour Hours Per Hire Day' must be numeric", min:"'Average Labour Hours Per Hire Day' cannot be less than zero"}
               },
               submitHandler: function(form) {
                    // $(form).ajaxSubmit(op);
               }
            });

            return validateFlag;

        }
        
        function doInsurerChoBandSubmit(){
            
            alert("sadasd -01");

            if(doFormValidation().form()){
            
                // $("#admin_param_panel").block();
                
                var op = { 
                    beforeSubmit:  onBeforeSubmit,
                    success:onSubmitResponseReceived,
                    timeout: 3000,
                    error: onSubmitError
                };
                
                // alert("sadasd -    02");
                // $("#formUpdateInsurerChoBandDetail").ajaxSubmit(op);
                // alert("sadasd -03");
                
           }
        }
        
        function doInsurerChoBandBack(){
            $("#admin_param_panel").load("loadAdminPanel.action?adminPanelName=InsurerOrgMgmt");
        }
        
        function onBeforeSubmit(formData, jqForm, options) { 
        }

        function onSubmitResponseReceived(responseText, statusText){
            
            responseText = responseText.trim();
            var output = "Your changes have been saved.";
            
            if(responseText != "" && responseText != "1" && responseText.substring(0,9) == 'objectId:'){
                
                var newObjectId =  parseInt(responseText.substring(9,responseText.length));
                $("#admin_param_panel").load("updateInsurerDetailPanel.action?objectId=" + newObjectId);
                
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
        
        function setupTabPanels()
        {

           if(adminCurrentTabIndex==null || <s:property value="isNew"/>){
               adminCurrentTabIndex = 0;
           }
           
           adminTabs = new Ext.TabPanel({
           renderTo: 'mainPanel',
           height:630,
           autoScroll :true,
           activeTab: adminCurrentTabIndex,
           items:[
                {contentEl:'insurerChoBandDetailPanelTab', title:'Details',listeners: {activate: handleActivate}},
                {contentEl:'insurerBreMappingPanelTab', title:'Allias', disabled:<s:property value="isNew"/>, listeners: {activate: handleActivate}},
            ]
           });
        }

        Ext.onReady(function(){        
            setupTabPanels();
        }); 

        function handleActivate(tab){
            
            adminCurrentTabIndex = 0;
            
            if(adminTabs)
            {
                adminCurrentTabIndex = adminTabs.items.indexOf(adminTabs.getActiveTab());
            }

        }

</script>

<div id="mainPanel" class="adminTabCss"></div>

<div id="insurerBreMappingPanelTab" class="x-hide-display">BRE MAPPING</div>

<div id="insurerAlliasPanelTab" class="x-hide-display">
    <div class="subAdminTabCss">
        <s:action name="loadAdminPanel" executeResult="true">
            <s:param name="adminPanelName">InsurerAlliasMappingMgmt</s:param>
            <s:param name="selectOrgId"><s:property value="objectId" /></s:param>        
        </s:action> 
    </div>
</div>

<div id="insurerChoBandDetailPanelTab" class="x-hide-display">
    <div class="subAdminTabCss">
    <form id="formUpdateInsurerChoBandDetail" action="user/updateInsurerChoBandDetail.action" class="XXentity-form" onsubmit="return true;">
    <input type="hidden" name="objectId" id="objectId" value='<s:property value="objectId"/>'>
            <div class="form-container">
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Name<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDName" name="name" value="<s:property value="name" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Status</label>
                    <s:checkbox name="isActive" value="isActive" />
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Mobile Day Allowance<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDIsMobileDayAllowance" name="isMobileDayAllowance" value="<s:property value="isMobileDayAllowance" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Not Mobile Day Allowance<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDIsNotMobileDayAllowance" name="isNotMobileDayAllowance" value="<s:property value="isNotMobileDayAllowance" />"/>
                </div> 
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Take Vehicle To Garage Days - Mobile<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDTakeVehicleToGarageDaysMobile" name="takeVehicleToGarageDaysMobile" value="<s:property value="takeVehicleToGarageDaysMobile" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Take Vehicle To Garage Days - Non Mobile<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDTakeVehicleToGarageDaysNonMobile" name="takeVehicleToGarageDaysNonMobile" value="<s:property value="takeVehicleToGarageDaysNonMobile" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Weekend Buffer Days<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDWeekendBufferDays" name="weekendBufferDays" value="<s:property value="weekendBufferDays" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Engineer Inspection Delay Days<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDEngineerInspectionDelayDays" name="engineerInspectionDelayDays" value="<s:property value="engineerInspectionDelayDays" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Offer Made Days<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDOfferMadeDays" name="offerMadeDays" value="<s:property value="offerMadeDays" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Receipt Of Final Statement Cheque Days<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDReceiptOfFinalStatementChequeDays" name="receiptOfFinalStatementChequeDays" value="<s:property value="receiptOfFinalStatementChequeDays" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Inspection Delay Days<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDInspectionDelayDays" name="inspectionDelayDays" value="<s:property value="inspectionDelayDays" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Hire Rate Charge Tolerance<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDHireRateChargeTolerance" name="hireRateChargeTolerance" value="<s:property value="hireRateChargeTolerance" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Hire Net Ceiling<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDHireNetCeiling" name="hireNetCeiling" value="<s:property value="hireNetCeiling" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Hire Day Ceiling<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDHireDayCeiling" name="hireDayCeiling" value="<s:property value="hireDayCeiling" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Max Repair Value<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDMaxRepairValue" name="maxRepairValue" value="<s:property value="maxRepairValue" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Average Labour Rate<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDAverageLabourRate" name="averageLabourRate" value="<s:property value="averageLabourRate" />"/>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Average Labour Hours Per Hire Day<span class="mandatory">*</span></label>
                    <input type="text" class="chox-ttxt" id="CCDAverageLabourHoursPerHireDay" name="averageLabourHoursPerHireDay" value="<s:property value="averageLabourHoursPerHireDay" />"/>
                </div>
                <div class="chox-form-button">
                    <input type="button" value="Save Changes" onclick="javascript: doInsurerChoBandSubmit();"/>
                    <input type="button" value="Cancel" class="cancel" onclick="javascript: doInsurerChoBandBack();" />
                </div>
                <div id="CDInsurerChoBandmessageBox" style="text-align:center"></div>
                <div class="chox-form-submit-result"></div>  
            </div>
    </form>        
    </div>
</div>
