    <%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script src="<%= request.getContextPath()%>/scripts/actionPanelLib.js" type="text/javascript"></script>
<script src="<%= request.getContextPath()%>/scripts/activityMonitor.js" type="text/javascript"></script>

<script type="text/javascript">

    var claimDetailTabAccessibility = <s:property value="tabAccessibility.claimDetailTabAccessibility" />;
    var invoiceDetailTabAccessibility = <s:property value="tabAccessibility.invoiceDetailTabAccessibility" />;
    var hireMonitoringTabAccessibility = <s:property value="tabAccessibility.hireMonitoringTabAccessibility" />;
    var historyTabAccessibility = <s:property value="tabAccessibility.historyTabAccessibility" />;
    var notesTabAccessibility = <s:property value="tabAccessibility.notesTabAccessibility" />;
    var paymentPackTabAccessibility = <s:property value="tabAccessibility.paymentPackTabAccessibility" />;
    var auditTrailTabAccessibility = <s:property value="tabAccessibility.auditTrailTabAccessibility" />;

    var claimDetailsDisabled = claimDetailTabAccessibility == 0;
    var hireMonitoringDetailsDisabled = hireMonitoringTabAccessibility  == 0;
    var invoiceDetailsDisabled = invoiceDetailTabAccessibility == 0;
    var paymentPackDisabled = paymentPackTabAccessibility == 0;
    var historyDetailsDisabled = historyTabAccessibility == 0;
    var commentsDisabled = notesTabAccessibility == 0;
    var auditTrailDisabled = auditTrailTabAccessibility == 0;
    var popupTimeUp = 900000;

    $(function(){
        
        $('fieldset.partial legend').next().hide();
        var fsets =  $('fieldset:not(.partial) legend');
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        var pingServerUrl = '<%=request.getContextPath()%>/prv/p/activityMonitoringAction.action';
        var checkStatusIUrl = '<%=request.getContextPath()%>/prv/p/checkViewingStatus.action';
        var claimId = <s:property value="id" />;

        activityMonitor.setup(pingServerUrl, checkStatusIUrl, claimId);
        
        if(!<s:property value="isChoxAdmin"/>){
            activityMonitor.pingServer();
        }

    });

    Ext.BLANK_IMAGE_URL = '<%= request.getContextPath()%>/images/default/s.gif';

    Ext.onReady(function(){

        new Ext.TabPanel({
            renderTo: 'tabContainer',
            width:960,
            activeTab: 0,
            frame:false,
            plain:true,
            defaults:{autoHeight: true},
            items:[
                {contentEl:'claimDetails', title: 'Claim Details', disabled: claimDetailsDisabled},
                {contentEl:'hireMonitoringDetails', title: 'Hire Monitoring', disabled: hireMonitoringDetailsDisabled},
                {contentEl:'invoiceDetails', title: 'Invoice Details', disabled: invoiceDetailsDisabled},
                {contentEl:'attachmentTab', title: 'Attachments', disabled: paymentPackDisabled, autoLoad: {url:"p/getAttachmentPage.action?claimId="+<s:property value="id" />+"&rdn="+getRandomNumber(), scripts:true}},
                {contentEl:'historyTab', title: 'History', disabled: historyDetailsDisabled, autoLoad: {url:"p/getHistoryPage.action?claimId="+<s:property value="id" />+"&rdn="+getRandomNumber(), scripts:true}},
                {contentEl:'auditTrailTab', title: 'Claim Cycle', disabled: auditTrailDisabled, autoLoad: {url:"p/getAuditTrailPage.action?claimId="+<s:property value="id" />+"&rdn="+getRandomNumber(), scripts:true}},
                {contentEl:'commentTab', title: 'Notes', disabled: commentsDisabled, autoLoad: {url:"p/getClaimDetailCommentPage.action?claimId="+<s:property value="id" />+"&rdn="+getRandomNumber(), scripts:true}}
            ]
        });

        /***********************************************************************************
         * HIRE MONITORING
         ***********************************************************************************/
        if(!hireMonitoringDetailsDisabled){

            ecdJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'sequence'},
                    {name:'ecdDate'},
                    {name:'reason'},
                    {name:'supportingNote'}]
            });

            ecdDataStore = new Ext.data.Store({
                proxy: new Ext.data.HttpProxy
                ({url: '<%= request.getContextPath()%>/prv/p/getHireMonitoringEcds.action',method:'GET'}),
                reader:ecdJsonReader
            });

            ecdGrid = new Ext.grid.GridPanel({
                listeners:  {cellclick:loadHireMonitor },
                store: ecdDataStore,
                renderTo:'ecdGridHolder',
                enableHdMenu:false,
                layout:'fit',
                viewConfig:{forceFit:true},
                columns: [
                    {header: "", width: 20, dataIndex: 'sequence', sortable: false, resizable: true},
                    {header: "ECD Date", width: 70, dataIndex: 'ecdDate', sortable: false, resizable: true},
                    {header: "Reason", width: 80, dataIndex: 'reason', sortable: false, resizable: true},
                    {header: "Supporting Note", width: 280, dataIndex: 'supportingNote', sortable: false, resizable: true}
                ],
                width:445,
                autoHeight:true
            });

            loadEcds();
        }
    });

    function loadHireMonitor(grid, rowIndex, columnIndex, e){
        var hiremonitoringECD = ecdGrid.getStore().getAt(rowIndex);
        var supportingNoteText = "<br/><b>Supporting note</b>: <br/>"+hiremonitoringECD.get("supportingNote");
        var EcdText = "<b>ECD Date</b>: "+hiremonitoringECD.get("ecdDate");
        var ReasonText = "<b>Reason</b>: "+hiremonitoringECD.get("reason");
        var title = EcdText;
        var msg = EcdText + "<br/>" + ReasonText + "<br/>" + supportingNoteText;
        propmtMsg(title, msg);
    }

    function loadEcds(){
        if(!hireMonitoringDetailsDisabled){
            ecdDataStore.load({params:{id : <s:property value="id" />}});
        }
    }

    /***********************************************************************************
     * UPDATE ANOMALIES
     ***********************************************************************************/
    function updateAnomalies(a){
        document.location = "<%= request.getContextPath()%>/prv/doUpdateAnomalies.action?id="+a;
    }

    /***********************************************************************************
     * CLOSE OR OPEN CLAIM
     ***********************************************************************************/
    function closeClaimStatus(){

        if(confirm('Are you sure you want to close this claim?')){
            var url = "<%= request.getContextPath()%>/prv/processClaim.action";
            var param = {"name":"closeClaim", "id":<s:property value="id" />};
            ajax.loadHtml(url, param, pageRefresh);
            return true;
        }

        return false;
    }

    function reopenClaimStatus(){

        if(confirm('Are you sure you want to re-open this claim?')){
            var url = "<%= request.getContextPath()%>/prv/processClaim.action";
            var param = {"name":"reopenClaim", "id":<s:property value="id" />};
            ajax.loadHtml(url, param, pageRefresh);
            return true;
        }
        return false;
    }

    function pageRefresh(){
        document.location = "<%= request.getContextPath()%>/prv/openClaimDetail.action?id="+<s:property value="id" />;
    }

    /***********************************************************************************
     * REMOVE NOTIFICATION
     ***********************************************************************************/
    function removeNotification(notificationId)
    {
        var url = "<%= request.getContextPath()%>/prv/p/removeNotification.action";
        var param = {"notificationId" : notificationId,"id": <s:property value="id" />};
        ajax.loadHtml(url,param,function(data){
            $("div#notificationNotesDiv").html(data);
        });
    }

    /***********************************************************************************
     * CLAIM DETAIL MORE ACTION PANEL
     ***********************************************************************************/
    function moreActionOnchange(){

        var target = "#moreActionPanel";
        var selectedAction = $("div#claim-detail-extra #extraAction").val();
        $(target).html("");

        if(selectedAction!="" && selectedAction!=null){
            var url = "<%= request.getContextPath()%>/prv/p/"+selectedAction+".action";
            var param = {"id":<s:property value="id" />};
            ajax.loadHtml(url,param,function(data){
                $(target).html(data);
            });
        }

    }
