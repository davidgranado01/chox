<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){

        $("form#formClaimReviewByEngAction").validate(
        {
            errorLabelContainer: "#formClaimReviewByEngActionMessageBox",
            rules: {
                indemnityAmount:{
                    required:true,
                    number:true
                },
                percentageLiabilityAccepted:{
                    required:true,
                    number:true,
                    max: 100.00,
                    min: 0.01
                }
            },
            messages: {
                indemnityAmount: {
                    required:"You must supply a value for 'Indemnity'",
                    number:"You must supply a numeric value for 'Indemnity'"
                },
                percentageLiabilityAccepted: {
                    required:"You must supply a value for 'Percentage Liability Accepted'",
                    number:"You must supply a numeric value for 'Percentage Liability Accepted'",
                    max:"'Percentage Liability Accepted' cannot be more than 100",
                    min:"'Percentage Liability Accepted' must be more than or equal to 0.01"
                }
            }
        });
    });

    function doClaimReviewByEngFormSubmit(action){
        actionPanel.registerAction(action);
        if($("#formClaimReviewByEngAction").valid()){
            $("form#formClaimReviewByEngAction").submit();
        }
    }

</script>

<form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formClaimReviewByEngAction" name="formClaimReviewByEngAction">
    <fieldset class="x-fieldset">
        <legend>Engineer Review - Action Required</legend>
        <div>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="name" name="name" />
            <div>
                <div class="status-info">
                    Please enter your private notes in the 'Claim Review Notes' box and add public notes in the 'Notes' tab in order to communicate detailed comments you may have for the CHO.  Click on the 'Submit' button to progress the claim without updating a Claims Handler, use the 'Update Claims Handler' button to notify a Claims Handler of the note/action made.
                </div>
                <div class="status-control-set">
                    <table class="status-table">
                        <tr>
                            <td>
                                <label>
                                    Indemnity (Decimal)<span class="mandatory">*</span></label></td><td>
                                <input type="text" class="chox-ttxt" id="indemnityAmount" name="indemnityAmount" value="<s:property value="indemnityAmount" />"/>
                            </td>
                            <td>
                                <label>
                                    Invoice Review Required?</label></td><td>
                                    <s:checkbox name="isInvoiceReviewRequired" />
                            </td>                            
                        </tr>
                        <tr>
                            <td>
                                <label>
                                    Claim Number<span class="mandatory">*</span></label></td><td>
                                <input type="text" class="chox-ttxt-readonly" id="claimNumber" name="claimNumber" value="<s:property value="claimNumber" />" readonly="TRUE"/>

                            </td>
                            <td>
                                <label>
                                    Quantum Dispute?</label></td><td>
                                    <s:checkbox name="isQuantumDispute" />
                            </td>
                        </tr>
                        <tr valign="top">
                            <td>
                                <label>
                                    % Liability Accepted<span class="mandatory">*</span></label></td><td colspan="3">
                                <input type="text" class="chox-ttxt" id="percentageLiabilityAccepted" name="percentageLiabilityAccepted" value="<s:property value="percentageLiabilityAccepted" />"/>
                            </td>
                        </tr>
                        <tr valign="top">
                            <td>
                                <label>
                                    Claim Review Notes</label></td><td colspan="3">
                                <textarea class="chox-canote" cols="20" rows="5" name="engineerClaimReviewNotes" id="engineerClaimReviewNotes"><s:property value="engineerClaimReviewNotes" /></textarea>
                            </td>
                        </tr>                        
                        <tr>
                            <td>
                                <div class="no-format">
                                    <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" class="choice"> 
                                <input type="button" value="Acknowledge" onclick="doClaimReviewByEngFormSubmit('acknowledgeClaim')"  />
                                <input type="button" value="Update Claims Handler" onclick="doClaimReviewByEngFormSubmit('updatedByEng')"  />
                            </td>
                        </tr>
                    </table>
                    <div id="formClaimReviewByEngActionMessageBox" class="action-error-msg"></div>
                </div>
            </div>
        </div>
    </fieldset>
</form>