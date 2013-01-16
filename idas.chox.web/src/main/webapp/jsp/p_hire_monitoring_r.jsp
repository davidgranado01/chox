<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">


    $(function(){


        createInfoHelpToolTip_r();


    });


    function createInfoHelpToolTip_r(){

        new Ext.ToolTip({
            target: 'inspectionBookedDate_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="inspectionBookedDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        new Ext.ToolTip({
            target: 'inspectionDate_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="inspectionDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        new Ext.ToolTip({
            target: 'dateRepairAuthorised_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="repairAuthorisedDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'repairBookInDate_r_Id',
            html: '<s:date  format="EEE d MMM HH:mm:ss yyyy" name="repairBookInDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        new Ext.ToolTip({
            target: 'dateRepairCommenced_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="repairCommencedDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        new Ext.ToolTip({
            target: 'isTotalLoss_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="isTotalLostCheckLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });


        new Ext.ToolTip({
            target: 'dateTotalLossOfferMade_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="totalLossOfferMadeLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });


        new Ext.ToolTip({
            target: 'dateTotalLossOfferAccepted_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="totalLossOfferAcceptedLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        new Ext.ToolTip({
            target: 'dateTotalLossChequeIssued_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="totalLossCheckIssuedLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'dateTotalLossChequeReceived_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="totalLossCheckReceivedLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        new Ext.ToolTip({
            target: 'repairCompletionDate_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="repairCompletionDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        new Ext.ToolTip({
            target: 'dateRepairOnlyOnHire_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="isRepairOnlyCheckLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'dateNonFaultinsurerManagingRepair_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="isNFInsurerManagingRepairLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'dateClientVatRegistered_r_Id',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="clientVatRegisteredLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
   
    }

</script>
<fieldset class="x-fieldset">
    <legend>Hire Monitoring</legend>
    <div style="display:none" class="form-container" id="hireMonitoringRId">
        <table class="chox-table-form">
            <tr>
                <td><label class="std-label-ro">Next Review Date</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="nextReviewDate" /></label></td>
            </tr>
            <tr>
                <td><label class="std-label-ro">Original ECD</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="customer.InitialECDDesc" /></label></td>
            </tr>
            <tr>
                <td><label class="std-label-ro">Name Of Repairer</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="nameOfRepairer" /></label></td>
            </tr>

            <s:if test="inspectionBookedDateLastModified!=null">
                <tr>
                    <td><label class="std-label-ro">Inspection Booked Date</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="inspectionBookedDate" /> <img src="../images/sign_info.png" alt="" width="13" height="13" id="inspectionBookedDate_r_Id" /></label></td>
                </tr>
            </s:if>
            <s:else>
                <tr>
                    <td><label class="std-label-ro">Inspection Booked Date</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="inspectionBookedDate" /> <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="inspectionBookedDate_r_Id" /></label></td>
                </tr>
            </s:else>

            <s:if test="inspectionDateLastModified!=null">
                <tr>
                    <td><label class="std-label-ro">Inspection Date</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="inspectionDate" /> <img src="../images/sign_info.png" width="13" alt="" height="13" id="inspectionDate_r_Id" /></label></td>
                </tr>
            </s:if>
            <s:else>
                <tr>
                    <td><label class="std-label-ro">Inspection Date</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="inspectionDate" /> <img style="display: none" src="../images/sign_info.png" width="13" alt="" height="13" id="inspectionDate_r_Id" /></label></td>
                </tr>
            </s:else>

            <s:if test="repairAuthorisedDateLastModified!=null">
                <tr>
                    <td><label class="std-label-ro">Date Repair Authorised</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairAuthorisedDate" /> <img src="../images/sign_info.png" alt="" width="13" height="13" id="dateRepairAuthorised_r_Id" /></label></td>
                </tr>
            </s:if>
            <s:else>
                <tr>
                    <td><label class="std-label-ro">Date Repair Authorised</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairAuthorisedDate" /> <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="dateRepairAuthorised_r_Id" /></label></td>
                </tr>
            </s:else>

            <s:if test="repairBookInDateLastModified!=null">
                <tr>
                    <td><label class="std-label-ro">Repair Book In Date</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairBookInDate" /> <img src="../images/sign_info.png" alt="" width="13" height="13" id="repairBookInDate_r_Id" /></label></td>
                </tr>
            </s:if>
            <s:else>
                <tr>
                    <td><label class="std-label-ro">Repair Book In Date</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairBookInDate" /> <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="repairBookInDate_r_Id" /></label></td>
                </tr>
            </s:else>

            <s:if test="repairCommencedDateLastModified!=null">
                <tr>
                    <td><label class="std-label-ro">Date Repair Commenced</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairCommencedDate" /> <img src="../images/sign_info.png" width="13" alt="" height="13" id="dateRepairCommenced_r_Id" /></label></td>
                </tr>
            </s:if>
            <s:else>
                <tr>
                    <td><label class="std-label-ro">Date Repair Commenced</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairCommencedDate" /> <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="dateRepairCommenced_r_Id" /></label></td>
                </tr>
            </s:else>

            <s:if test="repairCompletionDateLastModified!=null">
                <tr>
                    <td><label class="std-label-ro">Repair Completion Date</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairCompletionDate"  /> <img src="../images/sign_info.png" alt="" width="13" height="13"  id="repairCompletionDate_r_Id" /></label></td>
                </tr>
            </s:if>
            <s:else>
                <tr>
                    <td><label class="std-label-ro">Repair Completion Date</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="repairCompletionDate" /> <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13"  id="repairCompletionDate_r_Id" /></label></td>
                </tr>
            </s:else>

            <s:if test="isTotalLostCheckLastModified!=null">
                <tr>
                    <td><label class="std-label-ro">Is Total Loss?</label></td>
                    <td>&nbsp;</td>
                    <td><label id="hireMonitoringTotalLossId" class="std-data-ro"><s:property value="isTotalLossDesc" /> <img src="../images/sign_info.png" alt="" width="13" height="13" id="isTotalLoss_r_Id" /></label></td>
                </tr>
            </s:if>
            <s:else>
                <tr>
                    <td><label class="std-label-ro">Is Total Loss?</label></td>
                    <td>&nbsp;</td>
                    <td><label id="hireMonitoringTotalLossId" class="std-data-ro"><s:property value="isTotalLossDesc" /> <img style="display: none" alt="" src="../images/sign_info.png" width="13" height="13" id="isTotalLoss_r_Id" /></label></td>
                </tr>
            </s:else>

            <s:if test="totalLossOfferMadeLastModified!=null">
                <tr>
                    <td><label class="std-label-ro">Date Total Loss Offer Made</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferMadeDate" /> <img src="../images/sign_info.png" alt="" width="13" height="13" id="dateTotalLossOfferMade_r_Id" /></label></td>
                </tr>
            </s:if>
            <s:else>
                <tr>
                    <td><label class="std-label-ro">Date Total Loss Offer Made</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferMadeDate" /> <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="dateTotalLossOfferMade_r_Id" /></label></td>
                </tr>
            </s:else>

            <s:if test="totalLossOfferAcceptedLastModified!=null">
                <tr>
                    <td><label class="std-label-ro">Date Total Loss Offer Accepted</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferAcceptedDate" /> <img src="../images/sign_info.png" alt="" width="13" height="13" id="dateTotalLossOfferAccepted_r_Id" /></label></td>
                </tr>
            </s:if>
            <s:else>
                <tr>
                    <td><label class="std-label-ro">Date Total Loss Offer Accepted</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferAcceptedDate" /> <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="dateTotalLossOfferAccepted_r_Id" /></label></td>
                </tr>
            </s:else>

            <s:if test="totalLossCheckIssuedLastModified!=null">
                <tr>
                    <td><label class="std-label-ro">Date Total Loss Cheque Issued</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferCheckIssuedDate" /> <img src="../images/sign_info.png" alt="" width="13" height="13" id="dateTotalLossChequeIssued_r_Id" /></label></td>
                </tr>
            </s:if>
            <s:else>
                <tr>
                    <td><label class="std-label-ro">Date Total Loss Cheque Issued</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferCheckIssuedDate" /> <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="dateTotalLossChequeIssued_r_Id" /></label></td>
                </tr>
            </s:else>

            <s:if test="totalLossCheckReceivedLastModified!=null">
                <tr>
                    <td><label class="std-label-ro">Date Total Loss Cheque Received</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferCheckReceivedDate" /> <img src="../images/sign_info.png" alt="" width="13" height="13" id="dateTotalLossChequeReceived_r_Id" /></label></td>
                </tr>
            </s:if>
            <s:else>
                <tr>
                    <td><label class="std-label-ro">Date Total Loss Cheque Received</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:date format="dd/MM/yyyy" name="totalLossOfferCheckReceivedDate" /> <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="dateTotalLossChequeReceived_r_Id" /></label></td>
                </tr>
            </s:else>



            <tr>
                <td><label class="std-label-ro">Name of IME</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="nameOfIme" /></label></td>
            </tr>
            <tr>
                <td><label class="std-label-ro">Labour Rate (Per Hour)</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro">£<s:property value="labourRate" /></label></td>
            </tr>
            <tr>
                <td><label class="std-label-ro">Labour Hours</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="labourHour" /></label></td>
            </tr>
            <tr>
                <td><label class="std-label-ro">Total Labour Cost</label></td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro">£<s:property value="labourCost" /></label></td>
            </tr>
            <tr>
                <td><label class="std-label-ro">Labour Information Non-Provision Reason</label>
                </td>
                <td>&nbsp;</td>
                <td><label class="std-data-ro"><s:property value="nonProvisionReason" /></label>
                </td>
            </tr>

            <s:if test="isRepairOnlyCheckLastModified != null">
                <tr>
                    <td><label class="std-label-ro">Repair Only (No Hire)?</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:if test="isRepairOnlyCheck==true">Yes</s:if><s:else>No</s:else> <img src="../images/sign_info.png" alt="" width="13" height="13" id="dateRepairOnlyOnHire_r_Id" /></label></td>
                </tr>
            </s:if>
            <s:else>
                <tr>
                    <td><label class="std-label-ro">Repair Only (No Hire)?</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:if test="isRepairOnlyCheck==true">Yes</s:if><s:else>No</s:else> <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="dateRepairOnlyOnHire_r_Id" /></label></td>
                </tr>
            </s:else>

            <s:if test="isNFInsurerManagingRepairLastModified != null">
                <tr>
                    <td><label class="std-label-ro">Non-Fault Insurer Managing Repair?</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:if test="isNFInsurerManagingRepair==true">Yes</s:if><s:else>No</s:else> <img  src="../images/sign_info.png" alt="" width="13" height="13" id="dateNonFaultinsurerManagingRepair_r_Id" /></label></td>
                </tr>
            </s:if>
            <s:else>
                <tr>
                    <td><label class="std-label-ro">Non-Fault Insurer Managing Repair?</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:if test="isNFInsurerManagingRepair==true">Yes</s:if><s:else>No</s:else> <img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="dateNonFaultinsurerManagingRepair_r_Id" /></label></td>
                </tr>
            </s:else>

            <s:if test="clientVatRegisteredLastModified != null">
                <tr>
                    <td><label class="std-label-ro">Is Your Client VAT Registered?</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:property value="clientVatRegisteredDesc" /> <img  src="../images/sign_info.png" alt="" width="13" height="13" id="dateClientVatRegistered_r_Id" /></label></td>
                </tr>
            </s:if>
            <s:else>
                <tr>
                    <td><label class="std-label-ro">Is Your Client VAT Registered?</label></td>
                    <td>&nbsp;</td>
                    <td><label class="std-data-ro"><s:property value="clientVatRegisteredDesc" /><img style="display: none" src="../images/sign_info.png" alt="" width="13" height="13" id="dateClientVatRegistered_r_Id" /></label></td>
                </tr>
            </s:else>

        </table>
    </div>
</fieldset>
