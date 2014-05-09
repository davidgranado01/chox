<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

     Ext.onReady(function(){
        openTab(4);
        var form = $("form#resubmitInvoiceForm");
        choxJqueryHttpSubmit(form, doMaskClaimDetailPage);
    });
        
    function doMaskClaimDetailPage() {
        Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
//        return true;
    }
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="resubmitInvoiceForm" name="resubmitInvoiceForm" action="<%=request.getContextPath()%>/prv/processClaim.action" method="post">
        <fieldset class="x-fieldset"><legend>Claim Data - Action Required</legend>
            <s:hidden id="claimId" name="id" />
            <s:hidden id="name" name="name" value="resubmitInvoice"/>
            <div>
                <div class="status-info">
                    Please review the 'History' tab for details on why the claim has been rejected, amend details accordingly and re-submit.
                </div>
                <div class="status-info-submit">
                    <table>
                        <tr>
                            <td>
                                <div class="no-format">
                                    <span>Please specify how you wish to proceed &nbsp;&nbsp;</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td><input type="submit" id="RIDRe-SubmitClaimInvoiceButtonId"value="Re-Submit Claim/Invoice" /></td>
                        </tr>
                    </table>
                </div>
            </div>
        </fieldset>
        <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    </form>
</div>