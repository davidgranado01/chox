<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    var finalReviewRequiredOrig = -1;

    Ext.onReady(function() {

        finalReviewRequiredOrig = $("#finalReviewRequiredOrigId").val();

        if (finalReviewRequiredOrig == 'true') {
//            $("#finalReviewReasonId").prop('disabled', true);
            $("#finalReviewReasonId").attr('disabled', true);
        }


        // SETUP FORM VALIDATION
        var form = $("form#formUpdateFinalReview");
        if (finalReviewRequiredOrig != 'true') {
            form.validate(
            {
                errorLabelContainer: "#UpdateFinalReviewMessageBox",
                rules: {
                    finalReviewRequired : {required:true},
                    finalReviewReason:{required:true}
                },
                messages: {
                    finalReviewRequired: {required:"You must select 'Is this the final time you will review the claim?'"},
                    finalReviewReason: {required:"You must select a 'Final Review Reason'"}
                }
            });
        }
        
        choxJqueryHttpSubmit(form, function(){});

    });
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/updateFinalReview.action" method="post" id="formUpdateFinalReview" name="formUpdateFinalReview">
        <fieldset class="x-fieldset">
            <legend>Final Review</legend>
            <s:hidden id="claimId" name="id" />
            <input type="hidden" id="finalReviewRequiredOrigId" name="finalReviewRequiredOrig" value="<s:property value="finalReviewRequired"/>">
            <div class="status-info">
                Once you have made a final decision on this claim/invoice and do not wish to review this
                claim/invoice again, please tick the box then select the relevant ‘Final Review Reason’ from the
                dropdown box and click ‘Update’. This will add a Private Note to the claim and move
                the claim from the ‘Approved Invoices Awaiting Liability Resolution’ queue to the
                ‘Invoices With Final Review’ queue.
            </div>
            <div class="status-control-set">
                <table class="status-table" width="100%">
                    <tr>
                        <td align="right" width="10%"><label>Is this the final time you will review the claim?</label></td> 
                        <td width="20%"><s:checkbox id="finalReviewRequiredId" name="finalReviewRequired" /></td>
                        <td width="70%"></td>
                    </tr>
                    <tr>
                        <td align="right" width="10%"><label>Final Review Reason</label></td>
                        <td width="20%">
                            <s:select
                                name="finalReviewReason"
                                id="finalReviewReasonId"
                                list="finalReviewReasons"
                                headerKey=""
                                listKey="text"
                                listValue="value"
                                headerValue="-- Please Select --">
                            </s:select>
                        </td>
                        <td width="70%"></td>
                    </tr>
                    <tr>
                        <td>
                            <input id="assign" type="submit" value="Update"/>
                        </td>
                    </tr>
                </table>
                <div class="action-error-msg" id="UpdateFinalReviewMessageBox"></div>
            </div>
        </fieldset>
    </form>
</div>
