<%-- 
    Document   : p_claimUnacknowledgeUnrouted
    Created on : Nov 28, 2008, 11:54:01 AM
    Author     : Carlson
--%>


<%@ taglib uri="/struts-tags" prefix="s" %>




<form onsubmit="return true;" action="user/route.action" method="post" id="route" name="route">
    
    
    <fieldset class="x-fieldset"><legend>Claim Routing - Action Required</legend>  
        
        <div>
            
            <div class="status-info">
                Please select the 'Line of Business' in order to route the claim to the relevant handling team.
            </div>            
            
            
            <s:hidden name="id" />
            
            

            <div class="status-control-set">
                <label>Line of Business</label>
                    

                <s:select name="lineOfBusiness.id" list="lineOfBusinesses" headerKey="1" listKey="id" listValue="name"></s:select>
                                                                           
                <input type="submit" value="Assign Line of Business" />
                
                
                </div>
        </div>
    </fieldset>
</form>