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
        <s:head theme="ajax" />
        
    </head>    
    
    <body>
        <s:hidden name="id" />
        
        <s:form action="updateIncident" theme="ajax">
            <s:hidden name="id" />
            <s:hidden name="incident.id" />
            <s:hidden name="incident.created" />
            
            <fieldset class="x-fieldset">
                <legend><input type="checkbox" class="jq-toggle-fieldset" checked="checked"/>Incident Details</legend>
                <div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Date / Time</label>
                        <s:datetimepicker cssClass="chox-ttxt"name="incident.date" />
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Location</label>
                        <s:textfield cssClass="chox-ttxt" name ="incident.location"/>
                    </div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Police Involved?</label>
                    <s:checkbox cssClass="chox-tcb" name="incident.isPoliceInvolved"/></div>
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Description</label>
                        <s:textarea cssClass="chox-tta" name ="incident.incidentDescription" cols="20" rows="5"/>                        
                    </div>                                        
                </div>  
            </fieldset>
            
            <s:submit theme="ajax" targets="results" />
            
            
        </s:form>
        <div id="results"></div>
        
        
    </body>
</html>
