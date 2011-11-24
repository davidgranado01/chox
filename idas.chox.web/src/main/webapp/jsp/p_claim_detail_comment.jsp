<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var commentsJsonReader;
    var commentsDataStore;
    var commentsGrid;

    $(function(){

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

        ui.ajaxForm(form, loadComments);

        // LOAD RECORDS
        commentsJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount', root: 'results', fields:[
                {name:'id'},
                {name:'createdBy'},
                {name:'createdDate', type: 'date',  dateFormat: 'd/m/Y H:i:s'},
                {name:'comment'},
                {name:'visibilityType'}]
        });

        commentsDataStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({url: '<%= request.getContextPath()%>/prv/p/getComments.action',method:'POST'}), 
            reader:commentsJsonReader
        });

        commentsDataStore.setDefaultSort('createdDate', 'desc');
        var dateRenderer = Ext.util.Format.dateRenderer('d/m/Y H:i:s');

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
                {header: "Message", width: 700, dataIndex: 'comment', sortable: true, resizable: true}
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
    
    function commentOnClick(grid, rowIndex){
        var comment = commentsGrid.getStore().getAt(rowIndex);
        var title="Notes";
        var msg = "<b>Created Date</b>: " + comment.get("createdDate");
        msg += "<br/><b>Created By</b>: " + comment.get("createdBy") + "<br/>";
        msg += "<br/><b>Message";

        if(comment.get("visibilityType")>0){
            msg += " (Private Note)";
        }
        msg += "</b>: <br/>" + comment.get("comment");
        propmtMsg(title, msg);
    }

    function loadComments(){
        
        $("form#claimCommentForm").each(function(){
            this.reset();
        });

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
            <fieldset class="x-fieldset">
                <legend>Add New Note</legend>
                <div class="chox-form-item">
                    <s:textarea cols="100" rows="5" id="comment" disabled="isChoxAdmin" name="comment" />
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
                        <s:elseif test="isChoxAdmin">
                        <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityType" value="0" title="All" checked="true" disabled /> Public Note (Visible By Insurer)</span>
                        <span class="input-radio"><input type="radio" name="visibilityType" id="visibilityType" value="2" title="Credit Hire only" disabled /> Private Note (Only Visible Internally)</span>
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