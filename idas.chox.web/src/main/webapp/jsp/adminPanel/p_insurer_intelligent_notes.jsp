<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

var iinGridViewDataStore;
var iinGridView;

$(function(){
	
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
	
	var iNotesProxy = new Ext.data.HttpProxy({
	    url: '<%= request.getContextPath()%>/prv/p/getInteligentNotes.action',
	    method: 'post'
	});

	iinGridViewDataStore = new Ext.data.Store({
	    proxy: iNotesProxy,
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
	    viewConfig:{forceFit:true},
	    columns: [
            {header: "Intelligent Claim Note Name", width: 180, dataIndex: 'intelligentNoteName', sortable: true, resizable: true},
	        {header: "Intelligent Claim Note", width: 400, dataIndex: 'intelligentNote', sortable: true, resizable: true},
	        {header: "Active", width: 80, dataIndex: 'status', sortable: true, resizable: true, renderer: booleanLink}
	    ],
	    height:510,
	    width: 760
	});
	
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
    if(columnIndex==2){
    	 var iinId = gridView.get("id");
    	 var inId = gridView.get("intelligentNoteId");
    	 var status = gridView.get("status") == "Yes" ? true : false;
         var url = "<%= request.getContextPath()%>/prv/p/updateInsurerInteligentNoteStatus.action";
         var param = {"intelligentNoteId":inId, "status":status, "insurerInteligentNoteId": iinId, "insurerId":<s:property value="insurerId" />};
         ajax.loadHtml2(url, param, onSubmitHandler);
    }
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

</div>