<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">

    Ext.onReady(function(){
        openTab(1);
        expandHireMonitoringDetails(true);
    });
        
    function doMaskClaimDetailPageAndSubmit() {
        Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
        choxJqueryHttpSubmit($("form#awaitingcarHireInfoForm"));
    }
</script>


<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form  id="awaitingcarHireInfoForm" name="awaitingcarHireInfoForm" action="<%=request.getContextPath()%>/prv/processClaim.action" method="POST">
        <fieldset class="x-fieldset"><legend>Hire Monitoring - Action Required</legend>
            <div>
                <s:hidden id="claimId" name="id" />
                <s:hidden id="name" name="name" value="awaitingCarHireInfo"/>
                <div class="status-info">
                    Please complete the 'Hire Monitoring' tab with required details regarding the hire of the vehicle. When you are ready, please click the 'Proceed' button below.
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
                            <td><input type="submit" id="UCHIProceedButtonId" value="Proceed" onclick="event.preventDefault(); doMaskClaimDetailPageAndSubmit();"/></td>
                        </tr>
                    </table>
                </div>
            </div>
        </fieldset>
        <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
    </form>
</div>