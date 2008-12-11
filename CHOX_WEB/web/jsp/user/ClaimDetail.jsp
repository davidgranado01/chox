<%-- 
    Document   : claimDetail
    Created on : 23-Nov-2008, 12:04:54
    Author     : Dermot
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>IDAS-CHOX</title>
        
        
        <link href="<%= request.getContextPath()%>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/>        
        <link href="<%= request.getContextPath()%>/css/ext-all.css" rel="stylesheet" type="text/css" media="all"/>
        
        
        <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery-1.2.6.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.form.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.blockUI.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/ext-jquery-adapter.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.metadata.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.validate.min.js"></script>            
        
        
        <script src="<%= request.getContextPath()%>/scripts/ext-base.js" type="text/javascript"></script>
        <script src="<%= request.getContextPath()%>/scripts/ext-all.js" type="text/javascript"></script> 
        <script src="<%= request.getContextPath()%>/scripts/Application.js" type="text/javascript"></script> 
    
        
        
        <script type="text/javascript">
            
            var newwindow;
            function openFile(url,name)
            {
                newwindow=window.open(url,name);
                if (window.focus) {newwindow.focus()}
            }


            var claimDetailTabAccessibility = <s:property value="tabAccessibility.claimDetailTabAccessibility" />;
            var invoiceDetailTabAccessibility = <s:property value="tabAccessibility.invoiceDetailTabAccessibility" />;
            var hireMonitoringTabAccessibility = <s:property value="tabAccessibility.hireMonitoringTabAccessibility" />;
            var historyTabAccessibility = <s:property value="tabAccessibility.historyTabAccessibility" />;
            var notesTabAccessibility = <s:property value="tabAccessibility.notesTabAccessibility" />;
            var paymentPackTabAccessibility = <s:property value="tabAccessibility.paymentPackTabAccessibility" />;
    
            var hasFormUnderSubmission = false;
            var elementToBlock;


            var claimDetailsDisabled = claimDetailTabAccessibility == 0;
            var hireMonitoringDetailsDisabled = hireMonitoringTabAccessibility  == 0;
            var invoiceDetailsDisabled = invoiceDetailTabAccessibility == 0;
            var paymentPackDisabled = paymentPackTabAccessibility == 0;
            var historyDetailsDisabled = historyTabAccessibility == 0;
            var commentsDisabled = notesTabAccessibility == 0;
         

            var commentsJsonReader;
            var commentsDataStore;
            var commentsGrid;  

            var paymentPackJsonReader;
            var paymentPackDataStore;
            var paymentPackGrid; 


            // override these in your code to change the default behavior and style 
            $.blockUI.defaults = { 
                // message displayed when blocking (use null for no message) 
                message:  '<h1 class="block">Please wait...</h1>', 
         
                // styles for the message when blocking; if you wish to disable 
                // these and use an external stylesheet then do this in your code: 
                // $.blockUI.defaults.css = {}; 
                css: {  
                    padding:        0, 
                    margin:         0, 
                    width:          '40%',  
                    top:            '40%',  
                    left:           '35%',  
                    textAlign:      'center',  
                    color:          '#000',  
                    border:         '3px solid #aaa', 
                    backgroundColor:'#fff', 
                    cursor:         'wait' 
                }, 
         
                // styles for the overlay 
                overlayCSS:  {  
                    backgroundColor:'#6c8cbe',  
                    opacity:        '0.5'  
                }, 
         
                // z-index for the blocking overlay 
                baseZ: 1000, 
         
                // set these to true to have the message automatically centered 
                centerX: true, // <-- only effects element blocking (page block controlled via css above) 
                centerY: true, 
         
                // allow body element to be stetched in ie6; this makes blocking look better 
                // on "short" pages.  disable if you wish to prevent changes to the body height 
                allowBodyStretch: true, 
         
                // be default blockUI will supress tab navigation from leaving blocking content; 
                constrainTabKey: true, 
         
                // fadeOut time in millis; set to 0 to disable fadeout on unblock 
                fadeOut:  0, 
         
                // suppresses the use of overlay styles on FF/Linux (due to significant performance issues with opacity) 
                applyPlatformOpacityRules: true 
            }; 
            
            
            
            var globalEntityFormOptions = { 
                    beforeSubmit:  onBeforeSubmit,  // pre-submit callback 
                    success:       onSubmitResponseReceived,  // post-submit callback 
                    timeout: 3000,
                    error: onSubmitError
                }; 

    
            $(document).ready(function(){
    
                var fsets =  $('legend');
                fsets.click(function(){ $(this).next().toggle();});
                fsets.mouseover(function(){ $(this).css("cursor","pointer"); }); 
                fsets.mouseout(function(){ $(this).css("cursor","normal");});  

                /*
                var options = { 
                    beforeSubmit:  onBeforeSubmit,  // pre-submit callback 
                    success:       onSubmitResponseReceived,  // post-submit callback 
                    timeout: 3000,
                    error: onSubmitError
                }; 
                */
                $('.entity-form').ajaxForm(globalEntityFormOptions); //wrap all <form> elements with ajax submission config   
            });

            function onBeforeSubmit(formData, jqForm, options) { 
                if(!hasFormUnderSubmission){
                    if(elementToBlock != undefined){
                        outputDiv = elementToBlock.find('div.chox-form-submit-result');
                        outputDiv.text("");
                        outputDiv.removeClass("submit-error");
                    }
                    hasFormUnderSubmission = true;
                    elementToBlock = jqForm.find('div.form-container');
                    elementToBlock.block({ message: "Please wait.." });
            
                    var queryString = $.param(formData); 
                    return true; 
                }else alert("Please wait until other save operations have completed");
            } 

            // post-submit callback 
            function onSubmitResponseReceived(responseText, statusText)  {         
                elementToBlock.unblock();
                var output = "Your changes have been saved.";
                var outputDiv =  elementToBlock.find('div.chox-form-submit-result');
                if(responseText.trim() != "" && responseText.trim() != "1"){
                    output = "There was an error: " + responseText.substring(0,40);
                    outputDiv.addClass("submit-error");
                }
                
                
                
                outputDiv.text(output);
                hasFormUnderSubmission = false;  
        
                //alert('status: ' + statusText + '\n\nresponseText: \n' + responseText + 
                //     '\n\nThe output div should have already been updated with the responseText.'); 
            }    

            function onSubmitError(XMLHttpRequest, textStatus, errorThrown) {
                elementToBlock.unblock();
                var outputDiv =  elementToBlock.find('div.chox-form-submit-result');
                outputDiv.addClass("submit-error");
                outputDiv.text(textStatus  + ":" + errorThrown);   
                hasFormUnderSubmission = false;                
            }
         
         
         
         
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
                    disabled: claimDetailsDisabled
                },
                {
                    contentEl:'hireMonitoringDetails', 
                    title: 'Hire Monitoring', 
                    disabled: hireMonitoringDetailsDisabled                                      
                },
                {
                    contentEl:'invoiceDetails', 
                    title: 'Invoice Details', 
                    disabled: invoiceDetailsDisabled
                },
                {
                    contentEl:'paymentPack', 
                    title: 'Attachments', 
                    disabled: paymentPackDisabled 
                },
                {
                    contentEl:'historyDetails', 
                    title: 'History', disabled: historyDetailsDisabled 
                },            
                {
                    contentEl:'comments', 
                    title: 'Notes', 
                    disabled: commentsDisabled,
                    listeners: {activate : loadComments}
                }            
            ]
        }); 
        
        
        
        
        // ADDED BY CARLSON @ 2008-12-02
        if(!paymentPackDisabled){
            
            paymentPackJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',   
                root: 'results', 
                fields:
                [
                    {name:'id'},
                    {name:'fileName'},
                    {name:'category'},                     
                    {name:'remarks' },
                    {name:'delete' }
                ]
            });

            paymentPackDataStore = new Ext.data.Store({
                proxy: new Ext.data.HttpProxy
                ({url: 'user/getAttachments.action',method:'GET'}),
                reader:paymentPackJsonReader        
            });

            paymentPackGrid = new Ext.grid.GridPanel({
                listeners:  {cellclick:loadAttachment },
                store: paymentPackDataStore,
                loadMask: true,
                columns: [
                    {header: "File Id", width: 60, dataIndex: 'id', sortable: false, resizable: false},
                    {header: "File Name", width: 300, dataIndex: 'fileName', sortable: false, resizable: false},
                    {header: "Category", width: 200, dataIndex: 'category', sortable: false, resizable: false},
                    {header: "Description", width: 270, dataIndex: 'remarks', sortable: false, resizable: false},
                    {header: "", width: 60, dataIndex: 'delete', sortable: false, resizable: false, renderer:function(value,p,r){
                return '<a href="doDeleteFile.action?fileId=' + r.data['id'] + '">' + value + '</a>'}}
                ],
                renderTo:'paymentPackGrid',
                width:960,
                autoHeight:true,
                enableHdMenu:false
            });
            
            loadAttachments();
        }
        
        function loadAttachment(grid, rowIndex, columnIndex, e){
            var attachment = paymentPackGrid.getStore().getAt(rowIndex);  // Get the Record
            var fileId = attachment.get("id");
            if(columnIndex!=4){
                var popwin = window.open("doExportFile.action?fileId="+fileId, "Attachment", "WIDTH=575,HEIGHT=500,RESIZABLE=No,SCROLLBARS=YES,TOOLBAR=NO,LEFT=200,TOP=100");
            }else{
                //var popwin = window.open("doDeleteFile.action?fileId="+fileId, "Attachment", "WIDTH=575,HEIGHT=500,RESIZABLE=No,SCROLLBARS=YES,TOOLBAR=NO,LEFT=200,TOP=100");
            }
            loadAttachments();
        }
        
        //Emmanuel 
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
                ({url: 'user/getHireMonitoringEcds.action',method:'GET'}),
                reader:ecdJsonReader       
            });

            ecdGrid = new Ext.grid.GridPanel({
                store: ecdDataStore,
                loadMask: true,
                columns: [
                    {header: "", width: 30, dataIndex: 'sequence', sortable: false, resizable: false},
                    {header: "ECD Date", width: 80, dataIndex: 'ecdDate', sortable: false, resizable: false},
                    {header: "Reason", width: 100, dataIndex: 'reason', sortable: false, resizable: false},
                    {header: "Supporting Note", width: 260, dataIndex: 'supportingNote', sortable: false, resizable: false}
                ],
                renderTo:'ecdGridHolder',
                width:460,
                autoHeight:true,
                enableHdMenu:false
            });
           
            loadEcds();
        }
        
        
        
        
        
        if(!commentsDisabled){
            
            
            commentsJsonReader = new Ext.data.JsonReader({
                totalProperty: 'totalCount',   
                root: 'results', 
                fields:
                [
                    {name:'id'}, 
                    {name:'createdBy'},                     
                    {name:'createdDate'},        
                    {name:'comment'}
                ]
            });// {name:'created', type: 'date', dateFormat: 'd/m/Y'},



            commentsDataStore = new Ext.data.Store({
                proxy: new Ext.data.HttpProxy
                ({url: 'user/getComments.action',method:'GET'}),
                reader:commentsJsonReader        
            });



            commentsGrid = new Ext.grid.GridPanel({
                
                listeners:  {cellclick:loadComment },

                store: commentsDataStore,
                loadMask: true,
                columns: [
                     {header: "Created", width: 110, dataIndex: 'createdDate', sortable: false, resizable: true}, 
                    {header: "Created By", width: 130, dataIndex: 'createdBy', sortable: false, resizable: true},                   
                    {header: "Message", width: 630, dataIndex: 'comment', sortable: false, resizable: true}
                ],
                renderTo:'commentsGrid',
                width:960,
                autoHeight:true,
                enableHdMenu:false
                
                
            });
            

            
        }
            

        function loadComment(grid, rowIndex, columnIndex, e){
            $("#comments").block({message: $("#commentTemplate")  });
            var comment = commentsGrid.getStore().getAt(rowIndex);  // Get the Record
            var commentText = comment.get("comment");
            $("#commentMessage").text(commentText);
        }




        
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
            });// {name:'created', type: 'date', dateFormat: 'd/m/Y'},



            
            
            var historyData = new Ext.data.Store({
                proxy: new Ext.data.HttpProxy
                ({url: 'user/getHistories.action',method:'GET'}),
                reader:historyJsonReader        
            });            


            // create the grid
            var grid = new Ext.grid.GridPanel({
                store: historyData,
                columns: [
                    {header: "Created On", width: 110, dataIndex: 'createdDate', sortable: false, resizable: false},
                    {header: "Created By", width: 110, dataIndex: 'createdBy', sortable: false, resizable: false},
                    {header: "Message Text", width: 650, dataIndex: 'narrative', sortable: false, resizable: false}
                ],
                renderTo:'historyGrid',
                width:960,
                autoHeight:true,
                enableHdMenu:false
            });

            //historyData.load();

            historyData.load(
            {
                params:
                {
                    claimId : <s:property value="id" />
                }
            });              
            
        }


    
    });         

    
            var commentsLoaded = false;
    
            function loadComments(){
            
                if(!commentsDisabled){
                    if(!commentsLoaded){
                        
  
                        commentsDataStore.load(
                        {
                            params:
                            {
                                claimId : <s:property value="id" />
                            }
                        });                          

                        
                        commentsLoaded = true;
                    }       
                }
  
            }   
            var paymentPackLoaded = false;
    
            function loadAttachments(){
                if(!paymentPackDisabled){
                    if(!paymentPackLoaded){
                        paymentPackDataStore.load(
                        {
                            params:
                            {
                                claimId : <s:property value="id" />
                            }
                        });                          
                        paymentPackLoaded = true;
                    }       
                }
            }   


    
        </script>        
        
    </head>    
    
    <body>
        
        <div class="outer">
            
            <div class="inner">        
                
                
                
                
                <div id="chox-menu">
                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                        <tr valign="middle">
                            <td>
                                <img src="<%= request.getContextPath()%>/images/chox_logo_small.jpg" style="display: inline; float: left" />
                            </td>
                            <td width="100%" align="right">
                                <div class="top-menu">
                                    <a href='<s:url action="inbox"/>'>Home</a>&nbsp;|&nbsp;
                                    
                                    <s:if test="isCHO">
                                    <a href='<s:url action="uploadClaims"/>'>XML Uploads</a>&nbsp;|&nbsp;
                                    </s:if>
                                    
                                     <s:if test="isCHO">
                                        <a href="javascript:openFile('<%= request.getContextPath()%>/download/iDAS_CHOX_CHO_UG_1.1-1.pdf','Help');">Help</a>
                                    </s:if>
                                    <s:else>
                                        <a href="javascript:openFile('<%= request.getContextPath()%>/download/iDAS_CHOX_IUG_1.0-1.pdf','Help');">Help</a>
                                    </s:else>&nbsp;|&nbsp;
                                    <a href="#">Support</a>&nbsp;|&nbsp; 
                                    <a href="#">About Chox</a>&nbsp;|&nbsp;
                                    <a href="<%=request.getContextPath()%>/j_acegi_logout">Log Off</a>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
                
                
                
                <div style="width:960px">
                    <div class="chox-claim-header x-panel-bwrap chox-form-container">
                        <fieldset class="x-fieldset">
                            <legend>Claim Summary</legend>
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <!-- UPDATED BY CALRSON @ 2008-12-03 - START !-->
                                    <td>
                                        <label class="chox-claim-header-label">Third Party Insurer</label>
                                    <label class="chox-claim-header-text"><s:property value="thirdParty.insurer.name" /></label></td>
                                    <td>
                                        <!-- UPDATED BY CALRSON @ 2008-12-03 - END !-->
                                        <label class="chox-claim-header-label">
                                    Credit-hire Organsation</label><label class="chox-claim-header-text"><s:property value="chorganisation.name" /></label></td>
                                    <td>
                                        <label class="chox-claim-header-label">
                                    Created By</label><label class="chox-claim-header-text"><s:property value="createdByDesc" /></label></td>
                                </tr>
                                <tr>
                                    <td>
                                        <label class="chox-claim-header-label">
                                    Supplier Reference</label><label class="chox-claim-header-text"><s:property value="choReference" /></label></td>
                                    <td>
                                        <label class="chox-claim-header-label">
                                    Insurer Claim Number</label><label class="chox-claim-header-text"><s:property value="claimNumber" /></label></td>
                                    <td>
                                        <label class="chox-claim-header-label">
                                    Created On</label><label class="chox-claim-header-text"><s:date name="createdDate" format="dd MMM yyyy hh:mm"  /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="chox-claim-header-label">Customer</label><label class="chox-claim-header-text"><span id="status"><s:property value="customer.formattedName" /></span></label></td>
                                    <td><label class="chox-claim-header-label">Current Status</label><label class="chox-claim-header-text"><span id="status"><s:property value="status" /></span></label><!--span id="statusTip"><img src="img/tip.gif" style="fixed:relative;top:-50" /></span--></td>
                                    <td><label class="chox-claim-header-label">Policy Holder Contact Date</label><label class="chox-claim-header-text"><span id="status"><s:date name="policyHolderContactDate" format="dd MMM yyyy hh:mm"  /></span></label></td>
                                </tr>
                                <tr>
                                    <td><label class="chox-claim-header-label">Percentage Liability Accepted</label><label class="chox-claim-header-text"><span id="status"><s:property value="percentageLiabilityAccepted" />%</span></label></td>
                                    <td><label class="chox-claim-header-label">Indemnity</label><label class="chox-claim-header-text"><span id="status">£<s:property value="indemnityAmount" /></span></label></td>
                                    <td>&nbsp;</td>  
                                </tr>                                

                                    
                            </table>
                        </fieldset>
                    </div>
                </div>
                
                <div class="chox-claim-header x-panel-bwrap chox-form-container">    
                    <s:action name="getActionPanel" executeResult="true" />
                    <div class="action-message"><s:property value="actionResult" /></div>
                </div>
                
                
   
                

                <div id="tabContainer">
                    
                    
                    <div id="claimDetails">         

