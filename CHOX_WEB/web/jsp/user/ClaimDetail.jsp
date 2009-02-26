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
        <script src="<%= request.getContextPath()%>/scripts/general.js" type="text/javascript"></script> 
        
<script type="text/javascript">
    
    
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

    // COMMENT
    var commentsJsonReader;
    var commentsDataStore;
    var commentsGrid;  

    // PAYMENT PACK
    var paymentPackJsonReader;
    var paymentPackDataStore;
    var paymentPackGrid; 

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
        $('.entity-form').ajaxForm(globalEntityFormOptions);
        pingServer();
        
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
        responseText = responseText.trim();
        elementToBlock.unblock();
        var output = "Your changes have been saved.";
        var outputDiv =  elementToBlock.find('div.chox-form-submit-result');

        if(responseText != "" && responseText != "1"){

            if(responseText.substring(0,4) == 'new:')
            {
                var newObjectId =  parseInt(responseText.substring(4,responseText.length));
                var hvObjectId = elementToBlock.find("input[name='objectId']");
                hvObjectId.val(newObjectId);                   
            }   
            else
            {
                output = "There was an error: " + responseText.substring(0,40);
                outputDiv.addClass("submit-error");
            }
        }

        outputDiv.text(output);
        hasFormUnderSubmission = false;
    }    

    function onSubmitError(XMLHttpRequest, textStatus, errorThrown) {
        elementToBlock.unblock();
        var outputDiv =  elementToBlock.find('div.chox-form-submit-result');
        outputDiv.addClass("submit-error");
        outputDiv.text(textStatus  + ":" + errorThrown);   
        hasFormUnderSubmission = false;                
    }
    
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
                    contentEl:'comments', 
                    title: 'Notes', 
                    disabled: commentsDisabled,
                    listeners: {activate : loadComments}
                }
            ]
        }); 
        
        /*
         * CREATED BY: CALRSON HOO
         * CREATED DATE: 6 DEC 2008
         * DESC: PAYMENT PACK / ATTACHMENT
         **/
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
                    {header: "File Name", width: 300, dataIndex: 'fileName', sortable: false, resizable: true},
                    {header: "Category", width: 200, dataIndex: 'category', sortable: false, resizable: true},
                    {header: "Description", width: 270, dataIndex: 'remarks', sortable: false, resizable: true},
                    {header: "", width: 60, dataIndex: 'delete', sortable: false, resizable: false, renderer:function(value,p,r){
                    return "<a href='#attachmentlisting'>" + value + "</a>"}}
                ],
                renderTo:'paymentPackGrid',
                width:960,
                autoHeight:true,
                enableHdMenu:false
            });
            loadAttachments();
        }
        
        //return confirm('Are you sure you want to reject this invoice?')
        //return '<a href="doDeleteFile.action?fileId=' + r.data['id'] + '">' + value + '</a>'
        function deleteAttachment(a){
            var deleteAtt = confirm("Are you sure you want to delete this attachment?")
            if(deleteAtt){
                paymentPackLoaded = false;
                 $.ajax({
                   url: "doDeleteFile.action?fileId="+a,
                   success: loadAttachments
                 });
            }
            doCleanResult();
        }
                
        function loadAttachment(grid, rowIndex, columnIndex, e){
            var attachment = paymentPackGrid.getStore().getAt(rowIndex);  // Get the Record
            var fileId = attachment.get("id");
            if(columnIndex!=3){
                window.location= "doExportFile.action?fileId=" + fileId;
            }else{
                deleteAttachment(fileId);
            }
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
                width:460,
                autoHeight:true,
                enableHdMenu:false
            });
           
            loadEcds();
        }
        
        function loadHireMonitor(grid, rowIndex, columnIndex, e){
            $("#hireMonitoringDetails").block({message: $("#hireMonitorTemplate"), css: { backgroundColor: '#FFFFFF', height:'160', width:'400px', top:'10px', padding:'10px', overflow: 'auto'}  });
            var hiremonitoringECD = ecdGrid.getStore().getAt(rowIndex);
            var supportingNoteText = hiremonitoringECD.get("supportingNote");
            $("#hireMonitorMessage").text(supportingNoteText);
            //setTimeout($("#hireMonitoringDetails").unblock(), 5000);
            setTimeout(function(){ $("#hireMonitoringDetails").unblock(); }, 10000);

        }        
        
        /*
         * DESC: COMMENT
         **/
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
            });

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
                     {header: "Created", width: 200, dataIndex: 'createdDate', sortable: false, resizable: true}, 
                    {header: "Created By", width: 200, dataIndex: 'createdBy', sortable: false, resizable: true},                   
                    {header: "Message", width: 500, dataIndex: 'comment', sortable: false, resizable: true}
                ],
                renderTo:'commentsGrid',
                width:960,
                autoHeight:true,
                enableHdMenu:false
            });
        }
        
        function loadComment(grid, rowIndex, columnIndex, e){
            $("#comments").block({message: $("#commentTemplate"), css: { backgroundColor: '#FFFFFF', height:'160', width:'400px', padding:'10px', overflow: 'auto'}  });
            var comment = commentsGrid.getStore().getAt(rowIndex);
            var commentText = comment.get("comment");
            $("#commentMessage").text(commentText);
            setTimeout(function(){ $("#comments").unblock(); }, 10000);
        }

        /*
         * DESC: HISTORY
         **/        
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

            var historyData = new Ext.data.Store({
                proxy: new Ext.data.HttpProxy
                ({url: 'user/getHistories.action',method:'GET'}),
                reader:historyJsonReader        
            });            

            var grid = new Ext.grid.GridPanel({
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
                    claimId : <s:property value="id" />
                }
            });    
        }

    
    });         

    // LOAD COMMENT
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
    
    // LOAD PAYMENT PACK / ATTACHMENT
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
                resetAttachmentForm();
            }       
        }
    }   
    
    
    function resetAttachmentForm(){

        $("#fAttachment").each(function(){
	        this.reset();
	});
    }
    
    function registeAction(val)
    {
        $("#actionName").val(val);
    }
    
            
    //$(document).everyTime(4000, function() {
    //    pingServer();
    //});
    
    var t;
    
    function random_number() {
        var min = 10000000;
        var max = 99999999;
        return (Math.round((max-min) * Math.random() + min));
    }
    
     function pingServer()
    {
        $.getJSON('activityMonitoringAction.action?claimId=<s:property value="id" />' + "&token=" + random_number(),
        function(data){
            
            if(data.results.length > 0)
            {

                $("#userViewingThisClaim").empty();
                $.each(data.results, function(i,result){
                    if(i > 0)
                    {
                        $("#userViewingThisClaim").append(', ');
                    }
                    $("#userViewingThisClaim").append(result);
                });
                $("#userViewingThisClaimDiv").show();

            }
            else
            {
                $("#userViewingThisClaimDiv").hide();
            }
            
        });
        
        t=setTimeout("pingServer()",4000);
    }
   
   function updateAnomalies(a){
        document.location = "doUpdateAnomalies.action?id="+a;
    }

   function closeClaimStatus(a){
       
       if(!confirm('Are you sure you want to close this claim?')){
                return false;
        }else{
            document.location = "doUpdateClaimStatus.action?id="+a;
        }
        return true;
   }

   
      function reopenClaimStatus(a){
       
       if(!confirm('Are you sure you want to re-open this claim?')){
                return false;
        }else{
            document.location = "doReopenClaimStatus.action?id="+a;
        }
        return true;
   }
   
   function checkAndConfirClaimNumberDuplication(sClaimNumber,sClaimId,form)
    {
        if(sClaimNumber && sClaimNumber != null)
        {
            $.get("checkIsClaimNumberDuplicated.action", { claimNumber: sClaimNumber, claimId: sClaimId },
            function(data){
                if(data.trim()== "yes"){
                    if(confirm("The claim number you have supplied is already associated with another claim(s). Do you wish to continue?"))
                    {
                        form.submit();
                    }
                }
                else{
                    form.submit();
                }                            
            });
                    
        }
        else
        {
            form.submit();
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
                                    <a href="<s:url action="openUserAccount" />">Settings</a>&nbsp;|&nbsp;
                                    <s:if test="isCHO">
                                    <a href='<s:url action="uploadClaims"/>'>XML Uploads</a>&nbsp;|&nbsp;
                                    </s:if>
                                    
                                     <s:if test="isCHO">
                                        <a href="javascript:openFile('<%= request.getContextPath()%>','ChoHelp');">Help</a>
                                    </s:if>
                                    <s:else>
                                        <a href="javascript:openFile('<%= request.getContextPath()%>','InsHelp');">Help</a>
                                    </s:else>&nbsp;|&nbsp;
                                    <a href="javascript:openFile('<%= request.getContextPath()%>','Support');">Support</a>&nbsp;|&nbsp; 
                                    <a href="javascript:onOpenAbout();">About CHOX</a>&nbsp;|&nbsp;
                                    <b><s:property value="CurrentUserDesc" /></b>&nbsp;&nbsp;<a href="<%=request.getContextPath()%>/j_acegi_logout">( Log Off )</a>
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
                                    Created On</label><label class="chox-claim-header-text"><s:date name="createdDate" format="dd MMM yyyy kk:mm"  /></label></td>
                                </tr>
                                <tr>
                                    <td><label class="chox-claim-header-label">Customer</label><label class="chox-claim-header-text"><span id="status"><s:property value="customer.formattedName" /></span></label></td>
                                    <td><label class="chox-claim-header-label">Current Status</label><label class="chox-claim-header-text"><span id="status"><s:property value="status" /></span></label><!--span id="statusTip"><img src="img/tip.gif" style="fixed:relative;top:-50" /></span--></td>
                                    <td><label class="chox-claim-header-label">Policy Holder Contact Date</label><label class="chox-claim-header-text"><span id="status"><s:date name="policyHolderContactDate" format="dd MMM yyyy kk:mm"  /></span></label></td>
                                </tr>
                                <tr>
                                    <td><label class="chox-claim-header-label">Percentage Liability Accepted</label><label class="chox-claim-header-text"><span id="status"><s:property value="percentageLiabilityAccepted" />%</span></label></td>
                                    <td><label class="chox-claim-header-label">Indemnity</label><label class="chox-claim-header-text"><span id="status">£<s:property value="indemnityAmount" /></span></label></td>
                                    
                                    <s:if test="!isCHO">
                                        <td><label class="chox-claim-header-label">ECD Anomaly?</label><label class="chox-claim-header-text"><span id="status">
                                        <s:if test="isAnomalies">
                                            <s:property value="isAnomaliesDesc" /> ( <a href="javascript:updateAnomalies('<s:property value="id" />');">Remove from hire anomalies</a> )
                                        </s:if>
                                        <s:else>
                                            <s:property value="isAnomaliesDesc" />
                                        </s:else>
                                        </span></label></td>
                                    </s:if>
                                    
                                </tr>
<s:if test="!isCHO && isFnolReviewed && isFnolPanelVisible">
<tr>
<td colspan="3">
    <div class="status-info">This claim has been reviewed by an FNOL Handler, please review notes that may have been added before proceeding.</div>
</td>
</tr><br/>
</s:if>

<s:if test="!isClaimClosed && isCHO">
<tr>
    <td colspan="3" align="right"><input value="Close Claim" type="button" onclick="javascript:return closeClaimStatus('<s:property value="id" />');"/></td>
</tr>

</s:if>
<s:elseif test="isClaimClosed && isCHO">
<tr>
    <td colspan="3" align="right"><input value="Re-Open Claim" type="button" onclick="javascript:return reopenClaimStatus('<s:property value="id" />');"/></td>
</tr>
</s:elseif>


                            </table>
                        </fieldset>
                    
<tr>
    <td style="padding-right:136px;">
        <a href="<s:url action="inbox"/>">« Back to Search Results</a>
    </td>
    <td>&nbsp;</td>
    <td align="left">
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
            onchange="javascript:extraActionChange();">
    </s:select>
    </s:if>  
    </td>
</tr>       

                    </div>
                </div>
                
                 <s:if test="isClaimNumberDuplicated">
                        <s:action name="getDuplicatedClaimAlert" executeResult="true">
                            <s:param name="claimId"><s:property value="id" /></s:param> 
                            <s:param name="claimNumber"><s:property value="claimNumber" /></s:param>   
                        </s:action>
                </s:if>

                <div id="userViewingThisClaimDiv" class="status-warning" style="display:none;">                      
                        This claim is currently being viewed and / or modified by the following user(s) : <span id="userViewingThisClaim"></span>
                </div>
                
<script language="JavaScript">
    
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
                    <s:action name="getActionPanel" executeResult="true" />
                    <div class="action-message"><s:property value="actionResult" /></div>
                </div>
                  
                <s:if test="isShowPenaltyChargeAlert">
                    <div class="chox-claim-header x-panel-bwrap chox-form-container">   
                        <s:action name="getAlertPanel" executeResult="true" />                    
                    </div>
                </s:if>
                
               
                
<s:if test="!isCHO">
    
<script language="JavaScript">
                                
    $(document).ready(function() {
        $("#extraAction").val("");
        
    });

   function doShowHideExtraAction(a, b){
       if(b){
            $("#"+a).css("display:", "block");  
            $("#"+a).slideDown();
       }else{
            $("#"+a).slideUp();
            $("#"+a).css("display:", "none"); 
            $("#extraAction").val("");
            
       }
   }
   
   function extraActionChange(){
       
       var selectedAction = $("#extraAction").val();
       $(".extraActionClass").slideUp();
       $(".extraActionClass").css("display:", "none");  
       
       if(selectedAction!=null && selectedAction!=""){
           doShowHideExtraAction(selectedAction, 1);
       }
   }
   
</script>

<div id="updateInsurerClaimNumber" class="extraActionClass" style="display: none;">
    
    <table width="100%">
    <tr><td>
    <div class="chox-claim-header x-panel-bwrap chox-form-container">
        <s:action name="getUpdateInsurerClaimNumberAction" executeResult="true"></s:action>
        <div class="action-message"><s:property value="actionResult" /></div>
    </div>
    </td></tr>
    </table>
</div>

</s:if>

                <div id="tabContainer">
                    <div id="claimDetails">         

<s:if test="tabAccessibility.claimDetailTabAccessibility != 0">    
                       
                        <div class="x-panel-bwrap chox-form-container">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr valign="top">
                                    <td class="chox-form-left-col">
                                                                         
                                        <s:action name="getCustomer" executeResult="true">
                                            <s:param name="objectId"><s:property value="customerId" /></s:param> 
                                            <s:param name="claimStatus"><s:property value="status" /></s:param>         
                                        </s:action>  
                                       
                                        <s:action name="getInjury" executeResult="true">
                                        <s:param name="objectId"><s:property value="injuryId" /></s:param>
                                        <s:param name="incidentId"><s:property value="incidentId" /></s:param>
                                        <s:param name="claimStatus"><s:property value="status" /></s:param>
                                        </s:action>
                                        
                                        <s:action name="getSolicitor" executeResult="true">
                                            <s:param name="objectId"><s:property value="injurySolicitorId" /></s:param>
                                            <s:param name="incidentId"><s:property value="incidentId" /></s:param>   
                                            <s:param name="claimStatus"><s:property value="status" /></s:param>
                                        </s:action> 
                                        
                                        <s:action name="getCustomerVehicleDamage" executeResult="true">
                                            <s:param name="objectId"><s:property value="customerId" /></s:param>
                                            <s:param name="claimStatus"><s:property value="status" /></s:param>        
                                        </s:action>                                        
                                        
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

<s:if test="!isCHO">
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
        </div>
    </fieldset>
</s:else>


                                        <s:action name="getIncident" executeResult="true">
                                            <s:param name="objectId"><s:property value="incidentId" /></s:param>
                                            <s:param name="claimId"><s:property value="id" /></s:param> 
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>
                                        
                                        <s:action name="getThirdParty" executeResult="true">
                                            <s:param name="objectId"><s:property value="thirdPartyId" /></s:param> 
                                            <s:param name="claimId"><s:property value="id" /></s:param> 
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>

                                        <s:action name="getWitness" executeResult="true">
                                            <s:param name="incidentId"><s:property value="incidentId" /></s:param>
                                            <s:param name="objectId"><s:property value="witnessId" /></s:param>
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
                                            <s:param name="customerId"><s:property value="customerId" /></s:param> 
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
                        
<!-- template for modal comment-->
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
                                        <s:action name="getInvoice" executeResult="true">
                                            <s:param name="objectId"><s:property value="invoiceId" /></s:param> 
                                            <s:param name="claimId"><s:property value="id" /></s:param>
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>
                                        
                                        
                                         <s:action name="getVehicleHire" executeResult="true">
                                            <s:param name="objectId"><s:property value="vehicleHireId" /></s:param> 
                                            <s:param name="claimId"><s:property value="id" /></s:param>
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>

                                    </td>
                                    <td>
                                        
                                        
                                        <s:action name="getExtra" executeResult="true">
                                            <s:param name="objectId"><s:property value="invoiceId" /></s:param> 
                                            <s:param name="claimId"><s:property value="id" /></s:param>
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>
                                        
                                        <s:action name="getEngineerReport" executeResult="true">
                                            <s:param name="objectId"><s:property value="engineerReportId" /></s:param> 
                                            <s:param name="claimId"><s:property value="id" /></s:param>
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
                            <script language="JavaScript">
                                
                                $(document).ready(function() { 
                                        
                                    var options = { 
                                        success: showResponseAtt  // post-submit callback 
                                    }; 
                                    $('#fAttachment').ajaxForm(options); 
                                });
                                
                                function showResponseAtt(responseText, statusText){ 
                                    paymentPackLoaded = false;
                                    loadAttachments();
                                    $("#AttMsgBox").text("File has been uploaded successfully");
                                } 
                                
                                function fileValidation(){
                                    
                                    var uploadFile = document.Attform.attachmentFile.value;
                                    if(uploadFile==""){
                                        alert("Please select a file to upload");
                                        return false;
                                    }
                                    
                                    if((uploadFile.lastIndexOf("."))>0){
                                        var filename = uploadFile.substr(uploadFile.lastIndexOf('\\')+1, uploadFile.length);
                                    }
                                    
                                    document.Attform.uploadFileName.value = filename;
                                    return true;
                                }
                            </script>
                                
                            <div class="attachments  x-panel-bwrap chox-form-container">
                                <form id="fAttachment" action="user/createNewAttachment.action" method="POST" enctype="multipart/form-data" name="Attform">
                                    <input type="hidden" name="claimId" value='<s:property value="id" />'>
                                    <input type="hidden" name="uploadFileName">
                                    <fieldset class="x-fieldset">
                                        <legend>Add a new Attachment</legend>
                                        <table class="chox-form-item">
                                            <tr>
                                                <td width="30%" align="right"><label class="std-label-ro">File</label></td>
                                                <td>
                                                    <s:file id="fileUploader" name ="attachmentFile" label ="Attachment" size="40"/>   
                                                </td>
                                            </tr>
                                            <tr>
                                                <td align="right"><label class="std-label-ro">Attachment Type</label></td>
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
                                                <td align="right" valign="top"><label class="std-label-ro">Description</label></td>
                                                <td>
                                                    <s:textarea rows="3" cols="30" name="remark" label="Remark:"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>&nbsp;</td>
                                                <td>
                                                    <input type="submit" id="bAddAttachment" value="Add File" onclick="return fileValidation()"/>
                                                </td>
                                            </tr>
                                            <tr><td colspan="2"><div class="chox-form-submit-result" id="AttMsgBox"></div></td>
                                        </table>
                                    </fieldset>
                                </form>
                            </div>
                            <a name="attachmentlisting"></a>
                            <div id="paymentPackGrid"></div>   
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
            
                var optionsComment = { 
                    success: showResponse  // post-submit callback 
                }; 
                
                // bind form using 'ajaxForm' 
                $('#fComments').ajaxForm(optionsComment); 

                //bind close comment button behaviour
                $("#commentModalClose").click(function(){ $("#comments").unblock();});    

        });

        function showResponse(responseText, statusText)  { 
            commentsLoaded = false;
            
            $("#fComments").each(function(){
                this.reset();
            });
                
            loadComments();
        }    
     
        function commentFormValidation(){
            var inp = $("#commentBox").val();
            if(inp==null || inp==""){
                $("#CmErrMsgBox").show();
                $("#CmErrMsgBox").text("Note blank - Please enter text in the Note field and then click on 'Add Note'");
                return false;
            }else{
                $("#CmErrMsgBox").hide();
            }
            return true;
        }
        
    </script>
   
    <div class="comments  x-panel-bwrap chox-form-container">

