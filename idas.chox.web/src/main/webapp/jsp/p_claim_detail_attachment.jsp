<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var attachmentJsonReader;
    var attachmentData;
    var attachmentGrid;
    
    Ext.onReady(function(){

        // GENERATE HELP NOTES
        createHelpNote();

        // DECLARE FOR VALIDATION
        

        attachmentJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'fileName'},
                {name:'category'},
                {name:'remarks'},
                {name:'modifiedDate', type: 'string', dateFormat:'timestamp'},
                {name:'delete'}
            ]
        });

        attachmentData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getAttachments.action', method:'POST'}),
            reader:attachmentJsonReader
        });

        attachmentData.setDefaultSort('modifiedDate', 'desc');

        attachmentGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:attachmentOnClick },
            store: attachmentData,
            renderTo:'attachmentGrid',
            enableColumnMove: false,
            enableHdMenu:false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "File Name", width: 250, dataIndex: 'fileName', sortable: true, resizable: true},
                {header: "Attachment Type", width: 150, dataIndex: 'category', sortable: true, resizable: true},
                {header: "Description", width: 300, dataIndex: 'remarks', sortable: true, resizable: true},
                {header: "Created Date", width: 150, dataIndex: 'modifiedDate', sortable: true, resizable: true},
                {header: "", width: 60, dataIndex: 'delete', sortable: false, resizable: false, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>" + value + "</a>";}}
            ],
            width:990,
            height:160
        });

        loadAttachments();

        var remarkField = new Ext.form.TextArea({
            name             : 'remark',
            width            :  350,
            height           :  80,
            allowBlank       :  false,
            renderTo         : 'RemarkFieldId'
        });

        var uploadFileField = new Ext.form.TextField({
            name             : 'attachmentFile',
            id               : 'attachmentFile',
            width            :  300,
            allowBlank       :  false,
            inputType        : 'file',
            renderTo         : 'FileUploadId'

        });

        var op = {
            beforeSubmit: onBeforeSubmit,
            success: attachmentUploadAfterSubmit
        };

        $("form#attachmentForm").validate({
            errorLabelContainer: "#attachmentFormMsgBox",
            rules: {
                remark:{ required:true },
                attachmentFile:{ required:true }
            },
            messages: {
                remark: {required:"You must supply a value for 'Description'"},
                attachmentFile: {required:"You must select an Attachment"}
            },

            submitHandler: function(form) {

                var uploadFile = Ext.getDom('attachmentFile').value;
                if((uploadFile.lastIndexOf("."))>0){
                    var filename = uploadFile.substr(uploadFile.lastIndexOf('\\')+1, uploadFile.length);
                    $("#uploadFileName").val(filename);
                }
                if (!validateFileExtension(uploadFile)) {
                    Ext.MessageBox.alert('Sorry this file type is not allowed',
                    '<br> Currently, CHOX supports attachments in the following formats only: </br>.doc, .docx, .jpeg, .jpg, .pdf, .rtf, .tif, .tiff, .txt, .xls, .xlsx, .xml, .zip');
                    return;
                }else{
                    $(form).ajaxSubmit(op);
                }
                
            }
        });
    });

    function onSubmitError(XMLHttpRequest,responseText, textStatus, errorThrown) {

        var response = eval('(' + responseText.trim() + ')');

        Ext.MessageBox.show({
            title: 'Upload failure',
            msg: response.errors,
            width:300,
            buttons: Ext.MessageBox.OK,
            icon : Ext.MessageBox.ERROR
        });

    }
    
    function attachmentUploadAfterSubmit(responseText, statusText, form, responseType){
        
        onFormSubmitCompleted(responseText, statusText, form, responseType);
        Ext.get('attachmentForm').unmask();
        loadAttachments();
        $("#formSubmitResultId").fadeOut(10000);
    }
    
    function onFormSubmitCompleted(responseText, statusText, form, responseType)  {

        if (responseText.indexOf('You have been denied access') !==-1) {
            Ext.MessageBox.alert('Error', 'You have been denied access and will now be logged out', function() {
                window.location = '<%=request.getContextPath()%>/j_spring_security_logout';
                return;
            });
        }
        if(responseText){
            var response = eval('(' + responseText.trim() + ')');
            if(response && response.isValid){
                if(response.resultType && response.resultType === 'Message'){
                    Ext.MessageBox.show({
                        title: 'Upload successful',
                        msg: response.result,
                        width:300,
                        buttons: Ext.MessageBox.OK
                    });
                }
                else if(!response.result){

                    Ext.MessageBox.show({
                        title: 'Upload failure',
                        msg: 'File size exceeded 20 MB limit.',
                        width:300,
                        buttons: Ext.MessageBox.OK,
                        icon : Ext.MessageBox.ERROR
                    });
                }
            }
            else if(response.errors){
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

    function onBeforeSubmit(){
        Ext.get('attachmentForm').mask('Please wait, file is being uploaded...');
        return true;
    }

    function validateFileExtension(fileName){
        var exp = /^.*.(jpg|JPG|png|PNG|xls|XLS|doc|DOC|docx|DOCX|jpeg|JPEG|pdf|PDF|rtf|RTF|tif|TIF|tiff|TIFF|txt|TXT|xlsx|XLSX|xml|XML|zip|ZIP)$/;
        return exp.test(fileName);
    }

    function loadAttachments(){
        resetAttachmentForm();
        attachmentData.load({params:{claimId : <s:property value="claimId" />}});
    }

    function attachmentOnClick(grid, rowIndex, columnIndex, e){

        var attachment = attachmentGrid.getStore().getAt(rowIndex);
        var fileId = attachment.get("id");

        if(columnIndex!==4){
            var link = "<%= request.getContextPath()%>/prv/p/doExportAttachment.action?fileId=" + fileId+"&claimId="+<s:property value="claimId" />;
            window.open(link,"","width=600,height=800,status=yes,menubar=no,scrollbars=1,resizable=1");
        }else{
            deleteAttachment(fileId);
        }
    }

    function deleteAttachment(a){
        
        var box= Ext.Msg.show({
            title      : 'Confirm',
            msg        : 'Are you sure you want to delete this attachment?',
            width      : 400,
            buttons    : Ext.MessageBox.OKCANCEL,
            fn         : function(btn) {
                if(btn==='ok') {
                    var url = "<%= request.getContextPath()%>/prv/p/doDeleteAttachment.action";
                    var param = {"fileId":a,"claimId":<s:property value="claimId" />};
                    ajax.loadJson2(url, param, function(data){
                        Ext.MessageBox.show({
                            title: '',
                            msg: data.result,
                            width:300,
                            buttons: Ext.MessageBox.OK
                        });
                        loadAttachments();
                    });
                    
                }
            }
        });

        //    box.getDialog().setPosition(700,900);
        //    alert(box.getDialog().getPosition());

    }

   
    function hideActionResultAfter10Seconds() {
        $("#actionResultId").fadeOut(10000);
    }
    

    function resetAttachmentForm(){
        $("form#attachmentForm").each(function(){
            this.reset();
        });
    }


    function createHelpNote(){

        var attachmentHtmlDesc = "";

        attachmentHtmlDesc = "<table cellpadding='0' cellspacing='0' border='0' class='remark-table'>";
        attachmentHtmlDesc += "<tr><th width='28%'><b>Type</b></th><th width='70%'><b>Description</b></th></tr>";

    <s:iterator value="AllowFileTypes">
            attachmentHtmlDesc += '<tr>';
            attachmentHtmlDesc += '<td>.<s:property value="code"/></td>';
            attachmentHtmlDesc += '<td><s:property value="description"/></td>';
            attachmentHtmlDesc += '</tr>';
    </s:iterator>

            attachmentHtmlDesc += "</table>";

            new Ext.ToolTip({
                target: 'claimDetailAttachmentTypeSpan',
                html: attachmentHtmlDesc,
                title: 'Attachment Formats',
                autoHide: false,
                closable: true,
                draggable:true
            });

            Ext.QuickTips.init();
        }
</script>

<div class="claim-detail-tab">

    <form id="attachmentForm" name="attachmentForm" action="<%= request.getContextPath()%>/prv/p/createNewAttachment.action" method="POST" enctype="multipart/form-data">
        <div class="form-container">
            <input type="hidden" name="claimId" id="claimId" value='<s:property value="claimId" />'>
            <input type="hidden" name="uploadFileName" id="uploadFileName">
            <fieldset class="x-fieldset">
                <legend>Add New Attachment&nbsp;</legend>
                <table class="chox-form-item" cellpadding="0" cellspacing="0" border="0" width="100%">
                    <tr>
                        <td width="200" align="right">
                            <label class="std-label-ro">File&nbsp;&nbsp;</label>
                        </td>
                        <td>
                            <div id="FileUploadId"/>
                            <!--  <s:file id="attachmentFile" name ="attachmentFile" label ="Attachment" cssStyle="height: 20px;" size="40"/> -->
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <div class="column-remark" style="padding:10px 0 10px 0;">
                                Maximum attachment size is <s:property value="maxFileSize/1024/1024"/> MB. <br/>
                                Currently, CHOX supports attachments in the following formats: <br/>
                                <s:property value="AllowFileTypeHelpNote"/>&nbsp;&nbsp;<img src="../images/help.png" id="claimDetailAttachmentTypeSpan" alt=""/>
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
                        <s:if test="!insurerUploadedClaim">
                            <td align="right" valign="top"><label class="std-label-ro">Notify <s:property value="IsChoOrIns"/> Of Attachment&nbsp;&nbsp;</label></td>
                            <td>
                                <s:checkbox name="notifyTask" value="true" id="checkboxId" />
                            </td>
                        </s:if>
                        <s:else>
                            <td align="right" valign="top"><label class="std-label-ro">Notify <s:property value="IsChoOrIns"/> Of Attachment&nbsp;&nbsp;</label></td>
                            <td>
                                <s:checkbox name="notifyTask" value="false" id="checkboxId" disabled="true"/>
                            </td>
                        </s:else>    
                    </tr>
                    <tr>

                        <td align="right" valign="top"><label class="std-label-ro">Description&nbsp;&nbsp;</label></td>
                        <td>
                            <div id="RemarkFieldId"/>
                            <!-- <s:textarea rows="3" cols="30" id="remark" name="remark" label="Remark:"/> -->
                        </td>
                    </tr>

                    <tr>
                        <td>&nbsp;</td>
                        <td>
                            <input type="submit" id="claimDetailAttachmentSubmitButton" value="Add Attachment" />
                        </td>
                    </tr>
                </table>
                <div class="chox-form-submit-result" id="formSubmitResultId"/>
                <div class="action-error-msg" id="attachmentFormMsgBox"/>

                <!-- <div class="chox-form-submit-result" id="actionResultId"></div> -->
            </fieldset>
        </div>
        <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce")%>'/>
        <!--s:token/-->
    </form>
    <div id="attachmentGrid"></div>
</div>