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
        
        
        <script language="JavaScript">
           
            function onIncidentUpdateStatus(responseText, statusText)  { 
                $('#tempres').text(responseText);
            }
            
            
            /*
            
            
            $(document).ready(function(){
                
                
                alert("hi there");
                $('#formUpdateIncident').ajaxForm(); 
                
                
            });
            
            
             */
           
           
            $(document).ready(function() { 
                var options = { 
                    target:        '#tempres',   // target element(s) to be updated with server response 
                    beforeSubmit:  showRequest,  // pre-submit callback 
                    success:       showResponse  // post-submit callback 

                    // other available options: 
                    //url:       url         // override for form's 'action' attribute 
                    //type:      type        // 'get' or 'post', override for form's 'method' attribute 
                    //dataType:  null        // 'xml', 'script', or 'json' (expected server response type) 
                    //clearForm: true        // clear all form fields after successful submit 
                    //resetForm: true        // reset the form after successful submit 

                    // $.ajax options can be used here too, for example: 
                    //timeout:   3000 
                }; 

                // bind form using 'ajaxForm' 
                $('#formUpdateIncident').ajaxForm(options); 
                
                
            }); 
                
                
            function showRequest(formData, jqForm, options) { 
                // formData is an array; here we use $.param to convert it to a string to display it 
                // but the form plugin does this for you automatically when it submits the data 
                var queryString = $.param(formData); 

                // jqForm is a jQuery object encapsulating the form element.  To access the 
                // DOM element for the form do this: 
                // var formElement = jqForm[0]; 

                alert('About to submit: \n\n' + queryString); 

                // here we could return false to prevent the form from being submitted; 
                // returning anything other than false will allow the form submit to continue 
                return true; 
            } 

            // post-submit callback 
            function showResponse(responseText, statusText)  { 
                // for normal html responses, the first argument to the success callback 
                // is the XMLHttpRequest object's responseText property 

                // if the ajaxForm method was passed an Options Object with the dataType 
                // property set to 'xml' then the first argument to the success callback 
                // is the XMLHttpRequest object's responseXML property 

                // if the ajaxForm method was passed an Options Object with the dataType 
                // property set to 'json' then the first argument to the success callback 
                // is the json data object returned by the server 

                alert('status: ' + statusText + '\n\nresponseText: \n' + responseText + 
                    '\n\nThe output div should have already been updated with the responseText.'); 
            }                 
           
        </script>
        
        
    </head>    
    
    <body>
        
        
        
        <ul>
            <li>claimDetailTabAccessibility : <s:property value="tabAccessibility.claimDetailTabAccessibility" /></li>
            <li>invoiceDetailTabAccessibility : <s:property value="tabAccessibility.invoiceDetailTabAccessibility" /></li>
            <li>hireMonitoringTabAccessibility : <s:property value="tabAccessibility.hireMonitoringTabAccessibility" /></li>;
            <li>historyTabAccessibility : <s:property value="tabAccessibility.historyTabAccessibility" /></li>;
            <li>notesTabAccessibility : <s:property value="tabAccessibility.notesTabAccessibility" /></li>
            <li>paymentPackTabAccessibility : <s:property value="tabAccessibility.paymentPackTabAccessibility" /></li>
        </ul>  
        
        
        
        
        
        
        <s:action name="openIncident" executeResult="true">
            <s:param name="incidentId"><s:property value="incident.id" /></s:param> 
            <s:param name="claimStatus"><s:property value="status" /></s:param> 
        </s:action>
        
        
        
        
        
        
        
        
        
    </body>
</html>
