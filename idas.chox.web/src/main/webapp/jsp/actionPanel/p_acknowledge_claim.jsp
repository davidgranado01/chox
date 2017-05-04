<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ include file="s_liability_validation.jspf" %>

<script type="text/javascript">
Ext.onReady(function() {

    var rejectionDescField = new Ext.form.TextArea({
        name             : 'rejectionDescription',
        id               : 'rejecDescJspfId',
        width            :  620,
        height           :  47,
        allowBlank       :  false,
        renderTo         : 'rejectionDescJspfId',
        disabled         : '<s:property value="rejectButtonEnabled"/>' === 'false'
    });

<s:if test="acceptanceReasosnsEnabled">

    var acceptanceReasonsJsonReader = new Ext.data.JsonReader({
        totalProperty: 'totalCount',
        root: 'results',
        fields: [
            {name:'text'},
            {name:'value'}
        ]
    });
    
    var acceptanceReasonsStore = new Ext.data.Store({
        reader : acceptanceReasonsJsonReader
    });

    var   acceptanceReasonsCombo = new Ext.form.ComboBox({
            store: acceptanceReasonsStore,
            width: 300,
            renderTo: 'acceptanceReasonsDiv',
            valueField: 'text',
            id: 'acceptanceReasonsComboId',
            hiddenName: 'acceptanceReason',
            displayField:'text',
            typeAhead: false,
            mode: 'local',
            listWidth: 300,
            forceSelection: true,
            triggerAction: 'all',
            emptyText : 'Please Select a Reason',
            blankText : 'Please Select a Reason'
        });
        
        var acceptanceReasonsJsonString = '<s:property value="acceptanceReasonsJsonString" escapeHtml="false"/>';
        if (acceptanceReasonsJsonString !== '') {
            acceptanceReasonsStore.loadData(Ext.util.JSON.decode(acceptanceReasonsJsonString));
        }

</s:if>
    $(function() {
            $("#indemnityStance").val("<s:property value="indemnityStance" />");
        });
});

var reasonOfRejectionDescReader = new Ext.data.JsonReader({
    fields:[{name:'id'},{name:'description'}]
});

var reasonOfRejectionDescStore = new Ext.data.Store({
    data : Ext.util.JSON.decode('<s:property value="jsonReasonOfClaimRejectionDesc" escapeHtml="false"/>'),
    reader : reasonOfRejectionDescReader
});

