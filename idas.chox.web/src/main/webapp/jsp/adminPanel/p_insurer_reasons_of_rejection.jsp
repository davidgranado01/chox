<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

var rorGridViewDataStore;
var rorEditWindow;
var rorGridView;

$(function(){

    var rejectionDescField = new Ext.form.TextArea({
        name             : 'rejectionDescription',
        id               : 'rejecDescId',
        width            :  350,
        height           :  80,
        allowBlank       :  false,
        renderTo         : 'rorId'
    });
    
    var rorJsonReader = new Ext.data.JsonReader({
        totalProperty: 'totalCount',
        root: 'results',
        fields:
            [
             {name:'id'},
             {name:'name'},
             {name:'description'},
             {name:'type'},
             {name:'status'},
             {name:'restricted'},
             {name:'createdBy'},
             {name:'createdDate'}
        ]
    });
    
    rorGridViewDataStore = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy
        ({url: '<%= request.getContextPath()%>/prv/p/getInsurersReasonsOfRejection.action', method:'POST'}),
        reader: rorJsonReader
    });
    
    rorGridView = new Ext.grid.GridPanel({
        listeners:  {cellclick:removeReasonOfRejection},
        store: rorGridViewDataStore,
        renderTo:'rorGridView',
        enableHdMenu:false,
        layout:'fit',
        viewConfig:{forceFit:true},
        columns: [
            {header: "Name", width: 200, dataIndex: 'name', sortable: true, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='high-light-item'>"+value+"</a>" }},
            {header: "Description", width: 160, dataIndex: 'description', sortable: true, resizable: true},
            {header: "Type", width: 160, dataIndex: 'type', sortable: true, resizable: true},
            {header: "Status", width: 160, dataIndex: 'status', sortable: true, resizable: true},
            {header: "Restricted", width: 160, dataIndex: 'restricted', sortable: true, resizable: true},
            {header: "", width: 80, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='high-light-item'>Remove</a>"}}
        ],
        height:243,
        width: 760
    });
    
    if(!rorEditWindow || rorEditWindow==null)
    {
    	rorEditWindow =  new Ext.Window({
            applyTo:'rorEditWindow',
            width:600,
            height:300,
            layout:'fit',
            modal:true,
            closeAction:'hide',
            plain: false,
            title: 'Edit Reason Of Rejection',
            resizable : false,
            items: new Ext.Panel({
                applyTo: 'rorEditPanel'
            }),
            buttons: [{
                    text:'Ok', handler: function(){
                    	if($("form#rorEditForm").valid()){
	                        var op = {
	                            success: closeWindowAndRefresh,
	                            timeout: 3000,
	                            error: ui.onSubmitError
	                        };
	
	                        $("form#rorEditForm").ajaxSubmit(op);
                    	}
                    }
                },{
                    text: 'Close', handler: function(){
                    	rorEditWindow.hide();
                    }
                }]
        });
    }
    
    var rejectionDescField = new Ext.form.TextArea({
        name             : 'reasonOfRejectionDesc',
        id               : 'rorDescTextId',
        width            :  350,
        height           :  80,
        allowBlank       :  false,
        renderTo         : 'rorDescId'
    });
    
    var rejectionDescField = new Ext.form.TextArea({
        name             : 'reasonOfRejectionDesc',
        id               : 'rorDescEditTextId',
        width            :  350,
        height           :  80,
        allowBlank       :  false,
        renderTo         : 'rorDescEditId'
    });
    
    var form = $("form#rorForm");

    form.validate(
    {
        errorLabelContainer: "#rorErrorMessageBox",
        rules: {
        	reasonOfRejectionName:{ required:true}
        },
        messages: {
        	reasonOfRejectionName: { required:"You must supply a 'Reason Of Rejection'"}
        }
    });

    ui.ajaxForm(form,function(responseText, statusText){
         console.log(responseText);
         console.log(statusText);
         
        var response = eval('(' + responseText.trim() + ')');
        if(response)
        {
            if(response.isValid){
                onPageRefresh();
            } else {
                Ext.Msg.show({
                    title: 'Error',
                    msg:response.errors,
                    icon:Ext.Msg.ERROR,
                    buttons:Ext.Msg.OK,
                    width : 400
                });
                onPageRefresh();
            }
        }
    });
    
    var editForm = $("form#rorEditForm");
    editForm.validate(
    {
        errorLabelContainer: "#rorEditErrorMessageBox",
        rules: {
        	reasonOfRejectionName:{ required:true}
        },
        messages: {
        	reasonOfRejectionName: { required:"You must supply a 'Reason Of Rejection'"}
        }
    });
    
    onPageRefresh();
});

function closeWindowAndRefresh(){
	rorEditWindow.hide();
	onPageRefresh();
}

function removeReasonOfRejection(grid, rowIndex, columnIndex, e){
    var gridView = rorGridView.getStore().getAt(rowIndex);
    if(columnIndex==5){
        var rorId = gridView.get("id");
        var url = "<%= request.getContextPath()%>/prv/p/deleteReasonOfRejection.action";
        var param = {"reasonOfRejectionId":rorId};
        ajax.loadHtml2(url, param, onPageRefresh);
    }else if(columnIndex==0){
    	showEditReasonOfRejection(gridView);
    }
}

function onPageRefresh(){
    loadGridViewList();
    refreshForm();
}

function refreshForm(){
    $("#rorId").val("");
    $("#rorDescTextId").val("");
    $("form#rorForm input#restricted").attr('checked',false);
    $("form#rorForm input#status").attr('checked',false);
    $("form#rorForm input[type='radio']").get(0).setAttribute('checked', 'checked');
    $("form#rorForm input[type='radio']").get(1).removeAttribute('checked');
}

