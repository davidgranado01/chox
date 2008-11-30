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
        <title>Claim Detail</title>
        
        <s:action name="rendarActionPanel" namespace="/user" executeResult="true">
            <param name="claimStatus" value="status">
        </s:action>
        
        <link href="<%= request.getContextPath()%>/styles/chox.css" rel="stylesheet" type="text/css" media="all"/>        
        <link href="<%= request.getContextPath()%>/css/ext-all.css" rel="stylesheet" type="text/css" media="all"/>
        
        
        <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery-1.2.6.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.form.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/jquery.blockUI.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/adapter/jquery/ext-jquery-adapter.js"></script>
        
        
        <script src="<%= request.getContextPath()%>/scripts/ext-base.js" type="text/javascript"></script>
        <script src="<%= request.getContextPath()%>/scripts/ext-all.js" type="text/javascript"></script> 
        <script src="<%= request.getContextPath()%>/scripts/Application.js" type="text/javascript"></script> 
        
        
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

            var commentsJsonReader;
            var commentsDataStore;
            var commentsGrid;  



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

    
            $(document).ready(function(){
    
                var fsets =  $('legend');
                fsets.click(function(){ $(this).next().toggle();});
                fsets.mouseover(function(){ $(this).css("cursor","pointer"); }); 
                fsets.mouseout(function(){ $(this).css("cursor","normal");});  

                var options = { 
                    beforeSubmit:  onBeforeSubmit,  // pre-submit callback 
                    success:       onSubmitResponseReceived,  // post-submit callback 
                    timeout: 3000,
                    error: onSubmitError
                };                      
                $('form').ajaxForm(options); //wrap all <form> elements with ajax submission config   
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
                if(responseText != ""){
                    output = responseText;
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
                            title: 'Payment Pack', 
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

        
                var historyData = new Ext.data.Store({
                    // load using HTTP
                    url: 'history.xml',

                    // the return will be XML, so lets set up a reader
                    reader: new Ext.data.XmlReader({
                        // records will have an "Item" tag
                        record: 'Item',
                        id: 'ASIN',
                        totalRecords: '@total'
                    }, [
                        // set up the fields mapping into the xml doc
                        // The first needs mapping, the others are very basic
                        {name: 'Author', mapping: 'ItemAttributes > Author'},
                        'Title', 'Manufacturer', 'ProductGroup', 'ASIN'
                    ])
                });
        

                // create the grid
                var grid = new Ext.grid.GridPanel({
                    store: historyData,
                    columns: [
                        {header: "ID", width: 80, dataIndex: 'ASIN', sortable: false, resizable: false},
                        {header: "Created On", width: 110, dataIndex: 'Author', sortable: false, resizable: false},
                        {header: "Created By", width: 110, dataIndex: 'Author', sortable: false, resizable: false},
                        {header: "Event Type", width: 110, dataIndex: 'Author', sortable: false, resizable: false},                
                        {header: "Message Text", width: 540, dataIndex: 'Title', sortable: false, resizable: false}
                    ],
                    renderTo:'historyGrid',
                    width:960,
                    height:500,
                    enableHdMenu:false
                });

                historyData.load();


        
                commentsJsonReader = new Ext.data.JsonReader({
                    totalProperty: 'totalCount',   
                    root: 'comments', 
                    fields:
                        [
                        {name:'id'},
                        {name:'createdBy'},            
                        {name:'commentText'}
                    ]
                });// {name:'created', type: 'date', dateFormat: 'd/m/Y'},
        
        
            
                commentsDataStore = new Ext.data.Store({
                    proxy: new Ext.data.HttpProxy
                    ({url: 'comments_dummy.js',method:'GET'}),
                    reader:commentsJsonReader        
                });
        
        
        
                commentsGrid = new Ext.grid.GridPanel({
                    store: commentsDataStore,
                    loadMask: true,
                    columns: [
                        {header: "ID", width: 80, dataIndex: 'id', sortable: false, resizable: false},
                        {header: "Created", width: 110, dataIndex: 'createdBy', sortable: false, resizable: false},
                        {header: "Message", width: 760, dataIndex: 'commentText', sortable: false, resizable: false}
                    ],
                    renderTo:'commentsGrid',
                    width:960,
                    height:500,
                    enableHdMenu:false
                });
        
        

                /*
        
        
        
        var commentsData = new Ext.data.Store({
            // load using HTTP
            url: 'history.xml',

            // the return will be XML, so lets set up a reader
            reader: new Ext.data.XmlReader({
                   // records will have an "Item" tag
                   record: 'Item',
                   id: 'ASIN',
                   totalRecords: '@total'
               }, [
                   // set up the fields mapping into the xml doc
                   // The first needs mapping, the others are very basic
                   {name: 'Author', mapping: 'ItemAttributes > Author'},
                   'Title', 'Manufacturer', 'ProductGroup'
               ])
        });



        // create the grid
        var commentsGrid = new Ext.grid.GridPanel({
            store: commentsData,
            loadMask: true,
            columns: [
                {header: "ID", width: 80, dataIndex: 'Author', sortable: false, resizable: false},
                {header: "Created", width: 110, dataIndex: 'Author', sortable: false, resizable: false},
                {header: "Message", width: 760, dataIndex: 'Title', sortable: false, resizable: false}
            ],
            renderTo:'commentsGrid',
            width:960,
            height:500,
            enableHdMenu:false
        });  
        
        
                 */
        

    
            });
    
    
            var commentsLoaded = false;
    
            function loadComments(){
                if(!commentsLoaded){
                    commentsDataStore.load();
                    commentsLoaded = true;
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
                                    <a href='<s:url action="uploadClaims"/>'>XML Uploads</a>&nbsp;|&nbsp; 
                                    <a href="#">Help</a> &nbsp;|&nbsp;
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
                            <legend>Claim Header</legend>
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td>
                                        <label class="chox-claim-header-label">
                                    Insurer</label><label class="chox-claim-header-text"><s:property value="insurer.name" /></label></td>
                                    <td>
                                        <label class="chox-claim-header-label">
                                    Credit-hire Organsation</label><label class="chox-claim-header-text"><s:property value="chorganisation.name" /></label></td>
                                    <td>
                                        <label class="chox-claim-header-label">
                                    Created By</label><label class="chox-claim-header-text"><s:property value="getCreatedBy" /></label></td>
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
                                    Created On</label><label class="chox-claim-header-text"><s:date name="createdDate" format="yyyy-MM-dd hh:mm"  /></label></td>
                                </tr>
                                <tr>
                                    <td>
                                        <label class="chox-claim-header-label">
                                    Current Status</label><label class="chox-claim-header-text"><span id="status"><s:property value="status" /></span></label><!--span id="statusTip"><img src="img/tip.gif" style="fixed:relative;top:-50" /></span--></td>
                                    <td colspan="2">
                                    &nbsp;</td>
                                </tr>
                            </table>
                        </fieldset>
                    </div>
                </div>
                
                <div class="chox-claim-header x-panel-bwrap chox-form-container">    
                    <s:action name="getActionPanel" executeResult="true" />
                </div>
                
                
                
                
                
                <div id="tabContainer">
                    
                    
                    
                    <div id="claimDetails">
                        
                        
                        <div class="x-panel-bwrap chox-form-container">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr valign="top">
                                    <td class="chox-form-left-col">
                                        
                                        
                                        <s:action name="getCustomer" executeResult="true">
                                            <s:param name="objectId"><s:property value="customer.id" /></s:param> 
                                            <s:param name="claimStatus"><s:property value="status" /></s:param>         
                                        </s:action>  
                                        
                                       
                                        <s:action name="getInjury" executeResult="true">
                                            <s:param name="objectId"><s:property value="injury.id" /></s:param> 
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>


                                        <s:action name="getSolicitor" executeResult="true">
                                            <s:param name="objectId"><s:property value="solicitor.id" /></s:param> 
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>       



                                        <s:action name="getIncident" executeResult="true">
                                            <s:param name="objectId"><s:property value="incident.id" /></s:param> 
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>
                                        

                                                                                
                                        
                                    </td>
                                    <td>
                                        
                                        


<!-- third pary -->



                                        <s:action name="getThirdParty" executeResult="true">
                                            <s:param name="objectId"><s:property value="thirdParty.id" /></s:param> 
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>



                                        <s:action name="getWitness" executeResult="true">
                                            <s:param name="objectId"><s:property value="witness.id" /></s:param> 
                                            <s:param name="claimStatus"><s:property value="status" /></s:param> 
                                        </s:action>

                                        
                                        
                                        <form id="f6" action="dummyAction">
                                            <fieldset class="x-fieldset">
                                                <legend>Customer Vehicle Damage</legend>
                                                <div style="display:none" class="form-container">
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Description</label>
                                                    <textarea class="chox-tta" id="CVDDescription" cols="20" rows="5"></textarea></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Usable?</label>
                                                    <input type="checkbox" class="chox-tcb" id="CVDUsable" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Initial ECD</label>
                                                    <input type="text" class="chox-ttxt" id="CVDECD" /></div>
                                                    <div class="chox-form-button">
                                                        <input type="submit" value="Save Changes" />
                                                    </div>
                                                    <div class="chox-form-submit-result">&nbsp;</div>                                            
                                                </div>
                                            </fieldset>
                                        </form>
                                        
                                        
                                      
                                        

                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div id="hireMonitoringDetails" class="x-hide-display">
                        <div class="x-panel-bwrap chox-form-container">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr valign="top">
                                    <td class="chox-form-left-col">
                                        <form id="f8" action="dummyAction">
                                            <fieldset class="x-fieldset">
                                                <legend>Hire Monitoring</legend>
                                                <div style="display:none" class="form-container">
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Original ECD</label>
                                                    <input type="text" class="chox-ttxt" id="HMInitialECD" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                            Name Of Repairer
                                                        </label>
                                                    <input type="text" class="chox-ttxt" id="HMNameOfRepairer" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Repair Book In Date</label>
                                                    <input type="text" class="chox-ttxt" id="HMRepairBookInDate" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Inspection Booked Date</label><input type="text" class="chox-ttxt" id="HMInspectionBookedDate" />
                                                    </div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Inspection Date</label>
                                                    <input type="text" class="chox-ttxt" id="HMInspectionDate" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Total Loss Check</label>
                                                    <input type="checkbox" class="chox-tcb" id="HMTotalLossCheck" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Total Loss Inspection Report</label>
                                                    <input type="text" class="chox-ttxt" id="HMTotalLossInspectionReport" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Repair Completion Date</label>
                                                    <input type="text" class="chox-ttxt" id="HMRepairCompletionDate" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Name of IME</label>
                                                    <input type="text" class="chox-ttxt" id="HMNameofIME" /></div>
                                                    <div class="chox-form-button">
                                                        <input type="submit" value="Save Changes" />
                                                    </div>
                                                    <div class="chox-form-submit-result">&nbsp;</div>                                              
                                                </div>
                                            </fieldset>
                                        </form>
                                    </td>
                                    <td>
                                        &nbsp;
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div id="invoiceDetails" class="x-hide-display">
                        <div class="x-panel-bwrap chox-form-container">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr valign="top">
                                    <td class="chox-form-left-col">
                                        <form id="f9" action="dummyAction">
                                            <fieldset class="x-fieldset">
                                                <legend>Invoice Details</legend>
                                                <div style="display:none" class="form-container">
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Supplier Claims Handling #</label>
                                                    <input type="text" class="chox-ttxt" id="INVSupplierClaimsHandlingInvoiceNum" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Supplier Claim Invoice #</label>
                                                    <input type="text" class="chox-ttxt" id="INVSupplierClaimInvoiceNum" /></div>
                                                </div>
                                            </fieldset>
                                        </form>
                                        <form id="f10" action="dummyAction">
                                            <fieldset class="x-fieldset">
                                                <legend>Invoice Breakdown</legend>
                                                <div style="display:none" class="form-container">
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Hire Net</label>
                                                    <input type="text" class="chox-ttnum" id="INVHireNet" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Hire Vat</label>
                                                    <input type="text" class="chox-ttnum" id="INVHireVAT" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Hire Gross</label>
                                                    <input type="text" class="chox-ttnum" id="INVHireGross" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Repair Net</label>
                                                    <input type="text" class="chox-ttnum" id="INVRepairNet" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Repair Vat</label>
                                                    <input type="text" class="chox-ttnum" id="INVRepairVAT" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Repair Gross</label>
                                                    <input type="text" class="chox-ttnum" id="INVRepairGross" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Engineer Fee Net</label>
                                                    <input type="text" class="chox-ttnum" id="INVEngineerFeeNet" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Engieer Fee Vat</label>
                                                    <input type="text" class="chox-ttnum" id="INVEngineerFeeVAT" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Engineer Fee Gross</label>
                                                    <input type="text" class="chox-ttnum" id="INVEngineerFeeGross" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                            Storage Recovery Net
                                                        </label>
                                                    <input type="text" class="chox-ttnum" id="INVStorageRecoveryNet" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Storage Recovery Vat</label>
                                                    <input type="text" class="chox-ttnum" id="INVStorageRecoveryVAT" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Storage Recovery Gross</label>
                                                    <input type="text" class="chox-ttnum" id="INVStorageRecoveryGross" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Total Net</label>
                                                    <input type="text" class="chox-ttnum" id="INVTotalNet" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Total Vat</label>
                                                    <input type="text" class="chox-ttnum" id="INVTotalVat" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Total Gross</label>
                                                    <input type="text" class="chox-ttnum" id="INVTotalGross" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Claims Handling Invoice Amount</label>
                                                    <input type="text" class="chox-ttnum" id="INVClaimsHandlingInvoiceAmount" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Deduction For Claims Handling Fee</label>
                                                    <input type="text" class="chox-ttnum" id="INVDeductionForClaimsHandlingFee" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Discount</label>
                                                    <input type="text" class="chox-ttnum" id="INVDiscount" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Total To Pay</label>
                                                    <input type="text" class="chox-ttnum" id="INVTotalToPay" /></div>
                                                    <div class="chox-form-button">
                                                        <input type="submit" value="Save Changes" />
                                                    </div>
                                                    <div class="chox-form-submit-result">&nbsp;</div>                                              
                                                </div>
                                            </fieldset>
                                        </form>
                                        <form id="f12" action="dummyAction">
                                            <fieldset class="x-fieldset">
                                                <legend>Hire Vehicle Details</legend>
                                                <div style="display:none" class="form-container">
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Manufacturer</label>
                                                    <input type="text" class="chox-ttxt" id="HVDManufacturer" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Model</label>
                                                    <input type="text" class="chox-ttxt" id="HVDModel" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Registration</label>
                                                    <input type="text" class="chox-ttxt" id="HVDRegistration" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Replacement Vehicle Class</label>
                                                    <input type="text" class="chox-ttxt" id="HVDReplacementVehicleClass" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Hire Start</label>
                                                    <input type="text" class="chox-ttxt" id="HVDHireStart" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Hire End</label>
                                                    <input type="text" class="chox-ttxt" id="HVDHireEnd" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Reason For Collection</label>
                                                    <input type="text" class="chox-ttxt" id="HVDReasonForCollection" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        No. Days Hire</label>
                                                    <input type="text" class="chox-ttnum" id="HVDNNumberOfDaysHire" /></div>
                                                    <div class="chox-form-button">
                                                        <input type="submit" value="Save Changes" />
                                                    </div>
                                                    <div class="chox-form-submit-result">&nbsp;</div>                                              
                                                </div>
                                            </fieldset>
                                        </form>                            
                                    </td>
                                    <td>
                                        <form id="f11" action="dummyAction">
                                            <fieldset class="x-fieldset">
                                                <legend>Extras</legend>
                                                <div style="display:none" class="form-container">
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        CDW Fee</label>
                                                    <input type="text" class="chox-ttnum" id="EXTCDWFee" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        CDW Quantity</label>
                                                    <input type="text" class="chox-ttnum" id="EXTCDWQuantity" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Automatic Fee</label>
                                                    <input type="text" class="chox-ttnum" id="EXTAutomaticFee" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Automatic Quantity</label>
                                                    <input type="text" class="chox-ttnum" id="EXTAutomaticQuantity" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Sat Nav Fee</label>
                                                    <input type="text" class="chox-ttnum" id="EXTSatNavFee" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Sat Nav Quantity</label>
                                                    <input type="text" class="chox-ttnum" id="EXTSatNavQuantity" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Estate Fee</label>
                                                    <input type="text" class="chox-ttnum" id="EXTEstateFee" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Estate Quantity</label>
                                                    <input type="text" class="chox-ttnum" id="EXTEstateQuantity" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Baby Seat Fee</label>
                                                    <input type="text" class="chox-ttnum" id="EXTBabySeatFee" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                            Baby Seat Quantity
                                                        </label>
                                                    <input type="text" class="chox-ttnum" id="EXTBabySeatQuantity" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Tow Bars Fee</label>
                                                    <input type="text" class="chox-ttnum" id="EXTTowBarsFee" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Tow Bars Quantity</label>
                                                    <input type="text" class="chox-ttnum" id="EXTTowBarsQuantity" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Non-standard Risk Ins. Premium Fee</label>
                                                    <input type="text" class="chox-ttnum" id="EXTNonStandardRiskInsurancePremiumFee" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Non-standard Risk Ins. Premium Qty</label>
                                                    <input type="text" class="chox-ttnum" id="EXTNonStandardRiskInsurancePremiumQuantity" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Admin Fee</label>
                                                    <input type="text" class="chox-ttnum" id="EXTAdminFee" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Admin Quantity</label>
                                                    <input type="text" class="chox-ttnum" id="EXTAdminQuantity" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Roof Rack Fee</label>
                                                    <input type="text" class="chox-ttnum" id="EXTRoofRackFee" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Roof Rack Quantity</label>
                                                    <input type="text" class="chox-ttnum" id="EXTRoofRackQuantity" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Dual Control Fee</label>
                                                    <input type="text" class="chox-ttnum" id="EXTDualControlFee" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Dual Control Quantity</label>
                                                    <input type="text" class="chox-ttnum" id="EXTDualControlQuantity" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Delivery Collection Fee</label>
                                                    <input type="text" class="chox-ttnum" id="EXTDeliveryCollectionFee" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Delivery Collection Fee Quantity</label>
                                                    <input type="text" class="chox-ttnum" id="EXTDeliveryCollectionFeeQuantity" /></div>
                                                    <div class="chox-form-button">
                                                        <input type="submit" value="Save Changes" />
                                                    </div>
                                                    <div class="chox-form-submit-result">&nbsp;</div>                                              
                                                </div>
                                            </fieldset>
                                        </form>
                                        
                                        <form id="f13" action="dummyAction">
                                            <fieldset class="x-fieldset">
                                                <legend>Engineer Report</legend>
                                                <div style="display:none" class="form-container">
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Estimated Labour Amount</label>
                                                    <input type="text" class="chox-ttnum" id="ERPTEstimatedLabourAmount" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Estimated Total Repair Amount</label>
                                                    <input type="text" class="chox-ttnum" id="ERPTEstimatedTotalRepairAmount " /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Estimated Days Under Repair</label>
                                                    <input type="text" class="chox-ttnum" id="ERPTEstimatedDaysUnderRepair" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Usable?</label><input type="checkbox" class="chox-tcb" id="ERPTUsable" />
                                                    </div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Name</label>
                                                    <input type="text" class="chox-ttxt" id="ERPTEngineerName" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Company</label>
                                                    <input type="text" class="chox-ttxt" id="ERPTEngineerCompany" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Engineer Address 1</label>
                                                    <input type="text" class="chox-ttxt" id="ERPTEngineerAddress1" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Engineer Address 2</label>
                                                    <input type="text" class="chox-ttxt" id="ERPTEngineerAddress2" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Engineer Address 3</label>
                                                    <input type="text" class="chox-ttxt" id="ERPTEngineerAddress3" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Engineer Address 4</label>
                                                    <input type="text" class="chox-ttxt" id="ERPTEngineerAddress4" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Engineer Address 5</label>
                                                    <input type="text" class="chox-ttxt" id="ERPTEngineerAddress5" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Engineer Postcode</label>
                                                    <input type="text" class="chox-ttxt" id="ERPTEngineerPostcode" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Engineer Telephone</label>
                                                    <input type="text" class="chox-ttxt" id="ERPTEngineerTelephone" /></div>
                                                    <div class="chox-form-item">
                                                        <label class="chox-form-std-label">
                                                        Engineer Email</label>
                                                    <input type="text" class="chox-ttxt" id="ERPTEngineerEmail" /></div>
                                                    <div class="chox-form-button">
                                                        <input type="submit" value="Save Changes" />
                                                    </div>
                                                    <div class="chox-form-submit-result">&nbsp;</div>                                              
                                                </div>
                                            </fieldset>
                                        </form>
                                    </td>
                                </tr>
                            </table>
                            
                        </div>
                    </div>
                    <div id="paymentPack" class="x-hide-display">
                    </div>
                    <div id="historyDetails" class="x-hide-display">
                        <div id="historyGrid">
                        </div>
                    </div>
                    <div id="comments" class="x-hide-display">
                        <div class="comments  x-panel-bwrap chox-form-container">
                            <form id="fComments" action="WebForm1.aspx" method="get">
                                <fieldset class="x-fieldset">
                                    <legend>Add a new note</legend>
                                    <textarea id="commentBox" cols="70" rows="4" name="commentBox"></textarea><br/><input type="submit" id="bAddComment" value="Add Note"/>
                                </fieldset>
                            </form>
                        </div>
                        
                        
                        <div id="commentsGrid">
                        </div>
                    </div>                                     
                </div>

            </div>
        </div>
        
    </body>
</html>
