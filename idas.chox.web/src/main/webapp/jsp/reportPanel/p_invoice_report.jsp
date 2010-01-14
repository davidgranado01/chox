<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var reportName = 'InvoiceReport-Excel';

    $(document).ready(function(){

        ui.dateField('DateStart',getTodayDate(),'dateFromDiv');
        ui.dateField('DateEnd',getTodayDate(),'dateToDiv');

        new Ext.ToolTip({ target: 'help-supplier-reference-input', html: 'Supplier Reference Number input format: ABC123, ABC124, ABC125'});

        var target = "div#rptInvoiceWorkgroupSelectionHolder";
        var url = "<%=request.getContextPath()%>/prv/p/GetWorkgroupOnlyDropDownActionByInsurer.action";
        var param = {};

        ajax.loadHtml(url,param,function(data){
            $(target).html(data);
        });

        $("form#formInvoiceReportParam").validate(
        {
            errorLabelContainer: "#formInvoiceReportParamMessageBox",
            rules: {
                supplierId:{
                    required:true
                },
                DateStart:{
                    required:true,
                    date: true
                },
                DateEnd:{
                    required:true,
                    date: true
                }
            },
            messages: {
                supplierId:{
                    required:"You must select 'Credit Hire Organisation'"
                },
                DateStart: {
                    required:"A value must be supplied for 'Invoice Uploaded From'",
                    date:"You must supply a date value 'Invoice Uploaded From'"
                },
                DateEnd: {
                    required:"A value must be supplied for 'Invoice Uploaded To'",
                    date:"You must supply a date value 'Invoice Uploaded To'"
                }
            }
        });

    });
    
    function openInvoiceReport()
    {
        if($("form#formInvoiceReportParam").valid()){
            var queryString = $('form#formInvoiceReportParam').formSerialize();
            window.location= "<%=request.getContextPath()%>/prv/p/exportExcelReport.action?" + "reportName=" + reportName + "&" + queryString;
        }
    }

</script>
<fieldset class="x-fieldset">

    <legend>CHO Invoice Report</legend>
    <form id="formInvoiceReportParam" name="formInvoiceReportParam" class="XXentity-form" action="POST">


        <div class="x-panel-bwrap chox-form-container">
            <div class="form-container">

                <div class="instruction-message">This report allows a user to produce an invoice for a particular claim or set of claims. Invoices are produced per Credit Hire Organisation and are based on specific Supplier Reference Number(s) and/or specifying a date range based on the invoice upload date.</div>

                <table class="report-form">

                    <s:if test="!isCHO && !isCH">
                        <tr>
                            <td nowrap><label>Workgroup</label></td>
                            <td><div id="rptInvoiceWorkgroupSelectionHolder"></div></td>
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
                        <td nowrap><label>Supplier Reference(s)</label></td><td><textarea cols="20" rows="5" id="supplierReferences" name="supplierReferences"></textarea><img id="help-supplier-reference-input" class="help-icon" src="<%= request.getContextPath()%>/images/help.png" alt="Help"/></td>
                    </tr>

                </table>

                <div class="chox-report-button">
                    <button type="button" onclick="javascript:openInvoiceReport();">Generate Report</button>
                </div>

            </div>

            <div id="formInvoiceReportParamMessageBox" class="action-error-msg"></div>

        </div>

    </form>

</fieldset>
