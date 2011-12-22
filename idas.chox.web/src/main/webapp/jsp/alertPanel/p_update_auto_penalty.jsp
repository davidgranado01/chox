<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    Ext.onReady(function(){
        
        var autoPenaltyStartDate = new Ext.form.DateField({
            name: 'autoPenaltyStart',
            renderTo: 'autoPenaltyStartDateDiv',
            width: 120,
            allowBlank: false,
            format: 'd/m/Y',
            //            hideMode: 'offsets',
            value: '<s:date format="dd/MM/yyyy" name="autoPenaltyStartDate" />',
            showWeekNumber: true
        });
     


    });

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">

    <form action="<%= request.getContextPath()%>/prv/doAdjustAutoPenalty.action" method="post" id="doAdjustAutoPenalty" name="doAdjustAutoPenalty">

        <s:hidden name="id" />

        <fieldset class="x-fieldset"><legend>Adjust Auto Penalty Charge</legend>

            <div class="status-warning">
                Adjust or Disable Auto Penalty Charge.
            </div>

            <table border="0" cellspacing="0" cellpadding="0" style="width:60%">

                <tr>
                </tr>
                <tr>
                    <td align="left"><label>Auto Penalty Start Date </label></td>
                    <td align="left">
                        <div id="autoPenaltyStartDateDiv"></div>
                    </td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td colspan="4" align="left">
                        <input type="submit" id="PCApplyButtonId"value="Apply" />

                        <s:if test="showAutoPenaltyCheckbox ">
                            <s:if test="autoPenaltyChargeEnabled">
                                &nbsp;<s:checkbox name="stopAutoPenaltyCharge" id="APStopAutoPenaltyChargeId"/>
                                <label>Stop Automatic Penalty Charges &nbsp;</label>
                            </s:if>
                            <s:else>
                                &nbsp;<s:checkbox name="autoPenaltyChargeEnabled" id="APAutoPenaltyChargeEnabledId"/>
                                <label>Enable Automatic Penalty Charges &nbsp;</label>
                            </s:else>
                        </s:if>
                    </td>
                </tr>
            </table>
            <div class="chox-form-submit-result">&nbsp;</div>
            <div id="autoPenaltyChargeBox" class="action-error-msg"></div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>



