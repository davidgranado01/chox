<%-- 
    Document   : p_claimAwaitingCarHireInfo
    Created on : Nov 28, 2008, 11:56:08 AM
    Author     : Carlson
--%>

<%@ taglib uri="/struts-tags" prefix="s" %>

<form onsubmit="return true;" action="user/submitHireMonitoringDetail.action" method="post" id="route" name="route">
    
    <fieldset class="x-fieldset"><legend>Hire Monitoring - Action Required</legend>                
        
        <div>
            
            <div class="status-info">
                Please complete the 'Hire Monitoring' tab with required details regarding the hire of the vehicle. When you are ready, please click the 'Proceed' button below. 
            </div>
                
            <s:hidden name="id" />
             
           <div class="status-info-submit">
                <table>
                    <tr>
                        <td>
                            <div class="no-format">
                                <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                            </div>
                        </td>
                    </tr>
                    <tr>                        
                        <td><input type="submit" value="Proceed" /></td>
                    </tr>
                </table>                
            </div>
                
        </div>
    </fieldset>
    
</form>
