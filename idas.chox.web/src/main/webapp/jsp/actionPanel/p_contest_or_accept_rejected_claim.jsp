<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">

   // $(document).ready(function() {
   Ext.onReady(function(){
        openTab(6);
        });
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/processClaim.action"
          method="post"
          id="contestOrAcceptRejectedClaim" name="contestOrAcceptRejectedClaim">
        <s:hidden id="claimId" name="id" />
        <s:hidden id="name" name="name" />
        <fieldset class="x-fieldset">
            <legend>Rejected Claim - Action Required</legend>
            <div>
                <div class="status-info">
                    Please review the Insurer's notes against rejection reasoning and decide whether to accept or reject the Insurer's rejection decision.
                    Please include supporting notes on the decision made using the 'Notes' tab.
                </div>
                <div class="status-control-set">
                    <table>
                        <tr>
                            <td>
                                <div class="no-format">
                                    <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="submit" value="Contest This Claim"  onclick="javascript: actionPanel.registerAction('contestRejectedClaim');" />
                                <input type="submit" value="Accept Rejection Decision" onclick="javascript: actionPanel.registerAction('acceptRejectedClaim')"  />
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="ActionPanelMessageBox"></div>
            </div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    </form>
</div>