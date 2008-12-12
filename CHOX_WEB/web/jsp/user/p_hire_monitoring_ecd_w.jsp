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
                width: 185,
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
                $('#formAddNewHireMonitoringEcd').ajaxForm(ecdOptions); //wrap all <form> elements with ajax submission config   
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
            }
    
    
    
</script>



<form id="formAddNewHireMonitoringEcd" action="user/addNewHireMonitoringEcd.action">
    <fieldset class="x-fieldset">
        <legend>Revised ECD</legend>
        <div style="display:none" class="form-container">
            <input type="hidden" name="objectId" value='<s:property value="id"/>'>
            <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
            <div class="chox-form-item">
                <label class="chox-form-std-label">New ECD</label>
                <span id="ecdDatePH"></span>
            </div>                                          
            
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Reason of Delay</label>
                <s:select name="reason" list="reasonTypes" headerKey="-1"
                          headerValue="--- SELECT ---"
                          emptyOption="false"></s:select>
            </div>    
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Supporting Note</label>
                <textarea class="chox-tta" id="ECDSupportingNote" cols="20" rows="5" name="supportingNote"><s:property value="supportingNote" /></textarea>
            </div>  
            
            <div class="chox-form-button">
                <input type="submit" value="Add New ECD" />
            </div>
            <div class="chox-form-submit-result">&nbsp;</div>  
            <div id="ecdGridHolder"></div>
        </div>
         
    </fieldset>
</form>