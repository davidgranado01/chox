<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){
        $("#fPercentageLiabilityAccepted").blur(function(){
            var liabilityStatus = $("#fLiabilityStatus").val();
            var ins = parseFloat($("#fPercentageLiabilityAccepted").val());
            if (liabilityStatus == 5 && !isNaN(ins)&& ins > 0 && ins <=100 ){
                ins = ins.toFixed(2);
                $("#fPercentageLiabilityAccepted").val(ins);
                var cho = (100.00-ins);
                cho = cho.toFixed(2);
                $("#fPercentageLiabilityCho").val(cho);
            }
        });
                $.validator.addMethod(
            "checkAcceptedDate",
            function(value, element) {
                if (isLiabilityAccepted()){
                    var accdate = Ext.getCmp('fLiabilityAgreedDate').getValue();
                    if ( accdate == "" ){
                        return false;
                    }
                    var cur = new Date();
                    if ( ( cur - accdate) < 0 ){
                        return false;
                    }
                }
                return true;
            }
        );
        $.validator.addMethod(
            "checkTotal",
            function(value, element, para) {
                if (isLiabilityAccepted()){
                    var total = parseFloat($("#fPercentageLiabilityAccepted").val()) + parseFloat($("#fPercentageLiabilityCho").val());

                    if (total > 100) {

                        return false;
                    }
                }
                return true;
            }
        );
        $("form#formUpdateSaveLiabilityStatus").validate(
        {
            errorLabelContainer: "#ACKmUpdateInsurerClaimNumbermessageBox",
            rules: {
                claimNumber:{
                    required:true
                },
                fPercentageLiabilityAccepted:{
                    required:function(element){
                        return isLiabilityAccepted();
                    },
                    number:true,
                    max: 100.00,
                    checkTotal:true
                },
                fPercentageLiabilityCho:{
                    required:function(element){
                        return isLiabilityAccepted();
                    },
                    number:true,
                    max: 100.00
                },
                fLiabilityStatus:{
                    range:[1,5]
                },
                liabilityAgreedDate:{
                    checkAcceptedDate:true
                },
                liabilityAgreedDate:{
                    checkAcceptedDate:"Liability agreed date cannot be empty or a future date"
                }
            },
            messages: {
                claimNumber: {
                    required:"You must supply a value for 'Claim Number'",
                    textDigitOnly:"Invalid 'Claim Number' Format"
                },
                fPercentageLiabilityAccepted:{
                    required:"You must supply a value for 'Percentage Liability Accepted'",
                    number:"You must supply a numeric value for 'Percentage Liability Agreed'",
                    max:"'Percentage Liability Accepted' cannot be more than 100",
                    checkTotal:"Sum of percantage liability fields must not exceed 100."
                },
                fPercentageLiabilityCho: {
                    required:"You must supply a value for 'Percentage Liability CHO'",
                    number:"You must supply a numeric value for 'Percentage Liability CHO'",
                    max:"'Percentage Liability CHO' cannot be more than 100"
                },
                fLiabilityStatus:{
                    range:"You must select a liability status"
                }
            }
        });
        var liabilityAgreedDatePicker = ui.dateField('fLiabilityAgreedDate','<s:date format="dd/MM/yyyy" name="fLiabilityAgreedDate" />','liabilityAgreedDateDiv');
        


    });


    function isLiabilityAccepted(){
        var liabilityStatus = $("#fLiabilityStatus").val();
        if ( liabilityStatus == 1 || liabilityStatus == 5 || liabilityStatus == 6){
            return true;
        }
        return false;
    }

    function doUpdateSaveLiabilityStatus(){

        if($("form#formUpdateSaveLiabilityStatus").valid()){
            $("form#formUpdateSaveLiabilityStatus").submit();
        }
    }

    function onLiabilityStatusSelectionChange(){

        var liabilityStatus = $("#fLiabilityStatus").val();

        if ( isLiabilityAccepted()){
            if ( liabilityStatus != 5){
                $("#fPercentageLiabilityAccepted").val((100.00).toFixed(2));
                $("#fPercentageLiabilityCho").val((0.00).toFixed(2));
            }
            Ext.getCmp('fLiabilityAgreedDate').setValue(new Date());
        }else{
            $("#fPercentageLiabilityAccepted").val("");
            $("#fPercentageLiabilityCho").val("");

            Ext.getCmp('fLiabilityAgreedDate').setValue("");
        }

    }

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form action="<%=request.getContextPath()%>/prv/updateSaveLiabilityStatus.action" method="post" id="formUpdateSaveLiabilityStatus" name="formUpdateSaveLiabilityStatus">
        <fieldset class="x-fieldset">
            <legend>Update Liability</legend>
            <s:hidden id="claimId" name="id" />
            <div>
                <div class="status-control-set">
                    <table class="status-table">
                            <tr>
                                <td width="20%">
                                    <label>Liability Status
                                        <span class="mandatory">*</span></label>
                                </td>
                                <td>
                                    <s:select
                                        id="fLiabilityStatus"
                                        name="fLiabilityStatus"
                                        list="liabilityStatusDropDownMap"                                        
                                        value="fLiabilityStatus.ordinal()"
                                        emptyOption="false"
                                        onchange="javascript:onLiabilityStatusSelectionChange()"
                                        >
                                    </s:select>
                                </td>
                                <td colspan="2">
                                    <label></label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label>
                                        Liability Percentage Agreed (<b>Insurer</b>)</label>

                                </td>
                                <td>
                                        <input type="text" class="chox-ttxt" name="fPercentageLiabilityAccepted" id="fPercentageLiabilityAccepted" value="<s:property value="fPercentageLiabilityAccepted" />"/>
                                </td>
                                <td>
                                    <label>
                                        Liability Percentage Agreed (<b>CHO</b>)</label>
                                </td>
                                <td>
                                        <input type="text" class="chox-ttxt" name="fPercentageLiabilityCho" id="fPercentageLiabilityCho" value="<s:property value="fPercentageLiabilityCho" />"/>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label>Date Liability Agreed</label>
                                </td>
                                <td><div id="liabilityAgreedDateDiv"></div></td>
                                <td colspan="2">
                                    <label></label>
                                </td>
                            </tr>
                            <tr valign="top">
                                <td>
                                    <label>Liability Notes</label>
                                </td>
                                <td colspan="3">
                                    <textarea class="chox-canote" cols="80" rows="5" name="fLiabilityNotes"><s:property value="liabilityNotes" /></textarea>
                                </td>
                            </tr>
                        <tr>
                            <td>                                                                
                                <input type="button" value="Update Liability" onclick="javascript: return doUpdateSaveLiabilityStatus()"/>
                            </td>
                            <td></td><td></td><td></td>
                        </tr>
                    </table>
                </div>
                <div class="action-error-msg" id="ACKmUpdateInsurerClaimNumbermessageBox"></div>
            </div>
        </fieldset>
        <s:token/>
    </form>
</div>