<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var commentsJsonReader;
    var commentsDataStore;
    var commentsGrid;

    commentsJsonReader = new Ext.data.JsonReader({
        totalProperty: 'totalCount', root: 'results', fields:[
            {name:'id'},
            {name:'createdBy'},
            {name:'createdDate'},
            {name:'comment'},
            {name:'visibilityType'}]
    });

    commentsDataStore = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({url: '<%= request.getContextPath()%>/prv/p/getComments.action',method:'GET'}), reader:commentsJsonReader
    });

    commentsGrid = new Ext.grid.GridPanel({
        listeners:  {cellclick:loadComment },
        store: commentsDataStore, loadMask: true,
        columns: [
            {header: "Created", width: 130, dataIndex: 'createdDate', sortable: false, resizable: true},
            {header: "Created By", width: 260, dataIndex: 'createdBy', sortable: false, resizable: true},
            {header: "Message", width: 700, dataIndex: 'comment', sortable: false, resizable: true}
        ],
        viewConfig:{
            getRowClass: function(record, index) {
                var c = record.get('visibilityType');
                if(c>0){
                    return 'private-comment';
                }

            }
        },
        renderTo:'commentsGrid', width:960, autoHeight:true, enableHdMenu:false
    });
            
    /*
    $(document).ready(function() {

        var optionsComment = {
            success: showResponse
        };

        $('#fComments').ajaxForm(optionsComment);

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
     */
   
</script>
<div class="claim-detail-tab">

    <form id="claimCommentForm" name="claimCommentForm" action="<%= request.getContextPath()%>/prv/p/createNewComment.action" method="POST">
        <fieldset class="x-fieldset">
            <legend>Add a new note</legend>
            <div class="chox-form-item">
                <s:textarea id="commentBox" cols="70" rows="4" id="commentBox" name="comment" />
            </div>
            <div class="chox-form-item">
                <s:if test="isInsurer">
                    <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityType" value="0" title="All" checked="true"/> Public Note (Visible By CHO)</span>
                    <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityType" value="1" title="Insurer only"/> Private Note (Only Visible Internally)</span>
                    </s:if>
                    <s:elseif test="isCHO">
                    <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityType" value="0" title="All" checked="true"/> Public Note (Visible By Insurer)</span>
                    <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityType" value="2" title="Credit Hire only"/> Private Note (Only Visible Internally)</span>
                    </s:elseif>
                    <s:else>
                    <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityType" value="0" title="All" checked="true"/> Public Note</span>
                    <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityType" value="1" title="Insurer only"/> Private Note (Only Visible By Insurer)</span>
                    <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityType" value="2" title="Credit Hire only"/> Private Note (Only Visible By CHO)</span>
                    </s:else>
            </div>
            <input type="submit" id="bAddComment" value="Add Note" onclick="javascript:return commentFormValidation();"/>
        </fieldset>
    </form>
    <div class="action-error-msg" id="CmErrMsgBox" style="color:red;font-weight: bold;font-size: 10px;"></div>
    <div class="remark-indicator">Private notes are highlighted in blue</div>
    <div id="commentsGrid"></div>
</div>