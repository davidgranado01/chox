<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var reportName = 'BreInvoiceApprovalDisputeReport-Excel';
    
    
    Ext.onReady(function(){
        ui.unvalidatedDateField('DateStart',getTodayDate(),'dateFromDiv');
        
    <s:if test="isInsurer" >

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
                renderTo: 'searchBreScreenSupplierDropDownDiv',
                width: 220,
                valueField : 'text',
                id : 'supplierComboId',
                displayField :'value',
                hiddenName:'supplierId',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                emptyText : '--- ALL ---',
                allowBlank : true,
                forceSelection : true,
                listeners: { blur: function () {
                        if(this.getRawValue() === "" ) {
                            this.clearValue();
                        }
                    }
                }
            });
    </s:if>
    
            $("form#formReportParam").validate(
            {
                errorLabelContainer: "#formReportParamMessageBox",
                rules: {
                    DateStart:{
                        required:true,
                        dateITA: true
                    }
                },
                messages: {
                    DateStart: {
                        required:"A value must be supplied for 'Start Date'",
                        dateITA:"You must supply valid date format for 'Start Date'"
                    }
                }
            });

        
        });

        function openReport()
        {
            if($("form#formReportParam").valid()){
//                var queryString = $('#formReportParam').formSerialize();
                var queryString = {};
                $.each($('#formReportParam').serializeArray(), function() {queryString[this.name] = this.value;});
                generateReport(queryString);
            }
        }


</script>

<fieldset class="x-fieldset">

    <legend>BRE Invoice Approval & Dispute Report</legend>

    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">

        <div class="x-panel-bwrap chox-form-container">

            <div class="form-container">

                <div class="instruction-message">
                    This report provides an insight into the the number of invoices that pass the business rules and the resulting action taken, looking at whether the invoices are being paid or disputed.  This report also provides a breakdown of why invoices are being disputed after being approved by the business rules.  The report runs for the last 12 months to date, the start date determines the 12 months to run the report to.
                </div>

                <table class="report-form">
                    <s:if test="!isCHO">
                        <tr>
                            <td nowrap><label>Credit Hire Organisation</label></td>
                            <td><div id="searchBreScreenSupplierDropDownDiv"></div></td>
                        </tr>
                    </s:if>
                    <tr>
                        <td nowrap width="30%"><label>Start Date</label><span class="mandatory">*</span></td><td><div id="dateFromDiv" /></td>
                    </tr>
                </table>

                <div class="chox-report-button">
                    <button type="button" id="BIADRPGenerateReportId" onclick="javascript:openReport();">Generate Report</button>
                </div>
            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div>
    </form>
</fieldset>