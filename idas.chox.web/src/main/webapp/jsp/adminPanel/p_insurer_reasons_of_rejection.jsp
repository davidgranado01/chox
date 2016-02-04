<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

var rorGridViewDataStore;
var rorEditPopWindow;
var rorGridView;

Ext.onReady(function() {

    var rejectionDescField = new Ext.form.TextArea({
        name             : 'description',
        id               : 'rorDescTextId',
        width            :  350,
        height           :  80,
        allowBlank       :  true,
        renderTo         : 'rorDescId'
    });
    
    var rejectionEditDescField = new Ext.form.TextArea({
        name             : 'description',
        id               : 'rorDescEditTextId',
        width            :  350,
        height           :  80,
        allowBlank       :  true,
        renderTo         : 'rorDescEditId'
    });
    
    var rorJsonReader = new Ext.data.JsonReader({
        totalProperty: 'totalCount',
        root: 'results',
        fields:
            [
             {name:'id'},
             {name:'rorName'},
             {name:'description'},
             {name:'type'},
             {name:'gtaActive'},
             {name:'collaborationActive'},
             {name:'insurerVsInsurerActive'},
             {name:'subscriberActive'},
             {name:'fixedFeeActive'},
             {name:'insurerUploadActive'},
             {name:'tpiActive'},
             {name:'restricted'},
             {name:'createdBy'},
             {name:'createdDate'}
             ]
    });
    
    rorGridViewDataStore = new choxDataStore({
        url: '/prv/p/getInsurersReasonsOfRejection.action',
        reader: rorJsonReader
    });
    
    rorGridView = new Ext.grid.GridPanel({
        listeners:  {cellclick:editReasonOfRejection},
        store: rorGridViewDataStore,
        renderTo:'rorGridViewPanel',
        enableHdMenu:false,
        enableColumnMove:false,
        layout:'fit',
        viewConfig:{forceFit:true},
        columns: [
            {header: "Reason", width: 100, dataIndex: 'rorName', sortable: true, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='high-light-item'>"+value+"</a>"; }},
            {header: "Default Supporting Note", width: 100, dataIndex: 'description', sortable: true, resizable: true},
            {header: "Type", width: 78, dataIndex: 'type', sortable: true, resizable: true},
            {header: "GTA Active", width: 38, dataIndex: 'gtaActive', sortable: true, resizable: true, 
                renderer: booleanLink},
            {header: "Insurer Vs Insurer Active", width: 38, dataIndex: 'insurerVsInsurerActive', sortable: true, resizable: true, 
                renderer: booleanLink},
            {header: "Subscriber Active", width: 38, dataIndex: 'subscriberActive', sortable: true, resizable: true, 
                renderer: booleanLink},
            {header: "Fixed Fee Active", width: 38, dataIndex: 'fixedFeeActive', sortable: true, resizable: true, 
                renderer: booleanLink},
            {header: "Insurer Upload Active", width: 38, dataIndex: 'insurerUploadActive', sortable: true, resizable: true, 
                renderer: booleanLink},
            {header: "TPI Active", width: 38, dataIndex: 'tpiActive', sortable: true, resizable: true, 
                renderer: booleanLink},
            {header: "Collaboration Active", width: 38, dataIndex: 'collaborationActive', sortable: true, resizable: true, 
                renderer: booleanLink},
            {header: "Visible Before Assigned", width: 38, dataIndex: 'restricted', sortable: true, resizable: true,
                renderer:booleanLink},                    
            {header: "", width: 38, dataIndex: '', sortable: false, resizable: true, renderer:function(value,p,r){
                    return "<a href='#' class='high-light-item'>Remove</a>";}}
        ],
        height:155,
        width: 760
    });
    
    function booleanLink(value,p,r){
        if(value){
            value = "Yes";
        }else{
            value = "No";
        }
        return "<a href='#' class='high-light-item'>" + value + "</a>";
    }
    
    if(!rorEditPopWindow || rorEditPopWindow===null)
    {
        rorEditPopWindow =  new Ext.Window({
            applyTo:'rorEditWindow',
            width:600,
            height:215,
            layout:'fit',
            modal:true,
            closeAction:'hide',
            plain: false,
            title: 'Edit Rejection Reason',
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
                            choxJqueryAjaxSubmit($("form#rorEditForm"), op);
                        }
                    }
                },{
                    text: 'Close', handler: function(){
                        rorEditPopWindow.hide();
                    }
                }]
        });
    }
    
    var form = $("form#rorForm");

    form.validate(
    {
        errorLabelContainer: "#rorErrorMessageBox",
        rules: {
            rorName:{ required:true, minlength:5 , maxlength:32}
        },
        messages: {
            rorName: { required:"You must supply a 'Rejection Reason'", 
                minlength:"'Rejection Reason Name' must be at least 5 characters long." , 
                maxlength:"'Rejection Reason Name' can have maximum 32 characters."}
        }
    });

    ui.ajaxForm(form, onSubmitHandler);

    var editForm = $("form#rorEditForm");
    editForm.validate(
    {
        errorLabelContainer: "#rorEditErrorMessageBox",
        rules: {
            rorName:{ required:true, minlength:5 , maxlength:32 }
        },
        messages: {
            rorName: { required:"You must supply a 'Rejection Reason'", 
                minlength:"'Rejection Reason Name' must be at least 5 characters long." , 
                maxlength:"'Rejection Reason Name' can have maximum 32 characters."}
        }
    });
    
    var types = ['Claim Rejection','Invoice Rejection', 'Acceptance', 'Closure'];
    
    var typeCombo = new Ext.form.ComboBox({
        store: types,
        valueField:'type',
        displayField:'Type',
        fieldLabel: 'Type',
        mode:'local',
        renderTo: 'typeDivId',
        name: 'rorTypeName',
        id: 'rorTypeId',
        triggerAction: 'all',
        hiddenName: 'type',
        width: 150,
        selectOnFocus: true,
        editable: true,
        allowBlank: true,
        emptyText: '--- Please Select ---',
        forceSelection: true,
        listeners: {
            blur: function() {
                loadGridViewList();
            },
            select: function() {
                   if(this.getValue() === 'Claim Rejection'){
                       $("#supportingNoteDivId").slideDown();
                       $("#restrictedDivId").slideDown();
                       $("form#rorForm input#restricted").attr('checked',false);
                   } else if(this.getValue() === 'Invoice Rejection'){
                       $("#restrictedDivId").slideUp();
                       $("#supportingNoteDivId").slideDown();
                   } else if(this.getValue() === 'Acceptance'){
                       $("#restrictedDivId").slideUp();
                       $("#supportingNoteDivId").slideUp();
                   } else if(this.getValue() === 'Closure'){
                       $("#supportingNoteDivId").slideDown();
                       $("#restrictedDivId").slideUp();
                   }
                   loadGridViewList();
            }
        }
    });
    
    onPageRefresh();
});

