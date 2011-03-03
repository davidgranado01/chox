<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
        
    $(function(){

        var repairBookInDatePicker = ui.dateField('repairBookInDate','<s:date format="dd/MM/yyyy" name="repairBookInDate" />','repairBookInDatePH');
        var repairAuthorisedDatePicker = ui.dateField('repairAuthorisedDate','<s:date format="dd/MM/yyyy" name="repairAuthorisedDate" />','repairAuthorisedDatePH');
        var repairCommencedDatePicker = ui.dateField('repairCommencedDate','<s:date format="dd/MM/yyyy" name="repairCommencedDate" />','repairCommencedDatePH');
        var inspectionBookedDateDatePicker = ui.dateField('inspectionBookedDate','<s:date format="dd/MM/yyyy" name="inspectionBookedDate" />','inspectionBookedDatePH');
        var inspectionDateDatePicker = ui.dateField('inspectionDate','<s:date format="dd/MM/yyyy" name="inspectionDate" />','inspectionDatePH');
        var nextReviewDatePicker = ui.dateField('nextReviewDate','<s:date format="dd/MM/yyyy" name="nextReviewDate" />','nextReviewDatePH');
        var repairCompletionDateDatePicker = ui.dateField('repairCompletionDate','<s:date format="dd/MM/yyyy" name="repairCompletionDate" />','repairCompletionDatePH');
        var totalLossOfferMadeDatePicker = ui.dateField('totalLossOfferMadeDate','<s:date format="dd/MM/yyyy" name="totalLossOfferMadeDate" />','totalLossOfferMadeDatePH');
        var totalLossOfferAcceptedDatePicker = ui.dateField('totalLossOfferAcceptedDate','<s:date format="dd/MM/yyyy" name="totalLossOfferAcceptedDate" />','totalLossOfferAcceptedDatePH');
        var totalLossOfferCheckIssuedDatePicker = ui.dateField('totalLossOfferCheckIssuedDate','<s:date format="dd/MM/yyyy" name="totalLossOfferCheckIssuedDate" />','totalLossOfferCheckIssuedDatePH');
        var totalLossOfferCheckReceivedDatePicker = ui.dateField('totalLossOfferCheckReceivedDate','<s:date format="dd/MM/yyyy" name="totalLossOfferCheckReceivedDate" />','totalLossOfferCheckReceivedDatePH');

        
        var form = $("form#formUpdateHireMonitoringDetail");

        var fsets =  $('legend',form);
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});
        
        form.validate(
        {
            errorLabelContainer: "#HMmessageBox",
            rules: {
                repairBookInDate:{date:true},
                repairAuthorisedDate:{date:true},
                repairCommencedDate:{date:true},
                inspectionBookedDate:{date:true},
                inspectionDate:{date:true},
                repairCompletionDate:{date:true},
                totalLossOfferMadeDate:{date:true},
                totalLossOfferAcceptedDate:{date:true},
                totalLossOfferCheckIssuedDate:{date:true},
                totalLossOfferCheckReceivedDate:{date:true},
                nextReviewDate:{date:true},
                labourRate :{number:true},
                labourHour :{number:true},
                labourCost :{number:true},
                nonProvisionReason :{required: isNonProvisionReasonRequired},
                date_compare_field:{required: isDateCorrect}
            },
            messages: {
                nameOfRepairer:{required:"You must supply a date for 'Name Of Repairer'"},
                repairBookInDate: {date:"Invalid date format for 'Repair Book In Date'"},
                repairAuthorisedDate: {date:"Invalid date format for 'Date Repair Authorised'"},
                repairCommencedDate: {date:"Invalid date format for 'Date Repair Commenced'"},
                inspectionBookedDate: {date:"Invalid date format for 'Inspection Booked Date'"},
                inspectionDate: {date:"Invalid date format for 'Inspection Date'"},
                nextReviewDate: {date:"Invalid date format for 'Next Review Date'"},
                repairCompletionDate: {date:"Invalid date format for 'Repair Completion Date'"},
                totalLossOfferMadeDate: {date:"Invalid date format for 'Date Total Loss Offer Made'"},
                totalLossOfferAcceptedDate: {date:"Invalid date format for 'Date Total Loss Offer Accepted'"},
                totalLossOfferCheckIssuedDate: {date:"Invalid date format for 'Date Total Loss Cheque Issued'"},
                totalLossOfferCheckReceivedDate: {date:"Invalid date format for 'Date Total Loss Cheque Received'"},
                labourRate :{number:"You must supply a numeric value for 'Labour Rate'"},
                labourHour :{number:"You must supply a numeric value for 'Labour Hours'"},
                labourCost :{number:"You must supply a numeric value for 'Total Labour Cost'"},
                nonProvisionReason :{required:"You must select 'Labour Information Non-Provision Reason' if 'Labour Rate', 'Labour Hours' or 'Total Labour Cost' cannot be provided"},
                date_compare_field:{required:"The 'Repair Completion Date' must be after the 'Repair Book In Date'"}
            }
        });

        ui.ajaxForm(form,onHireMonitoringSubmitResponseReceived,'html');

        var repairBookDt = $("#repairBookInDate").val();
        $("#notificationRepairBookInDate").val(repairBookDt);




        //Call Information Help ToolTip
        createInfoHelp();


    });
        
    function isDateCorrect(){
            
        var bFlag = true;
        var repairBookInDt = $("#repairBookInDatePH :input").val();
        var repairCompletionDt = $("#repairCompletionDatePH :input").val();
            
        if(repairBookInDt!="" && repairCompletionDt!=""){
            var dRepairBookInDt = getDate(repairBookInDt);
            var dRepairCompletionDt = getDate(repairCompletionDt);
            bFlag = (dRepairBookInDt <= dRepairCompletionDt);
        }
            
        return !bFlag;
    }
        
    function isNonProvisionReasonRequired(){
        $(".chox-form-submit-result").html("");
        return false;
    }
        
    function onHireMonitoringSubmitResponseReceived(responseText, statusText){
        var repairBookDt = $("#repairBookInDate").val();
        $("#notificationRepairBookInDate").val(repairBookDt);            
    }

    function createInfoHelp(){
        
        new Ext.ToolTip({
            target: 'inspectionBookedDateId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="inspectionBookedDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'inspectionDateId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="inspectionDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        new Ext.ToolTip({
            target: 'dateRepairAuthorisedId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="repairAuthorisedDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        new Ext.ToolTip({
            target: 'repairBookInDateId',
            html: '<s:date  format="EEE d MMM HH:mm:ss yyyy" name="repairBookInDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'dateRepairCommencedId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="repairCommencedDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'isTotalLossId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="isTotalLostCheckLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'dateTotalLossOfferMadeId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="totalLossOfferMadeLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'dateTotalLossOfferAcceptedId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="totalLossOfferAcceptedLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });

        new Ext.ToolTip({
            target: 'dateTotalLossChequeIssuedId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="totalLossCheckIssuedLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'dateTotalLossChequeReceivedId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="totalLossCheckReceivedLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
        new Ext.ToolTip({
            target: 'repairCompletionDateId',
            html: '<s:date format="EEE d MMM HH:mm:ss yyyy" name="repairCompletionDateLastModified"/>',
            title: 'Field Last Modified On',
            autoHide: true,
            closable: true,
            draggable:true
        });
    }

</script>

<form id="formUpdateHireMonitoringDetail" name="formUpdateHireMonitoringDetail"
      action="<%=request.getContextPath()%>/prv/p/updateHireMonitorDetail.action" method="POST">

    <input type="hidden" name="claimId" value='<s:property value="claimId"/>'>
    <input type="hidden" name="date_compare_field" value=''>
    <s:hidden value="notificationRepairBookInDate" id="notificationRepairBookInDate" name="notificationRepairBookInDate"/>

    <fieldset class="x-fieldset partial">

        <legend>Hire Monitoring</legend>

        <div class="form-container" id="hireMonitoringWId">

            <div class="chox-form-item">
                <label class="chox-form-std-label2">
                    Next Review Date</label>
                <span id="nextReviewDatePH"></span>
            </div>

            <div class="chox-form-item">
                <label class="chox-form-std-label2">
                    Original ECD</label><label class="std-data-ro"><s:property value="customer.InitialECDDesc" /></label>&nbsp;</div>

            <div class="chox-form-item">
                <label class="chox-form-std-label2">Name Of Repairer</label>
                <input type="text" class="chox-ttxt" name="nameOfRepairer" value="<s:property value="nameOfRepairer" />"/></div>


            <s:if test="inspectionBookedDateLastModified!=null">

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">
                        Inspection Booked Date <img src="../images/sign_info.png" width="13" height="13" id="inspectionBookedDateId" /></label>
                    <span id="inspectionBookedDatePH"></span>
                </div>

            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">
                        Inspection Booked Date <img style="display:none" src="../images/sign_info.png" width="13" height="13" id="inspectionBookedDateId" /></label>
                    <span id="inspectionBookedDatePH"></span>
                </div>
            </s:else>



            <s:if  test="inspectionDateLastModified != null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">
                        Inspection Date <img src="../images/sign_info.png" width="13" height="13" id="inspectionDateId" /></label>
                    <span id="inspectionDatePH"></span>
                </div>


            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">
                        Inspection Date <img style="display:none" src="../images/sign_info.png" width="13" height="13" id="inspectionDateId" /> </label>
                    <span id="inspectionDatePH"></span>
                </div>
            </s:else>

            <s:if test="repairAuthorisedDateLastModified != null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Repair Authorised <img src="../images/sign_info.png" width="13" height="13" id="dateRepairAuthorisedId" /></label>
                    <span id="repairAuthorisedDatePH"></span>
                </div>

            </s:if>

            <s:else>

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Repair Authorised <img  style="display:none" src="../images/sign_info.png" width="13" height="13" id="dateRepairAuthorisedId" /></label>
                    <span id="repairAuthorisedDatePH"></span>
                </div>

            </s:else>


            <s:if test="repairBookInDateLastModified!=null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Repair Book In Date <img src="../images/sign_info.png" width="13" height="13" id="repairBookInDateId" /></label>
                    <span id="repairBookInDatePH"></span>
                </div>

            </s:if>
            <s:else>
                <div class="chox-form-item" >
                    <label class="chox-form-std-label2">Repair Book In Date <img style="display: none" src="../images/sign_info.png" width="13" height="13" id="repairBookInDateId" /></label>
                    <span id="repairBookInDatePH"></span>
                </div>

            </s:else>



            <s:if test="repairCommencedDateLastModified != null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Repair Commenced <img src="../images/sign_info.png" width="13" height="13" id="dateRepairCommencedId" /></label>
                    <span id="repairCommencedDatePH"></span>
                </div>

            </s:if>

            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Repair Commenced <img style="display: none" src="../images/sign_info.png" width="13" height="13" id="dateRepairCommencedId" /></label>
                    <span id="repairCommencedDatePH"></span>
                </div>


            </s:else>

            <s:if test="repairCompletionDateLastModified != null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">
                        Repair Completion Date <img src="../images/sign_info.png" width="13" height="13" id="repairCompletionDateId" /></label>
                    <span id="repairCompletionDatePH"></span>
                </div>

            </s:if>
            <s:else>

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">
                        Repair Completion Date <img style="display: none" src="../images/sign_info.png" width="13" height="13" id="repairCompletionDateId" /></label>
                    <span id="repairCompletionDatePH"></span>
                </div>
            </s:else>

            <s:if test="isTotalLostCheckLastModified!=null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">
                        Is Total Loss? <img src="../images/sign_info.png" width="13" height="13" id="isTotalLossId" /></label>
                        <s:checkbox name="isTotalLostCheck" />
                </div>
            </s:if>
            <s:else>

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">
                        Is Total Loss? <img style="display: none" src="../images/sign_info.png" width="13" height="13" id="isTotalLossId" /></label>
                        <s:checkbox name="isTotalLostCheck" />
                </div>
            </s:else>



            <s:if test="totalLossOfferMadeLastModified!=null">

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Offer Made <img src="../images/sign_info.png" width="13" height="13" id="dateTotalLossOfferMadeId" /></label>
                    <span id="totalLossOfferMadeDatePH"></span>
                </div>
            </s:if>
            <s:else>

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Offer Made <img style="display: none" src="../images/sign_info.png" width="13" height="13" id="dateTotalLossOfferMadeId" /></label>
                    <span id="totalLossOfferMadeDatePH"></span>
                </div>

            </s:else>


            <s:if test="totalLossOfferAcceptedLastModified!=null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Offer Accepted <img src="../images/sign_info.png" width="13" height="13" id="dateTotalLossOfferAcceptedId" /></label>
                    <span id="totalLossOfferAcceptedDatePH"></span>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Offer Accepted <img style="display: none"src="../images/sign_info.png" width="13" height="13" id="dateTotalLossOfferAcceptedId" /></label>
                    <span id="totalLossOfferAcceptedDatePH"></span>
                </div>
            </s:else>


            <s:if test="totalLossCheckIssuedLastModified!=null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Cheque Issued <img src="../images/sign_info.png" width="13" height="13" id="dateTotalLossChequeIssuedId" /></label>
                    <span id="totalLossOfferCheckIssuedDatePH"></span>
                </div>
            </s:if>
            <s:else>

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Cheque Issued <img style="display: none" src="../images/sign_info.png" width="13" height="13" id="dateTotalLossChequeIssuedId" /></label>
                    <span id="totalLossOfferCheckIssuedDatePH"></span>
                </div>
            </s:else>

            <s:if test="totalLossCheckReceivedLastModified != null">
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Cheque Received <img src="../images/sign_info.png" width="13" height="13" id="dateTotalLossChequeReceivedId" /></label>
                    <span id="totalLossOfferCheckReceivedDatePH"></span>
                </div>
            </s:if>
            <s:else>
                <div class="chox-form-item">
                    <label class="chox-form-std-label2">Date Total Loss Cheque Received <img style="display: none" src="../images/sign_info.png" width="13" height="13" id="dateTotalLossChequeReceivedId" /></label>
                    <span id="totalLossOfferCheckReceivedDatePH"></span>
                </div>
            </s:else>


            <div class="chox-form-item">
                <label class="chox-form-std-label2">
                    Name of IME</label>
                <input type="text" class="chox-ttxt" name="nameOfIme" value="<s:property value="nameOfIme" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label2">
                    Labour Rate (Per Hour)</label>
                <input type="text" class="chox-ttxt" name="labourRate" id="labourRate" value="<s:property value="labourRate" />"/></div>
            <div class="chox-form-item">
                <label class="chox-form-std-label2">
                    Labour Hours</label>
                <input type="text" class="chox-ttxt" name="labourHour" id="labourHour" value="<s:property value="labourHour" />"/></div>

            <div class="chox-form-item">
                <label class="chox-form-std-label2">
                    Total Labour Cost</label>
                <input type="text" class="chox-ttxt" name="labourCost" id="labourCost" value="<s:property value="labourCost" />"/>
            </div>
            
            <div class="chox-form-item">
                <label class="chox-form-std-label2">Labour Information <br/>Non-Provision Reason</label>
                    <s:select name="nonProvisionReason"
                              list="nonProvisionReasons"
                              headerKey="" listKey="text"
                              listValue="value"
                              headerValue="-- Please Select --"
                              emptyOption="false" cssStyle="width:230px"></s:select>
            </div><br/>

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">
                        Repair Only (No Hire)? </label>
                    <s:checkbox  name="isRepairOnlyCheck" />
                </div>

                <div class="chox-form-item">
                    <label class="chox-form-std-label2">
                        Non-Fault Insurer Managing Repair? </label>
                    <s:checkbox  name="isNFInsurerManagingRepair" />
                </div><br/>


            <div class="chox-form-button">
                <input type="submit" value="Save Changes" /><s:checkbox name="isUpdateInsurer" /><label>Update Insurer</label>
            </div>
            <div id="HMmessageBox" style="text-align:center" class="action-error-msg"></div>
            <div class="chox-form-submit-result"><s:property value="actionResult" /></div>

        </div>

    </fieldset>
    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    <!--s:token/-->
</form>