<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    $(function(){

        $("form#formUpdateSaveLiabilityStatus").validate(
        {
            errorLabelContainer: "#ACKmUpdateInsurerClaimNumbermessageBox",
            rules: {
                claimNumber:{
                    required:true
                }
            },
            messages: {
                claimNumber: {
                    required:"You must supply a value for 'Claim Number'",
                    textDigitOnly:"Invalid 'Claim Number' Format"
                }
            }
        });
        var liabilityAgreedDatePicker = ui.dateField('fLiabilityAgreedDate','<s:date format="dd/MM/yyyy" name="fLiabilityAgreedDate" />','liabilityAgreedDateDiv');
        


    });



    function doUpdateSaveLiabilityStatus(){
        console.log('doUpdateSaveLiabilityStatus');
        if($("form#formUpdateSaveLiabilityStatus").valid()){
            $("form#formUpdateSaveLiabilityStatus").submit();
        }
    }

    function onLiabilityStatusSelectionChange(){
        var liabilityStatus = $("#fLiabilityStatus").val();
        console.log(liabilityStatus);
        if ( liabilityStatus == 1 || liabilityStatus == 5 || liabilityStatus == 6){
            $("#fPercentageLiabilityAccepted").val(100.00);
            $("#fPercentageLiabilityCho").val(0.00);
            Ext.getCmp('fLiabilityAgreedDate').setValue(new Date());
        }else{
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
                                        Liability Percentage Agreed(Insurer)</label>

                                </td>
                                <td>
                                        <input type="text" class="chox-ttxt" name="fPercentageLiabilityAccepted" id="fPercentageLiabilityAccepted" value="<s:property value="fPercentageLiabilityAccepted" />"/>
                                </td>
                                <td>
                                    <label>
                                        Liability Percentage Agreed(CHO)</label>
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
    </form>
</div>