<s:if test="tabAccessibility.claimDetailTabAccessibility != 0">    
                       
                        
                        <div class="x-panel-bwrap chox-form-container">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr valign="top">
                                    <td class="chox-form-left-col">
                                        
                                        
                                        <s:action name="getCustomer" executeResult="true">
                                            <s:param name="objectId"><s:property value="customer.id" /></s:param> 
                                            <s:param name="claimStatus"><s:property value="status" /></s:param>         
                                        </s:action>  
                                       
                                        
                                        <s:action name="getInjury" executeResult="true">
                                        <s:param name="objectId"><s:property value="incident.injury.id" /></s:param>
                                        <s:param name="incidentId"><s:property value="incident.id" /></s:param>
                                        <s:param name="claimStatus"><s:property value="status" /></s:param>
                                        </s:action>
                                        

                                        <s:action name="getSolicitor" executeResult="true">
                                            <s:param name="objectId"><s:property value="incident.injury.solicitor.id" /></s:param>
                                            <s:param name="injuryId"><s:property value="incident.injury.id" /></s:param>   
                                            <s:param name="incidentId"><s:property value="incident.id" /></s:param>   
                                            <s:param name="claimStatus"><s:property value="status" /></s:param>
                                        </s:action> 
                                        <s:action name="getCustomerVehicleDamage" executeResult="true">
                                            <s:param name="objectId"><s:property value="customer.id" /></s:param>
                                            <s:param name="claimStatus"><s:property value="status" /></s:param>        
                                        </s:action>
                                        

                                    </td>
                                    <td>
                                        
