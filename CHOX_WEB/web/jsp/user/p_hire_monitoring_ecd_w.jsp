<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
        
    $(document).ready(function(){
        
        var ecdDateDatePicker = new Ext.form.DateField({
            name: 'ecdDate',
            width: 100,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true,
            validationEvent : false,
            value: '<s:date format="dd/MM/yyyy" name="date" />',
            renderTo:'ecdDatePH'
        });
        
        var ecdOptions = { 
            beforeSubmit:  onBeforeSubmit,  // pre-submit callback 
            success:       onAfterEcdSubmit,  // post-submit callback 
            timeout: 3000,
            error: onSubmitError
        };
        
        $("#formAddNewHireMonitoringEcd").validate(
        {
            errorLabelContainer: "#ECDMessageBox",                
            rules: {
                reasonOfDelayId:{required:true},
                ecdDate:{required:true, date:true},
                supportingNote:{required:true}
            },
            messages: {
                reasonOfDelayId: {
                    required:"You must supply a value for 'Reason for Delay'"
                },
                ecdDate: {
                    required:"You must supply a value for 'New ECD'",
                    date:"You must supply valid date format for 'New ECD'"
                },
                supportingNote:{
                    required:"You must supply a value for 'Supporting Note'"
                }
            },
            submitHandler: function(form) {
                $(form).ajaxSubmit(ecdOptions);
            }   
        });
        
        $("#hireMonitorModalClose").click(function(){ $("#hireMonitoringDetails").unblock();});  
    });

    var ecdsLoaded = false;
    function loadEcds(){

        if(!hireMonitoringDetailsDisabled){

            if(!ecdsLoaded)
            {
                ecdDataStore.load(
                {
                    params:
                        {
                        claimId : <s:property value="id" />
                    }
                });   
            }
        }
    }

    function onAfterEcdSubmit(responseText, statusText)  {    
        onSubmitResponseReceived(responseText, statusText);
        loadEcds();
        doResetForm();
        
    }
    
    function doResetForm(){
        document.formAddNewHireMonitoringEcd.reason.value = "";
        document.formAddNewHireMonitoringEcd.supportingNote.value = "";
    }
    
    function doPopulateNote(){
        var reasonOfDelayId = $('#reasonOfDelayId :selected').val();
        $("#ECDSupportingNote").val(getReasonDescription(reasonOfDelayId));
    }
    
    function getReasonDescription(id){
        
        <s:iterator value="reasonOfDelay">
            
            if(id=="<s:property value="id"/>"){
                return "<s:property value="description"/>";
            }

        </s:iterator>
        
    }
    
</script>

<form id="formAddNewHireMonitoringEcd" action="user/addNewHireMonitoringEcd.action" name="formAddNewHireMonitoringEcd" class="XXentity-form">
   <input type="hidden" name="objectId" value='<s:property value="id"/>'>
   <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <fieldset class="x-fieldset">
        <legend>New/Revised ECD</legend>
        <div style="display:none" class="form-container">           
                <s:if test="isECDFormVisible">
                <div class="chox-form-item">
                    <label class="chox-form-std-label" style="width:150px;">New ECD</label>
                    <span id="ecdDatePH"></span>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label" style="width:150px;">Reason for Delay</label>
                    
                    <s:select 
                    name="reasonOfDelayId" 
                    id="reasonOfDelayId"
                    list="reasonOfDelay" 
                    listKey="id" 
                    listValue="name" 
                    headerKey=""
                    headerValue="--- SELECT ---"
                    emptyOption="false" onchange="doPopulateNote();">
                    </s:select>

                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label" style="width:150px;">Supporting Note</label>
                    <textarea class="chox-tta" id="ECDSupportingNote" cols="30" rows="5" name="supportingNote"><s:property value="supportingNote" /></textarea>
                </div>
                
                <div class="chox-form-button">
                    <input type="submit" value="Add New ECD" readonly="<s:property value="isECDFormVisible"/>" />
                </div>
            <div class="errorBox" id="ECDMessageBox"></div>
            <div class="chox-form-submit-result">&nbsp;</div>                
                </s:if>
                <s:else>
                    <span id="ecdDatePH" style="visibility:hidden;"></span>
                    <input type="hidden" name="reason"/>
                    <input id="ECDSupportingNote" name="supportingNote" type="hidden"/>
                </s:else>

            <div id="ecdGridHolder"></div>
        </div>
        
    </fieldset></form>