</script>

<div style="width:960px">

    <div class="chox-claim-header x-panel-bwrap chox-form-container">

        <fieldset class="x-fieldset loaded open-by-default">
            <legend>Claim Summary</legend>
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td><label class="chox-claim-header-label">Third Party Insurer</label><label class="chox-claim-header-text"><s:property value="thirdParty.insurer.name" /></label></td>
                    <td><label class="chox-claim-header-label">Credit Hire Organisation</label><label class="chox-claim-header-text"><s:property value="chorganisation.name" /></label></td>
                    <td><label class="chox-claim-header-label">Created By</label><label class="chox-claim-header-text"><s:property value="createdByDesc" /></label></td>
                </tr>
                <tr>
                    <td><label class="chox-claim-header-label">Supplier Reference</label><label class="chox-claim-header-text"><s:property value="choReference" /></label></td>
                    <td><label class="chox-claim-header-label">Insurer Claim Number</label><label class="chox-claim-header-text"><s:property value="claimNumber" /></label></td>
                    <td><label class="chox-claim-header-label">Created On</label><label class="chox-claim-header-text"><s:date name="createdDate" format="dd MMM yyyy HH:mm"  /></label></td>
                </tr>
                <tr>
                    <td><label class="chox-claim-header-label">Customer</label><label class="chox-claim-header-text"><span id="status"><s:property value="customer.formattedName" /></span></label></td>
                    <td><label class="chox-claim-header-label">Current Status</label><label class="chox-claim-header-text"><span id="status"><s:property value="status" /></span></label><!--span id="statusTip"><img src="img/tip.gif" style="fixed:relative;top:-50" /></span--></td>
                    <td><label class="chox-claim-header-label">Policy Holder Contact Date</label><label class="chox-claim-header-text"><span id="status"><s:date name="policyHolderContactDate" format="dd MMM yyyy HH:mm"  /></span></label></td>
                </tr>
                <tr>
                    <td><label class="chox-claim-header-label">Claim Owner</label><label class="chox-claim-header-text"><span id="status"><s:property value="claimOwner.fullName"  /></span></label></td>
                    <td><label class="chox-claim-header-label">Workgroup</label><label class="chox-claim-header-text"><s:property value="workgroup.name" /></label></td>
                    <td><label class="chox-claim-header-label">Indemnity Value</label><label class="chox-claim-header-text"><span id="status">£<s:property value="indemnityAmount" /></span></label></td>
                </tr>
                <tr>
                    <td><label class="chox-claim-header-label">Liability Status</label><label class="chox-claim-header-text"><span id="status"><s:property value="liabilityStatus" /></span></label></td>
                    <td><label class="chox-claim-header-label">Percentage Liability Agreed (Insurer)</label><label class="chox-claim-header-text"><span id="status"><s:property value="formattedInsLiab" />%</span></label></td>
                    <td><label class="chox-claim-header-label">Percentage Liability Agreed (CHO)</label><label class="chox-claim-header-text"><s:property value="formattedChoLiab" />%</label></td>
                </tr>
                <tr>
                    <td><label class="chox-claim-header-label">Liability Agreed Date</label><label class="chox-claim-header-text"><span id="status"><s:property value="liabilityAgreedDate" /></span></label></td>
                    <td></td>
                    <td></td>
                </tr>

                <s:if test="!isCHO && isFnolReviewed && isFnolPanelVisible">
                    <tr>
                        <td colspan="3">
                            <div class="status-info">This claim has been reviewed by an FNOL Handler, please review notes that may have been added before proceeding.</div>
                        </td>
                    </tr>
                </s:if>
                <s:if test="!IsClaimClosedStatuses && isCHO">
                    <tr>
                        <td colspan="3" align="right"><input value="Close Claim" type="button" onclick="javascript: return closeClaimStatus();"/></td>
                    </tr>
                </s:if>
                <s:elseif test="isClaimClosed && isCHO">
                    <tr>
                        <td colspan="3" align="right"><input value="Re-Open Claim" type="button" onclick="javascript: return reopenClaimStatus();"/></td>
                    </tr>
                </s:elseif>
            </table>
        </fieldset>
        <div id="claim-detail-extra">
            <div>
                <a href="<s:url action="inbox"><s:param name="showHistory">1</s:param></s:url>">« Back to Search Results</a>
            </div>
            <div>
                <s:if test="extraActionList.size()>0">
                    <s:select
                        name="extraAction"
                        id="extraAction"
                        list="extraActionList"
                        listKey="text"
                        listValue="value"
                        headerKey=""
                        headerValue="-- More Actions --"
                        emptyOption="false"
                        onchange="javascript: moreActionOnchange();">
                    </s:select>
                </s:if>
            </div>
        </div>
    </div>
