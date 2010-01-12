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


    // PAYMENT PACK
    var paymentPackJsonReader;
    var paymentPackDataStore;
    var paymentPackGrid;

    var auditTrailJsonReader;
    var auditTrailDataStore;
    var auditTrailGrid;

    var popupTimeUp = 900000;

   

    $(function(){
        $('fieldset.partial legend').next().hide();
        var fsets =  $('fieldset:not(.partial) legend');
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});

        activityMonitor.pingServer(<s:property value="id"/>);
    });

    function doCleanResult(){
        $(".chox-form-submit-result").html("");
    }

    Ext.BLANK_IMAGE_URL = '<%= request.getContextPath()%>/images/default/s.gif';

    Ext.onReady(function(){

        var tabs = new Ext.TabPanel({
            renderTo: 'tabContainer',
            width:960,
            activeTab: 0,
            frame:false,
            plain:true,
            defaults:{autoHeight: true},
            items:[
                {
                    contentEl:'claimDetails',
                    title: 'Claim Details',
                    disabled: claimDetailsDisabled,
                    listeners: {activate : doCleanResult}
                },
                {
                    contentEl:'hireMonitoringDetails',
                    title: 'Hire Monitoring',
                    disabled: hireMonitoringDetailsDisabled,
                    listeners: {activate : doCleanResult}
                },
                {
                    contentEl:'invoiceDetails',
                    title: 'Invoice Details',
                    disabled: invoiceDetailsDisabled,
                    listeners: {activate : doCleanResult}
                },
                {
                    contentEl:'paymentPack',
                    title: 'Attachments',
                    disabled: paymentPackDisabled,
                    listeners: {activate : doCleanResult}
                },
                {
                    contentEl:'historyDetails',
                    title: 'History', disabled: historyDetailsDisabled,
                    listeners: {activate : doCleanResult}
                },
                {
                    contentEl:'auditTrailDetails',
                    title: 'Claim Cycle', disabled: auditTrailDisabled,
                    listeners: {activate : doCleanResult}
                },
                {
                    contentEl:'commentTab',
                    title: 'Notes',
                    disabled: commentsDisabled,
                    listeners: {activate : doCleanTab},
                    autoLoad: {url:"p/getClaimDetailCommentPage.action?claimId="+<s:property value="id" />, scripts:true}
                }
            ]
        });

        function doCleanTab(){
            var tabIndex = 0;
            if(tabs){
                tabIndex = tabs.items.indexOf(tabs.getActiveTab());
            }
        }
        
        /***********************************************************************************
         * ATTACHMENT / PAYMENT PACK
         ***********************************************************************************/

        if(!paymentPackDisabled){

            paymentPackJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'id',  hidden:true},
                    {name:'fileName'},
                    {name:'category'},
                    {name:'remarks' },
                    {name:'modifiedDate' },
                    {name:'modifiedBy' },
                    {name:'delete' }
                ]
            });

            paymentPackDataStore = new Ext.data.Store({
                proxy: new Ext.data.HttpProxy
                ({url: '<%= request.getContextPath()%>/prv/p/getAttachments.action',method:'GET'}),
                reader:paymentPackJsonReader
            });

            paymentPackGrid = new Ext.grid.GridPanel({
                listeners:  {cellclick:loadAttachment },
                store: paymentPackDataStore,
                loadMask: true,
                columns: [
                    {header: "File Name", width: 250, dataIndex: 'fileName', sortable: true, resizable: true},
                    {header: "Category", width: 150, dataIndex: 'category', sortable: true, resizable: true},
                    {header: "Description", width: 300, dataIndex: 'remarks', sortable: true, resizable: true},
                    {header: "Created Date", width: 150, dataIndex: 'modifiedDate', sortable: true, resizable: true},
                    {header: "", width: 60, dataIndex: 'delete', sortable: false, hidden:(paymentPackTabAccessibility!=2), resizable: false, renderer:function(value,p,r){
                            return "<a href='#attachmentlisting'>" + value + "</a>"}}
                ],
                renderTo:'paymentPackGrid',
                width:960,
                autoHeight:true,
                enableHdMenu:false
            });
            loadAttachments();
        }

        function loadAttachment(grid, rowIndex, columnIndex, e){

            var attachment = paymentPackGrid.getStore().getAt(rowIndex);
            var fileId = attachment.get("id");
                    
            if(columnIndex!=4){
                var link = "<%= request.getContextPath()%>/prv/p/doExportFile.action?fileId=" + fileId;
                window.open(link,"","width=600,height=400,status=yes,menubar=no");
            }else{
                deleteAttachment(fileId);
            }
        }

        function deleteAttachment(a){

            var deleteAtt = confirm("Are you sure you want to delete this attachment?");

            if(deleteAtt){
                paymentPackLoaded = false;
                var url = "<%= request.getContextPath()%>/prv/p/doDeleteFile.action";
                var param = {"fileId":a};
                ajax.loadJson(url,param,loadAttachments);
            }

            doCleanResult();
        }

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
                    {name:'supportingNote' }
                ]
            });

            ecdDataStore = new Ext.data.Store({
                proxy: new Ext.data.HttpProxy
                ({url: '<%= request.getContextPath()%>/prv/p/getHireMonitoringEcds.action',method:'GET'}),
                reader:ecdJsonReader
            });

            ecdGrid = new Ext.grid.GridPanel({
                listeners:  {cellclick:loadHireMonitor },
                store: ecdDataStore,
                loadMask: true,
                columns: [
                    {header: "", width: 20, dataIndex: 'sequence', sortable: false, resizable: true},
                    {header: "ECD Date", width: 70, dataIndex: 'ecdDate', sortable: false, resizable: true},
                    {header: "Reason", width: 80, dataIndex: 'reason', sortable: false, resizable: true},
                    {header: "Supporting Note", width: 280, dataIndex: 'supportingNote', sortable: false, resizable: true}
                ],
                renderTo:'ecdGridHolder',
                width:445,
                autoHeight:true,
                enableHdMenu:false
            });

            loadEcds();
        }
    
        function loadHireMonitor(grid, rowIndex, columnIndex, e){
            var hiremonitoringECD = ecdGrid.getStore().getAt(rowIndex);
            var supportingNoteText = "<br/><b>Supporting note</b>: <br/>"+hiremonitoringECD.get("supportingNote");
            var EcdText = "<b>ECD Date</b>: "+hiremonitoringECD.get("ecdDate");
            var ReasonText = "<b>Reason</b>: "+hiremonitoringECD.get("reason");

            var title = EcdText;
            var msg = EcdText + "<br/>" + ReasonText + "<br/>" + supportingNoteText;
            propmtMsg(title, msg);
        }


        /***********************************************************************************
         * AUDIT TRAIL
         ***********************************************************************************/

        if(!auditTrailDisabled){

            auditTrailJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'modifiedDate'},
                    {name:'modifiedBy'},
                    {name:'status'}
                ]
            });

            var auditTrailData = new Ext.data.Store({
                proxy: new Ext.data.HttpProxy
                ({url: '<%= request.getContextPath()%>/prv/p/getAuditTrail.action',method:'GET'}),
                reader:auditTrailJsonReader
            });

            var auditGrid = new Ext.grid.GridPanel({
                listeners:  {cellclick:loadAudit},
                store: auditTrailData,
                columns: [
                    {header: "Modified Date", width: 130, dataIndex: 'modifiedDate', sortable: false, resizable: true},
                    {header: "Modified By", width: 260, dataIndex: 'modifiedBy', sortable: false, resizable: true},
                    {header: "Status", width: 500, dataIndex: 'status', sortable: false, resizable: true}
                ],
                renderTo:'auditTrailGrid',
                width:960,
                autoHeight:true,
                enableHdMenu:false
            });

            auditTrailData.load(
            {
                params:
                    {
                    claimId : <s:property value="id" />
                }
            });
        }

        function loadAudit(grid, rowIndex, columnIndex, e){
            var audit = auditGrid.getStore().getAt(rowIndex);

            var title = "Claim Cycle";
            var msg = "<b>Modified Date</b>: " + audit.get("modifiedDate")
                + "<br/><b>Modified By</b>: " + audit.get("modifiedBy")
                + "<br/><br/><b>Status</b>: " + audit.get("status")

            propmtMsg(title, msg);
        }

        /***********************************************************************************
         * HISTORY
         ***********************************************************************************/

        var historyData;
        var historyJsonReader;
        var historyGrid;

        if(!historyDetailsDisabled){

            historyJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'createdBy'},
                    {name:'createdDate'},
                    {name:'narrative'}
                ]
            });

            historyData = new Ext.data.Store({
                proxy: new Ext.data.HttpProxy
                ({url: '<%= request.getContextPath()%>/prv/p/getHistories.action',method:'GET'}),
                reader:historyJsonReader
            });

            historyGrid = new Ext.grid.GridPanel({
                listeners:  {cellclick:loadHistory },
                store: historyData,
                columns: [
                    {header: "Created On", width: 110, dataIndex: 'createdDate', sortable: false, resizable: true},
                    {header: "Created By", width: 110, dataIndex: 'createdBy', sortable: false, resizable: true},
                    {header: "Message Text", width: 650, dataIndex: 'narrative', sortable: false, resizable: true}
                ],
                renderTo:'historyGrid',
                width:960,
                autoHeight:true,
                enableHdMenu:false
            });

            historyData.load(
            {
                params:
                    {
                    id : <s:property value="id" />
                }
            });
        }

        function loadHistory(grid, rowIndex, columnIndex, e){
            var historyItem = historyGrid.getStore().getAt(rowIndex);

            var title = "History";
            var msg = "<b>Created Date</b>: " + historyItem.get("createdDate")
                + "<br/><b>Created By</b>: " + historyItem.get("createdBy")
                + "<br/><br/><b>Message</b>: <br/>" + historyItem.get("narrative")

            propmtMsg(title, msg);
        }

    });


    // LOAD PAYMENT PACK / ATTACHMENT
    var paymentPackLoaded = false;
            
    function loadAttachments(){

        if(!paymentPackDisabled && !paymentPackLoaded){
                    
            paymentPackDataStore.load(
            {
                params:
                    {
                    id : <s:property value="id" />
                }
            });

            paymentPackLoaded = true;
            resetAttachmentForm();
        }
    }

    function resetAttachmentForm(){
                
        if(paymentPackTabAccessibility>=2){
            $("#fAttachment").each(function(){
                this.reset();
            });
        }

    }
            
    var t;

    function updateAnomalies(a){
        document.location = "<%= request.getContextPath()%>/prv/doUpdateAnomalies.action?id="+a;
    }

    function closeClaimStatus(){

        if(!confirm('Are you sure you want to close this claim?')){
            return false;
        }else{
            document.location = '<%= request.getContextPath()%>/prv/doUpdateClaimStatus.action?id=<s:property value="id" />';
        }

        return true;
    }

    function reopenClaimStatus(){

        if(!confirm('Are you sure you want to re-open this claim?')){
            return false;
        }else{
            document.location = '<%= request.getContextPath()%>/prv/doReopenClaimStatus.action?id=<s:property value="id" />';
        }
        return true;
    }

    $(document).ready(function() {
        $("#popGeneralTemplateClose").click(function(){ $.unblockUI();});
    });

    function removeNotification(notificationId)
    {
        var url = "<%= request.getContextPath()%>/prv/p/removeNotification.action";
        var param = {"notificationId" : notificationId,"id": <s:property value="id" />};

        ajax.loadHtml(url,param,function(data){
            $("div#notificationNotesDiv").html(data);
        });
    }

    var ecdsLoaded = false;
    function loadEcds(){

        if(!hireMonitoringDetailsDisabled){

            if(!ecdsLoaded)
            {
                ecdDataStore.load(
                {
                    params:
                        {
                        id : <s:property value="id" />
                    }
                });
            }
        }
    }

    function doMoreActionOnchange(){
        actionPanel.handleExtraActionChange();
    }
