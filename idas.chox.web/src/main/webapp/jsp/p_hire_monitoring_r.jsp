<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">

   function inspectionBookedDate_r(){

        new Ext.ToolTip({
            target: 'inspectionBookedDate_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="inspectionBookedDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        Ext.QuickTips.init();

    }
    function inspectionDate_r(){


        new Ext.ToolTip({
            target: 'inspectionDate_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="inspectionDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        Ext.QuickTips.init();
    }

    function dateRepairAuthorised_r(){
        new Ext.ToolTip({
            target: 'dateRepairAuthorised_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="repairAuthorisedDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        Ext.QuickTips.init();
        // alert("repairBookInDateId :"+repairBookInDateId);


    }
    function repairBookInDate_r(){
        new Ext.ToolTip({
            target: 'repairBookInDate_r_Id',
            html: '<s:date  format="EEE d MMM HH:mm:ss yyyy" name="repairBookInDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        Ext.QuickTips.init();
    }
    function dateRepairCommenced_r(){
        new Ext.ToolTip({
            target: 'dateRepairCommenced_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="repairCommencedDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        Ext.QuickTips.init();

    }

    function isTotalLoss_r(){
        new Ext.ToolTip({
            target: 'isTotalLoss_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="isTotalLostCheckLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        Ext.QuickTips.init();

    }


    function dateTotalLossOfferMade_r(){

        new Ext.ToolTip({
            target: 'dateTotalLossOfferMade_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="totalLossOfferMadeLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        Ext.QuickTips.init();

    }
    function dateTotalLossOfferAccepted_r(){

        new Ext.ToolTip({
            target: 'dateTotalLossOfferAccepted_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="totalLossOfferAcceptedLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        Ext.QuickTips.init();
    }

    function dateTotalLossChequeIssued_r(){
        new Ext.ToolTip({
            target: 'dateTotalLossChequeIssued_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="totalLossCheckIssuedLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        Ext.QuickTips.init();

    }
    function dateTotalLossChequeReceived_r(){
        new Ext.ToolTip({
            target: 'dateTotalLossChequeReceived_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="totalLossCheckReceivedLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        Ext.QuickTips.init();
    }
    function repairCompletionDate_r(){
        new Ext.ToolTip({
            target: 'repairCompletionDate_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="repairCompletionDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        Ext.QuickTips.init();
    }

</script>
<fieldset class="x-fieldset">
    <legend>Hire Monitoring</legend>
    <div style="display:none" class="form-container" id="hireMonitoringRId">
        <table class="chox-table-form">
            <tr>
                <td><label class="std-label-ro">Next Review Date</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="nextReviewDate" /></label></td></tr>
            <tr>
                <td><label class="std-label-ro">
                        Original ECD</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="customer.InitialECDDesc" /></label></td></tr>
            <tr>
                <td><label class="std-label-ro">
                        Name Of Repairer
                    </label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="nameOfRepairer" /></label></td></tr>


            <s:if test="inspectionBookedDate!=null">
                <tr>
                    <td><label class="std-label-ro">
                            Inspection Booked Date<img src="../images/sign_info.png" id="inspectionBookedDate_r_Id" onmouseover="javascript:inspectionBookedDate_r();"/></label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="inspectionBookedDate" /></label></td></tr>
                </s:if>

            <s:else>
                <tr>
                    <td><label class="std-label-ro">
                            Inspection Booked Date</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="inspectionBookedDate" /></label></td></tr>

            </s:else>



            <s:if test="inspectionDate!=null">
                <tr>
                    <td><label class="std-label-ro">
                            Inspection Date<img src="../images/sign_info.png" id="inspectionDate_r_Id" onmouseover="javascript:inspectionDate_r();"/></label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="inspectionDate" /></label></td></tr>

            </s:if>
            <s:else>
                <tr>
                    <td><label class="std-label-ro">
                            Inspection Date</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="inspectionDate" /></label></td></tr>

            </s:else>

            <s:if test="repairAuthorisedDate!=null">
                <tr>
                    <td><label class="std-label-ro">
                            Date Repair Authorised<img src="../images/sign_info.png" id="dateRepairAuthorised_r_Id" onmouseover="javascript:dateRepairAuthorised_r();"/></label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairAuthorisedDate" /></label></td></tr>
                </s:if>
                <s:else>
                <tr>
                    <td><label class="std-label-ro">
                            Date Repair Authorised</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairAuthorisedDate" /></label></td></tr>

            </s:else>


            <s:if test="repairBookInDate!=null">
                <tr>
                    <td><label class="std-label-ro">
                            Repair Book In Date<img src="../images/sign_info.png" id="repairBookInDate_r_Id" onmouseover="javascript:repairBookInDate_r();"/></label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairBookInDate" /></label></td></tr>
                </s:if>
                <s:else>
                <tr>
                    <td><label class="std-label-ro">
                            Repair Book In Date</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairBookInDate" /></label></td></tr>

            </s:else>

            <s:if test="repairCommencedDate!=null">
                <tr>
                    <td><label class="std-label-ro">
                            Date Repair Commenced<img src="../images/sign_info.png" id="dateRepairCommenced_r_Id" onmouseover="javascript:dateRepairCommenced_r();"/></label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairCommencedDate" /></label></td></tr>
                </s:if>
                <s:else>
                <tr>
                    <td><label class="std-label-ro">
                            Date Repair Commenced</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairCommencedDate" /></label></td></tr>
                </s:else>

            <s:if test="isTotalLostCheck">
                <tr>
                    <td><label class="std-label-ro">
                            Is Total Loss?<img src="../images/sign_info.png" id="isTotalLoss_r_Id" onmouseover="javascript:isTotalLoss_r();"/></label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:property value="isTotalLossDesc" /></label></td></tr>
                </s:if>
                <s:else>
                <tr>
                    <td><label class="std-label-ro">
                            Is Total Loss?</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:property value="isTotalLossDesc" /></label></td></tr>
                </s:else>

            <s:if test="totalLossOfferMadeDate!=null">
                <tr>
                    <td><label class="std-label-ro">
                            Date Total Loss Offer Made<img src="../images/sign_info.png" id="dateTotalLossOfferMade_r_Id" onmouseover="javascript:dateTotalLossOfferMade_r();"/></label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferMadeDate" /></label></td></tr>
                </s:if>
                <s:else>
                <tr>
                    <td><label class="std-label-ro">
                            Date Total Loss Offer Made</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferMadeDate" /></label></td></tr>

            </s:else>

            <s:if test="totalLossOfferAcceptedDate!=null">
                <tr>
                    <td><label class="std-label-ro">
                            Date Total Loss Offer Accepted<img src="../images/sign_info.png" id="dateTotalLossOfferAccepted_r_Id" onmouseover="javascript:dateTotalLossOfferAccepted_r();"/></label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferAcceptedDate" /></label></td></tr>
                </s:if>
                <s:else>

                <tr>
                    <td><label class="std-label-ro">
                            Date Total Loss Offer Accepted</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferAcceptedDate" /></label></td></tr>

            </s:else>

            <s:if test="totalLossOfferCheckIssuedDate!=null">
                <tr>
                    <td><label class="std-label-ro">
                            Date Total Loss Cheque Issued<img src="../images/sign_info.png" id="dateTotalLossChequeIssued_r_Id" onmouseover="javascript:dateTotalLossChequeIssued_r();"/></label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferCheckIssuedDate" /></label></td></tr>
                </s:if>
                <s:else>
                <tr>
                    <td><label class="std-label-ro">
                            Date Total Loss Cheque Issued</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferCheckIssuedDate" /></label></td></tr>
                </s:else>
                <s:if test="totalLossOfferCheckReceivedDate!=null">
                <tr>
                    <td><label class="std-label-ro">
                            Date Total Loss Cheque Received<img src="../images/sign_info.png" id="dateTotalLossChequeReceived_r_Id" onmouseover="javascript:dateTotalLossChequeReceived_r();"/></label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferCheckReceivedDate" /></label></td></tr>
                </s:if>
                <s:else>
                <tr>
                    <td><label class="std-label-ro">
                            Date Total Loss Cheque Received</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferCheckReceivedDate" /></label></td></tr>

            </s:else>

            <s:if test="repairCompletionDate!=null">
                <tr>
                    <td><label class="std-label-ro">
                            Repair Completion Date<img src="../images/sign_info.png" id="repairCompletionDate_r_Id"  onmouseover="javascript:repairCompletionDate_r();"/></label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairCompletionDate"  /></label></td></tr>
                </s:if>

            <s:else>
                <tr>
                    <td><label class="std-label-ro">
                            Repair Completion Date</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairCompletionDate" /></label></td></tr>
                </s:else>

            <tr>
                <td><label class="std-label-ro">
                        Name of IME</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="nameOfIme" /></label></td></tr>
            <tr>
                <td><label class="std-label-ro">
                        Labour Rate (Per Hour)</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro">£<s:property value="labourRate" /></label></td></tr>
            <tr>
                <td><label class="std-label-ro">
                        Labour Hours</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="labourHour" /></label></td></tr>
            <tr>
                <td><label class="std-label-ro">
                        Total Labour Cost</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro">£<s:property value="labourCost" /></label></td></tr>
            <tr>
                <td><label class="std-label-ro">Labour Information Non-Provision Reason</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="nonProvisionReason" /></label></td>
            </tr>
        </table>
    </div>
</fieldset>
