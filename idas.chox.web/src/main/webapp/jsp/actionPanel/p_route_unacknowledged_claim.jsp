<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    function doValidation(){
        
        var inp = document.getElementById("workgroupId");
        if(inp.value=="-1"){
            $("#ClaimUnacknowledgeMessageBox").css("errorBox");
            $("#ClaimUnacknowledgeMessageBox").text("Please select a Workgroup");
            return false;
        }
        return true;
    }
</script>

<form onsubmit="return true;" action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="route" name="route">
    
    <fieldset class="x-fieldset"><legend>Claim Routing - Action Required</legend>  
        <div>
            
            <div class="status-info">
                Please select the 'Workgroup' in order to route the claim to the relevant handling team.
            </div>            
            <s:hidden name="id" />
            <s:hidden name="name" value="assignWorkgroup" />
            <div class="status-control-set">                     
                <div class="status-info-submit">
                    <table>
                        <tr>
                            <td>
                                <div class="no-format">
                                    <label>Workgroup</label>
                                    <s:select name="workgroupId" id="workgroupId"
                                    list="workgroups" headerKey="-1"
                                    listKey="id" listValue="name" 
                                    headerValue="-- Please Select --"></s:select>
                                    <input type="submit" value="Assign Workgroup" onclick="return doValidation()"/>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
                <div id="ClaimUnacknowledgeMessageBox" class="submit-error"></div>
            </div>
        </div>
    </fieldset>
</form>
