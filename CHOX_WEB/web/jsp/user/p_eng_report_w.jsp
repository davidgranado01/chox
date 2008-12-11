<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
    
    $(document).ready(function(){
            
        $("#formEngRptAction").validate(
        {
            errorLabelContainer: "#EngRptmessageBox",                
            rules: {
                
                labourAmount:{number:true},
                totalAmount:{number:true},
                days:{digits:true}
                
            },
            messages: {
                
                labourAmount: {number:"You must supply a numeric value for 'Estimated Labour Amount'"}, 
                totalAmount: {number:"You must supply a numeric value for 'Estimated Total Repair Amount'"},
                days: {digits:"You must supply a integer value for 'Estimated Days Under Repair'"}
                
            }
        });
    }); 
    
</script>

<form id="formEngRptAction" action="user/updateEngineerReport.action" class="entity-form" name="formEngRptAction">
    <input type="hidden" name="objectId" value='<s:property value="id"/>'>
    <fieldset class="x-fieldset">
        <legend>Engineer Report</legend>
        <div style="display:none" class="form-container">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Estimated Labour Amount</label>
             <input type="text" class="chox-tnum" name="labourAmount" value='<s:property value="labourAmount" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Estimated Total Repair Amount</label>
            <input type="text" class="chox-tnum" name="totalAmount" value='<s:property value="totalAmount" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Estimated Days Under Repair</label>
            <input type="text" class="chox-tnum" name="days" value='<s:property value="days" />'/></div>      
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Usable?</label><s:checkbox name="isUsable" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Name</label>
            <input type="text" class="chox-ttxt" name="name" value='<s:property value="name" />'/></div> 
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Company</label>
            <input type="text" class="chox-ttxt" name="company" value='<s:property value="company" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Address 1</label>
            <input type="text" class="chox-ttxt" name="address1" value='<s:property value="address1" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Address 2</label>
            <input type="text" class="chox-ttxt" name="address2" value='<s:property value="address2" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Address 3</label>
            <input type="text" class="chox-ttxt" name="address3" value='<s:property value="address3" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Address 4</label>
            <input type="text" class="chox-ttxt" name="address4" value='<s:property value="address4" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Address 5</label>
            <input type="text" class="chox-ttxt" name="address5" value='<s:property value="address5" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Postcode</label>
            <input type="text" class="chox-ttxt" name="postcode" value='<s:property value="postcode" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Telephone</label>
            <input type="text" class="chox-ttxt" name="telephone" value='<s:property value="telephone" />'/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Email</label>
            <input type="text" class="chox-ttxt" name="email" value='<s:property value="email" />'/></div>
            
            
            <div class="chox-form-button">
                <input type="submit" value="Save Changes"/><div class="chox-form-submit-result"></div>
            </div>        
            <div class="errorBox" id="EngRptmessageBox"></div>
        </div>
    </fieldset>
</form>