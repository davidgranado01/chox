<%@ taglib uri="/struts-tags" prefix="s" %>

<link href="<%= request.getContextPath()%>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/>    

<script language="JavaScript">
    function doValidation(){
        
        var inp = document.getElementById("lineOfBusinessId");
        if(inp.value==1){
            $("#ClaimUnacknowledgeMessageBox").css("errorBox");
            $("#ClaimUnacknowledgeMessageBox").text("Please select line of business!");
            return false;
        }
        return true;
    }
</script>

<form onsubmit="return true;" action="user/route.action" method="post" id="route" name="route">
    
    <fieldset class="x-fieldset"><legend>Claim Routing - Action Required</legend>  
        <div>
            
            <div class="status-info">
                Please select the 'Line of Business' in order to route the claim to the relevant handling team.
            </div>            
            <s:hidden name="id" />
            <div class="status-control-set">                     
                <div class="status-info-submit">
                    <table>
                        <tr>
                            <td>
                                <div class="no-format">
                                    <label>Line of Business</label>
                                    <s:select name="lineOfBusiness.id" id="lineOfBusinessId"
                                    list="lineOfBusinesses" headerKey="1" 
                                    listKey="id" listValue="name" 
                                    headerValue="-- Please Select --"></s:select>
                                    <input type="submit" value="Assign Line of Business" onclick="return doValidation()"/>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
                <div id="ClaimUnacknowledgeMessageBox" style="color:#FF0000; font-weight:bold" class="errorBox"></div>
            </div>
        </div>
    </fieldset>
</form>
