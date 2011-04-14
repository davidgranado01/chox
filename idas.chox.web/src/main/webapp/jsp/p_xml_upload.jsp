<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var uploadedFileJsonReader;
    var uploadedFileData;
    var uploadedFileGrid;
    var xmlClaimsStatusJsonReader;
    var xmlClaimsStatusData;
    var xmlClaimsStatusGrid;
    var processStatus=0;
    
    //var sm;
    //var claimsSm;
    //var loadLiveClaimData;
    var selectedRecord;
    var intervelId;

    // $(function(){

    Ext.onReady(function(){



        // GENERATE HELP NOTES
        var uploadedFileField = new Ext.form.TextField({
            name             : 'uploadedFile',
            id               : 'uploadedFile',
            width            :  200,
            allowBlank       :  false,
            inputType        : 'file',
            renderTo         : 'FileUploadId'

        });
        var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true,
            header: ' ',
            listeners:{
                rowselect : function ( selmo, rowIndex, record ){
                    if(record.get('processed')){
                        setGridHeight(record.get('totalClaims'));
                        xmlClaimsStatusGrid.getGridEl().mask('Please wait loading claims...');
                        loadProcessedClaimDetails(record.get('id'));
                        claimDetailsGridRowColourRenderer();
                        showUploadedClaimsDetailStatusBar(record.get('totalClaims'),record.get('totalClaims'),record.get('valid'));
                        
                    }else{
                        xmlClaimsStatusGrid.setHeight(50);
                        xmlClaimsStatusData.removeAll();
                        if(record.get('valid')){
                            xmlClaimsStatusGrid.setTitle("Please click the process button inorder to process this file.");
                        }else{
                            xmlClaimsStatusGrid.setTitle("This is not a valid XML file. Please upload a valid XML file.");
                        }
                       
                    }
                    
                },
                rowdeselect : function(){
                    xmlClaimsStatusData.removeAll();
                    xmlClaimsStatusGrid.setTitle("Uploaded claims details");
                    xmlClaimsStatusGrid.setHeight(50);
                }
            }
        });
         
        var tbar = new Ext.Toolbar({
            id : 'fileUploadToolbarId',
            items:[{
                    text:'Process',
                    id : 'fileUploadProcessButtonId',
                    handler : function() {

                        selectedRecord = sm.getSelected();
                        if(sm.getSelected()){

                            if((sm.getSelected().get('processed')==false) && (sm.getSelected().get('valid')==true)){
                                if(processStatus==0){
                                    processStatus=1;
                                }else{
                                    return false;
                                }
                                uploadedFileGrid.getGridEl().mask();
                                //                                xmlClaimsStatusGrid.getGridEl().mask('Please wait processing claims...');
                               
                                Ext.Ajax.request({
                                    url: '<%= request.getContextPath()%>/prv/p/processUploadedFile.action',
                                    timeout:1800000,
                                    callback : function(options,success,response){
                                        processStatus=0;
                                        uploadedFileGrid.getGridEl().unmask();
                                        loadUploadedFiles(1);
                                        intervelId=window.clearInterval(intervelId);
                                        loadProcessedClaimDetails(selectedRecord.get('id'));
                                        xmlClaimsStatusGrid.setTitle('All '+ selectedRecord.get('totalClaims') + ' Claims have been processed');
                                        //                                        xmlClaimsStatusGrid.getGridEl().unmask();
                                        //intervelId=window.clearInterval(intervelId);
                                        //loadProcessedClaimDetails(sm.getSelected().get('id'));
                                        if(response.responseText){
                                            var resp = Ext.util.JSON.decode(response.responseText);
                                            if(resp && resp.isValid){
                                                if(resp.resultType && resp.resultType == 'Message')
                                                {
                                                    //                                                    Ext.MessageBox.show({
                                                    //                                                        title: 'Upload successful',
                                                    //                                                        msg: resp.result,
                                                    //                                                        width:300,
                                                    //                                                        buttons: Ext.MessageBox.OK
                                                    //                                                    });
                                                }else if(!resp.result){

                                                    Ext.MessageBox.show({
                                                        title: 'File Process failure',
                                                        msg: 'Unexpected Error occured. Please report to chox admin.',
                                                        width:300,
                                                        buttons: Ext.MessageBox.OK,
                                                        icon : Ext.MessageBox.ERROR
                                                    });
                                                }
                                            }
                                            else if(resp.errors)
                                            {
                                                Ext.MessageBox.show({
                                                    title: 'File Process failure',
                                                    msg: resp.errors,
                                                    width:300,
                                                    buttons: Ext.MessageBox.OK,
                                                    icon : Ext.MessageBox.ERROR
                                                });
                                            }}else{
                                            Ext.MessageBox.show({
                                                title: 'Server too busy',
                                                msg: 'Timeout Error has occured because Server is handeling too many request. Please referesh the page to see the processed claim details.',
                                                width:300,
                                                buttons: Ext.MessageBox.OK,
                                                icon : Ext.MessageBox.INFO
                                            });
                                        }
                                    },
                                    params: {
                                        bordereauId: sm.getSelected().get('id')
                                    }
                                });
                                intervelId=setInterval(loadLiveClaimData, 300);
                            }else{

                                if(sm.getSelected().get('valid')==false){
                                    Ext.MessageBox.show({
                                        title: 'process failure',
                                        msg: 'The selected File is not valid.',
                                        width:300,
                                        buttons: Ext.MessageBox.OK,
                                        icon : Ext.MessageBox.ERROR
                                    });
                                }else{
                                    Ext.MessageBox.show({
                                        title: 'process failure',
                                        msg: 'The selected File has been processed already.',
                                        width:300,
                                        buttons: Ext.MessageBox.OK,
                                        icon : Ext.MessageBox.ERROR
                                    });
                                }
                               
                            }
                        }else{
                            Ext.MessageBox.show({
                                title: 'process failure',
                                msg: 'No records have been selected.',
                                width:300,
                                buttons: Ext.MessageBox.OK,
                                icon : Ext.MessageBox.ERROR
                            });
                        }
                      
                    }
                
                },'-','',{
                    text:'Delete ',
                    id : 'fileUploadDeleteButtonId',
                    handler : function() {
                        if(sm.getSelected().get('processed')==true){
                            Ext.MessageBox.show({
                                title: '',
                                msg: 'Processed files cannot be removed from the system',
                                width:300,
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.INFO
                            });
                            //                            Ext.MessageBox.alert('', 'Sorry processed file can not be removed from the system.' );
                        }else{
                            
                            uploadedFileGrid.getGridEl().mask();
                            Ext.Ajax.request({
                                url: '<%= request.getContextPath()%>/prv/p/deleteUploadedFile.action',
                                timeout:480000,
                                callback : function(options,success,response){
                                    uploadedFileGrid.getGridEl().unmask();
                                    loadUploadedFiles(1);
                                    //intervelId=window.clearInterval(intervelId);
                                    //loadProcessedClaimDetails(sm.getSelected().get('id'));
                                    if(response.responseText){
                                        var resp = Ext.util.JSON.decode(response.responseText);
                                        if(resp && resp.isValid){
                                            if(resp.resultType && resp.resultType == 'Message')
                                            {
                                                //                                                Ext.MessageBox.show({
                                                //                                                    title: 'Deletion successful',
                                                //                                                    msg: resp.result,
                                                //                                                    width:300,
                                                //                                                    buttons: Ext.MessageBox.OK
                                                //                                                });
                                            }else if(!resp.result){

                                                Ext.MessageBox.show({
                                                    title: 'File deletion failure',
                                                    msg: 'Unexpected Error occured. Please report to chox admin.',
                                                    width:300,
                                                    buttons: Ext.MessageBox.OK,
                                                    icon : Ext.MessageBox.ERROR
                                                });
                                            }
                                        }
                                        else if(resp.errors)
                                        {
                                            Ext.MessageBox.show({
                                                title: 'File deletion failure',
                                                msg: resp.errors,
                                                width:300,
                                                buttons: Ext.MessageBox.OK,
                                                icon : Ext.MessageBox.ERROR
                                            });
                                        }}else{
                                        Ext.MessageBox.show({
                                            title: 'Server too busy',
                                            msg: 'A timeout error has occurred because the server is handling too many requests. Please click OK in order to continue processing the claims.',
                                            width:300,
                                            buttons: Ext.MessageBox.OK,
                                            icon : Ext.MessageBox.INFO
                                        });
                                    }
                                },
                                params: {
                                    bordereauId: sm.getSelected().get('id')
                                }
                            });

                        }
                    }
                },'->',{
                    text : 'Uploaded today',
                    id : 'fileUploadUploadTodayButtonId',
                    handler : function() {
                        uploadedFileGrid.setTitle('uploaded files (Today)');
                        loadUploadedFiles(1);
                    }
                },'-','',{
                    text : 'In 7 days',
                    id : 'fileUploadIn7DaysButtonId',
                    handler : function() {
                        uploadedFileGrid.setTitle('uploaded files in 7 days');
                        loadUploadedFiles(6);
                    }
                },'-','',{
                    text : 'In 30 days',
                    id : 'fileUploadIn30DaysButtonId',
                    handler : function() {
                        uploadedFileGrid.setTitle('uploaded files in 30 days');
                        loadUploadedFiles(29);
                    }
                },'-','',{
                    text : 'All',
                    id : 'fileUploadAllButtonId',
                    handler : function() {
                        uploadedFileGrid.setTitle('uploaded files (All)');
                        loadUploadedFiles(999);
                    }
                }
            ]
        });

        /*
         *        Uploaded Files Grid
         */

        uploadedFileJsonReader = new Ext.data.JsonReader({
            id : 'uploadedFileJsonReaderId',
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'fileName'},
                {name:'fileSize'},
                {name:'processed'},
                {name:'message'},
                {name:'createdBy'},
                {name:'createdDate'},
                {name:'status'},
                {name:'totalClaims'},
                {name:'valid'},
                {name:'description'}

            ]
        });

        uploadedFileData = new Ext.data.Store({
            id : 'uploadedFileDataId',
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getUploadedfiles.action', method:'POST',timeout:60000}),
            reader:uploadedFileJsonReader,
            remoteSort: true
        });

        uploadedFileData.setDefaultSort('createdDate', 'asc');


        uploadedFileGrid = new Ext.grid.GridPanel({
            id : 'uploadedFileGridId',
            listeners: {cellclick:FilesOnClick},
            loadMask:true,
            store: uploadedFileData,
            renderTo:'uploadedFileGrid',
            enableHdMenu:false,
            layout:'fit',
            viewConfig:{forceFit:true},
            selModel : sm,
            tbar:tbar,
            title:'uploaded files (Today)',
            deferRowRender:false,
            columns: [
                
                sm,
                new Ext.grid.RowNumberer(),
                {header: "File Name", width:100, dataIndex: 'fileName', sortable: true, resizable: true},
                {header: "File Size", width:50, dataIndex: 'fileSize', sortable: true, resizable: true},
                {header: "Total Claims", width:40, dataIndex: 'totalClaims', sortable: true, resizable: true},
                {header: "Status",  width:150, dataIndex: 'status', sortable: true, resizable: true},
                {header: "Description",  width:250, dataIndex: 'description', sortable: true, resizable: true},
                {header: "Created Date", width:100, dataIndex: 'createdDate', sortable: true, resizable: true},
                {header: "Created By", width:150, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Error Message", width:150, dataIndex: 'message', sortable: true, resizable: true}

                
                //                {header: "", width: 60, dataIndex: 'delete', sortable: false, resizable: false, renderer:function(value,p,r){
                //                        return "<a href='#' class='high-light-item'>" + value + "</a>"}}
            ],
            width:990,
            height:160
        });

        uploadedFileData.setDefaultSort('createdDate', 'desc');
        
        loadUploadedFiles(1);


        /*
         *         Claim Details Grid
         */

        xmlClaimsStatusJsonReader = new Ext.data.JsonReader({
            id : 'xmlClaimsStatusJsonReaderId',
            totalProperty: 'totalCount',
            totalProcessedClaims:'totalProcessedClaims',
            root: 'results',
            fields:
                [
                {name:'supplierReferenceNumber'},
                {name:'claimStatus'},
                {name:'processStatus'},
                {name:'remark'},
                {name:'message'},
                {name:'claimId'},
                {name:'valid'}
            ]
        });

        xmlClaimsStatusData = new Ext.data.Store({
            id : 'xmlClaimsStatusDataId',
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getUploadedClaimsDetails.action', method:'POST',timeout:60000}),
            reader:xmlClaimsStatusJsonReader
        });

        xmlClaimsStatusGrid = new Ext.grid.GridPanel({
            id : 'xmlClaimsStatusGridId',
            listeners:  {cellclick:ClaimsOnClick },
            //loadMask:true,
            //tbar:claimsStatusBar,
            store: xmlClaimsStatusData,
            renderTo:'xmlClaimsStatus',
            enableHdMenu:false,
            layout:'fit',
            //autoHeight:true,
            viewConfig:{forceFit:true},
            title:'Uploaded claims details',
            columns: [
                new Ext.grid.RowNumberer(),
                {header: "Supplier Reference", width:150, dataIndex: 'supplierReferenceNumber', sortable: true, resizable: true,
                    renderer:function(value,p,r){ if( (r.data['claimStatus']!=null && r.data['claimStatus']!='' && r.data['claimStatus']!='N/A' ) || r.data['valid'] ){
                            return '<a href="<%=request.getContextPath()%>/prv/openClaimDetail.action?id=' + r.data['claimId']+ '&tab=' + currentTabIndex + '">' + value + '</a>'}
                        else{return r.data.supplierReferenceNumber}}},
                {header: "Claim Status", width:200, dataIndex: 'claimStatus', sortable: true, resizable: true},
                {header: "Process Status", width:140, dataIndex: 'processStatus', sortable: true, resizable: true},
                {header: "Remark",  width:250, dataIndex: 'remark', sortable: true, resizable: true},
                {header: "Error Message", width:250, dataIndex: 'message', sortable: true, resizable: true}
                


                //                {header: "", width: 60, dataIndex: 'delete', sortable: false, resizable: false, renderer:function(value,p,r){
                //                        return "<a href='#' class='high-light-item'>" + value + "</a>"}}
            ],
            width:990
            ,height:50
        });

      
        
        var op = {
            beforeSubmit: onBeforeSubmit,
            success: UploadedFileAfterSubmit
            // timeout: 50000
            // error: onSubmitError
        };

        $("form#uploadClaimForm").validate(
        {
            errorLabelContainer: "#uploadFormMsgBox",
            rules: {

                uploadedFile :{ required:true }
            },
            messages:
                {

                uploadedFile : {required:"You must select an Attachment"}
            },

            submitHandler: function(form) {

                var uploadFile = Ext.getDom('uploadedFile').value;
                if((uploadFile.lastIndexOf("."))>0){
                    var filename = uploadFile.substr(uploadFile.lastIndexOf('\\')+1, uploadFile.length);
                    $("#uploadFileName").val(filename);
                }
                if (!validateFileExtension(uploadFile)) {
                    Ext.MessageBox.show({
                        title: 'Upload failure',
                        msg: 'Please select a valid xml file to upload into CHOX',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                    return;
                }else{
                    $(form).ajaxSubmit(op);
                }
            }
        });
    });

    var loadLiveClaimData = function loadLiveUploadedClaimsDetails(){
        
        if(selectedRecord.get('totalClaims')>xmlClaimsStatusData.getCount()){
            setGridHeight(xmlClaimsStatusData.getCount());
            //            xmlClaimsStatusGrid.getGridEl().mask(xmlClaimsStatusData.getCount() + ' of '+selectedRecord.get('totalClaims')+' Claims have been processed');
            loadProcessedClaimDetails(selectedRecord.get('id'));
            claimDetailsGridRowColourRenderer();
            updateUploadedClaimsDetailStatus(selectedRecord.get('totalClaims'),xmlClaimsStatusData.getCount());
        }else{
            setGridHeight(xmlClaimsStatusData.getCount());
            //            xmlClaimsStatusGrid.getGridEl().unmask();
            intervelId=window.clearInterval(intervelId);
            xmlClaimsStatusGrid.setTitle('All '+ xmlClaimsStatusData.getCount() + ' Claims have been processed');
            
            // selectedRecord=0;
        }
    
    }

    function setGridHeight(columnSize){
        var heightSize=50
        if(columnSize>0){
            //            if(columnSize<5){
            heightSize = heightSize + columnSize*28;
            //            }else{
            //                heightSize =  columnSize*30;
            //            }
            if(heightSize<140){
                heightSize = 140
            }
            if(heightSize>530){
                heightSize=530;
            }
        }
        xmlClaimsStatusGrid.setHeight(heightSize);
    }

    function loadProcessedClaimDetails(id){
        xmlClaimsStatusData.load({
            params:{
                bordereauId:id
            },
            callback :  function(options,success,response){ xmlClaimsStatusGrid.getGridEl().unmask();}
        });
    }

    function updateUploadedClaimsDetailStatus(totalamount,received){
        xmlClaimsStatusGrid.setTitle(received+ ' of '+totalamount+' Claims have been processed');
    }

    function showUploadedClaimsDetailStatusBar(totalamount,received,valid){
        if(totalamount==0 && valid ){
            xmlClaimsStatusGrid.setTitle('No claim details found for the selected file. This file might be processed before this future is implemented.');
        }else{
            xmlClaimsStatusGrid.setTitle('Showing '+received+ ' of '+totalamount+' Claims.');
        }
    }

    function claimDetailsGridRowColourRenderer(){
        xmlClaimsStatusGrid.getView().getRowClass = function(record, index) {
            var status = record.data.claimStatus;
            if(!record.data.valid){
                return 'red-row';
            }else if(status=='InvoiceApproved'|| status=='InvoiceApprovedByBRE' || status=='AwaitingInvoicePayment' ){
                return 'green-row';
            }else if(status=='InvoiceDataCalculationIncorrect'||status=='N/A' ){
                return 'red-row';
            }else if(status=='AwaitingPaymentPack' || status=='InvoiceEscalated' || status=='InvoiceUnassigned' || status=='InvoiceEscalatedToHandler'){
                return 'orange-row';
            }else{
                return 'blue-row';
            }
        };
    }

    function uploadedFileDetailsGridRowColourRenderer(){
        uploadedFileGrid.getView().getRowClass = function(record, index) {
            var status = record.data.status;
            if(status=='Waiting to be Processed'){
                return 'black-row';
            }else if(status=='All Uploaded' || status=='ALLUPLOADED'){
                return 'green-row';
            }else if(status=='Partially Uploaded' || status=='PARTIALUPLOAD'){
                return 'orange-row';
            }else if(status=='All Rejected' || status=='ALLREJECTED' || status=='ERROR'){
                return 'red-row';
            }else if(record.data.valid==false || status=='Error'){
                return 'gray-row'
            }

            // return (record.data.complete ? 'gray-row' : (days > 0 ? 'black-row' : (days < 0 ? 'red-row' : 'orange-row')));
        };
    }

   

    function validateFileExtension(fileName) {
        var exp = /^.*.(xml|XML)$/;
        return exp.test(fileName);
    }

    function loadUploadedFiles(days){
        resetUploadedFileForm();
        uploadedFileData.load({ params : {"days":days}});
        uploadedFileDetailsGridRowColourRenderer();
        //sm.selectFirstRow();
    }

    function resetUploadedFileForm(){
        $("form#uploadClaimForm").each(function(){
            this.reset();
        });
    }

    function onBeforeSubmit() {
        Ext.get('uploadClaimForm').mask('Please wait, file is being uploaded...');
        return true;
    }

    function UploadedFileAfterSubmit(responseText, statusText, form, responseType)  {

        if (responseText.indexOf('You have been denied access') !=-1) {
            Ext.MessageBox.alert('Error', 'You have been denied access and will now be logged out', function() {
                window.location = '/j_spring_security_logout';
                return;
            });
        }
        if(responseText)
        {
            var response = eval('(' + responseText.trim() + ')');
            if(response && response.isValid){
                if(response.resultType && response.resultType == 'Message')
                {
                    //                    Ext.MessageBox.show({
                    //                        title: 'Upload successful',
                    //                        msg: response.result,
                    //                        width:300,
                    //                        buttons: Ext.MessageBox.OK
                    //                    });
                }else if(!response.result){
                    Ext.MessageBox.show({
                        title: 'Upload failure',
                        msg: 'File size exceeded 10 MB limit.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
            }
            else if(response.errors)
            {
                Ext.MessageBox.show({
                    title: 'Upload failure',
                    msg: response.errors,
                    width:300,
                    buttons: Ext.MessageBox.OK,
                    icon : Ext.MessageBox.ERROR
                });
            }
            else{
                Ext.MessageBox.show({
                    title: 'Upload failure',
                    msg: 'Unknown Error Encountered, please try again, if same problem exists please report to the chox support team.',
                    width:300,
                    buttons: Ext.MessageBox.OK,
                    icon : Ext.MessageBox.ERROR
                });
            }
        }
        else
        {
            Ext.MessageBox.show({
                title: 'Upload failure',
                msg: 'Unknown Error Encountered, please try again, if same problem exists please report to the chox support team.',
                width:300,
                buttons: Ext.MessageBox.OK,
                icon : Ext.MessageBox.ERROR
            });
        }
        Ext.get('uploadClaimForm').unmask();
        uploadedFileGrid.setTitle('uploaded files (Today)');
        loadUploadedFiles(1);
        xmlClaimsStatusGrid.setHeight(50);
        xmlClaimsStatusData.removeAll();
        xmlClaimsStatusGrid.setTitle("Uploaded claims details");
    }
    function ClaimsOnClick(grid, rowIndex, columnIndex){
        if (columnIndex == 6 ||columnIndex == 3 ||columnIndex == 4 ||columnIndex == 5) {
            var task = xmlClaimsStatusGrid.getStore().getAt(rowIndex);
            var title="Uploaded Claim Status";
            var msg = "<b>Supplier Reference</b>: " + task.get("supplierReferenceNumber");
            msg += "<br/><b>Claim Status</b>: " + task.get("claimStatus");
            msg += "<br/><b>Process Status</b>: " + task.get("processStatus");
            msg += "<br/><b>Remark</b>: " + task.get("remark");
            msg += "<br/><b>Error Message</b>: " + task.get("message");
            propmtMsg(title, msg);
        }
    }

    function FilesOnClick(grid, rowIndex, columnIndex){
        if (columnIndex == 8 ||columnIndex == 5 ||columnIndex == 6 ||columnIndex == 7||columnIndex == 9) {
            var task = uploadedFileGrid.getStore().getAt(rowIndex);
            var title="Uploaded File Details";
            var msg = "<b>File Status</b>: " + task.get("status");
            msg += "<br/><b>Description</b>: " + task.get("description");
            msg += "<br/><b>Created Date</b>: " + task.get("createdDate");
            msg += "<br/><b>Created By</b>: " + task.get("createdBy");
            msg += "<br/><b>Error Message</b>: " + task.get("message");
            propmtMsg(title, msg);
        }
    }

    
  

    
</script>

<div class="claim-detail-tab">

    <form id="uploadClaimForm" name="uploadClaimForm" action="<%= request.getContextPath()%>/prv/p/uploadNewClaimsFile.action" method="POST" enctype="multipart/form-data">
        <div class="form-container">
            <fieldset class="x-fieldset">
                <legend>Upload XML File&nbsp;</legend>
                <table class="chox-form-item" cellpadding="0" cellspacing="0" border="0" width="100%">
                    <tr>
                        <td width="200" align="right">
                            <label class="std-label-ro">File&nbsp;&nbsp;</label>
                        </td>
                        <td>
                            <div id="FileUploadId"/>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <div class="column-remark" style="padding:10px 0 10px 0;">
                                Maximum attachment size is 10 MB. <br/>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>

                        <td>
                            <s:if test="uploadFlag">
                                <input id="fileUploadUploadClaimsButtonId" type="submit" value="Upload Claims" />
                            </s:if>
                            <s:else><br/>
                                <div class="action-error-msg"><b>A Credit Hire Mapping Relationship Does Not Exist. Please Contact CHOX Admin.</b></div>
                            </s:else>
                        </td>
                    </tr>
                </table>
                <div class="chox-form-submit-result" id="formSubmitResultId"/>
                <div class="action-error-msg" id="uploadFormMsgBox"/>

                <!-- <div class="chox-form-submit-result" id="actionResultId"></div> -->
            </fieldset>
        </div>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
    </form>
    <div id="uploadedFileGrid"></div>
    <div id="uploadedFileStatusGrid"></div>
    <div id="xmlClaimsStatus"></div>
</div>