<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    var reportName = 'AdminWeeklyOverviewReport-Excel';
    
    Ext.onReady(function(){
        
        var dateField = new Ext.form.DateField({
            name: 'DateStart',
            id: 'DateStartAWRId',
            width: 100,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true,
            validationEvent : false,
            //                        value: getTodayDate(),
            renderTo: 'dateFromDiv',
            disabledDays: [0,2,3,4,5,6] 
        });
        
        
        var dateField = new Ext.form.DateField({
            name: 'DateEnd',
            id: 'DateEndAWRId',
            width: 100,
            allowBlank: true,
            format: 'd/m/Y',
            showWeekNumber: true,
            validationEvent : false,
            //            value: defaultValue,
            renderTo: 'dateToDiv',
            disabledDays: [1,2,3,4,5,6] 
            
        });
        

        //        ui.dateField('DateStart',getTodayDate(),'dateFromDiv');
        //        ui.dateField('DateEnd',getTodayDate(),'dateToDiv');

        $("#formReportParam").validate(
        {
            errorLabelContainer: "#formReportParamMessageBox",
            rules: {
            	
                DateStart:{required:true, dateITA:true, max:function(){
                	var sd = Ext.get('DateStartAWRId').getValue().split("/");
                    var ed = Ext.get('DateEndAWRId').getValue().split("/");
                    var time = new Date(sd[2],sd[1] - 1 ,sd[0]).getTime() - new Date(ed[2],ed[1] - 1 ,ed[0]).getTime();
	                if(time > 0)
                        return true;
	                }
                },
                DateEnd:{required:true, dateITA:true}
            },
            messages: {
                DateStart: {required:"A value must be supplied for 'Date From'", dateITA:"You must supply valid date format for 'Date From'", max:"'Date To' can't be before 'Date From'"},
                DateEnd: {required:"A value must be supplied for 'Date To'", dateITA:"You must supply valid date format for 'Date To'"
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
                renderTo: 'adminWeeklyOverviewInsurerDropDownDiv',
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
                renderTo: 'adminWeeklyOverviewSupplierDropDownDiv',
                width: 220,
                valueField : 'text',
                hiddenName: 'supplierId',
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

    <legend>Admin Weekly Overview Report</legend>

    <form id="formReportParam" class="XXentity-form" name="formReportParam" action="POST">

        <div class="x-panel-bwrap chox-form-container">

            <div class="form-container">

                <div class="instruction-message">
                    This report provides an overview of claims activity reported by weekly segments, exposing both a historical and current position regarding CHOX claims. The dates that require selection below refer to the dates you wish the report to include, remember the report is based on weekly segments with weeks running from Monday to Sunday.
                </div>

                <table class="report-form">
                    <tr>
                        <s:if test="isInsurer">
                            <td nowrap><label>Credit Hire Organisation</label></td>
                            <td>
                                <div id="adminWeeklyOverviewSupplierDropDownDiv"></div>
                            </td>
                        </s:if>
                        <s:if test="isCHO">
                            <td nowrap><label>Insurer</label></td>

                            <td>
                                <div id="adminWeeklyOverviewInsurerDropDownDiv"></div>
                            </td>
                        </s:if>
                    </tr>
					<tr>
						<td nowrap width="30%"><label>Date From</label><span
							class="mandatory">*</span></td>
						<td><div id="dateFromDiv" /></td>
					</tr>
					<tr>
						<td nowrap><label>Date To</label><span class="mandatory">*</span></td>
						<td><div id="dateToDiv" /></td>
					</tr>
				</table>

                <div class="chox-report-button">
                    <button type="button" id="AWOPPGenerateReportId" onclick="javascript:openReport();">Generate Report</button>
                </div>

            </div>

            <div id="formReportParamMessageBox" class="action-error-msg"></div>

        </div>

    </form>

</fieldset>