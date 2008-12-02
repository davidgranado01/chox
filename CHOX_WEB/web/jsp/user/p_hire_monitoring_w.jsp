<%@ taglib uri="/struts-tags" prefix="s" %>
<form id="formUpdateIncident" action="user/updateHireMonitorDetail.action" class="NNNentity-form">
    
    
    <script language="JavaScript">
        
        
        function doDummySave(){
            
            $("#ddummy").text("Your changes have been saved");
            
            return false;
        }
        
        </script>
    <input type="hidden" name="objectId" value='<s:property value="id"/>'>
    <fieldset class="x-fieldset">
        <legend>Hire Monitoring</legend>
        <div style="display:none" class="form-container">
            
           <div class="chox-form-item">
                <label class="chox-form-std-label">
                Original ECD</label>&nbsp;</div>

           <div class="chox-form-item">
                <label class="chox-form-std-label">
                    Name Of Repairer
                </label>
           <input type="text" class="chox-ttxt" name="nameOfRepairer" value='<s:property value="nameOfRepairer" />'/></div>
           
           <div class="chox-form-item">
                <label class="chox-form-std-label">
                Repair Book In Date</label>
            <input type="text" class="chox-ttxt" name="repairBookInDate" value='<s:property value="repairBookInDate" />'/></div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Inspection Booked Date</label>
            <input type="text" class="chox-ttxt" name="inspectionBookedDate" value='<s:property value="inspectionBookedDate" />'/></div>
           
           <div class="chox-form-item">
                <label class="chox-form-std-label">
                Inspection Date</label>
            <input type="text" class="chox-ttxt" name="inspectionDate" value='<s:property value="inspectionDate" />'/></div>
           
           <div class="chox-form-item">
                <label class="chox-form-std-label">
                Is Total Loss?</label>
            <s:checkbox name="isTotalLostCheck" /></div>
           
           <div class="chox-form-item">
                <label class="chox-form-std-label">
                Total Loss Inspection Report</label>
           NONE</div>
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Repair Completion Date</label>
            <input type="text" class="chox-ttxt" name="repairCompletionDate" value='<s:property value="repairCompletionDate" />'/></div>
            
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Name of IME</label>
            <input type="text" class="chox-ttxt" name="nameOfIme" value='<s:property value="nameOfIme" />'/></div>
            
            
            <div class="chox-form-button">
                <input type="submit" value="Save Changes" onclick="return doDummySave()" />
            </div>
            <div class="chox-form-submit-result" id="ddummy">&nbsp;</div>   
            
            
        </div>
    </fieldset>
</form>