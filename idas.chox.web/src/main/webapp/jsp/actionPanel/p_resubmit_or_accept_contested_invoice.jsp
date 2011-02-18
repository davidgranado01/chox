<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    $(document).ready(function() {
        openTab(6);
    });
    function resubmitOrAcceptContestedInvoiceSubmit(action){
        actionPanel.registerAction(action);
    }
    
</script>
<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="resubmitOrAcceptContestedInvoice" name="resubmitOrAcceptContestedInvoice"
          action="<%=request.getContextPath()%>/prv/processClaim.action"
          method="post">
        <fieldset class="x-fieldset">
            <legend>Contested Invoice - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="name" name="name"/>
            <div>
                <div class="status-info">
                    Please review the 'Notes' tab for details regarding the rejection reasoning made by the Insurer.
                    Decide whether to accept or reject the Insurer's rejection decision.
                    Rejection of the Insurer's decision will require a modification to the claim details and/or the attachment of a Payment Pack.
                    Please include supporting notes on the decision made using the 'Notes' tab.
                </div>
                <div>
                    <table>
                        <tr>
                            <td>
                                <div>
                                    <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="submit" value="Reject Decision and Resubmit"  onclick="resubmitOrAcceptContestedInvoiceSubmit('contestRejectedInvoice');" />
                                <input type="submit" value="Accept Rejection Decision" onclick="resubmitOrAcceptContestedInvoiceSubmit('acceptRejectedInvoice')"  />
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="ActionPanelMessageBox"></div>
            </div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>