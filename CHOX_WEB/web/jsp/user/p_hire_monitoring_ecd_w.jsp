<%-- 
    Document   : p_hire_monitoring_ecd_w
    Created on : 03-Dec-2008, 20:09:07
    Author     : Emmanuel
--%>

<%@ taglib uri="/struts-tags" prefix="s" %>


<script language="JavaScript">
    
    
    
            $(document).ready(function(){
  

                var options = { 
                    beforeSubmit:  onBeforeSubmit,  // pre-submit callback 
                    success:       onAfterEcdSubmit,  // post-submit callback 
                    timeout: 3000,
                    error: onSubmitError
                };                      
                $('#formAddNewHireMonitoringEcd').ajaxForm(options); //wrap all <form> elements with ajax submission config   
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
    <input type="hidden" name="objectId" value='<s:property value="id"/>'>
    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <fieldset class="x-fieldset">
        <legend>Estimate complete Date (ECD)</legend>
        <div style="display:none" class="form-container">
            <div class="chox-form-item">
                <label class="chox-form-std-label">
                Estimate complete Date</label>
            <input type="text" class="chox-tnum" name="ecdDate" value='<s:property value="ecdDate" />'/></div>                                          
            
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