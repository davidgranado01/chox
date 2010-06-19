<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    
    var attachmentJsonReader;
    var attachmentData;
    var attachmentGrid;

    $(function(){

        // GENERATE HELP NOTES
        createHelpNote();

        // DECLARE FOR VALIDATIOn
        var form = $("form#attachmentForm");

        form.validate(
        {
            errorLabelContainer: "#attachmentFormMsgBox",
            rules: {
                remark:{ required:true },
                attachmentFile:{ required:true }
            },
            messages:
                {
                remark: {required:"You must supply a value for 'Remark'"},
                attachmentFile: {required:"You must select an Attachment"}
            }
        });

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
            enableHdMenu:false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "File Name", width: 250, dataIndex: 'fileName', sortable: true, resizable: true},
                {header: "Category", width: 150, dataIndex: 'category', sortable: true, resizable: true},
                {header: "Description", width: 300, dataIndex: 'remarks', sortable: true, resizable: true},
                {header: "Created Date", width: 150, dataIndex: 'modifiedDate', sortable: true, resizable: true},
                {header: "", width: 60, dataIndex: 'delete', sortable: false, resizable: false, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>" + value + "</a>"}}
            ],
            width:950,
            height:160
        });
        
        loadAttachments();
        
    });

    function loadAttachments(){
        resetAttachmentForm();
        attachmentData.load({params:{claimId : <s:property value="claimId" />}});
    }

    function attachmentOnClick(grid, rowIndex, columnIndex, e){

        var attachment = attachmentGrid.getStore().getAt(rowIndex);
        var fileId = attachment.get("id");

        if(columnIndex!=4){
            var link = "<%= request.getContextPath()%>/prv/p/doExportAttachment.action?fileId=" + fileId+"&claimId="+<s:property value="claimId" />;
            window.open(link,"","width=600,height=800,status=yes,menubar=no,scrollbars=1,resizable=1");
        }else{
            deleteAttachment(fileId);
        }
    }

    function deleteAttachment(a){
        if(confirm("Are you sure you want to delete this attachment?")){
            $(".chox-form-submit-result").html("");
            var url = "<%= request.getContextPath()%>/prv/p/doDeleteAttachment.action";
            var param = {"fileId":a,"claimId":<s:property value="claimId" />};
            ajax.loadJson2(url, param, loadAttachments);
        }
    }

    function doClaimAttachmentSubmit(){

  
        var uploadFile = $("#attachmentFile").val();
        if((uploadFile.lastIndexOf("."))>0){
            var filename = uploadFile.substr(uploadFile.lastIndexOf('\\')+1, uploadFile.length);
            $("#uploadFileName").val(filename);
        }

        var options = {
            beforeSubmit: ui.onBeforeSubmit,
            success: attachmentUploadAfterSubmit,
            timeout: 50000,
            error: ui.onSubmitError
        };
        
        $("form#attachmentForm").ajaxSubmit(options);

    }

    function attachmentUploadAfterSubmit(responseText, statusText){
        loadAttachments();
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
                target: 'attachmentTypeSpan',
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
                <legend>Add a new Attachment&nbsp;</legend>
                <table class="chox-form-item" cellpadding="0" cellspacing="0" border="0" width="100%">
                    <tr>
                        <td width="200" align="right">
                            <label class="std-label-ro">File&nbsp;&nbsp;</label>
                        </td>
                        <td>
                            <s:file id="attachmentFile" name ="attachmentFile" label ="Attachment" cssStyle="height: 20px;" size="40"/>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <div class="column-remark" style="padding:10px 0 10px 0;">
                                Maximum attachment size is <s:property value="maxFileSize/1000/1024"/> MB. <br/>
                                Currently, CHOX supports attachments in the following formats: <br/>
                                <s:property value="AllowFileTypeHelpNote"/>&nbsp;&nbsp;<img src="../images/help.png" id="attachmentTypeSpan" alt=""/>
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
                        <td align="right" valign="top"><label class="std-label-ro">Remark&nbsp;&nbsp;</label></td>
                        <td>
                            <s:textarea rows="3" cols="30" id="remark" name="remark" label="Remark:"/>
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td>
                            <input type="button" value="Add Attachment" onclick="javascript:doClaimAttachmentSubmit()"/>
                        </td>
                    </tr>
                </table>
                <div class="chox-form-submit-result"/>
                <div class="action-error-msg" id="attachmentFormMsgBox"/>
            </fieldset>
        </div>
    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
    <!--s:token/-->
    </form>
    <div id="attachmentGrid"></div>
</div>