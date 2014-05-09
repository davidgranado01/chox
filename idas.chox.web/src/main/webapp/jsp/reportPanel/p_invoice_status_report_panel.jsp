<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var reportName = 'InvoiceStatusReport-Excel';


    Ext.onReady(function(){
        ui.dateField('DateStart',getTodayDate(),'startDateDiv');

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
                renderTo: 'invoiceStatusInsurerDropDownDiv',
                id : 'ISRPInsurerComboId',
                width: 220,
                valueField : 'text',
                hiddenName: 'insurerId',
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
                renderTo: 'invoiceStatusSupplierDropDownDiv',
                id : 'ISRPSupplierComboId',
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
                        required:"A value must be supplied for 'Create Date'",
                        dateITA:"You must supply a date value for 'Create Date'"
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
    <legend>Invoice Status Report</legend>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <s:if test="!isCHO">
                    <div class="instruction-message">This report provides an insight into the status of all invoices uploaded during the selected period including the value of the invoices at the various statuses.  The start date below determines the month to run the report from which will include the selected month and the preceding 11 months.</div>
                </s:if>
                <s:else>
                    <div class="instruction-message">This report provides an insight into the status of all invoices uploaded during the selected period including the value of the invoices at the various statuses.  The start date below determines the month to run the report from which will include the selected month and the preceding 11 months.</div>
                </s:else>
                <table class="report-form">


                    <s:if test="!isCHO">
                        <tr>
                            <td nowrap><label>Credit Hire Organisation</label></td>
                            <td>
                                <div id="invoiceStatusSupplierDropDownDiv"></div>
                            </td>
                        </tr>
                    </s:if>
                    <s:else>
                        <tr>
                            <td nowrap><label>Insurer</label></td>
                            <td>
                                <div id="invoiceStatusInsurerDropDownDiv"></div>
                            </td>
                        </tr>
                    </s:else>

                    <tr>
                        <td nowrap><label>Create Date</label><span class="mandatory">*</span></td>
                        <td>
                            <div id="startDateDiv" />
                        </td>
                    </tr>

                </table>

                <div class="chox-report-button">
                    <button type="button" id="ISRPGenerateReportId" onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div></form>
</fieldset>
