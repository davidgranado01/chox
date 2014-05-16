<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var ipWhitelist_gridviewJsonReader;
    var ipWhitelist_gridviewDataStore;
    var ipWhitelist_gridviewGrid;
    var ipWhitelist_gridviewData;
    var ipWhitelistRowEditor;
    var choId = <s:property value="choId" />;
    var choId = -1;
    var insId = -1;

    Ext.onReady(function(){
        
        if ('<s:property value="orgType"/>' == 3) {
            choId = '<s:property value="orgId"/>';
        } else if ('<s:property value="orgType"/>' == 2) {
            insId = '<s:property value="orgId"/>';
        }
        
        var ipAddressField =new Ext.form.TextField({
            id:"ipWhitelistIPAddressId",
            name:"ipAddress",
            width:140,
            renderTo:'ipAddressHolder'
        });
            
            
        var descriptionField = new Ext.form.TextField({
            id : 'ipWhitelistDescriptionId',
            name: 'description',
            width:220,
            renderTo: 'descriptionHolder'
        });
        
        ipWhitelist_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'insName'},
                {name:'choName'},
                {name:'ipAddress'},
                {name:'description'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        ipWhitelist_gridviewData = new choxDataStore({
            url: '/prv/p/getIPWhitelist.action',
            params : {"orgId":'<s:property value="orgId"/>', "orgType":'<s:property value="orgType"/>'},
            reader:ipWhitelist_gridviewJsonReader,
            listeners: {update : function(store,record,operation) {
                    $("div#CDInsureripWhitelistMessageBox").html("");
                    var url = "/prv/p/updateIPWhitelist.action";
                    var param = {"id": record.get('id'),"choId": choId,"insId": insId,"ipAddress": record.get('ipAddress'),"description": record.get('description')};
                    ajax.loadHtml2(url, param, function(responseText, statusText){
                
                        var response = eval('(' + responseText.trim() + ')');
                        if(response){
                    
                            if(response.success){
                                ipWhitelist_loadGridViewList();
                            } else if(response.error){
                                Ext.MessageBox.show({
                                    title: 'ERROR',
                                    msg: response.error,
                                    width:300,
                                    buttons: Ext.MessageBox.OK,
                                    icon : Ext.MessageBox.ERROR
                                });
                                ipWhitelist_loadGridViewList();
                            }
                        }
                    });
                }
            }
        });
        ipWhitelistRowEditor = new Ext.ux.grid.RowEditor({
            saveText: 'Update',
            clicksToEdit: 2
        });

        ipWhitelist_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:ipWhitelist_recordOnclick },
            store: ipWhitelist_gridviewData,
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            plugins: [ipWhitelistRowEditor],
            viewConfig:{forceFit:true},
            columns: [
                {header: "IP Address",  width: 90, dataIndex: 'ipAddress', sortable: true, resizable: true,editor: {xtype: 'textfield',allowBlank: false, emptyText  : 'IP Address is required'}},
                {header: "Description",  width: 90, dataIndex: 'description', sortable: true, resizable: true,editor: {xtype: 'textfield',allowBlank: false, emptyText  : 'Description is required'}},
                {header: "Created By", width: 150, dataIndex: 'createdBy', sortable: true, resizable: true,editable : false},
                {header: "Created Date", width: 130, dataIndex: 'createdDate', sortable: true, resizable: true,editable : false},
                {header: "Action", width: 70, dataIndex: 'Remove', sortable: true, resizable: true,editable : false, renderer:function(value,p,r){ return "<a href='#' class='high-light-item'>Remove</a>"}}
            ],
            renderTo:'ipWhitelist_gridviewGridPanel',
            height:450,
            width: 760
        });

        ipWhitelist_loadGridViewList();

    });

    function ipWhitelist_loadGridViewList(){
        ipWhitelist_gridviewData.load({params : {"orgId":'<s:property value="orgId"/>', "orgType":'<s:property value="orgType"/>'}});
    }

    function ipWhitelist_recordOnclick(grid, rowIndex, columnIndex, e){
        var gridView = ipWhitelist_gridviewGrid.getStore().getAt(rowIndex);
        
        if(columnIndex==4){
            ipWhitelist_triggerStatusRemoveRecord(gridView);
        }
    }

    function ipWhitelist_triggerStatusAddRecord(){
        
        $("div#CDInsureripWhitelistMessageBox").html("");
        var ipWhitelistIPAddress = $("#ipWhitelistIPAddressId").val().trim();
        var ipWhitelistDescription = $("#ipWhitelistDescriptionId").val().trim();
        
        if (ipWhitelistIPAddress==null || $.trim(ipWhitelistIPAddress) == "" || ipWhitelistDescription==null || $.trim(ipWhitelistDescription) == "") {
            if((ipWhitelistIPAddress==null || $.trim(ipWhitelistIPAddress) == "") && (ipWhitelistDescription==null || $.trim(ipWhitelistDescription) == "")){
                triggerCss("div#CDInsureripWhitelistMessageBox", true);
                $("div#CDInsureripWhitelistMessageBox").html("Please enter a 'IP Address',&nbsp;&nbsp;").append("&nbsp;&nbsp;Please enter a 'Description'");
            } else if (ipWhitelistIPAddress==null || $.trim(ipWhitelistIPAddress) == ""){
                triggerCss("div#CDInsureripWhitelistMessageBox", true);
                $("div#CDInsureripWhitelistMessageBox").html("Please enter a 'IP Address'");
            } else if(ipWhitelistDescription==null || $.trim(ipWhitelistDescription) == ""){
                triggerCss("div#CDInsureripWhitelistMessageBox", true);
                $("div#CDInsureripWhitelistMessageBox").html("Please enter a 'Description'");
            }
        } else{
            var url = "/prv/p/addIPWhitelist.action";
            var param = {"choId":choId,"insId": insId,"ipAddress": ipWhitelistIPAddress,"description": ipWhitelistDescription};
            ajax.loadHtml2(url, param, function(responseText, statusText){
                
                var response = eval('(' + responseText.trim() + ')');
                if(response){
                    if(response.success){
                        Ext.getCmp('ipWhitelistDescriptionId').reset();
                        Ext.getCmp('ipWhitelistIPAddressId').reset();
                        ipWhitelist_loadGridViewList();
                    } else if(response.error){
                        Ext.MessageBox.show({
                            title: 'ERROR',
                            msg: response.error,
                            width:300,
                            buttons: Ext.MessageBox.OK,
                            icon : Ext.MessageBox.ERROR
                        });
                        ipWhitelist_loadGridViewList();
                    }
                }
            });
        }
    }

    
    function ipWhitelist_triggerStatusRemoveRecord(gridView){
        
        Ext.MessageBox.show({
            title: '',
            msg: 'Are you sure you want to remove this IP Address?',
            width:300,
            buttons: Ext.MessageBox.OKCANCEL,
            icon : Ext.MessageBox.QUESTION,
            fn: function removeipWhitelist(btn){
                if(btn=='ok'){
                    var ipWhitelistId = gridView.get("id");
                    var url = "/prv/p/deleteIPWhitelist.action";
                    var param = {"id": ipWhitelistId};
                    ajax.loadHtml2(url, param, ipWhitelist_onSubmitResponseReceived);
                } 
            }
        });
    }

    function ipWhitelist_onSubmitResponseReceived(responseText, statusText)  {

        var response = eval('(' + responseText.trim() + ')');
        if(response)
        {
            if(response.success){
                ipWhitelist_loadGridViewList();
            }
            else if (response.error)
            {
                Ext.MessageBox.show({
                    title: 'ERROR',
                    msg: response.error,
                    width:300,
                    buttons: Ext.MessageBox.OK,
                    icon : Ext.MessageBox.ERROR
                });
                ipWhitelist_loadGridViewList();
            }
        }
    }

    
</script>
<div class="sub-admin-tab-css">
    <div class="status-info">
        This tab allows you to setup white listed IP Addresses.
    </div>

    <div class="grid-view-header">

        <table>
             <tr>
                <td width ="260" align="left">
                    <p class="std-label-ipWhitelist">IP Address</p><div id="ipAddressHolder"></div>
                </td> 
                <td width ="380" align="left">
                    <p class="std-label-ipWhitelist">Description</p><div id="descriptionHolder"></div>
                </td> 
                <td align="center">
                    <input type="button" onclick="javascript:return ipWhitelist_triggerStatusAddRecord();" value="Add"/>
                </td>
            </tr>  
        </table>

    </div>
    <div id="CDInsureripWhitelistMessageBox" class="chox-form-submit-result"></div>
    <div id="ipWhitelist_gridviewGridPanel"></div>
</div>