function closeWindowAndRefresh(){
    rorEditPopWindow.hide();
    onPageRefresh();
}

function editReasonOfRejection(grid, rowIndex, columnIndex, e){
    var gridView = rorGridView.getStore().getAt(rowIndex);
    
    if(columnIndex===0){
        showEditReasonOfRejection(gridView);
    }else if(columnIndex >= 3 && columnIndex <= 9){
        var rorId = gridView.get("id");
        var url = "/prv/p/updateReasonOfRejectionActive.action";
        var selectedColumn = rorGridView.getColumnModel().getColumnAt(columnIndex).dataIndex;
        var param = {"reasonOfRejectionId":rorId, "activeType": selectedColumn};
        ajax.loadHtml2(url, param, onSubmitHandler);
    }else if(columnIndex===10){
        var rorId = gridView.get("id");
        var url = "/prv/p/updateReasonOfRejectionRestricted.action";
        var param = {"reasonOfRejectionId":rorId};
        ajax.loadHtml2(url, param, onSubmitHandler);
    } else if(columnIndex===11){
        var rorId = gridView.get("id");
        var url = "/prv/p/deleteReasonOfRejection.action";
        var param = {"reasonOfRejectionId":rorId};
        ajax.loadHtml2(url, param, onSubmitHandler);
    }
}

