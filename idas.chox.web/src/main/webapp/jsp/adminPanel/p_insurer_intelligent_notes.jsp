<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

var iinGridViewDataStore;
var iinGridView;
var intelligentNotePopUp;

Ext.onReady(function() {
	
	var iNotesJsonReader = new Ext.data.JsonReader({
        totalProperty: 'totalCount',
        root: 'results',
        fields:
            [
             {name:'id'},
             {name:'intelligentNoteId'},
             {name:'intelligentNoteName'},
             {name:'intelligentNote'},
             {name:'status'}
             ]
    });

	iinGridViewDataStore = new choxDataStore({
	    url: '/prv/p/getInteligentNotes.action',
	    reader: iNotesJsonReader, 
	    autoLoad: true,
	    baseParams: {insurerId: <s:property value="insurerId" />}
	});
	
	iinGridView = new Ext.grid.GridPanel({
	    listeners:  {cellclick:updateIntelligentNoteStatus},
	    store: iinGridViewDataStore,
	    renderTo:'inteligentNotesPanel',
	    enableHdMenu:false,
	    layout:'fit',
	    enableColumnMove:false,
	    viewConfig:{forceFit:true},
	    columns: [
            {header: "Intelligent Claim Note Name", width: 180, dataIndex: 'intelligentNoteName', sortable: true, resizable: true, renderer:function(value,p,r){
                return  value }},
	        {header: "Intelligent Claim Note", width: 400, dataIndex: 'intelligentNote', sortable: true, resizable: true, renderer:function(value,p,r){
                return  value }},
	        {header: "Active", width: 80, dataIndex: 'status', sortable: true, resizable: true, renderer: booleanLink}
	    ],
	    height:510,
	    width: 760
	});
	
	if(!intelligentNotePopUp || intelligentNotePopUp==null)
    {
		intelligentNotePopUp =  new Ext.Window({
            applyTo:'iinEditWindow',
            width:600,
            height:215,
            layout:'fit',
            modal:true,
            closeAction:'hide',
            plain: false,
            title: 'Insurer Intelligent Note',
            resizable : true,
            items: new Ext.Panel({
                applyTo: 'iinEditPanel'
            }),
            buttons: [{
                    text: 'Close', handler: function(){
                    	intelligentNotePopUp.hide();
                    }
                }]
        });
    }
	
});

function booleanLink(value,p,r){
    if(value){
        value = "Yes";
    }else{
        value = "No";
    }
    return "<a href='#' class='high-light-item'>" + value + "</a>"
}

function loadGridViewList(){
    iinGridViewDataStore.load({params:{insurerId:<s:property value="insurerId" />}});
}

function updateIntelligentNoteStatus(grid, rowIndex, columnIndex, e){
    var gridView = iinGridView.getStore().getAt(rowIndex);
    if(columnIndex==0 || columnIndex==1){
    	showIntelligentNotePopUp(gridView);
    } else if(columnIndex==2){
    	 var iinId = gridView.get("id");
    	 var inId = gridView.get("intelligentNoteId");
    	 var status = gridView.get("status") == "Yes" ? true : false;
         var url = "/prv/p/updateInsurerInteligentNoteStatus.action";
         var param = {"intelligentNoteId":inId, "status":status, "insurerInteligentNoteId": iinId, "insurerId":<s:property value="insurerId" />};
         ajax.loadHtml2(url, param, onSubmitHandler);
    }
}
    
function showIntelligentNotePopUp(gridView){
	intelligentNotePopUp.show();
	
	$("form#iinEditForm label#iinName").html(gridView.get("intelligentNoteName"));
	$("form#iinEditForm label#iinNote").html(gridView.get("intelligentNote"));
	$("form#iinEditForm label#iinActive").html(gridView.get("status") == true ? "Yes" : "No");
}

function onSubmitHandler(responseText, statusText){
	try{
		var response = eval('(' + responseText.trim() + ')');
	    var outputDiv = $('div#iNoteErrorMessageBox');
	    triggerCss("div#iNoteErrorMessageBox", true);
	    if(response && !response.isValid){
	        triggerCss("div#iNoteErrorMessageBox", true);
	        $.each(response.errors, function() {
	            Ext.Msg.show({
	                title: 'Error',
	                msg:this.toString(),
	                icon:Ext.Msg.ERROR,
	                buttons:Ext.Msg.OK,
	                width : 400
	            });
	        });
	    }
	} catch (e) {
		Ext.Msg.show({
            title: 'Error',
            msg:responseText,
            icon:Ext.Msg.ERROR,
            buttons:Ext.Msg.OK,
            width : 400
        });
	}
    loadGridViewList();
}

</script>
<div class="sub-admin-tab-css">
    <div class="status-info">
        This tab allows you to switch on/off intelligent claim notes, this will apply to all CHOs.  These notes appear under the 'Additional Notes' section within a claim and are triggered at the claim upload stage.
    </div>

    <div id="inteligentNotesPageId">
       <div id="inteligentNotesPanel"></div>
       <div id="iNoteErrorMessageBox" class="action-error-msg"></div>
    </div>

	<div id="iinEditWindow" class="x-hidden">
        <div id="iinEditPanel">
             <div class="form-container" style="min-height:275px; padding-bottom:30px; padding-top:15px">
                <form id="iinEditForm" name="rorEditForm" class="XXentity-form" method="post">

					<div class="chox-form-item">
						<table style="font-size:12px">
						   <tr>
                                <td><label class="chox-form-std-label">Intelligent Claim Note Name : </label></td>
                                <td><label id="iinName"></label></td>
                            </tr>
						    <tr><td colspan="2"></br></td></tr>
							<tr>
								<td><label class="chox-form-std-label">Intelligent Claim Note : </label></td>
								<td><label id="iinNote"></label></br></td>
							</tr>
							<tr><td colspan="2"></br></td></tr>
                            <tr>
                                <td><label class="chox-form-std-label">Intelligent Claim Active : </label></td>
                                <td><label id="iinActive"></label></br></td>
                            </tr>
						</table>
					</div>

				</form>
            </div> 
        </div>
    </div>

</div>