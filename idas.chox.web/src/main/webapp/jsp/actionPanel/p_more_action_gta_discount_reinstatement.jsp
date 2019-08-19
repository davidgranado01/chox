<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    Ext.onReady(function(){
        
        var gtaDiscountStartDate = new Ext.form.DateField({
            id: 'gtaDiscountExpiryId',
            name: 'gtaDiscountExpiry',
            renderTo: 'gtaDiscountExpiryDateDiv',
            width: 120,
            allowBlank: false,
            format: 'd/m/Y',
            msgTarget : 'qtip',
            minValue: new Date(),
            showWeekNumber: true
        });
        
        var form = $("form#doReinstateGtaDiscount");
        choxJqueryHttpSubmit(form, function(){});
    });

    function updateGtaDiscount() {
        if (Ext.getCmp("gtaDiscountExpiryId").isValid()) {
            Ext.get('doReinstateGtaDiscount').mask("Reloading Claim...");
            choxJqueryHttpSubmit($("form#doReinstateGtaDiscount"));
            return false;
        }
        return false;
    }
</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">

    <form action="<%= request.getContextPath()%>/prv/doReinstateGtaDiscount.action" method="post" id="doReinstateGtaDiscount" name="doReinstateGtaDiscount">

        <s:hidden name="id" />

        <fieldset class="x-fieldset"><legend>GTA Discount Reinstatement</legend>

            <div class="status-info">
                Please confirm the new date on which the GTA Discount will expire. After this date, the GTA Discount will be automatically removed.
                Please note that by reinstating the GTA Discount, any LPPs that currently apply will be automatically removed.
            </div>

            <table border="0" cellspacing="0" cellpadding="0" style="width:70%">
                <tr>
                </tr>
                <tr >
                    <td align="right"><label>New GTA Discount Expiry Date: </label></td>
                    <td align="left">
                        <div id="gtaDiscountExpiryDateDiv"></div>
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td colspan="3" align="left">
                        <input type="submit" id="PCApplyButtonId" value="Apply" onclick="event.preventDefault(); updateGtaDiscount();"/>
                    </td>
                </tr>
            </table>
            <div class="chox-form-submit-result">&nbsp;</div>
            <div id="autoPenaltyChargeBox" class="action-error-msg"></div>
        </fieldset>
    </form>
</div>




