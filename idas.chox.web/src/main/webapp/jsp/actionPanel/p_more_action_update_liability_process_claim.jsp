<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ include file="s_liability_validation.jspf" %>
<script type="text/javascript">
        
    // $(function(){
    Ext.onReady(function(){
        $(function() {
            $("#indemnityStance").val("<s:property value="indemnityStance" />");
        });
    });
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formAcknowledgeAction" name="formAcknowledgeAction">

        <fieldset class="x-fieldset">
            <legend>Update Liability/Indemnity</legend>
            <div>
                <s:hidden id="claimId" name="id" />
                <s:hidden id="name" name="name" />
                <s:hidden id="isClaimNumberValidFlag" name="isClaimNumberValidFlag" value="1"/>
                <input id="claimNumber" name="claimNumber" type="hidden"/>
                <input id="reasonOfRejectionId" name="reasonOfRejectionId" type="hidden">
                <div>
                    <div class="status-control-set">
                        <table class="status-table">
                            <tr>
                                <td width="20%">
                                    <label>Liability Status
                                        <span class="mandatory">*</span>
                                    </label>
                                    <img src="../images/help.png" id="liabilityStatusHelp" alt=""/>
                                </td>
                                <td>
                                    <s:select
                                        id="liabilityStatus"
                                        name="liabilityStatus"
                                        list="liabilityStatusDropDownMap"
                                        emptyOption="false"
                                        value="liabilityStatus.getLiablityValue()"
                                        >
                                    </s:select>
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
                                    <input type="text" class="chox-ttxt" name="percentageLiabilityAccepted" id="percentageLiabilityAccepted" value="<s:property value="percentageLiabilityAccepted" />" onkeyup="extractNumber(this,2,false);"/>
                                </td>
                                <td align="right">
                                    <label>Liability Percentage Agreed (<b>CHO</b>)</label>
                                </td>
                                <td>
                                    <input type="text" class="chox-ttxt" name="percentageLiabilityCho" id="percentageLiabilityCho" value="<s:property value="percentageLiabilityCho" />" onkeyup="extractNumber(this,2,false);"/>
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
                            <tr valign="top">
                                <td>
                                    <label>Supporting Liability Notes (Public)</label>
                                </td>
                                <td colspan="3">
                                    <textarea class="chox-canote" cols="80" rows="3" name="claimReviewNotes" id="supportingLiabilityNotesId"><s:property value="claimReviewNotes" /></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label>Indemnity Stance</label>
                                </td>
                                <td>
                                    <select id="indemnityStance" name="indemnityStance" value="<s:property value="indemnityStance" />">
                                        <option value="">-- Please Select--</option>
                                        <option value="Dealing Under Article 75">Dealing Under Article 75</option>
                                        <option value="Dealing Under Road Traffic Act">Dealing Under Road Traffic Act</option>
                                        <option value="No Involvement">No Involvement</option>
                                        <option value="Not Indemnifying">Not Indemnifying</option>
                                        <option value="Pending Indemnity">Pending Indemnity</option>
                                        <option value="Providing Indemnity">Providing Indemnity</option>
                                    </select>
                                </td>
                                <td align="right">
                                    <label>Reserve Value</label>
                                </td>
                                <td>
                                    <input type="text" class="chox-ttxt" name="indemnityAmount" id="ACIndemityAmountId" value="<s:property value="indemnityAmount" />"/>
                                </td>
                                <td colspan="2">
                                    <label></label>
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
                                    <input type="button" value="Update Liability" id="MAULPCUpdateLiabilityButtonId"onclick="doUpdateLiabilityFormSubmit('updateLiability');" />

                                </td>
                            </tr>
                        </table>
                        <div id="ACKmessageBox" class="action-error-msg"></div>

                    </div>
                </div>
            </div>
        </fieldset>
        <%@ include file="s_liability_tooltip_notes.jspf" %>
        <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    </form>
</div>