<!-- third pary -->

<fieldset class="x-fieldset">
    <legend>Claim Details</legend>
    <div style="display:none" class="form-container">
        <div class="chox-form-item">
            <label class="std-label-ro">Managing Repair</label>
            <label class="std-data-ro"><s:property value="managingRepair" /></label>
        </div>
        <div class="chox-form-item">
            <label class="std-label-ro">GTA 4.1 Notice Date</label>
            <label class="std-data-ro"><s:date name="gtaNoticeDate" format="dd MMM yyyy hh:mm"  /></label>
        </div>        
        <div class="chox-form-item">
            <label class="std-label-ro">Credit Agreement Signed by Insurer Date</label>
            <label class="std-data-ro"><s:date name="creditAgreementDate" format="dd MMM yyyy hh:mm"  /></label>
        </div>    
    </div>
</fieldset>
<s:if test="isCHO">
</s:if>
<s:else>
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
            <div class="chox-form-item">
                <label class="std-label-ro">Engineer's Claim Review Notes</label>
                <label class="std-data-ro"><s:property value="engineerClaimReviewNotes" /></label>
            </div>    
        </div>
    </fieldset>
</s:else>


                                        <s:action name="getIncident" executeResult="true">
                                            <s:param name="objectId"><s:property value="incident.id" /></s:param> 
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>
                                        
                                        
                                        
                                        
                                        <s:action name="getThirdParty" executeResult="true">
                                            <s:param name="objectId"><s:property value="thirdParty.id" /></s:param> 
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>




                                        <s:action name="getWitness" executeResult="true">
                                            <s:param name="incidentId"><s:property value="incident.id" /></s:param>
                                            <s:param name="objectId"><s:property value="incident.witness.id" /></s:param>
                                            <s:param name="claimStatus"><s:property value="status" /></s:param>
                                        </s:action>


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
                                        
          
                                        
                                        
                                        <s:action name="getHireMonitoringDetail" executeResult="true">

                                            <s:param name="claimId"><s:property value="id" /></s:param> 
                                            <s:param name="customerId"><s:property value="customer.id" /></s:param> 
                                            <s:param name="objectId"><s:property value="hireMonitoringDetailId" /></s:param>                                            
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>
                                        
                                        
                                        
                                    </td>
                                    <td>
                                         <s:action name="getHireMonitoringEcd" executeResult="true">
                                            <s:param name="claimId"><s:property value="id" /></s:param>                                         
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>  
                                    </td>
                                </tr>
                            </table>
                        </div>
                        
