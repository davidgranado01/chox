<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<%@ include file="s_liability_validation.jspf" %>
<script type="text/javascript">
$(function(){

    var rejectionDescField = new Ext.form.TextArea({
        name             : 'rejectionDescription',
        id               : 'rejecDescJspfId',
        width            :  350,
        height           :  80,
        allowBlank       :  false,
        renderTo         : 'rejectionDescJspfId',
        disabled         : '<s:property value="rejectButtonEnabled"/>' == 'false'
    });

});
var reasonOfRejectionDescReader = new Ext.data.JsonReader({
    fields:[{name:'id'},{name:'description'}]
});

var reasonOfRejectionDescStore = new Ext.data.Store({
    data : Ext.util.JSON.decode('<s:property value="jsonReasonOfClaimRejectionDesc" escape="false"/>'),
    reader : reasonOfRejectionDescReader
});

function refreshDesc(id){
    reasonOfRejectionDescStore.each(function(rec) {
        if(id == rec.json.text){
            Ext.getCmp('rejecDescJspfId').setValue(rec.json.value);
        }
    });
    if(id == -1 || id == '')
        Ext.getCmp('rejecDescJspfId').setValue("");
}
</script>
<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formAcknowledgeAction" name="formAcknowledgeAction">

        <fieldset class="x-fieldset">
            <legend>Claim Acknowledgement - Action Required</legend>
            <div>
                <s:hidden id="claimId" name="id" />
                <s:hidden id="name" name="name" />
                <%-- <input name="currentVersion" type="hidden" value="<s:property value="version" />" /> --%>
                <s:hidden id="isClaimNumberValidFlag" name="isClaimNumberValidFlag" value="1"/>
                <div>
                    <div class="status-info">
                        <s:if test="insurerIsEngineersEnabled">
                            Please enter details of the claim and decide whether to acknowledge or refer to an engineer. You can enter public notes in the 'Claim Review Notes' box in order to communicate detailed comments you may have for the CHO.
                        </s:if>
                        <s:else>
                            Please enter details of the claim and  acknowledge the claim. You can enter public notes in the 'Claim Review Notes' box in order to communicate detailed comments you may have for the CHO.
                        </s:else>
                        <s:if test="rejectButtonEnabled">
                            Alternatively, if you would like to reject the claim back to the CHO, then select a 'Reason For Rejection'.
                        </s:if>
                        <s:elseif test="isSubscriberClaim">
                            <s:if test="slaExtDays > 0">
                                This claim cannot be rejected as the Subscriber notification 5 day SLA + <s:property value="slaExtDays"/> day extension has passed.
                            </s:if>
                            <s:else>
                                This claim cannot be rejected as the Subscriber notification 5 day SLA has passed.
                            </s:else>
                        </s:elseif>
                        <s:elseif test="isFixedFeeClaim">
                            <s:if test="slaExtDays > 0">
                                This claim cannot be rejected as the Fixed Fee notification 14 day SLA + <s:property value="slaExtDays"/> day extension has passed.
                            </s:if>
                            <s:else>
                                This claim cannot be rejected as the Fixed Fee notification 14 day SLA has passed.
                            </s:else>
                        </s:elseif>
                        <br/><br/>This claim has been pending for <s:property value="daysInStatus" /> day(s).
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
                                        value="liabilityStatus.getLiablityValue()"
                                        emptyOption="false">
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
                            <tr valign="top">
                                <td>
                                    <label>Supporting Liability Notes (Public)</label>
                                </td>
                                <td colspan="3">
                                    <textarea class="chox-canote" cols="80" rows="3" name="supportingLiabilityNotes" id="supportingLiabilityNotesId"><s:property value="supportingLiabilityNotes" /></textarea>
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
                                    <label>Claim Review Notes (Public)</label>
                                </td>
                                <td colspan="3">
                                    <textarea class="chox-canote" cols="80" rows="3" name="engineerClaimReviewNotes"><s:property value="engineerClaimReviewNotes" /></textarea>
                                </td>
                            </tr>
                            <tr valign="top">
                                <td>
                                    <label>Reason For Rejection</label>
                                </td>
                                <td colspan="3">
                                    <div id="ReasonOfRejectionDiv">
                                        <s:if test="rejectButtonEnabled">
                                            <s:select
                                                name="reasonOfRejectionId"
                                                id="reasonOfRejectionId"
                                                list="reasonOfClaimRejections"
                                                listKey="id"
                                                listValue="rorName"
                                                onchange="refreshDesc(this.value)"
                                                headerKey=""
                                                headerValue="N/A"
                                                emptyOption="false">
                                            </s:select>
                                        </s:if>
                                        <s:else>
                                            <s:select
                                                name="reasonOfRejectionId"
                                                id="reasonOfRejectionId"
                                                list="reasonOfClaimRejections"
                                                listKey="id"
                                                listValue="rorName"
                                                onchange="refreshDesc(this.value)"
                                                headerKey=""
                                                disabled="true"
                                                headerValue="N/A"
                                                emptyOption="false">
                                            </s:select>
                                        </s:else>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td align="left" valign="top"><label class="std-label-ro">Supporting Rejection Note&nbsp;&nbsp;</label></td>
                                <td colspan="3">
                                    <div id="rejectionDescJspfId"/>
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
                                    <input type="button" id="PCLRejectButtonId" value="Reject" <s:if test="rejectButtonEnabled == false">disabled='true'</s:if> onclick="doAcknowledgeFormSubmit('rejectClaim');" />
                                    <input type="button" id="PCLAcknowledgeButtonId" value="Acknowledge" onclick="doAcknowledgeFormSubmit('acknowledgeClaim')"  />
                                    <s:if test="insurerIsEngineersEnabled">
                                        <input type="button" id="PCLReferToEngineerButtonId"value="Refer To Engineer" onclick="doAcknowledgeFormSubmit('referEng');" />
                                    </s:if>
                                </td>
                            </tr>
                        </table>
                        <div id="ACKmessageBox" class="action-error-msg"></div>

                    </div>
                </div>
            </div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
        <%@ include file="s_liability_tooltip_notes.jspf" %>
    </form>
</div>

