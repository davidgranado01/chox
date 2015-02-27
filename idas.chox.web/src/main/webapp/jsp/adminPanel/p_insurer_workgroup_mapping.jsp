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
        <s:if test="insurerPaymentsTeamEnabled">
                {name:'stpExcluded'},
                {name:'stpExcludedDesc'},
        </s:if>
                {name:'createdBy'},
                {name:'createdDate', type: 'date', dateFormat:'d/m/Y H:i'}
            ]
        });

        workgroup_gridviewData = new choxDataStore({
            url: '/prv/p/getInsurerWorkgroups.action',
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
                {header: "Active", width: 50, dataIndex: 'statusDesc', sortable: true, resizable: true, renderer:function(value,p,r){ return "<a href='#' class='high-light-item'>" + value + "</a>";}},
        <s:if test="insurerPaymentsTeamEnabled">
                {header: "STP Excluded", width: 50, dataIndex: 'stpExcludedDesc', sortable: true, resizable: true, renderer:function(value,p,r){ return "<a href='#' class='high-light-item'>" + value + "</a>";}},
        </s:if>
                {header: "Action", width: 60, dataIndex: 'Remove', sortable: true, resizable: true, renderer:function(value,p,r){ return "<a href='#' class='high-light-item'>Remove</a>";}},
                {header: "Created By", width: 80, dataIndex: 'createdBy', sortable: true, resizable: true},
                {header: "Created Date", width: 120, dataIndex: 'createdDate', sortable: true, resizable: true, renderer: Ext.util.Format.dateRenderer('d/m/Y H:i')}
            ],
            renderTo:'workgroup_gridviewGrid',
            height:335,
            width: 760
        });

        workgroup_loadGridViewList();
        
        var form = $("form#insurerWorkgroupMappingForm");

        form.validate({
            ignore: [],
            errorLabelContainer: "#CDInsurerWorkgroupMessageBox",
            rules: {
                workgroupName : {required:true},
                workgroupSite : {required:true},
                workgroupTeam : {required:true}
            },
            messages: {
                workgroupName : { required : "Please enter 'Workgroup Name'" },
                workgroupSite : { required : "Please enter 'Workgroup Team'" },
                workgroupTeam : { required : "Please enter 'Workgroup Site'" }
            }
        });
        
        ui.ajaxForm(form, workgroup_onSubmitResponseReceived, 'json');

    });

    function workgroup_loadGridViewList(){
        workgroup_gridviewData.load({ params : { insurerId:<s:property value="insurerId" /> } });
    }

    function workgroup_recordOnclick(grid, rowIndex, columnIndex, e){
        var gridView = workgroup_gridviewGrid.getStore().getAt(rowIndex);
        if(columnIndex===4){
            workgroup_triggerStatusUpdateRecord(gridView);
        }
        <s:if test="insurerPaymentsTeamEnabled">
        else if(columnIndex===5){
            workgroup_triggerStatusStpExcluded(gridView);
        } else if(columnIndex===6){
            workgroup_triggerStatusRemoveRecord(gridView);
        }
        </s:if>
        <s:else >
        else if(columnIndex===5){
            workgroup_triggerStatusRemoveRecord(gridView);
        }
        </s:else>
    }

    function workgroup_triggerStatusUpdateRecord(gridView){

        var workgroupId = gridView.get("id");
        var url = "/prv/p/triggerInsurerWorkgroupStatus.action";
        var param = {"insurerId":<s:property value="insurerId" />,"workgroupId":workgroupId};
        ajax.loadHtml2(url, param, workgroup_onSubmitResponseReceived);

    }

    function workgroup_triggerStatusStpExcluded(gridView){

        var workgroupId = gridView.get("id");
        var url = "/prv/p/triggerInsurerWorkgroupStpExcluded.action";
        var param = {"insurerId":<s:property value="insurerId" />,"workgroupId":workgroupId};
        ajax.loadHtml2(url, param, workgroup_onSubmitResponseReceived);

    }

    function workgroup_triggerStatusRemoveRecord(gridView){
        Ext.MessageBox.confirm('Confirm', 'Are you sure you want to remove this Workgroup?',function(btn){
        if(btn==='yes'){

            var workgroupId = gridView.get("id");

            var url = "/prv/p/removeInsurerWorkgroup.action";
            var param = {"insurerId":<s:property value="insurerId" />,"workgroupId":workgroupId};
            ajax.loadHtml2(url, param, workgroup_onSubmitResponseReceived);
        }
        });
    }

    function workgroup_onSubmitResponseReceived(responseText, statusText)  {

        var response = eval('(' + responseText.trim() + ')');
        var outputDiv = $('div#CDInsurerWorkgroupMessageBox');

        if(response)
        {
            if(response.isValid){
                clearFormValues();
                if(response.resultType && response.resultType === 'Message')
                {
                    Ext.MessageBox.show({
                        title: '',
                        msg: response.result,
                        width:300,
                        buttons: Ext.MessageBox.OK
                    });
                    workgroup_loadGridViewList();
                }
                else
                {
                    workgroup_loadGridViewList();
                }
            }
            else
            {
                $.each(response.errors, function() {
                    
                    Ext.Msg.show({
                        title: 'Error',
                        msg:this.toString(),
                        icon:Ext.Msg.ERROR,
                        buttons:Ext.Msg.OK,
                        width : 400
                    });
                });
                workgroup_loadGridViewList();
            }
        }
        else
        {
            triggerCss(outputDiv, true);
            outputDiv.append("Unknown Error Encountered, please try again.");
            outputDiv.addClass("submit-error");
        }

    }
    
    function clearFormValues() {
        $("#workgroupName").val('');
        $("#workgroupTeam").val('');
        $("#workgroupSite").val('');
        $("#stpExcluded").attr('checked', false);
    }
    
</script>
<div class="sub-admin-tab-css">

    <div class="status-info">
        The Workgroups that dictate where claims are routed to and therefore which users have access/visibility of the said claims is managed here.  Please note, it is not possible to remove a Workgroup where there is an open claim within the system that is assigned to the said Workgroup.
    </div>

    <div class="grid-view-header">
        <form id="insurerWorkgroupMappingForm" name="insurerWorkgroupMappingForm" class="XXentity-form" action="<%= request.getContextPath()%>/prv/p/addNewInsurerWorkgroup.action" method="post">
            <input id="insurerId" name="insurerId" type="hidden" value="<s:property value="insurerId"/>"/>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Workgroup name<span class="mandatory">*</span></label>
                <input name="workgroupName" id="workgroupName" type="text">
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Site<span class="mandatory">*</span></label>
                <input name="workgroupSite" id="workgroupSite" type="text">
            </div>
            <div class="chox-form-item">
                <label class="chox-form-std-label">Team<span class="mandatory">*</span></label>
                <input name="workgroupTeam" id="workgroupTeam" type="text">
            </div>
            <s:if test="insurerPaymentsTeamEnabled">
                <div class="chox-form-item">
                    <label class="chox-form-std-label">Exclude from STP</label>
                    <s:checkbox name="stpExcluded" id="stpExcluded" value="stpExcluded" />
                </div>
            </s:if>
            <div class="chox-form-button">
                <input type="submit" value="Add"/>
            </div>
        </form>
    </div>
    <div id="CDInsurerWorkgroupMessageBox" class="chox-form-submit-result"></div>
    <div id="workgroup_gridviewGrid"></div>
</div>