<s:if test="!isClaimClosed"> 
   
        <form id="fComments" action="user/createNewComment.action" method="post">
            <input type="hidden" name="claimId" value='<s:property value="id" />'>
            <fieldset class="x-fieldset">
                <legend>Add a new note</legend>
                 <div class="chox-form-item">
                    <s:textarea id="commentBox" cols="70" rows="4" id="commentBox" name="comment" />
                </div>
                 <s:if test="!isCHO"> 
                <div class="chox-form-item">
                    <s:checkbox name="isPublic"/>
                    <label class="std-label-ro">Visible to CHO?</label>                    
                </div>
                </s:if>
                <s:else><input name="isPublic" type="hidden" value="true"/></s:else>
                <input type="submit" id="bAddComment" value="Add Note" onclick="javascript:return commentFormValidation();"/>
                
                
            </fieldset>
        </form>
</s:if>

        <div class="errorBox" id="CmErrMsgBox" style="color:red;font-weight: bold;font-size: 10px;"></div>        
    </div>

    <div style="display:none" id="commentTemplate">
        <input type="button" value="Close" id="commentModalClose">
        <br/>
        <div id="commentMessage"></div>
    </div>                      
        
    <div id="commentsGrid"></div>   
    
</s:if>                         
                    </div>   
                </div>
            </div>
<div class="footerText">©2009 Sherwood Compliance Services Ltd | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','Copyright');" class="footerText">Copyright</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','PrivacyPolicy');" class="footerText">Privacy Policy</a> | <a href="javascript:openChoxPolicyPage('<%= request.getContextPath()%>','TermsOfService');" class="footerText">Terms of Service</a></div>
        </div>
    </body>
</html>
