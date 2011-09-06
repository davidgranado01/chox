<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var reportName = 'InvoiceReport-Excel';

    Ext.onReady(function(){
        ui.dateField('DateStart',getTodayDate(),'dateFromDiv');
        ui.dateField('DateEnd',getTodayDate(),'dateToDiv');

        // This line is currently commented-out as it makes the combo-box display empty!
        // Reported to ExtJS forum and awaiting a solution
        //        new Ext.ToolTip({ target: 'help-supplier-reference-input', html: 'Supplier Reference Number input format: ABC123, ABC124, ABC125'});

        // Add Workgroup drop-down menu
    <s:if test="isCHO!=true && isCH!=true && insurerIsWorkgroupEnabled)" >
                var invoiceReportWorkgroupJsonReader = new Ext.data.JsonReader({
                    totalProperty: 'totalCount',
                    root: 'results',
                    fields:
                        [
                        {name:'text'},
                        {name:'value'}
                    ]
                });
                var invoiceReportWorkgroupStore = new Ext.data.Store({
                    proxy : new Ext.data.HttpProxy
                    ({url : "<%= request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer2.action", method:'GET'}),
                    reader : invoiceReportWorkgroupJsonReader
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
                    mode: 'local',
                    emptyText: '--- All ---',
                    triggerAction : 'all',
                    emptyValue: '-1',
                    selectOnFocus : true,
                    forceSelection : true,
                    listeners: {blur: function () {
                            if(this.getRawValue() == "" ) {
                                this.clearValue();
                            }
                        }
                    }
                });
                invoiceReportWorkgroupStore.load();
    </s:if>

                $("form#formInvoiceReportParam").validate(
                {
                    errorLabelContainer: "#formInvoiceReportParamMessageBox",
                    rules: {
                        supplierId:{
                            required:true
                        },
                        DateStart:{
                            required:true,
                            dateITA: true
                        },
                        DateEnd:{
                            required:true,
                            dateITA: true
                        }
                    },
                    messages: {
                        supplierId:{
                            required:"You must select 'Credit Hire Organisation'"
                        },
                        DateStart: {
                            required:"A value must be supplied for 'Invoice Uploaded From'",
                            dateITA:"You must supply a date value 'Invoice Uploaded From'"
                        },
                        DateEnd: {
                            required:"A value must be supplied for 'Invoice Uploaded To'",
                            dateITA:"You must supply a date value 'Invoice Uploaded To'"
                        }
                    }
                });
            });
    
            function openInvoiceReport()
            {
                if($("form#formInvoiceReportParam").valid()){
                    var queryString = $('form#formInvoiceReportParam').formSerialize();
                    // If no workgroup selected, insert a '-1' into the query string
                    if (queryString.indexOf('workgroupId=&') >= 0)
                        queryString = queryString.replace('workgroupId=&', 'workgroupId=-1&')
                    generateReport(queryString);        }
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
                            <td nowrap><label>Credit Hire Organisation</label></td>
                            <td>
                                <s:select name="supplierId" id="supplierId" list="suppliers"
                                          listKey="id" listValue="name" headerKey="" headerValue="-- Please Select --" emptyOption="false">
                                </s:select>
                            </td>
                        </tr>
                    </s:if>

                    <tr>
                        <td nowrap width="30%"><label>Invoice Uploaded From</label></td><td><div id="dateFromDiv" /></td>
                    </tr>
                    <tr>
                        <td nowrap><label>Invoice Uploaded To</label></td><td><div id="dateToDiv"/></td>
                    </tr>
                    <tr>
                        <td nowrap><label>Supplier Reference(s)<br/><br/><font size="1">(Supplier Reference Number input<br/>format: ABC123, ABC124, ABC125)</font></label></td><td><textarea cols="20" rows="5" id="supplierReferences" name="supplierReferences"></textarea><!--img id="help-supplier-reference-input" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt="Help"/--></td>
                    </tr>

                </table>

                <div class="chox-report-button">
                    <button type="button" id="invoiceReportGenerateReportId" onclick="javascript:openInvoiceReport();">Generate Report</button>
                </div>

            </div>

            <div id="formInvoiceReportParamMessageBox" class="action-error-msg"></div>

        </div>

    </form>

</fieldset>
