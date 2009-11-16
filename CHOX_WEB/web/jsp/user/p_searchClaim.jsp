<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script language="JavaScript">  

    var isChoxAdmin = false
    var insurerId = -1;
    var claimOwnerId = -1;
    
    $(document).ready(function(){
        doInsurerSearchSelectOnChange();
        doShowClaimHandler(-1, -1);
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

        var reviewRequiredDateFromPicker = new Ext.form.DateField({
            name: 'reviewRequiredDateFrom',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="hireDateTo" />',
            showWeekNumber: true
        });
        
        var reviewRequiredDateToPicker = new Ext.form.DateField({
            name: 'reviewRequiredDateTo',
            width: 120,
            allowBlank: true,
            format: 'd/m/Y',
            value: '<s:date format="dd/MM/yyyy" name="hireDateTo" />',
            showWeekNumber: true
        });

        reviewRequiredDateFromPicker.on('change', onReveiwDateChange);
        reviewRequiredDateToPicker.on('change', onReveiwDateChange);

        claimUploadDateFromPicker.render('claimUploadDateFromDiv');
        claimUploadDateToPicker.render('claimUploadDateToDiv');
        invoiceUploadDateFromPicker.render('invoiceUploadDateFromDiv');
        invoiceUploadDateToPicker.render('invoiceUploadDateToDiv');
        hireDateFromPicker.render('hireDateFromDiv');
        hireDateToPicker.render('hireDateToDiv');

        if(<s:property value="isCHO" />){
            reviewRequiredDateFromPicker.render('reviewRequiredDateFromDiv');
            reviewRequiredDateToPicker.render('reviewRequiredDateToDiv');
        }
        
    }); 
    
    function setSelectedInsurerId(){

       var isInsurerUser = <s:property value="isInsurer"/>;
       if(isInsurerUser){
           insurerId = <s:property value="OrganisationId"/>;
       }else{
            if($("#insurerId").val()!=null){
                insurerId = $("#insurerId").val();
            }
       }

    }

    function doInsurerSearchSelectOnChange(){
        setSelectedInsurerId();
        $("#searchScreenWorkgroupDropDownDiv").load("SearchWorkgroupDropDownAction.action?orgId=" + insurerId);
        doShowClaimHandler(-1, insurerId);
    }

    function doSearchWorkgroupOnChange(){
        setSelectedInsurerId();
        var workgroupId = -1;

        if($("#workgroup").val()!=null){
            workgroupId = $("#workgroup").val();
        }
            
        doShowClaimHandler(workgroupId, insurerId);
    }
    
   function doShowClaimHandler(selectedWorkgroupId, selectedInsurerId){
       $('#searchScreenClaimhandlerDownDiv').load("SearchClaimHandlerRoleUserDropDownAction.action?workgroupId="+selectedWorkgroupId+"&insurerId="+selectedInsurerId);
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

        $('#searchForm').contents().find(':checkbox').each(function() {
              this.checked = false;
        });

    }

    function statusChange(){

        if(($('#status :selected').val()!="AwaitingCarHireInfo") && <s:property value="isCHO" />){
            $("input[name='reviewRequiredDateTo']").val("");
            $("input[name='reviewRequiredDateFrom']").val("");
        }

    }
    
    function onReveiwDateChange(){
        $("#status").val("AwaitingCarHireInfo");
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
            <td><label>Open Claims</label></td><td><s:checkbox name="isOpenClaim" /></td>
            
        </tr>
        
        <tr>
            <td><label>Customer VRN</label></td><td><s:textfield name="customerVrn" /></td>
            <td><label>Third Party VRN</label></td><td><s:textfield name="thirdPartyVrn" /></td>
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
        
        <s:if test="isCHO">
        <tr>
            <td nowrap><label>Hire Monitoring Review Required Date From</label></td><td><div id="reviewRequiredDateFromDiv" /></td>
            <td nowrap><label>Hire Monitoring Review Required Date To</label></td><td><div id="reviewRequiredDateToDiv" /></td>
        </tr>
        </s:if>
        <s:else>
            <input type="hidden" name="reviewRequiredDateFrom" id="reviewRequiredDateFrom">
            <input type="hidden" name="reviewRequiredDateTo" id="reviewRequiredDateTo">
            
        </s:else>
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
                    onchange="javascript: doInsurerSearchSelectOnChange();"
                    emptyOption="false">
                    </s:select>
                </td>
            </s:if>
            <s:elseif test="isInsurer">
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
            </s:elseif>
            <td><label>Status</label></td><td>
                <s:select
                    name="status"
                    list="statuses"
                    headerKey=""
                    listKey="value"
                    listValue="text"
                    headerValue="--- ALL ---" headerKey=""
                    emptyOption="false"
                    value="status" onchange="javascript: statusChange();">
                </s:select>
            </td>
        </tr>


        <tr>
            <s:if test="isInsurer">
            <tr>
                 <td><label>Workgroup</label></td>
                 <td><div id="searchScreenWorkgroupDropDownDiv"></div></td>
                 <td><label>Claim Owner</label></td>
                 <td><div id="searchScreenClaimhandlerDownDiv"></div></td>
            </tr>
            </s:if>
            <s:else>
            <tr>
                 <td><label>Insurer's Workgroup</label></td>
                 <td><div id="searchScreenWorkgroupDropDownDiv"></div></td>
                 <td><label>Insurer's Claim Owner</label></td>
                 <td><div id="searchScreenClaimhandlerDownDiv"></div></td>
            </tr>
            </s:else>
        </tr>

        <tr>
            <s:if test="isChoxAdmin">
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
                 <td><label></label></td><td></td>
            </s:if>
        </tr>

    </table>
    <div class="buttonPanel">
        <div>
            <input type="button" onclick="javascript:searchClaim();" value="Search" />
            <input type="reset" onclick="javascript:clearForm();" value="Reset" />
        </div>
    </div>
</div>