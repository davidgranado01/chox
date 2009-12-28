<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
    
    $(document).ready(function(){
            
        $("#formEngRptAction").validate(
        {
            errorLabelContainer: "#EngRptmessageBox",                
            rules: {
                
                labourAmount:{required:true, number:true},
                totalAmount:{required:true, number:true},
                days:{required:true, digits:true}
                
            },
            messages: {
                
                labourAmount: {
                    number:"You must supply a numeric value for 'Estimated Labour Amount'",
                    required:"You must supply a value for 'stimated Labour Amount'"
                }, 
                totalAmount: {
                    required:"You must supply a value for 'Estimated Total Repair Amount'",
                    number:"You must supply a numeric value for 'Estimated Total Repair Amount'"
                },
                days: {
                    required:"You must supply a value for 'Estimated Days Under Repair'",
                    digits:"You must supply a integer value for 'Estimated Days Under Repair'"
                }
                
            },
            submitHandler: function(form) {
                $(form).ajaxSubmit(globalEntityFormOptions);
            }   
        });
    });
    
</script>

<form id="formEngRptAction" action="<%=request.getContextPath()%>/prv/p/updateEngineerReport.action" class="XXentity-form" name="formEngRptAction">
    <input type="hidden" name="objectId" value='<s:property value="objectId"/>'>
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <fieldset class="x-fieldset">
        <legend>Engineer Report</legend>
        <div style="display:none" class="form-container">           
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Estimated Labour Amount<span class="mandatory">*</span></label>
             <input type="text" class="chox-tnum" name="labourAmount" value="<s:property value="labourAmount" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Estimated Total Repair Amount<span class="mandatory">*</span></label>
            <input type="text" class="chox-tnum" name="totalAmount" value="<s:property value="totalAmount" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Estimated Days Under Repair<span class="mandatory">*</span></label>
            <input type="text" class="chox-tnum" name="days" value="<s:property value="days" />"/></div>      
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Usable?</label><s:checkbox name="isUsable" />
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Name</label>
            <input type="text" class="chox-ttxt" name="name" value="<s:property value="name" />"/></div> 
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Company</label>
            <input type="text" class="chox-ttxt" name="company" value="<s:property value="company" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Address 1</label>
            <input type="text" class="chox-ttxt" name="address1" value="<s:property value="address1" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Address 2</label>
            <input type="text" class="chox-ttxt" name="address2" value="<s:property value="address2" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Address 3</label>
            <input type="text" class="chox-ttxt" name="address3" value="<s:property value="address3" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Address 4</label>
            <input type="text" class="chox-ttxt" name="address4" value="<s:property value="address4" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Address 5</label>
            <input type="text" class="chox-ttxt" name="address5" value="<s:property value="address5" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Postcode</label>
            <input type="text" class="chox-ttxt" name="postcode" value="<s:property value="postcode" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Telephone</label>
            <input type="text" class="chox-ttxt" name="telephone" value="<s:property value="telephone" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Engineer Email</label>
            <input type="text" class="chox-ttxt" name="email" value="<s:property value="email" />"/></div>
            
            <div class="chox-form-button">
                <input type="submit" value="Save Changes"/>
            </div>
            <div class="chox-form-submit-result"></div>
            <div class="errorBox" id="EngRptmessageBox"></div>
        </div>
    </fieldset>
</form>