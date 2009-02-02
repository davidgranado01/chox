<%-- 
    Document   : p_report_main
    Created on : 26-Jan-2009, 17:12:00
    Author     : Emmanuel
--%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<head>
    <script>
        
        function renderParameterPanel(report)
        {
            $("#param_panel").load("loadParameterPanel.action?reportName=" + report);
        }
        
    </script>
</head>

<div class="x-panel-bwrap chox-form-container">
    
    
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr valign="top">
            <td class="chox-form-left-col">
                <fieldset class="x-fieldset">
                    <legend>Report List</legend>
                    <div class="x-panel-bwrap chox-form-container">
                        <div style="height:400px;" class="x-panel-bwrap chox-form-container">
                            <ul>
                                <li><a href="javascript:renderParameterPanel('InsurerAdminWeeklyOverviewReport-Excel');">Insurer Admin Weekly Overview Report</a></li>
                                <li><a href="javascript:renderParameterPanel('InvoiceSummaryReport-Excel');">Invoice Summary Report</a></li>
                            </ul>
                        </div>
                    </div>  
                </fieldset>
            </td>
            <td class="chox-form-right-col">
                    <div id="param_panel"></div>
            </td>
        </tr>
    </table>
    
    
    
</div>

