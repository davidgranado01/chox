<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var alias_gridviewJsonReader;
    var alias_gridviewDataStore;
    var alias_gridviewGrid;
    var alias_gridviewData;

    Ext.onReady(function(){

        alias_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'name'},
                {name:'insurerName'},
                {name:'insurerId'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        alias_gridviewData = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy
            ({url: '<%= request.getContextPath()%>/prv/p/getInsurerAlias.action',method:'POST'}),
            reader:alias_gridviewJsonReader
        });

        alias_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:alias_recordOnclick },
            store: alias_gridviewData,
            enableHdMenu:false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "Insurer", width: 100, dataIndex: 'insurerName', sortable: true, resizable: true},
                {header: "Alias Name", width: 180, dataIndex: 'name', sortable: true, resizable: true},
                {header: "Action", width: 80, dataIndex: 'Remove', sortable: true, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>Remove</a>"}},
                {header: "Created By", width: 100, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: true, resizable: true}
            ],
            renderTo:'alias_gridviewGrid',
            height:420,
            width: 715
        });

        alias_loadGridViewList();

    });

    function alias_loadGridViewList(){
        alias_gridviewData.load({ params : { insurerId:<s:property value="insurerId" /> } });
    }

    function alias_recordOnclick(grid, rowIndex, columnIndex, e){
        var gridView = alias_gridviewGrid.getStore().getAt(rowIndex);
        if(columnIndex==2){
            alias_triggerStatusRemoveRecord(gridView);
        }
    }

    function alias_triggerStatusAddRecord(){

        var insurerAliasName = $("#insurerAliasName").val();

        if(insurerAliasName!=null && insurerAliasName!=""){

            var url = "<%= request.getContextPath()%>/prv/p/addNewInsurerAlias.action";
            var param = {"insurerId":<s:property value="insurerId" />,"insurerAliasName":insurerAliasName};
            ajax.loadHtml(url, param, onInsurerAliasMappingSubmitResult);

        }else{

            triggerCss("div#CDInsurerAliasMessageBox", true);
            $("div#CDInsurerAliasMessageBox").html("please select 'Insurer Alias Name'");

        }
    }

    function onInsurerAliasMappingSubmitResult(responseText, statusText){

        var response = eval('(' + responseText.trim() + ')');
        var outputDiv = $('div#CDInsurerAliasMessageBox');

        triggerCss("div#CDInsurerAliasMessageBox", true);

        if(response)
        {
            triggerCss("div#CDInsurerAliasMessageBox", false);

            if(response.isValid){

                outputDiv.addClass("chox-form-submit-result");

                if(response.resultType && response.resultType == 'Message')
                {
                    alert(response.result);
                    insurerAlias_doRefreshPage();
                }
                else
                {
                    alert("Your Changes Have Been Saved");
                    insurerAlias_doRefreshPage();
                }

            }
            else
            {
                triggerCss("div#CDInsurerAliasMessageBox", true);
                $.each(response.errors, function() {
                    outputDiv.append(this.toString());
                });
            }
        }
        else
        {
            outputDiv.append("Unknown Error Encountered, please try again.");
            outputDiv.addClass("submit-error");
        }
    }

    function insurerAlias_doRefreshPage(){

        var tabIndex = 0;
        if(<s:property value="isChoxAdmin"/>){
            tabIndex = 1;
        }

        var target = "#admin_param_panel";
        var url = "<%= request.getContextPath()%>/prv/p/updateInsurerDetailPanel.action";
        var param = {"objectId":<s:property value="insurerId" />,"tabIndex":tabIndex};
        ajax.loadHtml(url,param,function(data){
            $(target).html(data);
        });
    }

    function alias_triggerStatusRemoveRecord(gridView){

        if(confirm("Are you sure you want to remove this alias?")){
            var insurerAliasId = gridView.get("id");
            var url = "<%= request.getContextPath()%>/prv/p/removeInsurerAlias.action";
            var param = {"insurerAliasId":insurerAliasId};
            ajax.loadHtml(url, param, onInsurerAliasMappingSubmitResult);
        }

    }

</script>

<div class="sub-admin-tab-css">

    <div class="status-info">
        This tab contains a list of all the alias’ that identify the particular Insurer as the Third Party Insurer when a CHO is uploading a claim into the system.  The text expression is case sensitive, the Third Party Insurer field on the claim upload looks for a match against the alias’ as held within the below table.
    </div>

    <div class="grid-view-header">
        <table width="100%">
            <tr>
                <td>
                    <div class="label-block">
                        <p class="std-label">Insurer Alias: </p> <input name="insurerAliasName" id="insurerAliasName" type="text">
                        <input type="submit" onclick="javascript: return alias_triggerStatusAddRecord();" value="Add"/>
                    </div>
                </td>
            </tr>
        </table>
    </div>
    <div id="CDInsurerAliasMessageBox" class="chox-form-submit-result"></div>
    <div id="alias_gridviewGrid"></div>

</div>