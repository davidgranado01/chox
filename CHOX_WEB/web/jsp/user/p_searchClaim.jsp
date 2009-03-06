<%-- 
    Document   : partial-searchClaim
    Created on : 20-Nov-2008, 21:37:13
    Author     : Emmanuel
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">  
        
    Ext.onReady(function(){                              
    
        var claimUploadDateFromPicker = new Ext.form.DateField({
            name: 'claimUploadDateFrom',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="claimUploadDateFrom" />',
            showWeekNumber: true
        });
        
        var claimUploadDateToPicker = new Ext.form.DateField({
            name: 'claimUploadDateTo',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="claimUploadDateTo" />',
            showWeekNumber: true
        });
        
        var invoiceUploadDateFromPicker = new Ext.form.DateField({
            name: 'invoiceUploadDateFrom',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="invoiceUploadDateFrom" />',
            showWeekNumber: true
        });
        
        var invoiceUploadDateToPicker = new Ext.form.DateField({
            name: 'invoiceUploadDateTo',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="invoiceUploadDateTo" />',
            showWeekNumber: true
        });
        
        var hireDateFromPicker = new Ext.form.DateField({
            name: 'hireDateFrom',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="hireDateFrom" />',
            showWeekNumber: true
        });
        
        var hireDateToPicker = new Ext.form.DateField({
            name: 'hireDateTo',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="hireDateTo" />',
            showWeekNumber: true
        });
        
        claimUploadDateFromPicker.render('claimUploadDateFromDiv');
        claimUploadDateToPicker.render('claimUploadDateToDiv');
        invoiceUploadDateFromPicker.render('invoiceUploadDateFromDiv');
        invoiceUploadDateToPicker.render('invoiceUploadDateToDiv');
        hireDateFromPicker.render('hireDateFromDiv');
        hireDateToPicker.render('hireDateToDiv');       
        
    }); 
    
</script>
<div>
    
    <table id="searchForm" cellpadding="0" cellspacing="0" class="searchForm" border="0">
        
        
        <tr>
            <td><label>Supplier Reference</label></td>
            <td><s:textfield name="supplierReference"/></td>
            <td><label>Claim Number</label></td>
            <td><s:textfield name="claimNumber"/></td>
            <td><label>Invoice Number</label></td>
            <td><s:textfield name="invoiceNumber"/></td>    
        </tr>    
        
        <tr>
            
            <td><label>VRN</label></td><td><s:textfield name="vrn" /></td>
            <td><label>Status</label></td><td><s:select name="status" list="statuses" headerKey="" listKey="value" listValue="text"
                                                            headerValue="--- ALL ---" headerKey=""
                                                        emptyOption="false" value="status"></s:select></td>
            <td><label>Line of Business</label></td><td><s:select name="lineOfBusiness" list="lineOfBusinesses" listKey="id" listValue="name" headerKey="-1"
                                                                      headerValue="--- ALL ---"
                                                                  emptyOption="false" value="lineOfBusinessId"></s:select></td> 
            
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
                <td><label>Supplier Name</label></td><td colspan="2">
                    <s:select 
                    name="supplierId" 
                    list="suppliers" 
                    listKey="id" 
                    listValue="name" 
                    headerKey="-1"
                    headerValue="--- ALL ---"
                    emptyOption="false">
                    </s:select></td>
            </s:if> 
            <s:else>
                <td colspan="3">&nbsp;</td>
            </s:else>
            
            <s:if test="isCHO">
                <td><label>Insurer Name</label></td><td colspan="2">
                <s:select 
                name="insurerId" 
                list="insurers" 
                listKey="id" 
                listValue="name" 
                headerKey="-1"
                headerValue="--- ALL ---"
                emptyOption="false">
                </s:select>  </td>
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