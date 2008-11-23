<%-- 
    Document   : partial-searchClaim
    Created on : 20-Nov-2008, 21:37:13
    Author     : Emmanuel
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<div>
    <s:form namespace="/user" action="doSearchClaim" method="POST" theme="simple">
        <table>
            <tr>
                <td>
                    <table class="searchForm">
                        <tr>
                            <td><label>Supplier Reference</label></td><td><s:textfield name="supplierReference" /></td>
                            
                            <s:if test="isInsurer">
                                <td><label>Supplier Name</label></td><td><s:select name="supplierId" list="suppliers" listKey="id" listValue="name" headerKey="-1"
                                                                                       headerValue="--- Please Select ---"
                                                                                   emptyOption="false"></s:select></td>
                            </s:if>                        
                            <s:if test="isCHO">
                                <td><label>Insurer Name</label></td><td><s:select name="insurerId" list="insurers" listKey="id" listValue="name" headerKey="-1"
                                                                                      headerValue="--- Please Select ---"
                                                                                  emptyOption="false"></s:select>  </td>
                            </s:if>  
                        </tr>
                        <tr>
                            <td><label>Claim Number</label></td><td><s:textfield name="claimNumber" /></td>
                            <td><label>Invoice Numnber</label></td><td><s:textfield name="invoiceNumber"/></td>                            
                        </tr>                        
                        <tr>
                            <td><label>VRN</label></td><td colspan="3"><s:textfield name="vrn" /></td>
                        </tr>                        
                        <tr>
                            <td><label>Claim Upload Date From</label></td><td><div id="claimUploadDateFromDiv" /></td>
                            <td><label>Claim Upload Date To</label></td><td><div id="claimUploadDateToDiv" /></td>
                        </tr>
                        <tr>
                            <td><label>Invoice Upload Date From</label></td><td><div id="invoiceUploadDateFromDiv" /></td>
                            <td><label>Invoice Upload Date To</label></td><td><div id="invoiceUploadDateToDiv" /></td>
                        </tr>                        
                        <tr>
                            <td><label>Hire Date From</label></td><td><div id="hireDateFromDiv" /></td>
                            <td><label>Hire Date To</label></td><td><div id="hireDateToDiv"/></td>                            
                        </tr>                        
                        <tr>                            
                            <td><label>Status</label></td><td><s:select name="status" list="statuses" headerKey="" listKey="value" listValue="text"
                                                                            headerValue="--- Please Select ---"
                                                                        emptyOption="false"></s:select> </td>
                            <td><label>Line of Business</label></td><td><s:select name="lineOfBusiness" list="lineOfBusinesses" listKey="id" listValue="name" headerKey="-1"
                                                                                      headerValue="--- Please Select ---"
                                                                                  emptyOption="false"></s:select></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="buttonPanel">
                        <s:reset key="Reset" />
                        <input type="button" onclick="javascript:searchClaim();" value="Search" />                        
                    </div>
                </td>
            </tr>
        </table> 
    </s:form>    
</div>