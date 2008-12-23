<%-- 
    Document   : p_hire_monitoring_ecd_w
    Created on : 03-Dec-2008, 20:09:07
    Author     : Emmanuel
--%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
    
    $(document).ready(function(){

        var ecdDateDatePicker = new Ext.form.DateField({
            name: 'ecdDate',
            width: 175,
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
                reason:{required:true},
                ecdDate:{required:true, date:true},
                supportingNote:{required:true}
            },
            messages: {
                reason: {
                    required:"You must supply a value for 'Reason of Delay'"
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
    
</script>

<form id="formAddNewHireMonitoringEcd" action="user/addNewHireMonitoringEcd.action" name="formAddNewHireMonitoringEcd" class="XXentity-form">
    <fieldset class="x-fieldset">
        <legend>Revised ECD</legend>
        <div style="display:none" class="form-container">
            <input type="hidden" name="objectId" value='<s:property value="id"/>'>
            <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
                <s:if test="isECDFormVisible">
                <div class="chox-form-item">
                    <label class="chox-form-std-label">New ECD</label>
                    <span id="ecdDatePH"></span>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">
                    Reason of Delay</label>
                    <s:select name="reason" list="reasonTypes" headerKey=""
                              headerValue="--- SELECT ---"
                              emptyOption="false"></s:select>
                </div>
                <div class="chox-form-item">
                    <label class="chox-form-std-label">
                    Supporting Note</label>
                    <textarea class="chox-tta" id="ECDSupportingNote" cols="20" rows="5" name="supportingNote"><s:property value="supportingNote" /></textarea>
                </div>
                
                <div class="chox-form-button">
                    <input type="submit" value="Add New ECD" readonly='<s:property value="isECDFormVisible"/>' />
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