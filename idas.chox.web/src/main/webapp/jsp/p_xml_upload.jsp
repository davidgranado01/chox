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
    var xmlUploadFileGridRecordPerPage=10;
    var start=0;
    var defaultDays = 1;
    var processing = false;
    var selectedFileId;
    var selectedFileTotalClaims;
    var intervelId;
    var totalRecordLoaded=0;
    var sm;
    var lastSelectedFile=-1;
    var canSelectRow = false;


    Ext.onReady(function(){
        
        Ext.state.Manager.clear('xmlClaimsStatusGridId');
        var uploadedFileField = new Ext.form.TextField({
            name             : 'uploadedFile',
            id               : 'uploadedFile',
            width            :  200,
            allowBlank       :  false,
            inputType        : 'file',
            renderTo         : 'FileUploadId'

        });

        sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true,
            header: ' ',
            listeners:{
                rowselect : function ( selmo, rowIndex, record ){
                    lastSelectedFile=rowIndex;
                    if(record.get('processed')){
                        setGridHeight(record.get('totalClaims'));
                        xmlClaimsStatusGrid.getGridEl().mask('Please wait loading claims ...');
                        loadProcessedClaimDetails(record.get('id'));
                        claimDetailsGridRowColourRenderer();
                        showUploadedClaimsDetailStatusBar(record.get('totalClaims'),record.get('totalClaims'),record.get('valid'));
                    }else if(record.get('status')=="Processing.."){
                        uploadedFileGrid.getGridEl().mask('Please wait, claims are being processed ...');
                        selectedFileId = sm.getSelected().get('id');
                        selectedFileTotalClaims = sm.getSelected().get('totalClaims');
                        totalRecordLoaded=0;
                        intervelId=setInterval(loadLiveClaimData, 1500);
                        
                    }else{
                        xmlClaimsStatusData.removeAll();
                        xmlClaimsStatusGrid.setHeight(50);
                        if(record.get('valid')){
                            xmlClaimsStatusGrid.setTitle("Please click the process button in order to process this file");
                        }else{
                            xmlClaimsStatusGrid.setTitle("This is not a valid XML file. Please upload a valid XML file.");
                        }
                    }
                    
                },
                rowdeselect : function(){
                    lastSelectedFile=-1;
                    emptyClaimsDetailGrid();
                }
            }
        });
         
        var tbar = new Ext.Toolbar({
            items:[{
                    text:'Process',
                    id : 'fileUploadProcessButtonId',
                    handler : function() {
                        
                        if(sm.getSelected()){

                            if((sm.getSelected().get('processed')==false) && (sm.getSelected().get('valid')==true) && (sm.getSelected().get('status')!="Processing..") && (sm.getSelected().get('id')>0)){
                                if(processStatus==0){
                                    processStatus=1;
                                }else{
                                    return false;
                                }
                                uploadedFileGrid.getGridEl().mask('Please wait, claims are being processed ...');
                                selectedFileId = sm.getSelected().get('id');
                                selectedFileTotalClaims = sm.getSelected().get('totalClaims');
                                totalRecordLoaded=0;
                                choxExtAjaxRequest({
                                    url: '/prv/p/processUploadedFile.action',
                                    timeout:1800000,
                                    params: {
                                        bordereauId: sm.getSelected().get('id')
                                    },
                                    callback : function(options,success,response){
                                        
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
                                                        msg: 'Unexpected Error occurred. Please report to chox admin.',
                                                        width:300,
                                                        buttons: Ext.MessageBox.OK,
                                                        icon : Ext.MessageBox.ERROR
                                                    });
                                                    intervelId=window.clearInterval(intervelId);
                                                    processStatus=0;
                                                    uploadedFileGrid.getGridEl().unmask();
                                                    defaultDays=1;
                                                    loadUploadedFiles();
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
                                                processStatus=0;
                                                uploadedFileGrid.getGridEl().unmask();
                                                defaultDays=1;
                                                loadUploadedFiles();
                                            }}else{
                                            /*
                                             * this error message is commented out cos it pop's up when clicking the supplier reference while the file is being processed.
                                             */
                                            //                                            Ext.MessageBox.show({
                                            //                                                title: 'Server Busy',
                                            //                                                msg: 'A timeout error has occurred because the server is handling too many requests. Please click OK in order to continue processing the claims.',
                                            //                                                width:300,
                                            //                                                buttons: Ext.MessageBox.OK,
                                            //                                                icon : Ext.MessageBox.INFO
                                            //                                            });
                                        }
                                    }
                                });
                                intervelId=setInterval(loadLiveClaimData, 1500);
                            }else{

                                if(sm.getSelected().get('valid')==false){
                                    Ext.MessageBox.show({
                                        title: 'process failure',
                                        msg: 'The selected File is not valid.',
                                        width:300,
                                        buttons: Ext.MessageBox.OK,
                                        icon : Ext.MessageBox.ERROR
                                    });
                                }else if(sm.getSelected().get('status')=="Processing.."){
                                    Ext.MessageBox.show({
                                        title: 'process failure',
                                        msg: 'The selected file is being processed in the server.',
                                        width:300,
                                        buttons: Ext.MessageBox.OK,
                                        icon : Ext.MessageBox.ERROR
                                    });
                                }else if(sm.getSelected().get('id')<=0){
                                    Ext.MessageBox.show({
                                        title: 'process failure',
                                        msg: 'The selected file is missing process information. Please click process again. If problem still exists please contact Chox support.',
                                        width:300,
                                        buttons: Ext.MessageBox.OK,
                                        icon : Ext.MessageBox.ERROR,
                                        fn : function reloadUploadedFileGridData(btn){
                                            if(btn=='ok'){
                                                loadUploadedFiles();
                                            }
                                        }
                                    });
                                } else{
                                    Ext.MessageBox.show({
                                        title: 'process failure',
                                        msg: 'The selected file has already been processed.',
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
                        if(sm.getSelected()){
                            if(sm.getSelected().get('processed')==true || (sm.getSelected().get('status')=="Processing..")){
                                Ext.MessageBox.show({
                                    title: '',
                                    msg: 'Processed files cannot be removed from the system',
                                    width:300,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.INFO
                                });
                            }else{
                            
                                uploadedFileGrid.getGridEl().mask();
                                choxExtAjaxRequest({
                                    url: '/prv/p/deleteUploadedFile.action',
                                    timeout:480000,
                                    callback : function(options,success,response){
                                        uploadedFileGrid.getGridEl().unmask();
                                        defaultDays=1;
                                        loadUploadedFiles();
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
                                                        msg: 'Unexpected Error occurred. Please report to chox admin.',
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
                                                title: 'Response failure',
                                                msg: 'Response from server failured because the server is handling too many requests.',
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
                },'->',{
                    text : 'Files Uploaded Today',
                    id : 'fileUploadUploadTodayButtonId',
                    handler : function() {
                        uploadedFileGrid.setTitle('Files Uploaded Today');
                        defaultDays=1;
                        loadUploadedFiles();
                        emptyClaimsDetailGrid();
                    }
                },'-','',{
                    text : 'Files Uploaded In Last 7 days',
                    id : 'fileUploadIn7DaysButtonId',
                    handler : function() {
                        uploadedFileGrid.setTitle('Files Uploaded In Last 7 Days');
                        defaultDays=6;
                        loadUploadedFiles();
                        emptyClaimsDetailGrid();
                    }
                },'-','',{
                    text : 'Files Uploaded In Last 30 days',
                    id : 'fileUploadIn30DaysButtonId',
                    handler : function() {
                        uploadedFileGrid.setTitle('Files Uploaded In Last 30 Days');
                        defaultDays=29;
                        loadUploadedFiles();
                        emptyClaimsDetailGrid();
                    }
                },'-','',{
                    text : 'All Uploaded Files',
                    id : 'fileUploadAllButtonId',
                    handler : function() {
                        uploadedFileGrid.setTitle('All Uploaded Files');
                        defaultDays = 999;
                        loadUploadedFiles();
                        emptyClaimsDetailGrid();
                    }
                }
            ]
        });

        var exortToExcelTbar = new Ext.Toolbar({
            items:['->',{
                    text:'Export To Excel',
                    id : 'xmlUploadClaimsExportToExcelButtonId',
                    handler : doExportUploadedClaimDetailsToExcel
                }]
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
                {name:'valid'},
                {name:'description'}

            ]
        });

        uploadedFileData = new choxDataStore({
            url: '/prv/p/getUploadedfiles.action', 
            timeout:60000,
            reader:uploadedFileJsonReader,
            //baseParams:{"days":defaultDays, start:start, limit:recordPerPage},
            remoteSort: true
            ,listeners:  {
                            load: function( store, records, options){
                                if(canSelectRow){
                                    SelectLastSelectedRow();
                                    canSelectRow=false
                                }
                            },
                            beforeload : function(scope,options){
                                uploadedFileData.baseParams = {"days":defaultDays};
                            }
            }
        });

        uploadedFileData.setDefaultSort('createdDate', 'desc');

        var pagingBar = new Ext.PagingToolbar({
            pageSize: xmlUploadFileGridRecordPerPage,
            store: uploadedFileData,
            displayInfo: true,
            displayMsg: 'Displaying Files {0} - {1} of {2}',
            emptyMsg: "No Files to display",
            plugins: new Ext.ux.ProgressBarPager()
        });

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
            bbar: pagingBar,
            title:'Files Uploaded Today',
            deferRowRender:false,
            enableColumnMove: false,
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

            ],
            width:990,
            height:210
        });

        
        defaultDays=1;
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
                {name : 'supplierReferenceNumber'},
                {name : 'claimStatus'},
                {name : 'processStatus'},
                {name : 'remark'},
                {name : 'message'},
                {name : 'claimId'},
                {name : 'valid'},
                {name : 'breFailureMessages'}
            ]
        });

        xmlClaimsStatusData = new choxDataStore({
            id : 'xmlClaimsStatusDataId',
            url: '/prv/p/getUploadedClaimsDetails.action', 
            timeout:60000,
            reader:xmlClaimsStatusJsonReader,
            listeners:  {load: function( store, records, options){
                    totalRecordLoaded = store.getCount();
                    if(processing==true){
                        setGridHeight(totalRecordLoaded);
                        xmlClaimsStatusGrid.setTitle(totalRecordLoaded + ' of '+selectedFileTotalClaims+' Claims have been processed');
                    } }}
        });

        xmlClaimsStatusGrid = new Ext.grid.GridPanel({
            id : 'xmlClaimsStatusGridId',
            listeners:  {cellclick: ClaimsOnClick },
            //            selModel : claimsDetailGridSelectionModel,
            store: xmlClaimsStatusData,
            renderTo:'xmlClaimsStatusGrid',
            //bbar : exportToExcelButtonBar,
            enableHdMenu:false,
            layout:'fit',
            viewConfig:{forceFit:true},
            title:'Uploaded Claim Details',
            tbar : exortToExcelTbar,
            columns: [
                //                claimsDetailGridSelectionModel,
                new Ext.grid.RowNumberer({width:40}),
                {header: "Supplier Reference", width:100, dataIndex: 'supplierReferenceNumber', sortable: true, resizable: true,
                    // if( (r.data['claimStatus']!=null && r.data['claimStatus']!='' && r.data['claimStatus']!='N/A' ) || r.data['valid'] )
                    renderer:function(value,p,r){ if( r.data['claimId']>0 ){
                            return '<span style="text-decoration: underline; color: #15428B; font-size:12px; cursor: pointer;">' + value + '</span>';}
                        else{return r.data.supplierReferenceNumber}}},
                {header: "Claim Status", width:150, dataIndex: 'claimStatus', sortable: true, resizable: true},
                {header: "Process Status", width:150, dataIndex: 'processStatus', sortable: true, resizable: true},
                {header: "Remark",  width:180, dataIndex: 'remark', sortable: true, resizable: true},
                {header: "Error Message", width:210, dataIndex: 'message', sortable: true, resizable: true},
                {header: "BRE Failure Message", width:210, dataIndex: 'breFailureMessages', sortable: true, resizable: true}
            ],
            width:1270
            ,height:50
        });

      
        
        var op = {
            beforeSubmit: onBeforeSubmit,
            success: UploadedFileAfterSubmit
        };

        $("form#uploadClaimForm").validate(
        {
            errorLabelContainer: "#uploadFormMsgBox",
            rules: {

                uploadedFile :{ required:true }
            },
            messages:
                {

                uploadedFile : {required:"You must select an XML Bordereau."}
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
//                    $(form).ajaxSubmit(op);
                    choxJqueryAjaxSubmit($(form), op);
                }
            }
        });
    });

    var loadLiveClaimData = function loadLiveUploadedClaimsDetails(){
        if(selectedFileTotalClaims>totalRecordLoaded){
            processing = true;
            loadLiveProcessedClaimDetails(selectedFileId);
            claimDetailsGridRowColourRenderer();
        }else{
            setGridHeight(totalRecordLoaded);
            processing = false;
            intervelId=window.clearInterval(intervelId);
            xmlClaimsStatusGrid.setTitle('All '+ totalRecordLoaded + ' Claims have been processed');
            totalRecordLoaded=0;
            processStatus=0;
            uploadedFileGrid.getGridEl().unmask();
            defaultDays=1;
            canSelectRow=true;
            loadUploadedFiles();
            loadQueueGrid();
        }
    
    }

    function setGridHeight(columnSize){
        var heightSize=50
        if(columnSize>0){
            heightSize = heightSize + columnSize*28;
            if(heightSize<140){
                heightSize = 140
            }
            if(heightSize>530){
                heightSize=530;
            }
        }
        xmlClaimsStatusGrid.setHeight(heightSize);
    }

    function loadLiveProcessedClaimDetails(id){
        xmlClaimsStatusData.load({
            params:{
                bordereauId:id
            }
        });
    }

    function loadProcessedClaimDetails(id){
        xmlClaimsStatusData.load({
            params:{
                bordereauId:id
            },
            //            add : true,
            callback :  function(options,success,response){ xmlClaimsStatusGrid.getGridEl().unmask();}
        });
    }

    function updateUploadedClaimsDetailStatus(totalamount,received){
        xmlClaimsStatusGrid.setTitle(received+ ' of '+totalamount+' Claims have been processed');
    }

    function showUploadedClaimsDetailStatusBar(totalamount,received,valid){
        if(totalamount==0 && valid ){
            xmlClaimsStatusGrid.setTitle('No processed information available as this file was processed prior to the XML upload function which stores processed claim details being implemented.');
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
            }else if(status=='ManualInvoiceBRERejected'){
                return 'red-row';
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
            } else if(status=='All Rejected' || status=='ALLREJECTED' || status=='ERROR' || status=='Failed') {
                return 'red-row';
            }else if(record.data.valid==false || status=='Error'){
                return 'gray-row'
            }
        };
    }

    function validateFileExtension(fileName) {
        var exp = /^.*.(xml|XML)$/;
        return exp.test(fileName);
    }

    function loadUploadedFiles(){
        resetUploadedFileForm();
        uploadedFileData.load({ params: { start:start, limit:xmlUploadFileGridRecordPerPage}});
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
//                window.location = '<%=request.getContextPath()%>/j_spring_security_logout';
                logout();
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
        uploadedFileGrid.setTitle('Uploaded Files (Today)');
        defaultDays=1;
        loadUploadedFiles();
        emptyClaimsDetailGrid();
        //        xmlClaimsStatusGrid.setHeight(50);
        //        xmlClaimsStatusData.removeAll();
        //        xmlClaimsStatusGrid.setTitle("Uploaded Claim Details");
    }
    function ClaimsOnClick(grid, rowIndex, columnIndex){
        if (columnIndex == 6 ||columnIndex == 3 ||columnIndex == 4 ||columnIndex == 5 ||columnIndex == 7) {
            var task = xmlClaimsStatusGrid.getStore().getAt(rowIndex);
            var title="Uploaded Claim Status";
            var msg = "<b>Supplier Reference</b>: " + task.get("supplierReferenceNumber");
            msg += "<br/><b>Claim Status</b>: " + task.get("claimStatus");
            msg += "<br/><b>Process Status</b>: " + task.get("processStatus");
            msg += "<br/><b>Remark</b>: " + task.get("remark");
            msg += "<br/><b>Error Message</b>:" + getFormatedErrorMessage(task.get("message"));
            msg += "<br/><b>BRE Failure Message</b>:" + task.get("breFailureMessages").replace(/BRE Rule Failed/g,"<br />BRE Rule Failed");
            Ext.MessageBox.show({
                title: title,
                msg: msg,
                width : 1270,
                buttons: Ext.MessageBox.OK
            });

            
        }
        if(columnIndex == 1){
            var record = grid.getStore().getAt(rowIndex);
            if(record.get('claimId')>0){
                Ext.get('inboxScreenDiv').mask("loading claim details ...");
                loadClaimDetail(record.get('claimId'));
            }
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
            msg += "<br/><b>Error Message</b>: " + task.get("message") ;
            propmtMsg(title, msg);
        }
    }

    function getFormatedErrorMessage(msg){
        if(msg.length > 0){
            var errorMessageList=msg.split('.,');
            var messageerrorHTML="";
            if(errorMessageList.length>0){
                for(var i=0;i<errorMessageList.length;i++){
                    if((i+1) < errorMessageList.length){
                        messageerrorHTML+=(errorMessageList[i]+'<br/>');
                    }else{
                        messageerrorHTML+=(errorMessageList[i]);
                    }
                }
                return messageerrorHTML;
            }  
        }else{
            return "";
        }
    }
    
    function emptyClaimsDetailGrid(){
        xmlClaimsStatusData.removeAll();
        xmlClaimsStatusGrid.setTitle("Uploaded Claim Details");
        xmlClaimsStatusGrid.setHeight(50);
        //        $("#UploadedClaimDetailsExportId").hide();
    }
    
    function doExportUploadedClaimDetailsToExcel(){
        if(sm.getSelected()){
            if(xmlClaimsStatusData.getCount()<=0){
                if(!sm.getSelected().get('processed')){Ext.Msg.alert('','The selected file has not been processed yet.');}
                else{
                    Ext.Msg.alert('','No processed information available.');
                }
                //                Ext.Msg.alert('','No File has been Selected or the selected file has not been processed');
            }else if(sm.getSelected().get('processed')){
                if(sm.getSelected().get('id')>0){
                    window.location= "generateExcelReportForProcessedClaimDetails.action?bordereauId="+sm.getSelected().get('id');
                }else{
                    Ext.MessageBox.show({
                        title: '',
                        msg: 'Please re-select the file to Export To Excel',
                        width:300,
                        buttons: Ext.MessageBox.OK
                    });
                }
            
           
            }else{
                Ext.MessageBox.show({
                    title: '',
                    msg: 'Please wait, claims are being processed',
                    width:300,
                    buttons: Ext.MessageBox.OK,
                    icon : Ext.MessageBox.ERROR
                });
        
            }
        }else{
            Ext.MessageBox.show({
                title: '',
                msg: 'Please select the file to \'Export To Excel\'',
                width:300,
                buttons: Ext.MessageBox.OK
            });
        }
    }
    
    function SelectLastSelectedRow(){
        sm.selectRow(lastSelectedFile);
    }


</script>
 <!--<form autocomplete="off" id="generateUploadedClaimsDetailFormId" action="<%= request.getContextPath()%>/prv/p/generateExcelReportForProcessedClaimDetails.action"  method="post">
    <input type="hidden"  name="jsonData" value="" id="jsonDataId" />
</form> -->

<div class="claim-detail-tab">

    <form id="uploadClaimForm" name="uploadClaimForm" action="<%= request.getContextPath()%>/prv/p/uploadNewClaimsFile.action?${_csrf.parameterName}=${_csrf.token}" method="POST" enctype="multipart/form-data">
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
    </form>
    <div id="uploadedFileGrid"></div>
</div>