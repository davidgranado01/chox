<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">

    var cho_alias_gridviewJsonReader;
    var cho_alias_gridviewGrid;
    var cho_alias_gridviewData;

    Ext.onReady(function(){

        cho_alias_gridviewJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount',
            root: 'results',
            fields:
                [
                {name:'id'},
                {name:'name'},
                {name:'choName'},
                {name:'choId'},
                {name:'createdBy'},
                {name:'createdDate'}
            ]
        });

        cho_alias_gridviewData = new choxDataStore({
            url: '/prv/p/getChoAlias.action',
            reader:cho_alias_gridviewJsonReader
        });

        cho_alias_gridviewGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:cho_alias_recordOnclick },
            store: cho_alias_gridviewData,
            enableHdMenu:false,
            enableColumnMove: false,
            layout:'fit',
            viewConfig:{forceFit:true},
            columns: [
                {header: "CHO", width: 100, dataIndex: 'choName', sortable: true, resizable: true},
                {header: "Alias Name", width: 180, dataIndex: 'name', sortable: true, resizable: true},
                {header: "Action", width: 80, dataIndex: 'Remove', sortable: true, resizable: true, renderer:function(value,p,r){
                        return "<a href='#' class='high-light-item'>Remove</a>";}},
                {header: "Created By", width: 100, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width: 140, dataIndex: 'createdDate', sortable: true, resizable: true}
            ],
            renderTo:'cho_alias_gridviewGrid',
            height:450,
            width: 750
        });

        cho_alias_loadGridViewList();

    });

    function cho_alias_loadGridViewList(){
        cho_alias_gridviewData.load({ params : { choId:<s:property value="choId" /> } });
    }

    function cho_alias_recordOnclick(grid, rowIndex, columnIndex, e){
        var gridView = cho_alias_gridviewGrid.getStore().getAt(rowIndex);
        if(columnIndex===2){
            cho_alias_triggerStatusRemoveRecord(gridView);
        }
    }

    function cho_alias_triggerStatusAddRecord(){

        var choAliasName = $("#choAliasName").val();

        if(choAliasName!==null && choAliasName!==""){

            var url = "/prv/p/addNewChoAlias.action";
            var param = {"choId":<s:property value="choId" />,"choAliasName":choAliasName};
            ajax.loadHtml2(url, param, onChoAliasMappingSubmitResult);

        }else{

            triggerCss("div#CDChoAliasMessageBox", true);
            $("div#CDChoAliasMessageBox").html("please select 'Insurer Alias Name'");

        }
    }

    function onChoAliasMappingSubmitResult(responseText, statusText){

        var response = eval('(' + responseText.trim() + ')');
        var outputDiv = $('div#CDChoAliasMessageBox');

        triggerCss("div#CDChoAliasMessageBox", true);

        if(response)
        {
            triggerCss("div#CDChoAliasMessageBox", false);

            if(response.isValid){

                outputDiv.addClass("chox-form-submit-result");

                if(response.resultType && response.resultType === 'Message')
                {
                    choAlias_doRefreshPage();
                }
                else
                {
                    choAlias_doRefreshPage();
                }

            }
            else
            {
                triggerCss("div#CDChoAliasMessageBox", true);
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

    function choAlias_doRefreshPage(){

        var tabIndex = 0;
    <s:if test="isChoxAdmin">
            tabIndex = 1;
    </s:if>

            var target = "#admin_param_panel";
            var url = "/prv/p/updateChorganisationDetailPanel.action";
            var param = {"objectId":<s:property value="choId" />,"tabIndex":tabIndex};
            ajax.loadHtml2(url,param,function(data){
                $(target).html(data);
    <s:if test="isChoxAdmin">
                choAdminTabs.activate(tabIndex); 
    </s:if>
            });
        }

        function cho_alias_triggerStatusRemoveRecord(gridView){
            Ext.MessageBox.confirm('Confirm', 'Are you sure you want to remove this alias?',function(btn){
            if(btn==='yes'){
                var choAliasId = gridView.get("id");
                var url = "/prv/p/removeChoAlias.action";
                var param = {"choAliasId" : choAliasId};
                ajax.loadHtml2(url, param, onChoAliasMappingSubmitResult);
            }
            });
        }

</script>

<div class="sub-admin-tab-css">

    <div class="status-info">
        This tab contains a list of all the alias’ that identify the particular CHO when an Insurer is uploading a claim into the system.  The text expression is case sensitive, the Cho name field on the claim upload looks for a match against the alias’ as held within the below table.
    </div>

    <div class="grid-view-header">
        <table width="100%">
            <tr>
                <td style="width: 38%">
                    <p class="std-label" style="float: right">CHO Alias: </p>
                </td>    
                <td style="width: 24%">
                    <input name="choAliasName" id="choAliasName" type="text"/>
                </td>
                <td style="width: 38%">
                    <input style="float: left" type="button" onclick="cho_alias_triggerStatusAddRecord();" value="Add"/>
                </td>
            </tr>
        </table>
    </div>
    <div id="CDChoAliasMessageBox" class="chox-form-submit-result"></div>
    <div id="cho_alias_gridviewGrid"></div>

</div>