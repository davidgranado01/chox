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
                        
                        loadProcessedClaimDetails(record.get('id'));
                        claimDetailsGridRowColourRenderer();
                        showUploadedClaimsDetailStatusBar(record.get('totalClaims'),record.get('totalClaims'));
                    }else{
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
                }
            }
        });
         
        var claimsSm = new Ext.grid.CheckboxSelectionModel();
        var tbar = new Ext.Toolbar({
            items:[{
                    text:'Process',
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
                               
                                Ext.Ajax.request({
                                    url: '<%= request.getContextPath()%>/prv/p/processUploadedFile.action',
                                    timeout:180000,
                                    callback : function(options,success,response){
                                        processStatus=0;
                                        uploadedFileGrid.getGridEl().unmask();
                                        loadUploadedFiles();
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
                                intervelId=setInterval(loadLiveClaimData, 200);
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
                    handler : function() {
                        if(sm.getSelected().get('processed')==true){
                            Ext.MessageBox.show({
                                                    title: '',
                                                    msg: 'Sorry processed file can not be removed from the system.',
                                                    width:300,
                                                    buttons: Ext.MessageBox.OK,
                                                    icon: Ext.MessageBox.INFO
                                                });
//                            Ext.MessageBox.alert('', 'Sorry processed file can not be removed from the system.' );
                        }else{
                            
                            uploadedFileGrid.getGridEl().mask();
                            Ext.Ajax.request({
                                url: '<%= request.getContextPath()%>/prv/p/deleteUploadedFile.action',
                                timeout:180000,
                                callback : function(options,success,response){
                                    uploadedFileGrid.getGridEl().unmask();
                                    loadUploadedFiles();
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

                        }
                    }
                }
            ]
        });

        /*
         *        Uploaded Files Grid
         */

        uploadedFileJsonReader = new Ext.data.JsonReader({
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
                {name:'valid'}

            ]
        });

        uploadedFileData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getUploadedfiles.action', method:'POST',timeout:60000}),
            reader:uploadedFileJsonReader
        });


        uploadedFileGrid = new Ext.grid.GridPanel({
            listeners: {cellclick:FilesOnClick},
            loadMask:true,
            store: uploadedFileData,
            renderTo:'uploadedFileGrid',
            enableHdMenu:false,
            layout:'fit',
            viewConfig:{forceFit:true},
            selModel : sm,
            tbar:tbar,
            title:'List of uploaded file',
            deferRowRender:false,
            columns: [
                
                sm,
                new Ext.grid.RowNumberer(),
                {header: "File Name", width:100, dataIndex: 'fileName', sortable: true, resizable: true},
                {header: "File Size", width:50, dataIndex: 'fileSize', sortable: true, resizable: true},
                {header: "Total Claims", width:40, dataIndex: 'totalClaims', sortable: true, resizable: true},
                {header: "Status",  width:250, dataIndex: 'status', sortable: true, resizable: true},
                {header: "Created Date", width:150, dataIndex: 'createdDate', sortable: true, resizable: true},
                {header: "Created By", width:150, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Error Message(if any)", width:250, dataIndex: 'message', sortable: true, resizable: true}

                
                //                {header: "", width: 60, dataIndex: 'delete', sortable: false, resizable: false, renderer:function(value,p,r){
                //                        return "<a href='#' class='high-light-item'>" + value + "</a>"}}
            ],
            width:990,
            height:160
        });

        uploadedFileData.setDefaultSort('createdDate', 'desc');
        
        loadUploadedFiles();


        /*
         *         Claim Details Grid
         */

        xmlClaimsStatusJsonReader = new Ext.data.JsonReader({
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
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getUploadedClaimsDetails.action', method:'POST',timeout:60000}),
            reader:xmlClaimsStatusJsonReader
        });

        xmlClaimsStatusGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:ClaimsOnClick },
            //loadMask:true,
            //tbar:claimsStatusBar,
            store: xmlClaimsStatusData,
            renderTo:'xmlClaimsStatus',
            enableHdMenu:false,
            layout:'fit',
            autoHeight:true,
            viewConfig:{forceFit:true},
            selModel : claimsSm,
            title:'Uploaded claims details',
            columns: [
                claimsSm,
                new Ext.grid.RowNumberer(),
                {header: "Supplier Reference", width:150, dataIndex: 'supplierReferenceNumber', sortable: true, resizable: true,
                    renderer:function(value,p,r){ if(r.data['valid']){
                            return '<a href="<%=request.getContextPath()%>/prv/openClaimDetail.action?id=' + r.data['claimId']+ '&tab=' + currentTabIndex + '">' + value + '</a>'}
                        else{return r.data.supplierReferenceNumber}}},
                {header: "Claim Status", width:200, dataIndex: 'claimStatus', sortable: true, resizable: true},
                {header: "Process Status", width:140, dataIndex: 'processStatus', sortable: true, resizable: true},
                {header: "Remark",  width:250, dataIndex: 'remark', sortable: true, resizable: true},
                {header: "Error Message(if any)", width:250, dataIndex: 'message', sortable: true, resizable: true}
                


                //                {header: "", width: 60, dataIndex: 'delete', sortable: false, resizable: false, renderer:function(value,p,r){
                //                        return "<a href='#' class='high-light-item'>" + value + "</a>"}}
            ],
            width:990
            //,height:350
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
            loadProcessedClaimDetails(selectedRecord.get('id'));
            claimDetailsGridRowColourRenderer();
            updateUploadedClaimsDetailStatus(selectedRecord.get('totalClaims'),xmlClaimsStatusData.getCount());
        }else{
            intervelId=window.clearInterval(intervelId);
            xmlClaimsStatusGrid.setTitle('All '+ xmlClaimsStatusData.getCount() + ' Claims have been processed.');
            // selectedRecord=0;
        }
    
    }

    function loadProcessedClaimDetails(id){
        xmlClaimsStatusData.load({
            params:{
                bordereauId:id
            }
        });
    }

    function updateUploadedClaimsDetailStatus(totalamount,received){
        xmlClaimsStatusGrid.setTitle(received+ ' of '+totalamount+' Claims have been processed.');
    }

    function showUploadedClaimsDetailStatusBar(totalamount,received){
        xmlClaimsStatusGrid.setTitle('Showing '+received+ ' of '+totalamount+' Claims.');
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
            }else if(status=='All Uploaded'){
                return 'green-row';
            }else if(status=='Partially Uploaded'){
                return 'orange-row';
            }else if(status=='All Rejected'){
                return 'red-row';
            }else if(record.data.valid==false){
                return 'gray-row'
            }

            // return (record.data.complete ? 'gray-row' : (days > 0 ? 'black-row' : (days < 0 ? 'red-row' : 'orange-row')));
        };
    }

   

    function validateFileExtension(fileName) {
        var exp = /^.*.(xml|XML)$/;
        return exp.test(fileName);
    }

    function loadUploadedFiles(){
        resetUploadedFileForm();
        uploadedFileData.load();
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
        loadUploadedFiles();
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
        if (columnIndex == 8 ||columnIndex == 5 ||columnIndex == 6 ||columnIndex == 7) {
            var task = uploadedFileGrid.getStore().getAt(rowIndex);
            var title="Uploaded File Details";
            var msg = "<b>File Status</b>: " + task.get("status");
            msg += "<br/><b>Created Date</b>: " + dateRenderer(task.get("createdDate"));
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
                            <input type="submit" value="Upload Claims" />
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