</s:if>                        
                    </div>
                    <div id="invoiceDetails" class="x-hide-display">    
  
                      <s:if test="tabAccessibility.invoiceDetailTabAccessibility != 0">   

  
                        <div class="x-panel-bwrap chox-form-container">
                            
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr valign="top">
                                    <td class="chox-form-left-col">
                                        
                                          

                                        
                                        <s:action name="getInvoice" executeResult="true">
                                            <s:param name="objectId"><s:property value="invoice.id" /></s:param> 
                                            <s:param name="claimId"><s:property value="id" /></s:param>
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>
                                        
                                        
                                         <s:action name="getVehicleHire" executeResult="true">
                                            <s:param name="objectId"><s:property value="vehicleHire.id" /></s:param> 
                                            <s:param name="claimId"><s:property value="id" /></s:param>
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>
                                        

     
                                        
                                    </td>
                                    <td>
                                        
                                        
                                        <s:action name="getExtra" executeResult="true">
                                            <s:param name="objectId"><s:property value="invoice.id" /></s:param> 
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>
                                        
                                        <s:action name="getEngineerReport" executeResult="true">
                                            <s:param name="objectId"><s:property value="engineerReport.id" /></s:param> 
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>
                                        
                                        

                                    </td>
                                </tr>
                            </table>
                            
                            
                            
                        </div>
                        
