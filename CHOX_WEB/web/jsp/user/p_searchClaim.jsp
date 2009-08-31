<%-- 
    Document   : partial-searchClaim
    Created on : 20-Nov-2008, 21:37:13
    Author     : Emmanuel
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">  
    
    $(document).ready(function(){
        doInsuereSearchSelectOnChange();
    }); 
    
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
    
    function doInsuereSearchSelectOnChange(){
        var selectedInsurerId = -1;
        
        if($("#insurerId").val()!=null){
            selectedInsurerId = $("#insurerId").val();
        }
        
        // $("#searchScreenLineOfBusinessDropDownDiv").load("LineOfBusinessDropDownAction.action?orgId=" + selectedInsurerId);
        $("#searchScreenWorkgroupDropDownDiv").load("WorkgroupDropDownAction.action?orgId=" + selectedInsurerId);
    }
   
    function clearForm(){
   
        $('#searchForm').contents().find(':input').each(function() {
                var type = this.type;
                var tag = this.tagName.toLowerCase();

                  if (type == 'text' || type == 'password' || tag == 'textarea'){
                      this.value = "";
                  }else if(tag == 'select'){
                      this.selectedIndex = 0;
                  }
        });

    }

    
</script>
<div>
    
    <table id="searchForm" cellpadding="0" cellspacing="0" class="searchForm" border="0">
        
        <tr>
            <td><label>Supplier Reference</label></td>
            <td><s:textfield name="supplierReference"/></td>
            <td><label>Claim Number</label></td>
            <td><s:textfield name="claimNumber"/></td>
        </tr>    
        
        <tr>
            <td><label>Invoice Number</label></td>
            <td><s:textfield name="invoiceNumber"/></td> 
            <td><label>VRN</label></td><td><s:textfield name="vrn" /></td>
        </tr>
        
        <tr>
            <td nowrap><label>Claim Upload Date From</label></td><td><div id="claimUploadDateFromDiv" /></td>
            <td nowrap><label>Claim Upload Date To</label></td><td><div id="claimUploadDateToDiv" /></td>
        </tr>
        <tr>
            <td nowrap><label>Invoice Upload Date From</label></td><td><div id="invoiceUploadDateFromDiv" /></td>
            <td nowrap><label>Invoice Upload Date To</label></td><td><div id="invoiceUploadDateToDiv" /></td>
        </tr>                        
        <tr>
            <td nowrap><label>Hire Date From</label></td><td><div id="hireDateFromDiv" /></td>
            <td nowrap><label>Hire Date To</label></td><td><div id="hireDateToDiv"/></td>
        </tr>

        <tr>
            <s:if test="isInsurer || isChoxAdmin">
                <td><label>Supplier Name</label></td>
                <td>
                        <s:select 
                        name="supplierId" 
                        list="suppliers"
                        listKey="id"
                        listValue="name"
                        headerKey="-1"
                        headerValue="--- ALL ---"
                        emptyOption="false">
                        </s:select>
                    </td>
            </s:if>
            <s:else>
                <td><label></label></td>
                <td></td>
            </s:else>
            
            <td><label>Status</label></td><td>
                <s:select 
                    name="status" 
                    list="statuses" 
                    headerKey="" 
                    listKey="value" 
                    listValue="text"
                    headerValue="--- ALL ---" headerKey=""
                    emptyOption="false" 
                    value="status">
                </s:select></td>            
            
        </tr>
<tr>
            
            <s:if test="isCHO || isChoxAdmin">
                <td><label>Insurer Name</label></td>
                <td>
                    <s:select 
                    name="insurerId" 
                    list="insurers" 
                    listKey="id" 
                    listValue="name" 
                    headerKey="-1"
                    headerValue="--- ALL ---"
                    onchange="javascript: doInsuereSearchSelectOnChange();"
                    emptyOption="false">
                    </s:select>
                </td>
            </s:if>   
            <s:else>
                 <td><label></label></td>
                <td></td>
            </s:else>

            <td><label>Workgroup</label></td><td>
                <!--
                <div id="searchScreenLineOfBusinessDropDownDiv"></div>   
                !-->
                <div id="searchScreenWorkgroupDropDownDiv"></div>   
            </td>
            
        </tr>        
    </table>
    <div class="buttonPanel">
        <div>
            <input type="button" onclick="javascript:searchClaim();" value="Search" />
            <input type="reset" onclick="javascript:clearForm();" value="Reset" />
        </div>
    </div>
</div>