</script>

<div style="width:960px">

    <div class="chox-claim-header x-panel-bwrap chox-form-container">

        <fieldset class="x-fieldset loaded open-by-default">
            <legend>Claim Summary</legend>
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td><label class="chox-claim-header-label">Third Party Insurer</label><label class="chox-claim-header-text"><s:property value="thirdParty.insurer.name" /></label></td>
                    <td><label class="chox-claim-header-label">Credit-hire Organsation</label><label class="chox-claim-header-text"><s:property value="chorganisation.name" /></label></td>
                    <td><label class="chox-claim-header-label">Created By</label><label class="chox-claim-header-text"><s:property value="createdByDesc" /></label></td>
                </tr>
                <tr>
                    <td><label class="chox-claim-header-label">Supplier Reference</label><label class="chox-claim-header-text"><s:property value="choReference" /></label></td>
                    <td><label class="chox-claim-header-label">Insurer Claim Number</label><label class="chox-claim-header-text"><s:property value="claimNumber" /></label></td>
                    <td><label class="chox-claim-header-label">Created On</label><label class="chox-claim-header-text"><s:date name="createdDate" format="dd MMM yyyy kk:mm"  /></label></td>
                </tr>
                <tr>
                    <td><label class="chox-claim-header-label">Customer</label><label class="chox-claim-header-text"><span id="status"><s:property value="customer.formattedName" /></span></label></td>
                    <td><label class="chox-claim-header-label">Current Status</label><label class="chox-claim-header-text"><span id="status"><s:property value="status" /></span></label><!--span id="statusTip"><img src="img/tip.gif" style="fixed:relative;top:-50" /></span--></td>
                    <td><label class="chox-claim-header-label">Policy Holder Contact Date</label><label class="chox-claim-header-text"><span id="status"><s:date name="policyHolderContactDate" format="dd MMM yyyy kk:mm"  /></span></label></td>
                </tr>
                <tr>
                    <td><label class="chox-claim-header-label">Percentage Liability Accepted</label><label class="chox-claim-header-text"><span id="status"><s:property value="percentageLiabilityAccepted" />%</span></label></td>
                    <td><label class="chox-claim-header-label">Indemnity</label><label class="chox-claim-header-text"><span id="status">£<s:property value="indemnityAmount" /></span></label></td>
                    <td><label class="chox-claim-header-label">Workgroup</label><label class="chox-claim-header-text"><s:property value="workgroup.name" /></label></td>
                </tr>
                <tr>
                    <td><label class="chox-claim-header-label">Claim Owner</label><label class="chox-claim-header-text"><span id="status"><s:property value="claimOwner.displayName" /></span></label></td>
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
                <a href="<s:url action="inbox"/>">« Back to Search Results</a>
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
                        headerValue="More Actions"
                        emptyOption="false"
                        onchange="doMoreActionOnchange();">
                    </s:select>
                </s:if>
            </div>
        </div>

    </div>