function onSubmitHandler(responseText, statusText){
    var response = eval('(' + responseText.trim() + ')');
    var outputDiv = $('div#rorEditErrorMessageBox');
    triggerCss("div#rorEditErrorMessageBox", true);
   
    if(response && !response.isValid){
        triggerCss("div#rorEditErrorMessageBox", true);
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
    onPageRefresh();
    
}

function onPageRefresh(){
    refreshForm();
    loadGridViewList();
}

function refreshForm(){
    $("#rorId").val("");
    $("#rorDescTextId").val("");
    $("form#rorForm input#restricted").attr('checked',false);
    $("form#rorForm input#gtaActiveId").attr('checked',false);
    $("form#rorForm input#collaborationActiveId").attr('checked',false);
    $("form#rorForm input#subscriberActiveId").attr('checked',false);
    $("form#rorForm input#fixedFeeActiveId").attr('checked',false);
    $("form#rorForm input#tpiActiveId").attr('checked',false);
    $("form#rorForm input#insurerVsInsurerActiveId").attr('checked',false);
    $("form#rorForm input#insurerUploadActiveId").attr('checked',false);
    $("form#rorForm input#restricted").attr('checked',false);
    $("form#rorForm input#active").attr('checked',false);
}

function loadGridViewList(){
    rorGridViewDataStore.load({params:{insurerId:<s:property value="insurerId" />, activeType:Ext.getCmp('rorTypeId').getValue()}});
}

function showEditReasonOfRejection(gridView){
    rorEditPopWindow.show();
    
    $("form#rorEditForm input[name$='reasonOfRejectionId']").val(gridView.get("id"));
    $("form#rorEditForm input[name$='rorName']").val(gridView.get("rorName"));
    $("form#rorEditForm #rorDescEditTextId").val(gridView.get("description"));
}

</script>
<div class="sub-admin-tab-css">
    <div class="status-info">
        This tab allows the Acceptance/Rejection Reasons at First Notification, Invoice and Claim Closure stages to be customised for the selected Insurer.
    </div>

    <div id="rorGridId">
        <div class="grid-view-header">
            <table style="width: 100%">
                <tr>
                    <td>
                        <div class="admin-bre-band-detail-section">
                            <div class="section-name">Reason Management</div>
                            <div class="form-container">
                                <form id="rorForm" name="rorForm" action="<%= request.getContextPath()%>/prv/p/addReasonOfRejection.action" class="XXentity-form" method="post">
                                    <input id="insurerId" name="insurerId" type="hidden" value="<s:property value="insurerId"/>"/>
                                    <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
                                    
                                    <div class="chox-form-item" style="padding-bottom: 2px">
                                        <label class="chox-form-std-label">Type</label>
                                        <div id="typeDivId" style="padding-left: 70px"></div>
                                    </div>
                                    
                                    <div class="chox-form-item" style="padding-bottom: 2px">
                                        <label class="chox-form-std-label">Reason<span class="mandatory">*</span></label>
                                        <input type="text" id="rorId" name="rorName" style="width: 175px" minlength="5" />
                                    </div>
                                    
                                    <div class="chox-form-item" id="supportingNoteDivId">
                                        <label class="chox-form-std-label">Default Supporting Note</label>
                                        <div id="rorDescId" style="padding-left: 12px"></div>
                                    </div>
                                    
                                    <br/>
                                    <table style="width: 100%">
                                        <tr>
                                            <td style="width: 50%; height: 15px">
                                                <div style="position:relative;width:239px;">
                                                    <div style="position:absolute;right:0;">
                                                    <label >GTA Active</label>
                                                    <s:checkbox id="gtaActiveId" name="gtaActive"/>
                                                    </div>
                                                </div>
                                            </td>
                                            <td style="width: 50%; height: 15px">
                                                <div style="position:relative;width:220px;">
                                                    <div style="position:absolute;right:0;">
                                                    <label >Insurer Vs Insurer Active</label>
                                                    <s:checkbox id="insurerVsInsurerActiveId" name="insurerVsInsurerActive" />
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="width: 50%; height: 15px">
                                                <div style="position:relative;width:239px;">
                                                    <div style="position:absolute;right:0;">
                                                        <label >Subscriber Active</label>
                                                        <s:checkbox id="subscriberActiveId" name="subscriberActive"/>
                                                    </div>
                                                </div>
                                            </td>
                                            <td style="width: 50%; height: 15px">
                                                <div style="position:relative;width:220px;">
                                                    <div style="position:absolute;right:0;">
                                                        <label >Insurer Upload Active</label>
                                                        <s:checkbox id="insurerUploadActiveId" name="insurerUploadActive" />
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="width: 50%; height: 15px;">
                                                <div style="position:relative;width:239px;">
                                                    <div style="position:absolute;right:0;">
                                                        <label >TPI Active</label>
                                                        <s:checkbox id="tpiActiveId" name="tpiActive"/>
                                                    </div>
                                                </div>
                                            </td>
                                            <td style="width: 50%; height: 15px;">
                                                <div style="position:relative;width:220px;">
                                                    <div style="position:absolute;right:0;">
                                                        <label >Fixed Fee Active</label>
                                                        <s:checkbox id="fixedFeeActiveId" name="fixedFeeActive" />
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="width: 50%; height: 15px;">
                                                <div style="position:relative;width:239px;">
                                                    <div style="position:absolute;right:0;">
                                                        <label >Collaboration Protocol Active</label>
                                                        <s:checkbox id="collaborationActiveId" name="collaborationActive"/>
                                                    </div>
                                                </div>
                                            </td>
                                            <td style="width: 50%; height: 15px;"></td>
                                        </tr>
                                        <tr>
                                            <td style="width: 50%"><br/></td>
                                        </tr>
                                        <tr>
                                            <td colspan="2">
                                               <div style="position:relative;width:239px;">
                                                    <div id="restrictedDivId" style="position:absolute;right:0;">
                                                        <label >Visible Before Assigned</label>
                                                        <s:checkbox id="restricted" name="restricted" />
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                    <br/>
                                    <br/>
                                    <div class="chox-form-button">
                                        <input type="submit" value="Add Reason"/>
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
        <div id="rorGridViewPanel"></div>
    </div>
    
    <div id="rorEditWindow" class="x-hidden">
        <div id="rorEditPanel">
            <div class="form-container" style="height:275px; padding-bottom:30px; padding-top:30px">
                <form id="rorEditForm" name="rorEditForm" class="XXentity-form" action="<%= request.getContextPath()%>/prv/p/updateReasonOfRejection.action" method="post">
                    <input id="reasonOfRejectionId" name="reasonOfRejectionId" type="hidden"/>
                    
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Rejection Reason<span class="mandatory">*</span></label>
                        <input id="rorEditId" name="rorName" style="width: 175px" disabled="disabled"/>
                    </div>
                    
                    <div class="chox-form-item">
                        <label class="chox-form-std-label">Supporting Rejection Note</label>
                        <div id="rorDescEditId"/>
                    </div>
                    <!--<input type="hidden" id="nonceId" name="nonce" value='<%= session.getAttribute("SessionNonce") %>'/>-->
                </form>
            </div>
        </div>
    </div>
    
</div>