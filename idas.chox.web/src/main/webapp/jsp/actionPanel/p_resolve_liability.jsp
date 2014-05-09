<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ include file="s_liability_validation.jspf" %>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formAcknowledgeAction" name="formAcknowledgeAction">

        <fieldset class="x-fieldset">
            <legend>Update Liability</legend>
            <div>
                <s:hidden id="claimId" name="id" />
                <s:hidden id="isClaimNumberValidFlag" name="isClaimNumberValidFlag" value="1"/>
                <s:hidden id="name" name="name" />
                <input id="claimNumber" name="claimNumber" type="hidden"/>
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
                                <td>
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
                                    <textarea class="chox-canote" cols="80" rows="3" name="engineerClaimReviewNotes" id="supportingLiabilityNotesId"><s:property value="engineerClaimReviewNotes" /></textarea>
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
                                    <input type="button" id="RLUpdateLiabilityButtonId"value="Update Liability" onclick="doUpdateLiabilityFormSubmit('resolveLiability');" />

                                </td>
                            </tr>
                        </table>
                        <div id="ACKmessageBox" class="action-error-msg"></div>
                    </div>
                </div>
            </div>
        </fieldset>
        <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
        <%@ include file="s_liability_tooltip_notes.jspf" %>
    </form>
</div>

