<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ include file="batch_update.jspf" %>

<head>
    <title>CHOX</title>
    
    <script type="text/javascript">

        var defaultDropdownValue={'value':-1,'text':'--- ALL ---'};
        var pagingBar;
        var tabs;
        var recordPerPage = 20;
        var claimsGrid;
        var claimStore;
        var exportIntervelId;
        var doClaimOwnerAction;
        var doClaimRoutedAction;
        var doInsurerClaimOwnerAction;
        var manualInvoiceFilter;
        var dashboardActionName;
        var isChoxAdmin = <s:property value="isChoxAdmin"/>;
        var isTaskManagementEnabled = <s:property value="taskManagementEnabled"/>;
        var taskTabTitle = isChoxAdmin ? 'Tasks' : 'Tasks&nbsp  <div  class = "noti_bubble" style="background-color:'+'black'+'; ">'+ '?' +'</div>';
        var filterName;
        var title;
        var actionMenu;
        var batchUpdateSelectionModel;
//        var isQueueSelectionSearch = false;
//        var isSearchScreenSearch = false;
//        Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
//        Ext.BLANK_IMAGE_URL = '<%= request.getContextPath()%>/images/default/s.gif';

        Ext.onReady(function() {
            // change the header width to 1300px from 1000px(used for claim detail page).
            $('div.inner').css({"width":"1190px"});
            Ext.QuickTips.init();
            if (isChoxAdmin) {
                Ext.util.CSS.swapStyleSheet("theme","<%= request.getContextPath()%>/css/xtheme-seeTestTheme.css");
            } else if (<s:property value="isInsurer"/>) {
                Ext.util.CSS.swapStyleSheet("theme","<%= request.getContextPath()%>/css/xtheme-gray.css");
            }
            setupDashboardActionName();
            loadDataFromSession();
            // Need to comes before setupTabPanel method so that we can hide or show the grid depends on the tab selected.
            setupGrid(); 
            setupTabPanels();
            setupActivityMonitor();
            showBrowserWarningIfNeeded();
        });
        
        function setupDashboardActionName() {
            <s:if test="isInsurer">
                dashboardActionName = "showInsurerBoardHeader";
            </s:if>
            <s:if test="isCHO">
                dashboardActionName = "showChoBoardHeader";
            </s:if>
        }
        
        function showBrowserWarningIfNeeded() {
            <s:if test="showSplash" >
                    onShowBrowserWarning();
            </s:if>
        }
        
        function setupActivityMonitor() {
            <s:if test="isChoxAdmin!=true && enableActivityMonitor">
                var pingServerUrl = '/prv/p/activityMonitoringAction.action';
                var checkStatusIUrl = '/prv/p/checkViewingStatus.action';
                activityMonitor.setup(pingServerUrl, checkStatusIUrl,  <s:property value="activityMonitorRequestInterval"/>);
            </s:if>
        }

        function loadDataFromSession() {  
            <s:if test="loadingInboxPageFirstTimeAfterLogin"> 
                Ext.state.Manager.set("claims_grid_baseParams", null);
                Ext.state.Manager.set("xml_upload_grid_baseParams", null);
                Ext.state.Manager.set("recentlyClickedQueueRowNumber", null);
                Ext.state.Manager.set("recentlyClickedXmlUploadRowNumber", null);
                Ext.state.Manager.set("isClaimSearchMade", false);
                Ext.state.Manager.set("currentTabIndex", <s:property value="preSelectedActiveTab"/>);
                Ext.state.Manager.set("uploaded_files_grid_title", 'Files Uploaded Today');
//                Ext.state.Manager.set("syncWithSearchField", false);
                manualInvoiceFilter = false;
            </s:if>
            <s:else > // The below line need to be investigated
                manualInvoiceFilter = Ext.state.Manager.get("manualInvoiceFilter");
            </s:else>
        }

        function setupGrid() {
            
            var claimReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',
                root: 'results',
                fields:[
                    {name:'id'},
                    {name:'status'},
                    {name:'createdBy'},
                    {name:'invoiceAmount'},
                    {name:'invoiceUploadDate'},
                    {name:'policyNumber'},
                    {name:'workgroup'},
                    {name:'supplierReference'},
                    {name:'claimNumber'},
                    {name:'claimType'},
                    {name:'statusModifiedDate', type: 'string', dateFormat:'timestamp'},
                    {name:'reviewDate', type: 'string', dateFormat:'timestamp'},
                    {name:'insurer'},
                    {name:'cho'},
                    {name:'isWorkgroupEditable', type:'boolean'},
                    {name:'isOwnershipEditable', type:'boolean'},
                    {name:'ownerName'},
                    {name:'choOwnerName'},
                    {name:'noAttachments'}
                ]
            });
            
            claimStore = new choxDataStore({
                url: '/prv/p/doSearchClaim.action',
                autoLoad:false,
                reader: claimReader,
                remoteSort: true,
                timeout:1800000,
                listeners:{
                    beforeload: function(store, options) {
                        var baseParams = Ext.apply({}, options.params, store.baseParams);
                        options.params.queueCount = null;
                        options.params.queueNumber = null;
                        
//                        if (baseParams.canLoadData !== false && typeof baseParams.canLoadData !== 'undefined') { 
                        // This is used as key(when rendering the grid) weather to load the empty grid or load grid with previos search criteria. 
                        if (baseParams.canLoadData === true) {
                            Ext.state.Manager.set("isClaimSearchMade", true);
                        } else {
                            Ext.state.Manager.set("isClaimSearchMade", false);
                        }
                        // store the search criteria in the cookie
                        Ext.state.Manager.set("claims_grid_baseParams", baseParams);
                        if (claimsGrid) {
                            claimsGrid.setTitle("");
                        }
                    }
                    ,load: function(store, records, options) {
                            // If the queue count does not match with the claims count result upon clicking the queue button, then update the queue total.
                            if (typeof queueGrid !== 'undefined' && typeof store.baseParams.queueNumber !== 'undefined' 
                                    && queueGrid.getStore().getTotalCount() > 0 && store.baseParams.queueCount !== store.getTotalCount()) {
                                var queueRecord = queueGrid.getStore().getAt(store.baseParams.queueNumber);
                                queueRecord.set('queueNameWithCount', queueRecord.get('queueName') + ' (' +store.getTotalCount() + ')');
                                queueRecord.commit();
                            }
//                        if (options.params.canLoadData) {
                          if (options.params.gridTitle !== '') {
                              claimsGrid.setTitle(options.params.gridTitle +" ("+store.getTotalCount()+")");
                          }
//                        } else if(options.params.canLoadData === false) {
//                            claimsGrid.setTitle("");
//                        } else if(typeof options.params.canLoadData === 'undefined') {
//                            claimsGrid.setTitle(claimsGridTitle +" ("+store.getTotalCount()+")");
//                        }
                    }
                }
            });
            
            claimStore.setDefaultSort('created', 'desc');
            
            batchUpdateSelectionModel = new Ext.grid.CheckboxSelectionModel();

            pagingBar = new Ext.PagingToolbar({
                pageSize: recordPerPage,
                store: claimStore,
                displayInfo: true,
                displayMsg: 'Displaying claims {0} - {1} of {2}',
                emptyMsg: "No claims to display",
                plugins: new Ext.ux.ProgressBarPager()
            });

            renderBatchUpdate();
            
            var claimsExportToExcelTbar = new Ext.Toolbar({
            items:[{
                    text:'Export To Excel',
                    id : 'claimsExportToExcelButtonId',
                    disabled : !<s:property value="canExport" />,
                    handler : function() {
                        doExportExcel();
                    }
                }]
            });
           

            claimsGrid = new Ext.grid.GridPanel({
                id : 'inboxClaimsGridId',
                loadMask: {msg:"Loading Claims..."},
                ds: claimStore,
                renderTo : 'gridHolder',
                listeners:  {
                                cellclick: maskInboxScreen,
                                afterrender : function(grid) {
                                    if (Ext.state.Manager.get("isClaimSearchMade")) {
                                        claimStore.baseParams = Ext.state.Manager.get("claims_grid_baseParams");
                                        claimStore.load();
                                    }
                                }
                            },
                width: 1190,
                enableColumnMove: false,
                columns: [
                    batchUpdateSelectionModel,
                    {header: "Supplier Ref", width: 100, sortable: true, dataIndex: 'supplierReference',
                        renderer:function(value,p,r){
                            return '<span style="text-decoration: underline; color: #15428B; font-size:12px; cursor: pointer;">' + value + '</span>';}},
                    {header: "Claim Type", width: 50, sortable: true, dataIndex: 'claimType'},
                    {header: "Claim No", width: 60, sortable: true, dataIndex: 'claimNumber'},
                    {header: "Insurer's Policy No", width: 60, sortable: true, dataIndex: 'policyNumber'},
                    {header: "Invoice Upload Date", width: 60, sortable: true, dataIndex: 'invoiceUploadDate'},
                    {header: "Status", width: 100, sortable: true, dataIndex: 'status'},
                    {header: "Total To Pay", width: 60, sortable: true, dataIndex: 'invoiceAmount', align: 'right'},
                    {header: "Workgroup", width: 100, sortable: true,hidden: (<s:property value="isInsurer"/> && !<s:property value="insurerIsWorkgroupEnabled"/>) , dataIndex: 'workgroup'},
                    {header: "Ins Owner", width: 50, sortable: true,hidden: (<s:property value="isInsurer"/> && !<s:property value="insurerIsClaimOwnershipEnabled"/> ), dataIndex: 'ownerName'},
                    {header: "CHO Owner", width: 50, sortable: true,hidden: (<s:property value="isCHO"/> && !<s:property value="choIsClaimOwnershipEnabled"/>), dataIndex: 'choOwnerName'},
                    {header: "Status Modified Date", width: 40, sortable: true, dataIndex: 'statusModifiedDate'},
                    {header: "Review Date", width: 40, sortable: true, dataIndex: 'reviewDate'},
                    {header: "CHO", width: 80, sortable: true, dataIndex: 'cho'},
                    {header: "Insurer", width: 80, sortable: true, dataIndex: 'insurer'},
                    {header: "Viewing", width: 30, sortable: false, dataIndex: 'id',renderer:function(value,p,r){
                            return '<input type="hidden" name="viewingId" value="' + value + '" /><label id="viewingLabel_' + value + '">-</label>';}},
                    {header: "", width : 40, sortable : true, dataIndex: 'noAttachments', renderer : function(value, metaData, record, rowIndex, colIndex, store){
                            if(value > 0){metaData.css = 'paperClip';} 
                        }}
                ],
                stateId:'chox_claim_grid',
                stateful:true,
                sm:batchUpdateSelectionModel,
                stripeRows:true,
                layout:'fit',
                autoHeight:true,
                enableHdMenu:false,
                title:' ',
                viewConfig:{forceFit:true},
                bbar: pagingBar,
                tbar:[actionMenu, '->', claimsExportToExcelTbar]
            });
        }

        function maskInboxScreen(grid, rowIndex, columnIndex){
            var record = grid.getStore().getAt(rowIndex);
            if(columnIndex === 1){
                Ext.get('inboxScreenDiv').mask("loading claim details ...");
                /*
                 *  this is extra call to load claim details page. this will be called when column no one is clicked not the hiberlink. This make sure the page is not only masked but also loading claim details page.
                 */
                loadClaimDetail(record.get('id'));
            };
            var selModel = grid.getSelectionModel();
            var selectedRecords = selModel.getSelections();
            var selectedCount = selModel.getCount();
            var allInsurerInvoice = true;
            for(var i =0;i<selectedCount;i++) {
                if (selectedRecords[i].get('claimType') !== 'Insurer Invoice'
                        && (selectedRecords[i].get('claimType') !== 'Insurer Claim' || selectedRecords[i].get('status') === 'ClaimUnacknowledgedUnrouted')){
                    allInsurerInvoice = false;
                }
            }
            if (selectedCount > 0 && allInsurerInvoice === true && !manualInvoiceFilter){
                Ext.state.Manager.set("manualInvoiceFilter",true);
                manualInvoiceFilter = true;
            }else if (allInsurerInvoice === false && manualInvoiceFilter){
                Ext.state.Manager.set("manualInvoiceFilter",false);
                manualInvoiceFilter = false;
            }
        }

        function setupTabPanels() {

            var taskTab = { 
                            contentEl:'taskPanelTab', 
                            id:'taskPanelTabId', 
                            tabCls : 'noti_Container',
                            title: taskTabTitle, 
                            autoHeight:'true', 
                            listeners: {activate: handleActivate}, 
                            autoLoad: choxUpdateEl({url:'/prv/p/getTaskPanel.action'})
            };
            var inboxTab = { 
                            contentEl:'searchPanelTab', 
                            id:'searchPanelTabId', 
                            title:'Inbox',
                            listeners: {activate: handleActivate}, 
                            autoLoad: choxUpdateEl({url:'/prv/p/searchClaim.action', params : {loadSearchPanelSelectionFromSession : (Ext.state.Manager.get("isClaimSearchMade")) ? true : false}})
            };
            var reportTab = { 
                            contentEl:'reportPanelTab', 
                            id:'reportPanelTabId', 
                            title:'Reports',
                            listeners: {activate: handleActivate}, 
                            autoLoad: choxUpdateEl({url:'/prv/p/buildReport.action'})
            };
            var adminTab = { 
                            contentEl:'adminPanelTab', 
                            id:'adminPanelTabId', 
                            title:'Admin<sup>'+' '+'</sup>', 
                            listeners: {activate: handleActivate}, 
                            autoLoad: choxUpdateEl({url:'/prv/p/adminFunction.action'})
            };
            var dashboardTab = { 
                                contentEl:'boardPanelTab', 
                                id:'boardPanelTabId', 
                                title:'Dashboard<sup>'+' '+'</sup>', 
                                listeners: {activate: handleActivate}, 
                                autoLoad: choxUpdateEl({url:'/prv/p/'+dashboardActionName+'.action'})
            };
            var xmlUploadTab = { 
                                contentEl:'xmlUploadTab', 
                                id:'xmlUploadTabId', 
                                title:'Claim/Invoice Upload<sup>'+' '+'</sup>', 
                                listeners: {activate: handleActivate}, 
                                autoLoad: choxUpdateEl({url:'/prv/p/XmlUpload.action'})
            };
            
            var tabItems = [];
            
            <s:if test="taskManagementEnabled">
                tabItems.push(taskTab);
            </s:if>

            <s:if test="menuAccessibility.isSearchMenuAccessibility">
                tabItems.push(inboxTab);
            </s:if>

            <%--<s:if test="menuAccessibility.isInboxMenuAccessibility!=true">--%>
        //            tabs.remove('inboxPanelTabId', true);
            <%--</s:if>--%>

            <s:if test="menuAccessibility.isReportMenuAccessibility">
                tabItems.push(reportTab);
            </s:if>

            <s:if test="menuAccessibility.isAdminMenuAccessibility">
                tabItems.push(adminTab);
            </s:if>

            <s:if test="menuAccessibility.isDashBoardMenuAccessibility">
                <s:if test="IsComUser || IsScrUser">
                    tabItems.push(dashboardTab);
                </s:if>
                <s:else >
                    tabItems.splice(0, 0, dashboardTab);
                </s:else>
            </s:if>

            <s:if test="menuAccessibility.isUploadMenuAccessibility">
                tabItems.push(xmlUploadTab);
            </s:if>

            tabs = new Ext.TabPanel({
                renderTo: 'tabPanel',
                autoheight: true,
//                width : 1300,
                activeTab: Ext.state.Manager.get("currentTabIndex"),
                listeners: { 
                    beforerender : updateTaskTab,
                    tabchange : function(tab, activeTab) {
                        Ext.state.Manager.set("currentTabIndex", tab.items.indexOf(activeTab));
                    }
                },
                items: tabItems
            });
        }

        function handleActivate(tab){
            
//            claimsGrid.hide();
            Ext.fly('inboxClaimsGridId').addClass('x-hide-display');
            Ext.fly('xmlClaimsStatusGridDiv').addClass('x-hide-display');
            activityMonitor.clearViewingStatus();

            if(tab.title.indexOf('Inbox') > -1) {
                
//                claimsGrid.show();
                Ext.fly('inboxClaimsGridId').removeClass('x-hide-display');
                
                <s:if test="isChoxAdmin!=true && enableActivityMonitor">
                        activityMonitor.refreshViewingStatus();
                </s:if>
                
                if(tab.title.indexOf('Search') > -1){
                    doClaimRoutedAction.setText('Route Claim(s)');
                }
            } else if(tab.title.indexOf('Claim/Invoice Upload') > -1) { 
                Ext.fly('xmlClaimsStatusGridDiv').removeClass('x-hide-display');
            }
        }

        function updateTaskTab() {
            if (isChoxAdmin || !isTaskManagementEnabled) {
                return;
            }
            choxExtAjaxRequest({
                        url: '/prv/p/getVisibleTasks.action',
                        success : function(response, opts) {
                            var resp = Ext.decode(response.responseText);
                            if (resp && tabs) {
                                updateTaskTabCount(resp.totalCount, resp.colorCode);
                            }
                        },
                        params: {
                            hideCompleted : true,
                            showAssignedTasksOnly : true
                        }
            });
        }
        
        function updateTaskTabCount(taskCount, colorCode) {
            if (tabs) {
                var color = colorCode;
                if (taskCount <= 0) { 
                    color = 'black';
                }
                var title = 'Tasks';
                if (taskCount < 10) {
                    title = title + '&nbsp';
                } else if (taskCount < 100) {
                    title = title + '&nbsp&nbsp';
                } else if (taskCount < 1000) {
                    title = title + '&nbsp&nbsp&nbsp';
                } else if (taskCount < 10000) {
                    title = title + '&nbsp&nbsp&nbsp&nbsp';
                }
                taskTabTitle = title + '<div  class = "noti_bubble" style="background-color:'+color+'; ">'+taskCount +'</div>';
                tabs.getComponent('taskPanelTabId').setTitle(taskTabTitle);
            }
        }

    </script>