</s:if>                         
                    </div>
                    <div id="paymentPack" class="x-hide-display">
<s:if test="tabAccessibility.paymentPackTabAccessibility != 0">
<!-- START - CREATED BY CARL AttachmentAction -->
    <script language="JavaScript">
    $(document).ready(function() { 
            var options = { 
                success: showResponseAtt  // post-submit callback 
            }; 

            // bind form using 'ajaxForm' 
            $('#fAttachment').ajaxForm(options); 
    });
    
    function showResponseAtt(responseText, statusText)  { 
        
        paymentPackLoaded = false;
        loadAttachments();
    } 
    
    function fileValidation(){
        
        var uploadFile = document.form.attachmentFile.value;
        
        if(uploadFile==""){
            alert("No xml document selected for upload");
            return false;
        }
        
        if((uploadFile.lastIndexOf("."))>0){
            var filename = uploadFile.substr(uploadFile.lastIndexOf('\\')+1, uploadFile.length);
        }
        
        document.form.uploadFileName.value = filename;
        return true;
    }
    </script>
<div class="attachments  x-panel-bwrap chox-form-container">
    <form id="fAttachment" action="user/createNewAttachment.action" method="POST" enctype="multipart/form-data" name="form">
        <input type="hidden" name="claimId" value='<s:property value="id" />'>
        <input type="hidden" name="uploadFileName">
        <fieldset class="x-fieldset">
        <legend>Add a new Attachment</legend>
        <table class="chox-form-item">
        <tr>
            <td width="30%"><label class="std-label-ro">File</label></td>
            <td>
            <s:file id="fileUploader" name ="attachmentFile" label ="Attachment" size="40"/>   
            </td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Attachment Type</label></td>
            <td>
            <s:select name="category" 
            list="attachmentCategory" 
            headerKey="" 
            listKey="value" 
            listValue="text" 
            emptyOption="false"></s:select>
            </td>
        </tr>
        <tr>
            <td><label class="std-label-ro">Description</label></td>
            <td>
                <s:textarea rows="6" cols="30" name="remark" label="Remark:"/>
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>
            <input type="submit" id="bAddAttachment" value="Add File" onclick="return fileValidation()"/>
            </td>
        </tr>
        </table> 
        </fieldset>
    </form>
