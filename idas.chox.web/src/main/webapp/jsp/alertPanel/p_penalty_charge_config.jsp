<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    Ext.onReady(function(){
        
        var autoPenaltyStartDate = new Ext.form.DateField({
            id: 'autoPenaltyStartDateId',
            name: 'autoPenaltyStart',
            renderTo: 'autoPenaltyStartDateDiv',
            width: 120,
            allowBlank: false,
            format: 'd/m/Y',
            msgTarget : 'qtip',
        <s:if test="addPenaltyChargeConfigValidation" >
            minValue: '<s:date format="dd/MM/yyyy" name="invoiceCreationDate" />',
        </s:if>
            value: '<s:date format="dd/MM/yyyy" name="autoPenaltyStartDate" />',
            showWeekNumber: true
        });
        
        var form = $("form#doAdjustAutoPenalty");
        choxJqueryHttpSubmit(form, function(){});
    });

    function updatePenaltyChargeConfig() {
        if (Ext.getCmp("autoPenaltyStartDateId").isValid()) {
            choxJqueryHttpSubmit($("form#doAdjustAutoPenalty"));
            return true;
        }
        return false;
    }
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">

    <form action="<%= request.getContextPath()%>/prv/doAdjustAutoPenalty.action" method="post" id="doAdjustAutoPenalty" name="doAdjustAutoPenalty">

        <s:hidden name="id" />

        <fieldset class="x-fieldset"><legend>Penalty Charge Configuration</legend>

            <div class="status-info">
                <s:if test="showAutoPenaltyCheckbox ">
                    Modify the date from which the penalty charges will be calculated.
                    Adjusting this date will remove all penalty charges currently applied to the invoice.
                    Once the date is adjusted, penalty charges may be automatically re-calculated.
                    You may wish to check and/or adjust the penalty charges manually.
                    You can also enable/disable automatic penalty charges for this claim.
                </s:if>
                <s:else>
                    Modify the date from which the penalty charges will be calculated.
                    Adjusting this date will remove all penalty charges currently applied to the invoice.
                    Once the date is adjusted, if applicable, please apply the correct penalty charges.
                </s:else>
            </div>

            <table border="0" cellspacing="0" cellpadding="0" style="width:70%">
                <tr>
                </tr>
                <tr >
                    <td align="left"><label>Penalty Charge Start Date</label></td>
                    <td align="left">
                        <div id="autoPenaltyStartDateDiv"></div>
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td colspan="3" align="left">
                        <input type="submit" id="PCApplyButtonId" value="Apply" onclick="event.preventDefault(); updatePenaltyChargeConfig();"/>
                            
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
    </form>
</div>



