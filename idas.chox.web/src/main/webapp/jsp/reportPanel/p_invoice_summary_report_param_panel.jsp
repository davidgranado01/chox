<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'InvoiceSummaryReport-Excel';
    
    Ext.onReady(function(){
        ui.dateField('DateStart',getTodayDate(),'dateFromDiv');
        ui.dateField('DateEnd',getTodayDate(),'dateToDiv');

        $("form#formReportParam").validate(
        {
            errorLabelContainer: "#formReportParamMessageBox",
            rules: {
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


    <s:if test="isCHO" >


            var insurersJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'text'},
                    {name:'value'}
                ]
            });

            var myinsurers = Ext.util.JSON.decode('<s:property value="insurersJsonString" escape="false"/>');
            var insurersStore = new Ext.data.Store({
                data : myinsurers,
                reader : insurersJsonReader
            });


            var insurerCombo = new Ext.form.ComboBox({
                store : insurersStore,
                id : 'ISRPPInsurerComboId',
                renderTo: 'invoiceSummeryReportInsurerDropDownDiv',
                width: 220,
                valueField : 'text',
                hiddenName: 'insurerId',
                displayField :'value',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                emptyText : '--- ALL ---',
                selectOnFocus : false,
                forceSelection : true,
                allowBlank : true,
                listeners: { blur: function () {
                        if(this.getRawValue() == "" ) {
                            this.clearValue();

                        }
                    }
                }
            });


    </s:if>

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

            var mysuppliers = Ext.util.JSON.decode('<s:property value="suppliersJsonString" escape="false"/>');
            var suppliersStore = new Ext.data.Store({
                data : mysuppliers,
                reader : suppliersJsonReader
            });

            var supplierCombo = new Ext.form.ComboBox({
                store : suppliersStore,
                id : 'ISRPPSupplierCombo',
                renderTo: 'invoiceSummeryReportSupplierDropDownDiv',
                width: 220,
                valueField : 'text',
                hiddenName: 'supplierId',
                displayField :'value',
                typeAhead : true,
                mode : 'local',
                triggerAction : 'all',
                emptyText : '--- ALL ---',
                selectOnFocus : false,
                allowBlank : true,
                forceSelection : true,
                listeners: { blur: function () {
                        if(this.getRawValue() == "" ) {
                            this.clearValue();
                        }
                    }
                }
            });

    </s:if>


        });
    
        function openReport()
        {
            if($("form#formReportParam").valid()){
                var queryString = $('#formReportParam').formSerialize();
                generateReport(queryString);
            }
        }
    
    
</script>

<fieldset class="x-fieldset">

    <legend>Invoice Summary Report</legend>

    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">

        <div class="x-panel-bwrap chox-form-container">

            <div class="form-container">

                <div class="instruction-message">
                    This report provides information at a high level regarding the financials of CHOX invoices, including details relating to penalty charges as a result of late payments. The dates that require selection below refer to the date the invoice was uploaded onto CHOX.
                </div>

                <table class="report-form">
                    <s:if test="!isCHO">
                        <tr>
                            <td nowrap><label>Credit Hire Organisation</label></td>

                            <td>
                                <div id="invoiceSummeryReportSupplierDropDownDiv"></div>
                            </td>
                        </tr>
                    </s:if>
                    <s:else>
                        <tr>
                            <td nowrap><label>Insurer</label></td>
                            <td>
                                <div id="invoiceSummeryReportInsurerDropDownDiv"></div>
                            </td>
                        </tr>
                    </s:else>
                    <tr>
                        <td nowrap width="30%"><label>Invoice Uploaded From</label></td><td><div id="dateFromDiv" /></td>                       
                    </tr>    
                    <tr>
                        <td nowrap><label>Invoice Uploaded To</label></td><td><div id="dateToDiv"/></td>                            
                    </tr>                      
                </table>

                <div class="chox-report-button">
                    <button type="button" id="ISRPPGenerateReportButtonId"onclick="javascript:openReport();">Generate Report</button>
                </div>
            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div>
    </form>
</fieldset>