</head>

<div id="inboxScreenDiv">
    <div id="tabPanel"></div>

    <div id="boardPanelTab" class="x-hide-display"></div>
    <div id="taskPanelTab" class="x-hide-display"></div>
    <div id="filterPanelTab" class="x-hide-display"></div>
    <div id="searchPanelTab" class="x-hide-display"></div>
    <div id="reportPanelTab" class="x-hide-display"></div>
    <div id="adminPanelTab" class="x-hide-display"></div>
    <div id="xmlUploadTab" class="x-hide-display"></div>
    <div id="gridHolder"></div>


    <input id="userInsurerId" name="userInsurerId" value="<s:property value="AuthenticatedUser.insurer.id"/>" type="hidden"/>
    <input id="userSupplierId" name="userSupplierId" value="<s:property value="AuthenticatedUser.Chorganisation.id"/>" type="hidden"/>
    <input id="userInsurerWorkgroupEnable" name="userInsurerWorkgroupEnable" value="<s:property value="AuthenticatedUser.insurer.workgroupEnable"/>" type="hidden"/>



    <div id="claimRoutedSelectionDlgHolder" class="x-hidden">
        <div id="claimRoutedSelectionPanel">
            <form id="routeClaimForm" action="<%=request.getContextPath()%>/prv/processBatchClaims.action?" class="XXentity-form" method="POST">
                <input name="selectedClaimIds" type="hidden" />
                <table class="selection-form" cellspacing="0" cellpadding="0" border="0">
                    <tr>
                        <th colspan="2"><label>Please select the 'Workgroup' in order to route the claim(s) to the relevant handling team.</label></th>
                    </tr>
                    <tr>
                        <td><label>Workgroup</label></td>
                        <td><div id="claimRoutedSelectionHolder"></div></td>
                    </tr>
                    <tr>
                        <td colspan="2"><div id="routeClaimFormMessageBox" class="action-error-msg"/></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>

    <div id="claimOwnerSelectionDlgHolder" class="x-hidden">
        <div id="claimOwnerSelectionPanel">
                <form id="ownershipClaimForm" name="ownershipClaimForm" action="<%=request.getContextPath()%>/prv/processBatchClaims.action?" class="XXentity-form" method="POST">
                <input name="selectedClaimIds" type="hidden"/>
                <table class="selection-form" cellspacing="0" cellpadding="0" border="0">
                    <tr>
                        <th colspan="2"><label>Please assign the claim(s) to a Claim Owner.</label></th>
                    </tr>
                    <s:if test="AuthenticatedUser.insurer.workgroupEnable">
                        <tr>
                            <td class="pop-claim-ownership-label"><label>Workgroup</label></td>
                            <td class="pop-claim-ownership-column"><div id="claimOwnerWorkgroupDropDownDiv"></div></td>
                        </tr>
                    </s:if>
                    <tr>
                        <td class="pop-claim-ownership-label" style="height:60px;"><label>Claim Owner</label></td>
                        <td class="pop-claim-ownership-column"><div id="claimOwnerClaimHandlerRoleUserDropDownDiv"></div></td>
                    </tr>
                    <tr>
                        <td colspan="2"><div id="ownershipClaimFormMessageBox" class="action-error-msg"/></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
            
    <div id="claimOwnerSelectionDlgHolder1" class="x-hidden">
        <div id="claimOwnerSelectionPanel1">
                <form id="ownershipClaimForm1" name="ownershipClaimForm" action="<%=request.getContextPath()%>/prv/processBatchClaims.action?" class="XXentity-form" method="POST">
                <input name="selectedClaimIds" type="hidden"/>
                <table class="selection-form" cellspacing="0" cellpadding="0" border="0">
                    <tr>
                        <th colspan="2"><label>Please assign the claim(s) to a Claim Owner.</label></th>
                    </tr>
                    <tr>
                        <td class="pop-claim-ownership-label" style="height:60px;"><label>Claim Owner</label></td>
                        <td class="pop-claim-ownership-column"><div id="claimOwnerClaimHandlerRoleUserDropDownDiv1"></div></td>
                    </tr>
                    <tr>
                        <td colspan="2"><div id="ownershipClaimFormMessageBox1" class="action-error-msg"/></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>

    <div id="supplierClaimOwnerSelectionDlgHolder" class="x-hidden">
        <div id="supplierClaimOwnerSelectionPanel">
            <form id="supplierOwnershipClaimForm" name="supplierOwnershipClaimForm" action="<%=request.getContextPath()%>/prv/processBatchClaims.action?name=assignSupplierOwner" class="XXentity-form" method="POST">
                <input name="selectedClaimIds" type="hidden"/>
                <table class="selection-form" cellspacing="0" cellpadding="0" border="0">
                    <tr>
                        <th colspan="2"><label>Please assign the claim(s) to a Claim Owner.</label></th>
                    </tr>
                    <tr>
                        <td class="pop-claim-ownership-label" style="height:60px;"><label>Claim Owner</label></td>
                        <td class="pop-claim-ownership-column"><div id="supplierClaimOwnerDropDownDiv"></div></td>
                    </tr>
                    <tr>
                        <td colspan="2"><div id="supplierOwnershipClaimFormMessageBox" class="action-error-msg"/></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>

    <div id="couSelectionDlgHolder" class="x-hidden">
        <div id="couSelectionPanel">
            <form id="ClaimOwnershipUpdateForm" action="<%=request.getContextPath()%>/prv/p/doClaimOwnershipUpdateAction.action" class="XXentity-form" method="POST">
                <input name="selectedClaimIds" type="hidden" />
                <table class="selection-form" cellspacing="0" cellpadding="0" border="0" width="100%">
                    <tr>
                        <th colspan="2"><label>Please update the claim(s) with a Workgroup and Claim Owner.</label></th>
                    </tr>
                    <s:if test="AuthenticatedUser.insurer.workgroupEnable">
                        <tr>
                            <td class="pop-claim-ownership-label"><label>Workgroup</label></td>
                            <td class="pop-claim-ownership-column"><div id="couWorkgroupDropDownDiv"></div></td>
                        </tr>
                    </s:if>
                    <tr>
                        <td class="pop-claim-ownership-label" style="height:60px;"><label>Claim Owner</label></td>
                        <td class="pop-claim-ownership-column"><div id="couClaimHandlerRoleUserDropDownDiv"></div></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
    <div id="xmlClaimsStatusGridDiv" class="x-hide-display">
        <div id="xmlClaimsStatusGrid"></div>
    </div>
</div>
