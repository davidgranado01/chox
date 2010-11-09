<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){

        ui.dateField('rentalStart', '<s:date format="dd/MM/yyyy" name="rentalStart" />' ,'rentalMonitoringStartPH');

        var rentalStartTimPicker = new Ext.form.TimeField({
            name: 'rentalStartTime',
            width: 100,
            allowBlank: true,
            validationEvent : false,
            increment: 15,
            format:'H:i',
            value: '<s:property value="rentalStartTime" />',
            renderTo:'rentalMonitoringStartTimePH'
        });


        $.validator.addMethod('time', function (value) {
            if (value === '') return true;
            else return (/^(\d{2}:\d{2})$/).test(value);
        });

        var form = $("#formUpdateHireMonitoringVehicle");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        form.validate(
        {
            errorLabelContainer: "#HVMDmessageBox",
            rules: {
                rentalStart:{date:true},
                rentalStartTime:{time:true}
 //               vehicleClassId : { min: }
            },
            messages: {
                rentalStart: {date:"Invalid date format for 'Hire Start (Date)'"},
                rentalStartTime: {time:"Invalid date format for 'Hire Start (Time)'"}
 //               vehicleClassId:{min: "You must select a Vehicle Class"}
            }
        });
        ui.ajaxForm(form,null,'html');

    });

</script>

<form id="formUpdateHireMonitoringVehicle" action="<%=request.getContextPath()%>/prv/p/updateMonitoringVehicleHire.action" class="XXentity-form">
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <fieldset class="x-fieldset partial">
        <legend>Hire Vehicle Details</legend>
        <div class="form-container" id="hireMonitoringVehicleDetailWId">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Replacement Vehicle Class</label>
                    <s:select name="vehicleClassMonitoringId" list="vehicleClasses" listKey="id" listValue="name" headerKey="-1" headerValue="--- SELECT ---" emptyOption="false"></s:select>
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Hire Start (Date)</label>
                <span id="rentalMonitoringStartPH"></span></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Hire Start (Time)</label>
                <span id="rentalMonitoringStartTimePH"></span></div>
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" />
            </div>

            <div id="HVMDmessageBox" class="action-error-msg"><s:property value="actionError" /></div>
            <div class="chox-form-submit-result"><s:property value="actionResult" /></div>
        </div>
    </fieldset>
    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    <!--s:token/-->
</form>