</div>

<div id="moreActionPanel" class="extra-action-class"></div>

<s:if test="isClaimNumberDuplicated && notificationAccessibility.claimNumberNotificationAccessibility">
    <s:action name="getDuplicatedClaimAlert" namespace="/prv/p" executeResult="true">
        <s:param name="claimId"><s:property value="id" /></s:param>
        <s:param name="claimNumber"><s:property value="claimNumber" /></s:param>
    </s:action>
</s:if>

<s:if test="notificationAccessibility.userViewingNotificationAccessibility">
    <div id="userViewingThisClaimDiv" class="status-warning" style="display:none;">
        This claim is currently being viewed and / or modified by the following user(s) : <span id="userViewingThisClaim"></span>
    </div>
</s:if>

<s:if test="isAnyIntelligentNotes && notificationAccessibility.intelligentNotesNotificationAccessibility">
    <div class="chox-claim-header x-panel-bwrap chox-form-container">
        <fieldset class="x-fieldset">
            <legend>Additional Notes</legend>
            <div id="intelligentNotesDiv" class="status-warning listContainer">
                <ul>
                    <s:iterator value="intelligentNotes">
                        <li><s:property/></li>
                    </s:iterator>
                </ul>
            </div>
        </fieldset>
    </div>
</s:if>

