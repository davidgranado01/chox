<%-- 
    Document   : claimDetail
    Created on : 23-Nov-2008, 12:04:54
    Author     : Dermot
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Claim Detail</title>
        
        
        <script type="text/javascript">
            
            
            var claimDetailsAccessibility = <s:property value="tabAccessibility.claimDetailTabAccessibility" />;
            var hireMonitoringDetailsAccessibility = <s:property value="tabAccessibility.hireMonitoringTabAccessibility" />;
            var invoiceDetailsAccessibility = <s:property value="tabAccessibility.invoiceDetailTabAccessibility" />;
            var paymentPackAccessibility = <s:property value="tabAccessibility.paymentPackTabAccessibility" />;
            var historyDetailsAccessibility = <s:property value="tabAccessibility.historyTabAccessibility" />;
            var notesAccessibility = <s:property value="tabAccessibility.notesTabAccessibility" />;
            

            
            $(document).ready(function(){
                
                
                //populate incident form
                
               // populateIndicent();
                var options = {success : onIncidentUpdateSuccess, beforeSubmit:  showRequest};                 
                $('#formIncident').ajaxForm(options); 

            });
            
            function onIncidentUpdateSuccess(responseText, statusText)  { 
                alert('status: ' + statusText + '\n\nresponseText: \n' + responseText); 
                
                
        document.getElementById('results').innerHTML = responseText;
            } 


            function showRequest(formData, jqForm, options){

                 var queryString = $.param(formData); 
                 alert('About to submit: \n\n' + queryString); 
                 return true;
            }


            /*
            function populateIndicent(){
                $.getJSON("test.js", function(json){
                  alert("JSON Data: " + json.users[3].name);
                });
            }*/



            
            
        </script>
        
        
    </head>    
       
    <body>


       <form id="formIncident" name="formIncident" action="user/updateIncident.action" method="post">
            <input type="hidden" value='<s:property value="incident.id"  />' name="id" id="incidentId">
            <fieldset class="x-fieldset">
                <legend><input type="checkbox" class="jq-toggle-fieldset" checked="checked"/>Incident Details</legend>
                <div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                            Date / Time</label>
                        <input type="text" class="chox-ttxt" name="date"  /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                            Location</label>
                        <input type="text" class="chox-ttxt" name="location" /></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                            Police Involved?</label>
                        <input type="checkbox" class="chox-tcb" name="isPoliceInvolved"/></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">
                            Description</label>
                        <textarea class="chox-tta" name="incidentDescription" cols="20" rows="5"></textarea>
                    </div>
                    
                    <input type="submit" name="incidentForm" action="user/updateIncident.action"/>
                </div>
                
                
            </fieldset>
        </form>

<div id="results"></div>
    
        
    </body>
</html>
