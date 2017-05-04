<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var reportName = 'InvoiceReport-Excel';

    Ext.onReady(function(){
        ui.unvalidatedDateField('DateStart',getTodayDate(),'dateFromDiv');
        ui.unvalidatedDateField('DateEnd',getTodayDate(),'dateToDiv');

        // This line is currently commented-out as it makes the combo-box display empty!
        // Reported to ExtJS forum and awaiting a solution
        //        new Ext.ToolTip({ target: 'help-supplier-reference-input', html: 'Supplier Reference Number input format: ABC123, ABC124, ABC125'});

        // Add Workgroup drop-down menu
    <s:if test="isCHO!=true && isCH!=true && insurerIsWorkgroupEnabled" >
                var invoiceReportWorkgroupJsonReader = new Ext.data.JsonReader({
                    totalProperty: 'totalCount',
                    root: 'results',
                    fields:
                        [
                        {name:'text'},
                        {name:'value'}
                    ]
                });
                var invoiceReportWorkgroupStore = new choxDataStore({
                    url : "/prv/p/WorkgroupDropDownActionByInsurer2.action",
                    reader : invoiceReportWorkgroupJsonReader,
                    listeners: {load: function() {
                            var  defaultName={'value':'--- All ---','text':-1};
                            this.insert(0, new Ext.data.Record(defaultName));
                            }
                    }
                });

                var invoiceReportWorkgroupCombo = new Ext.form.ComboBox({
                    store: invoiceReportWorkgroupStore,
                    renderTo: 'rptInvoiceWorkgroupSelectionDiv',
                    valueField: 'text',
                    id: 'invoiceReportWorkgroupComboId',
                    hiddenName: 'workgroupId',
                    displayField:'value',
                    typeAhead: true,
                    autoWidth: true,
                    width: 220,
                    mode: 'local',
                    emptyText: '--- All ---',
                    triggerAction : 'all',
                    emptyValue: '-1',
                    selectOnFocus : true,
                    forceSelection : true,
                    listeners: {blur: function () {
                            if(this.getRawValue() === "" ) {
                                this.clearValue();
                            }
                        },
                        select: function () {
                            if (invoiceReportWorkgroupCombo.getValue() === "--- All ---") {
                            	invoiceReportWorkgroupCombo.clearValue();
                            	invoiceReportWorkgroupCombo.reset();
                            }                             
                        }
                    }
                });
                invoiceReportWorkgroupStore.load();
    </s:if>

                $("form#formInvoiceReportParam").validate(
                {
                    errorLabelContainer: "#errorMsgBox",
                    rules: {
                    	supplierCombo:{
                    		equalTo: "--- Please Select ---",
                            required:function(){
                            	if(Ext.get('supplierCombo').getValue() !== "--- Please Select ---")
                            		return true;
                            	else
                            		return false;
                            }
                        },
                        DateStart:{
                        	max:function(){
                        		var sd = Ext.get('DateStart').getValue().split("/");
                                var ed = Ext.get('DateEnd').getValue().split("/");
                                var time = new Date(sd[2],sd[1] - 1 ,sd[0]).getTime() - new Date(ed[2],ed[1] - 1 ,ed[0]).getTime();
                                if(time > 0)
                                    return true;
                            },
                            required:true,
                            dateITA: true
                        },
                        DateEnd:{
                            required:true,
                            dateITA: true
                        }
                    },
                    messages: {
                    	supplierCombo:{
                    		equalTo: "You must select 'Credit Hire Organisation'",
                      		required:"You must select 'Credit Hire Organisation'"
                        },
                        DateStart: {
                        	max:"'Invoice Uploaded From' can't be before 'Invoice Uploaded From'",
                            required:"A value must be supplied for 'Invoice Uploaded From'",
                            dateITA:"You must supply valid date format for 'Invoice Uploaded From'"
                        },
                        DateEnd: {
                            required:"A value must be supplied for 'Invoice Uploaded To'",
                            dateITA:"You must supply valid date format for 'Invoice Uploaded To'"
                        }
                    }
                });
                
                
                <s:if test="!isCHO">
	                var suppliersJsonReader = new Ext.data.JsonReader({
	                    totalProperty: 'totalCount',
	                    root: 'results',
	                    fields:
	                        [
	                        {name:'text'},
	                        {name:'value'}
	                    ]
	                });
	
	                var mysuppliers = Ext.util.JSON.decode('<s:property value="suppliersJsonString" escapeHtml="false"/>');
	                var suppliersStore = new Ext.data.Store({
	                    data : mysuppliers,
	                    reader : suppliersJsonReader
	                });
	                
	                var supplierCombo = new Ext.form.ComboBox({
	                    store : suppliersStore,
	                    id : 'supplierCombo',
	                    renderTo: 'choDDid',
	                    width: 220,
	                    valueField : 'text',
	                    hiddenName: 'supplierId',
	                    displayField :'value',
	                    typeAhead : true,
	                    mode : 'local',
	                    triggerAction : 'all',
	                    emptyText: '--- Please Select ---',
	                    emptyValue: '-1',
	                    selectOnFocus : false,
	                    allowBlank : true,
	                    forceSelection : true,
	                    listeners: { blur: function () {
	                            if(this.getRawValue() === "" ) {
	                                this.clearValue();
	                                this.reset();
	                            }
	                        }
	                    }
	                });
                
                </s:if>
                
            });
    
    function openInvoiceReport(){
    	var msgBox = $('#errorMsgBox');
        if(Ext.get('supplierCombo').getValue() === "--- Please Select ---"){
        	msgBox.empty();
        	msgBox.text("You must select 'Credit Hire Organisation'").append('<br/>').show();
        }
        if($("form#formInvoiceReportParam").valid() && Ext.get('supplierCombo').getValue() !== "--- Please Select ---"){
//            var queryString = $('form#formInvoiceReportParam').formSerialize();
            var queryString = {};
            $.each($('form#formInvoiceReportParam').serializeArray(), function() {queryString[this.name] = this.value;});
            msgBox.empty();
            // If no workgroup selected, insert a '-1' into the query string
            if (queryString.workgroupId === "") {
                queryString.workgroupId = -1;
            }
//            if (queryString.indexOf('workgroupId=&') >= 0)
//                queryString = queryString.replace('workgroupId=&', 'workgroupId=-1&')
            generateReport(queryString);       
        }
        msgBox.show();
    }


