<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    Ext.onReady(function() {
        var form = $("form#formUpdateInvoiceReviewRequired");
        choxJqueryHttpSubmit(form, function(){Ext.get('formUpdateInvoiceReviewRequired').mask("Reloading Claim...");});
    });
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/updateInvoiceReviewRequired.action" method="post" id="formUpdateInvoiceReviewRequired" name="formUpdateInvoiceReviewRequired">
        <fieldset class="x-fieldset">
            <legend>Invoice Review Required</legend>
            <s:hidden id="claimId" name="id" />
            <div class="status-info">
                    Setting the status to 'Invoice Review Required' will flag the invoice for review regardless of the business rules engine result.
            </div>
            <div>
                <div class="status-control-set">
                    <table class="status-table">
                        <tr>
                            <td align="right">
                                <label>Invoice Review Required?</label>
                            </td>
                            <td nowrap>
                                <s:checkbox id="MAUIRRisInvoiceReviewRequiredId" name="isInvoiceReviewRequired" />
                            </td>
                            <td>
                            </td>
                            <td>
                            </td>
                        </tr>
                        <tr>
                          <td align="right">
                              <input type="submit" id="MAUIRRisInvoiceReviewRequiredButtonId"value="Update"/>
                          </td>  
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="MAUIRRInvoiceReviewRequiredmessageBox"></div>
            </div>
        </fieldset>
    </form>
</div>