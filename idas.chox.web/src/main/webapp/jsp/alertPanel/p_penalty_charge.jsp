<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    Ext.onReady(function(){
        
        var hirePenaltyPercentageReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [{name:'text'}
                ,{name:'value'}]
        });

        var hirePenaltyPercentageStore = new Ext.data.Store({
            data : Ext.util.JSON.decode('<s:property value="hirePenaltyPercentageJsonString" escape="false"/>'),
            reader : hirePenaltyPercentageReader,
            listeners: {load: function() {this.insert(0, new Ext.data.Record({'value':'','text':'--- Please Select ---'}));}}
        });

        var hirePenaltyPercentageCombo = new Ext.form.ComboBox({
            store : hirePenaltyPercentageStore,
            width: 145,
            renderTo: 'hirePenaltyPercentageDiv',
            valueField : 'value',
            hiddenName: 'hirePenaltyPercentage',
            valueNotFoundText : '--- Please Select ---',
            id : 'hirePenaltyPercentageComboId',
            displayField :'text',
            mode : 'local',
            triggerAction : 'all',
            forceSelection : true,
            editable : false,
            listeners: {
                afterrender : function(){
                    this.setValue('<s:property value="calculatedHirePenaltyPercentage"/>');
                },
                select : function(){
                    Ext.Ajax.request({
                        url: '<%= request.getContextPath()%>/prv/p/getHirePenaltyAmount.action',
                        success : function(response, opts) {
                            var obj = Ext.decode(response.responseText);
                            if ( obj.success ){
                                $('#tHirePenaltyChargeAmount').val((obj.hirePenaltyAmount).toFixed(2));
                                updateTotalToPay();
                            }
                        },
                        params: {
                            hirePenaltyPercentage : this.getValue(),
                            nonce:'<%= session.getAttribute("SessionNonce")%>'
                        }

                    });
                }
            }
        });

           
        
        var repairPenaltyPercentageReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [{name:'text'}
                ,{name:'value'}]
        });

        var repairPenaltyPercentageStore = new Ext.data.Store({
            data : Ext.util.JSON.decode('<s:property value="repairPenaltyPercentageJsonString" escape="false"/>'),
            reader : repairPenaltyPercentageReader,
            listeners: {load: function() {this.insert(0, new Ext.data.Record({'value':'','text':'--- Please Select ---'}));}}
        });

        var repairPenaltyPercentageCombo = new Ext.form.ComboBox({
            store : repairPenaltyPercentageStore,
            width: 145,
            renderTo: 'repairPenaltyPercentageDiv',
            valueField : 'value',
            hiddenName: 'repairPenaltyPercentage',
            valueNotFoundText : '--- Please Select ---',
            id : 'repairPenaltyPercentageComboId',
            displayField :'text',
            mode : 'local',
            triggerAction : 'all',
            forceSelection : true,
            editable : false,
            listeners: {
                afterrender : function(){
                    this.setValue('<s:property value="calculatedRepairPenaltyPercentage"/>');
                },
                select : function(){
                    Ext.Ajax.request({
                        url: '<%= request.getContextPath()%>/prv/p/getRepairPenaltyAmount.action',
                        success : function(response, opts) {
                            var obj = Ext.decode(response.responseText);
                            if ( obj.success ){
                                $('#tRepairPenaltyChargeAmount').val((obj.repairPenaltyAmount).toFixed(2));
//                                $('#hvRepairPenaltyPercentage').val(opts.params.repairPenaltyPercentage);
                                updateTotalToPay();
                            }
                        },
                        params: {
                            repairPenaltyPercentage : this.getValue(),
                            nonce:'<%= session.getAttribute("SessionNonce")%>'
                        }

                    });
                }
            }
        });
            

     

        var form = $("form#applyPenaltyCharge");

        form.validate(
        {
            errorLabelContainer: "#PenaltyChargeBox",
            rules: {
                hirePenaltyChargeAmount:{
                    required:true,
                    min:0,
                    number:true,
                    max:9999999
                },
                repairPenaltyChargeAmount:{
                    required:true,
                    min:0,
                    number:true,
                    max:99999999
                }
            },
            messages: {
                repairPenaltyChargeAmount:{
                    required:"You must supply a value for 'Repair Penalty Charge Amount'",
                    min:"'Repair Penalty Charge Amount' must be larger than 0",
                    number:"You must supply a numeric value for 'Repair Penalty Charge Amount'",
                    max:"'Repair Penalty Charge Amount' must be Less Than Or Equal To 9999999."
                },
                hirePenaltyChargeAmount:{
                    required:"You must supply a value for 'Hire Penalty Charge Amount'",
                    min:"'Hire Penalty Charge Amount' must be larger than 0",
                    number:"You must supply a numeric value for 'Hire Penalty Charge Amount'",
                    max:"'Hire Penalty Charge Amount' must be Less Than Or Equal To 9999999."
                }
            }
        });
        
        $("#tHirePenaltyChargeAmount").keyup(function (e) {
            updateTotalToPay();
        });

        $("#tRepairPenaltyChargeAmount").keyup(function (e) {
            updateTotalToPay();
        });
        updateTotalToPay();
    });

    function updateTotalToPay(inputValue) {
        var repairAmount = $("#tRepairPenaltyChargeAmount").val();
        var hireAmount = $("#tHirePenaltyChargeAmount").val();
        var repairPenaltyCharge;
        var hirePenaltyCharge;
        var totalPenaltyCharge;
        var totalAmountToPayBeforeNewPenaltyCharge
        var totalAmountToPayAfterNewPenaltyCharge;
    
        if (!isNaN(repairAmount)) {
            repairPenaltyCharge = parseFloat(repairAmount) == NaN ? 0 : parseFloat(repairAmount);
            if(isNaN(repairPenaltyCharge)){
                repairPenaltyCharge = 0;
            }
        }
        else {
            repairPenaltyCharge = 0;
        }

        if (!isNaN(hireAmount)) {
            hirePenaltyCharge = parseFloat(hireAmount) == NaN ? 0 : parseFloat(hireAmount);
            if(isNaN(hirePenaltyCharge)){
                hirePenaltyCharge = 0;
            }
        }
        else {
            hirePenaltyCharge = 0;
        }
        totalPenaltyCharge = repairPenaltyCharge + hirePenaltyCharge;


        if (hirePenaltyCharge == 0) {
            $("form#applyPenaltyCharge #hirePenaltyPercentageComboId").rules("remove");
            //$("form#applyPenaltyCharge #tRepairPenaltyChargeAmount").rules("add", {required: true, messages: {required: "You must supply a value for 'Repair Penalty Charge Amount' or 'Hire Penalty Charge Amount'"}});
            //$("form#applyPenaltyCharge #tRepairPenaltyChargeAmount").rules("add", {min: 0, messages: {required: "One of 'Repair Penalty Charge Amount' and 'Hire Penalty Charge Amount' must be larger than 0"}});  
        }
        else {
            $("form#applyPenaltyCharge #hirePenaltyPercentageComboId").rules("add", {required: true, messages: {required: "You must supply a value for 'Hire Penalty Percentage'"}});
            //            $("form#applyPenaltyCharge #tRepairPenaltyChargeAmount").rules("remove");
        }

        if (repairPenaltyCharge == 0) {
            $("form#applyPenaltyCharge #repairPenaltyPercentageComboId").rules("remove");
            // $("form#applyPenaltyCharge #tHirePenaltyChargeAmount").rules("add", {required: true, messages: {required: "You must supply a value for 'Repair Penalty Charge Amount' or 'Hire Penalty Charge Amount'"}});
            // $("form#applyPenaltyCharge #tHirePenaltyChargeAmount").rules("add", {min: 0, messages: {required: "One of 'Repair Penalty Charge Amount' and 'Hire Penalty Charge Amount' must be larger than 0"}});
        }
        else {
            $("form#applyPenaltyCharge #repairPenaltyPercentageComboId").rules("add", {required: true, messages: {required: "You must supply a value for 'Repair Penalty Percentage'"}});
            
        }

        totalAmountToPayBeforeNewPenaltyCharge = parseFloat($("#hvTotalAmountToPayBeforeNewPenaltyCharge").val());
        totalAmountToPayAfterNewPenaltyCharge = totalPenaltyCharge + totalAmountToPayBeforeNewPenaltyCharge;
        $("#totalAmountToPayAfterNewPenaltyChargeLabel").text('£' + totalAmountToPayAfterNewPenaltyCharge.toFixed(2));
        
        //        var percentageAccepted = parseFloat($("#percentageLiabilityAcceptedForPenalty").val());
        //        if (! isNaN(percentageAccepted)){
        //            $("#splitLiabilityToPayAfterPenaltyFormattedLabel").text('£' + ((totalAmountToPayAfterNewPenaltyCharge*percentageAccepted)/100).toFixed(2));
        //        }
    }

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">

    <form action="<%= request.getContextPath()%>/prv/doApplyPenaltyCharge.action" method="post" id="applyPenaltyCharge" name="applyPenaltyCharge">

        <s:hidden name="id" />
        <s:hidden id="hvTotalAmountToPayBeforeNewPenaltyCharge" name="totalAmountToPayBeforeNewPenaltyCharge" />

        <fieldset class="x-fieldset"><legend>Apply Penalty Charge</legend>

            <div class="status-warning">
                <s:if test="IsInsurerUploadClaim">
                    A penalty charge may be applicable to this invoice.  If so, please select the relevant penalty charge percentage and charge and click on the 'Apply' button.
                </s:if>
                <s:elseif test="showAutoPenaltyCheckbox && invoiceIntroducedDays > 30 && invoiceIntroducedDays < 61 && autoPenaltyChargeEnabled && penaltyChargeEnabledInBreBand">
                    Payment for this invoice is overdue. The number of days since the invoice was created
                    is <s:property value="invoiceIntroducedDays" />. A penalty charge may be applicable to this invoice. <br /> <br />Please
                    note that unless automatic penalty charges are switched off for this claim using the 'Penalty Charge Configuration' function in
                    the 'More Actions' drop down menu then penalties will be automatically re-calculated and added to the invoice on day 61
                    if the invoice remains overdue.
                </s:elseif>
                <s:else>
                    Payment for this invoice is overdue. The number of days since the invoice was created
                    is <s:property value="invoiceIntroducedDays" />. A penalty charge may be applicable to this invoice.
                </s:else>
            </div>

            <table border="0" cellspacing="0" cellpadding="0" style="width:100%">
                <tr>
                    <td align="left" style="width:20%"><label>Total Gross &nbsp;</label></td>
                    <td align="left" style="width:20%"><s:property value="totalAmountToPayBeforeNewPenaltyChargeFormatted" /></td>
                    <td style="width:20%"></td>
                    <td style="width:20%"></td>
                </tr>

                <s:if test="outstandingInterimPayment > 0 && interimPaymentReceived > 0">
                    <tr>
                        <td align="left" style="width:20%"><label>Interim Payment &nbsp;</label></td>
                        <td align="left" style="width:20%"><label class="std-data-ro-red">£<s:property value="interimPaymentMade" /> (Only £<s:property value="interimPaymentReceived" /> Received)</label></td>
                        <td style="width:20%"></td>
                        <td style="width:20%"></td>
                    </tr>
                </s:if>
                <s:elseif test="(interimPaymentMade  == interimPaymentReceived) && interimPaymentMade > 0">
                    <tr>
                        <td align="left" style="width:20%"><label>Interim Payment &nbsp;</label></td>
                        <td align="left" style="width:20%"><label class="std-data-ro-red">£<s:property value="interimPaymentMade" /> (Received)</label></td>
                        <td style="width:20%"></td>
                        <td style="width:20%"></td>
                    </tr>
                </s:elseif>
                <s:elseif test="interimPaymentMade  > 0">
                    <tr>
                        <td align="left" style="width:20%"><label>Interim Payment &nbsp;</label></td>
                        <td align="left" style="width:20%"><label class="std-data-ro-red">£<s:property value="interimPaymentMade" /> (Not Yet Received)</label></td>
                        <td style="width:20%"></td>
                        <td style="width:20%"></td>
                    </tr>
                </s:elseif>
                <s:else>
                    <tr>
                        <td align="left" style="width:20%"><label>Interim Payment &nbsp;</label></td>
                        <td align="left" style="width:20%">N/A</td>
                        <td style="width:20%"></td>
                        <td style="width:20%"></td>
                    </tr>
                </s:else>
                <tr>
                    <td align="left"><label>Hire Gross</label></td>
                    <td align="left">£&nbsp;<s:property value="invHireGross" /></td>
                    <td align="left"><label>Repair Gross</label></td>
                    <td align="left">£&nbsp;<s:property value="repairGross" /></td>
                </tr>     

                <tr>
                    <td align="left"><label>Hire Penalty Percentage </label></td>
                    <td align="left">
                        <div id="hirePenaltyPercentageDiv"></div>
                    </td>
                    <td align="left"><label>Repair Penalty Percentage </label></td>
                    <td align="left">
                        <div id="repairPenaltyPercentageDiv"></div>
                    </td>
                </tr>

                <tr>
                    <td align="left"><label>Hire Penalty Amount</label></td>
                    <td align="left">£<input type="text" class="chox-ttxt" id="tHirePenaltyChargeAmount" name="hirePenaltyChargeAmount" value="<s:property value="calculatedHirePenaltyChargeAmount" />" onkeyup="extractNumber(this,2,false);"/></td>
                    <td align="left"><label>Repair Penalty Amount</label></td>
                    <td align="left" nowrap >£<input type="text" class="chox-ttxt" id="tRepairPenaltyChargeAmount" name="repairPenaltyChargeAmount" value="<s:property value="calculatedRepairPenaltyChargeAmount" />" onkeyup="extractNumber(this,2,false);"/></td>
                </tr>

                <tr>
                    <td align="left"><label>Full Total Requested (After Penalty Charge) &nbsp;</label></td>
                    <td colspan="3" align="left"><label id="totalAmountToPayAfterNewPenaltyChargeLabel" style="font-weight:bold"><s:property value="totalAmountToPayAfterNewPenaltyChargeFormatted" />&nbsp;&nbsp;</label></td>
                </tr>

                <tr>
                    <td colspan="4" align="left">
                        <input type="submit" id="PCApplyButtonId" value="Apply"/>
                        <s:if test="isShowPenaltyChargeAlert">
                            &nbsp;<s:checkbox name="isRemovePenaltyAlert" id="PCRemoveFromQueueId" label="Remove From Penalty Charge Queue"/>
                            <label>Remove From Queue</label>
                        </s:if>
                    </td>
                </tr>
            </table>
            <div class="chox-form-submit-result">&nbsp;</div>
            <div id="PenaltyChargeBox" class="action-error-msg"></div>
        </fieldset>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
</div>
