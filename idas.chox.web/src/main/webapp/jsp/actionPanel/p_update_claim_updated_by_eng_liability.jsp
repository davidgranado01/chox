<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ include file="s_liability_validation.jspf" %>
<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formAcknowledgeAction" name="formAcknowledgeAction">

        <fieldset class="x-fieldset">
            <legend>Claim Acknowledgement - Action Required</legend>
            <div>
                <s:hidden id="claimId" name="id" />
                <s:hidden id="name" name="name" />
                <input name="currentVersion" type="hidden" value="<s:property value="version" />" />
                <s:hidden id="isClaimNumberValidFlag" name="isClaimNumberValidFlag" value="1"/>
                <div>
                    <div class="status-info">
                        <s:if test="insurerIsEngineersEnabled">
                            Please enter details of the claim and decide whether to acknowledge the claim, refer the claim to an engineer, reject the claim or set the claim to pending. You can enter private notes in the 'Claim Review Notes' box and add public notes in the 'Notes' tab in order to communicate detailed comments you may have for the CHO.
                        </s:if>
                        <s:else>
                            Please enter details of the claim and decide whether to acknowledge the claim, reject the claim or set the claim to pending. You can enter private notes in the 'Claim Review Notes' box and add public notes in the 'Notes' tab in order to communicate detailed comments you may have for the CHO.
                        </s:else>
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
                                    <s:select
                                        id="liabilityStatus"
                                        name="liabilityStatus"
                                        list="liabilityStatusDropDownMap"
                                        emptyOption="false"
                                        value="liabilityStatus.ordinal()"  >
                                    </s:select>
                                </td>
                                <td colspan="2">
                                    <label></label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label>
                                        Liability Percentage Agreed (<b>Insurer</b>)</label>

                                </td>
                                <td>
                                    <input type="text" class="chox-ttxt" name="percentageLiabilityAccepted" id="percentageLiabilityAccepted" value="<s:property value="percentageLiabilityAccepted" />"/>
                                </td>
                                <td>
                                    <label>
                                        Liability Percentage Agreed (<b>CHO</b>)</label>
                                </td>
                                <td>
                                    <input type="text" class="chox-ttxt" name="percentageLiabilityCho" id="percentageLiabilityCho" value="<s:property value="percentageLiabilityCho" />"/>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label>Date Liability Agreed</label>
                                </td>
                                <td><div id="liabilityAgreedDateDiv"></div></td>
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
                            <tr valign="top">
                                <td>
                                    <label>Reason for Rejection</label>
                                </td>
                                <td colspan="3">
                                    <div id="ReasonOfRejectionDiv">
                                        <s:select
                                            name="reasonOfRejectionId"
                                            id="reasonOfRejectionId"
                                            list="reasonOfClaimRejections"
                                            listKey="id"
                                            listValue="name"
                                            headerKey=""
                                            headerValue="N/A"
                                            emptyOption="false"></s:select>
                                    </div>
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
                                <td colspan="4" class="choice" nowrap>
                                    <input type="button" value="Reject" onclick="doAcknowledgeFormSubmit('rejectClaim');" />
                                    <input type="button" value="Acknowledge" onclick="doAcknowledgeFormSubmit('acknowledgeClaim')"  />
                                    <s:if test="insurerIsEngineersEnabled">
                                        <input type="button" value="Refer To Engineer" onclick="doAcknowledgeFormSubmit('referEng');" />
                                    </s:if>
                                    <!--
                                    <s:if test="insurerIsFnolEnabled">
                                        <input type="button" value="Refer to FNOL" onclick="doAcknowledgeFormSubmit('referFNOL');" />
                                    </s:if>
                                    -->
                                    <input type="button" value="Claim Pending" onclick="doAcknowledgeFormSubmit('pending');" />
                                </td>
                            </tr>
                        </table>
                        <div id="ACKmessageBox" class="action-error-msg"></div>

                    </div>
                </div>
            </div>
        </fieldset>
        <%@ include file="s_liability_tooltip_notes.jspf" %>
    </form>
</div>

