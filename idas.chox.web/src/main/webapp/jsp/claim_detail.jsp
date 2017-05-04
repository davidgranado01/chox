<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
    var reportName = 'ClaimFileReport-Excel';
    var tabPanel1;
    var selectedTab=0;
    var notesTabLoaded = false;
    var taskTabLoaded = false;
    var claimDetailTabAccessibility = <s:property value="tabAccessibility.claimDetailTabAccessibility" />;
    var invoiceDetailTabAccessibility = <s:property value="tabAccessibility.invoiceDetailTabAccessibility" />;
    var hireMonitoringTabAccessibility = <s:property value="tabAccessibility.hireMonitoringTabAccessibility" />;
    var historyTabAccessibility = <s:property value="tabAccessibility.historyTabAccessibility" />;
    var notesTabAccessibility = <s:property value="tabAccessibility.notesTabAccessibility" />;
    var tasksTabAccessibility = <s:property value="tabAccessibility.tasksTabAccessibility" />;
    var paymentPackTabAccessibility = <s:property value="tabAccessibility.paymentPackTabAccessibility" />;
    var auditTrailTabAccessibility = <s:property value="tabAccessibility.auditTrailTabAccessibility" />;
    var claimDetailsDisabled = claimDetailTabAccessibility === 0;
    var hireMonitoringDetailsDisabled = hireMonitoringTabAccessibility  === 0;
    var invoiceDetailsDisabled = invoiceDetailTabAccessibility === 0;
    var paymentPackDisabled = paymentPackTabAccessibility === 0;
    var historyDetailsDisabled = historyTabAccessibility === 0;
    var commentsDisabled = notesTabAccessibility === 0;
    var tasksDisabled = ((!<s:property value="taskManagementEnabled" />) || tasksTabAccessibility === 0);
    var auditTrailDisabled = auditTrailTabAccessibility === 0;
    var availableSlaExtensionDays = <s:property value="availableSlaExtensionDays" />;
    var appliedSlaExtDays = <s:property value="slaExtDays" />;
    var ecdDataStore;
    var ecdGrid;
    var isChoxAdmin = <s:property value="isChoxAdmin" />;
    var taskTabTitle = 'Tasks';
    var commentTabTitle = 'Notes';
    var reviewDateSelectionDlg;
    var reviewDateSelectionWin;

    Ext.BLANK_IMAGE_URL = '<%= request.getContextPath()%>/images/default/s.gif';

    Ext.onReady(function(){        
        $('fieldset.partial legend').next().hide();
        var fsets =  $('fieldset:not(.partial) legend');
        fsets.click(function(){ $(this).next().toggle();});
        fsets.mouseover(function(){ $(this).css("cursor","pointer"); });
        fsets.mouseout(function(){ $(this).css("cursor","normal");});
        var claimId = <s:property value="id" />;
        
        <s:if test="isChoxAdmin!=true && enableActivityMonitor">
            var pingServerUrl = '/p/activityMonitoringAction.action';
            var checkStatusIUrl = '/p/checkViewingStatus.action';
            activityMonitor.setup(pingServerUrl, checkStatusIUrl, <s:property value="activityMonitorRequestInterval"/>);
            activityMonitor.pingServer();
        </s:if>
        
        // mappedInsurersStore and switchClaimToMulInsForm are declared in claim_detail.js file.
        <s:if test="isInsurer != true">
            var mappedInsurersJsonString = '<s:property value="insurersJsonString" escapeHtml="false"/>';
            if (mappedInsurersJsonString !== '') {
                mappedInsurersStore.loadData(Ext.util.JSON.decode(mappedInsurersJsonString));
            }
            switchClaimToMulInsForm.getForm().setValues([{id : 'policyNumberId', value : '<s:property value="policyNumber" />'}]);
        </s:if>
        var closeClaimReasonsJsonString = '<s:property value="closeClaimReasonsJsonString" escapeHtml="false"/>';
        if (closeClaimReasonsJsonString !== '') {
            closeClaimReasonsStore.loadData(Ext.util.JSON.decode(closeClaimReasonsJsonString));
        }


        tabPanel1= new Ext.TabPanel({
            renderTo: 'tabContainer',
            width:1000,
            activeTab: selectedTab,
            frame:false,
            plain:true,
            defaults:{autoHeight: true},
            items:[
                {contentEl:'claimDetails', title: 'Claim Details', disabled: claimDetailsDisabled,listeners: {activate: clearActionResult}},
                {contentEl:'hireMonitoringDetails', title: 'Hire Monitoring', disabled: hireMonitoringDetailsDisabled,listeners: {activate: clearActionResult}},
<s:if test="insurerLouDates">
                {contentEl:'insurerHireMonitoringDetails', title: 'Insurer Hire Monitoring', listeners: {activate: clearActionResult}},
</s:if>
                {contentEl:'invoiceDetails', title: 'Invoice Details', disabled: invoiceDetailsDisabled},
                {contentEl:'attachmentTab', title: 'Attachments', disabled: paymentPackDisabled, autoLoad: choxUpdateEl({url:'/prv/p/getAttachmentPage.action', params:{"claimId" : '<s:property value="id" />'}})},
                {contentEl:'historyTab', title: 'BRE Results', disabled: historyDetailsDisabled, autoLoad: choxUpdateEl({url:'/prv/p/getHistoryPage.action', params:{"claimId" : '<s:property value="id" />'}})},
                {contentEl:'auditTrailTab', title: 'Claim Cycle', disabled: auditTrailDisabled, autoLoad: choxUpdateEl({url:'/prv/p/getAuditTrailPage.action', params:{"claimId" : '<s:property value="id" />'}})},
                {contentEl:'commentTab', id:'claimCommentPanelTabId', title: commentTabTitle, disabled: commentsDisabled, autoLoad: choxUpdateEl({url:'/prv/p/getClaimDetailCommentPage.action', params:{"claimId" : '<s:property value="id" />'}}),listeners: {activate: doLoadComments}},
                {contentEl:'taskTab', id:'claimTaskPanelTabId', title: taskTabTitle, disabled: tasksDisabled, autoLoad: choxUpdateEl({url:'/prv/p/getClaimDetailTaskPage.action', params:{"claimId" : '<s:property value="id" />'}}),listeners: {activate: doLoadTasks}}
            ],
            listeners: { 
                beforerender : updateTabs
            }
        });

        /***********************************************************************************
         * HIRE MONITORING
         ***********************************************************************************/
        if(!hireMonitoringDetailsDisabled){

            var ecdJsonReader = new Ext.data.JsonReader({ 
                totalProperty: 'totalCount',
                root: 'results',
                fields:
                    [
                    {name:'sequence'},
                    {name:'ecdDate'},
                    {name:'createdDate'},
                    {name:'reason'},
                    {name:'supportingNote'}]
            });

            ecdDataStore = new choxDataStore({
                url: '/prv/p/getHireMonitoringEcds.action',
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
                    {header: "ECD Date", width: 70, dataIndex: 'ecdDate', sortable: false, resizable: true},
                    {header: "Created", width: 70, dataIndex: 'createdDate', sortable: false, resizable: true},
                    {header: "Reason", width: 90, dataIndex: 'reason', sortable: false, resizable: true},
                    {header: "Supporting Note", width: 235, dataIndex: 'supportingNote', sortable: false, resizable: true}
                ],
                width:475,
                autoHeight:true
            });

            loadEcds();
        }
        
        <s:if test="showMessage">
            Ext.Msg.alert('Status', '<c:out value='${statusMsg}' />');
        </s:if>
        <s:elseif test="showErrorMessage">
            Ext.Msg.alert('Error', '<c:out value='${statusMsg}' />');
        </s:elseif>
            
<s:if test="isInsurer">
        
        var reviewItems = [{
                xtype : 'hidden',
                name : 'name',
                value : 'lastReviewDate'
            },{
                xtype : 'label',
                style: 'font:bold 12px tahoma',
<s:if test="hasLastReviewDate">
                html: 'Please enter a \'Last Review Date\' together with any relevant notes.<br>You may leave the this field empty to remove the current date set:'
</s:if>
<s:else>
                text: 'Please enter a \'Last Review Date\' together with any relevant notes:'
</s:else>
            },{
                xtype : 'label',
                style: 'font:bold 12px tahoma',
                html: '<br>&nbsp;<br>'
            },{
                xtype : 'datefield',
                name : 'lastReviewDate',
                format : 'd/m/Y',
                maxValue: new Date(),
                maxText: 'The \'Last Review Date\' cannot be in the future',
                showWeekNumber: true,
<s:if test="hasLastReviewDate">
                labelStyle: 'align:right; width: 110;',
                fieldLabel : 'Last Review Date',
                allowBlank: true
</s:if>
<s:else>
                labelStyle: 'align:right; width: 120;',
                fieldLabel : 'Last Review Date <span class="mandatory">*</span>',
                allowBlank: false
</s:else>
            },{
                xtype : 'textarea',
                width : 250,
                name : 'lastReviewNote',
                fieldLabel : 'Last Review Notes',
<s:if test="hasLastReviewDate">
                labelStyle: 'align:right; width: 110;',
</s:if>
<s:else>
                labelStyle: 'align:right; width: 120;',
</s:else>
                allowBlank: true
            }];
        
        reviewDateSelectionDlg =  new choxExtJsFormPanel({
            autoHeight: true,
            autoWidth: true,
            frame:true,
            title: 'Last Review Date',
            buttonAlign : 'center',
            items : reviewItems,
            buttons: [{
                text:'Ok',
                handler:function(){
                    if(reviewDateSelectionDlg.getForm().isValid()){
                        reviewDateSelectionWin.hide();
                        var url = "<%= request.getContextPath()%>/prv/processClaim.action";
                        var form = reviewDateSelectionDlg.getForm();
                        var form = $('<form action="' + url + '" method="post">' +
                            '<s:hidden name="name" value="lastReviewDate" />' +
                            '<s:hidden name="lastReviewDate" value="' + reviewDateSelectionDlg.getForm().getValues()['lastReviewDate'] + '" />' +
                            '<s:hidden name="lastReviewNote" value="' + reviewDateSelectionDlg.getForm().getValues()['lastReviewNote'] + '" />' +
                            '</form>');
                        $('body').append(form);
                        Ext.get('claimDetailScreenDiv').mask("Reloading claim...");
                        choxJqueryHttpSubmit($(form));


                    }
                }
                },{
                text: 'Close',
                handler: function(){
                    reviewDateSelectionWin.hide();
                }
            }]
          });

        if (getIEVersion() == 9) {
            reviewDateSelectionWin = new Ext.Window({
                layout:'fit',
                height: 210,
<s:if test="hasLastReviewDate">
                width: 480,
</s:if>
<s:else>
                width: 460,
</s:else>
                closable:false,
                resizable : false,
                modal: true,
                items : [
                    reviewDateSelectionDlg
                ]
            });
        }else{
            reviewDateSelectionWin = new Ext.Window({
                layout:'fit',
                autoHeight: true,
                autoWidth: true,
                closable:false,
                resizable : false,
                modal: true,
                items : [
                    reviewDateSelectionDlg
                ]
            });
        }


</s:if>
    expandClaimDetails(false);
    }); // End of Ext.onReady()

    function updateTabs() {
        if (isChoxAdmin) {
            return;
        }

        choxExtAjaxRequest({
            url: '/prv/p/getClaimVisibleTasks.action',
            success : function(response, opts) {
                        var resp = Ext.decode(response.responseText);
                        if (resp && tabPanel1) {
                           updateClaimTaskTabCount(resp.totalCount);
                        }
            },
            params: {
                hideCompleted : true,
                showAssignedTasksOnly : true,
                claimId : '<s:property value="id" />'
            }
        });

        choxExtAjaxRequest({
            url: '/prv/p/getClaimNotesRequireReview.action',
            success : function(response, opts) {
                        var resp = Ext.decode(response.responseText);
                        if (resp && tabPanel1) {
                           updateClaimNoteTabCount(resp.totalCount);
                        }
            },
            params: {
                claimId : '<s:property value="id" />'
            }
        });
    }

    function updateClaimTaskTabCount(taskCount) {
        var title = 'Tasks';
        if (taskCount > 0) { 
            title = title + '&nbsp<sup class="notes_bubble" style="background-color:red;">'+taskCount +'</sup>';
        }
        tabPanel1.getComponent('claimTaskPanelTabId').setTitle(title);
    }

    function updateClaimNoteTabCount(noteCount) {
        var title = 'Notes';
        if (noteCount > 0) { 
            title = title + '&nbsp<sup class="notes_bubble" style="background-color:red;">'+noteCount +'</sup>';
        }
        tabPanel1.getComponent('claimCommentPanelTabId').setTitle(title);
    }


    function doLoadComments(){
        if(notesTabLoaded){
            refereshComments();
        }
    }

    function doLoadTasks(){
        if(taskTabLoaded){
            loadClaimTasks();
        }
    }

    function clearActionResult(tab){

        if(document.getElementById("HMmessageBox")){
            document.getElementById("HMmessageBox").innerHTML = '';
        }
        if(document.getElementById("HVDmessageBox")){
            document.getElementById("HVDmessageBox").innerHTML = '';
        }
    
        if(document.getElementById("customerVehicleDamageMsgBox")){
            document.getElementById("CDmessageBox").innerHTML = '';
            document.getElementById("CDmessageBox1").innerHTML = '';
            document.getElementById("customerVehicleDamageMsgBox").innerHTML = '';
            document.getElementById("incidentMsgBox").innerHTML = '';
            document.getElementById("claimDetailsMsgBox").innerHTML = '';
            document.getElementById("thirdPartyMsgBox").innerHTML = '';
            document.getElementById("injuryMsgBox").innerHTML = '';
            document.getElementById("solicitorMsgBox").innerHTML = '';
            document.getElementById("witnessMsgBox").innerHTML = '';
        }
    
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
     * GENERATE CLAIM REPORT FILE
     ***********************************************************************************/
    function claimReport(){
        var queryString = {'claimId' : <s:property value="id" />};
        generateReport(queryString); 
    }

    /***********************************************************************************
     * Revert claim to previous status
     ***********************************************************************************/
    function revertClaimStatus(){
        
        var warningMessage = 'Are you sure you want to revert the status of this claim?';
        var showDeleteWarning = '<s:property value="invoiceDeleteWarning" />';
        if(showDeleteWarning === 'true'){
            warningMessage = 'This Claim has an Invoice. If you revert the status of this Claim, the Invoice will be deleted. Are you sure you want to continue?';
        }
        Ext.MessageBox.confirm('Confirm', warningMessage,function(btn){
            if(btn==='yes'){
                Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
                var url = "<%= request.getContextPath()%>/prv/processClaim.action";
                var form = $('<form action="' + url + '" method="post">' +
                    '<s:hidden name="name" value="revertClaim" />' +
                    '</form>');
                $('body').append(form);
                choxJqueryHttpSubmit($(form));
            }
        });
    }

    function reopenClaimStatus(){
        Ext.MessageBox.confirm('Confirm', 'Are you sure you want to re-open this claim?',function(btn){
            if(btn==='yes'){
                Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
                var url = "<%= request.getContextPath()%>/prv/processClaim.action";
                var form = $('<form action="' + url + '" method="post">' +
                    '<s:hidden name="name" value="reopenClaim" />' +
                    '</form>');
                $('body').append(form);
                choxJqueryHttpSubmit($(form));
            }
        });
    }

    function pageRefresh(){
        loadClaimDetail(<s:property value="id" />);
    }


    /***********************************************************************************
     * REMOVE NOTIFICATION
     ***********************************************************************************/

    function removeNotification(notificationId)
    {
        var url = "/prv/p/removeNotification.action";
        var param = {"notificationId" : notificationId};
        ajax.loadHtml2(url,param,pageRefresh,function(data){
            $("div#notificationNotesDiv").html(data);
        });
    }



    /***********************************************************************************
     * ACKNOWLEDGE NOTIFICATION
     ***********************************************************************************/

    function acknowledgeNotification(notificationId)
    {
        var url = "/prv/p/acknowledgeNotification.action";
        var param = {"notificationId" : notificationId};
        ajax.loadHtml2(url,param,pageRefresh,function(data){
            $("div#notificationNotesDiv").html(data);
        });

    }


    /***********************************************************************************
     * SWITCH CLAIM
     ***********************************************************************************/

    function claimChangeOver(){

        Ext.MessageBox.confirm('Confirm', 'Are you sure you want to switch the insurer of this claim?', 
            function ChangeOver(btn){ 
                if(btn==='yes') {
                     choxExtAjaxRequest({
                     url: '/prv/p/switchClaim.action',
                     params: {
                                 name  : 'switchClaim',
                                 id    : <s:property value="id" />
                              },
                     callback : function(options,success,response){
                         if(response.responseText){
                             var resp = Ext.util.JSON.decode(response.responseText);
                             if(resp && resp.success){
                                 // if not admin chox then load inbox as the current insurer no longer own the switched claim.
                                 loadPage(); 
                              }else if(!resp.success){ 
                                 Ext.MessageBox.show({
                                    title: 'Error',
                                    msg: resp.errors,
                                    width:300,
                                    buttons: Ext.MessageBox.OK,
                                    icon : Ext.MessageBox.ERROR
                                  });
                              }
                             }
                            }
                       });
                   }
            });
    }
    
    function lastReviewDate() {
        reviewDateSelectionDlg.getForm().reset();
        reviewDateSelectionWin.show();
    }
    
    /***********************************************************************************
     * SWITCH CHO
     ***********************************************************************************/

    function switchCho(){

        Ext.MessageBox.confirm('Confirm', 'Are you sure you want to switch the CHO of this claim to <s:property value="linkedChoName"/>?', 
            function changeCho(btn){ 
                if(btn==='yes') {
                     choxExtAjaxRequest({
                     url: '/prv/p/switchCho.action',
                     params: {
                                 name  : 'switchCho',
                                 id    : <s:property value="id" />
                              },
                     callback : function(options,success,response){
                         if(response.responseText){
                             var resp = Ext.util.JSON.decode(response.responseText);
                             if(resp && resp.success){
                                 // if not admin chox then load inbox as the current CHO no longer own the switched claim.
                                 loadPage(); 
                              }else if(!resp.success){ 
                                 Ext.MessageBox.show({
                                    title: 'Error',
                                    msg: resp.errors,
                                    width:300,
                                    buttons: Ext.MessageBox.OK,
                                    icon : Ext.MessageBox.ERROR
                                  });
                              }
                             }
                            }
                       });
                   }
            });
    }
    

    function loadPage(result, request){
        <s:if test="isChoxAdmin" >
            Ext.get('claimDetailScreenDiv').mask();
            Ext.Msg.alert('Status', 'Claim Switched Over Successfully.',pageRefresh);
        
        </s:if>
        <s:else >
            Ext.get('claimDetailScreenDiv').mask();
            Ext.Msg.alert('Status', 'Claim Switched Over Successfully.',function(){loadInbox(true);});
        </s:else>
        
    }
    
    
    /***********************************************************************************
     * CLAIM DETAIL MORE ACTION PANEL
     ***********************************************************************************/
    function moreActionOnchange(){

        var target = "#moreActionPanel";
        var selectedAction = $("div#claim-detail-extra #extraAction").val();
        $(target).html("");

        function doMarkSupplementaryInvoiced(btn){
            if(btn==='yes') {
                var url = "/prv/"+selectedAction+".action";
                var param = {"id":<s:property value="id" />};
                ajax.loadHtml2(url, param, pageRefresh);
            }else{
                $("div#claim-detail-extra #extraAction").val('-- More Actions --');
                return false;
            }
        }

        function doRunFraudCheck(btn){
            if(btn==='yes') {
                var url = "/prv/p/runFraudCheck.action";
                var param = {"id":<s:property value="id" />,"name":"runFraudCheck"};
                ajax.loadHtml2(url, param, pageRefresh);
            }else{
                $("div#claim-detail-extra #extraAction").val('-- More Actions --');
                return false;
            }
        }

        if(selectedAction!=="" && selectedAction!==null){
            
            if(selectedAction==='markSupplementaryInvoicedClaim'){
                
                Ext.MessageBox.confirm('Confirm', 'Are you sure you want to mark this as the original claim for Supplementary Invoices as this claim shares the same Customer Claim Number as another claim?',doMarkSupplementaryInvoiced);
                
            }
<s:if test="moreOptionRequestFraudCheck">
            else if(selectedAction==='fraudCheck') {
                Ext.MessageBox.confirm('Run Fraud Check', 'Running the Fraud Check will send the claim data to Keoghs ADA Fraud Tool and return a result to CHOX. It may take a short while to return a result and will require a browser refresh to be visible.\n Continue?',doRunFraudCheck);
            }
</s:if>
            else{
                var url = "/prv/p/"+selectedAction+".action";
                var param = {"id":<s:property value="id" />};
                ajax.loadHtml2(url,param,function(data){
                    $(target).html(data);
                });
                
                //in case we already have the interim payment div on page we remove the bottom div
                if(selectedAction === 'updateInterimPayment' && $('#interimPaymentDiv').size() !== 0)
                	$('#interimPaymentDiv').remove();
            }
        }

    }

    function maskClaimdetailsPage(){
        Ext.get('claimDetailScreenDiv').mask("Loading search result ...");
    }

        function getIEVersion(){
            var ua = window.navigator.userAgent;
            var msie = ua.indexOf ( "MSIE " );

            if ( msie > 0 )      // If Internet Explorer, return version number
                return parseInt (ua.substring (msie+5, ua.indexOf (".", msie )));
            else if (ua.indexOf("rv:11") > 0) {
                return 11;
            } else // If another browser, return 0
                return 0;
	}

</script>
<div id="claimDetailScreenDiv">
    <div style="width:1000px">

        <div class="chox-claim-header x-panel-bwrap chox-form-container">
<!--            <script type="text/javascript">
                changeBrandingCss();
            </script>-->

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
                        <td><label class="chox-claim-header-label">Customer</label><label class="chox-claim-header-text"><span id="ClaimDetailsCustomerLableId"><s:property value="customer.formattedName" /></span></label></td>
                        <td><label class="chox-claim-header-label">Current Status</label><label class="chox-claim-header-text"><span id="ClaimDetailsCurrentStatusLableId"><s:property value="status" /></span></label><!--span id="statusTip"><img src="img/tip.gif" style="fixed:relative;top:-50" /></span--></td>
                        <td><label class="chox-claim-header-label">Customer Contact Date</label><label class="chox-claim-header-text"><span id="ClaimDetailsCustomerContactDateLableId"><s:date name="policyHolderContactDate" format="dd MMM yyyy HH:mm"  /></span></label></td>
                    </tr>
                    <tr>
                        <s:if test="isInsurer">
                            <td><label class="chox-claim-header-label">Claim Owner</label><label class="chox-claim-header-text"><span id="ClaimDetailsInsurerClaimOwnerLableId"><s:property value="claimOwner.fullName"  /></span></label></td>
                        </s:if>
                        <s:else>
                            <td><label class="chox-claim-header-label">Insurer' Claim Owner</label><label class="chox-claim-header-text"><span id="ClaimDetailsInsurerClaimOwnerLableId"><s:property value="claimOwner.fullName"  /></span></label></td>
                        </s:else>
                        <td><label class="chox-claim-header-label">Workgroup</label><label class="chox-claim-header-text"><s:property value="workgroup.name" /></label></td>
                        <s:if test="isCHO">
                            <td><label class="chox-claim-header-label">Claim Owner</label><label class="chox-claim-header-text"><span id="ClaimDetailsCHOClaimOwnerLableId"><s:property value="supplierClaimOwner.fullName"  /></span></label></td>
                        </s:if>
                        <s:else>
                            <td><label class="chox-claim-header-label">Supplier Claim Owner</label><label class="chox-claim-header-text"><span id="ClaimDetailsCHOSupplierClaimOwnerLableId"><s:property value="supplierClaimOwner.fullName"  /></span></label></td>
                        </s:else>
                    </tr>
                    <tr>
                        <td><label class="chox-claim-header-label">Liability Status</label><label class="chox-claim-header-text"><span id="ClaimDetailsLiablityStatusLableId"><s:property value="liabilityStatus" /></span></label></td>
                        <td><label class="chox-claim-header-label">% Liability Agreed</label><label class="chox-claim-header-text"><span id="ClaimDetailsPercentageLiablityAggreedLableId"><s:property value="formattedLiability" /></span></label></td>
                        <td><label class="chox-claim-header-label">Indemnity Stance </label><label class="chox-claim-header-text"><s:property value="indemnityStance" /></label></td>
                    </tr>
                    <tr>
                        <td><label class="chox-claim-header-label">Liability Agreed Date</label><label class="chox-claim-header-text"><span id="ClaimDetailsLiablityAggreedDateLableId"><s:property value="liabilityAgreedDate" /></span></label></td>
                        <td><label class="chox-claim-header-label">Reserve Value</label><label class="chox-claim-header-text"><span id="ClaimDetailsIndemnityValueLableId">£<s:property value="indemnityAmount" /></span></label></td>
                        <td><label class="chox-claim-header-label">Claim Type</label><label class="chox-claim-header-text"><span id="ClaimDetailsClaimTypeLableId"><s:property value="claimType" /></span></label></td>
                    </tr>
                    <tr>
                        <td colspan="3" align="right">
                            <s:if test="canShowSwitchClaimButton" >
                        
                               <input id="mb1" value='Switch Claim To <s:property value="relatedInsurerName"/>' type="button" onclick="return claimChangeOver();"/>

                            </s:if>
                            <s:if test="canShowSwitchClaimToMultipleInsButton" >
                                        
                               <input id="mb2" value="Switch Insurer" type="button" onclick="return switchClaimToMultipleInsurer();"/>

                            </s:if>
                            <s:if test="canShowSwitchChoButton" >
                                        
                               <input id="mb3" value='Switch CHO To <s:property value="linkedChoName"/>' type="button" onclick="return switchCho();"/>

                            </s:if>
                            <s:if test="canRevertClaimStatus">
                                        
                               <input value="Revert Status" type="button" onclick="return revertClaimStatus();"/>
                       
                            </s:if>
                            <s:if test="canCloseClaim">
                                
                               <input value="Close Claim" type="button" onclick="return closeClaimStatus();"/>
                                        
                            </s:if>
                            <s:if test="canReopenClaim">
                                        
                               <input value="Re-Open Claim" type="button" onclick="return reopenClaimStatus();"/>
                       
                            </s:if>
                            <s:if test="canShowSlaExtensionButton">

                                <input value="Grant Extension" type="button" onclick="return setSlaExtension();"/>

                            </s:if>
                                <s:if test="canShowLastReviewButton" >
                                        
                               <input id="lastReviewButtonId" value='Last Review Date' type="button" onclick="return lastReviewDate();"/>

                            </s:if>
                         </td>
                     </tr>
   
                    <s:if test="isFnolPanelVisible">
                        <tr>
                            <td colspan="3">
                                <div class="status-info">This claim has been reviewed by an FNOL Handler, please review notes that may have been added before proceeding.</div>
                            </td>
                        </tr>
                    </s:if>
                    <s:if test="subscriberClaimUnderSlaDays">
                        <tr>
                            <td colspan="3">
                                <div class="status-info"><s:property value="subscriberTimeLeft" /> before this claim will become a Subscriber claim by default.</div>
                            </td>
                        </tr>
                    </s:if>
                    <s:elseif test="subscriberClaimAtSlaDays">
                        <tr>
                            <td colspan="3">
                                <div class="status-info">This claim will become a Subscriber claim by default at <s:property value="subscriberCutOffTime" /> today.</div>
                            </td>
                        </tr>
                    </s:elseif>
                    <s:elseif test="fixedFeeClaimUnderSlaDays">
                        <tr>
                            <td colspan="3">
                                <div class="status-info"><s:property value="fixedFeeTimeLeft" /> before this claim will become a Fixed Fee claim by default.</div>
                            </td>
                        </tr>
                    </s:elseif>
                    <s:elseif test="fixedFeeClaimAtSlaDays">
                        <tr>
                            <td colspan="3">
                                <div class="status-info">This claim will become a Fixed Fee claim by default at <s:property value="fixedFeeCutOffTime" /> today.</div>
                            </td>
                        </tr>
                    </s:elseif>
                </table>
            </fieldset>
            <div id="claim-detail-extra" >
                <table>
                    <tr>
                        <td width="80%" align="left">
                            <div>
                                <a href="javascript: loadInbox(true);" onclick="javascript: return maskClaimdetailsPage();">« Back to Search Results</a>
                                &nbsp;&nbsp;
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
                        </td>
                        <td width="20%" align="right">
                            <s:if test="canExport">
                                <div>
                                    <a href="javascript:claimReport();">Export Claim To Excel</a>
                                </div>
                            </s:if>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>

    <div id="moreActionPanel" class="extra-action-class"></div>

    <s:if test="claimNumberDuplications > 0 &&  claimNumberDuplications < 20 && notificationAccessibility.claimNumberNotificationAccessibility">
        <s:action name="getDuplicatedClaimAlert" namespace="/prv/p" executeResult="true">
            <s:param name="claimId"><s:property value="id" /></s:param>
            <s:param name="claimNumber"><s:property value="claimNumber" /></s:param>
        </s:action>
    </s:if>
    <s:elseif test="claimNumberDuplications > 19 && notificationAccessibility.claimNumberNotificationAccessibility">
        <div class="chox-claim-header x-panel-bwrap chox-form-container">
            <div class="status-info">
                This claim shares it's claim number with another <s:property value="claimNumberDuplications" /> claims.
            </div> 
        </div>
    </s:elseif>

    <s:if test="isDuplicatedSupplementaryInvoiceExists && notificationAccessibility.duplicatedSupplementaryInvoiceNotificationAccessibility">
        <s:action name="getDuplicatedSupplementaryInvoiceAlert" namespace="/prv/p" executeResult="true">
            <s:param name="claimId"><s:property value="id" /></s:param>
            <s:param name="customerClaimRefNum"><s:property value="customer.claimReference" /></s:param>
        </s:action>
    </s:if>

    <s:if test="isEscalatedToSupervisor">
        <s:action name="getClaimEscalatedToSupervisorAlert" namespace="/prv/p" executeResult="true"></s:action>
    </s:if>

    <s:if test="fraudCheckPanelVisible">
        <s:action name="fraudCheck" namespace="/prv/p" executeResult="true"></s:action>
    </s:if>

    <s:if test="finalReviewRequired">
        <div class="chox-claim-header x-panel-bwrap chox-form-container">
            <div class="status-info">
                <s:property value="finalReviewMessage" />
            </div> 
        </div>
    </s:if>

    <s:if test="notificationAccessibility.userViewingNotificationAccessibility">
        <div id="userViewingThisClaimDiv" class="status-warning" style="display:none;">
            This claim is currently being viewed and / or modified by the following user(s) : <span id="userViewingThisClaim"></span>
        </div>
    </s:if>
    
    <s:if test="notificationAccessibility.awaitingLitigationOutcomeNotificationAccessibility && !isInsurer">
        <s:action name="getAwaitingLitigationOutcomeAlert" namespace="/prv/p" executeResult="true">
            <s:param name="claimId"><s:property value="id" /></s:param>
        </s:action>
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
    <s:elseif test="isAnyAllIntelligentNotes && notificationAccessibility.intelligentNotesNotificationAccessibility">
        <div class="chox-claim-header x-panel-bwrap chox-form-container">
            <fieldset class="x-fieldset">
                <legend>Additional Notes</legend>
                <div id="intelligentNotesDiv" class="status-warning listContainer" style="display:none">
                    <ul>
                        <s:iterator value="intelligentNotes2">
                            <li><s:property/></li>
                        </s:iterator>
                    </ul>
                </div>
            </fieldset>
        </div>
    </s:elseif>

    <s:if test="notificationAccessibility.notificationNotesNotificationAccessibility">
        <div>
            <div id="notificationNotesDiv">
                <s:action namespace="/prv/p" executeResult="true" name="renderNotifications">
                    <s:param name="id"><s:property value="id" /></s:param>
                </s:action>
            </div>
        </div>
    </s:if>

    <s:if test="hasOutstandingInterimPayment">
        <div id="interimPaymentDiv">
            <s:action namespace="/prv/p" executeResult="true" name="updateInterimPayment">
                <s:param name="id"><s:property value="id" /></s:param>
            </s:action>
        </div>
    </s:if>

    <s:if test="caseWithClientsSolicitor">
        <div class="chox-claim-header x-panel-bwrap chox-form-container">
            <div class="status-info">
                This invoice was flagged as with the Clients Solicitor on <s:property value="dateMarkedWithSolicitorAsString" /> by <s:property value="userMarkedWithSolicitor" />.
            </div> 
        </div>
    </s:if>

    <s:if test="matchedClaimRequiringReview">
        <div id="matchedClaimRequiringReviewDiv">
            <s:action namespace="/prv/p" executeResult="true" name="matchedClaimReview">
                <s:param name="id"><s:property value="id" /></s:param>
            </s:action>
        </div>
    </s:if>


    <script type="text/javascript">
    

        Ext.onReady(function() {

        

            var strgeneralActionPanelText = $("#generalActionPanel").html();
            strgeneralActionPanelText = strgeneralActionPanelText.replace('<div class="action-message"></div>',"");
            strgeneralActionPanelText = strgeneralActionPanelText.replace('<h1>',"");
            strgeneralActionPanelText = strgeneralActionPanelText.replace('</h1>',"");
            strgeneralActionPanelText = strgeneralActionPanelText.replace(/\s+/g,'');

            $("#formSubmitButtons").css("display", "none");

            if(strgeneralActionPanelText.length<=0){
                $("#generalActionPanel").hide();
                $("#generalActionPanel").css("display:", "none");
            }else{
                $("#generalActionPanel").show();
                $("#generalActionPanel").css("display:", "block");
            }
        

        });

        function openTab(tabPosition){
            tabPanel1.setActiveTab(tabPosition);
        }

        function expandHireMonitoringDetails(expand) {
        
            if (expand) {
                document.getElementById("expandAllHireId").onclick = function (){expandHireMonitoringDetails(false);};
                document.getElementById("expandAllHireId").innerHTML = '-Collapse All';
                $("#expandAllHireId").attr("title", "Collapse All");
                $("#hireMonitoringWId").css("display", "block");
                $("#hireMonitoringRId").css("display", "block");
                $("#newRevisedECDWId").css("display", "block");
                if (document.getElementById("hireMonitoringVehicleDetailRId") !== null)
                    $("#hireMonitoringVehicleDetailRId").css("display", "block");
                if (document.getElementById("hireMonitoringVehicleDetailWId") !== null)
                    $("#hireMonitoringVehicleDetailWId").css("display", "block");
            } else {
                document.getElementById("expandAllHireId").onclick = function (){expandHireMonitoringDetails(true);};
                document.getElementById("expandAllHireId").innerHTML = '+Expand All';
                $("#expandAllHireId").attr("title", "Expand All");
                $("#hireMonitoringWId").css("display", "none");
                $("#hireMonitoringRId").css("display", "none");
                $("#newRevisedECDWId").css("display", "none");
                if (document.getElementById("hireMonitoringVehicleDetailRId") !== null)
                    $("#hireMonitoringVehicleDetailRId").css("display", "none");
                if (document.getElementById("hireMonitoringVehicleDetailWId") !== null)
                    $("#hireMonitoringVehicleDetailWId").css("display", "none");
            }
        }

        function expandInsurerHireMonitoringDetails(expand) {
        
            if (expand) {
                document.getElementById("expandAllInsurerHireId").onclick = function (){expandInsurerHireMonitoringDetails(false);};
                document.getElementById("expandAllInsurerHireId").innerHTML = '-Collapse All';
                $("#expandAllInsurerHireId").attr("title", "Collapse All");
                $("#insurerHireMonitoringWId").css("display", "block");
                $("#insurerHireMonitoringRId").css("display", "block");
                $("#newInsurerRevisedECDWId").css("display", "block");
                if (document.getElementById("insurerHireMonitoringVehicleDetailRId") !== null)
                    $("#insurerHireMonitoringVehicleDetailRId").css("display", "block");
                if (document.getElementById("insurerHireMonitoringVehicleDetailWId") !== null)
                    $("#insurerHireMonitoringVehicleDetailWId").css("display", "block");
            } else {
                document.getElementById("expandAllInsurerHireId").onclick = function (){expandInsurerHireMonitoringDetails(true);};
                document.getElementById("expandAllInsurerHireId").innerHTML = '+Expand All';
                $("#expandAllInsurerHireId").attr("title", "Expand All");
                $("#insurerHireMonitoringWId").css("display", "none");
                $("#insurerHireMonitoringRId").css("display", "none");
                $("#newInsurerRevisedECDWId").css("display", "none");
                if (document.getElementById("insurerHireMonitoringVehicleDetailRId") !== null)
                    $("#insurerHireMonitoringVehicleDetailRId").css("display", "none");
                if (document.getElementById("insurerHireMonitoringVehicleDetailWId") !== null)
                    $("#insurerHireMonitoringVehicleDetailWId").css("display", "none");
            }
        }

        function expandClaimDetails(expand) {
            if (expand) {
                document.getElementById("expandAllClaimId").onclick = function (){expandClaimDetails(false);};
                document.getElementById("expandAllClaimId").innerHTML = '-Collapse All';
                $("#expandAllClaimId").attr("title", "Collapse All");
                $("#customerDetailsRId").css("display", "block");
                $("#customerDetailsWId").css("display", "block");
                $("#injuryRId").css("display", "block");
                $("#injuryWId").css("display", "block");
                $("#injurySolicitorRId").css("display", "block");
                $("#injurySolicitorWId").css("display", "block");
                $("#vehicleDamageRId").css("display", "block");
                $("#vehicleDamageWId").css("display", "block");
                $("#customerVehicleDamageRId").css("display", "block");
                $("#customerVehicleDamageWId").css("display", "block");
                $("#claimDetailsRId").css("display", "block");
                $("#claimDetailsWId").css("display", "block");
                $("#claimReviewsId").css("display", "block");
                $("#incidenDetailsRId").css("display", "block");
                $("#incidenDetailsWId").css("display", "block");
                $("#thirdPartyDetailsRId").css("display", "block");
                $("#thirdPartyDetailsWId").css("display", "block");
                $("#witnessDetailsRId").css("display", "block");
                $("#witnessDetailsWId").css("display", "block");
                $("#mitigationStatementRId").css("display", "block");
                $("#mitigationStatementWId").css("display", "block");
            } else {
                document.getElementById("expandAllClaimId").onclick = function (){expandClaimDetails(true);};
                document.getElementById("expandAllClaimId").innerHTML = '+Expand All';
                $("#expandAllClaimId").attr("title", "Expand All");
                $("#customerDetailsRId").css("display", "none");
                $("#customerDetailsWId").css("display", "none");
                $("#injuryRId").css("display", "none");
                $("#injuryWId").css("display", "none");
                $("#injurySolicitorRId").css("display", "none");
                $("#injurySolicitorWId").css("display", "none");
                $("#vehicleDamageRId").css("display", "none");
                $("#vehicleDamageWId").css("display", "none");
                $("#customerVehicleDamageRId").css("display", "none");
                $("#customerVehicleDamageWId").css("display", "none");
                $("#claimDetailsRId").css("display", "none");
                $("#claimDetailsWId").css("display", "none");
                $("#claimReviewsId").css("display", "none");
                $("#incidenDetailsRId").css("display", "none");
                $("#incidenDetailsWId").css("display", "none");
                $("#thirdPartyDetailsRId").css("display", "none");
                $("#thirdPartyDetailsWId").css("display", "none");
                $("#witnessDetailsRId").css("display", "none");
                $("#witnessDetailsWId").css("display", "none");
                $("#mitigationStatementRId").css("display", "none");
                $("#mitigationStatementWId").css("display", "none");
            }
        }

        function expandInvoiceDetails(expand) {
            if (expand) {

                document.getElementById("expandAllInvoiceId").onclick = function (){expandInvoiceDetails(false);};
                document.getElementById("expandAllInvoiceId").innerHTML = '-Collapse All';
                $("#expandAllInvoiceId").attr("title", "Collapse All");
                $("#invoiceDetailRId").css("display", "block");
                $("#invoiceDetailWId").css("display", "block");
                $("#hireVehicleDetailRId").css("display", "block");
                $("#hireVehicleDetailWId").css("display", "block");
                $("#extrasRId").css("display", "block");
                $("#extrasWId").css("display", "block");
                $("#engineerReportRId").css("display", "block");
                $("#engineerReportWId").css("display", "block");
                $("#formPaymentDetailsRId").css("display", "block");
                $("#formRepairExtrasRId").css("display", "block");
                $("#formRepairExtrasWId").css("display", "block");
                $("#formSubmitButtons").css("display", "block");
                document.getElementById('hideAndShow').value=1;
            } else {
                document.getElementById("expandAllInvoiceId").onclick = function (){expandInvoiceDetails(true);};
                document.getElementById("expandAllInvoiceId").innerHTML = '+Expand All';
                $("#expandAllInvoiceId").attr("title", "Expand All");
                $("#invoiceDetailRId").css("display", "none");
                $("#invoiceDetailWId").css("display", "none");
                $("#hireVehicleDetailRId").css("display", "none");
                $("#hireVehicleDetailWId").css("display", "none");
                $("#extrasRId").css("display", "none");
                $("#extrasWId").css("display", "none");
                $("#engineerReportRId").css("display", "none");
                $("#engineerReportWId").css("display", "none");
                $("#formPaymentDetailsRId").css("display", "none");
                $("#formRepairExtrasRId").css("display", "none");
                $("#formRepairExtrasWId").css("display", "none");
                $("#formSubmitButtons").css("display", "none");
                document.getElementById('hideAndShow').value=0;
            }
        
        }
   
    
    
    </script>

    <div id="generalActionPanel" style="display: none;">
        <s:action name="getActionPanel" namespace="/prv/p" executeResult="true" />
        <s:if test="actionError!=null">
            <div class="chox-claim-header x-panel-bwrap chox-form-container">
                <div class="status-error">
                    <div id="errorMessage"></div>
                    <script type="text/javascript" language="JavaScript">
                        var errorMessages="<s:property value="actionError" />";
                        var errorMessageList=errorMessages.split('.');
                        var messageerrorHTML="";
                        //alert("<s:property value="actionError" />");
                        if(errorMessageList.length>0){
                            for(var i=0;i<errorMessageList.length;i++){
                                if(i>0){
                                    if(errorMessageList[i].charAt(0)==="'") {
                                        messageerrorHTML+=('<p>'+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+ errorMessageList[i]);
                                    }else if( errorMessageList[i-1].charAt(errorMessageList[i-1].length-1)==="," ){
                                        messageerrorHTML+=('<p>'+"&nbsp;&nbsp;&nbsp;"+ errorMessageList[i]);
                                    }else{
                                        messageerrorHTML+=('<p>' + errorMessageList[i]);}
                                }
                                else{
                                    messageerrorHTML+=('<p>' + errorMessageList[i]);}
                        
                                var errorMesgeLength=errorMessageList[i].length;

                                if(errorMesgeLength>0&&errorMessageList[i].charAt(errorMesgeLength-1)!==","&&errorMessageList[i].charAt(errorMesgeLength-1)!==" "){
                                    messageerrorHTML+='.</p>';
                                } else  if(errorMesgeLength>0&&errorMessageList[i].charAt(errorMesgeLength-1)===" "){
                                                             
                                    messageerrorHTML+='</p>';
                                }
                                else
                                {
                                    messageerrorHTML+='</p>';
                                }
                            }
                            document.getElementById("errorMessage").innerHTML = messageerrorHTML;
                        }
                
                    </script>
                </div>
            </div>
        </s:if>
        <div class="action-message"><s:property value="actionResult" /></div>
    </div>

    <s:if test="isShowPenaltyChargeAlert">
    	<div id="penaltyAlertPanelId">
        	<s:action name="getAlertPanel" namespace="/prv/p" executeResult="true" />
        </div>
    </s:if>
    
    <div id="tabContainer">
        <div id="claimDetailsContainer" class="x-hide-display">
            <div id="claimDetails">
                <s:if test="tabAccessibility.claimDetailTabAccessibility != 0">
                    <div class="x-panel-bwrap chox-form-container">
                        <label id="expandAllClaimId" onclick="expandClaimDetails(true);" title="Expand All" style="cursor:pointer;font: 11px tahoma,arial,verdana,sans-serif;">+Expand All</label>
                        <table cellpadding="0" cellspacing="0" border="0" width="100%">
                            <tr valign="top">
                                <td class="chox-form-left-col">
                                    <div>
                                        <s:action name="getCustomer" namespace="/prv/p" executeResult="true">
                                            <s:param name="claimId"><s:property value="id"/></s:param>
                                        </s:action>
                                    </div>
                                    <div>
                                        <s:action name="getCustomerMitigation" namespace="/prv/p" executeResult="true">
                                            <s:param name="claimId"><s:property value="id" /></s:param>
                                        </s:action>
                                    </div>
                                    <div>
                                        <s:action name="getCustomerVehicleDamage" namespace="/prv/p" executeResult="true">
                                            <s:param name="claimId"><s:property value="id" /></s:param>
                                        </s:action>
                                    </div>
                                    <div>
                                        <s:action name="getIncident" namespace="/prv/p" executeResult="true">
                                            <s:param name="claimId"><s:property value="id" /></s:param>
                                        </s:action>
                                    </div>
                                    <div>
                                        <s:action name="getClaimDetails" namespace="/prv/p" executeResult="true">
                                            <s:param name="claimId"><s:property value="id" /></s:param>
                                        </s:action>
                                    </div>
                                </td>
                                <td>
                                    <s:if test="isInsurer || isChoxAdmin">
                                        <fieldset class="x-fieldset">
                                            <legend>Claim Reviews</legend>
                                            <div style="display:none" class="form-container" id="claimReviewsId">
                                                <table class="chox-table-form">
                                                    <tr>
                                                        <td><label class="std-label-ro">Invoice Review Required</label></td>
                                                        <td>&nbsp;</td>
                                                        <td><label class="std-data-ro"><s:property value="isInvoiceReviewRequiredDesc" /></label></td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </fieldset>
                                    </s:if>
                                    <div>
                                        <s:action name="getThirdParty" namespace="/prv/p" executeResult="true">
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
        </div>

        <div id="hireMonitoringDetails" class="x-hide-display">
            <s:if test="tabAccessibility.hireMonitoringTabAccessibility != 0">
                <div class="x-panel-bwrap chox-form-container">
                    <label id="expandAllHireId" onclick="expandHireMonitoringDetails(true);" title="Expand All" style="cursor:pointer;font: 11px tahoma,arial,verdana,sans-serif;">+Expand All</label>
                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                        <tr valign="top">
                            <td class="chox-form-left-col">
                                <s:action name="getHireMonitoringDetail" namespace="/prv/p" executeResult="true">
                                    <s:param name="claimId"><s:property value="id" /></s:param>
                                </s:action>
                            </td>
                            <td>
                                <div>
                                    <s:action name="getHireMonitoringEcd" namespace="/prv/p" executeResult="true">
                                        <s:param name="claimId"><s:property value="id" /></s:param>
                                        <s:param name="ecdFormAccessRight"><s:property value="tabAccessibility.hireMonitoringTabAccessibility" /></s:param>
                                    </s:action>
                                </div>
                                <s:if test="!isInsurerInvoice" >
                                    <div>
                                        <s:action name="getVehicleMonitoringHire" namespace="/prv/p" executeResult="true">
                                            <s:param name="claimId"><s:property value="id" /></s:param>
                                            <s:param name="claimStatus"><s:property value="status" /></s:param>
                                        </s:action>
                                    </div>
                                </s:if>
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
<s:if test="insurerLouDates">
        <div id="insurerHireMonitoringDetails" class="x-hide-display">
                <div class="x-panel-bwrap chox-form-container">
                    <label id="expandAllInsurerHireId" onclick="expandInsurerHireMonitoringDetails(true);" title="Expand All" style="cursor:pointer;font: 11px tahoma,arial,verdana,sans-serif;">+Expand All</label>
                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                        <tr valign="top">
                            <td class="chox-form-left-col">
                                <s:action name="getInsurerHireMonitoringDetail" namespace="/prv/p" executeResult="true">
                                    <s:param name="claimId"><s:property value="id" /></s:param>
                                </s:action>
                            </td>
                            <td>
                                <div>
                                    <s:action name="getInsurerHireMonitoringEcd" namespace="/prv/p" executeResult="true">
                                        <s:param name="claimId"><s:property value="id" /></s:param>
                                        <s:param name="ecdFormAccessRight"><s:property value="tabAccessibility.insurerHireMonitoringTabAccessibility" /></s:param>
                                    </s:action>
                                </div>
                                <div>
                                    <s:action name="getInsurerVehicleMonitoringHire" namespace="/prv/p" executeResult="true">
                                        <s:param name="claimId"><s:property value="id" /></s:param>
                                    </s:action>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
        </div>
</s:if>
        <div id="invoiceDetails" class="x-hide-display">
            <s:if test="tabAccessibility.invoiceDetailTabAccessibility != 0">
                <div class="x-panel-bwrap chox-form-container">
                    <label id="expandAllInvoiceId" onclick="expandInvoiceDetails(true);" title="Expand All" style="cursor:pointer;font: 11px tahoma,arial,verdana,sans-serif;">+Expand All</label>
                    <s:action name="getInvoiceRecalculation" namespace="/prv/p" executeResult="true">
                        <s:param name="claimId"><s:property value="id" /></s:param>
                        <s:param name="claimStatus"><s:property value="status" /></s:param>
                    </s:action>
                </div>
            </s:if>
        </div>

        <div id="attachmentTab" class="x-hide-display"></div>

        <div id="historyTab" class="x-hide-display"></div>

        <div id="auditTrailTab" class="x-hide-display"></div>

        <div id="commentTab" class="x-hide-display"></div>

        <div id="taskTab" class="x-hide-display"></div>
        
    </div>
</div>