</script>
<fieldset class="x-fieldset">

    <legend>CHO Invoice Report</legend>
    <form id="formInvoiceReportParam" name="formInvoiceReportParam" class="XXentity-form" action="POST">

        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report allows a user to produce an invoice for a particular claim or set of claims. Invoices are produced per Credit Hire Organisation and are based on specific Supplier Reference Number(s) and/or specifying a date range based on the invoice upload date.</div>

                <table class="report-form">

                    <s:if test="!isCHO && !isCH && insurerIsWorkgroupEnabled">
                        <tr>
                            <td nowrap><label>Workgroup</label></td>
                            <td><div id="rptInvoiceWorkgroupSelectionDiv"></div></td>
                        </tr>
                    </s:if>
                    <s:else>
                        <input type="hidden" id="workgroupId" name="workgroupId" value="-1"/>
                    </s:else>
                    <s:if test="!isCHO">
                        <tr>
							<td nowrap><label>Credit Hire Organisation</label><span class="mandatory">*</span></td>
							<td><div id="choDDid"></td>
						</tr>
                    </s:if>

                    <tr>
						<td nowrap width="30%"><label>Invoice Uploaded From</label><span class="mandatory">*</span></td>
						<td><div id="dateFromDiv" /></td>
					</tr>
                    <tr>
                        <td nowrap><label>Invoice Uploaded To</label><span class="mandatory">*</span></td><td><div id="dateToDiv"/></td>
                    </tr>
                    <tr>
                        <td nowrap><label>Supplier Reference(s)<br/><br/><font size="1">(Supplier Reference Number input<br/>format: ABC123, ABC124, ABC125)</font></label></td><td><textarea cols="20" rows="5" id="supplierReferences" name="supplierReferences"></textarea><!--img id="help-supplier-reference-input" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt="Help"/--></td>
                    </tr>

                </table>

                <div class="chox-report-button">
                    <button type="button" id="invoiceReportGenerateReportId" onclick="javascript:openInvoiceReport();">Generate Report</button>
                </div>

            </div>

        </div>

    </form>
    
    <div id="errorMsgBox" class="action-error-msg"></div>

</fieldset>
