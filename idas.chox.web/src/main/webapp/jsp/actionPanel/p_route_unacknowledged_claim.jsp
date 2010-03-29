<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<script type="text/javascript">
    Ext.onReady(function(){
//        Ext.BLANK_IMAGE_URL = 'images/s.gif';
//        var insurerId = "<s:property value="AuthenticatedUser.Insurer.id"/>";

        var workgroupJsonReader = new Ext.data.JsonReader({
                                totalProperty: 'totalCount',
                                root: 'results',
                                fields:
                                [
                                    {name:'text'},
                                    {name:'value'}
                                ]
        });

        var workgroupStore = new Ext.data.Store({
                                proxy : new Ext.data.HttpProxy
                                    ({url : "<%= request.getContextPath()%>/prv/p/WorkgroupDropDownActionByInsurer.action", method:'GET'}),
                                reader : workgroupJsonReader
        });

        var workgroupCombo = new Ext.form.ComboBox({
                                store: workgroupStore,
                                renderTo: 'workgroupSelectionHolder',
                                valueField: 'text',
                                id: 'workgroupComboId',
                                hiddenName: 'workgroupId',
                                displayField:'value',
                                typeAhead: true,
                                autoWidth: true,
                                mode: 'local',
                                triggerAction: 'all',
                                emptyText: '--- Please Select ---',
//                                selectOnFocus: true,
//                                forceSelection: true,
//                                allowBlank: false
                                listeners: {
                                            blur: function () {
                                                if(this.getRawValue() == "") {
                                                    this.clearValue(); this.reset();
                                                }
                                            }
                                           }
         });

         $.validator.addMethod("workgroupSelection",
                            function(value) {
                                if(value === "") {
                                    return false;
                                }
                                return true;
                            }, "You must select a 'Workgroup'"
         );

         workgroupStore.load();

/****
        // Create the search and reset buttons
        new Ext.Button({
                    renderTo: 'submitButton',
                    text: 'Assign Workgroup',
                    handler: function(button, event) {
                        Ext.getDom('routeUnacknowledgedUnroutedClaim').elements['submitHTML'].click();
                             }
        });
****/
  });


    $(function(){

        var form = $("form#routeUnacknowledgedUnroutedClaim");
        form.validate(
        {
            errorLabelContainer: "#RouteUnacknowledgedUnroutedClaimMessageBox",
            rules: {
                workgroupId: {workgroupSelection: document.getElementById('workgroupComboId')}
//                workgroupId:{min:1}
            },
            messages: {
                workgroupId:{workgroupSelection:"You must select a 'Workgroup'."}
//                workgroupId:{min:"You must supply a value for 'Workgroup'"}
            }
        });
        
    });


</script>
<div class="chox-claim-header x-panel-bwrap chox-form-container">
    <form id="routeUnacknowledgedUnroutedClaim" name="routeUnacknowledgedUnroutedClaim" action="<%=request.getContextPath()%>/prv/processClaim.action" method="POST">

        <fieldset class="x-fieldset"><legend>Claim Routing - Action Required</legend>
            <div>
                <div class="status-info">
                    Please select the 'Workgroup' in order to route the claim to the relevant handling team.
                </div>
                <s:hidden name="id" />
                <s:hidden name="name" value="assignWorkgroup" />
                <!--s:hidden name="workgroupId" value="-1"/-->
                <div class="status-control-set">
                    <div class="status-info-submit">
                                <label style="position: relative; left: -850px; top: 0px;">Workgroup</label>
                                <div id="workgroupSelectionHolder" style="position: relative; left: 90px; top: -25px;"></div>
                                        <!--s:select name="workgroupId" id="workgroupId"
                                                  list="workgroups" headerKey="-1"
                                                  listKey="id" listValue="name"
                                                  headerValue=" Please Select "-->
                              <!--div id="submitButton" style="position: relative; left: 300px; top: -45px;"/-->
                              <input type="submit" value="Assign Workgroup" style="position: relative; left: -500px; top: -45px;"/>
                    </div>
                    <div id="RouteUnacknowledgedUnroutedClaimMessageBox" class="action-error-msg"style="position: relative; top: -40px;">&nbsp</div>
                </div>
            </div>
        </fieldset>
    </form>
</div>