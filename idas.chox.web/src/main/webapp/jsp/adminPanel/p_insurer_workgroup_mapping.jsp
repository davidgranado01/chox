<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var workgroup_gridviewJsonReader;
    var workgroup_gridviewDataStore;
    var workgroup_gridviewGrid;
    var workgroup_gridviewData;

    Ext.onReady(function(){

        workgroup_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'name'},
                {name:'site'},
                {name:'team'},
                {name:'insurerId'},
                {name:'insurerName'},
                {name:'status'},
                {name:'statusDesc'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        workgroup_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getInsurerWorkgroups.action',method:'POST'}),
            reader:workgroup_gridviewJsonReader
        });

        workgroup_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:workgroup_recordOnclick },
            store: workgroup_gridviewData,
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            loadMask : true,
            viewConfig:{forceFit:true},
            columns: [
                {header: "Insurer", width: 100, dataIndex: 'insurerName', sortable: true, resizable: true},
                {header: "Workgroup", width: 180, dataIndex: 'name', sortable: true, resizable: true},
                {header: "Site", width: 80, dataIndex: 'site', sortable: true, resizable: true},
                {header: "Team", width: 100, dataIndex: 'team', sortable: true, resizable: true},
                {header: "Active", width: 50, dataIndex: 'statusDesc', sortable: true, resizable: true, renderer:function(value,p,r){ return "<a href='#' class='high-light-item'>" + value + "</a>"}},
                {header: "Action", width: 60, dataIndex: 'Remove', sortable: true, resizable: true, renderer:function(value,p,r){ return "<a href='#' class='high-light-item'>Remove</a>"}},
                {header: "Created By", width: 80, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width: 120, dataIndex: 'createdDate', sortable: true, resizable: true}
            ],
            renderTo:'workgroup_gridviewGrid',
            height:365,
            width: 760
        });

        workgroup_loadGridViewList();

    });

    function workgroup_loadGridViewList(){
        workgroup_gridviewData.load({ params : { insurerId:<s:property value="insurerId" /> } });
    }

    function workgroup_recordOnclick(grid, rowIndex, columnIndex, e){
        var gridView = workgroup_gridviewGrid.getStore().getAt(rowIndex);
        if(columnIndex==4){
            workgroup_triggerStatusUpdateRecord(gridView);
        }else if(columnIndex==5){
            workgroup_triggerStatusRemoveRecord(gridView);
        }
    }

    function workgroup_triggerStatusAddRecord(){

        var workgroupName = $("#workgroupName").val();
        var workgroupTeam = $("#workgroupTeam").val();
        var workgroupSite = $("#workgroupSite").val();

        if(workgroupName==null || workgroupName==""){
            triggerCss("div#CDInsurerWorkgroupMessageBox", true);
            $("div#CDInsurerWorkgroupMessageBox").html("Please enter 'Workgroup Name'");
        }else if(workgroupTeam==null || workgroupTeam==""){
            triggerCss("div#CDInsurerWorkgroupMessageBox", true);
            $("div#CDInsurerWorkgroupMessageBox").html("Please enter 'Workgroup Team'");
        }else if(workgroupSite==null || workgroupSite==""){
            triggerCss("div#CDInsurerWorkgroupMessageBox", true);
            $("div#CDInsurerWorkgroupMessageBox").html("Please enter 'Workgroup Site'");
        }else{
            var url = "<%= request.getContextPath()%>/prv/p/addNewInsurerWorkgroup.action";
            var param = {"insurerId":<s:property value="insurerId" />,"workgroupName":workgroupName,"workgroupSite":workgroupSite,"workgroupTeam":workgroupTeam};
            ajax.loadHtml2(url, param, workgroup_onSubmitResponseReceived);
        }

    }

    function workgroup_triggerStatusUpdateRecord(gridView){

        var workgroupId = gridView.get("id");
        var url = "<%= request.getContextPath()%>/prv/p/triggerInsurerWorkgroupStatus.action";
        var param = {"insurerId":<s:property value="insurerId" />,"workgroupId":workgroupId};
        ajax.loadHtml2(url, param, workgroup_onSubmitResponseReceived);

    }

    function workgroup_triggerStatusRemoveRecord(gridView){
        Ext.MessageBox.confirm('Confirm', 'Are you sure you want to remove this Workgroup?',function(btn){
        if(btn=='yes'){

            var workgroupId = gridView.get("id");

            var url = "<%= request.getContextPath()%>/prv/p/removeInsurerWorkgroup.action";
            var param = {"insurerId":<s:property value="insurerId" />,"workgroupId":workgroupId};
            ajax.loadHtml2(url, param, workgroup_onSubmitResponseReceived);
        }
        });
    }

    function workgroup_onSubmitResponseReceived(responseText, statusText)  {

        var response = eval('(' + responseText.trim() + ')');
        var outputDiv = $('div#CDInsurerWorkgroupMessageBox');

        triggerCss("div#CDInsurerWorkgroupMessageBox", true);

        if(response)
        {
            triggerCss("div#CDInsurerWorkgroupMessageBox", false);

            if(response.isValid){

                outputDiv.addClass("chox-form-submit-result");

                if(response.resultType && response.resultType == 'Message')
                {
                    Ext.MessageBox.show({
                        title: '',
                        msg: response.result,
                        width:300,
                        buttons: Ext.MessageBox.OK
                    });
                    workgroup_loadGridViewList();
                    clearFormValues();
                }
                else
                {
                    //                    alert("Your Changes Have Been Saved");
                    workgroup_loadGridViewList();
                    clearFormValues();
                }

            }
            else
            {
                triggerCss("div#CDInsurerWorkgroupMessageBox", true);
                $.each(response.errors, function() {
                    
                    Ext.Msg.show({
                        title: 'Error',
                        msg:this.toString(),
                        icon:Ext.Msg.ERROR,
                        buttons:Ext.Msg.OK,
                        width : 400
                    });
                    //                    outputDiv.append(this.toString());
                });
                workgroup_loadGridViewList();
            }
        }
        else
        {
            outputDiv.append("Unknown Error Encountered, please try again.");
            outputDiv.addClass("submit-error");
        }

    }
    
    function clearFormValues() {
        $("#workgroupName").val('');
        $("#workgroupTeam").val('');
        $("#workgroupSite").val('');
    }
    
</script>
<div class="sub-admin-tab-css">

    <div class="status-info">
        The Workgroups that dictate where claims are routed to and therefore which users have access/visibility of the said claims is managed here.  Please note, it is not possible to remove a Workgroup where there is an open claim within the system that is assigned to the said Workgroup.
    </div>

    <div class="grid-view-header">

        <table width="100%">
            <tr><td>
                    <div class="label-block">
                        <p class="std-label">Workgroup name<span class="mandatory">*</span> </p><input name="workgroupName" id="workgroupName" type="text">
                    </div>
                </td>
            </tr>
            <tr><td>
                    <div class="label-block">
                        <p class="std-label">Site<span class="mandatory">*</span> </p><input name="workgroupSite" id="workgroupSite" type="text">
                    </div>
                </td>
            </tr>
            <tr><td>
                    <div class="label-block">
                        <p class="std-label">Team<span class="mandatory">*</span> </p><input name="workgroupTeam" id="workgroupTeam" type="text">
                    </div>
                </td>
            </tr>
            <tr><td align="center">
                    <input type="button" onclick="javascript:return workgroup_triggerStatusAddRecord();" value="Add"/>
                </td>
            </tr>
        </table>

    </div>
    <div id="CDInsurerWorkgroupMessageBox" class="chox-form-submit-result"></div>
    <div id="workgroup_gridviewGrid"></div>
</div>