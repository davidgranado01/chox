<%-- 
    Document   : partial-searchClaim
    Created on : 20-Nov-2008, 21:37:13
    Author     : Emmanuel
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">
    function resetForm(){
        $("#searchForm").each(function(){
            this.reset();
        });
        
    }
    
</script>

<div>
    
    <table cellpadding="0" cellspacing="0" class="searchForm" border="0">

        
        <tr>
            <td><label>Supplier Reference</label></td>
            <td><s:textfield name="supplierReference" /></td>
            <td><label>Claim Number</label></td>
            <td><s:textfield name="claimNumber" /></td>
            <td><label>Invoice Number</label></td>
            <td><s:textfield name="invoiceNumber"/></td>    
        </tr>    
        
        <tr>
            
            <td><label>VRN</label></td><td><s:textfield name="vrn" /></td>
            <td><label>Status</label></td><td><s:select name="status" list="statuses" headerKey="" listKey="value" listValue="text"
                                                            headerValue="--- ALL ---"
                                                        emptyOption="false"></s:select></td>
            <td><label>Line of Business</label></td><td><s:select name="lineOfBusiness" list="lineOfBusinesses" listKey="id" listValue="name" headerKey="-1"
                                                                      headerValue="--- ALL ---"
                                                                  emptyOption="false"></s:select></td> 
            
        </tr>
        <tr>
            <td nowrap><label>Claim Upload Date From</label></td><td colspan="2"><div id="claimUploadDateFromDiv" /></td>
            <td nowrap><label>Claim Upload Date To</label></td><td colspan="2"><div id="claimUploadDateToDiv" /></td>
        </tr>
        <tr>
            <td nowrap><label>Invoice Upload Date From</label></td><td colspan="2"><div id="invoiceUploadDateFromDiv" /></td>
            <td nowrap><label>Invoice Upload Date To</label></td><td colspan="2"><div id="invoiceUploadDateToDiv" /></td>
        </tr>                        
        <tr>
            <td nowrap><label>Hire Date From</label></td><td colspan="2"><div id="hireDateFromDiv" /></td>
            <td nowrap><label>Hire Date To</label></td><td colspan="2"><div id="hireDateToDiv"/></td>                            
        </tr>
        <tr>
            <s:if test="isInsurer">
            
                <td><label>Supplier Name</label></td><td colspan="2"><s:select name="supplierId" list="suppliers" listKey="id" listValue="name" headerKey="-1"
                                                                                   headerValue="--- ALL ---"
                                                                               emptyOption="false"></s:select></td>
            </s:if> 
            <s:else>
                <td colspan="3">&nbsp;</td>
            </s:else>
            
            <s:if test="isCHO">
                <td><label>Insurer Name</label></td><td colspan="2"><s:select name="insurerId" list="insurers" listKey="id" listValue="name" headerKey="-1"
                                                                                  headerValue="--- ALL ---"
                                                                              emptyOption="false"></s:select>  </td>
            </s:if>   
            
            <s:else>
                <td colspan="3">&nbsp;</td>
            </s:else> 
        </tr>                 
        
    </table>
    
    <div class="buttonPanel">
        <div>
        <input type="button" onclick="javascript:searchClaim();" value="Search" />
        </div>
   
    </div>

</div>