</div>

<s:if test="!IsCHO">

    <div id="updateInsurerClaimNumber" class="extraActionClass" style="display: none;">

        <table width="100%">
            <tr><td>
                    <div class="chox-claim-header x-panel-bwrap chox-form-container">
                        <s:action name="getUpdateInsurerClaimNumber" namespace="/prv/p" executeResult="true"></s:action>
                        <div class="action-message"><s:property value="actionResult" /></div>
                    </div>
                </td></tr>
        </table>

    </div>

    <div id="updateClaimOwner" class="extraActionClass" style="display: none;">

        <table width="100%">
            <tr><td>
                    <div class="chox-claim-header x-panel-bwrap chox-form-container">
                        <s:action name="getUpdateClaimOwnership" namespace="/prv/p" executeResult="true"></s:action>
                        <div class="action-message"><s:property value="actionResult" /></div>
                    </div>
                </td></tr>
        </table>

    </div>

    <div id="escalateUnassignedClaim" class="extraActionClass" style="display: none;">
        <table width="100%">
            <tr><td>
                    <div class="chox-claim-header x-panel-bwrap chox-form-container">
                        <s:action name="getEscalateUnassignedClaim" namespace="/prv/p" executeResult="true"></s:action>
                        <div class="action-message"><s:property value="actionResult" /></div>
                    </div>
                </td></tr>
        </table>

    </div>