</div>
<div id="paymentPackGrid"></div>
    
<!-- END - CREATED BY CARL -->    
</s:if>
                    </div>
                    <div id="historyDetails" class="x-hide-display">
                        
<s:if test="tabAccessibility.historyTabAccessibility != 0"> 

                        <div id="historyGrid">

                        </div>
</s:if>                        
                    </div>
                    <div id="comments" class="x-hide-display">
<s:if test="tabAccessibility.notesTabAccessibility != 0">  


    <script language="JavaScript">
        
        
    

        $(document).ready(function() { 
                var options = { 
                    success:       showResponse  // post-submit callback 
                }; 

                // bind form using 'ajaxForm' 
                $('#fComments').ajaxForm(options); 

                //bind close comment button behaviour
                $("#commentModalClose").click(function(){ $("#comments").unblock();});    


        });


        function showResponse(responseText, statusText)  { 
            commentsLoaded = false;
            loadComments();
        }    

        
     
        
    </script>
                        
                        
    <div class="comments  x-panel-bwrap chox-form-container">
        <form id="fComments" action="user/createNewComment.action" method="get">
            <input type="hidden" name="claimId" value='<s:property value="id" />'>
            <fieldset class="x-fieldset">
                <legend>Add a new note</legend>
                <textarea id="commentBox" cols="70" rows="4" id="commentBox" name="comment"></textarea><br/><input type="submit" id="bAddComment" value="Add Note" />
            </fieldset>
        </form>
    </div>
                        
  
  
    <!-- template for modal comment-->
    <div style="display:none" id="commentTemplate">
            <div id="commentMessage"></div><br/><br/>
             <input type="button" value="Close" id="commentModalClose">
    </div>                      

                        
    <div id="commentsGrid">
    </div>
                        
                        
                        
                        
                        
</s:if>                         
                    </div>   
                </div>
            </div>
        </div>
    </body>
</html>
