<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        

    Ext.onReady(function() {

        var incidentDateDatePicker = ui.dateField('date','<s:date format="dd/MM/yyyy" name="date" />','incidentDatePH');

        var incidentDateTimPicker = new Ext.form.TimeField({
            name: 'time',
            width: 100,
            allowBlank: true,
            validationEvent : false,
            increment: 15,
            format:'H:i',
            value: '<s:property value="time" />',
            renderTo:'incidentTimePH'
        });
       
        $.validator.addMethod('timeFormat', function (value) {
            return /^(\d{2}:\d{2})$/.test(value);
        });

        var form = $("#formUpdateIncidentForm");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});
        incidentDateTimPicker.setRawValue('<s:property value="time" />');
        
        form.validate(
        {
            errorLabelContainer: "#incidentMsgBox",
            rules: {
                incidentDescription:{
                    required:true
                },
                location:{
                    required:true
                },
                date: {
                    required:true,
                    dateITA:true
                },
                time: {
                    required:true,
                    timeFormat:true
                }
            },
            messages: {
                incidentDescription: {
                    required:"You must supply a value for 'Incident Description'"
                },
                location: {
                    required:"You must supply a value for 'Incident Location'"
                },
                date:{
                    required:"You must supply a value for 'Date'",
                    dateITA:"Invalid date format for 'Date'"
                },
                time:
                    {
                    required:"You must supply a value for 'Time'",
                    timeFormat:"Invalid format for 'Time'"
                }
            }
        });
        ui.ajaxForm(form,null,'html');
        
        $("#incidentSuccessBox").fadeOut(10000);
    });
</script>


<form id="formUpdateIncidentForm" action="<%=request.getContextPath()%>/prv/p/updateIncident.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <fieldset class="x-fieldset partial">
        <legend>Incident Details</legend>
        <div class="form-container" id="incidenDetailsWId">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Date<span class="mandatory">*</span></label>
                <span id="incidentDatePH"></span>  
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Time<span class="mandatory">*</span></label>
                <span id="incidentTimePH"></span>  
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Location<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="IDLocation" name="location" value="<s:property value="location" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Police Involved?</label>
                <s:checkbox id="IncidentIspoliceInvolvedId" name="isPoliceInvolved" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Description<span class="mandatory">*</span></label>
                <textarea class="chox-tta" id="IDDescription" cols="20" rows="5" name="incidentDescription"><s:property value="incidentDescription" /></textarea>
            </div>
            <div class="chox-form-button">
                <input type="submit" id="IncidentSubmitButtonId" value="Save Changes" />
            </div>
            <div id="incidentMsgBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div id="incidentSuccessBox" class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
    <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    <!--s:token/-->
</form>    