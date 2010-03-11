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
                }
            },
            messages: {
                indemnityAmount: {
                    required:"You must supply a value for 'Indemnity'",
                    number:"You must supply a numeric value for 'Indemnity'"
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

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formClaimReviewByEngAction" name="formClaimReviewByEngAction">
        <fieldset class="x-fieldset">
            <legend>Engineer Review - Action Required</legend>
            <div>
                <s:hidden id="claimId" name="id" />
                <s:hidden id="name" name="name" />
                <div>
                    <div class="status-info">
                        Please enter your private notes in the ‘Claim Review Notes’ box and add public notes in the ‘Notes’ tab in order to communicate detailed comments you may have for the CHO. Click on the ‘Acknowledge’ button to progress the claim without updating a Claims Handler, use the ‘Update Claims Handler’ button to notify a Claims Handler of the note/action made.
                    </div>
                    <div class="status-control-set">
                        <table class="status-table">
                            <tr>
                                <td>
                                    <label>Claim Number <span class="mandatory">*</span></label>
                                </td>
                                <td>
                                    <input type="text" class="chox-ttxt" id="claimNumber" name="claimNumber" value="<s:property value="claimNumber" />"/>
                                </td>
                                <td colspan="2">
                                    <label></label>
                                </td>

                            </tr>

                            <tr>
                                <td width="20%">
                                    <label>Liability Status
                                        <span class="mandatory">*</span>
                                    </label>
                                    <img src="../images/help.png" id="liabilityStatusHelp" alt=""/>
                                </td>
                                <!--
                                <td><div id="liabilityStatusDropDownDiv" ></div></td>
                                -->
                                <td>
                                    <input type="text" readonly="true" class="chox-ttxt"  name="showliabilityStatus"  value="<s:property value="liabilityStatus" />"/>
                                    <input type="hidden"  name="liabilityStatus" value="<s:property value="liabilityStatus.ordinal()" />"/>

                                </td>
                                <td colspan="2">
                                    <label></label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label>
                                        Liability Percentage Agreed(Insurer)</label>

                                </td>
                                <td>
                                    <input type="text" readonly="true" class="chox-ttxt"  name="percentageLiabilityAccepted" id="percentageLiabilityAccepted" value="<s:property value="percentageLiabilityAccepted" />"/>
                                </td>
                                <td>
                                    <label>
                                        Liability Percentage Agreed(CHO)</label>
                                </td>
                                <td>
                                    <input type="text" readonly="true" class="chox-ttxt"  name="percentageLiabilityCho" id="percentageLiabilityCho" value="<s:property value="percentageLiabilityCho" />"/>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label>Date Liability Agreed</label>
                                </td>
                                <td><input type="text" readonly="true" class="chox-ttxt" name="liabilityAgreedDate" id="liabilityAgreedDate" value="<s:property value="liabilityAgreedDate" />"/></td>
                                <td colspan="2">
                                    <label></label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label>
                                        Indemnity Value</label>
                                </td>
                                <td>
                                    <input type="text" class="chox-ttxt" name="indemnityAmount" value="<s:property value="indemnityAmount" />"/>
                                </td>
                                <td colspan="2">
                                    <label></label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>
                                        Invoice Review Required?</label>
                                </td>
                                <td>
                                    <s:checkbox name="isInvoiceReviewRequired" />
                                </td>
                                <td colspan="2"></td>
                            </tr>
                            <tr valign="top">
                                <td>
                                    <label>Claim Review Notes</label>
                                </td>
                                <td colspan="3">
                                    <textarea class="chox-canote" cols="80" rows="5" name="engineerClaimReviewNotes"><s:property value="engineerClaimReviewNotes" /></textarea>
                                </td>
                            </tr>
 
                            <tr>
                                <td colspan="4">
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
</div>