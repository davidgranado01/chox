<%@ taglib uri="/struts-tags" prefix="s" %>
<form onsubmit="return true;" action="user/acknowledge.action" method="post" id="route"
    name="route">
    <fieldset class="x-fieldset">
        <legend>Claim Acknowledgement - Action Required</legend>
        <div>
            <s:hidden name="id" />
            <div>
                <div class="status-info">
                    Please enter details of the claim review and decide whether to acknowledge or reject
                    the claim. Please use the 'Notes' tab in order to communicate detailed comments
                    you may have for the CHO.
                </div>
                <div class="status-control-set">
                    <table>
                        <tr>
                            <td>
                                <label>
                                    Indemnity (Decimal)</label>
                                <input type="text" class="chox-ttxt" name="indemintyAmount" />
                            </td>
                            <td>
                                <label>
                                    % Liability Accepted</label>
                                <input type="text" class="chox-ttxt" name="percentageLiabilityAccepted" />
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label>
                                    Claim Number</label>
                                <input type="text" class="chox-ttxt" name="claimNumber" />
                            </td>
                            <td>
                                <label>
                                    Quantum Dispute?</label>
                                <s:checkbox name="isQuantumDispute" />
                            </td>
                        </tr>
                        <tr valign="top">
                            <td>
                                <label>
                                    Engineer's Claim Review Notes</label>
                                <textarea class="chox-tta" cols="20" rows="5" name="engineerClaimReviewNotes"></textarea>
                            </td>
                            <td>
                                <label>
                                    Invoice Review Required</label>
                                <s:checkbox name="isInvoiceReviewRequired" />
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div class="no-format">
                                    <span>Action &nbsp;</span>
                                    <s:radio name="actionName" list="actionNames" />
                                </div>
                            </td>
                            <td>
                                <input type="submit" value="Submit" />
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </fieldset>
</form>