function refreshDesc(id){
    reasonOfRejectionDescStore.each(function(rec) {
        if(id === rec.json.text){
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
                <s:hidden id="isClaimNumberValidFlag" name="isClaimNumberValidFlag" value="1"/>
                <div>
                    <div class="status-info">
                        <s:if test="updatedByEng">
                            Please enter details of the claim and decide whether to acknowledge the claim, refer the claim to an engineer or set the claim to pending.
                        </s:if>
                        <s:elseif test="insurerIsEngineersEnabled && insurerIsFnolEnabled">
                            Please enter details of the claim and decide whether to acknowledge the claim, refer the claim to an engineer, refer the claim to an FNOL handler or set the claim to pending.
                        </s:elseif>
                        <s:elseif test="!insurerIsEngineersEnabled && !insurerIsFnolEnabled">
                            Please enter details of the claim and decide whether to acknowledge the claim or set the claim to pending.
                        </s:elseif>
                        <s:elseif test="!insurerIsEngineersEnabled && insurerIsFnolEnabled">
                            Please enter details of the claim and decide whether to acknowledge the claim, refer the claim to an FNOL handler or set the claim to pending.
                        </s:elseif>
                        <s:elseif test="insurerIsEngineersEnabled && !insurerIsFnolEnabled">
                            Please enter details of the claim and decide whether to acknowledge the claim, refer the claim to an engineer or set the claim to pending.
                        </s:elseif>
                        <s:if test="!insurerClaim">
                            You can enter public notes in the 'Claim Review Notes' box in order to communicate detailed comments you may have for the CHO.
                        </s:if>
                        <s:if test="rejectButtonEnabled">
                            Alternatively, if you would like to reject the claim back to the CHO, then select a 'Reason For Rejection'.
                        </s:if>
                        <s:elseif test="isFixedFeeClaim">
                            <s:if test="slaExtDays > 0">
                                This claim cannot be rejected as the Fixed Fee notification <s:property value="fixedFeeSlaDays"/> day SLA + <s:property value="slaExtDays"/> day extension has passed.
                            </s:if>
                            <s:else>
                                This claim cannot be rejected as the Fixed Fee notification <s:property value="fixedFeeSlaDays"/> day SLA has passed.
                            </s:else>
                        </s:elseif>
                        <s:elseif test="isSubscriberClaim">
                            <s:if test="slaExtDays > 0">
                                This claim cannot be rejected as the Subscriber notification <s:property value="subscriberSlaDays"/> day SLA + <s:property value="slaExtDays"/> day extension has passed.
                            </s:if>
                            <s:else>
                                This claim cannot be rejected as the Subscriber notification <s:property value="subscriberSlaDays"/> day SLA has passed.
                            </s:else>
                        </s:elseif>
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
                                <td colspan="2"></td>

                            </tr>

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
                                        value="liabilityStatus.getLiablityValue()"/>
                                </td>
                                <td colspan="2"></td>
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
                                <td colspan="2"></td>
                            </tr>
                            <tr valign="top">
                                <td>
                                    <label>Supporting Liability Notes (Public)</label>
                                </td>
                                <td colspan="3">
                                    <textarea class="chox-canote" rows="3" name="supportingLiabilityNotes" id="supportingLiabilityNotesId"><s:property value="supportingLiabilityNotes" /></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td><label>Indemnity Stance</label></td>
                                <td>
                                    <select id="indemnityStance" name="indemnityStance">
                                        <option value="">-- Please Select--</option>
                                        <option value="Dealing Under Article 75">Dealing Under Article 75</option>
                                        <option value="Dealing Under Road Traffic Act">Dealing Under Road Traffic Act</option>
                                        <option value="No Involvement">No Involvement</option>
                                        <option value="Not Indemnifying">Not Indemnifying</option>
                                        <option value="Pending Indemnity">Pending Indemnity</option>
                                        <option value="Providing Indemnity">Providing Indemnity</option>
                                    </select>
                                </td>
                                <td width="20%" align="right">
                                    <label>Reserve Value</label>
                                </td>
                                <td>
                                    <input type="text" class="chox-ttxt" name="indemnityAmount" id="ACIndemityAmountId" value="<s:property value="indemnityAmount" />"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>Invoice Review Required?</label>
                                </td>
                                <td>
                                    <s:checkbox id="ACisInvoiceReviewRequiredId" name="isInvoiceReviewRequired" />
                                </td>
                                <td colspan="2"></td>
                            </tr>
                            <s:if test="acceptanceReasosnsEnabled">
                                <tr>
                                    <td>
                                        <label>Acceptance Reason</label>
                                    </td>
                                    <td colspan="2">
                                        <div id="acceptanceReasonsDiv"></div>
                                    </td>
                                    <td</td>
                                </tr>
                            </s:if>
                            <tr valign="top">
                                <td>
                                    <label>Claim Review Notes (Public)</label>
                                </td>
                                <td colspan="3">
                                    <textarea class="chox-canote" cols="85" rows="3" id="ACengineerClaimReviewNotesId" name="engineerClaimReviewNotes"><s:property value="engineerClaimReviewNotes" /></textarea>
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
                                                onchange="refreshDesc(this.value)"
                                                listKey="id"
                                                listValue="rorName"
                                                headerKey=""
                                                headerValue="N/A"
                                                disabled="true"
                                                emptyOption="false">
                                            </s:select>
                                        </s:else>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td align="left" valign="top"><label class="std-label-ro">Supporting Rejection Note&nbsp;&nbsp;</label></td>
                                <td colspan="3">
                                    <div id="rejectionDescJspfId"></div>
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
                                    <input type="button" id="ACRejectButtonId" value="Reject" <s:if test="rejectButtonEnabled == false">disabled='true'</s:if> onclick="return doAcknowledgeFormSubmit('rejectClaim');" />
                                    <input type="button" id="ACAcknowledgeButtonId" value="Acknowledge" onclick="return doAcknowledgeFormSubmit('acknowledgeClaim');"  />
                                    <s:if test="insurerIsEngineersEnabled">
                                        <input type="button" id="ACReferToEngineerButtonId" value="Refer To Engineer" onclick="return doAcknowledgeFormSubmit('referEng');" />
                                    </s:if>
                                    <s:if test="insurerIsFnolEnabled && !updatedByEng">
                                        <input type="button" id="ACReferToFnolButtonId" value="Refer to FNOL" onclick="return doAcknowledgeFormSubmit('referFNOL');" />
                                    </s:if>
                                    <input type="button" id="ACClaimPendingButtonId" value="Claim Pending" onclick="return doAcknowledgeFormSubmit('pending');" />
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