</s:if>

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

<div class="chox-claim-header x-panel-bwrap chox-form-container" id="generalActionPanel" style="display: none;">
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

    <!-- ************************ CLAIM DETAIL  ************************ !-->
    <div id="claimDetails">

        <s:if test="tabAccessibility.claimDetailTabAccessibility != 0">

            <div class="x-panel-bwrap chox-form-container">
                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                    <tr valign="top">
                        <td class="chox-form-left-col">

                            <div>
                                <s:action name="getCustomer" namespace="/prv/p" executeResult="true">
                                    <s:param name="claimId"><s:property value="id" /></s:param>
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

                            <fieldset class="x-fieldset">
                                <legend>Claim Details</legend>
                                <div style="display:none" class="form-container">
                                    <div class="chox-form-item">
                                        <label class="std-label-ro">Managing Repair</label>
                                        <label class="std-data-ro"><s:property value="IsManagingRepairDesc" /></label>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="std-label-ro">GTA 4.1 Notice Date</label>
                                        <label class="std-data-ro"><s:date name="gtaNoticeDate" format="dd MMM yyyy kk:mm"  /></label>
                                    </div>
                                    <div class="chox-form-item">
                                        <label class="std-label-ro">Credit Agreement Signed by Insurer Date</label>
                                        <label class="std-data-ro"><s:date name="creditAgreementDate" format="dd MMM yyyy kk:mm"  /></label>
                                    </div>
                                </div>
                            </fieldset>

                            <s:if test="isCHO">
                                <fieldset class="x-fieldset">
                                    <legend>Claim Reviews</legend>
                                    <div style="display:none" class="form-container">
                                        <div class="chox-form-item">
                                            <label class="std-label-ro">Quantum</label>
                                            <label class="std-data-ro"><s:property value="isQuantumDisputeDesc"/></label>
                                        </div>
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

    <!-- ************************ HIRE MONITORING  ********************* !-->
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

            <!-- template for modal Hire Monitoring-->
            <div style="display:none" id="hireMonitorTemplate">
                <input type="button" value="Close" id="hireMonitorModalClose"><br/>
                <div id="hireMonitorMessage"></div>
            </div>

        </s:if>

    </div>

    <!-- ************************ INVOICE  ***************************** !-->
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

    <!-- ************************ PAYMENT PACK / ATTACHMENT  *********** !-->
    <div id="paymentPack" class="x-hide-display">

        <s:if test="tabAccessibility.paymentPackTabAccessibility != 0">

            <script type="text/javascript">
                            
                var sucessColor = "#15428b";
                var warningColor = "red";

                $(document).ready(function() {
                                    
                    var options = {
                        success: showResponseAtt
                    };
                                    
                    $('#fAttachment').ajaxForm(options);
                                    
                });
                            
                function showResponseAtt(responseText, statusText){

                    var msg = "";
                    var cssColor = sucessColor;
                    var response = eval('(' + responseText.trim() + ')');

                    if(response){
                        if(response.isValid){
                            msg = response.result;
                            paymentPackLoaded = false;
                            paymentPackDisabled = false;
                            loadAttachments();
                        }
                        else{
                            cssColor = warningColor;
                            msg = formErrorMessage(response.errors);
                        }
                    }
                    else{
                        cssColor = warningColor;
                        msg = "Unknown Error Encountered, please try again.";
                    }

                    $("#AttMsgBox").css("color",cssColor);
                    $("#AttMsgBox").html(msg);
                    paymentPackLoaded = false;
                                    
                }
                                
                function fileValidation(){

                    var bFlag = true;
                    var uploadFile = document.Attform.attachmentFile.value;

                    if(uploadFile==""){
                        showPaymentProcessMsg("Please select file to upload");
                        return false;
                    }

                    var remark = document.Attform.remark.value;
                    if(remark.length<=0){
                        showPaymentProcessMsg("Description cannot be empty");
                        return false;
                    }

                    if(bFlag){
                        $("#AttMsgBox").css("color",sucessColor);
                        if((uploadFile.lastIndexOf("."))>0){
                            var filename = uploadFile.substr(uploadFile.lastIndexOf('\\')+1, uploadFile.length);
                            document.Attform.uploadFileName.value = filename;
                        }
                    }

                    return bFlag;
                }

                function showPaymentProcessMsg(errorMsg){
                    $("#AttMsgBox").css("color", warningColor);
                    $("#AttMsgBox").text(errorMsg);
                }

                Ext.onReady(function(){

                    var attachmentHtmlDesc = "";

                    attachmentHtmlDesc = "<table cellpadding='0' cellspacing='0' border='0' class='remark-table'>";
                    attachmentHtmlDesc += "<tr><th width='28%'><b>Type</b></th><th width='70%'><b>Description</b></th></tr>";

                <s:iterator value="AllowFileTypes">
                        attachmentHtmlDesc +=     '<tr>';
                        attachmentHtmlDesc += '<td>.<s:property value="code"/>    </td>';
                        attachmentHtmlDesc += '<td><s:property value="description"/></td>';
                        attachmentHtmlDesc += '</tr>';
                </s:iterator>

                        attachmentHtmlDesc += "</table>";

                        new Ext.ToolTip({
                            target: 'attachmentTypeSpan',
                            html: attachmentHtmlDesc,
                            title: 'Attachment Formats',
                            autoHide: false,
                            closable: true,
                            draggable:true
                        });

                        Ext.QuickTips.init();

                    });
                                
            </script>

            <div class="attachments  x-panel-bwrap chox-form-container">

                <form id="fAttachment" action="<%= request.getContextPath()%>/prv/p/createNewAttachment.action" method="POST" enctype="multipart/form-data" name="Attform">
                    <input type="hidden" name="claimId" value='<s:property value="id" />'>
                    <input type="hidden" name="uploadFileName">

                    <fieldset class="x-fieldset">

                        <legend>Add a new Attachment&nbsp;</legend>

                        <table class="chox-form-item" cellpadding="0" cellspacing="0" border="0" width="100%">
                            <tr>
                                <td width="200" align="right">
                                    <label class="std-label-ro">File&nbsp;&nbsp;</label>
                                </td>
                                <td>
                                    <s:file id="fileUploader" name ="attachmentFile" label ="Attachment" cssStyle="height: 20px;" size="40"/>
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td>
                                    <div class="column_remark" style="padding:10px 0 10px 0;">
                                        Maximum attachment size is <s:property value="maxFileSize/1000/1024"/> MB. <br/>
                                        Currently, CHOX supports attachments in the following formats: <br/>
                                        <s:property value="AllowFileType"/>&nbsp;&nbsp;<img src="../images/help.png" id="attachmentTypeSpan" alt=""/>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td align="right"><label class="std-label-ro">Attachment Type&nbsp;&nbsp;</label></td>
                                <td>
                                    <s:select name="category" id="category"
                                              list="attachmentCategory"
                                              headerKey=""
                                              listKey="value"
                                              listValue="text"
                                              emptyOption="false"></s:select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right" valign="top"><label class="std-label-ro">Description&nbsp;&nbsp;</label></td>
                                <td>
                                    <s:textarea rows="3" cols="30" id="remark" name="remark" label="Remark:"/>
                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td>
                                    <input type="submit" id="bAddAttachment" value="Add File" onclick="return fileValidation()"/>
                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td align="left" valign="top"><div class="chox-form-submit-result" id="AttMsgBox" style="text-align: left;"></div></td>
                            </tr>
                        </table>

                    </fieldset>
                </form>
            </div>

            <a name="attachmentlisting"></a>
            <div id="paymentPackGrid"></div>

        </s:if>
    </div>

    <!-- ************************ HISTORY  ***************************** !-->
    <div id="historyDetails" class="x-hide-display">
        <s:if test="tabAccessibility.historyTabAccessibility != 0">
            <div id="historyGrid"></div>
        </s:if>
    </div>

    <!-- ************************ AUDIT TRAIL  ************************* !-->
    <div id="auditTrailDetails" class="x-hide-display">
        <s:if test="tabAccessibility.auditTrailTabAccessibility != 0">
            <div id="auditTrailGrid"></div>
        </s:if>
    </div>

    <div id="commentTab" class="x-hide-display"></div>
</div>