<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var commentsJsonReader;
    var commentsDataStore;
    var commentsGrid;

    Ext.onReady(function(){

        // SET VALIDATION
        var form = $("form#claimCommentForm");
        form.validate(
        {
            errorLabelContainer: "#claimCommentFormMsgBox",
            rules: {
                comment:{ required:true }
            },
            messages:
                {
                comment: {required:"Note blank - Please enter text in the Note field and then click on 'Add Note'" }
            }
        });

        ui.ajaxForm(form, onAfterFormSubmit);

        // LOAD RECORDS
        commentsJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount', root: 'results', fields:[
                {name:'id'},
                {name:'createdBy'},
                {name:'createdDate', type: 'date',  dateFormat: 'd/m/Y H:i'},
                {name:'comment'},
                {name:'visibilityType'},
                {name:'delete'}]
        });

        commentsDataStore = new choxDataStore({
            url: '/prv/p/getComments.action',
            reader:commentsJsonReader
        });

        commentsDataStore.setDefaultSort('id', 'desc');
        var dateRenderer = Ext.util.Format.dateRenderer('d/m/Y H:i');

        commentsGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:commentOnClick},
            loadMask:true,
            store: commentsDataStore,
            renderTo:'commentsGrid',
            enableHdMenu:false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Created", width: 130, dataIndex: 'createdDate', sortable: true, resizable: true, renderer: dateRenderer},
                {header: "Created By", width: 260, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Message", width: 540, dataIndex: 'comment', sortable: true, resizable: true},
                {header: "", width: 60, dataIndex: 'delete', sortable: false, resizable: false, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>" + value + "</a>"}}
            ],
            viewConfig:{
                getRowClass: function(record, index) {
                    var c = record.get('visibilityType');
                    if(c>0){
                        return 'private-comment';
                    }
                }
            },
            width:990,
            height:300
        });

        loadComments();
        
    });
    
    
    function commentOnClick(grid, rowIndex, columnIndex, e){
        var comment = commentsGrid.getStore().getAt(rowIndex);
        var fileId = comment.get("id");
        
        if(columnIndex == 2){
            var title="Notes";
            var msg = "<b>Created Date</b>: " + comment.get("createdDate");
            msg += "<br/><b>Created By</b>: " + comment.get("createdBy");
            msg += "<br/><b>Message";

            if(comment.get("visibilityType")>0){
                msg += " (Private Note)";
            }
            msg += "</b>: <br/>" + comment.get("comment");
            propmtMsg(title, getFormatedMessage(msg));
        }
        else if(columnIndex == 3 && comment.get("delete")!=""){
            deleteComment(fileId);
        }
    }
    
    function deleteComment(a){
        
        var box= Ext.Msg.show({
            title      : 'Confirm',
            msg        : 'Are you sure you want to delete this note?',
            width      : 400,
            buttons    : Ext.MessageBox.OKCANCEL,
            fn         : function(btn) {
                if(btn=='ok') {
                    var url = "/prv/p/doDeleteComment.action";
                    var param = {"commentId":a,"claimId":<s:property value="claimId" />};
                    ajax.loadJson2(url, param, function(data){
                        Ext.MessageBox.show({
                            title: '',
                            msg: data.result,
                            width:300,
                            buttons: Ext.MessageBox.OK
                        });
                        refereshComments();
                    });
                    
                }
            }
        });
    }
    
    function onAfterFormSubmit(responseText, statusText,form,responseType) {
        var response = eval('(' + responseText.trim() + ')');
        if(response && !response.isValid){
            $.each(response.errors, function() {
                Ext.Msg.show({
                    title: 'Error',
                    msg:this.toString(),
                    icon:Ext.Msg.ERROR,
                    buttons:Ext.Msg.OK,
                    width : 400
                });
            });
        } else {
            $("form#claimCommentForm").each(function(){
                this.reset();
            });
        }
        loadComments();
    }

    function getFormatedMessage(msg){
        if(msg.length > 0){
            msg = msg.replace('An invoice amendment has been made to the following fields:', 'An invoice amendment has been made to the following fields:<br/>');
            msg = msg.replace('Invoice Details:', '<b>Invoice Details: </b><br/>');
            msg = msg.replace('Hire Vehicle Details:', '<b>Hire Vehicle Details: </b><br/>');
            msg = msg.replace('Engineer Report:', '<b>Engineer Report: </b><br/>');
            var messageList=msg.split('). ');
            var messageHTML="";
            if(messageList.length>0){
                for(var i=0;i<messageList.length;i++){
                    if((i+1) < messageList.length){
                        messageHTML+=(messageList[i]+')<br/>');
                    }else{
                        messageHTML+=(messageList[i]);
                    }
                }
                return messageHTML;
            }  
        }else{
            return "";
        }
    }
    
    function loadComments(){
        commentsDataStore.load({params:{claimId : <s:property value="claimId" />}});
        // setting notestabloaded = true, will enable notes tab grid panel to reload every time notes tab clicked.'
        // notesTabLoaded flag is used to find this page is loaded from p_claim_detail.jsp page.
        notesTabLoaded = true;
    }
    
    function refereshComments(){
        commentsDataStore.load({params:{claimId : <s:property value="claimId" />}});
    }
   
</script>
<div class="claim-detail-tab">

    <form id="claimCommentForm" name="claimCommentForm" action="<%= request.getContextPath()%>/prv/p/createNewComment.action" method="POST">
        <div class="form-container">
            <input name="claimId" id="claimId" type="hidden" value="<s:property value="claimId" />" />
            <input name="name" id="activityNameId" type="hidden" value="addNote" />
            <fieldset class="x-fieldset">
                <legend>Add New Note</legend>
                <div class="chox-form-item">
                    <s:textarea cols="100" rows="5" id="comment" disabled="isChoxAdmin" name="comment" />
                </div>

                <div class="chox-form-item">
                        <s:if test="isInsurer">
                            <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityType" value="0" title="All" checked="true" <s:if test="insurerIsDisablePrivateNotes">disabled="disabled"</s:if>/> Public Note (Visible By CHO)</span>
                            <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityType" value="1" title="Insurer only" <s:if test="insurerIsDisablePrivateNotes">disabled="disabled"</s:if>/> Private Note (Only Visible Internally)</span>
                        </s:if>
                        <s:elseif test="isCHO">
                            <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityType" value="0" title="All" checked="true" <s:if test="choIsDisablePrivateNotes">disabled="disabled"</s:if>/> Public Note (Visible By Insurer)</span>
                            <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityType" value="2" title="Credit Hire only" <s:if test="choIsDisablePrivateNotes">disabled="disabled"</s:if>/> Private Note (Only Visible Internally)</span>
                        </s:elseif>
                        <s:elseif test="isChoxAdmin">
                            <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityType" value="0" title="All" checked="true" disabled="disabled" /> Public Note (Visible By Insurer)</span>
                            <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityType" value="2" title="Credit Hire only" disabled="disabled" /> Private Note (Only Visible Internally)</span>
                        </s:elseif>
                </div>
                    <s:submit type="submit" id="claimDetailsCommentId" disabled="isChoxAdmin" value="Add Note"/>
                <div class="action-error-msg" id="claimCommentFormMsgBox"></div>
            </fieldset>
        </div>
    </form>
    <div class="remark-indicator">Private notes are highlighted in blue</div>
    <div id="commentsGrid"></div>
</div>