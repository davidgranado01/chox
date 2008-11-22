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
                    <ul class="searchForm">
                        <li><label>Supplier Reference</label><s:textfield name="supplierReference" /></li>
                        <s:if test="isInsurer">
                            <li><label>Supplier Name</label><s:select name="supplierId" list="suppliers" listKey="id" listValue="name" headerKey="-1"
                                                                         headerValue="--- Please Select ---"
                                                                     emptyOption="false"></s:select></li> 
                        </s:if>                        
                        <s:if test="isCHO">
                            <li><label>Insurer Name</label><s:select name="insurerId" list="insurers" listKey="id" listValue="name" headerKey="-1"
                                                                         headerValue="--- Please Select ---"
                                                                     emptyOption="false"></s:select></li>  
                        </s:if>                        
                        <li><label>Invoice Numnber</label><s:textfield name="invoiceNumber" /></li>   
                        <li><label>Claim Number</label><s:textfield name="claimNumber" /></li>
                        <li><label>VRN</label><s:textfield name="vrn" /></li>
                       <li><label>Claim Upload Date From</label><s:textfield name="claimUploadDateFrom" /></li>
                        <li><label>Claim Upload Date To</label><s:textfield name="claimUploadDateTo" /></li> 
                        <li><label>Invoice Upload Date From</label><s:textfield name="invoiceUploadDateFrom" /></li>
                        <li><label>Invoice Upload Date To</label><s:textfield name="invoiceUploadDateTo" /></li>   
                        <li><label>Hire Date From</label><s:textfield name="hireDateFrom" /></li>
                        <li><label>Hire Date To</label><s:textfield name="hireDateTo"/></li>
                        <li><label>Status</label><s:select name="status" list="statuses" headerKey="" listKey="value" listValue="text"
                                                               headerValue="--- Please Select ---"
                                                           emptyOption="false"></s:select> </li>
                        <li><label>Line of Business</label><s:select name="lineOfBusiness" list="lineOfBusinesses" listKey="id" listValue="name" headerKey="-1"
                                                                         headerValue="--- Please Select ---"
                                                                     emptyOption="false"></s:select></li>
                        
                        
                    </ul>
                </td>
            </tr>
            <tr><td>
                    <div class="buttonPanel">
                        <input type="button" onclick="javascript:searchClaim();" value="Search" />
                        <s:reset key="Reset" />
                    </div>
            </td></tr>
        </table>        
        
    </s:form>

</div>