function loadGridViewList(){
    rorGridViewDataStore.load({params:{insurerId:<s:property value="insurerId" />}});
}

function showEditReasonOfRejection(gridView){
	rorEditWindow.show();
    $("form#rorEditForm input[name$='reasonOfRejectionId']").val(gridView.get("id"));
    $("form#rorEditForm input[name$='reasonOfRejectionName']").val(gridView.get("name"));
    $("form#rorEditForm input#statusWindowId").attr('checked', gridView.get("status"));
    $("form#rorEditForm input#restrictedWindowId").attr('checked', gridView.get("restricted"));
    $("form#rorEditForm #rorDescEditTextId").val(gridView.get("description"));
    if(gridView.get("type").toLowerCase() == "claim"){
        $("form#rorEditForm  input[type='radio']").get(0).setAttribute('checked', 'checked');
        $("form#rorEditForm  input[type='radio']").get(1).removeAttribute('checked');
    } else if(gridView.get("type").toLowerCase() == "invoice"){
    	$("form#rorEditForm  input[type='radio']").get(1).setAttribute('checked', 'checked');
    	$("form#rorEditForm  input[type='radio']").get(0).removeAttribute('checked');
    }

}

</script>
<div class="sub-admin-tab-css">
    <div class="status-info">
        Status information needs confirmation!!!
    </div>

    <div id="rorGridId">
        <div class="grid-view-header">
            <table width="100%">
                <tr>
                    <td>
                        <div class="admin-bre-band-detail-section">
                            <div class="section-name">Reason Of Rejection</div>
                            <div class="form-container">
                                <form id="rorForm" name="rorForm" action="<%= request.getContextPath()%>/prv/p/addReasonOfRejection.action" class="XXentity-form" method="post">
                                    <input id="insurerId" name="insurerId" type="hidden" value="<s:property value="insurerId"/>"/>
                                    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
                                    
                                    <div class="chox-form-item" style="padding-bottom: 2px">
                                        <label class="chox-form-std-label">Reason Of Rejection<span class="mandatory">*</span></label>
                                        <input id="rorId" name="reasonOfRejectionName" style="width: 350px"/>
                                    </div>
                                    
                                    <div class="chox-form-item">
                                        <label class="chox-form-std-label">Supporting Rejection Note</label>
                                        <div id="rorDescId" style="padding-left: 12px"/>
                                    </div>
                                    <br/>
                                    <table width="100%">
	                                    <tr>
		                                    <td width="30%">
			                                    <div class="chox-form-item" id="radioBox" style="padding-left: 70px">
		                                        <span class="input-radio"> 
		                                            Claim Type - confirm message!!!
		                                            <input type="radio" name="type" checked="checked" value="claim" style="margin-left: 8px"/>
		                                            </span>
		                                        <br/>
		                                        <span class="input-radio">
		                                            Invoice Type - confirm message!!!
		                                            <input type="radio" name="type" value="invoice"/> 
		                                            </span>
		                                        </div>
		                                    </td>
			                                <td width="15%" >
                                                <label>Status</label>
                                                <s:checkbox id="status" name="status" style="margin-left: 19px"/>
                                                <br/>
                                                <label>Restricted</label>
                                                <s:checkbox id="restricted" name="restricted" />
                                            </td>
			                            </tr>
                                    </table>
                                    
                                     <div class="chox-form-button">
                                        <input type="submit" value="Add New Reason Of Rejection"/>
                                    </div>
                                    
                                    <div class="chox-form-submit-result"></div>
                                    <div id="rorErrorMessageBox" class="action-error-msg"></div>
                                     
                                </form>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
        <div id="rorGridView"/>
    </div>
    
    <div id="rorEditWindow" class="x-hidden">
        <div id="rorEditPanel">
            <div class="form-container" style="height:300px; padding-bottom:30px; padding-top:30px">
                <form id="rorEditForm" name="rorEditForm" class="XXentity-form" action="<%= request.getContextPath()%>/prv/p/updateReasonOfRejection.action" method="post">
                    <input id="reasonOfRejectionId" name="reasonOfRejectionId" type="hidden"/>
                    
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Reason Of Rejection<span class="mandatory">*</span></label>
                        <input id="rorEditId" name="reasonOfRejectionName" style="width: 350px"/>
                    </div>
                    
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Supporting Rejection Note&nbsp;&nbsp;</label>
                        <div id="rorDescEditId"/>
                    </div>
                    <br/>
                    <table width="100%">
                        <tr>
                            <td width="30%">
                                <div class="chox-form-item" id="radioBox" style="padding-left: 70px">
	                                <span class="input-radio"> 
	                                    Claim Type - confirm message!!!
	                                    <input type="radio" name="type" checked="checked" value="claim" style="margin-left: 8px"/>
                                    </span>
                                <br/>
	                                <span class="input-radio">
	                                    Invoice Type - confirm message!!!
	                                    <input type="radio" name="type" value="invoice"/> 
                                    </span>
                                </div>
                            </td>
                            <td width="15%" >
	                            <div class="chox-form-item">
	                                <label>Status</label>
	                                <s:checkbox id="status" name="status" id="statusWindowId" style="margin-left: 19px"/>
	                                <br/>
	                                <label>Restricted</label>
	                                <s:checkbox id="restricted" name="restricted" id="restrictedWindowId"/>
	                            </div>
                            </td>
                        </tr>
                        <tr>
	                        <td colspan="2">
	                           <div id="rorEditErrorMessageBox" class="action-error-msg"></div>
	                        </td>
                        </tr>
                    </table>
                    
                    <input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>
                </form>
            </div>
        </div>
    </div>
    
</div>