<s:if test="notificationAccessibility.notificationNotesNotificationAccessibility">
    <div id="notificationNotesDiv">
        <s:action namespace="/prv/p" executeResult="true" name="renderNotifications">
            <s:param name="id"><s:property value="id" /></s:param>
        </s:action>
    </div>
</s:if>

<script type="text/javascript">

    $(document).ready(function() {

        var strgeneralActionPanelText = $("#generalActionPanel").html();
        strgeneralActionPanelText = strgeneralActionPanelText.replace('<div class="action-message"></div>',"");
        strgeneralActionPanelText = strgeneralActionPanelText.replace('<h1>',"");
        strgeneralActionPanelText = strgeneralActionPanelText.replace('</h1>',"");
        strgeneralActionPanelText = strgeneralActionPanelText.replace(/\s+/g,'');

        if(strgeneralActionPanelText.length<=0){
            $("#generalActionPanel").hide();
            $("#generalActionPanel").css("display:", "none");
        }else{
            $("#generalActionPanel").show();
            $("#generalActionPanel").css("display:", "block");
        }

    });

</script>

<div id="generalActionPanel" style="display: none;">
    <s:action name="getActionPanel" namespace="/prv/p" executeResult="true" />
    <div class="action-message"><s:property value="actionResult" /></div>
    <div class="action-error-msg"><s:property value="actionError" /></div>
</div>

<s:if test="isShowPenaltyChargeAlert">
    <div class="chox-claim-header x-panel-bwrap chox-form-container">
        <s:action name="getAlertPanel" namespace="/prv/p" executeResult="true" />
    </div>
</s:if>

