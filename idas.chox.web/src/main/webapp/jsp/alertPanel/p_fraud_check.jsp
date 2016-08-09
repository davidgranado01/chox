<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
<s:if test="fraudIndicatorsAvailable">
    var fraudIndicatorsJsonReader;
    var fraudIndicatorsDataStore;
    var fraudIndicatorsGrid;
</s:if>

    Ext.onReady(function(){
//        var form = $("form#formRunFraudCheckAction");
//        choxJqueryHttpSubmit(form, function(){});
<s:if test="fraudIndicatorsAvailable">
       fraudIndicatorsJsonReader = new Ext.data.JsonReader({
            totalProperty: 'totalCount', root: 'results', fields:[
                {name:'id'},
                {name:'scoreMessageHeading'},
                {name:'scoreMessageDetail'}]
        });

        fraudIndicatorsDataStore = new choxDataStore({
            url: '/prv/p/getFraudIndicators.action',
            reader:fraudIndicatorsJsonReader
        });

        fraudIndicatorsDataStore.setDefaultSort('id', 'desc');

        fraudIndicatorsGrid = new Ext.grid.GridPanel({
            listeners:  {cellclick:scoreMessageOnClick},
            loadMask:true,
            store: fraudIndicatorsDataStore,
            renderTo:'fraudIndicatorsGridId',
            enableHdMenu:false,
            layout:'fit',
            columns: [
                {header: "Message Header", width: 345, dataIndex: 'scoreMessageHeading', sortable: false, resizable: true},
                {header: "Message Detail", width: 616, dataIndex: 'scoreMessageDetail', sortable: false, resizable: true}
            ],
            width:970,
            height:100
        });

        loadFraudIndicators();
</s:if>

    });

<s:if test="fraudIndicatorsAvailable">
    function loadFraudIndicators(){
        fraudIndicatorsDataStore.load({params:{claimId : <s:property value="id" />}});
    }
</s:if>

    function scoreMessageOnClick(grid, rowIndex, columnIndex, e){
        var message = fraudIndicatorsGrid.getStore().getAt(rowIndex);
        var title="Fraud Score Message";
        var msg = "<b>Heading</b>: " + message.get("scoreMessageHeading");
        msg += "<br/><b>Detail</b>: " + message.get("scoreMessageDetail");

        propmtMsg(title, msg);
        
    }

function doFraudCheckFormSubmit(action){
        actionPanel.registerAction(action);
        var form = $("form#formRunFraudCheckAction");
        var input = $("<input>").attr("type", "hidden").attr("name", "name").val(action);
        form.append($(input));
        
        var warningMessage;
        if (action ==='referFraudCheck'){
            warningMessage = 'Do you want to refer this claim to Keoghs?';
        }else if (action ==='runFraudCheck'){
            warningMessage = 'Re-running the Fraud Check will send the claim data to Keoghs ADA Fraud Tool and return a result to CHOX.\n It will take a short while to return a result and will require a browser refresh to be visible.\n Continue?';
        }else {
            Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
            choxJqueryHttpSubmit(form);
            return false;
        }
        Ext.MessageBox.confirm('Confirm', warningMessage,function(btn){
            if(btn==='yes'){
                Ext.get('claimDetailScreenDiv').mask("Reloading Claim...");
                choxJqueryHttpSubmit($(form));
                return false;
            }
        });
        
        return false;
    }

</script>

<div class="chox-claim-header x-panel-bwrap chox-form-container">

    <form action="<%=request.getContextPath()%>/prv/processClaim.action" method="post" id="formRunFraudCheckAction" name="formRunFraudCheckAction">
        <s:hidden id="claimId" name="id" />

        <fieldset class="x-fieldset"><legend>ADA Fraud Check Report</legend>

<s:if test="fraudCheckAvailable">
            <div class="status-info">
                This claim has been put through the Keogh's ADA Fraud Check Tool and has returned a '<s:property value="claimFraudRagResult" />'
                status with a total score of <s:property value="claimFraudTotalScore" />.
<s:if test="fraudIndicatorsAvailable">
                <br/>Please see below for the key fraud indicators found.
</s:if>
            </div>
<s:if test="fraudIndicatorsAvailable">
            <div id="fraudIndicatorsGridId"></div>
</s:if>

</s:if>
<s:else>
<s:if test="fraudCheckError">
            <div class="status-info">
                This claim has been put through the Keogh's ADA Fraud Check Tool but an internal error occurred and no results are available.
            </div> 
</s:if>
<s:else>

            <div class="status-info">
                This claim has been submitted the Keogh's ADA Fraud Check Tool and a result is pending.
            </div> 
</s:else>
</s:else>
                    <div class="status-control-set">
                        <table class="status-table">
                            <col width="10%">
                            <col width="80%">
                            <col width="10%">
                            <tr>
                                <td/>
                                <td class="choice" nowrap>
                                    <input type="button" id="FCAcknowledgeButtonId" value="Acknowledgee Fraud Result" <s:if test="fraudCheckAcknowledged == true">disabled='true'</s:if> onclick="return doFraudCheckFormSubmit('acknowledgeFraudCheck');" />
                                    <input type="button" id="FCReferButtonId" value="Refer Claim To Keoghs" <s:if test="referredToKeoghs == true">disabled='true'</s:if> onclick="return doFraudCheckFormSubmit('referFraudCheck');"  />
                                    <input type="button" id="FCRerunButtonId" value="Re-Run Fraud Check" <s:if test="canRerunFraudCheck == false">disabled='true'</s:if> onclick="return doFraudCheckFormSubmit('runFraudCheck');" />
                                </td>
                                <td/>
                            </tr>
                        </table>
                    </div>
        </fieldset>
    </form>
</div>
