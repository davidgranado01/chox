<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){
        createHelpNote();
        $("form#formClaimReviewByEngAction").validate(
        {
            errorLabelContainer: "#formClaimReviewByEngActionMessageBox",
            rules: {
                indemnityAmount: {
                    required:true,
                    number:true
                }
            },
            messages: {
                indemnityAmount: {
                    required:"You Must Supply A Valid 'Indemnity Value'",
                    number:"You Must Supply A Numeric Value For 'Indemnity Value'"
                }
            }
        });
    });

    function doClaimReviewByEngFormSubmit(action){
        actionPanel.registerAction(action);
        if (action=='acknowledgeClaim') {
            $("form#formClaimReviewByEngAction #claimNumber").rules("add", {
                required: true,
                messages: {required: "You Must Supply A Valid Claim Number"}
            });

        } else {
            $("form#formClaimReviewByEngAction #claimNumber").rules("remove");
        }

        if($("#formClaimReviewByEngAction").valid()){
            $("form#formClaimReviewByEngAction").submit();
        }
    }
    
    function createHelpNote(){
        var note = $('#liabilityStatusHelpNotes').html();
        new Ext.ToolTip({
            target: 'liabilityStatusHelp',
            html: note,
            title: 'Liability Status',
            autoHide: false,
            closable: true,
            draggable:true
        });

        Ext.QuickTips.init();
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
                        Click on the ‘Update Claims Handler’ button to notify a Claims Handler of the note/action made. On clicking this button any notes entered into the ‘Claim Review Notes’ section will be private and not visible to the CHO. Alternatively, click on the ‘Acknowledge’ button to progress the claim without updating a Claims Handler. On clicking this button any notes entered into the ‘Claim Review Notes’ section will be public and visible to the CHO.
                    </div>
                    <div class="status-control-set">
                        <table class="status-table">
                            <tr>
                                <td>
                                    <label>Claim Number</label>
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
                                    <label>Liability Status</label>
                                    <img src="../images/help.png" id="liabilityStatusHelp" alt=""/>
                                </td>
                                <!--
                                <td><div id="liabilityStatusDropDownDiv" ></div></td>
                                -->
                                <td>
                                    <input type="text" class="chox-ttxt-readonly" readonly="true"  name="showliabilityStatus"  value="<s:property value="liabilityStatus" />"/>
                                    <input type="hidden"  name="liabilityStatus" value="<s:property value="liabilityStatus.getLiablityValue()" />"/>

                                </td>
                                <td colspan="2">
                                    <label></label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label>Liability Percentage Agreed (<b>Insurer</b>)</label>

                                </td>
                                <td>
                                    <input type="text"  class="chox-ttxt-readonly" readonly="true" name="percentageLiabilityAccepted" id="percentageLiabilityAccepted" value="<s:property value="percentageLiabilityAccepted" />"/>
                                </td>
                                <td>
                                    <label>
                                        Liability Percentage Agreed (<b>CHO</b>)</label>
                                </td>
                                <td>
                                    <input type="text" class="chox-ttxt-readonly" readonly="true" name="percentageLiabilityCho" id="percentageLiabilityCho" value="<s:property value="percentageLiabilityCho" />"/>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label>Date Liability Agreed</label>
                                </td>
                                <td><input type="text" class="chox-ttxt-readonly" readonly="true" name="liabilityAgreedDate" id="liabilityAgreedDate" value="<s:property value="liabilityAgreedDate" />"/></td>
                                <td colspan="2">
                                    <label></label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label>Indemnity Value <span class="mandatory">*</span></label>
                                </td>
                                <td>
                                    <input type="text" id="RBELindemnityAmountId" class="chox-ttxt" name="indemnityAmount" value="<s:property value="indemnityAmount" />"/>
                                </td>
                                <td colspan="2">
                                    <label></label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>Invoice Review Required?</label>
                                </td>
                                <td>
                                    <s:checkbox id="RBELisInvoiceReviewRequiredId" name="isInvoiceReviewRequired" />
                                </td>
                                <td colspan="2"></td>
                            </tr>
                            <tr valign="top">
                                <td>
                                    <s:if test="isDisablePrivateNotes">
                                        <label>Claim Review Notes (public)</label>
                                    </s:if>
                                    <s:else>
                                        <label>Claim Review Notes</label>
                                    </s:else>
                                </td>
                                <td colspan="3">
                                    <textarea class="chox-canote" id="RBELengineerClaimReviewNotesId" cols="80" rows="5" name="engineerClaimReviewNotes"><s:property value="engineerClaimReviewNotes" /></textarea>
                                    <label class="std-label-ro-small" style="padding-top: 0px; vertical-align: top; height:30px">
                                    <s:if test="!isDisablePrivateNotes">
                                        N.B. The above note will be ’Public’ if you are acknowledging the claim and ‘Private’ if you are updating the Claims Handler.
                                    </s:if>
                                    </label>
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
                                    <input type="button" id="RBELAcknowledgeButtonId" value="Acknowledge" onclick="doClaimReviewByEngFormSubmit('acknowledgeClaim')"  />
                                    <input type="button" id="RBELUpdateClaimsHandlerButtonId" value="Update Claims Handler" onclick="doClaimReviewByEngFormSubmit('updatedByEng')"  />
                                </td>
                            </tr>
                        </table>
                        <div id="formClaimReviewByEngActionMessageBox" class="action-error-msg"></div>
                    </div>
                </div>
            </div>
        </fieldset>
        <%@ include file="s_liability_tooltip_notes.jspf" %>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    </form>
</div>