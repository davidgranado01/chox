<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'PaymentReport-Excel';

    Ext.onReady(function(){

        <s:if test="insurerIsWorkgroupEnabled" >

            var choPaymentWorkgroupJsonReader = new Ext.data.JsonReader({
                                totalProperty: 'totalCount',
                                root: 'results',
                                fields:
                                [
                                    {name:'text'},
                                    {name:'value'}
                                ]
            });

            var choPaymentWorkgroupStore = new choxDataStore({
                                url : "/prv/p/WorkgroupDropDownActionByInsurer2.action",
                                reader : choPaymentWorkgroupJsonReader
            });

            var choPaymentWorkgroupCombo = new Ext.form.ComboBox({
                                store: choPaymentWorkgroupStore,
                                renderTo: 'rptPaymentWorkgroupSelectionHolder',
                                valueField: 'text',
                                width: 220,
                                id: 'choPaymentWorkgroupComboId',
                                hiddenName: 'workgroupId',
                                displayField:'value',
                                typeAhead: true,
                                autoWidth: true,
                                mode: 'local',
                                emptyText: '--- All ---',
                                triggerAction : 'all',
                                forceSelection : true,
                                listeners: {blur: function () {
                                                if(this.getRawValue() == "" ) {
                                                    this.clearValue();
                                                }
                                           }
                                }
            });
            choPaymentWorkgroupStore.load();
        </s:if>

        $("form#formReportParam").validate(
        {
            errorLabelContainer: "#formReportParamMessageBox",
            rules: {
                supplierId:{
                    required:true
                }

            },
            messages: {
                supplierId:{
                    required:"You must select 'Credit Hire Organisation'"
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

        var mysuppliers = Ext.util.JSON.decode('<s:property value="suppliersJsonString" escape="false"/>');
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
                    if(this.getRawValue() == "" ) {
                        this.clearValue();
                        this.reset();
                    }
                }
            }
        });
    
        </s:if>

    }); 

    function openReport()
    {
    	var msgBox = $('#formReportParamMessageBox');
    	if(Ext.get('supplierCombo').getValue() == "--- Please Select ---"){
            msgBox.empty();
            msgBox.text("You must select 'Credit Hire Organisation'").append('<br/>').show();
        }
        if($("form#formReportParam").valid() && Ext.get('supplierCombo').getValue() != "--- Please Select ---"){
//            var queryString = $('#formReportParam').formSerialize();
            var queryString = {};
            $.each($('#formReportParam').serializeArray(), function() {queryString[this.name] = this.value;});
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
    <legend>CHO Payment Bordereau</legend>
    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report produces a list of claims that require payment. The report results are per CHO and allow an Insurer to make payments in a more efficient manner.<br/>
                                                 Please note CHOs setup for Insurer uploaded claims and invoices are excluded from this report.
                </div>

                <table class="report-form">

                    <s:if test="!isCHO && !isCH && insurerIsWorkgroupEnabled">
                        <tr>
                            <td nowrap><label>Workgroup</label></td>
                            <td>
                                <div id="rptPaymentWorkgroupSelectionHolder"></div>
                            </td>
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
                </table>

                <div class="chox-report-button">
                    <button type="button" id="PRPPGenerateReportId" onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>
            <div id="formReportParamMessageBox" class="action-error-msg"></div>
        </div></form>
</fieldset>
