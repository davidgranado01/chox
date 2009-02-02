<%-- 
    Document   : p_insurer_admin_weekly_overview_param_panel
    Created on : 26-Jan-2009, 17:12:00
    Author     : Emmanuel
--%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
        
    var reportName = 'InsurerAdminWeeklyOverviewReport-Excel';

    $(document).ready(function(){  
                                    
        $("#formReportParam").submit(function() {
            var queryString = $('#formReportParam').formSerialize();    
            var popwin = window.open("exportExcelReport.action?" + "reportName=" + reportName + "&" + queryString, "Report", "WIDTH=575,HEIGHT=500,RESIZABLE=No,SCROLLBARS=YES,TOOLBAR=NO,LEFT=200,TOP=100");
            return false;
        });
    }); 
        
</script>

<fieldset class="x-fieldset">
    <legend><s:property value="reportName"/></legend>  
    <div class="x-panel-bwrap chox-form-container">        
        <form id="formReportParam" class="XXentity-form">            
            <div class="form-container">
                <div class="chox-form-item">
                    <label class="chox-form-std-label">
                    Claim Id<span class="mandatory">*</span></label>
                <input type="text" class="chox-ttxt" id="claimId" name="claimId"/></div>             
                
                <div class="chox-form-button">
                    <input type="submit" value="Open Report" methid="post" />
                </div>
                <div id="INCmessageBox" class="errorBox"></div>
                <div id="submitResult" class="chox-form-submit-result"></div>    
            </div>
        </form>            
    </div>
</fieldset>