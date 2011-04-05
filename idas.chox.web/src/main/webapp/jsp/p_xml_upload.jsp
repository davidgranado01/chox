<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var uploadedFileJsonReader;
    var uploadedFileData;
    var uploadedFileGrid;
    var xmlClaimsStatusJsonReader;
    var xmlClaimsStatusData;
    var xmlClaimsStatusGrid;


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

        var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true, header: ' '});
        var tbar = new Ext.Toolbar({
            items:[{
                    text:'Process',
                    handler : function() {

                        Ext.MessageBox.alert('Status', 'Sorry this function is not yet implemented.' );
                        //                        cb.billingFormObj.getForm().reset();
                        //                        cb.billingWindowObj.show();
                    }
                
                },'-','',{
                    text:'Delete ',
                    handler : function() {
                        Ext.MessageBox.alert('Status', 'Sorry this function is not yet implemented.' );
                        //                        var selected = cb.schSel.getSelected();
                        //                        if( selected ){
                        //                            var rptName;
                        //                            if ( Chox.billing.billingmode =='insurer'){
                        //                                rptName = 'BillingInsurerReport-Excel';
                        //                            }else{
                        //                                rptName = 'BillingChoReport-Excel';
                        //                            }
                        //                            var rpthref = Chox.appname+ '/prv/p/exportExcelReport.action?reportName=' + rptName +'&' +Ext.urlEncode(selected.data);//+dtstr;
                        //
                        //                            location.href = rpthref;
                        //                        }
                    }
                }
            ]
        });

        uploadedFileJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'fileName'},
                {name:'createdDate'},
                {name:'status'}

            ]
        });

        uploadedFileData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getUploadedfiles.action', method:'POST'}),
            reader:uploadedFileJsonReader
        });


        uploadedFileGrid = new Ext.grid.GridPanel({
            //listeners:  {cellclick:uploadedFileOnClick },
            loadMask:true,
            store: uploadedFileData,
            renderTo:'uploadedFileGrid',
            enableHdMenu:false,
            layout:'fit',
            viewConfig:{forceFit:true},
            selModel : sm,
            tbar:tbar,
            columns: [
                sm,
                {header: "File Name", width: 250, dataIndex: 'fileName', sortable: true, resizable: true},
                {header: "Status", width: 150, dataIndex: 'status', sortable: true, resizable: true},
                {header: "Created Date", width: 150, dataIndex: 'createdDate', sortable: true, resizable: true}
                //                {header: "", width: 60, dataIndex: 'delete', sortable: false, resizable: false, renderer:function(value,p,r){
                //                        return "<a href='#' class='high-light-item'>" + value + "</a>"}}
            ],
            width:990,
            height:160
        });

        uploadedFileData.setDefaultSort('createdDate', 'desc');

        loadUploadedFiles();
        
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

    function validateFileExtension(fileName) {
        var exp = /^.*.(xml|XML)$/;
        return exp.test(fileName);
    }

    function loadUploadedFiles(){
        resetUploadedFileForm();
        uploadedFileData.load();
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
                    Ext.MessageBox.show({
                        title: 'Upload successful',
                        msg: response.result,
                        width:300,
                        buttons: Ext.MessageBox.OK
                    });
                }else if(!response.result){

                    Ext.MessageBox.show({
                        title: 'Upload failure',
                        msg: 'File size exceeded 5 MB limit.',
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
                                Maximum attachment size is 5 MB. <br/>
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