<div id="tabContainer">

    <div id="claimDetails">
        <s:if test="tabAccessibility.claimDetailTabAccessibility != 0">
            <div class="x-panel-bwrap chox-form-container">
                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                    <tr valign="top">
                        <td class="chox-form-left-col">
                            <div>
                                <s:action name="getCustomer" namespace="/prv/p" executeResult="true">
                                    <s:param name="claimId"><s:property value="id"/></s:param>
                                </s:action>
                            </div>
                            <div>
                                <s:action name="getInjury" namespace="/prv/p" executeResult="true">
                                    <s:param name="claimId"><s:property value="id" /></s:param>
                                </s:action>
                            </div>
                            <div>
                                <s:action name="getSolicitor" namespace="/prv/p" executeResult="true">
                                    <s:param name="claimId"><s:property value="id" /></s:param>
                                </s:action>
                            </div>
                            <div>
                                <s:action name="getCustomerVehicleDamage" namespace="/prv/p" executeResult="true">
                                    <s:param name="claimId"><s:property value="id" /></s:param>
                                </s:action>
                            </div>
                        </td>
                        <td>
                            <div>
                                <s:action name="getClaimDetails" namespace="/prv/p" executeResult="true">
                                    <s:param name="claimId"><s:property value="id" /></s:param>
                                </s:action>
                            </div>
                            <s:if test="isInsurer || isChoxAdmin">
                                <fieldset class="x-fieldset">
                                    <legend>Claim Reviews</legend>
                                    <div style="display:none" class="form-container">
                                        <!--
                                        <div class="chox-form-item">
                                            <label class="std-label-ro">Quantum</label>
                                            <label class="std-data-ro"><s:property value="isQuantumDisputeDesc"/></label>
                                        </div>
                                        -->
                                        <div class="chox-form-item">
                                            <label class="std-label-ro">Invoice Review Required</label>
                                            <label class="std-data-ro"><s:property value="isInvoiceReviewRequiredDesc" /></label>
                                        </div>
                                    </div>
                                </fieldset>
                            </s:if>
                            <div>
                                <s:action name="getIncident" namespace="/prv/p" executeResult="true">
                                    <s:param name="claimId"><s:property value="id" /></s:param>
                                </s:action>
                            </div>
                            <div>
                                <s:action name="getThirdParty" namespace="/prv/p" executeResult="true">
                                    <s:param name="claimId"><s:property value="id" /></s:param>
                                </s:action>
                            </div>
                            <div>
                                <s:action name="getWitness" namespace="/prv/p" executeResult="true">
                                    <s:param name="claimId"><s:property value="id" /></s:param>
                                </s:action>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </s:if>
    </div>

    <div id="hireMonitoringDetails" class="x-hide-display">
        <s:if test="tabAccessibility.hireMonitoringTabAccessibility != 0">
            <div class="x-panel-bwrap chox-form-container">
                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                    <tr valign="top">
                        <td class="chox-form-left-col">
                            <s:action name="getHireMonitoringDetail" namespace="/prv/p" executeResult="true">
                                <s:param name="claimId"><s:property value="id" /></s:param>
                            </s:action>
                        </td>
                        <td>
                            <s:action name="getHireMonitoringEcd" namespace="/prv/p" executeResult="true">
                                <s:param name="claimId"><s:property value="id" /></s:param>
                                <s:param name="iECDFormAccessRight"><s:property value="tabAccessibility.hireMonitoringTabAccessibility" /></s:param>
                            </s:action>
                        </td>
                    </tr>
                </table>
            </div>
            <div style="display:none" id="hireMonitorTemplate">
                <input type="button" value="Close" id="hireMonitorModalClose"><br/>
                <div id="hireMonitorMessage"></div>
            </div>
        </s:if>
    </div>

    <div id="invoiceDetails" class="x-hide-display">
        <s:if test="tabAccessibility.invoiceDetailTabAccessibility != 0">
            <div class="x-panel-bwrap chox-form-container">
                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                    <tr valign="top">
                        <td class="chox-form-left-col">
                            <div>
                                <s:action name="getInvoice" namespace="/prv/p" executeResult="true">
                                    <s:param name="claimId"><s:property value="id" /></s:param>
                                    <s:param name="claimStatus"><s:property value="status" /></s:param>
                                </s:action>
                            </div>
                            <div>
                                <s:action name="getVehicleHire" namespace="/prv/p" executeResult="true">
                                    <s:param name="claimId"><s:property value="id" /></s:param>
                                    <s:param name="claimStatus"><s:property value="status" /></s:param>
                                </s:action>
                            </div>
                        </td>
                        <td>
                            <div>
                                <s:action name="getExtra" namespace="/prv/p" executeResult="true">
                                    <s:param name="claimId"><s:property value="id" /></s:param>
                                    <s:param name="claimStatus"><s:property value="status" /></s:param>
                                </s:action>
                            </div>
                            <div>
                                <s:action name="getEngineerReport" namespace="/prv/p" executeResult="true">
                                    <s:param name="claimId"><s:property value="id" /></s:param>
                                    <s:param name="claimStatus"><s:property value="status" /></s:param>
                                </s:action>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </s:if>
    </div>

    <div id="attachmentTab" class="x-hide-display"></div>

    <div id="historyTab" class="x-hide-display"></div>

    <div id="auditTrailTab" class="x-hide-display"></div>

    <div id="commentTab" class="x-hide-display"></div>

</div>
